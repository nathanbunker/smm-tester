package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class ST extends HL7Component
{

  public ST()
  {
    super(ItemType.DATATYPE, "ST", "String", "", 0);
  }

  public ST(String componentNameSpecific) {
    super(ItemType.DATATYPE, "ST", "String", componentNameSpecific, 0);
  }

  public ST(String componentNameSpecific, UsageType usageType, int lengthMax) {
    super(ItemType.DATATYPE, "ST", "String", componentNameSpecific, 0, usageType, lengthMax);
  }

  public ST(String componentNameSpecific, UsageType usageType) {
    super(ItemType.DATATYPE, "ST", "String", componentNameSpecific, 0, usageType, Integer.MAX_VALUE);
  }

  public ST(String componentNameSpecific, UsageType usageType, Cardinality cardinality, int lengthMax) {
    super(ItemType.DATATYPE, "ST", "String", componentNameSpecific, 0, usageType, cardinality, lengthMax);
  }

  public ST(String componentNameSpecific, UsageType usageType, Cardinality cardinality) {
    super(ItemType.DATATYPE, "ST", "String", componentNameSpecific, 0, usageType, Integer.MAX_VALUE);
  }

  @Override
  public HL7Component makeAnother() {
    return new ST(this);
  }
  
  public ST(ST copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init 
  }
}
