package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class XAD extends HL7Component
{
  private SAD streetAddress = null;
  private ST otherDesignation = null;
  private ST city = null;
  private ST stateOrProvince = null;
  private ST zipOrPostalCode = null;
  private ID country = null;
  private ID addressType = null;
  private ST otherGeographicDesignation = null;
  private IS countyParishCode = null;
  private IS censusTract = null;
  private ID addressRepresentationCode = null;
  private DR addressValidityRange = null;
  private TS effectiveDate = null;
  private TS expirationDate = null;
  
  public IS getCensusTract() {
    return censusTract;
  }
  
  public void setCensusTract(IS censusTract) {
    this.censusTract = censusTract;
  }
  
  public SAD getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(SAD streetAddress) {
    this.streetAddress = streetAddress;
  }

  public ST getOtherDesignation() {
    return otherDesignation;
  }

  public void setOtherDesignation(ST otherDesignation) {
    this.otherDesignation = otherDesignation;
  }

  public ST getCity() {
    return city;
  }

  public void setCity(ST city) {
    this.city = city;
  }

  public ST getStateOrProvince() {
    return stateOrProvince;
  }

  public void setStateOrProvince(ST stateOrProvince) {
    this.stateOrProvince = stateOrProvince;
  }

  public ST getZipOrPostalCode() {
    return zipOrPostalCode;
  }

  public void setZipOrPostalCode(ST zipOrPostalCode) {
    this.zipOrPostalCode = zipOrPostalCode;
  }

  public ID getCountry() {
    return country;
  }

  public void setCountry(ID country) {
    this.country = country;
  }

  public ID getAddressType() {
    return addressType;
  }

  public void setAddressType(ID addressType) {
    this.addressType = addressType;
  }

  public ST getOtherGeographicDesignation() {
    return otherGeographicDesignation;
  }

  public void setOtherGeographicDesignation(ST otherGeographicDesignation) {
    this.otherGeographicDesignation = otherGeographicDesignation;
  }

  public IS getCountyParishCode() {
    return countyParishCode;
  }

  public void setCountyParishCode(IS countyParishCode) {
    this.countyParishCode = countyParishCode;
  }

  public ID getAddressRepresentationCode() {
    return addressRepresentationCode;
  }

  public void setAddressRepresentationCode(ID addressRepresentationCode) {
    this.addressRepresentationCode = addressRepresentationCode;
  }

  public DR getAddressValidityRange() {
    return addressValidityRange;
  }

  public void setAddressValidityRange(DR addressValidityRange) {
    this.addressValidityRange = addressValidityRange;
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

  @Override
  public HL7Component makeAnother() {
    return new XAD(this);
  }
  
  public XAD(XAD copy)
  {
    super(copy);
    init();
  }

  public XAD(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "XAD", "Extended Address", componentName, 14, usageType);
    init();
  }

  public XAD(String componentName, UsageType usageType, Cardinality cardinality)
  {
    super(ItemType.DATATYPE, "XAD", "Extended Address", componentName, 14, usageType, cardinality);
    init();
  }

  public XAD(String componentName, UsageType usageType, int lengthMax)
  {
    super(ItemType.DATATYPE, "XAD", "Extended Address", componentName, 14, usageType, lengthMax);
    init();
  }

  public void init() {
    setChild(1, streetAddress = new SAD("Street Address", UsageType.RE));
    setChild(2, otherDesignation = new ST("Other Designation", UsageType.RE, 120));
    setChild(3, city = new ST("City", UsageType.RE, 50));
    setChild(4, stateOrProvince = new ST("State or Province", UsageType.RE, 50));
    setChild(5, zipOrPostalCode = new ST("Zip or Postal Code", UsageType.RE, 12));
    setChild(6, country = new ID("Country", UsageType.RE, ValueSet.HL70399));
    country.setLength(3, 3);
    country.setDefaultValueWhenBlank("USA");
    country.setDefaultValueWhenBlankPredicate(new IsValued(stateOrProvince));
    setChild(7, addressType = new ID("Address Type", UsageType.R, 3, ValueSet.HL70190));
    setChild(8, otherGeographicDesignation = new ST("Other Geographic Designation", UsageType.O));
    setChild(9, countyParishCode = new IS("County/Parish Code", UsageType.O));
    setChild(10, censusTract = new IS("Census Tract", UsageType.O));
    setChild(11, addressRepresentationCode = new ID("Address Representation Code", UsageType.O));
    setChild(12, addressValidityRange = new DR("Address Validity Range", UsageType.X));
    setChild(13, effectiveDate = new TS("Effective Date", UsageType.O));
    setChild(14, expirationDate = new TS("Expiration Date", UsageType.O));
  }
}
