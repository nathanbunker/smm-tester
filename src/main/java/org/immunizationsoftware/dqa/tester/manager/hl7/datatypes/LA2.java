package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class LA2 extends HL7Component
{
  private IS pointOfCare = null;
  private IS room = null;
  private IS bed = null;
  private HD facility = null;
  private IS locationStatus = null;
  private IS patientLocationType = null;
  private IS building = null;
  private IS floor = null;
  private ST streetAddress = null;
  private ST otherDesignation = null;
  private ST city = null;
  private ST stateOrProvince = null;
  private ST zipOrPostalCode = null;
  private ID country = null;
  private ID addressType = null;
  private ST otherGeographicDesignation = null;

  public IS getPointOfCare() {
    return pointOfCare;
  }

  public void setPointOfCare(IS pointOfCare) {
    this.pointOfCare = pointOfCare;
  }

  public IS getRoom() {
    return room;
  }

  public void setRoom(IS room) {
    this.room = room;
  }

  public IS getBed() {
    return bed;
  }

  public void setBed(IS bed) {
    this.bed = bed;
  }

  public HD getFacility() {
    return facility;
  }

  public void setFacility(HD facility) {
    this.facility = facility;
  }

  public IS getLocationStatus() {
    return locationStatus;
  }

  public void setLocationStatus(IS locationStatus) {
    this.locationStatus = locationStatus;
  }

  public IS getPatientLocationType() {
    return patientLocationType;
  }

  public void setPatientLocationType(IS patientLocationType) {
    this.patientLocationType = patientLocationType;
  }

  public IS getBuilding() {
    return building;
  }

  public void setBuilding(IS building) {
    this.building = building;
  }

  public IS getFloor() {
    return floor;
  }

  public void setFloor(IS floor) {
    this.floor = floor;
  }

  public ST getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(ST streetAddress) {
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

  @Override
  public HL7Component makeAnother() {
    return new LA2(this);
  }
  
  public LA2(LA2 copy)
  {
    super(copy);
    init();
  }

  public LA2(String componentNameSpecific, UsageType usageType) {
    super(ItemType.DATATYPE, "LA2", "Location with Address Variation 2", componentNameSpecific, 16, usageType);
    init();
  }

  public LA2(String componentNameSpecific, UsageType usageType, Cardinality cardinality) {
    super(ItemType.DATATYPE, "LA2", "Location with Address Variation 2", componentNameSpecific, 16, usageType, cardinality);
    init();
  }

  public void init() {
    setChild(1, pointOfCare = new IS("Point of Care", UsageType.O));
    setChild(2, room = new IS("Room", UsageType.O));
    setChild(3, bed = new IS("Bed", UsageType.O));
    setChild(4, facility = new HD("Facility", UsageType.R));
    setChild(5, locationStatus = new IS("Location Status", UsageType.O));
    setChild(6, patientLocationType = new IS("Patient Location Type", UsageType.O));
    setChild(7, building = new IS("Building", UsageType.O));
    setChild(8, floor = new IS("Floor", UsageType.O));
    setChild(9, streetAddress = new ST("Street Address", UsageType.O));
    setChild(10, otherDesignation = new ST("Other Designation", UsageType.O));
    setChild(11, city = new ST("City", UsageType.O));
    setChild(12, stateOrProvince = new ST("State or Province", UsageType.O));
    setChild(13, zipOrPostalCode = new ST("Zip or Postal Code", UsageType.O));
    setChild(14, country = new ID("Country", UsageType.O));
    setChild(15, addressType = new ID("AddressType", UsageType.O));
    setChild(16, otherGeographicDesignation = new ST("Other Geographic Designation", UsageType.O));
  }
}
