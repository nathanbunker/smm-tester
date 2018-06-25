package org.immregistries.smm.tester.certify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.immregistries.smm.RecordServletInterface;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.tester.CertifyClient;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.TestCaseMessageManager;
import org.immregistries.smm.tester.manager.TestParticipant;
import org.immregistries.smm.tester.manager.TestParticipantManager;
import org.immregistries.smm.transform.TestCaseMessage;

public class CertifyRunner implements RecordServletInterface {

  // public static final String REPORT_URL = SoftwareVersion.DQACM_BASE + "record";
  // private static final String MANUAL_URL = SoftwareVersion.DQACM_BASE + "manual";
  // private static final String TEST_MESSAGE_DOWNLOAD_URL =
  // SoftwareVersion.DQACM_BASE + "testMessageDownload";



  public static TestCaseMessage getTestCaseMessage(CertifyClient certifyClient) throws Exception {
    StringBuilder testCaseMessageText = new StringBuilder();
    String testCaseMessageUrl = null;
    try {
      HttpURLConnection urlConn;
      InputStreamReader input = null;
      testCaseMessageUrl = createTestCaseMessageUrl(certifyClient);
      URL url = new URL(testCaseMessageUrl);

      urlConn = (HttpURLConnection) url.openConnection();
      urlConn.setRequestMethod("GET");

      urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      urlConn.setDoInput(true);
      urlConn.setDoOutput(false);
      urlConn.setUseCaches(false);

      input = new InputStreamReader(urlConn.getInputStream());
      BufferedReader in = new BufferedReader(input);
      String line = in.readLine();

      while ((line = in.readLine()) != null) {
        testCaseMessageText.append(line);
        testCaseMessageText.append("\n");
      }
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    TestCaseMessage testCaseMessage =
        TestCaseMessageManager.createTestCaseMessage(testCaseMessageText.toString());
    if (testCaseMessage == null && testCaseMessageUrl != null) {
      System.err
          .println("Unable to load test case " + certifyClient.getStatusLog().getTestMessageId()
              + " for this URL: " + testCaseMessageUrl);
    }
    return testCaseMessage;
  }

  public static String createTestCaseMessageUrl(CertifyClient certifyClient) {
    StringBuilder r = new StringBuilder(certifyClient.getAartUrl() + "testMessageDownload");
    r.append("?");
    r.append(PARAM_TEST_MESSAGE_ID);
    r.append("=");
    r.append(certifyClient.getStatusLog().getTestMessageId());

    String testCaseMessageUrl = r.toString();
    return testCaseMessageUrl;
  }

  private static final long MINUTE = 60 * 1000;

  public static String reportStatus(CertifyClient certifyClient) throws IOException {

    String aartName = CertifyClient.getAartName();
    String status = certifyClient.getStatus();
    Connector connector = certifyClient.getConnector();
    HttpURLConnection urlConn;
    InputStreamReader input = null;
    StringBuilder r = new StringBuilder(certifyClient.getAartUrl());
    r.append("manual?");
    r.append(PARAM_TESTER_STATUS_UPDATE);
    r.append("=");
    r.append("Yes");
    r.append("&");
    r.append(PARAM_TESTER_STATUS_TESTER_NAME);
    r.append("=");
    r.append(URLEncoder.encode(aartName, "UTF-8"));
    r.append("&");
    r.append(PARAM_TESTER_STATUS_READY_STATUS);
    r.append("=");
    r.append(URLEncoder.encode(status, "UTF-8"));
    if (connector != null) {
      r.append("&");
      r.append(PARAM_TESTER_STATUS_PUBLIC_ID_CODE);
      r.append("=");
      r.append(URLEncoder.encode(connector.getAartPublicIdCode(), "UTF-8"));
      r.append("&");
      r.append(PARAM_TESTER_STATUS_ACCESS_PASSCODE);
      r.append("=");
      r.append(URLEncoder.encode(connector.getAartAccessPasscode(), "UTF-8"));
    }

    String line = null;

    {
      boolean disconnected = false;
      long timeoutWait = 10 * 1000;
      certifyClient.setAartConnectStatus("Connecting...");
      while (line == null) {
        try {
          URL url = new URL(r.toString());
          urlConn = (HttpURLConnection) url.openConnection();
          urlConn.setRequestMethod("GET");

          urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
          urlConn.setDoInput(true);
          urlConn.setDoOutput(false);
          urlConn.setUseCaches(false);
          input = new InputStreamReader(urlConn.getInputStream());
          BufferedReader in = new BufferedReader(input);
          line = in.readLine();
          input.close();
          if (disconnected) {
            disconnected = false;
            certifyClient.setAartConnectStatus("Reconnected");
            System.err
                .println("  + " + certifyClient.getAartConnectStatus() + " URL=" + r.toString());
          } else {
            certifyClient.setAartConnectStatus("Connected");
          }
          if (line == null) {
            return "";
          }
          return line;
        } catch (ConnectException connectException) {
          disconnected = true;
          certifyClient.setAartConnectStatus("Connecting...Unable to Connect...Trying again...");
          try {
            if (timeoutWait < MINUTE) {
              timeoutWait *= 2;
            } else if (timeoutWait < 5 * MINUTE) {
              if (timeoutWait < 5 * MINUTE) {
                timeoutWait += MINUTE;
              } else {
                timeoutWait = 5 * MINUTE;
              }
            } else {
              certifyClient.setAartConnectStatus(
                  "Unable to connect to AART, will retry every 5 minutes until reconnected");
            }
            System.err
                .println("  + " + certifyClient.getAartConnectStatus() + " URL=" + r.toString());
            synchronized (connectException) {
              connectException.wait(timeoutWait);
            }
          } catch (InterruptedException e) {
            break;
            // continue
          }
        }
      }
    }

    return "";
  }

  public static void reportProgress(TestCaseMessage testCaseMessage, CertifyClient certifyClient) {
    String testMessageId = certifyClient.getStatusLog().getTestMessageId();
    SendData sendData = certifyClient.getSendData();
    try {

      HttpURLConnection urlConn;
      DataOutputStream printout;
      InputStreamReader input = null;
      URL url = new URL(certifyClient.getAartUrl() + "record");

      urlConn = (HttpURLConnection) url.openConnection();
      urlConn.setConnectTimeout(120 * 1000);

      urlConn.setRequestMethod("POST");

      urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      urlConn.setUseCaches(false);
      String content;
      StringBuilder sb = new StringBuilder();
      Connector connector = sendData.getConnector();

      sb.append("action=Submit");
      if (sendData.getTestParticipant() != null) {
        addField(sb, PARAM_TPAR_ORGANIZATION_NAME,
            sendData.getTestParticipant().getOrganizationName());
      }
      addField(sb, PARAM_TC_PUBLIC_ID_CODE, connector.getAartPublicIdCode());
      addField(sb, PARAM_TC_ACCESS_PASSCODE, connector.getAartAccessPasscode());
      addField(sb, PARAM_TC_CONNECTION_TYPE, connector.getType());
      addField(sb, PARAM_TC_CONNECTION_URL, connector.getUrl());
      addField(sb, PARAM_TC_CONNECTION_ACK_TYPE, connector.getAckType().toString());
      addField(sb, PARAM_TC_CONNECTION_CONFIG, connector.getScript());
      addField(sb, PARAM_TEST_MESSAGE_ID, testMessageId);
      addField(sb, PARAM_TESTER_STATUS_READY_STATUS, PARAM_TESTER_STATUS_TESTER_STATUS_TESTING);

      {
        int i = 1;
        if (!connector.getCustomTransformations().equals("")) {
          addField(sb, PARAM_TC_TRANSFORMS + i, "\n" + connector.getCustomTransformations());
          i++;
        }
        for (String key : connector.getScenarioTransformationsMap().keySet()) {
          String transforms = connector.getScenarioTransformationsMap().get(key);
          addField(sb, PARAM_TC_TRANSFORMS + i, key + "\n" + transforms);
          i++;
        }
      }


      StringBuilder out = new StringBuilder();
      out.append("----- " + (connector.getLabel()
          + " --------------------------------------------------------------------------------")
              .substring(0, 80)
          + "\n");
      out.append("  URL: " + connector.getUrl() + "\n");
      if (testCaseMessage == null) {
        addField(sb, PARAM_TM_RESULT_EXECEPTION_TEXT, "SMM/Tester was unable to load test case #"
            + testMessageId + " from this AART URL: " + createTestCaseMessageUrl(certifyClient));
        out.append("  Unable to load test case #" + testMessageId + " from this AART URL: "
            + createTestCaseMessageUrl(certifyClient) + "\n");
        System.err.println(out);
      } else {
        out.append("  Test Case Number: " + testCaseMessage.getTestCaseCategoryId() + "\n");
        out.append("  Test Description: " + testCaseMessage.getDescription() + "\n");
        out.append("  Test Type:        " + testCaseMessage.getTestType() + "\n");
        out.append("  Result Status:    " + testCaseMessage.getActualResultStatus() + "\n");
        addField(sb, PARAM_TM_TEST_POSITION, testCaseMessage.getTestPosition());
        addField(sb, PARAM_TM_TEST_TYPE, testCaseMessage.getTestType());
        addField(sb, PARAM_TM_TEST_CASE_NUMBER, testCaseMessage.getTestCaseCategoryId());
        addField(sb, PARAM_TM_TEST_CASE_DESCRIPTION, testCaseMessage.getDescription());
        addField(sb, PARAM_TM_TEST_CASE_ASSERT_RESULT, testCaseMessage.getAssertResult());
        addField(sb, PARAM_TM_TEST_CASE_FIELD_NAME, testCaseMessage.getFieldName());
        addField(sb, PARAM_TM_PREP_MAJOR_CHANGES_MADE, testCaseMessage.isMajorChangesMade());
        addField(sb, PARAM_TM_PREP_MESSAGE_DERIVED_FROM,
            testCaseMessage.getDerivedFromVXUMessage());
        addField(sb, PARAM_TM_PREP_MESSAGE_ORIGINAL_RESPONSE,
            testCaseMessage.getOriginalMessageResponse());
        addField(sb, PARAM_TM_PREP_MESSAGE_ACTUAL, testCaseMessage.getMessageTextSent());
        addField(sb, PARAM_TM_RESULT_MESSAGE_ACTUAL, testCaseMessage.getActualResponseMessage());
        addField(sb, PARAM_TM_RESULT_STATUS, testCaseMessage.getActualResultStatus());
        addField(sb, PARAM_TM_RESULT_QUERY_TYPE, testCaseMessage.getActualResultQueryType());
        addField(sb, PARAM_TM_RESULT_FORECAST_STATUS, testCaseMessage.getResultForecastStatus());
        addField(sb, PARAM_TM_RESULT_ACCEPTED, testCaseMessage.isAccepted());
        addField(sb, PARAM_TM_RESULT_EXECEPTION_TEXT, testCaseMessage.getException());
        addField(sb, PARAM_TM_RESULT_ACK_TYPE, testCaseMessage.getActualResultAckType());
        addField(sb, PARAM_TM_RESULT_RUN_TIME_MS, testCaseMessage.getTotalRunTime());
        addField(sb, PARAM_TM_TEST_RUN_LOG, testCaseMessage.getLog());
        if (testCaseMessage.getException() == null) {
          System.out.println(out);
        } else {
          System.err.println(out);
          testCaseMessage.getException().printStackTrace(System.err);
        }
      }
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
        if (response.length() > 0) {
          response.append('\r');
        }
        response.append(line);
      }
      input.close();
      String responseString = response.toString();
      if (!responseString.equals("ok")) {
        System.err.println("Problem! Unable to report to central server: " + responseString);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  public static TestParticipant getParticipantResponse(CertifyClient certifyClient) {
    TestParticipant testParticipant = null;
    SendData sendData = certifyClient.getSendData();
    if (sendData.getConnector() != null
        && !sendData.getConnector().getAartPublicIdCode().equals("")) {
      try {
        HttpURLConnection urlConn;
        InputStreamReader input = null;
        String link = certifyClient.getAartUrl() + "record?" + PARAM_RESOURCE + "="
            + RESOURCE_TEST_PARTICIPANT + "&" + PARAM_TPAR_PUBLIC_ID_CODE + "="
            + sendData.getConnector().getAartPublicIdCode() + "&" + PARAM_TPAR_ACCESS_PASSCODE + "="
            + sendData.getConnector().getAartAccessPasscode();
        URL url = new URL(link);
        urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setConnectTimeout(120 * 1000);
        urlConn.setRequestMethod("GET");

        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        input = new InputStreamReader(urlConn.getInputStream());
        StringBuilder response = new StringBuilder();
        BufferedReader in = new BufferedReader(input);
        String line;
        while ((line = in.readLine()) != null) {
          response.append(line);
          response.append('\r');
        }
        input.close();
        testParticipant = TestParticipantManager.readString(response.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return testParticipant;
  }


  private static void addField(StringBuilder sb, String field, String value)
      throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    if (value != null) {
      sb.append(URLEncoder.encode(value, "UTF-8"));
    }
  }

  private static void addField(StringBuilder sb, String field, Throwable value)
      throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    if (value != null) {
      StringWriter sw = new StringWriter();
      PrintWriter out = new PrintWriter(sw);
      value.printStackTrace(out);
      out.close();
      sb.append(URLEncoder.encode(sw.toString(), "UTF-8"));
    }
  }

  private static void addField(StringBuilder sb, String field, boolean value)
      throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    sb.append(value ? VALUE_YES : VALUE_NO);
  }

  private static void addField(StringBuilder sb, String field, int value)
      throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    sb.append(value);
  }

  private static void addField(StringBuilder sb, String field, long value)
      throws UnsupportedEncodingException {
    sb.append("&");
    sb.append(field);
    sb.append("=");
    sb.append(value);
  }


}
