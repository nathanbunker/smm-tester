package org.immunizationsoftware.dqa.tester.profile;

public enum CompatibilityConformance {
  COMPATIBLE, ALLOWANCE, CONSTRAINT, MAJOR_CONSTRAINT, CONFLICT, MAJOR_CONFLICT, UNABLE_TO_DETERMINE;

  public String toString() {
    switch (this) {
    case ALLOWANCE:
      return "Allowance";
    case COMPATIBLE:
      return "Compatible";
    case CONFLICT:
      return "Conflict";
    case CONSTRAINT:
      return "Constraint";
    case MAJOR_CONFLICT:
      return "Major Conflict";
    case MAJOR_CONSTRAINT:
      return "Major Constraint";
    case UNABLE_TO_DETERMINE:
      return "Unable to Determine";
    }
    return super.toString();
  };
}
