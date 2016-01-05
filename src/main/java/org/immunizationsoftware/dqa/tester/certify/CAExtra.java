package org.immunizationsoftware.dqa.tester.certify;

public class CAExtra extends CertifyArea
{

  public static final String TEST_CASE_SET_FOR_EXTRA = "Extra";
  public static final String TEST_CASE_SET_FOR_GLOBAL_EXTRA = "Global: Extra";

  public CAExtra(CertifyRunner certifyRunner) {
    super("O", VALUE_TEST_SECTION_TYPE_EXTRA, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    addTestCasesFromSavedSet(TEST_CASE_SET_FOR_EXTRA);
    addTestCasesFromSavedSet(TEST_CASE_SET_FOR_GLOBAL_EXTRA);
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
