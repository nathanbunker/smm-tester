package org.immregistries.smm.tester.connectors;

import java.io.IOException;
import servicecontracts.hie.acs._2009._10.PartnerHIEServiceStub;
import servicecontracts.hie.acs._2009._10.SendHIEMessage;
import servicecontracts.hie.acs._2009._10.SendHIEMessageResponse;

public class KYKHIEConnector extends HttpConnector {
  public KYKHIEConnector(String label, String url) throws Exception {
    super(label, url, ConnectorFactory.TYPE_KY_KHIE);
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    return "KY web service does not support connectivity test";
  }

  @Override
  public String sendRequest(String request, ClientConnection conn, boolean debug)
      throws IOException {
    return "Not yet implemented";
  }
  
  private static final String EXAMPLE_MESSAGE = "MSH|^~\\&|ACS-EHR|AmericanImmRegAssoc^9014000947^ISO|KY0000|KY0000|20200323092738-0600||VXU^V04^VXU_V04|M33X1.290|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|\r" + 
      "PID|1||M33X1^^^AIRA-TEST^MR||NicholasAIRA^CarnelianAIRA^Ganymede^^^^L|WhitakerAIRA^MikaelaAIRA^^^^^M|20190920|M|||1710 Uit Pl^^Hamburg^MI^48139^USA^P||^PRN^PH^^^810^8461129|\r" + 
      "NK1|1|NicholasAIRA^MikaelaAIRA^^^^^L|MTH^Mother^HL70063|1710 Uit Pl^^Hamburg^MI^48139^USA^P|^PRN^PH^^^810^8461129|\r" + 
      "PV1|1|R|\r" + 
      "ORC|RE||M33X1.1^AIRA|\r" + 
      "RXA|0|1|20200119||20^DTaP^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r" + 
      "ORC|RE||M33X1.2^AIRA|\r" + 
      "RXA|0|1|20200119||116^Rotavirus^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r" + 
      "ORC|RE||M33X1.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^PRN|\r" + 
      "RXA|0|1|20200323||116^Rotavirus^CVX|2|mL^milliliters^UCUM||00^Administered^NIP001||||||K7164HI||MSD^Merck and Co^MVX|||CP|A|\r" + 
      "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20200323|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r" + 
      "OBX|2|CE|30956-7^Vaccine Type^LN|2|122^Rotavirus^CVX||||||F|\r" + 
      "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20101206||||||F|\r" + 
      "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20200323||||||F|\r" + 
      "";
  
  public static void main(String[] args) throws Exception {
    System.setProperty("javax.net.ssl.trustStore", "C:/dev/immregistries/smm-tester/florence_immregistries_org.jks"); 
    System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
    System.setProperty("javax.net.debug", "all");
    
    
   
    PartnerHIEServiceStub stub = new PartnerHIEServiceStub("https://webservices.pmt.khie.healthinteractive.net/PartnerHIEHTTPSService/HIEServicePort/PartnerHIEHTTPSService?wsdl");
    SendHIEMessage sendHIEMessage4 = new SendHIEMessage();
    sendHIEMessage4.setMessage(EXAMPLE_MESSAGE);
    sendHIEMessage4.setRequestingSystemUserID("B72785EC-DBA5-454B-AA18-3A700CDB4C96");
    sendHIEMessage4.setPartnerLocationID("9014000947");
    // 556^#YHGup3
    SendHIEMessageResponse hieResponse = stub.sendHIEMessage(sendHIEMessage4);
    System.out.println(hieResponse.getSendHIEMessageResult());
    
//    HttpURLConnection urlConn;
//    InputStreamReader input = null;
//    URL url = new URL("https://webservices.pmt.khie.healthinteractive.net/PartnerHIEHTTPSService/HIEServicePort/PartnerHIEHTTPSService?wsdl");
//
//    urlConn = (HttpURLConnection) url.openConnection();
//
//    urlConn.setRequestMethod("GET");
//
//    urlConn.setRequestProperty("Content-Type", "text/plain");
//    urlConn.setDoInput(true);
//    urlConn.setDoOutput(true);
//    urlConn.setUseCaches(false);
//
//    input = new InputStreamReader(urlConn.getInputStream());
//    StringBuilder response = new StringBuilder();
//    BufferedReader in = new BufferedReader(input);
//    String line;
//    System.out.println("Returned information: ");
//    System.out.println("-------------------------------------------------------------------------------------");
//    while ((line = in.readLine()) != null) {
//      System.out.println(line);
//      response.append(line);
//      response.append('\r');
//    }
//    input.close();
//    System.out.println("-------------------------------------------------------------------------------------");
  }
}
