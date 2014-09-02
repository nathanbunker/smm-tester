package org.immunizationsoftware.dqa.tester.manager.hl7.conformance;


public class PositiveInteger extends ConformanceStatement
{
  public PositiveInteger(String text) {
    super(text);
  }

  @Override
  public boolean conforms() {
    if (component.getValue() == null || component.getValue().equals("")) {
      return true;
    }
    try {
      String value = component.getValue();
      int i = Integer.parseInt(value);
      return i >= 0;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }
}
