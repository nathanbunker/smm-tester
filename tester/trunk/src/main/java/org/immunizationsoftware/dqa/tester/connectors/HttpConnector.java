/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.connectors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 *
 * @author nathan
 */
public class HttpConnector extends Connector {

    public static final String FIELD_USERID = "USERID";
    public static final String FIELD_PASSWORD = "PASSWORD";
    public static final String FIELD_FACILITYID = "FACILITYID";
    public static final String FIELD_MESSAGEDATA = "MESSAGEDATA";
    public static int USERID = 0;
    public static int PASSWORD = 1;
    public static int FACILITYID = 2;
    public static int MESSAGEDATA = 3;
    private boolean stripXML = false;
    private String[] fieldNames = {FIELD_USERID, FIELD_PASSWORD, FIELD_FACILITYID, FIELD_MESSAGEDATA};

    public HttpConnector(String label, String url) {
        super(label, "POST");
        this.url = url;
    }

    public HttpConnector setFieldName(int field, String fieldName) {
        fieldNames[field] = fieldName;
        return this;
    }

    public HttpConnector stripXML() {
        stripXML = true;
        return this;
    }

    @Override
    public String submitMessage(String message) throws Exception {

        ClientConnection cc = new ClientConnection();
        cc.userId = userid;
        cc.password = password;
        cc.facilityId = facilityid;
        cc.url = url;
        String result = "";
        try {

            result = sendRequest(message, cc);
        } catch (Exception e) {
            return "Unable to relay message, received this error: " + e.getMessage();
        }

        if (stripXML) {
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
        }
        return result;

    }

    public String sendRequest(String request, ClientConnection conn) throws IOException {
        URLConnection urlConn;
        DataOutputStream printout;
        InputStreamReader input = null;
        URL url = new URL(conn.url);
        urlConn = url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        printout = new DataOutputStream(urlConn.getOutputStream());
        String content =
                fieldNames[USERID] + "=" + URLEncoder.encode(conn.userId, "UTF-8") + "&"
                + fieldNames[PASSWORD] + "=" + URLEncoder.encode(conn.password, "UTF-8") + "&"
                + fieldNames[FACILITYID] + "=" + URLEncoder.encode(conn.facilityId, "UTF-8") + "&"
                + fieldNames[MESSAGEDATA] + "=" + URLEncoder.encode(request, "UTF-8");
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
        return response.toString();
    }

    @Override
    public String connectivityTest(String message) throws Exception {
        return "Connectivity test not supported for HTTPS POST connections";
    }

    public static class ClientConnection {

        public String url = "";
        public String userId = "";
        public String password = "";
        public String facilityId = "";
    }

    @Override
    protected void setupFields(List<String> fields) {
        for (String field : fields) {
            if (field.startsWith("Set FieldName USERID:")) {
                setFieldName(HttpConnector.USERID, readValue(field));
            } else if (field.startsWith("Set FieldName PASSWORD:")) {
                setFieldName(HttpConnector.PASSWORD, readValue(field));
            } else if (field.startsWith("Set FieldName FACILITYID:")) {
                setFieldName(HttpConnector.FACILITYID, readValue(field));
            } else if (field.startsWith("Set FieldName MESSAGEDATA:")) {
                setFieldName(HttpConnector.MESSAGEDATA, readValue(field));
            } else if (field.startsWith("Strip XML:")) {
                stripXML = true;
            }
        }

    }

    @Override
    protected void makeScriptAdditions(StringBuilder sb) {
        if (!fieldNames[USERID].equals(FIELD_USERID)) {
            sb.append("Set FieldName USERID: ");
            sb.append(fieldNames[USERID]);
            sb.append("\n");
        }
        if (!fieldNames[PASSWORD].equals(FIELD_PASSWORD)) {
            sb.append("Set FieldName PASSWORD: ");
            sb.append(fieldNames[PASSWORD]);
            sb.append("\n");
        }
        if (!fieldNames[FACILITYID].equals(FIELD_FACILITYID)) {
            sb.append("Set FieldName FACILITYID: ");
            sb.append(fieldNames[FACILITYID]);
            sb.append("\n");
        }
        if (!fieldNames[MESSAGEDATA].equals(FIELD_MESSAGEDATA)) {
            sb.append("Set FieldName MESSAGEDATA: ");
            sb.append(fieldNames[MESSAGEDATA]);
            sb.append("\n");
        }
        if (stripXML) {
            sb.append("Strip XML: true\n");
        }
    }
}
