package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.analyze.HL7DateAnalyzer;

public class DTM extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new DTM(this);
  }

  public DTM(DTM copy) {
    super(copy);
    init();
  }

  public DTM(String componentNameSpecific, UsageType usageType) {
    super(ItemType.DATATYPE, "DTM", "Date/Time", componentNameSpecific, 0, usageType);
    setLength(4, 24);
    init();
  }

  @Override
  public void init() {
    setFormatAnalyzer(new HL7DateAnalyzer(this));
  }
}
