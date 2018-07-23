package org.immregistries.smm.tester.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.tester.CreateTestCaseServlet;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.CvsReader;
import org.immregistries.smm.tester.manager.forecast.EvaluationActual;
import org.immregistries.smm.tester.manager.forecast.ForecastActual;
import org.immregistries.smm.tester.manager.forecast.ForecastTesterManager;
import org.immregistries.smm.tester.manager.query.QueryConverter;
import org.immregistries.smm.tester.manager.query.QueryType;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;

public class QueryRunner extends Thread {


  private Connector connector;
  private Connector queryConnector;
  private ForecastTesterManager forecastTesterManager = null;
  private String testCaseSet = "";
  private Date testStarted = null;
  private Date testFinished = null;
  private QueryType queryType = null;
  private boolean willQuery = false;
  private boolean pauseBeforeQuerying = false;
  private boolean redactListResponses = false;
  private boolean reportErrorsOnly = false;
  private boolean condenseErrors = false;
  private String uniqueMRNBase = "";
  private static int uniqueMRNBaseInc = 0;
  private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
  private Transformer transformer;
  private SendData sendData;
  private File testDir;
  private TestCaseMessage testCaseMessageBase = null;
  private String status = "";
  protected List<String> statusMessageList = null;
  protected Throwable exception = null;
  protected boolean keepRunning = true;
  protected Map<String, Map<String, TestCaseMessage>> testMessageMapMap;
  private long lastLogStatus = System.currentTimeMillis();


  public static final String QUERY_TYPE_QBP_Z34 = "QBP-Z34";
  public static final String QUERY_TYPE_QBP_Z34_Z44 = "QBP-Z34-Z44";
  public static final String QUERY_TYPE_QBP_Z44 = "QBP-Z44";
  public static final String QUERY_TYPE_VXQ = "VXQ";
  public static final String QUERY_TYPE_NONE = "None";

  public static final String STATUS_INITIALIZED = "Initialized";
  public static final String STATUS_STARTED = "Started";
  public static final String STATUS_COMPLETED = "Completed";
  public static final String STATUS_STOPPED = "Stopped";
  public static final String STATUS_PROBLEM = "Problem";
  public static final String STATUS_PAUSED = "Paused";

  public static final String FILE_NAME = "fileName";
  public static final String USER_NAME = "userName";
  public static final String PASSWORD = "password";
  public static final String TRANSFORMS = "transforms";

  private int taskGroupId = 0;
  private String testPanelLabel = "";
  private Set<String> filenamesSelectedSet = null;
  private String userName = null;
  private String password = null;
  private String transforms = null;

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

  public void switchStatus(String status, String logMessage) {
    this.status = status;
    logStatusMessage(logMessage);
  }

  protected void indicateActive() {
    lastLogStatus = System.currentTimeMillis();
  }

  protected void logStatusMessage(String status) {
    indicateActive();
    synchronized (statusMessageList) {
      statusMessageList.add(sdf.format(new Date()) + " : " + status);
    }
  }


  public void oldCertifyRunnerInit(Connector connector, SendData sendData, QueryType queryType) {
    this.connector = connector;
    this.queryConnector = connector.getOtherConnectorMap().get(Connector.PURPOSE_QUERY);
    if (this.queryConnector == null) {
      queryConnector = connector;
    }
    this.queryType = queryType;

    this.sendData = sendData;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    testCaseSet =
        CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX + " " + sdf.format(new Date());

    sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    statusMessageList = new ArrayList<String>();

    switchStatus(STATUS_INITIALIZED, "Initializing CertifyRunner");
    willQuery = queryType != null
        && (queryType.equals(QUERY_TYPE_QBP_Z34) || queryType.equals(QUERY_TYPE_QBP_Z34_Z44)
            || queryType.equals(QUERY_TYPE_QBP_Z44) || queryType.equals(QUERY_TYPE_VXQ));
    if (willQuery) {
      logStatusMessage("Query will be run: " + queryType);
    } else {
      logStatusMessage("Query was not enabled");
    }

    logStatusMessage("IIS Tester Initialized");
  }

  public static final String TCH_FORECAST_TESTER_URL =
      "http://tchforecasttester.org/ft/ExternalTestServlet";
  public static final boolean REPORT_RESULTS = true;

  public void stopRunning() {
    keepRunning = false;
  }

  public String getStatus() {
    return status;
  }

  public List<String> getStatusMessageList() {
    synchronized (statusMessageList) {
      return new ArrayList<String>(statusMessageList);
    }
  }

  public QueryRunner(Connector connector, SendData sendData, QueryType queryType,
      Set<String> filenamesSelectedSet, String userName, String password, String transforms) {
    oldCertifyRunnerInit(connector, sendData, queryType);
    this.filenamesSelectedSet = filenamesSelectedSet;
    this.userName = userName;
    this.password = password;
    this.transforms = transforms;
    sendData.setupQueryDir();
    if (!sendData.getQueryDir().exists()) {
      switchStatus(STATUS_PROBLEM,
          "Query directory does not exist: " + sendData.getQueryDir().getName());
      return;
    }
    forecastTesterManager = new ForecastTesterManager(TCH_FORECAST_TESTER_URL);
    switchStatus(STATUS_INITIALIZED, "Setup and ready to go");
  }

