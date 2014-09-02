package org.immunizationsoftware.dqa.tester.manager.hl7;

public class HL7ComponentItem
{

  
  private HL7Component component = null;
  private String description = "";
  private ItemType itemType = null;
  public HL7Component getComponent() {
    return component;
  }
  public void setComponent(HL7Component component) {
    this.component = component;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public ItemType getItemType() {
    return itemType;
  }
  public void setItemType(ItemType itemType) {
    this.itemType = itemType;
  }
  
  public HL7ComponentItem(String description, HL7Component component)
  {
    this.description = description;
    this.component = component;
  }
  
  public HL7ComponentItem(HL7Component component)
  {
    if (component.getItemType() == ItemType.DATATYPE)
    {
      this.description = "Data Type " + component.getComponentCode() + " - " + component.getComponentNameGeneric();
    }
    else if (component.getItemType() == ItemType.SEGMENT)
    {
      this.description = "Segment " + component.getComponentCode() + " - " + component.getComponentNameGeneric();
    }
    else if (component.getItemType() == ItemType.MESSAGE_PART)
    {
      this.description = "Message Part " + component.getComponentCode() + " - " + component.getComponentNameGeneric();
    }
    else if (component.getItemType() == ItemType.MESSAGE)
    {
      this.description = "Message " + component.getComponentCode() + " - " + component.getComponentNameGeneric();
    }
    else
    {
      this.description = "Component " + component.getComponentCode() + " - " + component.getComponentNameGeneric();
    }
    this.component = component;
  }
  
}
