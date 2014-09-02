package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;

public class PT extends HL7Component
{

  private ID processingID = null;
  private ID processingMode = null;

  public ID getProcessingID() {
    return processingID;
  }

  public void setProcessingID(ID processingID) {
    this.processingID = processingID;
  }

  public ID getProcessingMode() {
    return processingMode;
  }

  public void setProcessingMode(ID processingMode) {
    this.processingMode = processingMode;
  }

  @Override
  public HL7Component makeAnother() {
    return new PT(this);
  }
  
  public PT(PT copy)
  {
    super(copy);
    init();
  }

  public PT(String componentName, UsageType usageType) {
    super(ItemType.DATATYPE, "PT", "Processing Type", componentName, 2, usageType);
    init();
  }

  public PT(String componentName, UsageType usageType, Cardinality cardinality) {
    super(ItemType.DATATYPE, "PT", "Processing Type", componentName, 2, usageType, cardinality);
    init();
  }

  public void init() {
    setChild(1, processingID = new ID("Processing Id", UsageType.R, 1, ValueSet.HL70103));
    setChild(2, processingMode = new ID("Processing Mode", UsageType.O));
  }
}
