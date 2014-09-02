package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.AllEmpty;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class XCN extends HL7Component
{

  private ST idNumber = null;
  private FN familyName = null;
  private ST givenName = null;
  private ST secondAndFurtherGivenNamesOrInitialsTherof = null;
  private ST suffix = null;
  private ST prefix = null;
  private IS degree = null;
  private IS sourceTable = null;
  private HD assigningAuthority = null;
  private ID nameTypeCode = null;
  private ST identifierCheckDigit = null;
  private ID checkDigitScheme = null;
  private ID identifierTypeCode = null;
  private HD assigningFacility = null;
  private ID nameRepresentationCode = null;
  private CE nameContext = null;
  private DR nameValidityRange = null;
  private ID nameAssemblyOrder = null;
  private TS effectiveDate = null;
  private TS expirationDate = null;
  private ST professionalSuffix = null;
  private CWE assigningJurisdiction = null;
  private CWE assigningAgencyOrDepartment = null;

  public ST getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(ST idNumber) {
    this.idNumber = idNumber;
  }

  public FN getFamilyName() {
    return familyName;
  }

  public void setFamilyName(FN familyName) {
    this.familyName = familyName;
  }

  public ST getGivenName() {
    return givenName;
  }

  public void setGivenName(ST givenName) {
    this.givenName = givenName;
  }

  public ST getSecondAndFurtherGivenNamesOrInitialsTherof() {
    return secondAndFurtherGivenNamesOrInitialsTherof;
  }

  public void setSecondAndFurtherGivenNamesOrInitialsTherof(ST secondAndFurtherGivenNamesOrInitialsTherof) {
    this.secondAndFurtherGivenNamesOrInitialsTherof = secondAndFurtherGivenNamesOrInitialsTherof;
  }

  public ST getSuffix() {
    return suffix;
  }

  public void setSuffix(ST suffix) {
    this.suffix = suffix;
  }

  public ST getPrefix() {
    return prefix;
  }

  public void setPrefix(ST prefix) {
    this.prefix = prefix;
  }

  public IS getDegree() {
    return degree;
  }

  public void setDegree(IS degree) {
    this.degree = degree;
  }

  public IS getSourceTable() {
    return sourceTable;
  }

  public void setSourceTable(IS sourceTable) {
    this.sourceTable = sourceTable;
  }

  public HD getAssigningAuthority() {
    return assigningAuthority;
  }

  public void setAssigningAuthority(HD assigningAuthority) {
    this.assigningAuthority = assigningAuthority;
  }

  public ID getNameTypeCode() {
    return nameTypeCode;
  }

  public void setNameTypeCode(ID nameTypeCode) {
    this.nameTypeCode = nameTypeCode;
  }

  public ST getIdentifierCheckDigit() {
    return identifierCheckDigit;
  }

  public void setIdentifierCheckDigit(ST identifierCheckDigit) {
    this.identifierCheckDigit = identifierCheckDigit;
  }

  public ID getCheckDigitScheme() {
    return checkDigitScheme;
  }

  public void setCheckDigitScheme(ID checkDigitScheme) {
    this.checkDigitScheme = checkDigitScheme;
  }

  public ID getIdentifierTypeCode() {
    return identifierTypeCode;
  }

  public void setIdentifierTypeCode(ID identifierTypeCode) {
    this.identifierTypeCode = identifierTypeCode;
  }

  public HD getAssigningFacility() {
    return assigningFacility;
  }

  public void setAssigningFacility(HD assigningFacility) {
    this.assigningFacility = assigningFacility;
  }

  public ID getNameRepresentationCode() {
    return nameRepresentationCode;
  }

  public void setNameRepresentationCode(ID nameRepresentationCode) {
    this.nameRepresentationCode = nameRepresentationCode;
  }

  public CE getNameContext() {
    return nameContext;
  }

  public void setNameContext(CE nameContext) {
    this.nameContext = nameContext;
  }

  public DR getNameValidityRange() {
    return nameValidityRange;
  }

  public void setNameValidityRange(DR nameValidityRange) {
    this.nameValidityRange = nameValidityRange;
  }

  public ID getNameAssemblyOrder() {
    return nameAssemblyOrder;
  }

  public void setNameAssemblyOrder(ID nameAssemblyOrder) {
    this.nameAssemblyOrder = nameAssemblyOrder;
  }

  public TS getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(TS effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public TS getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(TS expirationDate) {
    this.expirationDate = expirationDate;
  }

  public ST getProfessionalSuffix() {
    return professionalSuffix;
  }

  public void setProfessionalSuffix(ST professionalSuffix) {
    this.professionalSuffix = professionalSuffix;
  }

  public CWE getAssigningJurisdiction() {
    return assigningJurisdiction;
  }

  public void setAssigningJurisdiction(CWE assigningJurisdiction) {
    this.assigningJurisdiction = assigningJurisdiction;
  }

  public CWE getAssigningAgencyOrDepartment() {
    return assigningAgencyOrDepartment;
  }

  public void setAssigningAgencyOrDepartment(CWE assigningAgencyOrDepartment) {
    this.assigningAgencyOrDepartment = assigningAgencyOrDepartment;
  }

  public XCN(String componentName, UsageType usageType) {
    this(componentName, usageType, Integer.MAX_VALUE);
  }

  @Override
  public HL7Component makeAnother() {
    return new XCN(this);
  }
  
  public XCN(XCN copy)
  {
    super(copy);
    init();
  }

  public XCN(String componentName, UsageType usageType, int lengthMax) {
    super(ItemType.DATATYPE, "XCN", "Extended Composite ID Number and Name for Persons", componentName, 23, usageType, lengthMax);
    init();
  }

  public XCN(String componentName, UsageType usageType, Cardinality cardinality) {
    super(ItemType.DATATYPE, "XCN", "Extended Composite ID Number and Name for Persons", componentName, 23, usageType, cardinality);
    init();
  }

  public void init() {
    setChild(1, idNumber = new ST("ID Number", UsageType.C, 15));
    setChild(2, familyName = new FN("Family Name", UsageType.RE));
    setChild(3, givenName = new ST("Given Name", UsageType.RE));
    idNumber.setConditionalPredicate(new AllEmpty(new HL7Component[] { familyName, givenName }, UsageType.R,
        UsageType.RE));
    setChild(4, secondAndFurtherGivenNamesOrInitialsTherof = new ST(
        "Second and Further Given Names or Initials Thereof", UsageType.RE, 30));
    setChild(5, suffix = new ST("Suffix", UsageType.O));
    setChild(6, prefix = new ST("Prefix", UsageType.O));
    setChild(7, degree = new IS("Degree", UsageType.X));
    setChild(8, sourceTable = new IS("Source Table", UsageType.O));
    setChild(9, assigningAuthority = new HD("Assigning Authority", UsageType.C, ValueSet.HL70363));
    assigningAuthority.setConditionalPredicate(new IsValued(idNumber, UsageType.R, UsageType.X));
    setChild(10, nameTypeCode = new ID("Name Type Code", UsageType.RE, 1, ValueSet.HL70200));
    setChild(11, identifierCheckDigit = new ST("Identifier Check Digit", UsageType.O));
    setChild(12, checkDigitScheme = new ID("Check Digit Scheme", UsageType.C));
    checkDigitScheme.setConditionalPredicate(new IsValued(identifierCheckDigit, UsageType.O, UsageType.X));
    setChild(13, identifierTypeCode = new ID("Identifier Type Code", UsageType.O));
    setChild(14, assigningFacility = new HD("Assigning Facility", UsageType.O));
    setChild(15, nameRepresentationCode = new ID("Name Representation Code", UsageType.O));
    setChild(16, nameContext = new CE("Name Context", UsageType.O));
    setChild(17, nameValidityRange = new DR("Name Validity Range", UsageType.X));
    setChild(18, nameAssemblyOrder = new ID("Name Assembly Order", UsageType.X));
    setChild(19, effectiveDate = new TS("Effective Date", UsageType.O));
    setChild(20, expirationDate = new TS("Expiration Date", UsageType.O));
    setChild(21, professionalSuffix = new ST("Professional Suffix", UsageType.O));
    setChild(22, assigningJurisdiction = new CWE("Assigning Jurisdiction", UsageType.O));
    setChild(23, assigningJurisdiction = new CWE("Assigning Agency or Department", UsageType.O));
  }
}
