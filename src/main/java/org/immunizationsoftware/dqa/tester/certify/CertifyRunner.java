package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.ClientServlet;
import org.immunizationsoftware.dqa.tester.CreateTestCaseServlet;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.RunAgainstConnector;
import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.manager.QueryConverter;
import org.immunizationsoftware.dqa.tester.manager.forecast.EvaluationActual;
import org.immunizationsoftware.dqa.tester.manager.forecast.ForecastActual;
import org.immunizationsoftware.dqa.tester.manager.forecast.ForecastTesterManager;
import org.immunizationsoftware.dqa.tester.manager.nist.Assertion;
import org.immunizationsoftware.dqa.tester.profile.CompatibilityConformance;
import org.immunizationsoftware.dqa.tester.profile.CompatibilityInteroperability;
import org.immunizationsoftware.dqa.tester.profile.MessageAcceptStatus;
import org.immunizationsoftware.dqa.tester.profile.ProfileLine;
import org.immunizationsoftware.dqa.tester.profile.ProfileManager;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsage;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsageValue;
import org.immunizationsoftware.dqa.tester.run.TestRunner;
import org.immunizationsoftware.dqa.transform.Comparison;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;
import org.openimmunizationsoftware.dqa.tr.RecordServletInterface;

public class CertifyRunner extends Thread implements RecordServletInterface
{

  private static final String REPORT_URL = "http://localhost:8289/record";
  // "http://localhost:8289/record";
  // "http://ois-pt.org/dqacm/record";

  private static final boolean SAVE_TEST_CASES_TO_DIR = false;

  private static final String REPORT_EXPLANATION_URL = "http://ois-pt.org/tester/reportExplanation.html";

  public static final String QUERY_TYPE_QBP_Z34 = "QBP-Z34";
  public static final String QUERY_TYPE_QBP_Z34_Z44 = "QBP-Z34-Z44";
  public static final String QUERY_TYPE_QBP_Z44 = "QBP-Z44";
  public static final String QUERY_TYPE_VXQ = "VXQ";
  public static final String QUERY_TYPE_NONE = "None";

  public static final String STATUS_INITIALIZED = "Initialized";
  public static final String STATUS_STARTED = "Started";
  public static final String STATUS_COMPLETED = "Completed";
  public static final String STATUS_STOPPED = "Stopped";
  public static final String STATUS_PAUSED = "Paused";

  protected String status = "";
  protected List<String> statusMessageList = null;
  protected Throwable exception = null;
  protected boolean keepRunning = true;
  protected CAPerformance performance = null;

  protected void logStatus(String status) {
    synchronized (statusMessageList) {
      statusMessageList.add(sdf.format(new Date()) + " " + certifyAreas[currentSuite].getAreaLabel() + ": " + status);
    }
  }

  public static final int SUITE_A_BASIC = 0;
  public static final int SUITE_K_NOT_ACCEPTED = 1;
  public static final int SUITE_J_ONC_2015 = 2;
  public static final int SUITE_B_INTERMEDIATE = 3;
  public static final int SUITE_C_ADVANCED = 4;
  public static final int SUITE_D_EXCEPTIONAL = 5;
  public static final int SUITE_I_PROFILING = 6;
  public static final int SUITE_M_QBP_SUPPORT = 7;
  public static final int SUITE_N_TRANSFORM = 8;
  public static final int SUITE_O_EXTRA = 9;
  public static final int SUITE_P_DEDUPLICATION_ENGAGED = 10;
  public static final int SUITE_Q_FORECASTER_ENGAGED = 11;
  public static final int SUITE_E_FORECAST_PREP = 12;
  public static final int SUITE_F_FORECAST = 13;
  public static final int SUITE_G_PERFORMANCE = 14;
  public static final int SUITE_H_CONFORMANCE = 15;
  public static final int SUITE_L_CONFORMANCE_2015 = 16;
  public static final int SUITE_COUNT = 17;

  protected int currentSuite = SUITE_A_BASIC;
  protected IncrementingInt incrementingInt = null;

  private static int REPORT_1_INTEROP = 0;
  private static int REPORT_2_CODED = 1;
  private static int REPORT_3_LOCAL = 2;
  private static int REPORT_4_NATIONAL = 3;
  private static int REPORT_5_TOLERANCE = 4;
  private static int REPORT_6_EHR = 5;
  private static int REPORT_7_PERFORM = 6;
  private static int REPORT_8_ACK = 7;

  private double[] reportScore = new double[8];
  protected ForecastTesterManager forecastTesterManager = null;

  protected CertifyArea[] certifyAreas = new CertifyArea[SUITE_COUNT];

  protected CATotal caTotal = new CATotal(this);

  {
    certifyAreas[SUITE_A_BASIC] = new CABasic(this);
    certifyAreas[SUITE_B_INTERMEDIATE] = new CAIntermediate(this);
    certifyAreas[SUITE_C_ADVANCED] = new CAAdvanced(this);
    certifyAreas[SUITE_D_EXCEPTIONAL] = new CAExceptional(this);
    certifyAreas[SUITE_E_FORECAST_PREP] = new CAForecastPrep(this);
    certifyAreas[SUITE_F_FORECAST] = new CAForecast(this);
    certifyAreas[SUITE_G_PERFORMANCE] = new CAPerformance(this);
    certifyAreas[SUITE_H_CONFORMANCE] = new CAConformance(this);
    certifyAreas[SUITE_I_PROFILING] = new CAProfiling(this);
    certifyAreas[SUITE_J_ONC_2015] = new CAOnc2015(this);
    certifyAreas[SUITE_K_NOT_ACCEPTED] = new CANotAccepted(this);
    certifyAreas[SUITE_L_CONFORMANCE_2015] = new CAConformance2015(this);
    certifyAreas[SUITE_M_QBP_SUPPORT] = new CAQbpSupport(this);
    certifyAreas[SUITE_N_TRANSFORM] = new CATransform(this);
    certifyAreas[SUITE_O_EXTRA] = new CAExtra(this);
    certifyAreas[SUITE_P_DEDUPLICATION_ENGAGED] = new CADeduplicationEngaged(this);
    certifyAreas[SUITE_Q_FORECASTER_ENGAGED] = new CAForecasterEngaged(this);
    performance = (CAPerformance) certifyAreas[SUITE_G_PERFORMANCE];
  }

  private Map<String, PrintWriter> exampleOutSet = new HashMap<String, PrintWriter>();
  private Map<String, PrintWriter> exampleAckOutSet = new HashMap<String, PrintWriter>();

  double overallScore = 0.0;

