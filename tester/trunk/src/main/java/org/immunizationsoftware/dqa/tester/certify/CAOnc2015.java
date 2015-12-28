package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_1_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_2_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_3_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_4_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_5_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_ONC_2015_IZ_AD_6_R;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class CAOnc2015 extends CertifyArea
{

  private String[] onc2015Scenarios = { SCENARIO_ONC_2015_IZ_AD_1_R, SCENARIO_ONC_2015_IZ_AD_2_R,
      SCENARIO_ONC_2015_IZ_AD_3_R, SCENARIO_ONC_2015_IZ_AD_4_R, SCENARIO_ONC_2015_IZ_AD_5_R,
      SCENARIO_ONC_2015_IZ_AD_6_R };

  public CAOnc2015(CertifyRunner certifyRunner) {
    super("L", VALUE_TEST_SECTION_TYPE_ONC_2015, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    int count = 0;
    TestCaseMessage original = null;
    for (String scenario : onc2015Scenarios) {
      count++;
      TestCaseMessage testCaseMessage = createTestCaseMessage(scenario);
      testCaseMessage.setAssertResult("Accept - *");
      register(count, testCaseMessage);
      if (testCaseMessage.getScenario().equals(SCENARIO_ONC_2015_IZ_AD_3_R)
          || testCaseMessage.getScenario().equals(SCENARIO_ONC_2015_IZ_AD_5_R)) {
        testCaseMessage.setDoNotQueryFor(true);
        original = testCaseMessage;
      } else if (testCaseMessage.getScenario().equals(SCENARIO_ONC_2015_IZ_AD_4_R)
          || testCaseMessage.getScenario().equals(SCENARIO_ONC_2015_IZ_AD_6_R)) {
        testCaseMessage.setUpdateTestCaseMessage(original);
      }
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
