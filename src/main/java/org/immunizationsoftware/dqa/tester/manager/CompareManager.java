package org.immunizationsoftware.dqa.tester.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.transform.Comparison;

public class CompareManager
{

  public static boolean acksAppearToBeTheSame(String ackMessageOriginal, String ackMessageCompare) {
    HL7Reader ack1Reader = new HL7Reader(ackMessageOriginal);
    HL7Reader ack2Reader = new HL7Reader(ackMessageCompare);
    if (ack1Reader.advanceToSegment("MSA")) {
      if (ack2Reader.advanceToSegment("MSA")) {
        if (ack1Reader.getValue(1).equals(ack2Reader.getValue(1))) {
          boolean anotherErr1 = ack1Reader.advanceToSegment("ERR");
          boolean anotherErr2 = ack2Reader.advanceToSegment("ERR");
          while (anotherErr1 == anotherErr2) {
            if (!anotherErr1) {
              return true;
            }
            if (!ack1Reader.getValue(4).equals(ack2Reader.getValue(4))) {
              return false;
            }
            if (!ack1Reader.getValue(3).equals(ack2Reader.getValue(3))) {
              return false;
            }
            anotherErr1 = ack1Reader.advanceToSegment("ERR");
            anotherErr2 = ack2Reader.advanceToSegment("ERR");
          }
          return false;
        } else {
          return false;
        }
      } else {
        return false;
      }
    } else if (ack2Reader.advanceToSegment("MSA")) {
      return false;
    }
    return true;
  }

  public static boolean queryReturnedMostImportantData(String vxu, String rsp) {
    HL7Reader vxuReader = new HL7Reader(vxu);
    HL7Reader rspReader = new HL7Reader(rsp);
    if (vxuReader.advanceToSegment("PID")) {
      if (!rspReader.advanceToSegment("PID")) {
        return false;
      }
      {
        String vxuLastName = vxuReader.getValue(5, 1);
        String rspLastName = rspReader.getValue(5, 1);
        if (!vxuLastName.equalsIgnoreCase(rspLastName)) {
          return false;
        }
      }
      {
        String vxuFirstName = vxuReader.getValue(5, 2);
        String rpsFirstName = rspReader.getValue(5, 2);
        if (!vxuFirstName.equalsIgnoreCase(rpsFirstName)) {
          return false;
        }
      }
      {
        String vxuDob = vxuReader.getValue(7);
        String rspDob = rspReader.getValue(7);
        if (!vxuDob.equalsIgnoreCase(rspDob)) {
          return false;
        }
      }
      while(vxuReader.advanceToSegment("RXA"))
      {
        String cvxCode = vxuReader.getValue(5);
        String adminDate = trunc(vxuReader.getValue(3), 8);
        if (cvxCode.equals("") || adminDate.equals("") || cvxCode.equals("999") || cvxCode.equals("998")) {
          continue;
        }
        if (adminDate.length() > 8) {
          adminDate = adminDate.substring(0, 8);
        }
        rspReader.resetPostion();
        boolean found = false;
        while (rspReader.advanceToSegment("RXA")) {
          String rspCvxCode = rspReader.getValue(5);
          String rspCvxCode2 = aliasMap.get(rspCvxCode);
          if (rspCvxCode.equals(cvxCode) || (rspCvxCode2 != null && rspCvxCode2.equals(cvxCode))) {
            if (cvxCode.equals("998")) {
              found = true;
              break;
            } else if (trunc(rspReader.getValue(3), 8).equals(adminDate)) {
              found = true;
              break;
            }
          }
        }
        if (!found)
        {
          return false;
        }
      }
    }
    return true;
  }

