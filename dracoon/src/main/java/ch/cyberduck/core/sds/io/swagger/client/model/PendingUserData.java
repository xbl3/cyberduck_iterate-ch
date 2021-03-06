/*
 * DRACOON
 * REST Web Services for DRACOON<br>Version: 4.10.7-LTS  - built at: 2019-03-19 14:24:35<br><br><a title='Developer Information' href='https://developer.dracoon.com'>Developer Information</a>&emsp;&emsp;<a title='Get SDKs on GitHub' href='https://github.com/dracoon'>Get SDKs on GitHub</a>
 *
 * OpenAPI spec version: 4.10.7-LTS
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package ch.cyberduck.core.sds.io.swagger.client.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * PendingUserData
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-13T14:25:40.305+02:00")
public class PendingUserData {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("login")
  private String login = null;

  @JsonProperty("displayName")
  private String displayName = null;

  @JsonProperty("email")
  private String email = null;

  public PendingUserData id(Long id) {
    this.id = id;
    return this;
  }

   /**
    * Unique identifier for the user
   * @return id
  **/
   @ApiModelProperty(required = true, value = "Unique identifier for the user")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PendingUserData login(String login) {
    this.login = login;
    return this;
  }

   /**
    * User login name
   * @return login
  **/
   @ApiModelProperty(required = true, value = "User login name")
  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public PendingUserData displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

   /**
    * Display name
   * @return displayName
  **/
   @ApiModelProperty(required = true, value = "Display name")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public PendingUserData email(String email) {
    this.email = email;
    return this;
  }

   /**
    * Email (not used)
   * @return email
  **/
   @ApiModelProperty(example = "john.doe@email.com", required = true, value = "Email (not used)")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PendingUserData pendingUserData = (PendingUserData) o;
    return Objects.equals(this.id, pendingUserData.id) &&
        Objects.equals(this.login, pendingUserData.login) &&
        Objects.equals(this.displayName, pendingUserData.displayName) &&
        Objects.equals(this.email, pendingUserData.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, login, displayName, email);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendingUserData {\n");

      sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    login: ").append(toIndentedString(login)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

