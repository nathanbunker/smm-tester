package org.immunizationsoftware.dqa.tester;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_FOUR_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_TWELVE_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_TWO_MONTHS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_TWO_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_2_R_ADMIN_ADULT;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_FOUR_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_TWELVE_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_TWO_MONTHS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_TWO_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_4_R_CONSENTED_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_5_P_REFUSED_TODDLER;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_6_P_VARICELLA_HISTORY_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_7_R_COMPLETE_RECORD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_FULL_RECORD_FOR_PROFILING;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.RunAgainstConnector;
import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.QueryConverter;
import org.immunizationsoftware.dqa.tester.manager.TestCaseMessageManager;
import org.immunizationsoftware.dqa.tester.manager.forecast.ForecastTesterManager;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.profile.CompatibilityConformance;
import org.immunizationsoftware.dqa.tester.profile.CompatibilityInteroperability;
import org.immunizationsoftware.dqa.tester.profile.MessageAcceptStatus;
import org.immunizationsoftware.dqa.tester.profile.ProfileCategory;
import org.immunizationsoftware.dqa.tester.profile.ProfileLine;
import org.immunizationsoftware.dqa.tester.profile.ProfileManager;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsage;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsageValue;
import org.immunizationsoftware.dqa.tester.profile.Usage;
import org.immunizationsoftware.dqa.tester.run.TestRunner;
import org.immunizationsoftware.dqa.tester.transform.Issue;
import org.immunizationsoftware.dqa.transform.Comparison;
import org.immunizationsoftware.dqa.transform.ScenarioManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestCase;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestEvent;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;

public class CertifyRunner extends Thread
{
  private static final String REPORT_EXPLANATION_URL = "http://ois-pt.org/tester/reportExplanation.html";

  private static final String QUERY_TYPE_QBP = "Q";
  private static final String QUERY_TYPE_VXQ = "V";
  private static final String QUERY_TYPE_NONE = "N";

  public static final String STATUS_INITIALIZED = "Initialized";
  public static final String STATUS_STARTED = "Started";
  public static final String STATUS_COMPLETED = "Completed";
  public static final String STATUS_STOPPED = "Stopped";
  public static final String STATUS_PAUSED = "Paused";

  private String status = "";
  private List<String> statusMessageList = null;
  private Throwable exception = null;
  private boolean keepRunning = true;
  private TestCaseMessage testCaseMessageBase = null;

  private void logStatus(String status) {
    synchronized (statusMessageList) {
      statusMessageList.add(sdf.format(new Date()) + " " + status);
    }
  }

  public static final int SUITE_A_BASIC = 0;
  public static final int SUITE_B_INTERMEDIATE = 1;
  public static final int SUITE_C_ADVANCED = 2;
  public static final int SUITE_D_EXCEPTIONAL = 3;
  public static final int SUITE_E_FORECAST_PREP = 4;
  public static final int SUITE_F_FORECAST = 5;
  public static final int SUITE_G_PERFORMANCE = 6;
  public static final int SUITE_H_CONFORMANCE = 7;
  public static final int SUITE_I_PROFILING = 8;
  public static final int SUITE_COUNT = 9;

  private boolean run[] = new boolean[SUITE_COUNT];
  private int[][] areaScore = new int[SUITE_COUNT][3];
  private int[][] areaProgress = new int[SUITE_COUNT][3];
  private int[][] areaCount = new int[SUITE_COUNT][3];
  private String[] areaLabel = new String[SUITE_COUNT];

  private static int REPORT_1_INTEROP = 0;
  private static int REPORT_2_CODED = 1;
  private static int REPORT_3_LOCAL = 2;
  private static int REPORT_4_NATIONAL = 3;
  private static int REPORT_5_TOLERANCE = 4;
  private static int REPORT_6_EHR = 5;
  private static int REPORT_7_PERFORM = 6;
  private static int REPORT_8_ACK = 7;

  private double[] reportScore = new double[8];

  {
    for (int i = 0; i < SUITE_COUNT; i++) {
      areaScore[i][0] = -1;
      areaScore[i][1] = -1;
      areaScore[i][2] = -1;
      areaProgress[i][0] = -1;
      areaProgress[i][1] = -1;
      areaProgress[i][2] = -1;
      areaCount[i][0] = -1;
      areaCount[i][1] = -1;
      areaCount[i][2] = -1;
    }
    areaLabel[SUITE_A_BASIC] = "Basic";
    areaLabel[SUITE_B_INTERMEDIATE] = "Intermediate";
    areaLabel[SUITE_C_ADVANCED] = "Advanced";
    areaLabel[SUITE_D_EXCEPTIONAL] = "Exceptional";
    areaLabel[SUITE_E_FORECAST_PREP] = "Forecast Prep";
    areaLabel[SUITE_F_FORECAST] = "Forecast";
    areaLabel[SUITE_G_PERFORMANCE] = "Performance";
    areaLabel[SUITE_H_CONFORMANCE] = "Conformance";
    areaLabel[SUITE_I_PROFILING] = "Profiling";

  }

  private Map<String, PrintWriter> exampleOutSet = new HashMap<String, PrintWriter>();
  private Map<String, PrintWriter> exampleAckOutSet = new HashMap<String, PrintWriter>();

  public boolean isRun(int suite) {
    return run[suite];
  }

  public void setRun(boolean r, int suite) {
    run[suite] = r;
  }

  private long totalQueryTime = 0;
  private long totalUpdateTime = 0;
  private int totalQueryCount = 0;
  private int totalUpdateCount = 0;

  private String testCaseSet = "";
  private HttpSession session = null;

  private Date testStarted = null;
  private Date testFinished = null;

  private String queryType = "";
  private boolean willQuery = false;
  private boolean pauseBeforeQuerying = false;
  private String uniqueMRNBase = "";

  public boolean isPauseBeforeQuerying() {
    return pauseBeforeQuerying;
  }

  public void setPauseBeforeQuerying(boolean pauseBeforeQuerying) {
    this.pauseBeforeQuerying = pauseBeforeQuerying;
  }

  private List<TestCaseMessage> statusCheckTestCaseList = new ArrayList<TestCaseMessage>();

  private List<TestCaseMessage> statusCheckTestCaseBasicList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseIntermediateList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseExceptionalList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseProfilingList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseForecastPrepList = new ArrayList<TestCaseMessage>();

  private List<TestCaseMessage> statusCheckQueryTestCaseBasicList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseIntermediateList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseProfilingList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseTolerantList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseForecastPrepList = new ArrayList<TestCaseMessage>();

  private List<TestCaseMessage> ackAnalysisList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> rspAnalysisList = new ArrayList<TestCaseMessage>();

  private List<ProfileLine> profileLineList = null;
  private TestCaseMessage tcmFull = null;

  private void register(TestCaseMessage tcm) {
    tcm.setTestCaseId(statusCheckTestCaseList.size());
    statusCheckTestCaseList.add(tcm);
  }

  public List<TestCaseMessage> getStatusCheckTestCaseList() {
    return statusCheckTestCaseList;
  }

  public String getQueryType() {
    return queryType;
  }

  public void setQueryType(String queryType) {
    this.queryType = queryType;
  }

  private String[] statusCheckScenarios = { SCENARIO_1_R_ADMIN_CHILD, SCENARIO_2_R_ADMIN_ADULT,
      SCENARIO_3_R_HISTORICAL_CHILD, SCENARIO_4_R_CONSENTED_CHILD, SCENARIO_5_P_REFUSED_TODDLER,
      SCENARIO_6_P_VARICELLA_HISTORY_CHILD, SCENARIO_7_R_COMPLETE_RECORD };

  private List<TestCaseMessage>[] profileTestCaseLists = new ArrayList[3];

  Connector connector;
  Connector queryConnector;

  public CertifyRunner(Connector connector, HttpSession session) {
    this.connector = connector;
    this.queryConnector = connector.getOtherConnectorMap().get(Connector.PURPOSE_QUERY);
    if (this.queryConnector == null) {
      queryConnector = connector;
    }
    this.session = session;
    status = STATUS_INITIALIZED;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    testCaseSet = CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX + " " + sdf.format(new Date());

    sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    statusMessageList = new ArrayList<String>();
    logStatus("IIS Tester Initialized");
  }

  private static int uniqueMRNBaseInc = 0;

  private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

  private Transformer transformer;
  private SendData sendData;
  private File testDir;

  @Override
  public void run() {

    status = STATUS_STARTED;
    try {

      Authenticate.User user = (Authenticate.User) session.getAttribute("user");
      sendData = user.getSendData();
      if (sendData == null) {
        testDir = null;
      } else {
        testDir = new File(sendData.getRootDir(), testCaseSet);
        if (!testDir.exists()) {
          testDir.mkdir();
        }
      }

      File testDataFile = CreateTestCaseServlet.getTestDataFile(user);
      if (testDataFile == null) {
        transformer = new Transformer();
      } else {
        transformer = new Transformer(testDataFile);
      }

      uniqueMRNBase = Transformer.makeBase62Number(System.currentTimeMillis() % 1000000 + uniqueMRNBaseInc++) + "-";
      willQuery = queryType != null && (queryType.equals(QUERY_TYPE_QBP) || queryType.equals(QUERY_TYPE_VXQ));

      testCaseMessageBase = createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessageBase.setTestCaseSet(testCaseSet);
      testCaseMessageBase.setTestCaseCategoryId("BASE");
      testCaseMessageBase.setTestCaseNumber(uniqueMRNBase + testCaseMessageBase.getTestCaseCategoryId());

      testStarted = new Date();

      if (runAgainstFolder != null) {
        connector = new RunAgainstConnector(connector, runAgainstFolder);
        logStatus("Running test against previously received responses in this forder: " + runAgainstFolder);
        if (queryConnector != null) {
          queryConnector = new RunAgainstConnector(queryConnector, runAgainstFolder);
          ;
        }
      } else {
        logStatus("Connecting directly to real-time interface");
      }

      if (run[SUITE_A_BASIC]) {

        logStatus("Preparing basic messages");
        prepareBasic();

        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }

        logStatus("Sending basic messages");
        updateBasic();
        printReportToFile();
      }

      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
      if (run[SUITE_B_INTERMEDIATE]) {

        logStatus("Preparing intermediate");
        prepareIntermediate();

        logStatus("Sending intermediate messages");
        updateIntermediate();
        printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }

      }
      if (run[SUITE_C_ADVANCED]) {
        Map<Integer, List<Issue>> issueMap = new HashMap<Integer, List<Issue>>();

        logStatus("Preparing advanced messages");
        prepareAdvanced(issueMap);
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }

