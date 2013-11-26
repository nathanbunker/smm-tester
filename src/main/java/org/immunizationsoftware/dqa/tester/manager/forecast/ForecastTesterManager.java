package org.immunizationsoftware.dqa.tester.manager.forecast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.immunizationsoftware.dqa.tester.connectors.HttpConnector.AuthenticationMethod;

public class ForecastTesterManager
{

  private static final String TPC_TEST_PANEL_CASE_ID = "testPanelCase.testPanelCaseId";
  private static final String TPC_CATEGORY_NAME = "testPanelCase.categoryName";
  private static final String TPC_TEST_CASE_NUMBER = "testPanelCase.testCaseNumber";
  private static final String TC_TEST_CASE_ID = "testCase.testCaseId";
  private static final String TC_LABEL = "testCase.label";
  private static final String TC_DESCRIPTION = "testCase.description";
  private static final String TC_EVAL_DATE = "testCase.evalDate";
  private static final String TC_PATIENT_FIRST = "testCase.patientFirst";
  private static final String TC_PATIENT_LAST = "testCase.patientLast";
  private static final String TC_PATIENT_SEX = "testCase.patientSex";
  private static final String TC_PATIENT_DOB = "testCase.patientDob";
  private static final String TE_TEST_EVENT_ID = "testEvent.testEventId";
  private static final String TE_LABEL = "testEvent.label";
  private static final String TE_EVENT_TYPE_CODE = "testEvent.eventTypeCode";
  private static final String TE_VACCINE_CVX = "testEvent.vaccineCvx";
  private static final String TE_VACCINE_MVX = "testEvent.vaccineMvx";
  private static final String TE_EVENT_DATE = "testEvent.eventDate";
  private static final String FE_FORECAST_EXPECTED_ID = "forecastExpected.forecastExpectedId";
  private static final String FE_DOSE_NUMBER = "forecastExpected.doseNumber";
  private static final String FE_VALID_DATE = "forecastExpected.validDate";
  private static final String FE_DUE_DATE = "forecastExpected.dueDate";
  private static final String FE_OVERDUE_DATE = "forecastExpected.overdueDate";
  private static final String FE_FINISHED_DATE = "forecastExpected.finishedDate";
  private static final String FE_VACCINE_CVX = "forecastExpected.vaccineCvx";

  public static final int TEST_PANEL_TCH_INITIAL = 2;
  public static final int TEST_PANEL_CDSI_TEST_CASES_V1_0 = 13;
  public static final int TEST_PANEL_IHS_ROLLOUT_2013 = 18;
  public static final int TEST_PANEL_IHS_FROM_RPMS = 12;
  public static final int TEST_PANEL_VDH = 15;

  private String forecastTesterUrl = null;

  public ForecastTesterManager(String forecastTesterUrl) {
    this.forecastTesterUrl = forecastTesterUrl;
  }

