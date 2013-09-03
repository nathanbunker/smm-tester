package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class FT extends HL7Component
{
  public FT()
  {
    super("FT", "Formatted Text", "", 0);
    setLengthMax(65536);
  }

  public FT(String componentNameSpecific) {
    super("FT", "Formatted Text", componentNameSpecific, 0);
    setLengthMax(65536);
  }

  public FT(String componentNameSpecific, UsageType usageType, int lengthMax) {
    super("FT", "Formatted Text", componentNameSpecific, 0, usageType, lengthMax);
  }

  public FT(String componentNameSpecific, UsageType usageType) {
    super("FT", "Formatted Text", componentNameSpecific, 0, usageType, Integer.MAX_VALUE);
    setLengthMax(65536);
  }

  @Override
  public void init() {
    // nothing to init
  }
  
  @Override
  public HL7Component makeAnother() {
    return new FT(this);
  }
  
  public FT(FT copy)
  {
    super(copy);
    init();
  }


}
