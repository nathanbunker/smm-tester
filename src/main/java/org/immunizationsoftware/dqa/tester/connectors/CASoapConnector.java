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

/**
 * 
 * @author nathan
 */
public class CASoapConnector extends HttpConnector
{

  public CASoapConnector(String label, String url) {
    super(label, "CA SOAP");
    this.url = url;
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception
  {
    ClientConnection cc = new ClientConnection();
    cc.userId = userid;
    cc.password = password;
    cc.facilityId = facilityid;
    cc.url = url;
    String result = "";
    try
    {
      result = sendRequest(message, cc, debug);
    } catch (Exception e)
    {
      return "Unable to relay message, received this error: " + e.getMessage();
    }

    StringBuffer sbuf = new StringBuffer(result.length());
    boolean inTag = false;
    for (char c : result.toCharArray())
    {
      if (c == '<')
      {
        inTag = true;
      } else if (c == '>')
      {
        inTag = false;
      } else if (!inTag)
      {
        sbuf.append(c);
      }
    }
    if (sbuf.length() > 0)
    {
      result = sbuf.toString();
    }
    return result;
  }

  public String sendRequest(String request, ClientConnection conn, boolean debug) throws IOException
  {
    StringBuilder debugLog = null;
    if (debug) {
      debugLog = new StringBuilder();
    }
    SSLSocketFactory factory = setupSSLSocketFactory(debug, debugLog);
    URLConnection urlConn;
    DataOutputStream printout;
    InputStreamReader input = null;
    URL url = new URL(conn.url);
    urlConn = url.openConnection();
    if (factory != null && urlConn instanceof HttpsURLConnection) {
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
    String messageBeingSent = stringWriter.toString();
    printout.writeBytes(messageBeingSent);
    printout.flush();
    printout.close();
    input = new InputStreamReader(urlConn.getInputStream());
    StringBuilder response = new StringBuilder();
    BufferedReader in = new BufferedReader(input);
    String line;
    while ((line = in.readLine()) != null)
    {
      response.append(line);
      response.append('\r');
    }
    input.close();
    if (debug) {
      response.append("\r");
      response.append("SOAP SENT: \r");
      response.append(messageBeingSent);
    }
    return response.toString();
  }

  @Override
  public String connectivityTest(String message) throws Exception
  {
    return "Connectivity test not implemented for CA connections";
  }

  public static class ClientConnection
  {

    public String url = "";
    public String userId = "";
    public String password = "";
    public String facilityId = "";
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb)
  {
    // do nothing
  }

  @Override
  protected void setupFields(List<String> fields)
  {
    // do nothing
  }
}
