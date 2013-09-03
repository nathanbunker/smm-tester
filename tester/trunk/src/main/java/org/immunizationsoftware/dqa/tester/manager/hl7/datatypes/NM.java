package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class NM extends HL7Component
{
  
  
  public NM(String componentName)
  {
    super("NM", "Numeric", componentName, 0);
    setLengthMax(16);
  }
  
  public NM(String componentName, UsageType usageType)
  {
    super("NM", "Numeric", componentName, 0, usageType);
  }
  
  @Override
  public HL7Component makeAnother() {
    return new NM(this);
  }
  
  public NM(NM copy)
  {
    super(copy);
    init();
  }

  
  public NM(String componentName, UsageType usageType, int lengthMax)
  {
    super("NM", "Numeric", componentName, 0, usageType, lengthMax);
  }
  
  @Override
  public void init() {
    // Nothing to init 
  }
}
