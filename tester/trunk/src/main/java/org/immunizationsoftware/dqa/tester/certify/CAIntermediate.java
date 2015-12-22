package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_FOUR_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_TWELVE_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_TWO_MONTHS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_TWO_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_FOUR_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_TWELVE_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_TWO_MONTHS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_3_R_HISTORICAL_TWO_YEARS_OLD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_5_P_REFUSED_TODDLER;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_6_P_VARICELLA_HISTORY_CHILD;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import java.util.List;

import org.immunizationsoftware.dqa.tester.Certify;
import org.immunizationsoftware.dqa.transform.ScenarioManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class CAIntermediate extends CertifyArea
{

  public CAIntermediate(CertifyRunner certifyRunner) {
    super("B", VALUE_TEST_SECTION_TYPE_INTERMEDIATE, certifyRunner);
  }

  @Override
  public void prepareUpdates() {


    int masterCount = 0;
    int count;
    List<Certify.CertifyItem> certifyItems;
    Certify certify = new Certify();

    certifyItems = certify.getCertifyItemList(Certify.FIELD_SEX);
    count = 0;
    masterCount++;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Sex is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-8=" + certifyItem.getCode());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_RACE);
    count = 0;
    masterCount++;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Race is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-10=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-10.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-10.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_RACE_FULL);
    count = 0;
    masterCount++;
    {
      Certify.CertifyItem certifyItem = certifyItems.get((int) (System.currentTimeMillis() % certifyItems.size()));
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Race is " + certifyItem.getLabel() + " (Random value from CDC full set)");
      testCaseMessage.appendCustomTransformation("PID-10=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-10.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-10.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ETHNICITY);
    count = 0;
    masterCount++;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Ethnicity is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-22=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-22.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-22.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_HISTORICAL);
    count = 0;
    masterCount++;
    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage;
      if (count == 1) {
        testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      } else {
        testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_CHILD);
      }

      testCaseMessage.setDescription("Information Source is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-9=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-9.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-9.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VFC);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("VFC Status is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("OBX-5=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("OBX-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("OBX-5.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_HOD);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_6_P_VARICELLA_HISTORY_CHILD);
      testCaseMessage.setDescription("History of Disease is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("OBX-5=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("OBX-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("OBX-5.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_REGISTRY_STATUS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Registry Status is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PD1-16=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PD1-17=[NOW]");
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_REFUSAL_REASON);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_5_P_REFUSED_TODDLER);
      testCaseMessage.setDescription("Refusal Reason is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-18=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-18.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-18.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_RELATIONSHIP);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Relationship is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("NK1-3.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("NK1-3.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("NK1-3.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_HISTORICAL_TWO_MONTHS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_TWO_MONTHS_OLD);
      testCaseMessage.setDescription("Historical vaccination is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_HISTORICAL_TWO_YEARS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_TWO_YEARS_OLD);
      testCaseMessage.setDescription("Historical vaccination is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_HISTORICAL_FOUR_YEARS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_FOUR_YEARS_OLD);
      testCaseMessage.setDescription("Historical vaccination is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_VAC_HISTORICAL_TWELVE_YEARS);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_3_R_HISTORICAL_TWELVE_YEARS_OLD);
      testCaseMessage.setDescription("Historical vaccination is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-5.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    String[][] adminChecks = { { "two months", Certify.FIELD_VAC_ADMIN_TWO_MONTHS, SCENARIO_1_R_ADMIN_TWO_MONTHS_OLD },
        { "two years", Certify.FIELD_VAC_ADMIN_TWO_YEARS, SCENARIO_1_R_ADMIN_TWO_YEARS_OLD },
        { "four years", Certify.FIELD_VAC_ADMIN_FOUR_YEARS, SCENARIO_1_R_ADMIN_FOUR_YEARS_OLD },
        { "twelve years", Certify.FIELD_VAC_ADMIN_TWELVE_YEARS, SCENARIO_1_R_ADMIN_TWELVE_YEARS_OLD } };

    List<Certify.CertifyItem> productItems = certify.getCertifyItemList(Certify.FIELD_VAC_PRODUCT);
    List<Certify.CertifyItem> mvxItems = certify.getCertifyItemList(Certify.FIELD_MVX);
    List<Certify.CertifyItem> cptItems = certify.getCertifyItemList(Certify.FIELD_CPT);
    for (int i = 0; i < adminChecks.length; i++) {
      certifyItems = certify.getCertifyItemList(adminChecks[i][1]);
      count = 0;
      masterCount++;

      for (Certify.CertifyItem certifyItem : certifyItems) {
        for (Certify.CertifyItem productCi : productItems) {
          if (productCi.getLabel().equals(certifyItem.getCode())) {
            Certify.CertifyItem mvxItem = null;
            for (Certify.CertifyItem ci : mvxItems) {
              if (ci.getCode().equalsIgnoreCase(productCi.getTable())) {
                mvxItem = ci;
                break;
              }
            }
            if (mvxItem == null) {
              mvxItem = new Certify.CertifyItem();
            }
            {
              count++;
              TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(adminChecks[i][2]);
              testCaseMessage.setDescription("Vaccination administered at " + adminChecks[i][0] + " of age is "
                  + certifyItem.getLabel() + " (" + productCi.getCode() + ")");
              testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
              testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
              testCaseMessage.appendCustomTransformation("RXA-5.3=CVX");
              testCaseMessage.appendCustomTransformation("RXA-17.1=" + mvxItem.getCode());
              testCaseMessage.appendCustomTransformation("RXA-17.2=" + mvxItem.getLabel());
              testCaseMessage.appendCustomTransformation("RXA-17.3=" + mvxItem.getTable());
              register(count, masterCount, testCaseMessage);
            }

            for (Certify.CertifyItem cptCi : cptItems) {
              if (cptCi.getTable().equals(certifyItem.getCode())) {
                {
                  count++;
                  TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(adminChecks[i][2]);
                  testCaseMessage.setDescription("Vaccination administered at " + adminChecks[i][0] + " of age is CPT "
                      + cptCi.getLabel() + " (" + productCi.getCode() + ")");
                  testCaseMessage.appendCustomTransformation("RXA-5.1=" + certifyItem.getCode());
                  testCaseMessage.appendCustomTransformation("RXA-5.2=" + certifyItem.getLabel());
                  testCaseMessage.appendCustomTransformation("RXA-5.3=CVX");
                  testCaseMessage.appendCustomTransformation("RXA-5.4=" + cptCi.getCode());
                  testCaseMessage.appendCustomTransformation("RXA-5.5=" + cptCi.getLabel());
                  testCaseMessage.appendCustomTransformation("RXA-5.6=CPT");
                  testCaseMessage.appendCustomTransformation("RXA-17.1=" + mvxItem.getCode());
                  testCaseMessage.appendCustomTransformation("RXA-17.2=" + mvxItem.getLabel());
                  testCaseMessage.appendCustomTransformation("RXA-17.3=" + mvxItem.getTable());
                  register(count, masterCount, testCaseMessage);
                }
              }
            }
          }
        }
      }
    }
    certifyItems = certify.getCertifyItemList(Certify.FIELD_BODY_ROUTE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Body Route is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXR-1.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXR-1.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXR-1.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_BODY_SITE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Body Site is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXR-2.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("RXR-2.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXR-2.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ADDRESS_TYPE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Additional Address Type is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-11.1#2=PID-11.1");
      testCaseMessage.appendCustomTransformation("PID-11.2#2=PID-11.2");
      testCaseMessage.appendCustomTransformation("PID-11.3#2=PID-11.3");
      testCaseMessage.appendCustomTransformation("PID-11.4#2=PID-11.4");
      testCaseMessage.appendCustomTransformation("PID-11.5#2=PID-11.5");
      testCaseMessage.appendCustomTransformation("PID-11.7#2=" + certifyItem.getCode());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_NAME_TYPE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Additional Name Type is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-5.1#2=[FATHER]");
      testCaseMessage.appendCustomTransformation("PID-5.2#2=[LAST_DIFFERENT]");
      testCaseMessage.appendCustomTransformation("PID-5.7#2=" + certifyItem.getCode());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_TEL_USE_CODE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Phone Telcommunications Use Code is " + certifyItem.getLabel());
      if (certifyItem.getCode().equalsIgnoreCase("NET")) {
        testCaseMessage.appendCustomTransformation("PID-13.3#2=X.400");
        testCaseMessage.appendCustomTransformation("PID-13.4#2=[EMAIL]");
        testCaseMessage.appendCustomTransformation("PID-13.2#2=" + certifyItem.getCode());
      } else if (certifyItem.getCode().equalsIgnoreCase("BPN")) {
        testCaseMessage.appendCustomTransformation("PID-13.2#2=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PID-13.3#2=BP");
      } else {
        testCaseMessage.appendCustomTransformation("PID-13.2=" + certifyItem.getCode());
      }
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_TEL_EQUIPMENT_TYPE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Phone Telcommunications Equipment Type is " + certifyItem.getLabel());
      if (certifyItem.getCode().equals("BP")) {
        testCaseMessage.appendCustomTransformation("PID-13.2#2=BPN");
        testCaseMessage.appendCustomTransformation("PID-13.3#2=" + certifyItem.getCode());
      } else if (certifyItem.getCode().equalsIgnoreCase("X.400")) {
        testCaseMessage.appendCustomTransformation("PID-13.2#2=NET");
        testCaseMessage.appendCustomTransformation("PID-13.3#2=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PID-13.4#2=[EMAIL]");
      } else if (certifyItem.getCode().equalsIgnoreCase("Internet")) {
        testCaseMessage.appendCustomTransformation("PID-13.2#2=NET");
        testCaseMessage.appendCustomTransformation("PID-13.3#2=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PID-13.4#2=http://openimmunizationsoftware.net/");
      } else {
        testCaseMessage.appendCustomTransformation("PID-13.3=" + certifyItem.getCode());
      }
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ID_TYPE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      if (certifyItem.getCode().equals("MR")) {
        // do not test this here, already being tested by messages
        continue;
      }
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Person Id Type is " + certifyItem.getLabel());
      if (certifyItem.getCode().equals("MA")) {
        testCaseMessage.appendCustomTransformation("PID-3.1#2=[MEDICAID]");
      } else if (certifyItem.getCode().equals("SS")) {
        testCaseMessage.appendCustomTransformation("PID-3.1#2=[SSN]");
      } else {
        testCaseMessage.appendCustomTransformation("PID-3.1#2=[MRN]");
      }
      testCaseMessage.appendCustomTransformation("PID-3.4#2=OIS");
      testCaseMessage.appendCustomTransformation("PID-3.5#2=" + certifyItem.getCode());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_PUBLICITY_CODE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Publicity Code is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PD1-11.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PD1-11.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PD1-11.3=" + certifyItem.getTable());

      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_COUNTY_CODE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("County Code is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-11.4=" + certifyItem.getTable());
      testCaseMessage.appendCustomTransformation("PID-11.9=" + certifyItem.getCode());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_LANGUAGE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Primary Language is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-15.1=" + certifyItem.getCode());
      testCaseMessage.appendCustomTransformation("PID-15.2=" + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("PID-15.3=" + certifyItem.getTable());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_COMPLETION);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = null;
      String completionStatus = certifyItem.getCode();
      if (completionStatus.equals("CP") || completionStatus.equals("PA")) {
        testCaseMessage = createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      } else if (completionStatus.equals("NA")) {
        testCaseMessage = createTestCaseMessage(SCENARIO_5_P_REFUSED_TODDLER);
        testCaseMessage.appendCustomTransformation("RXA-5.1=998");
        testCaseMessage.appendCustomTransformation("RXA-5.2=Not administered");
      } else {
        testCaseMessage = createTestCaseMessage(SCENARIO_5_P_REFUSED_TODDLER);
      }
      testCaseMessage.setDescription("Vaccination completion is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-20.1=" + completionStatus);
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ACTION);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Vaccination Action Code is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-21=" + certifyItem.getCode());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_ACTION);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Vaccination Action is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-21=" + certifyItem.getCode());
      register(count, masterCount, testCaseMessage);
    }

    certifyItems = certify.getCertifyItemList(Certify.FIELD_DEGREE);
    count = 0;
    masterCount++;

    for (Certify.CertifyItem certifyItem : certifyItems) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Vaccination Administering Provider's Degree is " + certifyItem.getLabel());
      testCaseMessage.appendCustomTransformation("RXA-10.2=[LAST_DIFFERENT]");
      testCaseMessage.appendCustomTransformation("RXA-10.3=[FATHER]");
      testCaseMessage.appendCustomTransformation("RXA-10.21=" + certifyItem.getCode());
      register(count, masterCount, testCaseMessage);
    }

    count = 0;
    masterCount++;
    for (int i = 1; i <= 4; i++) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Multiple Birth is First of " + i);
      if (i == 1) {
        testCaseMessage.appendCustomTransformation("PID-24=N");
        testCaseMessage.appendCustomTransformation("PID-25=");
      } else {
        testCaseMessage.appendCustomTransformation("PID-24=Y");
        testCaseMessage.appendCustomTransformation("PID-25=1");
      }
      register(count, masterCount, testCaseMessage);

    }

    count = 0;
    masterCount++;
    for (int i = 2; i <= 4; i++) {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Multiple Birth is Last of " + i);
      testCaseMessage.appendCustomTransformation("PID-24=Y");
      testCaseMessage.appendCustomTransformation("PID-25=" + i);
      register(count, masterCount, testCaseMessage);
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
