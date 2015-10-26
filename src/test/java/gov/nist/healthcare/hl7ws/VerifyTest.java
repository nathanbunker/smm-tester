package gov.nist.healthcare.hl7ws;

import static org.junit.Assert.assertEquals;
import gov.nist.healthcare.hl7ws.client.MessageValidationV2SoapClient;

import org.junit.Test;

public class VerifyTest {
  
  private static final String EXAMPLE_MESSAGE = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r" +
"PID|1||D26376273^^^NIST MPI^MR||Snow^^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r" +
"PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r" +
"NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r" +
"ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r" +
"RXA|0|1|||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r" +
"RXR|C28161^Intramuscular^NCIT|LD^Left Arm^HL70163\r" +
"OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r" +
"OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r" +
"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r" +
"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r";
  
  private static final String OID = "2.16.840.1.113883.3.72.2.2.99001";
  /*
   * 
   * <Resource>
    <resourceID>2.16.840.1.113883.3.72.2.2.99001</resourceID>
    <name>Immunization Messaging</name>
    <version>IZ MU 2014 1.1</version>
    <organization>NIST</organization>
    <HL7version>2.5.1</HL7version>
    <resourceType>PROFILE</resourceType>
  </Resource>
   */

  @Test
  public void test() {
    MessageValidationV2SoapClient soapClient = new MessageValidationV2SoapClient("http://hit-testing2.nist.gov:8090/hl7v2ws/services/soap/MessageValidationV2");
    String result = soapClient.validate(EXAMPLE_MESSAGE, OID, "", "");
    System.out.println(result);
    
  }

}
