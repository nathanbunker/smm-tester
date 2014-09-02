package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class TQ extends HL7Component
{

  public TQ()
  {
    super(ItemType.DATATYPE, "TQ", "Timing/quantity", "Timing/quantity", 0);
  }
  
  public TQ(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "TQ", "Timing/quantity", componentName, 0, usageType);
  }
  
  public TQ(TQ tq)
  {
    super(tq);
  }
  @Override
  public void init() {

  }

  @Override
  public HL7Component makeAnother() {
    return new TQ(this);
  }

}
