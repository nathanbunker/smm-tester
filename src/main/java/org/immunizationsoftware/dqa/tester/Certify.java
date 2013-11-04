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
  public static final String FIELD_RELATIONSHIP = "RELATIONSHIP";
  public static final String FIELD_CPT = "CPT";
  public static final String FIELD_BODY_ROUTE = "BODY ROUTE";
  public static final String FIELD_BODY_SITE = "BODY SITE";
  public static final String FIELD_VAC_HISTORICAL_TWO_MONTHS = "VAC HISTORICAL 2 MONTHS";
  public static final String FIELD_VAC_HISTORICAL_TWO_YEARS = "VAC HISTORICAL 2 YEARS";
  public static final String FIELD_VAC_HISTORICAL_FOUR_YEARS = "VAC HISTORICAL 4 YEARS";
  public static final String FIELD_VAC_HISTORICAL_TWELVE_YEARS = "VAC HISTORICAL 12 MONTHS";
  public static final String FIELD_VAC_ADMIN_TWO_MONTHS = "VAC ADMIN 2 MONTHS";
  public static final String FIELD_VAC_ADMIN_TWO_YEARS = "VAC ADMIN 2 YEARS";
  public static final String FIELD_VAC_ADMIN_FOUR_YEARS = "VAC ADMIN 4 YEARS";
  public static final String FIELD_VAC_ADMIN_TWELVE_YEARS = "VAC ADMIN 12 MONTHS";
  public static final String FIELD_VAC_PRODUCT = "VAC PRODUCT";
  public static final String FIELD_MVX = "MVX";
  public static final String FIELD_ADDRESS_TYPE = "ADDRESS TYPE";
  public static final String FIELD_NAME_TYPE = "NAME TYPE";
  public static final String FIELD_TEL_USE_CODE = "TEL USE CODE";
  public static final String FIELD_TEL_EQUIPMENT_TYPE = "TEL EQUIPMENT TYPE";
  public static final String FIELD_ID_TYPE = "ID TYPE";
  public static final String FIELD_PUBLICITY_CODE = "PUBLICITY CODE";
  public static final String FIELD_COUNTY_CODE = "COUNTY CODE";
  public static final String FIELD_LANGUAGE = "LANGUAGE";
  public static final String FIELD_LANGUAGE_FULL = "LANGUAGE FULL";
  public static final String FIELD_RACE_FULL = "RACE FULL";
  public static final String FIELD_COMPLETION = "COMPLETION";
  public static final String FIELD_ACTION = "ACTION";
  public static final String FIELD_DEGREE = "DEGREE";
  public static final String FIELD_ = "";

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
      if (values[pos] == null)
      {
        return "";
      }
      return values[pos].trim();
    }
    return "";
  }

}
