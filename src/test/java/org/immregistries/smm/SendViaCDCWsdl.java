package org.immregistries.smm;

import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import org.junit.Test;

public class SendViaCDCWsdl {
  
  private static final String SANDBOX_URL = "https://florence.immregistries.org/iis-sandbox/soap";
  
  private static final String EXAMPLE_HL7 = "MSH|^~\\&|||||20200603101019-0600||VXU^V04^VXU_V04|A04U12.2wd|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|\r" + 
      "PID|1||A04U12^^^AIRA-TEST^MR||SalineAIRA^JezebelAIRA^Udaya^^^^L|LaenAIRA^KarsynAIRA^^^^^M|20160528|F||1002-5^American Indian or Alaska Native^CDCREC|1375 Mcheemda Ave^^Saginaw^MI^48638^USA^P||^PRN^PH^^^989^4571715|||||||||2186-5^not Hispanic or Latino^CDCREC|\r" + 
      "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20200603|20200603|\r" + 
      "NK1|1|DawsonAIRA^KarsynAIRA^^^^^L|MTH^Mother^HL70063|1375 Mcheemda Ave^^Saginaw^MI^48638^USA^P|^PRN^PH^^^989^4571715|\r" + 
      "ORC|RE||A04U12.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^PRN|\r" + 
      "RXA|0|1|20200603||94^MMRV^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||Q3110HZ||MSD^Merck and Co^MVX|||CP|A|\r" + 
      "RXR|C38299^^NCIT|RA^^HL70163|\r" + 
      "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20200603|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r" + 
      "OBX|2|CE|30956-7^Vaccine Type^LN|2|94^MMRV^CVX||||||F|\r" + 
      "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20100521||||||F|\r" + 
      "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20200603||||||F|";
  @Test 
  public void sendToIISSandbox() throws Exception
  {
    Connector connector = ConnectorFactory.getConnector(ConnectorFactory.TYPE_SOAP, "IIS Sandbox", SANDBOX_URL);
    connector.setUserid("SMM Tester");
    connector.setPassword("password1234");
    connector.setFacilityid("SMM Tester Junit");
    boolean debug = false;
    String response = connector.submitMessage(EXAMPLE_HL7, debug);
    System.out.println(response);
  }
}
