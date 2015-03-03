package org.immunizationsoftware.dqa.tester;

import static org.junit.Assert.*;

import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.transform.Transformer;
import org.junit.Test;

public class TestTransformer
{

  @Test
  public void testTransformInsertSegments() throws Exception {
    Transformer transformer = new Transformer();
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_POST, "Test", "");
    String messageText = "";

    messageText = "MSH|\r";
    connector.setCustomTransformations("insert segment FHS first\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("FHS|^~\\&|\rMSH|\r", messageText);

    messageText = "MSH|";
    connector.setCustomTransformations("insert segment FHS first\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("FHS|^~\\&|\rMSH|\r", messageText);

    messageText = "MSH|\r";
    connector.setCustomTransformations("insert segment FTS last\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rFTS|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|\r";
    connector.setCustomTransformations("insert segment PD1 after PID\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rPD1|\rNK1|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|\rRXA|\rRXR|\rRXA|\rRXR|\rRXA|\rRXR|\r";
    connector.setCustomTransformations("insert segment OBX after RXR#2\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|\rRXA|\rRXR|\rRXA|\rRXR|\rOBX|\rRXA|\rRXR|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|\rRXA|\rRXR|\rRXA|\rRXR|\rRXA|\rRXR|\r";
    connector.setCustomTransformations("insert segment ORC before RXA#2\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|\rRXA|\rRXR|\rORC|\rRXA|\rRXR|\rRXA|\rRXR|\r", messageText);

  }

  @Test
  public void testTransformRemoveSegments() throws Exception {
    Transformer transformer = new Transformer();
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_POST, "Test", "");
    String messageText = "";

    messageText = "MSH|\rPID|\rNK1|\r";
    connector.setCustomTransformations("remove segment PID\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rNK1|\r", messageText);

    messageText = "MSH|\rPID|\rPID|\rNK1|\rPID|\r";
    connector.setCustomTransformations("remove segment PID#3\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rPID|\rNK1|\r", messageText);

    messageText = "MSH|\rPID|\rPID|\rNK1|\rPID|\r";
    connector.setCustomTransformations("remove segment PID all\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rNK1|\r", messageText);
  }

  private static final String CLEAN_TEST_BEFORE = "MSH|^~\\&|||||20130823111809||VXU^V04^VXU_V04|X94P18|P|2.5.1|\r"
      + "PID|1||X94P18^^^OIS-TEST^MR||Court^Nye^^^^^L|Brazos^Maia|20130224|M|||321 Dundy St^^Haslett^MI^48840^USA^P||^PRN^PH^^^517^5489090|\r"
      + "NK1|1|Court^Maia|MTH^Mother^HL70063|\r";

  @Test
  public void testClean() throws Exception {
    Transformer transformer = new Transformer();
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_POST, "Test", "");
    String messageText = "";

    messageText = "MSH|\rPID|\rNK1||||\r";
    connector.setCustomTransformations("clean\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|^|~~~||\r";
    connector.setCustomTransformations("clean\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|^|~~Hi~||\r";
    connector.setCustomTransformations("clean\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1||~~Hi|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|^|203999~||\r";
    connector.setCustomTransformations("clean\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1||203999|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|^|203999~~9||\r";
    connector.setCustomTransformations("clean\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1||203999~~9|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|^|203999^^9||\r";
    connector.setCustomTransformations("clean\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1||203999^^9|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|^|203999^^9^||\r";
    connector.setCustomTransformations("clean\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1||203999^^9|\r", messageText);

    messageText = "MSH|\rPID|\rNK1|^|203999^^9~||\r";
    connector.setCustomTransformations("clean\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1||203999^^9|\r", messageText);

    connector.setCustomTransformations("clean\n");
    messageText = transformer.transform(connector, CLEAN_TEST_BEFORE);
    assertEquals(CLEAN_TEST_BEFORE, messageText);

  }

  @Test
  public void testFix() throws Exception {
    Transformer transformer = new Transformer();
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_POST, "Test", "");
    String messageText = "";

    messageText = "MSH|^~\\&|RPMS|91116||NIST TEST IZ REG|20130730091012|ASD123|VXU^V04^VXU_V04|744705554.7469|P|2.5.1|||AL|AL|\r"
        + "PID|1||I15J12^^^DEMO HOSP^MR~950831309^^^SSA^SS~441904393^^^MCD^MA||Lincoln^Geraldine^^^^^L^^^^^|Johnsson^Atin^^^^|19410508|F||1002-5^AMERICAN INDIAN OR ALASKA NATIVE^HL70005|311 Cherokee Cir^^ANYCITY^NC^28719^USA^L||^PRN^PH^^^906^4869809~^NET^^ottoline@hotmail.com|^PRN^PH^^^555^9993616||||||||2186-5^NOT HISPANIC OR LATINO^CDCREC||N\r"
        + "PD1|||||||||||02^REMINDER/RECALL - ANY METHOD^HL70215|N|20070910|||A|20070910|20070910\r"
        + "NK1|1|Johnsson^Atin^^^^^L|MTH^MOTHER^HL70063|^^^^^USA^L|||NOK^NEXT OF KIN^99IHS\r"
        + "PV1|1|R|||||AB2001434^BEATTY,CINDY||AL2025662^LUCKETT,RANDALL|AMB|||||||||3245421|V01^20091120||||||||||||||||||||||||20091120\r"
        + "ORC|RE|157598-232101^IHS|20120814-157598-232101000000^DEMO HOSP||IP|||||I-23432^BURDEN^DONNA^A^^^^^IHS^L||57422^RADON^NICHOLAS^^^^^^IHS^L|||||^^^DEMO HOSP\r"
        + "RXA|0|1|20120814|20120814|52^HEP A, ADULT^CVX|1|mL^MilliLiters^UCUM||00^NEW IMMUNIZATION RECORD^NIP001|7832-1^LEMON^MIKE^A^^^^^IHS^L|^^^XYZ123||||I90FV|20121214|MSD^MERCK & CO.^MVX|||CP|A|20091103\r"
        + "RXR|IM^INTRAMUSCULAR^HL70162|RA^Right Arm^HL70163\r"
        + "OBX|1|CE|64994-7^funding eligibility for immunization^LN|1|V01^Not Eligible^HL70064||||||F|||20091103|||VXC40^per immunization^CDCPHINVS\r"
        + "OBX|2|CE|30956-7^vaccine type^LN|2|85^HEP A, NOS^CVX||||||F\r"
        + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F\r"
        + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";
    connector.setCustomTransformations("fix ampersand\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals(
        "MSH|^~\\&|RPMS|91116||NIST TEST IZ REG|20130730091012|ASD123|VXU^V04^VXU_V04|744705554.7469|P|2.5.1|||AL|AL|\r"
            + "PID|1||I15J12^^^DEMO HOSP^MR~950831309^^^SSA^SS~441904393^^^MCD^MA||Lincoln^Geraldine^^^^^L^^^^^|Johnsson^Atin^^^^|19410508|F||1002-5^AMERICAN INDIAN OR ALASKA NATIVE^HL70005|311 Cherokee Cir^^ANYCITY^NC^28719^USA^L||^PRN^PH^^^906^4869809~^NET^^ottoline@hotmail.com|^PRN^PH^^^555^9993616||||||||2186-5^NOT HISPANIC OR LATINO^CDCREC||N\r"
            + "PD1|||||||||||02^REMINDER/RECALL - ANY METHOD^HL70215|N|20070910|||A|20070910|20070910\r"
            + "NK1|1|Johnsson^Atin^^^^^L|MTH^MOTHER^HL70063|^^^^^USA^L|||NOK^NEXT OF KIN^99IHS\r"
            + "PV1|1|R|||||AB2001434^BEATTY,CINDY||AL2025662^LUCKETT,RANDALL|AMB|||||||||3245421|V01^20091120||||||||||||||||||||||||20091120\r"
            + "ORC|RE|157598-232101^IHS|20120814-157598-232101000000^DEMO HOSP||IP|||||I-23432^BURDEN^DONNA^A^^^^^IHS^L||57422^RADON^NICHOLAS^^^^^^IHS^L|||||^^^DEMO HOSP\r"
            + "RXA|0|1|20120814|20120814|52^HEP A, ADULT^CVX|1|mL^MilliLiters^UCUM||00^NEW IMMUNIZATION RECORD^NIP001|7832-1^LEMON^MIKE^A^^^^^IHS^L|^^^XYZ123||||I90FV|20121214|MSD^MERCK \\T\\ CO.^MVX|||CP|A|20091103\r"
            + "RXR|IM^INTRAMUSCULAR^HL70162|RA^Right Arm^HL70163\r"
            + "OBX|1|CE|64994-7^funding eligibility for immunization^LN|1|V01^Not Eligible^HL70064||||||F|||20091103|||VXC40^per immunization^CDCPHINVS\r"
            + "OBX|2|CE|30956-7^vaccine type^LN|2|85^HEP A, NOS^CVX||||||F\r"
            + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F\r"
            + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r", messageText);

  }

  @Test
  public void testMap() throws Exception {
    Transformer transformer = new Transformer();
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_POST, "Test", "");
    String messageText = "";

    messageText = "MSH|\rPID|\rNK1|Hi|||\r";
    connector.setCustomTransformations("NK1-1=[MAP 'Hi'=>'Bye', 'Joe'=>'Sam', default=>'']\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Bye|||\r", messageText);

    messageText = "MSH|\rPID|\rNK1|Howdy|||\r";
    connector.setCustomTransformations("NK1-1=[MAP 'Hi'=>'Bye', 'Joe'=>'Sam']\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Howdy|||\r", messageText);

  }

  @Test
  public void testSetRepeats() throws Exception {
    Transformer transformer = new Transformer();
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_POST, "Test", "");
    String messageText = "";

    messageText = "MSH|\rPID|\rNK1|Hi|||\r";
    connector.setCustomTransformations("NK1-1#2=Bye\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Hi~Bye|||\r", messageText);

    messageText = "MSH|\rPID|\rNK1|Hi~|||\r";
    connector.setCustomTransformations("NK1-1#2=Bye\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Hi~Bye|||\r", messageText);

    messageText = "MSH|\rPID|\rNK1|Hi~~Welcome|||\r";
    connector.setCustomTransformations("NK1-1#2=Bye\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Hi~Bye~Welcome|||\r", messageText);

    messageText = "MSH|\rPID|\rNK1|~~Welcome|||\r";
    connector.setCustomTransformations("NK1-1#2=Bye\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|~Bye~Welcome|||\r", messageText);

    messageText = "MSH|\rPID|\rNK1|~~Welcome|||\r";
    connector.setCustomTransformations("NK1-1#1=Bye\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Bye~~Welcome|||\r", messageText);

    messageText = "MSH|\rPID|\rNK1||||\r";
    connector.setCustomTransformations("NK1-1#1=Bye\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Bye|||\r", messageText);

    messageText = "MSH|\rPID|\rNK1|Hi~Hi|||\r";
    connector.setCustomTransformations("NK1-1#2=Bye\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Hi~Bye|||\r", messageText);

    messageText = "MSH|\rPID|\rNK1|Hi~Hi~|||\r";
    connector.setCustomTransformations("NK1-1#2=Bye\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Hi~Bye~|||\r", messageText);

    messageText = "MSH|\rPID|\rNK1|Hi~Hi~Welcome~|||\r";
    connector.setCustomTransformations("NK1-1#2=Bye\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals("MSH|\rPID|\rNK1|Hi~Bye~Welcome~|||\r", messageText);

  }

  private static final String TEST_NMSIIS_1 = "MSH|^~\\&||718||NMSIIS|20120731085717||VXU^V04^VXU_V04|CI809903|P|2.5.1|||AL|AL|\r"
      + "PID|1||232101152165^^^CHINLE HOSP^MR~258723162^^^^SS~646047356^^^^MA||LOSSIAH^JENNY^FAY^^^|SPARKS^DORIS^GORE^^^|19970501|F||1002-5^AMERICAN INDIAN OR ALASKA NATIVE^HL70005|238 GOOSECREEK RD^^ANYCITY^NC^28719^^P||^PRN^PH^^^555^555186|^WPN^PH^^^^||||||||2186-5^NOT HISPANIC OR LATINO^HL70006||N\r"
      + "PD1|||232101^CHINLE HOSP||||||||||20120720|||A\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "PV1|1|R|CHINLE HOSP^|||||||EVE|||||||||3294622|V04^20100226~||||||||||||||||||||||||20100226120000-0500\r"
      + "ORC|RE|49340-232101|19980302-49340-232101152165^CHINLE HOSP||IP|||||1234567890^HORNBUCKLE^CLAUDE^ALBEE^^^||1234567890^HORNBUCKLE^CLAUDE^ALBEE^^^|||||^^^CHINLE HOSP\r"
      + "RXA|0|1|19980302091900|19980302091900|10^IPV^CVX|.5|ML^ML^ISO+||00^NEW IMMUNIZATION RECORD^NIP001|1234567890^HORNBUCKLE^CLAUDE|^^^CHINLE HOSP||||A1060-2|20091026|PMC^SANOFI PASTEUR^MVX|||CP|A|19980302091900\r";

  private static final String TEST_NMSIIS_1_RESULT = "FHS|^~\\&|RPMS|IHS2||NMSIIS|20120731085717|CR|HL7.251.batch.txt||CI809903|\r"
      + "BHS|^~\\&|RPMS|IHS2||NMSIIS|20120731085717||||CI809903|\r"
      + "MSH|^~\\&|RPMS|||NMSIIS|20120731085717||VXU^V04^VXU_V04|CI809903|P|2.5.1|||AL|AL|\r"
      + "PID|1||232101152165^^^CHINLE HOSP^MR~258723162^^^^SS~646047356^^^^MA||LOSSIAH^JENNY^FAY^^^|SPARKS^DORIS^GORE^^^|19970501|F||1002-5^AMERICAN INDIAN OR ALASKA NATIVE^HL70005|238 GOOSECREEK RD^^ANYCITY^NC^28719^^P||^PRN^PH^^^555^555186|^WPN^PH^^^^||||||||2186-5^NOT HISPANIC OR LATINO^HL70006||N\r"
      + "PD1|||232101^CHINLE HOSP||||||||||20120720|||A\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "PV1|1|R|CHINLE HOSP^|||||||EVE|||||||||3294622|V04^20100226~||||||||||||||||||||||||20100226120000-0500\r"
      + "IN1|1|\r"
      + "IN2|\r"
      + "ORC|RE|49340-232101|19980302-49340-232101152165^CHINLE HOSP||IP|||||1234567890^HORNBUCKLE^CLAUDE^ALBEE^^^||1234567890^HORNBUCKLE^CLAUDE^ALBEE^^^|||||^^^CHINLE HOSP\r"
      + "RXA|0|1|19980302091900|19980302091900|10^IPV^CVX|.5|ML^ML^ISO+||00^NEW IMMUNIZATION RECORD^NIP001|1234567890^HORNBUCKLE^CLAUDE|^^^CHINLE HOSP||||A1060-2|20091026|PMC^SANOFI PASTEUR^MVX|||CP|A|19980302091900\r"
      + "BTS|1|CR|\r" + "FTS|1|CR|\r";

  private static final String TEST_NMSIIS_CHILD_1 = "MSH|^~\\&|||||20141205054808-0700||VXU^V04^VXU_V04|6882330-A1.2.8238|P|2.5.1|\r"
      + "PID|1||6882330-A1.2^^^OIS-TEST^MR||Durbin^Payge^Stefanie^^^^L|SMITH^KRISTI|20071202|F||2106-3^White^HL70005|18 Isdal Ln^^Casco Township^MI^48064^USA^P||^PRN^PH^^^810^4927434|||||||||2186-5^not Hispanic or Latino^CDCREC|\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "ORC|RE||N89X3.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20141205||52^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y11VA||SKB^GlaxoSmithKline^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20141205|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20141205||||||F|\r";

  private static final String TEST_NMSIIS_CHILD_1_RESULT = "FHS|^~\\&|RPMS|IHS2||NMSIIS|20141205054808-0700|CR|HL7.251.batch.txt||6882330-A1.2.8238|\r"
      + "BHS|^~\\&|RPMS|IHS2||NMSIIS|20141205054808-0700||||6882330-A1.2.8238|\r"
      + "MSH|^~\\&|RPMS|||NMSIIS|20141205054808-0700||VXU^V04^VXU_V04|6882330-A1.2.8238|P|2.5.1|\r"
      + "PID|1||6882330-A1.2^^^OIS-TEST^MR||Durbin^Payge^Stefanie^^^^L|SMITH^KRISTI|20071202|F||2106-3^White^HL70005|18 Isdal Ln^^Casco Township^MI^48064^USA^P||^PRN^PH^^^810^4927434|||||||||2186-5^not Hispanic or Latino^CDCREC|\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "IN1|1|\r"
      + "IN2|\r"
      + "ORC|RE||N89X3.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20141205||52^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y11VA||SKB^GlaxoSmithKline^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20141205|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20141205||||||F|\r"
      + "BTS|1|CR|\r"
      + "FTS|1|CR|\r";

  private static final String TEST_NMSIIS_CHILD_2 = "MSH|^~\\&|||||20141205054808-0700||VXU^V04^VXU_V04|6882330-A1.2.8238|P|2.5.1|\r"
      + "PID|1||6882330-A1.2^^^OIS-TEST^MR||Durbin^Payge^Stefanie^^^^L|SMITH^KRISTI^STOWE|20071202|F||2106-3^White^HL70005|18 Isdal Ln^^Casco Township^MI^48064^USA^P||^PRN^PH^^^810^4927434|||||||||2186-5^not Hispanic or Latino^CDCREC|\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "ORC|RE||N89X3.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20141205||52^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y11VA||SKB^GlaxoSmithKline^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20141205|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20141205||||||F|\r";

  private static final String TEST_NMSIIS_CHILD_2_RESULT = "FHS|^~\\&|RPMS|IHS2||NMSIIS|20141205054808-0700|CR|HL7.251.batch.txt||6882330-A1.2.8238|\r"
      + "BHS|^~\\&|RPMS|IHS2||NMSIIS|20141205054808-0700||||6882330-A1.2.8238|\r"
      + "MSH|^~\\&|RPMS|||NMSIIS|20141205054808-0700||VXU^V04^VXU_V04|6882330-A1.2.8238|P|2.5.1|\r"
      + "PID|1||6882330-A1.2^^^OIS-TEST^MR||Durbin^Payge^Stefanie^^^^L|SMITH^KRISTI^STOWE|20071202|F||2106-3^White^HL70005|18 Isdal Ln^^Casco Township^MI^48064^USA^P||^PRN^PH^^^810^4927434|||||||||2186-5^not Hispanic or Latino^CDCREC|\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "IN1|1|\r"
      + "IN2|\r"
      + "ORC|RE||N89X3.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20141205||52^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y11VA||SKB^GlaxoSmithKline^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20141205|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20141205||||||F|\r"
      + "BTS|1|CR|\r"
      + "FTS|1|CR|\r";

  private static final String TEST_NMSIIS_CHILD_3 = "MSH|^~\\&|TEST EHR APPLICATION|X68||NIST TEST IZ REG|20130730091054|ASD123|VXU^V04^VXU_V04|-1140235900.5074|P|2.5.1|||AL|AL|\r"
      + "PID|1||M28U38^^^DEMO HOSP^MR~720808646^^^SSA^SS||Clinton^Annamaria^^^^^L^^^^^|Dundy^Alex^STOWE^^^|20010615|F||1002-5^AMERICAN INDIAN OR ALASKA NATIVE^HL70005|44 Swanwick St^^ANYCITY^NC^28719^USA^L||^PRN^PH^^^989^8451877|^PRN^PH^^^555^9992224||||||||2186-5^NOT HISPANIC OR LATINO^CDCREC||N\r"
      + "PD1|||||||||||02^REMINDER/RECALL - ANY METHOD^HL70215|N|20071105|||A|20071105|20071105\r"
      + "NK1|1|Clinton^Alex^CHESBIE^^^^L|MTH^MOTHER^HL70063|^^^^^USA^L|||NOK^NEXT OF KIN^99IHS\r"
      + "PV1|1|R|||||AB2001434^BEATTY,CINDY||AG2016752^GOVERNO,NANCY R|AMB|||||||||3252667|V01^20100115||||||||||||||||||||||||20100115\r"
      + "ORC|RE||9999^CDC\r"
      + "RXA|0|1|20110215|20110215|998^No vaccine administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|59784-9^Disease with presumed immunity ^LN|1|38907003^Varicella infection^SCT||||||F\r";
  private static final String TEST_NMSIIS_CHILD_3_RESULT = "FHS|^~\\&|RPMS|IHS2||NMSIIS|20130730091054|CR|HL7.251.batch.txt||-1140235900.5074|\r"
      + "BHS|^~\\&|RPMS|IHS2||NMSIIS|20130730091054||||-1140235900.5074|\r"
      + "MSH|^~\\&|RPMS|||NMSIIS|20130730091054|ASD123|VXU^V04^VXU_V04|-1140235900.5074|P|2.5.1|||AL|AL|\r"
      + "PID|1||M28U38^^^DEMO HOSP^MR~720808646^^^SSA^SS||Clinton^Annamaria^^^^^L^^^^^|Dundy^Alex^STOWE^^^|20010615|F||1002-5^AMERICAN INDIAN OR ALASKA NATIVE^HL70005|44 Swanwick St^^ANYCITY^NC^28719^USA^L||^PRN^PH^^^989^8451877|^PRN^PH^^^555^9992224||||||||2186-5^NOT HISPANIC OR LATINO^CDCREC||N\r"
      + "PD1|||||||||||02^REMINDER/RECALL - ANY METHOD^HL70215|N|20071105|||A|20071105|20071105\r"
      + "NK1|1|Clinton^Alex^CHESBIE^^^^L|MTH^MOTHER^HL70063|^^^^^USA^L|||NOK^NEXT OF KIN^99IHS\r"
      + "PV1|1|R|||||AB2001434^BEATTY,CINDY||AG2016752^GOVERNO,NANCY R|AMB|||||||||3252667|V01^20100115||||||||||||||||||||||||20100115\r"
      + "IN1|1|\r"
      + "IN2|\r"
      + "ORC|RE||9999^CDC\r"
      + "RXA|0|1|20110215|20110215|998^No vaccine administered^CVX|999||||||||||||||NA\r"
      + "OBX|1|CE|59784-9^Disease with presumed immunity ^LN|1|38907003^Varicella infection^SCT||||||F\r"
      + "BTS|1|CR|\r" + "FTS|1|CR|\r";

  private static final String TEST_NMSIIS_ADULT_1 = "MSH|^~\\&|||||20141205054808-0700||VXU^V04^VXU_V04|6882330-A1.2.8238|P|2.5.1|\r"
      + "PID|1||6882330-A1.2^^^OIS-TEST^MR||Durbin^Payge^Stefanie^^^^L|SMITH^|19471202|F||2106-3^White^HL70005|18 Isdal Ln^^Casco Township^MI^48064^USA^P||^PRN^PH^^^810^4927434|||||||||2186-5^not Hispanic or Latino^CDCREC|\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "ORC|RE||N89X3.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20141205||52^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y11VA||SKB^GlaxoSmithKline^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20141205|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20141205||||||F|\r";

  private static final String TEST_NMSIIS_ADULT_1_RESULT = "FHS|^~\\&|RPMS|IHS2||NMSIIS|20141205054808-0700|CR|HL7.251.batch.txt||6882330-A1.2.8238|\r"
      + "BHS|^~\\&|RPMS|IHS2||NMSIIS|20141205054808-0700||||6882330-A1.2.8238|\r"
      + "MSH|^~\\&|RPMS|||NMSIIS|20141205054808-0700||VXU^V04^VXU_V04|6882330-A1.2.8238|P|2.5.1|\r"
      + "PID|1||6882330-A1.2^^^OIS-TEST^MR||Durbin^Payge^Stefanie^^^^L|SMITH^KRISTI|19471202|F||2106-3^White^HL70005|18 Isdal Ln^^Casco Township^MI^48064^USA^P||^PRN^PH^^^810^4927434|||||||||2186-5^not Hispanic or Latino^CDCREC|\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "IN1|1|\r"
      + "IN2|\r"
      + "ORC|RE||N89X3.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20141205||52^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y11VA||SKB^GlaxoSmithKline^MVX||||A|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20141205||||||F|\r"
      + "BTS|1|CR|\r"
      + "FTS|1|CR|\r";

  private static final String TEST_NMSIIS_ADULT_2 = "MSH|^~\\&|||||20141205054808-0700||VXU^V04^VXU_V04|6882330-A1.2.8238|P|2.5.1|\r"
      + "PID|1||6882330-A1.2^^^OIS-TEST^MR||Durbin^Payge^Stefanie^^^^L|SMITH^|19471202|F||2106-3^White^HL70005|18 Isdal Ln^^Casco Township^MI^48064^USA^P||^PRN^PH^^^810^4927434|||||||||2186-5^not Hispanic or Latino^CDCREC|\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "IN1|1|\r"
      + "IN2|\r"
      + "ORC|RE||N89X3.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20141205||52^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y11VA||SKB^GlaxoSmithKline^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20141205|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20141205||||||F|\r";

  private static final String TEST_NMSIIS_ADULT_2_RESULT = "FHS|^~\\&|RPMS|IHS2||NMSIIS|20141205054808-0700|CR|HL7.251.batch.txt||6882330-A1.2.8238|\r"
      + "BHS|^~\\&|RPMS|IHS2||NMSIIS|20141205054808-0700||||6882330-A1.2.8238|\r"
      + "MSH|^~\\&|RPMS|||NMSIIS|20141205054808-0700||VXU^V04^VXU_V04|6882330-A1.2.8238|P|2.5.1|\r"
      + "PID|1||6882330-A1.2^^^OIS-TEST^MR||Durbin^Payge^Stefanie^^^^L|SMITH^KRISTI|19471202|F||2106-3^White^HL70005|18 Isdal Ln^^Casco Township^MI^48064^USA^P||^PRN^PH^^^810^4927434|||||||||2186-5^not Hispanic or Latino^CDCREC|\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "IN1|1|\r"
      + "IN2|\r"
      + "ORC|RE||N89X3.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20141205||52^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Y11VA||SKB^GlaxoSmithKline^MVX||||A|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20141205||||||F|\r"
      + "BTS|1|CR|\r"
      + "FTS|1|CR|\r";

  private static final String TEST_ASIIS_1 = "MSH|^~\\&|||||20130820070412||VXU^V04^VXU_V04|Q43B1|P|2.5.1|\r"
      + "PID|1||Q43B1^^^OIS-TEST^MR||Richardson^Carmelo^F^^^^L|Lamar^Galatea|20130216|M|||1 Eastland Pl^^Portage^MI^49081^USA^P||^PRN^PH^^^269^7293157|\r"
      + "NK1|1|Lamar^Galatea|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||Q43B1.1^OIS|\r"
      + "RXA|0|1|20130416||20^DTaP^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||Q43B1.2^OIS|\r"
      + "RXA|0|1|20130619||133^PCV 13^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||Q43B1.3^OIS|\r"
      + "RXA|0|1|20130820||20^DTaP^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||O4134EX||PMC^sanofi pasteur^MVX||||A|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V03^VFC eligible - Uninsured^HL70064||||||F|||20130820|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|20^DTaP^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20070517||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20130820||||||F|\r";

  private static final String TEST_ASIIS_1_RESULT = "MSH|^~\\&|RPMS||||20130820070412||VXU^V04^VXU_V04|Q43B1|P|2.5.1|\r"
      + "PID|1||Q43B1^^^OIS-TEST^MR||Richardson^Carmelo^F^^^^L|Lamar^Galatea|20130216|M|||1 Eastland Pl^^Portage^MI^49081^USA^P||^PRN^PH^^^269^7293157|\r"
      + "NK1|1|Lamar^Galatea|MTH^Mother^HL70063|\r"
      + "PV1|1|R|\r"
      + "ORC|RE||Q43B1.1^OIS|\r"
      + "RXA|0|1|20130416|20130416|20^DTaP^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||Q43B1.2^OIS|\r"
      + "RXA|0|1|20130619|20130619|133^PCV 13^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||Q43B1.3^OIS|\r"
      + "RXA|0|1|20130820|20130820|20^DTaP^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||O4134EX||PMC^sanofi pasteur^MVX||||A|\r";

  private static final String TEST_NMSIIS_PROBLEM_1 = "MSH|^~\\&|RPMS|2011 DEMO HOSPITAL|||20140721165517||VXU^V04^VXU_V04|IHS-3908655|P|2.5.1|||AL|AL|\r"
      + "PID|1||232101263762^^^2011 DEMO HOSPITAL^MR||SNOW^MADELYNN^AINSLEY^^^^L^^^^^|LAM^^^^^|20070706|F||2131-1^OTHER RACE^HL70005|32 PRESCOTT STREET AVE^^WARWICK^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||^UNKNOWN^CDCREC||N\r"
      + "PD1|||||||||||02^REMINDER/RECALL - ANY METHOD^HL70215|||||A|20140715|20140715\r"
      + "NK1|1|^^^^^^L|GRD^GUARDIAN^HL70063|^^^^^USA^L|||NOK^NEXT OF KIN^99IHS\r"
      + "PV1|1|R|||||||||||||||||3733037|V04^20120514||||||||||||||||||||||||20120514\r"
      + "ORC|RE|183892-232101^IHS|20110601-183892-232101263762^2011 DEMO HOSPITAL||IP|||||^TOWN^CECILE^^^^^^^L|||||||^^^2011 DEMO HOSPITAL\r"
      + "RXA|0|1|20110601|20110601|141^INFLUENZA [TIV], SEASONAL, INJ^CVX|999|mL^MilliLiters^UCUM||01^HISTORICAL INFORMATION - SOURCE UNSPECIFIED^NIP001||^^^2011 DEMO||||U3562CA|20110630|PMC^SANOFI PASTEUR^MVX|||CP|A|20110601\r"
      + "RXR|IM^INTRADERMAL^HL70162|LI^Left Lower Forearm^HL70163\r"
      + "OBX|1|CE|64994-7^funding eligibility for immunization^LN|1|V04^Am Indian/AK Native^HL70064||||||F|||20110601|||VXC40^per immunization^CDCPHINVS\r"
      + "OBX|2|CE|30956-7^vaccine type^LN|2|88^INFLUENZA, NOS^CVX||||||F\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20110601||||||F\r";
  
  private static final String TEST_NMSIIS_PROBLEM_1_RESULT = "FHS|^~\\&|RPMS|IHS2||NMSIIS|20140721165517|CR|HL7.251.batch.txt||IHS-3908655|\r" +
"BHS|^~\\&|RPMS|IHS2||NMSIIS|20140721165517||||IHS-3908655|\r" +
"MSH|^~\\&|RPMS|||NMSIIS|20140721165517||VXU^V04^VXU_V04|IHS-3908655|P|2.5.1|||AL|AL|\r" +
"PID|1||232101263762^^^2011 DEMO HOSPITAL^MR||SNOW^MADELYNN^AINSLEY^^^^L^^^^^|^^^^^|20070706|F||2131-1^OTHER RACE^HL70005|32 PRESCOTT STREET AVE^^WARWICK^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||^UNKNOWN^CDCREC||N\r" +
"PD1|||||||||||02^REMINDER/RECALL - ANY METHOD^HL70215|||||A|20140715|20140715\r" +
"NK1|1|^^^^^^L|GRD^GUARDIAN^HL70063|^^^^^USA^L|||NOK^NEXT OF KIN^99IHS\r" +
"PV1|1|R|||||||||||||||||3733037|V04^20120514||||||||||||||||||||||||20120514\r" +
"IN1|1|\r" +
"IN2|\r" +
"ORC|RE|183892-232101^IHS|20110601-183892-232101263762^2011 DEMO HOSPITAL||IP|||||^TOWN^CECILE^^^^^^^L|||||||^^^2011 DEMO HOSPITAL\r" +
"RXA|0|1|20110601|20110601|141^INFLUENZA [TIV], SEASONAL, INJ^CVX|999|mL^MilliLiters^UCUM||01^HISTORICAL INFORMATION - SOURCE UNSPECIFIED^NIP001||^^^2011 DEMO||||U3562CA|20110630|PMC^SANOFI PASTEUR^MVX|||CP|A|20110601\r" +
"RXR|IM^INTRADERMAL^HL70162|LI^Left Lower Forearm^HL70163\r" +
"OBX|1|CE|64994-7^funding eligibility for immunization^LN|1|V04^Am Indian/AK Native^HL70064||||||F|||20110601|||VXC40^per immunization^CDCPHINVS\r" +
"OBX|2|CE|30956-7^vaccine type^LN|2|88^INFLUENZA, NOS^CVX||||||F\r" +
"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r" +
"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20110601||||||F\r" +
"BTS|1|CR|\r" +
"FTS|1|CR|\r";

  @Test
  public void testNMSIIS() throws Exception {
    Transformer transformer = new Transformer();
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_POST, "Test", "");
    String messageText = "";

    connector.setCustomTransformations("MSH-3=RPMS\n" + "MSH-4=[OTHERID]\n" + "MSH-6=NMSIIS\n"
        + "insert segment BHS first\n" + "insert segment BTS last\n" + "insert segment FHS first\n"
        + "insert segment FTS last\n" + "FHS-8=CR\n" + "BSH-8=CR\n" + "FHS-9=[FILENAME]\n" + "FTS-1=1\n" + "BTS-1=1\n"
        + "FTS-2=CR\n" + "BTS-2=CR\n" + "FHS-4=[USERID]\n" + "BHS-4=[USERID]\n"
        + "insert segment IN1 before ORC if missing\n" + "insert segment IN2 after IN1 if missing\n" + "IN1-1=1\n"
        + "fix missing mother maiden first\n" + "remove observation 64994-7 if 18+\n");
    connector.setCurrentFilename("HL7.251.batch.txt");
    connector.setCurrentControlId("CI809903");
    connector.setUserid("IHS2");

    messageText = transformer.transform(connector, TEST_NMSIIS_1);
    assertEquals(TEST_NMSIIS_1_RESULT, messageText);

    messageText = transformer.transform(connector, TEST_NMSIIS_CHILD_1);
    assertEquals(TEST_NMSIIS_CHILD_1_RESULT, messageText);

    messageText = transformer.transform(connector, TEST_NMSIIS_CHILD_2);
    assertEquals(TEST_NMSIIS_CHILD_2_RESULT, messageText);

    messageText = transformer.transform(connector, TEST_NMSIIS_CHILD_3);
    assertEquals(TEST_NMSIIS_CHILD_3_RESULT, messageText);

    messageText = transformer.transform(connector, TEST_NMSIIS_ADULT_1);
    assertEquals(TEST_NMSIIS_ADULT_1_RESULT, messageText);

    messageText = transformer.transform(connector, TEST_NMSIIS_ADULT_2);
    assertEquals(TEST_NMSIIS_ADULT_2_RESULT, messageText);
    
    messageText = transformer.transform(connector, TEST_NMSIIS_PROBLEM_1);
    assertEquals(TEST_NMSIIS_PROBLEM_1_RESULT, messageText);
  }

  @Test
  public void testASIIS() throws Exception {
    Transformer transformer = new Transformer();
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_POST, "Test", "");
    String messageText = "";

    messageText = TEST_ASIIS_1;
    connector.setCustomTransformations("MSH-3=RPMS\n" + "remove segment OBX all\n" + "RXA-4*=[RXA-3]\n");
    messageText = transformer.transform(connector, messageText);
    assertEquals(TEST_ASIIS_1_RESULT, messageText);
  }

}
