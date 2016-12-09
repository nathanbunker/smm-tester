package org.immregistries.smm.tester.manager.hl7.datatypes;

import org.immregistries.smm.tester.manager.hl7.Cardinality;
import org.immregistries.smm.tester.manager.hl7.HL7Component;
import org.immregistries.smm.tester.manager.hl7.ItemType;
import org.immregistries.smm.tester.manager.hl7.UsageType;

public abstract class DynamicComponent extends HL7Component
{

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

  public DynamicComponent(String componentName, UsageType usageType, Cardinality cardinality) {
    super(ItemType.DATATYPE, "", "Dynamic Component", componentName, 0, usageType, cardinality);
  }

  @Override
  public HL7Component makeAnother() {
    return null;
  }

  public abstract HL7Component makeComponent();

}
