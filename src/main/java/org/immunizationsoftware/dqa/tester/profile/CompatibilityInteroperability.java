package org.immunizationsoftware.dqa.tester.profile;

public enum CompatibilityInteroperability {
  COMPATIBLE, DATA_LOSS, IF_CONFIGURED, IF_POPULATED, NO_PROBLEM, PROBLEM, MAJOR_PROBLEM, UNABLE_TO_DETERMINE
  ;
  public String toString() {
    switch(this)
    {
    case COMPATIBLE:
      return "Compatible";
    case DATA_LOSS:
      return "Data Loss";
    case IF_CONFIGURED:
      return "If Configured";
    case IF_POPULATED:
      return "If Populated";
    case MAJOR_PROBLEM:
      return "Major Problem";
    case NO_PROBLEM:
      return "No Problem";
    case PROBLEM:
      return "Problem";
    case UNABLE_TO_DETERMINE:
      return "Unable To Determine";
    }
    return super.toString();
  };
}