  private Map<CompatibilityConformance, List<ProfileLine>> updateOverallScore() {

    synchronized (reportScore) {
      overallScore = 0.0;

      reportScore[REPORT_1_INTEROP] = 0.0;
      reportScore[REPORT_2_CODED] = 0.0;
      reportScore[REPORT_3_LOCAL] = 0.0;
      reportScore[REPORT_4_NATIONAL] = 0.0;
      reportScore[REPORT_5_TOLERANCE] = 0.0;
      reportScore[REPORT_6_EHR] = 0.0;
      reportScore[REPORT_7_PERFORM] = 0.0;
      reportScore[REPORT_8_ACK] = 0.0;
      Map<CompatibilityConformance, List<ProfileLine>> compatibilityMap = new HashMap<CompatibilityConformance, List<ProfileLine>>();

      if (certifyAreas[SUITE_A_BASIC].getAreaProgress()[0] > 0) {
        reportScore[REPORT_1_INTEROP] = certifyAreas[SUITE_A_BASIC].getAreaScore()[0] / 100.0;
      }

      if (certifyAreas[SUITE_B_INTERMEDIATE].getAreaProgress()[0] > 0) {
        double score = Math.log(certifyAreas[SUITE_B_INTERMEDIATE].getAreaScore()[0] + 1) / Math.log(101);
        reportScore[REPORT_2_CODED] = score;
      }

      if (certifyAreas[SUITE_I_PROFILING].getAreaProgress()[0] > 0
          && ((CAProfiling) certifyAreas[SUITE_I_PROFILING]).getProfileLineList() != null) {
        int countRun = 0;
        {
          int countPass = 0;
          for (ProfileLine profileLine : ((CAProfiling) certifyAreas[SUITE_I_PROFILING]).getProfileLineList()) {
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
      if (((CAProfiling) certifyAreas[SUITE_I_PROFILING]).getProfileLineList() != null) {
        for (ProfileLine profileLine : ((CAProfiling) certifyAreas[SUITE_I_PROFILING]).getProfileLineList()) {
          if (profileUsageComparisonConformance != null
              && profileUsageComparisonConformance.getProfileUsageValueMap() != null) {
            ProfileUsageValue profileUsageValueConformance = profileUsageComparisonConformance.getProfileUsageValueMap()
                .get(profileLine.getField());
            if (profileUsageValueConformance != null) {
              CompatibilityConformance compatibility = ProfileManager
                  .getCompatibilityConformance(profileLine.getUsageDetected(), profileUsageValueConformance.getUsage());
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

      if (certifyAreas[SUITE_D_EXCEPTIONAL].getAreaProgress()[0] > 0) {
        int countTolerance = 0;
        int countEhr = 0;
        int countTolerancePass = 0;
        int countEhrPass = 0;

        for (TestCaseMessage testCaseMessage : certifyAreas[SUITE_D_EXCEPTIONAL].getUpdateList()) {
          if (testCaseMessage.getDescription().startsWith(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK)) {
            countTolerance++;
            if (testCaseMessage.isHasRun() && testCaseMessage.isPassedTest()) {
              countTolerancePass++;
            }
          } else if (testCaseMessage.getDescription().startsWith(VALUE_EXCEPTIONAL_PREFIX_CERTIFIED_MESSAGE)) {
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

      if (certifyAreas[SUITE_G_PERFORMANCE].getAreaProgress()[0] > 0) {
        int averageMs = certifyAreas[SUITE_G_PERFORMANCE].getAreaScore()[0];
        if (averageMs < 3000) {
          reportScore[REPORT_7_PERFORM] = 1.0;
        } else if (averageMs > 6000) {
          reportScore[REPORT_7_PERFORM] = 0.0;
        } else {
          reportScore[REPORT_7_PERFORM] = 1.0 - ((averageMs - 3000) / 3000.0);
        }
      }

      if (certifyAreas[SUITE_H_CONFORMANCE].getAreaProgress()[0] > 0) {
        reportScore[REPORT_8_ACK] = certifyAreas[SUITE_H_CONFORMANCE].getAreaScore()[0] / 100.0;
      }
      for (int i = 0; i < reportScore.length; i++) {
        if (reportScore[i] < 0) {
          reportScore[i] = 0;
        }
      }

      overallScore += reportScore[REPORT_1_INTEROP] * 0.5;
      overallScore += reportScore[REPORT_2_CODED] * 0.1;
      // overallScore += reportScore[REPORT_3_LOCAL] * 0.1;
      // overallScore += reportScore[REPORT_4_NATIONAL] * 0.1;
      overallScore += reportScore[REPORT_5_TOLERANCE] * 0.05;
      overallScore += reportScore[REPORT_6_EHR] * 0.05;
      overallScore += reportScore[REPORT_7_PERFORM] * 0.05;
      overallScore += reportScore[REPORT_8_ACK] * 0.25;

      return compatibilityMap;
    }
  }

  public boolean isRun(int suite) {
    return certifyAreas[suite].isRun();
  }

  public void setRun(boolean r, int suite) {
    certifyAreas[suite].setRun(r);
  }

  protected String testCaseSet = "";

  private Date testStarted = null;
  private Date testFinished = null;

  private String queryType = "";
  private boolean willQuery = false;
  private boolean pauseBeforeQuerying = false;
  protected String uniqueMRNBase = "";

  public boolean isPauseBeforeQuerying() {
    return pauseBeforeQuerying;
  }

  public void setPauseBeforeQuerying(boolean pauseBeforeQuerying) {
    this.pauseBeforeQuerying = pauseBeforeQuerying;
  }

  protected List<TestCaseMessage> statusCheckTestCaseList = new ArrayList<TestCaseMessage>();

  protected void register(TestCaseMessage tcm) {
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

  protected Connector connector;

  public Connector getConnector() {
    return connector;
  }

  Connector queryConnector;

  public CertifyRunner(Connector connector, SendData sendData, String queryType) {
    this.connector = connector;
    this.queryConnector = connector.getOtherConnectorMap().get(Connector.PURPOSE_QUERY);
    if (this.queryConnector == null) {
      queryConnector = connector;
    }
    this.queryType = queryType;

    this.sendData = sendData;

    status = STATUS_INITIALIZED;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    testCaseSet = CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX + " " + sdf.format(new Date());

    sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    statusMessageList = new ArrayList<String>();

    willQuery = queryType != null && (queryType.equals(QUERY_TYPE_QBP_Z34) || queryType.equals(QUERY_TYPE_QBP_Z44)
        || queryType.equals(QUERY_TYPE_VXQ));
    if (willQuery) {
      logStatus("Query will be run: " + queryType);
    } else {
      logStatus("Query was not enabled");
    }

    logStatus("IIS Tester Initialized");
  }

  private static int uniqueMRNBaseInc = 0;

  private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

  protected Transformer transformer;
  protected SendData sendData;
  protected File testDir;
  protected ParticipantResponse participantResponse = null;
  protected TestCaseMessage testCaseMessageBase = null;

  public ParticipantResponse getParticipantResponse() {
    return participantResponse;
  }

  public void setParticipantResponse(ParticipantResponse participantResponse) {
    this.participantResponse = participantResponse;
  }

  @Override
  public void run() {
    status = STATUS_STARTED;
    logStatus("Starting to run report");
    incrementingInt = new IncrementingInt();
    try {

      File testDataFile = CreateTestCaseServlet.getTestDataFile(sendData);
      if (testDataFile == null) {
        transformer = new Transformer();
      } else {
        transformer = new Transformer(testDataFile);
      }

      uniqueMRNBase = Transformer.makeBase62Number(System.currentTimeMillis() % 1000000 + uniqueMRNBaseInc++) + "-";

      testStarted = new Date();

      boolean goodToGo = true;

      logStatus("Reporting progress");
      if (participantResponse != null) {
        reportParticipant(participantResponse);
      }
      reportProgress(null, true, null);

      if (runAgainstFolder != null) {
        connector = new RunAgainstConnector(connector, runAgainstFolder);
        logStatus("Running test against previously received responses in this forder: " + runAgainstFolder);
        if (queryConnector != null) {
          queryConnector = new RunAgainstConnector(queryConnector, runAgainstFolder);
        }
      } else {
        logStatus("Connecting directly to real-time interface");

        logStatus("Verify connection works by sending a message that is expected to pass");
        testCaseMessageBase = createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessageBase.setTestCaseSet(testCaseSet);
        testCaseMessageBase.setTestCaseCategoryId("BASE");
        testCaseMessageBase.setTestCaseNumber(uniqueMRNBase + testCaseMessageBase.getTestCaseCategoryId());
        transformer.transform(testCaseMessageBase);
        testCaseMessageBase.setAssertResult("Accept - *");
        testCaseMessageBase.setTestType(VALUE_TEST_TYPE_PREP);
        testCaseMessageBase.setTestPosition(incrementingInt.next());
        TestRunner testRunner = new TestRunner();
        testRunner.setValidateResponse(certifyAreas[SUITE_L_CONFORMANCE_2015].isRun());
        try {
          testRunner.runTest(connector, testCaseMessageBase);
          boolean pass = testCaseMessageBase.isAccepted();
          if (pass) {
            logStatus("Base message was accepted, test should be good to run");
            goodToGo = true;
          } else {
            logStatus("Base message was NOT accepted, tests may not be able to run properly");
            goodToGo = false;
          }
        } catch (Throwable t) {
          logStatus("Exception running base test case message: " + t.getMessage());
          t.printStackTrace();
          goodToGo = false;
        }
        reportProgress(testCaseMessageBase);
      }
      if (!goodToGo) {
        logStatus("Test is being cancelled, basic message was not accepted");
        return;
      }

      if (sendData == null) {
        testDir = null;
      } else {
        if (SAVE_TEST_CASES_TO_DIR) {
          testDir = new File(sendData.getRootDir(), testCaseSet);
          if (!testDir.exists()) {
            testDir.mkdir();
          }
        }
      }

      caTotal.getAreaCount()[0] = 0;
      for (int i = 0; i < certifyAreas.length; i++) {
        if (certifyAreas[i].isRun() && !certifyAreas[i].isPerformanceConformance()) {
          currentSuite = i;
          logStatus("Preparing Updates");
          certifyAreas[i].prepareUpdates();
          certifyAreas[i].areaCount[0] = certifyAreas[i].updateList.size();
          logStatus("Reporting Progress");
          reportProgress(null);
          if (certifyAreas[i].areaCount[0] > 0) {
            caTotal.getAreaCount()[0] += certifyAreas[i].areaCount[0];
          }
        }
      }
      caTotal.startingUpdates();
      for (int i = 0; i < certifyAreas.length; i++) {
        if (certifyAreas[i].isRun() && !certifyAreas[i].isPerformanceConformance()) {
          currentSuite = i;
          logStatus("Sending Updates");
          certifyAreas[i].sendUpdates();
          logStatus("Reporting Progress");
          reportProgress(null);
        }
      }
      caTotal.getAreaScore()[0] = 100;
      for (int i = 0; i < certifyAreas.length; i++) {
        if (certifyAreas[i].isRun() && certifyAreas[i].isPerformanceConformance()) {
          currentSuite = i;
          logStatus("Running Conformance/Performance");
          certifyAreas[i].sendUpdates();
          logStatus("Reporting Progress");
          reportProgress(null);
        }
      }

      if (willQuery) {
        if (pauseBeforeQuerying) {
          logStatus("Paused, waiting to start query process");
          status = STATUS_PAUSED;
          reportProgress(null);
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
          reportProgress(null);
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }
        for (int i = 0; i < certifyAreas.length; i++) {
          if (certifyAreas[i].isRun() && !certifyAreas[i].isPerformanceConformance()) {
            currentSuite = i;
            logStatus("Preparing Query");
            certifyAreas[i].prepareQueries();
            certifyAreas[i].areaCount[1] = certifyAreas[i].queryList.size();
            logStatus("Reporting Progress");
            reportProgress(null);
            caTotal.getAreaCount()[1] += certifyAreas[i].areaCount[1];
          }
        }
        caTotal.startingQueries();
        for (int i = 0; i < certifyAreas.length; i++) {
          if (certifyAreas[i].isRun() && !certifyAreas[i].isPerformanceConformance()) {
            currentSuite = i;
            logStatus("Sending Queries");
            certifyAreas[i].sendQueries();
            logStatus("Reporting Progress");
            reportProgress(null);
          }
        }
        caTotal.getAreaScore()[1] = 100;
        for (int i = 0; i < certifyAreas.length; i++) {
          if (certifyAreas[i].isRun() && certifyAreas[i].isPerformanceConformance()) {
            currentSuite = i;
            logStatus("Running Conformance/Performance");
            certifyAreas[i].sendQueries();
            logStatus("Reporting Progress");
            reportProgress(null);
          }
        }
      }

      testFinished = new Date();
      reportProgress(null);

      printReportToFile();
    }

    catch (Throwable t) {
      t.printStackTrace();
      exception = t;
      status = STATUS_STOPPED;
      reportProgress(null);
      logStatus("Exception ocurred: " + exception.getMessage());
    } finally {
      if (status != STATUS_STOPPED) {
        status = STATUS_COMPLETED;
        reportProgress(null);
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

  public PrintWriter setupExampleFile(String name, TestCaseMessage testCaseMessage) {
    if (testCaseMessage != null) {
      if (SAVE_TEST_CASES_TO_DIR) {
        if (testDir != null) {
          logStatus("Saving example");
          File exampleFile;
          if (testCaseMessage.getForecastTestPanel() != null) {
            exampleFile = new File(testDir,
                "Example Messages " + name + "" + testCaseMessage.getForecastTestPanel().getLabel() + ".hl7");
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
    }
    return null;
  }

  public PrintWriter setupAckFile(String name, TestCaseMessage testCaseMessage) {
    if (testCaseMessage != null && sendData != null) {
      if (testDir != null) {
        logStatus("Saving example");
        File exampleFile;
        if (testCaseMessage.getForecastTestPanel() != null) {
          exampleFile = new File(testDir,
              "Example Messages " + name + "" + testCaseMessage.getForecastTestPanel().getLabel() + ".ack.hl7");
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
        out.println("<h1>Detailed Run Log for " + connector.getLabel() + "</h1>");
        out.println(
            "<p>This detail run log gives complete details on what was tested. For a summary and condensed view see the IIS Testing Results. </p>");
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

    }
  }

  public void printExampleMessage(TestCaseMessage testCaseMessage, String type) {
    if (SAVE_TEST_CASES_TO_DIR) {
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
  }

  protected ProfileUsage profileUsage = null;
  protected ProfileUsage profileUsageComparisonInteroperability = null;
  protected ProfileUsage profileUsageComparisonConformance = null;

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
    ((CAProfiling) certifyAreas[SUITE_I_PROFILING]).setProfileManager(profileManager);
  }

  public void addForecastTestPanel(ForecastTestPanel forecastTestPanel) {
    ((CAForecast) certifyAreas[SUITE_F_FORECAST]).forecastTestPanelList.add(forecastTestPanel);
  }

  public void setDerivedFrom(TestCaseMessage testCaseMessage, TestCaseMessage queryTestCaseMessage) {
    queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
    queryTestCaseMessage.setOriginalMessageResponse(testCaseMessage.getActualResponseMessage());
    queryTestCaseMessage.setOriginalAccepted(testCaseMessage.isAccepted());
  }

  public void setQueryReturnedMostImportantData(TestCaseMessage queryTestCaseMessage) {
    if (CompareManager.queryReturnedMostImportantData(queryTestCaseMessage.getDerivedFromVXUMessage(),
        queryTestCaseMessage.getActualResponseMessage())) {
      if (queryTestCaseMessage.isOriginalAccepted()) {
        queryTestCaseMessage
            .setResultStoreStatus(RecordServletInterface.VALUE_RESULT_ACK_STORE_STATUS_ACCEPTED_RETURNED);
      } else {
        queryTestCaseMessage
            .setResultStoreStatus(RecordServletInterface.VALUE_RESULT_ACK_STORE_STATUS_NOT_ACCEPTED_RETURNED);
      }
    } else {
      if (queryTestCaseMessage.isOriginalAccepted()) {
        queryTestCaseMessage
            .setResultStoreStatus(RecordServletInterface.VALUE_RESULT_ACK_STORE_STATUS_ACCEPTED_NOT_RETURNED);
      } else {
        queryTestCaseMessage
            .setResultStoreStatus(RecordServletInterface.VALUE_RESULT_ACK_STORE_STATUS_NOT_ACCEPTED_NOT_RETURNED);
      }
    }
  }

  public String doSafeQuery(String message) throws Exception {
    String response = queryConnector.submitMessage(message, false);
    HL7Reader responseReader = new HL7Reader(response);
    if (responseReader.advanceToSegment("MSH")) {
      boolean okay = false;
      String messageType = responseReader.getValue(9);
      if (messageType.equals("VXR")) {
        okay = true;
      } else if (messageType.equals("RSP")) {
        String profile = responseReader.getValue(21);
        if (profile.equalsIgnoreCase("Z32") || profile.equalsIgnoreCase("Z34")) {
          okay = true;
        }
      }
      if (!okay) {
        response = responseReader.getOriginalSegment();
        while (responseReader.advance()) {
          if (responseReader.getSegmentName().equals("PID") || responseReader.getSegmentName().equals("NK1")) {
            response += responseReader.getSegmentName() + "|[Redacted by IIS Tester]\r";
          } else {
            response += responseReader.getOriginalSegment() + "\r";
          }
        }
        response += "\r[IIS Tester has redacted patient identifying information because a single match was not found. PID and NK1 segments were truncated by the IIS Tester. ]";
      }
    } else {
      response = "[IIS Tester: Unrecognized response message, entire result redacted]";
    }
    return response;
  }

  public String convertToQuery(TestCaseMessage testCaseMessage) {
    if (queryType.equals(QUERY_TYPE_QBP_Z34)) {
      return QueryConverter.convertVXUtoQBPZ34(testCaseMessage.getMessageText());
    }
    if (queryType.equals(QUERY_TYPE_QBP_Z34_Z44)) {
      return QueryConverter.convertVXUtoQBPZ34Z44(testCaseMessage.getMessageText());
    }
    if (queryType.equals(QUERY_TYPE_QBP_Z44)) {
      return QueryConverter.convertVXUtoQBPZ44(testCaseMessage.getMessageText());
    }
    if (queryType.equals(QUERY_TYPE_VXQ)) {
      return QueryConverter.convertVXUtoVXQ(testCaseMessage.getMessageText());
    }
    throw new IllegalArgumentException(
        "Unable to convert query because query type '" + queryType + "' is not recognized");
  }

  protected void saveTestCase(TestCaseMessage tcm) {
    if (SAVE_TEST_CASES_TO_DIR) {
      if (testDir != null) {
        CreateTestCaseServlet.saveTestCaseHtml(tcm, testDir);
      }
    }
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
    out.println("    <th rowspan=\"2\">Test Areas</th>");
    out.println("    <th colspan=\"3\">Update</th>");
    if (willQuery) {
      out.println("    <th colspan=\"3\">Query " + queryType + "</th>");
    }
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Complete</th>");
    out.println("    <th>Progress</th>");
    out.println("    <th>Pass</th>");
    if (willQuery) {
      out.println("    <th>Complete</th>");
      out.println("    <th>Progress</th>");
      out.println("    <th>Pass</th>");
    }
    out.println("  </tr>");
    for (CertifyArea certifyArea : certifyAreas) {
      if (!certifyArea.isPerformanceConformance()) {
        printRow(out, certifyArea);
      }
    }
    printRow(out, caTotal);

    out.println("</table>");
    out.println("<br/>");
    out.println("<table border=\"1\" cellspacing=\"0\" width=\"720\">");
    out.println("  <tr>");
    out.println("    <th rowspan=\"2\">Analysis Areas</th>");
    out.println("    <th colspan=\"3\">Update</th>");
    if (willQuery) {
      out.println("    <th colspan=\"3\">Query " + queryType + "</th>");
    }
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Complete</th>");
    out.println("    <th>Progress</th>");
    out.println("    <th>Pass</th>");
    if (willQuery) {
      out.println("    <th>Complete</th>");
      out.println("    <th>Progress</th>");
      out.println("    <th>Pass</th>");
    }
    out.println("  </tr>");
    for (CertifyArea certifyArea : certifyAreas) {
      if (certifyArea.isPerformanceConformance()) {
        printRow(out, certifyArea);
      }
    }
    out.println("</table>");
    out.println("<br/>");

  }

  public void printRow(PrintWriter out, CertifyArea certifyArea) {
    if (certifyArea.isRun()) {
      {
        String classStyle = "";
        if (certifyArea.getAreaProgress()[0] >= 100) {
          if (certifyArea.getAreaScore()[0] >= 70) {
            classStyle = "pass";
          } else {
            classStyle = "fail";
          }
        } else if (certifyArea.getAreaProgress()[0] > 0 && !(certifyArea instanceof CATotal)) {
          classStyle = "running";
        }
        out.println("  <tr>");
        out.println("    <td><font size=\"+1\">" + certifyArea.getAreaLabel() + "</font></td>");
        if (certifyArea.getAreaCount()[0] <= 0) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">-</td>");
        } else {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">"
              + certifyArea.getAreaProgressCount()[0] + "/" + certifyArea.getAreaCount()[0] + "</td>");
        }
        if (certifyArea.getAreaProgress()[0] <= 0) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">-</td>");
        } else {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">"
              + certifyArea.getAreaProgress()[0] + "%</td>");
        }
        if (certifyArea.getAreaScore()[0] >= 100) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">Yes</td>");
        } else if (certifyArea.getAreaScore()[0] == 0) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">No</td>");
        } else if (certifyArea.getAreaScore()[0] >= 0) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">"
              + certifyArea.getAreaScore()[0] + "% Yes</td>");
        } else {
          if (certifyArea instanceof CATotal) {
            Date updateEtc = caTotal.estimatedUpdateCompletion();
            SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
            if (updateEtc != null) {
              out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">ETC "
                  + timeFormat.format(updateEtc) + "</td>");
            } else {
              out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">-</td>");
            }
          } else {
            out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">-</td>");
          }
        }
      }
      if (willQuery) {
        String classStyle = "";
        if (certifyArea.getAreaProgress()[1] >= 100) {
          if (certifyArea.getAreaScore()[1] >= 70) {
            classStyle = "pass";
          } else {
            classStyle = "fail";
          }
        } else if (certifyArea.getAreaProgress()[1] > 0 && !(certifyArea instanceof CATotal)) {
          classStyle = "running";
        }
        if (certifyArea.getAreaCount()[1] <= 0) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">-</td>");
        } else {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">"
              + certifyArea.getAreaProgressCount()[1] + "/" + certifyArea.getAreaCount()[1] + "</td>");
        }
        if (certifyArea.getAreaProgress()[1] <= 0) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">-</td>");
        } else {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">"
              + certifyArea.getAreaProgress()[1] + "%</td>");
        }
        if (certifyArea.getAreaScore()[1] >= 100) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">Yes</td>");
        } else if (certifyArea.getAreaScore()[1] == 0) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">No</td>");
        } else if (certifyArea.getAreaScore()[1] >= 0) {
          out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">"
              + certifyArea.getAreaScore()[1] + "% Yes</td>");
        } else {
          if (certifyArea instanceof CATotal) {
            Date queryEtc = caTotal.estimatedQueryCompletion();
            SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
            if (queryEtc != null) {
              out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">ETC "
                  + timeFormat.format(queryEtc) + "</td>");
            } else {
              out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">-</td>");
            }
          } else {
            out.println("    <td class=\"" + classStyle + "\" style=\"text-align: center;\">-</td>");
          }
        }
      }
      out.println("  </tr>");
    }
  }

  public void printReport(PrintWriter out, boolean toFile) {
    if (testFinished == null) {
      out.println(
          "<p>Test in progress, unable to provide final report. <a href=\"IIS Testing Report Detail.html\">See supporting details for current progress. </a></p>");
    } else {
      Map<CompatibilityConformance, List<ProfileLine>> compatibilityMap = updateOverallScore();

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
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th>Core Section (Beta)</th>");
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
      out.println("<br/>");

      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th>Assumption Testing (Alpha)</th>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#localRequirements\">Local Requirement Implementation</a></td>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><a href=\"#nationalCompatibility\">National Compatibility</a></td>");
      out.println("  </tr>");
      out.println("</table>");
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL + "#overallScore\" class=\"boxLinks\">Description</a></p>");

      out.println("<h3>Setup</h3>");
      out.println("<p>Here are the connection details that were used to connect to IIS to create report. </p>");

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
        out.println("<h3>Custom Modications</h3>");
        if (!connector.getCustomTransformations().equals("")) {
          out.println(
              "<p>This interface requires customized Transformations to modify each message before transmitting "
                  + "them to the IIS. These transformations can range from setting the correct submitter facility in the "
                  + "message header to modifying the structure of the HL7 message to meet local requirements. </p>");
          {
            try {
              BufferedReader customTransformsIn = new BufferedReader(
                  new StringReader(connector.getCustomTransformations()));
              StringBuilder expectedTransforms = new StringBuilder();
              StringBuilder unexpectedTransforms = new StringBuilder();
              String line = "";
              String[] expectedStarts = { "MSH-3=", "MSH-4=", "MSH-5=", "MSH-6=", "MSH-22=", "RXA-11.4=",
                  "RXA-11.4*=" };
              while ((line = customTransformsIn.readLine()) != null) {
                if (line.length() > 0) {
                  boolean foundExpectedStart = false;
                  for (String expectedStart : expectedStarts) {
                    if (line.startsWith(expectedStart)) {
                      foundExpectedStart = true;
                      break;
                    }
                  }
                  if (foundExpectedStart) {
                    expectedTransforms.append(line);
                    expectedTransforms.append("\n");
                  } else {
                    unexpectedTransforms.append(line);
                    unexpectedTransforms.append("\n");
                  }
                }
              }
              if (expectedTransforms.length() > 0) {
                out.println("<h4>Expected Modifications</h4>");
                out.println(
                    "<p>Changes to certain fields such as MSH-4 and RXA-11.4 are expected as IIS may request specific values in these fields.  </p>");
                out.println("  <pre>" + expectedTransforms + "</pre>");
              }
              if (expectedTransforms.length() > 0) {
                out.println("<h4>Unexpected Modifications</h4>");
                out.println("<p>These changes were not anticipated in the national standard or in NIST testing. "
                    + "Please examine the need for these changes carefully as they are likely to result in significant "
                    + "effort by EHR-s and other trading partners to achieve interoperability. </p>");
                out.println("  <pre>" + unexpectedTransforms + "</pre>");
              }
            } catch (IOException ioe) {
              // Not expected when reading string
              out.println("Exception ocurred: " + ioe.getMessage());
            }
          }
        }
        List<String> scenarioList = new ArrayList<String>(connector.getScenarioTransformationsMap().keySet());
        Collections.sort(scenarioList);
        for (String scenario : scenarioList) {
          out.println("  <h4>" + scenario + "</h4>");
          out.println("  <pre>" + connector.getScenarioTransformationsMap().get(scenario) + "</pre>");
        }
      }

      if (connector.getAckType().getDescription() != null) {
        out.println("<h4>Custom Logic for Reading Acknowledgment</h4>");
        out.println(
            "<p>Special logic was developed to determine whether test messages were accepted or not because this"
                + "IIS returns a non-standard acknowledgement message. An effort was made to properly understand these messages, "
                + "but the logic below should be carefully reviewed as it forms the basis for this testing report. </p>");
        out.println("<pre>" + connector.getAckType().getDescription() + "</pre>");
      }

      out.println("<div id=\"interoperability\"/>");
      out.println("<h2>Interoperability</h2>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th>Test</th>");
      out.println("    <th>Result</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : certifyAreas[SUITE_A_BASIC].getUpdateList()) {
        printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("<p>");
      out.println("  <a href=\"" + REPORT_EXPLANATION_URL + "#interoperability\" class=\"boxLinks\">Description</a>");
      out.println("  <a href=\"IIS Testing Report Detail.html#areaALevel1\" class=\"boxLinks\">More Details</a>");
      out.println("</p>");

      out.println("<div id=\"codedValues\"/>");
      out.println("<h2>Coded Values</h2>");
      {
        if (reportScore[REPORT_2_CODED] == 1.0) {
          out.println("<p>All coded value messages were accepted. </p>");
        } else {
          out.println("<table border=\"1\" cellspacing=\"0\">");
          out.println("  <tr>");
          out.println("    <th>Test</th>");
          out.println("    <th>Result</th>");
          out.println("  </tr>");
          for (TestCaseMessage testCaseMessage : certifyAreas[SUITE_B_INTERMEDIATE].getUpdateList()) {
            if (testCaseMessage.isHasRun() && !testCaseMessage.isPassedTest()) {
              printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
            }
          }
          out.println("</table>");
        }
      }
      out.println("<p>");
      out.println("  <a href=\"" + REPORT_EXPLANATION_URL + "#codedValues\" class=\"boxLinks\">Description</a>");
      out.println("  <a href=\"IIS Testing Report Detail.html#areaBLevel1\" class=\"boxLinks\">More Details</a>");
      out.println("</p>");

      out.println("<div id=\"tolerance\"/>");
      out.println("<h2>Tolerance</h2>");
      if (reportScore[REPORT_5_TOLERANCE] == 1.0) {
        out.println(
            "<p>All tolerance messages were accepted. IIS is tolerant of unusual and unexpected message formats or values.</p>");
      } else {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>Test</th>");
        out.println("    <th>Results</th>");
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : certifyAreas[SUITE_D_EXCEPTIONAL].getUpdateList()) {
          if (testCaseMessage.getDescription().startsWith(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK)
              && testCaseMessage.isHasRun() && !testCaseMessage.isPassedTest()) {
            printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
          }
        }
        out.println("</table>");
      }
      out.println("<p>");
      out.println("  <a href=\"" + REPORT_EXPLANATION_URL + "#tolerance\" class=\"boxLinks\">Description</a>");
      out.println("  <a href=\"IIS Testing Report Detail.html#areaDLevel1\" class=\"boxLinks\">More Details</a>");
      out.println("</p>");

      out.println("<div id=\"ehrExample\"/>");
      out.println("<h2>EHR Examples</h2>");
      if (reportScore[REPORT_6_EHR] == 1.0) {
        out.println("<p>All EHR sample messages were accepted. </p>");
      } else {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>Test</th>");
        out.println("    <th>Results</th>");
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : certifyAreas[SUITE_D_EXCEPTIONAL].getUpdateList()) {
          if (testCaseMessage.getDescription().startsWith(VALUE_EXCEPTIONAL_PREFIX_CERTIFIED_MESSAGE)
              && testCaseMessage.isHasRun() && !testCaseMessage.isPassedTest()) {
            printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
          }
        }
        out.println("</table>");
      }
      out.println("<p>");
      out.println("  <a href=\"" + REPORT_EXPLANATION_URL + "#ehrExamples\" class=\"boxLinks\">Description</a>");
      out.println("  <a href=\"IIS Testing Report Detail.html#areaDLevel1\" class=\"boxLinks\">More Details</a>");
      out.println("</p>");

      out.println("<div id=\"performance\"/>");
      if (certifyAreas[SUITE_G_PERFORMANCE].getAreaCount()[0] > 0) {
        out.println("<h2>Performance</h2>");
        if (certifyAreas[SUITE_G_PERFORMANCE].getAreaScore()[0] < 3000) {
          out.println("    <p>Response time was as fast enough: " + printSeconds(performance.getUpdateAverage())
              + " average processing time per test message. </p>");
        } else {
          out.println("    <p>Response time was as slower than anticipated: "
              + printSeconds(performance.getUpdateAverage()) + " average processing time per test message. </p>");
        }
      }
      if (performance != null && performance.getTotalUpdateCount() > 0) {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>Average</th>");
        out.println("    <td>" + printSeconds(performance.getUpdateAverage()) + "</td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <th>Fastest</th>");
        out.println("    <td>" + printSeconds(performance.getMinUpdateTime()));
        if (performance.getMinUpdateTestCase() != null) {
          out.println(makeTestCaseMessageDetailsLink(performance.getMinUpdateTestCase(), toFile));
        }
        out.println("</td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <th>Slowest</th>");
        out.println("    <td>" + printSeconds(performance.getMaxUpdateTime()));
        if (performance.getMaxUpdateTestCase() != null) {
          out.println(makeTestCaseMessageDetailsLink(performance.getMaxUpdateTestCase(), toFile));
        }
        out.println("</td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <th>Std Dev</th>");
        out.println("    <td>" + printSeconds(performance.getUpdateSDev()) + "</td>");
        out.println("  </tr>");
        out.println("</table>");
      }
      if (performance != null && performance.getTotalQueryCount() > 0) {
        out.println("    <p>Slowest Query: ");
        out.printf("%.2fs ", (performance.getMaxQueryTime() / 1000.0));
        out.println("     <br/>Fastest Query: ");
        out.printf("%.2fs ", (performance.getMinQueryTime() / 1000.0));
        out.println("     <br/>Average: ");
        out.printf("%.2fs ", (performance.getQueryAverage() / 1000.0));
        out.println("     <br/>Std Dev: ");
        out.printf("%.2fs ", (performance.getQuerySDev() / 1000.0));
        out.println("    </p>");
      }
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL + "#performance\" class=\"boxLinks\">Description</a></p>");

      out.println("<div id=\"acknowledgmentConformance\"/>");
      out.println("<h2>Acknowledgment Conformance</h2>");
      if (reportScore[REPORT_8_ACK] == 1.0) {
        out.println("<p>All response messages (ACKs) conformed to expectations.</p>");
      } else {
        if (reportScore[REPORT_8_ACK] == 0.0) {
          out.println("<p>None of the response messages (ACKs) conformed to expectations.</p>");
        } else {
          out.println("<p>Not all response messages (ACKs) conformed to expectations.</p>");
        }
      }
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL
          + "#acknowledgmentConformance\" class=\"boxLinks\">Description</a></p>");

      if (certifyAreas[SUITE_I_PROFILING].getAreaProgress()[0] > 0) {
        out.println("<div id=\"localRequirements\"/>");
        out.println("<h2>Local Requirement Implementation</h2>");

        out.println("<h3>Base Message</h3>");
        out.println("<p>Base Message was accepted.  "
            + makeTestCaseMessageDetailsLink(((CAProfiling) certifyAreas[SUITE_I_PROFILING]).getTcmFull(), toFile)
            + "</p>");
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
          for (ProfileLine profileLine : ((CAProfiling) certifyAreas[SUITE_I_PROFILING]).getProfileLineList()) {
            if (profileLine.isHasRun() && !profileLine.isPassed()) {
              printProfileLine(out, toFile, profileLine, false);
            }
          }
          out.println("</table>");
        }
        out.println("<p>");
        out.println(
            "  <a href=\"" + REPORT_EXPLANATION_URL + "#localRequirements\" class=\"boxLinks\">Description</a>");
        out.println("  <a href=\"IIS Testing Report Detail.html#areaILevel1\" class=\"boxLinks\">More Details</a>");
        out.println("</p>");

        if (profileUsageComparisonConformance != null) {
          out.println("<div id=\"nationalCompatibility\"/>");
          out.println("<h2>National Compatibility</h2>");
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
          out.println("<p><a href=\"" + REPORT_EXPLANATION_URL
              + "#nationalCompatibility\" class=\"boxLinks\">Description</a></p>");
        }
      }

      out.println("<p><a href=\"IIS Testing Report Detail.html#areaHLevel1\">More Details</a></p>");
      out.println(
          "<div style=\"position: fixed; bottom: 0; right: 0; background-color: red; color: white; font-size: 180%; font-weight: bold; padding-left: 5px; padding-right 5px; \">DRAFT REPORT &#8212; DO NOT DISTRIBUTE</div>");

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
      out.println("<div style=\"width: " + ((int) (score * 200)) + "px; text-align: center;\" class=\"pass\">" + message
          + "</div>");
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

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
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
      out.println("  <tr>");
      out.println("    <th>Elapsed Test Time</th>");
      long elapsedTime = testFinished.getTime() - testStarted.getTime();
      int elapsedMinutes = (int) (elapsedTime / (1000.0 * 60.0) + 0.5);
      int elapsedHours = 0;
      if (elapsedMinutes > 60) {
        elapsedHours = elapsedMinutes / 60;
        elapsedMinutes = elapsedMinutes % 60;
      }
      out.println("    <td>" + elapsedHours + ":" + (elapsedMinutes < 10 ? "0" : "") + elapsedMinutes + "</td>");
      out.println("  </tr>");
    } else {
      if (caTotal.getAreaProgress()[0] < 100) {
        Date updateEtc = caTotal.estimatedUpdateCompletion();
        if (updateEtc != null) {
          out.println("  <tr>");
          out.println("    <th>Update ETC</th>");
          out.println("    <td>" + sdf.format(updateEtc) + "</td>");
          out.println("  </tr>");
        }
      } else if (caTotal.getAreaProgress()[1] < 100) {
        Date updateEtc = caTotal.estimatedQueryCompletion();
        if (updateEtc != null) {
          out.println("  <tr>");
          out.println("    <th>Query ETC</th>");
          out.println("    <td>" + sdf.format(updateEtc) + "</td>");
          out.println("  </tr>");
        }
      }
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
    out.println("<br/>");

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

    if (!connector.getCustomTransformations().equals("")) {
      out.println("<p>This interface requires customized modifications to modify each message before transmitting "
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
      out.println("<h3>Custom Modifications</h3>");
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
      out.println(
          "<p>The connection information for performing queries is different than for the updates. This is to either accomodate "
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
        out.println("    <th>Modifications</th>");
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
      if (certifyAreas[i].isRun()) {
        out.println("  <tr>");
        out.println("    <td>" + certifyAreas[i].getAreaLabel() + "</td>");
        for (int j = 0; j < 3; j++) {
          if (certifyAreas[i].getAreaCount()[j] == -1) {
            out.println("    <td>&nbsp;</td>");
          } else {
            out.println("    <td>" + certifyAreas[i].getAreaCount()[j] + "</td>");
          }
        }
      }
    }
    out.println("</table>");
    out.println("</br>");

    TestCaseMessage tcmFull = ((CAProfiling) certifyAreas[SUITE_I_PROFILING]).getTcmFull();
    if (tcmFull != null) {
      out.println("<h3>Base Message for Profiling</h3>");
      if (tcmFull.isAccepted()) {
        out.println("<p>Base Message was accepted.  " + makeTestCaseMessageDetailsLink(tcmFull, toFile) + "</p>");
      } else {
        out.println("<p>Base Message was NOT accepted.  " + makeTestCaseMessageDetailsLink(tcmFull, toFile) + "</p>");
      }
    }
  }

  public void printTestCaseMessageDetailsQueryOptional(PrintWriter out, boolean toFile,
      TestCaseMessage testCaseMessage) {
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

  public void printTestCaseMessageDetailsQueryRequired(PrintWriter out, boolean toFile,
      TestCaseMessage testCaseMessage) {
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
      if (testCaseMessage.isMajorChangesMade()) {
        out.println("    <td class=\"fail\">Not all core data could be submitted. "
            + makeCompareDetailsLink(testCaseMessage, toFile, true));
        out.println("<br/>");
        if (testCaseMessage.isAccepted()) {
          out.println("Message was accepted.");
        } else {
          out.println(" Message was rejected.");
        }
        out.println(makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
      } else if (testCaseMessage.isAccepted()) {
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

  public void printProfileLine(PrintWriter out, boolean toFile, ProfileLine profileLine, boolean highlightUnexpected) {

    String profileLineClassText = "nottested";
    boolean profileLinePassed = profileLine.isPassed();
    if (profileLine.isHasRun() && highlightUnexpected) {
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
        out.println("    <td class=\"" + profileLineClassText + "\">Unexpected</td>");
      }
    } else {
      out.println("    <td class=\"" + profileLineClassText + "\">-</td>");
    }
    printTestMessageCell(out, toFile, profileLine.getTestCaseMessagePresent(), highlightUnexpected);
    printTestMessageCell(out, toFile, profileLine.getTestCaseMessageAbsent(), highlightUnexpected);
    out.println("  </tr>");
  }

  public void printProfileLineComparisonConformance(PrintWriter out, boolean toFile, ProfileLine profileLine) {
    ProfileUsageValue profileUsageValue = profileUsageComparisonConformance.getProfileUsageValueMap()
        .get(profileLine.getField());
    if (profileUsageValue != null) {
      CompatibilityConformance compatibilityConformance = ProfileManager
          .getCompatibilityConformance(profileLine.getUsageDetected(), profileUsageValue.getUsage());
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
    ProfileUsageValue profileUsageValue = profileUsageComparisonInteroperability.getProfileUsageValueMap()
        .get(profileLine.getField());
    if (profileUsageValue != null) {
      CompatibilityInteroperability compatibilityInteroperability = ProfileManager
          .getCompatibilityInteroperability(profileLine.getProfileUsageValue(), profileUsageValue);
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

  public void printTestMessageCell(PrintWriter out, boolean toFile, TestCaseMessage testCaseMessage,
      boolean highlightUnexpected) {
    String classText = "nottested";
    boolean testPassed = false;
    if (testCaseMessage == null || !testCaseMessage.hasIssue()) {
      out.println("    <td class=\"" + classText + "\">Not Defined</td>");
    } else {
      if (!testCaseMessage.isHasRun()) {
        out.println("    <td class=\"" + classText + "\">Not Run</td>");
      } else {
        testPassed = testCaseMessage.getActualResultStatus().equals(TestRunner.ACTUAL_RESULT_STATUS_PASS);
        classText = "";
        if (highlightUnexpected) {
          classText = testPassed ? "pass" : "fail";
        }

        if (testPassed) {
          if (testCaseMessage == ((CAProfiling) certifyAreas[SUITE_I_PROFILING]).getTcmFull()) {
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
          if (testCaseMessage == ((CAProfiling) certifyAreas[SUITE_I_PROFILING]).getTcmFull()) {
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

  public String getStatus() {
    return status;
  }

  public void stopRunning() {
    keepRunning = false;
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

  private static String printSeconds(double ms) {
    if (ms == 0) {
      return "0s";
    }
    if (ms < 100) {
      return ((int) (ms + 0.5)) + "ms";
    }
    return ((int) (ms / 100.0 + 0.5)) / 10.0 + "s";
  }

  public static void reportParticipant(ParticipantResponse participantResponse) {
    if (REPORT_URL == null) {
      return;
    }
    try {
      HttpURLConnection urlConn;
      DataOutputStream printout;
      InputStreamReader input = null;
      URL url = new URL(REPORT_URL);

      urlConn = (HttpURLConnection) url.openConnection();

      urlConn.setRequestMethod("POST");

      urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);
      String content;
      StringBuilder sb = new StringBuilder();

      sb.append("action=Submit");
      addField(sb, PARAM_TPAR_ORGANIZATION_NAME, participantResponse.getOrganizationName());
      addField(sb, PARAM_TPAR_CONNECTION_LABEL, participantResponse.getFolderName());
      if (participantResponse.getRow() == 0 && participantResponse.getCol() == 0
          && !participantResponse.getMap().equals("1,1")) {
        addField(sb, PARAM_TPAR_MAP_ROW, 0);
        addField(sb, PARAM_TPAR_MAP_COL, 0);
      } else {
        addField(sb, PARAM_TPAR_MAP_ROW, participantResponse.getRow() + 1);
        addField(sb, PARAM_TPAR_MAP_COL, participantResponse.getCol() + 1);
      }
      addField(sb, PARAM_TPAR_PLATFORM_LABEL, participantResponse.getPlatform());
      addField(sb, PARAM_TPAR_VENDOR_LABEL, participantResponse.getVendor());
      addField(sb, PARAM_TPAR_INTERNAL_COMMENTS, participantResponse.getInternalComments());
      addField(sb, PARAM_TPAR_PHASE1_PARTICIPATION, participantResponse.getPhaseIParticipation());
      addField(sb, PARAM_TPAR_PHASE1_STATUS, participantResponse.getPhase1Status());
      addField(sb, PARAM_TPAR_PHASE1_COMMENTS, participantResponse.getPhase1Comments());
      addField(sb, PARAM_TPAR_PHASE2_PARTICIPATION, participantResponse.getPhaseIIParticipation());
      addField(sb, PARAM_TPAR_PHASE2_STATUS, participantResponse.getPhaseIIStatus());
      addField(sb, PARAM_TPAR_PHASE2_COMMENTS, participantResponse.getPhaseIIComments());
      addField(sb, PARAM_TPAR_IHS_STATUS, participantResponse.getIHS());
      addField(sb, PARAM_TPAR_GUIDE_STATUS, participantResponse.getRecordRequirementsStatus());
      addField(sb, PARAM_TPAR_GUIDE_NAME, participantResponse.getGuideName());
      addField(sb, PARAM_TPAR_CONNECT_STATUS, participantResponse.getConnecttoIISStatus());
      addField(sb, PARAM_TPAR_GENERAL_COMMENTS, participantResponse.getComments());
      addField(sb, PARAM_TPAR_TRANSPORT_TYPE, participantResponse.getTransport());
      addField(sb, PARAM_TPAR_QUERY_SUPPORT, participantResponse.getQuerySupport());
      addField(sb, PARAM_TPAR_NIST_STATUS, participantResponse.getNistStatus());
      addField(sb, PARAM_TPAR_ACCESS_PASSCODE, participantResponse.getAccessPasscode());

      content = sb.toString();
      printout = new DataOutputStream(urlConn.getOutputStream());
      printout.writeBytes(content);
      printout.flush();
      printout.close();
      input = new InputStreamReader(urlConn.getInputStream());
      StringBuilder response = new StringBuilder();
      BufferedReader in = new BufferedReader(input);
      String line;
      while ((line = in.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      input.close();
      response.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void reportProgress(TestCaseMessage testMessage) {
    reportProgress(testMessage, false, null);
  }

  protected void reportProgress(TestCaseMessage testMessage, boolean firstTime, ProfileLine profileLine) {
    if (REPORT_URL == null) {
      return;
    }
    try {

      Map<CompatibilityConformance, List<ProfileLine>> compatibilityMap = updateOverallScore();

      HttpURLConnection urlConn;
      DataOutputStream printout;
      InputStreamReader input = null;
      URL url = new URL(REPORT_URL);

      urlConn = (HttpURLConnection) url.openConnection();

      urlConn.setRequestMethod("POST");

      urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);
      String content;
      StringBuilder sb = new StringBuilder();

      sb.append("action=Submit");
      if (participantResponse != null) {
        addField(sb, PARAM_TPAR_ORGANIZATION_NAME, participantResponse.getOrganizationName());
      }
      addField(sb, PARAM_TC_CONNECTION_LABEL, connector.getLabel());
      addField(sb, PARAM_TC_CONNECTION_TYPE, connector.getType());
      addField(sb, PARAM_TC_CONNECTION_URL, connector.getUrl());
      addField(sb, PARAM_TC_CONNECTION_ACK_TYPE, connector.getAckType().toString());
      addField(sb, PARAM_TC_CONNECTION_CONFIG, connector.getScript());
      boolean latestTest = status.equals(STATUS_COMPLETED) && certifyAreas[SUITE_A_BASIC].getAreaProgress()[0] == 100
          && certifyAreas[SUITE_B_INTERMEDIATE].getAreaProgress()[0] == 100
          && certifyAreas[SUITE_D_EXCEPTIONAL].getAreaProgress()[0] == 100
          && certifyAreas[SUITE_I_PROFILING].getAreaProgress()[0] == 100
          && certifyAreas[SUITE_L_CONFORMANCE_2015].getAreaProgress()[0] == 100
          && certifyAreas[SUITE_C_ADVANCED].getAreaProgress()[0] == 100
          && certifyAreas[SUITE_K_NOT_ACCEPTED].getAreaProgress()[0] == 100
          && certifyAreas[SUITE_J_ONC_2015].getAreaProgress()[0] == 100;
      addField(sb, PARAM_TC_COMPLETE_TEST, latestTest);

      addField(sb, PARAM_TC_QUERY_TYPE, queryType);
      addField(sb, PARAM_TC_QUERY_ENABLED, willQuery);
      addField(sb, PARAM_TC_QUERY_PAUSE, pauseBeforeQuerying);
      addField(sb, PARAM_TC_TEST_LOG, statusMessageList);
      addField(sb, PARAM_TC_TEST_STATUS, status);
      addField(sb, PARAM_TC_TEST_STARTED_TIME, testStarted);
      addField(sb, PARAM_TC_TEST_FINISHED_TIME, testFinished);
      addField(sb, PARAM_TC_COUNT_UPDATE, performance.getTotalUpdateCount());
      addField(sb, PARAM_TC_COUNT_QUERY, performance.getTotalQueryCount());
      if (profileUsage != null) {
        addField(sb, PARAM_TC_PROFILE_BASE_NAME, profileUsage.toString());
      }
      if (profileUsageComparisonConformance != null) {
        addField(sb, PARAM_TC_PROFILE_COMPARE_NAME, profileUsageComparisonConformance.toString());
      }

      addField(sb, PARAM_TS_TEST_SECTION_TYPE, certifyAreas[currentSuite].getAreaLabel());
      addField(sb, PARAM_TS_TEST_ENABLED, certifyAreas[currentSuite].isRun());
      addField(sb, PARAM_TS_SCORE_LEVEL1, certifyAreas[currentSuite].getAreaScore()[0]);
      addField(sb, PARAM_TS_SCORE_LEVEL2, certifyAreas[currentSuite].getAreaScore()[1]);
      addField(sb, PARAM_TS_SCORE_LEVEL3, certifyAreas[currentSuite].getAreaScore()[1]);
      addField(sb, PARAM_TS_PROGRESS_LEVEL1, certifyAreas[currentSuite].getAreaProgress()[0]);
      addField(sb, PARAM_TS_PROGRESS_LEVEL2, certifyAreas[currentSuite].getAreaProgress()[1]);
      addField(sb, PARAM_TS_PROGRESS_LEVEL3, certifyAreas[currentSuite].getAreaProgress()[2]);
      addField(sb, PARAM_TS_COUNT_LEVEL1, certifyAreas[currentSuite].getAreaCount()[0]);
      addField(sb, PARAM_TS_COUNT_LEVEL2, certifyAreas[currentSuite].getAreaCount()[1]);
      addField(sb, PARAM_TS_COUNT_LEVEL3, certifyAreas[currentSuite].getAreaCount()[2]);
      addField(sb, PARAM_TC_SCORE_OVERALL, (int) (overallScore * 100));
      addField(sb, PARAM_TC_SCORE_INTEROP, (int) (reportScore[REPORT_1_INTEROP] * 100));
      addField(sb, PARAM_TC_SCORE_CODED, (int) (reportScore[REPORT_2_CODED] * 100));
      addField(sb, PARAM_TC_SCORE_LOCAL, (int) (reportScore[REPORT_3_LOCAL] * 100));
      addField(sb, PARAM_TC_SCORE_NATIONAL, (int) (reportScore[REPORT_4_NATIONAL] * 100));
      addField(sb, PARAM_TC_SCORE_TOLERANCE, (int) (reportScore[REPORT_5_TOLERANCE] * 100));
      addField(sb, PARAM_TC_SCORE_EHR, (int) (reportScore[REPORT_6_EHR] * 100));
      addField(sb, PARAM_TC_SCORE_PERFORM, (int) (reportScore[REPORT_7_PERFORM] * 100));
      addField(sb, PARAM_TC_SCORE_ACK, (int) (reportScore[REPORT_8_ACK] * 100));
      addField(sb, PARAM_TC_PER_QUERY_TOTAL, performance.getTotalQueryTime());
      addField(sb, PARAM_TC_PER_QUERY_COUNT, performance.getTotalQueryCount());
      addField(sb, PARAM_TC_PER_QUERY_MIN,
          performance.getMinQueryTime() == Long.MAX_VALUE ? 0 : performance.getMinQueryTime());
      addField(sb, PARAM_TC_PER_QUERY_MAX, performance.getMaxQueryTime());
      addField(sb, PARAM_TC_PER_QUERY_STD, performance.getQuerySDev());
      addField(sb, PARAM_TC_PER_UPDATE_TOTAL, performance.getTotalUpdateTime());
      addField(sb, PARAM_TC_PER_UPDATE_COUNT, performance.getTotalUpdateCount());
      addField(sb, PARAM_TC_PER_UPDATE_MIN,
          performance.getMinUpdateTime() == Long.MAX_VALUE ? 0 : performance.getMinUpdateTime());
      addField(sb, PARAM_TC_PER_UPDATE_MAX, performance.getMaxUpdateTime());
      addField(sb, PARAM_TC_PER_UPDATE_STD, performance.getUpdateSDev());
      if (firstTime) {
        int i = 1;
        if (connector.getCustomTransformations() != null && !connector.getCustomTransformations().equals("")) {
          addField(sb, PARAM_TC_TRANSFORMS + i, "\n" + connector.getCustomTransformations());
        }
        for (String scenario : connector.getScenarioTransformationsMap().keySet()) {
          i++;
          String transformText = connector.getScenarioTransformationsMap().get(scenario);
          if (transformText != null && !transformText.equals("")) {
            addField(sb, PARAM_TC_TRANSFORMS + i, "\n" + transformText);
          }
        }
      }
      if (testMessage != null) {
        addField(sb, PARAM_TM_TEST_POSITION, testMessage.getTestPosition());
        addField(sb, PARAM_TM_TEST_TYPE, testMessage.getTestType());
        addField(sb, PARAM_TM_TEST_CASE_SET, testMessage.getTestCaseSet());
        addField(sb, PARAM_TM_TEST_CASE_NUMBER, testMessage.getTestCaseNumber());
        addField(sb, PARAM_TM_TEST_CASE_CATEGORY, testMessage.getTestCaseCategoryId());
        addField(sb, PARAM_TM_TEST_CASE_DESCRIPTION, testMessage.getDescription());
        addField(sb, PARAM_TM_TEST_CASE_ASSERT_RESULT, testMessage.getAssertResult());
        addField(sb, PARAM_TM_PREP_PATIENT_TYPE, testMessage.getPatientType().toString());
        addField(sb, PARAM_TM_PREP_TRANSFORMS_QUICK, testMessage.getQuickTransformations());
        addField(sb, PARAM_TM_PREP_TRANSFORMS_CUSTOM, testMessage.getCustomTransformations());
        addField(sb, PARAM_TM_PREP_TRANSFORMS_ADDITION, testMessage.getAdditionalTransformations());
        addField(sb, PARAM_TM_PREP_TRANSFORMS_CAUSE_ISSUE, testMessage.getCauseIssueTransforms());
        addField(sb, PARAM_TM_PREP_CAUSE_ISSUE_NAMES, testMessage.getCauseIssues());
        addField(sb, PARAM_TM_PREP_HAS_ISSUE, testMessage.hasIssue());
        addField(sb, PARAM_TM_PREP_MAJOR_CHANGES_MADE, testMessage.isMajorChangesMade());
        addField(sb, PARAM_TM_PREP_NOT_EXPECTED_TO_CONFORM, testMessage.isResultNotExpectedToConform());
        addField(sb, PARAM_TM_PREP_MESSAGE_ACCEPT_STATUS_DEBUG, testMessage.getMessageAcceptStatusDebug());
        addField(sb, PARAM_TM_PREP_SCENARIO_NAME, testMessage.getScenario());
        addField(sb, PARAM_TM_PREP_MESSAGE_DERIVED_FROM, testMessage.getDerivedFromVXUMessage());
        addField(sb, PARAM_TM_PREP_MESSAGE_ORIGINAL, testMessage.getOriginalMessage());
        addField(sb, PARAM_TM_PREP_MESSAGE_ORIGINAL_RESPONSE, testMessage.getOriginalMessageResponse());
        addField(sb, PARAM_TM_PREP_MESSAGE_ACTUAL, testMessage.getMessageTextSent());
        addField(sb, PARAM_TM_RESULT_MESSAGE_ACTUAL, testMessage.getActualResponseMessage());
        addField(sb, PARAM_TM_RESULT_STATUS, testMessage.getActualResultStatus());
        addField(sb, PARAM_TM_RESULT_ACCEPTED, testMessage.isAccepted());
        addField(sb, PARAM_TM_RESULT_EXECEPTION_TEXT, testMessage.getException());
        addField(sb, PARAM_TM_RESULT_ACCEPTED_MESSAGE, testMessage.getActualResultAckMessage());
        addField(sb, PARAM_TM_RESULT_RESPONSE_TYPE, testMessage.getActualMessageResponseType());
        addField(sb, PARAM_TM_RESULT_ACK_TYPE, testMessage.getActualResultAckType());
        if (testMessage.getValidationReport() == null) {
          addField(sb, PARAM_TM_RESULT_ACK_CONFORMANCE, VALUE_RESULT_ACK_CONFORMANCE_NOT_RUN);
        } else {
          boolean errorFound = (testMessage.getValidationReport().getAssertionList().size() == 0);
          if (!errorFound) {
            for (Assertion assertion : testMessage.getValidationReport().getAssertionList()) {
              if (assertion.getResult().equalsIgnoreCase("ERROR") || assertion.getResult().equals("")) {
                errorFound = true;
                break;
              }
            }
          }
          if (errorFound) {
            addField(sb, PARAM_TM_RESULT_ACK_CONFORMANCE, VALUE_RESULT_ACK_CONFORMANCE_ERROR);
          } else {
            addField(sb, PARAM_TM_RESULT_ACK_CONFORMANCE, VALUE_RESULT_ACK_CONFORMANCE_OK);
          }
        }
        addField(sb, PARAM_TM_RESULT_QUERY_TYPE, testMessage.getActualResultQueryType());
        addField(sb, PARAM_TM_RESULT_ACK_STORE_STATUS, testMessage.getResultStoreStatus());
        addField(sb, PARAM_TM_RESULT_FORECAST_STATUS, testMessage.getResultForecastStatus());
        if (testMessage.getForecastTestCase() != null) {
          addField(sb, PARAM_TM_FORECAST_TEST_PANEL_CASE_ID, testMessage.getForecastTestCase().getTestPanelCaseId());
        }
        if (testMessage.getForecastTestPanel() != null) {
          addField(sb, PARAM_TM_FORECAST_TEST_PANEL_ID, testMessage.getForecastTestPanel().getId());
        }
        if (profileLine != null) {
          addField(sb, PARAM_TP_PROFILE_FIELD_NAME, profileLine.getField().getFieldName());
          if (profileLine.getTestCaseMessagePresent() == testMessage) {
            addField(sb, PARAM_TP_TEST_PROFILE_TYPE, VALUE_PROFILE_TYPE_PRESENT);
          } else if (profileLine.getTestCaseMessageAbsent() == testMessage) {
            addField(sb, PARAM_TP_TEST_PROFILE_TYPE, VALUE_PROFILE_TYPE_ABSENT);
          }
        }

        List<Comparison> comparisonList;
        if (testMessage.getTestType().equals(VALUE_TEST_TYPE_QUERY)) {
          comparisonList = CompareManager.compareMessages(testMessage.getDerivedFromVXUMessage(),
              testMessage.getActualResponseMessage());
        } else {
          comparisonList = CompareManager.compareMessages(testMessage.getMessageText(),
              testMessage.getMessageTextSent());
        }
        int position = 0;
        for (Comparison comparison : comparisonList) {
          position++;
          addField(sb, PARAM_C_FIELD_NAME + position, comparison.getHl7FieldName());
          addField(sb, PARAM_C_FIELD_LABEL + position, comparison.getFieldLabel());
          addField(sb, PARAM_C_PRIORITY_LABEL + position, comparison.getPriorityLevelLabel());
          addField(sb, PARAM_C_VALUE_ORIGINAL + position, comparison.getOriginalValue());
          addField(sb, PARAM_C_VALUE_COMPARE + position, comparison.getReturnedValue());
          if (comparison.isTested()) {
            if (comparison.isPass()) {
              addField(sb, PARAM_C_COMPARISON_STATUS + position, VALUE_COMPARISON_STATUS_PASS);
            } else {
              addField(sb, PARAM_C_COMPARISON_STATUS + position, VALUE_COMPARISON_STATUS_FAIL);
            }
          } else {
            addField(sb, PARAM_C_COMPARISON_STATUS + position, VALUE_COMPARISON_STATUS_NOT_TESTED);
          }
        }
        if (testMessage.getValidationReport() != null) {
          position = 0;
          for (Assertion assertion : testMessage.getValidationReport().getAssertionList()) {
            position++;
            addField(sb, PARAM_A_ASSERTION_DESCRIPTION + position, assertion.getDescription());
            addField(sb, PARAM_A_ASSERTION_RESULT + position, assertion.getResult());
            addField(sb, PARAM_A_ASSERTION_TYPE + position, assertion.getType());
            addField(sb, PARAM_A_LOCATION_PATH + position, assertion.getPath());
          }
          // to save on memory remove validation report
          testMessage.setValidationReport(null);
        }
      }

      content = sb.toString();
      printout = new DataOutputStream(urlConn.getOutputStream());
      printout.writeBytes(content);
      printout.flush();
      printout.close();
      input = new InputStreamReader(urlConn.getInputStream());
      StringBuilder response = new StringBuilder();
      BufferedReader in = new BufferedReader(input);
      String line;
      while ((line = in.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      input.close();
      response.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  protected void reportForecastProgress(TestCaseMessage testMessage) {
    if (REPORT_URL == null) {
      return;
    }
    try {

      HttpURLConnection urlConn;
      DataOutputStream printout;
      InputStreamReader input = null;
      URL url = new URL(REPORT_URL);

      urlConn = (HttpURLConnection) url.openConnection();

      urlConn.setRequestMethod("POST");

      urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);
      String content;
      StringBuilder sb = new StringBuilder();

      sb.append("action=Submit");
      addField(sb, PARAM_TC_CONNECTION_LABEL, connector.getLabel());

      addField(sb, PARAM_TC_TEST_STARTED_TIME, testStarted);
      addField(sb, PARAM_TS_TEST_SECTION_TYPE, certifyAreas[currentSuite].getAreaLabel());
      addField(sb, PARAM_TM_TEST_POSITION, testMessage.getTestPosition());

      if (testMessage.getForecastActualList() != null) {
        int position = 0;
        for (ForecastActual forecastActual : testMessage.getForecastActualList()) {
          position++;
          addField(sb, PARAM_F_VACCINE_CODE + position, forecastActual.getVaccineCvx());
          addField(sb, PARAM_F_FORECAST_TYPE + position, VALUE_FORECAST_TYPE_ACTUAL);
          addField(sb, PARAM_F_SCHEDULE_NAME + position, forecastActual.getScheduleName());
          addField(sb, PARAM_F_SERIES_NAME + position, forecastActual.getSeriesName());
          addField(sb, PARAM_F_SERIES_DOSE_COUNT + position, forecastActual.getSeriesDoseCount());
          addField(sb, PARAM_F_DOSE_NUMBER + position, forecastActual.getDoseNumber());
          addField(sb, PARAM_F_DATE_EARLIEST + position, forecastActual.getValidDate());
          addField(sb, PARAM_F_DATE_DUE + position, forecastActual.getDueDate());
          addField(sb, PARAM_F_DATE_OVERDUE + position, forecastActual.getOverdueDate());
          addField(sb, PARAM_F_DATE_LATEST + position, forecastActual.getFinishedDate());
          addField(sb, PARAM_F_SERIES_STATUS + position, forecastActual.getSeriesStatus());
          addField(sb, PARAM_F_REASON_CODE + position, forecastActual.getReasonCode());
        }
      }

      if (testMessage.getEvaluationActualList() != null) {
        int position = 0;
        for (EvaluationActual evaluationActual : testMessage.getEvaluationActualList()) {
          position++;
          addField(sb, PARAM_E_COMPONENT_CODE + position, evaluationActual.getComponentCvx());
          addField(sb, PARAM_E_VACCINE_CODE + position, evaluationActual.getVaccineCvx());
          addField(sb, PARAM_E_VACCINE_DATE + position, evaluationActual.getVaccineDate());
          addField(sb, PARAM_E_EVALUATION_TYPE + position, VALUE_FORECAST_TYPE_ACTUAL);
          addField(sb, PARAM_E_SCHEDULE_NAME + position, evaluationActual.getScheduleName());
          addField(sb, PARAM_E_DOSE_NUMBER + position, evaluationActual.getDoseNumber());
          addField(sb, PARAM_E_DOSE_VALIDITY + position, evaluationActual.getDoseValidity());
          addField(sb, PARAM_E_SERIES_NAME + position, evaluationActual.getSeriesName());
          addField(sb, PARAM_E_SERIES_DOSE_COUNT + position, evaluationActual.getSeriesDoseCount());
          addField(sb, PARAM_E_SERIES_STATUS + position, evaluationActual.getSeriesStatus());
          addField(sb, PARAM_E_REASON_CODE + position, evaluationActual.getReasonCode());
        }
      }

      content = sb.toString();
      printout = new DataOutputStream(urlConn.getOutputStream());
      printout.writeBytes(content);
      printout.flush();
      printout.close();
      input = new InputStreamReader(urlConn.getInputStream());
      StringBuilder response = new StringBuilder();
      BufferedReader in = new BufferedReader(input);
      String line;
      while ((line = in.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      input.close();
      response.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private static void addField(StringBuilder sb, String field, String value) throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    if (value != null) {
      sb.append(URLEncoder.encode(value, "UTF-8"));
    }
  }

  private static void addField(StringBuilder sb, String field, Throwable value) throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    if (value != null) {
      StringWriter sw = new StringWriter();
      PrintWriter out = new PrintWriter(sw);
      value.printStackTrace(out);
      out.close();
      sb.append(URLEncoder.encode(sw.toString(), "UTF-8"));
    }
  }

  private static void addField(StringBuilder sb, String field, String[] values) throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    if (values != null) {
      for (String value : values) {
        sb.append(URLEncoder.encode(value + "\n", "UTF-8"));
      }
    }
  }

  private static void addField(StringBuilder sb, String field, List<String> values)
      throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    if (values != null) {
      for (String value : values) {
        sb.append(URLEncoder.encode(value + "\n", "UTF-8"));
      }
    }
  }

  private static void addField(StringBuilder sb, String field, boolean value) throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    sb.append(value ? VALUE_YES : VALUE_NO);
  }

  private static void addField(StringBuilder sb, String field, Date value) throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    if (value != null) {
      sb.append(URLEncoder.encode(VALUE_DATE_FORMAT.format(value), "UTF-8"));
    }
  }

  private static void addField(StringBuilder sb, String field, int value) throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    sb.append(value);
  }

  private static void addField(StringBuilder sb, String field, long value) throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    sb.append(value);
  }

  private static void addField(StringBuilder sb, String field, double value) throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    sb.append(value);
  }

}