  public void run() {
    if (status.equals(STATUS_INITIALIZED)) {
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
            switchStatus(STATUS_PROBLEM,
                "Unable to read file " + file.getName() + ": " + ioe.getMessage());
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
    PrintWriter saveOut = null;
    if (transforms != null) {
      String fn = queryFile.getName() + ".response.hl7";
      File saveFile = new File(queryFile.getParentFile(), fn);
      saveOut = new PrintWriter(new FileWriter(saveFile));
    }

    String line;
    BufferedReader in = setupReader(queryFile);

    int lineNumber = 0;
    while ((line = in.readLine()) != null && keepRunning) {
      lineNumber++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();

      boolean goodToGo = readLine(queryFile, line, lineNumber, queryTestCaseMessage);
      if (goodToGo) {
        if (!queryTestCaseMessage.getActualResponseMessage().equals("")) {
          if (userName != null && !userName.equals("")) {
            ForecastTesterManager.readForecastActual(queryTestCaseMessage);
            List<ForecastActual> forecastActualList = queryTestCaseMessage.getForecastActualList();
            if (forecastActualList.size() == 0) {
              logStatusMessage("    PROBLEM: No forecast results returned");
            } else {
              logStatusMessage("    GOOD     Forecast results returned");
              queryTestCaseMessage.setTchForecastTesterTestPanelLabel(
                  queryFile.getName().substring(0, queryFile.getName().length() - 4));
              queryTestCaseMessage.setTchForecastTesterUserName(userName);
              queryTestCaseMessage.setTchForecastTesterPassword(password);
              if (REPORT_RESULTS) {
                String result =
                    forecastTesterManager.reportForecastResults(queryTestCaseMessage, connector);
                logStatusMessage("             Reported to TCH: " + result);
              }
            }
            for (EvaluationActual evaluationActual : queryTestCaseMessage
                .getEvaluationActualList()) {
              System.out.println("  + " + evaluationActual.getVaccineCvx() + " given "
                  + evaluationActual.getVaccineDate());
            }
            for (ForecastActual forecastActual : forecastActualList) {
              System.out.println(
                  "  + " + forecastActual.getSeriesName() + " due " + forecastActual.getDueDate());
            }
          }
          if (saveOut != null) {
            String rspMessage = queryTestCaseMessage.getActualResponseMessage();
            if (transforms != null) {
              TestCaseMessage rspTCM = new TestCaseMessage();
              rspTCM.appendOriginalMessage(rspMessage);
              rspTCM.setCustomTransformations(transforms + "\n");
              Transformer transformer = new Transformer();
              transformer.transform(rspTCM);
              rspMessage = rspTCM.getMessageText();
            }
            saveOut.print(rspMessage);
          }
        } else {
          logStatusMessage("    PROBLEM: No message returned");
        }
      }
    }
    in.close();
    if (saveOut != null) {
      saveOut.close();
    }
  }

  private BufferedReader setupReader(File queryFile) throws FileNotFoundException {
    logStatusMessage("Reading from file: " + queryFile.getName());
    BufferedReader in = new BufferedReader(new FileReader(queryFile));
    return in;
  }

  private boolean readLine(File queryFile, String line, int lineNumber,
      TestCaseMessage queryTestCaseMessage) {
    List<String> valueList;
    line = line.trim();
    boolean goodToGo = true;
    if (line.length() < 1) {
      goodToGo = false;
    } else {
      String tchForecastTesterTestCaseNumber;
      {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        tchForecastTesterTestCaseNumber =
            sdf.format(new Date(queryFile.lastModified())) + "-" + lineNumber;
        queryTestCaseMessage.setTchForecastTesterTestCaseNumber(tchForecastTesterTestCaseNumber);
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
        goodToGo = false;
      } else {

        logStatusMessage("  + Sending " + id);

        TestCaseMessage vxuTCM = new TestCaseMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("MSH|\nPID|\n");
        vxuTCM.appendOriginalMessage(sb.toString());
        vxuTCM.setQuickTransformations(new String[] {"2.5.1", (sex.equals("M") ? "BOY" : "GIRL")});
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
      }
    }
    return goodToGo;
  }

  public String convertToQuery(TestCaseMessage testCaseMessage) {
    QueryConverter queryConverter = QueryConverter.getQueryConverter(queryType);
    if (queryConverter != null) {
      return queryConverter.convert(testCaseMessage.getMessageText());
    }
    throw new IllegalArgumentException(
        "Unable to convert query because query type '" + queryType + "' is not recognized");
  }

}
