package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.DatePrecision;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CWE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DLN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.HD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XAD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XPN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XPN_maiden;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XTN;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValuedAs;

public class PID extends HL7Component
{

  private SI setIDPID;
  private CX patientID;
  private CX patientIdentiferList;
  private CX alternatePatientID;
  private XPN patientName;
  private XPN_maiden mothersMaidenName;
  private TS dateTimeOfBirth;
  private IS administrativeSex;
  private XPN patientAlias;
  private CE race;
  private XAD patientAddress;
  private IS countyCode;
  private XTN phoneNumberHome;
  private XTN PhoneNumberBusiness;
  private CE primaryLanguage;
  private CE maritalStatus;
  private CE religion;
  private CX patientAccountNumber;
  private ST ssnNumberPatient;
  private DLN driversLicenseNumberPatient;
  private CX mothersIdentifer;
  private CE ethnicGroup;
  private ST birthPlace;
  private ID multipleBirthIndicator;
  private NM birthOrder;
  private CE citizenship;
  private CE veteransMilitaryStatus;
  private CE nationality;
  private TS patientDeathDateAndTime;
  private ID patientDeathIndicator;
  private ID identityUnknownIndicator;
  private IS identityReliabilityCode;
  private TS lastUpdateDateTime;
  private HD lastUpdateFacility;
  private CE speciesCode;
  private CE breedCode;
  private ST strain;
  private CE productionClassCode;
  private CWE tribalCitizenship;

  public SI getSetIDPID() {
    return setIDPID;
  }

  public void setSetIDPID(SI setIDPID) {
    this.setIDPID = setIDPID;
  }

  public CX getPatientID() {
    return patientID;
  }

  public void setPatientID(CX patientID) {
    this.patientID = patientID;
  }

  public CX getPatientIdentiferList() {
    return patientIdentiferList;
  }

  public void setPatientIdentiferList(CX patientIdentiferList) {
    this.patientIdentiferList = patientIdentiferList;
  }

  public CX getAlternatePatientID() {
    return alternatePatientID;
  }

  public void setAlternatePatientID(CX alternatePatientID) {
    this.alternatePatientID = alternatePatientID;
  }

  public XPN getPatientName() {
    return patientName;
  }

  public void setPatientName(XPN patientName) {
    this.patientName = patientName;
  }

  public XPN_maiden getMothersMaidenName() {
    return mothersMaidenName;
  }

  public void setMothersMaidenName(XPN_maiden mothersMaidenName) {
    this.mothersMaidenName = mothersMaidenName;
  }

  public TS getDateTimeOfBirth() {
    return dateTimeOfBirth;
  }

  public void setDateTimeOfBirth(TS dateTimeOfBirth) {
    this.dateTimeOfBirth = dateTimeOfBirth;
  }

  public IS getAdministrativeSex() {
    return administrativeSex;
  }

  public void setAdministrativeSex(IS administrativeSex) {
    this.administrativeSex = administrativeSex;
  }

  public XPN getPatientAlias() {
    return patientAlias;
  }

  public void setPatientAlias(XPN patientAlias) {
    this.patientAlias = patientAlias;
  }

  public CE getRace() {
    return race;
  }

  public void setRace(CE race) {
    this.race = race;
  }

  public XAD getPatientAddress() {
    return patientAddress;
  }

  public void setPatientAddress(XAD patientAddress) {
    this.patientAddress = patientAddress;
  }

  public IS getCountyCode() {
    return countyCode;
  }

  public void setCountyCode(IS countyCode) {
    this.countyCode = countyCode;
  }

  public XTN getPhoneNumberHome() {
    return phoneNumberHome;
  }

  public void setPhoneNumberHome(XTN phoneNumberHome) {
    this.phoneNumberHome = phoneNumberHome;
  }

  public XTN getPhoneNumberBusiness() {
    return PhoneNumberBusiness;
  }

  public void setPhoneNumberBusiness(XTN phoneNumberBusiness) {
    PhoneNumberBusiness = phoneNumberBusiness;
  }

  public CE getPrimaryLanguage() {
    return primaryLanguage;
  }

  public void setPrimaryLanguage(CE primaryLanguage) {
    this.primaryLanguage = primaryLanguage;
  }

