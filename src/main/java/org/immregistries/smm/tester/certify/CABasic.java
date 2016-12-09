package org.immregistries.smm.tester.certify;

import static org.immregistries.smm.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;
import static org.immregistries.smm.transform.ScenarioManager.SCENARIO_2_R_ADMIN_ADULT;
import static org.immregistries.smm.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_CHILD;
import static org.immregistries.smm.transform.ScenarioManager.SCENARIO_4_R_CONSENTED_CHILD;
import static org.immregistries.smm.transform.ScenarioManager.SCENARIO_5_P_REFUSED_TODDLER;
import static org.immregistries.smm.transform.ScenarioManager.SCENARIO_6_P_VARICELLA_HISTORY_CHILD;
import static org.immregistries.smm.transform.ScenarioManager.SCENARIO_7_R_COMPLETE_RECORD;
import static org.immregistries.smm.transform.ScenarioManager.createTestCaseMessage;

import org.immregistries.smm.transform.TestCaseMessage;

public class CABasic extends CertifyArea
{

  private String[] statusCheckScenarios = { SCENARIO_1_R_ADMIN_CHILD, SCENARIO_2_R_ADMIN_ADULT,
      SCENARIO_3_R_HISTORICAL_CHILD, SCENARIO_4_R_CONSENTED_CHILD, SCENARIO_5_P_REFUSED_TODDLER,
      SCENARIO_6_P_VARICELLA_HISTORY_CHILD, SCENARIO_7_R_COMPLETE_RECORD };

  public CABasic(CertifyRunner certifyRunner) {
    super("A", VALUE_TEST_SECTION_TYPE_BASIC, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    int count = 0;
    for (String scenario : statusCheckScenarios) {
      count++;
      TestCaseMessage testCaseMessage = createTestCaseMessage(scenario);
      testCaseMessage.setAssertResult("Accept");
      register(count, testCaseMessage);
    }
  }

  @Override
  public void sendUpdates() {
    runUpdates();
    if (areaScore[0] == 0) {
      certifyRunner.switchStatus(CertifyRunner.STATUS_PROBLEM, "None of the basic messages passed. Stopping test process. ");
      certifyRunner.keepRunning = false;
    }
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
