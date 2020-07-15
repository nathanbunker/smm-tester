package org.immregistries.smm.tester.connectors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class IZGatewayConnector extends HttpConnector {
  public IZGatewayConnector(String label, String url) throws Exception {
    super(label, url, ConnectorFactory.TYPE_IZ_GATEWAY);
  }

  @Override
  public boolean connectivityTestSupported() {
    return true;
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    ClientConnection cc = new ClientConnection();
    cc.setUrl(url);
    return sendConnectivityTest(message, cc);
  }


  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    ClientConnection cc = new ClientConnection();
    cc.setUrl(url);
    cc.setDestinationId(destinationid);
    cc.setUserId(userid);
    cc.setPassword(password);
    cc.setFacilityId(facilityid);
    String result = "";
    try {
      result = sendRequestToSubmitMessage(message, cc);
    } catch (Exception e) {
      if (throwExceptions) {
        throw e;
      }
      return "Unable to relay message, received this error: " + e.getMessage();
    }

    return result;
  }

  public String sendConnectivityTest(String message, ClientConnection conn) throws IOException {
    URLConnection urlConn;
    DataOutputStream printout;
    InputStreamReader input = null;
    URL url = new URL(conn.getUrl());
    urlConn = url.openConnection();
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    urlConn.setUseCaches(false);
    urlConn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
    printout = new DataOutputStream(urlConn.getOutputStream());
    StringWriter stringWriter = new StringWriter();
    PrintWriter out = new PrintWriter(stringWriter);
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println(
        "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:cdc:iisb:2014\">");
    out.println("   <soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
    out.println(
        "      <wsa:Action>urn:cdc:iisb:2014:IISPortType:ConnectivityTestRequest</wsa:Action>");
    out.println("      <wsa:MessageID>uuid:702c904a-5d3a-4e62-ba11-facffcc359ad</wsa:MessageID>");
    out.println("   </soap:Header>");
    out.println("   <soap:Body>");
    out.println("      <urn:ConnectivityTestRequest>");
    out.println("         <urn:EchoBack>" + message + "</urn:EchoBack>");
    out.println("      </urn:ConnectivityTestRequest>");
    out.println("   </soap:Body>");
    out.println("</soap:Envelope>");
    printout.writeBytes(stringWriter.toString());
    printout.flush();
    printout.close();
    input = getInputAndLog(urlConn);
    StringBuilder response = new StringBuilder();
    BufferedReader in = new BufferedReader(input);
    String line;
    while ((line = in.readLine()) != null) {
      response.append(line);
      response.append('\r');
    }
    input.close();
    StringBuilder s = extractResponse(response, "EchoBack>", "</");
    return s.toString();

  }

  public String sendRequestToSubmitMessage(String message, ClientConnection conn)
      throws IOException {
    URLConnection urlConn;
    DataOutputStream printout;
    InputStreamReader input = null;
    URL url = new URL(conn.getUrl());
    urlConn = url.openConnection();
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    urlConn.setUseCaches(false);
    urlConn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
    printout = new DataOutputStream(urlConn.getOutputStream());
    StringWriter stringWriter = new StringWriter();
    PrintWriter out = new PrintWriter(stringWriter);
    out.println("<?xml version='1.0' encoding='UTF-8'?>");
    out.println(
        "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:cdc:iisb:hub:2014\" xmlns:urn1=\"urn:cdc:iisb:2014\">");
    out.println("   <soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
    out.println("      <urn:HubRequestHeader>");
    out.println("         <urn:DestinationId>" + conn.getDestinationId() + "</urn:DestinationId>");
    out.println("      </urn:HubRequestHeader>");
    out.println(
        "      <wsa:Action>urn:cdc:iisb:hub:2014:IISHubPortType:SubmitSingleMessageRequest</wsa:Action>");
    out.println("      <wsa:MessageID>uuid:702c904a-5d3a-4e62-ba11-facffcc359ad</wsa:MessageID>");
    out.println("   </soap:Header>");
    out.println("   <soap:Body>");
    out.println("      <urn1:SubmitSingleMessageRequest>");
    out.println("        <urn1:Username>" + conn.getUserId() + "</urn1:Username>");
    out.println("        <urn1:Password>" + conn.getPassword() + "</urn1:Password>");
    out.println("         <urn1:FacilityID>" + conn.getFacilityId() + "</urn1:FacilityID>");
    out.println(
        "         <urn1:Hl7Message>" + message.replace("&", "&amp;") + "</urn1:Hl7Message>");
    out.println("      </urn1:SubmitSingleMessageRequest>");
    out.println("   </soap:Body>");
    out.println("</soap:Envelope>");
    printout.writeBytes(stringWriter.toString());
    printout.flush();
    printout.close();
    input = getInputAndLog(urlConn);

    StringBuilder response = new StringBuilder();
    BufferedReader in = new BufferedReader(input);
    String line;
    while ((line = in.readLine()) != null) {
      response.append(line);
      response.append('\r');
    }
    input.close();
    StringBuilder s = extractResponse(response, "Hl7Message>", "</");
    return s.toString();
  }

  public InputStreamReader getInputAndLog(URLConnection urlConn) throws IOException {
    InputStreamReader input;
    try {
      input = new InputStreamReader(urlConn.getInputStream());
    } catch (IOException e) {
      e.printStackTrace(System.err);
      HttpURLConnection httpURL = (HttpURLConnection) urlConn;
      input = new InputStreamReader(httpURL.getErrorStream());
      String line;
      BufferedReader in = new BufferedReader(input);
      while ((line = in.readLine()) != null) {
        System.err.println(line);
      }
      throw e;
    }
    return input;
  }



}
