package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;

import java.util.List;

import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.QueryConverter;
import org.immunizationsoftware.dqa.transform.Comparison;
import org.immunizationsoftware.dqa.transform.ScenarioManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;
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
    addQuerySupport("First Twin", "PID-24=Y\rPID-25=1", ++count);
    {
      String middleNameOriginal = "";
      String middleInitial = "";
      String middleName = "";
      String gender = "";
      TestCaseMessage testCaseMessage1 = updateList
          .get(updateList.size() - 1);
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
  
  public void addQuerySupport(String description, String customTransformation, int count) {
    TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    if (customTransformation != null) {
      testCaseMessage.appendCustomTransformation(customTransformation);
    }
    testCaseMessage.setDescription(description);
    register(count, 1, testCaseMessage);
  }

  @Override
  public void sendUpdates() {
    runUpdates();
  }

  @Override
  public void prepareQueries() {
    int passCount = 0;
    int count = 0;
    for (TestCaseMessage testCaseMessage : updateList) {
      count++;
      {
        TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z32 Complete Immunization History", count,
            testCaseMessage, false, false);
        queryTestCaseMessage.setAssertResult("Match");
      }
      count++;
      {
        TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z42 Evaluated History and Forecast", count,
            testCaseMessage, true, false);
        queryTestCaseMessage.setAssertResult("Match");
      }
      if (testCaseMessage.getDescription().equals("First Twin")) {
        count++;
        {
          TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z31 List of Candidates", count, testCaseMessage,
              false, true);
          queryTestCaseMessage.setAssertResult("List");
        }
        count++;
        {
          TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z33 Too Many", count, testCaseMessage, true, true);
          queryTestCaseMessage.setAssertResult("Too Many");
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
        queryTestCaseMessage.setAssertResult("Not Found");
      }
      count++;
      {
        TestCaseMessage queryTestCaseMessage = prepareQuery("Expecting Z33 Not Found", count, testCaseMessage, true, false);
        queryTestCaseMessage.setAssertResult("Not Found");
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
    registerQuery(count, 1, testCaseMessage, null, queryTestCaseMessage);
    
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
