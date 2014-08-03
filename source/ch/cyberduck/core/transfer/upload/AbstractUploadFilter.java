package ch.cyberduck.core.transfer.upload;

/*
 * Copyright (c) 2002-2013 David Kocher. All rights reserved.
 * http://cyberduck.ch/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Bug fixes, suggestions and comments should be sent to feedback@cyberduck.ch
 */

import ch.cyberduck.core.*;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.NotfoundException;
import ch.cyberduck.core.features.AclPermission;
import ch.cyberduck.core.features.Attributes;
import ch.cyberduck.core.features.Find;
import ch.cyberduck.core.features.Move;
import ch.cyberduck.core.features.Timestamp;
import ch.cyberduck.core.features.UnixPermission;
import ch.cyberduck.core.features.Write;
import ch.cyberduck.core.transfer.TransferOptions;
import ch.cyberduck.core.transfer.TransferPathFilter;
import ch.cyberduck.core.transfer.TransferStatus;
import ch.cyberduck.core.transfer.symlink.SymlinkResolver;

import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @version $Id$
 */
public abstract class AbstractUploadFilter implements TransferPathFilter {
    private static final Logger log = Logger.getLogger(AbstractUploadFilter.class);

    private SymlinkResolver<Local> symlinkResolver;

    private Session<?> session;

    protected Map<Path, Path> temporary
            = new HashMap<Path, Path>();

    private UploadFilterOptions options;

    protected Find find;

    protected Attributes attribute;

    private MimeTypeService mapping
            = new MappingMimeTypeService();

    private Preferences preferences
            = Preferences.instance();

    public AbstractUploadFilter(final SymlinkResolver<Local> symlinkResolver, final Session<?> session,
                                final UploadFilterOptions options) {
        this.symlinkResolver = symlinkResolver;
        this.session = session;
        this.options = options.withTemporary(options.temporary && session.getFeature(Write.class).temporary());
        this.find = session.getFeature(Find.class);
        this.attribute = session.getFeature(Attributes.class);
    }

    public AbstractUploadFilter withCache(final Cache cache) {
        find.withCache(cache);
        attribute.withCache(cache);
        return this;
    }

    public AbstractUploadFilter withOptions(final UploadFilterOptions options) {
        this.options = options;
        return this;
    }

    @Override
    public boolean accept(final Path file, final Local local, final TransferStatus parent) throws BackgroundException {
        if(!local.exists()) {
            // Local file is no more here
            throw new NotfoundException(local.getAbsolute());
        }
        return true;
    }

