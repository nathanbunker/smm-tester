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
import java.util.List;

public class StatusReporter extends Thread implements RemoteConnectionReportingInterface
{
  public static final int SEND_MAXIMUM_INTERVAL = 2 * 60 * 1000;
  public static final int SEND_MINIMUM_INTERVAL = 1 * 1000;

  private SendData sendData;
  private StatusLogger statusLogger;
  private URL supportCenterUrl = null;
  private List<StatusLog> statusLogList = new ArrayList<StatusLog>();
  private boolean sendStatus = true;
  private int logLevel = LOG_LEVEL_WARNING;

  public void setSendStatus()
  {
    sendStatus = true;
  }

  private long lastUpdate = 0l;

  public StatusReporter(SendData sendData, StatusLogger statusLogger) throws MalformedURLException {
    this.sendData = sendData;
    this.statusLogger = statusLogger;
    if (ManagerServlet.getSupportCenterUrl() != null)
    {
      supportCenterUrl = new URL(ManagerServlet.getSupportCenterUrl());
    }

  }

  @Override
  public void run()
  {
    while (true)
    {
      boolean shouldUpdate = false;
      long timeSinceLastUpdate = System.currentTimeMillis() - lastUpdate;
      if (sendStatus)
      {
        shouldUpdate = true;
      } else if (statusLogList.size() > 0)
      {
        shouldUpdate = true;
      } else if (timeSinceLastUpdate > SEND_MAXIMUM_INTERVAL)
      {
        shouldUpdate = true;
      }

      if (shouldUpdate)
      {
        List<StatusLog> statusLogListToSend = getStatusLogList();
        if (statusLogListToSend == null)
        {
          updateSupportCenter(null);
        } else
        {
          for (StatusLog statusLog : statusLogListToSend)
          {
            updateSupportCenter(statusLog);
          }
        }
      }
      synchronized (statusLogList)
      {
        try
        {
          statusLogList.wait(SEND_MINIMUM_INTERVAL);
        } catch (InterruptedException ie)
        {
          // continue
        }
      }
    }
  }

  public void registerIssue(String message, int logLevel, Throwable exception)
  {
    if (logLevel <= this.logLevel)
    {
      synchronized (statusLogList)
      {
        StatusLog statusLog = new StatusLog();
        statusLog.setIssueText(message);
        statusLog.setLogLevel(logLevel);
        statusLog.setException(exception);
        statusLogList.add(statusLog);
      }
    }
  }

  private List<StatusLog> getStatusLogList()
  {
    synchronized (statusLogList)
    {
      if (statusLogList.size() == 0)
      {
        return null;
      }
      List<StatusLog> statusLogListCopy = new ArrayList<StatusLog>(statusLogList);
      statusLogList.clear();
      return statusLogListCopy;
    }
  }

  private void updateSupportCenter(StatusLog statusLog)
  {

    if (supportCenterUrl != null)
    {

      try
      {
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
        if (sendData.getConnector() == null)
        {
          append(CONNECTION_LABEL, "None", content);
          append(LOCATION_TO, "", content);
          append(ACCOUNT_NAME, "", content);
        } else
        {
          append(CONNECTION_LABEL, sendData.getConnector().getLabel(), content);
          append(LOCATION_TO, sendData.getConnector().getUrl(), content);
          append(ACCOUNT_NAME, sendData.getConnector().getUserid(), content);
        }
        append(LOCATION_FROM, sendData.getRootDir().getAbsolutePath(), content);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        append(UP_SINCE_DATE, sdf.format(sendData.getUpSinceDate()), content);
        append(STATUS_LABEL, sendData.getScanStatus().toString(), content);
        append(ATTEMPT_COUNT, String.valueOf(statusLogger.getAttemptCount()), content);
        append(SENT_COUNT, String.valueOf(statusLogger.getSentCount()), content);
        append(ERROR_COUNT, String.valueOf(statusLogger.getErrorCount()), content);
        if (statusLog != null)
        {
          append(ISSUE_TEXT, statusLog.getIssueText(), content);
          append(LOG_LEVEL, String.valueOf(statusLog.getLogLevel()), content);
          if (statusLog.getException() != null)
          {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            statusLog.getException().printStackTrace(pw);
            append(EXCEPTION_TRACE, sw.toString(), content);
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
        if (response == null || !response.equals("OK"))
        {
          throw new IOException("Unexpected response: " + response);
        } else
        {
          lastUpdate = System.currentTimeMillis();
          sendStatus = false;
        }
      } catch (IOException e)
      {
        String message = "Unable to update support center";
        statusLogger.logError(message);
        e.printStackTrace(statusLogger.getOut());
        statusLogger.getOut().flush();
      }
    }
  }

  private void append(String field, String value, StringBuilder content) throws UnsupportedEncodingException
  {
    content.append(field);
    content.append("=");
    content.append(URLEncoder.encode(value, "UTF-8"));
    content.append("&");
  }
}
