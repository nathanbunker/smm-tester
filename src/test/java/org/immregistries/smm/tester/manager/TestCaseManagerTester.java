package org.immregistries.smm.tester.manager;

import org.immregistries.smm.transform.TestCaseMessage;
import org.junit.Test;
import junit.framework.TestCase;

public class TestCaseManagerTester extends TestCase {
  
  private static final String MESSAGE = "--------------------------------------------------------------------------------\n" + 
      "Test Case Number: D71T1.1ha\n" + 
      "Test Case Set: \n" + 
      "Description: \n" + 
      "Expected Result: \n" + 
      "Assert Result: Accept\n" + 
      "Original Message: \n" + 
      "Patient Type: ANY_CHILD\n" + 
      "Test Type: VXU\n" + 
      "--------------------------------------------------------------------------------\n" + 
      "MSH|^~\\&|RPMS|WPIH|||20181022153734-0400||VXU^V04^VXU_V04|D71T1.1ha|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|\n" + 
      "PID|1||D71T1^^^AIRA-TEST^MR||Riley^Sinbad^K^^^^L|Oklahoma^Endocia^^^^^M|20180422|M|||357 Leonard Ave^^Grand Rapids^MI^49550^USA^P||^PRN^PH^^^616^8924467|\n" + 
      "NK1|1|Riley^Endocia^^^^^L|MTH^Mother^HL70063|357 Leonard Ave^^Grand Rapids^MI^49550^USA^P|^PRN^PH^^^616^8924467|\n" + 
      "PV1|1|R|\n" + 
      "ORC|RE||D71T1.1^AIRA|||||||||||||||\n" + 
      "RXA|0|1|20180821||20^DTaP^CVX|999|||01^Historical^NIP001||||||||||||A|\n" + 
      "ORC|RE||D71T1.2^AIRA|||||||||||||||\n" + 
      "RXA|0|1|20180821||116^Rotavirus^CVX|999|||01^Historical^NIP001||||||||||||A|\n" + 
      "ORC|RE||D71T1.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L||||||\n" + 
      "RXA|0|1|20181022||48^Hib^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Z3814QH||PMC^sanofi pasteur^MVX||||A|\n" + 
      "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20181022|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\n" + 
      "OBX|2|CE|30956-7^Vaccine Type^LN|2|17^Hib^CVX||||||F|\n" + 
      "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|19981216||||||F|\n" + 
      "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20181022||||||F|\n";
  
  @Test
  public void testReadMessage() throws Exception
  {
    TestCaseMessage testCaseMessage =
        TestCaseMessageManager.createTestCaseMessage(MESSAGE);
    assertEquals("D71T1.1ha", testCaseMessage.getTestCaseNumber());
  }
  
}
