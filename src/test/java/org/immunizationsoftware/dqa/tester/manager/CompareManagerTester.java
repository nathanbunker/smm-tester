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

}
