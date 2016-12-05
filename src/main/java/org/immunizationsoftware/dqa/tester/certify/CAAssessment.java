package org.immunizationsoftware.dqa.tester.certify;

import org.immunizationsoftware.dqa.transform.TestCaseMode;

public class CAAssessment extends CertifyArea
{

  public static final String TEST_CASE_SET_FOR_ASSESSMENT = "Global: Assessment";

  public CAAssessment(CertifyRunner certifyRunner) {
    super("S", VALUE_TEST_SECTION_TYPE_ASSESSMENT, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    addTestCasesFromSavedSetAssessment(TEST_CASE_SET_FOR_ASSESSMENT, TestCaseMode.ASSESSMENT);
    addTestCasesFromSavedSetAssessment(TEST_CASE_SET_FOR_ASSESSMENT, TestCaseMode.DEFAULT);
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
