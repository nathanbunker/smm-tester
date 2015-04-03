package org.immunizationsoftware.dqa.tester.profile;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_FULL_RECORD_FOR_PROFILING;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.SoftwareVersion;
import org.immunizationsoftware.dqa.tester.manager.CvsReader;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class ProfileManager
{

  public static TestCaseMessage getPresentTestCase(ProfileField field, TestCaseMessage defaultTestCaseMessage) {
    TestCaseMessage testCaseMessage = null;
    if (testCaseMessage == null) {
      testCaseMessage = createTestCaseMessage(SCENARIO_FULL_RECORD_FOR_PROFILING);
    }
    testCaseMessage.setHasIssue(false);

    switch (field) {

    case MSH_3:
      testCaseMessage.appendAdditionalTransformation("MSH-3=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;

    case MSH_3_1:
      testCaseMessage.appendAdditionalTransformation("MSH-3=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;

    case MSH_3_2:
      testCaseMessage.appendAdditionalTransformation("MSH-3.1=[MAP ''=>'TEST']");
      testCaseMessage.appendAdditionalTransformation("MSH-3.2=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      
      break;
    case MSH_4:
      testCaseMessage.appendAdditionalTransformation("MSH-4=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_4_1:
      testCaseMessage.appendAdditionalTransformation("MSH-4=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_4_2:
      testCaseMessage.appendAdditionalTransformation("MSH-4.1=[MAP ''=>'TEST']");
      testCaseMessage.appendAdditionalTransformation("MSH-4.2=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_5:
      testCaseMessage.appendAdditionalTransformation("MSH-5=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_5_1:
      testCaseMessage.appendAdditionalTransformation("MSH-5=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_5_2:
      testCaseMessage.appendAdditionalTransformation("MSH-5.1=[MAP ''=>'TEST']");
      testCaseMessage.appendAdditionalTransformation("MSH-5.2=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_6:
      testCaseMessage.appendAdditionalTransformation("MSH-6=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_6_1:
      testCaseMessage.appendAdditionalTransformation("MSH-6=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_6_2:
      testCaseMessage.appendAdditionalTransformation("MSH-6.1=[MAP ''=>'TEST']");
      testCaseMessage.appendAdditionalTransformation("MSH-6.2=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_7:
      return defaultTestCaseMessage;
      
    case MSH_7_TZ:
      return defaultTestCaseMessage;
      
    case MSH_8:
      testCaseMessage.appendAdditionalTransformation("MSH-8=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_9:
      return defaultTestCaseMessage;
      
    case MSH_10:
      return defaultTestCaseMessage;
      
    case MSH_11:
      testCaseMessage.appendAdditionalTransformation("MSH-11=[MAP ''=>'T']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_12:
      testCaseMessage.appendAdditionalTransformation("MSH-12=[MAP ''=>'2.5.1']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_15:
      testCaseMessage.appendAdditionalTransformation("MSH-16=[MAP ''=>'AL']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_16:
      testCaseMessage.appendAdditionalTransformation("MSH-16=[MAP ''=>'AL']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_17:
      testCaseMessage.appendAdditionalTransformation("MSH-17=[MAP ''=>'USA']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_22:
      testCaseMessage.appendAdditionalTransformation("MSH-22=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case MSH_23:
      testCaseMessage.appendAdditionalTransformation("MSH-23=[MAP ''=>'TEST']");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID:
      return defaultTestCaseMessage;
      
    case PID_1:
      testCaseMessage.appendAdditionalTransformation("PID-1=1");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_3:
      return defaultTestCaseMessage;
      
    case PID_3_1:
      return defaultTestCaseMessage;
      
    case PID_3_4:
      return defaultTestCaseMessage;
      
    case PID_3_5:
      return defaultTestCaseMessage;
      
    case PID_3_SSN:
      testCaseMessage.appendAdditionalTransformation("PID-3.1#2=[SSN]");
      testCaseMessage.appendAdditionalTransformation("PID-3.4#2=USA");
      testCaseMessage.appendAdditionalTransformation("PID-3.5#2=SS");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_3_MEDICAID:
      testCaseMessage.appendAdditionalTransformation("PID-3.1#2=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("PID-3.4#2=MI");
      testCaseMessage.appendAdditionalTransformation("PID-3.5#2=MA");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_5:
      return defaultTestCaseMessage;
      
    case PID_5_1:
      return defaultTestCaseMessage;
      
    case PID_5_2:
      return defaultTestCaseMessage;
      
    case PID_5_3:
      testCaseMessage.appendAdditionalTransformation("PID-5.3=[BOY_MIDDLE]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_5_4:
      testCaseMessage.appendAdditionalTransformation("PID-5.4=Jr");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_5_7:
      testCaseMessage.appendAdditionalTransformation("PID-5.7=L");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_6:
      return defaultTestCaseMessage;
      
    case PID_6_1:
      return defaultTestCaseMessage;
      
    case PID_6_2:
      return defaultTestCaseMessage;
      
    case PID_6_3:
      testCaseMessage.appendAdditionalTransformation("PID-6.3=[GIRL_MIDDLE]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_6_7:
      testCaseMessage.appendAdditionalTransformation("PID-6.7=M");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_7:
      return defaultTestCaseMessage;
      
    case PID_8:
      return defaultTestCaseMessage;
      
    case PID_10:
      return defaultTestCaseMessage;
      
    case PID_11:
      return defaultTestCaseMessage;
      
    case PID_11_1:
      return defaultTestCaseMessage;
      
    case PID_11_2:
      return defaultTestCaseMessage;
      
    case PID_11_3:
      return defaultTestCaseMessage;
      
    case PID_11_4:
      return defaultTestCaseMessage;
      
    case PID_11_5:
      return defaultTestCaseMessage;
      
    case PID_11_6:
      return defaultTestCaseMessage;
      
    case PID_11_7:
      return defaultTestCaseMessage;
      
    case PID_11_9:
      testCaseMessage.appendAdditionalTransformation("PID-11.9=26001");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_13:
      return defaultTestCaseMessage;
      
    case PID_13_1:
      testCaseMessage.appendAdditionalTransformation("PID-13=[PHONE]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_13_2:
      return defaultTestCaseMessage;
      
    case PID_13_3:
      return defaultTestCaseMessage;
      
    case PID_13_6:
      return defaultTestCaseMessage;
      
    case PID_13_7:
      return defaultTestCaseMessage;
      
    case PID_13_EMAIL:
      return defaultTestCaseMessage;
      
    case PID_14:
      return defaultTestCaseMessage;
      
    case PID_14_1:
      testCaseMessage.appendAdditionalTransformation("PID-14=[PHONE_ALT]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_14_2:
      return defaultTestCaseMessage;
      
    case PID_14_3:
      return defaultTestCaseMessage;
      
    case PID_14_6:
      return defaultTestCaseMessage;
      
    case PID_14_7:
      return defaultTestCaseMessage;
      
    case PID_15:
      return defaultTestCaseMessage;
      
    case PID_22:
      return defaultTestCaseMessage;
      
    case PID_24:
      testCaseMessage.appendAdditionalTransformation("PID-24=Y");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_25:
      testCaseMessage.appendAdditionalTransformation("PID-24=Y");
      testCaseMessage.appendAdditionalTransformation("PID-25=2");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_29:
      testCaseMessage.appendAdditionalTransformation("PID-29=[TODAY]");
      testCaseMessage.appendAdditionalTransformation("PID-30=Y");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_30:
      testCaseMessage.appendAdditionalTransformation("PID-30=Y");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1:
      return defaultTestCaseMessage;
      
    case PD1_3:
      testCaseMessage.appendAdditionalTransformation("PD1-3.1=[RESPONSIBLE_ORG_NAME]");
      testCaseMessage.appendAdditionalTransformation("PD1-3.6=IIS");
      testCaseMessage.appendAdditionalTransformation("PD1-3.7=FI");
      testCaseMessage.appendAdditionalTransformation("PD1-3.10=[RESPONSIBLE_ORG_ID]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_3_1:
      testCaseMessage.appendAdditionalTransformation("PD1-3.1=[RESPONSIBLE_ORG_NAME]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_3_2:
      testCaseMessage.appendAdditionalTransformation("PD1-3.1=[RESPONSIBLE_ORG_NAME]");
      testCaseMessage.appendAdditionalTransformation("PD1-3.2=[RESPONSIBLE_ORG_NAME]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_3_3:
      testCaseMessage.appendAdditionalTransformation("PD1-3.1=[RESPONSIBLE_ORG_NAME]");
      testCaseMessage.appendAdditionalTransformation("PD1-3.1=[RESPONSIBLE_ORG_ID]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_3_6:
      testCaseMessage.appendAdditionalTransformation("PD1-3.6=IIS");
      testCaseMessage.appendAdditionalTransformation("PD1-3.7=FI");
      testCaseMessage.appendAdditionalTransformation("PD1-3.10=[RESPONSIBLE_ORG_ID]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_3_7:
      testCaseMessage.appendAdditionalTransformation("PD1-3.6=IIS");
      testCaseMessage.appendAdditionalTransformation("PD1-3.7=FI");
      testCaseMessage.appendAdditionalTransformation("PD1-3.10=[RESPONSIBLE_ORG_ID]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_3_10:
      testCaseMessage.appendAdditionalTransformation("PD1-3.6=IIS");
      testCaseMessage.appendAdditionalTransformation("PD1-3.7=FI");
      testCaseMessage.appendAdditionalTransformation("PD1-3.10=[RESPONSIBLE_ORG_ID]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_4:
      testCaseMessage.appendAdditionalTransformation("PD1-4.1=[ORDERED_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.2=[ORDERED_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.3=[ORDERED_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.4=[ORDERED_BY_MIDDLE]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.9=CMS");
      testCaseMessage.appendAdditionalTransformation("PD1-4.10=L");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_4_1:
      testCaseMessage.appendAdditionalTransformation("PD1-4.1=[ORDERED_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.9=CMS");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_4_2:
      testCaseMessage.appendAdditionalTransformation("PD1-4.2=[ORDERED_BY_LAST]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_4_3:
      testCaseMessage.appendAdditionalTransformation("PD1-4.3=[ORDERED_BY_FIRST]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_4_4:
      testCaseMessage.appendAdditionalTransformation("PD1-4.4=[ORDERED_BY_MIDDLE]");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_4_9:
      testCaseMessage.appendAdditionalTransformation("PD1-4.1=[ORDERED_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.9=CMS");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_4_10:
      testCaseMessage.appendAdditionalTransformation("PD1-4.2=[ORDERED_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.3=[ORDERED_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.10=L");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_4_13:
      testCaseMessage.appendAdditionalTransformation("PD1-4.1=[ORDERED_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.9=CMS");
      testCaseMessage.appendAdditionalTransformation("PD1-4.13=NPI");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_4_21:
      testCaseMessage.appendAdditionalTransformation("PD1-4.2=[ORDERED_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.3=[ORDERED_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("PD1-4.10=L");
      testCaseMessage.appendAdditionalTransformation("PD1-4.21=Dr");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PD1_11:
      return defaultTestCaseMessage;

    case PD1_12:
      return defaultTestCaseMessage;

    case PD1_13:
      return defaultTestCaseMessage;

    case PD1_16:
      return defaultTestCaseMessage;

    case PD1_17:
      return defaultTestCaseMessage;

    case PD1_18:
      return defaultTestCaseMessage;

    case NK1_1:
      return defaultTestCaseMessage;

    case NK1:
      return defaultTestCaseMessage;

    case NK1_2:
      return defaultTestCaseMessage;

    case NK1_2_1:
      return defaultTestCaseMessage;

    case NK1_2_2:
      return defaultTestCaseMessage;

    case NK1_2_3:
      return defaultTestCaseMessage;

    case NK1_2_7:
      return defaultTestCaseMessage;

    case NK1_3:
      return defaultTestCaseMessage;

    case NK1_4:
      return defaultTestCaseMessage;

    case NK1_4_1:
      return defaultTestCaseMessage;

    case NK1_4_2:
      return defaultTestCaseMessage;

    case NK1_4_3:
      return defaultTestCaseMessage;

    case NK1_4_4:
      return defaultTestCaseMessage;

    case NK1_4_5:
      return defaultTestCaseMessage;

    case NK1_4_6:
      return defaultTestCaseMessage;

    case NK1_4_7:
      return defaultTestCaseMessage;

    case NK1_5:
      return defaultTestCaseMessage;

    case NK1_5_1:
      testCaseMessage.appendAdditionalTransformation("NK1-5.1=[PHONE]");
      testCaseMessage.setHasIssue(true);
      break;

    case NK1_5_2:
      return defaultTestCaseMessage;

    case NK1_5_3:
      return defaultTestCaseMessage;

    case NK1_5_6:
      return defaultTestCaseMessage;

    case NK1_5_7:
      return defaultTestCaseMessage;

    case NK1_6:
      return defaultTestCaseMessage;

    case NK1_7:
      testCaseMessage.appendAdditionalTransformation("NK1-7=N");
      testCaseMessage.appendAdditionalTransformation("NK1-7=Next-of-Kin");
      testCaseMessage.appendAdditionalTransformation("NK1-7=HL70131");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1:
      return defaultTestCaseMessage;

    case PV1_1:
      return defaultTestCaseMessage;

    case PV1_2:
      return defaultTestCaseMessage;

    case PV1_7:
      testCaseMessage.appendAdditionalTransformation("PD1-7.1=[ORDERED_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("PD1-7.9=CMS");
      testCaseMessage.appendAdditionalTransformation("PD1-7.2=[ORDERED_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("PD1-7.3=[ORDERED_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("PD1-7.4=[ORDERED_BY_MIDDLE]");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1_7_1:
      testCaseMessage.appendAdditionalTransformation("PD1-7.1=[ORDERED_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("PD1-7.9=CMS");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1_7_2:
      testCaseMessage.appendAdditionalTransformation("PD1-7.2=[ORDERED_BY_LAST]");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1_7_3:
      testCaseMessage.appendAdditionalTransformation("PD1-7.3=[ORDERED_BY_FIRST]");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1_7_4:
      testCaseMessage.appendAdditionalTransformation("PD1-7.4=[ORDERED_BY_MIDDLE]");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1_19:
      testCaseMessage.appendAdditionalTransformation("PV1-19.1=[MSH-10]");
      testCaseMessage.appendAdditionalTransformation("PV1-19.4=[RESPONSIBLE_ORG_ID]");
      testCaseMessage.appendAdditionalTransformation("PV1-19.4=VN");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1_20:
      testCaseMessage.appendAdditionalTransformation("PV1-20.1=[VFC]");
      testCaseMessage.appendAdditionalTransformation("PV1-20.2=[TODAY]");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1_20_1:
      testCaseMessage.appendAdditionalTransformation("PV1-20.1=[VFC]");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1_20_2:
      testCaseMessage.appendAdditionalTransformation("PV1-20.2=[TODAY]");
      testCaseMessage.setHasIssue(true);
      break;

    case PV1_44:
      testCaseMessage.appendAdditionalTransformation("PV1-44=[TODAY]");
      testCaseMessage.setHasIssue(true);
      break;

    case ORC:
      return defaultTestCaseMessage;

    case ORC_GROUP:
      return defaultTestCaseMessage;

    case ADMIN_ORC_1:
      return defaultTestCaseMessage;

    case ADMIN_ORC_2:
      testCaseMessage.appendAdditionalTransformation("ORC-2.1=[VAC1_ID]");
      testCaseMessage.appendAdditionalTransformation("ORC-2.2=[RESPONSIBLE_ORG_ID]");
      testCaseMessage.setHasIssue(true);
      break;

    case ADMIN_ORC_2_1:
      testCaseMessage.appendAdditionalTransformation("ORC-2.1=[VAC1_ID]");
      testCaseMessage.setHasIssue(true);
      break;

    case ADMIN_ORC_2_2:
      testCaseMessage.appendAdditionalTransformation("ORC-2.1=[VAC1_ID]");
      testCaseMessage.appendAdditionalTransformation("ORC-2.2=[RESPONSIBLE_ORG_ID]");
      testCaseMessage.setHasIssue(true);
      break;

    case ADMIN_ORC_2_3:
      // not implemented
      break;

    case ADMIN_ORC_3:
      return defaultTestCaseMessage;

    case ADMIN_ORC_3_1:
      // not implemented
      break;

    case ADMIN_ORC_3_2:
      // not implemented
      break;

    case ADMIN_ORC_3_3:
      // not implemented
      break;

    case ADMIN_ORC_5:
      testCaseMessage.appendAdditionalTransformation("ORC-5=CM");
      testCaseMessage.setHasIssue(true);
      break;

    case ADMIN_ORC_10:
      return defaultTestCaseMessage;

    case ADMIN_ORC_10_1:
      return defaultTestCaseMessage;

    case ADMIN_ORC_10_2:
      return defaultTestCaseMessage;

    case ADMIN_ORC_10_3:
      return defaultTestCaseMessage;

    case ADMIN_ORC_10_4:
      return defaultTestCaseMessage;

    case ADMIN_ORC_12:
      return defaultTestCaseMessage;

    case ADMIN_ORC_12_1:
      return defaultTestCaseMessage;

    case ADMIN_ORC_12_2:
      return defaultTestCaseMessage;

    case ADMIN_ORC_12_3:
      return defaultTestCaseMessage;

    case ADMIN_ORC_12_4:
      return defaultTestCaseMessage;

    case ADMIN_ORC_17:
      return defaultTestCaseMessage;

    case ADMIN_ORC_17_1:
      return defaultTestCaseMessage;

    case ADMIN_ORC_17_4:
      testCaseMessage.appendAdditionalTransformation("ORC-17.4=[ADMIN_ORG_1_NAME]");
      testCaseMessage.setHasIssue(true);
      break;

    case ADMIN_RXA:
      return defaultTestCaseMessage;

    case ADMIN_RXA_1:
      return defaultTestCaseMessage;

    case ADMIN_RXA_2:
      return defaultTestCaseMessage;

    case ADMIN_RXA_3:
      return defaultTestCaseMessage;

    case ADMIN_RXA_4:
      return defaultTestCaseMessage;

    case ADMIN_RXA_5:
      return defaultTestCaseMessage;

    case ADMIN_RXA_6:
      return defaultTestCaseMessage;

    case ADMIN_RXA_7:
      return defaultTestCaseMessage;

    case ADMIN_RXA_9:
      return defaultTestCaseMessage;

    case ADMIN_RXA_10:
      return defaultTestCaseMessage;

    case ADMIN_RXA_10_1:
      return defaultTestCaseMessage;

    case ADMIN_RXA_10_2:
      return defaultTestCaseMessage;

    case ADMIN_RXA_10_3:
      return defaultTestCaseMessage;

    case ADMIN_RXA_10_4:
      return defaultTestCaseMessage;

    case ADMIN_RXA_11:
      return defaultTestCaseMessage;

    case ADMIN_RXA_11_4:
      return defaultTestCaseMessage;

    case ADMIN_RXA_15:
      return defaultTestCaseMessage;

    case ADMIN_RXA_16:
      return defaultTestCaseMessage;

    case ADMIN_RXA_17:
      return defaultTestCaseMessage;

    case ADMIN_RXA_20:
      return defaultTestCaseMessage;

    case ADMIN_RXA_21:
      return defaultTestCaseMessage;

    case ADMIN_RXA_22:
      return defaultTestCaseMessage;

    case ADMIN_RXR_1:
      return defaultTestCaseMessage;

    case ADMIN_RXR_2:
      return defaultTestCaseMessage;

    case ADMIN_OBX_1:
      return defaultTestCaseMessage;

    case ADMIN_OBX_2:
      return defaultTestCaseMessage;

    case ADMIN_OBX_3:
      return defaultTestCaseMessage;

    case ADMIN_OBX_4:
      return defaultTestCaseMessage;

    case ADMIN_OBX_5:
      return defaultTestCaseMessage;

    case ADMIN_OBX_11:
      return defaultTestCaseMessage;

    case ADMIN_OBX_14:
      return defaultTestCaseMessage;

    case ADMIN_OBX_17:
      return defaultTestCaseMessage;

    case ADMIN_OBX_64994_7:
      return defaultTestCaseMessage;

    case ADMIN_OBX_69764_9:
      return defaultTestCaseMessage;

    case ADMIN_OBX_29768_9:
      return defaultTestCaseMessage;

    case ADMIN_OBX_30956_7:
      return defaultTestCaseMessage;

    case ADMIN_OBX_29769_7:
      return defaultTestCaseMessage;

    case HIST_RXA_1:
      return defaultTestCaseMessage;

    case HIST_RXA_2:
      return defaultTestCaseMessage;

    case HIST_RXA_3:
      return defaultTestCaseMessage;

    case HIST_RXA_4:
      testCaseMessage.appendAdditionalTransformation("RXA#2-4=[VAC2_DATE]");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_5:
      return defaultTestCaseMessage;

    case HIST_RXA_6:
      testCaseMessage.appendAdditionalTransformation("RXA#2-6=[VAC2_AMOUNT]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.1=mL");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.2=milliliters");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.3=UCUM");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_7:
      testCaseMessage.appendAdditionalTransformation("RXA#2-6=[VAC2_AMOUNT]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.1=mL");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.2=milliliters");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.3=UCUM");
      testCaseMessage.setHasIssue(true);
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_9:
      return defaultTestCaseMessage;

    case HIST_RXA_10:
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.1=[ADMIN_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.2=[ADMIN_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.3=[ADMIN_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.4=[ADMIN_BY_MIDDLE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.9=CMS");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.10=L");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.13=NPI");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_10_1:
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.1=[ADMIN_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.9=CMS");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.13=NPI");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_10_2:
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.2=[ADMIN_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.3=[ADMIN_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.10=L");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_10_3:
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.2=[ADMIN_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.3=[ADMIN_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.10=L");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_10_4:
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.2=[ADMIN_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.3=[ADMIN_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.4=[ADMIN_BY_MIDDLE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.10=L");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_11:
      testCaseMessage.appendAdditionalTransformation("RXA#2-11.4=Other Clinic");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_11_4:
      testCaseMessage.appendAdditionalTransformation("RXA#2-11.4=Other Clinic");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_15:
      testCaseMessage.appendAdditionalTransformation("RXA#2-15=[VAC2_LOT]");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_16:
      testCaseMessage.appendAdditionalTransformation("RXA#2-16=[FUTURE]");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_17:
      testCaseMessage.appendAdditionalTransformation("RXA#2-17.1=[VAC2_MVX]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-17.2=[VAC2_MVX_LABEL]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-17.3=MVX");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXA_20:
      return defaultTestCaseMessage;

    case HIST_RXA_21:
      return defaultTestCaseMessage;

    case HIST_RXR_1:
      testCaseMessage.appendAdditionalTransformation("insert segment RXR after RXA#2");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.1=[VAC2_ROUTE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.3=HL70162");
      testCaseMessage.setHasIssue(true);
      break;

    case HIST_RXR_2:
      testCaseMessage.appendAdditionalTransformation("insert segment RXR after RXA#2");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.1=[VAC2_ROUTE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.3=HL70162");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-2.1=[VAC2_SITE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-2.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-2.3=HL70163");
      testCaseMessage.setHasIssue(true);

    case ADMIN_NTE:
      testCaseMessage.appendAdditionalTransformation("insert segment NTE after OBX");
      testCaseMessage.appendAdditionalTransformation("NTE-1=1");
      testCaseMessage.appendAdditionalTransformation("NTE-3=VFC Eligible");
      testCaseMessage.setHasIssue(true);

    case ADMIN_OBX:
      return defaultTestCaseMessage;

    case ADMIN_RXA_18:
      testCaseMessage.appendAdditionalTransformation("RXA-18=00");
      testCaseMessage.setHasIssue(true);
      break;

    case ADMIN_RXR:
      return defaultTestCaseMessage;

    case GT1:
      testCaseMessage.appendAdditionalTransformation("insert segment GT1 after PV1");
      testCaseMessage.appendAdditionalTransformation("GT1-1=1");
      testCaseMessage.appendAdditionalTransformation("GT1-3.1=~60%[LAST]:[LAST_DIFFERENT]");
      testCaseMessage.appendAdditionalTransformation("GT1-3.2=[MOTHER]");
      testCaseMessage.appendAdditionalTransformation("GT1-3.3=[GIRL_MIDDLE]");
      testCaseMessage.appendAdditionalTransformation("GT1-3.7=L");
      testCaseMessage.appendAdditionalTransformation("GT1-1=1");
      testCaseMessage.appendAdditionalTransformation("GT1-4.1=~60%[LAST]:[LAST_DIFFERENT]");
      testCaseMessage.appendAdditionalTransformation("GT1-4.2=[FATHER]");
      testCaseMessage.appendAdditionalTransformation("GT1-4.3=[BOY_MIDDLE]");
      testCaseMessage.appendAdditionalTransformation("GT1-4.7=L");
      testCaseMessage.appendAdditionalTransformation("GT1-5.1=[STREET]");
      testCaseMessage.appendAdditionalTransformation("GT1-5.2=[STREET2]");
      testCaseMessage.appendAdditionalTransformation("GT1-5.3=[CITY]");
      testCaseMessage.appendAdditionalTransformation("GT1-5.4=[STATE]");
      testCaseMessage.appendAdditionalTransformation("GT1-5.5=[ZIP]");
      testCaseMessage.appendAdditionalTransformation("GT1-5.6=USA");
      testCaseMessage.appendAdditionalTransformation("GT1-6");
      testCaseMessage.appendAdditionalTransformation("GT1-6.7=P");
      testCaseMessage.appendAdditionalTransformation("GT1-6.2=PRN");
      testCaseMessage.appendAdditionalTransformation("GT1-6.3=PH");
      testCaseMessage.appendAdditionalTransformation("GT1-6.6=[PHONE_AREA]");
      testCaseMessage.appendAdditionalTransformation("GT1-6.7=[PHONE_LOCAL]");
      testCaseMessage.appendAdditionalTransformation("GT1-7.2=WPN");
      testCaseMessage.appendAdditionalTransformation("GT1-7.3=PH");
      testCaseMessage.appendAdditionalTransformation("GT1-7.6=[PHONE_ALT_AREA]");
      testCaseMessage.appendAdditionalTransformation("GT1-7.7=[PHONE_ALT_LOCAL]");
      testCaseMessage.appendAdditionalTransformation("GT1-7.7=[PHONE_ALT_LOCAL]");
      testCaseMessage.appendAdditionalTransformation("GT1-8=[MOTHER_DOB]");
      testCaseMessage.appendAdditionalTransformation("GT1-9=F");
      testCaseMessage.appendAdditionalTransformation("GT1-11.1=MTH");
      testCaseMessage.appendAdditionalTransformation("GT1-11.2=Mother");
      testCaseMessage.appendAdditionalTransformation("GT1-11.3=HL70063");
      testCaseMessage.appendAdditionalTransformation("GT1-12=[MOTHER_SSN]");
      testCaseMessage.appendAdditionalTransformation("GT1-13=[DOB]");
      testCaseMessage.appendAdditionalTransformation("GT1-14=[FUTURE]");
      testCaseMessage.appendAdditionalTransformation("GT1-15=1");
      testCaseMessage.appendAdditionalTransformation("GT1-16.1=~60%[LAST]:[LAST_DIFFERENT]");
      testCaseMessage.appendAdditionalTransformation("GT1-16.2=[MOTHER]");
      testCaseMessage.appendAdditionalTransformation("GT1-16.3=[GIRL_MIDDLE]");
      testCaseMessage.appendAdditionalTransformation("GT1-16.7=L");
      testCaseMessage.appendAdditionalTransformation("GT1-17.1=[STREET]");
      testCaseMessage.appendAdditionalTransformation("GT1-17.2=[STREET2]");
      testCaseMessage.appendAdditionalTransformation("GT1-17.3=[CITY]");
      testCaseMessage.appendAdditionalTransformation("GT1-17.4=[STATE]");
      testCaseMessage.appendAdditionalTransformation("GT1-17.5=[ZIP]");
      testCaseMessage.appendAdditionalTransformation("GT1-17.6=USA");
      testCaseMessage.appendAdditionalTransformation("GT1-18.2=WPN");
      testCaseMessage.appendAdditionalTransformation("GT1-18.3=PH");
      testCaseMessage.appendAdditionalTransformation("GT1-18.6=[PHONE_ALT_AREA]");
      testCaseMessage.appendAdditionalTransformation("GT1-18.7=[PHONE_ALT_LOCAL]");
      testCaseMessage.appendAdditionalTransformation("GT1-18.7=[PHONE_ALT_LOCAL]");
      testCaseMessage.appendAdditionalTransformation("GT1-19.1=[MOTHER_SSN]");
      testCaseMessage.appendAdditionalTransformation("GT1-19.4=US");
      testCaseMessage.appendAdditionalTransformation("GT1-19.5=SS");
      testCaseMessage.appendAdditionalTransformation("GT1-20=4");
      testCaseMessage.appendAdditionalTransformation("GT1-21=\"\"");
      testCaseMessage.appendAdditionalTransformation("GT1-22=N");
      testCaseMessage.appendAdditionalTransformation("GT1-28=3");
      testCaseMessage.appendAdditionalTransformation("GT1-36.1=[LANGUAGE]");
      testCaseMessage.appendAdditionalTransformation("GT1-36.2=[LANGUAGE_LABEL]");
      testCaseMessage.appendAdditionalTransformation("GT1-36.3=HL70296");
      testCaseMessage.appendAdditionalTransformation("GT1-28=N");
      testCaseMessage.appendAdditionalTransformation("GT1-40=N");
      testCaseMessage.appendAdditionalTransformation("GT1-42=[LAST_DIFFERENT]");
      testCaseMessage.appendAdditionalTransformation("GT1-44.1=[ETHNICITY]");
      testCaseMessage.appendAdditionalTransformation("GT1-44.2=[ETHNICITY_LABEL]");
      testCaseMessage.appendAdditionalTransformation("GT1-44.3=CDCREC");
      testCaseMessage.appendAdditionalTransformation("GT1-55.1=[RACE]");
      testCaseMessage.appendAdditionalTransformation("GT1-55.2=[RACE_LABEL]");
      testCaseMessage.appendAdditionalTransformation("GT1-55.3=HL70005");
      testCaseMessage.setHasIssue(true);
      break;

    case NON_ADMIN_NTE:
      convertHistoryToNonAdmin(testCaseMessage);

    case HIST_NTE:
      testCaseMessage.appendAdditionalTransformation("insert segment OBX after RXA#2");
      testCaseMessage.appendAdditionalTransformation("insert segment NTE after RXA#2:OBX");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-1=1");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-2=CE");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-3.1=64994-7");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-3.2=Vaccine funding program eligibility category");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-3.3=LN");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-4=1");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-5.1=[VFC]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-5.2=[VFC_LABEL]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-5.3=HL70064");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-11=F");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-14=[TODAY]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-17.1=VXC40");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-17.2=Eligibility captured at the immunization level");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-17.3=CDCPHINVS");
      testCaseMessage.appendAdditionalTransformation("NTE-1=1");
      testCaseMessage.appendAdditionalTransformation("NTE-3=VFC Eligible");
      testCaseMessage.setHasIssue(true);
      break;

    case NON_ADMIN_OBX:
      convertHistoryToNonAdmin(testCaseMessage);

    case HIST_OBX:
      testCaseMessage.appendAdditionalTransformation("insert segment OBX after RXA#2 if missing");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-1=1");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-2=CE");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-3.1=64994-7");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-3.2=Vaccine funding program eligibility category");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-3.3=LN");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-4=1");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-5.1=[VFC]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-5.2=[VFC_LABEL]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-5.3=HL70064");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-11=F");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-14=[TODAY]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-17.1=VXC40");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-17.2=Eligibility captured at the immunization level");
      testCaseMessage.appendAdditionalTransformation("RXA#2:OBX#1-17.3=CDCPHINVS");
      testCaseMessage.setHasIssue(true);

    case HIST_RXR:
      testCaseMessage.appendAdditionalTransformation("insert segment RXR after RXA#2 if missing");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.1=[VAC2_ROUTE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.3=HL70162");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-2.1=[VAC2_SITE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-2.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-2.3=HL70163");
      testCaseMessage.setHasIssue(true);
      break;

    case IN1:
      testCaseMessage.appendAdditionalTransformation("insert segment IN1 after NK1 if missing");
      testCaseMessage.appendAdditionalTransformation("IN1-1=1");
      testCaseMessage.appendAdditionalTransformation("IN1-2=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN1-3.1=79413");
      testCaseMessage.appendAdditionalTransformation("IN1-3.2=United HealthCare Insurance Company");
      testCaseMessage.appendAdditionalTransformation("IN1-3.1=SAIC");
      testCaseMessage.appendAdditionalTransformation("IN1-4.1=5");
      testCaseMessage.appendAdditionalTransformation("IN1-4.2=Private Insurance");
      testCaseMessage.appendAdditionalTransformation("IN1-4.3=HL70086");
      testCaseMessage.appendAdditionalTransformation("IN1-5.1=United HealthCare Insurance Company");
      testCaseMessage.appendAdditionalTransformation("IN1-6.1=PO BOX 740800");
      testCaseMessage.appendAdditionalTransformation("IN1-6.3=Atlanta");
      testCaseMessage.appendAdditionalTransformation("IN1-6.4=GA");
      testCaseMessage.appendAdditionalTransformation("IN1-6.5=30374-0800");
      testCaseMessage.appendAdditionalTransformation("IN1-6.6=USA");
      testCaseMessage.appendAdditionalTransformation("IN1-6.7=M");
      testCaseMessage.appendAdditionalTransformation("IN1-7.2=WPN");
      testCaseMessage.appendAdditionalTransformation("IN1-7.3=PH");
      testCaseMessage.appendAdditionalTransformation("IN1-7.6=877");
      testCaseMessage.appendAdditionalTransformation("IN1-7.7=8423210");
      testCaseMessage.appendAdditionalTransformation("IN1-8=109988");
      testCaseMessage.appendAdditionalTransformation("IN1-9=Nihon Corp");
      testCaseMessage.appendAdditionalTransformation("IN1-10.1=[SSN]");
      testCaseMessage.appendAdditionalTransformation("IN1-10.4=Nihon");
      testCaseMessage.appendAdditionalTransformation("IN1-10.5=PN");
      testCaseMessage.appendAdditionalTransformation("IN1-12=[DOB]");
      testCaseMessage.appendAdditionalTransformation("IN1-13=[FUTURE]");
      testCaseMessage.appendAdditionalTransformation("IN1-14.1=59399945");
      testCaseMessage.appendAdditionalTransformation("IN1-14.2=[DOB]");
      testCaseMessage.appendAdditionalTransformation("IN1-14.3=Hospital admission authorization for birth");
      testCaseMessage.appendAdditionalTransformation("IN1-15.1=5");
      testCaseMessage.appendAdditionalTransformation("IN1-15.2=Private Insurance");
      testCaseMessage.appendAdditionalTransformation("IN1-15.3=HL7086");
      testCaseMessage.appendAdditionalTransformation("IN1-16.1=~60%[LAST]:[LAST_DIFFERENT]");
      testCaseMessage.appendAdditionalTransformation("IN1-16.2=[MOTHER]");
      testCaseMessage.appendAdditionalTransformation("IN1-16.3=[GIRL_MIDDLE]");
      testCaseMessage.appendAdditionalTransformation("IN1-16.7=L");
      testCaseMessage.appendAdditionalTransformation("IN1-17.1=MTH");
      testCaseMessage.appendAdditionalTransformation("IN1-17.2=Mother");
      testCaseMessage.appendAdditionalTransformation("IN1-17.3=HL70063");
      testCaseMessage.appendAdditionalTransformation("IN1-18=[MOTHER_DOB]");
      testCaseMessage.appendAdditionalTransformation("IN1-19.1=[STREET]");
      testCaseMessage.appendAdditionalTransformation("IN1-19.2=[STREET2]");
      testCaseMessage.appendAdditionalTransformation("IN1-19.3=[CITY]");
      testCaseMessage.appendAdditionalTransformation("IN1-19.4=[STATE]");
      testCaseMessage.appendAdditionalTransformation("IN1-19.5=[ZIP]");
      testCaseMessage.appendAdditionalTransformation("IN1-19.6=USA");
      testCaseMessage.appendAdditionalTransformation("IN1-20=Y");
      testCaseMessage.appendAdditionalTransformation("IN1-21=CO");
      testCaseMessage.appendAdditionalTransformation("IN1-22=1");
      testCaseMessage.appendAdditionalTransformation("IN1-23=Y");
      testCaseMessage.appendAdditionalTransformation("IN1-24=[DOB]");
      testCaseMessage.appendAdditionalTransformation("IN1-25=Y");
      testCaseMessage.appendAdditionalTransformation("IN1-26=[VAC1_DATE]");
      testCaseMessage.appendAdditionalTransformation("IN1-27=N");
      testCaseMessage.appendAdditionalTransformation("IN1-28=PAC-2093994");
      testCaseMessage.appendAdditionalTransformation("IN1-29=[DOB]");
      testCaseMessage.appendAdditionalTransformation("IN1-30.1=");
      testCaseMessage.appendAdditionalTransformation("IN1-30.2=Hammersmith");
      testCaseMessage.appendAdditionalTransformation("IN1-30.3=Nigel");
      testCaseMessage.appendAdditionalTransformation("IN1-30.4=William");
      testCaseMessage.appendAdditionalTransformation("IN1-30.8=L");
      testCaseMessage.appendAdditionalTransformation("IN1-31=S");
      testCaseMessage.appendAdditionalTransformation("IN1-33=199");
      testCaseMessage.appendAdditionalTransformation("IN1-34=0");
      testCaseMessage.appendAdditionalTransformation("IN1-36=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN1-39=15");
      testCaseMessage.appendAdditionalTransformation("IN1-43=F");
      testCaseMessage.appendAdditionalTransformation("IN1-44.1=[STREET]");
      testCaseMessage.appendAdditionalTransformation("IN1-44.2=[STREET2]");
      testCaseMessage.appendAdditionalTransformation("IN1-44.3=[CITY]");
      testCaseMessage.appendAdditionalTransformation("IN1-44.4=[STATE]");
      testCaseMessage.appendAdditionalTransformation("IN1-44.5=[ZIP]");
      testCaseMessage.appendAdditionalTransformation("IN1-44.6=USA");
      testCaseMessage.appendAdditionalTransformation("IN1-47=B");
      testCaseMessage.appendAdditionalTransformation("IN1-50.1=S");
      testCaseMessage
          .appendAdditionalTransformation("IN1-50.2=Signed authorization for release for process this claim on file");
      testCaseMessage.appendAdditionalTransformation("IN1-50.2=HL70535");
      testCaseMessage.appendAdditionalTransformation("IN1-51=[DOB]");
      testCaseMessage.appendAdditionalTransformation("IN1-52=St. Francis Communtiy Hospital of Lower South Side");
      testCaseMessage.setHasIssue(true);
      break;

    case IN1_GROUP:
      testCaseMessage.appendAdditionalTransformation("insert segment IN1 after NK1 if missing");
      testCaseMessage.appendAdditionalTransformation("IN1-1=1");
      testCaseMessage.appendAdditionalTransformation("IN1-2=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN1-3.1=79413");
      testCaseMessage.appendAdditionalTransformation("IN1-3.2=United HealthCare Insurance Company");
      testCaseMessage.appendAdditionalTransformation("IN1-3.1=SAIC");
      testCaseMessage.appendAdditionalTransformation("IN1-15.1=5");
      testCaseMessage.appendAdditionalTransformation("IN1-15.2=Private Insurance");
      testCaseMessage.appendAdditionalTransformation("IN1-15.3=HL7086");
      testCaseMessage.appendAdditionalTransformation("IN1-29=[DOB]");
      testCaseMessage.appendAdditionalTransformation("IN1-36=[MEDICAID]");
      testCaseMessage.setHasIssue(true);
      break;

    case IN2:
      testCaseMessage.appendAdditionalTransformation("insert segment IN1 after NK1 if missing");
      testCaseMessage.appendAdditionalTransformation("insert segment IN2 after IN1 if missing");
      testCaseMessage.appendAdditionalTransformation("IN1-1=1");
      testCaseMessage.appendAdditionalTransformation("IN1-2=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN1-3.1=79413");
      testCaseMessage.appendAdditionalTransformation("IN1-3.2=United HealthCare Insurance Company");
      testCaseMessage.appendAdditionalTransformation("IN1-3.1=SAIC");
      testCaseMessage.appendAdditionalTransformation("IN1-15.1=5");
      testCaseMessage.appendAdditionalTransformation("IN1-15.2=Private Insurance");
      testCaseMessage.appendAdditionalTransformation("IN1-15.3=HL7086");
      testCaseMessage.appendAdditionalTransformation("IN1-29=[DOB]");
      testCaseMessage.appendAdditionalTransformation("IN1-36=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN2-6=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN2-8=[MEDICAID]");
      testCaseMessage.setHasIssue(true);
      break;

    case IN3:
      testCaseMessage.appendAdditionalTransformation("insert segment IN1 after NK1 if missing");
      testCaseMessage.appendAdditionalTransformation("insert segment IN2 after IN1 if missing");
      testCaseMessage.appendAdditionalTransformation("insert segment IN3 after IN2 if missing");
      testCaseMessage.appendAdditionalTransformation("IN1-1=1");
      testCaseMessage.appendAdditionalTransformation("IN1-2=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN1-3.1=79413");
      testCaseMessage.appendAdditionalTransformation("IN1-3.2=United HealthCare Insurance Company");
      testCaseMessage.appendAdditionalTransformation("IN1-3.1=SAIC");
      testCaseMessage.appendAdditionalTransformation("IN1-15.1=5");
      testCaseMessage.appendAdditionalTransformation("IN1-15.2=Private Insurance");
      testCaseMessage.appendAdditionalTransformation("IN1-15.3=HL7086");
      testCaseMessage.appendAdditionalTransformation("IN1-29=[DOB]");
      testCaseMessage.appendAdditionalTransformation("IN1-36=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN2-6=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN2-8=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN3-1=1");
      testCaseMessage.appendAdditionalTransformation("IN3-2=[MEDICAID]");
      testCaseMessage.appendAdditionalTransformation("IN3-6=[DOB]");
      testCaseMessage.setHasIssue(true);
      break;

    case MSH_21:
      testCaseMessage.appendAdditionalTransformation("MSH-21.1=Z22");
      testCaseMessage.appendAdditionalTransformation("MSH-21.2=CDCPHINVS");
      testCaseMessage.setHasIssue(true);
      break;

    case NK1_16:
      testCaseMessage.appendAdditionalTransformation("MSH-16=[MOTHER_DOB]");
      testCaseMessage.setHasIssue(true);
      break;

    case NK1_6_1:
      testCaseMessage.appendAdditionalTransformation("NK1-6.1=[PHONE_ALT]");
      testCaseMessage.appendAdditionalTransformation("NK1-6.2=WPN");
      testCaseMessage.appendAdditionalTransformation("NK1-6.3=PH");
      testCaseMessage.appendAdditionalTransformation("NK1-6.6=[PHONE_ALT_AREA]");
      testCaseMessage.appendAdditionalTransformation("NK1-6.7=[PHONE_ALT_LOCAL]");
      testCaseMessage.setHasIssue(true);
      break;

    case NK1_6_2:
      return defaultTestCaseMessage;

    case NK1_6_3:
      return defaultTestCaseMessage;

    case NK1_6_6:
      return defaultTestCaseMessage;

    case NK1_6_7:
      return defaultTestCaseMessage;

    case NK1_6_8:
      testCaseMessage.appendAdditionalTransformation("NK1-6.8=101");
      testCaseMessage.setHasIssue(true);

    case NON_ADMIN_RXA_1:
      convertHistoryToNonAdmin(testCaseMessage);
      break;

    case NON_ADMIN_RXA_10:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.1=[ADMIN_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.2=[ADMIN_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.3=[ADMIN_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.4=[ADMIN_BY_MIDDLE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.9=CMS");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.10=L");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.13=NPI");
      break;

    case NON_ADMIN_RXA_10_1:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.1=[ADMIN_BY_NPI]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.9=CMS");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.13=NPI");
      break;

    case NON_ADMIN_RXA_10_2:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.2=[ADMIN_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.3=[ADMIN_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.10=L");
      break;
    
    case NON_ADMIN_RXA_10_3:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.2=[ADMIN_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.3=[ADMIN_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.10=L");
      break;

    case NON_ADMIN_RXA_10_4:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.2=[ADMIN_BY_LAST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.3=[ADMIN_BY_FIRST]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.4=[ADMIN_BY_MIDDLE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-10.10=[L");
      break;

    case NON_ADMIN_RXA_11:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-11.4=[ADMIN_ORG_1_ID]");
      break;
    
    case NON_ADMIN_RXA_11_4:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-11.4=[ADMIN_ORG_1_ID]");
      break;
    
    case NON_ADMIN_RXA_15:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-15=[VAC2_LOT]");
      break;
    
    case NON_ADMIN_RXA_16:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-16=[FUTURE]");
      break;
    
    case NON_ADMIN_RXA_17:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-17.1=[VAC2_MVX]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-17.2=[VAC2_MVX_LABEL]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-17.3=MVX");
      break;

    case NON_ADMIN_RXA_2:
      convertHistoryToNonAdmin(testCaseMessage);
      break;

    case NON_ADMIN_RXA_20:
      convertHistoryToNonAdmin(testCaseMessage);
  break;

    case NON_ADMIN_RXA_21:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-21=A");
      break;

    case NON_ADMIN_RXA_3:
      convertHistoryToNonAdmin(testCaseMessage);
      break;
      
    case NON_ADMIN_RXA_4:
      convertHistoryToNonAdmin(testCaseMessage);
      break;
      
    case NON_ADMIN_RXA_5:
      convertHistoryToNonAdmin(testCaseMessage);
      break;
      
    case NON_ADMIN_RXA_6:
      convertHistoryToNonAdmin(testCaseMessage);
      break;
      
    case NON_ADMIN_RXA_7:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-6=[VAC2_AMOUNT]");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.1=mL");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.2=milliliters");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.3=UCUM");
      testCaseMessage.setHasIssue(true);
      break;
      
    case NON_ADMIN_RXA_9:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("RXA#2-9.1=00");
      testCaseMessage.appendAdditionalTransformation("RXA#2-9.2=Administered");
      testCaseMessage.appendAdditionalTransformation("RXA#2-8.3=NIP001");
      testCaseMessage.setHasIssue(true);
      break;
      
    case NON_ADMIN_RXR:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("insert segment RXR after RXA#2");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.1=[VAC2_ROUTE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.3=HL70162");
      testCaseMessage.setHasIssue(true);
      break;
      
    case NON_ADMIN_RXR_1:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("insert segment RXR after RXA#2");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.1=[VAC2_ROUTE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.3=HL70162");
      testCaseMessage.setHasIssue(true);
      break;
      
    case NON_ADMIN_RXR_2:
      convertHistoryToNonAdmin(testCaseMessage);
      testCaseMessage.appendAdditionalTransformation("insert segment RXR after RXA#2");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.1=[VAC2_ROUTE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-1.3=HL70162");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-2.1=[VAC2_SITE]");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-2.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2:RXR-2.3=HL70163");
      testCaseMessage.setHasIssue(true);
      break;
      
    case ADMIN_ORC_10_9:
      break;
      
    case PID_5_1_ALIAS:
      testCaseMessage.appendAdditionalTransformation("PID-");
      testCaseMessage.setHasIssue(true);
      break;
      
    case PID_5_2_ALIAS:
      break;
    case PID_5_3_ALIAS:
      break;
    case PID_5_4_ALIAS:
      break;
    case PID_5_7_ALIAS:
      break;
    case PID_5_ALIAS:
      break;
    case PV2:
      break;
    case REFUSAL_NTE:
      break;
    case REFUSAL_OBX:
      break;
    case REFUSAL_RXA_1:
      break;
    case REFUSAL_RXA_10:
      break;
    case REFUSAL_RXA_10_1:
      break;
    case REFUSAL_RXA_10_2:
      break;
    case REFUSAL_RXA_10_3:
      break;
    case REFUSAL_RXA_10_4:
      break;
    case REFUSAL_RXA_11:
      break;
    case REFUSAL_RXA_11_4:
      break;
    case REFUSAL_RXA_15:
      break;
    case REFUSAL_RXA_16:
      break;
    case REFUSAL_RXA_17:
      break;
    case REFUSAL_RXA_2:
      break;
    case REFUSAL_RXA_20:
      break;
    case REFUSAL_RXA_21:
      break;
    case REFUSAL_RXA_3:
      break;
    case REFUSAL_RXA_4:
      break;
    case REFUSAL_RXA_5:
      break;
    case REFUSAL_RXA_6:
      break;
    case REFUSAL_RXA_7:
      break;
    case REFUSAL_RXA_9:
      break;
    case REFUSAL_RXR:
      break;
    case REFUSAL_RXR_1:
      break;
    case REFUSAL_RXR_2:
      break;
    case SFT:
      testCaseMessage.appendAdditionalTransformation("insert segment SFT after MSH if missing");
      testCaseMessage.appendAdditionalTransformation("SFT-1=" + SoftwareVersion.SOFTWARE_VENDOR);
      testCaseMessage.appendAdditionalTransformation("SFT-2=" + SoftwareVersion.VERSION);
      testCaseMessage.appendAdditionalTransformation("SFT-3=" + SoftwareVersion.SOFTWARE_PRODUCT_NAME);
      testCaseMessage.appendAdditionalTransformation("SFT-4=" + SoftwareVersion.SOFTWARE_BINARY_ID);
      testCaseMessage.appendAdditionalTransformation("SFT-6=" + SoftwareVersion.SOFTWARE_RELEASE_DATE);
      testCaseMessage.setHasIssue(true);
      break;
    case TQ1:
      testCaseMessage.appendAdditionalTransformation("insert segment TQ1 after ORC if missing");
      testCaseMessage.appendAdditionalTransformation("TQ1-1=1");
      testCaseMessage.appendAdditionalTransformation("TQ1-2.1=[VAC1_AMOUNT]");
      testCaseMessage.appendAdditionalTransformation("TQ1-2.2=mL&milliliters&UCUM");
      testCaseMessage.appendAdditionalTransformation("TQ1-9=R");
      break;
    case TQ2:
      testCaseMessage.appendAdditionalTransformation("insert segment TQ2 after ORC#3 if missing");
      testCaseMessage.appendAdditionalTransformation("TQ2-1=1");
      testCaseMessage.appendAdditionalTransformation("TQ2-2=S");
      testCaseMessage.appendAdditionalTransformation("TQ2-4=[VAC1_ID]");
      testCaseMessage.appendAdditionalTransformation("TQ2-6=EE");
      testCaseMessage.appendAdditionalTransformation("TQ2-9=R");
      break;
    case ADMIN_OBX_10:
      break;
    case ADMIN_OBX_12:
      break;
    case ADMIN_OBX_13:
      break;
    case ADMIN_OBX_15:
      break;
    case ADMIN_OBX_16:
      break;
    case ADMIN_OBX_18:
      break;
    case ADMIN_OBX_19:
      break;
    case ADMIN_OBX_20:
      break;
    case ADMIN_OBX_21:
      break;
    case ADMIN_OBX_22:
      break;
    case ADMIN_OBX_23:
      break;
    case ADMIN_OBX_24:
      break;
    case ADMIN_OBX_25:
      break;
    case ADMIN_OBX_6:
      break;
    case ADMIN_OBX_7:
      break;
    case ADMIN_OBX_8:
      break;
    case ADMIN_OBX_9:
      break;
    case ADMIN_RCA_10_23:
      break;
    case ADMIN_RXA_10_10:
      break;
    case ADMIN_RXA_10_11:
      break;
    case ADMIN_RXA_10_12:
      break;
    case ADMIN_RXA_10_13:
      break;
    case ADMIN_RXA_10_14:
      break;
    case ADMIN_RXA_10_15:
      break;
    case ADMIN_RXA_10_16:
      break;
    case ADMIN_RXA_10_17:
      break;
    case ADMIN_RXA_10_18:
      break;
    case ADMIN_RXA_10_19:
      break;
    case ADMIN_RXA_10_20:
      break;
    case ADMIN_RXA_10_21:
      break;
    case ADMIN_RXA_10_22:
      break;
    case ADMIN_RXA_10_5:
      break;
    case ADMIN_RXA_10_6:
      break;
    case ADMIN_RXA_10_7:
      break;
    case ADMIN_RXA_10_8:
      break;
    case ADMIN_RXA_10_9:
      break;
    case ADMIN_RXA_11_1:
      break;
    case ADMIN_RXA_11_10:
      break;
    case ADMIN_RXA_11_11:
      break;
    case ADMIN_RXA_11_12:
      break;
    case ADMIN_RXA_11_13:
      break;
    case ADMIN_RXA_11_14:
      break;
    case ADMIN_RXA_11_15:
      break;
    case ADMIN_RXA_11_16:
      break;
    case ADMIN_RXA_11_2:
      break;
    case ADMIN_RXA_11_3:
      break;
    case ADMIN_RXA_11_5:
      break;
    case ADMIN_RXA_11_6:
      break;
    case ADMIN_RXA_11_7:
      break;
    case ADMIN_RXA_11_8:
      break;
    case ADMIN_RXA_11_9:
      break;
    case ADMIN_RXA_12:
      break;
    case ADMIN_RXA_13:
      break;
    case ADMIN_RXA_14:
      break;
    case ADMIN_RXA_16_YM:
      break;
    case ADMIN_RXA_19:
      break;
    case ADMIN_RXA_23:
      break;
    case ADMIN_RXA_24:
      break;
    case ADMIN_RXA_25:
      break;
    case ADMIN_RXA_26:
      break;
    case ADMIN_RXA_8:
      break;
    case ADMIN_RXR_3:
      break;
    case ADMIN_RXR_4:
      break;
    case ADMIN_RXR_5:
      break;
    case ADMIN_RXR_6:
      break;
    case HIST_RXA:
      break;
    case MSH_13:
      testCaseMessage.appendAdditionalTransformation("MSH-13=0");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_14:
      // Can't test, this requires sending a previously fragmented message
      break;
    case MSH_18:
      testCaseMessage.appendAdditionalTransformation("MSH-18=UNICODE UTF-8");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_19:
      testCaseMessage.appendAdditionalTransformation("MSH-19.1=eng");
      testCaseMessage.appendAdditionalTransformation("MSH-19.2=English");
      testCaseMessage.appendAdditionalTransformation("MSH-19.3=ISO 639-3");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_20:
      testCaseMessage.appendAdditionalTransformation("MSH-20=2.3");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_24:
      testCaseMessage.appendAdditionalTransformation("MSH-24=http://localhost:8282/ProfileServlet");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_25:
      testCaseMessage.appendAdditionalTransformation("MSH-25=http://localhost:8008/dqa/in");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_10:
      break;
    case NK1_11:
      break;
    case NK1_12:
      break;
    case NK1_13:
      break;
    case NK1_14:
      break;
    case NK1_15:
      break;
    case NK1_17:
      break;
    case NK1_18:
      break;
    case NK1_19:
      break;
    case NK1_20:
      break;
    case NK1_21:
      break;
    case NK1_22:
      break;
    case NK1_23:
      break;
    case NK1_24:
      break;
    case NK1_25:
      break;
    case NK1_26:
      break;
    case NK1_27:
      break;
    case NK1_28:
      break;
    case NK1_29:
      break;
    case NK1_30:
      break;
    case NK1_31:
      break;
    case NK1_32:
      break;
    case NK1_33:
      break;
    case NK1_34:
      break;
    case NK1_35:
      break;
    case NK1_36:
      break;
    case NK1_37:
      break;
    case NK1_38:
      break;
    case NK1_39:
      break;
    case NK1_8:
      break;
    case NK1_9:
      break;
    case NON_ADMIN_RXA:
      break;
    case ADMIN_ORC_10_10:
      break;
    case ADMIN_ORC_11:
      break;
    case ADMIN_ORC_12_10:
      break;
    case ADMIN_ORC_12_9:
      break;
    case ADMIN_ORC_13:
      break;
    case ADMIN_ORC_14:
      break;
    case ADMIN_ORC_15:
      break;
    case ADMIN_ORC_16:
      break;
    case ADMIN_ORC_18:
      break;
    case ADMIN_ORC_19:
      break;
    case ADMIN_ORC_20:
      break;
    case ADMIN_ORC_21:
      break;
    case ADMIN_ORC_22:
      break;
    case ADMIN_ORC_23:
      break;
    case ADMIN_ORC_24:
      break;
    case ADMIN_ORC_25:
      break;
    case ADMIN_ORC_26:
      break;
    case ADMIN_ORC_27:
      break;
    case ADMIN_ORC_28:
      break;
    case ADMIN_ORC_29:
      break;
    case ADMIN_ORC_30:
      break;
    case ADMIN_ORC_31:
      break;
    case ADMIN_ORC_4:
      break;
    case ADMIN_ORC_6:
      break;
    case ADMIN_ORC_7:
      break;
    case ADMIN_ORC_8:
      break;
    case ADMIN_ORC_9:
      break;
    case PD1_1:
      break;
    case PD1_10:
      break;
    case PD1_14:
      break;
    case PD1_15:
      break;
    case PD1_19:
      break;
    case PD1_2:
      break;
    case PD1_20:
      break;
    case PD1_21:
      break;
    case PD1_5:
      break;
    case PD1_6:
      break;
    case PD1_7:
      break;
    case PD1_8:
      break;
    case PD1_9:
      break;
    case PID_11_10:
      break;
    case PID_11_11:
      break;
    case PID_11_12:
      break;
    case PID_11_13:
      break;
    case PID_11_14:
      break;
    case PID_11_1_1:
      break;
    case PID_11_1_2:
      break;
    case PID_11_1_3:
      break;
    case PID_11_8:
      break;
    case PID_12:
      break;
    case PID_13_10:
      break;
    case PID_13_11:
      break;
    case PID_13_12:
      break;
    case PID_13_4:
      break;
    case PID_13_5:
      break;
    case PID_13_8:
      break;
    case PID_13_9:
      break;
    case PID_13_CELL:
      break;
    case PID_16:
      break;
    case PID_17:
      break;
    case PID_18:
      break;
    case PID_19:
      break;
    case PID_2:
      break;
    case PID_20:
      break;
    case PID_21:
      break;
    case PID_23:
      break;
    case PID_26:
      break;
    case PID_27:
      break;
    case PID_28:
      break;
    case PID_31:
      break;
    case PID_32:
      break;
    case PID_33:
      break;
    case PID_34:
      break;
    case PID_35:
      break;
    case PID_36:
      break;
    case PID_37:
      break;
    case PID_38:
      break;
    case PID_39:
      break;
    case PID_4:
      break;
    case PID_5_10:
      break;
    case PID_5_11:
      break;
    case PID_5_12:
      break;
    case PID_5_13:
      break;
    case PID_5_14:
      break;
    case PID_5_5:
      break;
    case PID_5_6:
      break;
    case PID_5_8:
      break;
    case PID_5_9:
      break;
    case PID_9:
      break;
    case REFUSAL_RXA:
      break;
    case REFUSAL_RXA_18:
      break;
    case IN1_1:
      break;
    case IN1_10:
      break;
    case IN1_11:
      break;
    case IN1_12:
      break;
    case IN1_13:
      break;
    case IN1_14:
      break;
    case IN1_15:
      break;
    case IN1_16:
      break;
    case IN1_17:
      break;
    case IN1_18:
      break;
    case IN1_19:
      break;
    case IN1_2:
      break;
    case IN1_20:
      break;
    case IN1_21:
      break;
    case IN1_22:
      break;
    case IN1_23:
      break;
    case IN1_24:
      break;
    case IN1_25:
      break;
    case IN1_26:
      break;
    case IN1_27:
      break;
    case IN1_28:
      break;
    case IN1_29:
      break;
    case IN1_3:
      break;
    case IN1_30:
      break;
    case IN1_31:
      break;
    case IN1_32:
      break;
    case IN1_33:
      break;
    case IN1_34:
      break;
    case IN1_35:
      break;
    case IN1_36:
      break;
    case IN1_37:
      break;
    case IN1_38:
      break;
    case IN1_39:
      break;
    case IN1_4:
      break;
    case IN1_40:
      break;
    case IN1_41:
      break;
    case IN1_42:
      break;
    case IN1_43:
      break;
    case IN1_44:
      break;
    case IN1_45:
      break;
    case IN1_46:
      break;
    case IN1_47:
      break;
    case IN1_48:
      break;
    case IN1_49:
      break;
    case IN1_5:
      break;
    case IN1_50:
      break;
    case IN1_51:
      break;
    case IN1_52:
      break;
    case IN1_53:
      break;
    case IN1_6:
      break;
    case IN1_7:
      break;
    case IN1_8:
      break;
    case IN1_9:
      break;
    case ADMIN_ORC:
      break;
    case ADMIN_ORC_12_7:
      break;
    case BHS:
      break;
    case BTS:
      break;
    case FHS:
      break;
    case FTS:
      break;
    case HIST_ORC:
      break;
    case HIST_ORC_1:
      break;
    case HIST_ORC_10:
      break;
    case HIST_ORC_11:
      break;
    case HIST_ORC_12:
      break;
    case HIST_ORC_13:
      break;
    case HIST_ORC_14:
      break;
    case HIST_ORC_15:
      break;
    case HIST_ORC_16:
      break;
    case HIST_ORC_17:
      break;
    case HIST_ORC_18:
      break;
    case HIST_ORC_19:
      break;
    case HIST_ORC_2:
      break;
    case HIST_ORC_20:
      break;
    case HIST_ORC_21:
      break;
    case HIST_ORC_22:
      break;
    case HIST_ORC_23:
      break;
    case HIST_ORC_24:
      break;
    case HIST_ORC_25:
      break;
    case HIST_ORC_26:
      break;
    case HIST_ORC_27:
      break;
    case HIST_ORC_28:
      break;
    case HIST_ORC_29:
      break;
    case HIST_ORC_3:
      break;
    case HIST_ORC_30:
      break;
    case HIST_ORC_31:
      break;
    case HIST_ORC_4:
      break;
    case HIST_ORC_5:
      break;
    case HIST_ORC_6:
      break;
    case HIST_ORC_7:
      break;
    case HIST_ORC_8:
      break;
    case HIST_ORC_9:
      break;
    case MSH:
      break;
    case NK1_4_HOME:
      break;
    case NK1_5_CELL:
      break;
    case NK1_5_EMAIL:
      break;
    case NON_ADMIN_ORC:
      break;
    case NON_ADMIN_ORC_1:
      break;
    case NON_ADMIN_ORC_10:
      break;
    case NON_ADMIN_ORC_11:
      break;
    case NON_ADMIN_ORC_12:
      break;
    case NON_ADMIN_ORC_13:
      break;
    case NON_ADMIN_ORC_14:
      break;
    case NON_ADMIN_ORC_15:
      break;
    case NON_ADMIN_ORC_16:
      break;
    case NON_ADMIN_ORC_17:
      break;
    case NON_ADMIN_ORC_18:
      break;
    case NON_ADMIN_ORC_19:
      break;
    case NON_ADMIN_ORC_2:
      break;
    case NON_ADMIN_ORC_20:
      break;
    case NON_ADMIN_ORC_21:
      break;
    case NON_ADMIN_ORC_22:
      break;
    case NON_ADMIN_ORC_23:
      break;
    case NON_ADMIN_ORC_24:
      break;
    case NON_ADMIN_ORC_25:
      break;
    case NON_ADMIN_ORC_26:
      break;
    case NON_ADMIN_ORC_27:
      break;
    case NON_ADMIN_ORC_28:
      break;
    case NON_ADMIN_ORC_29:
      break;
    case NON_ADMIN_ORC_3:
      break;
    case NON_ADMIN_ORC_30:
      break;
    case NON_ADMIN_ORC_31:
      break;
    case NON_ADMIN_ORC_4:
      break;
    case NON_ADMIN_ORC_5:
      break;
    case NON_ADMIN_ORC_6:
      break;
    case NON_ADMIN_ORC_7:
      break;
    case NON_ADMIN_ORC_8:
      break;
    case NON_ADMIN_ORC_9:
      break;
    case PID_11_BIRTH:
      break;
    case PID_11_HOME:
      break;
    case REFUSAL_ORC:
      break;
    case REFUSAL_ORC_1:
      break;
    case REFUSAL_ORC_10:
      break;
    case REFUSAL_ORC_11:
      break;
    case REFUSAL_ORC_12:
      break;
    case REFUSAL_ORC_13:
      break;
    case REFUSAL_ORC_14:
      break;
    case REFUSAL_ORC_15:
      break;
    case REFUSAL_ORC_16:
      break;
    case REFUSAL_ORC_17:
      break;
    case REFUSAL_ORC_18:
      break;
    case REFUSAL_ORC_19:
      break;
    case REFUSAL_ORC_2:
      break;
    case REFUSAL_ORC_20:
      break;
    case REFUSAL_ORC_21:
      break;
    case REFUSAL_ORC_22:
      break;
    case REFUSAL_ORC_23:
      break;
    case REFUSAL_ORC_24:
      break;
    case REFUSAL_ORC_25:
      break;
    case REFUSAL_ORC_26:
      break;
    case REFUSAL_ORC_27:
      break;
    case REFUSAL_ORC_28:
      break;
    case REFUSAL_ORC_29:
      break;
    case REFUSAL_ORC_3:
      break;
    case REFUSAL_ORC_30:
      break;
    case REFUSAL_ORC_31:
      break;
    case REFUSAL_ORC_4:
      break;
    case REFUSAL_ORC_5:
      break;
    case REFUSAL_ORC_6:
      break;
    case REFUSAL_ORC_7:
      break;
    case REFUSAL_ORC_8:
      break;
    case REFUSAL_ORC_9:
      break;
    case ADMIN_OBX_30945_0:
      break;
    case ADMIN_OBX_30963_3:
      break;
    case ADMIN_OBX_31044_1:
      break;
    case ADMIN_OBX_59784_9:
      break;
    case ADMIN_OBX_59785_6:
      break;
    case ADMIN_RXA_9_FREE:
      break;
    case NK1_5_FAX:
      break;
    case PID_13_FAX:
      break;
    }

    return testCaseMessage;
  }

  public static void convertHistoryToNonAdmin(TestCaseMessage testCaseMessage) {
    testCaseMessage.appendAdditionalTransformation("RXA#2-5.1=998");
    testCaseMessage.appendAdditionalTransformation("RXA#2-5.2=no vaccine administered");
    testCaseMessage.appendAdditionalTransformation("RXA#2-6=999");
    testCaseMessage.appendAdditionalTransformation("RXA#2-9.1=");
    testCaseMessage.appendAdditionalTransformation("RXA#2-9.2=");
    testCaseMessage.appendAdditionalTransformation("RXA#2-9.3=");
    testCaseMessage.appendAdditionalTransformation("RXA#2-20=NA");
    testCaseMessage.setHasIssue(true);
  }

  public static TestCaseMessage getAbsentTestCase(ProfileField field, TestCaseMessage defaultTestCaseMessage) {
    TestCaseMessage testCaseMessage = null;
    if (testCaseMessage == null) {
      testCaseMessage = createTestCaseMessage(SCENARIO_FULL_RECORD_FOR_PROFILING);
    }
    testCaseMessage.setHasIssue(false);

    switch (field) {
    case MSH_3:
      testCaseMessage.appendAdditionalTransformation("MSH-3.1=");
      testCaseMessage.appendAdditionalTransformation("MSH-3.2=");
      testCaseMessage.appendAdditionalTransformation("MSH-3.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_3_1:
      testCaseMessage.appendAdditionalTransformation("MSH-3.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_3_2:
      testCaseMessage.appendAdditionalTransformation("MSH-3.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_4:
      testCaseMessage.appendAdditionalTransformation("MSH-4.1=");
      testCaseMessage.appendAdditionalTransformation("MSH-4.2=");
      testCaseMessage.appendAdditionalTransformation("MSH-4.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_4_1:
      testCaseMessage.appendAdditionalTransformation("MSH-4.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_4_2:
      testCaseMessage.appendAdditionalTransformation("MSH-4.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_5:
      testCaseMessage.appendAdditionalTransformation("MSH-5.1=");
      testCaseMessage.appendAdditionalTransformation("MSH-5.2=");
      testCaseMessage.appendAdditionalTransformation("MSH-5.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_5_1:
      testCaseMessage.appendAdditionalTransformation("MSH-5.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_5_2:
      testCaseMessage.appendAdditionalTransformation("MSH-5.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_6:
      testCaseMessage.appendAdditionalTransformation("MSH-6.1=");
      testCaseMessage.appendAdditionalTransformation("MSH-6.2=");
      testCaseMessage.appendAdditionalTransformation("MSH-6.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_6_1:
      testCaseMessage.appendAdditionalTransformation("MSH-6.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_6_2:
      testCaseMessage.appendAdditionalTransformation("MSH-6.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_7:
      testCaseMessage.appendAdditionalTransformation("MSH-7=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_7_TZ:
      testCaseMessage.appendAdditionalTransformation("MSH-7=[NOW_NO_TIMEZONE]");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_8:
      testCaseMessage.appendAdditionalTransformation("MSH-8=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_9:
      testCaseMessage.appendAdditionalTransformation("MSH-9.1=");
      testCaseMessage.appendAdditionalTransformation("MSH-9.2=");
      testCaseMessage.appendAdditionalTransformation("MSH-9.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_10:
      testCaseMessage.appendAdditionalTransformation("MSH-10=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_11:
      testCaseMessage.appendAdditionalTransformation("MSH-11=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_12:
      testCaseMessage.appendAdditionalTransformation("MSH-12=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_15:
      testCaseMessage.appendAdditionalTransformation("MSH-16=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_16:
      testCaseMessage.appendAdditionalTransformation("MSH-16=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_17:
      testCaseMessage.appendAdditionalTransformation("MSH-17=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_21:
      testCaseMessage.appendAdditionalTransformation("MSH-21=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_22:
      testCaseMessage.appendAdditionalTransformation("MSH-22.1=");
      testCaseMessage.appendAdditionalTransformation("MSH-22.2=");
      testCaseMessage.appendAdditionalTransformation("MSH-22.3=");
      testCaseMessage.appendAdditionalTransformation("MSH-22.4=");
      testCaseMessage.appendAdditionalTransformation("MSH-22.5=");
      testCaseMessage.appendAdditionalTransformation("MSH-22.6=");
      testCaseMessage.appendAdditionalTransformation("MSH-22.7=");
      testCaseMessage.appendAdditionalTransformation("MSH-22.8=");
      testCaseMessage.appendAdditionalTransformation("MSH-22.9=");
      testCaseMessage.appendAdditionalTransformation("MSH-22.10=");
      testCaseMessage.setHasIssue(true);
      break;
    case MSH_23:
      testCaseMessage.appendAdditionalTransformation("MSH-23.1=");
      testCaseMessage.appendAdditionalTransformation("MSH-23.2=");
      testCaseMessage.appendAdditionalTransformation("MSH-23.3=");
      testCaseMessage.appendAdditionalTransformation("MSH-23.4=");
      testCaseMessage.appendAdditionalTransformation("MSH-23.5=");
      testCaseMessage.appendAdditionalTransformation("MSH-23.6=");
      testCaseMessage.appendAdditionalTransformation("MSH-23.7=");
      testCaseMessage.appendAdditionalTransformation("MSH-23.8=");
      testCaseMessage.appendAdditionalTransformation("MSH-23.9=");
      testCaseMessage.appendAdditionalTransformation("MSH-23.10=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID:
      testCaseMessage.appendAdditionalTransformation("remove segment PID");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_1:
      testCaseMessage.appendAdditionalTransformation("PID-1=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_3:
      testCaseMessage.appendAdditionalTransformation("PID-3.1=");
      testCaseMessage.appendAdditionalTransformation("PID-3.2=");
      testCaseMessage.appendAdditionalTransformation("PID-3.3=");
      testCaseMessage.appendAdditionalTransformation("PID-3.4=");
      testCaseMessage.appendAdditionalTransformation("PID-3.5=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_3_1:
      testCaseMessage.appendAdditionalTransformation("PID-3.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_3_4:
      testCaseMessage.appendAdditionalTransformation("PID-3.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_3_5:
      testCaseMessage.appendAdditionalTransformation("PID-3.5=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_3_SSN:
      return defaultTestCaseMessage;

    case PID_3_MEDICAID:
      return defaultTestCaseMessage;

    case PID_5:
      testCaseMessage.appendAdditionalTransformation("PID-5.1=");
      testCaseMessage.appendAdditionalTransformation("PID-5.2=");
      testCaseMessage.appendAdditionalTransformation("PID-5.3=");
      testCaseMessage.appendAdditionalTransformation("PID-5.4=");
      testCaseMessage.appendAdditionalTransformation("PID-5.5=");
      testCaseMessage.appendAdditionalTransformation("PID-5.6=");
      testCaseMessage.appendAdditionalTransformation("PID-5.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_5_1:
      testCaseMessage.appendAdditionalTransformation("PID-5.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_5_2:
      testCaseMessage.appendAdditionalTransformation("PID-5.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_5_3:
      testCaseMessage.appendAdditionalTransformation("PID-5.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_5_4:
      return defaultTestCaseMessage;

    case PID_5_7:
      testCaseMessage.appendAdditionalTransformation("PID-5.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_6:
      testCaseMessage.appendAdditionalTransformation("PID-6.1=");
      testCaseMessage.appendAdditionalTransformation("PID-6.2=");
      testCaseMessage.appendAdditionalTransformation("PID-6.3=");
      testCaseMessage.appendAdditionalTransformation("PID-6.4=");
      testCaseMessage.appendAdditionalTransformation("PID-6.5=");
      testCaseMessage.appendAdditionalTransformation("PID-6.6=");
      testCaseMessage.appendAdditionalTransformation("PID-6.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_6_1:
      testCaseMessage.appendAdditionalTransformation("PID-6.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_6_2:
      testCaseMessage.appendAdditionalTransformation("PID-6.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_6_3:
      testCaseMessage.appendAdditionalTransformation("PID-6.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_6_7:
      testCaseMessage.appendAdditionalTransformation("PID-6.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_7:
      testCaseMessage.appendAdditionalTransformation("PID-7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_8:
      testCaseMessage.appendAdditionalTransformation("PID-8=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_10:
      testCaseMessage.appendAdditionalTransformation("PID-10.1=");
      testCaseMessage.appendAdditionalTransformation("PID-10.2=");
      testCaseMessage.appendAdditionalTransformation("PID-10.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_11:
      testCaseMessage.appendAdditionalTransformation("PID-11.1=");
      testCaseMessage.appendAdditionalTransformation("PID-11.2=");
      testCaseMessage.appendAdditionalTransformation("PID-11.3=");
      testCaseMessage.appendAdditionalTransformation("PID-11.4=");
      testCaseMessage.appendAdditionalTransformation("PID-11.5=");
      testCaseMessage.appendAdditionalTransformation("PID-11.6=");
      testCaseMessage.appendAdditionalTransformation("PID-11.7=");
      testCaseMessage.appendAdditionalTransformation("PID-11.9=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_11_1:
      testCaseMessage.appendAdditionalTransformation("PID-11.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_11_2:
      testCaseMessage.appendAdditionalTransformation("PID-11.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_11_3:
      testCaseMessage.appendAdditionalTransformation("PID-11.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_11_4:
      testCaseMessage.appendAdditionalTransformation("PID-11.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_11_5:
      testCaseMessage.appendAdditionalTransformation("PID-11.5=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_11_6:
      testCaseMessage.appendAdditionalTransformation("PID-11.6=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_11_7:
      testCaseMessage.appendAdditionalTransformation("PID-11.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_11_9:
      testCaseMessage.appendAdditionalTransformation("PID-11.9=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_13:
      testCaseMessage.appendAdditionalTransformation("PID-13.1=");
      testCaseMessage.appendAdditionalTransformation("PID-13.2=");
      testCaseMessage.appendAdditionalTransformation("PID-13.3=");
      testCaseMessage.appendAdditionalTransformation("PID-13.4=");
      testCaseMessage.appendAdditionalTransformation("PID-13.5=");
      testCaseMessage.appendAdditionalTransformation("PID-13.6=");
      testCaseMessage.appendAdditionalTransformation("PID-13.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_13_1:
      testCaseMessage.appendAdditionalTransformation("PID-13.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_13_2:
      testCaseMessage.appendAdditionalTransformation("PID-13.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_13_3:
      testCaseMessage.appendAdditionalTransformation("PID-13.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_13_6:
      testCaseMessage.appendAdditionalTransformation("PID-13.6=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_13_7:
      testCaseMessage.appendAdditionalTransformation("PID-13.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_13_EMAIL:
      testCaseMessage.appendAdditionalTransformation("PID-13.1#1=");
      testCaseMessage.appendAdditionalTransformation("PID-13.2#2=");
      testCaseMessage.appendAdditionalTransformation("PID-13.3#2=");
      testCaseMessage.appendAdditionalTransformation("PID-13.4#2=");
      testCaseMessage.appendAdditionalTransformation("PID-13.5#2=");
      testCaseMessage.appendAdditionalTransformation("PID-13.6#2=");
      testCaseMessage.appendAdditionalTransformation("PID-13.7#2=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_14:
      testCaseMessage.appendAdditionalTransformation("PID-14.1=");
      testCaseMessage.appendAdditionalTransformation("PID-14.2=");
      testCaseMessage.appendAdditionalTransformation("PID-14.3=");
      testCaseMessage.appendAdditionalTransformation("PID-14.6=");
      testCaseMessage.appendAdditionalTransformation("PID-14.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_14_1:
      testCaseMessage.appendAdditionalTransformation("PID-14.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_14_2:
      testCaseMessage.appendAdditionalTransformation("PID-14.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_14_3:
      testCaseMessage.appendAdditionalTransformation("PID-14.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_14_6:
      testCaseMessage.appendAdditionalTransformation("PID-14.6=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_14_7:
      testCaseMessage.appendAdditionalTransformation("PID-14.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_15:
      testCaseMessage.appendAdditionalTransformation("PID-15.1=");
      testCaseMessage.appendAdditionalTransformation("PID-15.2=");
      testCaseMessage.appendAdditionalTransformation("PID-15.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_22:
      testCaseMessage.appendAdditionalTransformation("PID-22.1=");
      testCaseMessage.appendAdditionalTransformation("PID-22.2=");
      testCaseMessage.appendAdditionalTransformation("PID-22.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_24:
      testCaseMessage.appendAdditionalTransformation("PID-24=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_25:
      testCaseMessage.appendAdditionalTransformation("PID-25=");
      testCaseMessage.setHasIssue(true);
      break;
    case PID_29:
      return defaultTestCaseMessage;

    case PID_30:
      testCaseMessage.appendAdditionalTransformation("PID-30=");
      testCaseMessage.setHasIssue(true);
      break;
    case PD1:
      testCaseMessage.appendAdditionalTransformation("remove segment PD1");
      testCaseMessage.setHasIssue(true);
      break;
    case PD1_3:
      return defaultTestCaseMessage;

    case PD1_3_1:
      return defaultTestCaseMessage;

    case PD1_3_2:
      return defaultTestCaseMessage;

    case PD1_3_3:
      return defaultTestCaseMessage;

    case PD1_3_6:
      return defaultTestCaseMessage;

    case PD1_3_7:
      return defaultTestCaseMessage;

    case PD1_3_10:
      return defaultTestCaseMessage;

    case PD1_4:
      return defaultTestCaseMessage;

    case PD1_4_1:
      return defaultTestCaseMessage;

    case PD1_4_2:
      return defaultTestCaseMessage;

    case PD1_4_3:
      return defaultTestCaseMessage;

    case PD1_4_4:
      return defaultTestCaseMessage;

    case PD1_4_9:
      return defaultTestCaseMessage;

    case PD1_4_10:
      return defaultTestCaseMessage;

    case PD1_4_13:
      return defaultTestCaseMessage;

    case PD1_4_21:
      return defaultTestCaseMessage;

    case PD1_11:
      testCaseMessage.appendAdditionalTransformation("PD1-11.1=");
      testCaseMessage.appendAdditionalTransformation("PD1-11.2=");
      testCaseMessage.appendAdditionalTransformation("PD1-11.3=");
      testCaseMessage.appendAdditionalTransformation("PD1-18=");
      testCaseMessage.setHasIssue(true);
      testCaseMessage.setHasIssue(true);
      break;
    case PD1_12:
      testCaseMessage.appendAdditionalTransformation("PD1-12=");
      testCaseMessage.appendAdditionalTransformation("PD1-13=");
      testCaseMessage.setHasIssue(true);
      break;
    case PD1_13:
      testCaseMessage.appendAdditionalTransformation("PD1-13=");
      testCaseMessage.setHasIssue(true);
      break;
    case PD1_16:
      testCaseMessage.appendAdditionalTransformation("PD1-16=");
      testCaseMessage.appendAdditionalTransformation("PD1-17=");
      testCaseMessage.setHasIssue(true);
      break;
    case PD1_17:
      testCaseMessage.appendAdditionalTransformation("PD1-17=");
      testCaseMessage.setHasIssue(true);
      break;
    case PD1_18:
      testCaseMessage.appendAdditionalTransformation("PD1-18=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1:
      testCaseMessage.appendAdditionalTransformation("remove segment NK1");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_1:
      testCaseMessage.appendAdditionalTransformation("NK1-1=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_2:
      testCaseMessage.appendAdditionalTransformation("NK1-2.1=");
      testCaseMessage.appendAdditionalTransformation("NK1-2.2=");
      testCaseMessage.appendAdditionalTransformation("NK1-2.3=");
      testCaseMessage.appendAdditionalTransformation("NK1-2.4=");
      testCaseMessage.appendAdditionalTransformation("NK1-2.5=");
      testCaseMessage.appendAdditionalTransformation("NK1-2.6=");
      testCaseMessage.appendAdditionalTransformation("NK1-2.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_2_1:
      testCaseMessage.appendAdditionalTransformation("NK1-2.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_2_2:
      testCaseMessage.appendAdditionalTransformation("NK1-2.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_2_3:
      testCaseMessage.appendAdditionalTransformation("NK1-2.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_2_7:
      testCaseMessage.appendAdditionalTransformation("NK1-2.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_3:
      testCaseMessage.appendAdditionalTransformation("NK1-3.1=");
      testCaseMessage.appendAdditionalTransformation("NK1-3.2=");
      testCaseMessage.appendAdditionalTransformation("NK1-3.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_4:
      testCaseMessage.appendAdditionalTransformation("NK1-4.1=");
      testCaseMessage.appendAdditionalTransformation("NK1-4.2=");
      testCaseMessage.appendAdditionalTransformation("NK1-4.3=");
      testCaseMessage.appendAdditionalTransformation("NK1-4.4=");
      testCaseMessage.appendAdditionalTransformation("NK1-4.5=");
      testCaseMessage.appendAdditionalTransformation("NK1-4.6=");
      testCaseMessage.appendAdditionalTransformation("NK1-4.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_4_1:
      testCaseMessage.appendAdditionalTransformation("NK1-4.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_4_2:
      testCaseMessage.appendAdditionalTransformation("NK1-4.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_4_3:
      testCaseMessage.appendAdditionalTransformation("NK1-4.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_4_4:
      testCaseMessage.appendAdditionalTransformation("NK1-4.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_4_5:
      testCaseMessage.appendAdditionalTransformation("NK1-4.5=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_4_6:
      testCaseMessage.appendAdditionalTransformation("NK1-4.6=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_4_7:
      testCaseMessage.appendAdditionalTransformation("NK1-4.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_5:
      testCaseMessage.appendAdditionalTransformation("NK1-5.1=");
      testCaseMessage.appendAdditionalTransformation("NK1-5.2=");
      testCaseMessage.appendAdditionalTransformation("NK1-5.3=");
      testCaseMessage.appendAdditionalTransformation("NK1-5.4=");
      testCaseMessage.appendAdditionalTransformation("NK1-5.5=");
      testCaseMessage.appendAdditionalTransformation("NK1-5.6=");
      testCaseMessage.appendAdditionalTransformation("NK1-5.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_5_1:
      testCaseMessage.appendAdditionalTransformation("NK1-5.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_5_2:
      testCaseMessage.appendAdditionalTransformation("NK1-5.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_5_3:
      testCaseMessage.appendAdditionalTransformation("NK1-5.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_5_6:
      testCaseMessage.appendAdditionalTransformation("NK1-5.6=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_5_7:
      testCaseMessage.appendAdditionalTransformation("NK1-5.7=");
      testCaseMessage.setHasIssue(true);
      break;
    case NK1_6:
      return defaultTestCaseMessage;

    case NK1_7:
      return defaultTestCaseMessage;

    case PV1:
      testCaseMessage.appendAdditionalTransformation("remove segment PV1");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_1:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-20=[VFC]");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_2:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=");
      testCaseMessage.appendAdditionalTransformation("PV1-20=[VFC]");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_7:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-7.1=");
      testCaseMessage.appendAdditionalTransformation("PV1-7.2=");
      testCaseMessage.appendAdditionalTransformation("PV1-7.3=");
      testCaseMessage.appendAdditionalTransformation("PV1-7.4=");
      testCaseMessage.appendAdditionalTransformation("PV1-20=[VFC]");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_7_1:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-7.1=");
      testCaseMessage.appendAdditionalTransformation("PV1-20=[VFC]");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_7_2:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-7.2=");
      testCaseMessage.appendAdditionalTransformation("PV1-20=[VFC]");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_7_3:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-7.3=");
      testCaseMessage.appendAdditionalTransformation("PV1-20=[VFC]");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_7_4:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-7.4=");
      testCaseMessage.appendAdditionalTransformation("PV1-20=[VFC]");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_19:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-20=[VFC]");
      testCaseMessage.appendAdditionalTransformation("PV1-19=");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_20:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-20.1=");
      testCaseMessage.appendAdditionalTransformation("PV1-20.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_20_1:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-20.1=");
      testCaseMessage.appendAdditionalTransformation("PV1-20.2=[VAC1_DATE]");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_20_2:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-20.1=[VFC]");
      testCaseMessage.appendAdditionalTransformation("PV1-20.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case PV1_44:
      testCaseMessage.appendAdditionalTransformation("insert segment PV1 after PID if missing");
      testCaseMessage.appendAdditionalTransformation("PV1-1=1");
      testCaseMessage.appendAdditionalTransformation("PV1-2=R");
      testCaseMessage.appendAdditionalTransformation("PV1-44=");
      testCaseMessage.setHasIssue(true);
      break;
    case ORC:
      testCaseMessage.appendAdditionalTransformation("remove segment ORC");
      testCaseMessage.setHasIssue(true);
      break;
    case ORC_GROUP:
      testCaseMessage.appendAdditionalTransformation("remove segment ORC all");
      testCaseMessage.appendAdditionalTransformation("remove segment RXA all");
      testCaseMessage.appendAdditionalTransformation("remove segment RXR all");
      testCaseMessage.appendAdditionalTransformation("remove segment OBX all");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_1:
      testCaseMessage.appendAdditionalTransformation("ORC-1=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_2:
      testCaseMessage.appendAdditionalTransformation("ORC-2.1=");
      testCaseMessage.appendAdditionalTransformation("ORC-2.2=");
      testCaseMessage.appendAdditionalTransformation("ORC-2.3=");
      testCaseMessage.appendAdditionalTransformation("ORC-2.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_2_1:
      testCaseMessage.appendAdditionalTransformation("ORC-2.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_2_2:
      testCaseMessage.appendAdditionalTransformation("ORC-2.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_2_3:
      testCaseMessage.appendAdditionalTransformation("ORC-2.3=");
      testCaseMessage.appendAdditionalTransformation("ORC-2.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_3:
      testCaseMessage.appendAdditionalTransformation("ORC-3.1=");
      testCaseMessage.appendAdditionalTransformation("ORC-3.2=");
      testCaseMessage.appendAdditionalTransformation("ORC-3.3=");
      testCaseMessage.appendAdditionalTransformation("ORC-3.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_3_1:
      testCaseMessage.appendAdditionalTransformation("ORC-3.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_3_2:
      testCaseMessage.appendAdditionalTransformation("ORC-3.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_3_3:
      testCaseMessage.appendAdditionalTransformation("ORC-3.3=");
      testCaseMessage.appendAdditionalTransformation("ORC-3.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_5:
      testCaseMessage.appendAdditionalTransformation("ORC-5=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_10:
      testCaseMessage.appendAdditionalTransformation("ORC-10.1=");
      testCaseMessage.appendAdditionalTransformation("ORC-10.2=");
      testCaseMessage.appendAdditionalTransformation("ORC-10.3=");
      testCaseMessage.appendAdditionalTransformation("ORC-10.4=");
      testCaseMessage.appendAdditionalTransformation("ORC-10.5=");
      testCaseMessage.appendAdditionalTransformation("ORC-10.6=");
      testCaseMessage.appendAdditionalTransformation("ORC-10.7=");
      testCaseMessage.appendAdditionalTransformation("ORC-10.8=");
      testCaseMessage.appendAdditionalTransformation("ORC-10.9=");
      testCaseMessage.appendAdditionalTransformation("ORC-10.10=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_10_1:
      testCaseMessage.appendAdditionalTransformation("ORC-10.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_10_2:
      testCaseMessage.appendAdditionalTransformation("ORC-10.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_10_3:
      testCaseMessage.appendAdditionalTransformation("ORC-10.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_10_4:
      testCaseMessage.appendAdditionalTransformation("ORC-10.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_12:
      testCaseMessage.appendAdditionalTransformation("ORC-12.1=");
      testCaseMessage.appendAdditionalTransformation("ORC-12.2=");
      testCaseMessage.appendAdditionalTransformation("ORC-12.3=");
      testCaseMessage.appendAdditionalTransformation("ORC-12.4=");
      testCaseMessage.appendAdditionalTransformation("ORC-12.5=");
      testCaseMessage.appendAdditionalTransformation("ORC-12.6=");
      testCaseMessage.appendAdditionalTransformation("ORC-12.7=");
      testCaseMessage.appendAdditionalTransformation("ORC-12.8=");
      testCaseMessage.appendAdditionalTransformation("ORC-12.9=");
      testCaseMessage.appendAdditionalTransformation("ORC-12.10=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_12_1:
      testCaseMessage.appendAdditionalTransformation("ORC-12.1=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_12_2:
      testCaseMessage.appendAdditionalTransformation("ORC-12.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_12_3:
      testCaseMessage.appendAdditionalTransformation("ORC-12.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_12_4:
      testCaseMessage.appendAdditionalTransformation("ORC-12.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_17:
      testCaseMessage.appendAdditionalTransformation("ORC-17.1=");
      testCaseMessage.appendAdditionalTransformation("ORC-17.2=");
      testCaseMessage.appendAdditionalTransformation("ORC-17.3=");
      testCaseMessage.appendAdditionalTransformation("ORC-17.4=");
      testCaseMessage.appendAdditionalTransformation("ORC-17.5=");
      testCaseMessage.appendAdditionalTransformation("ORC-17.6=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_17_1:
      testCaseMessage.appendAdditionalTransformation("ORC-17.1=");
      testCaseMessage.appendAdditionalTransformation("ORC-17.2=");
      testCaseMessage.appendAdditionalTransformation("ORC-17.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_ORC_17_4:
      return defaultTestCaseMessage;

    case ADMIN_RXA:
      testCaseMessage.appendAdditionalTransformation("remove segment RXA");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_1:
      testCaseMessage.appendAdditionalTransformation("RXA-1=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_2:
      testCaseMessage.appendAdditionalTransformation("RXA-2=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_3:
      testCaseMessage.appendAdditionalTransformation("RXA-3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_4:
      testCaseMessage.appendAdditionalTransformation("RXA-4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_5:
      testCaseMessage.appendAdditionalTransformation("RXA-5.1=");
      testCaseMessage.appendAdditionalTransformation("RXA-5.2=");
      testCaseMessage.appendAdditionalTransformation("RXA-5.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_6:
      testCaseMessage.appendAdditionalTransformation("RXA-6=999");
      testCaseMessage.appendAdditionalTransformation("RXA-7.1=");
      testCaseMessage.appendAdditionalTransformation("RXA-7.2=");
      testCaseMessage.appendAdditionalTransformation("RXA-7.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_7:
      testCaseMessage.appendAdditionalTransformation("RXA-7.1=");
      testCaseMessage.appendAdditionalTransformation("RXA-7.2=");
      testCaseMessage.appendAdditionalTransformation("RXA-7.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_9:
      testCaseMessage.appendAdditionalTransformation("RXA-9.1=");
      testCaseMessage.appendAdditionalTransformation("RXA-9.2=");
      testCaseMessage.appendAdditionalTransformation("RXA-9.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_10:
      testCaseMessage.appendAdditionalTransformation("RXA-10.1=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.2=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.3=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.4=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.5=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.6=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.7=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.8=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.9=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.10=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.11=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.12=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.13=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_10_1:
      testCaseMessage.appendAdditionalTransformation("RXA-10.1=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.9=");
      testCaseMessage.appendAdditionalTransformation("RXA-10.13=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_10_2:
      testCaseMessage.appendAdditionalTransformation("RXA-10.2=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_10_3:
      testCaseMessage.appendAdditionalTransformation("RXA-10.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_10_4:
      testCaseMessage.appendAdditionalTransformation("RXA-10.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_11:
      testCaseMessage.appendAdditionalTransformation("RXA-11.1=");
      testCaseMessage.appendAdditionalTransformation("RXA-11.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_11_4:
      testCaseMessage.appendAdditionalTransformation("RXA-11.4=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_15:
      testCaseMessage.appendAdditionalTransformation("RXA-15=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_16:
      testCaseMessage.appendAdditionalTransformation("RXA-16=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_17:
      testCaseMessage.appendAdditionalTransformation("RXA-17.1=");
      testCaseMessage.appendAdditionalTransformation("RXA-17.2=");
      testCaseMessage.appendAdditionalTransformation("RXA-17.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_20:
      testCaseMessage.appendAdditionalTransformation("RXA-20=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_21:
      testCaseMessage.appendAdditionalTransformation("RXA-21=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXA_22:
      testCaseMessage.appendAdditionalTransformation("RXA-22=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXR_1:
      testCaseMessage.appendAdditionalTransformation("RXR-1.1=");
      testCaseMessage.appendAdditionalTransformation("RXR-1.2=");
      testCaseMessage.appendAdditionalTransformation("RXR-1.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_RXR_2:
      testCaseMessage.appendAdditionalTransformation("RXR-2.1=");
      testCaseMessage.appendAdditionalTransformation("RXR-2.2=");
      testCaseMessage.appendAdditionalTransformation("RXR-2.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_1:
      testCaseMessage.appendAdditionalTransformation("OBX-1=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_2:
      testCaseMessage.appendAdditionalTransformation("OBX-2=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_3:
      testCaseMessage.appendAdditionalTransformation("OBX-3.1=");
      testCaseMessage.appendAdditionalTransformation("OBX-3.2=");
      testCaseMessage.appendAdditionalTransformation("OBX-3.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_4:
      testCaseMessage.appendAdditionalTransformation("OBX#3-4*=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_5:
      testCaseMessage.appendAdditionalTransformation("OBX-5.1=");
      testCaseMessage.appendAdditionalTransformation("OBX-5.2=");
      testCaseMessage.appendAdditionalTransformation("OBX-5.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_11:
      testCaseMessage.appendAdditionalTransformation("OBX-11*=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_14:
      testCaseMessage.appendAdditionalTransformation("OBX-14*=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_17:
      testCaseMessage.appendAdditionalTransformation("OBX-14.1*=");
      testCaseMessage.appendAdditionalTransformation("OBX-14.2*=");
      testCaseMessage.appendAdditionalTransformation("OBX-14.3*=");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_64994_7:
      testCaseMessage.appendAdditionalTransformation("remove observation 64994-7");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_69764_9:
      testCaseMessage.appendAdditionalTransformation("remove observation 69764-9");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_29768_9:
      testCaseMessage.appendAdditionalTransformation("remove observation 29768-9");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_30956_7:
      testCaseMessage.appendAdditionalTransformation("remove observation 30956-7");
      testCaseMessage.setHasIssue(true);
      break;
    case ADMIN_OBX_29769_7:
      testCaseMessage.appendAdditionalTransformation("remove observation 29769-7");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_1:
      testCaseMessage.appendAdditionalTransformation("RXA#2-1=");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_2:
      testCaseMessage.appendAdditionalTransformation("RXA#2-2=");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_3:
      testCaseMessage.appendAdditionalTransformation("RXA#2-3=");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_4:
      testCaseMessage.appendAdditionalTransformation("RXA#2-4=");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_5:
      testCaseMessage.appendAdditionalTransformation("RXA#2-5.1=");
      testCaseMessage.appendAdditionalTransformation("RXA#2-5.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2-5.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_6:
      testCaseMessage.appendAdditionalTransformation("RXA#2-6=");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.1=");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.3==");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_7:
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.1=");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2-7.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_9:
      testCaseMessage.appendAdditionalTransformation("RXA#2-9.1=");
      testCaseMessage.appendAdditionalTransformation("RXA#2-9.2=");
      testCaseMessage.appendAdditionalTransformation("RXA#2-9.3=");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_10:
      return defaultTestCaseMessage;

    case HIST_RXA_10_1:
      return defaultTestCaseMessage;

    case HIST_RXA_10_2:
      return defaultTestCaseMessage;

    case HIST_RXA_10_3:
      return defaultTestCaseMessage;

    case HIST_RXA_10_4:
      return defaultTestCaseMessage;

    case HIST_RXA_11:
      return defaultTestCaseMessage;

    case HIST_RXA_11_4:
      return defaultTestCaseMessage;

    case HIST_RXA_15:
      return defaultTestCaseMessage;

    case HIST_RXA_16:
      return defaultTestCaseMessage;

    case HIST_RXA_17:
      return defaultTestCaseMessage;

    case HIST_RXA_20:
      testCaseMessage.appendAdditionalTransformation("RXA#2-20=");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXA_21:
      testCaseMessage.appendAdditionalTransformation("RXA#2-21=");
      testCaseMessage.setHasIssue(true);
      break;
    case HIST_RXR_1:
      return defaultTestCaseMessage;

    case HIST_RXR_2:
      return defaultTestCaseMessage;
    case ADMIN_NTE:
      break;
    case ADMIN_OBX:
      break;
    case ADMIN_RXA_18:
      return defaultTestCaseMessage;

    case ADMIN_RXR:
      break;

    case GT1:
      return defaultTestCaseMessage;

    case HIST_NTE:
      return defaultTestCaseMessage;

    case HIST_OBX:
      return defaultTestCaseMessage;

    case HIST_RXR:
      return defaultTestCaseMessage;

    case IN1:
      break;
    case IN1_GROUP:
      return defaultTestCaseMessage;

    case IN2:
      return defaultTestCaseMessage;

    case IN3:
      return defaultTestCaseMessage;

    case NK1_16:
      return defaultTestCaseMessage;

    case NK1_6_1:
      return defaultTestCaseMessage;

    case NK1_6_2:
      break;
    case NK1_6_3:
      break;
    case NK1_6_6:
      break;
    case NK1_6_7:
      break;
    case NK1_6_8:
      return defaultTestCaseMessage;

    case NON_ADMIN_NTE:
      return defaultTestCaseMessage;

    case NON_ADMIN_RXA_1:
      break;
    case NON_ADMIN_RXA_10:
      break;
    case NON_ADMIN_RXA_10_1:
      break;
    case NON_ADMIN_RXA_10_2:
      break;
    case NON_ADMIN_RXA_10_3:
      break;
    case NON_ADMIN_RXA_10_4:
      break;
    case NON_ADMIN_RXA_11:
      break;
    case NON_ADMIN_RXA_11_4:
      break;
    case NON_ADMIN_RXA_15:
      break;
    case NON_ADMIN_RXA_16:
      break;
    case NON_ADMIN_RXA_17:
      break;
    case NON_ADMIN_RXA_2:
      break;
    case NON_ADMIN_RXA_20:
      break;
    case NON_ADMIN_RXA_21:
      break;
    case NON_ADMIN_RXA_3:
      break;
    case NON_ADMIN_RXA_4:
      break;
    case NON_ADMIN_RXA_5:
      break;
    case NON_ADMIN_RXA_6:
      break;
    case NON_ADMIN_RXA_7:
      break;
    case NON_ADMIN_RXA_9:
      break;
    case NON_ADMIN_RXR:
      break;
    case NON_ADMIN_RXR_1:
      break;
    case NON_ADMIN_RXR_2:
      break;
    case ADMIN_ORC_10_9:
      break;
    case PID_5_1_ALIAS:
      break;
    case PID_5_2_ALIAS:
      break;
    case PID_5_3_ALIAS:
      break;
    case PID_5_4_ALIAS:
      break;
    case PID_5_7_ALIAS:
      break;
    case PID_5_ALIAS:
      break;
    case PV2:
      break;
    case REFUSAL_NTE:
      break;
    case REFUSAL_OBX:
      break;
    case REFUSAL_RXA_1:
      break;
    case REFUSAL_RXA_10:
      break;
    case REFUSAL_RXA_10_1:
      break;
    case REFUSAL_RXA_10_2:
      break;
    case REFUSAL_RXA_10_3:
      break;
    case REFUSAL_RXA_10_4:
      break;
    case REFUSAL_RXA_11:
      break;
    case REFUSAL_RXA_11_4:
      break;
    case REFUSAL_RXA_15:
      break;
    case REFUSAL_RXA_16:
      break;
    case REFUSAL_RXA_17:
      break;
    case REFUSAL_RXA_2:
      break;
    case REFUSAL_RXA_20:
      break;
    case REFUSAL_RXA_21:
      break;
    case REFUSAL_RXA_3:
      break;
    case REFUSAL_RXA_4:
      break;
    case REFUSAL_RXA_5:
      break;
    case REFUSAL_RXA_6:
      break;
    case REFUSAL_RXA_7:
      break;
    case REFUSAL_RXA_9:
      break;
    case REFUSAL_RXR:
      break;
    case REFUSAL_RXR_1:
      break;
    case REFUSAL_RXR_2:
      break;
    case SFT:
      break;
    case TQ1:
      break;
    case TQ2:
      break;
    case ADMIN_OBX_10:
      break;
    case ADMIN_OBX_12:
      break;
    case ADMIN_OBX_13:
      break;
    case ADMIN_OBX_15:
      break;
    case ADMIN_OBX_16:
      break;
    case ADMIN_OBX_18:
      break;
    case ADMIN_OBX_19:
      break;
    case ADMIN_OBX_20:
      break;
    case ADMIN_OBX_21:
      break;
    case ADMIN_OBX_22:
      break;
    case ADMIN_OBX_23:
      break;
    case ADMIN_OBX_24:
      break;
    case ADMIN_OBX_25:
      break;
    case ADMIN_OBX_6:
      break;
    case ADMIN_OBX_7:
      break;
    case ADMIN_OBX_8:
      break;
    case ADMIN_OBX_9:
      break;
    case ADMIN_RCA_10_23:
      break;
    case ADMIN_RXA_10_10:
      break;
    case ADMIN_RXA_10_11:
      break;
    case ADMIN_RXA_10_12:
      break;
    case ADMIN_RXA_10_13:
      break;
    case ADMIN_RXA_10_14:
      break;
    case ADMIN_RXA_10_15:
      break;
    case ADMIN_RXA_10_16:
      break;
    case ADMIN_RXA_10_17:
      break;
    case ADMIN_RXA_10_18:
      break;
    case ADMIN_RXA_10_19:
      break;
    case ADMIN_RXA_10_20:
      break;
    case ADMIN_RXA_10_21:
      break;
    case ADMIN_RXA_10_22:
      break;
    case ADMIN_RXA_10_5:
      break;
    case ADMIN_RXA_10_6:
      break;
    case ADMIN_RXA_10_7:
      break;
    case ADMIN_RXA_10_8:
      break;
    case ADMIN_RXA_10_9:
      break;
    case ADMIN_RXA_11_1:
      break;
    case ADMIN_RXA_11_10:
      break;
    case ADMIN_RXA_11_11:
      break;
    case ADMIN_RXA_11_12:
      break;
    case ADMIN_RXA_11_13:
      break;
    case ADMIN_RXA_11_14:
      break;
    case ADMIN_RXA_11_15:
      break;
    case ADMIN_RXA_11_16:
      break;
    case ADMIN_RXA_11_2:
      break;
    case ADMIN_RXA_11_3:
      break;
    case ADMIN_RXA_11_5:
      break;
    case ADMIN_RXA_11_6:
      break;
    case ADMIN_RXA_11_7:
      break;
    case ADMIN_RXA_11_8:
      break;
    case ADMIN_RXA_11_9:
      break;
    case ADMIN_RXA_12:
      break;
    case ADMIN_RXA_13:
      break;
    case ADMIN_RXA_14:
      break;
    case ADMIN_RXA_16_YM:
      break;
    case ADMIN_RXA_19:
      break;
    case ADMIN_RXA_23:
      break;
    case ADMIN_RXA_24:
      break;
    case ADMIN_RXA_25:
      break;
    case ADMIN_RXA_26:
      break;
    case ADMIN_RXA_8:
      break;
    case ADMIN_RXR_3:
      break;
    case ADMIN_RXR_4:
      break;
    case ADMIN_RXR_5:
      break;
    case ADMIN_RXR_6:
      break;
    case HIST_RXA:
      break;

    case MSH_13:
      return defaultTestCaseMessage;

    case MSH_14:
      return defaultTestCaseMessage;

    case MSH_18:
      return defaultTestCaseMessage;

    case MSH_19:
      return defaultTestCaseMessage;

    case MSH_20:
      return defaultTestCaseMessage;

    case MSH_24:
      return defaultTestCaseMessage;

    case MSH_25:
      return defaultTestCaseMessage;

    case NK1_10:
      return defaultTestCaseMessage;

    case NK1_11:
      return defaultTestCaseMessage;

    case NK1_12:
      return defaultTestCaseMessage;

    case NK1_13:
      return defaultTestCaseMessage;

    case NK1_14:
      return defaultTestCaseMessage;

    case NK1_15:
      return defaultTestCaseMessage;

    case NK1_17:
      return defaultTestCaseMessage;

    case NK1_18:
      return defaultTestCaseMessage;

    case NK1_19:
      return defaultTestCaseMessage;

    case NK1_20:
      return defaultTestCaseMessage;

    case NK1_21:
      return defaultTestCaseMessage;

    case NK1_22:
      return defaultTestCaseMessage;

    case NK1_23:
      return defaultTestCaseMessage;

    case NK1_24:
      return defaultTestCaseMessage;

    case NK1_25:
      return defaultTestCaseMessage;

    case NK1_26:
      return defaultTestCaseMessage;

    case NK1_27:
      return defaultTestCaseMessage;

    case NK1_28:
      return defaultTestCaseMessage;

    case NK1_29:
      return defaultTestCaseMessage;

    case NK1_30:
      return defaultTestCaseMessage;

    case NK1_31:
      return defaultTestCaseMessage;

    case NK1_32:
      return defaultTestCaseMessage;

    case NK1_33:
      return defaultTestCaseMessage;

    case NK1_34:
      return defaultTestCaseMessage;

    case NK1_35:
      return defaultTestCaseMessage;

    case NK1_36:
      return defaultTestCaseMessage;

    case NK1_37:
      return defaultTestCaseMessage;

    case NK1_38:
      return defaultTestCaseMessage;

    case NK1_39:
      return defaultTestCaseMessage;

    case NK1_8:
      return defaultTestCaseMessage;

    case NK1_9:
      return defaultTestCaseMessage;

    case NON_ADMIN_RXA:
      break;
    case ADMIN_ORC_10_10:
      break;
    case ADMIN_ORC_11:
      break;
    case ADMIN_ORC_12_10:
      break;
    case ADMIN_ORC_12_9:
      break;
    case ADMIN_ORC_13:
      break;
    case ADMIN_ORC_14:
      break;
    case ADMIN_ORC_15:
      break;
    case ADMIN_ORC_16:
      break;
    case ADMIN_ORC_18:
      break;
    case ADMIN_ORC_19:
      break;
    case ADMIN_ORC_20:
      break;
    case ADMIN_ORC_21:
      break;
    case ADMIN_ORC_22:
      break;
    case ADMIN_ORC_23:
      break;
    case ADMIN_ORC_24:
      break;
    case ADMIN_ORC_25:
      break;
    case ADMIN_ORC_26:
      break;
    case ADMIN_ORC_27:
      break;
    case ADMIN_ORC_28:
      break;
    case ADMIN_ORC_29:
      break;
    case ADMIN_ORC_30:
      break;
    case ADMIN_ORC_31:
      break;
    case ADMIN_ORC_4:
      break;
    case ADMIN_ORC_6:
      break;
    case ADMIN_ORC_7:
      break;
    case ADMIN_ORC_8:
      break;
    case ADMIN_ORC_9:
      break;
    case PD1_1:
      break;
    case PD1_10:
      break;
    case PD1_14:
      break;
    case PD1_15:
      break;
    case PD1_19:
      break;
    case PD1_2:
      break;
    case PD1_20:
      break;
    case PD1_21:
      break;
    case PD1_5:
      break;
    case PD1_6:
      break;
    case PD1_7:
      break;
    case PD1_8:
      break;
    case PD1_9:
      break;
    case PID_11_10:
      break;
    case PID_11_11:
      break;
    case PID_11_12:
      break;
    case PID_11_13:
      break;
    case PID_11_14:
      break;
    case PID_11_1_1:
      break;
    case PID_11_1_2:
      break;
    case PID_11_1_3:
      break;
    case PID_11_8:
      break;
    case PID_12:
      break;
    case PID_13_10:
      break;
    case PID_13_11:
      break;
    case PID_13_12:
      break;
    case PID_13_4:
      break;
    case PID_13_5:
      break;
    case PID_13_8:
      break;
    case PID_13_9:
      break;
    case PID_13_CELL:
      break;
    case PID_16:
      break;
    case PID_17:
      break;
    case PID_18:
      break;
    case PID_19:
      break;
    case PID_2:
      break;
    case PID_20:
      break;
    case PID_21:
      break;
    case PID_23:
      break;
    case PID_26:
      break;
    case PID_27:
      break;
    case PID_28:
      break;
    case PID_31:
      break;
    case PID_32:
      break;
    case PID_33:
      break;
    case PID_34:
      break;
    case PID_35:
      break;
    case PID_36:
      break;
    case PID_37:
      break;
    case PID_38:
      break;
    case PID_39:
      break;
    case PID_4:
      break;
    case PID_5_10:
      break;
    case PID_5_11:
      break;
    case PID_5_12:
      break;
    case PID_5_13:
      break;
    case PID_5_14:
      break;
    case PID_5_5:
      break;
    case PID_5_6:
      break;
    case PID_5_8:
      break;
    case PID_5_9:
      break;
    case PID_9:
      break;
    case REFUSAL_RXA:
      break;
    case REFUSAL_RXA_18:
      break;
    case IN1_1:
      break;
    case IN1_10:
      break;
    case IN1_11:
      break;
    case IN1_12:
      break;
    case IN1_13:
      break;
    case IN1_14:
      break;
    case IN1_15:
      break;
    case IN1_16:
      break;
    case IN1_17:
      break;
    case IN1_18:
      break;
    case IN1_19:
      break;
    case IN1_2:
      break;
    case IN1_20:
      break;
    case IN1_21:
      break;
    case IN1_22:
      break;
    case IN1_23:
      break;
    case IN1_24:
      break;
    case IN1_25:
      break;
    case IN1_26:
      break;
    case IN1_27:
      break;
    case IN1_28:
      break;
    case IN1_29:
      break;
    case IN1_3:
      break;
    case IN1_30:
      break;
    case IN1_31:
      break;
    case IN1_32:
      break;
    case IN1_33:
      break;
    case IN1_34:
      break;
    case IN1_35:
      break;
    case IN1_36:
      break;
    case IN1_37:
      break;
    case IN1_38:
      break;
    case IN1_39:
      break;
    case IN1_4:
      break;
    case IN1_40:
      break;
    case IN1_41:
      break;
    case IN1_42:
      break;
    case IN1_43:
      break;
    case IN1_44:
      break;
    case IN1_45:
      break;
    case IN1_46:
      break;
    case IN1_47:
      break;
    case IN1_48:
      break;
    case IN1_49:
      break;
    case IN1_5:
      break;
    case IN1_50:
      break;
    case IN1_51:
      break;
    case IN1_52:
      break;
    case IN1_53:
      break;
    case IN1_6:
      break;
    case IN1_7:
      break;
    case IN1_8:
      break;
    case IN1_9:
      break;
    case ADMIN_ORC:
      break;
    case ADMIN_ORC_12_7:
      break;
    case BHS:
      break;
    case BTS:
      break;
    case FHS:
      break;
    case FTS:
      break;
    case HIST_ORC:
      break;
    case HIST_ORC_1:
      break;
    case HIST_ORC_10:
      break;
    case HIST_ORC_11:
      break;
    case HIST_ORC_12:
      break;
    case HIST_ORC_13:
      break;
    case HIST_ORC_14:
      break;
    case HIST_ORC_15:
      break;
    case HIST_ORC_16:
      break;
    case HIST_ORC_17:
      break;
    case HIST_ORC_18:
      break;
    case HIST_ORC_19:
      break;
    case HIST_ORC_2:
      break;
    case HIST_ORC_20:
      break;
    case HIST_ORC_21:
      break;
    case HIST_ORC_22:
      break;
    case HIST_ORC_23:
      break;
    case HIST_ORC_24:
      break;
    case HIST_ORC_25:
      break;
    case HIST_ORC_26:
      break;
    case HIST_ORC_27:
      break;
    case HIST_ORC_28:
      break;
    case HIST_ORC_29:
      break;
    case HIST_ORC_3:
      break;
    case HIST_ORC_30:
      break;
    case HIST_ORC_31:
      break;
    case HIST_ORC_4:
      break;
    case HIST_ORC_5:
      break;
    case HIST_ORC_6:
      break;
    case HIST_ORC_7:
      break;
    case HIST_ORC_8:
      break;
    case HIST_ORC_9:
      break;
    case MSH:
      break;
    case NK1_4_HOME:
      break;
    case NK1_5_CELL:
      break;
    case NK1_5_EMAIL:
      break;
    case NON_ADMIN_OBX:
      break;
    case NON_ADMIN_ORC:
      break;
    case NON_ADMIN_ORC_1:
      break;
    case NON_ADMIN_ORC_10:
      break;
    case NON_ADMIN_ORC_11:
      break;
    case NON_ADMIN_ORC_12:
      break;
    case NON_ADMIN_ORC_13:
      break;
    case NON_ADMIN_ORC_14:
      break;
    case NON_ADMIN_ORC_15:
      break;
    case NON_ADMIN_ORC_16:
      break;
    case NON_ADMIN_ORC_17:
      break;
    case NON_ADMIN_ORC_18:
      break;
    case NON_ADMIN_ORC_19:
      break;
    case NON_ADMIN_ORC_2:
      break;
    case NON_ADMIN_ORC_20:
      break;
    case NON_ADMIN_ORC_21:
      break;
    case NON_ADMIN_ORC_22:
      break;
    case NON_ADMIN_ORC_23:
      break;
    case NON_ADMIN_ORC_24:
      break;
    case NON_ADMIN_ORC_25:
      break;
    case NON_ADMIN_ORC_26:
      break;
    case NON_ADMIN_ORC_27:
      break;
    case NON_ADMIN_ORC_28:
      break;
    case NON_ADMIN_ORC_29:
      break;
    case NON_ADMIN_ORC_3:
      break;
    case NON_ADMIN_ORC_30:
      break;
    case NON_ADMIN_ORC_31:
      break;
    case NON_ADMIN_ORC_4:
      break;
    case NON_ADMIN_ORC_5:
      break;
    case NON_ADMIN_ORC_6:
      break;
    case NON_ADMIN_ORC_7:
      break;
    case NON_ADMIN_ORC_8:
      break;
    case NON_ADMIN_ORC_9:
      break;
    case PID_11_BIRTH:
      break;
    case PID_11_HOME:
      break;
    case REFUSAL_ORC:
      break;
    case REFUSAL_ORC_1:
      break;
    case REFUSAL_ORC_10:
      break;
    case REFUSAL_ORC_11:
      break;
    case REFUSAL_ORC_12:
      break;
    case REFUSAL_ORC_13:
      break;
    case REFUSAL_ORC_14:
      break;
    case REFUSAL_ORC_15:
      break;
    case REFUSAL_ORC_16:
      break;
    case REFUSAL_ORC_17:
      break;
    case REFUSAL_ORC_18:
      break;
    case REFUSAL_ORC_19:
      break;
    case REFUSAL_ORC_2:
      break;
    case REFUSAL_ORC_20:
      break;
    case REFUSAL_ORC_21:
      break;
    case REFUSAL_ORC_22:
      break;
    case REFUSAL_ORC_23:
      break;
    case REFUSAL_ORC_24:
      break;
    case REFUSAL_ORC_25:
      break;
    case REFUSAL_ORC_26:
      break;
    case REFUSAL_ORC_27:
      break;
    case REFUSAL_ORC_28:
      break;
    case REFUSAL_ORC_29:
      break;
    case REFUSAL_ORC_3:
      break;
    case REFUSAL_ORC_30:
      break;
    case REFUSAL_ORC_31:
      break;
    case REFUSAL_ORC_4:
      break;
    case REFUSAL_ORC_5:
      break;
    case REFUSAL_ORC_6:
      break;
    case REFUSAL_ORC_7:
      break;
    case REFUSAL_ORC_8:
      break;
    case REFUSAL_ORC_9:
      break;
    case ADMIN_OBX_30945_0:
      break;
    case ADMIN_OBX_30963_3:
      break;
    case ADMIN_OBX_31044_1:
      break;
    case ADMIN_OBX_59784_9:
      break;
    case ADMIN_OBX_59785_6:
      break;
    case ADMIN_RXA_9_FREE:
      break;
    case NK1_5_FAX:
      break;
    case PID_13_FAX:
      break;
    }

    return testCaseMessage;
  }

  public static List<ProfileLine> readProfileLines(File profileFile) throws FileNotFoundException, IOException {
    List<ProfileLine> profileLineList;
    profileLineList = new ArrayList<ProfileLine>();
    BufferedReader in = new BufferedReader(new FileReader(profileFile));
    String line = in.readLine();
    List<String> valueList = CvsReader.readValuesFromCsv(line);
    int posField = findPosition("Field", valueList);
    int posUsage = findPosition("Usage", valueList);
    while ((line = in.readLine()) != null) {
      valueList = CvsReader.readValuesFromCsv(line);
      String fieldName = CvsReader.readValue(posField, valueList);
      fieldName = fieldName.toUpperCase();
      fieldName = fieldName.replace(" ", "_");
      fieldName = fieldName.replace(".", "_");
      fieldName = fieldName.replace("-", "_");
      ProfileField field = null;
      try {
        field = ProfileField.valueOf(fieldName);
      } catch (IllegalArgumentException iae) {
        // skip this one then, it's not recognized
      }
      if (field != null) {
        ProfileLine profileLine = new ProfileLine();
        profileLine.setField(field);
        profileLine.setUsage(CvsReader.readValue(posUsage, valueList));
        profileLineList.add(profileLine);
      }
    }
    in.close();
    return profileLineList;
  }

  public static void updateMessageAcceptStatus(List<ProfileLine> profileLineList) {
    MessageAcceptStatus masSegment = MessageAcceptStatus.ONLY_IF_PRESENT;
    MessageAcceptStatus masField = null;
    MessageAcceptStatus masFieldPart = null;
    for (ProfileLine profileLine : profileLineList) {
      ProfileField field = profileLine.getField();
      if (field.getType() == ProfileFieldType.SEGMENT) {
        masSegment = determineMessageAcceptStatus(profileLine, MessageAcceptStatus.ONLY_IF_PRESENT);
        profileLine.setMessageAcceptStatus(masSegment);
      } else if (field.getType() == ProfileFieldType.FIELD) {
        masField = determineMessageAcceptStatus(profileLine, masSegment);
        profileLine.setMessageAcceptStatus(masField);
      } else if (field.getType() == ProfileFieldType.FIELD_PART) {
        masFieldPart = determineMessageAcceptStatus(profileLine, masField);
        profileLine.setMessageAcceptStatus(masFieldPart);
      } else if (field.getType() == ProfileFieldType.FIELD_SUB_PART) {
        profileLine.setMessageAcceptStatus(determineMessageAcceptStatus(profileLine, masFieldPart));
      }
    }
  }

  public static MessageAcceptStatus determineMessageAcceptStatus(ProfileLine profileLine, MessageAcceptStatus masHigher) {
    MessageAcceptStatus mas = null;
    if (masHigher == MessageAcceptStatus.ONLY_IF_ABSENT || profileLine.getUsage() == Usage.X) {
      mas = MessageAcceptStatus.ONLY_IF_ABSENT;
    } else if (masHigher == MessageAcceptStatus.ONLY_IF_PRESENT && profileLine.getUsage() == Usage.R) {
      mas = MessageAcceptStatus.ONLY_IF_PRESENT;
    } else {
      mas = MessageAcceptStatus.IF_PRESENT_OR_ABSENT;
    }
    return mas;
  }

  private static int findPosition(String headerName, List<String> valueList) {
    int pos = 0;
    for (String value : valueList) {
      if (value.equalsIgnoreCase(headerName)) {
        return pos;
      }
      pos++;
    }
    return -1;
  }

}
