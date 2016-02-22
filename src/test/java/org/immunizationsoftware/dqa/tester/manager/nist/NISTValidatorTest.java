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

  
  private static final String TEST2_ORIG = "MSH|^~\\&|MCIR|MCIR|||20160222052801-0700||ACK^V04^ACK|20160222052801-0700.2030|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|\r"
      + "SFT|AIRA|1.10.00|DQA|1.10.00.20160219|\r" + "MSA|AE|K47Q1.ay|\r"
      + "ERR||PID^1^3^1|101^Required field missing^HL70357|E||||Patient submitter id is missing|\r"
      + "ERR||ORC^1^2^1|101^Required field missing^HL70357|W||||Vaccination placer order number is missing|\r"
      + "ERR||ORC^2^2^1|101^Required field missing^HL70357|W||||Vaccination placer order number is missing, in repeat #2|\r"
      + "ERR||ORC^3^2^1|101^Required field missing^HL70357|W||||Vaccination placer order number is missing, in repeat #3|\r"
      + "ERR||RXA^3^11^1^4|101^Required field missing^HL70357|W||||Vaccination facility id is missing, in repeat #3|\r"
      + "ERR|||101^Required field missing^HL70357|E||||Message rejected: Because of serious problems encountered none of the information in this message will be accepted (see error details above)|\r";

  @Test
  public void test() {
    runTest(TEST1, "Test 1");
    runTest(TEST2, "Test 2");
    runTest(TEST3, "Test 3");
    runTest(TEST4, "Test 4");
  }

  public void runTest(String testMessage, String testName) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    TestRunner.validateResponseWithNIST(testCaseMessage, testMessage);
    System.out.println(testName + ": " + (testCaseMessage.isValidationReportPass() ? " Passed" : " Failed"));
    for (Assertion assertion : testCaseMessage.getValidationReport().getAssertionList()) {
      if (assertion.getResult().equalsIgnoreCase("error")) {
        System.out.println("  + " + assertion.getDescription() + " - " + assertion.getPath());
      }
    }

    assertTrue(testCaseMessage.isValidationReportPass());
  }
}
