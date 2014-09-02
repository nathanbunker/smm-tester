package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.HD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XAD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XPN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XTN;

public class QPD extends HL7Component
{

  private CE messageQueryName = null;
  private ST queryTag = null;
  private CX patientList = null;
  private XPN patientName = null;
  private XPN patientMotherMaidenName = null;
  private TS patientDateOfBirth = null;
  private IS patientSex = null;
  private XAD patientAddress = null;
  private XTN patientHomePhone = null;
  private ID patientMultipleBirthIndicator = null;
  private NM patientBirthOrder = null;
  private TS clientLastUpdatedDate = null;
  private HD clientLastUpdateFacility = null;

  public CE getMessageQueryName() {
    return messageQueryName;
  }

  public void setMessageQueryName(CE messageQueryName) {
    this.messageQueryName = messageQueryName;
  }

  public ST getQueryTag() {
    return queryTag;
  }

  public void setQueryTag(ST queryTag) {
    this.queryTag = queryTag;
  }

  public CX getPatientList() {
    return patientList;
  }

  public void setPatientList(CX patientList) {
    this.patientList = patientList;
  }

  public XPN getPatientName() {
    return patientName;
  }

  public void setPatientName(XPN patientName) {
    this.patientName = patientName;
  }

  public XPN getPatientMotherMaidenName() {
    return patientMotherMaidenName;
  }

  public void setPatientMotherMaidenName(XPN patientMotherMaidenName) {
    this.patientMotherMaidenName = patientMotherMaidenName;
  }

  public TS getPatientDateOfBirth() {
    return patientDateOfBirth;
  }

  public void setPatientDateOfBirth(TS patientDateOfBirth) {
    this.patientDateOfBirth = patientDateOfBirth;
  }

  public IS getPatientSex() {
    return patientSex;
  }

  public void setPatientSex(IS patientSex) {
    this.patientSex = patientSex;
  }

  public XAD getPatientAddress() {
    return patientAddress;
  }

  public void setPatientAddress(XAD patientAddress) {
    this.patientAddress = patientAddress;
  }

  public XTN getPatientHomePhone() {
    return patientHomePhone;
  }

  public void setPatientHomePhone(XTN patientHomePhone) {
    this.patientHomePhone = patientHomePhone;
  }

  public ID getPatientMultipleBirthIndicator() {
    return patientMultipleBirthIndicator;
  }

  public void setPatientMultipleBirthIndicator(ID patientMultipleBirthIndicator) {
    this.patientMultipleBirthIndicator = patientMultipleBirthIndicator;
  }

  public NM getPatientBirthOrder() {
    return patientBirthOrder;
  }

  public void setPatientBirthOrder(NM patientBirthOrder) {
    this.patientBirthOrder = patientBirthOrder;
  }

  public TS getClientLastUpdatedDate() {
    return clientLastUpdatedDate;
  }

  public void setClientLastUpdatedDate(TS clientLastUpdatedDate) {
    this.clientLastUpdatedDate = clientLastUpdatedDate;
  }

  public HD getClientLastUpdateFacility() {
    return clientLastUpdateFacility;
  }

  public void setClientLastUpdateFacility(HD clientLastUpdateFacility) {
    this.clientLastUpdateFacility = clientLastUpdateFacility;
  }

  @Override
  public HL7Component makeAnother() {
    return new QPD(this);
  }

  public QPD(QPD copy) {
    super(copy);
    init();
  }

  public QPD(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "QPD", "Query Parameter Definition Segment", "Query Parameter Definition Segment", 13, usageType, cardinality);
    init();
  }

  @Override
  public void init() {
    setChild(1, messageQueryName = new CE("Message Query Name", UsageType.O, Cardinality.ONE_TIME_ONLY,
        ValueSet.HL70471));
    messageQueryName.getIdentifier().addConformanceStatement(new ExactValue("The only value supported", "Z34"));
    setChild(2, queryTag = new ST("Query Tag", UsageType.R));
    queryTag.setLength(0, 32);
    setChild(3, patientList = new CX("Patient List", UsageType.RE, Cardinality.ZERO_OR_MORE));
    setChild(4, patientName = new XPN("Patient Name", UsageType.RE));
    setChild(5, patientMotherMaidenName = new XPN("Patient Mother Maiden Name", UsageType.RE));
    setChild(6, patientDateOfBirth = new TS("Patient Date of Birth", UsageType.RE));
    setChild(7, patientSex = new IS("Patient Sex", UsageType.RE));
    setChild(8, patientAddress = new XAD("Patient Address", UsageType.RE));
    setChild(9, patientHomePhone = new XTN("Patient Home Phone", UsageType.RE));
    setChild(10, patientMultipleBirthIndicator = new ID("Patient multiple birth indicator", UsageType.RE));
    patientMultipleBirthIndicator.setLength(1, 1);
    setChild(11, patientBirthOrder = new NM("Patient birth order", UsageType.RE));
    patientBirthOrder.setLength(1, 2);
    setChild(12, clientLastUpdatedDate = new TS("Client last updated date", UsageType.RE));
    setChild(13, clientLastUpdateFacility = new HD("Client last update facility", UsageType.RE));
  }

}
