package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;

import org.immunizationsoftware.dqa.tester.manager.QueryConverter;
import org.immunizationsoftware.dqa.transform.ScenarioManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.openimmunizationsoftware.dqa.tr.RecordServletInterface;

public class CAQbpSupport extends CertifyArea
{

  public CAQbpSupport(CertifyRunner certifyRunner) {
    super("M", VALUE_TEST_SECTION_TYPE_QBP_SUPPORT, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    int count = 0;
    addQuerySupport("Base Message", null, ++count);
    addTwins(count);
  }


  @Override
  public void sendUpdates() {
    runUpdates();
  }

  @Override
  public void prepareQueries() {
    int count = 0;
    for (TestCaseMessage testCaseMessage : updateList) {
      count++;
      {
        TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z32 Complete Immunization History", count,
            testCaseMessage, false, false);
        queryTestCaseMessage.setAssertResult(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH);
      }
      count++;
      {
        TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z42 Evaluated History and Forecast", count,
            testCaseMessage, true, false);
        queryTestCaseMessage.setAssertResult(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH);
      }
      if (testCaseMessage.getDescription().equals("First Twin")) {
        count++;
        {
          TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z31 List of Candidates", count, testCaseMessage,
              false, true);
          queryTestCaseMessage.setAssertResult(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_LIST);
        }
        count++;
        {
          TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z33 Too Many", count, testCaseMessage, true, true);
          queryTestCaseMessage.setAssertResult(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_TOO_MANY);
        }
      }
    }
    {
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      certifyRunner.transformer.transform(testCaseMessage);
      testCaseMessage.setDescription("Unsubmitted Patient");

      count++;
      // Z42 - return evaluated history and forecast
      // Z33 - Return an ack with no person records
      // Z31 - Return a list of candidates profile
      // Z32 - Return complete immunization history
      {
        TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z33 Not Found", count, testCaseMessage, false,
            false);
        queryTestCaseMessage.setAssertResult(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND);
      }
      count++;
      {
        TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z33 Not Found", count, testCaseMessage, true, false);
        queryTestCaseMessage.setAssertResult(RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND);
      }
    }
  }
  
  public TestCaseMessage prepareQuery(String description, int count, TestCaseMessage testCaseMessage, boolean z44,
      boolean removeMultipleBirth) {
    TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
    queryTestCaseMessage.setDescription(description + " for " + testCaseMessage.getDescription());
    if (z44) {
      queryTestCaseMessage.setMessageText(QueryConverter.convertVXUtoQBPZ44(testCaseMessage.getMessageText()));
    } else {
      queryTestCaseMessage.setMessageText(QueryConverter.convertVXUtoQBPZ34(testCaseMessage.getMessageText()));
    }
    registerQuery(count, 1, testCaseMessage, queryTestCaseMessage);
    
    if (removeMultipleBirth) {
      queryTestCaseMessage.appendCustomTransformation("QPD-4.3=");
      queryTestCaseMessage.appendCustomTransformation("clear QPD-10");
      queryTestCaseMessage.appendCustomTransformation("clear QPD-11");
      certifyRunner.transformer.transform(queryTestCaseMessage);
    }
    return queryTestCaseMessage;
  }

  @Override
  public void sendQueries() {
    runQueries();
  }

}
