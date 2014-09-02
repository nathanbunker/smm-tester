package org.immunizationsoftware.dqa.mover;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAckAnalyzer
{

  private static final String AZ_ACK_POS = "MSH|^~\\&|^^|NIST Test Iz Reg^^|Test EHR Application^^|X68^^|20130111105113||ACK^|8142841480.100375760|P|2.5.1|\r"
      + "MSA|AA|NIST-IZ-001.00|Patient D26376273 \"Madelynn Snow\" with 1 vaccination accepted into vaccination staging table for immediate deduplication|\r";

  private static final String AL_ACK_NEG = "MSH|^~\\&|AL-IIS|AL-IIS|1270|9250|20130329094218-0500000||ACK|401008|P|2.5.1||||\r"
      + "MSA|AE|NIST-IZ-001.0B0|Error 20301 - Message Validation Failed\r"
      + "ERR|MSH^1^5^1006&Required field missing&Symphonia Validation&&MSH/ReceivingApplication~RXA^1^4^1006&Required field missing&Symphonia Validation&&ORDER[0]/RXA/DateTimeEndOfAdministration/Time/Year|||\r";

  private static final String CA_ACK_NEG = "MSH|^~\\&|CAIR|CAIR||X68|20130215155934||ACK^V04^ACK|NIST-IZ-016.00|P|2.5.1|||ER\r"
      + "MSA|AE|NIST-IZ-016.00\r" + "ERR||RXA^5|^Invalid vaccine code: CVX code: 998, Admin Date: 2/15/2011|E\r";

  private static final String CO_ACK_POS = "MSH|^~\\&|CIIS|CDPHE|||20130221090016||ACK|2013022109001679|D|2.5.1\r" + "MSA|AA\r";

  private static final String IL_ACK_POS = "MSH|^~\\&||ICARE|TEST||20130313162758||ACK^V04^ACK_V04|3352136|P|2.5.1|||||||||\r"
      + "MSA|AA|NIST-IZ-001.00|Message had been sent to queue for updates.\r" + "ERR|||0|I\r";

  private static final String KY_ACK_POS = "MSH|^~\\&|KYIR|CDP^5555500001|Test EHR Application|Test^5555500001^DNS|20130312102854||ACK^V04^ACK|ANIST-IZ-001.00|P|2.5.1|\r"
      + "MSA|AA|NIST-IZ-001.00|\r";

  private static final String NM_ACK_POS = "MSH|^~\\&|NMSIIS2.2|NMSIIS||908|20130108125954.660||ACK|NIST-IZ-001.00|P|2.5.1\r"
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

  private static final String NM_ACK_NEG = "MSH|^~\\&|NMSIIS2.2|NMSIIS||908|20130110100349.301||ACK|NIST-IZ-016.00|P|2.5.1\r"
      + "MSA|AA|NIST-IZ-016.00|INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\n" + "ERR||OBX^1^3^0|204^Unknown key identifier^HL70357\n"
      + "MSA|AA|NIST-IZ-016.00|Record Rejected. 998 is an invalid CVX code\n" + "ERR||RXA^1^5^1^1|103^Table value not found^HL70357\n"
      + "MSA|AA|NIST-IZ-016.00|Record rejected. All immunizations invalid.\n" + "ERR||MSH^1^0|100^Segment sequence error^HL70357\n"
      + "MSA|AA|NIST-IZ-016.00|Informational Message - If supplied, MSH 6 must match constraint listed in spec.\n" + "ERR||MSH^1|102^^HL70357\n";

  private static final String NV_ACK_POS = "MSH|^~\\&|IZ Registry^1.3.6.1.4.1.26279.1.1.20090930^L|NV0000^DBO^L|Test EHR Application|NV2201|20130124115205||ACK|20130124NV0000520548|P|2.3.1||||||||\r"
      + "MSA|AA|NIST-IZ-001.00|VXU processed.|||";

  private static final String OR_ACK_POS = "MSH|^~\\&amp;|ALERT IIS3.2.0|ALERT IIS||AL9998|20130315||ACK|NIST-IZ-001.00|P|2.5.1\r"
      + "MSA|AR|NIST-IZ-001.00|INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"
      + "ERR||OBX^1^3^0|204^Unknown key identifier^HL70357\r"
      + "MSA|AR|NIST-IZ-001.00|INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"
      + "ERR||OBX^2^3^0|204^Unknown key identifier^HL70357\r"
      + "MSA|AR|NIST-IZ-001.00|INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\r"
      + "ERR||OBX^4^3^0|204^Unknown key identifier^HL70357\r"
      + "MSA|AR|NIST-IZ-001.00|System Vaccine Lot information not available. The incoming immunization that this system retained may be identified by the following characteristics -> Vaccination Date: 08142012 CVX Code:140 Lot Number:Z0860BB.\r"
      + "ERR|||^^HL70357\r";

  @Test
  public void testAckAnalyzer()
  {
    AckAnalyzer ackAnalyzer;

    ackAnalyzer = new AckAnalyzer(AZ_ACK_POS);
    assertTrue(ackAnalyzer.isAckMessage());
    assertTrue(ackAnalyzer.isPositive());
    assertEquals("AA", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(AL_ACK_NEG);
    assertTrue(ackAnalyzer.isAckMessage());
    assertFalse(ackAnalyzer.isPositive());
    assertEquals("AE", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(CA_ACK_NEG);
    assertTrue(ackAnalyzer.isAckMessage());
    assertFalse(ackAnalyzer.isPositive());
    assertEquals("AE", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(CO_ACK_POS);
    assertTrue(ackAnalyzer.isAckMessage());
    assertTrue(ackAnalyzer.isPositive());
    assertEquals("AA", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(IL_ACK_POS);
    assertTrue(ackAnalyzer.isAckMessage());
    assertTrue(ackAnalyzer.isPositive());
    assertEquals("AA", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(KY_ACK_POS);
    assertTrue(ackAnalyzer.isAckMessage());
    assertTrue(ackAnalyzer.isPositive());
    assertEquals("AA", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(NM_ACK_POS, AckAnalyzer.AckType.NMSIIS, null);
    assertTrue(ackAnalyzer.isAckMessage());
    assertTrue(ackAnalyzer.isPositive());
    assertEquals("AA", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(NM_ACK_NEG, AckAnalyzer.AckType.NMSIIS, null);
    assertTrue(ackAnalyzer.isAckMessage());
    assertFalse(ackAnalyzer.isPositive());
    assertEquals("AA", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(NV_ACK_POS);
    assertTrue(ackAnalyzer.isAckMessage());
    assertTrue(ackAnalyzer.isPositive());
    assertEquals("AA", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(OR_ACK_POS, AckAnalyzer.AckType.ALERT, null);
    assertTrue(ackAnalyzer.isAckMessage());
    assertTrue(ackAnalyzer.isPositive());
    assertEquals("AR", ackAnalyzer.getAckCode());
  }

}
