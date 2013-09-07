package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.SetID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.JCC;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XAD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XON;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XPN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XTN;

public class NK1 extends HL7Component
{
  private SI setIDNK1 = null;
  private XPN name = null;
  private CE relationship = null;
  private XAD address = null;
  private XTN phoneNumber = null;
  private XTN businessPhoneNumber = null;
  private CE contactRole = null;
  private DT startDate = null;
  private DT endDate = null;
  private ST nextOfKinAssociatedPartiesJobTitle = null;
  private JCC nextOfKinAssociatedPartiesJobCodeClass = null;
  private CX nextOfKinAssociatedPartiesEmployeeNumber = null;
  private XON organizationNameNK1 = null;
  private CE maritalStatus = null;
  private IS administrativeSex = null;
  private TS dateTimeOfBirth = null;
  private IS livingDependency = null;
  private IS ambulatoryStatus = null;
  private CE citizenship = null;
  private CE primaryLanguage = null;
  private IS livingArrangement = null;
  private CE publicityCode = null;
  private ID protectionIndicator = null;
  private IS studentIndicator = null;
  private CE religion = null;
  private XPN mothersMaidenName = null;
  private CE nationality = null;
  private CE ethnicGroup = null;
  private CE contactReason = null;
  private XPN contactPersonsName = null;
  private XTN contactPersonsTelephoneNumber = null;
  private XAD contactPersonsAddress = null;
  private CX nextOfKinAssociatedPartysIdentifiers = null;
  private IS jobStatus = null;
  private CE race = null;
  private IS handicap = null;
  private ST contactPersonSocialSecurityNumber = null;
  private ST nextOfKinBirthPlace = null;
  private IS vipIndicator = null;

  public SI getSetIDNK1() {
    return setIDNK1;
  }

  public void setSetIDNK1(SI setIDNK1) {
    this.setIDNK1 = setIDNK1;
  }

  public XPN getName() {
    return name;
  }

  public void setName(XPN name) {
    this.name = name;
  }

  public CE getRelationship() {
    return relationship;
  }

  public void setRelationship(CE relationship) {
    this.relationship = relationship;
  }

  public XAD getAddress() {
    return address;
  }

  public void setAddress(XAD address) {
    this.address = address;
  }

