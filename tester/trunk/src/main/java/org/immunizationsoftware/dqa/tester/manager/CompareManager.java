package org.immunizationsoftware.dqa.tester.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    
    private Map<String, String> equivalents = new HashMap<String, String>();
    
    public void registerEquivelant(String s1, String s2)
    {
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
        if (equivalents.size() > 0 && originalValue != null && returnedValue != null && !originalValue.equals("") && !returnedValue.equals(""))
        {
          if (originalValue.equalsIgnoreCase(returnedValue))
          {
            pass = true;
          }
          else
          {
            String otherValue = equivalents.get(originalValue);
            if (otherValue.equalsIgnoreCase(returnedValue))
            {
              pass = true;
            }
            else
            {
              otherValue = equivalents.get(returnedValue);
              if (originalValue.equals(otherValue))
              {
                pass = true;
              }
              else
              {
                pass = false;
              }
            }
          }
        }
        else {
        pass = originalValue != null && returnedValue != null && originalValue.equalsIgnoreCase(returnedValue);
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
    comparison.setHl7FieldName("PID-22");
    comparison.setFieldLabel("Ethnic Group");
    comparison.setOriginalValue(vxuReader.getValue(22));
    comparison.setReturnedValue(rspReader.getValue(22));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparison.registerEquivelant("2135-2", "H");
    comparison.registerEquivelant("2186-5", "N");
    comparison.registerEquivelant("H", "2135-2");
    comparison.registerEquivelant("N", "2186-5");
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-25");
    comparison.setFieldLabel("Birth Order");
    comparison.setOriginalValue(vxuReader.getValue(25));
    comparison.setReturnedValue(rspReader.getValue(25));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_REQUIRED);
    comparisonList.add(comparison);

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

    vxuReader.resetPostion();
    rspReader.resetPostion();
    vxuReader.advanceToSegment("NK1");
    rspReader.advanceToSegment("NK1");

    comparison = new Comparison();
    comparison.setHl7FieldName("NK1-2.1");
    comparison.setFieldLabel("Name Last");
    comparison.setOriginalValue(vxuReader.getValue(2));
    comparison.setReturnedValue(rspReader.getValue(2));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("NK1-2.2");
    comparison.setFieldLabel("Name First");
    comparison.setOriginalValue(vxuReader.getValue(2, 2));
    comparison.setReturnedValue(rspReader.getValue(2, 2));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("NK1-2.3");
    comparison.setFieldLabel("Name Middle");
    comparison.setOriginalValue(vxuReader.getValue(2, 3));
    comparison.setReturnedValue(rspReader.getValue(2, 3));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("NK1-2.4");
    comparison.setFieldLabel("Suffix");
    comparison.setOriginalValue(vxuReader.getValue(2, 4));
    comparison.setReturnedValue(rspReader.getValue(2, 4));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
    comparisonList.add(comparison);

    comparison = new Comparison();
    comparison.setHl7FieldName("NK1-2.7");
    comparison.setFieldLabel("Name Type Code");
    comparison.setOriginalValue(vxuReader.getValue(2, 7));
    comparison.setReturnedValue(rspReader.getValue(2, 7));
    comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_EXTRA);
    comparisonList.add(comparison);

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
        if (rspReader.getValue(3).equals(adminDate) && rspReader.getValue(5).equals(cvxCode)) {
          break;
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
        comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
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

      int obxCount = 0;
      int rspReaderSegmentPosition = rspReader.getSegmentPosition();
      while (vxuReader.advanceToSegment("OBX", new String[] { "RXA", "ORC" })) {
        obxCount++;
        String obsId = vxuReader.getValue(3);
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

        } else if (cvxCode.equals("998") && obsId.equals("59784-9")) {
          comparison = new Comparison();
          comparison.setHl7FieldName("OBX-5 #" + count + "." + obxCount);
          comparison.setFieldLabel("History of disease");
          comparison.setOriginalValue(vxuReader.getValue(5));
          comparison.setReturnedValue(rspReader.getValue(5));
          comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
          comparisonList.add(comparison);
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
