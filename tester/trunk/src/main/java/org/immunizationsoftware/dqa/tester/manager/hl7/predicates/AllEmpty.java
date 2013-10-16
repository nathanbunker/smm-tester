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

  @Override
  public String printDocument() {
    if (components == null || components.length == 0) {
      return "";
    } else if (components.length == 1) {
      return "If " + components[0].getComponentReferenceShort() + " is not valued";
    } else {
      StringBuilder sb = new StringBuilder();
      sb.append("If ");
      boolean first = true;
      for (HL7Component c : components) {
        if (!first) {
          sb.append(" and ");
        }
        first = false;
        sb.append(c.getComponentNameSpecific());

      }
      sb.append(" are not valued");
    }
    return "";
  }
}
