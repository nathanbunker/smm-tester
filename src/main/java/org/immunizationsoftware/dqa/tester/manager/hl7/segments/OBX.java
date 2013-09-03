package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class OBX extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new OBX(this);
  }
  
  public OBX(OBX copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public OBX(UsageType usageType, Cardinality cardinality) {
    super("OBX", "Observation Result Segment", "Observation Result Segment", 0, usageType, cardinality);
  }

}
