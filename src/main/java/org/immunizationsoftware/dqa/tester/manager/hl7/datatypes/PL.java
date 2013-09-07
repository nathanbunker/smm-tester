package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class PL extends HL7Component
{

  public PL()
  {
    super(ItemType.DATATYPE, "PL", "Person location", "Person location", 0);
  }
  
  public PL(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "PL", "Person location", componentName, 0, usageType);
  }
  
  public PL(PL pl)
  {
    super(pl);
  }
  @Override
  public void init() {

  }

  @Override
  public HL7Component makeAnother() {
    return new PL(this);
  }

}
