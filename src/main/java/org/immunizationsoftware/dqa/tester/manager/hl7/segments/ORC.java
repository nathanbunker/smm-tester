package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class ORC extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new ORC(this);
  }
  
  public ORC(ORC copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public ORC(UsageType usageType, Cardinality cardinality) {
    super("ORC", "Order Request Segment", "Order Request Segment", 0, usageType, cardinality);
  }

}
