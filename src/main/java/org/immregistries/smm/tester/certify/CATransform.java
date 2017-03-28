package org.immregistries.smm.tester.certify;

public class CATransform extends CertifyArea
{

  public static final String TEST_CASE_SET_FOR_MODIFICATION_VERIFICATION = "Modification Verification";

  public CATransform(CertifyRunner certifyRunner) {
    super("N", VALUE_TEST_SECTION_TYPE_TRANSFORM, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    addTestCasesFromSavedSet(TEST_CASE_SET_FOR_MODIFICATION_VERIFICATION, 1);
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