package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class RXA extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new RXA(this);
  }
  
  public RXA(RXA copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public RXA(UsageType usageType, Cardinality cardinality) {
    super("RXA", "Pharmacy/Treatment Adminsitration Segment", "Pharmacy/Treatment Administration Segment", 0,
        usageType, cardinality);
  }

}
