package org.immunizationsoftware.dqa.mover;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusReporter extends Thread implements RemoteConnectionReportingInterface
{
  public static final int SEND_MAXIMUM_INTERVAL = 15 * 60 * 1000;
  public static final int SEND_MINIMUM_INTERVAL = 30 * 1000;

  private SendData sendData;
  private URL supportCenterUrl = null;
  private List<StatusLog> statusLogList = new ArrayList<StatusLog>();
  private Map<String, StatusFile> statusFileMap = new HashMap<String, StatusFile>();
  private boolean sendStatus = true;
  private int logLevel = LOG_LEVEL_INFO;
  private Throwable exception = null;
  private boolean notStopped = true;

  public void setSendStatus() {
    sendStatus = true;
  }

  private long lastUpdate = 0l;

  public StatusReporter(SendData sendData) throws MalformedURLException {
    this.sendData = sendData;
    if (ManagerServlet.getSupportCenterUrl() != null && !ManagerServlet.getSupportCenterUrl().equals("none")
        && !ManagerServlet.getSupportCenterUrl().equals("")) {
      supportCenterUrl = new URL(ManagerServlet.getSupportCenterUrl());
    }
  }

  @Override
  public void run() {
    while (sendData.isOkayToRun() && notStopped) {
      long timeSinceLastUpdate = System.currentTimeMillis() - lastUpdate;
      if (sendStatus || timeSinceLastUpdate > SEND_MAXIMUM_INTERVAL) {
        updateSupportCenter();
      }
      synchronized (this) {
        try {
          this.wait(SEND_MINIMUM_INTERVAL);
        } catch (InterruptedException ie) {
          // continue
        }
      }
    }
  }

  public void updateSupportCenter() {
    List<StatusLog> statusLogListToSend = getStatusLogList();
    List<StatusFile> statusFileListToSend = getStatusFileList();
    updateSupportCenter(statusLogListToSend, statusFileListToSend);
  }

  public void shutdown() {
    synchronized (this) {
      this.interrupt();
    }
  }

  public void registerFile(String fileName, SendData.ScanStatus scanStatus, int messageCount, int sentCount,
      int errorCount) {
    synchronized (statusFileMap) {
      StatusFile statusFile = statusFileMap.get(fileName);
      if (statusFile == null) {
        statusFile = new StatusFile();
        statusFile.setFileName(fileName);
        statusFileMap.put(fileName, statusFile);
      }
      statusFile.setStatusLabel(scanStatus.toString());
      if (messageCount > 0) {
        statusFile.setMessageCount(messageCount);
      }
      if (sentCount > 0) {
        statusFile.setSentCount(sentCount);
      }
      if (errorCount > 0) {
        statusFile.setErrorCount(errorCount);
      }
    }
  }

  private List<StatusFile> getStatusFileList() {
    synchronized (statusFileMap) {
      if (statusFileMap.size() == 0) {
        return null;
      }
      List<StatusFile> statusFileList = new ArrayList<StatusFile>(statusFileMap.values());
      statusFileMap.clear();
      return statusFileList;
    }
  }

  public void registerIssue(String message, int logLevel, Throwable exception) {
    if (logLevel <= this.logLevel) {
      synchronized (statusLogList) {
        StatusLog statusLog = new StatusLog();
        statusLog.setIssueText(message);
        statusLog.setLogLevel(logLevel);
        statusLog.setException(exception);
        statusLogList.add(statusLog);
      }
    }
  }

  private List<StatusLog> getStatusLogList() {
    synchronized (statusLogList) {
      if (statusLogList.size() == 0) {
        return null;
      }
      List<StatusLog> statusLogListCopy = new ArrayList<StatusLog>(statusLogList);
      statusLogList.clear();
      return statusLogListCopy;
    }
  }

  public void updateSupportCenter(List<StatusLog> statusLogList, List<StatusFile> statusFileList) {

    if (supportCenterUrl != null) {

      try {
        exception = null;
        HttpURLConnection urlConn;
        DataOutputStream printout;
        InputStreamReader input = null;
        URL url = new URL(ManagerServlet.getSupportCenterUrl());
        urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setRequestMethod("POST");

        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        StringBuilder content = new StringBuilder();

        append(CONNECTION_CODE, sendData.getStableSystemId(), content);
        append(SUPPORT_CENTER_CODE, ManagerServlet.getSupportCenterCode(), content);
        if (sendData.getConnector() == null) {
          append(CONNECTION_LABEL, "None", content);
          append(LOCATION_TO, "", content);
          append(ACCOUNT_NAME, "", content);
        } else {
          append(CONNECTION_LABEL, sendData.getConnector().getLabel(), content);
          append(LOCATION_TO, sendData.getConnector().getUrl(), content);
          append(ACCOUNT_NAME, sendData.getConnector().getUserid(), content);
        }
        append(LOCATION_FROM, sendData.getRootDir().getAbsolutePath(), content);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        append(UP_SINCE_DATE, sdf.format(sendData.getUpSinceDate()), content);
        append(STATUS_LABEL, sendData.getScanStatus().toString(), content);
        append(ATTEMPT_COUNT, String.valueOf(sendData.getAttemptCount()), content);
        append(SENT_COUNT, String.valueOf(sendData.getSentCount()), content);
        append(ERROR_COUNT, String.valueOf(sendData.getErrorCount()), content);
        if (statusLogList != null) {
          int pos = 0;
          for (StatusLog statusLog : statusLogList) {
            append(ISSUE_TEXT + pos, statusLog.getIssueText(), content);
            append(REPORTED_DATE + pos, sdf.format(statusLog.getReportedData()), content);
            append(LOG_LEVEL + pos, String.valueOf(statusLog.getLogLevel()), content);
            if (statusLog.getException() != null) {
              StringWriter sw = new StringWriter();
              PrintWriter pw = new PrintWriter(sw);
              statusLog.getException().printStackTrace(pw);
              append(EXCEPTION_TRACE + pos, sw.toString(), content);
            }
            pos++;
          }
        }
        if (statusFileList != null) {
          int pos = 0;
          for (StatusFile statusFile : statusFileList) {
            append(FILE_NAME + pos, statusFile.getFileName(), content);
            append(FILE_MESSAGE_COUNT + pos, String.valueOf(statusFile.getMessageCount()), content);
            append(FILE_SENT_COUNT + pos, String.valueOf(statusFile.getSentCount()), content);
            append(FILE_ERROR_COUNT + pos, String.valueOf(statusFile.getErrorCount()), content);
            append(FILE_STATUS_LABEL + pos, statusFile.getStatusLabel(), content);
            pos++;
          }
        }
        printout = new DataOutputStream(urlConn.getOutputStream());
        printout.writeBytes(content.toString());
        printout.flush();
        printout.close();
        input = new InputStreamReader(urlConn.getInputStream());
        BufferedReader in = new BufferedReader(input);
        String response = in.readLine();
        input.close();
        if (response == null) {
          exception = new IOException("Unexpected response from support center (null) ");
        } else if (response.equals("STOP")) {
          exception = new IOException("Support center requested that all updates be STOPPED, stopping status reporter");
          notStopped = false;
        } else if (response.equals("OK")) {
          lastUpdate = System.currentTimeMillis();
          sendStatus = false;
        }
      } catch (IOException e) {
        exception = e;
      }
    }
  }

  public Throwable getException() {
    return exception;
  }

  private void append(String field, String value, StringBuilder content) throws UnsupportedEncodingException {
    content.append(field);
    content.append("=");
    content.append(URLEncoder.encode(value, "UTF-8"));
    content.append("&");
  }
}
