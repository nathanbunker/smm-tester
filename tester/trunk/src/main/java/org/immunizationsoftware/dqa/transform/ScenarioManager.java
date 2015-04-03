package org.immunizationsoftware.dqa.transform;

public class ScenarioManager
{
  public static final String SCENARIO_BLANK = "Blank";
  public static final String SCENARIO_1_1_ADMIN_CHILD = "NIST MU2.1.1 - Admin Child";
  public static final String SCENARIO_1_R_ADMIN_CHILD = "NIST MU2.1.r - Admin Child (replica)";
  public static final String SCENARIO_1_R_ADMIN_TWO_MONTHS_OLD = "NIST MU2.1.r - Admin Child (replica) - 2 Months Old";
  public static final String SCENARIO_1_R_ADMIN_TWO_YEARS_OLD = "NIST MU2.1.r - Admin Child (replica) - 2 Years Old";
  public static final String SCENARIO_1_R_ADMIN_FOUR_YEARS_OLD = "NIST MU2.1.r - Admin Child (replica) - 4 Years Old";
  public static final String SCENARIO_1_R_ADMIN_TWELVE_YEARS_OLD = "NIST MU2.1.r - Admin Child (replica) - 12 Years Old";
  public static final String SCENARIO_2_1_ADMIN_ADULT = "NIST MU2.2.1 - Admin Adult";
  public static final String SCENARIO_2_R_ADMIN_ADULT = "NIST MU2.2.r - Admin Adult (replica)";
  public static final String SCENARIO_3_1_HISTORICAL_CHILD = "NIST MU2.3.1 - Historical Child";
  public static final String SCENARIO_3_R_HISTORICAL_CHILD = "NIST MU2.3.r - Historical Child (replica)";
  public static final String SCENARIO_3_R_HISTORICAL_TWO_MONTHS_OLD = "NIST MU2.3.r - Historical Child (replica) - 2 Months Old";
  public static final String SCENARIO_3_R_HISTORICAL_TWO_YEARS_OLD = "NIST MU2.3.r - Historical Child (replica) - 2 Years Old";
  public static final String SCENARIO_3_R_HISTORICAL_FOUR_YEARS_OLD = "NIST MU2.3.r - Historical Child (replica) - 4 Years Old";
  public static final String SCENARIO_3_R_HISTORICAL_TWELVE_YEARS_OLD = "NIST MU2.3.r - Historical Child (replica) - 12 Years Old";
  public static final String SCENARIO_4_1_CONSENTED_CHILD = "NIST MU2.4.1 - Consented Child";
  public static final String SCENARIO_4_R_CONSENTED_CHILD = "NIST MU2.4.r - Consented Child (replica)";
  public static final String SCENARIO_5_1_REFUSED_TODDLER = "NIST MU2.5.1 - Refused Toddler";
  public static final String SCENARIO_5_R_REFUSED_TODDLER = "NIST MU2.5.r - Refused Toddler (replica)";
  public static final String SCENARIO_5_P_REFUSED_TODDLER = "NIST MU2.5.p - Refused Toddler (replica plus immunization)";
  public static final String SCENARIO_6_1_VARICELLA_HISTORY_CHILD = "NIST MU2.6.1 - Varicella History Child";
  public static final String SCENARIO_6_R_VARICELLA_HISTORY_CHILD = "NIST MU2.6.r - Varicella History Child (replica)";
  public static final String SCENARIO_6_P_VARICELLA_HISTORY_CHILD = "NIST MU2.6.p - Varicella History Child (replica plus immunization)";
  public static final String SCENARIO_7_1_COMPLETE_RECORD = "NIST MU2.7.1 - Complete Record";
  public static final String SCENARIO_7_R_COMPLETE_RECORD = "NIST MU2.7.r - Complete Record (replica)";
  public static final String SCENARIO_FULL_RECORD_FOR_PROFILING = "Full Record for Profiling";
  public static final String SCENARIO_ADD_DELETE = "Add and then Delete - Admin Child";
  public static final String SCENARIO_MCIR_MPI = "MCIR MPI Test";

