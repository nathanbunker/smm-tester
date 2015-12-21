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
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_1_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_2_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_3_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_4_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_5_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_6_R;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.RunAgainstConnector;
import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.manager.QueryConverter;
import org.immunizationsoftware.dqa.tester.manager.TestCaseMessageManager;
import org.immunizationsoftware.dqa.tester.manager.forecast.ForecastTesterManager;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.nist.Assertion;
import org.immunizationsoftware.dqa.tester.manager.nist.ValidationResource;
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
import org.openimmunizationsoftware.dqa.tr.RecordServletInterface;

public class CertifyRunner extends Thread implements RecordServletInterface
{

  private static final String REPORT_URL = "http://ois-pt.org/dqacm/record";
  // "http://localhost:8289/record";
  // "http://ois-pt.org/dqacm/record";

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
  Performance performance = null;

  private class IncrementingInt
  {
    private int i = 0;

    public synchronized int next() {
      i++;
      return i;
    }
  }

  private void logStatus(String status) {
    synchronized (statusMessageList) {
      statusMessageList.add(sdf.format(new Date()) + " " + areaLabel[currentSuite] + ": " + status);
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
  public static final int SUITE_J_ONC_2015 = 9;
  public static final int SUITE_K_NOT_ACCEPTED = 10;
  public static final int SUITE_L_CONFORMANCE_2015 = 11;
  public static final int SUITE_M_QBP_SUPPORT = 12;
  public static final int SUITE_N_TRANSFORM = 13;
  public static final int SUITE_O_EXTRA = 14;
  public static final int SUITE_COUNT = 15;

  private int currentSuite = SUITE_A_BASIC;
  private IncrementingInt incrementingInt = null;

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
    areaLabel[SUITE_A_BASIC] = VALUE_TEST_SECTION_TYPE_BASIC;
    areaLabel[SUITE_B_INTERMEDIATE] = VALUE_TEST_SECTION_TYPE_INTERMEDIATE;
    areaLabel[SUITE_C_ADVANCED] = VALUE_TEST_SECTION_TYPE_ADVANCED;
    areaLabel[SUITE_D_EXCEPTIONAL] = VALUE_TEST_SECTION_TYPE_EXCEPTIONAL;
    areaLabel[SUITE_E_FORECAST_PREP] = VALUE_TEST_SECTION_TYPE_FORECAST_PREP;
    areaLabel[SUITE_F_FORECAST] = VALUE_TEST_SECTION_TYPE_FORECAST;
    areaLabel[SUITE_G_PERFORMANCE] = VALUE_TEST_SECTION_TYPE_PERFORMANCE;
    areaLabel[SUITE_H_CONFORMANCE] = VALUE_TEST_SECTION_TYPE_CONFORMANCE;
    areaLabel[SUITE_I_PROFILING] = VALUE_TEST_SECTION_TYPE_PROFILING;
    areaLabel[SUITE_J_ONC_2015] = VALUE_TEST_SECTION_TYPE_ONC_2015;
    areaLabel[SUITE_K_NOT_ACCEPTED] = VALUE_TEST_SECTION_TYPE_NOT_ACCEPTED;
    areaLabel[SUITE_L_CONFORMANCE_2015] = VALUE_TEST_SECTION_TYPE_CONFORMANCE_2015;
    areaLabel[SUITE_M_QBP_SUPPORT] = VALUE_TEST_SECTION_TYPE_QBP_SUPPORT;
    areaLabel[SUITE_N_TRANSFORM] = VALUE_TEST_SECTION_TYPE_QBP_SUPPORT;
    areaLabel[SUITE_O_EXTRA] = VALUE_TEST_SECTION_TYPE_QBP_SUPPORT;
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

      if (areaProgress[SUITE_D_EXCEPTIONAL][0] > 0) {
        int countTolerance = 0;
        int countEhr = 0;
        int countTolerancePass = 0;
        int countEhrPass = 0;

        for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
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
    return run[suite];
  }

  public void setRun(boolean r, int suite) {
    run[suite] = r;
  }

  private String testCaseSet = "";

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
  private List<TestCaseMessage> statusCheckTestCaseAdvancedList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseForecastPrepList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseOnc2015List = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseNotAcceptedList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseQuerySupportList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseTransformList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseExtraList = new ArrayList<TestCaseMessage>();

  private List<TestCaseMessage> statusCheckQueryTestCaseBasicList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseIntermediateList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseProfilingList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseAdvancedList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseTolerantList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseForecastPrepList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseOnc2015List = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseNotAcceptedList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestQuerySupportList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestTransformList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestExtraList = new ArrayList<TestCaseMessage>();

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

  private String[] onc2015Scenarios = { SCENARIO_ONC_2015_IZ_AD_1_R, SCENARIO_ONC_2015_IZ_AD_2_R,
      SCENARIO_ONC_2015_IZ_AD_3_R, SCENARIO_ONC_2015_IZ_AD_4_R, SCENARIO_ONC_2015_IZ_AD_5_R,
      SCENARIO_ONC_2015_IZ_AD_6_R };

  private List<TestCaseMessage>[] profileTestCaseLists = new ArrayList[3];

  Connector connector;

  public Connector getConnector() {
    return connector;
  }

  Connector queryConnector;

  public CertifyRunner(Connector connector, SendData sendData) {
    this.connector = connector;
    this.queryConnector = connector.getOtherConnectorMap().get(Connector.PURPOSE_QUERY);
    if (this.queryConnector == null) {
      queryConnector = connector;
    }

    this.sendData = sendData;

    status = STATUS_INITIALIZED;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    testCaseSet = CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX + " " + sdf.format(new Date());

    performance = new Performance();

    sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    statusMessageList = new ArrayList<String>();
    logStatus("IIS Tester Initialized");
  }

  private static int uniqueMRNBaseInc = 0;

  private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

  private Transformer transformer;
  private SendData sendData;
  private File testDir;
  private ParticipantResponse participantResponse = null;
  private TestCaseMessage testCaseMessageBase = null;

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
      willQuery = queryType != null && (queryType.equals(QUERY_TYPE_QBP) || queryType.equals(QUERY_TYPE_VXQ));

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
        testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
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
        testDir = new File(sendData.getRootDir(), testCaseSet);
        if (!testDir.exists()) {
          testDir.mkdir();
        }
      }

      {
        for (int i = 0; i < run.length; i++) {
          if (run[i]) {
            currentSuite = i;
            reportProgress(null);
          }
        }
        currentSuite = SUITE_A_BASIC;
      }

      if (run[SUITE_A_BASIC]) {

        currentSuite = SUITE_A_BASIC;
        logStatus("Preparing basic messages");
        prepareBasic();

        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        logStatus("Sending basic messages");
        updateBasic();
        // printReportToFile();
      }

      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }

