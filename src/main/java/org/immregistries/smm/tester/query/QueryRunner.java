package org.immregistries.smm.tester.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.tester.certify.CAForecast;
import org.immregistries.smm.tester.certify.CertifyRunner;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.CvsReader;
import org.immregistries.smm.tester.manager.TestParticipant;
import org.immregistries.smm.tester.manager.forecast.EvaluationActual;
import org.immregistries.smm.tester.manager.forecast.ForecastActual;
import org.immregistries.smm.tester.manager.forecast.ForecastTesterManager;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import org.immregistries.smm.transform.forecast.ForecastTestCase;

public class QueryRunner extends CertifyRunner {

  public static final String FILE_NAME = "fileName";
  public static final String USER_NAME = "userName";
  public static final String PASSWORD = "password";

  private int taskGroupId = 0;
  private String testPanelLabel = "";
  private Set<String> filenamesSelectedSet = null;
  private String userName = "";
  private String password = "";

  public String getPassword() {
    return password;
  }

  public String getUserName() {
    return userName;
  }

  public int getTaskGroupId() {
    return taskGroupId;
  }

  public void setTaskGroupId(int taskGroupId) {
    this.taskGroupId = taskGroupId;
  }

  public String getTestPanelLabel() {
    return testPanelLabel;
  }

  public void setTestPanelLabel(String testPanelLabel) {
    this.testPanelLabel = testPanelLabel;
  }

  public QueryRunner(Connector connector, SendData sendData, String queryType,
      Set<String> filenamesSelectedSet, String userName, String password) {
    super(connector, sendData, queryType);
    this.filenamesSelectedSet = filenamesSelectedSet;
    this.userName = userName;
    this.password = password;
    sendData.setupQueryDir();
    if (!sendData.getQueryDir().exists()) {
      switchStatus(STATUS_PROBLEM, "Query directory does not exist: " + sendData.getQueryDir().getName());
      return;
    }
    forecastTesterManager = new ForecastTesterManager(CAForecast.TCH_FORECAST_TESTER_URL);
    switchStatus(STATUS_INITIALIZED, "Setup and ready to go");
  }

  @Override
  public void run() {
    if (getStatus().equals(STATUS_INITIALIZED)) {
      switchStatus(STATUS_STARTED, "Starting");
      String[] filenames = getListOfFiles(sendData);
      for (String filename : filenames) {
        if (filenamesSelectedSet.contains(filename)) {
          File file = new File(sendData.getQueryDir(), filename);
          try {
            readAndQuery(file);
            if (!keepRunning) {
              switchStatus(STATUS_STOPPED, "Stopped by user");
              return;
            }
          } catch (IOException ioe) {
            ioe.printStackTrace();
            switchStatus(STATUS_PROBLEM, "Unable to read file " + file.getName() + ": " + ioe.getMessage());
            return;
          }
        }
      }
      switchStatus(STATUS_COMPLETED, "Queries completed");
    }
  }

