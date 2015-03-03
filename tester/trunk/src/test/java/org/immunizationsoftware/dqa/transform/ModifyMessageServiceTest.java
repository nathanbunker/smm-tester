package org.immunizationsoftware.dqa.transform;

import static org.junit.Assert.*;

import org.junit.Test;

public class ModifyMessageServiceTest
{

  private static final String TEST1_ORIGINAL = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r" +
"PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r" +
"NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" +
"PV1|1|R|\r" +
"ORC|RE||G98P76.1^OIS|\r" +
"RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r" +
"ORC|RE||G98P76.2^OIS|\r" +
"RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r" +
"ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r" +
"RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r" +
"OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r" +
"OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r" +
"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r" +
"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";
  
  private static final String TEST1_FINAL = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r" +
"PID|1||G98P76^^^OIS-TEST^MR||Burt^Hello^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r" +
"NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" +
"PV1|1|R|\r" +
"ORC|RE||G98P76.1^OIS|\r" +
"RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r" +
"ORC|RE||G98P76.2^OIS|\r" +
"RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r" +
"ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r" +
"RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r" +
"OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r" +
"OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r" +
"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r" +
"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";
  
  @Test
  public void test() {
    ModifyMessageService modifyMessageService = new ModifyMessageService();
    ModifyMessageRequest modifyMessageRequest = new ModifyMessageRequest();
    modifyMessageRequest.setMessageOriginal(TEST1_ORIGINAL);
    modifyMessageRequest.setTransformScript("PID-5.2=Hello");
    modifyMessageService.transform(modifyMessageRequest);
    assertEquals(TEST1_FINAL, modifyMessageRequest.getMessageFinal());
  }

}
