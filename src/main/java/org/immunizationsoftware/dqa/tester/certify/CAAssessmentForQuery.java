package org.immunizationsoftware.dqa.tester.certify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.TestCaseMode;

public class CAAssessmentForQuery extends CertifyArea
{

  public static final String TEST_CASE_SET_FOR_ASSESSMENT_FOR_QUERY = "Global: Assessment for Query";
  private Map<String, TestCaseMessage> updateTestCaseMessageMap = new HashMap<String, TestCaseMessage>();
  private Map<String, TestCaseMessage> allTestCaseMessageMap = new HashMap<String, TestCaseMessage>();

  public CAAssessmentForQuery(CertifyRunner certifyRunner) {
    super("T", VALUE_TEST_SECTION_TYPE_ASSESSMENT_FOR_QUERY, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    updateTestCaseMessageMap.clear();
    allTestCaseMessageMap.clear();
    addTestCasesFromSavedSetAssessment(TEST_CASE_SET_FOR_ASSESSMENT_FOR_QUERY, TestCaseMode.DEFAULT, "VXU");
    for (TestCaseMessage tcm : updateList)
    {
      System.out.println("--> --------------------------------------------------------------------- ");
      System.out.println("--> tcm.getTestCaseNumber() = '" + tcm.getTestCaseNumberOriginal() + "'");
      updateTestCaseMessageMap.put(tcm.getTestCaseNumberOriginal(), tcm);
    }
  }


  @Override
  public void sendUpdates() {
    runUpdates(true);
  }

  @Override
  public void prepareQueries() {
    allTestCaseMessageMap.putAll(updateTestCaseMessageMap);
    // do something special here
    TestCaseMode testCaseMode = TestCaseMode.DEFAULT;
    Map<String, TestCaseMessage> testCaseMessageMap = certifyRunner.testMessageMapMap.get(TEST_CASE_SET_FOR_ASSESSMENT_FOR_QUERY);
    if (testCaseMessageMap != null && testCaseMessageMap.size() > 0) {
      List<String> testNumList = new ArrayList<String>(testCaseMessageMap.keySet());
      Collections.sort(testNumList);
      for (String testNum : testNumList) {
        TestCaseMessage queryTestCaseMessage = testCaseMessageMap.get(testNum);
        if ("QBP" != null && !"QBP".equalsIgnoreCase(queryTestCaseMessage.getMessageType())) {
          continue;
        }
        System.out.println("--> ================================================================================");
        System.out.println("--> queryTestCaseMessage.getTestCaseNumberOriginal() = '" + queryTestCaseMessage.getDerivedFromTestCaseNumber() +"'");
        TestCaseMessage tcm = updateTestCaseMessageMap.get(queryTestCaseMessage.getDerivedFromTestCaseNumber());
        TestCaseMessage queryTcm = new TestCaseMessage(queryTestCaseMessage);
        allTestCaseMessageMap.put(queryTcm.getTestCaseNumberOriginal(), queryTcm);
        queryTcm.setTestCaseMode(testCaseMode);
        queryTcm.registerTestCaseMap(allTestCaseMessageMap);
        String testCaseId = queryTestCaseMessage.getTestCaseNumber();
        if (testCaseMode == TestCaseMode.DEVIATES) {
          testCaseId += "d";
          queryTestCaseMessage.setDescription(queryTestCaseMessage.getDescription() + " (Deviates)");
        }
        registerQuery(testCaseId, tcm, queryTcm);
      }
    }
  }

  @Override
  public void sendQueries() {
    runQueries();
  }

}
