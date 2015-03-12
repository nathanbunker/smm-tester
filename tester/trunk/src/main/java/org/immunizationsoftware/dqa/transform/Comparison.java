package org.immunizationsoftware.dqa.transform;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Comparison
{
  public static final int PRIORITY_LEVEL_REQUIRED = 0;
  public static final int PRIORITY_LEVEL_OPTIONAL = 1;
  public static final int PRIORITY_LEVEL_EXTRA = 2;

  private int priorityLevel = 0;
  private String hl7FieldName = "";
  private String fieldLabel = "";
  private String originalValue = "";
  private String returnedValue = "";
  private Boolean pass = null;
  private Boolean tested = null;
  private boolean treatZerosSameAsOs = false;

  public boolean isTreatZerosSameAsOs() {
    return treatZerosSameAsOs;
  }

  public void setTreatZerosSameAsOs(boolean treatZerosSameAsOs) {
    this.treatZerosSameAsOs = treatZerosSameAsOs;
  }

  private Map<String, String> equivalents = new HashMap<String, String>();
  private Set<String> allowedValuesMask = new HashSet<String>();

  public void registerAllowedValueMask(String s) {
    allowedValuesMask.add(s);
  }

  public void registerEquivalent(String s1, String s2) {
    equivalents.put(s1.toUpperCase(), s2);
  }

  public int getPriorityLevel() {
    return priorityLevel;
  }

  public String getPriorityLevelLabel() {
    if (priorityLevel == Comparison.PRIORITY_LEVEL_REQUIRED) {
      return "Required";
    }
    if (priorityLevel == Comparison.PRIORITY_LEVEL_OPTIONAL) {
      return "Optional";
    }
    if (priorityLevel == Comparison.PRIORITY_LEVEL_EXTRA) {
      return "Extra";
    }
    return "";
  }

  public void setPriorityLevel(int priorityLevel) {
    this.priorityLevel = priorityLevel;
  }

  public boolean isTested() {
    if (tested == null) {
      tested = originalValue != null && !originalValue.equals("");
    }
    return tested;
  }

  public void setTested(boolean b) {
    tested = b;
  }

  public String getHl7FieldName() {
    return hl7FieldName;
  }

  public void setHl7FieldName(String hl7FieldName) {
    this.hl7FieldName = hl7FieldName;
  }

  public boolean isPass() {

    if (pass == null) {
      String ov = originalValue;
      String rv = returnedValue;
      if (treatZerosSameAsOs) {
        ov = originalValue.toUpperCase().replace('0', 'O');
        rv = returnedValue.toUpperCase().replace('0', 'O');
      }
      if (equivalents.size() > 0 && originalValue != null && returnedValue != null && !originalValue.equals("")
          && !returnedValue.equals("")) {
        if (ov.equalsIgnoreCase(rv)) {
          pass = true;
        } else {
          String otherValue = equivalents.get(originalValue);
          if (treatZerosSameAsOs) {
            otherValue = otherValue.toUpperCase().replace('0', 'O');
          }
          if (otherValue != null && otherValue.equalsIgnoreCase(rv)) {
            pass = true;
          } else {
            otherValue = equivalents.get(returnedValue);
            if (otherValue != null && ov.equals(otherValue)) {
              pass = true;
            } else {
              if (otherValue != null && !allowedValuesMask.contains(originalValue) && otherValue.equals("")) {
                // if original value is non standard then don't expect the
                // non-standard value to come back
                pass = true;
              } else {
                pass = false;
              }
            }
          }
        }
      } else {
        if (allowedValuesMask.size() > 0 && returnedValue.equals("") && !allowedValuesMask.contains(originalValue)) {
          // if original value is non standard then don't expect the
          // non-standard value to come back
          pass = true;
        } else {
          pass = ov != null && rv != null && ov.equalsIgnoreCase(rv);
        }
      }
    }
    return pass;
  }

  public void setPass(boolean b) {
    pass = b;
  }

  public String getFieldLabel() {
    return fieldLabel;
  }

  public void setFieldLabel(String fieldLabel) {
    this.fieldLabel = fieldLabel;
  }

  public String getOriginalValue() {
    return originalValue;
  }

  public void setOriginalValue(String originalValue) {
    this.originalValue = originalValue;
  }

  public String getReturnedValue() {
    return returnedValue;
  }

  public void setReturnedValue(String returnedValue) {
    this.returnedValue = returnedValue;
  }
}
