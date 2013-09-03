package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class RXR extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new RXR(this);
  }
  
  public RXR(RXR copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public RXR(UsageType usageType, Cardinality cardinality) {
    super("RXR", "Pharmacy/Treatment Route Segment", "Pharmacy/Treatment Route Segment", 0, usageType, cardinality);
  }

}
