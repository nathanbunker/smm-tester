package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class ELD extends HL7Component
{
  public ELD(String componentName, UsageType usageType)
  {
    super("ELD", "Error location and description", componentName, 0, usageType);
  }
  
  @Override
  public void init() {
    // Nothing to do
  }
  
  @Override
  public HL7Component makeAnother() {
    return new ELD(this);
  }
  
  public ELD(ELD copy)
  {
    super(copy);
    init();
  }


}
