package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class TX extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new TX(this);
  }
  
  public TX(TX copy)
  {
    super(copy);
    init();
  }

  public TX(String componentName, UsageType usageType)
  {
    super("TX", "text data", componentName, 0, usageType);
  }
  
  @Override
  public void init() {
    // Nothing to init
  }
}
