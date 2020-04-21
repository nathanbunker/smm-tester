package org.immregistries.smm.tester.connectors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.codec.binary.Base64;
import org.immregistries.smm.tester.manager.HL7Reader;

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
    HttpURLConnection urlConn;
    DataOutputStream printout;
    InputStreamReader input = null;
    URL url = new URL(conn.getUrl());
    urlConn = (HttpURLConnection) url.openConnection();

    urlConn.setRequestMethod("POST");

    urlConn.setRequestProperty("Content-Type",
        "application/soap+xml; charset=UTF-8; action=\"SendHIEMessage\"");
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    String content;

    StringBuilder sb = new StringBuilder();
    sb.append("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">");
    sb.append("   <soap:Header>");
    sb.append(
        "       <Action xmlns=\"http://www.w3.org/2005/08/addressing\">SendHIEMessage</Action>");
    sb.append(
        "       <MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">urn:uuid:d45a8e03-4053-45c7-92eb-5d9cfd02ffb0</MessageID>");
    sb.append(
        "       <To xmlns=\"http://www.w3.org/2005/08/addressing\">https://localhost:8443/PartnerHIEHTTPSService/HIEServicePort/PartnerHIEHTTPSService.wsdl</To>");
    sb.append("       <ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\">");
    sb.append("           <Address>http://www.w3.org/2005/08/addressing/anonymous</Address>");
    sb.append("       </ReplyTo>");
    sb.append("   </soap:Header>");
    sb.append("   <soap:Body>");
    sb.append(
        "       <SendHIEMessage xmlns=\"http://ACS.HIE.ServiceContracts/2009/10\" xmlns:ns2=\"http://ACS.CCD.Facade.FaultContracts/2008/02\" xmlns:ns3=\"http://schemas.microsoft.com/2003/10/Serialization/\">");
    sb.append("           <EMRSystemOID>AART</EMRSystemOID>");
    sb.append("           <PartnerLocationID>" + conn.getPassword() + "</PartnerLocationID>");
    sb.append("           <LocationNPI>" + conn.getUserId() + "</LocationNPI>");
    sb.append("           <BusinessName>" + conn.getFacilityId() + "</BusinessName>");
    sb.append("           <MessageSystem>HL7</MessageSystem>");

    String messageType = "VXU^V04";
    HL7Reader reader = new HL7Reader(request);
    if (reader.advanceToSegment("MSH")) {
      messageType = reader.getValue(9, 1) + "^" + reader.getValue(9, 2);
    }
    sb.append("           <MessageType>" + messageType + "</MessageType>");
    sb.append("           <Message>");
    sb.append(new String(Base64.encodeBase64(request.getBytes())));
    sb.append("</Message>");
    sb.append("       </SendHIEMessage>");
    sb.append("   </soap:Body>");
    sb.append("</soap:Envelope>");


    content = sb.toString();

    printout = new DataOutputStream(urlConn.getOutputStream());
    printout.writeBytes(content);
    printout.flush();
    printout.close();
    input = new InputStreamReader(urlConn.getInputStream());
    StringBuilder response = new StringBuilder();
    BufferedReader in = new BufferedReader(input);
    String line;
    while ((line = in.readLine()) != null) {
      response.append(line);
      response.append('\r');
    }
    input.close();
    String startTag = "<SendHIEMessageResult>";
    String stopTag = "</SendHIEMessageResult>";
    response = extractResponse(response, startTag, stopTag);
    byte[] b = Base64.decodeBase64(response.toString());
    return new String(b);
  }

  private static final String EXAMPLE_MESSAGE =
      "MSH|^~\\&|ACS-EHR|AmericanImmRegAssoc^2.16.840.1.113883.3.8527.3.1^ISO|KY0000|KY0000|20200323092738-0600||VXU^V04^VXU_V04|M33X1.290|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|\r"
          + "PID|1||M33X1^^^AmericanImmRegAssoc^MR||NicholasAIRA^CarnelianAIRA^Ganymede^^^^L|WhitakerAIRA^MikaelaAIRA^^^^^M|20190920|M|||1710 Uit Pl^^Hamburg^MI^48139^USA^P||^PRN^PH^^^810^8461129|\r"
          + "NK1|1|NicholasAIRA^MikaelaAIRA^^^^^L|MTH^Mother^HL70063|1710 Uit Pl^^Hamburg^MI^48139^USA^P|^PRN^PH^^^810^8461129|\r"
          + "PV1|1|R|\r" + "ORC|RE||M33X1.1^AIRA|\r"
          + "RXA|0|1|20200119||20^DTaP^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
          + "ORC|RE||M33X1.2^AIRA|\r"
          + "RXA|0|1|20200119||116^Rotavirus^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
          + "ORC|RE||M33X1.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^PRN|\r"
          + "RXA|0|1|20200323||116^Rotavirus^CVX|2|mL^milliliters^UCUM||00^Administered^NIP001||||||K7164HI||MSD^Merck and Co^MVX|||CP|A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20200323|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|122^Rotavirus^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20101206||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20200323||||||F|\r"
          + "";



  public static void main(String[] args) throws Exception {
    System.setProperty("javax.net.ssl.keyStore",
        "C:/dev/immregistries/smm-tester/src/test/resources/florence_immregistries_org.jks");
    System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
    System.setProperty("javax.net.ssl.trustStore",
        "C:/dev/immregistries/smm-tester/src/test/resources/florence_immregistries_org.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    // System.setProperty("javax.net.debug", "all");


    {

      HttpURLConnection urlConn;
      DataOutputStream printout;
      InputStreamReader input = null;
      URL url = new URL(
          "https://webservices.pmt.khie.healthinteractive.net/PartnerHIEHTTPSService/HIEServicePort/PartnerHIEHTTPSService?wsdl");
      urlConn = (HttpURLConnection) url.openConnection();

      urlConn.setRequestMethod("POST");

      //urlConn.setRequestProperty("Accept", null);
      //        urlConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
      //        urlConn.setRequestProperty("Expect", "100-continue");
      urlConn.setRequestProperty("Content-Type",
          "application/soap+xml; charset=UTF-8; action=\"SendHIEMessage\"");
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      String content;

      StringBuilder sb = new StringBuilder();
      sb.append("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">");
      sb.append("   <soap:Header>");
      sb.append(
          "       <Action xmlns=\"http://www.w3.org/2005/08/addressing\">SendHIEMessage</Action>");
      sb.append(
          "       <MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">urn:uuid:d45a8e03-4053-45c7-92eb-5d9cfd02ffb0</MessageID>");
      sb.append(
          "       <To xmlns=\"http://www.w3.org/2005/08/addressing\">https://localhost:8443/PartnerHIEHTTPSService/HIEServicePort/PartnerHIEHTTPSService.wsdl</To>");
      sb.append("       <ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\">");
      sb.append("           <Address>http://www.w3.org/2005/08/addressing/anonymous</Address>");
      sb.append("       </ReplyTo>");
      sb.append("   </soap:Header>");
      sb.append("   <soap:Body>");
      sb.append(
          "       <SendHIEMessage xmlns=\"http://ACS.HIE.ServiceContracts/2009/10\" xmlns:ns2=\"http://ACS.CCD.Facade.FaultContracts/2008/02\" xmlns:ns3=\"http://schemas.microsoft.com/2003/10/Serialization/\">");
      sb.append("           <EMRSystemOID>AART</EMRSystemOID>");
      sb.append(
          "           <PartnerLocationID>d7dbacb0-4c3e-11ea-b77f-2e728ce88125</PartnerLocationID>");
      sb.append("           <LocationNPI>9014000947</LocationNPI>");
      sb.append("           <BusinessName>AmericanImmRegAssoc</BusinessName>");
      sb.append("           <MessageSystem>HL7</MessageSystem>");
      sb.append("           <MessageType>VXU^V04</MessageType>");
      sb.append("           <Message>");
      sb.append(new String(Base64.encodeBase64(EXAMPLE_MESSAGE.getBytes())));
      sb.append("</Message>");
      sb.append("       </SendHIEMessage>");
      sb.append("   </soap:Body>");
      sb.append("</soap:Envelope>");


      content = sb.toString();

      printout = new DataOutputStream(urlConn.getOutputStream());
      printout.writeBytes(content);
      printout.flush();
      printout.close();
      input = new InputStreamReader(urlConn.getInputStream());
      StringBuilder response = new StringBuilder();
      BufferedReader in = new BufferedReader(input);
      String line;
      while ((line = in.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      input.close();
      String startTag = "<SendHIEMessageResult>";
      String stopTag = "</SendHIEMessageResult>";
      response = extractResponse(response, startTag, stopTag);
      byte[] b = Base64.decodeBase64(response.toString());
      System.out.println(new String(b));
    }


  }
}
