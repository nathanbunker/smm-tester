package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ConformanceStatement;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.DatePrecision;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.EI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.HD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.MSG;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.PT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.VID;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValuedAs;

public class MSH extends HL7Component
{

  private ST fieldSeparator = null;
  private ST encodingCharacters = null;
  private HD sendingApplication = null;
  private HD sendingFacility = null;
  private HD receivingApplication = null;
  private HD receivingFacility = null;
  private TS dateTimeOfMessage = null;
  private ST security = null;
  private MSG messageType = null;
  private ST messageControlID = null;
  private PT processingID = null;
  private VID versionID = null;
  private NM sequenceNumber = null;
  private ST continuationPointer = null;
  private ID acceptAcknowledgementType = null;
  private ID applicationAcknowledgmentType = null;
  private ID countryCode = null;
  private ID characterSet = null;
  private CE principalLanguageOfMessage = null;
  private ID alternateCharacterSetHandlingScheme = null;
  private EI messageProfileIdentifier = null;

  public ST getFieldSeparator() {
    return fieldSeparator;
  }

  public void setFieldSeparator(ST fieldSeparator) {
    this.fieldSeparator = fieldSeparator;
  }

  public ST getEncodingCharacters() {
    return encodingCharacters;
  }

  public void setEncodingCharacters(ST encodingCharacters) {
    this.encodingCharacters = encodingCharacters;
  }

  public HD getSendingApplication() {
    return sendingApplication;
  }

  public void setSendingApplication(HD sendingApplication) {
    this.sendingApplication = sendingApplication;
  }

  public HD getSendingFacility() {
    return sendingFacility;
  }

  public void setSendingFacility(HD sendingFacility) {
    this.sendingFacility = sendingFacility;
  }

  public HD getReceivingApplication() {
    return receivingApplication;
  }

  public void setReceivingApplication(HD receivingApplication) {
    this.receivingApplication = receivingApplication;
  }

  public HD getReceivingFacility() {
    return receivingFacility;
  }

  public void setReceivingFacility(HD receivingFacility) {
    this.receivingFacility = receivingFacility;
  }

  public TS getDateTimeOfMessage() {
    return dateTimeOfMessage;
  }

  public void setDateTimeOfMessage(TS dateTimeOfMessage) {
    this.dateTimeOfMessage = dateTimeOfMessage;
  }

  public ST getSecurity() {
    return security;
  }

  public void setSecurity(ST security) {
    this.security = security;
  }

  public MSG getMessageType() {
    return messageType;
  }

  public void setMessageType(MSG messageType) {
    this.messageType = messageType;
  }

  public ST getMessageControlID() {
    return messageControlID;
  }

  public void setMessageControlID(ST messageControlID) {
    this.messageControlID = messageControlID;
  }

  public PT getProcessingID() {
    return processingID;
  }

  public void setProcessingID(PT processingID) {
    this.processingID = processingID;
  }

  public VID getVersionID() {
    return versionID;
  }

  public void setVersionID(VID versionID) {
    this.versionID = versionID;
  }

