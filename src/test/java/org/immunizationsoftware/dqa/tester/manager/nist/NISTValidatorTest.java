package org.immunizationsoftware.dqa.tester.manager.nist;

import org.immunizationsoftware.dqa.tester.run.TestRunner;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

import static org.junit.Assert.*;

import org.junit.Test;

public class NISTValidatorTest
{
  private static final String TEST1 = "MSH|^~\\&|MCIR|MCIR|||20160222052724-0700||ACK^V04^ACK|20160222052724-0700.2029|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AE|K47Q1.ay|\r"
      + "ERR||ORC^1^2^1|101^Required field missing^HL70357|W||||Vaccination placer order number is missing|\r"
      + "ERR||ORC^2^2^1|101^Required field missing^HL70357|W||||Vaccination placer order number is missing, in repeat #2|\r"
      + "ERR||ORC^3^2^1|101^Required field missing^HL70357|W||||Vaccination placer order number is missing, in repeat #3|\r"
      + "ERR||RXA^3^11^1^4|101^Required field missing^HL70357|W||||Vaccination facility id is missing, in repeat #3|\r"
      + "ERR|||0^Message accepted^HL70357|W||||Message accepted but some data was lost (see error details above)|\r";

  private static final String TEST2 = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AE|K47Q1.ay|\r"
      + "ERR||PID^1^3^1|101^Required field missing^HL70357|E||||Patient submitter id is missing|\r"
      + "ERR||ORC^1^2^1|101^Required field missing^HL70357|E||||Vaccination placer order number is missing|\r";


  private static final String TEST3 = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AE|K47Q1.ay|\r"
      + "ERR||PID^1^3^1|101^Required field missing^HL70357|W||||Patient submitter id is missing|\r"
      + "ERR||ORC^1^2^1|101^Required field missing^HL70357|W||||Vaccination placer order number is missing|\r";

  
  private static final String TEST4 = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AE|K47Q1.ay|\r"
      + "ERR||PID^1^3^1|101^Required field missing^HL70357|E||||Patient submitter id is missing|\r"
      + "ERR||ORC^1^2^1|101^Required field missing^HL70357|W||||Vaccination placer order number is missing|\r";

  private static final String TEST5 = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AE|K47Q1.ay|\r"
      + "ERR||PID^1^3^1|101^Required field missing^HL70357|I||||Patient submitter id is missing|\r"
      + "ERR||ORC^1^2^1|101^Required field missing^HL70357|I||||Vaccination placer order number is missing|\r";

  private static final String TEST6 = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AE|K47Q1.ay|\r";

  private static final String TEST7 = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AA|K47Q1.ay|\r";

  private static final String TEST8 = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AA|K47Q1.ay|\r"
      + "ERR||PID^1^3^1|101^Required field missing^HL70357|W||||Patient submitter id is missing|\r"
      + "ERR||ORC^1^2^1|101^Required field missing^HL70357|E||||Vaccination placer order number is missing|\r";

  
  private static final String TEST9 = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AA|K47Q1.ay|\r"
      + "ERR||PID^1^3^1|101^Required field missing^HL70357|W||||Patient submitter id is missing|\r";

  private static final String TEST10 = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AA|K47Q1.ay|\r"
      + "ERR||ORC^1^2^1|101^Required field missing^HL70357|E||||Vaccination placer order number is missing|\r";

  
  private static final String MN1 = "FHS|^~\\&|MIIC|MIIC||ATEST|20160222164438.919||1738749.response\r"+
"BHS|^~\\&|MIIC|MIIC||ATEST|20160222164438.920\r"+
"MSH|^~\\&|MIIC|MIIC||ATEST|20160222164438.920||ACK|V85I1.1Y3|P|2.5.1\r"+
"MSA|AE|V85I1.1Y3\r"+
"ERR||PV1^1^20|102^Data type error^HL70357|E||||NO CLIENT FINANCIAL CLASS VALUES SPECIFIED. PV1 SEGMENT IGNORED.\r"+
"ERR||RXA^1^0|100^Segment sequence error^HL70357|E||||MISSING_OBX_FOR_RXA\r"+
"ERR||RXA^2^0|100^Segment sequence error^HL70357|E||||MISSING_OBX_FOR_RXA\r"+
"ERR||OBX^2^3^0|204^Unknown key identifier^HL70357|E||||INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"+
"ERR||OBX^3^3^0|204^Unknown key identifier^HL70357|E||||INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"+
"ERR||OBX^4^3^0|204^Unknown key identifier^HL70357|E||||INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"+
"BTS|1\r"+
"FTS|1\r";

