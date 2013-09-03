package org.immunizationsoftware.dqa.tester.manager.hl7.predicates;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class IsNotValuedAs extends ConditionalPredicate
{

  private String value = "";

  public IsNotValuedAs(HL7Component component, String value, UsageType usageTypeMet, UsageType usageTypeNotMet) {
    super(component, usageTypeMet, usageTypeNotMet);
    this.value = value;
  }

  @Override
  public boolean isMet() {
    return component.getValue() == null || !component.getValue().equalsIgnoreCase(value);
  }

}
