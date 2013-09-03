package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class QAK extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new QAK(this);
  }
  
  public QAK(QAK copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public QAK(UsageType usageType, Cardinality cardinality) {
    super("QAK", "Query Acknowledgement Segment", "Query Acknowledgement Segment", 0, usageType, cardinality);
  }

}
