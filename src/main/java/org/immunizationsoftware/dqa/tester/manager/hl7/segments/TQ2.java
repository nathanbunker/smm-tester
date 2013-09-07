package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class TQ2 extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new TQ2(this);
  }
  
  public TQ2(TQ2 copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

 public TQ2(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "TQ2", "Timing/Quantity Relationship Segment", 0, usageType, cardinality);
  }

}
