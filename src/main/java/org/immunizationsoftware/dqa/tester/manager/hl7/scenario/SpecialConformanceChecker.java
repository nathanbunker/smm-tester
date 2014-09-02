package org.immunizationsoftware.dqa.tester.manager.hl7.scenario;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.RSP;

public class SpecialConformanceChecker
{
  public static boolean containsForecast(HL7Component comp) {
    boolean okayCDS = false;
    if (comp instanceof RSP) {
      boolean foundAtLeastOneForecast = false;
      boolean allVaccinationsHaveEvaluation = false;
      RSP rsp = (RSP) comp;
      RSP.PatientGroup patientGroup = rsp.getPatientGroup();
      RSP.PatientGroup.OrderGroup orderGroup = patientGroup.getOrderGroup();
      while (orderGroup != null) {
        if (orderGroup.getRxa().getAdministeredCode().getIdentifier().getValue().equals("998")) {
          // not administered
        } else if (orderGroup.getRxa().getCompletionStatus().getValue().equals("CP")
            || orderGroup.getRxa().getCompletionStatus().getValue().equals("")) {
          // must have evaluation
          RSP.PatientGroup.OrderGroup.ObservationGroup observationGroup = orderGroup.getObservationGroup();
          while (observationGroup != null) {
            
            observationGroup = (RSP.PatientGroup.OrderGroup.ObservationGroup) observationGroup.getNextComponent();
          }
        }
        orderGroup = (RSP.PatientGroup.OrderGroup) orderGroup.getNextComponent();
      }
    }
    return okayCDS;
  }
}