      if (run[SUITE_J_ONC_2015]) {

        currentSuite = SUITE_J_ONC_2015;
        logStatus("Preparing ONC 2015 messages");
        prepareOnc2015();

        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        logStatus("Sending basic messages");
        updateOnc2015();
        // printReportToFile();
      }

      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }

      if (run[SUITE_K_NOT_ACCEPTED]) {

        currentSuite = SUITE_K_NOT_ACCEPTED;
        logStatus("Preparing Not Accepted messages");
        prepareNotAccepted();

        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        logStatus("Sending basic messages");
        update("J ONC 2015", statusCheckTestCaseNotAcceptedList);
        // printReportToFile();
      }

      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }

      if (run[SUITE_B_INTERMEDIATE]) {
        currentSuite = SUITE_B_INTERMEDIATE;

        logStatus("Preparing intermediate");
        prepareIntermediate();

        logStatus("Sending intermediate messages");
        updateIntermediate();
        // printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

      }
      if (run[SUITE_C_ADVANCED]) {
        currentSuite = SUITE_C_ADVANCED;
        Map<Integer, List<Issue>> issueMap = new HashMap<Integer, List<Issue>>();

        logStatus("Preparing advanced messages");
        prepareAdvanced(issueMap);
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        logStatus("Sending advanced messages");
        updateAdvanced(issueMap);
        // printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }
      }

      if (run[SUITE_I_PROFILING]) {
        currentSuite = SUITE_I_PROFILING;
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
            // printReportToFile();
            if (!keepRunning) {
              status = STATUS_STOPPED;
              reportProgress(null);
              return;
            }
          }
        }
      }

      if (run[SUITE_D_EXCEPTIONAL]) {
        currentSuite = SUITE_D_EXCEPTIONAL;
        logStatus("Preparing exceptional messages");
        prepareExceptional();

        logStatus("Sending exceptional messages");
        updateExceptional();
        // printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }
      }

      if (run[SUITE_E_FORECAST_PREP]) {
        currentSuite = SUITE_E_FORECAST_PREP;
        logStatus("Preparing forecast prep messages");
        prepareForecastPrep();

        logStatus("Sending forecast prep messages");
        updateForecastPrep();
        // printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }
      }

      if (run[SUITE_M_QBP_SUPPORT]) {

        currentSuite = SUITE_M_QBP_SUPPORT;
        logStatus("Preparing Query Support messages");
        prepareQuerySupport();

        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        logStatus("Sending query support messages");
        updateQuerySupport();
        // printReportToFile();
      }

      if (run[SUITE_N_TRANSFORM]) {
        currentSuite = SUITE_N_TRANSFORM;
        logStatus("Preparing Transform messages");
        prepareTransform();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }
        logStatus("Sending transform messages");
        update("N Transform", statusCheckTestCaseTransformList);
      }

      if (run[SUITE_O_EXTRA]) {
        currentSuite = SUITE_O_EXTRA;
        logStatus("Preparing Extra messages");
        prepareExtra();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }
        logStatus("Sending Extra messages");
        update("O Extra", statusCheckTestCaseExtraList);
      }

      if (performance.getTotalUpdateCount() > 0) {
        areaScore[SUITE_G_PERFORMANCE][0] = (int) performance.getUpdateAverage();
        areaProgress[SUITE_G_PERFORMANCE][0] = 100;
        areaCount[SUITE_G_PERFORMANCE][0] = performance.getTotalUpdateCount();
      }
      if (run[SUITE_H_CONFORMANCE]) {
        currentSuite = SUITE_H_CONFORMANCE;

        logStatus("Prepare for format analysis of update messages");
        prepareFormatUpdateAnalysis();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        logStatus("Analyze format updates");
        analyzeConformance();
        reportProgress(null);
        // printReportToFile();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        if (run[SUITE_L_CONFORMANCE_2015]) {
          currentSuite = SUITE_L_CONFORMANCE_2015;

          logStatus("Prepare for conformance 2015 analysis of update messages");
          prepareConformance2015UpdateAnalysis();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }

          logStatus("Analyze format updates");
          analyzeConformance2015();
          reportProgress(null);
          // printReportToFile();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
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

        if (run[SUITE_A_BASIC]) {
          currentSuite = SUITE_A_BASIC;
          logStatus("Submit query for basic messages");
          queryBasic();
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        if (run[SUITE_J_ONC_2015]) {
          currentSuite = SUITE_J_ONC_2015;
          logStatus("Submit query for ONC 2015 messages");
          queryOnc2015();
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        if (run[SUITE_K_NOT_ACCEPTED]) {
          currentSuite = SUITE_K_NOT_ACCEPTED;
          logStatus("Submit query for Not Accepted messages");
          query(statusCheckTestCaseNotAcceptedList, statusCheckQueryTestCaseBasicList, "K");
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        if (run[SUITE_B_INTERMEDIATE]) {
          currentSuite = SUITE_B_INTERMEDIATE;
          logStatus("Prepare query for intermediate messages");
          prepareQueryIntermediate();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }

          logStatus("Submit query for intermediate messages");
          query(SUITE_B_INTERMEDIATE, statusCheckQueryTestCaseIntermediateList);
          // printReportToFile();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
        }

        if (run[SUITE_I_PROFILING]) {
          currentSuite = SUITE_I_PROFILING;
          logStatus("Prepare query for profiling messages");
          prepareQueryProfiling();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
          logStatus("Submit query for profiling messages");
          query(SUITE_I_PROFILING, statusCheckQueryTestCaseProfilingList);
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
        }

        if (run[SUITE_C_ADVANCED]) {
          currentSuite = SUITE_I_PROFILING;
          logStatus("Prepare query for advanced messages");
          prepareQueryAdvanced();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
          logStatus("Submit query for advanced messages");
          query(SUITE_C_ADVANCED, statusCheckQueryTestCaseAdvancedList);
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
        }

        if (run[SUITE_D_EXCEPTIONAL]) {
          currentSuite = SUITE_D_EXCEPTIONAL;
          logStatus("Prepare query for exceptional messages");
          prepareQueryExeptional();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
          logStatus("Submit query for exceptional messages");
          query(SUITE_D_EXCEPTIONAL, statusCheckQueryTestCaseTolerantList);
          // printReportToFile();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
        }

        if (run[SUITE_E_FORECAST_PREP]) {
          currentSuite = SUITE_E_FORECAST_PREP;
          logStatus("Prepare query for forecast prep messages");
          prepareQueryForecastPrep();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }

          logStatus("Submit query for forecast prep messages");
          query(SUITE_E_FORECAST_PREP, statusCheckQueryTestCaseForecastPrepList);

          // printReportToFile();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
        }

        if (run[SUITE_M_QBP_SUPPORT]) {
          currentSuite = SUITE_M_QBP_SUPPORT;
          logStatus("Submit query for Query Support messages");
          queryQbpSupport();
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        if (run[SUITE_N_TRANSFORM]) {
          currentSuite = SUITE_N_TRANSFORM;
          logStatus("Submit query for Transform messages");
          query(statusCheckTestCaseTransformList, statusCheckQueryTestTransformList, "N");
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        if (run[SUITE_O_EXTRA]) {
          currentSuite = SUITE_O_EXTRA;
          logStatus("Submit query for Extra messages");
          query(statusCheckTestCaseExtraList, statusCheckQueryTestExtraList, "O");
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          reportProgress(null);
          return;
        }

        if (performance.getTotalQueryCount() > 0) {
          areaScore[SUITE_G_PERFORMANCE][1] = (int) performance.getQueryAverage();
          areaCount[SUITE_G_PERFORMANCE][1] = performance.getTotalQueryCount();
          areaProgress[SUITE_G_PERFORMANCE][1] = 100;
        }

        if (run[SUITE_H_CONFORMANCE]) {
          currentSuite = SUITE_H_CONFORMANCE;
          logStatus("Prepare for format analysis of queries");
          prepareFormatQueryAnalysis();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }

          logStatus("Analyze format of query messages");
          analyzeFormatQueries();

          if (run[SUITE_L_CONFORMANCE_2015]) {
            currentSuite = SUITE_L_CONFORMANCE_2015;
            logStatus("Prepare for conformance 2015 analysis of queries");
            prepareConformance2015QueryAnalysis();
            if (!keepRunning) {
              status = STATUS_STOPPED;
              reportProgress(null);
              return;
            }

            logStatus("Analyze format of query messages");
            analyzeConformance2015Queries();
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
    tcmFull.setTestPosition(incrementingInt.next());
    tcmFull.setTestType(VALUE_TEST_TYPE_PREP);

    logStatus("Running full test record to see it will be accepted");
    TestRunner testRunner = new TestRunner();
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
    testRunner.runTest(connector, tcmFull);
    performance.addTotalUpdateTime(testRunner.getTotalRunTime(), tcmFull);
    tcmFull.setHasRun(true);

    if (tcmFull.isPassedTest()) {
      logStatus("Full record for profiling was accepted, profiling of all fields can begin");
    } else {
      logStatus("Full record for profiling was NOT accepted, profiling of all fields can not be conducted");
    }

    reportProgress(tcmFull);

  }

  public PrintWriter setupExampleFile(String name, TestCaseMessage testCaseMessage) {
    if (testCaseMessage != null) {
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

  private void prepareFormatUpdateAnalysis() {
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseList) {
      if (testCaseMessage.isHasRun() && testCaseMessage.getActualMessageResponseType().equals("ACK")
          && !testCaseMessage.isResultNotExpectedToConform()) {
        ackAnalysisList.add(testCaseMessage);
      }
    }
    areaCount[SUITE_H_CONFORMANCE][0] = ackAnalysisList.size();
  }

  private void prepareConformance2015UpdateAnalysis() {
    areaCount[SUITE_L_CONFORMANCE_2015][0] = ackAnalysisList.size();
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

  private void prepareConformance2015QueryAnalysis() {
    areaCount[SUITE_L_CONFORMANCE_2015][1] = rspAnalysisList.size();
  }

  private void analyzeConformance() {
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
        reportProgress(null);
        return;
      }
    }
    areaProgress[SUITE_H_CONFORMANCE][0] = makeScore(count, ackAnalysisList.size());
    areaScore[SUITE_H_CONFORMANCE][0] = makeScore(pass, ackAnalysisList.size());
  }

  private void analyzeConformance2015() {
    int count = 0;
    int pass = 0;
    for (TestCaseMessage testCaseMessage : ackAnalysisList) {
      count++;
      if (testCaseMessage.isHasRun()) {
        if (testCaseMessage.isValidationReportPass()) {
          pass++;
        }
        // TODO
        areaProgress[SUITE_L_CONFORMANCE_2015][0] = makeScore(count, ackAnalysisList.size());
      }
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaProgress[SUITE_L_CONFORMANCE_2015][0] = makeScore(count, ackAnalysisList.size());
    areaScore[SUITE_L_CONFORMANCE_2015][0] = makeScore(pass, ackAnalysisList.size());
  }

  public void ascertainValidationResource(TestCaseMessage testCaseMessage, String messageText) {
    ValidationResource validationResource = null;
    HL7Reader hl7Reader = new HL7Reader(messageText);
    if (hl7Reader.advanceToSegment("MSH")) {
      String messageType = hl7Reader.getValue(9);
      String profileId = hl7Reader.getValue(21);
      if (profileId.equals("Z31")) {
        validationResource = ValidationResource.IZ_RSP_Z31;
      } else if (profileId.equals("Z32")) {
        validationResource = ValidationResource.IZ_RSP_Z32;
      } else if (profileId.equals("Z33")) {
        validationResource = ValidationResource.IZ_RSP_Z33;
      } else if (profileId.equals("Z34")) {
        validationResource = ValidationResource.IZ_RSP_Z42;
      } else if (profileId.equals("Z23") || messageType.equals("ACK")) {
        validationResource = ValidationResource.IZ_ACK_Z23;
      }
    }
    testCaseMessage.setValidationResource(validationResource);
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
      }
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaProgress[SUITE_H_CONFORMANCE][1] = makeScore(count, rspAnalysisList.size());
    areaScore[SUITE_H_CONFORMANCE][1] = makeScore(pass, rspAnalysisList.size());
  }

  private void analyzeConformance2015Queries() {
    int count = 0;
    int pass = 0;
    for (TestCaseMessage testCaseMessage : rspAnalysisList) {
      count++;
      if (testCaseMessage.isHasRun()) {
        if (testCaseMessage.isValidationReportPass()) {
          pass++;
        }
        // TODO
        areaProgress[SUITE_L_CONFORMANCE_2015][1] = makeScore(count, rspAnalysisList.size());
      }
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaProgress[SUITE_L_CONFORMANCE_2015][1] = makeScore(count, rspAnalysisList.size());
    areaScore[SUITE_L_CONFORMANCE_2015][1] = makeScore(pass, rspAnalysisList.size());
  }

  private void updateAdvanced(Map<Integer, List<Issue>> issueMap) {
    TestRunner testRunner = new TestRunner();
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
    int overallTotal = 0;
    for (int i = 0; i < areaScore[SUITE_C_ADVANCED].length; i++) {
      int priority = i + 1;
      List<Issue> issueList = issueMap.get(priority);
      overallTotal = overallTotal + issueList.size();
    }
    int countPass = 0;
    int count = 0;
    for (int i = 0; i < areaScore[SUITE_C_ADVANCED].length; i++) {
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
      profileTestCaseLists[i] = new ArrayList<TestCaseMessage>();
      int priority = i + 1;
      List<Issue> issueList = issueMap.get(priority);
      if (issueList != null && issueList.size() > 0) {
        for (Issue issue : issueList) {
          count++;

          TestCaseMessage testCaseMessage = createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
          testCaseMessage.setTestCaseSet(testCaseSet);
          testCaseMessage.setTestCaseCategoryId("C.1." + paddWithZeros(count, 3));
          testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
          profileTestCaseLists[i].add(testCaseMessage);
          testCaseMessage.setDescription(issue.getName());
          testCaseMessage.addCauseIssues(issue.getName());
          transformer.transform(testCaseMessage);
          register(testCaseMessage);
          testCaseMessage.setAssertResult("Accept or Reject - *");
          if (testCaseMessage.hasIssue()) {
            try {
              testRunner.runTest(connector, testCaseMessage);
              performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessage);
              testCaseMessage
                  .setPassedTest(!CompareManager.acksAppearToBeTheSame(testCaseMessageBase.getActualResponseMessage(),
                      testCaseMessage.getActualResponseMessage()));
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
          reportProgress(testCaseMessage);
          statusCheckTestCaseAdvancedList.add(testCaseMessage);
          areaProgress[SUITE_C_ADVANCED][0] = makeScore(count, issueList.size());
          if (!keepRunning) {
            status = STATUS_STOPPED;
            reportProgress(null);
            return;
          }
        }
        areaScore[SUITE_C_ADVANCED][0] = makeScore(countPass, overallTotal);
        areaCount[SUITE_C_ADVANCED][0] = overallTotal;
      }
      areaProgress[SUITE_C_ADVANCED][0] = 100;
    }
    reportProgress(null);
  }

  private TestCaseMessage getPresentTestCase(ProfileLine profileLine, int count,
      TestCaseMessage defaultTestCaseMessage) {
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

  private TestCaseMessage getAbsentTestCase(ProfileLine profileLine, int count,
      TestCaseMessage defaultTestCaseMessage) {
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
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
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
              performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessagePresent);
              if (testCaseMessagePresent.isAccepted()) {
                statusCheckTestCaseProfilingList.add(testCaseMessagePresent);
                testCaseMessagePresent.setTestPosition(incrementingInt.next());
                testCaseMessagePresent.setTestType(VALUE_TEST_TYPE_UPDATE);
              }
            }
          }
          profileLine.setTestCaseMessageAbsent(testCaseMessageAbsent);
          if (testCaseMessageAbsent != tcmFull) {
            register(testCaseMessageAbsent);
            testRunner.runTestIfNew(connector, testCaseMessageAbsent, testCaseMessageMap);
            if (testRunner.isWasRun()) {
              performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessageAbsent);
              if (testCaseMessageAbsent.isAccepted()) {
                statusCheckTestCaseProfilingList.add(testCaseMessageAbsent);
                testCaseMessageAbsent.setTestPosition(incrementingInt.next());
                testCaseMessageAbsent.setTestType(VALUE_TEST_TYPE_UPDATE);
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
        reportProgress(testCaseMessagePresent, false, profileLine);
      }
      testCaseMessageAbsent.setHasRun(true);
      if (profileLine.isHasRun()) {
        saveTestCase(testCaseMessageAbsent);
        reportProgress(testCaseMessageAbsent, false, profileLine);
      }
      areaProgress[SUITE_I_PROFILING][0] = makeScore(count, profileLineList.size());
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    if (countTested > 0) {
      areaScore[SUITE_I_PROFILING][0] = makeScore(countPass, countTested);
    }
    areaCount[SUITE_I_PROFILING][0] = profilingRunCount;
    areaProgress[SUITE_I_PROFILING][0] = 100;
    reportProgress(null);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    areaCount[SUITE_A_BASIC][0] = statusCheckTestCaseBasicList.size();
  }

  private void prepareOnc2015() {

    int count = 0;
    for (String scenario : onc2015Scenarios) {
      count++;
      TestCaseMessage testCaseMessage = createTestCaseMessage(scenario);
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("J." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      statusCheckTestCaseOnc2015List.add(testCaseMessage);
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    areaCount[SUITE_J_ONC_2015][0] = statusCheckTestCaseBasicList.size();
  }

  private void prepareTransform() {
    // todo
  }

  private void prepareExtra() {
    // todo
  }

  private void prepareNotAccepted() {
    int count = 0;
    addNotAccepted("PID: Patient identifier segment is missing", "remove segment PID", ++count);
    addNotAccepted("PID-3: Patient id is missing", "CLEAR PID-3", ++count);
    addNotAccepted("PID-5: Patient name is missing", "CLEAR PID-5", ++count);
    addNotAccepted("PID-7: Patient dob is missing", "CLEAR PID-7", ++count);
    addNotAccepted("PID-7: Patient dob is unreadable", "PID-7=DOB", ++count);
    addNotAccepted("PID-7: Patient dob is in the future", "PID-7=[LONG_TIME_FROM_NOW]", ++count);
    addNotAccepted("RXA: RXA segment is missing", "remove segment RXA", ++count);
    addNotAccepted("RXA-3: Vaccination date is missing", "RXA-3=", ++count);
    addNotAccepted("RXA-3: Vaccination date is unreadable", "RXA-3=SHOT DATE", ++count);
    addNotAccepted("RXA-3: Vaccination date set in the future", "RXA-3=[LONG_TIME_FROM_NOW]", ++count);
    addNotAccepted("RXA-5: Vaccination code is missing", "RXA-5=", ++count);
    addNotAccepted("RXA-5: Vaccination code is invalid", "RXA-5=14000BADVALUE", ++count);
    areaCount[SUITE_K_NOT_ACCEPTED][0] = statusCheckTestCaseNotAcceptedList.size();
  }

  private void prepareQuerySupport() {
    int count = 0;
    addQuerySupport("Base Message", null, ++count);
    addQuerySupport("First Twin", "PID-24=Y\rPID-25=1", ++count);
    {
      String middleNameOriginal = "";
      String middleInitial = "";
      String middleName = "";
      String gender = "";
      TestCaseMessage testCaseMessage1 = statusCheckTestCaseQuerySupportList
          .get(statusCheckTestCaseQuerySupportList.size() - 1);
      HL7Reader vxuReader = new HL7Reader(testCaseMessage1.getMessageText());
      vxuReader.advanceToSegment("PID");
      middleNameOriginal = vxuReader.getValue(5, 3);
      if (middleNameOriginal.length() > 0) {
        middleInitial = middleNameOriginal.substring(0, 1);
      } else {
        middleInitial = "";
      }
      gender = vxuReader.getValue(8);
      int tryCount = 0;
      while (middleName.equals("") || !(middleInitial.equals("") || middleName.startsWith(middleInitial))) {
        middleName = transformer.getValue(gender.equals("M") ? "BOY" : "GIRL");
        tryCount++;
        if (tryCount > 1000) {
          // give up already! We'll go with what we have
          break;
        }
      }

      TestCaseMessage testCaseMessage2 = new TestCaseMessage();
      testCaseMessage2.setOriginalMessage(testCaseMessage1.getMessageText());
      testCaseMessage2.setDescription("Second Twin");
      testCaseMessage2.setTestCaseSet(testCaseSet);
      testCaseMessage2.setTestCaseCategoryId("M." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage2.setTestCaseNumber(uniqueMRNBase + testCaseMessage2.getTestCaseCategoryId());
      String uniqueId = testCaseMessage2.getTestCaseNumber();
      testCaseMessage2.appendCustomTransformation(
          "MSH-10=" + uniqueId + "." + Transformer.makeBase62Number(System.currentTimeMillis() % 10000));
      testCaseMessage2.appendCustomTransformation(
          "PID-3.1=" + (uniqueId.length() <= 15 ? uniqueId : uniqueId.substring(uniqueId.length() - 15)));
      testCaseMessage2.appendCustomTransformation("PID-5.3=" + middleName);
      testCaseMessage2.appendCustomTransformation("PID-24=Y");
      testCaseMessage2.appendCustomTransformation("PID-25=2");
      statusCheckTestCaseQuerySupportList.add(testCaseMessage2);
      testCaseMessage2.setTestPosition(incrementingInt.next());
      testCaseMessage2.setTestType(VALUE_TEST_TYPE_UPDATE);
      transformer.transform(testCaseMessage2);
      testCaseMessage2.setAssertResult("Accept - *");
      register(testCaseMessage2);
    }
    areaCount[SUITE_M_QBP_SUPPORT][0] = statusCheckTestCaseQuerySupportList.size();
  }

  public void addNotAccepted(String description, String customTransformation, int count) {
    TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    if (customTransformation != null) {
      testCaseMessage.appendCustomTransformation(customTransformation);
    }
    testCaseMessage.setDescription(description);
    testCaseMessage.setTestCaseSet(testCaseSet);
    testCaseMessage.setTestCaseCategoryId("K." + makeTwoDigits(1) + "." + makeTwoDigits(count));
    testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
    statusCheckTestCaseNotAcceptedList.add(testCaseMessage);
    testCaseMessage.setTestPosition(incrementingInt.next());
    testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
    transformer.transform(testCaseMessage);
    testCaseMessage.setAssertResult("Error - *");
    register(testCaseMessage);
  }

  public void addQuerySupport(String description, String customTransformation, int count) {
    TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    if (customTransformation != null) {
      testCaseMessage.appendCustomTransformation(customTransformation);
    }
    testCaseMessage.setDescription(description);
    testCaseMessage.setTestCaseSet(testCaseSet);
    testCaseMessage.setTestCaseCategoryId("M." + makeTwoDigits(1) + "." + makeTwoDigits(count));
    testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
    statusCheckTestCaseQuerySupportList.add(testCaseMessage);
    testCaseMessage.setTestPosition(incrementingInt.next());
    testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
    transformer.transform(testCaseMessage);
    testCaseMessage.setAssertResult("Accept - *");
    register(testCaseMessage);
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
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
    count = 0;
    int testAccepted = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        testCaseMessage.setMajorChangesMade(!verifyNoMajorChangesMade(testCaseMessage));
        performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessage);
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
      reportProgress(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[SUITE_A_BASIC][0] = makeScore(testPass, statusCheckTestCaseBasicList.size());
    areaProgress[SUITE_A_BASIC][0] = 100;
    reportProgress(null);

    if (testAccepted == 0) {
      logStatus("None of the basic messages were accepted. Stopping test process. ");
      keepRunning = false;
    }
  }

  private void updateOnc2015() {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseOnc2015List) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        testCaseMessage.setMajorChangesMade(!verifyNoMajorChangesMade(testCaseMessage));
        performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessage);
        if (pass) {
          if (!testCaseMessage.isMajorChangesMade()) {
            testPass++;
          }
        }
        testCaseMessage.setErrorList(testRunner.getErrorList());
        printExampleMessage(testCaseMessage, "J ONC 2015");
      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaProgress[SUITE_J_ONC_2015][0] = makeScore(count, statusCheckTestCaseOnc2015List.size());
      saveTestCase(testCaseMessage);
      reportProgress(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[SUITE_J_ONC_2015][0] = makeScore(testPass, statusCheckTestCaseOnc2015List.size());
    areaProgress[SUITE_J_ONC_2015][0] = 100;
    reportProgress(null);
  }

  private void update(String label, List<TestCaseMessage> testCaseMessageList) {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
    count = 0;
    for (TestCaseMessage testCaseMessage : testCaseMessageList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isPassedTest();
        performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessage);
        if (pass) {
          testPass++;
        }
        testCaseMessage.setErrorList(testRunner.getErrorList());
        printExampleMessage(testCaseMessage, label);
      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaProgress[currentSuite][0] = makeScore(count, testCaseMessageList.size());
      saveTestCase(testCaseMessage);
      reportProgress(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[currentSuite][0] = makeScore(testPass, testCaseMessageList.size());
    areaProgress[currentSuite][0] = 100;
    reportProgress(null);
  }

  private void updateQuerySupport() {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseQuerySupportList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isPassedTest();
        performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessage);
        if (pass) {
          testPass++;
        }
        testCaseMessage.setErrorList(testRunner.getErrorList());
        printExampleMessage(testCaseMessage, "M Query Support");
      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaProgress[SUITE_M_QBP_SUPPORT][0] = makeScore(count, statusCheckTestCaseQuerySupportList.size());
      saveTestCase(testCaseMessage);
      reportProgress(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[SUITE_M_QBP_SUPPORT][0] = makeScore(testPass, statusCheckTestCaseQuerySupportList.size());
    areaProgress[SUITE_M_QBP_SUPPORT][0] = 100;
    reportProgress(null);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
              testCaseMessage.setTestPosition(incrementingInt.next());
              testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
                  testCaseMessage.setTestPosition(incrementingInt.next());
                  testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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

          statusCheckTestCaseForecastPrepList.add(testCaseMessage);
          testCaseMessage.setTestPosition(incrementingInt.next());
          testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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

  private static String somewhatRandomLongString(int length) {
    String s = "";
    int count = 0;
    while (s.length() < length) {
      long r = System.currentTimeMillis();
      r = (r << count) - r;
      s = s + r;
      count++;
    }
    return s.substring(0, length);
  }

  private void prepareExceptional() {

    int count = 100;
    count = createToleranceCheck("MSH-10 Message Control Id is very long", "MSH-10=" + somewhatRandomLongString(198),
        count);
    count = createToleranceCheck("MSH-300 is empty with bars all the way out", "MSH-300=1\nMSH-300=", count);
    count = createToleranceCheck("MSH-300 set to a value of 1", "MSH-300=1", count);
    count = createToleranceCheck("PID-3.4 Patient Id Assigning Authority is very long",
        "PID-3.4=" + somewhatRandomLongString(226), count);
    count = createToleranceCheck("PID-3.5 Patient Id is PI instead of MR", "PID-3.5=PI", count);
    count = createToleranceCheck("PID-7 Date/Time of Birth includes a time", "PID-7=20131003113759-0700", count);
    count = createToleranceCheck("PID-10 Race contains invalid race",
        "PID-10.1=KLINGON\nPID-10.2=Klingon\nPID-10.3=CDCREC", count);
    count = createToleranceCheck("PID-22 Ethnicity contains invalid ethnicity",
        "PID-22.1=KLINGON\nPID-22.2=Klingon\nPID-22.3=CDCREC", count);
    count = createToleranceCheck("PID-300 is empty with bars all the way out", "PID-300=1\nPID-300=", count);
    count = createToleranceCheck("PID-300 set to a value of 1", "PID-300=1", count);
    count = createToleranceCheck("PV1-10 Hospital service code is set to a non-standard value", "PV1-10=AMB", count);
    count = createToleranceCheck("Message has no vaccinations",
        "remove segment ORC all\nremove segment RXA all\nremove segment RXR all\nremove segment OBX all", count);
    count = createToleranceCheck("RXA-3 Date/Time Start of Administration includes time", "RXA-3=[NOW]", count);
    count = createToleranceCheck("RXA-4 Date/Time End of Administration is empty", "RXA-4=", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped field separator in it",
        "RXA-5.2=This \\F\\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped component in it", "RXA-5.2=This \\S\\ That",
        count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped sub component separator in it",
        "RXA-5.2=This \\T\\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped repetition separator in it",
        "RXA-5.2=This \\R\\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped escape separator in it",
        "RXA-5.2=This \\E\\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as unescaped escape separator in it",
        "RXA-5.2=This \\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label is set to a very long value",
        "RXA-5.2=This is a very long description for a vaccine, that normally you shouldn't expect to see, but since this field should not be read by the receiver it should be accepted, HL7 allows up to 199 chars. ",
        count);
    count = createToleranceCheck("RXA-17.2 Manufacturer Label is set to a very long value",
        "RXA-17.2=This is a very long description for a manufac, that normally you shouldn't expect to see, but since this field should not be read by the receiver it should be accepted, HL7 allows up to 199 chars. ",
        count);
    count = createToleranceCheck("RXA-17.2 Manufacturer Label includes an un-encoded ampersand", "RXA-17.2=Merk & Co",
        count);
    count = createToleranceCheck("RXA-300 is empty with bars all the way out", "RXA-300=1\nRXA-300=", count);
    count = createToleranceCheck("RXA-300 set to a value of 1", "RXA-300=1", count);

    {
      count++;
      TestCaseMessage testCaseMessage1 = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage1.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Same message to be sent again");
      testCaseMessage1.setTestCaseSet(testCaseSet);
      testCaseMessage1.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage1.setTestCaseNumber(uniqueMRNBase + testCaseMessage1.getTestCaseCategoryId());
      statusCheckTestCaseExceptionalList.add(testCaseMessage1);
      testCaseMessage1.setTestPosition(incrementingInt.next());
      testCaseMessage1.setTestType(VALUE_TEST_TYPE_UPDATE);
      transformer.transform(testCaseMessage1);
      testCaseMessage1.setAssertResult("Accept - *");
      register(testCaseMessage1);

      count++;
      TestCaseMessage testCaseMessage2 = new TestCaseMessage();
      testCaseMessage2.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Same message as before");
      testCaseMessage2.setMessageText(testCaseMessage1.getMessageText());
      testCaseMessage2.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage2.setTestCaseNumber(uniqueMRNBase + testCaseMessage2.getTestCaseCategoryId());
      statusCheckTestCaseExceptionalList.add(testCaseMessage2);
      testCaseMessage2.setTestPosition(incrementingInt.next());
      testCaseMessage2.setTestType(VALUE_TEST_TYPE_UPDATE);
      testCaseMessage2.setAssertResult("Accept - *");
      register(testCaseMessage2);
    }

    count = 200;
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendOriginalMessage("OBX|5|NM|6287-7^Baker's yeast IgE Ab in Serum^LN||1945||||||F\n");
      testCaseMessage.setDescription(
          VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Message includes observation not typically sent to IIS");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendOriginalMessage("YES|This|is|a|segment^you^should^never^see|in|production\n");
      testCaseMessage
          .setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Message includes segment not defined by HL7");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
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
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Future Functionality Check: NDC and CVX code");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-5.1=00006-4047-20");
      testCaseMessage.appendCustomTransformation("RXA-5.2=RotaTeq");
      testCaseMessage.appendCustomTransformation("RXA-5.3=NDC");
      testCaseMessage.appendCustomTransformation("RXA-5.4=116");
      testCaseMessage.appendCustomTransformation("RXA-5.5=rotavirus, pentavalent");
      testCaseMessage.appendCustomTransformation("RXA-5.6=CVX");
      testCaseMessage.appendCustomTransformation("RXA-17.1=MSD");
      testCaseMessage.appendCustomTransformation("RXA-17.2=Merck and Co., Inc.");
      testCaseMessage.appendCustomTransformation("RXA-17.3=MVX");
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Future Functionality Check: CVX and NDC code");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-5.1=116");
      testCaseMessage.appendCustomTransformation("RXA-5.2=rotavirus, pentavalent");
      testCaseMessage.appendCustomTransformation("RXA-5.3=CVX");
      testCaseMessage.appendCustomTransformation("RXA-5.4=00006-4047-20");
      testCaseMessage.appendCustomTransformation("RXA-5.5=RotaTeq");
      testCaseMessage.appendCustomTransformation("RXA-5.6=NDC");
      testCaseMessage.appendCustomTransformation("RXA-17.1=MSD");
      testCaseMessage.appendCustomTransformation("RXA-17.2=Merck and Co., Inc.");
      testCaseMessage.appendCustomTransformation("RXA-17.3=MVX");
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Future Functionality Check: NDC only (no CVX)");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
      testCaseMessage.appendCustomTransformation("RXA-5.1=00006-4047-20");
      testCaseMessage.appendCustomTransformation("RXA-5.2=RotaTeq");
      testCaseMessage.appendCustomTransformation("RXA-5.3=NDC");
      testCaseMessage.appendCustomTransformation("RXA-17.1=MSD");
      testCaseMessage.appendCustomTransformation("RXA-17.2=Merck and Co., Inc.");
      testCaseMessage.appendCustomTransformation("RXA-17.3=MVX");
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      testCaseMessage.setTestPosition(incrementingInt.next());
      testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }
    // TODO add examples of message construction issues that should be ignored

    try {
      count = 0;
      BufferedReader in = new BufferedReader(
          new InputStreamReader(getClass().getResourceAsStream("exampleCertifiedMessages.txt")));
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

  public int createToleranceCheck(String label, String customTransformation, int count) {
    count++;
    TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    testCaseMessage.setAdditionalTransformations(customTransformation + "\n");
    testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " " + label);
    testCaseMessage.setTestCaseSet(testCaseSet);
    testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(1) + "." + makeTwoDigits(count));
    testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
    statusCheckTestCaseExceptionalList.add(testCaseMessage);
    testCaseMessage.setTestPosition(incrementingInt.next());
    testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
    transformer.transform(testCaseMessage);
    testCaseMessage.setAssertResult("Accept - *");
    register(testCaseMessage);
    return count;
  }

  private int createCertfiedMessageTestCaseMessage(Transformer transformer, int count, StringBuilder sb,
      String previousDescription) {
    count++;

    String messageText = sb.toString();

    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(messageText);

    changePatientIdentifyingInformation(messageText, testCaseMessage);

    testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_CERTIFIED_MESSAGE + " "
        + (previousDescription == null ? "Unidentified EHR" : previousDescription));
    testCaseMessage.setTestCaseSet(testCaseSet);
    testCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(2) + "." + makeTwoDigits(count));
    testCaseMessage.setTestCaseNumber(uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
    statusCheckTestCaseExceptionalList.add(testCaseMessage);
    transformer.transform(testCaseMessage);
    testCaseMessage.setAssertResult("Accept - *");
    testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
    testCaseMessage.setTestPosition(incrementingInt.next());
    register(testCaseMessage);
    return count;

  }

  private void changePatientIdentifyingInformation(String messageText, TestCaseMessage testCaseMessage) {
    HL7Reader hl7Reader = new HL7Reader(messageText);
    testCaseMessage
        .appendCustomTransformation("MSH-10=" + messageText.hashCode() + "." + (System.currentTimeMillis() % 10000));
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
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessage);
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
      reportProgress(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[SUITE_B_INTERMEDIATE][0] = makeScore(testPass, statusCheckTestCaseIntermediateList.size());
    reportProgress(null);
  }

  private void updateExceptional() {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessage);
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
      reportProgress(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[SUITE_D_EXCEPTIONAL][0] = makeScore(testPass, statusCheckTestCaseExceptionalList.size());
    reportProgress(null);
  }

  private void updateForecastPrep() {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    testRunner.setValidateResponse(run[SUITE_L_CONFORMANCE_2015]);
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseForecastPrepList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessage);
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
      reportProgress(testCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
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
      setDerivedFrom(testCaseMessage, queryTestCaseMessage);
      queryTestCaseMessage.setDescription("Query for " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("A." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseBasicList.add(queryTestCaseMessage);
      queryTestCaseMessage.setTestPosition(incrementingInt.next());
      queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
      register(queryTestCaseMessage);
      try {
        long startTime = System.currentTimeMillis();
        String message = prepareSendQueryMessage(queryTestCaseMessage);
        queryTestCaseMessage.setMessageTextSent(message);
        String rspMessage = doSafeQuery(message);
        performance.addTotalQueryTime(System.currentTimeMillis() - startTime, queryTestCaseMessage);
        queryTestCaseMessage.setHasRun(true);
        queryTestCaseMessage.setActualResponseMessage(rspMessage);
        setQueryReturnedMostImportantData(queryTestCaseMessage);
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
      reportProgress(queryTestCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[SUITE_A_BASIC][1] = makeScore(testQueryPassRequired, testQueryCountRequired);
    areaScore[SUITE_A_BASIC][2] = makeScore(testQueryPassOptional, testQueryCountOptional);
    areaCount[SUITE_A_BASIC][1] = testQueryCountRequired;
    areaCount[SUITE_A_BASIC][2] = testQueryCountOptional;
    reportProgress(null);
  }

  private void queryOnc2015() {
    int count;
    int testQueryPassRequired = 0;
    int testQueryPassOptional = 0;
    int testQueryCountRequired = 0;
    int testQueryCountOptional = 0;
    TestCaseMessage testCaseMessagePrevious = null;
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseOnc2015List) {
      count++;
      if (testCaseMessage.getScenario().equals(SCENARIO_ONC_2015_IZ_AD_3_R)
          || testCaseMessage.getScenario().equals(SCENARIO_ONC_2015_IZ_AD_5_R)) {
        testCaseMessagePrevious = testCaseMessage;
        continue;
      }
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      if (testCaseMessage.getScenario().equals(SCENARIO_ONC_2015_IZ_AD_4_R)
          || testCaseMessage.getScenario().equals(SCENARIO_ONC_2015_IZ_AD_6_R)) {
        queryTestCaseMessage
            .setDerivedFromVXUMessage(testCaseMessagePrevious.getMessageText() + testCaseMessage.getMessageText());
        queryTestCaseMessage.setOriginalMessageResponse(
            testCaseMessagePrevious.getActualResponseMessage() + testCaseMessage.getActualResponseMessage());
        queryTestCaseMessage.setOriginalAccepted(testCaseMessagePrevious.isAccepted() && testCaseMessage.isAccepted());
      } else {
        setDerivedFrom(testCaseMessage, queryTestCaseMessage);
      }
      queryTestCaseMessage.setDescription("Query for " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("J." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseBasicList.add(queryTestCaseMessage);
      queryTestCaseMessage.setTestPosition(incrementingInt.next());
      queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
      register(queryTestCaseMessage);
      try {
        long startTime = System.currentTimeMillis();
        String message = prepareSendQueryMessage(queryTestCaseMessage);
        queryTestCaseMessage.setMessageTextSent(message);
        String rspMessage = doSafeQuery(message);
        performance.addTotalQueryTime(System.currentTimeMillis() - startTime, queryTestCaseMessage);
        queryTestCaseMessage.setHasRun(true);
        queryTestCaseMessage.setActualResponseMessage(rspMessage);
        setQueryReturnedMostImportantData(queryTestCaseMessage);
        List<Comparison> comparisonList = CompareManager
            .compareMessages(queryTestCaseMessage.getDerivedFromVXUMessage(), rspMessage);
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
      areaProgress[SUITE_J_ONC_2015][2] = makeScore(count, statusCheckTestCaseOnc2015List.size());
      areaProgress[SUITE_J_ONC_2015][1] = areaProgress[SUITE_J_ONC_2015][2];
      saveTestCase(queryTestCaseMessage);
      reportProgress(queryTestCaseMessage);
      statusCheckQueryTestCaseOnc2015List.add(queryTestCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[SUITE_J_ONC_2015][1] = makeScore(testQueryPassRequired, testQueryCountRequired);
    areaScore[SUITE_J_ONC_2015][2] = makeScore(testQueryPassOptional, testQueryCountOptional);
    areaCount[SUITE_J_ONC_2015][1] = testQueryCountRequired;
    areaCount[SUITE_J_ONC_2015][2] = testQueryCountOptional;
    reportProgress(null);
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

  private void query(List<TestCaseMessage> updateList, List<TestCaseMessage> queryList, String testCasePrefix) {
    int count;
    count = 0;
    for (TestCaseMessage testCaseMessage : updateList) {
      count++;
      String vxuMessage = testCaseMessage.getMessageText();

      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      setDerivedFrom(testCaseMessage, queryTestCaseMessage);
      queryTestCaseMessage.setDescription("Query for " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId(testCasePrefix + "." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      queryList.add(queryTestCaseMessage);
      queryTestCaseMessage.setTestPosition(incrementingInt.next());
      queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
      register(queryTestCaseMessage);
      try {
        long startTime = System.currentTimeMillis();
        String message = prepareSendQueryMessage(queryTestCaseMessage);
        queryTestCaseMessage.setMessageTextSent(message);
        String rspMessage = doSafeQuery(message);
        performance.addTotalQueryTime(System.currentTimeMillis() - startTime, queryTestCaseMessage);
        queryTestCaseMessage.setHasRun(true);
        queryTestCaseMessage.setActualResponseMessage(rspMessage);
        setQueryReturnedMostImportantData(queryTestCaseMessage);
        List<Comparison> comparisonList = CompareManager.compareMessages(vxuMessage, rspMessage);
        queryTestCaseMessage.setComparisonList(comparisonList);
      } catch (Throwable t) {
        queryTestCaseMessage.setException(t);
      }
      areaProgress[currentSuite][1] = makeScore(count, updateList.size());
      areaProgress[currentSuite][2] = areaProgress[currentSuite][1];
      saveTestCase(queryTestCaseMessage);
      reportProgress(queryTestCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[currentSuite][1] = makeScore(count, updateList.size());
    areaScore[currentSuite][2] = areaScore[currentSuite][1];
    areaCount[currentSuite][1] = count;
    areaCount[currentSuite][2] = count;
    reportProgress(null);
  }

  private void queryQbpSupport() {

    int passCount = 0;
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseQuerySupportList) {
      count++;
      {
        TestCaseMessage queryTestCaseMessage = runQuery("Expecting Z32 Complete Immunization History", count,
            testCaseMessage, false, false);

        queryTestCaseMessage.setPassedTest(false);
        HL7Reader rspReader = new HL7Reader(queryTestCaseMessage.getActualResponseMessage());
        if (rspReader.advanceToSegment("MSH")) {
          String messageType = rspReader.getValue(9, 1);
          String profileId = rspReader.getValue(21, 1);
          if (messageType.equals("RSP") && profileId.equals("Z32")) {
            if (rspReader.advanceToSegment("QAK")) {
              if (rspReader.getValue(2).equals("OK")) {
                if (queryTestCaseMessage.getResultStoreStatus()
                    .equals(RecordServletInterface.VALUE_RESULT_ACK_STORE_STATUS_ACCEPTED_RETURNED)) {
                  passCount++;
                  queryTestCaseMessage.setPassedTest(true);
                }
              }
            }
          }
        }
      }
      count++;
      {
        TestCaseMessage queryTestCaseMessage = runQuery("Expecting Z42 Evaluated History and Forecast", count,
            testCaseMessage, true, false);
        queryTestCaseMessage.setPassedTest(false);
        HL7Reader rspReader = new HL7Reader(queryTestCaseMessage.getActualResponseMessage());
        if (rspReader.advanceToSegment("MSH")) {
          String messageType = rspReader.getValue(9, 1);
          String profileId = rspReader.getValue(21, 1);
          if (messageType.equals("RSP") && profileId.equals("Z42")) {
            if (rspReader.advanceToSegment("QAK")) {
              if (rspReader.getValue(2).equals("OK")) {
                if (queryTestCaseMessage.getResultStoreStatus()
                    .equals(RecordServletInterface.VALUE_RESULT_ACK_STORE_STATUS_ACCEPTED_RETURNED)) {
                  passCount++;
                  queryTestCaseMessage.setPassedTest(true);
                }
              }
            }
          }
        }
      }

      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
      if (testCaseMessage.getDescription().equals("First Twin")) {
        count++;
        {
          TestCaseMessage queryTestCaseMessage = runQuery("Expecting Z31 List of Candidates", count, testCaseMessage,
              false, true);
          queryTestCaseMessage.setPassedTest(false);
          HL7Reader rspReader = new HL7Reader(queryTestCaseMessage.getActualResponseMessage());
          if (rspReader.advanceToSegment("MSH")) {
            String messageType = rspReader.getValue(9, 1);
            String profileId = rspReader.getValue(21, 1);
            if (messageType.equals("RSP") && profileId.equals("Z31")) {
              queryTestCaseMessage.setPassedTest(true);
              passCount++;
            }
          }
        }
        count++;
        {
          TestCaseMessage queryTestCaseMessage = runQuery("Expecting Z33 Too Many", count, testCaseMessage, true, true);
          queryTestCaseMessage.setPassedTest(false);
          HL7Reader rspReader = new HL7Reader(queryTestCaseMessage.getActualResponseMessage());
          if (rspReader.advanceToSegment("MSH")) {
            String messageType = rspReader.getValue(9, 1);
            String profileId = rspReader.getValue(21, 1);
            if (messageType.equals("RSP") && profileId.equals("Z33")) {
              if (rspReader.advanceToSegment("QAK")) {
                if (rspReader.getValue(2).equals("TM")) {
                  queryTestCaseMessage.setPassedTest(true);
                  passCount++;
                }
              }
            }
          }
        }
      }
    }
    {
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      transformer.transform(testCaseMessage);
      testCaseMessage.setDescription("Unsubmitted Patient");

      count++;
      // Z42 - return evaluated history and forecast
      // Z33 - Return an ack with no person records
      // Z31 - Return a list of candidates profile
      // Z32 - Return complete immunization history
      {
        TestCaseMessage queryTestCaseMessage = runQuery("Expecting Z33 Not Found", count, testCaseMessage, false,
            false);
        queryTestCaseMessage.setPassedTest(false);
        HL7Reader rspReader = new HL7Reader(queryTestCaseMessage.getActualResponseMessage());
        if (rspReader.advanceToSegment("MSH")) {
          String messageType = rspReader.getValue(9, 1);
          String profileId = rspReader.getValue(21, 1);
          if (messageType.equals("RSP") && profileId.equals("Z33")) {
            if (rspReader.advanceToSegment("QAK")) {
              if (rspReader.getValue(2).equals("NF")) {
                queryTestCaseMessage.setPassedTest(true);
                passCount++;
              }
            }
          }
        }
      }
      count++;
      {
        TestCaseMessage queryTestCaseMessage = runQuery("Expecting Z33 Not Found", count, testCaseMessage, true, false);
        queryTestCaseMessage.setPassedTest(false);
        HL7Reader rspReader = new HL7Reader(queryTestCaseMessage.getActualResponseMessage());
        if (rspReader.advanceToSegment("MSH")) {
          String messageType = rspReader.getValue(9, 1);
          String profileId = rspReader.getValue(21, 1);
          if (messageType.equals("RSP") && profileId.equals("Z33")) {
            if (rspReader.advanceToSegment("QAK")) {
              if (rspReader.getValue(2).equals("NF")) {
                queryTestCaseMessage.setPassedTest(true);
                passCount++;
              }
            }
          }
        }
      }
    }

    areaScore[SUITE_M_QBP_SUPPORT][1] = makeScore(passCount, count);
    areaScore[SUITE_M_QBP_SUPPORT][2] = areaScore[SUITE_M_QBP_SUPPORT][1];
    areaCount[SUITE_M_QBP_SUPPORT][1] = count;
    areaCount[SUITE_M_QBP_SUPPORT][2] = count;
    reportProgress(null);
  }

  public TestCaseMessage runQuery(String description, int count, TestCaseMessage testCaseMessage, boolean z44,
      boolean removeMultipleBirth) {
    String vxuMessage = testCaseMessage.getMessageText();
    TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
    setDerivedFrom(testCaseMessage, queryTestCaseMessage);
    queryTestCaseMessage.setDescription(description + " for " + testCaseMessage.getDescription());
    if (z44) {
      queryTestCaseMessage.setMessageText(QueryConverter.convertVXUtoQBPZ44(testCaseMessage.getMessageText()));
    } else {
      queryTestCaseMessage.setMessageText(QueryConverter.convertVXUtoQBP(testCaseMessage.getMessageText()));
    }
    queryTestCaseMessage.setOriginalMessage(queryTestCaseMessage.getMessageText());
    queryTestCaseMessage.setTestCaseSet(testCaseSet);
    queryTestCaseMessage.setTestCaseCategoryId("M." + makeTwoDigits(3) + "." + makeTwoDigits(count));
    queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
    statusCheckQueryTestCaseBasicList.add(queryTestCaseMessage);
    queryTestCaseMessage.setTestPosition(incrementingInt.next());
    queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
    if (removeMultipleBirth) {
      queryTestCaseMessage.appendCustomTransformation("QPD-4.3=");
      queryTestCaseMessage.appendCustomTransformation("clear QPD-10");
      queryTestCaseMessage.appendCustomTransformation("clear QPD-11");
      transformer.transform(queryTestCaseMessage);
    }
    register(queryTestCaseMessage);
    try {
      long startTime = System.currentTimeMillis();
      String message = prepareSendQueryMessage(queryTestCaseMessage);
      queryTestCaseMessage.setMessageTextSent(message);
      String rspMessage = doSafeQuery(message);
      performance.addTotalQueryTime(System.currentTimeMillis() - startTime, queryTestCaseMessage);
      queryTestCaseMessage.setHasRun(true);
      queryTestCaseMessage.setActualResponseMessage(rspMessage);
      setQueryReturnedMostImportantData(queryTestCaseMessage);
      List<Comparison> comparisonList = CompareManager.compareMessages(vxuMessage, rspMessage);
      queryTestCaseMessage.setComparisonList(comparisonList);
    } catch (Throwable t) {
      queryTestCaseMessage.setException(t);
    }
    areaProgress[SUITE_M_QBP_SUPPORT][1] = makeScore(count, statusCheckTestCaseQuerySupportList.size() * 2 + 4);
    areaProgress[SUITE_M_QBP_SUPPORT][2] = areaProgress[SUITE_M_QBP_SUPPORT][1];
    saveTestCase(queryTestCaseMessage);
    reportProgress(queryTestCaseMessage);
    statusCheckQueryTestQuerySupportList.add(queryTestCaseMessage);
    return queryTestCaseMessage;
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
        response = responseReader.getOriginalSegment()
            + "\r[IIS Tester: Does not appear to be exact match, result redacted]";
      }

    } else {
      response = "[IIS Tester: Unrecognized response message, result redacted]";
    }
    return response;
  }

  public String convertToQuery(TestCaseMessage testCaseMessage) {
    if (queryType.equals(QUERY_TYPE_QBP)) {
      return QueryConverter.convertVXUtoQBP(testCaseMessage.getMessageText());
    }
    if (queryType.equals(QUERY_TYPE_VXQ)) {
      return QueryConverter.convertVXUtoVXQ(testCaseMessage.getMessageText());
    }
    throw new IllegalArgumentException(
        "Unable to convert query because query type '" + queryType + "' is not recognized");
  }

  private String prepareSendQueryMessage(TestCaseMessage queryTestCaseMessage) {
    return Transformer.transform(queryConnector, queryTestCaseMessage);
  }

  private void prepareQueryIntermediate() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      setDerivedFrom(testCaseMessage, queryTestCaseMessage);
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("BQ." + makeTwoDigits(1) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseIntermediateList.add(queryTestCaseMessage);
      queryTestCaseMessage.setTestPosition(incrementingInt.next());
      queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
      register(queryTestCaseMessage);
    }
  }

  private void prepareQueryExeptional() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      setDerivedFrom(testCaseMessage, queryTestCaseMessage);
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("D." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseTolerantList.add(queryTestCaseMessage);
      queryTestCaseMessage.setTestPosition(incrementingInt.next());
      queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
      register(queryTestCaseMessage);
    }
  }

  private void prepareQueryProfiling() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseProfilingList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      setDerivedFrom(testCaseMessage, queryTestCaseMessage);
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("I." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseProfilingList.add(queryTestCaseMessage);
      queryTestCaseMessage.setTestPosition(incrementingInt.next());
      queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
      register(queryTestCaseMessage);
    }
  }

  private void prepareQueryAdvanced() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseAdvancedList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      setDerivedFrom(testCaseMessage, queryTestCaseMessage);
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("C." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      statusCheckQueryTestCaseProfilingList.add(queryTestCaseMessage);
      queryTestCaseMessage.setTestPosition(incrementingInt.next());
      queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
      register(queryTestCaseMessage);
    }
  }

  private void prepareQueryForecastPrep() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseForecastPrepList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      setDerivedFrom(testCaseMessage, queryTestCaseMessage);
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(convertToQuery(testCaseMessage));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseCategoryId("E." + makeTwoDigits(3) + "." + makeTwoDigits(count));
      queryTestCaseMessage.setTestCaseNumber(uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
      queryTestCaseMessage.setForecastTestCase(testCaseMessage.getForecastTestCase());
      statusCheckQueryTestCaseForecastPrepList.add(queryTestCaseMessage);
      queryTestCaseMessage.setTestPosition(incrementingInt.next());
      queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
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
        queryTestCaseMessage.setMessageTextSent(message);
        String rspMessage = doSafeQuery(message);
        performance.addTotalQueryTime(System.currentTimeMillis() - startTime, queryTestCaseMessage);
        queryTestCaseMessage.setHasRun(true);
        queryTestCaseMessage.setActualResponseMessage(rspMessage);
        setQueryReturnedMostImportantData(queryTestCaseMessage);
        List<Comparison> comparisonList = CompareManager
            .compareMessages(queryTestCaseMessage.getDerivedFromVXUMessage(), rspMessage);
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
                  File testCaseFile = new File(testDir,
                      "TC-" + queryTestCaseMessage.getTestCaseNumber() + ".forecast-results.txt");
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
      reportProgress(queryTestCaseMessage);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaProgress[suite][1] = 100;
    areaProgress[suite][2] = 100;
    areaScore[suite][1] = makeScore(testQueryPassRequired, testQueryCountRequired);
    areaScore[suite][2] = makeScore(testQueryPassOptional, testQueryCountOptional);
    areaCount[suite][1] = testQueryCountRequired;
    areaCount[suite][2] = testQueryCountOptional;
    reportProgress(null);
  }

  private void saveTestCase(TestCaseMessage tcm) {
    if (testDir != null) {
      CreateTestCaseServlet.saveTestCaseHtml(tcm, testDir);
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
    out.println("    <th></th>");
    out.println("    <th>Level 1</th>");
    out.println("    <th>Level 2</th>");
    out.println("    <th>Level 3</th>");
    out.println("  </tr>");
    if (run[SUITE_A_BASIC]) {
      out.println("  <tr>");
      out.println(
          "    <th><font size=\"+1\">Basic</font><br/><font size=\"-2\">IIS can accept updates from EHR</font></th>");
      if (areaScore[SUITE_A_BASIC][0] >= 100) {
        out.println(
            "    <td class=\"pass\">All NIST 2014 scenarios are accepted. <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
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
        out.println(
            "    <td class=\"pass\">All required IIS core fields are supported.  <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_A_BASIC][1] >= 0) {
        out.println(
            "    <td class=\"fail\">Not all required IIS core fields are supported. (" + areaScore[SUITE_A_BASIC][1]
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
      out.println("  </tr>");
    }
    if (run[SUITE_J_ONC_2015]) {
      out.println("  <tr>");
      out.println(
          "    <th><font size=\"+1\">ONC 2015</font><br/><font size=\"-2\">IIS can accept updates from EHR certified by ONC 2015</font></th>");
      if (areaScore[SUITE_J_ONC_2015][0] >= 100) {
        out.println(
            "    <td class=\"pass\">All ONC 2015 scenarios are accepted. <font size=\"-1\"><a href=\"#areaJLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_J_ONC_2015][0] >= 0) {
        out.println("    <td class=\"fail\">Not all ONC 2015 scenarios are accepted. (" + areaScore[SUITE_J_ONC_2015][0]
            + "% accepted) <font size=\"-1\"><a href=\"#areaJLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_J_ONC_2015][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_J_ONC_2015][0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_J_ONC_2015][1] >= 100) {
        out.println(
            "    <td class=\"pass\">All required IIS core fields are supported.  <font size=\"-1\"><a href=\"#areaJLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_J_ONC_2015][1] >= 0) {
        out.println(
            "    <td class=\"fail\">Not all required IIS core fields are supported. (" + areaScore[SUITE_J_ONC_2015][1]
                + "% of tests passed) <font size=\"-1\"><a href=\"#areaJLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_J_ONC_2015][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_J_ONC_2015][1] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      if (areaScore[SUITE_J_ONC_2015][2] >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core data were returned when queried. "
            + "<font size=\"-1\"><a href=\"#areaJLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_J_ONC_2015][2] >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core data were returned when queried ("
            + areaScore[SUITE_J_ONC_2015][2]
            + "% of fields returned) <font size=\"-1\"><a href=\"#areaJLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_J_ONC_2015][2] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_J_ONC_2015][2] + "% complete</td>");
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
    if (run[SUITE_K_NOT_ACCEPTED]) {
      out.println("  <tr>");
      out.println(
          "    <th><font size=\"+1\">Bad Messages</font><br/><font size=\"-2\">IIS can identify clearly bad messages</font></th>");
      if (areaScore[SUITE_K_NOT_ACCEPTED][0] >= 100) {
        out.println(
            "    <td class=\"pass\">All bad messages NOT accepted <font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_K_NOT_ACCEPTED][0] >= 0) {
        out.println("    <td class=\"fail\">Messages that did not return error (" + areaScore[SUITE_K_NOT_ACCEPTED][0]
            + "% no error) <font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_K_NOT_ACCEPTED][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_K_NOT_ACCEPTED][0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_K_NOT_ACCEPTED][1] >= 100) {
        out.println(
            "    <td class=\"pass\">Queries performed.  <font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_K_NOT_ACCEPTED][1] >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields are supported. ("
            + areaScore[SUITE_K_NOT_ACCEPTED][1]
            + "% of tests passed) <font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_K_NOT_ACCEPTED][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_K_NOT_ACCEPTED][1] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      if (areaScore[SUITE_K_NOT_ACCEPTED][2] >= 100) {
        out.println("    <td class=\"pass\">Queries performed. "
            + "<font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_K_NOT_ACCEPTED][2] >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core data were returned when queried ("
            + areaScore[SUITE_K_NOT_ACCEPTED][2]
            + "% of fields returned) <font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_K_NOT_ACCEPTED][2] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_K_NOT_ACCEPTED][2] + "% complete</td>");
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
    if (run[SUITE_B_INTERMEDIATE]) {
      out.println("  <tr>");
      out.println(
          "    <th><font size=\"+1\">Intermediate</font><br/><font size=\"-2\">IIS can recognize valid codes</font></th>");
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
      out.println(
          "    <th><font size=\"+1\">Advanced</font><br/><font size=\"-2\">IIS can identify quality issues</font></th>");
      if (areaScore[SUITE_C_ADVANCED][0] >= 100) {
        out.println("    <td class=\"pass\">All high priority issues were identified. "
            + "<font size=\"-1\"><a href=\"#areaCLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_C_ADVANCED][0] >= 0) {
        out.println(
            "    <td class=\"fail\">Not all high priority issues were identified. (" + areaScore[SUITE_C_ADVANCED][0]
                + "% of issues identified) " + "<font size=\"-1\"><a href=\"#areaCLevel1\">(details)</a></font></td>");
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
        out.println(
            "    <td class=\"fail\">Not all medium priority issues were identified. (" + areaScore[SUITE_C_ADVANCED][1]
                + "% issues identified) " + "<font size=\"-1\"><a href=\"#areaCLevel2\">(details)</a></font></td>");
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
      out.println(
          "    <th><font size=\"+1\">Profiling</font><br/><font size=\"-2\">IIS meets own requirements</font></th>");
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
        out.println(
            "    <td class=\"pass\">All required IIS core fields were returned from messages that were accepted. "
                + "<font size=\"-1\"><a href=\"#areaILevel2\">(details)</a></font></td>");
      } else if (areaScore[SUITE_I_PROFILING][1] >= 0) {
        out.println(
            "    <td class=\"fail\">Not all required IIS core fields were returned from messages that were accepted. ("
                + areaScore[SUITE_I_PROFILING][1] + "% core fields returned) "
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
        out.println(
            "    <td class=\"pass\">All required and optional IIS core fields were returned from messages that were accepted. "
                + "<font size=\"-1\"><a href=\"#areaILevel3\">(details)</a></font></td>");
      } else if (areaScore[SUITE_I_PROFILING][2] >= 0) {
        out.println(
            "    <td class=\"fail\">Not all required or optional IIS core fields were returned from messages that were accepted. ("
                + areaScore[SUITE_I_PROFILING][2] + "% required and optional were returned) "
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
      out.println(
          "    <th><font size=\"+1\">Exceptional</font><br/><font size=\"-2\">IIS can allow for minor differences</font></th>");
      if (areaScore[SUITE_D_EXCEPTIONAL][0] >= 100) {
        out.println("    <td class=\"pass\">All messages accepted as-is. "
            + "<font size=\"-1\"><a href=\"#areaDLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_D_EXCEPTIONAL][0] >= 0) {
        out.println(
            "    <td class=\"fail\">Some messages were not accepted as-is. (" + areaScore[SUITE_D_EXCEPTIONAL][0]
                + "% messages accepted) " + "<font size=\"-1\"><a href=\"#areaDLevel1\">(details)</a></font></td>");
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
      out.println(
          "    <th><font size=\"+1\">Forecast Prep</font><br/><font size=\"-2\">IIS can accept shot histories</font></th>");
      if (areaScore[SUITE_E_FORECAST_PREP][0] >= 100) {
        out.println("    <td class=\"pass\">All shot histories were accepted. "
            + "<font size=\"-1\"><a href=\"#areaELevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_E_FORECAST_PREP][0] >= 0) {
        out.println(
            "    <td class=\"fail\">Some shot histories were NOT accepted. (" + areaScore[SUITE_E_FORECAST_PREP][0]
                + "% messages accepted) " + "<font size=\"-1\"><a href=\"#areaELevel1\">(details)</a></font></td>");
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
    if (run[SUITE_M_QBP_SUPPORT]) {
      out.println("  <tr>");
      out.println(
          "    <th><font size=\"+1\">QBP Support</font><br/><font size=\"-2\">IIS can support QBP requests</font></th>");
      if (areaScore[SUITE_M_QBP_SUPPORT][0] >= 100) {
        out.println(
            "    <td class=\"pass\">All preparatory messages accepted <font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_M_QBP_SUPPORT][0] >= 0) {
        out.println("    <td class=\"fail\">Messages that were not accepted (" + areaScore[SUITE_M_QBP_SUPPORT][0]
            + "% no error) <font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_M_QBP_SUPPORT][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_M_QBP_SUPPORT][0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaScore[SUITE_M_QBP_SUPPORT][1] >= 100) {
        out.println(
            "    <td class=\"pass\">Results as expected.  <font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_M_QBP_SUPPORT][1] >= 0) {
        out.println("    <td class=\"fail\">Unexpected results. (" + areaScore[SUITE_M_QBP_SUPPORT][1]
            + "% of tests passed) <font size=\"-1\"><a href=\"#areaKLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_M_QBP_SUPPORT][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_M_QBP_SUPPORT][1] + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      out.println("    <td>not defined</td>");
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
      out.println(
          "    <th><font size=\"+1\">Conformance</font><br/><font size=\"-2\">IIS can respond correctly to requests</font></th>");
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
    if (run[SUITE_L_CONFORMANCE_2015]) {
      out.println("  <tr>");
      out.println(
          "    <th><font size=\"+1\">Conformance 2015</font><br/><font size=\"-2\">IIS can respond correctly to requests</font></th>");
      if (areaScore[SUITE_L_CONFORMANCE_2015][0] >= 100) {
        out.println("    <td class=\"pass\">All acknowledgement (ACK) responses meet ONC 2015 standards. "
            + "<font size=\"-1\"><a href=\"#areaHLevel1\">(details)</a></font></td>");
      } else if (areaScore[SUITE_L_CONFORMANCE_2015][0] >= 0) {
        out.println("    <td class=\"fail\">Not all acknowledgement (ACK) responses meet ONC 2015 standards. ("
            + areaScore[SUITE_L_CONFORMANCE_2015][0] + "% of messages met standard) "
            + "<font size=\"-1\"><a href=\"#areaHLevel1\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_L_CONFORMANCE_2015][0] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_L_CONFORMANCE_2015][0] + "% complete</td>");
        } else {
          out.println("    <td>not analyzed yet</td>");
        }
      }
      if (areaScore[SUITE_L_CONFORMANCE_2015][1] >= 100) {
        out.println("    <td class=\"pass\">All query response (RSP) messages met ONC 2015 standards.  "
            + "<font size=\"-1\"><a href=\"#areaHLevel2\">(details)</a></font></td>");
      } else if (areaScore[SUITE_L_CONFORMANCE_2015][1] >= 0) {
        out.println("    <td class=\"fail\">Not all query response (RSP) messages ONC 2015 standards. ("
            + areaScore[SUITE_L_CONFORMANCE_2015][1] + "% of fields returned) "
            + "<font size=\"-1\"><a href=\"#areaHLevel2\">(details)</a></font></td>");
      } else {
        if (areaProgress[SUITE_L_CONFORMANCE_2015][1] > -1) {
          out.println("    <td>running now ... <br/>" + areaProgress[SUITE_L_CONFORMANCE_2015][1] + "% complete</td>");
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
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
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
          for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
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
        for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
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
        for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
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
      out.println("<h2>Performance</h2>");
      if (areaScore[SUITE_G_PERFORMANCE][0] < 3000) {
        out.println("    <p>Response time was as fast enough: " + printSeconds(performance.getUpdateAverage())
            + " average processing time per test message. </p>");
      } else {
        out.println("    <p>Response time was as slower than anticipated: "
            + printSeconds(performance.getUpdateAverage()) + " average processing time per test message. </p>");
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
      out.println("<p><a href=\"" + REPORT_EXPLANATION_URL
          + "#acknowledgmentConformance\" class=\"boxLinks\">Description</a></p>");

      if (areaProgress[SUITE_I_PROFILING][0] > 0) {
        out.println("<div id=\"localRequirements\"/>");
        out.println("<h2>Local Requirement Implementation</h2>");

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
    out.println("    <th>Conformance 2015 Tests</th>");
    out.println("    <td>" + (run[SUITE_L_CONFORMANCE_2015] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>ONC 2015 Tests</th>");
    out.println("    <td>" + (run[SUITE_J_ONC_2015] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Not Accepted Tests</th>");
    out.println("    <td>" + (run[SUITE_K_NOT_ACCEPTED] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Query Support Tests</th>");
    out.println("    <td>" + (run[SUITE_M_QBP_SUPPORT] ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Update Test Count</th>");
    out.println("    <td>" + performance.getTotalUpdateCount() + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Query Test Count</th>");
    out.println("    <td>" + performance.getTotalQueryCount() + "</td>");
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
      out.println(
          "<p><b>Requirements</b>: IIS must accept all 7 NIST messages than an EHR is required to be able to send in 2014. "
              + "In addition the IIS should be able to store all IIS required core fields, and if possible all the "
              + "IIS optional core fields. </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: The IIS must be able to accept replicas of all NIST messages. </li>");
      out.println(
          "  <li>Level 2: The IIS should return all required 2007 IIS Core Data included in the NIST messages.</li>");
      out.println(
          "  <li>Level 3: The IIS may return all IIS 2013-2017 Core Data that was included in the NIST messages.</li>");
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
                    out.println(
                        "      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel() + "</li>");
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
                    out.println(
                        "      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel() + "</li>");
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
                    out.println(
                        "      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel() + "</li>");
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

    if (areaProgress[SUITE_J_ONC_2015][0] > 0) {
      out.println("<h2>ONC 2015 Interoperability Tests</h2>");
      out.println(
          "<p><b>Purpose</b>: Test to see if the IIS can accept updates from ONC 2015 certified EHR systems. </p>");
      out.println(
          "<p><b>Requirements</b>: IIS must accept all 7 NIST messages than an EHR is required to be able to send after 2015. "
              + "In addition the IIS should be able to store all IIS required core fields, and if possible all the "
              + "IIS optional core fields. </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: The IIS must be able to accept replicas of all NIST messages. </li>");
      out.println(
          "  <li>Level 2: The IIS should return all required 2007 IIS Core Data included in the NIST messages.</li>");
      out.println(
          "  <li>Level 3: The IIS may return all IIS 2013-2017 Core Data that was included in the NIST messages.</li>");
      out.println("</ul>");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseOnc2015List) {
        testNum++;
        out.println("<div id=\"areaJLevel1\"/>");
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>ONC 2015 Test #" + testNum + "</th>");
        out.println("    <td><em>" + testCaseMessage.getDescription() + "</em></td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <th>Level 1</th>");
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
        if (statusCheckQueryTestCaseOnc2015List.size() >= testNum) {
          testCaseMessage = statusCheckQueryTestCaseOnc2015List.get(testNum - 1);
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
                    out.println(
                        "      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel() + "</li>");
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
                    out.println(
                        "      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel() + "</li>");
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
                    out.println(
                        "      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel() + "</li>");
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

    if (areaProgress[SUITE_K_NOT_ACCEPTED][0] > 0) {
      out.println("<h2>Not Accepted Tests</h2>");
      out.println(
          "<p><b>Purpose</b>: Test to see if the IIS can generate error responses for messages that clearly have problems. </p>");
      out.println(
          "<p><b>Requirements</b>: IIS should indicate to the sender when major problems occur in a message.  </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: The IIS must be able to indicate that a message was not accepted. </li>");
      out.println("  <li>Level 2: The IIS might return required 2007 IIS Core Data.</li>");
      out.println("  <li>Level 3: The IIS might return all IIS 2013-2017 Core Data.</li>");
      out.println("</ul>");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseNotAcceptedList) {
        testNum++;
        out.println("<div id=\"areaKLevel1\"/>");
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>ONC 2015 Test #" + testNum + "</th>");
        out.println("    <td><em>" + testCaseMessage.getDescription() + "</em></td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <th>Level 1</th>");
        if (testCaseMessage.isPassedTest()) {
          out.println("    <td class=\"pass\">Error returned. "
              + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
        } else if (testCaseMessage.isHasRun()) {
          out.println("    <td class=\"fail\">");
          out.println("Error not returned. " + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
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
        if (statusCheckQueryTestCaseNotAcceptedList.size() >= testNum) {
          testCaseMessage = statusCheckQueryTestCaseNotAcceptedList.get(testNum - 1);
          out.println("  <tr>");
          out.println("    <th>Level 2</th>");
          {
            boolean hasRun = false;
            if (hasRun) {
              out.println("    <td class=\"pass\">Response returned. "
                  + makeCompareDetailsLink(testCaseMessage, toFile, false));
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
        }
        out.println("</table>");
        out.println("</br>");
      }
    }

    if (areaProgress[SUITE_B_INTERMEDIATE][0] > 0) {

      out.println("<h2>Interoperability Tests</h2>");
      out.println("<p><b>Purpose</b>: Test to see if the IIS can accept recognize valid codes. </p>");
      out.println("<p><b>Requirements</b>: IIS must accept all valid codes for IIS core required and "
          + "optional fields, and not reject messages because of invalid or unrecognized codes in optional "
          + "fields. In addition the IIS should be able to store all IIS required core fields, and "
          + "if possible all the IIS optional core fields. </p>");
      out.println("<ul>");
      out.println(
          "  <li>Level 1: The IIS must be able to accept all valid codes, defined by the current CDC Implementation Guide. </li>");
      out.println(
          "  <li>Level 2: The IIS should return all required 2007 IIS Core Data that was submitted for Level 1 testing. </li>");
      out.println(
          "  <li>Level 3: The IIS may return all IIS 2013-2017 Core Data that was submitted for Level 1 testing.</li>");
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
      if (profileTestCaseLists[i] == null || areaProgress[SUITE_C_ADVANCED][0] < 100) {
        continue;
      }

      if (i == 0) {
        out.println("<h2>Advanced Interoperability Tests</h2>");
        out.println("<p><b>Purpose</b>: Test to see if the IIS can identify quality issues. </p>");
        out.println("<p><b>Requirements</b>: IIS must be able to identify critical data quality issues in messages. "
            + "These include items that were documented and detailed as part of the 2008 Data Quality Assurance for IIS: "
            + "Incoming Data project. </p>");
        out.println("<ul>");
        out.println(
            "  <li>Level 1: The IIS must be able to identify all Level 1 priority issues. For each issue the ACK message returned should indicate that the IIS recognized the quality issue. The identified severity of the issue is not evaluated for this test.  </li>");
        out.println(
            "  <li>Level 2: The IIS should be able to identify all Level 2 priority issues. For each issue the ACK message returned should indicate that the IIS recognized the quality issue. The identified severity of the issue is not evaluated for this test.</li>");
        out.println(
            "  <li>Level 3: The IIS may be able to identify all Level 3 priority issues. For each issue the ACK message returned should indicate that the IIS recognized the quality issue. The identified severity of the issue is not evaluated for this test.</li>");
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
      out.println(
          "<p><b>Requirements</b>: IIS should implement interface consistent with its own stated requirements. </p>");
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
                && profileLine.getTestCaseMessageAbsent() != null
                && profileLine.getTestCaseMessageAbsent().hasIssue())) {
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
          printProfileLine(out, toFile, profileLine, true);
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
      out.println(
          "  <li>Level 1: The IIS must be able to accept update messages with incorrect data in fields that are not critical to HL7 message format and are not used to transmit IIS core data; be able to accept example update messages from EHR systems that pass EHR certification; be able to accept sample update messages that transmit IIS core data that is defined by the IIS Implementation Guide but were not tested by EHR Certification.</li>");
      out.println(
          "  <li>Level 2: The IIS should return all required 2007 IIS Core Data that was submitted for Level 1 testing.</li>");
      out.println(
          "  <li>Level 3: The IIS may return all IIS 2013-2017 Core Data that was submitted for Level 1 testing. </li>");
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
      out.println(
          "<p><b>Purpose</b>: Test to submit patient histories to IIS in preparation for retrieving forecasts. </p>");
      out.println("<p><b>Requirements</b>: IIS should be able to accept and store vaccination histories. </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: The IIS must be able to accept vaccination histories. </li>");
      out.println(
          "  <li>Level 2: The IIS should return all required 2007 IIS Core Data that was submitted for Level 1 testing. </li>");
      out.println(
          "  <li>Level 3: The IIS may return all IIS 2013-2017 Core Data that was submitted for Level 1 testing.</li>");
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

    if (areaProgress[SUITE_M_QBP_SUPPORT][0] > 0) {
      out.println("<h2>Query Support Tests</h2>");
      out.println("<p><b>Purpose</b>: Test to see if the IIS can respond to QBP queries.  </p>");
      out.println("<p><b>Requirements</b>: IIS should respond to QBP queries. </p>");
      out.println("<ul>");
      out.println("  <li>Level 1: The IIS must accept data that will be later queried.  </li>");
      out.println("  <li>Level 2: The IIS should respond to queries.</li>");
      out.println("</ul>");
      out.println("<div id=\"areaKLevel1\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseQuerySupportList) {
        testNum++;
        out.println("  <tr>");
        out.println("    <td><em>" + testCaseMessage.getDescription() + "</em></td>");
        if (testCaseMessage.isPassedTest()) {
          out.println(
              "    <td class=\"pass\">Accepted. " + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
        } else if (testCaseMessage.isHasRun()) {
          out.println("    <td class=\"fail\">");
          out.println("Not accepted. " + makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
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
      }
      out.println("</table>");
      out.println("</br>");
      if (areaProgress[SUITE_M_QBP_SUPPORT][1] > 0) {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        for (TestCaseMessage testCaseMessage : statusCheckQueryTestQuerySupportList) {
          out.println("  <tr>");
          out.println("    <td><em>" + testCaseMessage.getDescription() + "</em></td>");
          {
            if (testCaseMessage.isPassedTest()) {
              out.println("    <td class=\"pass\">Expected response returned. "
                  + makeCompareDetailsLink(testCaseMessage, toFile, false));
              out.println(makeTestCaseMessageDetailsLink(testCaseMessage, toFile));
              out.println("    </td>");
            } else if (testCaseMessage.isHasRun()) {
              out.println("    <td class=\"fail\">Unexpected response returned. "
                  + makeCompareDetailsLink(testCaseMessage, toFile, false));
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
        }
        out.println("</table>");
        out.println("</br>");
      }

    }
    if (areaProgress[SUITE_H_CONFORMANCE][0] > 0) {
      out.println("<h2>Conformance Tests</h2>");
      out.println("<p><b>Purpose</b>: Test to see if the IIS can respond correctly to requests. </p>");
      out.println("<p><b>Requirements</b>: IIS must be able to return an ACK message that meets HL7 and CDC Standards "
          + "for format and structure. IIS should also be able to return RSP messages in response to "
          + "QBP messages that meet HL7 and CDC Standards for format and structure. </p>");
      out.println("<ul>");
      out.println(
          "  <li>Level 1: That IIS can return an acknowledgement (ACK) messages that meet HL7 and CDC standards for format and content. Return messages correctly indicate if messages were rejected or accepted according to the CDC standard.</li>");
      out.println(
          "  <li>Level 2: IIS supports QDP/RSP and RSP message meets HL7 and CDC standards for format and content. IIS must be able to return a single record match for a patient submitted by VXU when the patient is queried using the same information supplied in the VXU. The query results should also include a forecast recommendation.</li>");
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

    if (areaProgress[SUITE_L_CONFORMANCE_2015][0] > 0) {
      out.println("<h2>Conformance 2015 Tests</h2>");
      out.println("<p><b>Purpose</b>: Test to see if the IIS can respond correctly to requests. </p>");
      out.println("<p><b>Requirements</b>: IIS must be able to return an ACK message that meets ONC 2015 Standards "
          + "for format and structure. IIS should also be able to return RSP messages in response to "
          + "QBP messages that meet HL7 and CDC Standards for format and structure. </p>");
      out.println("<ul>");
      out.println(
          "  <li>Level 1: That IIS can return an acknowledgement (ACK) messages that meet HL7 and CDC standards for format and content. Return messages correctly indicate if messages were rejected or accepted according to the CDC standard.</li>");
      out.println(
          "  <li>Level 2: IIS supports QDP/RSP and RSP message meets HL7 and CDC standards for format and content. IIS must be able to return a single record match for a patient submitted by VXU when the patient is queried using the same information supplied in the VXU. The query results should also include a forecast recommendation.</li>");
      out.println("</ul>");

      out.println("<div id=\"areaHLevel1\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Conformance Tests - Level 1 - ACK Incorrectly Formatted</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : ackAnalysisList) {
        // HL7Component actualResponseMessageComponent =
        // TestCaseMessageManager.createHL7Component(testCaseMessage);
        if (testCaseMessage.getValidationResource() == null) {
          out.println("  <tr>");
          out.println("    <td class=\"fail\"><em>" + testCaseMessage.getDescription() + "</em></td>");
          out.println("    <td class=\"fail\">Response not recognized "
              + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
          out.println("  </tr>");
        } else if (testCaseMessage.getValidationReport() == null) {
          out.println("  <tr>");
          out.println("    <td class=\"fail\"><em>" + testCaseMessage.getDescription() + "</em></td>");
          out.println("    <td class=\"fail\">Unable to check HL7 message for conformance "
              + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
          out.println("  </tr>");
        } else if (testCaseMessage.isValidationReportPass()) {
          out.println("  <tr>");
          out.println("    <td class=\"fail\"><em>" + testCaseMessage.getDescription() + "</em></td>");
          out.println("    <td class=\"fail\">Message did not meet ONC 2015 standards. "
              + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
          out.println("  </tr>");
        }
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

  private static class Performance
  {
    private long totalQueryTime = 0;
    private long totalUpdateTime = 0;
    private int totalQueryCount = 0;
    private int totalUpdateCount = 0;
    private long minQueryTime = Long.MAX_VALUE;
    private long minUpdateTime = Long.MAX_VALUE;
    private long maxQueryTime = 0;
    private long maxUpdateTime = 0;
    private TestCaseMessage minQueryTestCase = null;
    private TestCaseMessage minUpdateTestCase = null;
    private TestCaseMessage maxQueryTestCase = null;
    private TestCaseMessage maxUpdateTestCase = null;
    private List<Long> queryTimeList = new ArrayList<Long>();
    private List<Long> updateTimeList = new ArrayList<Long>();

    public long getTotalQueryTime() {
      return totalQueryTime;
    }

    public long getTotalUpdateTime() {
      return totalUpdateTime;
    }

    public double getQuerySDev() {
      if (queryTimeList.size() == 0) {
        return 0;
      }
      long averageQuery = getQueryAverage();
      double variance = 0;
      for (Long queryTime : queryTimeList) {
        double v = Math.pow(queryTime - averageQuery, 2);
        variance += v;
      }
      return Math.sqrt(variance / queryTimeList.size());
    }

    public long getQueryAverage() {
      long averageQuery = totalQueryTime / totalQueryCount;
      return averageQuery;
    }

    public double getUpdateSDev() {
      if (updateTimeList.size() == 0) {
        return 0;
      }
      long averageUpdate = getUpdateAverage();
      double variance = 0;
      for (Long updateTime : updateTimeList) {
        double v = Math.pow(updateTime - averageUpdate, 2);
        variance += v;
      }
      return Math.sqrt(variance / updateTimeList.size());
    }

    public long getUpdateAverage() {
      long averageUpdate = totalUpdateTime / totalUpdateCount;
      return averageUpdate;
    }

    public void addTotalQueryTime(long queryTime, TestCaseMessage tcm) {
      queryTimeList.add(queryTime);
      totalQueryTime += queryTime;
      totalQueryCount++;
      if (queryTime < minQueryTime) {
        minQueryTime = queryTime;
        minQueryTestCase = tcm;
      }
      if (queryTime > maxQueryTime) {
        maxQueryTime = queryTime;
        maxQueryTestCase = tcm;
      }
    }

    public void addTotalUpdateTime(long updateTime, TestCaseMessage tcm) {
      updateTimeList.add(updateTime);
      totalUpdateTime += updateTime;
      totalUpdateCount++;
      if (updateTime < minUpdateTime) {
        minUpdateTime = updateTime;
        minUpdateTestCase = tcm;
      }
      if (updateTime > maxUpdateTime) {
        maxUpdateTime = updateTime;
        maxUpdateTestCase = tcm;
      }
    }

    public long getMinQueryTime() {
      return minQueryTime;
    }

    public long getMinUpdateTime() {
      return minUpdateTime;
    }

    public long getMaxQueryTime() {
      return maxQueryTime;
    }

    public long getMaxUpdateTime() {
      return maxUpdateTime;
    }

    public TestCaseMessage getMinQueryTestCase() {
      return minQueryTestCase;
    }

    public TestCaseMessage getMinUpdateTestCase() {
      return minUpdateTestCase;
    }

    public TestCaseMessage getMaxQueryTestCase() {
      return maxQueryTestCase;
    }

    public TestCaseMessage getMaxUpdateTestCase() {
      return maxUpdateTestCase;
    }

    public int getTotalQueryCount() {
      return totalQueryCount;
    }

    public int getTotalUpdateCount() {
      return totalUpdateCount;
    }
  }

  private static enum PerformanceColumn {
    CONNECTION("Connection"), TRANSPORT("Transport"), ACK_TYPE("Ack Type"), STARTED("Started"), FINISHED(
        "Finished"), TEST_STATUS("Test Status"), UPDATE_COUNT("Update Count"), AVERAGE_UPDATE(
            "Average Update (ms)"), STDEV_UPDATE("Std Dev Update"), MIN_UPDATE("Min Update (ms)"), MAX_UPDATE(
                "Max Update (ms)"), MIN_UPDATE_TEST_CASE("Min Update Test Case"), MAX_UPDATE_TEST_CASE(
                    "Max Update Test Case"), QUERY_COUNT("Query Count"), AVERAGE_QUERY(
                        "Average Query (ms)"), STDEV_QUERY("Std Dev Query"), MIN_QUERY("Min Query (ms)"), MAX_QUERY(
                            "Max Query (ms)"), MIN_QUERY_TEST_CASE("Min Query Test Case"), MAX_QUERY_TEST_CASE(
                                "Max Query Test Case"), BASIC_TEST("Basic Tests"), INTERMEDIATE_TEST(
                                    "Intermediate Tests"), ADVANCED_TEST("Advanced Tests"), PROFILING_TEST(
                                        "Profiling Tests"), EXCEPTIONAL_TEST("Exceptional Tests"), FORECAST_PREP(
                                            "Forecast Prep Tests"), PERFORMANCE("Performance Tests"), CONFORMANCE(
                                                "Conformance Tests"), ONC_2015("ONC 2015");
    String label;

    private PerformanceColumn(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
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

  private void reportProgress(TestCaseMessage testMessage) {
    reportProgress(testMessage, false, null);
  }

  private void reportProgress(TestCaseMessage testMessage, boolean firstTime, ProfileLine profileLine) {
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
      boolean latestTest = status.equals(STATUS_COMPLETED) && areaProgress[SUITE_A_BASIC][0] == 100
          && areaProgress[SUITE_B_INTERMEDIATE][0] == 100 && areaProgress[SUITE_D_EXCEPTIONAL][0] == 100
          && areaProgress[SUITE_I_PROFILING][0] == 100 && areaProgress[SUITE_H_CONFORMANCE][0] == 100;
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

      addField(sb, PARAM_TS_TEST_SECTION_TYPE, areaLabel[currentSuite]);
      addField(sb, PARAM_TS_TEST_ENABLED, run[currentSuite]);
      addField(sb, PARAM_TS_SCORE_LEVEL1, areaScore[currentSuite][0]);
      addField(sb, PARAM_TS_SCORE_LEVEL2, areaScore[currentSuite][1]);
      addField(sb, PARAM_TS_SCORE_LEVEL3, areaScore[currentSuite][1]);
      addField(sb, PARAM_TS_PROGRESS_LEVEL1, areaProgress[currentSuite][0]);
      addField(sb, PARAM_TS_PROGRESS_LEVEL2, areaProgress[currentSuite][1]);
      addField(sb, PARAM_TS_PROGRESS_LEVEL3, areaProgress[currentSuite][2]);
      addField(sb, PARAM_TS_COUNT_LEVEL1, areaCount[currentSuite][0]);
      addField(sb, PARAM_TS_COUNT_LEVEL2, areaCount[currentSuite][1]);
      addField(sb, PARAM_TS_COUNT_LEVEL3, areaCount[currentSuite][2]);
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
        addField(sb, PARAM_TM_RESULT_ACK_STORE_STATUS, testMessage.getResultStoreStatus());
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
