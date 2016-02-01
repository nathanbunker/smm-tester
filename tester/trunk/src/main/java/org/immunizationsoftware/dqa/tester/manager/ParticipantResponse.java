package org.immunizationsoftware.dqa.tester.manager;

import org.immunizationsoftware.dqa.tester.certify.CertifyRunner;

public class ParticipantResponse
{
  private String organizationName = "";
  private String folderName = "";
  private String map = "";
  private String platform = "";
  private String vendor = "";
  private String queryType = "";
  private boolean redactListResponses = false;
  private String internalComments = "";
  private String phaseIParticipation = "";
  private String phase1Status = "";
  private String phase1Comments = "";
  private String phaseIIParticipation = "";
  private String phaseIIStatus = "";
  private String phaseIIComments = "";
  private String iHS = "";
  private String recordRequirementsStatus = "";
  private String connecttoIISStatus = "";
  private String comments = "";
  private String guideName = "";
  private String transport = "";
  private String querySupport = "";
  private String nistStatus = "";
  private String accessPasscode = "";
  private String publicIdCode = "";
  private int col = 0;
  private int row = 0;
  
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

  public String getAccessPasscode() {
    return accessPasscode;
  }

  public void setAccessPasscode(String accessPasscode) {
    this.accessPasscode = accessPasscode;
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

  public String getNistStatus() {
    return nistStatus;
  }

  public void setNistStatus(String nistStatus) {
    this.nistStatus = nistStatus;
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

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getVendor() {
    return vendor;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public String getInternalComments() {
    return internalComments;
  }

  public void setInternalComments(String internalComments) {
    this.internalComments = internalComments;
  }

  public String getPhaseIParticipation() {
    return phaseIParticipation;
  }

  public void setPhaseIParticipation(String phaseIParticipation) {
    this.phaseIParticipation = phaseIParticipation;
  }

  public String getPhase1Status() {
    return phase1Status;
  }

  public void setPhase1Status(String phase1Status) {
    this.phase1Status = phase1Status;
  }

  public String getPhase1Comments() {
    return phase1Comments;
  }

  public void setPhase1Comments(String phase1Comments) {
    this.phase1Comments = phase1Comments;
  }

  public String getPhaseIIParticipation() {
    return phaseIIParticipation;
  }

  public void setPhaseIIParticipation(String phaseIIParticipation) {
    this.phaseIIParticipation = phaseIIParticipation;
  }

  public String getPhaseIIStatus() {
    return phaseIIStatus;
  }

  public void setPhaseIIStatus(String phaseIIStatus) {
    this.phaseIIStatus = phaseIIStatus;
  }

  public String getPhaseIIComments() {
    return phaseIIComments;
  }

  public void setPhaseIIComments(String phaseIIComments) {
    this.phaseIIComments = phaseIIComments;
  }

  public String getIHS() {
    return iHS;
  }

  public void setIHS(String iHS) {
    this.iHS = iHS;
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

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

}
