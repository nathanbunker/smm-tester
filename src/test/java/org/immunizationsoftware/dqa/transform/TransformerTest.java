package org.immunizationsoftware.dqa.transform;

import org.junit.Test;

import junit.framework.TestCase;

public class TransformerTest extends TestCase
{
  @Test
  public void testCreateTokenList() {
    assertEquals(0, Transformer.createTokenList("").size());
    assertEquals(1, Transformer.createTokenList("1").size());
    assertEquals(2, Transformer.createTokenList("hello goodbye").size());
    assertEquals(2, Transformer.createTokenList("hello  goodbye").size());
    assertEquals(3, Transformer.createTokenList("hello  good bye").size());
    assertEquals("hello", Transformer.createTokenList("hello  goodbye").removeFirst());
    assertEquals("hello", Transformer.createTokenList("'hello'  goodbye").removeFirst());
    assertEquals("hello goodbye", Transformer.createTokenList("'hello goodbye'").removeFirst());
    assertEquals("I'm", Transformer.createTokenList("I'm great").removeFirst());
    assertEquals("I'm great", Transformer.createTokenList("'I''m great'").removeFirst());

  }

  private static final String TEST1_ORIGINAL = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST1_FINALA = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||Le^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST1_FINALB = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||Burt&Le^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST2_ORIGINAL = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||Burt&Le^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";
  private static final String TEST2_FINAL = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||Burt&Le^Callis&Burt^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R||^&Le|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  public void testUpdateSubField() {
    testTransform("PID-5.1=Le\n", TEST1_ORIGINAL, TEST1_FINALA);
    testTransform("PID-5.1.2=Le\n", TEST1_ORIGINAL, TEST1_FINALB);
    testTransform("PV1-4.2.2=[PID-5.1.2]\nPID-5.2.2=[PID-5.1]\n", TEST2_ORIGINAL, TEST2_FINAL);
  }

  private static final String CLEAR1_ORIGINAL = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L~Burt^Callis^G^^^^L~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String CLEAR1_FINALA1 = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String CLEAR1_FINALA2 = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L~Burt^Callis^G^^^^L~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1||64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2||30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3||29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4||29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String CLEAR1_FINALB = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR|||Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String CLEAR1_FINALC1 = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||~Burt^Callis^G^^^^L~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String CLEAR1_FINALC2 = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L~~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String CLEAR1_FINALD = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
      + "PID|1||G98P76^^^OIS-TEST^MR||Burt^^G^^^^L~Burt^Callis^G^^^^L~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
      + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||G98P76.1^OIS|\r"
      + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.2^OIS|\r"
      + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  public void testClearField() {
    testTransform("clear PID\n", CLEAR1_ORIGINAL, CLEAR1_FINALA1);
    testTransform("clear OBX-2*\n", CLEAR1_ORIGINAL, CLEAR1_FINALA2);
    testTransform("clear PID-5\n", CLEAR1_ORIGINAL, CLEAR1_FINALB);
    testTransform("clear PID-5#1\n", CLEAR1_ORIGINAL, CLEAR1_FINALC1);
    testTransform("clear PID-5#2\n", CLEAR1_ORIGINAL, CLEAR1_FINALC2);
    testTransform("clear PID-5.2\n", CLEAR1_ORIGINAL, CLEAR1_FINALD);
  }

