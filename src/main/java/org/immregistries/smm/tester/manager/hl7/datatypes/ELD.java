package org.immregistries.smm.tester.manager.hl7.datatypes;

import org.immregistries.smm.tester.manager.hl7.HL7Component;
import org.immregistries.smm.tester.manager.hl7.ItemType;
import org.immregistries.smm.tester.manager.hl7.UsageType;

public class ELD extends HL7Component
{
  public ELD(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "ELD", "Error location and description", componentName, 0, usageType);
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
