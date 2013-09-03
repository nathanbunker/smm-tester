package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class RCP extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new RCP(this);
  }
  
  public RCP(RCP copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public RCP(UsageType usageType, Cardinality cardinality) {
    super("RCP", "Response Control Parameter Segment", "Response Control Parameter Segmetn", 0, usageType, cardinality);
  }

}
