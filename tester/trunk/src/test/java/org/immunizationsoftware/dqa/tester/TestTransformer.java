package org.immunizationsoftware.dqa.tester;

import static org.junit.Assert.*;

import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
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
  
  private static final String CLEAN_TEST_BEFORE = "MSH|^~\\&|||||20130823111809||VXU^V04^VXU_V04|X94P18|P|2.5.1|\r" + 
"PID|1||X94P18^^^OIS-TEST^MR||Court^Nye^^^^^L|Brazos^Maia|20130224|M|||321 Dundy St^^Haslett^MI^48840^USA^P||^PRN^PH^^^517^5489090|\r" + 
"NK1|1|Court^Maia|MTH^Mother^HL70063|\r";
 

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
  public void testSetRepeats() throws Exception
  {
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

  private static final String TEST_NMSIIS_1_RESULT = "FHS|^~\\&||IHS2||NMSIIS|20120731085717|CR|HL7.251.batch.txt||CI809903|\r"
      + "BHS|^~\\&||IHS2||NMSIIS|20120731085717|CR|||CI809903|\r"
      + "MSH|^~\\&||IHS2||NMSIIS|20120731085717||VXU^V04^VXU_V04|CI809903|P|2.5.1|||AL|AL|\r"
      + "PID|1||232101152165^^^CHINLE HOSP^MR~258723162^^^^SS~646047356^^^^MA||LOSSIAH^JENNY^FAY^^^|SPARKS^DORIS^GORE^^^|19970501|F||1002-5^AMERICAN INDIAN OR ALASKA NATIVE^HL70005|238 GOOSECREEK RD^^ANYCITY^NC^28719^^P||^PRN^PH^^^555^555186|^WPN^PH^^^^||||||||2186-5^NOT HISPANIC OR LATINO^HL70006||N\r"
      + "PD1|||232101^CHINLE HOSP||||||||||20120720|||A\r"
      + "NK1|1|LOSSIAH^KRISTI^LYNN^^^|MTH^MOTHER^HL70063||||\r"
      + "PV1|1|R|CHINLE HOSP^|||||||EVE|||||||||3294622|V04^20100226~||||||||||||||||||||||||20100226120000-0500\r"
      + "IN1|1|\r"
      + "IN2|\r"
      + "ORC|RE|49340-232101|19980302-49340-232101152165^CHINLE HOSP||IP|||||1234567890^HORNBUCKLE^CLAUDE^ALBEE^^^||1234567890^HORNBUCKLE^CLAUDE^ALBEE^^^|||||^^^CHINLE HOSP\r"
      + "RXA|0|1|19980302091900|19980302091900|10^IPV^CVX|.5|ML^ML^ISO+||00^NEW IMMUNIZATION RECORD^NIP001|1234567890^HORNBUCKLE^CLAUDE|^^^CHINLE HOSP||||A1060-2|20091026|PMC^SANOFI PASTEUR^MVX|||CP|A|19980302091900\r"
      + "BTS|1|CR|\r" + "FTS|1|CR|\r";

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

  @Test
  public void testNMSIIS() throws Exception {
    Transformer transformer = new Transformer();
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_POST, "Test", "");
    String messageText = "";

    messageText = TEST_NMSIIS_1;
    connector.setCustomTransformations("MSH-4=[USERID]\n" + "MSH-6=NMSIIS\n" + "insert segment BHS first\n"
        + "insert segment BTS last\n" + "insert segment FHS first\n" + "insert segment FTS last\n" + "FHS-8=CR\n"
        + "BHS-8=CR\n" + "FHS-9=[FILENAME]\n" + "FTS-1=1\n" + "BTS-1=1\n" + "FTS-2=CR\n" + "BTS-2=CR\n" + "insert segment IN1 before ORC\n" + "insert segment IN2 after IN1\n" + "IN1-1=1\n");
    connector.setCurrentFilename("HL7.251.batch.txt");
    connector.setCurrentControlId("CI809903");
    connector.setUserid("IHS2");
    messageText = transformer.transform(connector, messageText);
    assertEquals(TEST_NMSIIS_1_RESULT, messageText);
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
