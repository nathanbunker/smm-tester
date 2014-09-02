package org.immunizationsoftware.dqa.tester.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.server.session.HashSessionIdManager;

public class CompareManager
{
  public static class Comparison
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
        if (treatZerosSameAsOs)
        {
          ov = originalValue.toUpperCase().replace('0', 'O');
          rv = returnedValue.toUpperCase().replace('0', 'O');
        }
        if (equivalents.size() > 0 && originalValue != null && returnedValue != null && !originalValue.equals("")
            && !returnedValue.equals("")) {
          if (ov.equalsIgnoreCase(rv)) {
            pass = true;
          } else {
            String otherValue = equivalents.get(originalValue);
            if (treatZerosSameAsOs)
            {
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

  public static List<Comparison> compareMessages(String vxuMessage, String rspMessage) {
    List<Comparison> comparisonList = new ArrayList<CompareManager.Comparison>();
    HL7Reader vxuReader = new HL7Reader(vxuMessage);
    HL7Reader rspReader = new HL7Reader(rspMessage);

    Comparison comparison;

    vxuReader.advanceToSegment("PID");
    rspReader.advanceToSegment("PID");

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-3.1");
    comparison.setFieldLabel("MRN");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 1, "MR", 5));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 1, "MR", 5));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-3.1");
    comparison.setFieldLabel("SSN");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 1, "SS", 5));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 1, "SS", 5));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-3.1");
    comparison.setFieldLabel("Medicaid Number");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 1, "MA", 5));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 1, "MA", 5));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-3.4");
    comparison.setFieldLabel("MRN Assigning Authority");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 4, "MR", 5));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 4, "MR", 5));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-5.1");
    comparison.setFieldLabel("Name Last");
    comparison.setOriginalValue(vxuReader.getValue(5));
    comparison.setReturnedValue(rspReader.getValue(5));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-5.2");
    comparison.setFieldLabel("Name First");
    comparison.setOriginalValue(vxuReader.getValue(5, 2));
    comparison.setReturnedValue(rspReader.getValue(5, 2));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-5.3");
    comparison.setFieldLabel("Name Middle");
    comparison.setOriginalValue(vxuReader.getValue(5, 3));
    comparison.setReturnedValue(rspReader.getValue(5, 3));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-5.1");
    comparison.setFieldLabel("Alias Last");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(5, 1, "A", 7));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(5, 1, "A", 7));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-5.2");
    comparison.setFieldLabel("Alias First");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(5, 2, "A", 7));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(5, 2, "A", 7));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-5.3");
    comparison.setFieldLabel("Alias Middle");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(5, 3, "A", 7));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(5, 3, "A", 7));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-5.4");
    comparison.setFieldLabel("Suffix");
    comparison.setOriginalValue(vxuReader.getValue(5, 4));
    comparison.setReturnedValue(rspReader.getValue(5, 4));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-5.7");
    comparison.setFieldLabel("Name Type Code");
    comparison.setOriginalValue(vxuReader.getValue(5, 7));
    comparison.setReturnedValue(rspReader.getValue(5, 7));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID 6");
    comparison.setFieldLabel("Mother's Maiden Name");
    comparison.setOriginalValue(vxuReader.getValue(6));
    comparison.setReturnedValue(rspReader.getValue(6));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-7");
    comparison.setFieldLabel("Date/Time of Birth");
    comparison.setOriginalValue(vxuReader.getValue(7));
    comparison.setReturnedValue(rspReader.getValue(7));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-8");
    comparison.setFieldLabel("Administrative Sex");
    comparison.setOriginalValue(vxuReader.getValue(8));
    comparison.setReturnedValue(rspReader.getValue(8));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-10");
    comparison.setFieldLabel("Race");
    comparison.setOriginalValue(vxuReader.getValue(10));
    comparison.setReturnedValue(rspReader.getValue(10));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparison.registerEquivalent("1002-5", "I");
    comparison.registerEquivalent("2028-9", "A");
    comparison.registerEquivalent("2076-8", "A");
    comparison.registerEquivalent("2054-5", "B");
    comparison.registerEquivalent("2106-3", "W");
    comparison.registerEquivalent("2131-1", "O");
    comparison.registerEquivalent("I", "1002-5");
    comparison.registerEquivalent("B", "2054-5");
    comparison.registerEquivalent("W", "2106-3");
    comparison.registerEquivalent("U", "2131-1");
    comparison.registerAllowedValueMask("1002-5");
    comparison.registerAllowedValueMask("2028-9");
    comparison.registerAllowedValueMask("2076-8");
    comparison.registerAllowedValueMask("2054-5");
    comparison.registerAllowedValueMask("2106-3");
    comparison.registerAllowedValueMask("2131-1");
    comparison.registerAllowedValueMask("I");
    comparison.registerAllowedValueMask("B");
    comparison.registerAllowedValueMask("W");
    comparison.registerAllowedValueMask("U");
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-11.1");
    comparison.setFieldLabel("Address Street");
    comparison.setOriginalValue(vxuReader.getValue(11, 1));
    comparison.setReturnedValue(rspReader.getValue(11, 1));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-11.3");
    comparison.setFieldLabel("Address City");
    comparison.setOriginalValue(vxuReader.getValue(11, 3));
    comparison.setReturnedValue(rspReader.getValue(11, 3));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-11.4");
    comparison.setFieldLabel("Address State");
    comparison.setOriginalValue(vxuReader.getValue(11, 4));
    comparison.setReturnedValue(rspReader.getValue(11, 4));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-11.5");
    comparison.setFieldLabel("Address Zip");
    comparison.setOriginalValue(vxuReader.getValue(11, 5));
    comparison.setReturnedValue(rspReader.getValue(11, 5));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-11.4");
    comparison.setFieldLabel("Birth State");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(11, 4, "BDL", 7));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(11, 4, "BDL", 7));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-11.6");
    comparison.setFieldLabel("Birth Country");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(11, 6, "BDL", 7));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(11, 6, "BDL", 7));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-13.2");
    comparison.setFieldLabel("Phone Tel Use Code");
    comparison.setOriginalValue(vxuReader.getValue(13, 2));
    comparison.setReturnedValue(rspReader.getValue(13, 2));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-13.3");
    comparison.setFieldLabel("Phone Tel Equipment");
    comparison.setOriginalValue(vxuReader.getValue(13, 3));
    comparison.setReturnedValue(rspReader.getValue(13, 3));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-13.6");
    comparison.setFieldLabel("Phone Area Code");
    comparison.setOriginalValue(vxuReader.getValue(13, 6));
    comparison.setReturnedValue(rspReader.getValue(13, 6));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-13.7");
    comparison.setFieldLabel("Phone Number");
    comparison.setOriginalValue(vxuReader.getValue(13, 7));
    comparison.setReturnedValue(rspReader.getValue(13, 7));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-13.4");
    comparison.setFieldLabel("Email Address");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(13, 4, "NET", 2));
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(13, 4, "NET", 2));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-15");
    comparison.setFieldLabel("Primary Language");
    comparison.setOriginalValue(vxuReader.getValue(15));
    comparison.setReturnedValue(rspReader.getValue(15));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-22");
    comparison.setFieldLabel("Ethnic Group");
    comparison.setOriginalValue(vxuReader.getValue(22));
    comparison.setReturnedValue(rspReader.getValue(22));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparison.registerEquivalent("2135-2", "H");
    comparison.registerEquivalent("2186-5", "N");
    comparison.registerEquivalent("H", "2135-2");
    comparison.registerEquivalent("N", "2186-5");
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-23");
    comparison.setFieldLabel("Birth Place");
    comparison.setOriginalValue(vxuReader.getValue(23));
    comparison.setReturnedValue(rspReader.getValue(23));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-24");
    comparison.setFieldLabel("Multiple Birth Indicator");
    comparison.setOriginalValue(vxuReader.getValue(24));
    comparison.setReturnedValue(rspReader.getValue(24));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-16");
    comparison.setFieldLabel("Immunization Registry Status");
    comparison.setOriginalValue(vxuReader.getValue(16));
    comparison.setReturnedValue(rspReader.getValue(16));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
    comparisonList.add(comparison);

    vxuReader.advanceToSegment("PD1");
    rspReader.advanceToSegment("PD1");

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-25");
    comparison.setFieldLabel("Birth Order");
    comparison.setOriginalValue(vxuReader.getValue(25));
    comparison.setReturnedValue(rspReader.getValue(25));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparisonList.add(comparison);

    vxuReader.resetPostion();
    rspReader.resetPostion();

    {
      vxuReader.advanceToSegmentWithValue("NK1", 3, "MTH");
      rspReader.advanceToSegmentWithValue("NK1", 3, "MTH");

      comparison = new Comparison();
      comparison.setHl7FieldName("NK1-2.1");
      comparison.setFieldLabel("Mother's Name Last");
      comparison.setOriginalValue(vxuReader.getValue(2));
      comparison.setReturnedValue(rspReader.getValue(2));
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
      comparisonList.add(comparison);

      comparison = new Comparison();
      comparison.setHl7FieldName("NK1-2.2");
      comparison.setFieldLabel("Mother's Name First");
      comparison.setOriginalValue(vxuReader.getValue(2, 2));
      comparison.setReturnedValue(rspReader.getValue(2, 2));
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
      comparisonList.add(comparison);

      comparison = new Comparison();
      comparison.setHl7FieldName("NK1-2.3");
      comparison.setFieldLabel("Mother's Name Middle");
      comparison.setOriginalValue(vxuReader.getValue(2, 3));
      comparison.setReturnedValue(rspReader.getValue(2, 3));
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
      comparisonList.add(comparison);

      comparison = new Comparison();
      comparison.setHl7FieldName("NK1-33.1");
      comparison.setFieldLabel("Mother's SSN");
      comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(33, 1, "SS", 5));
      comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(33, 1, "SS", 5));
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
      comparisonList.add(comparison);

      vxuReader.resetPostion();
      rspReader.resetPostion();
    }

    {
      vxuReader.advanceToSegmentWithValue("NK1", 3, "FTH");
      rspReader.advanceToSegmentWithValue("NK1", 3, "FTH");

      comparison = new Comparison();
      comparison.setHl7FieldName("NK1-2.1");
      comparison.setFieldLabel("Father's Name Last");
      comparison.setOriginalValue(vxuReader.getValue(2));
      comparison.setReturnedValue(rspReader.getValue(2));
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
      comparisonList.add(comparison);

      comparison = new Comparison();
      comparison.setHl7FieldName("NK1-2.2");
      comparison.setFieldLabel("Father's Name First");
      comparison.setOriginalValue(vxuReader.getValue(2, 2));
      comparison.setReturnedValue(rspReader.getValue(2, 2));
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
      comparisonList.add(comparison);

      comparison = new Comparison();
      comparison.setHl7FieldName("NK1-2.3");
      comparison.setFieldLabel("Father's Name Middle");
      comparison.setOriginalValue(vxuReader.getValue(2, 3));
      comparison.setReturnedValue(rspReader.getValue(2, 3));
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
      comparisonList.add(comparison);

      comparison = new Comparison();
      comparison.setHl7FieldName("NK1-33.1");
      comparison.setFieldLabel("Father's SSN");
      comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(33, 1, "SS", 5));
      comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(33, 1, "SS", 5));
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
      comparisonList.add(comparison);

      vxuReader.resetPostion();
      rspReader.resetPostion();
    }

    while (vxuReader.advanceToSegment("NK1")) {
      String relationshipType = vxuReader.getValue(3);
      if (relationshipType.equals("GRD")) {
        rspReader.resetPostion();
        rspReader.advanceToSegmentWithValue("NK1", 3, relationshipType);

        comparison = new Comparison();
        comparison.setHl7FieldName("NK1-2.1");
        comparison.setFieldLabel("Responsible Person Name Last");
        comparison.setOriginalValue(vxuReader.getValue(2));
        comparison.setReturnedValue(rspReader.getValue(2));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("NK1-2.2");
        comparison.setFieldLabel("Responsible Person Name First");
        comparison.setOriginalValue(vxuReader.getValue(2, 2));
        comparison.setReturnedValue(rspReader.getValue(2, 2));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("NK1-2.3");
        comparison.setFieldLabel("Responsible Person Name Middle");
        comparison.setOriginalValue(vxuReader.getValue(2, 3));
        comparison.setReturnedValue(rspReader.getValue(2, 3));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        comparisonList.add(comparison);
      }

      rspReader.resetPostion();
    }

    vxuReader.resetPostion();
    int count = 0;
    while (vxuReader.advanceToSegment("RXA")) {
      count++;
      String cvxCode = vxuReader.getValue(5);
      String adminDate = vxuReader.getValue(3);
      if (cvxCode.equals("") || adminDate.equals("")) {
        // wouldn't expect this to be the case
        continue;
      }
      rspReader.resetPostion();
      while (rspReader.advanceToSegment("RXA")) {
        String rspCvxCode = rspReader.getValue(5);
        if (rspCvxCode.equals(cvxCode)) {
          if (cvxCode.equals("998")) {
            break;
          } else if (rspReader.getValue(3).equals(adminDate)) {
            break;
          }
        }
      }

      if (!cvxCode.equals("998")) {
        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-3 #" + count);
        comparison.setFieldLabel("Vaccination date");
        comparison.setOriginalValue(vxuReader.getValue(3));
        comparison.setReturnedValue(rspReader.getValue(3));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-5 #" + count);
        comparison.setFieldLabel("Vaccine type");
        comparison.setOriginalValue(vxuReader.getValue(5));
        comparison.setReturnedValue(rspReader.getValue(5));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-6 #" + count);
        comparison.setFieldLabel("Administered amount");
        comparison.setOriginalValue(vxuReader.getValue(6));
        comparison.setReturnedValue(rspReader.getValue(6));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        comparison.registerEquivalent("0.25", ".25");
        comparison.registerEquivalent(".25", "0.25");
        comparison.registerEquivalent("0.5", ".5");
        comparison.registerEquivalent(".5", "0.5");
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-7 #" + count);
        comparison.setFieldLabel("Administered units");
        comparison.setOriginalValue(vxuReader.getValue(7));
        comparison.setReturnedValue(rspReader.getValue(7));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-9 #" + count);
        comparison.setFieldLabel("Historical vaccination flag indicator");
        comparison.setOriginalValue(vxuReader.getValue(9));
        comparison.setReturnedValue(rspReader.getValue(9));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-10 #" + count);
        comparison.setFieldLabel("Vaccine administering provider");
        comparison.setOriginalValue(vxuReader.getValue(10));
        comparison.setReturnedValue(rspReader.getValue(10));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-11.4 #" + count);
        comparison.setFieldLabel("Vaccine administering at location");
        comparison.setOriginalValue(vxuReader.getValue(11, 4));
        comparison.setReturnedValue(rspReader.getValue(11, 4));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-15 #" + count);
        comparison.setFieldLabel("Vaccine lot number");
        comparison.setOriginalValue(vxuReader.getValue(15));
        comparison.setReturnedValue(rspReader.getValue(15));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
        comparison.setTreatZerosSameAsOs(true);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-16 #" + count);
        comparison.setFieldLabel("Vaccine expiration date");
        comparison.setOriginalValue(vxuReader.getValue(16));
        comparison.setReturnedValue(rspReader.getValue(16));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-17 #" + count);
        comparison.setFieldLabel("Vaccine manufacturer");
        comparison.setOriginalValue(vxuReader.getValue(17));
        comparison.setReturnedValue(rspReader.getValue(17));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-18 #" + count);
        comparison.setFieldLabel("Substance/Treatment Refusal Reason");
        comparison.setOriginalValue(vxuReader.getValue(18));
        comparison.setReturnedValue(rspReader.getValue(18));
        if (rspReader.getValue(5).equals("")) {
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
        } else {
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
        }
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-20 #" + count);
        comparison.setFieldLabel("Completion Status");
        comparison.setOriginalValue(vxuReader.getValue(20));
        comparison.setReturnedValue(rspReader.getValue(20));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
        comparisonList.add(comparison);

        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-21 #" + count);
        comparison.setFieldLabel("Action Code");
        comparison.setOriginalValue(vxuReader.getValue(21));
        comparison.setReturnedValue(rspReader.getValue(21));
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
        comparisonList.add(comparison);
      }

      rspReader.advanceToSegment("RXR", new String[] { "RXA", "OBX", "ORC" });
      vxuReader.advanceToSegment("RXR", new String[] { "RXA", "OBX", "ORC" });

      comparison = new Comparison();
      comparison.setHl7FieldName("RXR-2 #" + count);
      comparison.setFieldLabel("Vaccine injection site");
      comparison.setOriginalValue(vxuReader.getValue(2));
      comparison.setReturnedValue(rspReader.getValue(2));
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
      comparisonList.add(comparison);

      String vxuObsSubId = "";
      String rspObsSubId = "";
      int obxCount = 0;
      int rspReaderSegmentPosition = rspReader.getSegmentPosition();
      while (vxuReader.advanceToSegment("OBX", new String[] { "RXA", "ORC" })) {
        obxCount++;
        String obsId = vxuReader.getValue(3);

        String newVxuObsSubId = vxuReader.getValue(4);
        if (!vxuObsSubId.equals(newVxuObsSubId) || newVxuObsSubId.equals("")) {
          vxuObsSubId = newVxuObsSubId;
          rspObsSubId = "";
        }

        if (obsId.equals("64994-7")) {
          rspReader.gotoSegmentPosition(rspReaderSegmentPosition);
          while (rspReader.advanceToSegment("OBX", new String[] { "RXA", "ORC" })) {
            if (rspReader.getValue(3).equals(obsId)) {
              break;
            }
          }

          comparison = new Comparison();
          comparison.setHl7FieldName("OBX-5 #" + count + "." + obxCount);
          comparison.setFieldLabel("VFC elgibility");
          comparison.setOriginalValue(vxuReader.getValue(5));
          comparison.setReturnedValue(rspReader.getValue(5));
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
          comparisonList.add(comparison);

        } else if (obsId.equals("30945-0")) {
          rspReader.gotoSegmentPosition(rspReaderSegmentPosition);
          while (rspReader.advanceToSegment("OBX", new String[] { "RXA", "ORC" })) {
            if (rspReader.getValue(3).equals(obsId)) {
              break;
            }
          }

          comparison = new Comparison();
          comparison.setHl7FieldName("OBX-5 #" + count + "." + obxCount);
          comparison.setFieldLabel("Contraindication/Precaution");
          comparison.setOriginalValue(vxuReader.getValue(5));
          comparison.setReturnedValue(rspReader.getValue(5));
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
          comparisonList.add(comparison);

          comparison = new Comparison();
          comparison.setHl7FieldName("OBX-5 #" + count + "." + obxCount);
          comparison.setFieldLabel("Contraindication/Precaution Observation Date");
          comparison.setOriginalValue(vxuReader.getValue(14));
          comparison.setReturnedValue(rspReader.getValue(14));
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
          comparisonList.add(comparison);

        } else if (obsId.equals("31044-1")) {
          rspReader.gotoSegmentPosition(rspReaderSegmentPosition);
          while (rspReader.advanceToSegment("OBX", new String[] { "RXA", "ORC" })) {
            if (rspReader.getValue(3).equals(obsId)) {
              break;
            }
          }

          comparison = new Comparison();
          comparison.setHl7FieldName("OBX-5 #" + count + "." + obxCount);
          comparison.setFieldLabel("Reaction");
          comparison.setOriginalValue(vxuReader.getValue(5));
          comparison.setReturnedValue(rspReader.getValue(5));
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
          comparisonList.add(comparison);

        } else if (obsId.equals("30956-7")) {
          rspReader.gotoSegmentPosition(rspReaderSegmentPosition);
          while (rspReader.advanceToSegment("OBX", new String[] { "RXA", "ORC" })) {
            String newRspObsSubId = rspReader.getValue(4);
            if (rspReader.getValue(3).equals(obsId) && (rspObsSubId.equals("") || newRspObsSubId.equals(rspObsSubId))
                && rspReader.getValue(5).equals(vxuReader.getValue(5))) {
              break;
            }
          }

          comparison = new Comparison();
          comparison.setHl7FieldName("OBX-5 #" + count + "." + obxCount);
          comparison.setFieldLabel("VIS Type");
          comparison.setOriginalValue(vxuReader.getValue(5));
          comparison.setReturnedValue(rspReader.getValue(5));
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
          comparisonList.add(comparison);
          rspObsSubId = rspReader.getValue(4);

        } else if (obsId.equals("29768-9")) {
          rspReader.gotoSegmentPosition(rspReaderSegmentPosition);
          while (rspReader.advanceToSegment("OBX", new String[] { "RXA", "ORC" })) {
            String newRspObsSubId = rspReader.getValue(4);
            if (rspReader.getValue(3).equals(obsId) && (rspObsSubId.equals("") || newRspObsSubId.equals(rspObsSubId))) {
              break;
            }
          }

          comparison = new Comparison();
          comparison.setHl7FieldName("OBX-5 #" + count + "." + obxCount);
          comparison.setFieldLabel("VIS Type Published");
          comparison.setOriginalValue(vxuReader.getValue(5));
          comparison.setReturnedValue(rspReader.getValue(5));
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
          comparisonList.add(comparison);

          rspObsSubId = rspReader.getValue(4);
        } else if (obsId.equals("29769-7")) {
          rspReader.gotoSegmentPosition(rspReaderSegmentPosition);
          while (rspReader.advanceToSegment("OBX", new String[] { "RXA", "ORC" })) {
            String newRspObsSubId = rspReader.getValue(4);
            if (rspReader.getValue(3).equals(obsId) && (rspObsSubId.equals("") || newRspObsSubId.equals(rspObsSubId))) {
              break;
            }
          }

          comparison = new Comparison();
          comparison.setHl7FieldName("OBX-5 #" + count + "." + obxCount);
          comparison.setFieldLabel("VIS Presentation Date");
          comparison.setOriginalValue(vxuReader.getValue(5));
          comparison.setReturnedValue(rspReader.getValue(5));
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
          comparisonList.add(comparison);
          rspObsSubId = rspReader.getValue(4);

        } else if (cvxCode.equals("998") && obsId.equals("59784-9")) {
          rspReader.gotoSegmentPosition(rspReaderSegmentPosition);
          while (rspReader.advanceToSegment("OBX", new String[] { "RXA", "ORC" })) {
            if (rspReader.getValue(3).equals(obsId)) {
              break;
            }
          }

          comparison = new Comparison();
          comparison.setHl7FieldName("OBX-5 #" + count + "." + obxCount);
          comparison.setFieldLabel("History of disease");
          comparison.setOriginalValue(vxuReader.getValue(5));
          comparison.setReturnedValue(rspReader.getValue(5));
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
          comparisonList.add(comparison);
          rspObsSubId = "";
        }
      }

    }

    for (Iterator<Comparison> comparisonIt = comparisonList.iterator(); comparisonIt.hasNext();) {
      comparison = comparisonIt.next();
      if (!comparison.isTested()) {
        comparisonIt.remove();
      }
    }

    return comparisonList;
  }
}
