package org.immunizationsoftware.dqa.tester.manager.hl7.conformance;


public class ExactValue extends ConformanceStatement
{
  public ExactValue(String text, String requiredValue) {
    super(text);
    setRequiredValue(requiredValue);
  }

  public ExactValue(String text, String[] requiredValues) {
    super(text);
    setRequiredValues(requiredValues);
  }

  @Override
  public boolean conforms() {
    if (component.getValue() == null || component.getValue().equals("")) {
      return true;
    }
    for (String s : requiredValues) {
      if (component.getValue().equals(s)) {
        return true;
      }
    }
    return false;
  }
}
