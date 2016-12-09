package org.immregistries.smm.tester.certify;

public class CANotAccepted extends CertifyArea
{

  public CANotAccepted(CertifyRunner certifyRunner) {
    super("K", VALUE_TEST_SECTION_TYPE_NOT_ACCEPTED, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    int count = 0;
    registerAdminChild("PID: Patient identifier segment is missing", "remove segment PID", ++count, "PID")
        .setAssertResult("Reject");
    registerAdminChild("PID-3: Patient id is missing", "CLEAR PID-3", ++count, "PID-3").setAssertResult("Reject");
    registerAdminChild("PID-5: Patient name is missing", "CLEAR PID-5", ++count, "PID-5").setAssertResult("Reject");
    registerAdminChild("PID-7: Patient dob is missing", "CLEAR PID-7", ++count, "PID-7").setAssertResult("Reject");
    registerAdminChild("PID-7: Patient dob is unreadable", "PID-7=DOB", ++count, "PID-7").setAssertResult("Reject");
    registerAdminChild("PID-7: Patient dob is in the future", "PID-7=[LONG_TIME_FROM_NOW]", ++count, "PID-7")
        .setAssertResult("Reject");
    registerAdminChild("RXA: RXA segment is missing", "remove segment RXA", ++count, "RXA").setAssertResult("Reject");
    registerAdminChild("RXA-3: Vaccination date is missing", "RXA-3=", ++count, "RXA-3").setAssertResult("Reject");
    registerAdminChild("RXA-3: Vaccination date is unreadable", "RXA-3=SHOT DATE", ++count, "RXA-3")
        .setAssertResult("Reject");
    registerAdminChild("RXA-3: Vaccination date set in the future", "RXA-3=[LONG_TIME_FROM_NOW]", ++count, "RXA-3")
        .setAssertResult("Reject");
    registerAdminChild("RXA-5: Vaccination code is missing", "RXA-5=", ++count, "RXA-5").setAssertResult("Reject");
    registerAdminChild("RXA-5: Vaccination code is invalid", "RXA-5=14000BADVALUE", ++count, "RXA-5")
        .setAssertResult("Reject");
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
