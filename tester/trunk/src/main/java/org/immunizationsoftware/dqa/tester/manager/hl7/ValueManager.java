package org.immunizationsoftware.dqa.tester.manager.hl7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;

public class ValueManager
{
  private static Map<String, Set<String>> conceptMap = null;
  private static Map<String, List<CE>> conceptListMap = null;

  public static boolean recognizedConcept(String conceptType) {
    init();
    Set<String> conceptSet = conceptMap.get(conceptType);
    return conceptSet != null;
  }

  public static boolean recognizedValue(String value, String conceptType) {
    init();
    Set<String> conceptSet = conceptMap.get(conceptType);
    if (conceptSet != null) {
      return conceptSet.contains(value);
    }
    return false;
  }

  public static List<CE> getConceptList(String conceptType) {
    init();
    return conceptListMap.get(conceptType);
  }

  public static void init() {
    if (conceptMap == null) {
      try {
        conceptMap = new HashMap<String, Set<String>>();
        conceptListMap = new HashMap<String, List<CE>>();
        BufferedReader in = new BufferedReader(new InputStreamReader(
            ValueManager.class.getResourceAsStream("values.txt")));
        String line;
        while ((line = in.readLine()) != null) {
          int equals = line.indexOf("=");
          if (equals != -1) {
            String concept = line.substring(0, equals);
            String[] values = line.substring(equals + 1).split("\\,");
            Set<String> valueSet = conceptMap.get(concept);
            List<CE> valueList = conceptListMap.get(concept);
            if (valueSet == null) {
              valueSet = new HashSet<String>();
              conceptMap.put(concept, valueSet);
              valueList = new ArrayList<CE>();
              conceptListMap.put(concept, valueList);
            }
            valueSet.add(values[0]);
            CE ce = new CE(concept, UsageType.O);
            ce.getIdentifier().setValue(values[0]);
            if (values.length > 1) {
              ce.getText().setValue(values[1]);
              if (values.length > 2) {
                ce.getNameOfCodingSystem().setValue(values[2]);
              }
            }
            valueList.add(ce);
          }
        }
      } catch (IOException ioe) {
        throw new IllegalArgumentException("Unable to load required values from certify.txt");
      }
    }
  }
}
