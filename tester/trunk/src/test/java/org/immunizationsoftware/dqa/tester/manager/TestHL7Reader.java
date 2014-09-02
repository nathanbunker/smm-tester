package org.immunizationsoftware.dqa.tester.manager;

import junit.framework.TestCase;

public class TestHL7Reader extends TestCase
{
  
  public void testRandom()
  {
    long t = System.currentTimeMillis();
    int rand = (int) (t % 10);
    System.out.println("--> t    = " + t);
    System.out.println("--> rand = " + rand);
    assertTrue(rand >= 0 && rand < 10);
  }


  public void testReader() {
    HL7Reader hl7Reader = new HL7Reader(
        "MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000|P|2.5.1|\r"
            + "PID|1||MCIR3943959000^^^OIS-TEST^MR~Hi^^^^MA||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\r"
            + "PD1|\r"
            + "NK1|1|Dundy^Aldora|MTH^Mother^HL70063|\r"
            + "PV1|1|R|\r"
            + "ORC|RE||MCIR3943959000.1^OIS|\r"
            + "RXA|0|1|20111022||48^Hib^CVX|999|||01^Historical^NIP0001||||||||||||A|\r"
            + "ORC|RE||MCIR3943959000.2^OIS|\r"
            + "RXA|0|1|20111220||20^DTaP^CVX|0.5|ML||00^Administered^NIP0001||||||O4134EX||CHI^Chiron Corporation^MVX||||A|\r"
            + "RXR|IM^Intramuscular^HL70162|\r"
            + "RXA|0|1|20111220||806^DTaP^CVX|0.5|ML||00^Administered^NIP0001||||||O4134EX||CHI^Chiron Corporation^MVX||||A|\r"
            + "RXR|IM^Intramuscular^HL70162|\r");

    assertTrue(hl7Reader.advanceToSegment("PID"));
    assertEquals("Dundy", hl7Reader.getValue(5));
    assertEquals("Bennett", hl7Reader.getValue(5, 2));
    assertEquals("MA", hl7Reader.getValueRepeat(3, 5, 2));
    assertEquals("Hi", hl7Reader.getValueBySearchingRepeats(3, 1, "MA", 5));
    assertEquals(1, hl7Reader.getRepeatCount(5));
    assertEquals(2, hl7Reader.getRepeatCount(3));
    assertTrue(hl7Reader.advanceToSegment("RXA"));
    assertEquals("48", hl7Reader.getValue(5));
    assertFalse(hl7Reader.advanceToSegment("RXR", "ORC"));
    assertTrue(hl7Reader.advanceToSegment("RXA"));
    assertEquals("20", hl7Reader.getValue(5));
    assertTrue(hl7Reader.advanceToSegment("RXR", "RXA"));
    assertEquals("IM", hl7Reader.getValue(1));
    assertTrue(hl7Reader.advanceToSegment("RXA"));
    assertEquals("806", hl7Reader.getValue(5));
    assertFalse(hl7Reader.advanceToSegment("RXA"));

    hl7Reader = new HL7Reader(
        "MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000|P|2.5.1|\r"
            + "PID|1||MCIR3943959000^^^OIS-TEST^MR~^^^^MA||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\r"
            + "PD1|\r"
            + "NK1|1|Dundy^Aldora|MTH^Mother^HL70063|\r"
            + "PV1|1|R|\r"
            + "ORC|RE||MCIR3943959000.1^OIS|\r"
            + "RXA|0|1|20111022||48^Hib^CVX|999|||01^Historical^NIP0001||||||||||||A|\r"
            + "ORC|RE||MCIR3943959000.2^OIS|\r"
            + "RXA|0|1|20130816||998^No vaccine administered^CVX|999|||||||||||||||A|\r"
            + "OBX|1|CE|59784-9^Disease with presumed immunity^LN|1|38907003^Varicella infection^SCT||||||F|RXA|0|1|20111220||20^DTaP^CVX|0.5|ML||00^Administered^NIP0001||||||O4134EX||CHI^Chiron Corporation^MVX||||A|\r"
            + "RXR|IM^Intramuscular^HL70162|\r"
            + "RXA|0|1|20111220||806^DTaP^CVX|0.5|ML||00^Administered^NIP0001||||||O4134EX||CHI^Chiron Corporation^MVX||||A|\r"
            + "RXR|IM^Intramuscular^HL70162|\r");

    hl7Reader.advanceToSegment("RXA");
    assertEquals("48", hl7Reader.getValue(5));
    hl7Reader.advanceToSegment("RXR", new String[] {"OBX", "ORC", "RXA"});
    assertEquals("", hl7Reader.getValue(1));
    hl7Reader.advanceToSegment("RXA");
    assertEquals("998", hl7Reader.getValue(5));
    hl7Reader.advanceToSegment("OBX", new String[] {"ORC", "RXA"});
    assertEquals("59784-9", hl7Reader.getValue(3));
  }
}
