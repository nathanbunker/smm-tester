package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class NTE extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new NTE(this);
  }
  
  public NTE(NTE copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public NTE(UsageType usageType, Cardinality cardinality) {
    super("NTE", "Note Segment", "Note Segment", 0, usageType, cardinality);
  }
}
