package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.MSG;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;

public class MSA extends HL7Component
{
  private ID acknowledgmentCode = null;
  private ST messageControlID = null;
  private ST textMessage = null;
  private NM expectedSequenceNumber = null;
  private ST delayedAcknowledgmentType = null;
  private CE errorCondition = null;

  public ID getAcknowledgmentCode() {
    return acknowledgmentCode;
  }

  public void setAcknowledgmentCode(ID acknowledgmentCode) {
    this.acknowledgmentCode = acknowledgmentCode;
  }

  public ST getMessageControlID() {
    return messageControlID;
  }

  public void setMessageControlID(ST messageControlID) {
    this.messageControlID = messageControlID;
  }

  public ST getTextMessage() {
    return textMessage;
  }

  public void setTextMessage(ST textMessage) {
    this.textMessage = textMessage;
  }

  public NM getExpectedSequenceNumber() {
    return expectedSequenceNumber;
  }

  public void setExpectedSequenceNumber(NM expectedSequenceNumber) {
    this.expectedSequenceNumber = expectedSequenceNumber;
  }

  public ST getDelayedAcknowledgmentType() {
    return delayedAcknowledgmentType;
  }

  public void setDelayedAcknowledgmentType(ST delayedAcknowledgmentType) {
    this.delayedAcknowledgmentType = delayedAcknowledgmentType;
  }

  public CE getErrorCondition() {
    return errorCondition;
  }

  public void setErrorCondition(CE errorCondition) {
    this.errorCondition = errorCondition;
  }

  @Override
  public HL7Component makeAnother() {
    return new MSA(this);
  }
  
  public MSA(MSA copy)
  {
    super(copy);
    init();
  }

  public MSA(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "MSA", "Message Acknowledement Segment", "Message Acknowledement Segment", 6, usageType, cardinality);
    init();
  }

  public void init() {
    setChild(1, acknowledgmentCode = new ID("Acknowledgment Code", UsageType.R, Cardinality.ONE_TIME_ONLY,
        ValueSet.HL70008));
    acknowledgmentCode.setLength(2, 2);
    setChild(2, messageControlID = new ST("Message Control ID", UsageType.R, Cardinality.ONE_TIME_ONLY, 199));
    setChild(3, textMessage = new ST("Text Message", UsageType.X));
    setChild(4, expectedSequenceNumber = new NM("Expected Sequence Number", UsageType.O));
    setChild(5, delayedAcknowledgmentType = new ST("Delayed Acknowledgment Type", UsageType.O));
    setChild(6, errorCondition = new CE("Error Condition", UsageType.X));
  }
}
