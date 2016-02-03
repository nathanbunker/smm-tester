package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_2_R_ADMIN_ADULT;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_4_R_CONSENTED_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_5_P_REFUSED_TODDLER;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_6_P_VARICELLA_HISTORY_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_7_R_COMPLETE_RECORD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import java.util.List;

import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.run.TestRunner;
import org.immunizationsoftware.dqa.transform.Comparison;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;

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
