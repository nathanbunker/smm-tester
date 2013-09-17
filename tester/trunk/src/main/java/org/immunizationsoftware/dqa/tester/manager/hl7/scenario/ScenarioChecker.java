package org.immunizationsoftware.dqa.tester.manager.hl7.scenario;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;

public interface ScenarioChecker
{
  public boolean checkScenario(HL7Component comp);
}
