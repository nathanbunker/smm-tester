package org.immunizationsoftware.dqa.tester.manager.hl7.predicates;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class AllEmpty extends ConditionalPredicate
{

  private HL7Component[] components = null;

  public AllEmpty(HL7Component[] components, UsageType usageTypeMet, UsageType usageTypeNotMet) {
    super(components.length > 0 ? components[0] : null, usageTypeMet, usageTypeNotMet);
    this.components = components;
  }

  @Override
  public boolean isMet() {
    for (HL7Component c : components) {
      if (c.getValue() != null && !c.getValue().equals("")) {
        return false;
      }
    }
    return true;
  }
}
