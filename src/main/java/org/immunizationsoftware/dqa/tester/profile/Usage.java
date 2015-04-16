package org.immunizationsoftware.dqa.tester.profile;

public enum Usage {
  R, RE, O, X, RE_OR_O, R_OR_X, RE_OR_X, R_OR_RE, R_NOT_ENFORCED, RE_NOT_USED, O_NOT_USED, X_NOT_ENFORCED, NOT_DEFINED;
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
    case NOT_DEFINED:
      return "Not Defined";
    default:
      return toString();
    }
  }
  
  public static Usage readUsage(String usageString) {
    if (usageString == null) {
      return Usage.NOT_DEFINED;
    } else if (usageString.equalsIgnoreCase("R")) {
      return Usage.R;
    } else if (usageString.equalsIgnoreCase("RE")) {
      return Usage.RE;
    } else if (usageString.equalsIgnoreCase("O")) {
      return Usage.O;
    } else if (usageString.equalsIgnoreCase("X")) {
      return Usage.X;
    } else if (usageString.equalsIgnoreCase("RE_OR_O")) {
      return Usage.RE_OR_O;
    } else if (usageString.equalsIgnoreCase("R_OR_X")) {
      return Usage.R_OR_X;
    } else if (usageString.equalsIgnoreCase("RE_OR_X")) {
      return Usage.RE_OR_X;
    } else if (usageString.equalsIgnoreCase("R_OR_RE")) {
      return Usage.R_OR_RE;
    } else if (usageString.equalsIgnoreCase("R*")) {
      return Usage.R_NOT_ENFORCED;
    } else if (usageString.equalsIgnoreCase("RE*")) {
      return Usage.RE_NOT_USED;
    } else if (usageString.equalsIgnoreCase("O*")) {
      return Usage.O_NOT_USED;
    } else if (usageString.equalsIgnoreCase("X*")) {
      return Usage.X_NOT_ENFORCED;
    } else {
      return Usage.NOT_DEFINED;
    }
  }
  
  @Override
  public String toString() {
      switch (this) {
      case R:
        return "R";
      case RE:
        return "RE";
      case O:
        return "O";
      case X:
        return "X";
      case RE_OR_O:
        return "RE or O";
      case R_OR_RE:
        return "R or RE";
      case RE_OR_X:
        return "RE or X";
      case R_OR_X:
        return "R or X";
      case R_NOT_ENFORCED:
        return "R*";
      case RE_NOT_USED:
        return "RE*";
      case O_NOT_USED:
        return "O*";
      case X_NOT_ENFORCED:
        return "X*";
      case NOT_DEFINED:
        return "-";
      default:
        return super.toString();
      }
  }
}