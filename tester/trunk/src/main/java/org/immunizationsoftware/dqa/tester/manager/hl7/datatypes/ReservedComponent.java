package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class ReservedComponent extends HL7Component
{

  public ReservedComponent(String componentName, UsageType usageType) {
    super(ItemType.DATATYPE, "", "Reserved Component", componentName, 0, usageType);
  }

  public ReservedComponent(ReservedComponent copy) {
    super(copy);
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

  @Override
  public HL7Component makeAnother() {
    // TODO Auto-generated method stub
    return new ReservedComponent(this);
  }

}
