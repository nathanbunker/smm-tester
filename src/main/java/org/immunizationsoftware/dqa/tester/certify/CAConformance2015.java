package org.immunizationsoftware.dqa.tester.certify;

import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class CAConformance2015 extends CertifyArea
{

  public CAConformance2015(CertifyRunner certifyRunner) {
    super("L", VALUE_TEST_SECTION_TYPE_CONFORMANCE_2015, certifyRunner);
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
            if (testCaseMessage.isValidationReportPass()) {
              pass++;
            }
          }
        }
      }
    }
    areaCount[0] = count;
    areaProgressCount[0] = areaCount[0];
    areaScore[0] = makeScore(pass, count);
    areaProgress[0] = 100;
  }

  @Override
  public void prepareQueries() {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendQueries() {
    int count = 0;
    int pass = 0;
    for (TestCaseMessage testCaseMessage : certifyRunner.statusCheckTestCaseList) {
      if (testCaseMessage.isHasRun() && testCaseMessage.getActualMessageResponseType().equals("RSP")
          && !testCaseMessage.isResultNotExpectedToConform()) {
        count++;
        if (testCaseMessage.isValidationReportPass()) {
          pass++;
        }
      }
    }
    areaCount[1] = count;
    areaProgressCount[1] = areaCount[1];
    areaScore[1] = makeScore(pass, count);
    areaProgress[1] = 100;
  }

}
