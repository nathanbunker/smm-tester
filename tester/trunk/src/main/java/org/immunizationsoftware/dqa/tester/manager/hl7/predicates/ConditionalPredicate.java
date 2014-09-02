package org.immunizationsoftware.dqa.tester.manager.hl7.predicates;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public abstract class ConditionalPredicate
{
  protected HL7Component component = null;
  private UsageType usageTypeMet = UsageType.O;
  private UsageType usageTypeNotMet = UsageType.O;

  public UsageType getUsageTypeMet() {
    return usageTypeMet;
  }

  public void setUsageTypeMet(UsageType usageTypeMet) {
    this.usageTypeMet = usageTypeMet;
  }

  public UsageType getUsageTypeNotMet() {
    return usageTypeNotMet;
  }

  public void setUsageTypeNotMet(UsageType usageTypeNotMet) {
    this.usageTypeNotMet = usageTypeNotMet;
  }

  public ConditionalPredicate(HL7Component hl7Component, UsageType usageTypeMet, UsageType usageTypeNotMet)
  {
    this.component = hl7Component;
    this.usageTypeMet = usageTypeMet;
    this.usageTypeNotMet = usageTypeNotMet;
  }
  
  public abstract boolean isMet();
  
  public abstract String printDocument();
}
