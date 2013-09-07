package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class SAD extends HL7Component
{

  private ST streetOrMailingAddress = null;
  private ST streetName = null;
  private ST dwellingNumber = null;

  public ST getStreetOrMailingAddress() {
    return streetOrMailingAddress;
  }

  public void setStreetOrMailingAddress(ST streetOrMailingAddress) {
    this.streetOrMailingAddress = streetOrMailingAddress;
  }

  public ST getStreetName() {
    return streetName;
  }

  public void setStreetName(ST streetName) {
    this.streetName = streetName;
  }

  public ST getDwellingNumber() {
    return dwellingNumber;
  }

  public void setDwellingNumber(ST dwellingNumber) {
    this.dwellingNumber = dwellingNumber;
  }

  @Override
  public HL7Component makeAnother() {
    return new SAD(this);
  }
  
  public SAD(SAD copy)
  {
    super(copy);
    init();
  }

  public SAD(String componentName, UsageType usageType) {
    this(componentName, usageType, Integer.MAX_VALUE);
  }

  public SAD(String componentName, UsageType usageType, int lengthMax) {
    super(ItemType.DATATYPE, "SAD", "Street Address", componentName, 3, usageType, lengthMax);
    init();
  }

  public void init() {
    setChild(1, streetOrMailingAddress = new ST("Street or Mailing Address", UsageType.R, 120));
    setChild(2, streetName = new ST("Street Name", UsageType.O));
    setChild(3, dwellingNumber = new ST("Dwelling Number", UsageType.O));
  }
}
