package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class IN1 extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new IN1(this);
  }
  
  public IN1(IN1 copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public IN1(UsageType usageType, Cardinality cardinality)
  {
    super ("IN1", "Insurance Segment", "Insurance Segment", 0, usageType, cardinality);
  }
}
