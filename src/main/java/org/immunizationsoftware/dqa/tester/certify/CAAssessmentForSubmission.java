package org.immunizationsoftware.dqa.tester.certify;

import org.immunizationsoftware.dqa.transform.TestCaseMode;

public class CAAssessmentForSubmission extends CertifyArea
{

  public static final String TEST_CASE_SET_FOR_ASSESSMENT_FOR_SUBMISSION = "Global: Assessment for Submission";

  public CAAssessmentForSubmission(CertifyRunner certifyRunner) {
    super("S", VALUE_TEST_SECTION_TYPE_ASSESSMENT_FOR_SUBMISSION, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    addTestCasesFromSavedSetAssessment(TEST_CASE_SET_FOR_ASSESSMENT_FOR_SUBMISSION, TestCaseMode.ASSESSMENT);
    addTestCasesFromSavedSetAssessment(TEST_CASE_SET_FOR_ASSESSMENT_FOR_SUBMISSION, TestCaseMode.DEFAULT);
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
