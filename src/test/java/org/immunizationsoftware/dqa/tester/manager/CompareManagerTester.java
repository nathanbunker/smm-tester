package org.immunizationsoftware.dqa.tester.manager;

import static org.junit.Assert.*;

import org.junit.Test;

public class CompareManagerTester
{

  private static final String BASE_ACK = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF1 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|E||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF2 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AE|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF3 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AE|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|E||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF4 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF5 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1.022.Fq|\r"
      + "ERR|||10^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";
  private static final String SAME1 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String SAME2 = "MSH|^~\\&|||||2015125133132||ACK^V04^ACK|2TRF-5.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems fosdfage but this interface does not save or forward the data|\r";

  @Test
  public void test() {
    assertTrue(CompareManager.acksAppearToBeTheSame(BASE_ACK, BASE_ACK));
    assertTrue(CompareManager.acksAppearToBeTheSame(BASE_ACK, SAME1));
    assertTrue(CompareManager.acksAppearToBeTheSame(BASE_ACK, SAME2));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF1));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF2));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF3));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF4));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF5));
  }
  
  private static final String VXU = "MSH|^~\\&|||||20151217144717-0700||VXU^V04^VXU_V04|3uTl-A.01.01.1Xb|P|2.5.1|||||||||||\r" +
"PID|||3uTl-A.01.01^^^AIRA-TEST^MR||Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M||2131-1^Other Race^HL70005|356 Trimble Ln^^Tekonsha^MI^49092^USA^P||^PRN^PH^^^517^9212381|||||||||2186-5^not Hispanic or Latino^HL70005|\r" +
"PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20151217|20151217|\r" +
"NK1|1|Chambers^Elise^^^^^L|MTH^Mother^HL70063|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381|\r" +
"ORC|RE||J84D479.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r" +
"RXA|0|1|20151217||03^MMR^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||P3400YX||MSD^Merck and Co^MVX||||A|\r" +
"RXR|SC^^HL70162|LA^^HL70163|\r" +
"OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V03^VFC eligible - Uninsured^HL70064||||||F|||20151217|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r" +
"OBX|2|CE|30956-7^Vaccine Type^LN|2|03^MMR^CVX||||||F|\r" +
"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120420||||||F|\r" +
"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20151217||||||F|\r";

  private static final String RSP1 = "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20151217||RSP^K11^RSP_K11|1450391207965.3|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r" +
"MSA|AA|1450391207965.3||0||0^Message Accepted^HL70357\r" +
"QAK|1450391207965.3|OK|Z34\r" +
"QPD|Z34^Request Immunization History^HL70471|1450391207965.3|3uTl-A.01.01^^^AIRA-TEST^MR|Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381\r" +
"PID|1||7237424^^^ORA^SR~3uTl-A.01.01^^^ORA^MR||CHAMBERS^MAXIM^M^^^^L|JEFFERSON^ELISE|20111211|M||2131-1|356 TRIMBLE LN^^TEKONSHA^MI^49092^^H||^PRN^PH^^^517^9212381|||||||||2186-5||N|0\r" +
"PD1|||||||||||02|N||||A\r" +
"NK1|1|CHAMBERS^ELISE|MTH|356 TRIMBLE LN^^TEKONSHA^MI^49092^^P|^PRN^PH^^^517^9212381\r" +
"ORC|RE||134037533\r" +
"RXA|0|1|20151217|20151217|03^MMR^CVX|1.0|||00||^^^BUNKER CLINIC||||P3400YX||MSD|||CP\r" +
"RXR|SC|LA\r";
  
  private static final String RSP2 = "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20151217||RSP^K11^RSP_K11|1450391207965.3|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r" +
"MSA|AA|1450391207965.3||0||0^Message Accepted^HL70357\r" +
"QAK|1450391207965.3|OK|Z34\r" +
"QPD|Z34^Request Immunization History^HL70471|1450391207965.3|3uTl-A.01.01^^^AIRA-TEST^MR|Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381\r" +
"PID|1||7237424^^^ORA^SR~3uTl-A.01.01^^^ORA^MR||CHAMBERS^MAXIM^M^^^^L|JEFFERSON^ELISE|20111211|M||2131-1|356 TRIMBLE LN^^TEKONSHA^MI^49092^^H||^PRN^PH^^^517^9212381|||||||||2186-5||N|0\r" +
"PD1|||||||||||02|N||||A\r" +
"NK1|1|CHAMBERS^ELISE|MTH|356 TRIMBLE LN^^TEKONSHA^MI^49092^^P|^PRN^PH^^^517^9212381\r";

  private static final String RSP3 = "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20151217||RSP^K11^RSP_K11|1450391207965.3|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r" +
"MSA|AA|1450391207965.3||0||0^Message Accepted^HL70357\r" +
"QAK|1450391207965.3|OK|Z34\r" +
"QPD|Z34^Request Immunization History^HL70471|1450391207965.3|3uTl-A.01.01^^^AIRA-TEST^MR|Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381\r" +
"PID|1||7237424^^^ORA^SR~3uTl-A.01.01^^^ORA^MR||CHAMBERS^MAXIM^M^^^^L|JEFFERSON^ELISE|20111211|M||2131-1|356 TRIMBLE LN^^TEKONSHA^MI^49092^^H||^PRN^PH^^^517^9212381|||||||||2186-5||N|0\r" +
"PD1|||||||||||02|N||||A\r" +
"NK1|1|CHAMBERS^ELISE|MTH|356 TRIMBLE LN^^TEKONSHA^MI^49092^^P|^PRN^PH^^^517^9212381\r" +
"ORC|RE||134037533\r" +
"RXA|0|1|20151217|20151217|140^MMR^CVX|1.0|||00||^^^BUNKER CLINIC||||P3400YX||MSD|||CP\r" +
"RXR|SC|LA\r";
  private static final String RSP4 = "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20151217||RSP^K11^RSP_K11|1450391207965.3|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r" +
"MSA|AA|1450391207965.3||0||0^Message Accepted^HL70357\r" +
"QAK|1450391207965.3|OK|Z34\r" +
"QPD|Z34^Request Immunization History^HL70471|1450391207965.3|3uTl-A.01.01^^^AIRA-TEST^MR|Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381\r" +
"PID|1||7237424^^^ORA^SR~3uTl-A.01.01^^^ORA^MR||CHAMBERS^EDGAR^M^^^^L|JEFFERSON^ELISE|20111211|M||2131-1|356 TRIMBLE LN^^TEKONSHA^MI^49092^^H||^PRN^PH^^^517^9212381|||||||||2186-5||N|0\r" +
"PD1|||||||||||02|N||||A\r" +
"NK1|1|CHAMBERS^ELISE|MTH|356 TRIMBLE LN^^TEKONSHA^MI^49092^^P|^PRN^PH^^^517^9212381\r" +
"ORC|RE||134037533\r" +
"RXA|0|1|20151217|20151217|03^MMR^CVX|1.0|||00||^^^BUNKER CLINIC||||P3400YX||MSD|||CP\r" +
"RXR|SC|LA\r";

  
  @Test
  public void testQueryReturnedMostImportantData()
  {
    assertTrue(CompareManager.queryReturnedMostImportantData(VXU, RSP1));
    assertFalse(CompareManager.queryReturnedMostImportantData(VXU, RSP2));
    assertFalse(CompareManager.queryReturnedMostImportantData(VXU, RSP3));
    assertFalse(CompareManager.queryReturnedMostImportantData(VXU, RSP4));
  }
}
