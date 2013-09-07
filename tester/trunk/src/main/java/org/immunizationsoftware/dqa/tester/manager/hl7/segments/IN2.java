package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.MSG;

public class IN2 extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new IN2(this);
  }
  
  public IN2(IN2 copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public IN2(UsageType usageType, Cardinality cardinality)
  {
    super (ItemType.SEGMENT, "IN2", "Insurance Additional Info Segment", "Insurance Additional Info Segment", 0, usageType, cardinality);
  }
  
}
