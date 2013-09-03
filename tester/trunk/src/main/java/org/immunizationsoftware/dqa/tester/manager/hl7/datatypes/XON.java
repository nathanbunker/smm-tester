package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsNotValued;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class XON extends HL7Component
{
  private ST organizationName = null;
  private IS organizationNameTypeCode = null;
  private ST idNumber = null;
  private ST checkDigit = null;
  private ID checkDigitScheme = null;
  private HD assigningAuthority = null;
  private ID identifierTypeCode = null;
  private HD assigningFacility = null;
  private ID nameRepresentationCode = null;
  private ST organizationIdentifier = null;

  public ST getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(ST organizationName) {
    this.organizationName = organizationName;
  }

  public IS getOrganizationNameTypeCode() {
    return organizationNameTypeCode;
  }

  public void setOrganizationNameTypeCode(IS organizationNameTypeCode) {
    this.organizationNameTypeCode = organizationNameTypeCode;
  }

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

  public ST getOrganizationIdentifier() {
    return organizationIdentifier;
  }

  public void setOrganizationIdentifier(ST organizationIdentifier) {
    this.organizationIdentifier = organizationIdentifier;
  }

  @Override
  public HL7Component makeAnother() {
    return new XON(this);
  }
  
  public XON(XON copy)
  {
    super(copy);
    init();
  }

  public XON(String componentName, UsageType usageType, int lengthMax) {
    super("XON", "Extended Composite ID Number and Name for Organizations", componentName, 10, usageType, lengthMax);

    init();
  }

  public void init() {
    setChild(1, organizationName = new ST("Organization Name", UsageType.RE, 50));
    setChild(2, organizationNameTypeCode = new IS("Organization Name Type Code", UsageType.O));
    setChild(3, idNumber = new ST("ID Number", UsageType.X));
    setChild(4, checkDigit = new ST("Check Digit", UsageType.O));
    setChild(5, checkDigitScheme = new ID("Check Digit Scheme", UsageType.O));
    setChild(6, assigningAuthority = new HD("Assigning Authority", UsageType.C));
    setChild(7, identifierTypeCode = new ID("Identifier Type Code", UsageType.C, ValueSet.HL70203));
    identifierTypeCode.setLength(2, 5);
    setChild(8, assigningFacility = new HD("Assigning Facility", UsageType.O));
    setChild(9, nameRepresentationCode = new ID("Name Representation Code", UsageType.O));
    setChild(10, organizationIdentifier = new ST("Organization Identifier", UsageType.C, 20));
    organizationIdentifier.setConditionalPredicate(new IsNotValued(organizationName, UsageType.R, UsageType.RE));
    identifierTypeCode.setConditionalPredicate(new IsValued(organizationIdentifier, UsageType.R, UsageType.O));
    assigningAuthority.setConditionalPredicate(new IsValued(organizationIdentifier, UsageType.R, UsageType.X));
  }
}
