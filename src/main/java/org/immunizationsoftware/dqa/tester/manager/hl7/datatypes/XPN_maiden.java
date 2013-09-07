package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;

public class XPN_maiden extends HL7Component
{
  
  private FN familyName = null;
  private ST givenName = null;
  private ST secondAndFurtherGivenNamesOrInitialsTherof = null;
  private ST suffix = null;
  private ST prefix = null;
  private IS degree = null;
  private ID nameTypeCode = null;
  private ID nameRepresentationCode = null;
  private CE nameContext = null;
  private DR nameValidityRange = null;
  private ID nameAssemblyOrder = null;
  private TS effectiveDate = null;
  private TS expirationDate = null;
  private ST professionalSuffix = null;
  
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

  public ID getNameTypeCode() {
    return nameTypeCode;
  }

  public void setNameTypeCode(ID nameTypeCode) {
    this.nameTypeCode = nameTypeCode;
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
  
  @Override
  public HL7Component makeAnother() {
    return new XPN_maiden(this);
  }
  
  public XPN_maiden(XPN_maiden copy)
  {
    super(copy);
    init();
  }

  public XPN_maiden(String componentName, UsageType usageType) {
    super(ItemType.DATATYPE, "XPN", "Extended Person Name - Maiden Name Only", componentName, 14, usageType);
    init();
  }

  public XPN_maiden(String componentName, UsageType usageType, Cardinality cardinality) {
    super(ItemType.DATATYPE, "XPN", "Extended Person Name- Maiden Name Only", componentName, 14, usageType, cardinality);
    init();
  }

  public XPN_maiden(String componentName, UsageType usageType, int lengthMax) {
    super(ItemType.DATATYPE, "XPN", "Extended Person Name- Maiden Name Only", componentName, 14, usageType, lengthMax);
    init();
  }

  public void init() {
    setChild(1, familyName = new FN("Family Name", UsageType.R));
    setChild(2, givenName = new ST("Given Name", UsageType.O, 30));
    setChild(3, secondAndFurtherGivenNamesOrInitialsTherof = new ST("Second and Further Given Names or Intials Therof", UsageType.O, 30));
    setChild(4, suffix = new ST("Suffix", UsageType.O));
    setChild(5, prefix = new ST("Prefix", UsageType.O));
    setChild(6, degree = new IS("Degree", UsageType.X));
    setChild(7, nameTypeCode = new ID("Name Type Code", UsageType.RE, 1, ValueSet.HL70200));
    setChild(8, nameRepresentationCode = new ID("Name Representation Code", UsageType.O));
    setChild(9, nameContext = new CE("Name Context", UsageType.O));
    setChild(10, nameValidityRange = new DR("Name Validity Range", UsageType.X));
    setChild(11, nameAssemblyOrder = new ID("Name Assembly Order", UsageType.O));
    setChild(12, effectiveDate = new TS("Effective Date", UsageType.O));
    setChild(13, expirationDate = new TS("Expiration Date", UsageType.O));
    setChild(14, professionalSuffix = new ST("Professional Suffix", UsageType.O));
  }
}
