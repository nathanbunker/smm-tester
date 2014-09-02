package org.immunizationsoftware.dqa.tester.manager.hl7.predicates;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class IsNotValued extends ConditionalPredicate
{

  public IsNotValued(HL7Component component, UsageType usageTypeMet, UsageType usageTypeNotMet) {
    super(component, usageTypeMet, usageTypeNotMet);
  }

  @Override
  public boolean isMet() {
    return component.getValue() == null || component.getValue().equals("");
  }

  @Override
  public String printDocument() {
    if (component != null) {
      return "If " + component.getComponentReferenceShort() + " is not valued";
    }
    return "";
  }
}
