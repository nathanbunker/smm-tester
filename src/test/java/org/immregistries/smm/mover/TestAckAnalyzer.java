package org.immregistries.smm.mover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestAckAnalyzer {

  private static final String AZ_ACK_POS =
      "MSH|^~\\&|^^|NIST Test Iz Reg^^|Test EHR Application^^|X68^^|20130111105113||ACK^|8142841480.100375760|P|2.5.1|\r"
          + "MSA|AA|NIST-IZ-001.00|Patient D26376273 \"Madelynn Snow\" with 1 vaccination accepted into vaccination staging table for immediate deduplication|\r";

  private static final String AL_ACK_NEG =
      "MSH|^~\\&|AL-IIS|AL-IIS|1270|9250|20130329094218-0500000||ACK|401008|P|2.5.1||||\r"
          + "MSA|AE|NIST-IZ-001.0B0|Error 20301 - Message Validation Failed\r"
          + "ERR|MSH^1^5^1006&Required field missing&Symphonia Validation&&MSH/ReceivingApplication~RXA^1^4^1006&Required field missing&Symphonia Validation&&ORDER[0]/RXA/DateTimeEndOfAdministration/Time/Year|||\r";

  private static final String CA_ACK_NEG =
      "MSH|^~\\&|CAIR|CAIR||X68|20130215155934||ACK^V04^ACK|NIST-IZ-016.00|P|2.5.1|||ER\r"
          + "MSA|AE|NIST-IZ-016.00\r"
          + "ERR||RXA^5|^Invalid vaccine code: CVX code: 998, Admin Date: 2/15/2011|E\r";

  private static final String CO_ACK_POS =
      "MSH|^~\\&|CIIS|CDPHE|||20130221090016||ACK|2013022109001679|D|2.5.1\r" + "MSA|AA\r";

  private static final String IL_ACK_POS =
      "MSH|^~\\&||ICARE|TEST||20130313162758||ACK^V04^ACK_V04|3352136|P|2.5.1|||||||||\r"
          + "MSA|AA|NIST-IZ-001.00|Message had been sent to queue for updates.\r" + "ERR|||0|I\r";

  private static final String KY_ACK_POS =
      "MSH|^~\\&|KYIR|CDP^5555500001|Test EHR Application|Test^5555500001^DNS|20130312102854||ACK^V04^ACK|ANIST-IZ-001.00|P|2.5.1|\r"
          + "MSA|AA|NIST-IZ-001.00|\r";

  private static final String NM_ACK_POS_1 =
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

  private static final String NM_ACK_POS_2 =
      "FHS|^~\\&|NMSIIS2.9|NMSIIS||DEFAULT2|20141105135222.495||164004.response||6847500-A1.1.4750\r"
          + "BHS|^~\\&|NMSIIS2.9|NMSIIS||DEFAULT2|20141105135222.495||||6847500-A1.1.4750\r"
          + "MSH|^~\\&|NMSIIS2.9|NMSIIS||DEFAULT2|20141105135222.505||ACK|6847500-A1.1.4750|P|2.5.1\r"
          + "MSA|AA|6847500-A1.1.4750|WARNING:    Inaccurate or missing observation value.  No value stored. See New Mexico HL 7 specifications. Table:  NM001 [W.207.23]\r"
          + "ERR||OBX^2^3^0|204^Unknown key identifier^HL70357\r"
          + "MSA|AA|6847500-A1.1.4750|WARNING:    Inaccurate or missing observation value.  No value stored. See New Mexico HL 7 specifications. Table:  NM001 [W.207.23]\r"
          + "ERR||OBX^3^3^0|204^Unknown key identifier^HL70357\r"
          + "MSA|AA|6847500-A1.1.4750|WARNING:    Inaccurate or missing observation value.  No value stored. See New Mexico HL 7 specifications. Table:  NM001 [W.207.23]\r"
          + "ERR||OBX^4^3^0|204^Unknown key identifier^HL70357\r"
          + "MSA|AA|6847500-A1.1.4750|WARNING:   No insurance verification date sent for new administered immunization.  Please verify insurance. See New Mexico HL 7 specifications. IN1-29  [W.207.43]\r"
          + "ERR|||^^HL70357\r" + "BTS|1\r" + "FTS|1\r";

  private static final String NM_ACK_NEG =
      "MSH|^~\\&|NMSIIS2.2|NMSIIS||908|20130110100349.301||ACK|NIST-IZ-016.00|P|2.5.1\r"
          + "MSA|AA|NIST-IZ-016.00|INACCURATE OR MISSING OBSERVATION VALUE. NO VALUE STORED.\n"
          + "ERR||OBX^1^3^0|204^Unknown key identifier^HL70357\n"
          + "MSA|AA|NIST-IZ-016.00|Record Rejected. 998 is an invalid CVX code\n"
          + "ERR||RXA^1^5^1^1|103^Table value not found^HL70357\n"
          + "MSA|AA|NIST-IZ-016.00|Record rejected. All immunizations invalid.\n"
          + "ERR||MSH^1^0|100^Segment sequence error^HL70357\n"
          + "MSA|AA|NIST-IZ-016.00|Informational Message - If supplied, MSH 6 must match constraint listed in spec.\n"
          + "ERR||MSH^1|102^^HL70357\n";

  private static final String NV_ACK_POS =
      "MSH|^~\\&|IZ Registry^1.3.6.1.4.1.26279.1.1.20090930^L|NV0000^DBO^L|Test EHR Application|NV2201|20130124115205||ACK|20130124NV0000520548|P|2.3.1||||||||\r"
          + "MSA|AA|NIST-IZ-001.00|VXU processed.|||";

  private static final String OR_ACK_POS =
      "MSH|^~\\&|ALERT IIS4.0.0|ALERT IIS||AL9997|20160218||ACK^V04^ACK|47NR-A.01.01.1th|P|2.5.1||||||||||ALERT IIS|AL9997\r"
          + "MSA|AE|47NR-A.01.01.1th\r"
          + "ERR||MSH^1^15|101^Required field missing^HL70357|W|6^Required observation missing^HL70533|||Informational Error - REQUIRED FIELD MSH-15 MISSING.\r"
          + "ERR||MSH^1^16|101^Required field missing^HL70357|W|6^Required observation missing^HL70533|||Informational Error - REQUIRED FIELD MSH-16 MISSING.\r"
          + "ERR||ORC^1^12^1^13|0^Message accepted^HL70357|W|5^Table value not found^HL70533|||Informational error - No value was entered for ORC-12.13\r";

  private static final String TEST_01 =
      "MSH|^~\\&|MCIR|MCIR||[DEFAULT=>'TEST']|20150211070501-0700||ACK^V04^VXU_V04|20150211070501-0700.1|P|2.5.1|\r"
          + "SFT|OIS|1.09.00|DQA||\r" + "MSA|AA|4904500-I.001.493|\r"
          + "ERR|||101^Required field missing^HL70357|W|^^^DQA0051^HL7 PV1 segment is missing^HL70533|||HL7 PV1 segment is missing|\r"
          + "ERR|||101^Required field missing^HL70357|W|^^^DQA0051^HL7 PV1 segment is missing^HL70533|||HL7 PV1 segment is missing|\r"
          + "ERR||ORC^1^2^1|101^Required field missing^HL70357|W|^^^DQA0387^Vaccination placer order number is missing^HL70533|||Vaccination placer order number is missing|\r"
          + "ERR||RXA^1^11.4^1|101^Required field missing^HL70357|W|^^^DQA0314^Vaccination facility id is missing^HL70533|||Vaccination facility id is missing|\r"
          + "ERR|||0^Message accepted^HL70357|I||||Message accepted with 1 vaccination|\r";

  private static final String IL_TEST_01 =
      "MSH|^~\\&||ICARE|TEST||20180510081950-0500||ACK^V04^ACK|1298061|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS \r"
          + "MSA|AR|1tMo-SA.13.2.1|Message had been ignored by ICARE system \r"
          + "ERR||PID^7|101^Required field missing^HL70357|E|2^Invalid Date^HL70533^101.1118^Invalid Birth Date^L|||Message had been Ignored. Invalid Birth Date \r";

  private static final String WI_TEST_01 =
      "MSH|^~\\&|WIR|||10318|20190824074646-0500||ACK^V04^ACK|1079866|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS\r"
          + "MSA|AE|2015-0022QCai75\r"
          + "ERR||MSH^1^6^1|102^Data type error^HL70357|W|4^Invalid value^HL70533|23A~HL70362||Value [23A] is not valid for HL70362\r"
          + "ERR||PID^1^11^1^7|101^Required field missing^HL70357|W||Address Type~1||Address Type is required\r"
          + "ERR||RXA^1^9^1|102^Data type error^HL70357|W|4^Invalid value^HL70533|||IZ-47: If RXA-20 is NOT valued 'CP' or 'PA' then the first occurrence of RXA-9.1 (admin notes) SHALL be empty and the following repetitions should be empty or valued with text notes.\r";

  private static final String PA_ERR_4_MISSING_1 =
      "MSH|^~\\&|PHIS-IZ|PA-SIIS|||202012281844||ACK|637447778778467647|P^T|2.5.1|||NE|AL|USA\r"
          + "MSA|AE\r" + "ERR|^^^207&Application internal error&HL70357";

  private static final String PA_ERR_4_MISSING_2 =
      "MSH|^~\\&|AvanzaPHISIZ|Avanza|||202012281731||ACK|637447734694842729|P^T|2.5.1|||NE|AL|USA\r"
          + "MSA|AE\r" + "ERR|||207^Application internal error^HL70357";

  @Test
  public void testAckAnalyzer() {
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

    ackAnalyzer = new AckAnalyzer(NM_ACK_POS_1, AckAnalyzer.AckType.NMSIIS, null);
    assertTrue(ackAnalyzer.isAckMessage());
    assertTrue(ackAnalyzer.isPositive());
    assertEquals("AA", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(NM_ACK_POS_2, AckAnalyzer.AckType.NMSIIS, null);
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
    assertEquals("AE", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(TEST_01, AckAnalyzer.AckType.DEFAULT);
    assertTrue(ackAnalyzer.isAckMessage());
    assertTrue(ackAnalyzer.isPositive());
    assertEquals("AA", ackAnalyzer.getAckCode());

    ackAnalyzer = new AckAnalyzer(IL_TEST_01, AckAnalyzer.AckType.DEFAULT);
    assertTrue(ackAnalyzer.isAckMessage());
    assertFalse(ackAnalyzer.isPositive());
    assertEquals("AR", ackAnalyzer.getAckCode());

    for (AckAnalyzer.AckType ackType : AckAnalyzer.AckType.values()) {
      System.out.println("--> ackType = " + ackType);
      ackAnalyzer = new AckAnalyzer(WI_TEST_01, ackType);
      assertTrue(ackAnalyzer.isAckMessage());
      assertTrue(ackAnalyzer.isPositive());
      assertEquals("AE", ackAnalyzer.getAckCode());
    }

    ackAnalyzer = new AckAnalyzer(PA_ERR_4_MISSING_1, AckAnalyzer.AckType.DEFAULT);
    assertTrue(ackAnalyzer.isAckMessage());
    assertFalse(ackAnalyzer.isPositive());
    assertEquals("AE", ackAnalyzer.getAckCode());


    ackAnalyzer = new AckAnalyzer(PA_ERR_4_MISSING_2, AckAnalyzer.AckType.DEFAULT);
    assertTrue(ackAnalyzer.isAckMessage());
    assertFalse(ackAnalyzer.isPositive());
    assertEquals("AE", ackAnalyzer.getAckCode());


  }

}