  public CE getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(CE maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public CE getReligion() {
    return religion;
  }

  public void setReligion(CE religion) {
    this.religion = religion;
  }

  public CX getPatientAccountNumber() {
    return patientAccountNumber;
  }

  public void setPatientAccountNumber(CX patientAccountNumber) {
    this.patientAccountNumber = patientAccountNumber;
  }

  public ST getSsnNumberPatient() {
    return ssnNumberPatient;
  }

  public void setSsnNumberPatient(ST ssnNumberPatient) {
    this.ssnNumberPatient = ssnNumberPatient;
  }

  public DLN getDriversLicenseNumberPatient() {
    return driversLicenseNumberPatient;
  }

  public void setDriversLicenseNumberPatient(DLN driversLicenseNumberPatient) {
    this.driversLicenseNumberPatient = driversLicenseNumberPatient;
  }

  public CX getMothersIdentifer() {
    return mothersIdentifer;
  }

  public void setMothersIdentifer(CX mothersIdentifer) {
    this.mothersIdentifer = mothersIdentifer;
  }

  public CE getEthnicGroup() {
    return ethnicGroup;
  }

  public void setEthnicGroup(CE ethnicGroup) {
    this.ethnicGroup = ethnicGroup;
  }

  public ST getBirthPlace() {
    return birthPlace;
  }

  public void setBirthPlace(ST birthPlace) {
    this.birthPlace = birthPlace;
  }

  public ID getMultipleBirthIndicator() {
    return multipleBirthIndicator;
  }

  public void setMultipleBirthIndicator(ID multipleBirthIndicator) {
    this.multipleBirthIndicator = multipleBirthIndicator;
  }

  public NM getBirthOrder() {
    return birthOrder;
  }

  public void setBirthOrder(NM birthOrder) {
    this.birthOrder = birthOrder;
  }

  public CE getCitizenship() {
    return citizenship;
  }

  public void setCitizenship(CE citizenship) {
    this.citizenship = citizenship;
  }

  public CE getVeteransMilitaryStatus() {
    return veteransMilitaryStatus;
  }

  public void setVeteransMilitaryStatus(CE veteransMilitaryStatus) {
    this.veteransMilitaryStatus = veteransMilitaryStatus;
  }

  public CE getNationality() {
    return nationality;
  }

  public void setNationality(CE nationality) {
    this.nationality = nationality;
  }

  public TS getPatientDeathDateAndTime() {
    return patientDeathDateAndTime;
  }

  public void setPatientDeathDateAndTime(TS patientDeathDateAndTime) {
    this.patientDeathDateAndTime = patientDeathDateAndTime;
  }

  public ID getPatientDeathIndicator() {
    return patientDeathIndicator;
  }

  public void setPatientDeathIndicator(ID patientDeathIndicator) {
    this.patientDeathIndicator = patientDeathIndicator;
  }

  public ID getIdentityUnknownIndicator() {
    return identityUnknownIndicator;
  }

  public void setIdentityUnknownIndicator(ID identityUnknownIndicator) {
    this.identityUnknownIndicator = identityUnknownIndicator;
  }

  public IS getIdentityReliabilityCode() {
    return identityReliabilityCode;
  }

  public void setIdentityReliabilityCode(IS identityReliabilityCode) {
    this.identityReliabilityCode = identityReliabilityCode;
  }

  public TS getLastUpdateDateTime() {
    return lastUpdateDateTime;
  }

  public void setLastUpdateDateTime(TS lastUpdateDateTime) {
    this.lastUpdateDateTime = lastUpdateDateTime;
  }

  public HD getLastUpdateFacility() {
    return lastUpdateFacility;
  }

  public void setLastUpdateFacility(HD lastUpdateFacility) {
    this.lastUpdateFacility = lastUpdateFacility;
  }

  public CE getSpeciesCode() {
    return speciesCode;
  }

  public void setSpeciesCode(CE speciesCode) {
    this.speciesCode = speciesCode;
  }

  public CE getBreedCode() {
    return breedCode;
  }

  public void setBreedCode(CE breedCode) {
    this.breedCode = breedCode;
  }

  public ST getStrain() {
    return strain;
  }

  public void setStrain(ST strain) {
    this.strain = strain;
  }

  public CE getProductionClassCode() {
    return productionClassCode;
  }

  public void setProductionClassCode(CE productionClassCode) {
    this.productionClassCode = productionClassCode;
  }

  public CWE getTribalCitizenship() {
    return tribalCitizenship;
  }

  public void setTribalCitizenship(CWE tribalCitizenship) {
    this.tribalCitizenship = tribalCitizenship;
  }

  @Override
  public HL7Component makeAnother() {
    return new PID(this);
  }

  public PID(PID copy) {
    super(copy);
    init();
  }

  public PID(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "PID", "Patient Identifier Segment", "Patient Identifier Segment", 39, usageType, cardinality);
    init();
  }

