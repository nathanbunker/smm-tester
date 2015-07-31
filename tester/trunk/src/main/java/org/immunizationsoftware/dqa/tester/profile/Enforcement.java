package org.immunizationsoftware.dqa.tester.profile;

public enum Enforcement {
  ENFORCED, WARNING, NOT_ENFORCED, NOT_DEFINED;

  public String getDescription() {
    switch (this) {
    case ENFORCED:
      return "Enforced";
    case WARNING:
      return "Warning";
    case NOT_ENFORCED:
      return "Not Enforced";
    case NOT_DEFINED:
      return "-";
    default:
      return "";
    }
  }

  public static Enforcement readEnforcement(String enforcementString) {
    if (enforcementString == null || enforcementString.equals("") || enforcementString.equals("-")) {
      return NOT_DEFINED;
    } else if (enforcementString.equals("E")) {
      return ENFORCED;
    } else if (enforcementString.equals("W")) {
      return WARNING;
    } else if (enforcementString.equals("NE")) {
      return NOT_ENFORCED;
    } else {
      return NOT_DEFINED;
    }
  }

  @Override
  public String toString() {
    switch (this) {
    case ENFORCED:
      return "E";
    case WARNING:
      return "W";
    case NOT_ENFORCED:
      return "NE";
    case NOT_DEFINED:
      return "-";
    default:
      return "";
    }
  }
}
