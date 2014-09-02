package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class GT1 extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new GT1(this);
  }
  
  public GT1(GT1 copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public GT1(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "GT1", "Guarantor Segment", "Guarantor Segment", 0, usageType, cardinality);
  }

}
