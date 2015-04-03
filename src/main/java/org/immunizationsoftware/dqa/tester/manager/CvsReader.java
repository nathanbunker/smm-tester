package org.immunizationsoftware.dqa.tester.manager;

import java.util.ArrayList;
import java.util.List;

public class CvsReader
{
  public static List<String> readValuesFromCsv(String line) {
    ArrayList<String> valueList = new ArrayList<String>();
    String value = "";
    boolean inQuote = false;
    for (int i = 0; i < line.length(); i++) {
      char curr = line.charAt(i);
      char peak = (i + 1) < line.length() ? line.charAt(i + 1) : 0;
      if (curr == '"') {
        if (inQuote) {
          if (peak == '"') {
            value += curr;
            i++;
            continue;
          } else {
            inQuote = false;
          }
        } else if (value.length() > 0) {
          value += curr;
        } else {
          inQuote = true;
        }
      } else if (curr == ',' && !inQuote) {
        valueList.add(value.trim());
        value = "";
      } else {
        value += curr;
      }
    }
    valueList.add(value.trim());
    return valueList;
  }
  
  public static String readValue(int pos, List<String> valueList) {
    if (pos >= 0 && valueList.size() > pos) {
      return valueList.get(pos);
    }
    return "";
  }
}
