package org.immunizationsoftware.dqa.tester.profile;

public enum Usage {
  R, RE, O, X, RE_OR_O, R_OR_X, RE_OR_X, R_OR_RE, R_NOT_ENFORCED, RE_NOT_USED, O_NOT_USED, X_NOT_ENFORCED;
  public String getDescription() {
    switch (this) {
    case R:
      return "Required";
    case RE:
      return "Required, but may be empty";
    case O:
      return "Optional";
    case X:
      return "Not Supported";
    case RE_OR_O:
      return "Required, but may be empty or Optional";
    case R_OR_RE:
      return "Required or Required, but may be empty";
    case RE_OR_X:
      return "Required, but may be empty or Not supported";
    case R_OR_X:
      return "Required or Not supported";
    case R_NOT_ENFORCED:
      return "Required, but not enforced";
    case RE_NOT_USED:
      return "Required, but may be empty and is not used";
    case O_NOT_USED:
      return "Optional, but not used";
    case X_NOT_ENFORCED:
      return "Not Supported, but not enforced";
    default:
      return toString();
    }
  }
}