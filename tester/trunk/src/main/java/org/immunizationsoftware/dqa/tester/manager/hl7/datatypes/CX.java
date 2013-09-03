package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class CX extends HL7Component
{

  private ST idNumber = null;
  private ST checkDigit = null;
  private ID checkDigitScheme = null;
  private HD assigningAuthority = null;
  private ID identifierTypeCode = null;
  private HD assigningFacility = null;
  private DT effectiveDate = null;
  private DT expirationDate = null;
  private CWE assigningJurisdiction = null;
  private CWE assigningAgencyOrDepartment = null;

  public ST getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(ST idNumber) {
    this.idNumber = idNumber;
  }

  public ST getCheckDigit() {
    return checkDigit;
  }

  public void setCheckDigit(ST checkDigit) {
    this.checkDigit = checkDigit;
  }

  public ID getCheckDigitScheme() {
    return checkDigitScheme;
  }

  public void setCheckDigitScheme(ID checkDigitScheme) {
    this.checkDigitScheme = checkDigitScheme;
  }

  public HD getAssigningAuthority() {
    return assigningAuthority;
  }

  public void setAssigningAuthority(HD assigningAuthority) {
    this.assigningAuthority = assigningAuthority;
  }

  public ID getIdentifierTypeCode() {
    return identifierTypeCode;
  }

  public void setIdentifierTypeCode(ID identifyerTypeCode) {
    this.identifierTypeCode = identifyerTypeCode;
  }

  public HD getAssigningFacility() {
    return assigningFacility;
  }

  public void setAssigningFacility(HD assigningFacility) {
    this.assigningFacility = assigningFacility;
  }

  public DT getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(DT effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public DT getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(DT expirationDate) {
    this.expirationDate = expirationDate;
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

  @Override
  public HL7Component makeAnother() {
    return new CX(this);
  }

  public CX(CX copy) {
    super(copy);
    init();
  }

  public CX(String componentName, UsageType usageType, Cardinality cardinality) {
    super("CX", "Extended Composit ID With Check Digit", componentName, 10, usageType, cardinality);
    init();
  }

  public void init() {
    setChild(1, idNumber = new ST("ID Number", UsageType.R, 15));
    setChild(2, checkDigit = new ST("CheckDigit", UsageType.O));
    setChild(3, checkDigitScheme = new ID("Check Digit Scheme", UsageType.C, ValueSet.HL70061));
    checkDigitScheme.setConditionalPredicate(new IsValued(checkDigit, UsageType.O, UsageType.X));
    setChild(4, assigningAuthority = new HD("Assigning Authority", UsageType.R, ValueSet.HL70363));
    setChild(5, identifierTypeCode = new ID("Identifier Type Code", UsageType.R, ValueSet.HL70203));
    identifierTypeCode.setLength(2, 5);
    setChild(6, assigningFacility = new HD("Assigning Facility", UsageType.O));
    setChild(7, effectiveDate = new DT("Effective Date", UsageType.O));
    setChild(8, expirationDate = new DT("Expiration Date", UsageType.O));
    setChild(9, assigningJurisdiction = new CWE("Assigning Jurisdiction", UsageType.O));
    setChild(10, assigningAgencyOrDepartment = new CWE("Assigning Agency or Department", UsageType.O));
  }
}