  public List<ForecastTestCase> getForecastTestCaseList(int testPanelId) throws IOException {

    List<ForecastTestCase> forecastTestCaseList = new ArrayList<ForecastTestCase>();
    HttpURLConnection urlConn;
    URL url = new URL(forecastTesterUrl + "?testPanelId=" + testPanelId);

    urlConn = (HttpURLConnection) url.openConnection();
    urlConn.setRequestMethod("GET");
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    urlConn.setUseCaches(false);

    ForecastTestCase forecastTestCase = null;
    ForecastExpected forecastExpected = null;
    ForecastTestEvent forecastTestEvent = null;

    InputStreamReader input = null;
    input = new InputStreamReader(urlConn.getInputStream());
    BufferedReader in = new BufferedReader(input);
    String line;
    while ((line = in.readLine()) != null) {
      int pos = line.indexOf("=");
      if (pos != -1) {
        String name = line.substring(0, pos).trim();
        String value = line.substring(pos + 1).trim();
        if (value.equals("")) {
          continue;
        }
        if (name.equals(TPC_TEST_PANEL_CASE_ID)) {
          forecastTestCase = new ForecastTestCase();
          forecastTestCaseList.add(forecastTestCase);
          forecastTestCase.setTestCaseId(Integer.parseInt(value));
          forecastExpected = null;
          forecastTestEvent = null;
        } else if (forecastTestCase != null) {
          if (name.equals(TPC_CATEGORY_NAME)) {
            forecastTestCase.setCategoryName(value);
          } else if (name.equals(TPC_TEST_CASE_NUMBER)) {
            forecastTestCase.setTestCaseNumber(value);
          } else if (name.equals(TC_TEST_CASE_ID)) {
            forecastTestCase.setTestCaseId(Integer.parseInt(value));
          } else if (name.equals(TC_LABEL)) {
            forecastTestCase.setLabel(value);
          } else if (name.equals(TC_DESCRIPTION)) {
            forecastTestCase.setDescription(value);
          } else if (name.equals(TC_EVAL_DATE)) {
            forecastTestCase.setEvalDate(value);
          } else if (name.equals(TC_PATIENT_FIRST)) {
            forecastTestCase.setPatientFirst(value);
          } else if (name.equals(TC_PATIENT_LAST)) {
            forecastTestCase.setPatientLast(value);
          } else if (name.equals(TC_PATIENT_SEX)) {
            forecastTestCase.setPatientSex(value);
          } else if (name.equals(TC_PATIENT_DOB)) {
            forecastTestCase.setPatientDob(value);
          } else if (name.equals(TE_TEST_EVENT_ID)) {
            forecastTestEvent = new ForecastTestEvent();
            forecastTestEvent.setTestEventId(Integer.parseInt(value));
            forecastTestCase.getForecastTestEventList().add(forecastTestEvent);
            forecastExpected = null;
          } else if (name.equals(FE_FORECAST_EXPECTED_ID)) {
            forecastExpected = new ForecastExpected();
            forecastExpected.setForecastExpectedId(Integer.parseInt(value));
            forecastTestCase.getForecastExpectedList().add(forecastExpected);
            forecastTestEvent = null;
          } else {
            if (forecastTestEvent != null) {
              if (name.equals(TE_LABEL)) {
                forecastTestEvent.setLabel(value);
              } else if (name.equals(TE_EVENT_TYPE_CODE)) {
                forecastTestEvent.setEventTypeCode(value);
              } else if (name.equals(TE_VACCINE_CVX)) {
                forecastTestEvent.setVaccineCvx(value);
              } else if (name.equals(TE_VACCINE_MVX)) {
                forecastTestEvent.setVaccineMvx(value);
              } else if (name.equals(TE_EVENT_DATE)) {
                forecastTestEvent.setEventDate(value);
              }
            }
            if (forecastExpected != null) {
              if (name.equals(FE_DOSE_NUMBER)) {
                forecastExpected.setDoseNumber(value);
              } else if (name.equals(FE_VALID_DATE)) {
                forecastExpected.setValidDate(value);
              } else if (name.equals(FE_DUE_DATE)) {
                forecastExpected.setDueDate(value);
              } else if (name.equals(FE_OVERDUE_DATE)) {
                forecastExpected.setOverdueDate(value);
              } else if (name.equals(FE_FINISHED_DATE)) {
                forecastExpected.setFinishedDate(value);
              } else if (name.equals(FE_VACCINE_CVX)) {
                forecastExpected.setVaccineCvx(value);
              }
            }
          }
        }
      }
    }
    input.close();

    return forecastTestCaseList;
  }

  private static final String POST_FINISHED_DATE = "finishedDate";
  private static final String POST_OVERDUE_DATE = "overdueDate";
  private static final String POST_DUE_DATE = "dueDate";
  private static final String POST_VALID_DATE = "validDate";
  private static final String POST_DOSE_NUMBER = "doseNumber";
  private static final String POST_LOG = "log";
  // private static final String POST_SCHEDULE_NAME = "scheduleName";
  private static final String POST_FORECAST_CVX = "forecastCvx";
  private static final String POST_SOFTWARE_ID = "softwareId";
  private static final String POST_TEST_CASE_ID = "testCaseId";

