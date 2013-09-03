package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class PV2 extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new PV2(this);
  }
  
  public PV2(PV2 copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public PV2(UsageType usageType, Cardinality cardinality) {
    super("PV2", "Patient Visit - Additional Info Segment", "Patient Visit - Additional Info Segment 2", 0, usageType, cardinality);
  }

}
