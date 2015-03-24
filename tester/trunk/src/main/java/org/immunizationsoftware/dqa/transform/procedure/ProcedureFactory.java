package org.immunizationsoftware.dqa.transform.procedure;

public class ProcedureFactory
{
  public static final String REMOVE_VACCINATION_GROUPS = "REMOVE_VACCINATION_GROUPS";
  
  public static ProcedureInterface getProcedure(String procedureName)
  {
    if (procedureName.equalsIgnoreCase(REMOVE_VACCINATION_GROUPS))
    {
      return new RemoveVaccinationGroupsProcedure();
    }
    return null;
  }
}
