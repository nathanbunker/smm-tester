package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class EIP extends HL7Component
{

  public EIP()
  {
    super(ItemType.DATATYPE, "EIP", "Entity identifier pair", "Entity identifier pair", 0);
  }
  
  public EIP(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "EIP", "Entity identifier pair", componentName, 0, usageType);
  }
  
  public EIP(EIP eip)
  {
    super(eip);
  }
  @Override
  public void init() {

  }

  @Override
  public HL7Component makeAnother() {
    return new EIP(this);
  }

}
