package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.forecast.ForecastTesterManager;
import org.immunizationsoftware.dqa.tester.manager.nist.NISTValidator;
import org.immunizationsoftware.dqa.tester.manager.nist.ValidationReport;
import org.immunizationsoftware.dqa.tester.run.TestRunner;
import org.immunizationsoftware.dqa.transform.Comparison;
import org.immunizationsoftware.dqa.transform.ScenarioManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;
import org.openimmunizationsoftware.dqa.tr.RecordServletInterface;

public abstract class CertifyArea implements RecordServletInterface
{
  protected List<TestCaseMessage> updateList = new ArrayList<TestCaseMessage>();
  protected List<TestCaseMessage> queryList = new ArrayList<TestCaseMessage>();
  protected boolean run = false;
  protected int[] areaScore = new int[3];
  protected int[] areaProgressCount = new int[3];
  protected int[] areaProgress = new int[3];
  protected int[] areaCount = new int[3];
  protected String areaLabel = "";
  protected String areaLetter = "";
  protected boolean performanceConformance = false;

  protected void incrementUpdateProgress() {
    areaProgressCount[0]++;
    areaProgress[0] = makeScore(areaProgressCount[0], areaCount[0]);
    if (!isPerformanceConformance()) {
      certifyRunner.caTotal.areaProgressCount[0]++;
      certifyRunner.caTotal.areaProgress[0] = makeScore(certifyRunner.caTotal.areaProgressCount[0],
          certifyRunner.caTotal.areaCount[0]);
    }
  }

  protected void incrementQueryProgress() {
    areaProgressCount[1]++;
    areaProgress[1] = makeScore(areaProgressCount[1], areaCount[1]);
    if (!isPerformanceConformance()) {
      certifyRunner.caTotal.areaProgressCount[1]++;
      certifyRunner.caTotal.areaProgress[1] = makeScore(certifyRunner.caTotal.areaProgressCount[1],
          certifyRunner.caTotal.areaCount[1]);
    }
  }

  protected List<ForecastTestPanel> forecastTestPanelList = new ArrayList<ForecastTestPanel>();

  public int[] getAreaProgressCount() {
    return areaProgressCount;
  }

  public void setAreaProgressCount(int[] areaProgressCount) {
    this.areaProgressCount = areaProgressCount;
  }

  public boolean isPerformanceConformance() {
    return performanceConformance;
  }

  protected static enum PassCriteria {
    PASS_AND_NO_MAJOR_CHANGES, PASS_ONLY, NO_ACK_CHANGES
  }

  public boolean isRun() {
    return run;
  }

  public void setRun(boolean run) {
    this.run = run;
  }

  public int[] getAreaScore() {
    return areaScore;
  }

  public void setAreaScore(int[] areaScore) {
    this.areaScore = areaScore;
  }

  public int[] getAreaProgress() {
    return areaProgress;
  }

  public void setAreaProgress(int[] areaProgress) {
    this.areaProgress = areaProgress;
  }

  public int[] getAreaCount() {
    return areaCount;
  }

  public void setAreaCount(int[] areaCount) {
    this.areaCount = areaCount;
  }

  public String getAreaLabel() {
    return areaLabel;
  }

  public void setAreaLabel(String areaLabel) {
    this.areaLabel = areaLabel;
  }

  protected CertifyRunner certifyRunner;

  public CertifyArea(String areaLetter, String areaLabel, CertifyRunner certifyRunner) {
    this.areaLabel = areaLabel;
    this.areaLetter = areaLetter;
    this.certifyRunner = certifyRunner;
    areaScore[0] = -1;
    areaScore[1] = -1;
    areaScore[2] = -1;
    areaProgress[0] = -1;
    areaProgress[1] = -1;
    areaProgress[2] = -1;
    areaCount[0] = -1;
    areaCount[1] = -1;
    areaCount[2] = -1;
    areaProgressCount[0] = 0;
    areaProgressCount[1] = 0;
    areaProgressCount[2] = 0;
  }

  public List<TestCaseMessage> getUpdateList() {
    return updateList;
  }

  public void setUpdateList(List<TestCaseMessage> updateList) {
    this.updateList = updateList;
  }

  public List<TestCaseMessage> getQueryList() {
    return queryList;
  }

  public void setQueryList(List<TestCaseMessage> queryList) {
    this.queryList = queryList;
  }

