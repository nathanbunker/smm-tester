package org.immunizationsoftware.dqa.tester.manager.hl7.predicates;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class IsValued extends ConditionalPredicate
{
  
  public IsValued(HL7Component component, UsageType usageTypeMet, UsageType usageTypeNotMet) {
    super(component, usageTypeMet, usageTypeNotMet);
    if (component == null)
    {
      throw new IllegalArgumentException("HL7 Component must not be null");
    }
  }

  public IsValued(HL7Component component) {
    super(component, UsageType.O, UsageType.O);
    if (component == null)
    {
      throw new IllegalArgumentException("HL7 Component must not be null");
    }
  }

  @Override
  public boolean isMet() {
    return component.getValue() != null && !component.getValue().equals("");
  }

}
