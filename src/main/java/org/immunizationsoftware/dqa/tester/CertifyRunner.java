package org.immunizationsoftware.dqa.tester;

import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;
import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.SCENARIO_2_R_ADMIN_ADULT;
import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.SCENARIO_3_R_HISTORICAL_CHILD;
import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.SCENARIO_4_R_CONSENTED_CHILD;
import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.SCENARIO_5_R_REFUSED_TODDLER;
import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.SCENARIO_6_R_VARICELLA_HISTORY_CHILD;
import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.SCENARIO_7_R_COMPLETE_RECORD;
import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.SCENARIO_ADD_DELETE;
import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.createTestCaseMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.manager.HL7Analyzer;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.QueryConverter;
import org.immunizationsoftware.dqa.tester.manager.ScenarioManager;
import org.immunizationsoftware.dqa.tester.run.TestRunner;
import org.immunizationsoftware.dqa.tester.transform.Issue;

public class CertifyRunner extends Thread
{
  private static final String QUERY_TYPE_QBP = "Q";
  private static final String QUERY_TYPE_VXQ = "V";
  private static final String QUERY_TYPE_NONE = "N";


  
  public static final String STATUS_INITIALIZED = "Initialized";
  public static final String STATUS_STARTED = "Started";
  public static final String STATUS_COMPLETED = "Completed";
  public static final String STATUS_STOPPED = "Stopped";

  private String status = "";
  private String statusMessage = "";
  private Throwable exception = null;
  private boolean keepRunning = true;
  private TestCaseMessage testCaseMessageBase = null;

  private boolean runB = false;
  private boolean runC = false;
  private boolean runD = false;
  private boolean runF = false;

  public boolean isRunB() {
    return runB;
  }

  public void setRunB(boolean runB) {
    this.runB = runB;
  }

  public boolean isRunC() {
    return runC;
  }

  public void setRunC(boolean runC) {
    this.runC = runC;
  }

  public boolean isRunD() {
    return runD;
  }

  public void setRunD(boolean runD) {
    this.runD = runD;
  }

  public boolean isRunF() {
    return runF;
  }

  public void setRunF(boolean runF) {
    this.runF = runF;
  }

  private int areaALevel1Score = -1;
  private int areaALevel2Score = -1;
  private int areaALevel3Score = -1;

  private int areaBLevel1Score = -1;
  private int areaBLevel2Score = -1;
  private int areaBLevel3Score = -1;

  private int[] areaCLevelScore = new int[3];
  {
    areaCLevelScore[0] = -1;
    areaCLevelScore[1] = -1;
    areaCLevelScore[2] = -1;
  }

  private int areaDLevel1Score = -1;
  private int areaDLevel2Score = -1;
  private int areaDLevel3Score = -1;

  private int areaELevel1Score = -1;
  private int areaELevel2Score = -1;
  private int areaELevel3Score = -1;

  private int areaFLevel1Score = -1;
  private int areaFLevel2Score = -1;
  private int areaFLevel3Score = -1;

  private int areaALevel1Progress = -1;
  private int areaALevel2Progress = -1;
  private int areaALevel3Progress = -1;

  private int areaBLevel1Progress = -1;
  private int areaBLevel2Progress = -1;
  private int areaBLevel3Progress = -1;

  private int[] areaCLevelProgress = new int[3];
  {
    areaCLevelProgress[0] = -1;
    areaCLevelProgress[1] = -1;
    areaCLevelProgress[2] = -1;
  }

  private int areaDLevel1Progress = -1;
  private int areaDLevel2Progress = -1;
  private int areaDLevel3Progress = -1;

  private int areaELevel1Progress = -1;
  private int areaELevel2Progress = -1;
  private int areaELevel3Progress = -1;

  private int areaFLevel1Progress = -1;
  private int areaFLevel2Progress = -1;
  private int areaFLevel3Progress = -1;

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

  private List<TestCaseMessage> statusCheckTestCaseList = new ArrayList<TestCaseMessage>();

  private List<TestCaseMessage> statusCheckTestCaseBasicList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseIntermediateList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckTestCaseExceptionalList = new ArrayList<TestCaseMessage>();

  private List<TestCaseMessage> statusCheckQueryTestCaseBasicList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseIntermediateList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> statusCheckQueryTestCaseTolerantList = new ArrayList<TestCaseMessage>();

