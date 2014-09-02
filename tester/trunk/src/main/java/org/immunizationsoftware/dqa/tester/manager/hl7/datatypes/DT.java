package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class DT extends HL7Component
{

  @Override
  public HL7Component makeAnother() {
    return new DT(this);
  }
  
  public DT(DT copy)
  {
    super(copy);
    init();
  }

  public DT(String componentNameSpecific, UsageType usageType)
  {
    super(ItemType.DATATYPE, "DT", "Date", componentNameSpecific, 0, usageType);
    setLength(4, 8);
  }
  
  public DT(String componentNameSpecific, UsageType usageType, Cardinality cardinality)
  {
    super(ItemType.DATATYPE, "DT", "Date", componentNameSpecific, 0, usageType, cardinality);
    setLength(4, 8);
  }
  
  @Override
  public void init() {
    // TODO Nothing to init
  }

  
}
