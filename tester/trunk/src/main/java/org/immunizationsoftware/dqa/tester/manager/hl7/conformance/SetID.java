package org.immunizationsoftware.dqa.tester.manager.hl7.conformance;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;

public class SetID extends ConformanceStatement
{
  private HL7Component repeatingComponent = null;
  
  public SetID(String text, HL7Component repeatingComponent)
  {
    super(text);
    this.repeatingComponent = repeatingComponent;
  }

  @Override
  public boolean conforms() {
    if (component.getValue() == null || component.getValue().equals(""))
    {
      return true;
    }
    return String.valueOf(repeatingComponent.getCardinalityCount()).equals(component.getValue());
  }

}
