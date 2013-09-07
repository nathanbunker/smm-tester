package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;

public class MSG extends HL7Component
{

  private ID messageCode = null;
  private ID triggerEvent = null;
  private ID messageStructure = null;
  
  public ID getMessageCode() {
    return messageCode;
  }

  public void setMessageCode(ID messageCode) {
    this.messageCode = messageCode;
  }

  public ID getTriggerEvent() {
    return triggerEvent;
  }

  public void setTriggerEvent(ID triggerEvent) {
    this.triggerEvent = triggerEvent;
  }

  public ID getMessageStructure() {
    return messageStructure;
  }

  public void setMessageStructure(ID messageStructure) {
    this.messageStructure = messageStructure;
  }

  @Override
  public HL7Component makeAnother() {
    return new MSG(this);
  }
  
  public MSG(MSG copy)
  {
    super(copy);
    init();
  }

  public MSG(String componentNameSpecific, UsageType usageType)
  {
    super(ItemType.DATATYPE, "MSG", "Message Type", componentNameSpecific, 3, usageType);
    init();
  }

  public MSG(String componentNameSpecific, UsageType usageType, Cardinality cardinality)
  {
    super(ItemType.DATATYPE, "MSG", "Message Type", componentNameSpecific, 3, usageType, cardinality);
    init();
  }

  public void init() {
    setChild(1, messageCode = new ID("Message Code", UsageType.R, ValueSet.HL70076));
    setChild(2, triggerEvent = new ID("Trigger Event", UsageType.R, ValueSet.HL70003));
    setChild(3, messageStructure = new ID("Message Structure", UsageType.R, ValueSet.HL70354));
  }
}
