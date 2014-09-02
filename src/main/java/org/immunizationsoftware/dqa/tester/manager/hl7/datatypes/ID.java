package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;

public class ID extends HL7Component
{

  public ID(String componentName, ValueSet valueSet) {
    super(ItemType.DATATYPE, "ID", "Coded Values for HL7 Tables", componentName, 0);
    this.valueSet = valueSet;
    setLengthMax(15);
  }

    public ID(String componentName, UsageType usageType, ValueSet valueSet) {
    this(componentName, usageType, null, valueSet);
  }

  public ID(String componentName, UsageType usageType, Cardinality cardinality, ValueSet valueSet) {
    super(ItemType.DATATYPE, "ID", "Coded Values for HL7 Tables", componentName, 0, usageType, cardinality);
    this.valueSet = valueSet;
    setLengthMax(15);
  }

  public ID(String componentName, UsageType usageType, int lengthMax, ValueSet valueSet) {
    super(ItemType.DATATYPE, "ID", "Coded Values for HL7 Tables", componentName, 0, usageType, lengthMax);
    this.valueSet = valueSet;
  }

  public ID(String componentName, UsageType usageType) {
    super(ItemType.DATATYPE, "ID", "Coded Values for HL7 Tables", componentName, 0, usageType, Integer.MAX_VALUE);
    setLengthMax(15);
  }

  @Override
  public void init() {
    // nothing to init
  }

  @Override
  public HL7Component makeAnother() {
    return new ID(this);
  }

  public ID(ID copy) {
    super(copy);
    init();
  }

}
