package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class SRT extends HL7Component
{

  public SRT()
  {
    super(ItemType.DATATYPE, "SRT", "Sort order", "Sort order", 0);
  }
  
  public SRT(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "SRT", "Sort order", componentName, 0, usageType);
  }
  
  public SRT(SRT srt)
  {
    super(srt);
  }
  @Override
  public void init() {

  }

  @Override
  public HL7Component makeAnother() {
    return new SRT(this);
  }

}
