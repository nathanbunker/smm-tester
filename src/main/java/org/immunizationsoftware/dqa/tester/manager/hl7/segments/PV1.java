package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class PV1 extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new PV1(this);
  }
  
  public PV1(PV1 copy)
  {
    super(copy);
    init();
  }

  public PV1(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "PV1", "Patient Visit Segment", 0, usageType, cardinality);
  }

  @Override
  public void init() {
    // Nothing to init  
  }
}
