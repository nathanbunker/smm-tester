package org.immregistries.smm.tester.run;

import org.immregistries.smm.transform.TestCaseMessage;
import org.junit.Test;
import junit.framework.TestCase;

public class FindMatchingSegmentTest extends TestCase {

  static final String EXAMPLE_01 =
      "MSH|^~\\&|NMSIIS2.2|NMSIIS||908|20130108125954.660||ACK|NIST-IZ-001.00|P|2.5.1\r"
          + "MSA|AA|NIST-IZ-001.00|INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"
          + "ERR||OBX^2^3^0|204^Unknown key identifier^HL70357\r"
          + "MSA|AA|NIST-IZ-001.00|INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"
          + "ERR||OBX^3^3^0|204^Unknown key identifier^HL70357\r"
          + "MSA|AA|NIST-IZ-001.00|INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"
          + "ERR||OBX^4^3^0|204^Unknown key identifier^HL70357\r"
          + "MSA|AA|NIST-IZ-001.00|Informational Message - If supplied, MSH 6 must match constraint listed in spec.\r"
          + "ERR||MSH^1|102^^HL70357\r"
          + "MSA|AA|NIST-IZ-001.00|Informational Message - No insurance verification date sent for new administered immunization.  Please verify insurance.\r"
          + "ERR|||^^HL70357\r"
          + "MSA|AA|NIST-IZ-001.00|Informational Message - Invalid insurance coverage for clients age sent for new administered immunization.  Please verify insurance.\r"
          + "ERR|||^^HL70357\r";
  
  static final String EXAMPLE_02 = "MSH|^~\\&||IIS Sandbox v0.2.13|||20200416060540-0600||RSP^K11^RSP_K11|1587038740943785|P|2.5.1|||NE|NE|||||Z32^CDCPHINVS\r\n" + 
      "MSA|AA|1587038735803.5\r\n" + 
      "QAK|1587038735803.5|OK|Z34^Request a Complete Immunization History^CDCPHINVS\r\n" + 
      "QPD|Z34^Request Immunization History^CDCPHINVS|1587038735803.5|D81Z1^^^AIRA-TEST^MR|HardinAIRA^GwynforAIRA^N^^^^L|IndianaAIRA^CarlieAIRA^^^^^M|20191005|M|1126 Maadhoeke Pl^^Saint Clr Shrs^MI^48082^USA^P|^PRN^PH^^^586^3869647|||||\r\n" + 
      "PID|1||ZJD2PXSKDC2M^^^IIS^SR~D81Z1^^^AIRA-TEST^MR||HardinAIRA^GwynforAIRA^N^^^^L|IndianaAIRA^^^^^^M|20191005|M|||1126 Maadhoeke Pl^^Saint Clr Shrs^MI^48082^USA^P||^PRN^PH^^^586^3869647||||||||||||\r\n" + 
      "NK1|1|HardinAIRA^CarlieAIRA^^^^^L|MTH^Mother^HL70063\r\n" + 
      "ORC|RE|D81Z1.2^Mercy Healthcare|72250^IIS\r\n" + 
      "RXA|0|1|20200203||133^Pneumococcal conjugate PCV 13^CVX|999.0|mL^milliliters^UCUM||01^Historical information - source unspecified^NIP001|||||||||||CP|A\r\n" + 
      "";
  
  @Test 
  public void testMrnInQuery() throws Exception
  {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setActualResponseMessage(EXAMPLE_02);
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_SEGMENT_RETURNED);

    assertMatch(testCaseMessage, "PID|||D81Z1^^^^MR|||");
    
  }

  @Test
  public void test() throws Exception {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setActualResponseMessage(EXAMPLE_01);
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_SEGMENT_RETURNED);

    assertNotMatch(testCaseMessage, "ERR||PID|");
    assertMatch(testCaseMessage, "MSH||NMSIIS2.2|");
    assertMatch(testCaseMessage, "MSH|||NMSIIS|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^0|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^|");
    assertMatch(testCaseMessage, "ERR||OBX^2^^|");
    assertMatch(testCaseMessage, "ERR||OBX^^^|");
    assertMatch(testCaseMessage, "ERR||OBX|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^0| -OR- ERR||PID|");
    assertMatch(testCaseMessage, "ERR||PID| -OR- ERR||OBX^2^3^0|");
    assertNotMatch(testCaseMessage, "ERR||OBX^2^3^0| -AND- ERR||PID|");
    assertMatch(testCaseMessage, "ERR||PID| -OR- ERR||NK1| -OR- ERR||OBX^2^3^0|");
    assertNotMatch(testCaseMessage, "ERR||PID| -OR- ERR||NK1| -AND- ERR||OBX^2^3^0|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^0| -or- ERR||PID|");
    assertNotMatch(testCaseMessage, "ERR||OBX^2^3^0| -and- ERR||PID|");
  }

  @Test
  public void testNotReturned() throws Exception {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setActualResponseMessage(EXAMPLE_01);
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_SEGMENT_NOT_RETURNED);

    assertMatch(testCaseMessage, "MSH||NMSIIS2.2|");
    assertMatch(testCaseMessage, "MSH|||NMSIIS|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^0|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^|");
    assertMatch(testCaseMessage, "ERR||OBX^2^^|");
    assertMatch(testCaseMessage, "ERR||OBX^^^|");
    assertMatch(testCaseMessage, "ERR||OBX|");
    assertNotMatch(testCaseMessage, "ERR||PID|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^0| -OR- ERR||PID|");
    assertMatch(testCaseMessage, "ERR||PID| -OR- ERR||OBX^2^3^0|");
    assertNotMatch(testCaseMessage, "ERR||OBX^2^3^0| -AND- ERR||PID|");
    assertMatch(testCaseMessage, "ERR||PID| -OR- ERR||NK1| -OR- ERR||OBX^2^3^0|");
    assertNotMatch(testCaseMessage, "ERR||PID| -OR- ERR||NK1| -AND- ERR||OBX^2^3^0|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^0| -or- ERR||PID|");
    assertNotMatch(testCaseMessage, "ERR||OBX^2^3^0| -and- ERR||PID|");
  }

  @Test
  public void testEmpty() throws Exception {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setActualResponseMessage(EXAMPLE_01);
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_SEGMENT_RETURNED);

    assertMatch(testCaseMessage, "ERR|<EMPTY>|");
    assertNotMatch(testCaseMessage, "ERR|||^^<EMPTY>");
  }

  public void assertMatch(TestCaseMessage testCaseMessage, String match) {
    testCaseMessage.setAssertResultParameter(match);
    FindMatchingSegment findMatchingSegment = new FindMatchingSegment(testCaseMessage);
    assertTrue(findMatchingSegment.checkForMatch());
  }

  public void assertNotMatch(TestCaseMessage testCaseMessage, String match) {
    testCaseMessage.setAssertResultParameter(match);
    FindMatchingSegment findMatchingSegment = new FindMatchingSegment(testCaseMessage);
    assertFalse(findMatchingSegment.checkForMatch());
  }

}
