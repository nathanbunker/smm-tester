package org.immregistries.smm.tester.manager.hl7.segments;

import org.immregistries.smm.tester.manager.hl7.Cardinality;
import org.immregistries.smm.tester.manager.hl7.HL7Component;
import org.immregistries.smm.tester.manager.hl7.ItemType;
import org.immregistries.smm.tester.manager.hl7.UsageType;

public class SFT extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new SFT(this);
  }
  
  public SFT(SFT copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public SFT(UsageType usageType, Cardinality cardinality)
  {
    super(ItemType.SEGMENT, "SFT", "Software Segment", "Software Segment", 0, usageType, cardinality);
  }
}
