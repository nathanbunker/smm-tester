package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class PD1 extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new PD1(this);
  }
  
  public PD1(PD1 copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public PD1(UsageType usageType, Cardinality cardinality) {
    super("PD1", "Patient Demographic Segment", "Patient Demographic Segment", 0, usageType, cardinality);
  }
}
