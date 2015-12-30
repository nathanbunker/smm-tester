package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.tester.transform.Issue;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class CAAdvanced extends CertifyArea
{

  public CAAdvanced(CertifyRunner certifyRunner) {
    super("C", VALUE_TEST_SECTION_TYPE_ADVANCED, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    Map<Integer, List<Issue>> issueMap = new HashMap<Integer, List<Issue>>();
    for (Issue issue : Issue.values()) {
      int priority = issue.getPriority();
      List<Issue> issueList = issueMap.get(priority);
      if (issueList == null) {
        issueList = new ArrayList<Issue>();
        issueMap.put(priority, issueList);
      }
      issueList.add(issue);
    }

    int count = 0;
    for (int i = 0; i < areaScore.length; i++) {
      int priority = i + 1;
      List<Issue> issueList = issueMap.get(priority);
      if (issueList != null && issueList.size() > 0) {
        for (Issue issue : issueList) {
          count++;
          TestCaseMessage testCaseMessage = createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
          testCaseMessage.setDescription(issue.getName());
          testCaseMessage.addCauseIssues(issue.getName());
          registerIfHasIssue(count, priority, testCaseMessage).setAssertResult("Accept or Reject - *");
        }
      }
    }
  }

  public TestCaseMessage registerIfHasIssue(int count, int masterCount, TestCaseMessage testCaseMessage) {
    testCaseMessage.setTestCaseSet(certifyRunner.testCaseSet);
    testCaseMessage.setTestCaseCategoryId(areaLetter + "." + makeTwoDigits(masterCount) + "." + makeTwoDigits(count));
    testCaseMessage.setTestCaseNumber(certifyRunner.uniqueMRNBase + testCaseMessage.getTestCaseCategoryId());
    testCaseMessage.setTestPosition(certifyRunner.incrementingInt.next());
    testCaseMessage.setTestType(VALUE_TEST_TYPE_UPDATE);
    testCaseMessage.setAssertResult("Accept - *");
    certifyRunner.transformer.transform(testCaseMessage);
    if (testCaseMessage.hasIssue()) {
      certifyRunner.register(testCaseMessage);
      updateList.add(testCaseMessage);
    }
    return testCaseMessage;

  }

  @Override
  public void sendUpdates() {
    runUpdates(true);
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
