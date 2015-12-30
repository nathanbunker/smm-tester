package org.immunizationsoftware.dqa.tester.certify;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.immunizationsoftware.dqa.tester.manager.forecast.ForecastTesterManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestCase;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestEvent;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;

public class CAForecast extends CertifyArea
{

  public CAForecast(CertifyRunner certifyRunner) {
    super("F", VALUE_TEST_SECTION_TYPE_FORECAST, certifyRunner);
  }
  
  protected CAForecast(String areaLetter, String areaLabel, CertifyRunner certifyRunner)
  {
    super(areaLetter, areaLabel, certifyRunner);
  }

  private static final String TCH_FORECAST_TESTER_URL = "http://tchforecasttester.org/ft/ExternalTestServlet";
  // http://localhost:8181/ExternalTestServlet
  // http://tchforecasttester.org/ft/ExternalTestServlet

  @Override
  public void prepareUpdates() {
    List<ForecastTestPanel> ftpList = forecastTestPanelList;
    prepareForecastUpdates(ftpList);
  }

  protected void prepareForecastUpdates(List<ForecastTestPanel> ftpList) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, -18);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String eighteen = sdf.format(calendar.getTime());

    int count = 0;
    certifyRunner.forecastTesterManager = new ForecastTesterManager(TCH_FORECAST_TESTER_URL);
    logStatus("Will connect to " + TCH_FORECAST_TESTER_URL + " to retrieve test cases");
    try {
      for (ForecastTestPanel testPanel : ftpList) {
        logStatus("Downloding Result for Test Panel " + testPanel.getLabel());
        List<ForecastTestCase> forecastTestCaseList = certifyRunner.forecastTesterManager
            .getForecastTestCaseList(testPanel);
        logStatus("Found " + forecastTestCaseList.size() + " test case(s)");
        for (ForecastTestCase forecastTestCase : forecastTestCaseList) {
          count++;
          TestCaseMessage testCaseMessage = new TestCaseMessage();
          testCaseMessage.setForecastTestCase(forecastTestCase);
          StringBuilder sb = new StringBuilder();
          sb.append("MSH|\nPID|\n");
          boolean isUnderEighteen = forecastTestCase.getPatientDob().compareTo(eighteen) > 0;
          if (isUnderEighteen) {
            sb.append("NK1|\n");
          }
          for (ForecastTestEvent forecastTestEvent : forecastTestCase.getForecastTestEventList()) {
            if (forecastTestEvent.getEventTypeCode().equals("V")) {
              sb.append("ORC|\nRXA|\n");
            }
          }

          testCaseMessage.setForecastTestPanel(testPanel);
          testCaseMessage.appendOriginalMessage(sb.toString());
          testCaseMessage.setDescription(testPanel.getLabel() + ": " + forecastTestCase.getCategoryName() + ": "
              + forecastTestCase.getDescription());
          testCaseMessage.setQuickTransformations(
              new String[] { "2.5.1", (forecastTestCase.getPatientSex().equals("M") ? "BOY" : "GIRL"), "ADDRESS",
                  "PHONE", "MOTHER", "RACE", "ETHNICITY" });
          testCaseMessage.appendCustomTransformation("PID-7=" + forecastTestCase.getPatientDob());
          int vaccinationCount = 0;
          for (ForecastTestEvent forecastTestEvent : forecastTestCase.getForecastTestEventList()) {
            if (forecastTestEvent.getEventTypeCode().equals("V")) {
              vaccinationCount++;
              testCaseMessage.appendCustomTransformation("ORC#" + vaccinationCount + "-1=RE");
              testCaseMessage
                  .appendCustomTransformation("ORC#" + vaccinationCount + "-3.1=" + forecastTestEvent.getTestEventId());
              testCaseMessage.appendCustomTransformation("ORC#" + vaccinationCount + "-3.2=TCH-FT");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-1=0");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-2=1");
              testCaseMessage
                  .appendCustomTransformation("RXA#" + vaccinationCount + "-3=" + forecastTestEvent.getEventDate());
              testCaseMessage
                  .appendCustomTransformation("RXA#" + vaccinationCount + "-5.1=" + forecastTestEvent.getVaccineCvx());
              testCaseMessage
                  .appendCustomTransformation("RXA#" + vaccinationCount + "-5.2=" + forecastTestEvent.getLabel());
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-5.3=CVX");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-6=999");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-9.1=01");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-9.2=Historical");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-9.3=NIP001");
              if (!forecastTestEvent.getVaccineMvx().equals("")) {
                testCaseMessage.appendCustomTransformation(
                    "RXA#" + vaccinationCount + "-17.1=" + forecastTestEvent.getVaccineMvx());
                testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-17.3=MVX");
              }
            }
          }
          register(count, 1, testCaseMessage);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      logStatus("Unable to get forecast schedules because: " + e.getMessage());
    }
  }

  @Override
  public void sendUpdates() {
    runUpdates();
  }

  @Override
  public void prepareQueries() {
    doPrepareQueries();
  }

  @Override
  public void sendQueries() {
    runQueries();
  }
}
