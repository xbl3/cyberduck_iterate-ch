/*
 * Storegate.Web
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v4
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package ch.cyberduck.core.storegate.io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 */
@ApiModel(description = "")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-12-02T20:20:31.369+01:00")
public class PartnerRetailer {
  @JsonProperty("partnerId")
  private String partnerId = null;

  @JsonProperty("retailerId")
  private String retailerId = null;

  public PartnerRetailer partnerId(String partnerId) {
    this.partnerId = partnerId;
    return this;
  }

   /**
   * 
   * @return partnerId
  **/
  @ApiModelProperty(value = "")
  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public PartnerRetailer retailerId(String retailerId) {
    this.retailerId = retailerId;
    return this;
  }

   /**
   * 
   * @return retailerId
  **/
  @ApiModelProperty(value = "")
  public String getRetailerId() {
    return retailerId;
  }

  public void setRetailerId(String retailerId) {
    this.retailerId = retailerId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartnerRetailer partnerRetailer = (PartnerRetailer) o;
    return Objects.equals(this.partnerId, partnerRetailer.partnerId) &&
        Objects.equals(this.retailerId, partnerRetailer.retailerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partnerId, retailerId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartnerRetailer {\n");
    
    sb.append("    partnerId: ").append(toIndentedString(partnerId)).append("\n");
    sb.append("    retailerId: ").append(toIndentedString(retailerId)).append("\n");
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