  public XTN getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(XTN phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public XTN getBusinessPhoneNumber() {
    return businessPhoneNumber;
  }

  public void setBusinessPhoneNumber(XTN businessPhoneNumber) {
    this.businessPhoneNumber = businessPhoneNumber;
  }

  public CE getContactRole() {
    return contactRole;
  }

  public void setContactRole(CE contactRole) {
    this.contactRole = contactRole;
  }

  public DT getStartDate() {
    return startDate;
  }

  public void setStartDate(DT startDate) {
    this.startDate = startDate;
  }

  public DT getEndDate() {
    return endDate;
  }

  public void setEndDate(DT endDate) {
    this.endDate = endDate;
  }

  public ST getNextOfKinAssociatedPartiesJobTitle() {
    return nextOfKinAssociatedPartiesJobTitle;
  }

  public void setNextOfKinAssociatedPartiesJobTitle(ST nextOfKinAssociatedPartiesJobTitle) {
    this.nextOfKinAssociatedPartiesJobTitle = nextOfKinAssociatedPartiesJobTitle;
  }

  public JCC getNextOfKinAssociatedPartiesJobCodeClass() {
    return nextOfKinAssociatedPartiesJobCodeClass;
  }

  public void setNextOfKinAssociatedPartiesJobCodeClass(JCC nextOfKinAssociatedPartiesJobCodeClass) {
    this.nextOfKinAssociatedPartiesJobCodeClass = nextOfKinAssociatedPartiesJobCodeClass;
  }

  public CX getNextOfKinAssociatedPartiesEmployeeNumber() {
    return nextOfKinAssociatedPartiesEmployeeNumber;
  }

  public void setNextOfKinAssociatedPartiesEmployeeNumber(CX nextOfKinAssociatedPartiesEmployeeNumber) {
    this.nextOfKinAssociatedPartiesEmployeeNumber = nextOfKinAssociatedPartiesEmployeeNumber;
  }

  public XON getOrganizationNameNK1() {
    return organizationNameNK1;
  }

  public void setOrganizationNameNK1(XON organizationNameNK1) {
    this.organizationNameNK1 = organizationNameNK1;
  }

  public CE getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(CE maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public IS getAdministrativeSex() {
    return administrativeSex;
  }

  public void setAdministrativeSex(IS administrativeSex) {
    this.administrativeSex = administrativeSex;
  }

  public TS getDateTimeOfBirth() {
    return dateTimeOfBirth;
  }

  public void setDateTimeOfBirth(TS dateTimeOfBirth) {
    this.dateTimeOfBirth = dateTimeOfBirth;
  }

  public IS getLivingDependency() {
    return livingDependency;
  }

  public void setLivingDependency(IS livingDependency) {
    this.livingDependency = livingDependency;
  }

  public IS getAmbulatoryStatus() {
    return ambulatoryStatus;
  }

  public void setAmbulatoryStatus(IS ambulatoryStatus) {
    this.ambulatoryStatus = ambulatoryStatus;
  }

  public CE getCitizenship() {
    return citizenship;
  }

  public void setCitizenship(CE citizenship) {
    this.citizenship = citizenship;
  }

  public CE getPrimaryLanguage() {
    return primaryLanguage;
  }

  public void setPrimaryLanguage(CE primaryLanguage) {
    this.primaryLanguage = primaryLanguage;
  }

  public IS getLivingArrangement() {
    return livingArrangement;
  }

  public void setLivingArrangement(IS livingArrangement) {
    this.livingArrangement = livingArrangement;
  }

  public CE getPublicityCode() {
    return publicityCode;
  }

  public void setPublicityCode(CE publicityCode) {
    this.publicityCode = publicityCode;
  }

  public ID getProtectionIndicator() {
    return protectionIndicator;
  }

  public void setProtectionIndicator(ID protectionIndicator) {
    this.protectionIndicator = protectionIndicator;
  }

  public IS getStudentIndicator() {
    return studentIndicator;
  }

  public void setStudentIndicator(IS studentIndicator) {
    this.studentIndicator = studentIndicator;
  }

  public CE getReligion() {
    return religion;
  }

  public void setReligion(CE religion) {
    this.religion = religion;
  }

  public XPN getMothersMaidenName() {
    return mothersMaidenName;
  }

  public void setMothersMaidenName(XPN mothersMaidenName) {
    this.mothersMaidenName = mothersMaidenName;
  }

  public CE getNationality() {
    return nationality;
  }

  public void setNationality(CE nationality) {
    this.nationality = nationality;
  }

  public CE getEthnicGroup() {
    return ethnicGroup;
  }

  public void setEthnicGroup(CE ethnicGroup) {
    this.ethnicGroup = ethnicGroup;
  }

  public CE getContactReason() {
    return contactReason;
  }

  public void setContactReason(CE contactReason) {
    this.contactReason = contactReason;
  }

  public XPN getContactPersonsName() {
    return contactPersonsName;
  }

  public void setContactPersonsName(XPN contactPersonsName) {
    this.contactPersonsName = contactPersonsName;
  }

  public XTN getContactPersonsTelephoneNumber() {
    return contactPersonsTelephoneNumber;
  }

  public void setContactPersonsTelephoneNumber(XTN contactPersonsTelephoneNumber) {
    this.contactPersonsTelephoneNumber = contactPersonsTelephoneNumber;
  }

  public XAD getContactPersonsAddress() {
    return contactPersonsAddress;
  }

  public void setContactPersonsAddress(XAD contactPersonsAddress) {
    this.contactPersonsAddress = contactPersonsAddress;
  }

  public CX getNextOfKinAssociatedPartysIdentifiers() {
    return nextOfKinAssociatedPartysIdentifiers;
  }

  public void setNextOfKinAssociatedPartysIdentifiers(CX nextOfKinAssociatedPartysIdentifiers) {
    this.nextOfKinAssociatedPartysIdentifiers = nextOfKinAssociatedPartysIdentifiers;
  }

  public IS getJobStatus() {
    return jobStatus;
  }

  public void setJobStatus(IS jobStatus) {
    this.jobStatus = jobStatus;
  }

  public CE getRace() {
    return race;
  }

  public void setRace(CE race) {
    this.race = race;
  }

  public IS getHandicap() {
    return handicap;
  }

  public void setHandicap(IS handicap) {
    this.handicap = handicap;
  }

  public ST getContactPersonSocialSecurityNumber() {
    return contactPersonSocialSecurityNumber;
  }

  public void setContactPersonSocialSecurityNumber(ST contactPersonSocialSecurityNumber) {
    this.contactPersonSocialSecurityNumber = contactPersonSocialSecurityNumber;
  }

  public ST getNextOfKinBirthPlace() {
    return nextOfKinBirthPlace;
  }

  public void setNextOfKinBirthPlace(ST nextOfKinBirthPlace) {
    this.nextOfKinBirthPlace = nextOfKinBirthPlace;
  }

  public IS getVipIndicator() {
    return vipIndicator;
  }

  public void setVipIndicator(IS vipIndicator) {
    this.vipIndicator = vipIndicator;
  }

  @Override
  public HL7Component makeAnother() {
    return new NK1(this);
  }

  public NK1(NK1 copy) {
    super(copy);
    init();
  }

  public NK1(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "NK1", "Next of Kin Segment", "Next of Kin Segment", 39, usageType, cardinality);
    init();
  }

  @Override
  public void init() {
    setChild(1, setIDNK1 = new SI("Set ID - NK1", UsageType.R, Cardinality.ONE_TIME_ONLY));
    setIDNK1
        .addConformanceStatement(new SetID(
            "For the first occurrence of the segment, the sequence number shall be one, for the second occurrence, the sequence number shall be two, etc.",
            this));
    setChild(2, name = new XPN("Name", UsageType.R, Cardinality.ONE_OR_MORE_TIMES));
    name.getNameTypeCode().addConformanceStatement(
        new ExactValue("The legal name must be sent in the first sequence", "L"));
    setChild(3, relationship = new CE("Relationship", UsageType.R, Cardinality.ONE_TIME_ONLY, ValueSet.HL70063));
    setChild(4, address = new XAD("Address", UsageType.RE, cardinality.ZERO_OR_MORE));
    address.getAddressType().addConformanceStatement(
        new ExactValue("The mailing address must be sent in the first sequence", new String[] {"M", "L"}));
    setChild(5, phoneNumber = new XTN("Phone Number", UsageType.RE, Cardinality.ZERO_OR_MORE));
    phoneNumber.getTelecommunicationUseCode().addConformanceStatement(
        new ExactValue("The primary telephone number must be sent in the first sequence", "PRN"));
    phoneNumber.getTelecommunicationEquipmentType().addConformanceStatement(
        new ExactValue("The primary telephone number must be sent in the first sequence", "PH"));
    setChild(6, businessPhoneNumber = new XTN("Business Phone Number", UsageType.O));
    setChild(7, contactRole = new CE("Contact Role", UsageType.O));
    setChild(8, startDate = new DT("Start Date", UsageType.O));
    setChild(9, endDate = new DT("End Date", UsageType.O));
    setChild(10, nextOfKinAssociatedPartiesJobTitle = new ST("Next of Kin / Associated Parties Job Title", UsageType.O));
    setChild(11, nextOfKinAssociatedPartiesJobCodeClass = new JCC("Next of Kin / Associated Parties Job Code/Class",
        UsageType.O));
    setChild(12, nextOfKinAssociatedPartiesEmployeeNumber = new CX("Next of Kin / Associated Parties Employee Number",
        UsageType.O));
    setChild(13, organizationNameNK1 = new XON("Organization Name - NK1", UsageType.O));
    setChild(14, maritalStatus = new CE("Marital Status", UsageType.O));
    setChild(15, administrativeSex = new IS("Administrative Sex", UsageType.O));
    setChild(16, dateTimeOfBirth = new TS("Date/Time of Birth", UsageType.O));
    setChild(17, livingDependency = new IS("Living Dependency", UsageType.O));
    setChild(18, ambulatoryStatus = new IS("Ambulatory Status", UsageType.O));
    setChild(19, citizenship = new CE("Citzenship", UsageType.O));
    setChild(20, primaryLanguage = new CE("Primary Language", UsageType.O));
    setChild(21, livingArrangement = new IS("Living Arrangement", UsageType.O));
    setChild(22, publicityCode = new CE("Publicity Code", UsageType.O));
    setChild(23, protectionIndicator = new ID("Protection Indicator", UsageType.O));
    setChild(24, studentIndicator = new IS("Student Indicator", UsageType.O));
    setChild(25, religion = new CE("Religion", UsageType.O));
    setChild(26, mothersMaidenName = new XPN("Mother's Maiden Name", UsageType.O));
    setChild(27, nationality = new CE("Nationality", UsageType.O));
    setChild(28, ethnicGroup = new CE("Nationality", UsageType.O));
    setChild(29, contactReason = new CE("Contact Reason", UsageType.O));
    setChild(30, contactPersonsName = new XPN("Contact Person's Name", UsageType.O));
    setChild(31, contactPersonsTelephoneNumber = new XTN("Contact Person's Telephone Number", UsageType.O));
    setChild(32, contactPersonsAddress = new XAD("Contact Person's Address", UsageType.O));
    setChild(33, nextOfKinAssociatedPartysIdentifiers = new CX("Next of Kin/Associated Partiy's Identifiers",
        UsageType.O));
    setChild(34, jobStatus = new IS("Job Status", UsageType.O));
    setChild(35, race = new CE("Race", UsageType.O));
    setChild(36, handicap = new IS("Handicap", UsageType.O));
    setChild(37, contactPersonSocialSecurityNumber = new ST("Contact Person Social Security Number", UsageType.O));
    setChild(38, nextOfKinBirthPlace = new ST("Next Of Kin Birth Place", UsageType.O));
    setChild(39, vipIndicator = new IS("VIP Indicator", UsageType.O));
  }

}
