package org.immregistries.smm.tester.run;

import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.SoapConnector;
import org.immregistries.smm.transform.TestCaseMessage;
import org.junit.Test;

import junit.framework.TestCase;

public class TestRunnerTest extends TestCase {

  private static final String ACK_MESSAGE1 = "MSH|^~\\&|NISTIISAPP|NISTIISFAC|NISTEHRAPP|NISTEHRFAC|20150625121047.853-0500||ACK^V04^ACK|NIST-IZ-AD-7.2_Receive_ACK_Z23|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|NISTIISFAC^^^^^NIST-AA-IZ-1^XX^^^100-3322|NISTEHRFAC^^^^^NIST-AA-IZ-1^XX^^^100-6482\r"
      + "MSA|AE|NIST-IZ-AD-7.1_Send_V04_Z22\r"
      + "ERR||RXA^1^5^1^1|999^Application error^HL70357|E|5^Table value not found^HL70533|||Vaccine code not recognized - message rejected\r";

  private static final String ACK_MESSAGE2 = "MSH|^~\\&|NISTIISAPP|NISTIISFAC|NISTEHRAPP|NISTEHRFAC|20150625121047.853-0500||ACK^V04^ACK|NIST-IZ-AD-7.2_Receive_ACK_Z23|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|NISTIISFAC^^^^^NIST-AA-IZ-1^XX^^^100-3322|NISTEHRFAC^^^^^NIST-AA-IZ-1^XX^^^100-6482\r"
      + "MSA|AE|NIST-IZ-AD-7.1_Send_V04_Z22\r"
      + "ERR|||999^Application error^HL70357|E|5^Table value not found^HL70533|||Vaccine code not recognized - message rejected\r"
      + "ERR|||999^Application error^HL70357|E|5^Table value not found^HL70533|||Vaccine code not recognized - message rejected\r";

  private static final String ACK_MESSAGE3 = "MSH|^~\\&|NISTIISAPP|NISTIISFAC|NISTEHRAPP|NISTEHRFAC|20150625121047.853-0500||ACK^V04^ACK|NIST-IZ-AD-7.2_Receive_ACK_Z23|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|NISTIISFAC^^^^^NIST-AA-IZ-1^XX^^^100-3322|NISTEHRFAC^^^^^NIST-AA-IZ-1^XX^^^100-6482\r"
      + "MSA|AE|NIST-IZ-AD-7.1_Send_V04_Z22\r"
      + "ERR||RXA^1^4^1^1|999^Application error^HL70357|E|5^Table value not found^HL70533|||Vaccine code not recognized - message rejected\r"
      + "ERR||RXA^2^5^1^1|999^Application error^HL70357|E|5^Table value not found^HL70533|||Vaccine code not recognized - message rejected\r";

  @Test
  public void test() throws Exception {
    verifyErrorLocationCheckPass("RXA-5", ACK_MESSAGE1);
    verifyErrorLocationCheckPass("RXA-5.1", ACK_MESSAGE1);
    verifyErrorLocationCheckPass("RXA-5.5", ACK_MESSAGE1);
    verifyErrorLocationCheckFail("RXA-6", ACK_MESSAGE1);
    verifyErrorLocationCheckFail("RXA-6.1", ACK_MESSAGE1);
    verifyErrorLocationCheckPass("RXA-5 or RXA-6", ACK_MESSAGE1);
    verifyErrorLocationCheckPass("RXA-6 or RXA-5", ACK_MESSAGE1);
    verifyErrorLocationCheckFail("RXA-6 or RXA-7", ACK_MESSAGE1);

    verifyErrorLocationCheckFail("RXA-5", ACK_MESSAGE2);

    verifyErrorLocationCheckPass("RXA-5", ACK_MESSAGE3);
    verifyErrorLocationCheckFail("RXA-7", ACK_MESSAGE3);

  }

  private void verifyErrorLocationCheckPass(String field, String message) throws Exception {
    verifyErrorLocationCheck(true, field, message);

  }

  private void verifyErrorLocationCheckFail(String field, String message) throws Exception {
    verifyErrorLocationCheck(false, field, message);

  }

  private void verifyErrorLocationCheck(boolean pass, String field, String message) throws Exception {
    TestRunner testRunner = new TestRunner();
    testRunner.ackMessageText = message;
    Connector connector = new SoapConnector("Test", "http://localhost");
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_ERROR_LOCATION_IS_ + field);
    ;
    testRunner.evaluateRunTest(connector, testCaseMessage);
    if (pass) {
      assertTrue(testRunner.isPassedTest());
    } else {
      assertFalse(testRunner.isPassedTest());
    }
  }

}
