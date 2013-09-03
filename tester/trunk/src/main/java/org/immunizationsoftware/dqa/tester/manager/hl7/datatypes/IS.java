package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;

public class IS extends HL7Component
{
  public IS(String componentName, ValueSet valueSet) {
    super("IS", "coded value for user-defined tables", componentName, 0);
    this.valueSet = valueSet;
    setLengthMax(20);
  }

  public IS(String componentName, UsageType usageType, ValueSet valueSet) {
    super("IS", "coded value for user-defined tables", componentName, 0, usageType);
    this.valueSet = valueSet;
    setLengthMax(20);
  }

  public IS(String componentName, UsageType usageType, int lengthMax, ValueSet valueSet) {
    super("IS", "coded value for user-defined tables", componentName, 0, usageType, lengthMax);
    this.valueSet = valueSet;
  }

  public IS(String componentName, UsageType usageType) {
    super("IS", "coded value for user-defined tables", componentName, 0, usageType, Integer.MAX_VALUE);
    setLengthMax(20);
  }
  
  @Override
  public HL7Component makeAnother() {
    return new IS(this);
  }
  
  public IS(IS copy)
  {
    super(copy);
    init();
  }



  @Override
  public void init() {
    // nothing to do
  }
}
