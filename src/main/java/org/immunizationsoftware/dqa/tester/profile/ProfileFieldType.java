package org.immunizationsoftware.dqa.tester.profile;

public enum ProfileFieldType {
  SEGMENT, FIELD, FIELD_PART, FIELD_SUB_PART, FIELD_VALUE, FIELD_PART_VALUE, FIELD_SUB_PART_VALUE
  ;
  public String toString() {
    if (this == SEGMENT)
    {
      return "Segment";
    }
    if (this == FIELD)
    {
      return "Field";
    }
    if (this == FIELD_PART)
    {
      return "Field Part";
    }
    if (this == FIELD_SUB_PART)
    {
      return "Field Sub Part";
    }
    if (this == FIELD_VALUE)
    {
      return "Field Value";
    }
    if (this == FIELD_PART_VALUE)
    {
      return "Field Part Value";
    }
    if (this == FIELD_SUB_PART_VALUE)
    {
      return "Field Sub Part Value";
    }
    return super.toString();
  };
}
