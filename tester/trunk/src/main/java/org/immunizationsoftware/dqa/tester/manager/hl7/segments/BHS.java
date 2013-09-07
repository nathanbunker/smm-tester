package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.HD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;

public class BHS extends HL7Component
{
  private ST batchFieldSeparator = null;
  private ST batchEncodingCharacters = null;
  private HD batchSendingApplication = null;
  private HD batchSendingFacility = null;
  private HD batchReceivingApplication = null;
  private HD batchReceivingFacility;
  private TS batchCreationDateTime = null;
  private ST batchSecurity = null;
  private ST batchNameIDType = null;
  private ST batchComment = null;
  private ST batchControlID = null;
  private ST referenceBatchControlID = null;

  public ST getBatchFieldSeparator() {
    return batchFieldSeparator;
  }

  public void setBatchFieldSeparator(ST batchFieldSeparator) {
    this.batchFieldSeparator = batchFieldSeparator;
  }

  public ST getBatchEncodingCharacters() {
    return batchEncodingCharacters;
  }

  public void setBatchEncodingCharacters(ST batchEncodingCharacters) {
    this.batchEncodingCharacters = batchEncodingCharacters;
  }

  public HD getBatchSendingApplication() {
    return batchSendingApplication;
  }

  public void setBatchSendingApplication(HD batchSendingApplication) {
    this.batchSendingApplication = batchSendingApplication;
  }

  public HD getBatchSendingFacility() {
    return batchSendingFacility;
  }

  public void setBatchSendingFacility(HD batchSendingFacility) {
    this.batchSendingFacility = batchSendingFacility;
  }

  public HD getBatchReceivingApplication() {
    return batchReceivingApplication;
  }

  public void setBatchReceivingApplication(HD batchReceivingApplication) {
    this.batchReceivingApplication = batchReceivingApplication;
  }

  public HD getBatchReceivingFacility() {
    return batchReceivingFacility;
  }

  public void setBatchReceivingFacility(HD batchReceivingFacility) {
    this.batchReceivingFacility = batchReceivingFacility;
  }

  public TS getBatchCreationDateTime() {
    return batchCreationDateTime;
  }

  public void setBatchCreationDateTime(TS batchCreationDateTime) {
    this.batchCreationDateTime = batchCreationDateTime;
  }

  public ST getBatchSecurity() {
    return batchSecurity;
  }

  public void setBatchSecurity(ST batchSecurity) {
    this.batchSecurity = batchSecurity;
  }

  public ST getBatchNameIDType() {
    return batchNameIDType;
  }

  public void setBatchNameIDType(ST batchNameIDType) {
    this.batchNameIDType = batchNameIDType;
  }

  public ST getBatchComment() {
    return batchComment;
  }

  public void setBatchComment(ST batchComment) {
    this.batchComment = batchComment;
  }

  public ST getBatchControlID() {
    return batchControlID;
  }

  public void setBatchControlID(ST batchControlID) {
    this.batchControlID = batchControlID;
  }

  public ST getReferenceBatchControlID() {
    return referenceBatchControlID;
  }

  public void setReferenceBatchControlID(ST referenceBatchControlID) {
    this.referenceBatchControlID = referenceBatchControlID;
  }

  @Override
  public HL7Component makeAnother() {
    return new BHS(this);
  }
  
  public BHS(BHS copy)
  {
    super(copy);
    init();
  }

  public BHS(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "BHS", "Batch Header Segment", 12, usageType, cardinality);
    init();
  }

  public void init() {
    setChild(1, batchFieldSeparator = new ST("Batch Field Separator", UsageType.R, Cardinality.ONE_TIME_ONLY, 1));
    setChild(2, batchEncodingCharacters = new ST("Batch Encoding Characters", UsageType.R, Cardinality.ONE_TIME_ONLY));
    batchEncodingCharacters.setLength(4, 4);
    setChild(3, batchSendingApplication = new HD("Batch Sending Application", UsageType.O));
    setChild(4, batchSendingFacility = new HD("Batch Sending Facility", UsageType.O));
    setChild(5, batchReceivingApplication = new HD("Batch Receiving Application", UsageType.O));
    setChild(6, batchReceivingFacility = new HD("Batch Receiving Facility", UsageType.O));
    setChild(7, batchCreationDateTime = new TS("Batch Creation Date/Time", UsageType.O));
    setChild(8, batchSecurity = new ST("Batch Security", UsageType.O));
    setChild(9, batchNameIDType = new ST("Batch Name/ID/Type", UsageType.O));
    setChild(10, batchComment = new ST("Batch Comment", UsageType.O));
    setChild(11, batchControlID = new ST("Batch Control ID", UsageType.O));
    setChild(12, referenceBatchControlID = new ST("Reference Batch Control ID", UsageType.O));
  }
}
