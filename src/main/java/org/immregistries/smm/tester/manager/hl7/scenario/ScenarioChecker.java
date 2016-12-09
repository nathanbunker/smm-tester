package org.immregistries.smm.tester.manager.hl7.scenario;

import org.immregistries.smm.tester.manager.hl7.HL7Component;

public interface ScenarioChecker
{
  public boolean checkScenario(HL7Component comp);
}
