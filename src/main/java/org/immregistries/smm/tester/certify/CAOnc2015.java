package org.immregistries.smm.tester.certify;

import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.TestCaseMode;

public class CAOnc2015 extends CertifyArea
{

  public static final String TEST_CASE_SET_FOR_ASSESSMENT_FOR_SUBMISSION = "Global: NIST 2015";

  public CAOnc2015(CertifyRunner certifyRunner) {
    super("L", VALUE_TEST_SECTION_TYPE_ONC_2015, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    addTestCasesFromSavedSetAssessment(TEST_CASE_SET_FOR_ASSESSMENT_FOR_SUBMISSION, TestCaseMode.DEFAULT);
    for (TestCaseMessage tcm : updateList)
    {
      if (tcm.getTestCaseNumber().equals("IZ-AD-3") || tcm.getTestCaseNumber().equals("IZ-AD-5"))
      {
        tcm.setDoNotQueryFor(true);
      }
    }
  }

  @Override
  public void sendUpdates() {
    runUpdates();
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
