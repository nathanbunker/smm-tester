package org.immregistries.smm.tester.manager;

public class TestParticipant
{
  private String organizationName = "";
  private String folderName = "";
  private String queryType = "";
  private String recordRequirementsStatus = "";
  private String connecttoIISStatus = "";
  private String profileUsageId = "";
  private String transport = "";
  private String publicIdCode = "";
  
  public String getPublicIdCode() {
    return publicIdCode;
  }

  public void setPublicIdCode(String publicIdCode) {
    this.publicIdCode = publicIdCode;
  }

  public String getQueryType() {
    return queryType;
  }

  public void setQueryType(String queryType) {
    this.queryType = queryType;
  }

  public String getTransport() {
    return transport;
  }

  public void setTransport(String transport) {
    this.transport = transport;
  }


  public String getProfileUsageId() {
    return profileUsageId;
  }

  public void setProfileUsageId(String guideName) {
    this.profileUsageId = guideName;
  }

  public String getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  public String getFolderName() {
    return folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public String getRecordRequirementsStatus() {
    return recordRequirementsStatus;
  }

  public void setRecordRequirementsStatus(String recordRequirementsStatus) {
    this.recordRequirementsStatus = recordRequirementsStatus;
  }

  public String getConnecttoIISStatus() {
    return connecttoIISStatus;
  }

  public void setConnecttoIISStatus(String connecttoIISStatus) {
    this.connecttoIISStatus = connecttoIISStatus;
  }

}
