package org.immunizationsoftware.dqa.tester.manager;

import org.immunizationsoftware.dqa.tester.certify.CertifyRunner;

public class TestParticipant
{
  private String organizationName = "";
  private String folderName = "";
  private String queryType = "";
  private boolean redactListResponses = false;
  private String recordRequirementsStatus = "";
  private String connecttoIISStatus = "";
  private String guideName = "";
  private String transport = "";
  private String querySupport = "";
  private String publicIdCode = "";
  private String tchForecastSoftwareId = "";
  private int col = 0;
  private int row = 0;
  
  public String getTchForecastSoftwareId() {
    return tchForecastSoftwareId;
  }
  
  public void setTchForecastSoftwareId(String tchForecastSoftwareId) {
    this.tchForecastSoftwareId = tchForecastSoftwareId;
  }
  
  public String getPublicIdCode() {
    return publicIdCode;
  }

  public void setPublicIdCode(String publicIdCode) {
    this.publicIdCode = publicIdCode;
  }

  public boolean isRedactListResponses() {
    return redactListResponses;
  }

  public void setRedactListResponses(boolean redactListResponses) {
    this.redactListResponses = redactListResponses;
  }

  public String getQueryType() {
    return queryType;
  }

  public void setQueryType(String queryType) {
    this.queryType = queryType;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public String getTransport() {
    return transport;
  }

  public void setTransport(String transport) {
    this.transport = transport;
  }

  public String getQuerySupport() {
    return querySupport;
  }

  public void setQuerySupport(String querySupport) {
    this.querySupport = querySupport;
    if (querySupport.equals("QBP")) {
      this.queryType = CertifyRunner.QUERY_TYPE_QBP_Z34;
    } else if (querySupport.equals("QBP Z34")) {
      this.queryType = CertifyRunner.QUERY_TYPE_QBP_Z34;
    } else if (querySupport.equals("QBP Z44")) {
      this.queryType = CertifyRunner.QUERY_TYPE_QBP_Z44;
    } else if (querySupport.equals("VXQ")) {
      this.queryType = CertifyRunner.QUERY_TYPE_VXQ;
    } else if (querySupport.equals("QBP Z43 Z44") || querySupport.equals("QBP Z43-Z44")) {
      this.queryType = CertifyRunner.QUERY_TYPE_QBP_Z34_Z44;
    } else {
      this.queryType = CertifyRunner.QUERY_TYPE_NONE;
    }
  }

  public String getGuideName() {
    return guideName;
  }

  public void setGuideName(String guideName) {
    this.guideName = guideName;
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
