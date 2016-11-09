package org.immunizationsoftware.dqa.transform.procedure;

import org.immunizationsoftware.dqa.transform.Transformer;

public class ProcedureFactory {
  public static final String REMOVE_VACCINATION_GROUPS = "REMOVE_VACCINATION_GROUPS";
  public static final String ADD_FUNDING_ELGIBILITY_TO_ALL_RXA = "ADD_FUNDING_ELGIBILITY_TO_ALL_RXA";
  public static final String ANONYMIZE_AND_UPDATE_RECORD = "ANONYMIZE_AND_UPDATE_RECORD";

  public static ProcedureInterface getProcedure(String procedureName, Transformer transformer) {
    ProcedureInterface procedureInterface = null;
    if (procedureName.equalsIgnoreCase(REMOVE_VACCINATION_GROUPS)) {
      procedureInterface = new RemoveVaccinationGroupsProcedure();
    } else if (procedureName.equalsIgnoreCase(ADD_FUNDING_ELGIBILITY_TO_ALL_RXA)) {
      procedureInterface = new AddFundingEligibilityToAllRxa();
    } else if (procedureName.equalsIgnoreCase(ANONYMIZE_AND_UPDATE_RECORD)) {
      procedureInterface = new AnonymizeAndUpdateRecord();
    }
    if (procedureInterface != null) {
      procedureInterface.setTransformer(transformer);
    }
    return procedureInterface;
  }
}
