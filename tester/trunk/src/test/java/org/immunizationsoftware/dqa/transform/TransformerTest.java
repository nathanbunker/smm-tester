package org.immunizationsoftware.dqa.transform;

import org.junit.Test;

import junit.framework.TestCase;

public class TransformerTest extends TestCase {
	@Test
	public void testCreateTokenList() {
		assertEquals(0, Transformer.createTokenList("").size());
		assertEquals(1, Transformer.createTokenList("1").size());
		assertEquals(2, Transformer.createTokenList("hello goodbye").size());
		assertEquals(2, Transformer.createTokenList("hello  goodbye").size());
		assertEquals(3, Transformer.createTokenList("hello  good bye").size());
		assertEquals("hello", Transformer.createTokenList("hello  goodbye")
				.removeFirst());
		assertEquals("hello", Transformer.createTokenList("'hello'  goodbye")
				.removeFirst());
		assertEquals("hello goodbye",
				Transformer.createTokenList("'hello goodbye'").removeFirst());
		assertEquals("I'm", Transformer.createTokenList("I'm great")
				.removeFirst());
		assertEquals("I'm great", Transformer.createTokenList("'I''m great'")
				.removeFirst());

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

	private static final String TEST1_FINAL = "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
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
			+ "PV1|1|R||^&Le\r"
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
		Transformer transformer = new Transformer();
		TestCaseMessage testCaseMessage = new TestCaseMessage();
		testCaseMessage.setMessageText(TEST1_ORIGINAL);
		testCaseMessage.setCustomTransformations("PID-5.1=Le\n");
		transformer.transform(testCaseMessage);
        testCaseMessage = new TestCaseMessage();
        testCaseMessage.setMessageText(TEST1_ORIGINAL);
        testCaseMessage.setCustomTransformations("PID-5.1.2=Le\n");
        transformer.transform(testCaseMessage);
        assertEquals(TEST1_FINAL, testCaseMessage.getMessageText());
		testCaseMessage.setMessageText(TEST2_ORIGINAL);
		testCaseMessage.setCustomTransformations("PV1-4.2.2=[PID-5.1.2]\n" + "PID-5.2.2=[PID-5.1]\n");
		transformer.transform(testCaseMessage);
		assertEquals(TEST2_FINAL, testCaseMessage.getMessageText());
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


	public void testClearField()
	{
	  testTransform("clear PID\n", CLEAR1_ORIGINAL, CLEAR1_FINALA1);
      testTransform("clear OBX-2*\n", CLEAR1_ORIGINAL, CLEAR1_FINALA2);
      testTransform("clear PID-5\n", CLEAR1_ORIGINAL, CLEAR1_FINALB);
      testTransform("clear PID-5#1\n", CLEAR1_ORIGINAL, CLEAR1_FINALC1);
      testTransform("clear PID-5#2\n", CLEAR1_ORIGINAL, CLEAR1_FINALC2);
      testTransform("clear PID-5.2\n", CLEAR1_ORIGINAL, CLEAR1_FINALD);
	}


  private void testTransform(String transform, String originalMessage, String finalMessage) {
    Transformer transformer = new Transformer();
      TestCaseMessage testCaseMessage = new TestCaseMessage();
      testCaseMessage.setMessageText(originalMessage);
      testCaseMessage.setCustomTransformations(transform);
      transformer.transform(testCaseMessage);
      assertEquals(finalMessage, testCaseMessage.getMessageText());
  }
}
