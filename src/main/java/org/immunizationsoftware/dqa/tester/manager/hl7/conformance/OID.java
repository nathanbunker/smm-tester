package org.immunizationsoftware.dqa.tester.manager.hl7.conformance;

public class OID extends ConformanceStatement
{
  public OID(String text) {
    super(text);
  }

  @Override
  public boolean conforms() {
    if (component.getValue() == null || component.getValue().equals("")) {
      return true;
    }
    try {
      String values[] = component.getValue().split("\\.");
      for (String value : values) {
        int i = Integer.parseInt(value);
        if (i < 0)
        {
          return false;
        }
      }
    } catch (NumberFormatException nfe) {
      return false;
    }
    return false;
  }
}
