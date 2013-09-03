package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class VID extends HL7Component
{
  
  private ID versionID = null;
  private CE internationalizationCode  = null;
  private CE internationalVersionID = null;
  
  public ID getVersionID() {
    return versionID;
  }

  public void setVersionID(ID versionID) {
    this.versionID = versionID;
  }

  public CE getInternationalizationCode() {
    return internationalizationCode;
  }

  public void setInternationalizationCode(CE internationalizationCode) {
    this.internationalizationCode = internationalizationCode;
  }

  public CE getInternationalVersionID() {
    return internationalVersionID;
  }

  public void setInternationalVersionID(CE internationalVersionID) {
    this.internationalVersionID = internationalVersionID;
  }

  @Override
  public HL7Component makeAnother() {
    return new VID(this);
  }
  
  public VID(VID copy)
  {
    super(copy);
    init();
  }

  public VID(String componentName, UsageType usageType, Cardinality cardinality)
  {
    super("VID", "Version ID", componentName, 3, usageType, cardinality);
    
    init();
  }

  public void init() {
    setChild(1, versionID = new ID("Version Id", UsageType.R));
    versionID.setLength(5, 5);
    setChild(2, internationalizationCode = new CE("Internationalization Code", UsageType.O, null));
    setChild(3, internationalVersionID = new CE("International Version ID", UsageType.O, null));
  }
}
