package org.immunizationsoftware.dqa.tester.manager.hl7;

import java.util.List;

import junit.framework.TestCase;

import org.immunizationsoftware.dqa.tester.manager.hl7.messages.ACK;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.RSP;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.VXU;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.MSH;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PID;

public class ConformanceTest extends TestCase
{
  private static final String MSH_SEGMENT = "MSH|^~\\&|||||20130823111809||VXU^V04^VXU_V04|X94P18|P|2.5.1|";
  private static final String MSH_SEGMENT_BAD_DATE = "MSH|^~\\&|||||201308231118AA||VXU^V04^VXU_V04|X94P18|P|2.5.1|";
  private static final String PID_SEGMENT = "PID|1||X94P18^^^OIS-TEST^MR||Court^Nye^^^^^L|Brazos^Maia|20130224|M|||321 Dundy St^^Haslett^MI^48840^USA^P||^PRN^PH^^^517^5489090|\r";
  private static final String VXU_MESSAGE_SHORT = "MSH|^~\\&|||||20130823111809||VXU^V04^VXU_V04|X94P18|P|2.5.1|\r"
      + "PID|1||X94P18^^^OIS-TEST^MR||Court^Nye^^^^^L|Brazos^Maia|20130224|M|||321 Dundy St^^Haslett^MI^48840^USA^P||^PRN^PH^^^517^5489090|\r"
      + "NK1|1|Court^Maia|MTH^Mother^HL70063|\r";

  private static final String VXU_MESSAGE_1 = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
      + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
      + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
      + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
      + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
      + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
      + "RXR|IM^Intramuscular^HL70162|LD^Left Arm^HL70163\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";

  private static final String VXU_MESSAGE_1R = "MSH|^~\\&|||||20130906141610||VXU^V04^VXU_V04|[MRN].1378498569902|P|2.5.1|\r"
      + "PID|1||P91L1^^^OIS-TEST^MR||Comal^Maslin^^^^^L|Cumberland^Majesta|20130307|M|||297 Saint Joseph Ln^^Lake Orion^MI^48360^USA^P||^PRN^PH^^^248^3700547|\r"
      + "NK1|1|Templeman^Majesta|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||P91L1.1^OIS|\r"
      + "RXA|0|1|20130507||133^PCV 13^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||P91L1.2^OIS|\r"
      + "RXA|0|1|20130705||133^PCV 13^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||P91L1.3^OIS|\r"
      + "RXA|0|1|20130906||133^PCV 13^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Q8846RW||WAL^Wyeth^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20130906|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|133^PCV13^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20100416||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20130906||||||F|\r";

  private static final String ACK_MESSAGE = "MSH|^~\\&|MCIR|MCIR|||20130907054645-0600||ACK^V04^ACK_V04|20130907054645-0600.10|P|2.5.1|\r"
      + "SFT|OIS|1.06dev|DQA||\r"
      + "MSA|AA|A1.1.1378554404763|\r"
      + "ERR|||0|I|||||Message accepted with 1 vaccination|\r";

  private static final String RSP_MESSAGE = "MSH|^~\\&|MCIR|MCIR|||20130907061310-0600||RSP^K11^RSP_K11|20130907061310-0600.723|P|2.5.1|||NE|AL|||||Z32^CDCPHINVS|\r"
      + "MSA|AA|1378555990724.1|\r"
      + "QAK|1378555990724.1|OK|Z34|\r"
      + "QPD|Z34|1378555990724.1|A1.1^^^OIS-TEST^MR|Wheeler^Vera^Yoselin^^^^L|Farrell|20090911|F|97 Linn St^^Sterling Heights^MI^48314^USA^P^^|^PRN^PH^^^586^3288993^|||\r"
      + "PID|||A1.1^^^OIS-TEST^MR||Wheeler^Vera^Yoselin^^^^L|Farrell|20090911|F||2106-3^^HL70005|97 Linn St^^Sterling Heights^MI^48314^USA^P^^||^^^^^586^3288993^|||||||||2186-5^^CDCREC||||\r"
      + "NK1|1|Wheeler^Chanel^^^^^|MTH^^HL70063|^^^^^^^^||\r"
      + "ORC|RE|F74W1.3||\r"
      + "RXA|0|1|20130907||141^^CVX|0.25|mL^^UCUM||00^^NIP001||||||Z1364WM||SKB^^MVX|\r"
      + "RXR|IM^^HL70162|RA^^HL70163|\r" + "OBX|1|CE|64994-7^^LN||V04^^HL70064|||||F|||||||\r";

