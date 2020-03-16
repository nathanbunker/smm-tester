package org.immregistries.smm.tester.connectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
  
  public static void main(String[] args) throws Exception {
    System.setProperty("javax.net.ssl.trustStore", "C:/dev/immregistries/smm-tester/florence_immregistries_org.jks"); 
    System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
    System.setProperty("javax.net.debug", "all");
    
    
    HttpURLConnection urlConn;
    InputStreamReader input = null;
    URL url = new URL("https://webservices.pmt.khie.healthinteractive.net/PartnerHIEHTTPSService/HIEServicePort/PartnerHIEHTTPSService?wsdl");

    urlConn = (HttpURLConnection) url.openConnection();

    urlConn.setRequestMethod("GET");

    urlConn.setRequestProperty("Content-Type", "text/plain");
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    urlConn.setUseCaches(false);

    input = new InputStreamReader(urlConn.getInputStream());
    StringBuilder response = new StringBuilder();
    BufferedReader in = new BufferedReader(input);
    String line;
    System.out.println("Returned information: ");
    System.out.println("-------------------------------------------------------------------------------------");
    while ((line = in.readLine()) != null) {
      System.out.println(line);
      response.append(line);
      response.append('\r');
    }
    input.close();
    System.out.println("-------------------------------------------------------------------------------------");
  }
}
