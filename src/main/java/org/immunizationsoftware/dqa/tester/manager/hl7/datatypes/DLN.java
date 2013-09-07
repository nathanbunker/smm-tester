package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class DLN extends HL7Component
{

  public DLN()
  {
    super(ItemType.DATATYPE, "DLN", "Driver's license number", "Driver's license number", 0);
  }
  
  public DLN(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "DLN", "Driver's license number", componentName, 0, usageType);
  }
  
  public DLN(DLN dln)
  {
    super(dln);
  }
  @Override
  public void init() {

  }

  @Override
  public HL7Component makeAnother() {
    return new DLN(this);
  }

}
