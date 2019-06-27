package org.immregistries.smm.tester.manager.tximmtrac;

import org.immregistries.smm.tester.manager.query.ImmunizationUpdate;
import org.immregistries.smm.tester.manager.response.ImmunizationMessage;
import org.immregistries.smm.tester.manager.response.ResponseReader;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class TestAffirmationGeneration {


  private static String EXAMPLE =
      "MSH|^~\\&||TEST|||20111220043944||VXU^V04^VXU_V04|MCIR3943959000|P|2.5.1|\r"
          + "PID|1||MCIR3943959000^^^OIS-TEST^MR~Hi^^^^MA||Dundy^Bennett^A^^^^L|Comanche|20110614|M||2106-3^White^HL7005|177 Schoolcraft Ave^^Flint^MI^48556^USA||(810)509-9542^PRN^PH^^^810^509-9542|||||||||2186-5^not Hispanic or Latino^HL70189|\r"
          + "PD1|\r" + "NK1|1|Dundy^Aldora|MTH^Mother^HL70063|\r" + "PV1|1|R|\r"
          + "ORC|RE||MCIR3943959000.1^OIS|\r"
          + "RXA|0|1|20111022||48^Hib^CVX|999|||01^Historical^NIP0001||||||||||||A|\r"
          + "ORC|RE||MCIR3943959000.2^OIS|\r"
          + "RXA|0|1|20111220||20^DTaP^CVX|0.5|ML||00^Administered^NIP0001||||||O4134EX||CHI^Chiron Corporation^MVX||||A|\r"
          + "RXR|IM^Intramuscular^HL70162|\r"
          + "RXA|0|1|20111220||806^DTaP^CVX|0.5|ML||00^Administered^NIP0001||||||O4134EX||CHI^Chiron Corporation^MVX||||A|\r"
          + "RXR|IM^Intramuscular^HL70162|\r";

  private static String EXAMPLE_RESPONSE =
      "C           Dundy               Bennett             A                            M           20110614Aldora                                  Comanche                                                                        Y177 Schoolcraft Ave                                 Flint               MI48556       US810509-954MCIR3943959000  A OIS-TEST                 20190627TR";

  @Test
  public void test() {
    ImmunizationMessage immunizationMessage = ResponseReader.readMessage(EXAMPLE);
    assertTrue(immunizationMessage instanceof ImmunizationUpdate);
    ImmunizationUpdate immunizationUpdate = (ImmunizationUpdate) immunizationMessage;
    assertEquals(immunizationUpdate.getPatient().getNameFirst(), "Bennett");
    assertEquals(immunizationUpdate.getPatient().getMotherNameFirst(), "Aldora");
    AffirmationMessage affirmationMessage = new AffirmationMessage(immunizationUpdate.getPatient(), "OIS-TEST"); assertEquals(EXAMPLE_RESPONSE, affirmationMessage.serialize());
  }

}
