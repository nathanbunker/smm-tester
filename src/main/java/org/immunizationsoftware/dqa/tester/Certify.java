package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Certify
{

  public static final String FIELD_SEX = "SEX";
  public static final String FIELD_RACE = "RACE";
  public static final String FIELD_ETHNICITY = "ETHNICITY";
  public static final String FIELD_HISTORICAL = "HISTORICAL";
  public static final String FIELD_VFC = "VFC";
  public static final String FIELD_HOD = "HOD";
  public static final String FIELD_REGISTRY_STATUS = "REGISTRY STATUS";
  public static final String FIELD_REFUSAL_REASON = "REFUSAL REASON";

  private static Map<String, List<String[]>> conceptMap = null;

  public static class CertifyItem
  {
    private String code = "";
    private String label = "";
    private String table = "";

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getLabel() {
      return label;
    }

    public void setLabel(String label) {
      this.label = label;
    }

    public String getTable() {
      return table;
    }

    public void setTable(String table) {
      this.table = table;
    }

  }

  protected void init() {
    try {
      conceptMap = new HashMap<String, List<String[]>>();
      BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("certify.txt")));
      String line;
      while ((line = in.readLine()) != null) {
        int equals = line.indexOf("=");
        if (equals != -1) {
          String concept = line.substring(0, equals);
          String[] values = line.substring(equals + 1).split("\\,");
          List<String[]> valueList = conceptMap.get(concept);
          if (valueList == null) {
            valueList = new ArrayList<String[]>();
            conceptMap.put(concept, valueList);
          }
          valueList.add(values);
        }
      }
    } catch (IOException ioe) {
      throw new IllegalArgumentException("Unable to load required values from certify.txt");
    }
  }

  public List<CertifyItem> getCertifyItemList(String concept) {
    List<CertifyItem> certifyItemList = new ArrayList<Certify.CertifyItem>();

    if (conceptMap == null) {
      init();
    }
    List<String[]> valueList = conceptMap.get(concept);
    if (valueList != null) {
      for (String[] values : valueList) {
        CertifyItem certifyItem = new CertifyItem();
        certifyItem.setCode(readValue(values, 0));
        certifyItem.setLabel(readValue(values, 1));
        certifyItem.setTable(readValue(values, 2));
        certifyItemList.add(certifyItem);
      }
    }

    return certifyItemList;
  }

  private String readValue(String[] values, int pos) {
    if (pos < values.length) {
      return values[pos];
    }
    return "";
  }

}