    @Override
    public TransferStatus prepare(final Path file, final Local local, final TransferStatus parent) throws BackgroundException {
        final TransferStatus status = new TransferStatus();
        // Read remote attributes first
        if(parent.isExists()) {
            if(find.find(file)) {
                status.setExists(true);
                // Read remote attributes
                final PathAttributes attributes = attribute.find(file);
                status.setRemote(attributes);
            }
        }
        if(local.isFile()) {
            // Set content length from local file
            if(local.isSymbolicLink()) {
                if(!symlinkResolver.resolve(local)) {
                    // Will resolve the symbolic link when the file is requested.
                    final Local target = local.getSymlinkTarget();
                    status.setLength(target.attributes().getSize());
                }
                // No file size increase for symbolic link to be created on the server
            }
            else {
                // Read file size from filesystem
                status.setLength(local.attributes().getSize());
            }
            if(options.temporary) {
                final Path renamed = new Path(file.getParent(),
                        MessageFormat.format(preferences.getProperty("queue.upload.file.temporary.format"),
                                file.getName(), UUID.randomUUID().toString()), file.getType());
                status.rename(renamed);
                // File attributes should not change after calculate the hash code of the file reference
                temporary.put(file, renamed);
            }
            status.setMime(mapping.getMime(file.getName()));
        }
        if(this.options.permissions) {
            Permission permission = Permission.EMPTY;
            if(status.isExists()) {
                permission = status.getRemote().getPermission();
            }
            else {
                if(preferences.getBoolean("queue.upload.permissions.default")) {
                    if(local.isFile()) {
                        permission = new Permission(
                                preferences.getInteger("queue.upload.permissions.file.default"));
                    }
                    else {
                        permission = new Permission(
                                preferences.getInteger("queue.upload.permissions.folder.default"));
                    }
                }
                else {
                    // Read permissions from local file
                    permission = local.attributes().getPermission();
                }
            }
            // Setting target UNIX permissions in transfer status
            status.setPermission(permission);
        }
        if(this.options.acl) {
            Acl acl = Acl.EMPTY;
            if(status.isExists()) {
                final AclPermission feature = session.getFeature(AclPermission.class);
                if(feature != null) {
                    acl = feature.getPermission(file);
                }
            }
            else {
                final Permission permission;
                if(preferences.getBoolean("queue.upload.permissions.default")) {
                    if(local.isFile()) {
                        permission = new Permission(
                                preferences.getInteger("queue.upload.permissions.file.default"));
                    }
                    else {
                        permission = new Permission(
                                preferences.getInteger("queue.upload.permissions.folder.default"));
                    }
                }
                else {
                    // Read permissions from local file
                    permission = local.attributes().getPermission();
                }
                acl = new Acl();
                if(permission.getOther().implies(Permission.Action.read)) {
                    acl.addAll(new Acl.GroupUser(Acl.GroupUser.EVERYONE), new Acl.Role(Acl.Role.READ));
                }
                if(permission.getGroup().implies(Permission.Action.read)) {
                    acl.addAll(new Acl.GroupUser(Acl.GroupUser.AUTHENTICATED), new Acl.Role(Acl.Role.READ));
                }
                if(permission.getGroup().implies(Permission.Action.write)) {
                    acl.addAll(new Acl.GroupUser(Acl.GroupUser.AUTHENTICATED), new Acl.Role(Acl.Role.WRITE));
                }
            }
            // Setting target ACL in transfer status
            status.setAcl(acl);
        }
        if(options.timestamp) {
            // Read timestamps from local file
            status.setTimestamp(local.attributes().getModificationDate());
        }
        return status;
    }

    @Override
    public void apply(final Path file, final Local local, final TransferStatus status) throws BackgroundException {
        //
    }

    @Override
    public void complete(final Path file, final Local local,
                         final TransferOptions options, final TransferStatus status,
                         final ProgressListener listener) throws BackgroundException {
        if(log.isDebugEnabled()) {
            log.debug(String.format("Complete %s with status %s", file.getAbsolute(), status));
        }
        if(status.isComplete()) {
            if(local.isFile()) {
                if(this.options.temporary) {
                    final Move move = session.getFeature(Move.class);
                    move.move(temporary.get(file), file, status.isExists());
                    temporary.remove(file);
                }
            }
            if(!Permission.EMPTY.equals(status.getPermission())) {
                final UnixPermission feature = session.getFeature(UnixPermission.class);
                if(feature != null) {
                    try {
                        listener.message(MessageFormat.format(LocaleFactory.localizedString("Changing permission of {0} to {1}", "Status"),
                                file.getName(), status.getPermission()));
                        feature.setUnixPermission(file, status.getPermission());
                    }
                    catch(BackgroundException e) {
                        // Ignore
                        log.warn(e.getMessage());
                    }
                }
            }
            if(!Acl.EMPTY.equals(status.getAcl())) {
                final AclPermission feature = session.getFeature(AclPermission.class);
                if(feature != null) {
                    try {
                        listener.message(MessageFormat.format(LocaleFactory.localizedString("Changing permission of {0} to {1}", "Status"),
                                file.getName(), status.getAcl()));
                        feature.setPermission(file, status.getAcl());
                    }
                    catch(BackgroundException e) {
                        // Ignore
                        log.warn(e.getMessage());
                    }
                }
            }
            if(status.getTimestamp() != null) {
                final Timestamp feature = session.getFeature(Timestamp.class);
                if(feature != null) {
                    try {
                        listener.message(MessageFormat.format(LocaleFactory.localizedString("Changing timestamp of {0} to {1}", "Status"),
                                file.getName(), UserDateFormatterFactory.get().getShortFormat(status.getTimestamp())));
                        feature.setTimestamp(file, status.getTimestamp());
                    }
                    catch(BackgroundException e) {
                        // Ignore
                        log.warn(e.getMessage());
                    }
                }
            }
        }
    }
}