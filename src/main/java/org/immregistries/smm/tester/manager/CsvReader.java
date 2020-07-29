package org.immregistries.smm.tester.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

  private boolean inQuote = false;
  private String value = "";
  private ArrayList<String> valueList;
  private char prevChar = 0;

  public List<String> readValuesFromCsv(BufferedReader in) throws IOException {
    String line = in.readLine();
    if (line == null) {
      return null;
    }
    valueList = new ArrayList<String>();
    inQuote = false;
    value = "";
    prevChar = 0;
    readLineOfCsv(line);
    while (inQuote && (line = in.readLine()) != null) {
      value += "\n";
      readLineOfCsv(line);
    }
    valueList.add(value.trim());
    return valueList;
  }

  private void readLineOfCsv(String line) {
    for (int i = 0; i < line.length(); i++) {
      char curr = line.charAt(i);
      if (prevChar == ',' && curr == ' ') {
        continue;
      }
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
      prevChar = curr;
    }
  }

  public static List<String> readValuesFromCsv(String line) {
    ArrayList<String> valueList = new ArrayList<String>();
    String value = "";
    boolean inQuote = false;
    char prev = 0;
    for (int i = 0; i < line.length(); i++) {
      char curr = line.charAt(i);
      if (prev == ',' && curr == ' ') {
        continue;
      }
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
      prev = curr;
    }
    valueList.add(value.trim());
    return valueList;
  }

  public static int readValueInt(int pos, List<String> valueList) {
    String valueString = readValue(pos, valueList);
    if (valueString.equals("")) {
      return 0;
    }
    try {
      return Integer.parseInt(valueString);
    } catch (NumberFormatException nfe) {
      return 0;
    }
  }

  public static String readValue(int pos, List<String> valueList) {
    if (pos >= 0 && valueList.size() > pos) {
      return valueList.get(pos);
    }
    return "";
  }

  public static int findPosition(String headerName, List<String> valueList) {
    int pos = 0;
    for (String value : valueList) {
      if (value.equalsIgnoreCase(headerName)) {
        return pos;
      }
      pos++;
    }
    return -1;
  }
}
