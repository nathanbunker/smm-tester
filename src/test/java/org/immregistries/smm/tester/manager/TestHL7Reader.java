package org.immregistries.smm.tester.manager;

import org.junit.Test;
import junit.framework.TestCase;

public class TestHL7Reader extends TestCase {

  public void testRandom() {
    long t = System.currentTimeMillis();
    int rand = (int) (t % 10);
    assertTrue(rand >= 0 && rand < 10);
  }


  public void testReader() {
    HL7Reader hl7Reader =
        new HL7Reader("MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000|P|2.5.1|\r"
            + "PID|1||MCIR3943959000^^^OIS-TEST^MR~Hi^^^^MA||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\r"
            + "PD1|\r" + "NK1|1|Dundy^Aldora|MTH^Mother^HL70063|\r" + "PV1|1|R|\r"
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

    hl7Reader =
        new HL7Reader("MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000|P|2.5.1|\r"
            + "PID|1||MCIR3943959000^^^OIS-TEST^MR~^^^^MA||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\r"
            + "PD1|\r" + "NK1|1|Dundy^Aldora|MTH^Mother^HL70063|\r" + "PV1|1|R|\r"
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

  private static final String TEST_REPLACE_ESCAPES =
      "MSH|^~\\&|NISTEHRAPP|NISTEHRFAC|NISTIISAPP|NISTIISFAC|20200524084727.655-0500||VXU^V04^VXU_V04|NIST-IZ-AD-2.1_Send_V04_Z22|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|NISTEHRFAC|NISTIISFAC\r"
          + "PID|1||90012^^^NIST-MPI-1^MR||Wong^Elise^^^^^L||19830615|F||2028-9^Asian^CDCREC|9200 Wellington Trail^^Bozeman^MT^59715^USA^P||^PRN^PH^^^406^5557896~^NET^^Elise.Wong@isp.com|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|1|||||N\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20200524|||A|19830615|20150624\r"
          + "ORC|RE|4422^NIST-AA-IZ-2|13696^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||654^Thomas^Wilma^Elizabeth^^^^^NIST-PI-1^L^^^MD|||||NISTEHRFAC^NISTEHRFacility^HL70362|\r"
          + "RXA|0|1|20200524||135^Influenza, high dose seasonal^CVX|0.5|mL^mL^UCUM||00^New Record^NIP001|7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|^^^NIST-Clinic-1||||315841|20201216|PMC^Sanofi Pasteur^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|RD^Right Deltoid^HL70163\r"
          + "OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|PHC70^Private^CDCPHINVS||||||F|||20200524\r"
          + "OBX|2|CE|64994-7^Vaccine Funding Program Eligibility^LN|2|V01^Not VFC Eligible^HL70064||||||F|||20200524|||VXC40^per immunization^CDCPHINVS\r"
          + "OBX|3|CE|69764-9^Document Type^LN|3|88^Influenza (Live, Intranasal)^cdcgs1vis||||||F|||20200524\r"
          + "OBX|4|DT|29769-7^Date Vis Presented^LN|3|20200524||||||F|||20190815\r"
          + "OBX|5|CE|TPG_PAND^Priority Group - Pandemic^99TPG|4|P2020-Flu^Pandemic 2020 - Influenza^99TPG||||||F|||20200524\r"
          + "OBX|6|CE|TPG_POP_GRP^Priority Group - Population Group^99TPG|4|PG01^Deployed \\T\\ mission essential personnel^99TPG||||||F|||20200654\r"
          + "OBX|7|CE|TPG_TIER^Priority Group - Tier^99TPG|4|T1^Tier 1^99TPG||||||F|||20200524";

  @Test
  public void testReplaceEscapes() {
    assertEquals("", HL7Reader.replaceEscapes(""));
    assertEquals("Hello", HL7Reader.replaceEscapes("Hello"));
    assertEquals("&", HL7Reader.replaceEscapes("\\T\\"));
    assertEquals(" & ", HL7Reader.replaceEscapes(" \\T\\ "));
    assertEquals(" &", HL7Reader.replaceEscapes(" \\T\\"));
    assertEquals("& ", HL7Reader.replaceEscapes("\\T\\ "));
    assertEquals("&& ", HL7Reader.replaceEscapes("\\T\\\\T\\ "));
    assertEquals("& & ", HL7Reader.replaceEscapes("\\T\\ \\T\\ "));
    assertEquals(" & & ", HL7Reader.replaceEscapes(" \\T\\ \\T\\ "));
    assertEquals("^& ", HL7Reader.replaceEscapes("\\S\\\\T\\ "));
    assertEquals("\\U\\", HL7Reader.replaceEscapes("\\U\\"));

    {
      HL7Reader hl7Reader = new HL7Reader("PID|||^\\T\\|");
      assertTrue(hl7Reader.advanceToSegment("PID"));
      assertEquals("&", hl7Reader.getValue(3, 2));
    }

    {
      HL7Reader hl7Reader = new HL7Reader(TEST_REPLACE_ESCAPES);
      boolean found = false;
      while (hl7Reader.advanceToSegment("OBX")) {
        if (hl7Reader.getValue(3).equals("TPG_POP_GRP"))
        {
          assertEquals("Deployed & mission essential personnel", hl7Reader.getValue(5, 2));
          found = true;
          break;
        }
      }
      assertTrue(found);
    }

  }
}
