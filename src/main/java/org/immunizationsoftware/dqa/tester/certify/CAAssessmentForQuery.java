package org.immunizationsoftware.dqa.tester.certify;

import org.immunizationsoftware.dqa.transform.TestCaseMode;

public class CAAssessmentForQuery extends CertifyArea
{

  public static final String TEST_CASE_SET_FOR_ASSESSMENT_FOR_QUERY = "Global: Assessment for Query";

  public CAAssessmentForQuery(CertifyRunner certifyRunner) {
    super("T", VALUE_TEST_SECTION_TYPE_ASSESSMENT_FOR_QUERY, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    addTestCasesFromSavedSetAssessment(TEST_CASE_SET_FOR_ASSESSMENT_FOR_QUERY, TestCaseMode.ASSESSMENT);
    addTestCasesFromSavedSetAssessment(TEST_CASE_SET_FOR_ASSESSMENT_FOR_QUERY, TestCaseMode.DEFAULT);
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
