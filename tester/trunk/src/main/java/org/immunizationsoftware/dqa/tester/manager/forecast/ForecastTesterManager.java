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
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.forecast.ForecastExpected;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestCase;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestEvent;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;

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

  private String forecastTesterUrl = null;

  public ForecastTesterManager(String forecastTesterUrl) {
    this.forecastTesterUrl = forecastTesterUrl;
  }

  public List<ForecastTestCase> getForecastTestCaseList(ForecastTestPanel testPanel) throws IOException {

    List<ForecastTestCase> forecastTestCaseList = new ArrayList<ForecastTestCase>();
    HttpURLConnection urlConn;
    URL url = new URL(forecastTesterUrl + "?testPanelId=" + testPanel.getId());

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

  public String reportForecastResults(TestCaseMessage queryTestCaseMessage, int softwareId) throws IOException {
    StringBuilder submittedResults = new StringBuilder();

    for (ForecastActual forecastActual : queryTestCaseMessage.getForecastActualList()) {

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
      sb.append(POST_TEST_CASE_ID + "=" + queryTestCaseMessage.getForecastTestCase().getTestCaseId());
      sb.append("&");
      sb.append(POST_SOFTWARE_ID + "=" + softwareId);
      sb.append("&");
      sb.append(POST_FORECAST_CVX + "=" + forecastActual.getVaccineCvx());
      sb.append("&");
      sb.append(POST_LOG + "=");
      BufferedReader buffReader = new BufferedReader(
          new StringReader(queryTestCaseMessage.getActualMessageResponseType()));
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
      submittedResults.append(sb + "\n");
      if ((line = in.readLine()) != null) {
        submittedResults.append(" + " + line + "\n");
      }
    }
    return submittedResults.toString();
  }

  public static void readForecastActual(TestCaseMessage queryTestCaseMessage) {
    List<ForecastActual> forecastActualList = new ArrayList<ForecastActual>();
    queryTestCaseMessage.setForecastActualList(forecastActualList);
    List<EvaluationActual> evaluationActualList = new ArrayList<EvaluationActual>();
    queryTestCaseMessage.setEvaluationActualList(evaluationActualList);
    ForecastActual forecastActual = null;
    EvaluationActual evaluationActual = null;
    boolean addEvaluationToList = false;
    boolean addForecastToList = false;
    HL7Reader reader = new HL7Reader(queryTestCaseMessage.getActualResponseMessage());
    boolean isEvaluation = false;
    boolean isForecast = false;
    String vaccineCvx = "";
    String vaccineDate = "";
    while (reader.advance()) {
      if (reader.getSegmentName().equals("RXA")) {
        addToList(forecastActualList, evaluationActualList, forecastActual, evaluationActual, addEvaluationToList,
            addForecastToList);
        addEvaluationToList = false;
        addForecastToList = false;
        forecastActual = null;
        evaluationActual = null;
        vaccineDate = reader.getValue(3);
        vaccineCvx = reader.getValue(5);
        if (vaccineCvx.equals("998")) {
          isEvaluation = false;
          isForecast = true;
        } else {
          isEvaluation = true;
          isForecast = true;
        }
      } else if (reader.getSegmentName().equals("OBX")) {
        String obs = reader.getValue(3);
        String obs2 = reader.getValue(3, 1, 2);
        String obsValue = reader.getValue(5);
        if (isForecast) {
          if (obs.equals("30956-7") || (obs.equals("30979-9") && obs2.equals("")) || obs.equals("38890-0")) {
            if (forecastActual != null) {
              addToList(forecastActualList, evaluationActualList, forecastActual, evaluationActual, false,
                  addForecastToList);
            }
            forecastActual = new ForecastActual();
            forecastActual.setVaccineCvx(obsValue);
            addForecastToList = !isEvaluation;
          } else if (forecastActual != null) {
            if (obs.equals("30980-7") || (obs.equals("30979-9") && obs2.equals("30980-7"))) {
              addForecastToList = true;
              forecastActual.setDueDate(obsValue);
            } else if (obs.equals("30981-5")) {
              forecastActual.setValidDate(obsValue);
            } else if (obs.equals("30973-2")) {
              forecastActual.setDoseNumber(obsValue);
            } else if (obs.equals("30982-3")) {
              forecastActual.setReasonCode(obsValue);
            } else if (obs.equals("59779-9")) {
              forecastActual.setScheduleName(obsValue);
            } else if (obs.equals("59777-3")) {
              forecastActual.setFinishedDate(obsValue);
            } else if (obs.equals("59778-1")) {
              forecastActual.setOverdueDate(obsValue);
            } else if (obs.equals("59783-1")) {
              forecastActual.setSeriesStatus(obsValue);
            } else if (obs.equals("59780-7")) {
              forecastActual.setSeriesName(obsValue);
            } else if (obs.equals("59782-3")) {
              forecastActual.setSeriesDoseCount(obsValue);
            }
          }
        }
        if (isEvaluation) {
          if (obs.equals("30956-7") || obs.equals("30979-9") || obs.equals("38890-0")) {
            evaluationActual = new EvaluationActual();
            evaluationActual.setVaccineCvx(vaccineCvx);
            evaluationActual.setVaccineDate(vaccineDate);
            evaluationActual.setComponentCvx(obsValue);
          } else if (evaluationActual != null) {
            if (obs.equals("30973-2")) {
              evaluationActual.setDoseNumber(obsValue);
              addEvaluationToList = true;
            } else if (obs.equals("59779-9")) {
              evaluationActual.setScheduleName(obsValue);
              addEvaluationToList = true;
            } else if (obs.equals("30982-3")) {
              evaluationActual.setReasonCode(obsValue);
            } else if (obs.equals("59780-7")) {
              evaluationActual.setSeriesName(obsValue);
            } else if (obs.equals("59781-5")) {
              evaluationActual.setDoseValidity(obsValue);
            } else if (obs.equals("59782-3")) {
              evaluationActual.setSeriesDoseCount(obsValue);
            }
          }
        }
      }
    }
    addToList(forecastActualList, evaluationActualList, forecastActual, evaluationActual, addEvaluationToList,
        addForecastToList);

  }

  public static void addToList(List<ForecastActual> forecastActualList, List<EvaluationActual> evaluationActualList,
      ForecastActual forecastActual, EvaluationActual evaluationActual, boolean addEvaluationToList,
      boolean addForecastToList) {
    if (addEvaluationToList) {
      evaluationActualList.add(evaluationActual);
    }
    if (addForecastToList) {
      forecastActualList.add(forecastActual);
    }
  }
}