  protected static String makeTwoDigits(int i) {
    if (i < 10) {
      return "0" + i;
    } else {
      return "" + i;
    }
  }

  public TestCaseMessage register(int count, TestCaseMessage testCaseMessage) {
    return register(count, 1, testCaseMessage);
  }

  public TestCaseMessage register(int count, int masterCount, TestCaseMessage testCaseMessage) {
    testCaseMessage.setTestCaseSet(certifyRunner.testCaseSet);
    testCaseMessage.setTestCaseCategoryId(areaLetter + "." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
    testCaseMessage.setTestCaseNumber(certifyRunner.uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
    updateList.add(testCaseMessage);
    testCaseMessage.setTestPosition(certifyRunner.incrementingInt.next());
    testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
    if (testCaseMessage.getAssertResult() == null || testCaseMessage.getAssertResult().equals("")) {
      testCaseMessage.setAssertResult("Accept");
    }
    certifyRunner.transformer.transform(testCaseMessage);
    certifyRunner.register(testCaseMessage);
    return testCaseMessage;
  }

  public TestRunner createTestRunner() {
    TestRunner testRunner = new TestRunner();
    testRunner.setValidateResponse(shouldValidate());
    return testRunner;
  }

  public boolean shouldValidate() {
    return certifyRunner.certifyAreas[CertifyRunner.SUITE_L_CONFORMANCE_2015].isRun();
  }

  public TestCaseMessage registerAdminChild(String description, String customTransformation, int count) {
    TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    if (customTransformation != null) {
      testCaseMessage.appendCustomTransformation(customTransformation);
    }
    testCaseMessage.setDescription(description);
    return register(count, testCaseMessage);
  }

  protected boolean verifyNoMajorChangesMade(TestCaseMessage testCaseMessage) {
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

  protected int makeScore(int num, int denom) {
    if (denom == 0) {
      return 0;
    }
    return (int) (100.0 * num / denom);
  }

  public abstract void prepareUpdates();

  public abstract void sendUpdates();

  protected void runUpdates() {
    runUpdates(false);
  }

  protected void runUpdates(boolean passIfAcksDifferentThanBase) {
    int testPass = 0;
    TestRunner testRunner = createTestRunner();
    for (TestCaseMessage testCaseMessage : updateList) {
      testPass = runUpdate(passIfAcksDifferentThanBase, testPass, testRunner, testCaseMessage);
      incrementUpdateProgress();
      certifyRunner.saveTestCase(testCaseMessage);
      certifyRunner.reportProgress(testCaseMessage);
      if (!certifyRunner.keepRunning) {
        certifyRunner.status = CertifyRunner.STATUS_STOPPED;
        certifyRunner.reportProgress(null);
        return;
      }
    }
    areaScore[0] = makeScore(testPass, updateList.size());
    areaProgress[0] = 100;
    certifyRunner.reportProgress(null);
  }

  public int runUpdate(TestCaseMessage testCaseMessage) {
    TestRunner testRunner = createTestRunner();
    return runUpdate(false, 0, testRunner, testCaseMessage);
  }

  public int runUpdate(boolean passIfAcksDifferentThanBase, int testPass, TestRunner testRunner,
      TestCaseMessage testCaseMessage) {
    try {
      testRunner.setTestSectionType(areaLabel);
      testRunner.runTest(certifyRunner.connector, testCaseMessage);
      boolean pass = testCaseMessage.isPassedTest();
      testCaseMessage.setMajorChangesMade(!verifyNoMajorChangesMade(testCaseMessage));
      certifyRunner.performance.addTotalUpdateTime(testRunner.getTotalRunTime(), testCaseMessage);
      if (passIfAcksDifferentThanBase) {
        boolean same = CompareManager.acksAppearToBeTheSame(
            certifyRunner.testCaseMessageBase.getActualResponseMessage(), testCaseMessage.getActualResponseMessage());
        if (!same) {
          testPass++;
        }
        testCaseMessage.setPassedTest(!same);
        testCaseMessage
            .setActualResultStatus(same ? TestRunner.ACTUAL_RESULT_STATUS_FAIL : TestRunner.ACTUAL_RESULT_STATUS_PASS);
      } else {
        if (pass) {
          testPass++;
        }
      }
      testCaseMessage.setErrorList(testRunner.getErrorList());
      certifyRunner.printExampleMessage(testCaseMessage, "A Basic");
    } catch (Throwable t) {
      testCaseMessage.setException(t);
    }
    return testPass;
  }

  public abstract void prepareQueries();

  public abstract void sendQueries();

  protected void logStatus(String status) {
    certifyRunner.logStatus(status);
  }

  protected void reportProgress(TestCaseMessage testMessage) {
    certifyRunner.reportProgress(testMessage);
  }

  protected void reportForecastProgress(TestCaseMessage testMessage) {
    certifyRunner.reportForecastProgress(testMessage);
  }

  public void setDerivedFrom(TestCaseMessage tcm, TestCaseMessage tcmQuery) {

    tcmQuery.setDerivedFromVXUMessage(tcm.getMessageText());
    tcmQuery.setOriginalMessageResponse(tcm.getActualResponseMessage());
    tcmQuery.setOriginalAccepted(tcm.isAccepted());
    TestCaseMessage tcmUpdates = tcm.getUpdateTestCaseMessage();
    while (tcmUpdates != null) {
      tcmQuery.setDerivedFromVXUMessage(tcmUpdates.getMessageText() + tcmQuery.getDerivedFromVXUMessage());
      tcmQuery
          .setOriginalMessageResponse(tcmUpdates.getActualResponseMessage() + tcmQuery.getOriginalMessageResponse());
      tcmQuery.setOriginalAccepted(tcmUpdates.isAccepted() && tcmQuery.isOriginalAccepted());
      tcmUpdates = tcmUpdates.getUpdateTestCaseMessage();
    }
  }

  public TestCaseMessage registerQuery(int count, TestCaseMessage testCaseMessage) {
    return registerQuery(count, 2, testCaseMessage);
  }

  public TestCaseMessage registerQuery(int count, int masterCount, TestCaseMessage testCaseMessage) {
    TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
    queryTestCaseMessage.setDescription("Query for " + testCaseMessage.getDescription());
    queryTestCaseMessage.setMessageText(certifyRunner.convertToQuery(testCaseMessage));
    return registerQuery(count, masterCount, testCaseMessage, queryTestCaseMessage);
  }

  public TestCaseMessage registerQuery(int count, int masterCount, TestCaseMessage testCaseMessage,
      TestCaseMessage queryTestCaseMessage) {
    queryTestCaseMessage.setTestCaseSet(certifyRunner.testCaseSet);
    queryTestCaseMessage
        .setTestCaseCategoryId(areaLetter + "Q." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
    queryTestCaseMessage.setTestCaseNumber(certifyRunner.uniqueMRNBase + queryTestCaseMessage.getTestCaseCategoryId());
    setDerivedFrom(testCaseMessage, queryTestCaseMessage);
    queryList.add(queryTestCaseMessage);
    queryTestCaseMessage.setTestPosition(certifyRunner.incrementingInt.next());
    queryTestCaseMessage.setTestType(VALUE_TEST_TYPE_QUERY);
    queryTestCaseMessage.setAssertResult(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH);
    queryTestCaseMessage.setForecastTestCase(testCaseMessage.getForecastTestCase());
    String message = Transformer.transform(certifyRunner.queryConnector, queryTestCaseMessage);
    queryTestCaseMessage.setMessageTextSent(message);
    certifyRunner.register(queryTestCaseMessage);
    return queryTestCaseMessage;
  }

  public void runQueries() {
    int count = 0;
    int pass = 0;
    for (TestCaseMessage queryTestCaseMessage : queryList) {
      count++;
      pass = sendQuery(queryTestCaseMessage, pass);
      incrementQueryProgress();
      if (!certifyRunner.keepRunning) {
        certifyRunner.status = CertifyRunner.STATUS_STOPPED;
        reportProgress(null);
        return;
      }
    }
    areaScore[1] = makeScore(pass, count);
    areaCount[1] = count;
    areaProgress[1] = 100;
  }

  public int sendQuery(TestCaseMessage queryTestCaseMessage, int testPass) {
    try {
      long startTime = System.currentTimeMillis();
      String rspMessage = certifyRunner.doSafeQuery(queryTestCaseMessage.getMessageTextSent());
      certifyRunner.performance.addTotalQueryTime(System.currentTimeMillis() - startTime, queryTestCaseMessage);
      queryTestCaseMessage.setHasRun(true);
      queryTestCaseMessage.setActualResponseMessage(rspMessage);
      certifyRunner.setQueryReturnedMostImportantData(queryTestCaseMessage);
      List<Comparison> comparisonList = CompareManager.compareMessages(queryTestCaseMessage.getDerivedFromVXUMessage(),
          rspMessage);
      queryTestCaseMessage.setComparisonList(comparisonList);
      testPass = setPassFailForQuery(queryTestCaseMessage, testPass);
      readForecastActual(queryTestCaseMessage);
      if (queryTestCaseMessage.getForecastActualList().size() > 0) {
        queryTestCaseMessage.setResultForecastStatus(RecordServletInterface.VALUE_RESULT_FORECAST_STATUS_INCLUDED);
      } else {
        queryTestCaseMessage.setResultForecastStatus(RecordServletInterface.VALUE_RESULT_FORECAST_STATUS_NOT_INCLUDED);
      }
      recordForecastResults(queryTestCaseMessage);
      if (shouldValidate()
          && (!certifyRunner.isRedactListResponses() || rspMessage.indexOf(CertifyRunner.REDACTION_NOTICE) == -1)) {
        TestRunner.ascertainValidationResource(queryTestCaseMessage, rspMessage);
        if (queryTestCaseMessage.getValidationResource() != null) {
          ValidationReport validationReport = NISTValidator.validate(rspMessage,
              queryTestCaseMessage.getValidationResource());
          queryTestCaseMessage.setValidationReport(validationReport);
          if (validationReport != null) {
            queryTestCaseMessage
                .setValidationReportPass(validationReport.getHeaderReport().getValidationStatus().equals("Complete")
                    && validationReport.getHeaderReport().getErrorCount() == 0);
          }
        }
      }

    } catch (Throwable t) {
      queryTestCaseMessage.setException(t);
    }

    certifyRunner.saveTestCase(queryTestCaseMessage);
    reportProgress(queryTestCaseMessage);
    reportForecastProgress(queryTestCaseMessage);
    return testPass;
  }

  public void readForecastActual(TestCaseMessage queryTestCaseMessage) {
    ForecastTesterManager.readForecastActual(queryTestCaseMessage);
  }

  public void recordForecastResults(TestCaseMessage queryTestCaseMessage) {
    if (queryTestCaseMessage.getForecastTestCase() != null) {
      if (certifyRunner.connector.getTchForecastTesterSoftwareId() > 0) {
        try {
          String results = certifyRunner.forecastTesterManager.reportForecastResults(queryTestCaseMessage,
              certifyRunner.connector.getTchForecastTesterSoftwareId());
          if (queryTestCaseMessage.getTestCaseNumber() != null
              && !queryTestCaseMessage.getTestCaseNumber().equals("")) {
            if (certifyRunner.testDir != null) {
              File testCaseFile = new File(certifyRunner.testDir,
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
  }

  public int setPassFailForQuery(TestCaseMessage queryTestCaseMessage, int testPass) {
    String queryType = "";
    {
      HL7Reader responseReader = new HL7Reader(queryTestCaseMessage.getActualResponseMessage());
      if (responseReader.advanceToSegment("MSH")) {
        String messageType = responseReader.getValue(9);
        if (messageType.equals("VXR")) {
          queryType = RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH;
        } else if (messageType.equals("VXX")) {
          queryType = RecordServletInterface.VALUE_RESULT_QUERY_TYPE_LIST;
        } else if (messageType.equals("QCK")) {
          if (responseReader.advanceToSegment("QAK") && responseReader.getValue(2).equals("NF")) {
            queryType = RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND;
          }
        } else if (messageType.equals("ACK")) {
          queryType = RecordServletInterface.VALUE_RESULT_QUERY_TYPE_ERROR;
        } else if (messageType.equals("RSP")) {
          String profile = responseReader.getValue(21);
          if (profile.equalsIgnoreCase("Z32") || profile.equalsIgnoreCase("Z42")) {
            queryType = RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH;
          } else if (profile.equalsIgnoreCase("Z31")) {
            queryType = RecordServletInterface.VALUE_RESULT_QUERY_TYPE_LIST;
          } else if (profile.equalsIgnoreCase("Z33")) {
            if (responseReader.advanceToSegment("QAK")) {
              String responseStatus = responseReader.getValue(2);
              if (responseStatus.equals("NF")) {
                queryType = RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND;
              } else if (responseStatus.equals("TM")) {
                queryType = RecordServletInterface.VALUE_RESULT_QUERY_TYPE_TOO_MANY;
              }
            }
          }
        }
      }
    }

    queryTestCaseMessage.setActualResultQueryType(queryType);

    boolean passed = queryTestCaseMessage.getAssertResult().equals(queryType);

    if (queryTestCaseMessage.getAssertResult().equalsIgnoreCase(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH)) {
      passed = queryType.equals(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH);
    } else if (queryTestCaseMessage.getAssertResult()
        .equalsIgnoreCase(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_LIST)) {
      passed = queryType.equals(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_LIST);
    } else if (queryTestCaseMessage.getAssertResult()
        .equalsIgnoreCase(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND)) {
      passed = queryType.equals(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND);
    } else if (queryTestCaseMessage.getAssertResult()
        .equalsIgnoreCase(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_TOO_MANY)) {
      passed = queryType.equals(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_TOO_MANY);
    } else if (queryTestCaseMessage.getAssertResult()
        .equalsIgnoreCase(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_ERROR)) {
      passed = queryType.equals(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_ERROR);
    }

    if (passed) {
      queryTestCaseMessage.setActualResultStatus(TestRunner.ACTUAL_RESULT_STATUS_PASS);
      testPass++;
    } else {
      queryTestCaseMessage.setActualResultStatus(TestRunner.ACTUAL_RESULT_STATUS_FAIL);
    }
    return testPass;
  }

  public boolean fieldNotSupported(Comparison comparison) {
    return certifyRunner.connector.getQueryResponseFieldsNotReturnedSet() != null
        && certifyRunner.connector.getQueryResponseFieldsNotReturnedSet().contains(comparison.getFieldLabel());
  }

  public void doPrepareQueries() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : updateList) {
      if (testCaseMessage.isDoNotQueryFor()) {
        continue;
      }
      count++;
      registerQuery(count, testCaseMessage);
    }
  }

  public void addQuerySupport(String description, String customTransformation, int count) {
    TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    if (customTransformation != null) {
      testCaseMessage.appendCustomTransformation(customTransformation);
    }
    testCaseMessage.setDescription(description);
    register(count, 1, testCaseMessage);
  }

  public void addTwins(int count) {
    addQuerySupport("First Twin", "PID-24=Y\rPID-25=1", ++count);
    {
      String middleNameOriginal = "";
      String middleInitial = "";
      String middleName = "";
      String gender = "";
      TestCaseMessage testCaseMessage1 = updateList.get(updateList.size() - 1);
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
        middleName = certifyRunner.transformer.getValue(gender.equals("M") ? "BOY" : "GIRL");
        tryCount++;
        if (tryCount > 1000) {
          // give up already! We'll go with what we have
          break;
        }
      }

      TestCaseMessage testCaseMessage2 = new TestCaseMessage();
      testCaseMessage2.setOriginalMessage(testCaseMessage1.getMessageText());
      testCaseMessage2.setDescription("Second Twin");
      String uniqueId = testCaseMessage2.getTestCaseNumber();
      testCaseMessage2.appendCustomTransformation(
          "MSH-10=" + uniqueId + "." + Transformer.makeBase62Number(System.currentTimeMillis() % 10000));
      testCaseMessage2.appendCustomTransformation(
          "PID-3.1=" + (uniqueId.length() <= 15 ? uniqueId : uniqueId.substring(uniqueId.length() - 15)));
      testCaseMessage2.appendCustomTransformation("PID-5.3=" + middleName);
      testCaseMessage2.appendCustomTransformation("PID-24=Y");
      testCaseMessage2.appendCustomTransformation("PID-25=2");
      register(count, 1, testCaseMessage2);
    }
  }

  public void addTestCasesFromSavedSet(String testCaseSet) {
    Map<String, TestCaseMessage> testCaseMessageMap = certifyRunner.testMessageMapMap.get(testCaseSet);
    if (testCaseMessageMap != null && testCaseMessageMap.size() > 0) {
      List<String> testNumList = new ArrayList<String>(testCaseMessageMap.keySet());
      Collections.sort(testNumList);
      int count = 0;
      for (String testNum : testNumList) {
        count++;
        TestCaseMessage testCaseMessage = testCaseMessageMap.get(testNum);
        TestCaseMessage tcm = new TestCaseMessage(testCaseMessage);
        register(count, tcm);
      }
    }
  }
}