        logStatus("Sending advanced messages");
        updateAdvanced(issueMap);
        printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }

      if (run[SUITE_I_PROFILING]) {
        logStatus("Preparing profiling");

        prepareProfiling();

        if (profileManager == null) {
          logStatus("Profiling not setup");
        } else {
          if (profileUsage == null) {
            logStatus("Profile not selected");
          } else if (tcmFull.isPassedTest()) {
            profileLineList = profileManager.createProfileLines(profileUsage, false);
            ProfileManager.updateMessageAcceptStatus(profileLineList);
            logStatus("Found " + profileLineList.size() + " of profile lines to test");
            updateProfiling(profileLineList);
            printReportToFile();
            if (!keepRunning) {
              status = STATUS_STOPPED;
              return;
            }
          }
        }
      }

      if (run[SUITE_D_EXCEPTIONAL]) {
        logStatus("Preparing exceptional messages");
        prepareExceptional();

        logStatus("Sending exceptional messages");
        updateExceptional();
        printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }

      if (run[SUITE_E_FORECAST_PREP]) {
        logStatus("Preparing forecast prep messages");
        prepareForecastPrep();

        logStatus("Sending forecast prep messages");
        updateForecastPrep();
        printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }

      if (totalUpdateCount > 0) {
        areaScore[SUITE_G_PERFORMANCE][0] = (int) totalUpdateTime / totalUpdateCount;
        areaProgress[SUITE_G_PERFORMANCE][0] = 100;
        areaCount[SUITE_G_PERFORMANCE][0] = totalUpdateCount;
      }
      if (run[SUITE_H_CONFORMANCE]) {

        logStatus("Prepare for format analysis of update messages");
        prepareFormatUpdateAnalysis();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }

        logStatus("Analyze format updates");
        analyzeFormatUpdates();
        printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }

      if (willQuery) {

        if (pauseBeforeQuerying) {
          logStatus("Paused, waiting to start query process");
          status = STATUS_PAUSED;
          int maxWait = 0;
          while (pauseBeforeQuerying && keepRunning && maxWait < (60 * 10)) {
            try {
              synchronized (this) {
                this.wait(1000);
              }
            } catch (InterruptedException ieo) {
              // continue
            }
            maxWait++;
          }
          logStatus("Begin query process");
          status = STATUS_STARTED;
        }

        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }

        if (run[SUITE_A_BASIC]) {
          logStatus("Submit query for basic messages");
          queryBasic();
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }

        if (run[SUITE_B_INTERMEDIATE]) {

          logStatus("Prepare query for intermediate messages");
          prepareQueryIntermediate();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }

          logStatus("Submit query for intermediate messages");
          query(SUITE_B_INTERMEDIATE, statusCheckQueryTestCaseIntermediateList);
          printReportToFile();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
        }

        if (run[SUITE_I_PROFILING]) {

          logStatus("Prepare query for profiling messages");
          prepareQueryProfiling();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
          logStatus("Submit query for profiling messages");
          query(SUITE_I_PROFILING, statusCheckQueryTestCaseProfilingList);
          printReportToFile();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
        }

        if (run[SUITE_D_EXCEPTIONAL]) {

          logStatus("Prepare query for exceptional messages");
          prepareQueryExeptional();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
          logStatus("Submit query for exceptional messages");
          query(SUITE_D_EXCEPTIONAL, statusCheckQueryTestCaseTolerantList);
          printReportToFile();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
        }

        if (run[SUITE_E_FORECAST_PREP]) {

          logStatus("Prepare query for forecast prep messages");
          prepareQueryForecastPrep();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }

          logStatus("Submit query for forecast prep messages");
          query(SUITE_E_FORECAST_PREP, statusCheckQueryTestCaseForecastPrepList);

          printReportToFile();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
        }

        if (totalQueryCount > 0) {
          areaScore[SUITE_G_PERFORMANCE][1] = (int) totalQueryTime / totalQueryCount;
          areaCount[SUITE_G_PERFORMANCE][1] = totalQueryCount;
          areaProgress[SUITE_G_PERFORMANCE][1] = 100;
        }

        if (run[SUITE_H_CONFORMANCE]) {
          logStatus("Prepare for format analysis of queries");
          prepareFormatQueryAnalysis();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }

          logStatus("Analyze format of query messages");
          analyzeFormatQueries();
        }

      }

      areaProgress[SUITE_H_CONFORMANCE][2] = 100;
      testFinished = new Date();

      printReportToFile();
    }

    catch (Throwable t) {
      t.printStackTrace();
      exception = t;
      status = STATUS_STOPPED;
      logStatus("Exception ocurred: " + exception.getMessage());
    } finally {
      if (status != STATUS_STOPPED) {
        status = STATUS_COMPLETED;
      } else {
        logStatus("Process stopped by user");
      }
      for (PrintWriter exampleOut : exampleOutSet.values()) {
        exampleOut.close();
      }
      for (PrintWriter exampleAckOut : exampleAckOutSet.values()) {
        exampleAckOut.close();
      }
      logStatus("IIS Test Finished");
    }
  }

  public void prepareProfiling() throws Exception {
    tcmFull = createTestCaseMessage(SCENARIO_FULL_RECORD_FOR_PROFILING);
    tcmFull.setTestCaseSet(testCaseSet);
    tcmFull.setTestCaseCategoryId("I." + paddWithZeros(0, 3));
    tcmFull.setTestCaseNumber(uniqueMRNBase + tcmFull.getTestCaseCategoryId());
    tcmFull.setDescription("Base Record");
    transformer.transform(tcmFull);
    register(tcmFull);
    tcmFull.setAssertResult("Accept - *");
    tcmFull.setHasIssue(true);

    logStatus("Running full test record to see it will be accepted");
    TestRunner testRunner = new TestRunner();
    testRunner.runTest(connector, tcmFull);
    totalUpdateTime += testRunner.getTotalRunTime();
    totalUpdateCount++;
    tcmFull.setHasRun(true);

    if (tcmFull.isPassedTest()) {
      logStatus("Full record for profiling was accepted, profiling of all fields can begin");
    } else {
      logStatus("Full record for profiling was NOT accepted, profiling of all fields can not be conducted");
    }

  }

  public PrintWriter setupExampleFile(String name, TestCaseMessage testCaseMessage) {
    if (testCaseMessage != null) {
      if (testDir != null) {
        logStatus("Saving example");
        File exampleFile;
        if (testCaseMessage.getForecastTestPanel() != null) {
          exampleFile = new File(testDir, "Example Messages " + name + ""
              + testCaseMessage.getForecastTestPanel().getLabel() + ".hl7");
        } else {
          exampleFile = new File(testDir, "Example Messages " + name + ".hl7");
        }
        try {
          return new PrintWriter(exampleFile);
        } catch (IOException ioe) {
          ioe.printStackTrace();
          logStatus("Unable to write examples out: " + ioe);
        }
      }
    }
    return null;
  }

  public PrintWriter setupAckFile(String name, TestCaseMessage testCaseMessage) {
    if (testCaseMessage != null && sendData != null) {
      if (testDir != null) {
        logStatus("Saving example");
        File exampleFile;
        if (testCaseMessage.getForecastTestPanel() != null) {
          exampleFile = new File(testDir, "Example Messages " + name + ""
              + testCaseMessage.getForecastTestPanel().getLabel() + ".ack.hl7");
        } else {
          exampleFile = new File(testDir, "Example Messages " + name + ".ack.hl7");
        }
        try {
          return new PrintWriter(exampleFile);
        } catch (IOException ioe) {
          ioe.printStackTrace();
          logStatus("Unable to write examples out: " + ioe);
        }
      }
    }
    return null;
  }

  public void printReportToFile() {

    if (testDir != null) {
      logStatus("Writing out final report");
      File detailReportFile = new File(testDir, "IIS Testing Report Detail.html");
      try {
        PrintWriter out = new PrintWriter(new FileWriter(detailReportFile));
        String title = "IIS Testing Report Detail";
        ClientServlet.printHtmlHeadForFile(out, title);
        out.println("<h1>Detialed Run Log for " + connector.getLabel() + "</h1>");
        out.println("<p>This detail run log gives complete details on what was tested. For a summary and condensed view see the IIS Testing Results. </p>");
        out.println("<ul>");
        out.println("  <li><a href=\"IIS Testing Report.html\">IIS Testing Results</a></li>");
        out.println("</ul>");
        printResults(out);
        out.println("    <h3>IIS Testing Details</h3>");
        printProgressDetails(out, true);
        ClientServlet.printHtmlFootForFile(out);
        out.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
        // unable to save, continue as normal
      }

      File reportFile = new File(testDir, "IIS Testing Report.html");
      try {
        PrintWriter out = new PrintWriter(new FileWriter(reportFile));
        String title = "IIS Testing Report";
        ClientServlet.printHtmlHeadForFile(out, title);
        out.println("    <h1>IIS Testing Results for " + connector.getLabel() + "</h1>");
        printReport(out, true);
        ClientServlet.printHtmlFootForFile(out);
        out.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
        // unable to save, continue as normal
      }

      logStatus("Looking to see if SMM analysis can be written");
      File smmAnalysisDir = ManagerServlet.getSmmAnalysisFolder();
      if (smmAnalysisDir != null && areaProgress[SUITE_A_BASIC][0] > 0 && areaProgress[SUITE_B_INTERMEDIATE][0] > 0
          && areaProgress[SUITE_D_EXCEPTIONAL][0] > 0 && areaProgress[SUITE_G_PERFORMANCE][0] > 0
          && areaProgress[SUITE_H_CONFORMANCE][0] > 0 && areaProgress[SUITE_I_PROFILING][0] > 0) {
        logStatus("Writing SMM Analysis");
        File analysisFile = new File(smmAnalysisDir, "Analysis Report.html");
        try {
          PrintWriter out = new PrintWriter(new FileWriter(analysisFile));
          String title = "IIS Testing Analysis";
          ClientServlet.printHtmlHeadForFile(out, title);
          out.println("    <h1>IIS Testing Analysis</h1>");
          printAnalysis(out, true);
          ClientServlet.printHtmlFootForFile(out);
          out.close();
        } catch (IOException ioe) {
          ioe.printStackTrace();
          // unable to save, continue as normal
        }
        logStatus("Finished writing SMM Analysis");
      }
    }
  }

  private void prepareFormatUpdateAnalysis() {
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseList) {
      if (testCaseMessage.isHasRun() && testCaseMessage.getActualMessageResponseType().equals("ACK")
          && !testCaseMessage.isResultNotExpectedToConform()) {
        ackAnalysisList.add(testCaseMessage);
      }
    }
    areaCount[SUITE_H_CONFORMANCE][0] = ackAnalysisList.size();
  }

  private void prepareFormatQueryAnalysis() {
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseList) {
      if (testCaseMessage.isHasRun() && testCaseMessage.getActualMessageResponseType().equals("RSP")
          && !testCaseMessage.isResultNotExpectedToConform()) {
        rspAnalysisList.add(testCaseMessage);
      }
    }
    areaCount[SUITE_H_CONFORMANCE][1] = rspAnalysisList.size();
  }

  private void analyzeFormatUpdates() {
    int count = 0;
    int pass = 0;
    for (TestCaseMessage testCaseMessage : ackAnalysisList) {
      count++;
      if (testCaseMessage.isHasRun()) {
        HL7Component comp = TestCaseMessageManager.createHL7Component(testCaseMessage);
        if (comp != null && comp.hasNoErrors()) {
          pass++;
        }
        areaProgress[SUITE_H_CONFORMANCE][0] = makeScore(count, ackAnalysisList.size());
      }
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaProgress[SUITE_H_CONFORMANCE][0] = makeScore(count, ackAnalysisList.size());
    areaScore[SUITE_H_CONFORMANCE][0] = makeScore(pass, ackAnalysisList.size());
  }

  private void analyzeFormatQueries() {
    int count = 0;
    int pass = 0;
    for (TestCaseMessage testCaseMessage : rspAnalysisList) {
      count++;
      if (testCaseMessage.isHasRun()) {
        HL7Component comp = TestCaseMessageManager.createHL7Component(testCaseMessage);
        if (comp != null && comp.hasNoErrors()) {
          pass++;
        }

        areaProgress[SUITE_H_CONFORMANCE][1] = makeScore(count, rspAnalysisList.size());

        // TODO look for query and rate

        areaProgress[SUITE_H_CONFORMANCE][2] = makeScore(count, rspAnalysisList.size());
      }
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaProgress[SUITE_H_CONFORMANCE][1] = makeScore(count, rspAnalysisList.size());
    areaScore[SUITE_H_CONFORMANCE][1] = makeScore(pass, rspAnalysisList.size());

  }

  private void updateAdvanced(Map<Integer, List<Issue>> issueMap) {
    TestRunner testRunner = new TestRunner();
    int count;
    for (int i = 0; i < areaScore[SUITE_C_ADVANCED].length; i++) {
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
      profileTestCaseLists[i] = new ArrayList<TestCaseMessage>();
      int priority = i + 1;
      List<Issue> issueList = issueMap.get(priority);
      if (issueList != null && issueList.size() > 0) {
        int countPass = 0;
        count = 0;
        for (Issue issue : issueList) {
          count++;

          TestCaseMessage testCaseMessage = createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
          testCaseMessage.setTestCaseSet(testCaseSet);
          testCaseMessage.setTestCaseCategoryId("C." + makeTwoDigits(priority) + "." + paddWithZeros(count, 3));
          testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
          profileTestCaseLists[i].add(testCaseMessage);
          testCaseMessage.setDescription(issue.getName());
          testCaseMessage.addCauseIssues(issue.getName());
          testCaseMessage.appendCustomTransformation("MSH-11=D");
          transformer.transform(testCaseMessage);
          register(testCaseMessage);
          testCaseMessage.setAssertResultText(issue.getName());
          testCaseMessage.setAssertResultStatus("Accept or Reject");
          if (testCaseMessage.hasIssue()) {
            try {
              testRunner.runTest(connector, testCaseMessage);
              totalUpdateCount++;
              totalUpdateTime += testRunner.getTotalRunTime();
              if (testCaseMessage.isPassedTest()) {
                countPass++;
              }
              printExampleMessage(testCaseMessage, "C Advanced");
            } catch (Throwable t) {
              testCaseMessage.setException(t);
            }
          }
          testCaseMessage.setHasRun(true);
          saveTestCase(testCaseMessage);
          areaProgress[SUITE_C_ADVANCED][i] = makeScore(count, issueList.size());
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
        }
        areaScore[SUITE_C_ADVANCED][i] = makeScore(countPass, issueList.size());
        areaCount[SUITE_C_ADVANCED][i] = issueList.size();
      }
      areaProgress[SUITE_C_ADVANCED][i] = 100;

    }
  }

  private TestCaseMessage getPresentTestCase(ProfileLine profileLine, int count, TestCaseMessage defaultTestCaseMessage) {
    TestCaseMessage testCaseMessage = ProfileManager.getPresentTestCase(profileLine.getField(), defaultTestCaseMessage);
    if (testCaseMessage != defaultTestCaseMessage) {
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("I." + makeTwoDigits(1) + "." + paddWithZeros(count, 5));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.setDescription("Field " + profileLine.getField().getFieldName() + " is present");
      transformer.transform(testCaseMessage);
      if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_PRESENT) {
        testCaseMessage.setAssertResult("Accept - *");
      } else if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_ABSENT) {
        testCaseMessage.setAssertResult("Reject - *");
      } else {
        testCaseMessage.setAssertResult("Accept - *");
      }
      testCaseMessage.setMessageAcceptStatusDebug(profileLine.getMessageAcceptStatusDebug());
    }
    return testCaseMessage;
  }

  private TestCaseMessage getAbsentTestCase(ProfileLine profileLine, int count, TestCaseMessage defaultTestCaseMessage) {
    TestCaseMessage testCaseMessage = ProfileManager.getAbsentTestCase(profileLine.getField(), defaultTestCaseMessage);
    if (testCaseMessage != defaultTestCaseMessage) {
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("I." + makeTwoDigits(2) + "." + paddWithZeros(count, 5));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      transformer.transform(testCaseMessage);
      testCaseMessage.setDescription("Field " + profileLine.getField().getFieldName() + " is absent");
      if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_PRESENT) {
        testCaseMessage.setAssertResult("Reject - *");
      } else if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_ABSENT) {
        testCaseMessage.setAssertResult("Accept - *");
      } else {
        testCaseMessage.setAssertResult("Accept - *");
      }
      testCaseMessage.setMessageAcceptStatusDebug(profileLine.getMessageAcceptStatusDebug());
    }
    return testCaseMessage;
  }

  private void updateProfiling(List<ProfileLine> profileLineList) {
    TestRunner testRunner = new TestRunner();
    int count;
    int countPass = 0;
    int countTested = 0;
    count = 0;
    int profilingRunCount = 0;
    Map<String, TestCaseMessage> testCaseMessageMap = new HashMap<String, TestCaseMessage>();
    for (ProfileLine profileLine : profileLineList) {
      profileLine.setUsageDetected(profileLine.getUsage());
      count++;
      TestCaseMessage testCaseMessagePresent = getPresentTestCase(profileLine, count, tcmFull);
      TestCaseMessage testCaseMessageAbsent = getAbsentTestCase(profileLine, count, tcmFull);
      try {
        if (!testCaseMessagePresent.hasIssue() || !testCaseMessageAbsent.hasIssue()) {
          profileLine.setTestCaseMessagePresent(testCaseMessagePresent);
          profileLine.setTestCaseMessageAbsent(testCaseMessageAbsent);
          profileLine.setHasRun(false);
        } else {
          profileLine.setTestCaseMessagePresent(testCaseMessagePresent);
          if (testCaseMessagePresent != tcmFull) {
            register(testCaseMessagePresent);
            testCaseMessagePresent = testRunner.runTestIfNew(connector, testCaseMessagePresent, testCaseMessageMap);
            if (testRunner.isWasRun()) {
              totalUpdateTime += testRunner.getTotalRunTime();
              totalUpdateCount++;
              if (testCaseMessagePresent.isAccepted()) {
                statusCheckTestCaseProfilingList.add(testCaseMessagePresent);
              }
            }
          }
          profileLine.setTestCaseMessageAbsent(testCaseMessageAbsent);
          if (testCaseMessageAbsent != tcmFull) {
            register(testCaseMessageAbsent);
            testRunner.runTestIfNew(connector, testCaseMessageAbsent, testCaseMessageMap);
            if (testRunner.isWasRun()) {
              totalUpdateTime += testRunner.getTotalRunTime();
              totalUpdateCount++;
              if (testCaseMessageAbsent.isAccepted()) {
                statusCheckTestCaseProfilingList.add(testCaseMessageAbsent);
              }
            }
          }
          if (testCaseMessagePresent.isAccepted() && !testCaseMessageAbsent.isAccepted()) {
            profileLine.setMessageAcceptStatusDetected(MessageAcceptStatus.ONLY_IF_PRESENT);
          } else if (!testCaseMessagePresent.isAccepted() && testCaseMessageAbsent.isAccepted()) {
            profileLine.setMessageAcceptStatusDetected(MessageAcceptStatus.ONLY_IF_ABSENT);
          } else {
            profileLine.setMessageAcceptStatusDetected(MessageAcceptStatus.IF_PRESENT_OR_ABSENT);
          }
          if (profileLine.getMessageAcceptStatusDetected() != profileLine.getMessageAcceptStatus()) {
            if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_PRESENT) {
              if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.IF_PRESENT_OR_ABSENT) {
                profileLine.setUsageDetected(Usage.R_NOT_ENFORCED);
              } else if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.ONLY_IF_ABSENT) {
                profileLine.setUsageDetected(Usage.X);
              }
            } else if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.IF_PRESENT_OR_ABSENT) {
              if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.ONLY_IF_PRESENT) {
                if (profileLine.getUsage() == Usage.R) {
                  profileLine.setUsageDetected(Usage.R_SPECIAL);
                } else {
                  profileLine.setUsageDetected(Usage.R);
                }
              } else if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.ONLY_IF_ABSENT) {
                profileLine.setUsageDetected(Usage.X);
              }
            } else if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_ABSENT) {
              if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.ONLY_IF_PRESENT) {
                if (profileLine.getUsage() == Usage.R) {
                  profileLine.setUsageDetected(Usage.R_SPECIAL);
                } else {
                  profileLine.setUsageDetected(Usage.R);
                }
              } else if (profileLine.getMessageAcceptStatusDetected() == MessageAcceptStatus.IF_PRESENT_OR_ABSENT) {
                profileLine.setUsageDetected(Usage.X_NOT_ENFORCED);
              }
            }

          }
          if (testCaseMessagePresent.isPassedTest() && testCaseMessageAbsent.isPassedTest()) {
            countPass++;
            profileLine.setPassed(true);
          }
          countTested++;
          profileLine.setHasRun(true);
          printExampleMessage(testCaseMessagePresent, "I Profiling");
          printExampleMessage(testCaseMessageAbsent, "I Profiling");
          profilingRunCount++;
        }
      } catch (Throwable t) {
        testCaseMessagePresent.setException(t);
      }
      testCaseMessagePresent.setHasRun(true);
      if (profileLine.isHasRun()) {
        saveTestCase(testCaseMessagePresent);
      }
      testCaseMessageAbsent.setHasRun(true);
      if (profileLine.isHasRun()) {
        saveTestCase(testCaseMessageAbsent);
      }
      areaProgress[SUITE_I_PROFILING][0] = makeScore(count, profileLineList.size());
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    if (countTested > 0) {
      areaScore[SUITE_I_PROFILING][0] = makeScore(countPass, countTested);
    }
    areaCount[SUITE_I_PROFILING][0] = profilingRunCount;

    areaProgress[SUITE_I_PROFILING][0] = 100;
  }

  private void prepareAdvanced(Map<Integer, List<Issue>> issueMap) {
    for (Issue issue : Issue.values()) {
      int priority = issue.getPriority();
      List<Issue> issueList = issueMap.get(priority);
      if (issueList == null) {
        issueList = new ArrayList<Issue>();
        issueMap.put(priority, issueList);
      }
      issueList.add(issue);
    }
  }

  private void prepareBasic() {

    int count = 0;
    for (String scenario : statusCheckScenarios) {
      count++;
      TestCaseMessage testCaseMessage = createTestCaseMessage(scenario);
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("A." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      statusCheckTestCaseBasicList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
      String type = "A";
    }

    areaCount[SUITE_A_BASIC][0] = statusCheckTestCaseBasicList.size();
  }

  public void printExampleMessage(TestCaseMessage testCaseMessage, String type) {
    {
      PrintWriter exampleOut = exampleOutSet.get(type);
      if (exampleOut == null) {
        exampleOut = setupExampleFile(type, testCaseMessage);
        if (exampleOut != null) {
          exampleOutSet.put(type, exampleOut);
        }
      }
      if (exampleOut != null) {
        exampleOut.print(testCaseMessage.getMessageTextSent());
      }
    }
    {
      PrintWriter exampleAckOut = exampleAckOutSet.get(type);
      if (exampleAckOut == null) {
        exampleAckOut = setupAckFile(type, testCaseMessage);
        if (exampleAckOut != null) {
          exampleAckOutSet.put(type, exampleAckOut);
        }
      }
      if (exampleAckOut != null) {
        exampleAckOut.print(testCaseMessage.getActualResponseMessage());
      }
    }
  }

  private void updateBasic() {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    count = 0;
    int testAccepted = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        testCaseMessage.setMajorChangesMade(!verifyNoMajorChangesMade(testCaseMessage));
        totalUpdateCount++;
        totalUpdateTime += testRunner.getTotalRunTime();
        if (pass) {
          testAccepted++;
          if (!testCaseMessage.isMajorChangesMade()) {
            testPass++;
          }
        }
        testCaseMessage.setErrorList(testRunner.getErrorList());
        printExampleMessage(testCaseMessage, "A Basic");
      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaProgress[SUITE_A_BASIC][0] = makeScore(count, statusCheckTestCaseBasicList.size());
      saveTestCase(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaScore[SUITE_A_BASIC][0] = makeScore(testPass, statusCheckTestCaseBasicList.size());
    areaProgress[SUITE_A_BASIC][0] = 100;

    if (testAccepted == 0) {
      logStatus("None of the basic messages were accepted. Stopping test process. ");
      keepRunning = false;
    }
  }

  private boolean verifyNoMajorChangesMade(TestCaseMessage testCaseMessage) {
    boolean noMajorChangesMade = true;
    List<Comparison> comparisonList = CompareManager.compareMessages(testCaseMessage.getMessageText(),
        testCaseMessage.getMessageTextSent());
    for (Comparison comparison : comparisonList) {
      if (comparison.isTested() && comparison.getPriorityLevel() <= Comparison.PRIORITY_LEVEL_OPTIONAL) {
        if (!comparison.isPass()) {
          noMajorChangesMade = false;
          break;
        }
      }
    }
    return noMajorChangesMade;
  }

  private static String makeTwoDigits(int i) {
    if (i < 10) {
      return "0" + i;
    } else {
      return "" + i;
    }
  }

  private void prepareIntermediate() {

    int masterCount = 0;
    int count;
    List<Certify.CertifyItem> certifyItems;
    Certify certify = new Certify();

    certifyItems = certify.getCertifyItemList(Certify.FIELD_SEX);
    count = 0;
    masterCount++;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Sex is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PID-8=" + certifyItem.getCode());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_RACE);
    count = 0;
    masterCount++;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Race is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PID-10=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-10.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-10.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_RACE_FULL);
    count = 0;
    masterCount++;
    {
      Certify.CertifyItem certifyItem = certifyItems.get((int) (System.currentTimeMillis() % certifyItems.size()));
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Race is " + certifyItem.getLabel() + " (Random value from CDC full set)");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PID-10=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-10.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-10.3=" + certifyItem.getTable());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ETHNICITY);
    count = 0;
    masterCount++;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Ethnicity is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PID-22=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-22.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-22.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_HISTORICAL);
    count = 0;
    masterCount++;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage;
      if (count == 1) {
        testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      } else {
        testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_CHILD);
      }

      testCaseMessage.setDescription("Information Source is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-9=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-9.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-9.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VFC);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("VFC Status is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("OBX-5=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("OBX-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("OBX-5.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_HOD);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_6_P_VARICELLA_HISTORY_CHILD);
      testCaseMessage.setDescription("History of Disease is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("OBX-5=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("OBX-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("OBX-5.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_REGISTRY_STATUS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Registry Status is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PD1-16=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PD1-17=[NOW]");
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_REFUSAL_REASON);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_5_P_REFUSED_TODDLER);
      testCaseMessage.setDescription("Refusal Reason is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-18=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-18.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-18.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_RELATIONSHIP);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Relationship is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("NK1-3.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("NK1-3.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("NK1-3.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_HISTORICAL_TWO_MONTHS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_TWO_MONTHS_OLD);
      testCaseMessage.setDescription("Historical vaccination is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_HISTORICAL_TWO_YEARS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_TWO_YEARS_OLD);
      testCaseMessage.setDescription("Historical vaccination is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_HISTORICAL_FOUR_YEARS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_FOUR_YEARS_OLD);
      testCaseMessage.setDescription("Historical vaccination is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_HISTORICAL_TWELVE_YEARS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_TWELVE_YEARS_OLD);
      testCaseMessage.setDescription("Historical vaccination is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    String[][] adminChecks = { { "two months", Certify.FIELD_VAC_ADMIN_TWO_MONTHS, SCENARIO_1_R_ADMIN_TWO_MONTHS_OLD },
        { "two years", Certify.FIELD_VAC_ADMIN_TWO_YEARS, SCENARIO_1_R_ADMIN_TWO_YEARS_OLD },
        { "four years", Certify.FIELD_VAC_ADMIN_FOUR_YEARS, SCENARIO_1_R_ADMIN_FOUR_YEARS_OLD },
        { "twelve years", Certify.FIELD_VAC_ADMIN_TWELVE_YEARS, SCENARIO_1_R_ADMIN_TWELVE_YEARS_OLD } };

    List<Certify.CertifyItem> productItems = certify.getCertifyItemList(Certify.FIELD_VAC_PRODUCT);
    List<Certify.CertifyItem> mvxItems = certify.getCertifyItemList(Certify.FIELD_MVX);
    List<Certify.CertifyItem> cptItems = certify.getCertifyItemList(Certify.FIELD_CPT);
    for (int i = 0; i < adminChecks.length; i++) {
      certifyItems = certify.getCertifyItemList(adminChecks[i][1]);
      count = 0;
      masterCount++;

      for (Certify.CertifyItem certifyItem : certifyItems) {
        for (Certify.CertifyItem productCi : productItems) {
          if (productCi.getLabel().equals(certifyItem.getCode())) {
            Certify.CertifyItem mvxItem = null;
            for (Certify.CertifyItem ci : mvxItems) {
              if (ci.getCode().equalsIgnoreCase(productCi.getTable())) {
                mvxItem = ci;
                break;
              }
            }
            if (mvxItem == null) {
              mvxItem = new Certify.CertifyItem();
            }
            {
              count++;
              TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(adminChecks[i][2]);
              testCaseMessage.setDescription("Vaccination administered at " + adminChecks[i][0] + " of age is "
                  + certifyItem.getLabel() + " (" + productCi.getCode() + ")");
              testCaseMessage.setTestCaseSet(testCaseSet);
              testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
              testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
              testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
              testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
              testCaseMessage.appendCustomTransformation("RXA-5.3=CVX");
              testCaseMessage.appendCustomTransformation("RXA-17.1=" + mvxItem.getCode());
              testCaseMessage.appendCustomTransformation("RXA-17.2=" + mvxItem.getLabel());
              testCaseMessage.appendCustomTransformation("RXA-17.3=" + mvxItem.getTable());

              statusCheckTestCaseIntermediateList.add(testCaseMessage);
              transformer.transform(testCaseMessage);
              testCaseMessage.setAssertResult("Accept - *");
              register(testCaseMessage);
            }

            for (Certify.CertifyItem cptCi : cptItems) {
              if (cptCi.getTable().equals(certifyItem.getCode())) {
                {
                  count++;
                  TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(adminChecks[i][2]);
                  testCaseMessage.setDescription("Vaccination administered at " + adminChecks[i][0] + " of age is CPT "
                      + cptCi.getLabel() + " (" + productCi.getCode() + ")");
                  testCaseMessage.setTestCaseSet(testCaseSet);
                  testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
                  testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
                  testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
                  testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
                  testCaseMessage.appendCustomTransformation("RXA-5.3=CVX");
                  testCaseMessage.appendCustomTransformation("RXA-5.4=" + cptCi.getCode());
                  testCaseMessage.appendCustomTransformation("RXA-5.5=" + cptCi.getLabel());
                  testCaseMessage.appendCustomTransformation("RXA-5.6=CPT");
                  testCaseMessage.appendCustomTransformation("RXA-17.1=" + mvxItem.getCode());
                  testCaseMessage.appendCustomTransformation("RXA-17.2=" + mvxItem.getLabel());
                  testCaseMessage.appendCustomTransformation("RXA-17.3=" + mvxItem.getTable());

                  statusCheckTestCaseIntermediateList.add(testCaseMessage);
                  transformer.transform(testCaseMessage);
                  testCaseMessage.setAssertResult("Accept - *");
                  register(testCaseMessage);
                }
              }
            }
          }
        }
      }
    }
    certifyItems = certify.getCertifyItemList(Certify.FIELD_BODY_ROUTE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Body Route is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXR-1.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXR-1.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXR-1.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_BODY_SITE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Body Site is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXR-2.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXR-2.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXR-2.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ADDRESS_TYPE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Additional Address Type is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PID-11.1#2=PID-11.1");
      testCaseMessage.appendCustomTransformation("PID-11.2#2=PID-11.2");
      testCaseMessage.appendCustomTransformation("PID-11.3#2=PID-11.3");
      testCaseMessage.appendCustomTransformation("PID-11.4#2=PID-11.4");
      testCaseMessage.appendCustomTransformation("PID-11.5#2=PID-11.5");
      testCaseMessage.appendCustomTransformation("PID-11.7#2=" + certifyItem.getCode());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_NAME_TYPE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Additional Name Type is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PID-5.1#2=[FATHER]");
      testCaseMessage.appendCustomTransformation("PID-5.2#2=[LAST_DIFFERENT]");
      testCaseMessage.appendCustomTransformation("PID-5.7#2=" + certifyItem.getCode());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_TEL_USE_CODE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Phone Telcommunications Use Code is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      if (certifyItem.getCode().equalsIgnoreCase("NET")) {
        testCaseMessage.appendCustomTransformation("PID-13.3#2=X.400");
        testCaseMessage.appendCustomTransformation("PID-13.4#2=[EMAIL]");
        testCaseMessage.appendCustomTransformation("PID-13.2#2=" + certifyItem.getCode());
      } else if (certifyItem.getCode().equalsIgnoreCase("BPN")) {
        testCaseMessage.appendCustomTransformation("PID-13.2#2=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PID-13.3#2=BP");
      } else {
        testCaseMessage.appendCustomTransformation("PID-13.2=" + certifyItem.getCode());
      }
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_TEL_EQUIPMENT_TYPE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Phone Telcommunications Equipment Type is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      if (certifyItem.getCode().equals("BP")) {
        testCaseMessage.appendCustomTransformation("PID-13.2#2=BPN");
        testCaseMessage.appendCustomTransformation("PID-13.3#2=" + certifyItem.getCode());
      } else if (certifyItem.getCode().equalsIgnoreCase("X.400")) {
        testCaseMessage.appendCustomTransformation("PID-13.2#2=NET");
        testCaseMessage.appendCustomTransformation("PID-13.3#2=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PID-13.4#2=[EMAIL]");
      } else if (certifyItem.getCode().equalsIgnoreCase("Internet")) {
        testCaseMessage.appendCustomTransformation("PID-13.2#2=NET");
        testCaseMessage.appendCustomTransformation("PID-13.3#2=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PID-13.4#2=http://openimmunizationsoftware.net/");
      } else {
        testCaseMessage.appendCustomTransformation("PID-13.3=" + certifyItem.getCode());
      }
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ID_TYPE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      if (certifyItem.getCode().equals("MR")) {
        // do not test this here, already being tested by messages
        continue;
      }
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Person Id Type is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      if (certifyItem.getCode().equals("MA")) {
        testCaseMessage.appendCustomTransformation("PID-3.1#2=[MEDICAID]");
      } else if (certifyItem.getCode().equals("SS")) {
        testCaseMessage.appendCustomTransformation("PID-3.1#2=[SSN]");
      } else {
        testCaseMessage.appendCustomTransformation("PID-3.1#2=[MRN]");
      }
      testCaseMessage.appendCustomTransformation("PID-3.4#2=OIS");
      testCaseMessage.appendCustomTransformation("PID-3.5#2=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_PUBLICITY_CODE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Publicity Code is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PD1-11.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PD1-11.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PD1-11.3=" + certifyItem.getTable());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_COUNTY_CODE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("County Code is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PID-11.4=" + certifyItem.getTable());
      testCaseMessage.appendCustomTransformation("PID-11.9=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_LANGUAGE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Primary Language is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PID-15.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-15.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-15.3=" + certifyItem.getTable());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_COMPLETION);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = null;
      String completionStatus = certifyItem.getCode();
      if (completionStatus.equals("CP") || completionStatus.equals("PA")) {
        testCaseMessage = createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      } else if (completionStatus.equals("NA")) {
        testCaseMessage = createTestCaseMessage(SCENARIO_5_P_REFUSED_TODDLER);
        testCaseMessage.appendCustomTransformation("RXA-5.1=998");
        testCaseMessage.appendCustomTransformation("RXA-5.2=Not administered");
      } else {
        testCaseMessage = createTestCaseMessage(SCENARIO_5_P_REFUSED_TODDLER);
      }

      testCaseMessage.setDescription("Vaccination completion is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-20.1=" + completionStatus);

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ACTION);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Vaccination Action Code is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-21=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ACTION);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Vaccination Action is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-21=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_DEGREE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Vaccination Administering Provider's Degree is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-10.2=[LAST_DIFFERENT]");
      testCaseMessage.appendCustomTransformation("RXA-10.3=[FATHER]");
      testCaseMessage.appendCustomTransformation("RXA-10.21=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    count = 0;
    masterCount++;
    for (int i = 1; i <= 4; i++) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Multiple Birth is First of " + i);
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      if (i == 1) {
        testCaseMessage.appendCustomTransformation("PID-24=N");
        testCaseMessage.appendCustomTransformation("PID-25=");
      } else {
        testCaseMessage.appendCustomTransformation("PID-24=Y");
        testCaseMessage.appendCustomTransformation("PID-25=1");
      }

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);

    }

    count = 0;
    masterCount++;
    for (int i = 2; i <= 4; i++) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Multiple Birth is Last of " + i);
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("B." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());

      testCaseMessage.appendCustomTransformation("PID-24=Y");
      testCaseMessage.appendCustomTransformation("PID-25=" + i);

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    areaCount[SUITE_B_INTERMEDIATE][0] = statusCheckTestCaseIntermediateList.size();
  }

  private List<ForecastTestPanel> forecastTestPanelList = new ArrayList<ForecastTestPanel>();
  private ProfileUsage profileUsage = null;
  private ProfileUsage profileUsageComparisonInteroperability = null;
  private ProfileUsage profileUsageComparisonConformance = null;
  private ProfileManager profileManager = null;
  private File runAgainstFolder = null;

  public File getRunAgainstFolder() {
    return runAgainstFolder;
  }

  public void setRunAgainstFolder(File runAgainstFolder) {
    this.runAgainstFolder = runAgainstFolder;
  }

  public void setProfileUsage(ProfileUsage profileUsage) {
    this.profileUsage = profileUsage;
  }

  public void setProfileUsageComparisonInteroperability(ProfileUsage profileUsageComparisonEhr) {
    this.profileUsageComparisonInteroperability = profileUsageComparisonEhr;
  }

  public void setProfileUsageComparisonConformance(ProfileUsage profileUsageComparisonUs) {
    this.profileUsageComparisonConformance = profileUsageComparisonUs;
  }

  public void setProfileManager(ProfileManager profileManager) {
    this.profileManager = profileManager;
  }

  public void addForecastTestPanel(ForecastTestPanel forecastTestPanel) {
    forecastTestPanelList.add(forecastTestPanel);
  }

  private static final String TCH_FORECAST_TESTER_URL = "http://tchforecasttester.org/ft/ExternalTestServlet";

  private ForecastTesterManager forecastTesterManager = null;

  private void prepareForecastPrep() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, -18);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String eighteen = sdf.format(calendar.getTime());

    int count = 0;
    forecastTesterManager = new ForecastTesterManager(TCH_FORECAST_TESTER_URL);
    try {
      for (ForecastTestPanel testPanel : forecastTestPanelList) {
        List<ForecastTestCase> forecastTestCaseList = forecastTesterManager.getForecastTestCaseList(testPanel);
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
          testCaseMessage.setTestCaseSet(testCaseSet);
          testCaseMessage.setTestCaseCategoryId("F." + makeTwoDigits(1) + "." + makeTwoDigits(count));
          testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
          testCaseMessage.setQuickTransformations(new String[] { "2.5.1",
              (forecastTestCase.getPatientSex().equals("M") ? "BOY" : "GIRL"), "ADDRESS", "PHONE", "MOTHER", "RACE",
              "ETHNICITY" });
          testCaseMessage.appendCustomTransformation("PID-7=" + forecastTestCase.getPatientDob());
          int vaccinationCount = 0;
          for (ForecastTestEvent forecastTestEvent : forecastTestCase.getForecastTestEventList()) {
            if (forecastTestEvent.getEventTypeCode().equals("V")) {
              vaccinationCount++;
              testCaseMessage.appendCustomTransformation("ORC#" + vaccinationCount + "-1=RE");
              testCaseMessage.appendCustomTransformation("ORC#" + vaccinationCount + "-3.1="
                  + forecastTestEvent.getTestEventId());
              testCaseMessage.appendCustomTransformation("ORC#" + vaccinationCount + "-3.2=TCH-FT");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-1=0");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-2=1");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-3="
                  + forecastTestEvent.getEventDate());
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-5.1="
                  + forecastTestEvent.getVaccineCvx());
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-5.2="
                  + forecastTestEvent.getLabel());
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-5.3=CVX");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-6=999");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-9.1=01");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-9.2=Historical");
              testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-9.3=NIP001");
              if (!forecastTestEvent.getVaccineMvx().equals("")) {
                testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-17.1="
                    + forecastTestEvent.getVaccineMvx());
                testCaseMessage.appendCustomTransformation("RXA#" + vaccinationCount + "-17.3=MVX");
              }

            }
          }

          statusCheckTestCaseForecastPrepList.add(testCaseMessage);
          transformer.transform(testCaseMessage);
          testCaseMessage.setAssertResult("Accept - *");
          register(testCaseMessage);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      logStatus("Unable to get forecast schedules because: " + e.getMessage());
    }

    areaCount[SUITE_E_FORECAST_PREP][0] = statusCheckTestCaseForecastPrepList.size();
  }

  private void prepareExceptional() {

    int count = 0;

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendOriginalMessage("OBX|5|NM|6287-7^Baker's yeast IgE Ab in Serum^LN||1945||||||F\n");
      testCaseMessage.setDescription("Tolerance Check : Message includes observation not typically sent to IIS");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendCustomTransformation("RXA-3=" + sdf.format(new Date()));
      testCaseMessage.setDescription("Tolerance Check: RXA-3 contains a time component");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage
          .appendCustomTransformation("RXA-5.2=This is a very long description for a vaccine, that normally you shouldn't expect to see, but since this field should not be read by the receiver it should cause no problem, furthermore HL7 allows this field to have up to 999 characters, which should not cause a problem to the receiver. Also this tests to verify that the receiver is not trying to verify the text of the vaccine. ");
      testCaseMessage
          .setDescription("Tolerance Check: Message includes description for vaccine that is extremely long");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendCustomTransformation("PID-3.4=LOCAL_FACILITY_ASSIGNED_ID");
      testCaseMessage.setDescription("Tolerance Check: Patient identifier assigning authority is very long");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendOriginalMessage("YES|This|is|a|segment^you^should^never^see|in|production\n");
      testCaseMessage.setDescription("Tolerance Check: Message includes segment not defined by HL7");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Tolerance Check: Hospital service code is set to a non-standard value");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("PV1-10=AMB");
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Future Functionality Check: Observation at patient level (HL7 2.8 capability)");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("insert segment OBX after NK1");
      testCaseMessage.appendCustomTransformation("OBX-1=1");
      testCaseMessage.appendCustomTransformation("OBX-2=59784-9");
      testCaseMessage.appendCustomTransformation("OBX-2.2=Disease with presumed immunity");
      testCaseMessage.appendCustomTransformation("OBX-2.3=LN");
      testCaseMessage.appendCustomTransformation("OBX-3=1");
      testCaseMessage.appendCustomTransformation("OBX-5=38907003");
      testCaseMessage.appendCustomTransformation("OBX-5.2=Varicella infection");
      testCaseMessage.appendCustomTransformation("OBX-5.3=SCT");
      testCaseMessage.appendCustomTransformation("OBX-11=F");
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    // TODO add examples of message construction issues that should be ignored

    try {
      count = 0;
      BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
          "exampleCertifiedMessages.txt")));
      String line;
      StringBuilder sb = new StringBuilder();
      String previousDescription = null;
      String description = null;
      while ((line = in.readLine()) != null) {
        line = line.trim();
        if (line.startsWith("--")) {
          description = line.substring(2).trim();
        } else if (line.length() > 3) {
          if (line.startsWith("MSH|^~\\&|")) {
            if (sb != null && sb.length() > 0) {
              count = createCertfiedMessageTestCaseMessage(transformer, count, sb, previousDescription);
            }
            sb = new StringBuilder();
            previousDescription = description;
            description = null;
          }
          if (sb != null) {
            sb.append(line);
            sb.append("\r");
          }
        }
      }
      if (sb != null && sb.length() > 0) {
        count = createCertfiedMessageTestCaseMessage(transformer, count, sb, previousDescription);
      }

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    areaCount[SUITE_D_EXCEPTIONAL][0] = statusCheckTestCaseExceptionalList.size();
  }

  private int createCertfiedMessageTestCaseMessage(Transformer transformer, int count, StringBuilder sb,
      String previousDescription) {
    count++;

    String messageText = sb.toString();

    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(messageText);

    changePatientIdentifyingInformation(messageText, testCaseMessage);

    testCaseMessage.setDescription("Certified Message: "
        + (previousDescription == null ? "Unidentified EHR" : previousDescription));
    testCaseMessage.setTestCaseSet(testCaseSet);
    testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(2) + "." + makeTwoDigits(count));
    testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
    statusCheckTestCaseExceptionalList.add(testCaseMessage);
    transformer.transform(testCaseMessage);
    testCaseMessage.setAssertResult("Accept - *");
    register(testCaseMessage);
    return count;
  }

  private void changePatientIdentifyingInformation(String messageText, TestCaseMessage testCaseMessage) {
    HL7Reader hl7Reader = new HL7Reader(messageText);
    testCaseMessage.appendCustomTransformation("MSH-10=" + messageText.hashCode() + "."
        + (System.currentTimeMillis() % 10000));
    if (hl7Reader.advanceToSegment("PID")) {
      String dob = hl7Reader.getValue(7);
      if (dob.length() >= 8) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
          Date d = sdf.parse(dob.substring(0, 8));
          Calendar c = Calendar.getInstance();
          c.setTime(d);
          int dateOffset = (int) (System.currentTimeMillis() % 100);
          c.add(Calendar.DAY_OF_MONTH, -dateOffset);
          dob = sdf.format(c.getTime());
          testCaseMessage.appendCustomTransformation("PID-7=" + dob);
        } catch (ParseException pe) {
          // ignore
        }
      }
      String sex = hl7Reader.getValue(8);
      testCaseMessage.appendCustomTransformation("PID-5.1=~90%[LAST]:[MOTHER_MAIDEN]");
      if (sex.equals("M")) {
        testCaseMessage.appendCustomTransformation("PID-5.2=[BOY]");
      } else {
        testCaseMessage.appendCustomTransformation("PID-5.2=[GIRL]");
      }
      String middleName = hl7Reader.getValue(5, 3);
      if (!middleName.equals("")) {
        if (middleName.length() > 1) {
          if (sex.equals("M")) {
            testCaseMessage.appendCustomTransformation("PID-5.3=[BOY_MIDDLE]");
          } else {
            testCaseMessage.appendCustomTransformation("PID-5.3=[GIRL_MIDDLE]");
          }
        } else {
          testCaseMessage.appendCustomTransformation("PID-5.3=[BOY_MIDDLE_INITIAL]");
        }
      }
      testCaseMessage.appendCustomTransformation("PID-5.3");
      String streetAdd = hl7Reader.getValue(11);
      if (!streetAdd.equals("")) {
        testCaseMessage.appendCustomTransformation("PID-11.1=[STREET]");
      }
      String motherMaiden = hl7Reader.getValue(6);
      if (!motherMaiden.equals("")) {
        testCaseMessage.appendCustomTransformation("PID-6=~50%[MOTHER_MAIDEN]:[LAST_DIFFERENT]");
      }
      if (!hl7Reader.getValue(6, 2).equals("")) {
        testCaseMessage.appendCustomTransformation("PID-6.2=[MOTHER]");
      }
      String phone = hl7Reader.getValue(13, 7);
      if (!phone.equals("")) {
        if (!hl7Reader.getValue(13, 6).equals("")) {
          testCaseMessage.appendCustomTransformation("PID-13.6=[PHONE_AREA]");
        }
        testCaseMessage.appendCustomTransformation("PID-13.7=[PHONE_LOCAL]");
      }
      String mrnType;
      int i = 1;
      while (!(mrnType = hl7Reader.getValueRepeat(3, 5, i)).equals("")) {
        if (mrnType.equals("SS")) {
          testCaseMessage.appendCustomTransformation("PID-3#" + i + "=[SSN]");
        } else if (mrnType.equals("MA")) {
          testCaseMessage.appendCustomTransformation("PID-3#" + i + "=[MEDICAID]");
        } else {
          testCaseMessage.appendCustomTransformation("PID-3#" + i + "=[MRN]");
        }
        i++;
      }
      while (hl7Reader.advanceToSegment("NK1")) {
        String type = hl7Reader.getValue(3);
        if (type.equals("") || type.equals("MTH") || type.equals("GRD")) {
          if (!hl7Reader.getValue(2).equals("")) {
            testCaseMessage.appendCustomTransformation("NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]");
          }
          if (!hl7Reader.getValue(2, 1).equals("")) {
            testCaseMessage.appendCustomTransformation("NK1-2.2=[MOTHER]");
          }
        } else {
          if (!hl7Reader.getValue(2).equals("")) {
            testCaseMessage.appendCustomTransformation("NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]");
          }
          if (!hl7Reader.getValue(2, 1).equals("")) {
            testCaseMessage.appendCustomTransformation("NK1-2.2=[FATHER]");
          }
        }
      }
    }
  }

  private void updateIntermediate() {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        totalUpdateCount++;
        totalUpdateTime += testRunner.getTotalRunTime();
        testCaseMessage.setHasRun(true);
        if (pass) {
          testPass++;
        }
        testCaseMessage.setErrorList(testRunner.getErrorList());
        printExampleMessage(testCaseMessage, "B Intermediate");
      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaProgress[SUITE_B_INTERMEDIATE][0] = makeScore(count, statusCheckTestCaseIntermediateList.size());
      saveTestCase(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaScore[SUITE_B_INTERMEDIATE][0] = makeScore(testPass, statusCheckTestCaseIntermediateList.size());
  }

  private void updateExceptional() {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        totalUpdateCount++;
        totalUpdateTime += testRunner.getTotalRunTime();
        testCaseMessage.setHasRun(true);
        if (pass) {
          testPass++;
        }
        testCaseMessage.setErrorList(testRunner.getErrorList());
        printExampleMessage(testCaseMessage, "D Exceptional");
      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaProgress[SUITE_D_EXCEPTIONAL][0] = makeScore(count, statusCheckTestCaseExceptionalList.size());
      saveTestCase(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaScore[SUITE_D_EXCEPTIONAL][0] = makeScore(testPass, statusCheckTestCaseExceptionalList.size());

  }

  private void updateForecastPrep() {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseForecastPrepList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        totalUpdateCount++;
        totalUpdateTime += testRunner.getTotalRunTime();
        testCaseMessage.setHasRun(true);
        if (pass) {
          testPass++;
        }
        testCaseMessage.setErrorList(testRunner.getErrorList());
        printExampleMessage(testCaseMessage, "E Forecast Prep");
      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaProgress[SUITE_E_FORECAST_PREP][0] = makeScore(count, statusCheckTestCaseForecastPrepList.size());
      saveTestCase(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaScore[SUITE_E_FORECAST_PREP][0] = makeScore(testPass, statusCheckTestCaseForecastPrepList.size());

  }

  private void queryBasic() {
    int count;
    int testQueryPassRequired = 0;
    int testQueryPassOptional = 0;
    int testQueryCountRequired = 0;
    int testQueryCountOptional = 0;
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
      count++;
      String vxuMessage = testCaseMessage.getMessageText();

      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
      queryTestCaseMessage.setDescription("Query for " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("A." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseBasicList.add(queryTestCaseMessage);
      register(queryTestCaseMessage);
      try {
        long startTime = System.currentTimeMillis();
        String message = prepareSendQueryMessage(queryTestCaseMessage);
        String rspMessage = queryConnector.submitMessage(message, false);
        totalQueryCount++;
        totalQueryTime += System.currentTimeMillis() - startTime;
        queryTestCaseMessage.setHasRun(true);
        queryTestCaseMessage.setActualResponseMessage(rspMessage);
        List<Comparison> comparisonList = CompareManager.compareMessages(vxuMessage, rspMessage);
        queryTestCaseMessage.setComparisonList(comparisonList);
        for (Comparison comparison : comparisonList) {
          if (comparison.isTested() && comparison.getPriorityLevel() <= Comparison.PRIORITY_LEVEL_OPTIONAL) {
            testQueryCountOptional++;
            if (comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_REQUIRED) {
              testQueryCountRequired++;
            }

            if (comparison.isPass() || fieldNotSupported(comparison)) {
              testQueryPassOptional++;
              if (comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_REQUIRED) {
                testQueryPassRequired++;
              }
            }
          }
        }
      } catch (Throwable t) {
        queryTestCaseMessage.setException(t);
      }
      areaProgress[SUITE_A_BASIC][2] = makeScore(count, statusCheckTestCaseBasicList.size());
      areaProgress[SUITE_A_BASIC][1] = areaProgress[SUITE_A_BASIC][2];
      saveTestCase(queryTestCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaScore[SUITE_A_BASIC][1] = makeScore(testQueryPassRequired, testQueryCountRequired);
    areaScore[SUITE_A_BASIC][2] = makeScore(testQueryPassOptional, testQueryCountOptional);
    areaCount[SUITE_A_BASIC][1] = testQueryCountRequired;
    areaCount[SUITE_A_BASIC][2] = testQueryCountOptional;
  }

  public String convertToQuery(TestCaseMessage testCaseMessage) {
    if (queryType.equals(QUERY_TYPE_QBP)) {
      return QueryConverter.convertVXUtoQBP(testCaseMessage.getMessageText());
    }
    if (queryType.equals(QUERY_TYPE_VXQ)) {
      return QueryConverter.convertVXUtoVXQ(testCaseMessage.getMessageText());
    }
    throw new IllegalArgumentException("Unable to convert query because query type '" + queryType
        + "' is not recognized");
  }

  private String prepareSendQueryMessage(TestCaseMessage queryTestCaseMessage) {
    return Transformer.transform(queryConnector, queryTestCaseMessage);
  }

  private void prepareQueryIntermediate() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("BQ." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseIntermediateList.add(queryTestCaseMessage);
      register(queryTestCaseMessage);
    }
  }

  private void prepareQueryExeptional() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("D." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseTolerantList.add(queryTestCaseMessage);
      register(queryTestCaseMessage);
    }
  }

  private void prepareQueryProfiling() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseProfilingList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("I." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseProfilingList.add(queryTestCaseMessage);
      register(queryTestCaseMessage);
    }
  }

  private void prepareQueryForecastPrep() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseForecastPrepList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      queryTestCaseMessage.setForecastTestCase(testCaseMessage.getForecastTestCase());
      statusCheckQueryTestCaseForecastPrepList.add(queryTestCaseMessage);
      register(queryTestCaseMessage);
    }
  }

  private void query(int suite, List<TestCaseMessage> testCaseMessageList) {
    int count;
    int testQueryPassRequired = 0;
    int testQueryPassOptional = 0;
    int testQueryCountRequired = 0;
    int testQueryCountOptional = 0;
    count = 0;
    for (TestCaseMessage queryTestCaseMessage : testCaseMessageList) {
      count++;

      try {
        long startTime = System.currentTimeMillis();
        String message = prepareSendQueryMessage(queryTestCaseMessage);
        String rspMessage = queryConnector.submitMessage(message, false);
        totalQueryCount++;
        totalQueryTime += System.currentTimeMillis() - startTime;
        queryTestCaseMessage.setHasRun(true);
        queryTestCaseMessage.setActualResponseMessage(rspMessage);
        List<Comparison> comparisonList = CompareManager.compareMessages(
            queryTestCaseMessage.getDerivedFromVXUMessage(), rspMessage);
        queryTestCaseMessage.setComparisonList(comparisonList);
        queryTestCaseMessage.setPassedTest(true);
        for (Comparison comparison : comparisonList) {
          if (comparison.isTested() && comparison.getPriorityLevel() <= Comparison.PRIORITY_LEVEL_OPTIONAL) {
            testQueryCountOptional++;
            if (comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_REQUIRED) {
              testQueryCountRequired++;
            }

            if (comparison.isPass() || fieldNotSupported(comparison)) {
              testQueryPassOptional++;
              if (comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_REQUIRED) {
                testQueryPassRequired++;
              }
            } else if (comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_REQUIRED) {
              queryTestCaseMessage.setPassedTest(false);
            }
          }
        }
        if (queryTestCaseMessage.getForecastTestCase() != null) {
          if (connector.getTchForecastTesterSoftwareId() > 0) {
            try {
              String results = forecastTesterManager.reportForecastResults(queryTestCaseMessage.getForecastTestCase(),
                  rspMessage, connector.getTchForecastTesterSoftwareId());
              if (queryTestCaseMessage.getTestCaseNumber() != null
                  && !queryTestCaseMessage.getTestCaseNumber().equals("")) {
                if (testDir != null) {
                  File testCaseFile = new File(testDir, "TC-" + queryTestCaseMessage.getTestCaseNumber()
                      + ".forecast-results.txt");
                  try {
                    PrintWriter out = new PrintWriter(new FileWriter(testCaseFile));
                    out.print(results);
                    out.close();
                  } catch (IOException ioe) {
                    ioe.printStackTrace();
                    // unable to save, continue as normal
                  }
                }
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      } catch (Throwable t) {
        queryTestCaseMessage.setException(t);
      }
      areaProgress[suite][2] = makeScore(count, testCaseMessageList.size());
      areaProgress[suite][1] = areaProgress[suite][2];
      saveTestCase(queryTestCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaProgress[suite][1] = 100;
    areaProgress[suite][2] = 100;
    areaScore[suite][1] = makeScore(testQueryPassRequired, testQueryCountRequired);
    areaScore[suite][2] = makeScore(testQueryPassOptional, testQueryCountOptional);
    areaCount[suite][1] = testQueryCountRequired;
    areaCount[suite][2] = testQueryCountOptional;
  }

  private void saveTestCase(TestCaseMessage tcm) {
    if (testDir != null) {
      CreateTestCaseServlet.saveTestCaseHtml(tcm, testDir);
    }
    CreateTestCaseServlet.saveAnalysis(tcm, connector, session);
  }

  public boolean fieldNotSupported(Comparison comparison) {
    return connector.getQueryResponseFieldsNotReturnedSet() != null
        && connector.getQueryResponseFieldsNotReturnedSet().contains(comparison.getFieldLabel());
  }

  public boolean fieldSupported(Comparison comparison) {
    return connector.getQueryResponseFieldsNotReturnedSet() == null
        || !connector.getQueryResponseFieldsNotReturnedSet().contains(comparison.getFieldLabel());
  }

  public void printResults(PrintWriter out) {
    out.println("<table border=\"1\" cellspacing=\"0\" width=\"720\">");
    out.println("  <tr>");
    out.println("    <th></th>");
    out.println("    <th>Level 1</th>");
    out.println("    <th>Level 2</th>");
    out.println("    <th>Level 3</th>");
    out.println("  </tr>");
    if (run[SUITE_A_BASIC]) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Basic</font><br/><font size=\"-2\">IIS can accept updates from EHR</font></th>");
      if (areaScore[SUITE_A_BASIC][0] >= 100) {
        out.println("    <td class=\"pass\">All NIST 2014 scenarios are accepted. <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_A_BASIC][0] >= 0) {
        out.println("    <td class=\"fail\">Not all NIST 2014 scenarios are accepted. (" + areaScore[SUITE_A_BASIC][0]
            + "% accepted) <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_A_BASIC][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_A_BASIC][0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_A_BASIC][1] >= 100) {
        out.println("    <td class=\"pass\">All required IIS core fields are supported.  <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_A_BASIC][1] >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields are supported. ("
            + areaScore[SUITE_A_BASIC][1]
            + "% of tests passed) <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_A_BASIC][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_A_BASIC][1] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      if (areaScore[SUITE_A_BASIC][2] >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core data were returned when queried. "
            + "<font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_A_BASIC][2] >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core data were returned when queried ("
            + areaScore[SUITE_A_BASIC][2]
            + "% of fields returned) <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_A_BASIC][2] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_A_BASIC][2] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
    }
    out.println("  </tr>");
    if (run[SUITE_B_INTERMEDIATE]) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Intermediate</font><br/><font size=\"-2\">IIS can recognize valid codes</font></th>");
      if (areaScore[SUITE_B_INTERMEDIATE][0] >= 100) {
        out.println("    <td class=\"pass\">All messages with core coded data elements were accepted. "
            + "<font size=\"-1\"><a href=\"#areaBLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_B_INTERMEDIATE][0] >= 0) {
        out.println("    <td class=\"fail\">Some messages with core coded data elements were NOT accepted. ("
            + areaScore[SUITE_B_INTERMEDIATE][0] + "% messages accepted) "
            + "<font size=\"-1\"><a href=\"#areaBLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_B_INTERMEDIATE][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_B_INTERMEDIATE][0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_B_INTERMEDIATE][1] >= 100) {
        out.println("    <td class=\"pass\">All required IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaBLevel2\">(details)</a></font></td>");
      } else if (areaScore[SUITE_B_INTERMEDIATE][1] >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields were returned. ("
            + areaScore[SUITE_B_INTERMEDIATE][1] + "% core fields returned) "
            + "<font size=\"-1\"><a href=\"#areaBLevel2\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_B_INTERMEDIATE][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_B_INTERMEDIATE][1] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      if (areaScore[SUITE_B_INTERMEDIATE][2] >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaBLevel3\">(details)</a></font></td>");
      } else if (areaScore[SUITE_B_INTERMEDIATE][2] >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core fields were returned. ("
            + areaScore[SUITE_B_INTERMEDIATE][2] + "% required and optional were returned) "
            + "<font size=\"-1\"><a href=\"#areaBLevel3\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_B_INTERMEDIATE][2] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_B_INTERMEDIATE][2] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      out.println("  </tr>");
    }
    if (run[SUITE_C_ADVANCED]) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Advanced</font><br/><font size=\"-2\">IIS can identify quality issues</font></th>");
      if (areaScore[SUITE_C_ADVANCED][0] >= 100) {
        out.println("    <td class=\"pass\">All high priority issues were identified. "
            + "<font size=\"-1\"><a href=\"#areaCLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_C_ADVANCED][0] >= 0) {
        out.println("    <td class=\"fail\">Not all high priority issues were identified. ("
            + areaScore[SUITE_C_ADVANCED][0] + "% of issues identified) "
            + "<font size=\"-1\"><a href=\"#areaCLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_C_ADVANCED][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_C_ADVANCED][0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_C_ADVANCED][1] >= 100) {
        out.println("    <td class=\"pass\">All medium priority issues were identified. "
            + "<font size=\"-1\"><a href=\"#areaCLevel2\">(details)</a></font></td>");
      } else if (areaScore[SUITE_C_ADVANCED][1] >= 0) {
        out.println("    <td class=\"fail\">Not all medium priority issues were identified. ("
            + areaScore[SUITE_C_ADVANCED][1] + "% issues identified) "
            + "<font size=\"-1\"><a href=\"#areaCLevel2\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_C_ADVANCED][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_C_ADVANCED][1] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_C_ADVANCED][2] >= 100) {
        out.println("    <td class=\"pass\">All low priority issues were identified. "
            + "<font size=\"-1\"><a href=\"#areaCLevel3\">(details)</a></font></td>");
      } else if (areaScore[SUITE_C_ADVANCED][2] >= 0) {
        out.println("    <td class=\"fail\">Not all low priority issues were identified. ("
            + areaScore[SUITE_C_ADVANCED][2] + "% issues were identified) "
            + "<font size=\"-1\"><a href=\"#areaCLevel3\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_C_ADVANCED][2] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_C_ADVANCED][2] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      out.println("  </tr>");
    }

    if (run[SUITE_I_PROFILING]) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Profiling</font><br/><font size=\"-2\">IIS meets own requirements</font></th>");
      if (areaScore[SUITE_I_PROFILING][0] >= 100) {
        out.println("    <td class=\"pass\">All requirements are confirmed "
            + "<font size=\"-1\"><a href=\"#areaILevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_I_PROFILING][0] >= 0) {
        out.println("    <td class=\"fail\">Not all requirements are confirmed (" + areaScore[SUITE_I_PROFILING][0]
            + "% requirements confirmed) " + "<font size=\"-1\"><a href=\"#areaILevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_I_PROFILING][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_I_PROFILING][0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_I_PROFILING][1] >= 100) {
        out.println("    <td class=\"pass\">All required IIS core fields were returned from messages that were accepted. "
            + "<font size=\"-1\"><a href=\"#areaILevel2\">(details)</a></font></td>");
      } else if (areaScore[SUITE_I_PROFILING][1] >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields were returned from messages that were accepted. ("
            + areaScore[SUITE_I_PROFILING][1]
            + "% core fields returned) "
            + "<font size=\"-1\"><a href=\"#areaILevel2\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_I_PROFILING][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_I_PROFILING][1] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      if (areaScore[SUITE_I_PROFILING][2] >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core fields were returned from messages that were accepted. "
            + "<font size=\"-1\"><a href=\"#areaILevel3\">(details)</a></font></td>");
      } else if (areaScore[SUITE_I_PROFILING][2] >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core fields were returned from messages that were accepted. ("
            + areaScore[SUITE_I_PROFILING][2]
            + "% required and optional were returned) "
            + "<font size=\"-1\"><a href=\"#areaILevel3\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_I_PROFILING][2] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_I_PROFILING][2] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      out.println("  </tr>");
    }

    if (run[SUITE_D_EXCEPTIONAL]) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Exceptional</font><br/><font size=\"-2\">IIS can allow for minor differences</font></th>");
      if (areaScore[SUITE_D_EXCEPTIONAL][0] >= 100) {
        out.println("    <td class=\"pass\">All messages accepted as-is. "
            + "<font size=\"-1\"><a href=\"#areaDLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_D_EXCEPTIONAL][0] >= 0) {
        out.println("    <td class=\"fail\">Some messages were not accepted as-is. ("
            + areaScore[SUITE_D_EXCEPTIONAL][0] + "% messages accepted) "
            + "<font size=\"-1\"><a href=\"#areaDLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_D_EXCEPTIONAL][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_D_EXCEPTIONAL][0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_D_EXCEPTIONAL][1] >= 100) {
        out.println("    <td class=\"pass\">All required IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaDLevel2\">(details)</a></font></td>");
      } else if (areaScore[SUITE_D_EXCEPTIONAL][1] >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields were returned. ("
            + areaScore[SUITE_D_EXCEPTIONAL][1] + "% core fields returned) "
            + "<font size=\"-1\"><a href=\"#areaDLevel2\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_D_EXCEPTIONAL][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_D_EXCEPTIONAL][1] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      if (areaScore[SUITE_D_EXCEPTIONAL][2] >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaDLevel3\">(details)</a></font></td>");
      } else if (areaScore[SUITE_D_EXCEPTIONAL][2] >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core fields were returned. ("
            + areaScore[SUITE_D_EXCEPTIONAL][2] + "% required and optional were returned) "
            + "<font size=\"-1\"><a href=\"#areaDLevel3\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_D_EXCEPTIONAL][2] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_D_EXCEPTIONAL][2] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      out.println("  </tr>");
    }
    if (run[SUITE_E_FORECAST_PREP]) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Forecast Prep</font><br/><font size=\"-2\">IIS can accept shot histories</font></th>");
      if (areaScore[SUITE_E_FORECAST_PREP][0] >= 100) {
        out.println("    <td class=\"pass\">All shot histories were accepted. "
            + "<font size=\"-1\"><a href=\"#areaELevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_E_FORECAST_PREP][0] >= 0) {
        out.println("    <td class=\"fail\">Some shot histories were NOT accepted. ("
            + areaScore[SUITE_E_FORECAST_PREP][0] + "% messages accepted) "
            + "<font size=\"-1\"><a href=\"#areaELevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_E_FORECAST_PREP][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_E_FORECAST_PREP][0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_E_FORECAST_PREP][1] >= 100) {
        out.println("    <td class=\"pass\">All required IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaELevel2\">(details)</a></font></td>");
      } else if (areaScore[SUITE_E_FORECAST_PREP][1] >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields were returned. ("
            + areaScore[SUITE_E_FORECAST_PREP][1] + "% core fields returned) "
            + "<font size=\"-1\"><a href=\"#areaELevel2\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_E_FORECAST_PREP][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_E_FORECAST_PREP][1] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      if (areaScore[SUITE_E_FORECAST_PREP][2] >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaELevel3\">(details)</a></font></td>");
      } else if (areaScore[SUITE_E_FORECAST_PREP][2] >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core fields were returned. ("
            + areaScore[SUITE_E_FORECAST_PREP][2] + "% required and optional were returned) "
            + "<font size=\"-1\"><a href=\"#areaELevel3\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_E_FORECAST_PREP][2] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_E_FORECAST_PREP][2] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      out.println("  </tr>");
    }
    out.println("  <tr>");
    out.println("    <th><font size=\"+1\">Performance</font><br/><font size=\"-2\">IIS can reply quickly</font></th>");
    if (areaProgress[SUITE_G_PERFORMANCE][0] == -1) {
      out.println("    <td>not calculated yet</td>");
    } else {
      if (areaScore[SUITE_G_PERFORMANCE][0] < 3000) {
        out.println("    <td class=\"pass\">Performance was acceptable: " + areaScore[SUITE_G_PERFORMANCE][0]
            + "ms for updates.</td>");
      } else {
        out.println("    <td class=\"fail\">Performance was below acceptable levels: "
            + areaScore[SUITE_G_PERFORMANCE][0] + "ms for updates.</td>");
      }
    }
    if (areaProgress[SUITE_G_PERFORMANCE][1] == -1) {
      out.println("    <td>not calculated yet</td>");
    } else {
      if (areaScore[SUITE_G_PERFORMANCE][1] < 5000) {
        out.println("    <td class=\"pass\">Performance was acceptable: " + areaScore[SUITE_G_PERFORMANCE][1]
            + "ms for queries.</td>");
      } else {
        out.println("    <td class=\"fail\">Performance was below acceptable levels: "
            + areaScore[SUITE_G_PERFORMANCE][1] + "ms for queries.</td>");
      }
    }
    out.println("    <td>not defined</td>");
    out.println("  </tr>");
    if (run[SUITE_H_CONFORMANCE]) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Conformance</font><br/><font size=\"-2\">IIS can respond correctly to requests</font></th>");
      if (areaScore[SUITE_H_CONFORMANCE][0] >= 100) {
        out.println("    <td class=\"pass\">All acknowledgement (ACK) responses meet HL7 and CDC standards. "
            + "<font size=\"-1\"><a href=\"#areaHLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_H_CONFORMANCE][0] >= 0) {
        out.println("    <td class=\"fail\">Not all acknowledgement (ACK) responses meet HL7 and CDC standards. ("
            + areaScore[SUITE_H_CONFORMANCE][0] + "% of messages met standard) "
            + "<font size=\"-1\"><a href=\"#areaHLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_H_CONFORMANCE][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_H_CONFORMANCE][0] + "% complete</td>");
        } else {
          out.println("    <td>not analyzed yet</td>");
        }
      }
      if (areaScore[SUITE_H_CONFORMANCE][1] >= 100) {
        out.println("    <td class=\"pass\">All query response (RSP) messages met HL7 and CDC standards.  "
            + "<font size=\"-1\"><a href=\"#areaHLevel2\">(details)</a></font></td>");
      } else if (areaScore[SUITE_H_CONFORMANCE][1] >= 0) {
        out.println("    <td class=\"fail\">Not all query response (RSP) messages met HL7 and CDC standards. ("
            + areaScore[SUITE_H_CONFORMANCE][1] + "% of fields returned) "
            + "<font size=\"-1\"><a href=\"#areaHLevel2\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_H_CONFORMANCE][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_H_CONFORMANCE][1] + "% complete</td>");
        } else {
          out.println("    <td>not analyzed yet</td>");
        }
      }
      out.println("    <td>not defined</td>");
      out.println("  </tr>");
    }
    out.println("</table>");
    out.println("<br/>");
  }

  public void printReport(PrintWriter out, boolean toFile) {
    if (testFinished == null) {
      out.println("<p>Test in progress, unable to provide final report. <a href=\"IIS Testing Report Detail.html\">See supporting details for current progress. </a></p>");
    } else {
      reportScore[REPORT_1_INTEROP] = 0.0;
      reportScore[REPORT_2_CODED] = 0.0;
      reportScore[REPORT_3_LOCAL] = 0.0;
      reportScore[REPORT_4_NATIONAL] = 0.0;
      reportScore[REPORT_5_TOLERANCE] = 0.0;
      reportScore[REPORT_6_EHR] = 0.0;
      reportScore[REPORT_7_PERFORM] = 0.0;
      reportScore[REPORT_8_ACK] = 0.0;
      Map<CompatibilityConformance, List<ProfileLine>> compatibilityMap = new HashMap<CompatibilityConformance, List<ProfileLine>>();

      if (areaProgress[SUITE_A_BASIC][0] > 0) {
        reportScore[REPORT_1_INTEROP] = areaScore[SUITE_A_BASIC][0] / 100.0;
      }

      if (areaProgress[SUITE_B_INTERMEDIATE][0] > 0) {
        double score = Math.log(areaScore[SUITE_B_INTERMEDIATE][0] + 1) / Math.log(101);
        reportScore[REPORT_2_CODED] = score;
      }

      if (areaProgress[SUITE_I_PROFILING][0] > 0) {
        int countRun = 0;
        {
          int countPass = 0;
          for (ProfileLine profileLine : profileLineList) {
            if (profileLine.isHasRun()) {
              countRun++;
              if (profileLine.isPassed()) {
                countPass++;
              }
            }
          }
          if (countRun > 0) {
            reportScore[REPORT_3_LOCAL] = ((double) countPass) / countRun;
          }
        }
      }
      if (profileLineList != null) {
        for (ProfileLine profileLine : profileLineList) {
          if (profileUsageComparisonConformance != null
              && profileUsageComparisonConformance.getProfileUsageValueMap() != null) {
            ProfileUsageValue profileUsageValueConformance = profileUsageComparisonConformance
                .getProfileUsageValueMap().get(profileLine.getField());
            if (profileUsageValueConformance != null) {
              CompatibilityConformance compatibility = ProfileManager.getCompatibilityConformance(
                  profileLine.getUsageDetected(), profileUsageValueConformance.getUsage());
              List<ProfileLine> profileLineList = compatibilityMap.get(compatibility);
              if (profileLineList == null) {
                profileLineList = new ArrayList<ProfileLine>();
                compatibilityMap.put(compatibility, profileLineList);
              }
              profileLineList.add(profileLine);
            }
          }
        }
        if (compatibilityMap.get(CompatibilityConformance.MAJOR_CONFLICT) != null) {
          reportScore[REPORT_4_NATIONAL] = 0.0;
        } else if (compatibilityMap.get(CompatibilityConformance.MAJOR_CONSTRAINT) != null) {
          reportScore[REPORT_4_NATIONAL] = 0.7;
        } else if (compatibilityMap.get(CompatibilityConformance.CONSTRAINT) != null) {
          reportScore[REPORT_4_NATIONAL] = 0.8;
        } else if (compatibilityMap.get(CompatibilityConformance.ALLOWANCE) != null) {
          reportScore[REPORT_4_NATIONAL] = 0.9;
        } else {
          reportScore[REPORT_4_NATIONAL] = 1.0;
        }
        if (compatibilityMap.get(CompatibilityConformance.CONFLICT) != null) {
          reportScore[REPORT_4_NATIONAL] -= 0.05;
        }
        if (reportScore[REPORT_4_NATIONAL] < 0) {
          reportScore[REPORT_4_NATIONAL] = 0;
        }
      }

      if (areaProgress[SUITE_D_EXCEPTIONAL][0] > 0) {
        int countTolerance = 0;
        int countEhr = 0;
        int countTolerancePass = 0;
        int countEhrPass = 0;

        for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
          if (testCaseMessage.getDescription().startsWith("Tolerance Check:")) {
            countTolerance++;
            if (testCaseMessage.isHasRun() && testCaseMessage.isPassedTest()) {
              countTolerancePass++;
            }
          } else if (testCaseMessage.getDescription().startsWith("Certified Message:")) {
            countEhr++;
            if (testCaseMessage.isHasRun() && testCaseMessage.isPassedTest()) {
              countEhrPass++;
            }
          }
        }
        if (countTolerance == 0) {
          reportScore[REPORT_5_TOLERANCE] = 0.0;
        } else {
          reportScore[REPORT_5_TOLERANCE] = ((double) countTolerancePass) / countTolerance;
        }
        if (countEhr == 0) {
          reportScore[REPORT_6_EHR] = 0.0;
        } else {
          reportScore[REPORT_6_EHR] = ((double) countEhrPass) / countEhr;
        }
      }

      if (areaProgress[SUITE_G_PERFORMANCE][0] > 0) {
        int averageMs = areaScore[SUITE_G_PERFORMANCE][0];
        if (averageMs < 3000) {
          reportScore[REPORT_7_PERFORM] = 1.0;
        } else if (averageMs > 6000) {
          reportScore[REPORT_7_PERFORM] = 0.0;
        } else {
          reportScore[REPORT_7_PERFORM] = 1.0 - ((averageMs - 3000) / 3000.0);
        }
      }

      if (areaProgress[SUITE_H_CONFORMANCE][0] > 0) {
        reportScore[REPORT_8_ACK] = areaScore[SUITE_H_CONFORMANCE][0] / 100.0;
      }

      double overallScore = 0.0;
      overallScore += reportScore[REPORT_1_INTEROP] * 0.4;
      overallScore += reportScore[REPORT_2_CODED] * 0.2;
      overallScore += reportScore[REPORT_3_LOCAL] * 0.1;
      overallScore += reportScore[REPORT_4_NATIONAL] * 0.1;
      overallScore += reportScore[REPORT_5_TOLERANCE] * 0.05;
      overallScore += reportScore[REPORT_6_EHR] * 0.05;
      overallScore += reportScore[REPORT_7_PERFORM] * 0.05;
      overallScore += reportScore[REPORT_8_ACK] * 0.05;

      out.println("<p>This report gives quick view of how the interface responds when receiving VXU messages. "
          + "This report is preliminary and to be used to inform both national and local standardization efforts. This report is "
          + "not a complete nor definitive statement on the quality or abilities of an IIS interface. </p>");
      out.println("<font size=\"+2\" style=\"color: red;\"><em>DRAFT REPORT &#8212; DO NOT DISTRIBUTE</em></font>");
      out.println("<p>This report is not ready for general distribution. "
          + "It is provided by ARIA to local IIS with the request to provide feedback on improvements to the report.   "
          + "The reader should not draw any final conclusions. <p>");
      out.println("<ul>");
      out.println("  <li><a href=\"" + REPORT_EXPLANATION_URL + "\">How to Read This Report</a></li>");
      out.println("  <li><a href=\"IIS Testing Report Detail.html\">Detailed Run Log</a></li>");
      out.println("</ul>");

      out.println("<h2>Overall Score: ");
      out.printf("%.0f", (overallScore * 100));
      out.println("%</h2>");
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL + "#overallScore\">How to read this section...</a></p>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th>Report Section</th>");
      out.println("    <th>Score</th>");
      out.println("    <th width=\"200\">Problem</th>");
      out.println("    <th width=\"200\">Working</th>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#interoperability\">Interoperability</a></td>");
      out.println("    <td>");
      out.printf("%.0f", (reportScore[REPORT_1_INTEROP] * 100));
      out.println("%</td>");
      printBar(out, reportScore[REPORT_1_INTEROP]);
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#codedValues\">Coded Values</a></td>");
      out.println("    <td>");
      out.printf("%.0f", (reportScore[REPORT_2_CODED] * 100));
      out.println("%</td>");
      printBar(out, reportScore[REPORT_2_CODED]);
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#localRequirements\">Local Requirement Implementation</a></td>");
      out.println("    <td>");
      out.printf("%.0f", (reportScore[REPORT_3_LOCAL] * 100));
      out.println("%</td>");
      printBar(out, reportScore[REPORT_3_LOCAL]);
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#nationalCompatibility\">National Compatibility</a></td>");
      out.println("    <td>");
      out.printf("%.0f", (reportScore[REPORT_4_NATIONAL] * 100));
      out.println("%</td>");
      printBar(out, reportScore[REPORT_4_NATIONAL]);
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#tolerance\">Tolerance</a></td>");
      out.println("    <td>");
      out.printf("%.0f", (reportScore[REPORT_5_TOLERANCE] * 100));
      out.println("%</td>");
      printBar(out, reportScore[REPORT_5_TOLERANCE]);
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#ehrExample\">EHR Examples</a></td>");
      out.println("    <td>");
      out.printf("%.0f", (reportScore[REPORT_6_EHR] * 100));
      out.println("%</td>");
      printBar(out, reportScore[REPORT_6_EHR]);
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#performance\">Performance</a></td>");
      out.println("    <td>");
      out.printf("%.0f", (reportScore[REPORT_7_PERFORM] * 100));
      out.println("%</td>");
      printBar(out, reportScore[REPORT_7_PERFORM]);
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#acknowledgmentConformance\">Acknowledgment Conformance</a></td>");
      out.println("    <td>");
      out.printf("%.0f", (reportScore[REPORT_8_ACK] * 100));
      out.println("%</td>");
      printBar(out, reportScore[REPORT_8_ACK]);
      out.println("  </tr>");
      out.println("</table>");

      out.println("<h3>Setup</h3>");
      out.println("<p>Here is some connection details that were used to connect to IIS to create report. </p>");

      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th>Connection</th>");
      out.println("    <td>" + connector.getLabel() + "</td>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Type</th>");
      out.println("    <td>" + connector.getType() + "</td>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Ack Type</th>");
      out.println("    <td>" + connector.getAckType() + "</td>");
      out.println("  </tr>");
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
      out.println("  <tr>");
      out.println("    <th>Test Finished</th>");
      out.println("    <td>" + sdf.format(testFinished) + "</td>");
      out.println("  </tr>");
      out.println("</table>");

      if (!connector.getCustomTransformations().equals("") || connector.getScenarioTransformationsMap().size() > 0) {
        out.println("<h3>Custom Transformations</h3>");
        if (!connector.getCustomTransformations().equals("")) {
          out.println("<p>This interface requires customized Transformations to modify each message before transmitting "
              + "them to the IIS. These transformations can range from setting the correct submitter facility in the "
              + "message header to modifying the structure of the HL7 message to meet local requirements. </p>");
          out.println("  <pre>" + connector.getCustomTransformations() + "</pre>");
        }
        List<String> scenarioList = new ArrayList<String>(connector.getScenarioTransformationsMap().keySet());
        Collections.sort(scenarioList);
        for (String scenario : scenarioList) {
          out.println("  <h4>" + scenario + "</h4>");
          out.println("  <pre>" + connector.getScenarioTransformationsMap().get(scenario) + "</pre>");
        }
      }

      out.println("<div id=\"interoperability\"/>");
      out.println("<h2>Interoperability</h2>");
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL + "#interoperability\">How to read this section...</a></p>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th>Test</th>");
      out.println("    <th>Result</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
        printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("<p><a href=\"IIS Testing Report Detail.html#areaALevel1\">See detailed test results</a></p>");

      out.println("<div id=\"codedValues\"/>");
      out.println("<h2>Coded Values</h2>");
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL + "#codedValues\">How to read this section...</a></p>");
      {
        if (reportScore[REPORT_2_CODED] == 1.0) {
          out.println("<p>All coded value messages were accepted. </p>");
        } else {
          out.println("<table border=\"1\" cellspacing=\"0\">");
          out.println("  <tr>");
          out.println("    <th>Test</th>");
          out.println("    <th>Result</th>");
          out.println("  </tr>");
          for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
            if (testCaseMessage.isHasRun() && !testCaseMessage.isPassedTest()) {
              printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
            }
          }
          out.println("</table>");
        }
      }
      out.println("<p><a href=\"IIS Testing Report Detail.html#areaBLevel1\">See detailed test results</a></p>");

      if (areaProgress[SUITE_I_PROFILING][0] > 0) {
        out.println("<div id=\"localRequirements\"/>");
        out.println("<h2>Local Requirement Implementation</h2>");
        out.println("<p><a href=\"" + REPORT_EXPLANATION_URL
            + "#localRequirements\">How to read this section...</a></p>");

        out.println("<h3>Base Message</h3>");
        out.println("<p>Base Message was accepted.  " + makeTestCaseMessageDetailsLink(tcmFull, toFile) + "</p>");
        if (reportScore[REPORT_3_LOCAL] == 1.0) {
          out.println("<h3>Unexpected Responses</h3>");
          out.println("<p>All local requirements tested were confirmed.</p>");
        } else {
          out.println("<h3>Unexpected Responses</h3>");
          out.println("<table border=\"1\" cellspacing=\"0\">");
          out.println("  <tr>");
          out.println("    <th>Field</th>");
          out.println("    <th>Description</th>");
          out.println("    <th>Expect Accept If</th>");
          out.println("    <th>Status</th>");
          out.println("    <th>Field Present</th>");
          out.println("    <th>Field Absent</th>");
          out.println("  </tr>");
          for (ProfileLine profileLine : profileLineList) {
            if (profileLine.isHasRun() && !profileLine.isPassed()) {
              printProfileLine(out, toFile, profileLine);
            }
          }
          out.println("</table>");
        }
        out.println("<p><a href=\"IIS Testing Report Detail.html#areaILevel1\">See detailed test results</a></p>");

        if (profileUsageComparisonConformance != null) {
          out.println("<div id=\"nationalCompatibility\"/>");
          out.println("<h2>National Compatibility</h2>");
          out.println("<p><a href=\"" + REPORT_EXPLANATION_URL
              + "#nationalCompatibility\">How to read this section...</a> </span></p>");
          printConformanceCompatibility(out, compatibilityMap, CompatibilityConformance.MAJOR_CONFLICT,
              "Local Standard has Major Conflict with National Standards");
          printConformanceCompatibility(out, compatibilityMap, CompatibilityConformance.CONFLICT,
              "Local Standard Conflicts with National Standards");
          printConformanceCompatibility(out, compatibilityMap, CompatibilityConformance.MAJOR_CONSTRAINT,
              "Local Standard Defines Hard Constraint on National Standard");
          printConformanceCompatibility(out, compatibilityMap, CompatibilityConformance.CONSTRAINT,
              "Local Standard Defines Constraint on National Standard");
          printConformanceCompatibility(out, compatibilityMap, CompatibilityConformance.ALLOWANCE,
              "Local Standard Loosens National Constraints");
          printConformanceCompatibility(out, compatibilityMap, CompatibilityConformance.UNABLE_TO_DETERMINE,
              "Local Standard is Not Determined");
        }
      }

      out.println("<div id=\"tolerance\"/>");
      out.println("<h2>Tolerance</h2>");
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL + "#tolerance\">How to read this section...</a></p>");
      if (reportScore[REPORT_5_TOLERANCE] == 1.0) {
        out.println("<p>All tolerance messages were accepted. IIS is tolerant of unusual and unexpected message formats or values.</p>");
      } else {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>Test</th>");
        out.println("    <th>Results</th>");
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
          if (testCaseMessage.getDescription().startsWith("Tolerance Check:") && testCaseMessage.isHasRun()
              && !testCaseMessage.isPassedTest()) {
            printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
          }
        }
        out.println("</table>");
      }
      out.println("<p><a href=\"IIS Testing Report Detail.html#areaDLevel1\">See detailed test results</a></p>");

      out.println("<div id=\"ehrExample\"/>");
      out.println("<h2>EHR Examples</h2>");
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL + "#ehrExamples\">How to read this section...</a></p>");
      if (reportScore[REPORT_6_EHR] == 1.0) {
        out.println("<p>All EHR sample messages were accepted. </p>");
      } else {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>Test</th>");
        out.println("    <th>Results</th>");
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
          if (testCaseMessage.getDescription().startsWith("Certified Message:") && testCaseMessage.isHasRun()
              && !testCaseMessage.isPassedTest()) {
            printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
          }
        }
        out.println("</table>");
      }
      out.println("<p><a href=\"IIS Testing Report Detail.html#areaDLevel1\">See detailed test results</a></p>");

      out.println("<div id=\"performance\"/>");
      out.println("<h2>Performance</h2>");
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL + "#performance\">How to read this section...</a></p>");
      if (areaScore[SUITE_G_PERFORMANCE][0] < 3000) {
        out.println("    <p>Performance was as fast as normally expected: ");
        out.printf("%.2fs processing time for each update</p>", (areaScore[SUITE_G_PERFORMANCE][0] / 1000.0));
      } else {
        out.println("    <p>Performance was slower than was anticipated: ");
        out.printf("%.2fs processing time for each update</p>", (areaScore[SUITE_G_PERFORMANCE][0] / 1000.0));
      }

      out.println("<div id=\"acknowledgmentConformance\"/>");
      out.println("<h2>Acknowledgment Conformance</h2>");
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL
          + "#acknowledgmentConformance\">How to read this section...</a></p>");
      if (reportScore[REPORT_6_EHR] == 1.0) {
        out.println("<p>All response messages (ACKs) conformed to expectations.</p>");
      } else {
        if (reportScore[REPORT_6_EHR] == 0.0) {
          out.println("<p>None of the response messages (ACKs) conformed to expectations.</p>");
        } else {
          out.println("<p>Not all response messages (ACKs) conformed to expectations.</p>");
        }

        for (TestCaseMessage testCaseMessage : ackAnalysisList) {
          HL7Component actualResponseMessageComponent = TestCaseMessageManager.createHL7Component(testCaseMessage);
          if (actualResponseMessageComponent != null) {
            boolean pass = actualResponseMessageComponent.hasNoErrors();
            if (!pass) {
              out.println("<h3>Example Acknowledgement Message</h3>");
              out.println("<pre>" + testCaseMessage.getActualResponseMessage() + "</pre>");
              out.println("<h4>Analysis</h4>");
              ConformanceServlet.printConformanceIssues(out, actualResponseMessageComponent.getConformanceIssueList());
              break;
            }
          }
        }
      }
      out.println("<p><a href=\"IIS Testing Report Detail.html#areaHLevel1\">See detailed test results</a></p>");
      out.println("<div style=\"position: fixed; bottom: 0; right: 0; background-color: red; color: white; font-size: 180%; font-weight: bold; padding-left: 5px; padding-right 5px; \">DRAFT REPORT &#8212; DO NOT DISTRIBUTE</div>");

    }
  }

  private void printBar(PrintWriter out, double score) {
    String message = "Problem";
    if (score >= 0.9) {
      message = "Excellent";
    } else if (score >= 0.8) {
      message = "Well";
    } else if (score >= 0.7) {
      message = "Okay";
    } else if (score >= 0.6) {
      message = "Poor";
    }
    if (score >= 0.6) {
      out.println("    <td>&nbsp;</td>");
      out.println("    <td>");
      out.println("<div style=\"width: " + ((int) (score * 200)) + "px; text-align: center;\" class=\"pass\">"
          + message + "</div>");
      out.println("    </td>");
    } else {
      out.println("    <td>");
      out.println("<div style=\"width: " + ((int) ((1 - score) * 200))
          + "px; text-align: center; position: relative; float: right;\" class=\"fail\">" + message + "</div>");
      out.println("    </td>");
      out.println("    <td>&nbsp;</td>");
    }
  }

  public void printConformanceCompatibility(PrintWriter out,
      Map<CompatibilityConformance, List<ProfileLine>> compatibilityMap, CompatibilityConformance c, String heading) {
    if (compatibilityMap.get(c) != null) {
      out.println("<h3>" + heading + "</h3>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th>Field</th>");
      out.println("    <th>Description</th>");
      out.println("    <th>National Usage</th>");
      out.println("    <th>Documented Usage</th>");
      out.println("    <th>Detected Usage</th>");
      out.println("  </tr>");
      for (ProfileLine profileLine : compatibilityMap.get(c)) {
        ProfileUsageValue profileUsageValueConformance = profileUsageComparisonConformance.getProfileUsageValueMap()
            .get(profileLine.getField());
        if (profileUsageValueConformance != null) {
          out.println("  <tr>");
          out.println("    <td>" + profileLine.getField().getFieldName() + "</td>");
          out.println("    <td>" + profileLine.getField().getDescription() + "</td>");
          out.println("    <td>" + profileUsageValueConformance.getUsage() + "</td>");
          out.println("    <td>" + profileLine.getUsage() + "</td>");
          out.println("    <td>" + profileLine.getUsageDetected() + "</td>");
          out.println("  </tr>");
        }
      }
      out.println("</table>");
    }
  }

  public void printProgressDetails(PrintWriter out, boolean toFile) {

    out.println("<p>The IIS Test process is an automated sequence of tests to verify how closely an IIS HL7 Interface "
        + "conforms to national standards established by colloborate efforts of the Immunization Information Systems Support "
        + "Branch (IISSB) of the Centers for Disease Control and Prevention (CDC), the American Immunization Registry Association "
        + "(AIRA) and IIS community members.  </p>");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
    out.println("<table border=\"1\" cellspacing=\"0\">");
    if (testStarted != null) {
      out.println("  <tr>");
      out.println("    <th>Test Started</th>");
      out.println("    <td>" + sdf.format(testStarted) + "</td>");
      out.println("  </tr>");
    }
    if (testFinished != null) {
      out.println("  <tr>");
      out.println("    <th>Test Finished</th>");
      out.println("    <td>" + sdf.format(testFinished) + "</td>");
      out.println("  </tr>");
    }
    out.println("  <tr>");
    out.println("    <th>Test Status</th>");
    out.println("    <td>" + status + "</td>");
    out.println("  </tr>");
    synchronized (statusMessageList) {
      out.println("  <tr>");
      out.println("    <th>Currently</th>");
      out.println("    <td>" + statusMessageList.get(statusMessageList.size() - 1) + "</td>");
      out.println("  </tr>");
    }
    out.println("  <tr>");
    out.println("    <th>Basic Tests</th>");
    out.println("    <td>" + (run[SUITE_A_BASIC] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Intermediate Tests</th>");
    out.println("    <td>" + (run[SUITE_B_INTERMEDIATE] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Advanced Tests</th>");
    out.println("    <td>" + (run[SUITE_C_ADVANCED] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Profiling Tests</th>");
    out.println("    <td>" + (run[SUITE_I_PROFILING] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Exceptional Tests</th>");
    out.println("    <td>" + (run[SUITE_D_EXCEPTIONAL] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Forecast Prep</th>");
    out.println("    <td>" + (run[SUITE_E_FORECAST_PREP] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Performance Tests</th>");
    out.println("    <td>Enabled</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Conformance Tests</th>");
    out.println("    <td>" + (run[SUITE_H_CONFORMANCE] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Update Test Count</th>");
    out.println("    <td>" + totalUpdateCount + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Query Test Count</th>");
    out.println("    <td>" + totalQueryCount + "</td>");
    out.println("  </tr>");
    if (connector.getQueryResponseFieldsNotReturnedSet() != null) {
      out.println("  <tr>");
      out.println("    <th>Query Fields Not Returned</th>");
      out.print("    <td>");
      boolean notFirst = false;
      for (String value : connector.getQueryResponseFieldsNotReturnedSet()) {
        if (notFirst) {
          out.print(", ");
        }
        notFirst = true;
        out.print(value);
      }
      out.println("</td>");
      out.println("  </tr>");
    }
    out.println("</table>");

    out.println("<p>IIS Test Log</p>");
    synchronized (statusMessageList) {
      out.println("<pre>");
      for (String statusMessage : statusMessageList) {
        out.println(statusMessage);
      }
      out.println("</pre>");
    }

    if (exception != null) {
      out.println("<p>Exception occurred during processing. Unable to complete analysis as expected. </p>");
      out.println("<pre>");
      exception.printStackTrace(out);
      out.println("</pre>");
    }

    out.println("<p>The test process utilizes a real time connection to the IIS HL7 interface. "
        + "Connections are established using the Simple Message Mover standard for connecting "
        + "to IIS. This standard supports the most common used Web Service and HTTPS standards. </p>");

    if (!connector.getCustomTransformations().equals("")) {
      out.println("<p>This interface requires customized Transformations to modify each message before transmitting "
          + "them to the IIS. These transformations can range from setting the correct submitter facility in the "
          + "message header to modifying the structure of the HL7 message to meet local requirements. </p>");
    }

    out.println("<table border=\"1\" cellspacing=\"0\">");
    out.println("  <tr>");
    out.println("    <th>Connection</th>");
    out.println("    <td>" + connector.getLabel() + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Type</th>");
    out.println("    <td>" + connector.getType() + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Ack Type</th>");
    out.println("    <td>" + connector.getAckType() + "</td>");
    out.println("  </tr>");
    out.println("</table>");

    if (!connector.getCustomTransformations().equals("") || connector.getScenarioTransformationsMap().size() > 0) {
      out.println("<h3>Custom Transformations</h3>");
      if (!connector.getCustomTransformations().equals("")) {
        out.println("  <h4>Overall</h4>");
        out.println("  <pre>" + connector.getCustomTransformations() + "</pre>");
      }
      List<String> scenarioList = new ArrayList<String>(connector.getScenarioTransformationsMap().keySet());
      Collections.sort(scenarioList);
      for (String scenario : scenarioList) {
        out.println("  <h4>" + scenario + "</h4>");
        out.println("  <pre>" + connector.getScenarioTransformationsMap().get(scenario) + "</pre>");
      }
    }

    if (queryConnector != connector) {
      out.println("<p>The connection information for performing queries is different than for the updates. This is to either accomodate "
          + "different standards for message format or to allow for different credentials and URL. </p>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th>Query Connection</th>");
      out.println("    <td>" + queryConnector.getLabel() + "</td>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Type</th>");
      out.println("    <td>" + queryConnector.getType() + "</td>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Ack Type</th>");
      out.println("    <td>" + queryConnector.getAckType() + "</td>");
      out.println("  </tr>");
      if (!queryConnector.getCustomTransformations().equals("")) {
        out.println("  <tr>");
        out.println("    <th>Transformations</th>");
        out.println("    <td><pre>" + queryConnector.getCustomTransformations() + "</pre></td>");
        out.println("  </tr>");
      }
      out.println("</table>");
      out.println("</br>");
    }

    out.println("<h3>Test Counts</h3>");
    out.println("<table border=\"1\" cellspacing=\"0\">");
    out.println("  <tr>");
    out.println("    <th>Area</th>");
    out.println("    <th>Level 1</th>");
    out.println("    <th>Level 2</th>");
    out.println("    <th>Level 3</th>");
    out.println("  </tr>");
    for (int i = 0; i < SUITE_COUNT; i++) {
      if (run[i]) {
        out.println("  <tr>");
        out.println("    <td>" + areaLabel[i] + "</td>");
        for (int j = 0; j < 3; j++) {
          if (areaCount[i][j] == -1) {
            out.println("    <td>&nbsp;</td>");
          } else {
            out.println("    <td>" + areaCount[i][j] + "</td>");
          }
        }
      }
    }
    out.println("</table>");
    out.println("</br>");

    int testNum = 0;
    if (areaProgress[SUITE_A_BASIC][0] > 0) {
      out.println("<h2>Basic Interoperability Tests</h2>");
      out.println("<p><b>Purpose</b>: Test to see if the IIS can accept updates from certified EHR systems. </p>");
      out.println("<p><b>Requirements</b>: IIS must accept all 7 NIST messages than an EHR is required to be able to send in 2014. "
          + "In addition the IIS should be able to store all IIS required core fields, and if possible all the "
          + "IIS optional core fields. </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: The IIS must be able to accept replicas of all NIST messages. </li>");
      out.println("  <li>Level 2: The IIS should return all required 2007 IIS Core Data included in the NIST messages.</li>");
      out.println("  <li>Level 3: The IIS may return all IIS 2013-2017 Core Data that was included in the NIST messages.</li>");
      out.println("</ul>");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
        testNum++;
        out.println("<div id=\"areaALevel1\"/>");
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>Basic Test #" + testNum + "</th>");
        out.println("    <td><em>" + testCaseMessage.getDescription() + "</em></td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <th>Level 1</th>");
        if (testCaseMessage.isMajorChangesMade()) {
          out.println("    <td class=\"fail\">Message was significantly changed in the process of submitting it "
              + makeCompareDetailsLink(testCaseMessage, toFile, true));
          out.println("<br/>");
          if (testCaseMessage.isAccepted()) {
            out.println("Message was accepted.");
          } else {
            out.println(" Message was rejected.");
          }

          out.println(makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
        } else if (testCaseMessage.isAccepted()) {
          out.println("    <td class=\"pass\">Message was accepted. "
              + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
        } else if (testCaseMessage.isHasRun()) {
          out.println("    <td class=\"fail\">");
          out.println("Message was not accepted. " + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
          out.println("</td>");
        } else if (testCaseMessage.getException() != null) {
          out.println("    <td class=\"fail\">");
          out.println("Exception when transmitting message: " + testCaseMessage.getException().getMessage() + ". "
              + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
          out.println("</td>");
        } else {
          out.println("    <td class=\"nottested\">not run yet</td>");
        }
        out.println("  </tr>");
        if (statusCheckQueryTestCaseBasicList.size() >= testNum) {
          testCaseMessage = statusCheckQueryTestCaseBasicList.get(testNum - 1);
          out.println("  <tr>");
          out.println("    <th>Level 2</th>");
          {
            boolean hasRun = false;
            boolean hasPassed = true;
            boolean hasUnsupported = false;
            if (testCaseMessage.getComparisonList() != null) {
              hasRun = true;
              for (Comparison comparison : testCaseMessage.getComparisonList()) {
                if (comparison.isTested() && comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_REQUIRED) {
                  if (!comparison.isPass()) {
                    if (fieldSupported(comparison)) {
                      hasPassed = false;
                      break;
                    } else {
                      hasUnsupported = true;
                    }
                  }
                }
              }
            }
            if (hasPassed) {
              if (hasUnsupported) {
                out.println("    <td class=\"pass\">All required fields returned except those not supported: ");
                out.println("      <br/> <ul>");
                for (Comparison comparison : testCaseMessage.getComparisonList()) {
                  if (comparison.isTested() && comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_REQUIRED
                      && !comparison.isPass() && fieldNotSupported(comparison)) {
                    out.println("      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel()
                        + "</li>");
                  }
                }
                out.println("      </ul>");
                out.println(makeCompareDetailsLink(testCaseMessage, toFile, false));
              } else {
                out.println("    <td class=\"pass\">All required fields returned. "
                    + makeCompareDetailsLink(testCaseMessage, toFile, false));
              }
              out.println(makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
              out.println("    </td>");
            } else if (hasRun) {
              out.println("    <td class=\"fail\">Required fields not returned:");
              out.println("      <ul>");
              for (Comparison comparison : testCaseMessage.getComparisonList()) {
                if (comparison.isTested() && comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_REQUIRED
                    && !comparison.isPass()) {
                  if (fieldSupported(comparison)) {
                    out.println("      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel()
                        + "</li>");
                  } else {
                    out.println("      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel()
                        + " (not expected to be returned in query)</li>");
                  }
                }
              }
              out.println("      </ul>");
              out.println("      " + makeCompareDetailsLink(testCaseMessage, toFile, false));
              out.println(makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
              out.println("    </td>");
            } else if (testCaseMessage.getException() != null) {
              out.println("    <td class=\"fail\">");
              out.println("Exception when transmitting message: " + testCaseMessage.getException().getMessage() + ". "
                  + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
              out.println("</td>");
            } else {
              out.println("    <td class=\"nottested\">not run yet</td>");
            }
          }
          out.println("  </tr>");
          out.println("  <tr>");
          out.println("    <th>Level 3</th>");

          {
            boolean hasRun = false;
            boolean hasPassed = true;
            if (testCaseMessage.getComparisonList() != null) {
              hasRun = true;
              for (Comparison comparison : testCaseMessage.getComparisonList()) {
                if (comparison.isTested() && comparison.getPriorityLevel() <= Comparison.PRIORITY_LEVEL_OPTIONAL) {
                  if (!comparison.isPass()) {
                    hasPassed = false;
                    break;
                  }
                }
              }
            }
            if (hasPassed) {
              out.println("    <td class=\"pass\">All required and optional fields returned. "
                  + makeCompareDetailsLink(testCaseMessage, toFile, false));
            } else if (hasRun) {
              boolean hasOptionalFields = false;
              for (Comparison comparison : testCaseMessage.getComparisonList()) {
                if (comparison.isTested() && comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_OPTIONAL
                    && !comparison.isPass()) {
                  hasOptionalFields = true;
                  break;
                }
              }
              if (hasOptionalFields) {
                out.println("    <td class=\"fail\">Optional fields not returned:");
                out.println("      <ul>");
                for (Comparison comparison : testCaseMessage.getComparisonList()) {
                  if (comparison.isTested() && comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_OPTIONAL
                      && !comparison.isPass()) {
                    out.println("      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel()
                        + "</li>");
                  }
                }
                out.println("      </ul>");
              } else {
                out.println("    <td class=\"fail\">Level 2 not passed. ");
              }
              out.println("      " + makeCompareDetailsLink(testCaseMessage, toFile, false));
              out.println("    </td>");
            } else if (testCaseMessage.getException() != null) {
              out.println("    <td class=\"fail\">");
              out.println("Exception when transmitting message: " + testCaseMessage.getException().getMessage() + ". "
                  + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
              out.println("</td>");
            } else {
              out.println("    <td class=\"nottested\">not run yet</td>");
            }
          }
          out.println("  </tr>");
        }
        out.println("</table>");
        out.println("</br>");
      }
    }

    if (areaProgress[SUITE_B_INTERMEDIATE][0] > 0) {

      out.println("<h2>Basic Interoperability Tests</h2>");
      out.println("<p><b>Purpose</b>: Test to see if the IIS can accept recognize valid codes. </p>");
      out.println("<p><b>Requirements</b>: IIS must accept all valid codes for IIS core required and "
          + "optional fields, and not reject messages because of invalid or unrecognized codes in optional "
          + "fields. In addition the IIS should be able to store all IIS required core fields, and "
          + "if possible all the IIS optional core fields. </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: The IIS must be able to accept all valid codes, defined by the current CDC Implementation Guide. </li>");
      out.println("  <li>Level 2: The IIS should return all required 2007 IIS Core Data that was submitted for Level 1 testing. </li>");
      out.println("  <li>Level 3: The IIS may return all IIS 2013-2017 Core Data that was submitted for Level 1 testing.</li>");
      out.println("</ul>");

      out.println("<div id=\"areaBLevel1\"/>");
      out.println("<h3>Intermediate Tests - Level 1 - Accepting Core Data</h3>");
      String previousFieldName = "";
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
        previousFieldName = printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage, previousFieldName);
      }
      if (!previousFieldName.equals("")) {
        out.println("</ul>");
      }
    }
    if (areaProgress[SUITE_B_INTERMEDIATE][1] > 0) {

      out.println("<div id=\"areaBLevel2\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Intermediate Tests - Level 2 - Supports Required</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseIntermediateList) {
        printTestCaseMessageDetailsQueryRequired(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }
    if (areaProgress[SUITE_B_INTERMEDIATE][2] > 0) {
      out.println("<div id=\"areaBLevel3\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Intermediate Tests - Level 3 - Supports Optional</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseIntermediateList) {
        printTestCaseMessageDetailsQueryOptional(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }

    for (int i = 0; i < areaScore[SUITE_C_ADVANCED].length; i++) {
      if (profileTestCaseLists[i] == null || areaProgress[SUITE_C_ADVANCED][i] < 100) {
        continue;
      }

      if (i == 0) {
        out.println("<h2>Advanced Interoperability Tests</h2>");
        out.println("<p><b>Purpose</b>: Test to see if the IIS can identify quality issues. </p>");
        out.println("<p><b>Requirements</b>: IIS must be able to identify critical data quality issues in messages. "
            + "These include items that were documented and detailed as part of the 2008 Data Quality Assurance for IIS: "
            + "Incoming Data project. </p>");
        out.println("<ul>");
        out.println("  <li>Level 1: The IIS must be able to identify all Level 1 priority issues. For each issue the ACK message returned should indicate that the IIS recognized the quality issue. The identified severity of the issue is not evaluated for this test.  </li>");
        out.println("  <li>Level 2: The IIS should be able to identify all Level 2 priority issues. For each issue the ACK message returned should indicate that the IIS recognized the quality issue. The identified severity of the issue is not evaluated for this test.</li>");
        out.println("  <li>Level 3: The IIS may be able to identify all Level 3 priority issues. For each issue the ACK message returned should indicate that the IIS recognized the quality issue. The identified severity of the issue is not evaluated for this test.</li>");
        out.println("</ul>");
      }

      int priority = i + 1;
      out.println("<div id=\"areaCLevel" + priority + "\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      if (i == 0) {
        out.println("    <th colspan=\"2\">Advanced Tests - Level " + priority + " - High Priority</th>");
      } else if (i == 1) {
        out.println("    <th colspan=\"2\">Advanced Tests - Level " + priority + " - Medium Priority</th>");

      } else {

        out.println("    <th colspan=\"2\">Advanced Tests - Level " + priority + " - Low Priority </th>");
      }
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : profileTestCaseLists[i]) {
        String classText = "nottested";
        if (testCaseMessage.hasIssue() && testCaseMessage.isHasRun()) {
          classText = testCaseMessage.isPassedTest() ? "pass" : "fail";
        }
        out.println("  <tr>");
        out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
        if (!testCaseMessage.hasIssue()) {
          out.println("    <td class=\"" + classText + "\">issue is not supported by this tester</td>");
        } else {
          if (testCaseMessage.isHasRun()) {
            if (testCaseMessage.isPassedTest()) {
              out.println("    <td class=\"" + classText + "\">Issue was identified. "
                  + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
            } else {
              out.println("    <td class=\"" + classText + "\">Issue was NOT identified. "
                  + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
            }
          } else if (testCaseMessage.getException() != null) {
            out.println("    <td class=\"fail\">");
            out.println("Exception when transmitting message: " + testCaseMessage.getException().getMessage() + ". "
                + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
            out.println("</td>");
          } else {
            out.println("    <td class=\"" + classText + "\">not run yet</td>");
          }
        }
        out.println("  </tr>");
      }
      out.println("</table>");
      out.println("</br>");
    }
    if (tcmFull != null && tcmFull.isHasRun() && !tcmFull.isPassedTest()) {
      out.println("<h2>Profiling Tests</h2>");
      out.println("<p>Profiling tests could not be run. Base profiling message was not accepted. "
          + makeTestCaseMessageDetailsLink(tcmFull, toFile) + "</p>");
    }
    if (areaProgress[SUITE_I_PROFILING][0] > 0) {
      out.println("<div id=\"areaILevel1\"/>");
      out.println("<h2>Profiling Tests</h2>");
      out.println("<p><b>Purpose</b>: Test to see if the IIS is consistent with interface specification. </p>");
      out.println("<p><b>Requirements</b>: IIS should implement interface consistent with its own stated requirements. </p>");
      out.println("<h3>Base Message</h3>");
      out.println("<p>The first step is to run a base message and ensure that it is accepted. "
          + "The base message is a fully populated message that is expected to be accepted by most IIS. "
          + "Since the IIS has accepted this message we can use this as a basis for making assumptions about "
          + "which fields the IIS will accept without generating rejecting all or part of the message. </p>");

      out.println("<p>Base Message was accepted.  " + makeTestCaseMessageDetailsLink(tcmFull, toFile) + "</p>");

      String segmentName = "";
      for (ProfileLine profileLine : profileLineList) {
        if ((profileLine.getUsage() != Usage.NOT_DEFINED && profileUsage.getCategory() != ProfileCategory.US)
            || (profileLine.getTestCaseMessagePresent() != null && profileLine.getTestCaseMessagePresent().hasIssue()
                && profileLine.getTestCaseMessageAbsent() != null && profileLine.getTestCaseMessageAbsent().hasIssue())) {
          if (!segmentName.equals(profileLine.getField().getSegmentName())) {
            if (!segmentName.equals("")) {
              out.println("</table>");
            }
            out.println("<h3>" + profileLine.getField().getSegmentName() + " Segment</h3>");
            out.println("<table border=\"1\" cellspacing=\"0\">");
            out.println("  <tr>");
            out.println("    <th>Field</th>");
            out.println("    <th>Description</th>");
            out.println("    <th>Expect Accept If</th>");
            out.println("    <th>Status</th>");
            out.println("    <th>Field Present</th>");
            out.println("    <th>Field Absent</th>");
            out.println("  </tr>");
            segmentName = profileLine.getField().getSegmentName();
          }
          printProfileLine(out, toFile, profileLine);
        }
      }
      out.println("</table>");
      if (profileUsageComparisonConformance != null) {
        out.println("<h2>Conformance to " + profileUsageComparisonConformance + "</h2>");
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>Field</th>");
        out.println("    <th>Description</th>");
        out.println("    <th>Conformance Usage</th>");
        out.println("    <th>Detected Usage</th>");
        out.println("    <th>Status</th>");
        out.println("  </tr>");
        for (ProfileLine profileLine : profileLineList) {
          if (profileLine.getUsage() != Usage.NOT_DEFINED) {
            printProfileLineComparisonConformance(out, toFile, profileLine);
          }
        }
        out.println("</table>");
      }
      if (profileUsageComparisonInteroperability != null) {
        out.println("<h3>Interoperability with " + profileUsageComparisonConformance + "</h3>");
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>Field</th>");
        out.println("    <th>Description</th>");
        out.println("    <th>" + connector.getLabel() + " Usage</th>");
        out.println("    <th>Comparison Usage</th>");
        out.println("  </tr>");
        for (ProfileLine profileLine : profileLineList) {
          printProfileLineComparisonInteroperability(out, toFile, profileLine);
        }
        out.println("</table>");
      }
      out.println("</br>");
    }
    if (areaProgress[SUITE_I_PROFILING][1] > 0) {
      out.println("<div id=\"areaILevel2\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Profiling Tests - Level 2 - Supports Required</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseProfilingList) {
        printTestCaseMessageDetailsQueryRequired(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }
    if (areaProgress[SUITE_I_PROFILING][2] > 0) {
      out.println("<div id=\"areaILevel3\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Profiling Tests - Level 3 - Supports Optional</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseProfilingList) {
        printTestCaseMessageDetailsQueryOptional(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }

    if (areaProgress[SUITE_D_EXCEPTIONAL][0] > 0) {
      out.println("<div id=\"areaDLevel1\"/>");
      out.println("<h2>Tolerance and EHR Examples</h2>");
      out.println("<p><b>Purpose</b>: Test to see if the IIS can allow for minor differences. </p>");
      out.println("<p><b>Requirements</b>: IIS must be able to accept input outside of what the IIS "
          + "expects which does not directly impact HL7 message structure or the quality of core IIS fields. "
          + "In short the IIS should be tolerant of minor message format and content issues.  </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: The IIS must be able to accept update messages with incorrect data in fields that are not critical to HL7 message format and are not used to transmit IIS core data; be able to accept example update messages from EHR systems that pass EHR certification; be able to accept sample update messages that transmit IIS core data that is defined by the IIS Implementation Guide but were not tested by EHR Certification.</li>");
      out.println("  <li>Level 2: The IIS should return all required 2007 IIS Core Data that was submitted for Level 1 testing.</li>");
      out.println("  <li>Level 3: The IIS may return all IIS 2013-2017 Core Data that was submitted for Level 1 testing. </li>");
      out.println("</ul>");

      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Tolerance Tests - Level 1 - Allows minor differences</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
        printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }
    if (areaProgress[SUITE_D_EXCEPTIONAL][1] > 0) {
      out.println("<div id=\"areaDLevel2\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Exceptional Tests - Level 2 - Supports Required</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseTolerantList) {
        printTestCaseMessageDetailsQueryRequired(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }
    if (areaProgress[SUITE_D_EXCEPTIONAL][2] > 0) {
      out.println("<div id=\"areaDLevel3\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Exceptional Tests - Level 3 - Supports Optional</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseTolerantList) {
        printTestCaseMessageDetailsQueryOptional(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }

    if (areaProgress[SUITE_E_FORECAST_PREP][0] > 0) {

      out.println("<h2>Forecast Prep Tests</h2>");
      out.println("<p><b>Purpose</b>: Test to submit patient histories to IIS in preparation for retrieving forecasts. </p>");
      out.println("<p><b>Requirements</b>: IIS should be able to accept and store vaccination histories. </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: The IIS must be able to accept vaccination histories. </li>");
      out.println("  <li>Level 2: The IIS should return all required 2007 IIS Core Data that was submitted for Level 1 testing. </li>");
      out.println("  <li>Level 3: The IIS may return all IIS 2013-2017 Core Data that was submitted for Level 1 testing.</li>");
      out.println("</ul>");

      out.println("<div id=\"areaELevel1\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Forecaster Prep Tests - Level 1 - Accepting Vaccination Histories</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseForecastPrepList) {
        printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }
    if (areaProgress[SUITE_E_FORECAST_PREP][1] > 0) {

      out.println("<div id=\"areaELevel2\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Forecaster Prep Tests - Level 2 - Supports Required</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseForecastPrepList) {
        printTestCaseMessageDetailsQueryRequired(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }
    if (areaProgress[SUITE_E_FORECAST_PREP][2] > 0) {
      out.println("<div id=\"areaELevel3\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Forecaster Prep Tests - Level 3 - Supports Optional</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseForecastPrepList) {
        printTestCaseMessageDetailsQueryOptional(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }

    if (areaProgress[SUITE_H_CONFORMANCE][0] > 0) {
      out.println("<h2>Conformance Tests</h2>");
      out.println("<p><b>Purpose</b>: Test to see if the IIS can respond correctly to requests. </p>");
      out.println("<p><b>Requirements</b>: IIS must be able to return an ACK message that meets HL7 and CDC Standards "
          + "for format and structure. IIS should also be able to return RSP messages in response to "
          + "QBP messages that meet HL7 and CDC Standards for format and structure. </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: That IIS can return an acknowledgement (ACK) messages that meet HL7 and CDC standards for format and content. Return messages correctly indicate if messages were rejected or accepted according to the CDC standard.</li>");
      out.println("  <li>Level 2: IIS supports QDP/RSP and RSP message meets HL7 and CDC standards for format and content. IIS must be able to return a single record match for a patient submitted by VXU when the patient is queried using the same information supplied in the VXU. The query results should also include a forecast recommendation.</li>");
      out.println("</ul>");

      out.println("<div id=\"areaHLevel1\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Conformance Tests - Level 1 - ACK Incorrectly Formatted</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : ackAnalysisList) {
        HL7Component actualResponseMessageComponent = TestCaseMessageManager.createHL7Component(testCaseMessage);
        if (actualResponseMessageComponent == null || actualResponseMessageComponent.hasNoErrors()) {
          continue;
        }
        out.println("  <tr>");
        out.println("    <td class=\"fail\"><em>" + testCaseMessage.getDescription() + "</em></td>");
        out.println("    <td class=\"fail\">Ack did not meet HL7 or CDC standards. "
            + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
        out.println("  </tr>");
      }
      out.println("</table>");
      out.println("</br>");
    }

    if (areaProgress[SUITE_H_CONFORMANCE][1] > 0) {
      out.println("<div id=\"areaHLevel2\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Conformance Tests - Level 2 - RSP Correctly Formatted</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : rspAnalysisList) {
        String classText = "nottested";
        HL7Component actualResponseMessageComponent = TestCaseMessageManager.createHL7Component(testCaseMessage);
        if (actualResponseMessageComponent != null) {
          classText = actualResponseMessageComponent.hasNoErrors() ? "pass" : "fail";
        }
        out.println("  <tr>");
        out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
        if (actualResponseMessageComponent != null) {
          if (actualResponseMessageComponent.hasNoErrors()) {
            out.println("    <td class=\"" + classText + "\">RSP meets HL7 and CDC standards.  "
                + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
          } else {
            out.println("    <td class=\"" + classText + "\">RSP did not meet HL7 or CDC standards. "
                + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
          }
        } else if (testCaseMessage.getException() != null) {
          out.println("    <td class=\"fail\">");
          out.println("Exception when transmitting message: " + testCaseMessage.getException().getMessage() + ". "
              + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
          out.println("</td>");
        } else {
          out.println("    <td class=\"" + classText + "\">not run yet</td>");
        }
        out.println("  </tr>");
      }
      out.println("</table>");
      out.println("</br>");
    }
  }

  private static class ConsolidateResults
  {
    String filename = null;
    int acceptCount = 0;
    int notAcceptCount = 0;

    public String makeLink() {
      return " <font size=\"-1\"><a href=\"" + filename + "\">" + acceptCount + " accepted / " + notAcceptCount
          + " not accepted</a></font>";
    }
  }

  private ConsolidateResults consolidate(TestCaseMessage tcm) throws IOException {
    ConsolidateResults cr = new ConsolidateResults();
    String filenameStart = "TCA-" + tcm.getTestCaseCategoryId();
    cr.filename = filenameStart + ".html";
    final String filenameEnd = "-" + tcm.getTestCaseCategoryId() + ".part.html";
    File smmAnalysisDir = ManagerServlet.getSmmAnalysisFolder();
    File fileOut = new File(smmAnalysisDir, cr.filename);
    PrintWriter out = new PrintWriter(new FileWriter(fileOut));
    String[] filePartNames = smmAnalysisDir.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.startsWith("TCAP-") && name.endsWith(filenameEnd);
      }
    });
    Arrays.sort(filePartNames);
    String title = "Analysis of " + tcm.getDescription();
    ClientServlet.printHtmlHeadForFile(out, title);
    out.println("    <h2>" + title + "</h2>");

    out.println("<h3>Message Sent</h3>");
    out.println("<p>Here is an example of one of the messages that was submitted to each IIS. (Every IIS "
        + "received the same message with slightly different data and perhaps additional configurations "
        + "required by the IIS.)</p>");
    out.println("<pre>" + tcm.getMessageTextSent() + "</pre>");

    for (String filePartName : filePartNames) {
      BufferedReader in = new BufferedReader(new FileReader(new File(smmAnalysisDir, filePartName)));
      String line;
      while ((line = in.readLine()) != null) {
        out.println(line);
        if (line.indexOf("<span class=\"pass\">Accepted</span></h3>") > 0) {
          cr.acceptCount++;
        } else if (line.indexOf("<span class=\"fail\">NOT Accepted</span></h3>") > 0) {
          cr.notAcceptCount++;
        }
      }
      in.close();
    }
    ClientServlet.printHtmlFootForFile(out);
    out.close();
    return cr;
  }

  public void printAnalysis(PrintWriter out, boolean toFile) throws IOException {

    if (areaProgress[SUITE_A_BASIC][0] > 0) {
      out.println("<h2>Basic Interoperability Tests</h2>");
      out.println("<div id=\"areaALevel1\"/>");
      out.println("<ul>");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
        ConsolidateResults cr = consolidate(testCaseMessage);
        out.println("  <li>" + testCaseMessage.getDescription() + cr.makeLink() + "</li>");
      }
      out.println("</ul>");
    }

    if (areaProgress[SUITE_B_INTERMEDIATE][0] > 0) {

      out.println("<div id=\"areaBLevel1\"/>");
      out.println("<h2>Intermediate Tests - Level 1 - Accepting Core Data</h2>");
      String previousFieldName = "";
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
        previousFieldName = printTestCaseMessageDetailsUpdateForAnalysis(out, toFile, testCaseMessage,
            previousFieldName);
      }
      if (!previousFieldName.equals("")) {
        out.println("</ul>");
      }
    }

    for (int i = 0; i < areaScore[SUITE_C_ADVANCED].length; i++) {
      if (profileTestCaseLists[i] == null || areaProgress[SUITE_C_ADVANCED][i] < 100) {
        continue;
      }

      int priority = i + 1;
      out.println("<div id=\"areaCLevel" + priority + "\"/>");
      if (i == 0) {
        out.println("    <h2>Advanced Tests - Level " + priority + " - High Priority</h2>");
      } else if (i == 1) {
        out.println("    <h2>Advanced Tests - Level " + priority + " - Medium Priority</h2>");
      } else {
        out.println("    <h2>Advanced Tests - Level " + priority + " - Low Priority </h2>");
      }
      out.println("<ul>");
      for (TestCaseMessage testCaseMessage : profileTestCaseLists[i]) {
        ConsolidateResults cr = consolidate(testCaseMessage);
        out.println("  <li>" + testCaseMessage.getDescription() + cr.makeLink() + "</li>");
      }
      out.println("</ul>");
    }
    if (areaProgress[SUITE_I_PROFILING][0] > 0) {

      out.println("<div id=\"areaILevel1\"/>");
      String segmentName = "";
      for (ProfileLine profileLine : profileLineList) {
        if ((profileLine.getUsage() != Usage.NOT_DEFINED && profileUsage.getCategory() != ProfileCategory.US)
            || (profileLine.getTestCaseMessagePresent() != null && profileLine.getTestCaseMessagePresent().hasIssue()
                && profileLine.getTestCaseMessageAbsent() != null && profileLine.getTestCaseMessageAbsent().hasIssue())) {
          if (!segmentName.equals(profileLine.getField().getSegmentName())) {
            if (!segmentName.equals("")) {
              out.println("</table>");
            }
            out.println("<h2>" + profileLine.getField().getSegmentName() + " Segment</h2>");
            out.println("<table border=\"1\" cellspacing=\"0\">");
            out.println("  <tr>");
            out.println("    <th>Field</th>");
            out.println("    <th>Description</th>");
            out.println("    <th>Usage</th>");
            out.println("    <th>Field Present</th>");
            out.println("    <th>Field Absent</th>");
            out.println("  </tr>");
            segmentName = profileLine.getField().getSegmentName();
          }

          out.println("  <tr>");
          out.println("    <td>" + profileLine.getField().getFieldName() + "</td>");
          out.println("    <td>" + profileLine.getField().getDescription() + "</td>");
          out.println("    <td>" + profileLine.getField().getTestUsage() + "</td>");
          if (profileLine.getTestCaseMessagePresent() == null) {
            out.println("    <td>-</td>");
          } else {
            ConsolidateResults cr = consolidate(profileLine.getTestCaseMessagePresent());
            out.println("    <td>" + cr.makeLink() + "</td>");
          }
          if (profileLine.getTestCaseMessageAbsent() == null) {
            out.println("    <td>-</td>");
          } else {
            ConsolidateResults cr = consolidate(profileLine.getTestCaseMessageAbsent());
            out.println("    <td>" + cr.makeLink() + "</td>");
          }
          out.println("  </tr>");
        }
      }
      out.println("</table>");
      out.println("</br>");
    }

    if (areaProgress[SUITE_D_EXCEPTIONAL][0] > 0) {

      out.println("<div id=\"areaDLevel1\"/>");
      out.println("<h2>Tolerance Tests - Level 1 - Allows minor differences</h2>");
      out.println("<ul>");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
        ConsolidateResults cr = consolidate(testCaseMessage);
        out.println("    <li>" + testCaseMessage.getDescription() + cr.makeLink() + "</li>");
      }
      out.println("</ul>");
    }
  }

  public void printTestCaseMessageDetailsQueryOptional(PrintWriter out, boolean toFile, TestCaseMessage testCaseMessage) {
    String classText = "nottested";
    boolean passedAllOptional = false;
    if (testCaseMessage.isHasRun() && testCaseMessage.getComparisonList() != null) {
      passedAllOptional = true;
      for (Comparison comparison : testCaseMessage.getComparisonList()) {
        if (comparison.isTested() && comparison.getPriorityLevel() == Comparison.PRIORITY_LEVEL_OPTIONAL
            && !comparison.isPass() && !fieldNotSupported(comparison)) {
          passedAllOptional = false;
          break;
        }
      }
    }
    if (testCaseMessage.isHasRun()) {
      classText = testCaseMessage.isPassedTest() && passedAllOptional ? "pass" : "fail";
    }
    out.println("  <tr>");
    out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
    if (testCaseMessage.isHasRun()) {
      if (testCaseMessage.isPassedTest()) {

        if (passedAllOptional) {
          out.println("    <td class=\"" + classText + "\">All required and optional fields returned. "
              + makeCompareDetailsLink(testCaseMessage, toFile, false) + "</td>");
        } else {

          out.println("    <td class=\"" + classText + "\">Required fields were not returned. "
              + makeCompareDetailsLink(testCaseMessage, toFile, false) + "</td>");
        }
      } else {
        out.println("    <td class=\"" + classText + "\">Not all optional and/or required fields were returned. "
            + makeCompareDetailsLink(testCaseMessage, toFile, false) + "</td>");
      }
    } else if (testCaseMessage.getException() != null) {
      out.println("    <td class=\"fail\">");
      out.println("Exception when transmitting message: " + testCaseMessage.getException().getMessage() + ". "
          + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
      out.println("</td>");
    } else {
      out.println("    <td class=\"" + classText + "\">not run yet</td>");
    }
    out.println("  </tr>");
  }

  public void printTestCaseMessageDetailsQueryRequired(PrintWriter out, boolean toFile, TestCaseMessage testCaseMessage) {
    String classText = "nottested";
    if (testCaseMessage.isHasRun()) {
      classText = testCaseMessage.isPassedTest() ? "pass" : "fail";
    }
    out.println("  <tr>");
    out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
    if (testCaseMessage.isHasRun()) {
      if (testCaseMessage.isPassedTest()) {
        out.println("    <td class=\"" + classText + "\">All required fields were returned. "
            + makeCompareDetailsLink(testCaseMessage, toFile, false));
      } else {
        out.println("    <td class=\"" + classText + "\">Not all  required fields were returned. "
            + makeCompareDetailsLink(testCaseMessage, toFile, false));
      }
    } else if (testCaseMessage.getException() != null) {
      out.println("    <td class=\"fail\">");
      out.println("Exception when transmitting message: " + testCaseMessage.getException().getMessage() + ". "
          + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
      out.println("</td>");
    } else {
      out.println("    <td class=\"" + classText + "\">not run yet</td>");
    }
    out.println("  </tr>");
  }

  public void printTestCaseMessageDetailsUpdate(PrintWriter out, boolean toFile, TestCaseMessage testCaseMessage) {
    String classText = "nottested";
    if (testCaseMessage.isHasRun()) {
      classText = testCaseMessage.isAccepted() ? "pass" : "fail";
    }
    out.println("  <tr>");
    out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
    if (testCaseMessage.isHasRun()) {
      if (testCaseMessage.isAccepted()) {
        out.println("    <td class=\"" + classText + "\">Message accepted. "
            + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
      } else {
        out.println("    <td class=\"" + classText + "\">Message was NOT accepted. "
            + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
      }
    } else {
      out.println("    <td class=\"" + classText + "\">not run yet</td>");
    }
    out.println("  </tr>");
  }

  public String printTestCaseMessageDetailsUpdate(PrintWriter out, boolean toFile, TestCaseMessage testCaseMessage,
      String previousFieldName) {
    String classText = "nottested";
    if (testCaseMessage.isHasRun()) {
      classText = testCaseMessage.isAccepted() ? "pass" : "fail";
    }
    String fieldName = testCaseMessage.getDescription();
    String fieldValue = "";
    int isPos = fieldName.indexOf(" is ");
    if (isPos != -1) {
      fieldValue = fieldName.substring(isPos + 4).trim();
      fieldName = fieldName.substring(0, isPos).trim();
    }
    if (!previousFieldName.equals(fieldName)) {
      if (!previousFieldName.equals("")) {
        out.println("</ul>");
      }
      out.println("<p>" + fieldName + ":</p>");
      out.println("<ul>");
    }
    out.println("    <li class=\"" + classText + "\">" + fieldValue);
    if (testCaseMessage.isHasRun()) {
      if (testCaseMessage.isAccepted()) {
        out.println(" - accepted " + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
      } else {
        out.println(" - NOT accepted. " + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
      }
    } else {
      out.println(" - not run yet");
    }
    out.println("  </li>");
    return fieldName;
  }

  public String printTestCaseMessageDetailsUpdateForAnalysis(PrintWriter out, boolean toFile,
      TestCaseMessage testCaseMessage, String previousFieldName) throws IOException {
    ConsolidateResults cr = consolidate(testCaseMessage);
    String fieldName = testCaseMessage.getDescription();
    String fieldValue = "";
    int isPos = fieldName.indexOf(" is ");
    if (isPos != -1) {
      fieldValue = fieldName.substring(isPos + 4).trim();
      fieldName = fieldName.substring(0, isPos).trim();
    }
    if (!previousFieldName.equals(fieldName)) {
      if (!previousFieldName.equals("")) {
        out.println("</ul>");
      }
      out.println("<p>" + fieldName + ":</p>");
      out.println("<ul>");
    }
    out.println("    <li>" + fieldValue + cr.makeLink() + "</li>");
    return fieldName;
  }

  public void printProfileLine(PrintWriter out, boolean toFile, ProfileLine profileLine) {

    String profileLineClassText = "nottested";
    boolean profileLinePassed = profileLine.isPassed();
    if (profileLine.isHasRun()) {
      profileLineClassText = profileLinePassed ? "pass" : "fail";
    }

    out.println("  <tr>");
    out.println("    <td class=\"" + profileLineClassText + "\">" + profileLine.getField().getFieldName() + "</td>");
    out.println("    <td class=\"" + profileLineClassText + "\">" + profileLine.getField().getDescription() + "</td>");
    if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_ABSENT) {
      out.println("    <td class=\"" + profileLineClassText + "\">Absent</td>");
    } else if (profileLine.getMessageAcceptStatus() == MessageAcceptStatus.ONLY_IF_PRESENT) {
      out.println("    <td class=\"" + profileLineClassText + "\">Present</td>");
    } else {
      out.println("    <td class=\"" + profileLineClassText + "\">Present or Absent</td>");
    }
    if (profileLine.getTestCaseMessagePresent() == null || !profileLine.getTestCaseMessagePresent().hasIssue()) {
      out.println("    <td class=\"" + profileLineClassText + "\">Unable to Confirm, Present Test Not Defined</td>");
    } else if (profileLine.getTestCaseMessageAbsent() == null || !profileLine.getTestCaseMessageAbsent().hasIssue()) {
      out.println("    <td class=\"" + profileLineClassText + "\">Unable to Confirm, Absent Test Not Defined</td>");
    } else if (profileLine.isHasRun()) {
      if (profileLinePassed) {
        out.println("    <td class=\"" + profileLineClassText + "\">Confirmed</td>");
      } else {
        out.println("    <td class=\"" + profileLineClassText + "\">Inconsistent</td>");
      }
    } else {
      out.println("    <td class=\"" + profileLineClassText + "\">-</td>");
    }
    printTestMessageCell(out, toFile, profileLine.getTestCaseMessagePresent());
    printTestMessageCell(out, toFile, profileLine.getTestCaseMessageAbsent());
    out.println("  </tr>");
  }

  public void printProfileLineComparisonConformance(PrintWriter out, boolean toFile, ProfileLine profileLine) {
    ProfileUsageValue profileUsageValue = profileUsageComparisonConformance.getProfileUsageValueMap().get(
        profileLine.getField());
    if (profileUsageValue != null) {
      CompatibilityConformance compatibilityConformance = ProfileManager.getCompatibilityConformance(
          profileLine.getUsageDetected(), profileUsageValue.getUsage());
      String usageClass = "";
      switch (compatibilityConformance) {
      case COMPATIBLE:
        usageClass = "pass";
        break;
      case ALLOWANCE:
      case CONFLICT:
      case CONSTRAINT:
      case MAJOR_CONFLICT:
      case MAJOR_CONSTRAINT:
        usageClass = "fail";
        break;
      case NOT_DEFINED:
      case UNABLE_TO_DETERMINE:
        usageClass = "";
        break;
      }
      if (usageClass.equals("fail")) {
        out.println("  <tr>");
        out.println("    <td class=\"pass\">" + profileLine.getField().getFieldName() + "</td>");
        out.println("    <td class=\"pass\">" + profileLine.getField().getDescription() + "</td>");
        out.println("    <td class=\"" + usageClass + "\">" + profileUsageValue.getUsage() + "</td>");
        out.println("    <td class=\"" + usageClass + "\">" + profileLine.getUsageDetected() + "</td>");
        out.println("    <td class=\"" + usageClass + "\">" + compatibilityConformance + "</td>");
        out.println("  </tr>");
      }
    }
  }

  public void printProfileLineComparisonInteroperability(PrintWriter out, boolean toFile, ProfileLine profileLine) {
    ProfileUsageValue profileUsageValue = profileUsageComparisonInteroperability.getProfileUsageValueMap().get(
        profileLine.getField());
    if (profileUsageValue != null) {
      CompatibilityInteroperability compatibilityInteroperability = ProfileManager.getCompatibilityInteroperability(
          profileLine.getProfileUsageValue(), profileUsageValue);
      String usageClass = "";
      switch (compatibilityInteroperability) {
      case COMPATIBLE:
      case DATA_LOSS:
      case IF_CONFIGURED:
      case IF_POPULATED:
      case NO_PROBLEM:
        usageClass = "pass";
        break;
      case MAJOR_PROBLEM:
      case PROBLEM:
        usageClass = "fail";
        break;
      case UNABLE_TO_DETERMINE:
        usageClass = "";
        break;
      }
      out.println("  <tr>");
      out.println("    <td class=\"pass\">" + profileLine.getField().getFieldName() + "</td>");
      out.println("    <td class=\"pass\">" + profileLine.getField().getDescription() + "</td>");
      out.println("    <td class=\"" + usageClass + "\">" + compatibilityInteroperability + "</td>");
      out.println("    <td class=\"" + usageClass + "\">" + profileUsageValue.getUsage() + "</td>");
      out.println("  </tr>");
    }
  }

  // profileUsageComparisonInteroperability

  public void printTestMessageCell(PrintWriter out, boolean toFile, TestCaseMessage testCaseMessage) {
    String classText = "nottested";
    boolean testPassed = false;
    if (testCaseMessage == null || !testCaseMessage.hasIssue()) {
      out.println("    <td class=\"" + classText + "\">Not Defined</td>");
    } else {
      if (!testCaseMessage.isHasRun()) {
        out.println("    <td class=\"" + classText + "\">Not Run</td>");
      } else {
        testPassed = testCaseMessage.getActualResultStatus().equals(TestRunner.ACTUAL_RESULT_STATUS_PASS);
        classText = testPassed ? "pass" : "fail";
        if (testPassed) {
          if (testCaseMessage == tcmFull) {
            out.println("    <td class=\"" + classText + "\">Accepted Base Message</td>");
          } else {
            if (testCaseMessage.isAccepted()) {
              out.println("    <td class=\"" + classText + "\">Accepted as was Expected "
                  + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
            } else {
              out.println("    <td class=\"" + classText + "\">Not Accepted as was Expected "
                  + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
            }
          }
        } else {
          if (testCaseMessage == tcmFull) {
            out.println("    <td class=\"" + classText + "\">Not Accepted Base Message</td>");
          } else {
            if (testCaseMessage.isAccepted()) {
              out.println("    <td class=\"" + classText + "\">Accepted (Expecting NOT Accepted) "
                  + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
            } else {
              out.println("    <td class=\"" + classText + "\">Not Accepted  (Expecting Accepted) "
                  + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
            }
          }
        }

      }
    }
  }

  private int makeScore(int num, int denom) {
    return (int) (100.0 * num / denom);
  }

  public String getStatus() {
    return status;
  }

  public void stopRunning() {
    keepRunning = false;
  }

  private String paddWithZeros(int i, int zeros) {
    String s = "000000" + i;
    return s.substring(s.length() - zeros);
  }

  private String makeTestCaseMessageDetailsLink(TestCaseMessage testCaseMessage, boolean toFile) {
    if (toFile) {
      return "<font size=\"-1\"><a href=\"TC-" + testCaseMessage.getTestCaseNumber() + ".html\">details</a></font>";
    } else {
      return "<font size=\"-1\"><a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
          + testCaseMessage.getTestCaseId() + "\">details</a></font>";
    }
  }

  private String makeCompareDetailsLink(TestCaseMessage testCaseMessage, boolean toFile, boolean substantialChanges) {
    if (toFile) {
      if (substantialChanges) {
        return "<font size=\"-1\"><a href=\"TC-" + testCaseMessage.getTestCaseNumber()
            + ".html#changesMade\">(details)</a></font>";
      } else {
        return "<font size=\"-1\"><a href=\"TC-" + testCaseMessage.getTestCaseNumber()
            + ".html#compareDetails\">(details)</a></font>";
      }
    } else {
      if (substantialChanges) {
        return "<font size=\"-1\"><a href=\"CompareServlet?certifyServletBasicNum=" + testCaseMessage.getTestCaseId()
            + "&showSubmissionChange=true\">(compare details)</a></font>";
      } else {
        return "<font size=\"-1\"><a href=\"CompareServlet?certifyServletBasicNum=" + testCaseMessage.getTestCaseId()
            + "\">(compare details)</a></font>";

      }
    }
  }

}