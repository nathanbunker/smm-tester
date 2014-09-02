package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class FN extends HL7Component
{
  private ST surname = null;
  private ST ownSurnamePrefix = null;
  private ST ownSurname = null;
  private ST surnamePrefixFromPartnerSpouse = null;
  private ST surnameFromPartnerSpouse = null;

  public ST getSurname() {
    return surname;
  }

  public void setSurname(ST surname) {
    this.surname = surname;
  }

  public ST getOwnSurnamePrefix() {
    return ownSurnamePrefix;
  }

  public void setOwnSurnamePrefix(ST ownSurnamePrefix) {
    this.ownSurnamePrefix = ownSurnamePrefix;
  }

  public ST getOwnSurname() {
    return ownSurname;
  }

  public void setOwnSurname(ST ownSurname) {
    this.ownSurname = ownSurname;
  }

  public ST getSurnamePrefixFromPartnerSpouse() {
    return surnamePrefixFromPartnerSpouse;
  }

  public void setSurnamePrefixFromPartnerSpouse(ST surnamePrefixFromPartnerSpouse) {
    this.surnamePrefixFromPartnerSpouse = surnamePrefixFromPartnerSpouse;
  }

  public ST getSurnameFromPartnerSpouse() {
    return surnameFromPartnerSpouse;
  }

  public void setSurnameFromPartnerSpouse(ST surnameFromPartnerSpouse) {
    this.surnameFromPartnerSpouse = surnameFromPartnerSpouse;
  }

  public FN(String componentNameSpecific, UsageType usageType) {
    super(ItemType.DATATYPE, "FN", "Family Name", componentNameSpecific, 5, usageType);

    init();
  }

  @Override
  public HL7Component makeAnother() {
    return new FN(this);
  }

  public FN(FN copy) {
    super(copy);
    init();
  }

  public void init() {
    setChild(1, surname = new ST("Surname", UsageType.R, 50));
    setChild(2, ownSurnamePrefix = new ST("Own Surname Prefix", UsageType.O));
    setChild(3, ownSurname = new ST("Own Surname", UsageType.O));
    setChild(4, surnamePrefixFromPartnerSpouse = new ST("Surname Prefix From Patner/Spouse", UsageType.O));
    setChild(5, surnameFromPartnerSpouse = new ST("Surname From Partner/Spouse", UsageType.O));
  }
}
