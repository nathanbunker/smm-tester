package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class SI extends HL7Component
{
  public SI(String componentNameSpecific) {
    super(ItemType.DATATYPE, "SI", "Sequence ID", componentNameSpecific, 0);
    setLengthMax(4);
  }

  public SI(String componentNameSpecific, UsageType usageType, int lengthMax) {
    super(ItemType.DATATYPE, "SI", "Sequence ID", componentNameSpecific, 0, usageType, lengthMax);
  }

  public SI(String componentNameSpecific, UsageType usageType) {
    super(ItemType.DATATYPE, "SI", "Sequence ID", componentNameSpecific, 0, usageType, 4);
  }

  public SI(String componentNameSpecific, UsageType usageType, Cardinality cardinality) {
    super(ItemType.DATATYPE, "SI", "Sequence ID", componentNameSpecific, 0, usageType, cardinality);
    setLengthMax(4);
  }

  @Override
  public HL7Component makeAnother() {
    return new SI(this);
  }

  public SI(SI copy) {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to do
  }

}