  public static final String[] SCENARIOS = { SCENARIO_BLANK, SCENARIO_1_1_ADMIN_CHILD, SCENARIO_1_R_ADMIN_CHILD,
      SCENARIO_1_R_ADMIN_TWO_MONTHS_OLD, SCENARIO_1_R_ADMIN_TWO_YEARS_OLD, SCENARIO_1_R_ADMIN_FOUR_YEARS_OLD,
      SCENARIO_1_R_ADMIN_TWELVE_YEARS_OLD, SCENARIO_2_1_ADMIN_ADULT, SCENARIO_2_R_ADMIN_ADULT,
      SCENARIO_3_1_HISTORICAL_CHILD, SCENARIO_3_R_HISTORICAL_CHILD, SCENARIO_3_R_HISTORICAL_TWO_MONTHS_OLD,
      SCENARIO_3_R_HISTORICAL_TWO_YEARS_OLD, SCENARIO_3_R_HISTORICAL_FOUR_YEARS_OLD,
      SCENARIO_3_R_HISTORICAL_TWELVE_YEARS_OLD, SCENARIO_4_1_CONSENTED_CHILD, SCENARIO_4_R_CONSENTED_CHILD,
      SCENARIO_5_1_REFUSED_TODDLER, SCENARIO_5_R_REFUSED_TODDLER, SCENARIO_5_P_REFUSED_TODDLER,
      SCENARIO_6_1_VARICELLA_HISTORY_CHILD, SCENARIO_6_R_VARICELLA_HISTORY_CHILD, SCENARIO_6_P_VARICELLA_HISTORY_CHILD,
      SCENARIO_7_1_COMPLETE_RECORD, SCENARIO_7_R_COMPLETE_RECORD, SCENARIO_FULL_RECORD_FOR_PROFILING,
      SCENARIO_ADD_DELETE, SCENARIO_MCIR_MPI };