  public NM getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(NM sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public ST getContinuationPointer() {
    return continuationPointer;
  }

  public void setContinuationPointer(ST continuationPointer) {
    this.continuationPointer = continuationPointer;
  }

  public ID getAcceptAcknowledgementType() {
    return acceptAcknowledgementType;
  }

  public void setAcceptAcknowledgementType(ID acceptAcknowledgementType) {
    this.acceptAcknowledgementType = acceptAcknowledgementType;
  }

  public ID getApplicationAcknowledgmentType() {
    return applicationAcknowledgmentType;
  }

  public void setApplicationAcknowledgmentType(ID applicationAcknowledgmentType) {
    this.applicationAcknowledgmentType = applicationAcknowledgmentType;
  }

  public ID getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(ID countryCode) {
    this.countryCode = countryCode;
  }

  public ID getCharacterSet() {
    return characterSet;
  }

  public void setCharacterSet(ID characterSet) {
    this.characterSet = characterSet;
  }

  public CE getPrincipalLanguageOfMessage() {
    return principalLanguageOfMessage;
  }

  public void setPrincipalLanguageOfMessage(CE principalLanguageOfMessage) {
    this.principalLanguageOfMessage = principalLanguageOfMessage;
  }

  public ID getAlternateCharacterSetHandlingScheme() {
    return alternateCharacterSetHandlingScheme;
  }

  public void setAlternateCharacterSetHandlingScheme(ID alternateCharacterSetHandlingScheme) {
    this.alternateCharacterSetHandlingScheme = alternateCharacterSetHandlingScheme;
  }

  public EI getMessageProfileIdentifier() {
    return messageProfileIdentifier;
  }

  public void setMessageProfileIdentifier(EI messageProfileIdentifier) {
    this.messageProfileIdentifier = messageProfileIdentifier;
  }
  
  @Override
  public HL7Component makeAnother() {
    return new MSH(this);
  }
  
  public MSH(MSH copy)
  {
    super(copy);
    init();
  }

  public MSH(UsageType usageType, Cardinality cardinality) {
    super("MSH", "Message Header", "Message Header", 21, usageType, cardinality);
    init();
  }

  public void init() {
    setChild(1, fieldSeparator = new ST("Field Separator", UsageType.R, Cardinality.ONE_TIME_ONLY, 1));
    fieldSeparator.addConformanceStatement(new ExactValue(
        "IZ-12: The MSH.1 (Field Separator) field SHALL be valued \"|\"", "|"));
    setChild(2, encodingCharacters = new ST("Encoding Characters", UsageType.R, Cardinality.ONE_TIME_ONLY));
    encodingCharacters.setLength(4, 4);
    encodingCharacters.addConformanceStatement(new ExactValue(
        "IZ-13: The MSH.2 (Encoding Characters) field SHALL be valued \"^~\\&\"", "^~\\&"));
    setChild(3, sendingApplication = new HD("Sending Application", UsageType.RE, Cardinality.ZERO_OR_ONE,
        ValueSet.HL70361));
    setChild(4, sendingFacility = new HD("Sending Facility", UsageType.RE, Cardinality.ZERO_OR_ONE, ValueSet.HL70362));
    setChild(5, receivingApplication = new HD("Receiving Application", UsageType.RE, Cardinality.ZERO_OR_ONE,
        ValueSet.HL70361));
    setChild(6, receivingFacility = new HD("Receiving Facility", UsageType.RE, Cardinality.ZERO_OR_ONE,
        ValueSet.HL70362));
    setChild(7, dateTimeOfMessage = new TS("Date/Time of Message", UsageType.R, Cardinality.ONE_TIME_ONLY));
    dateTimeOfMessage.getTime()
        .addConformanceStatement(new DatePrecision(
            "IZ-14: The MSH-7 (Date/time of Message) SHALL have a degree of precision that must be at least to the minute. (Format YYYYMMDDHHMM) ",
            DatePrecision.PRECISION_MINUTE));
    setChild(8, security = new ST("Security", UsageType.O));
    setChild(9, messageType = new MSG("Message Type", UsageType.R, Cardinality.ONE_TIME_ONLY));

    // VXU conformance
    {
      ConformanceStatement conformance1 = new ExactValue(
          "IZ-17: MSH-9 (Message Type) SHALL contain the constant value \"VXU^V0^VXU_V04\"", "VXU");
      ConformanceStatement conformance2 = new ExactValue(
          "IZ-17: MSH-9 (Message Type) SHALL contain the constant value \"VXU^V0^VXU_V04\"", "V04");
      ConformanceStatement conformance3 = new ExactValue(
          "IZ-17: MSH-9 (Message Type) SHALL contain the constant value \"VXU^V0^VXU_V04\"", "VXU_V04");
      IsValuedAs isValuedAs = new IsValuedAs(messageType.getMessageCode(), "VXU");
      conformance1.setConditionalPredicate(isValuedAs);
      conformance2.setConditionalPredicate(isValuedAs);
      conformance3.setConditionalPredicate(isValuedAs);
      messageType.getMessageCode().addConformanceStatement(conformance1);
      messageType.getTriggerEvent().addConformanceStatement(conformance2);
      messageType.getMessageStructure().addConformanceStatement(conformance3);
    }

    // QPB conformance
    {
      ConformanceStatement conformance1 = new ExactValue(
          "IZ-18: MSH-9 (Message Type) SHALL contain the constant value \"QBP^Q11^QBP_Q11\"", "QBP");
      ConformanceStatement conformance2 = new ExactValue(
          "IZ-18: MSH-9 (Message Type) SHALL contain the constant value \"QBP^Q11^QBP_Q11\"", "Q11");
      ConformanceStatement conformance3 = new ExactValue(
          "IZ-18: MSH-9 (Message Type) SHALL contain the constant value \"QBP^Q11^QBP_Q11\"", "QBP_Q11");
      IsValuedAs isValuedAs = new IsValuedAs(messageType.getMessageCode(), "QBP");
      conformance1.setConditionalPredicate(isValuedAs);
      conformance2.setConditionalPredicate(isValuedAs);
      conformance3.setConditionalPredicate(isValuedAs);
      messageType.getMessageCode().addConformanceStatement(conformance1);
      messageType.getTriggerEvent().addConformanceStatement(conformance2);
      messageType.getMessageStructure().addConformanceStatement(conformance3);
    }

    // RSP conformance
    {
      ConformanceStatement conformance1 = new ExactValue(
          "IZ-19: MSH-9 (Message Type) SHALL contain the constant value \"RSP^K11^RSP_K11\"", "RSP");
      ConformanceStatement conformance2 = new ExactValue(
          "IZ-19: MSH-9 (Message Type) SHALL contain the constant value \"RSP^K11^RSP_K11\"", "K11");
      ConformanceStatement conformance3 = new ExactValue(
          "IZ-19: MSH-9 (Message Type) SHALL contain the constant value \"RSP^K11^RSP_K11\"", "RSP_K11");
      IsValuedAs isValuedAs = new IsValuedAs(messageType.getMessageCode(), "RSP");
      conformance1.setConditionalPredicate(isValuedAs);
      conformance2.setConditionalPredicate(isValuedAs);
      conformance3.setConditionalPredicate(isValuedAs);
      messageType.getMessageCode().addConformanceStatement(conformance1);
      messageType.getTriggerEvent().addConformanceStatement(conformance2);
      messageType.getMessageStructure().addConformanceStatement(conformance3);
    }

    setChild(10, messageControlID = new ST("Message Control ID", UsageType.R, Cardinality.ONE_TIME_ONLY, 199));
    setChild(11, processingID = new PT("Processing ID", UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(12, versionID = new VID("Version ID", UsageType.R, Cardinality.ONE_TIME_ONLY));
    versionID.addConformanceStatement(new ExactValue("IZ-15: The MSH-12 (Version ID) SHALL be valued \"2.5.1\"",
        "2.5.1"));
    setChild(13, sequenceNumber = new NM("Sequence Number", UsageType.O));
    setChild(14, continuationPointer = new ST("Continuation Pointer", UsageType.O, Cardinality.ZERO_OR_ONE));
    setChild(15, acceptAcknowledgementType = new ID("Accept Acknowledgement Type", UsageType.RE,
        Cardinality.ZERO_OR_ONE, ValueSet.HL70155));
    setChild(16, applicationAcknowledgmentType = new ID("Application Acknowledgment Type", UsageType.RE,
        Cardinality.ZERO_OR_ONE, ValueSet.HL70155));
    applicationAcknowledgmentType
        .addConformanceStatement(new ExactValue(
            "IZ-16: The value of MSH-16 (Application Acknowledgment Type) SHALL be one of the following: AL-always, NE-Never, ER-ERror/reject only, SU successful completion only",
            new String[] { "AL", "NE", "ER", "SU" }));
    setChild(17, countryCode = new ID("Country Code", UsageType.O));
    setChild(18, characterSet = new ID("Character Set", UsageType.O));
    setChild(19, principalLanguageOfMessage = new CE("Principal Language of Message", UsageType.O));
    setChild(20, alternateCharacterSetHandlingScheme = new ID("Alternate Character Set Handling Scheme", UsageType.O));
    setChild(21, messageProfileIdentifier = new EI("Message Profile Identifier", UsageType.C));
    messageProfileIdentifier.setConditionalPredicate(new IsValuedAs(messageType.getMessageCode(), new String[] { "QBP",
        "RSP" }, UsageType.R, UsageType.O));
  }
}
