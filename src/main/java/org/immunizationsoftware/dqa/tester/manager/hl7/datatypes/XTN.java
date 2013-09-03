package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsNotValuedAs;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValuedAs;

public class XTN extends HL7Component
{
  private ST telephoneNumber = null;
  private ID telecommunicationUseCode = null;
  private ID telecommunicationEquipmentType = null;
  private ST emailAddress = null;
  private NM countryCode = null;
  private NM areaCityCode = null;
  private NM localNumber = null;
  private NM extension = null;
  private ST anyText = null;
  private ST extensionPrefix = null;
  private ST speedDialCode = null;
  private ST unformattedTelephoneNumber = null;
  
  public ST getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(ST telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public ID getTelecommunicationUseCode() {
    return telecommunicationUseCode;
  }

  public void setTelecommunicationUseCode(ID telecommunicationUseCode) {
    this.telecommunicationUseCode = telecommunicationUseCode;
  }

  public ID getTelecommunicationEquipmentType() {
    return telecommunicationEquipmentType;
  }

  public void setTelecommunicationEquipmentType(ID telecommunicationEquipmentType) {
    this.telecommunicationEquipmentType = telecommunicationEquipmentType;
  }

  public ST getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(ST emailAddress) {
    this.emailAddress = emailAddress;
  }

  public NM getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(NM countryCode) {
    this.countryCode = countryCode;
  }

  public NM getAreaCityCode() {
    return areaCityCode;
  }

  public void setAreaCityCode(NM areaCityCode) {
    this.areaCityCode = areaCityCode;
  }

  public NM getLocalNumber() {
    return localNumber;
  }

  public void setLocalNumber(NM localNumber) {
    this.localNumber = localNumber;
  }

  public NM getExtension() {
    return extension;
  }

  public void setExtension(NM extension) {
    this.extension = extension;
  }

  public ST getAnyText() {
    return anyText;
  }

  public void setAnyText(ST anyText) {
    this.anyText = anyText;
  }

  public ST getExtensionPrefix() {
    return extensionPrefix;
  }

  public void setExtensionPrefix(ST extensionPrefix) {
    this.extensionPrefix = extensionPrefix;
  }

  public ST getSpeedDialCode() {
    return speedDialCode;
  }

  public void setSpeedDialCode(ST speedDialCode) {
    this.speedDialCode = speedDialCode;
  }

  public ST getUnformattedTelephoneNumber() {
    return unformattedTelephoneNumber;
  }

  public void setUnformattedTelephoneNumber(ST unformattedTelephoneNumber) {
    this.unformattedTelephoneNumber = unformattedTelephoneNumber;
  }
  
  @Override
  public HL7Component makeAnother() {
    return new XTN(this);
  }
  
  public XTN(XTN copy)
  {
    super(copy);
    init();
  }

  public XTN(String componentName, UsageType usageType) {
    super("XTN", "Extended Telecommunication Number", componentName, 12, usageType);
    
    init();
  }

  public void init() {
    setChild(1 , telephoneNumber = new ST("Telephone Number", UsageType.X));
    setChild(2 , telecommunicationUseCode = new ID("Telecommunication Use Code", UsageType.R, ValueSet.HL70201));
    setChild(3 , telecommunicationEquipmentType = new ID("Telecommunication Equipment Type", UsageType.RE, ValueSet.HL70202));
    setChild(4 , emailAddress = new ST("Email Address", UsageType.C, 199));
    emailAddress.setConditionalPredicate(new IsValuedAs(telecommunicationUseCode, "NET", UsageType.R, UsageType.X));
    setChild(5 , countryCode = new NM("Country Code", UsageType.O));
    setChild(6 , areaCityCode = new NM("Area/City Code", UsageType.C, 5));
    areaCityCode.setConditionalPredicate(new IsNotValuedAs(telecommunicationUseCode, "NET", UsageType.RE, UsageType.X));
    setChild(7 , localNumber = new NM("Local Number", UsageType.C, 9));
    localNumber.setConditionalPredicate(new IsNotValuedAs(telecommunicationUseCode, "NET", UsageType.R, UsageType.X));
    setChild(8 , extension = new NM("Extension", UsageType.O));
    setChild(9 , anyText = new ST("Any Text", UsageType.O));
    setChild(10,  extensionPrefix = new ST("Extension Prefix", UsageType.O));
    setChild(11,  speedDialCode = new ST("Speed Dial Code", UsageType.O));
    setChild(12,  unformattedTelephoneNumber = new ST("Unformatted Telphone Number", UsageType.O));
  }
}