  public void reportForecastResults(ForecastTestCase forecastTestCase, String rspMessage, int softwareId)
      throws IOException {
    List<ForecastActual> forecastActualList = readForecastActual(rspMessage);

    for (ForecastActual forecastActual : forecastActualList) {

      DataOutputStream printout;
      URL url = new URL(forecastTesterUrl);
      HttpURLConnection urlConn;
      urlConn = (HttpURLConnection) url.openConnection();
      urlConn.setRequestMethod("POST");
      urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);

      StringBuilder sb = new StringBuilder();
      sb.append(POST_TEST_CASE_ID + "=" + forecastTestCase.getTestCaseId());
      sb.append("&");
      sb.append(POST_SOFTWARE_ID + "=" + softwareId);
      sb.append("&");
      sb.append(POST_FORECAST_CVX + "=" + forecastActual.getVaccineCvx());
      sb.append("&");
      sb.append(POST_LOG + "=");
      BufferedReader buffReader = new BufferedReader(new StringReader(rspMessage));
      String line;
      while ((line = buffReader.readLine()) != null) {
        sb.append(URLEncoder.encode(line + "\n", "UTF-8"));
      }
      sb.append("&");
      sb.append(POST_DOSE_NUMBER + "=" + forecastActual.getDoseNumber());
      sb.append("&");
      sb.append(POST_VALID_DATE + "=" + forecastActual.getValidDate());
      sb.append("&");
      sb.append(POST_DUE_DATE + "=" + forecastActual.getDueDate());
      sb.append("&");
      sb.append(POST_OVERDUE_DATE + "=" + forecastActual.getOverdueDate());
      sb.append("&");
      sb.append(POST_FINISHED_DATE + "=" + forecastActual.getFinishedDate());

      printout = new DataOutputStream(urlConn.getOutputStream());
      printout.writeBytes(sb.toString());
      printout.flush();
      printout.close();
      InputStreamReader input = null;
      input = new InputStreamReader(urlConn.getInputStream());
      BufferedReader in = new BufferedReader(input);
      if ((line = in.readLine()) != null) {
        // should be OK
      }
    }
  }

  protected static List<ForecastActual> readForecastActual(String rspMessage) {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    ForecastActual forecastActual = null;
    BufferedReader in = new BufferedReader(new StringReader(rspMessage));
    String line;
    try {
      while ((line = in.readLine()) != null) {
        if (line.startsWith("OBX|")) {
          String[] fields = line.split("\\|");
          if (fields.length > 5 && fields[3] != null) {
            if (fields[3].startsWith("30979-9^")) {
              forecastActual = new ForecastActual();
              forecastActualList.add(forecastActual);
              if (fields[5] != null) {
                String[] subFields = fields[5].split("\\^");
                if (subFields.length > 0 && subFields[0] != null) {
                  forecastActual.setVaccineCvx(subFields[0]);
                }
              }
            } else if (forecastActual != null) {
              if (fields[3].startsWith("30979-9&30980-7") || fields[3].startsWith("30980-7")) {
                if (fields[5] != null) {
                  String[] subFields = fields[5].split("\\^");
                  if (subFields.length > 0 && subFields[0] != null) {
                    forecastActual.setDueDate(subFields[0]);
                  }
                }
              } else if (fields[3].startsWith("30981-5")) {
                if (fields[5] != null) {
                  String[] subFields = fields[5].split("\\^");
                  if (subFields.length > 0 && subFields[0] != null) {
                    forecastActual.setValidDate(subFields[0]);
                  }
                }
              } else if (fields[3].startsWith("30973-2")) {
                if (fields[5] != null) {
                  String[] subFields = fields[5].split("\\^");
                  if (subFields.length > 0 && subFields[0] != null) {
                    forecastActual.setDoseNumber(subFields[0]);
                  }
                }
              }
            }
          }
        }
      }
    } catch (IOException e) {
      // not expecting io exception while reading string
      e.printStackTrace();
    }
    return forecastActualList;
  }
}
