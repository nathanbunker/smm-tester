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

  @Test
  public void test() throws Exception {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setActualResponseMessage(EXAMPLE_01);
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_SEGMENT_RETURNED);

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
  public void testNotReturned() throws Exception {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setActualResponseMessage(EXAMPLE_01);
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_SEGMENT_NOT_RETURNED);

    assertNotMatch(testCaseMessage, "MSH||NMSIIS2.2|");
    assertNotMatch(testCaseMessage, "MSH|||NMSIIS|");
    assertNotMatch(testCaseMessage, "ERR||OBX^2^3^0|");
    assertNotMatch(testCaseMessage, "ERR||OBX^2^3^|");
    assertNotMatch(testCaseMessage, "ERR||OBX^2^^|");
    assertNotMatch(testCaseMessage, "ERR||OBX^^^|");
    assertNotMatch(testCaseMessage, "ERR||OBX|");
    assertMatch(testCaseMessage, "ERR||PID|");
    assertNotMatch(testCaseMessage, "ERR||OBX^2^3^0| -OR- ERR||PID|");
    assertNotMatch(testCaseMessage, "ERR||PID| -OR- ERR||OBX^2^3^0|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^0| -AND- ERR||PID|");
    assertNotMatch(testCaseMessage, "ERR||PID| -OR- ERR||NK1| -OR- ERR||OBX^2^3^0|");
    assertMatch(testCaseMessage, "ERR||PID| -OR- ERR||NK1| -AND- ERR||OBX^2^3^0|");
    assertNotMatch(testCaseMessage, "ERR||OBX^2^3^0| -or- ERR||PID|");
    assertMatch(testCaseMessage, "ERR||OBX^2^3^0| -and- ERR||PID|");
  }

  @Test
  public void testEmpty() throws Exception {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setActualResponseMessage(EXAMPLE_01);
    testCaseMessage.setAssertResult(TestRunner.ASSERT_RESULT_SEGMENT_RETURNED);

    assertMatch(testCaseMessage, "ERR|<EMPTY>|");
    assertNotMatch(testCaseMessage, "ERR||<EMPTY>");
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