  private static final String RSP_MESSAGE_2 = "MSH|^~\\&|MCIR|MCIR|||20130910114900-0600||RSP^K11^RSP_K11|20130910114900-0600.8|P|2.5.1|||NE|AL|||||Z32^CDCPHINVS|\r"
      + "MSA|AA|1378835340114.8|\r"
      + "QAK|1378835340114.8|OK|Z34|\r"
      + "QPD|Z34|1378835340114.8|A1.1^^^OIS-TEST^MR|Fillmore^Robin^Thanos^^^^L|O'Brian|20090911|M|309 Baylor Cir^^Pullman^MI^49450^USA^P^^|^PRN^PH^^^269^2832952^|||\r"
      + "PID|||A1.1^^^OIS-TEST^MR||Fillmore^Robin^Thanos^^^^L|O'Brian|20090911|M||2106-3^^HL70005|309 Baylor Cir^^Pullman^MI^49450^USA^P^^||^^^^^269^2832952^|||||||||2186-5^^CDCREC||||\r"
      + "NK1|1|O'Brian^Dep^^^^^|MTH^^HL70063|^^^^^^^^||\r"
      + "ORC|RE|U93P8.3||\r"
      + "RXA|0|1|20130910||^^CVX|0.25|mL^^UCUM||00^^NIP001||||||N2685LR||NOV^^MVX|\r"
      + "RXR|IM^^HL70162|LA^^HL70163|\r" + "OBX|1|CE|64994-7^^LN||V02^^HL70064|||||F|||||||\r";

  public void testConformance() {
    PID pid = new PID(UsageType.R, Cardinality.ONE_TIME_ONLY);
    pid.parseTextFromSegment(PID_SEGMENT);

    {
      MSH msh = new MSH(UsageType.R, Cardinality.ONE_TIME_ONLY);
      msh.parseTextFromSegment(MSH_SEGMENT);
      assertEquals("20130823111809", msh.getDateTimeOfMessage().getRawTextReceived());

      System.out.println(msh.createSegment());
      List<ConformanceIssue> conformanceIssueList = msh.checkConformance();
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
      System.out.println("Conformance of MSH");
      // msh.printValues(System.out);
    }
    {

      System.out.println("Testing VXU");
      VXU vxu = new VXU();
      vxu.parseTextFromMessage(VXU_MESSAGE_SHORT);

      List<ConformanceIssue> conformanceIssueList = vxu.checkConformance();
      System.out.println(vxu.createMessage());
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
    }

    {

      MSH msh = new MSH(UsageType.R, Cardinality.ONE_TIME_ONLY);
      msh.parseTextFromSegment(MSH_SEGMENT_BAD_DATE);
      assertEquals("201308231118AA", msh.getDateTimeOfMessage().getRawTextReceived());
      System.out.println(msh.createSegment());
      List<ConformanceIssue> conformanceIssueList = msh.checkConformance();
      assertEquals("", msh.getDateTimeOfMessage().getTime().getValue());
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
    }
    {

      System.out.println("Testing VXU from NIST Scenario #1");
      VXU vxu = new VXU();
      vxu.parseTextFromMessage(VXU_MESSAGE_1);

      List<ConformanceIssue> conformanceIssueList = vxu.checkConformance();
      System.out.println(vxu.createMessage());
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
      assertEquals(VXU_MESSAGE_1, vxu.createMessage());
      // vxu.printValues(System.out);
    }

    {

      System.out.println("Testing VXU from NIST Scenario #1 (replica)");
      VXU vxu = new VXU();
      vxu.parseTextFromMessage(VXU_MESSAGE_1R);

      List<ConformanceIssue> conformanceIssueList = vxu.checkConformance();
      System.out.println(vxu.createMessage());
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
      assertNull(vxu.getNextComponent());
      // assertEquals(VXU_MESSAGE_1R, vxu.createMessage());
      // vxu.printValues(System.out);
    }

    {
      System.out.println();
      System.out.println("Testing ACK");
      ACK ack = new ACK();
      ack.parseTextFromMessage(ACK_MESSAGE);

      List<ConformanceIssue> conformanceIssueList = ack.checkConformance();
      System.out.println(ack.createMessage());
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
    }
    {
      System.out.println();
      System.out.println("Testing RSP");
      RSP rsp = new RSP();
      rsp.parseTextFromMessage(RSP_MESSAGE);

      List<ConformanceIssue> conformanceIssueList = rsp.checkConformance();
      System.out.println(rsp.createMessage());
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
    }

    {
      System.out.println();
      System.out.println("Testing RSP 2");
      HL7Component rsp = new RSP().makeAnother();
      rsp.setUsageType(UsageType.R);
      rsp.setCardinalityCount(1);
      rsp.parseTextFromMessage(RSP_MESSAGE_2);

      List<ConformanceIssue> conformanceIssueList = rsp.checkConformance();
      System.out.println(rsp.createMessage());
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
      assertTrue(hasError(conformanceIssueList, "Administered Code - Identifier (RXA-5.1) is required but was found empty"));
      // rsp.printValues(System.out);

    }
  }
  
  private static boolean  hasError(List<ConformanceIssue> conformanceIssueList, String text)
  {
    for (ConformanceIssue conformanceIssue : conformanceIssueList) {
      if (conformanceIssue.getUserMessage().getValue().equals(text) && conformanceIssue.getSeverity().getValue().equals("E"))
      {
        return true;
      }
    }
    return false;
  }

}