  private List<TestCaseMessage> ackAnalysisList = new ArrayList<TestCaseMessage>();
  private List<TestCaseMessage> rspAnalysisList = new ArrayList<TestCaseMessage>();

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
      SCENARIO_3_R_HISTORICAL_CHILD, SCENARIO_4_R_CONSENTED_CHILD, SCENARIO_5_R_REFUSED_TODDLER,
      SCENARIO_6_R_VARICELLA_HISTORY_CHILD, SCENARIO_7_R_COMPLETE_RECORD };

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
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
    testCaseSet = CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX + " " + sdf.format(new Date());

  }

  @Override
  public void run() {
    status = STATUS_STARTED;
    try {
      willQuery = queryType != null && (queryType.equals(QUERY_TYPE_QBP) || queryType.equals(QUERY_TYPE_VXQ));

      testStarted = new Date();

      statusMessage = "Preparing basic messages";
      prepareBasic();

      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      statusMessage = "Updating basic messages";
      updateBasic();
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
      if (runB) {

        statusMessage = "Preparing intermediate";
        prepareIntermediate();

        statusMessage = "Updating intermediate";
        updateIntermediate();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }

        if (runC) {
          if (testCaseMessageBase != null) {
            Map<Integer, List<Issue>> issueMap = new HashMap<Integer, List<Issue>>();

            statusMessage = "Preparing advanced";
            prepareAdvanced(issueMap);
            if (!keepRunning) {
              status = STATUS_STOPPED;
              return;
            }

            statusMessage = "Updating advanced";
            updateAdvanced(issueMap);
            if (!keepRunning) {
              status = STATUS_STOPPED;
              return;
            }
          }
        }
      }
      if (runD) {
        statusMessage = "Preparing exceptional";
        prepareExceptional();

        statusMessage = "Updating exceptional";
        updateExceptional();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }

      if (totalUpdateCount > 0) {
        areaELevel1Score = (int) totalUpdateTime / totalUpdateCount;
        areaELevel1Progress = 100;
      }
      if (runF) {

        statusMessage = "Prepare format update analysis";
        prepareFormatUpdateAnalysis();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }

        statusMessage = "Analyze format updates";
        analyzeFormatUpdates();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }

      if (willQuery) {

        queryBasic();
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }

        if (runB) {

          statusMessage = "Prepare for query for intermediate";
          prepareQueryIntermediate();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }

          statusMessage = "Query for intermediate";
          queryIntermediate();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
        }

        if (runD) {
          statusMessage = "Query exceptional";
          queryExceptional();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
        }

        if (totalQueryCount > 0) {
          areaELevel2Score = (int) totalQueryTime / totalQueryCount;
          areaELevel2Progress = 100;
        }

        if (runF) {
          statusMessage = "Prepare for format query analysis";
          prepareFormatQueryAnalysis();
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }

          statusMessage = "Analyze format of queries";
          analyzeFormatQueries();
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }

      areaFLevel3Progress = 100;
      testFinished = new Date();

      if (testCaseMessageBase != null) {
        File testCaseDir = CreateTestCaseServlet.getTestCaseDir(testCaseMessageBase, session);
        if (testCaseDir != null) {
          statusMessage = "Writing out final report";
          File certificationReportFile = new File(testCaseDir, "IIS Testing Report.html");
          try {
            PrintWriter out = new PrintWriter(new FileWriter(certificationReportFile));
            String title = "IIS Testing Report";
            ClientServlet.printHtmlHeadForFile(out, title);
            out.println("    <h3>IIS Testing Results for " + connector.getLabel() + "</h3>");
            printResults(out);
            out.println("    <h3>IIS Testing Details</h3>");
            printProgressDetails(out, true);
            ClientServlet.printHtmlFootForFile(out);
            out.close();
          } catch (IOException ioe) {
            ioe.printStackTrace();
            // unable to save, continue as normal
          }
        }
      }
    }

    catch (Throwable t) {
      t.printStackTrace();
      exception = t;
      status = STATUS_STOPPED;
    } finally {
      if (status != STATUS_STOPPED) {
        status = STATUS_COMPLETED;
      }
      statusMessage = null;
    }
  }

  private void prepareFormatUpdateAnalysis() {
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseList) {
      if (testCaseMessage.isHasRun()) {
        ackAnalysisList.add(testCaseMessage);
      }
    }
  }

  private void prepareFormatQueryAnalysis() {
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseList) {
      if (testCaseMessage.getDerivedFromVXUMessage() != null) {
        rspAnalysisList.add(testCaseMessage);
      }
    }
  }

  private void analyzeFormatUpdates() {
    int count = 0;
    int pass = 0;
    for (TestCaseMessage testCaseMessage : ackAnalysisList) {
      count++;
      if (testCaseMessage.isHasRun()) {
        List<String> il = new ArrayList<String>();
        HL7Reader ackReader = new HL7Reader(testCaseMessage.getActualResponseMessage());
        HL7Reader vxuReader = new HL7Reader(testCaseMessage.getMessageText());
        HL7Analyzer analyzer = new HL7Analyzer(il, ackReader, vxuReader);
        if (analyzer.analyzeAck()) {
          pass++;
        }
        testCaseMessage.setHl7Analyzer(analyzer);
        areaFLevel1Progress = makeScore(count, ackAnalysisList.size());
      }
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaFLevel1Progress = makeScore(count, ackAnalysisList.size());
    areaFLevel1Score = makeScore(pass, ackAnalysisList.size());
  }

  private void analyzeFormatQueries() {
    int count = 0;
    int pass = 0;
    for (TestCaseMessage testCaseMessage : rspAnalysisList) {
      count++;
      if (testCaseMessage.isHasRun()) {
        List<String> il = new ArrayList<String>();
        HL7Reader ackReader = new HL7Reader(testCaseMessage.getActualResponseMessage());
        HL7Reader vxuReader = new HL7Reader(testCaseMessage.getMessageText());
        HL7Analyzer analyzer = new HL7Analyzer(il, ackReader, vxuReader);
        if (analyzer.analyzeRsp()) {
          pass++;
        }
        testCaseMessage.setHl7Analyzer(analyzer);
        areaFLevel2Progress = makeScore(count, rspAnalysisList.size());
      }
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaFLevel2Progress = makeScore(count, rspAnalysisList.size());
    areaFLevel2Score = makeScore(pass, rspAnalysisList.size());
  }

  private void updateAdvanced(Map<Integer, List<Issue>> issueMap) {
    TestRunner testRunner = new TestRunner();
    Transformer transformer = new Transformer();
    int count;
    for (int i = 0; i < areaCLevelScore.length; i++) {
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
          TestCaseMessage testCaseMessage = new TestCaseMessage(testCaseMessageBase);
          testCaseMessage.setTestCaseSet(testCaseSet);
          testCaseMessage.setTestCaseNumber("C" + priority + "." + paddWithZeros(count, 3));
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
            } catch (Throwable t) {
              testCaseMessage.setException(t);
            }
          }
          testCaseMessage.setHasRun(true);
          CreateTestCaseServlet.saveTestCase(testCaseMessage, session);
          areaCLevelProgress[i] = makeScore(count, issueList.size());
          if (!keepRunning) {
            status = STATUS_STOPPED;
            return;
          }
        }
        areaCLevelScore[i] = makeScore(countPass, issueList.size());
      }

    }
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
    Transformer transformer = new Transformer();
    int count = 0;
    for (String scenario : statusCheckScenarios) {
      count++;
      TestCaseMessage testCaseMessage = createTestCaseMessage(scenario);
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("A1." + count);
      statusCheckTestCaseBasicList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

  }

  private void updateBasic() {
    int count;
    int testPass = 0;
    TestRunner testRunner = new TestRunner();
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
      count++;
      try {
        testRunner.runTest(connector, testCaseMessage);
        boolean pass = testCaseMessage.isAccepted();
        testCaseMessage.setMajorChangesMade(!verifyNoMajorChangesMade(testCaseMessage));
        totalUpdateCount++;
        totalUpdateTime += testRunner.getTotalRunTime();
        if (pass && !testCaseMessage.isMajorChangesMade()) {
          testPass++;
          testCaseMessageBase = testCaseMessage;
        } else {
          testCaseMessageBase = null;
        }
        testCaseMessage.setErrorList(testRunner.getErrorList());

      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaALevel1Progress = makeScore(count, statusCheckTestCaseBasicList.size());
      CreateTestCaseServlet.saveTestCase(testCaseMessage, session);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaALevel1Score = makeScore(testPass, statusCheckTestCaseBasicList.size());
  }

  private boolean verifyNoMajorChangesMade(TestCaseMessage testCaseMessage) {
    boolean noMajorChangesMade = true;
    List<CompareManager.Comparison> comparisonList = CompareManager.compareMessages(testCaseMessage.getMessageText(),
        testCaseMessage.getMessageTextSent());
    for (CompareManager.Comparison comparison : comparisonList) {
      if (comparison.isTested() && comparison.getPriorityLevel() <= CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL) {

        if (!comparison.isPass()) {
          noMajorChangesMade = false;
          break;
        }
      }
    }
    return noMajorChangesMade;
  }

  private void prepareIntermediate() {

    Transformer transformer = new Transformer();
    int count;
    List<Certify.CertifyItem> certifyItems;
    Certify certify = new Certify();

    certifyItems = certify.getCertifyItemList(Certify.FIELD_SEX);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Sex is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B01." + count);
      testCaseMessage.appendCustomTransformation("PID-8=" + certifyItem.getCode());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_RACE);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Race is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B02." + count);
      testCaseMessage.appendCustomTransformation("PID-10=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-10.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-10.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_RACE_FULL);
    {
      Certify.CertifyItem certifyItem = certifyItems.get((int) (System.currentTimeMillis() % certifyItems.size()));
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Race is " + certifyItem.getLabel() + " (Random value from CDC full set)");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B02." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Ethnicity is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B03." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_CHILD);
      testCaseMessage.setDescription("Information Source is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B04." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("VFC Status is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B05." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_6_R_VARICELLA_HISTORY_CHILD);
      testCaseMessage.setDescription("History of Disease is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B06." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Registry Status is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B07." + count);
      testCaseMessage.appendCustomTransformation("PD1-16=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PD1-17=[NOW]");
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_REFUSAL_REASON);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_5_R_REFUSED_TODDLER);
      testCaseMessage.setDescription("Refusal Reason is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B08." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Relationship is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B09." + count);
      testCaseMessage.appendCustomTransformation("NK1-3.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("NK1-3.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("NK1-3.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_HISTORICAL);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_CHILD);
      testCaseMessage.setDescription("Historical vaccination is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B10." + count);
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=" + certifyItem.getTable());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_ADMIN);
    List<Certify.CertifyItem> mvxItems = certify.getCertifyItemList(Certify.FIELD_VAC_ADMIN);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Administered vaccination is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B11." + count);
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=CVX");
      Certify.CertifyItem mvxItem = null;
      for (Certify.CertifyItem ci : mvxItems) {
        if (ci.getCode().equalsIgnoreCase(certifyItem.getTable())) {
          mvxItem = ci;
          break;
        }
      }
      if (mvxItem != null) {
        testCaseMessage.appendCustomTransformation("RXA-17.1=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("RXA-17.2=" + certifyItem.getLabel());
        testCaseMessage.appendCustomTransformation("RXA-17.3=" + certifyItem.getTable());
      } else {
        testCaseMessage.appendCustomTransformation("RXA-17.1=");
        testCaseMessage.appendCustomTransformation("RXA-17.2=");
        testCaseMessage.appendCustomTransformation("RXA-17.3=");
      }

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_BODY_ROUTE);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Body Route is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B12." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Body Site is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B13." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Additional Address Type is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B14." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Additional Name Type is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B15." + count);
      testCaseMessage.appendCustomTransformation("PID-5.1#2=PID-5.1");
      testCaseMessage.appendCustomTransformation("PID-5.2#2=PID-5.2");
      testCaseMessage.appendCustomTransformation("PID-5.7#2=" + certifyItem.getCode());
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_TEL_USE_CODE);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Phone Telcommunications Use Code is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B16." + count);
      testCaseMessage.appendCustomTransformation("PID-13.2=" + certifyItem.getCode());
      if (certifyItem.getCode().equalsIgnoreCase("NET")) {
        testCaseMessage.appendCustomTransformation("PID-13.3=X.400");
        testCaseMessage.appendCustomTransformation("PID-13.4=[EMAIL]");
      } else if (certifyItem.getCode().equalsIgnoreCase("BPN")) {
        testCaseMessage.appendCustomTransformation("PID-13.3=BP");
      }
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_TEL_EQUIPMENT_TYPE);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Phone Telcommunications Equipment Type is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B17." + count);
      testCaseMessage.appendCustomTransformation("PID-13.3=" + certifyItem.getCode());
      if (certifyItem.getCode().equals("BP")) {
        testCaseMessage.appendCustomTransformation("PID-13.2=BPN");
      } else if (certifyItem.getCode().equalsIgnoreCase("X.400")) {
        testCaseMessage.appendCustomTransformation("PID-13.2=NET");
        testCaseMessage.appendCustomTransformation("PID-13.4=[EMAIL]");
      } else if (certifyItem.getCode().equalsIgnoreCase("Internet")) {
        testCaseMessage.appendCustomTransformation("PID-13.2=NET");
        testCaseMessage.appendCustomTransformation("PID-13.4=http://openimmunizationsoftware.net/");
      }
      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ID_TYPE);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Person Id Type is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B18." + count);
      testCaseMessage.appendCustomTransformation("PID-3.4#2=OIS");
      testCaseMessage.appendCustomTransformation("PID-3.5#2=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_PUBLICITY_CODE);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Publicity Code is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B19." + count);
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
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("County Code is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B20." + count);
      testCaseMessage.appendCustomTransformation("PID-11.4=" + certifyItem.getTable());
      testCaseMessage.appendCustomTransformation("PD1-11.7=N");
      testCaseMessage.appendCustomTransformation("PID-11.9=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_LANGUAGE);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Primary Language is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B21." + count);
      testCaseMessage.appendCustomTransformation("PID-20.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-20.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-20.3=" + certifyItem.getTable());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_LANGUAGE_FULL);
    {
      Certify.CertifyItem certifyItem = certifyItems.get((int) (System.currentTimeMillis() % certifyItems.size()));
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Primary Language is " + certifyItem.getLabel()
          + " (Random value from CDC full set)");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B21." + count);
      testCaseMessage.appendCustomTransformation("PID-20.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-20.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-20.3=" + certifyItem.getTable());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_COMPLETION);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = null;
      String completionStatus = certifyItem.getCode();
      if (completionStatus.equals("CP") || completionStatus.equals("PA")) {
        testCaseMessage = createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      } else if (completionStatus.equals("NA")) {
        testCaseMessage = createTestCaseMessage(SCENARIO_5_R_REFUSED_TODDLER);
        testCaseMessage.appendCustomTransformation("RXA-5.1=998");
        testCaseMessage.appendCustomTransformation("RXA-5.2=Not administered");
      } else {
        testCaseMessage = createTestCaseMessage(SCENARIO_5_R_REFUSED_TODDLER);
      }

      testCaseMessage.setDescription("Vaccination completion is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B22." + count);
      testCaseMessage.appendCustomTransformation("RXA-20.1=" + completionStatus);

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ACTION);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Vaccination Action Code is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B23." + count);
      testCaseMessage.appendCustomTransformation("RXA-21=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ACTION);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Vaccination Action is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B24." + count);
      testCaseMessage.appendCustomTransformation("RXA-21=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_DEGREE);
    count = 0;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Vaccination Administering Provider's Degree is " + certifyItem.getLabel());
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B25." + count);
      testCaseMessage.appendCustomTransformation("RXA-10.2=[LAST_DIFFERENT]");
      testCaseMessage.appendCustomTransformation("RXA-10.3=[FATHER]");
      testCaseMessage.appendCustomTransformation("RXA-10.21=" + certifyItem.getCode());

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    for (int i = 1; i < 3; i++) {
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Mulitiple Birth: First of " + i);
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B26." + count);
      if (i == 1) {
        testCaseMessage.appendCustomTransformation("PID-24=N");
        testCaseMessage.appendCustomTransformation("PID-25=1");
      } else {
        testCaseMessage.appendCustomTransformation("PID-24=Y");
        testCaseMessage.appendCustomTransformation("PID-25=1");
      }

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);

    }

    for (int i = 2; i < 3; i++) {
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Mulitiple Birth: Last of " + i);
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B27." + count);

      testCaseMessage.appendCustomTransformation("PID-24=Y");
      testCaseMessage.appendCustomTransformation("PID-25=" + i);

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_CHILD);
      testCaseMessage.setDescription("Death Recorded Without Date");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B28.1");
      testCaseMessage.appendCustomTransformation("PID-30=Y");

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);

      testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_CHILD);
      testCaseMessage.setDescription("Death Recorded With Date");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B28.2");
      testCaseMessage.appendCustomTransformation("PID-30=Y");
      testCaseMessage.appendCustomTransformation("PID-29=[TODAY]");

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);

      testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Patient Not Deceased");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B28.2");
      testCaseMessage.appendCustomTransformation("PID-30=N");

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }
    
    {
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_ADD_DELETE);
      testCaseMessage.setDescription("Vaccination Added and then Deleted");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("B29.1");

      statusCheckTestCaseIntermediateList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);

    }
  }

  private void prepareExceptional() {

    Transformer transformer = new Transformer();
    int count = 0;

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendOriginalMessage("OBX|5|NM|6287-7^Baker's yeast IgE Ab in Serum^LN||1945||||||F\n");
      testCaseMessage.setDescription("Tolerance Check: Message includes observation not typically sent to IIS");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("E1." + count);
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
      testCaseMessage.setTestCaseNumber("E1." + count);
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
      testCaseMessage.setTestCaseNumber("E1." + count);
      testCaseMessage.appendCustomTransformation("PV1-10=AMB");
      statusCheckTestCaseExceptionalList.add(testCaseMessage);
      transformer.transform(testCaseMessage);
      testCaseMessage.setAssertResult("Accept - *");
      register(testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Tolerance Check: Observation at patient level (HL7 2.8 capability)");
      testCaseMessage.setTestCaseSet(testCaseSet);
      testCaseMessage.setTestCaseNumber("E1." + count);
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

    try {

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
    testCaseMessage.setTestCaseNumber("E1." + count);
    statusCheckTestCaseExceptionalList.add(testCaseMessage);
    transformer.transform(testCaseMessage);
    testCaseMessage.setAssertResult("Accept - *");
    register(testCaseMessage);
    return count;
  }

  private void changePatientIdentifyingInformation(String messageText, TestCaseMessage testCaseMessage) {
    HL7Reader hl7Reader = new HL7Reader(messageText);
    testCaseMessage.appendCustomTransformation("MSH-10=" + messageText.hashCode() + "." + System.currentTimeMillis());
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
      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaBLevel1Progress = makeScore(count, statusCheckTestCaseIntermediateList.size());
      CreateTestCaseServlet.saveTestCase(testCaseMessage, session);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaBLevel1Score = makeScore(testPass, statusCheckTestCaseIntermediateList.size());
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
      } catch (Throwable t) {
        testCaseMessage.setException(t);
      }
      areaDLevel1Progress = makeScore(count, statusCheckTestCaseExceptionalList.size());
      CreateTestCaseServlet.saveTestCase(testCaseMessage, session);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaDLevel1Score = makeScore(testPass, statusCheckTestCaseExceptionalList.size());
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
      queryTestCaseMessage.setMessageText(QueryConverter.convertVXUtoQBP(testCaseMessage.getMessageText()));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseNumber("A3." + count);
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
        List<CompareManager.Comparison> comparisonList = CompareManager.compareMessages(vxuMessage, rspMessage);
        queryTestCaseMessage.setComparisonList(comparisonList);
        for (CompareManager.Comparison comparison : comparisonList) {
          if (comparison.isTested()
              && comparison.getPriorityLevel() <= CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL) {
            testQueryCountOptional++;
            if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
              testQueryCountRequired++;
            }

            if (comparison.isPass()) {
              testQueryPassOptional++;
              if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                testQueryPassRequired++;
              }
            }
          }
        }
      } catch (Throwable t) {
        queryTestCaseMessage.setException(t);
      }
      areaALevel3Progress = makeScore(count, statusCheckTestCaseBasicList.size());
      areaALevel2Progress = areaALevel3Progress;
      CreateTestCaseServlet.saveTestCase(queryTestCaseMessage, session);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaALevel2Score = makeScore(testQueryPassRequired, testQueryCountRequired);
    areaALevel3Score = makeScore(testQueryPassOptional, testQueryCountOptional);
  }

  private String prepareSendQueryMessage(TestCaseMessage queryTestCaseMessage) {
    String message = queryTestCaseMessage.getMessageText();
    if (!queryConnector.getCustomTransformations().equals("")) {
      Transformer transformer = new Transformer();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
      queryConnector.setCurrentFilename("dqa-tester-request" + sdf.format(new Date()) + ".hl7");
      message = transformer.transform(queryConnector, message);
    }
    queryTestCaseMessage.setMessageTextSent(message);
    return message;
  }

  private void prepareQueryIntermediate() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
      count++;
      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(QueryConverter.convertVXUtoQBP(testCaseMessage.getMessageText()));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseNumber("B3." + count);
      statusCheckQueryTestCaseIntermediateList.add(queryTestCaseMessage);
      register(queryTestCaseMessage);
    }
  }

  private void queryIntermediate() {
    int count;
    int testQueryPassRequired = 0;
    int testQueryPassOptional = 0;
    int testQueryCountRequired = 0;
    int testQueryCountOptional = 0;
    count = 0;
    for (TestCaseMessage queryTestCaseMessage : statusCheckQueryTestCaseIntermediateList) {
      count++;

      try {
        long startTime = System.currentTimeMillis();
        String message = prepareSendQueryMessage(queryTestCaseMessage);
        String rspMessage = queryConnector.submitMessage(message, false);
        totalQueryCount++;
        totalQueryTime += System.currentTimeMillis() - startTime;
        queryTestCaseMessage.setHasRun(true);
        queryTestCaseMessage.setActualResponseMessage(rspMessage);
        List<CompareManager.Comparison> comparisonList = CompareManager.compareMessages(
            queryTestCaseMessage.getDerivedFromVXUMessage(), rspMessage);
        queryTestCaseMessage.setComparisonList(comparisonList);
        queryTestCaseMessage.setPassedTest(true);
        for (CompareManager.Comparison comparison : comparisonList) {
          if (comparison.isTested()
              && comparison.getPriorityLevel() <= CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL) {
            testQueryCountOptional++;
            if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
              testQueryCountRequired++;
            }

            if (comparison.isPass()) {
              testQueryPassOptional++;
              if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                testQueryPassRequired++;
              }
            } else if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
              queryTestCaseMessage.setPassedTest(false);
            }
          }
        }
      } catch (Throwable t) {
        queryTestCaseMessage.setException(t);
      }
      areaBLevel3Progress = makeScore(count, statusCheckQueryTestCaseIntermediateList.size());
      areaBLevel2Progress = areaBLevel3Progress;
      CreateTestCaseServlet.saveTestCase(queryTestCaseMessage, session);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaBLevel2Score = makeScore(testQueryPassRequired, testQueryCountRequired);
    areaBLevel3Score = makeScore(testQueryPassOptional, testQueryCountOptional);
  }

  private void queryExceptional() {
    int count;
    int testQueryPassRequired = 0;
    int testQueryPassOptional = 0;
    int testQueryCountRequired = 0;
    int testQueryCountOptional = 0;
    count = 0;
    for (TestCaseMessage testCaseMessage : statusCheckTestCaseExceptionalList) {
      count++;
      String vxuMessage = testCaseMessage.getMessageText();

      TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
      queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
      queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
      queryTestCaseMessage.setMessageText(QueryConverter.convertVXUtoQBP(testCaseMessage.getMessageText()));
      queryTestCaseMessage.setTestCaseSet(testCaseSet);
      queryTestCaseMessage.setTestCaseNumber("B3." + count);
      statusCheckQueryTestCaseTolerantList.add(queryTestCaseMessage);
      register(queryTestCaseMessage);
      try {
        long startTime = System.currentTimeMillis();
        String message = prepareSendQueryMessage(queryTestCaseMessage);
        String rspMessage = queryConnector.submitMessage(message, false);
        totalQueryCount++;
        totalQueryTime += System.currentTimeMillis() - startTime;
        queryTestCaseMessage.setHasRun(true);
        queryTestCaseMessage.setActualResponseMessage(rspMessage);
        List<CompareManager.Comparison> comparisonList = CompareManager.compareMessages(vxuMessage, rspMessage);
        queryTestCaseMessage.setComparisonList(comparisonList);
        queryTestCaseMessage.setPassedTest(true);
        for (CompareManager.Comparison comparison : comparisonList) {
          if (comparison.isTested()
              && comparison.getPriorityLevel() <= CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL) {
            testQueryCountOptional++;
            if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
              testQueryCountRequired++;
            }

            if (comparison.isPass()) {
              testQueryPassOptional++;
              if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                testQueryPassRequired++;
              }
            } else if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
              queryTestCaseMessage.setPassedTest(false);
            }
          }
        }
      } catch (Throwable t) {
        queryTestCaseMessage.setException(t);
      }
      areaDLevel3Progress = makeScore(count, statusCheckTestCaseExceptionalList.size());
      areaDLevel2Progress = areaDLevel3Progress;
      CreateTestCaseServlet.saveTestCase(queryTestCaseMessage, session);
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }
    }
    areaDLevel2Score = makeScore(testQueryPassRequired, testQueryCountRequired);
    areaDLevel3Score = makeScore(testQueryPassOptional, testQueryCountOptional);
  }

  public void printResults(PrintWriter out) {
    out.println("<table border=\"1\" cellspacing=\"0\" width=\"720\">");
    out.println("  <tr>");
    out.println("    <th></th>");
    out.println("    <th>Level 1</th>");
    out.println("    <th>Level 2</th>");
    out.println("    <th>Level 3</th>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th><font size=\"+1\">Basic</font><br/><font size=\"-2\">IIS can accept updates from EHR</font></th>");
    if (areaALevel1Score >= 100) {
      out.println("    <td class=\"pass\">All NIST 2014 scenarios are accepted. <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
    } else if (areaALevel1Score >= 0) {
      out.println("    <td class=\"fail\">Not all NIST 2014 scenarios are accepted. (" + areaALevel1Score
          + "% accepted) <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
    } else {
      if (areaALevel1Progress > -1) {
        out.println("    <td>running now ... <br/>" + areaALevel1Progress + "% complete</td>");
      } else {
        out.println("    <td>updates not sent yet</td>");
      }
    }
    if (areaALevel2Score >= 100) {
      out.println("    <td class=\"pass\">All required IIS core fields are supported.  <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
    } else if (areaALevel2Score >= 0) {
      out.println("    <td class=\"fail\">Not all required IIS core fields are supported. (" + areaALevel2Score
          + "% of tests passed) <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
    } else {
      if (areaALevel2Progress > -1) {
        out.println("    <td>running now ... <br/>" + areaALevel2Progress + "% complete</td>");
      } else {
        if (willQuery) {
          out.println("    <td>queries not sent yet</td>");
        } else {
          out.println("    <td>query tests not enabled</td>");
        }
      }
    }
    if (areaALevel3Score >= 100) {
      out.println("    <td class=\"pass\">All required and optional IIS core data were returned when queried. "
          + "<font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
    } else if (areaALevel3Score >= 0) {
      out.println("    <td class=\"fail\">Not all required or optional IIS core data were returned when queried ("
          + areaALevel3Score
          + "% of fields returned) <font size=\"-1\"><a href=\"#areaALevel1\">(details)</a></font></td>");
    } else {
      if (areaALevel3Progress > -1) {
        out.println("    <td>running now ... <br/>" + areaALevel3Progress + "% complete</td>");
      } else {
        if (willQuery) {
          out.println("    <td>queries not sent yet</td>");
        } else {
          out.println("    <td>query tests not enabled</td>");
        }
      }
    }
    out.println("  </tr>");
    if (runB) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Intermediate</font><br/><font size=\"-2\">IIS can recognize valid codes</font></th>");
      if (areaBLevel1Score >= 100) {
        out.println("    <td class=\"pass\">All messages with core coded data elements were accepted. "
            + "<font size=\"-1\"><a href=\"#areaBLevel1\">(details)</a></font></td>");
      } else if (areaBLevel1Score >= 0) {
        out.println("    <td class=\"fail\">Some messages with core coded data elements were NOT accepted. ("
            + areaBLevel1Score + "% messages accepted) "
            + "<font size=\"-1\"><a href=\"#areaBLevel1\">(details)</a></font></td>");
      } else {
        if (areaBLevel1Progress > -1) {
          out.println("    <td>running now ... <br/>" + areaBLevel1Progress + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaBLevel2Score >= 100) {
        out.println("    <td class=\"pass\">All required IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaBLevel2\">(details)</a></font></td>");
      } else if (areaBLevel2Score >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields were returned. (" + areaBLevel2Score
            + "% core fields returned) " + "<font size=\"-1\"><a href=\"#areaBLevel2\">(details)</a></font></td>");
      } else {
        if (areaBLevel2Progress > -1) {
          out.println("    <td>running now ... <br/>" + areaBLevel2Progress + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      if (areaBLevel3Score >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaBLevel3\">(details)</a></font></td>");
      } else if (areaBLevel3Score >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core fields were returned. ("
            + areaBLevel3Score + "% required and optional were returned) "
            + "<font size=\"-1\"><a href=\"#areaBLevel3\">(details)</a></font></td>");
      } else {
        if (areaBLevel3Progress > -1) {
          out.println("    <td>running now ... <br/>" + areaBLevel3Progress + "% complete</td>");
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
    if (runC) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Advanced</font><br/><font size=\"-2\">IIS can identify quality issues</font></th>");
      if (areaCLevelScore[0] >= 100) {
        out.println("    <td class=\"pass\">All high priority issues were identified. "
            + "<font size=\"-1\"><a href=\"#areaCLevel1\">(details)</a></font></td>");
      } else if (areaCLevelScore[0] >= 0) {
        out.println("    <td class=\"fail\">Not all high priority issues were identified. (" + areaCLevelScore[0]
            + "% of issues identified) " + "<font size=\"-1\"><a href=\"#areaCLevel1\">(details)</a></font></td>");
      } else {
        if (areaCLevelProgress[0] > -1) {
          out.println("    <td>running now ... <br/>" + areaCLevelProgress[0] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaCLevelScore[1] >= 100) {
        out.println("    <td class=\"pass\">All medium priority issues were identified. "
            + "<font size=\"-1\"><a href=\"#areaCLevel2\">(details)</a></font></td>");
      } else if (areaCLevelScore[1] >= 0) {
        out.println("    <td class=\"fail\">Not all medium priority issues were identified. (" + areaCLevelScore[1]
            + "% issues identified) " + "<font size=\"-1\"><a href=\"#areaCLevel2\">(details)</a></font></td>");
      } else {
        if (areaCLevelProgress[1] > -1) {
          out.println("    <td>running now ... <br/>" + areaCLevelProgress[1] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaCLevelScore[2] >= 100) {
        out.println("    <td class=\"pass\">All low priority issues were identified. "
            + "<font size=\"-1\"><a href=\"#areaCLevel3\">(details)</a></font></td>");
      } else if (areaCLevelScore[2] >= 0) {
        out.println("    <td class=\"fail\">Not all low priority issues were identified. (" + areaCLevelScore[2]
            + "% issues were identified) " + "<font size=\"-1\"><a href=\"#areaCLevel3\">(details)</a></font></td>");
      } else {
        if (areaCLevelProgress[2] > -1) {
          out.println("    <td>running now ... <br/>" + areaCLevelProgress[2] + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      out.println("  </tr>");
    }
    if (runD) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Exceptional</font><br/><font size=\"-2\">IIS can allow for minor differences</font></th>");
      if (areaDLevel1Score >= 100) {
        out.println("    <td class=\"pass\">All messages accepted as-is. "
            + "<font size=\"-1\"><a href=\"#areaDLevel1\">(details)</a></font></td>");
      } else if (areaDLevel1Score >= 0) {
        out.println("    <td class=\"fail\">Some messages were not accepted as-is. (" + areaDLevel1Score
            + "% messages accepted) " + "<font size=\"-1\"><a href=\"#areaDLevel1\">(details)</a></font></td>");
      } else {
        if (areaDLevel1Progress > -1) {
          out.println("    <td>running now ... <br/>" + areaDLevel1Progress + "% complete</td>");
        } else {
          out.println("    <td>updates not sent yet</td>");
        }
      }
      if (areaDLevel2Score >= 100) {
        out.println("    <td class=\"pass\">All required IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaDLevel2\">(details)</a></font></td>");
      } else if (areaDLevel2Score >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields were returned. (" + areaDLevel2Score
            + "% core fields returned) " + "<font size=\"-1\"><a href=\"#areaDLevel2\">(details)</a></font></td>");
      } else {
        if (areaDLevel2Progress > -1) {
          out.println("    <td>running now ... <br/>" + areaDLevel2Progress + "% complete</td>");
        } else {
          if (willQuery) {
            out.println("    <td>queries not sent yet</td>");
          } else {
            out.println("    <td>query tests not enabled</td>");
          }
        }
      }
      if (areaDLevel3Score >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core fields were returned. "
            + "<font size=\"-1\"><a href=\"#areaDLevel3\">(details)</a></font></td>");
      } else if (areaDLevel3Score >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core fields were returned. ("
            + areaDLevel3Score + "% required and optional were returned) "
            + "<font size=\"-1\"><a href=\"#areaDLevel3\">(details)</a></font></td>");
      } else {
        if (areaDLevel3Progress > -1) {
          out.println("    <td>running now ... <br/>" + areaDLevel3Progress + "% complete</td>");
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
    if (areaELevel1Progress == -1) {
      out.println("    <td>not calculated yet</td>");
    } else {
      if (areaELevel1Score < 3000) {
        out.println("    <td class=\"pass\">Performance was acceptable: " + areaELevel1Score + "ms for updates.</td>");
      } else {
        out.println("    <td class=\"fail\">Performance was below acceptable levels: " + areaELevel1Score
            + "ms for updates.</td>");
      }
    }
    if (areaELevel2Progress == -1) {
      out.println("    <td>not calculated yet</td>");
    } else {
      if (areaELevel2Score < 5000) {
        out.println("    <td class=\"pass\">Performance was acceptable: " + areaELevel2Score + "ms for queries.</td>");
      } else {
        out.println("    <td class=\"fail\">Performance was below acceptable levels: " + areaELevel2Score
            + "ms for queries.</td>");
      }
    }
    out.println("    <td>not defined</td>");
    out.println("  </tr>");
    if (runF) {
      out.println("  <tr>");
      out.println("    <th><font size=\"+1\">Conformance</font><br/><font size=\"-2\">IIS can respond correctly to requests</font></th>");
      if (areaFLevel1Score >= 100) {
        out.println("    <td class=\"pass\">All acknowledgement (ACK) responses meet HL7 and CDC standards. "
            + "<font size=\"-1\"><a href=\"#areaFLevel1\">(details)</a></font></td>");
      } else if (areaFLevel1Score >= 0) {
        out.println("    <td class=\"fail\">Not all acknowledgement (ACK) responses meet HL7 and CDC standards. ("
            + areaFLevel1Score + "% of messages met standard) "
            + "<font size=\"-1\"><a href=\"#areaFLevel1\">(details)</a></font></td>");
      } else {
        if (areaFLevel1Progress > -1) {
          out.println("    <td>running now ... <br/>" + areaFLevel1Progress + "% complete</td>");
        } else {
          out.println("    <td>not analyzed yet</td>");
        }
      }
      if (areaFLevel2Score >= 100) {
        out.println("    <td class=\"pass\">All query response (RSP) messages met HL7 and CDC standards.  "
            + "<font size=\"-1\"><a href=\"#areaFLevel2\">(details)</a></font></td>");
      } else if (areaFLevel2Score >= 0) {
        out.println("    <td class=\"fail\">Not all query response (RSP) messages met HL7 and CDC standards. ("
            + areaFLevel2Score + "% of fields returned) "
            + "<font size=\"-1\"><a href=\"#areaFLevel2\">(details)</a></font></td>");
      } else {
        if (areaFLevel2Progress > -1) {
          out.println("    <td>running now ... <br/>" + areaFLevel2Progress + "% complete</td>");
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

  public void printProgressDetails(PrintWriter out, boolean toFile) {

    out.println("<p>The IIS Test process is an automated sequence of tests to verify how closely an IIS HL7 Interface " +
    		"conforms to national standards established by colloborate efforts of the Immunization Information Systems Support" +
    		"Branch (IISSB) of the Centers for Disease Control and Prevention (CDC), the American Immunization Registry Association" +
    		"(AIRA) and IIS community members.  </p>");
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
    if (statusMessage != null) {
      out.println("  <tr>");
      out.println("    <th>Currently</th>");
      out.println("    <td>" + statusMessage + "</td>");
      out.println("  </tr>");
    }
    out.println("  <tr>");
    out.println("    <th>Basic Tests</th>");
    out.println("    <td>Enabled</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Intermediate Tests</th>");
    out.println("    <td>" + (runB ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Advanced Tests</th>");
    out.println("    <td>" + (runC ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Exceptional Tests</th>");
    out.println("    <td>" + (runD ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Performance Tests</th>");
    out.println("    <td>Enabled</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Conformance Tests</th>");
    out.println("    <td>" + (runF ? "Enabled" : "Not Enabled") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Update Test Count</th>");
    out.println("    <td>" + totalUpdateCount + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Query Test Count</th>");
    out.println("    <td>" + totalQueryCount + "</td>");
    out.println("  </tr>");
    out.println("</table>");
    out.println("</br>");

    if (exception != null) {
      out.println("<p>Exception occurred during processing. Unable to complete analysis as expected. </p>");
      out.println("<pre>");
      exception.printStackTrace(out);
      out.println("</pre>");
    }
    
    out.println("<p>The test process utilizes a real time connection to the IIS HL7 interface. " +
    		"Connections are established using the Simple Message Mover standard for connecting " +
    		"to IIS. This standard supports the most common used Web Service and HTTPS standards. </p>");

    if (!connector.getCustomTransformations().equals("")) {
      out.println("<p>This interface requires customized Transformations to modify each message before trasnmitting" +
      		"them to the IIS. These transformations can range from setting the correct submitter facility in the " +
      		"message header to modifying the structure of the HL7 message to meet local requirements. </p>");
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
    if (!connector.getCustomTransformations().equals("")) {
      out.println("  <tr>");
      out.println("    <th>Transformations</th>");
      out.println("    <td><pre>" + connector.getCustomTransformations() + "</pre></td>");
      out.println("  </tr>");
    }
    out.println("</table>");
    out.println("</br>");

    if (queryConnector != connector) {
      out.println("<p>The connection information for performing queries is different than for the updates. This is to either accomodate" +
      		"different standards for message format or to allow for different credentials and URL. </p>");
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

    int testNum = 0;
    if (areaALevel1Progress > 0) {
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
            if (testCaseMessage.getComparisonList() != null) {
              hasRun = true;
              for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                if (comparison.isTested()
                    && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                  if (!comparison.isPass()) {
                    hasPassed = false;
                    break;
                  }
                }
              }
            }
            if (hasPassed) {
              out.println("    <td class=\"pass\">All required fields returned. "
                  + makeCompareDetailsLink(testCaseMessage, toFile, false));
            } else if (hasRun) {
              out.println("    <td class=\"fail\">Required fields not returned:");
              out.println("      <ul>");
              for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                if (comparison.isTested()
                    && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED
                    && !comparison.isPass()) {

                  out.println("      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel()
                      + "</li>");
                }
              }
              out.println("      </ul>");
              out.println("      " + makeCompareDetailsLink(testCaseMessage, toFile, false));
              out.println("    </td>");
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
              for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                if (comparison.isTested()
                    && comparison.getPriorityLevel() <= CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL) {
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
              for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                if (comparison.isTested()
                    && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL
                    && !comparison.isPass()) {
                  hasOptionalFields = true;
                  break;
                }
              }
              if (hasOptionalFields) {
                out.println("    <td class=\"fail\">Optional fields not returned:");
                out.println("      <ul>");
                for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                  if (comparison.isTested()
                      && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL
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

    if (areaBLevel1Progress > 0) {
      out.println("<div id=\"areaBLevel1\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Intermediate Tests - Level 1 - Accepting Core Data</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
        printTestCaseMessageDetailsUpdate(out, toFile, testCaseMessage);
      }
      out.println("</table>");
      out.println("</br>");
    }
    if (areaBLevel2Progress > 0) {
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
    if (areaBLevel3Progress > 0) {
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

    for (int i = 0; i < areaCLevelScore.length; i++) {
      if (profileTestCaseLists[i] == null || areaCLevelProgress[i] < 100) {
        continue;
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
          } else {
            out.println("    <td class=\"" + classText + "\">not run yet</td>");
          }
        }
        out.println("  </tr>");
      }
      out.println("</table>");
      out.println("</br>");
    }

    if (areaDLevel1Progress > 0) {
      out.println("<div id=\"areaDLevel1\"/>");
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
    if (areaDLevel2Progress > 0) {
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
    if (areaDLevel3Progress > 0) {
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

    if (areaFLevel1Progress > 0) {
      out.println("<div id=\"areaFLevel1\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Exceptional Tests - Level 1 - ACK Correctly Formatted</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : ackAnalysisList) {
        String classText = "nottested";
        HL7Analyzer ackAnalyzer = testCaseMessage.getHL7Analyzer();
        if (ackAnalyzer != null) {
          classText = ackAnalyzer.isPassedTest() ? "pass" : "fail";
        }
        out.println("  <tr>");
        out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
        if (ackAnalyzer != null) {
          if (ackAnalyzer.isPassedTest()) {
            out.println("    <td class=\"" + classText + "\">Ack meets HL7 and CDC standards.  "
                + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
          } else {
            out.println("    <td class=\"" + classText + "\">Ack did not meet HL7 or CDC standards. "
                + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
          }
        } else {
          out.println("    <td class=\"" + classText + "\">not run yet</td>");
        }
        out.println("  </tr>");
      }
      out.println("</table>");
      out.println("</br>");
    }

    if (areaFLevel2Progress > 0) {
      out.println("<div id=\"areaFLevel2\"/>");
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th colspan=\"2\">Exceptional Tests - Level 2 - RSP Correctly Formatted</th>");
      out.println("  </tr>");
      for (TestCaseMessage testCaseMessage : rspAnalysisList) {
        String classText = "nottested";
        HL7Analyzer ackAnalyzer = testCaseMessage.getHL7Analyzer();
        if (ackAnalyzer != null) {
          classText = ackAnalyzer.isPassedTest() ? "pass" : "fail";
        }
        out.println("  <tr>");
        out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
        if (ackAnalyzer != null) {
          if (ackAnalyzer.isPassedTest()) {
            out.println("    <td class=\"" + classText + "\">RSP meets HL7 and CDC standards.  "
                + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
          } else {
            out.println("    <td class=\"" + classText + "\">RSP did not meet HL7 or CDC standards. "
                + makeTestCaseMessageDetailsLink(testCaseMessage, toFile) + "</td>");
          }
        } else {
          out.println("    <td class=\"" + classText + "\">not run yet</td>");
        }
        out.println("  </tr>");
      }
      out.println("</table>");
      out.println("</br>");
    }

  }

  public void printTestCaseMessageDetailsQueryOptional(PrintWriter out, boolean toFile,
      TestCaseMessage testCaseMessage) {
    String classText = "nottested";
    boolean passedAllOptional = false;
    if (testCaseMessage.isHasRun() && testCaseMessage.getComparisonList() != null) {
      passedAllOptional = true;
      for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
        if (comparison.isTested()
            && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL
            && !comparison.isPass()) {
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

  private int makeScore(int num, int denom) {
    return (int) (100.0 * num / denom + 0.5);
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
            + "&showSubmissionChange=true\">(details)</a></font>";
      } else {
        return "<font size=\"-1\"><a href=\"CompareServlet?certifyServletBasicNum=" + testCaseMessage.getTestCaseId()
            + "\">(details)</a></font>";

      }
    }
  }
}
