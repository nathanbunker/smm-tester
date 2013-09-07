package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.HD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;

public class FHS extends HL7Component
{
  private ST fileFieldSeparator = null;
  private ST fileEncodingCharacters = null;
  private HD fileSendingApplication = null;
  private HD fileSendingFacility = null;
  private HD fileReceivingApplication = null;
  private HD fileReceivingFacility;
  private TS fileCreationDateTime = null;
  private ST fileSecurity = null;
  private ST fileNameID = null;
  private ST fileHeaderComment = null;
  private ST fileControlID = null;
  private ST referenceFileControlID = null;

  public ST getFileFieldSeparator() {
    return fileFieldSeparator;
  }

  public void setFileFieldSeparator(ST fileFieldSeparator) {
    this.fileFieldSeparator = fileFieldSeparator;
  }

  public ST getFileEncodingCharacters() {
    return fileEncodingCharacters;
  }

  public void setFileEncodingCharacters(ST fileEncodingCharacters) {
    this.fileEncodingCharacters = fileEncodingCharacters;
  }

  public HD getFileSendingApplication() {
    return fileSendingApplication;
  }

  public void setFileSendingApplication(HD fileSendingApplication) {
    this.fileSendingApplication = fileSendingApplication;
  }

  public HD getFileSendingFacility() {
    return fileSendingFacility;
  }

  public void setFileSendingFacility(HD fileSendingFacility) {
    this.fileSendingFacility = fileSendingFacility;
  }

  public HD getFileReceivingApplication() {
    return fileReceivingApplication;
  }

  public void setFileReceivingApplication(HD fileReceivingApplication) {
    this.fileReceivingApplication = fileReceivingApplication;
  }

  public HD getFileReceivingFacility() {
    return fileReceivingFacility;
  }

  public void setFileReceivingFacility(HD fileReceivingFacility) {
    this.fileReceivingFacility = fileReceivingFacility;
  }

  public TS getFileCreationDateTime() {
    return fileCreationDateTime;
  }

  public void setFileCreationDateTime(TS fileCreationDateTime) {
    this.fileCreationDateTime = fileCreationDateTime;
  }

  public ST getFileSecurity() {
    return fileSecurity;
  }

  public void setFileSecurity(ST fileSecurity) {
    this.fileSecurity = fileSecurity;
  }

  public ST getFileNameID() {
    return fileNameID;
  }

  public void setFileNameID(ST fileNameIDType) {
    this.fileNameID = fileNameIDType;
  }

  public ST getFileHeaderComment() {
    return fileHeaderComment;
  }

  public void setFileHeaderComment(ST fileComment) {
    this.fileHeaderComment = fileComment;
  }

  public ST getFileControlID() {
    return fileControlID;
  }

  public void setFileControlID(ST fileControlID) {
    this.fileControlID = fileControlID;
  }

  public ST getReferenceFileControlID() {
    return referenceFileControlID;
  }

  public void setReferenceFileControlID(ST referenceFileControlID) {
    this.referenceFileControlID = referenceFileControlID;
  }

  @Override
  public HL7Component makeAnother() {
    return new FHS(this);
  }
  
  public FHS(FHS copy)
  {
    super(copy);
    init();
  }

  public FHS(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "FHS", "File Header Segment", "File Header Segment", 12, usageType, cardinality);

    init();
  }

  public void init() {
    setChild(1, fileFieldSeparator = new ST("File Field Separator", UsageType.R, Cardinality.ONE_TIME_ONLY, 1));
    setChild(2, fileEncodingCharacters = new ST("File Encoding Characters", UsageType.R, Cardinality.ONE_TIME_ONLY));
    fileEncodingCharacters.setLength(4, 4);
    setChild(3, fileSendingApplication = new HD("File Sending Application", UsageType.O));
    setChild(4, fileSendingFacility = new HD("File Sending Facility", UsageType.O));
    setChild(5, fileReceivingApplication = new HD("File Receiving Application", UsageType.O));
    setChild(6, fileReceivingFacility = new HD("File Receiving Facility", UsageType.O));
    setChild(7, fileCreationDateTime = new TS("File Creation Date/Time", UsageType.O));
    setChild(8, fileSecurity = new ST("File Security", UsageType.O));
    setChild(9, fileNameID = new ST("File Name/ID", UsageType.O));
    setChild(10, fileHeaderComment = new ST("File Header Comment", UsageType.O));
    setChild(11, fileControlID = new ST("File Control ID", UsageType.O));
    setChild(12, referenceFileControlID = new ST("Reference File Control ID", UsageType.O));
  }
}
