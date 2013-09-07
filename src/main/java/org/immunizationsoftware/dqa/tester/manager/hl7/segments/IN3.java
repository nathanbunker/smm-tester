package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.MSG;

public class IN3 extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new IN3(this);
  }
  
  public IN3(IN3 copy)
  {
    super(copy);
    init();
  }

  public IN3(UsageType usageType, Cardinality cardinality)
  {
    super (ItemType.SEGMENT, "IN3", "Insurance Additional Info - Cert Segment", "Insurance Additional Info - Cert Segment", 0, usageType, cardinality);
  }
  
  @Override
  public void init() {
    // Nothing to init 
  }
}
