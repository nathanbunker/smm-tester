package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XCN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XON;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class PD1 extends HL7Component
{

  private IS livingDependency = null;
  private IS livingArrangement = null;
  private XON patientPrimaryFacility = null;
  private XCN patientPrimaryCareProviderNameIDNo = null;
  private IS studentIndicator = null;
  private IS handicap = null;
  private IS livingWillCode = null;
  private IS organDonorCode = null;
  private ID separateBill = null;
  private CX duplicatePatient = null;
  private CE publicityCode = null;
  private ID protectionIndicator = null;
  private DT protectionIndicatorEffectiveDate = null;
  private XON placeOfWorship = null;
  private CE advanceDirectiveCode = null;
  private IS immunizationRegistryStatus = null;
  private DT immunizationRegistryStatusEffectiveDate = null;
  private DT publicityCodeEffectiveDate = null;
  private IS militaryBranch = null;
  private IS militaryRankGrade = null;
  private IS militaryStatus = null;

  public IS getLivingDependency() {
    return livingDependency;
  }

  public void setLivingDependency(IS livingDependency) {
    this.livingDependency = livingDependency;
  }

  public IS getLivingArrangement() {
    return livingArrangement;
  }

  public void setLivingArrangement(IS livingArrangement) {
    this.livingArrangement = livingArrangement;
  }

  public XON getPatientPrimaryFacility() {
    return patientPrimaryFacility;
  }

  public void setPatientPrimaryFacility(XON patientPrimaryFacility) {
    this.patientPrimaryFacility = patientPrimaryFacility;
  }

  public XCN getPatientPrimaryCareProviderNameIDNo() {
    return patientPrimaryCareProviderNameIDNo;
  }

  public void setPatientPrimaryCareProviderNameIDNo(XCN patientPrimaryCareProviderNameIDNo) {
    this.patientPrimaryCareProviderNameIDNo = patientPrimaryCareProviderNameIDNo;
  }

  public IS getStudentIndicator() {
    return studentIndicator;
  }

  public void setStudentIndicator(IS studentIndicator) {
    this.studentIndicator = studentIndicator;
  }

  public IS getHandicap() {
    return handicap;
  }

  public void setHandicap(IS handicap) {
    this.handicap = handicap;
  }

  public IS getLivingWillCode() {
    return livingWillCode;
  }

  public void setLivingWillCode(IS livingWillCode) {
    this.livingWillCode = livingWillCode;
  }

  public IS getOrganDonorCode() {
    return organDonorCode;
  }

  public void setOrganDonorCode(IS organDonorCode) {
    this.organDonorCode = organDonorCode;
  }

  public ID getSeparateBill() {
    return separateBill;
  }

  public void setSeparateBill(ID separateBill) {
    this.separateBill = separateBill;
  }

  public CX getDuplicatePatient() {
    return duplicatePatient;
  }

  public void setDuplicatePatient(CX duplicatePatient) {
    this.duplicatePatient = duplicatePatient;
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

  public DT getProtectionIndicatorEffectiveDate() {
    return protectionIndicatorEffectiveDate;
  }

  public void setProtectionIndicatorEffectiveDate(DT protectionIndicatorEffectiveDate) {
    this.protectionIndicatorEffectiveDate = protectionIndicatorEffectiveDate;
  }

  public XON getPlaceOfWorship() {
    return placeOfWorship;
  }

  public void setPlaceOfWorship(XON placeOfWorship) {
    this.placeOfWorship = placeOfWorship;
  }

  public CE getAdvanceDirectiveCode() {
    return advanceDirectiveCode;
  }

  public void setAdvanceDirectiveCode(CE advanceDirectiveCode) {
    this.advanceDirectiveCode = advanceDirectiveCode;
  }

  public IS getImmunizationRegistryStatus() {
    return immunizationRegistryStatus;
  }

  public void setImmunizationRegistryStatus(IS immunizationRegistryStatus) {
    this.immunizationRegistryStatus = immunizationRegistryStatus;
  }

  public DT getImmunizationRegistryStatusEffectiveDate() {
    return immunizationRegistryStatusEffectiveDate;
  }

  public void setImmunizationRegistryStatusEffectiveDate(DT immunizationRegistryStatusEffectiveDate) {
    this.immunizationRegistryStatusEffectiveDate = immunizationRegistryStatusEffectiveDate;
  }

  public DT getPublicityCodeEffectiveDate() {
    return publicityCodeEffectiveDate;
  }

  public void setPublicityCodeEffectiveDate(DT publicityCodeEffectiveDate) {
    this.publicityCodeEffectiveDate = publicityCodeEffectiveDate;
  }

  public IS getMilitaryBranch() {
    return militaryBranch;
  }

  public void setMilitaryBranch(IS militaryBranch) {
    this.militaryBranch = militaryBranch;
  }

  public IS getMilitaryRankGrade() {
    return militaryRankGrade;
  }

  public void setMilitaryRankGrade(IS militaryRankGrade) {
    this.militaryRankGrade = militaryRankGrade;
  }

  public IS getMilitaryStatus() {
    return militaryStatus;
  }

  public void setMilitaryStatus(IS militaryStatus) {
    this.militaryStatus = militaryStatus;
  }

  @Override
  public HL7Component makeAnother() {
    return new PD1(this);
  }

  public PD1(PD1 copy) {
    super(copy);
    init();
  }

  public PD1(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "PD1", "Patient Demographic Segment", "Patient Demographic Segment", 21, usageType, cardinality);
    init();
  }

  @Override
  public void init() {
    setChild(1, livingDependency = new IS("Living Dependency", UsageType.O));
    setChild(2, livingArrangement = new IS("Living Arrangement", UsageType.O));
    setChild(3, patientPrimaryFacility = new XON("Patient Primary Facility", UsageType.O));
    setChild(4,
        patientPrimaryCareProviderNameIDNo = new XCN("Patient Primary Care Provider Name & ID No.", UsageType.O));
    setChild(5, studentIndicator = new IS("Student Indicator", UsageType.O));
    setChild(6, handicap = new IS("Handicap", UsageType.O));
    setChild(7, livingWillCode = new IS("Living Will Code", UsageType.O));
    setChild(8, organDonorCode = new IS("Organ Donor Code", UsageType.O));
    setChild(9, separateBill = new ID("Separate Bill", UsageType.O));
    setChild(10, duplicatePatient = new CX("Duplicate Patient", UsageType.O));
    setChild(11, publicityCode = new CE("Publicity Code", UsageType.RE, Cardinality.ZERO_OR_ONE, ValueSet.HL70215));
    setChild(12, protectionIndicator = new ID("Protection Indicator", UsageType.RE, Cardinality.ZERO_OR_ONE,
        ValueSet.HL70215));
    setChild(13, protectionIndicatorEffectiveDate = new DT("Protection Indicator Effective Date", UsageType.C,
        Cardinality.ZERO_OR_ONE));
    protectionIndicatorEffectiveDate.setConditionalPredicate(new IsValued(protectionIndicator, UsageType.RE,
        UsageType.X));
    setChild(14, placeOfWorship = new XON("Place of Worship", UsageType.O));
    setChild(15, advanceDirectiveCode = new CE("Advance Directive Code", UsageType.O));
    setChild(16, immunizationRegistryStatus = new IS("Immunization Registry Status", UsageType.RE,
        Cardinality.ZERO_OR_ONE, ValueSet.HL70441));
    setChild(17, immunizationRegistryStatusEffectiveDate = new DT("Immunization Registry Status Effective Date",
        UsageType.C, Cardinality.ZERO_OR_ONE));
    immunizationRegistryStatusEffectiveDate.setConditionalPredicate(new IsValued(immunizationRegistryStatus,
        UsageType.RE, UsageType.X));
    setChild(18, publicityCodeEffectiveDate = new DT("Publicity Code Effective Date", UsageType.C,
        Cardinality.ZERO_OR_ONE));
    publicityCodeEffectiveDate.setConditionalPredicate(new IsValued(publicityCode, UsageType.RE, UsageType.X));
    setChild(19, militaryBranch = new IS("Military Branch", UsageType.O));
    setChild(20, militaryRankGrade = new IS("Military Rank/Grade", UsageType.O));
    setChild(21, militaryStatus = new IS("Military Status", UsageType.O));
  }
}
