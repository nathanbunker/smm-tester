package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class DR extends HL7Component
{
  private TS rangeStartDateTime = null;
  private TS rangeEndDateTime = null;
  public TS getRangeStartDateTime() {
    return rangeStartDateTime;
  }
  public void setRangeStartDateTime(TS rangeStartDateTime) {
    this.rangeStartDateTime = rangeStartDateTime;
  }
  public TS getRangeEndDateTime() {
    return rangeEndDateTime;
  }
  public void setRangeEndDateTime(TS rangeEndDateTime) {
    this.rangeEndDateTime = rangeEndDateTime;
  }


  
  @Override
  public HL7Component makeAnother() {
    return new DR(this);
  }
  
  public DR(DR copy)
  {
    super(copy);
    init();
  }

  public DR(String componentName, UsageType usageType)
  {
    super(ItemType.DATATYPE, "DR", "Date/Time Range", componentName, 2, usageType);
    
    init();
  }
  public void init() {
    setChild(1, rangeStartDateTime = new TS("Range Start Date Time", UsageType.O));
    setChild(2, rangeEndDateTime = new TS("Range End Date Time", UsageType.O));
  }
}
