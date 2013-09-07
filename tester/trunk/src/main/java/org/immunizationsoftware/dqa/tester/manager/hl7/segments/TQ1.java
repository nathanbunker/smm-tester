package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class TQ1 extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new TQ1(this);
  }
  
  public TQ1(TQ1 copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public TQ1(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "TQ1", "Timing/Quantity Segment", 0, usageType, cardinality);
  }

}
