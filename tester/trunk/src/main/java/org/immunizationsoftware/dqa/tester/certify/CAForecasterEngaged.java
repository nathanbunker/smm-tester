package org.immunizationsoftware.dqa.tester.certify;

public class CAForecasterEngaged extends CertifyArea
{

  public CAForecasterEngaged(CertifyRunner certifyRunner) {
    super("Q", VALUE_TEST_SECTION_TYPE_FORECASTER_ENGAGED, certifyRunner);
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
