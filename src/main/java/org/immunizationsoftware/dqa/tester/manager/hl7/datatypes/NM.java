package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class NM extends HL7Component
{
  
  
  public NM(String componentName)
  {
    super(ItemType.DATATYPE, "NM", "Numeric", componentName, 0);
    setLengthMax(16);
  }
  
  public NM(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "NM", "Numeric", componentName, 0, usageType);
  }
  
  public NM(String componentName, UsageType usageType, Cardinality cardinality)
  {
    super(ItemType.DATATYPE, "NM", "Numeric", componentName, 0, usageType, cardinality);
  }
  
  @Override
  public HL7Component makeAnother() {
    return new NM(this);
  }
  
  public NM(NM copy)
  {
    super(copy);
    init();
  }

  
  public NM(String componentName, UsageType usageType, int lengthMax)
  {
    super(ItemType.DATATYPE, "NM", "Numeric", componentName, 0, usageType, lengthMax);
  }
  
  @Override
  public void init() {
    // Nothing to init 
  }
}