  public void init() {
    setChild(1, setIDPID = new SI("Set ID - PID", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(2, patientID = new CX("Patient ID", UsageType.X));
    setChild(3, patientIdentiferList = new CX("Patient Identifer List", UsageType.R, Cardinality.ONE_OR_MORE_TIMES));
    setChild(4, alternatePatientID = new CX("Alternate Patient ID", UsageType.X));
    setChild(5, patientName = new XPN("Patient Name", UsageType.R, Cardinality.ONE_OR_MORE_TIMES));
    patientName.getNameTypeCode().addConformanceStatement(new ExactValue("The primary or legal name of the patient is reported first, name type code in this field shoud be \"L - Legal\"", "L"));
    setChild(6, mothersMaidenName = new XPN_maiden("Mother's Maiden Name", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(7, dateTimeOfBirth = new TS("Date/Time of Birth", UsageType.R, Cardinality.ONE_TIME_ONLY));
    dateTimeOfBirth.getTime().addConformanceStatement(new DatePrecision("IZ-26: PID-7 (Birth date) SHALL be accurate at least to the day. (YYYYMMDD)", DatePrecision.PRECISION_DAY));
    setChild(8, administrativeSex = new IS("Administrative Sex", UsageType.RE, Cardinality.ZERO_OR_ONE,
        ValueSet.HL70001));
    setChild(9, patientAlias = new XPN("Patient Alias", UsageType.X));
    setChild(10, race = new CE("Race", UsageType.RE, Cardinality.ZERO_OR_MORE, ValueSet.HL70005));
    setChild(11, patientAddress = new XAD("Patient Address", UsageType.RE, Cardinality.ZERO_OR_MORE));
    patientAddress.getAddressType().addConformanceStatement(new ExactValue("The primary mailing address must be sent first in the sequence (for backward compatibility)", new String[] {"M", "L", "P"}));
    setChild(12, countyCode = new IS("County Code", UsageType.X));
    setChild(13, phoneNumberHome = new XTN("Phone Number Home", UsageType.RE, Cardinality.ZERO_OR_MORE));
    phoneNumberHome.getTelecommunicationUseCode().addConformanceStatement(
        new ExactValue("The primary telephone number must be sent in the first sequence", "PRN"));
    phoneNumberHome.getTelecommunicationEquipmentType().addConformanceStatement(
        new ExactValue("The primary telephone number must be sent in the first sequence", "PH"));
    setChild(14, PhoneNumberBusiness = new XTN("Phone Number Business", UsageType.O));
    setChild(15, primaryLanguage = new CE("Primary Language", UsageType.O));
    setChild(16, maritalStatus = new CE("Marital Status", UsageType.O));
    setChild(17, religion = new CE("Religion", UsageType.O));
    setChild(18, patientAccountNumber = new CX("Patient Account Number", UsageType.O));
    setChild(19, ssnNumberPatient = new ST("SSN Number - Patient", UsageType.X));
    setChild(20, driversLicenseNumberPatient = new DLN("Driver's License Number - Patient", UsageType.X));
    setChild(21, mothersIdentifer = new CX("Mother's Identifer", UsageType.X));
    setChild(22, ethnicGroup = new CE("Ethnic Group", UsageType.RE, Cardinality.ZERO_OR_ONE, ValueSet.HL70189));
    setChild(23, birthPlace = new ST("Birth Place", UsageType.O));
    setChild(24, multipleBirthIndicator = new ID("Multiple Birth Indicator", UsageType.RE, Cardinality.ZERO_OR_ONE,
        ValueSet.HL70136));
    setChild(25, birthOrder = new NM("Birth Order", UsageType.C, Cardinality.ZERO_OR_ONE));
    birthOrder.setLength(1, 2);
    birthOrder.setConditionalPredicate(new IsValuedAs(multipleBirthIndicator, "Y", UsageType.RE, UsageType.X));
    setChild(26, citizenship = new CE("Citizenship", UsageType.O));
    setChild(27, veteransMilitaryStatus = new CE("Veterans Military Status", UsageType.O));
    setChild(28, nationality = new CE("Nationality", UsageType.O));
    setChild(29, patientDeathDateAndTime = new TS("Patient Death Date and Time", UsageType.C, Cardinality.ZERO_OR_ONE));
    setChild(30, patientDeathIndicator = new ID("Patient Death Indicator", UsageType.RE, Cardinality.ZERO_OR_ONE,
        ValueSet.HL70136));
    patientDeathDateAndTime.setConditionalPredicate(new IsValuedAs(patientDeathIndicator, "Y", UsageType.RE,
        UsageType.X));
    setChild(31, identityUnknownIndicator = new ID("Identity Unknown Indicator", UsageType.O));
    setChild(32, identityReliabilityCode = new IS("Identity Reliability Code", UsageType.O));
    setChild(33, lastUpdateDateTime = new TS("Last Update Date/Time", UsageType.O));
    setChild(34, lastUpdateFacility = new HD("Last Update Facility", UsageType.O));
    setChild(35, speciesCode = new CE("Species Code", UsageType.O));
    setChild(36, breedCode = new CE("Breed Code", UsageType.O));
    setChild(37, strain = new ST("Strain", UsageType.O));
    setChild(38, productionClassCode = new CE("Production Class Code", UsageType.O));
    setChild(39, tribalCitizenship = new CWE("Tribal Citizenship", UsageType.O));

  }
}