  public static String[] getListOfFiles(SendData sendData) {
    String[] filenames = sendData.getQueryDir().list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".csv");
      }
    });
    return filenames;
  }

  private static final int POS_ID = 0;
  private static final int POS_ID_AA = 1;
  private static final int POS_ID_TYPE = 2;
  private static final int POS_LAST_NAME = 3;
  private static final int POS_FIRST_NAME = 4;
  private static final int POS_MIDDLE_NAME = 5;
  private static final int POS_DOB = 6;
  private static final int POS_SEX = 7;

  private void readAndQuery(File queryFile) throws IOException {

    logStatusMessage("Reading from file: " + queryFile.getName());
    BufferedReader in = new BufferedReader(new FileReader(queryFile));
    String line; //  = in.readLine();
    List<String> valueList; //  = CvsReader.readValuesFromCsv(line);
    // ignore this first one, it's the header

    int lineNumber = 0;
    while ((line = in.readLine()) != null && keepRunning) {
      line = line.trim();
      lineNumber++;
      if (line.length() < 1) {
        continue;
      }
      String tchForecastTesterTestCaseNumber;
      {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        tchForecastTesterTestCaseNumber = sdf.format(new Date(queryFile.lastModified())) + "-" + lineNumber;
      }
      valueList = CvsReader.readValuesFromCsv(line);
      String id = CvsReader.readValue(POS_ID, valueList);
      String idAa = CvsReader.readValue(POS_ID_AA, valueList);
      String idType = CvsReader.readValue(POS_ID_TYPE, valueList);
      String lastName = CvsReader.readValue(POS_LAST_NAME, valueList);
      String firstName = CvsReader.readValue(POS_FIRST_NAME, valueList);
      String middleName = CvsReader.readValue(POS_MIDDLE_NAME, valueList);
      String dob = CvsReader.readValue(POS_DOB, valueList);
      String sex = CvsReader.readValue(POS_SEX, valueList);
      if (id.equals("")) {
        continue;
      }

      logStatusMessage("  + Sending " + id);

      TestCaseMessage vxuTCM;
      vxuTCM = new TestCaseMessage();
      StringBuilder sb = new StringBuilder();
      sb.append("MSH|\nPID|\n");
      vxuTCM.appendOriginalMessage(sb.toString());
      vxuTCM.setQuickTransformations(new String[] { "2.5.1", (sex.equals("M") ? "BOY" : "GIRL") });
      vxuTCM.appendCustomTransformation("clear PID-3");
      vxuTCM.appendCustomTransformation("PID-3.1=" + id);
      vxuTCM.appendCustomTransformation("PID-3.4=" + idAa);
      vxuTCM.appendCustomTransformation("PID-3.5=" + idType);
      vxuTCM.appendCustomTransformation("PID-5.1=" + lastName);
      vxuTCM.appendCustomTransformation("PID-5.2=" + firstName);
      vxuTCM.appendCustomTransformation("PID-5.3=" + middleName);
      vxuTCM.appendCustomTransformation("PID-7=" + dob);
      Transformer transformer = new Transformer();
      transformer.transform(vxuTCM);

      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      queryTestCaseMessage.setMessageText(convertToQuery(vxuTCM));
      String message = Transformer.transform(connector, queryTestCaseMessage);
      System.out.println("--- REQUEST ---");
      System.out.println(message);
      try {
        String response = connector.submitMessage(message, false);
        System.out.println("--- RESPONSE ---");
        System.out.println(response);
        queryTestCaseMessage.setActualResponseMessage(response);
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (!queryTestCaseMessage.getActualResponseMessage().equals("")) {
        ForecastTesterManager.readForecastActual(queryTestCaseMessage);
        List<ForecastActual> forecastActualList = queryTestCaseMessage.getForecastActualList();
        if (forecastActualList.size() == 0) {
          logStatusMessage("    PROBLEM: No forecast results returned");
        } else {
          logStatusMessage("    GOOD     Forecast results returned");
          queryTestCaseMessage.setTchForecastTesterTestCaseNumber(tchForecastTesterTestCaseNumber);
          queryTestCaseMessage.setTchForecastTesterTestPanelLabel(queryFile.getName().substring(0, queryFile.getName().length() - 4));
          queryTestCaseMessage.setTchForecastTesterUserName(userName);
          queryTestCaseMessage.setTchForecastTesterPassword(password);
          if (CAForecast.REPORT_RESULTS) {
            String result = forecastTesterManager.reportForecastResults(queryTestCaseMessage, connector);
            logStatusMessage("             Reported to TCH: " + result);
          }
        }
        for (EvaluationActual evaluationActual : queryTestCaseMessage.getEvaluationActualList()) {
          System.out.println("  + " + evaluationActual.getVaccineCvx() + " given " + evaluationActual.getVaccineDate());
        }
        for (ForecastActual forecastActual : forecastActualList) {
          System.out.println("  + " + forecastActual.getSeriesName() + " due " + forecastActual.getDueDate());
        }
      } else {
        logStatusMessage("    PROBLEM: No message returned");
      }
    }
    in.close();
  }

}
