package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class QPD extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new QPD(this);
  }
  
  public QPD(QPD copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public QPD(UsageType usageType, Cardinality cardinality) {
    super("QPD", "Query Parameter Definition Segment", "Query Parameter Definition Segment", 0, usageType, cardinality);
  }

}
