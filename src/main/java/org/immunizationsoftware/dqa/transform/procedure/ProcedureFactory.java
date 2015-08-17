package org.immunizationsoftware.dqa.transform.procedure;

public class ProcedureFactory
{
  public static final String REMOVE_VACCINATION_GROUPS = "REMOVE_VACCINATION_GROUPS";
  public static final String ADD_FUNDING_ELGIBILITY_TO_ALL_RXA = "ADD_FUNDING_ELGIBILITY_TO_ALL_RXA";
  
  public static ProcedureInterface getProcedure(String procedureName)
  {
    if (procedureName.equalsIgnoreCase(REMOVE_VACCINATION_GROUPS))
    {
      return new RemoveVaccinationGroupsProcedure();
    }
    else if (procedureName.equalsIgnoreCase(ADD_FUNDING_ELGIBILITY_TO_ALL_RXA))
    {
      return new AddFundingEligibilityToAllRxa();
    }
    return null;
  }
}