  public static List<Comparison> compareMessages(String vxuMessage, String rspMessage) {
    List<Comparison> comparisonList = new ArrayList<Comparison>();
    HL7Reader vxuReader = new HL7Reader(vxuMessage);
    HL7Reader rspReader = new HL7Reader(rspMessage);

    Comparison comparison;

    vxuReader.advanceToSegment("PID");
    rspReader.advanceToSegment("PID");

    comparison = new Comparison();
    comparison.setHl7FieldName("PID-3.1");
    comparison.setFieldLabel("MRN");
    comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 1, "MR", 5));
    if (comparison.getOriginalValue().equals("")) {
      comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 1, "PI", 5));
      if (comparison.getOriginalValue().equals("")) {
        comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 1, "", 5));
      }
    }
    comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 1, "MR", 5));
    if (comparison.getReturnedValue().equals("")) {
      comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 1, "PI", 5));
      if (comparison.getReturnedValue().equals("")) {
        comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 1, "", 5));
      }
    }
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

    if (false) {
      comparison = new Comparison();
      comparison.setHl7FieldName("PID-3.4");
      comparison.setFieldLabel("MRN Assigning Authority");
      comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 4, "MR", 5));
      if (comparison.getOriginalValue().equals("")) {
        comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 4, "PI", 5));
        if (comparison.getOriginalValue().equals("")) {
          comparison.setOriginalValue(vxuReader.getValueBySearchingRepeats(3, 4, "", 5));
        }
      }
      comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 4, "MR", 5));
      if (comparison.getReturnedValue().equals("")) {
        comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 4, "PI", 5));
        if (comparison.getReturnedValue().equals("")) {
          comparison.setReturnedValue(rspReader.getValueBySearchingRepeats(3, 4, "", 5));
        }
      }
      comparison.setPriorityLevel(Comparison.PRIORITY_LEVEL_OPTIONAL);
      comparisonList.add(comparison);
    }

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
      String adminDate = trunc(vxuReader.getValue(3), 8);
      if (cvxCode.equals("") || adminDate.equals("")) {
        // wouldn't expect this to be the case
        continue;
      }
      if (adminDate.length() > 8) {
        adminDate = adminDate.substring(0, 8);
      }
      rspReader.resetPostion();
      while (rspReader.advanceToSegment("RXA")) {
        String rspCvxCode = rspReader.getValue(5);
        if (rspCvxCode.equals(cvxCode)) {
          if (cvxCode.equals("998")) {
            break;
          } else if (trunc(rspReader.getValue(3), 8).equals(adminDate)) {
            break;
          }
        }
      }

      if (!cvxCode.equals("998")) {
        comparison = new Comparison();
        comparison.setHl7FieldName("RXA-3 #" + count);
        comparison.setFieldLabel("Vaccination date");
        comparison.setOriginalValue(trunc(vxuReader.getValue(3), 8));
        comparison.setReturnedValue(trunc(rspReader.getValue(3), 8));
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

  private static final String trunc(String s, int length) {
    if (s != null && s.length() > length) {
      return s.substring(0, length);
    }
    return s;
  }
  
  private static Map<String, String> aliasMap = new HashMap<String, String>();
  static
  {
    aliasMap.put("01", "1");
    aliasMap.put("02", "2");
    aliasMap.put("03", "3");
    aliasMap.put("04", "4");
    aliasMap.put("05", "5");
    aliasMap.put("06", "6");
    aliasMap.put("07", "7");
    aliasMap.put("08", "8");
    aliasMap.put("09", "9");
    aliasMap.put("1", "01");
    aliasMap.put("2", "02");
    aliasMap.put("3", "03");
    aliasMap.put("4", "04");
    aliasMap.put("5", "05");
    aliasMap.put("6", "06");
    aliasMap.put("7", "07");
    aliasMap.put("8", "08");
    aliasMap.put("9", "09");
    aliasMap.put("49281-0623-15", "158");
    aliasMap.put("49281-0515-25", "161");
    aliasMap.put("49281-0415-50", "150");
    aliasMap.put("49281-0415-10", "150");
    aliasMap.put("33332-0115-10", "141");
    aliasMap.put("33332-0015-01", "140");
    aliasMap.put("66019-0302-10", "149");
    aliasMap.put("42874-0015-10", "155");
    aliasMap.put("66521-0118-02", "140");
    aliasMap.put("66521-0118-10", "141");
    aliasMap.put("58160-0883-52", "140");
    aliasMap.put("66521-0000-11", "");
    aliasMap.put("66019-0301-10", "149");
    aliasMap.put("33332-0014-01", "140");
    aliasMap.put("33332-0114-10", "141");
    aliasMap.put("63851-0613-01", "153");
    aliasMap.put("66521-0117-10", "141");
    aliasMap.put("66521-0117-02", "140");
    aliasMap.put("00006-4093-02", "8");
    aliasMap.put("00006-4094-02", "43");
    aliasMap.put("00006-4095-02", "83");
    aliasMap.put("00006-4096-02", "52");
    aliasMap.put("00006-4963-00", "121");
    aliasMap.put("00006-4963-41", "121");
    aliasMap.put("42874-0014-10", "155");
    aliasMap.put("49281-0414-50", "150");
    aliasMap.put("49281-0395-65", "135");
    aliasMap.put("49281-0709-55", "144");
    aliasMap.put("58160-0881-52", "140");
    aliasMap.put("58160-0809-05", "148");
    aliasMap.put("62577-0613-01", "153");
    aliasMap.put("19515-0893-07", "141");
    aliasMap.put("49281-0621-15", "158");
    aliasMap.put("00005-0100-02", "162");
    aliasMap.put("00005-0100-05", "162");
    aliasMap.put("00005-0100-10", "162");
    aliasMap.put("00006-4047-20", "116");
    aliasMap.put("00006-4109-02", "62");
    aliasMap.put("62577-0613-01", "153");
    aliasMap.put("00006-4121-02", "165");
    aliasMap.put("00006-4119-02", "165");
    aliasMap.put("00006-4119-03", "165");
    aliasMap.put("63851-0501-02", "18");
    aliasMap.put("49281-0562-10", "130");
    aliasMap.put("46028-0114-02", "163");
    aliasMap.put("46028-0114-01", "163");
    aliasMap.put("00006-4171-00", "94");
    aliasMap.put("13533-0131-01", "9");
    aliasMap.put("13533-0131-01", "9");
    aliasMap.put("49281-0396-15", "141");
    aliasMap.put("49281-0397-65", "135");
    aliasMap.put("62577-0614-01", "153");
    aliasMap.put("58160-0903-52", "150");
    aliasMap.put("19515-0898-11", "158");
    aliasMap.put("19515-0901-52", "150");
    aliasMap.put("49281-0393-65", "135");
    aliasMap.put("58160-0810-52", "20");
    aliasMap.put("58160-0810-11", "20");
    aliasMap.put("49281-0790-51", "101");
    aliasMap.put("49281-0790-20", "101");
    aliasMap.put("49281-0640-15", "127");
    aliasMap.put("49281-0650-10", "126");
    aliasMap.put("49281-0650-90", "126");
    aliasMap.put("49281-0650-25", "126");
    aliasMap.put("49281-0650-70", "126");
    aliasMap.put("49281-0650-50", "126");
    aliasMap.put("49281-0707-55", "144");
    aliasMap.put("66019-0110-10", "111");
    aliasMap.put("00006-4681-00", "3");
    aliasMap.put("49281-0215-10", "113");
    aliasMap.put("49281-0215-15", "113");
    aliasMap.put("46028-0208-01", "136");
    aliasMap.put("49281-0387-65", "135");
    aliasMap.put("49281-0386-15", "141");
    aliasMap.put("49281-0010-10", "140");
    aliasMap.put("49281-0010-25", "140");
    aliasMap.put("49281-0010-50", "140");
    aliasMap.put("54868-0980-00", "3");
    aliasMap.put("58160-0830-52", "118");
    aliasMap.put("58160-0830-34", "118");
    aliasMap.put("00005-1971-04", "133");
    aliasMap.put("00005-1971-05", "133");
    aliasMap.put("00005-1971-02", "133");
    aliasMap.put("17478-0131-01", "9");
    aliasMap.put("17478-0131-01", "9");
    aliasMap.put("54868-2219-01", "");
    aliasMap.put("54868-2219-00", "");
    aliasMap.put("58160-0812-52", "130");
    aliasMap.put("58160-0812-11", "130");
    aliasMap.put("66019-0108-10", "111");
    aliasMap.put("00006-4898-00", "51");
    aliasMap.put("58160-0801-11", "148");
    aliasMap.put("63851-0612-01", "153");
    aliasMap.put("00006-4992-00", "44");
    aliasMap.put("00006-4981-00", "8");
    aliasMap.put("00006-4980-00", "8");
    aliasMap.put("00006-4093-09", "8");
    aliasMap.put("00006-4094-09", "43");
    aliasMap.put("00006-4109-09", "62");
    aliasMap.put("55045-3841-01", "");
    aliasMap.put("66521-0112-02", "140");
    aliasMap.put("66521-0112-10", "141");
    aliasMap.put("66019-0107-01", "111");
    aliasMap.put("49281-0225-10", "28");
    aliasMap.put("49281-0705-55", "144");
    aliasMap.put("42515-0001-01", "134");
    aliasMap.put("58160-0879-52", "140");
    aliasMap.put("58160-0880-52", "140");
    aliasMap.put("58160-0820-11", "8");
    aliasMap.put("58160-0820-52", "8");
    aliasMap.put("58160-0821-11", "43");
    aliasMap.put("58160-0821-52", "43");
    aliasMap.put("58160-0821-34", "43");
    aliasMap.put("42874-0012-10", "155");
    aliasMap.put("49281-0278-10", "28");
    aliasMap.put("66019-0109-10", "111");
    aliasMap.put("19515-0890-07", "141");
    aliasMap.put("19515-0889-07", "141");
    aliasMap.put("58160-0900-52", "150");
    aliasMap.put("49281-0860-55", "10");
    aliasMap.put("49281-0860-10", "10");
    aliasMap.put("49281-0389-65", "135");
    aliasMap.put("49281-0388-15", "141");
    aliasMap.put("49281-0011-10", "140");
    aliasMap.put("49281-0011-50", "140");
    aliasMap.put("49281-0703-55", "144");
    aliasMap.put("49281-0111-25", "140");
    aliasMap.put("54868-0734-00", "");
    aliasMap.put("66521-0200-10", "126");
    aliasMap.put("66521-0200-02", "127");
    aliasMap.put("54868-6177-00", "");
    aliasMap.put("54868-6180-00", "");
    aliasMap.put("49281-0298-10", "20");
    aliasMap.put("58160-0806-05", "48");
    aliasMap.put("33332-0013-01", "140");
    aliasMap.put("33332-0113-10", "141");
    aliasMap.put("49281-0391-65", "135");
    aliasMap.put("49281-0012-50", "140");
    aliasMap.put("49281-0012-10", "140");
    aliasMap.put("49281-0112-25", "140");
    aliasMap.put("49281-0390-15", "141");
    aliasMap.put("00006-4897-00", "49");
    aliasMap.put("49281-0291-83", "113");
    aliasMap.put("49281-0291-10", "113");
    aliasMap.put("49281-0413-50", "150");
    aliasMap.put("49281-0413-10", "150");
    aliasMap.put("49281-0513-25", "161");
    aliasMap.put("66521-0113-02", "140");
    aliasMap.put("66521-0113-10", "141");
    aliasMap.put("00005-1970-50", "100");
    aliasMap.put("00006-4047-41", "116");
    aliasMap.put("58160-0811-52", "110");
    aliasMap.put("58160-0811-51", "110");
    aliasMap.put("00006-4827-00", "21");
    aliasMap.put("00006-4826-00", "21");
    aliasMap.put("58160-0842-51", "115");
    aliasMap.put("58160-0842-11", "115");
    aliasMap.put("58160-0842-52", "115");
    aliasMap.put("58160-0842-34", "115");
    aliasMap.put("66019-0300-10", "149");
    aliasMap.put("49281-0589-05", "114");
    aliasMap.put("00006-4095-09", "83");
    aliasMap.put("00006-4096-09", "52");
    aliasMap.put("00006-4831-41", "83");
    aliasMap.put("63851-0501-01", "18");
    aliasMap.put("66019-0200-10", "125");
    aliasMap.put("14362-0111-04", "9");
    aliasMap.put("33332-0519-01", "126");
    aliasMap.put("33332-0519-25", "126");
    aliasMap.put("33332-0629-10", "127");
    aliasMap.put("58160-0825-52", "83");
    aliasMap.put("58160-0825-11", "83");
    aliasMap.put("58160-0826-52", "52");
    aliasMap.put("58160-0826-34", "52");
    aliasMap.put("58160-0826-11", "52");
    aliasMap.put("33332-0010-01", "140");
    aliasMap.put("33332-0110-10", "141");
    aliasMap.put("66521-0115-10", "141");
    aliasMap.put("66521-0115-02", "140");
    aliasMap.put("00006-4133-41", "9");
    aliasMap.put("00006-4133-41", "9");
    aliasMap.put("54868-4320-00", "33");
    aliasMap.put("54868-3339-01", "33");
    aliasMap.put("00052-0603-02", "19");
    aliasMap.put("64678-0211-01", "24");
    aliasMap.put("76420-0470-10", "");
    aliasMap.put("00006-4739-00", "33");
    aliasMap.put("00006-4943-00", "33");
    aliasMap.put("49281-0013-10", "140");
    aliasMap.put("49281-0013-50", "140");
    aliasMap.put("49281-0392-15", "141");
    aliasMap.put("49281-0113-25", "140");
    aliasMap.put("49281-0820-10", "35");
    aliasMap.put("49281-0800-83", "35");
    aliasMap.put("49281-0400-15", "115");
    aliasMap.put("76420-0483-01", "");
    aliasMap.put("76420-0482-01", "");
    aliasMap.put("49281-0286-01", "106");
    aliasMap.put("49281-0286-05", "106");
    aliasMap.put("49281-0286-10", "106");
    aliasMap.put("21695-0413-01", "");
    aliasMap.put("58160-0815-46", "104");
    aliasMap.put("58160-0815-52", "104");
    aliasMap.put("58160-0815-34", "104");
    aliasMap.put("58160-0815-48", "104");
    aliasMap.put("58160-0815-11", "104");
    aliasMap.put("66521-0114-10", "141");
    aliasMap.put("66521-0114-02", "140");
    aliasMap.put("19515-0895-11", "158");
    aliasMap.put("76420-0471-01", "");
    aliasMap.put("00006-4999-00", "94");
    aliasMap.put("49281-0510-05", "120");
    aliasMap.put("51285-0138-50", "143");
    aliasMap.put("49281-0250-51", "18");
    aliasMap.put("49281-0489-01", "32");
    aliasMap.put("49281-0915-01", "37");
    aliasMap.put("49281-0545-05", "48");
    aliasMap.put("58160-0854-52", "119");
    aliasMap.put("49281-0915-05", "37");
    aliasMap.put("49281-0489-91", "32");
    aliasMap.put("46028-0208-01", "136");
    aliasMap.put("51285-0138-50", "143");
    aliasMap.put("49281-0510-05", "120");
    aliasMap.put("49281-0400-05", "115");
    aliasMap.put("49281-0400-10", "115");
    aliasMap.put("42874-0013-10", "155");
    aliasMap.put("66521-0116-10", "141");
    aliasMap.put("66521-0116-02", "140");
    aliasMap.put("00006-4045-00", "62");
    aliasMap.put("00006-4045-41", "62");
    aliasMap.put("00006-4995-41", "43");
    aliasMap.put("00006-4995-00", "43");
    aliasMap.put("00006-4841-41", "52");
    aliasMap.put("00006-4841-00", "52");
    aliasMap.put("58160-0808-15", "160");
    aliasMap.put("58160-0808-15", "160");
    aliasMap.put("58160-0901-52", "150");
    aliasMap.put("00006-4837-03", "33");
    aliasMap.put("19515-0891-11", "158");
    aliasMap.put("19515-0893-07", "141");
    aliasMap.put("19515-0894-52", "150");
    aliasMap.put("49281-0514-25", "161");
    aliasMap.put("49281-0394-15", "141");
    aliasMap.put("49281-0014-50", "140");
    aliasMap.put("49281-0414-10", "150");

  }
}
