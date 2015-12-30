package org.immunizationsoftware.dqa.tester.certify;

import org.immunizationsoftware.dqa.tester.manager.TestCaseMessageManager;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class CAConformance extends CertifyArea
{

  public CAConformance(CertifyRunner certifyRunner) {
    super("H", VALUE_TEST_SECTION_TYPE_CONFORMANCE, certifyRunner);
    performanceConformance = true;
  }

  @Override
  public void prepareUpdates() {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendUpdates() {
    logStatus("Analyze format updates");
    int count = 0;
    int pass = 0;
    for (CertifyArea certifyArea : certifyRunner.certifyAreas) {
      if (certifyArea.isRun() && !certifyArea.isPerformanceConformance()) {
        for (TestCaseMessage testCaseMessage : certifyArea.updateList) {
          if (testCaseMessage.isHasRun() && testCaseMessage.getActualMessageResponseType().equals("ACK")
              && !testCaseMessage.isResultNotExpectedToConform()) {
            count++;
            HL7Component comp = TestCaseMessageManager.createHL7Component(testCaseMessage);
            incrementUpdateProgress();
            if (comp != null && comp.hasNoErrors()) {
              pass++;
            }
          }
        }
      }
    }
    areaCount[0] = count;
    areaProgressCount[0] = areaCount[0];
    areaProgress[0] = 100;
    areaScore[0] = makeScore(pass, count);
  }

  @Override
  public void prepareQueries() {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendQueries() {
    int count = 0;
    int pass = 0;
    for (CertifyArea certifyArea : certifyRunner.certifyAreas) {
      if (certifyArea.isRun() && !certifyArea.isPerformanceConformance()) {
        for (TestCaseMessage testCaseMessage : certifyArea.queryList) {
          if (testCaseMessage.isHasRun() && testCaseMessage.getActualMessageResponseType().equals("RSP")
              && !testCaseMessage.isResultNotExpectedToConform()) {
            count++;
            HL7Component comp = TestCaseMessageManager.createHL7Component(testCaseMessage);
            incrementQueryProgress();
            if (comp != null && comp.hasNoErrors()) {
              pass++;
            }
          }
        }
      }
    }
    areaProgressCount[1] = areaCount[1];
    areaProgress[1] = makeScore(pass, count);
    areaScore[1] = 100;
  }

}
