package org.immunizationsoftware.dqa.tester.certify;

public class CADeduplicationEngaged extends CertifyArea
{

  public CADeduplicationEngaged(CertifyRunner certifyRunner) {
    super("P", VALUE_TEST_SECTION_TYPE_DEDUPLICATION_ENGAGED, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendUpdates() {
    // TODO Auto-generated method stub

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
