package org.immunizationsoftware.dqa.tester.manager.hl7.predicates;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class IsValuedAs extends ConditionalPredicate
{

  private String[] values;

  public IsValuedAs(HL7Component component, String value) {
    this(component, value, UsageType.O, UsageType.O);
    if (component == null) {
      throw new IllegalArgumentException("Component is required");
    }
  }

  public IsValuedAs(HL7Component component, String value, UsageType usageTypeMet, UsageType usageTypeNotMet) {
    super(component, usageTypeMet, usageTypeNotMet);
    values = new String[1];
    if (component == null) {
      throw new IllegalArgumentException("Component is required");
    }
    values[0] = value;
  }

  public IsValuedAs(HL7Component component, String[] values, UsageType usageTypeMet, UsageType usageTypeNotMet) {
    super(component, usageTypeMet, usageTypeNotMet);
    if (component == null) {
      throw new IllegalArgumentException("Component is required");
    }
    this.values = values;
  }

  @Override
  public boolean isMet() {
    if (component.getValue() == null) {
      return false;
    }
    for (String value : values) {
      if (component.getValue().equalsIgnoreCase(value)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String printDocument() {
    if (values != null && values.length > 0 && component != null) {
      if (values.length == 1) {
        return "If " + component.getComponentReferenceShort() + " is valued as \"" + values[0] + "\"";
      }
      StringBuilder sb = new StringBuilder();
      sb.append("If " + component.getComponentReferenceShort() + " is valued as ");
      boolean first = true;
      for (String value : values) {
        if (!first) {
          sb.append(" or ");
        }
        first = false;
        sb.append("\"" + value + "\"");
      }
      return sb.toString();
    }
    return "";
  }

}
