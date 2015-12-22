package org.immunizationsoftware.dqa.tester.certify;

public class CATransform extends CertifyArea
{
  
  public CATransform(CertifyRunner certifyRunner)
  {
    super("N", VALUE_TEST_SECTION_TYPE_TRANSFORM, certifyRunner);
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
