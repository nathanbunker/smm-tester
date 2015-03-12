/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.immunizationsoftware.dqa.tester.connectors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub;
import org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.ConnectivityTest;
import org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.ConnectivityTestRequestType;
import org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.ConnectivityTestResponse;
import org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.SubmitSingleMessage;
import org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.SubmitSingleMessageRequestType;
import org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.SubmitSingleMessageResponse;

/**
 * 
 * @author nathan
 */
public class CASoap2Connector extends HttpConnector
{

  public CASoap2Connector(String label, String url) {
    super(label, url, "CA SOAP2");
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    Client_ServiceStub serviceStub = new Client_ServiceStub();
    SubmitSingleMessage submitSingleMessage = new SubmitSingleMessage();
    SubmitSingleMessageRequestType submitSingleMessageRequestType = new SubmitSingleMessageRequestType();
    submitSingleMessageRequestType.setFacilityID(facilityid);
    submitSingleMessageRequestType.setHl7Message(message);
    submitSingleMessageRequestType.setPassword(password);
    submitSingleMessageRequestType.setUsername(userid);
    submitSingleMessage.setSubmitSingleMessage(submitSingleMessageRequestType);
    SubmitSingleMessageResponse submitSingleMessageResponse = serviceStub.submitSingleMessage(submitSingleMessage);
    return submitSingleMessageResponse.getSubmitSingleMessageResponse().get_return();
  }

  public String sendRequest(String request, ClientConnection conn, boolean debug) {
    StringBuilder response = new StringBuilder();

    StringBuilder debugLog = null;
    String messageBeingSent = null;
    if (debug) {
      debugLog = new StringBuilder();
    }
    try {
      SSLSocketFactory factory = setupSSLSocketFactory(debug, debugLog);
      URLConnection urlConn;
      DataOutputStream printout;
      InputStreamReader input = null;
      URL url = new URL(conn.url);
      urlConn = url.openConnection();
      if (factory != null && urlConn instanceof HttpsURLConnection) {
        if (debug) {
          debugLog.append("Using custom factory for SSL \r");
        }
        ((HttpsURLConnection) urlConn).setSSLSocketFactory(factory);
      }

      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);
      urlConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
      urlConn.setRequestProperty("SOAPAction", "urn:cdc:iisb:2011");
      urlConn.setRequestProperty("action", "urn:cdc:iisb:2011");
      printout = new DataOutputStream(urlConn.getOutputStream());
      StringWriter stringWriter = new StringWriter();
      PrintWriter out = new PrintWriter(stringWriter);
      // out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
      out.println("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:cdc:iisb:2011\">");
      out.println("  <soap:Header/>");
      out.println("    <soap:Body>");
      out.println("    <urn:submitSingleMessage> ");
      out.println("      <urn:username>" + conn.userId + "</urn:username>");
      out.println("      <urn:password>" + conn.password + "</urn:password>");
      out.println("      <urn:facilityID>" + conn.facilityId + "</urn:facilityID>");
      out.println("      <urn:hl7Message>");
      out.print("<![CDATA[");
      out.print(request);
      out.println("]]>");
      out.println("      </urn:hl7Message>");
      out.println("    </urn:submitSingleMessage>");
      out.println("  </soap:Body>");
      out.println("</soap:Envelope>");
      messageBeingSent = stringWriter.toString();
      printout.writeBytes(messageBeingSent);
      printout.flush();
      printout.close();
      input = new InputStreamReader(urlConn.getInputStream());
      BufferedReader in = new BufferedReader(input);
      String line;
      while ((line = in.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      input.close();
    } catch (IOException ioe) {
      response.append("Unable to relay message, received this error: " + ioe.getMessage());
    }
    if (debug) {
      response.append("\r");
      if (messageBeingSent != null) {
        response.append("SOAP SENT: \r");
        response.append(messageBeingSent);
      }
      response.append("DEBUG Statements: \r");
      response.append(debugLog);
    }
    return response.toString();
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    Client_ServiceStub serviceStub = new Client_ServiceStub();
    ConnectivityTest connectivityTest = new ConnectivityTest();
    ConnectivityTestRequestType connectivityTestRequestType = new ConnectivityTestRequestType();
    connectivityTestRequestType.setEchoBack(message);
    connectivityTest.setConnectivityTest(connectivityTestRequestType);
    ConnectivityTestResponse connectivityTestResponse = serviceStub.connectivityTest(connectivityTest);
    return connectivityTestResponse.getConnectivityTestResponse().get_return();
  }

  public static class ClientConnection
  {

    public String url = "";
    public String userId = "";
    public String password = "";
    public String facilityId = "";
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {
    // do nothing
  }

  @Override
  protected void setupFields(List<String> fields) {
    // do nothing
  }
}
