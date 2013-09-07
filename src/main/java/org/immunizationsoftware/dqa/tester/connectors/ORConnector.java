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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author nathan
 */
public class ORConnector extends Connector
{

  private static String XML_START_1 = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" "
      + "xmlns:vac=\"http://vaccination.org/\">";

  private static String XML_START_2 = "  <soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">"
      + "    <wsse:Security soap:mustUnderstand=\"true\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wsswssecurity-secext-1.0.xsd\">"
      + "      <wsse:UsernameToken wsu:Id=\"UsernameToken-2\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">"
      + "        <wsse:Username>";

  private static String XML_START_3 = "</wsse:Username>"
      + "        <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\"><![CDATA[";

  private static String XML_START_4 = "</wsse:Password>"
      + "        <wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">xxxxxxxxxxxx</wsse:Nonce>"
      + "        <wsu:Created>";

  private static String XML_START_5 = "</wsu:Created>" + "      </wsse:UsernameToken>"
      + "    </wsse:Security>"
      + "    <wsa:Action>http://vaccination.org/IVaccinationService/UpdateHistoryRequest</wsa:Action>";
 
  private static String CLIP_OUT = 
       "    <wsa:MessageID>uuid:7e2efb25-077c-4982-b6ee-a21463437094</wsa:MessageID>";

  private static String XML_START_6 =  "  </soap:Header>"
      + "  <soap:Body>" + "    <vac:UpdateHistory>" + "      <arg0>" + "<![CDATA[";
  private static String XML_END = "]]>" + "      </arg0>" + "    </vac:UpdateHistory>" + "  </soap:Body>"
      + "</soap:Envelope>";

  protected ORConnector(String label, String url, String type) {
    super(label, type);
    this.url = url;
  }

  public ORConnector(String label, String url) {
    super(label, "POST");
    this.url = url;
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    ClientConnection cc = new ClientConnection();
    cc.url = url;
    String result = "";
    try {
      result = sendRequest(message, cc, debug);
    } catch (Exception e) {
      if (throwExceptions) {
        throw e;
      }
      return "Unable to relay message, received this error: " + e.getMessage();
    }

    StringBuffer sbuf = new StringBuffer(result.length());
    boolean inTag = false;
    for (char c : result.toCharArray()) {
      if (c == '<') {
        inTag = true;
      } else if (c == '>') {
        inTag = false;
      } else if (!inTag) {
        sbuf.append(c);
      }
    }
    if (sbuf.length() > 0) {
      result = sbuf.toString();
    }

    while (result != null && result.length() > 0 && result.charAt(0) < ' ') {
      result = result.substring(1);
    }
    return result;

  }

  public String sendRequest(String request, ClientConnection conn, boolean debug) throws IOException {
    StringBuilder debugLog = null;
    if (debug) {
      debugLog = new StringBuilder();
    }
    try {
      SSLSocketFactory factory = setupSSLSocketFactory(debug, debugLog);

      HttpURLConnection urlConn;
      DataOutputStream printout;
      InputStreamReader input = null;
      URL url = new URL(conn.url);

      urlConn = (HttpURLConnection) url.openConnection();
      if (factory != null && urlConn instanceof HttpsURLConnection) {
        ((HttpsURLConnection) urlConn).setSSLSocketFactory(factory);
      }

      urlConn.setRequestMethod("POST");

      urlConn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);
      String content;

      SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd"); // 2011-12-22T21:55:18.593Z
      SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss.SSS");
      sdfTime.setTimeZone(TimeZone.getTimeZone("GMT"));
      sdfDate.setTimeZone(TimeZone.getTimeZone("GMT"));
      Date today = new Date();
      String messageDate = sdfDate.format(today) + "T" + sdfTime.format(today) + "Z";
      StringBuilder sb = new StringBuilder();
      sb.append(XML_START_1);
      sb.append(XML_START_2);
      sb.append(userid);
      sb.append(XML_START_3);
      sb.append(password);
      sb.append(XML_START_4);
      sb.append(messageDate);
      sb.append(XML_START_5);
      sb.append(XML_START_6);
      sb.append(request);
      sb.append(XML_END);
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
      if (debug) {
        response.append("\r");
        response.append("DEBUG LOG: \r");
        response.append(debugLog);
      }
      return response.toString();
    } catch (IOException e) {
      if (debug) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);
        out.println("Unable to complete request");
        e.printStackTrace(out);
        out.println("DEBUG LOG: \r");
        out.println(debugLog);
        out.close();
        return stringWriter.toString();
      } else {
        throw e;
      }
    }

  }

  protected SSLSocketFactory setupSSLSocketFactory(boolean debug, StringBuilder debugLog) {
    SSLSocketFactory factory = null;

    if (getKeyStore() != null) {
      if (debug) {
        debugLog.append("Key store defined, looking to load it for use on this connection \r");
      }
      try {
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(getKeyStore());
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[] { tm }, null);
        factory = context.getSocketFactory();
        if (debug) {
          debugLog.append("Key store loaded \r");
        }
      } catch (Exception e) {
        e.printStackTrace();
        if (debug) {
          debugLog.append("Unable to load key store: " + e.getMessage() + " \r");
        }
      }
    } else {
      if (debug) {
        debugLog.append("Key store was not defined, using default for this connection \r");
      }
    }
    return factory;
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    return "Connectivity test not supported for HTTPS POST connections";
  }

  public static class ClientConnection
  {
    public String url = "";
  }

  @Override
  protected void setupFields(List<String> fields) {
    for (String field : fields) {
      // nothing to do
    }

  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {
    // nothing to do
    // if (deduplicate) {
    // sb.append("Deduplicate: true\n");
    // }

  }
}