  private static final String INSERT_SEGMENT_TEST1_ORIG = "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
      + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
      + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||N90X5.1^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CNS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20120726|20120726|21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|0461325664^Banner^Kyra^Asa^^^^^CMS^L^^^NPI|||||N3783EO|20160730|MSD^Merck and Co^MVX|||CP|A|20150730072832-0600|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120726||||||F|\r"
      + "ORC|RE||N90X5.2^AIRA|\r"
      + "RXA|0|1|20130224|20130224|141^Influenza^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
      + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
      + "RXR|IM^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST1_FINAL = "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
      + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
      + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||N90X5.1^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CNS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20120726|20120726|21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|0461325664^Banner^Kyra^Asa^^^^^CMS^L^^^NPI|||||N3783EO|20160730|MSD^Merck and Co^MVX|||CP|A|20150730072832-0600|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120726||||||F|\r"
      + "ORC|RE||N90X5.2^AIRA|\r"
      + "RXA|0|1|20130224|20130224|141^Influenza^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
      + "RXR|\r"
      + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
      + "RXR|IM^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST2_ORIG = "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
      + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
      + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
      + "RXR|IM^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST2_FINAL = "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
      + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
      + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
      + "PV1|1|R|\r"
      + "ZZZ|\r"
      + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
      + "RXR|IM^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST3_ORIG = "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
      + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
      + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||N90X5.1^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CNS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20120726|20120726|21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|0461325664^Banner^Kyra^Asa^^^^^CMS^L^^^NPI|||||N3783EO|20160730|MSD^Merck and Co^MVX|||CP|A|20150730072832-0600|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120726||||||F|\r"
      + "ORC|RE||N90X5.2^AIRA|\r"
      + "RXA|0|1|20130224|20130224|141^Influenza^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
      + "OBX|\r"
      + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
      + "RXR|IM^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  
  private static final String INSERT_SEGMENT_TEST3_FINAL = "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
      + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
      + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||N90X5.1^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CNS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20120726|20120726|21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|0461325664^Banner^Kyra^Asa^^^^^CMS^L^^^NPI|||||N3783EO|20160730|MSD^Merck and Co^MVX|||CP|A|20150730072832-0600|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120726||||||F|\r"
      + "ORC|RE||N90X5.2^AIRA|\r"
      + "RXA|0|1|20130224|20130224|141^Influenza^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
      + "OBX|\r"
      + "NTE|\r"
      + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
      + "RXR|IM^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST4_ORIG = "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
      + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
      + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
      + "RXR|IM^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST4_FINAL = "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
      + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
      + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
      + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
      + "PV1|1|R|\r"
      + "IN1|\r"
      + "IN2|\r"
      + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
      + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
      + "RXR|IM^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  
  public void testInsertSegment() {
    testTransform("insert segment PID if missing\n", TEST1_ORIGINAL, TEST1_ORIGINAL);
    testTransform("insert segment RXA if missing\n", TEST1_ORIGINAL, TEST1_ORIGINAL);
    testTransform("insert segment RXA#2 if missing\n", TEST1_ORIGINAL, TEST1_ORIGINAL);
    testTransform("insert segment OBX after RXA#3 if missing\n", TEST1_ORIGINAL, TEST1_ORIGINAL);
    testTransform("insert segment RXR after RXA#2\n", INSERT_SEGMENT_TEST1_ORIG, INSERT_SEGMENT_TEST1_FINAL);
    testTransform("insert segment RXR after RXA#2 if missing\n", INSERT_SEGMENT_TEST1_ORIG, INSERT_SEGMENT_TEST1_FINAL);
    testTransform("insert segment ZZZ before ORC\n", INSERT_SEGMENT_TEST2_ORIG, INSERT_SEGMENT_TEST2_FINAL);
    testTransform("insert segment ZZZ before ORC if missing\n", INSERT_SEGMENT_TEST2_ORIG, INSERT_SEGMENT_TEST2_FINAL);
    testTransform("insert segment ZZZ before ORC if missing\n", INSERT_SEGMENT_TEST2_FINAL, INSERT_SEGMENT_TEST2_FINAL);
    testTransform("insert segment ZZZ before ORC if missing\n", INSERT_SEGMENT_TEST2_FINAL, INSERT_SEGMENT_TEST2_FINAL);
    testTransform("insert segment NTE after RXA#2:OBX if missing\n", INSERT_SEGMENT_TEST3_ORIG, INSERT_SEGMENT_TEST3_FINAL);
    testTransform("insert segment IN1,IN2 before ORC if missing\n", INSERT_SEGMENT_TEST4_ORIG, INSERT_SEGMENT_TEST4_FINAL);
    testTransform("insert segment IN1,IN2 before ORC if missing\n", INSERT_SEGMENT_TEST4_FINAL, INSERT_SEGMENT_TEST4_FINAL);
  }

  private void testTransform(String transform, String originalMessage, String finalMessage) {
    Transformer transformer = new Transformer();
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(originalMessage);
    testCaseMessage.setCustomTransformations(transform);
    transformer.transform(testCaseMessage);
    assertEquals(finalMessage, testCaseMessage.getMessageText());
  }
}
