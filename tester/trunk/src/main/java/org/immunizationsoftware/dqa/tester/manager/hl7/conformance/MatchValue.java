package org.immunizationsoftware.dqa.tester.manager.hl7.conformance;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;

public class MatchValue extends ConformanceStatement
{
  private HL7Component matchComponent = null;

  public MatchValue(String text, HL7Component matchComponent) {
    super(text);
    this.matchComponent = matchComponent;
  }

  @Override
  public boolean conforms() {
    if (component.getValue() == null || component.getValue().equals("")) {
      return true;
    }

    if (component.getValue().equals(matchComponent.getValue())) {
      return true;
    }
    return false;
  }
}