  private static final String MN2 = "FHS|^~\\&|MIIC|MIIC||ATEST|20160222164438.919||1738749.response\r"+
"BHS|^~\\&|MIIC|MIIC||ATEST|20160222164438.920\r"+
"MSH|^~\\&|MIIC|MIIC||ATEST|20160222164438.920||ACK|V85I1.1Y3|P|2.5.1\r"+
"MSA|AE|V85I1.1Y3\r"+
"ERR||PV1^1^20|102^Data type error^HL70357|E||||NO CLIENT FINANCIAL CLASS VALUES SPECIFIED. PV1 SEGMENT IGNORED.\r"+
"ERR||RXA^1^0|100^Segment sequence error^HL70357|E||||MISSING_OBX_FOR_RXA\r"+
"ERR||RXA^2^0|100^Segment sequence error^HL70357|E||||MISSING_OBX_FOR_RXA\r"+
"ERR||OBX^2^3^0|204^Unknown key identifier^HL70357|E||||INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"+
"ERR||OBX^3^3^0|204^Unknown key identifier^HL70357|E||||INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"+
"ERR||OBX^4^3^0|204^Unknown key identifier^HL70357|E||||INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"+
"BTS|1\r"+
"FTS|1\r";

  @Test
  public void test() {
    expectPass(TEST1, "Test 1");
    expectPass(TEST2, "Test 2");
    expectPass(TEST3, "Test 3");
    expectPass(TEST4, "Test 4");
    expectFail(TEST5, "Test 5");
    expectFail(TEST6, "Test 6");
    expectPass(TEST7, "Test 7");
    expectFail(TEST8, "Test 8");
    expectFail(TEST9, "Test 9");
    expectFail(TEST10, "Test 10");
  }

  
  @Test
  public void testMN() {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    TestRunner.validateResponseWithNIST(testCaseMessage, MN1);
    System.out.println("MN 1" + ": " + (testCaseMessage.isValidationReportPass() ? " Passed" : " Failed") + " Validation");
    int errorCount = 0;
    for (Assertion assertion : testCaseMessage.getValidationReport().getAssertionList()) {
      if (assertion.getResult().equalsIgnoreCase("error")) {
        System.out.println("  + " + assertion.getDescription() + " - " + assertion.getPath());
        errorCount++;
      }
    }
    assertTrue(!testCaseMessage.isValidationReportPass());
    assertTrue(errorCount > 0);
  }
  public void expectPass(String testMessage, String testName) {
    TestCaseMessage testCaseMessage = runTest(testMessage, testName);
    assertTrue(testCaseMessage.isValidationReportPass());
  }

  public void expectFail(String testMessage, String testName) {
    TestCaseMessage testCaseMessage = runTest(testMessage, testName);
    assertFalse(testCaseMessage.isValidationReportPass());
  }

  public TestCaseMessage runTest(String testMessage, String testName) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    TestRunner.validateResponseWithNIST(testCaseMessage, testMessage);
    System.out.println(testName + ": " + (testCaseMessage.isValidationReportPass() ? " Passed" : " Failed") + " Validation");
    for (Assertion assertion : testCaseMessage.getValidationReport().getAssertionList()) {
      if (assertion.getResult().equalsIgnoreCase("error")) {
        System.out.println("  + " + assertion.getDescription() + " - " + assertion.getPath());
      }
    }
    System.out.println("  + " + testCaseMessage.getValidationReport().getHeaderReport().getValidationStatus());
    return testCaseMessage;
  }
}
