package org.immunizationsoftware.dqa.tester.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.immunizationsoftware.dqa.mover.ConnectionManager;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.certify.CertifyRunner;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.CvsReader;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.manager.forecast.ForecastActual;
import org.immunizationsoftware.dqa.tester.manager.forecast.ForecastTesterManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;

public class QueryRunner extends CertifyRunner {

  public QueryRunner(Connector connector, SendData sendData, String queryType, ParticipantResponse participantResponse) {
    super(connector, sendData, queryType, participantResponse);
    sendData.setupQueryDir();
    if (!sendData.getQueryDir().exists()) {
      switchStatus(STATUS_PROBLEM, "Query directory does not exist: " + sendData.getQueryDir().getName());
      return;
    }
    switchStatus(STATUS_INITIALIZED, "Setup and ready to go");
  }

  @Override
  public void run() {
    if (getStatus().equals(STATUS_INITIALIZED)) {
      switchStatus(STATUS_STARTED, "Starting");
      String[] filenames = sendData.getQueryDir().list(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return name.endsWith(".csv");
        }
      });
      for (String filename : filenames) {
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
      switchStatus(STATUS_COMPLETED, "Queries completed");
    }
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
    String line = in.readLine();
    List<String> valueList = CvsReader.readValuesFromCsv(line);
    // ignore this first one, it's the header

    while ((line = in.readLine()) != null && keepRunning) {
      line = line.trim();
      if (line.length() < 1) {
        continue;
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
      if (id.equals(""))
      {
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
        if (forecastActualList.size() == 0)
        {
          logStatusMessage("    PROBLEM: No forecast results returned");
        }
        else
        {
          logStatusMessage("    GOOD     Forecast results returned");
        }
        for (ForecastActual forecastActual : forecastActualList) {
          System.out.println("  + " + forecastActual.getSeriesName() + " due " + forecastActual.getDueDate());
        }
      }
      else
      {
        logStatusMessage("    PROBLEM: No message returned");
      }
    }
    in.close();
  }

}