  public static TestCaseMessage createTestCaseMessage(String scenario) {
    TestCaseMessage testCaseMessage;
    testCaseMessage = new TestCaseMessage();
    testCaseMessage.setScenario(scenario);
    String originalMessage = "";
    String customTransformations = "";
    String description = "";
    String expectedResult = "";
    String assertResultText = "";
    String assertResultStatus = "";
    String testCaseSet = "";
    String[] quickTransformations = {};
    PatientType patientType = PatientType.ANY_CHILD;

    if (scenario.equals(SCENARIO_BLANK)) {
      description = "Administered for Child";
      originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE",
          "ETHNICITY", "VAC1_ADMIN" };
      patientType = PatientType.TODDLER;
    } else if (scenario.equals(SCENARIO_1_R_ADMIN_CHILD)) {
      description = "NIST IZ #1: Administered for Child (replica)";
      originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE",
          "ETHNICITY", "VAC1_ADMIN" };
      patientType = PatientType.TODDLER;
    } else if (scenario.equals(SCENARIO_1_R_ADMIN_TWO_MONTHS_OLD)) {
      description = "NIST IZ #1: Administered for Child (replica) 2 months old";
      originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE",
          "ETHNICITY", "VAC1_ADMIN" };
      patientType = PatientType.TWO_MONTHS_OLD;
    } else if (scenario.equals(SCENARIO_1_R_ADMIN_TWO_YEARS_OLD)) {
      description = "NIST IZ #1: Administered for Child (replica) 2 years old";
      originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE",
          "ETHNICITY", "VAC1_ADMIN" };
      patientType = PatientType.TWO_YEARS_OLD;
    } else if (scenario.equals(SCENARIO_1_R_ADMIN_FOUR_YEARS_OLD)) {
      description = "NIST IZ #1: Administered for Child (replica) 4 years old";
      originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE",
          "ETHNICITY", "VAC1_ADMIN" };
      patientType = PatientType.FOUR_YEARS_OLD;
    } else if (scenario.equals(SCENARIO_1_R_ADMIN_TWELVE_YEARS_OLD)) {
      description = "NIST IZ #1: Administered for Child (replica) 12 years old";
      originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE",
          "ETHNICITY", "VAC1_ADMIN" };
      patientType = PatientType.TWELVE_YEARS_OLD;
    } else if (scenario.equals(SCENARIO_ADD_DELETE)) {
      description = "Add and then Delete: Administered for Child";
      originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\nORC|\nRXA|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE",
          "ETHNICITY", "VAC1_ADMIN", "VAC1_DELETE" };
      patientType = PatientType.TODDLER;
    } else if (scenario.equals(SCENARIO_2_R_ADMIN_ADULT)) {
      description = "NIST IZ #2: Administered for Adult (replica)";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_ADMIN" };
      patientType = PatientType.ADULT;
    } else if (scenario.equals(SCENARIO_3_R_HISTORICAL_CHILD)) {
      description = "NIST IZ #3: Historical for Child (replica)";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_HIST" };
      patientType = PatientType.TWEEN;
    } else if (scenario.equals(SCENARIO_3_R_HISTORICAL_TWO_YEARS_OLD)) {
      description = "NIST IZ #3: Historical for Child (replica) 2 years old";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_HIST" };
      patientType = PatientType.TWO_YEARS_OLD;
    } else if (scenario.equals(SCENARIO_3_R_HISTORICAL_TWO_MONTHS_OLD)) {
      description = "NIST IZ #3: Historical for Child (replica) 2 months old";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_HIST" };
      patientType = PatientType.TWO_MONTHS_OLD;
    } else if (scenario.equals(SCENARIO_3_R_HISTORICAL_FOUR_YEARS_OLD)) {
      description = "NIST IZ #3: Historical for Child (replica) 4 years old";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_HIST" };
      patientType = PatientType.FOUR_YEARS_OLD;
    } else if (scenario.equals(SCENARIO_3_R_HISTORICAL_TWELVE_YEARS_OLD)) {
      description = "NIST IZ #3: Historical for Child (replica) 12 years old";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_HIST" };
      patientType = PatientType.TWELVE_YEARS_OLD;
    } else if (scenario.equals(SCENARIO_4_R_CONSENTED_CHILD)) {
      description = "NIST IZ #4: Consented for Child (replica)";
      originalMessage = "MSH|\nPID|\nPD1|\nORC|\nRXA|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_HIST" };
      patientType = PatientType.TWEEN;
      customTransformations = "PD1-12=N\n";
    } else if (scenario.equals(SCENARIO_5_R_REFUSED_TODDLER)) {
      description = "NIST IZ #5: Refused for Toddler (replica)";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\n";
      patientType = PatientType.TODDLER;
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_NA" };
      customTransformations = "RXA-18.1=00\n" + "RXA-18.2=Parental Decision\n" + "RXA-18.3=NIP002\n" + "RXA-20=RE\n";
    } else if (scenario.equals(SCENARIO_5_P_REFUSED_TODDLER)) {
      description = "NIST IZ #5: Refused for Toddler (replica plus immunization)";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\nORC|\nRXA|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      patientType = PatientType.TODDLER;
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_NA", "VAC2_ADMIN" };
      customTransformations = "RXA-18.1=00\n" + "RXA-18.2=Parental Decision\n" + "RXA-18.3=NIP002\n" + "RXA-20=RE\n";
    } else if (scenario.equals(SCENARIO_6_R_VARICELLA_HISTORY_CHILD)) {
      description = "NIST IZ #6: Varicella History for Child (replica)";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_NA" };
      customTransformations = "RXA-5.1=998\n" + "RXA-5.2=No vaccine administered\n" + "RXA-5.1=998\n" + "OBX-1=1\n"
          + "OBX-2=CE\n" + "OBX-3.1=59784-9\n" + "OBX-3.2=Disease with presumed immunity\n" + "OBX-3.3=LN\n"
          + "OBX-4=1\n" + "OBX-5.1=38907003\n" + "OBX-5.2=Varicella infection\n" + "OBX-5.3=SCT\n" + "OBX-11=F\n";
      patientType = PatientType.TWEEN;
    } else if (scenario.equals(SCENARIO_6_P_VARICELLA_HISTORY_CHILD)) {
      description = "NIST IZ #6: Varicella History for Child (replica plus immunization)";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\nOBX|\nORC|\nRXA|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_NA", "VAC2_ADMIN" };
      customTransformations = "RXA-5.1=998\n" + "RXA-5.2=No vaccine administered\n" + "RXA-5.1=998\n" + "OBX-1=1\n"
          + "OBX-2=CE\n" + "OBX-3.1=59784-9\n" + "OBX-3.2=Disease with presumed immunity\n" + "OBX-3.3=LN\n"
          + "OBX-4=1\n" + "OBX-5.1=38907003\n" + "OBX-5.2=Varicella infection\n" + "OBX-5.3=SCT\n" + "OBX-11=F\n";
      patientType = PatientType.TWEEN;
    } else if (scenario.equals(SCENARIO_7_R_COMPLETE_RECORD)) {
      description = "NIST IZ #7: Complete Record (replica)";
      originalMessage = "MSH|\nPID|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\nORC|\nRXA|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY",
          "VAC1_ADMIN", "VAC2_HIST", "VAC3_ADMIN" };
      patientType = PatientType.TODDLER;
    } else if (scenario.equals(SCENARIO_FULL_RECORD_FOR_PROFILING)) {
      description = "Full Record for Profiling";
      originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nPV1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\nORC|\nRXA|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE",
          "ETHNICITY", "VAC1_ADMIN", "VAC2_HIST", "VAC3_ADMIN" };
      patientType = PatientType.TODDLER;
      customTransformations = "MSH-15=NE\nMSH-16=AL\nMSH-22=[RESPONSIBLE_ORG_NAME]\n"
          + "PID-11.2=[STREET2]\n"
          + "PID-15=[LANGUAGE]\nPID-15.2=[LANGUAGE_LABEL]\nPID-15.3=HL70296\nPID-13.2#2=NET\nPID-13.4#2=[EMAIL]\n"
          + "PID-14.2=WPN\nPID-14.3=PH\nPID-14.6=[PHONE_ALT_AREA]\nPID-14.7=[PHONE_ALT_LOCAL]\nPID-24=N\nPID-25=1\nPID-30=N\n"
          + "PD1-11.1=02\nPD1-11.2=Reminder/recall - any method\nPD1-11.3=HL70215\nPD1-12=N\nPD1-13=[TODAY]\nPD1-16=A\nPD1-17=[TODAY]\nPD1-18=[TODAY]\n"
          + "NK1-2.3=[GIRL_MIDDLE]\nNK1-2.7=L\n" 
          + "NK1-4.1=[STREET]\nNK1-4.2=[STREET2]\nNK1-4.3=[CITY]\nNK1-4.4=[STATE]\nNK1-4.5=[ZIP]\nNK1-4.6=USA\nNK1-4.7=P\n"
          + "NK1-5.2=PRN\nNK1-5.3=PH\nNK1-5.6=[PHONE_AREA]\nNK1-5.7=[PHONE_LOCAL]\n"
          + "NK1-6.2=WPN\nNK1-6.3=PH\nNK1-6.6=[PHONE_ALT_AREA]\nNK1-6.7=[PHONE_ALT_LOCAL]\n"
          + "ORC-10.1=[ENTERED_BY_NPI]\nORC-10.2=[ENTERED_BY_LAST]\nORC-10.3=[ENTERED_BY_FIRST]\nORC-10.4=[ENTERED_BY_MIDDLE]\nORC-10.9=CNS\nORC-10.10=L\nORC-10.13=NPI\n"
          + "ORC-12.1=[ORDERED_BY_NPI]\nORC-12.2=[ORDERED_BY_LAST]\nORC-12.3=[ORDERED_BY_FIRST]\nORC-12.4=[ORDERED_BY_MIDDLE]\nORC-12.9=CMS\nORC-10.10=L\nORC-12.13=NPI\n"
          + "ORC-17.1=[ADMIN_ORG_1_ID]\nORC-17.2=[ADMIN_ORG_1_NAME]\nORC-17.3=HL70362\n"
          + "RXA-4=[VAC1_DATE]\n"
          + "RXA-10.1=[ADMIN_BY_NPI]\nRXA-10.2=[ADMIN_BY_LAST]\nRXA-10.3=[ADMIN_BY_FIRST]\nRXA-10.4=[ADMIN_BY_MIDDLE]\nRXA-10.9=CMS\nRXA-10.10=L\nRXA-10.13=NPI\n"
          + "RXA-11.4=[ADMIN_ORG_1_ID]\n"
          + "RXA-16=[FUTURE]\nRXA#3-16=[FUTURE]\nRXA*-20=CP\nRXA-22=[NOW]\n"
          + "RXA#2-4=[VAC2_DATE]\n"
          + "ORC#3-10.1=[ENTERED_BY_NPI]\nORC#3-10.2=[ENTERED_BY_LAST]\nORC#3-10.3=[ENTERED_BY_FIRST]\nORC#3-10.4=[ENTERED_BY_MIDDLE]\nORC#3-10.9=CMS\nORC#3-10.10=L\nORC#3-10.13=NPI\n"
          + "ORC#3-12.1=[ORDERED_BY_NPI]\nORC#3-12.2=[ORDERED_BY_LAST]\nORC#3-12.3=[ORDERED_BY_FIRST]\nORC#3-12.4=[ORDERED_BY_MIDDLE]\nORC#3-12.9=CMS\nORC#3-10.10=L\nORC#3-12.13=NPI\n"
          + "ORC#3-17.1=[ADMIN_ORG_1_ID]\nORC#3-17.2=[ADMIN_ORG_1_NAME]\nORC#3-17.3=HL70362\n";

    } else if (scenario.equals(SCENARIO_1_1_ADMIN_CHILD)) {
      description = "NIST IZ #1.1: Administered for Child";
      originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\n"
          + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\n"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\n"
          + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\n"
          + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\n"
          + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\n"
          + "RXR|IM^Intramuscular^HL70162|LA^Left Arm^HL70163\n"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\n"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\n"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\n"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\n";
      quickTransformations = new String[] {};
      patientType = PatientType.TODDLER;
    } else if (scenario.equals(SCENARIO_2_1_ADMIN_ADULT)) {
      description = "NIST IZ #2.1: Administered for Adult";
      originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-004.00|P|2.5.1|||AL|ER\n"
          + "PID|1||MR-76732^^^NIST MPI^MR~184-36-9200^^^MAA^SS||Vally^Nitika^^^^^L||19410813|F|||||^NET^^nvally@fastmail.com\n"
          + "ORC|RE||IZ-783275^NDA|||||||||57422^RADON^NICHOLAS^^^^^^NDA^L\n"
          + "RXA|0|1|20120814||52^Hep A^CVX|1|mL^milliliters^UCUM||00^New immunization record^NIP001||^^^F28||||I90FV|20121214|MSD^Merck and Co^MVX|||CP|A\n"
          + "RXR|IM^Intramuscular^HL70162|RA^Right Arm^HL70163\n"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20120814|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\n"
          + "OBX|2|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F\n"
          + "OBX|3|CE|30956-7^vaccine type^LN|2|85^Hepatitis A^CVX||||||F\n"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\n";
      quickTransformations = new String[] {};
      patientType = PatientType.ADULT;
    } else if (scenario.equals(SCENARIO_3_R_HISTORICAL_CHILD)) {
      description = "NIST IZ #3.1: Historical for Child";
      originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-007.00|P|2.5.1|||AL|ER\n"
          + "PID|1||MR-99922^^^NIST MPI^MR||Montgomery^Lewis^^^^^L||20010821|M\n"
          + "ORC|RE||IZ-783276^NDA\n"
          + "RXA|0|1|20110215||62^HPV^CVX|999|||01^Historical information - source unspecified^NIP001\n";
      quickTransformations = new String[] {};
      patientType = PatientType.TWEEN;
    } else if (scenario.equals(SCENARIO_4_R_CONSENTED_CHILD)) {
      description = "NIST IZ #4.1: Consented for Child";
      originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-010.00|P|2.5.1|||AL|ER\n"
          + "PID|1||X-6523^^^NIST MPI^MR||Newton^Lea^^^^^L||20010905|F\n"
          + "PD1||||||||||||N|20120701\n"
          + "ORC|RE||IZ-783277^NDA\n"
          + "RXA|0|1|20110214||115^Tdap^CVX|999|||01^Historical information - source unspecified^NIP001\n";
      quickTransformations = new String[] {};
      patientType = PatientType.TWEEN;
    } else if (scenario.equals(SCENARIO_5_1_REFUSED_TODDLER)) {
      description = "NIST IZ #5.1: Refused for Toddler";
      originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-013.00|P|2.5.1|||AL|ER\n"
          + "PID|1||MR-67323^^^NIST MPI^MR||Fleming^Chad^^^^^L||20100830|M\n"
          + "ORC|RE||9999^CDC\n"
          + "RXA|0|1|20120815||03^MMR^CVX|999||||||||||||00^Parental Refusal^NIP002||RE\n";
      patientType = PatientType.TODDLER;
      quickTransformations = new String[] {};
    } else if (scenario.equals(SCENARIO_6_R_VARICELLA_HISTORY_CHILD)) {
      description = "NIST IZ #6.1: Varicella History for Child";
      originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-016.00|P|2.5.1|||AL|ER\n"
          + "PID|1||MR-11891^^^NIST MPI^MR||Wolfe^Aron^^^^^L||20010907|M\n"
          + "ORC|RE||9999^CDC\n"
          + "RXA|0|1|20110215||998^No vaccine administered^CVX|999||||||||||||||NA\n"
          + "OBX|1|CE|59784-9^Disease with presumed immunity^LN|1|38907003^Varicella infection^SCT||||||F\n";
      quickTransformations = new String[] {};
      patientType = PatientType.TWEEN;
    } else if (scenario.equals(SCENARIO_7_1_COMPLETE_RECORD)) {
      description = "NIST IZ #7.1: Complete Record";
      originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-019.00|P|2.5.1|||AL|ER\n"
          + "PID|1||Q-73221^^^NIST MPI^MR||Mercer^Jirra^Emmanuelle^^^^L||20100907|F\n"
          + "ORC|RE||IZ-783278^NDA|||||||||57422^RADON^NICHOLAS^^^^^^NDA^L\n"
          + "RXA|0|1|20120816||141^Influenza^CVX|0.25|mL^milliliters^UCUM||00^New immunization record^NIP001||||||K5094SC|20121216|SKB^GlaxoSmithKline^MVX|||CP|A\n"
          + "RXR|IM^Intramuscular^HL70162|RA^Right Arm^HL70163\n"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\n"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\n"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\n"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\n"
          + "ORC|RE||IZ-783281^NDA|||||||||57422^RADON^NICHOLAS^^^^^^NDA^L\n"
          + "RXA|0|1|20110216||10^IPV^CVX|999|||01^Historical information - source unspecified^NIP001\n"
          + "ORC|RE||IZ-783282^NDA|||||||||57422^RADON^NICHOLAS^^^^^^NDA^L\n"
          + "RXA|0|1|20120816||120^DTaP-Hib-IPV^CVX|0.5|mL^milliliters^UCUM||00^New immunization record^NIP001||||||568AHK11|20121216|PMC^sanofi pasteur^MVX|||CP|A\n"
          + "RXR|IM^Intramuscular^HL70162|RA^Right Arm^HL70163\n"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\n"
          + "OBX|2|CE|30956-7^vaccine type^LN|2|107^DTaP^CVX||||||F\n"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20070517||||||F\n"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120816||||||F\n"
          + "OBX|5|CE|30956-7^vaccine type^LN|3|89^Polio^CVX||||||F\n"
          + "OBX|6|TS|29768-9^Date vaccine information statement published^LN|3|20111108||||||F\n"
          + "OBX|7|TS|29769-7^Date vaccine information statement presented^LN|3|20120816||||||F\n"
          + "OBX|8|CE|30956-7^vaccine type^LN|4|17^Hib^CVX||||||F\n"
          + "OBX|9|TS|29768-9^Date vaccine information statement published^LN|4|20111108||||||F\n"
          + "OBX|10|TS|29769-7^Date vaccine information statement presented^LN|4|20120816||||||F\n";
      quickTransformations = new String[] {};
      patientType = PatientType.TODDLER;
    } else if (scenario.equals(SCENARIO_MCIR_MPI)) {
      description = "MCIR MPI Test";
      originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
      quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE",
          "ETHNICITY", "VAC1_ADMIN" };
      patientType = PatientType.TODDLER;
    }

    testCaseMessage.setTestCaseSet(testCaseSet);
    testCaseMessage.setAssertResultStatus(assertResultStatus);
    testCaseMessage.setAssertResultText(assertResultText);
    testCaseMessage.setCustomTransformations(customTransformations);
    testCaseMessage.setDescription(description);
    testCaseMessage.setExpectedResult(expectedResult);
    testCaseMessage.setOriginalMessage(originalMessage);
    testCaseMessage.setQuickTransformations(quickTransformations);
    testCaseMessage.setPatientType(patientType);
    return testCaseMessage;
  }
}
