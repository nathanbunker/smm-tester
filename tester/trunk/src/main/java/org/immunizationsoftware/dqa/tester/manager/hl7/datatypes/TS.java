package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class TS extends HL7Component
{
  private DTM time = null;
  private ID degreeOfPrecision = null;
  
  public DTM getTime() {
    return time;
  }

  public void setTime(DTM time) {
    this.time = time;
  }

  public ID getDegreeOfPrecision() {
    return degreeOfPrecision;
  }

  public void setDegreeOfPrecision(ID degreeOfPrecision) {
    this.degreeOfPrecision = degreeOfPrecision;
  }

  @Override
  public HL7Component makeAnother() {
    return new TS(this);
  }
  
  public TS(TS copy)
  {
    super(copy);
    init();
  }

  public TS(String componentName, UsageType usageType)
  {
    this(componentName, usageType, null);
  }

  public TS(String componentName, UsageType usageType, Cardinality cardinality)
  {
    super(ItemType.DATATYPE, "TS", "Time Stamp", componentName, 2, usageType, cardinality, 26);
    init();
  }

  public void init() {
    setChild(1, time = new DTM("Time", UsageType.R));
    setChild(2, degreeOfPrecision = new ID("Degree of Precision", UsageType.X));
  }
}
