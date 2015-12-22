package org.immunizationsoftware.dqa.tester.certify;

public class CAExtra extends CertifyArea
{

  public CAExtra(CertifyRunner certifyRunner) {
    super("O", VALUE_TEST_SECTION_TYPE_EXTRA, certifyRunner);
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
