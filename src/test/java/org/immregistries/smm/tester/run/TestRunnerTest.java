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
    testRunner.actualResponseMessage = message;
    Connector connector = new SoapConnector("Test", "http://localhost");
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setTestType("VXU");
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_ERROR_LOCATION_IS_ + field);
    testRunner.evaluateRunTest(connector, testCaseMessage);
    if (pass) {
      assertTrue(testRunner.isPassedTest());
    } else {
      assertFalse(testRunner.isPassedTest());
    }
  }
  
  private static final String SEGMENT_RETURNED_MESSSAGE = "MSH|^~\\&||IIS Sandbox v0.2.04|||20191226020336-0700||RSP^K11^RSP_K11|15773942162132475|P|2.5.1|||NE|NE|||||Z32^CDCPHINVS\r" + 
      "MSA|AA|3YFi-GM-1.1-Q\r" + 
      "QAK|1577393948228.2486|OK|Z34^Request a Complete Immunization History^CDCPHINVS\r" + 
      "QPD|Z34^Request Immunization History^CDCPHINVS|1577393948228.2486|Q00X19308^^^NIST-MPI-1^MR|GageAIRA^KaranAIRA^Elbow^^^^L|MuhlenbergAIRA^^^^^^M|20191016|M|1211 Zeggen St^^Prattville^MI^49271^USA^P|^PRN^PH^^^517^2216175|N|1|||\r" + 
      "PID|1||KRJSYXC1AQXH^^^IIS^SR~Q00X19308^^^NIST-MPI-1^MR||GageAIRA^KaranAIRA^Elbow^^^^L|MuhlenbergAIRA^^^^^^M|20191016|M|||1211 Zeggen St^^Prattville^MI^49271^USA^P||^PRN^PH^^^517^2216175\r" + 
      "NK1|1|GageAIRA^MuhlenbergAIRA^^^^^L|MTH^Mother^HL70063\r" + 
      "ORC|RE|BQ00X19308.1^AART Florence|10690^IIS\r" + 
      "RXA|0|1|20191225||120^DTaP-Hib-IPV^CVX~49281-0560-05^DTAP-IPV^NDC|0.5|mL^milliliters^UCUM||00^New immunization record^NIP001||||||526434|20200122|PMC^sanofi pasteur^MVX|||CP|A\r" + 
      "RXR|C28161^Intramuscular^NCIT|RT^Right Thigh^HL70163\r" + 
      "OBX|1|CE|30956-7^Vaccine type^LN|1|17^17^CVX||||||F\r" + 
      "OBX|2|CE|59781-5^Dose validity^LN|1|Y^Y^99107||||||F\r" + 
      "OBX|3|CE|30956-7^Vaccine type^LN|2|17^17^CVX||||||F";
  
  @Test
  public void testVerifySegmentReturned() throws Exception {
    verifySegmentReturned(true, "OBX|1");
    verifySegmentReturned(true, "OBX|1|");
    verifySegmentReturned(true, "OBX|1^|");
    verifySegmentReturned(true, "OBX|1^^|");
    verifySegmentReturned(true, "OBX|1~|");
    verifySegmentReturned(true, "OBX|2");
    verifySegmentReturned(true, "OBX|3");
    verifySegmentReturned(false, "OBX|4");
    verifySegmentReturned(false, "ZZZ|4");
    verifySegmentReturned(true, "OBX|||59781-5^Dose validity^LN||");
    verifySegmentReturned(true, "OBX|2|CE|59781-5^Dose validity^LN|1|Y^Y^99107||||||F");
  }

  public void verifySegmentReturned(boolean pass, String segmentToMatch) throws Exception {
    Connector connector = new SoapConnector("Test", "http://localhost");
    String message = SEGMENT_RETURNED_MESSSAGE;
    TestRunner testRunner = new TestRunner();
    testRunner.actualResponseMessage = message;
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setTestType("QBP");
    testCaseMessage.setActualResponseMessage(message);
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_SEGMENT_RETURNED);
    testCaseMessage.setAssertResultParameter(segmentToMatch);
    testRunner.evaluateRunTest(connector, testCaseMessage);
    if (pass) {
      assertTrue(testRunner.isPassedTest());
    } else {
      assertFalse(testRunner.isPassedTest());
    }
  }

}
