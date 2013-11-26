package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class DSC extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new DSC(this);
  }
  
  public DSC(DSC copy)
  {
    super(copy);
    init();
  }

  public DSC(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "DSC", "Continuation Pointer", "Continuation Pointer", 0, usageType, cardinality);
    init();
  }
  
  @Override
  public void init() {
    // Nothing to init  
  }

}
