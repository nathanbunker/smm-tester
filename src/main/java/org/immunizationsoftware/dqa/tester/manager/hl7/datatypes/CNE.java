package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class CNE extends HL7Component
{

  public CNE()
  {
    super(ItemType.DATATYPE, "CNE", "Coded with no exceptions", "Coded with no exceptions", 0);
  }
  
  public CNE(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "CNE", "Coded with no exceptions", componentName, 0, usageType);
  }
  
  public CNE(CNE cne)
  {
    super(cne);
  }
  @Override
  public void init() {

  }

  @Override
  public HL7Component makeAnother() {
    return new CNE(this);
  }

}
