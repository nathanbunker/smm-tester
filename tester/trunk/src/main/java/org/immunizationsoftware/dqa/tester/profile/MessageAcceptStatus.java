package org.immunizationsoftware.dqa.tester.profile;

public enum MessageAcceptStatus {
  ONLY_IF_PRESENT, ONLY_IF_ABSENT, IF_PRESENT_OR_ABSENT;
  
  public String toString() {
    if (this == ONLY_IF_PRESENT)
    {
      return "Only if Present";
    }
    if (this == ONLY_IF_ABSENT)
    {
      return "Only if Absent";
    }
    if (this == IF_PRESENT_OR_ABSENT)
    {
      return "If Present or Absent";
    }
    return super.toString();
  };
}
