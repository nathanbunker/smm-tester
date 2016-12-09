package org.immregistries.smm.tester.certify;

import static org.immregistries.smm.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;
import static org.immregistries.smm.transform.ScenarioManager.createTestCaseMessage;

import org.immregistries.smm.transform.TestCaseMessage;

public class CAExtra extends CertifyArea {

  public static final String TEST_CASE_SET_FOR_EXTRA = "Extra";
  public static final String TEST_CASE_SET_FOR_GLOBAL_EXTRA = "Global: Extra";

  public CAExtra(CertifyRunner certifyRunner) {
    super("O", VALUE_TEST_SECTION_TYPE_EXTRA, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    addTestCasesFromSavedSet(TEST_CASE_SET_FOR_EXTRA, 1);
    addTestCasesFromSavedSet(TEST_CASE_SET_FOR_GLOBAL_EXTRA, 2);
    
    int count = 0;
    {
      count++;
      TestCaseMessage testCaseMessage = createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setAssertResult("Accept");
      testCaseMessage.setDescription("Patient alias name listed first followed by legal name");
      testCaseMessage.setFieldName("PID-5");
      testCaseMessage.appendCustomTransformation("PID-5.1#2=[PID-5.1] \nPID-5.2#2=[PID-5.2] \nPID-5.3#2=[PID-5.3] \nPID-5.3#4=[PID-5.4] \nPID-5.7#2=L \nPID-5.1=[PID-6.1] \nPID-5.2=[PID-6.2] \nPID-5.3= \nPID-5.4= \nPID-5.7=A \n");
      register(count, 3, testCaseMessage);
    }
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
