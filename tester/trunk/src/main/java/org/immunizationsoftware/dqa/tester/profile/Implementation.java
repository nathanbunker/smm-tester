package org.immunizationsoftware.dqa.tester.profile;

public enum Implementation {
  INDIFFERENT, SUPPORTED, DEPRECATED, FUTURE, NOT_DEFINED;

  public String getDescription() {
    switch (this) {
    case INDIFFERENT:
      return "Indifferent";
    case SUPPORTED:
      return "Supported";
    case DEPRECATED:
      return "Deprecated";
    case FUTURE:
      return "Future";
    case NOT_DEFINED:
      return "-";
    default:
      return "";
    }
  }

  public static Implementation readImplementation(String implementationString) {
    if (implementationString == null || implementationString.equals("") || implementationString.equals("-")) {
      return NOT_DEFINED;
    } else if (implementationString.equals("I")) {
      return INDIFFERENT;
    } else if (implementationString.equals("S")) {
      return SUPPORTED;
    } else if (implementationString.equals("D")) {
      return DEPRECATED;
    } else if (implementationString.equals("F")) {
      return FUTURE;
    } else {
      return NOT_DEFINED;
    }
  }

  @Override
  public String toString() {
    switch (this) {
    case INDIFFERENT:
      return "I";
    case SUPPORTED:
      return "S";
    case DEPRECATED:
      return "D";
    case FUTURE:
      return "F";
    case NOT_DEFINED:
      return "-";
    default:
      return "";
    }
  }
}
