/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.immregistries.smm.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.immregistries.smm.RecordServletInterface;
import org.immregistries.smm.SoftwareVersion;
import org.immregistries.smm.mover.ConnectionManager;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.mover.ShutdownInterceptor;
import org.immregistries.smm.tester.certify.AartUrl;
import org.immregistries.smm.tester.certify.CertifyRunner;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.run.TestRunner;
import org.immregistries.smm.transform.TestCaseMessage;

/**
 * 
 * @author nathan
 */
public class CertifyClient extends Thread {

  private SendData sendData = null;
  private Connector connector = null;
  private static String aartName = null;
  private static List<AartUrl> aartUrlList = new ArrayList<AartUrl>();
  private boolean keepRunning = true;
  private String aartUrl = "";
  private String threadId = null;
  private String sectionTypes = null;
  private int sendCount = 0;
  private StatusLog statusLog = null;
  private static List<StatusLog> statusLogList = new ArrayList<CertifyClient.StatusLog>();
  private String status = "";
  private String aartConnectStatus = "";
  private Date aartConnectStatusDate = null;

  public Date getAartConnectStatusDate() {
    return aartConnectStatusDate;
  }

  public String getSectionTypes() {
    return sectionTypes;
  }

  public String getThreadId() {
    return threadId;
  }

  public void setThreadId(String threadId) {
    this.threadId = threadId;
  }

  public StatusLog getStatusLog() {
    return statusLog;
  }

  public SendData getSendData() {
    return sendData;
  }

  public void setSendData(SendData sendData) {
    this.sendData = sendData;
  }

  public String getAartConnectStatus() {
    return aartConnectStatus;
  }

  public void setAartConnectStatus(String aartConnectStatus) {
    this.aartConnectStatus = aartConnectStatus;
    this.aartConnectStatusDate = new Date();
  }

  public static String getAartName() {
    return aartName;
  }

  public String getAartUrl() {
    return aartUrl;
  }

  public Connector getConnector() {
    return connector;
  }

  public String getStatus() {
    return status;
  }

  public class StatusLog {

    private String statusMessage = "";
    private String aartPublicIdCode = "";
    private String testMessageId = "";
    private String problemMessage = null;
    private String organizationName = "";
    private Throwable exception = null;
    private String testCaseDescription = "";

    public String getTestCaseDescription() {
      return testCaseDescription;
    }

    public void setTestCaseDescription(String testCaseDescription) {
      this.testCaseDescription = testCaseDescription;
    }

    public Throwable getException() {
      return exception;
    }

    public void setException(Throwable exception) {
      this.exception = exception;
    }

    public String getStatusMessage() {
      return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
    }

    public String getAartPublicIdCode() {
      return aartPublicIdCode;
    }

    public void setAartPublicIdCode(String aartPublicIdCode) {
      this.aartPublicIdCode = aartPublicIdCode;
    }

    public String getTestMessageId() {
      return testMessageId;
    }

    public void setTestMessageId(String testMessageId) {
      this.testMessageId = testMessageId;
    }

    public String getProblemMessage() {
      return problemMessage;
    }

    public void setProblemMessage(String problemMessage) {
      this.problemMessage = problemMessage;
    }

    public String getOrganizationName() {
      return organizationName;
    }

    public void setOrganizationName(String organizationName) {
      this.organizationName = organizationName;
    }
  }

  private static final int STATUS_LOG_LIST_MAX_SIZE = 5000;

  private static void addToStatusLogList(StatusLog statusLog) {
    synchronized (statusLogList) {
      while (statusLogList.size() > STATUS_LOG_LIST_MAX_SIZE) {
        statusLogList.remove(0);
      }
      statusLogList.add(statusLog);
    }
  }

  private static StatusLog getStatusLog(String testMessageId) {
    for (StatusLog statusLog : statusLogList) {
      if (statusLog.testMessageId.equals(testMessageId)) {
        return statusLog;
      }
    }
    return null;
  }

  public void stopRunning() {
    keepRunning = false;
  }

  public CertifyClient(String aartUrl) {
    this.aartUrl = aartUrl;
  }

  @Override
  public void run() {
    while (keepRunning) {
      try {
        String aartAction = null;
        {
          status = RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_READY;
          aartAction = CertifyRunner.reportStatus(this);
          if (aartAction.equals("")) {
            aartAction = null;
          } else {
            if (aartAction.startsWith(RecordServletInterface.PARAM_TESTER_ACTION_RUN)) {
              String[] actionArgs = aartAction.split("\\s");
              if (actionArgs.length > 2) {
                statusLog = new StatusLog();
                statusLog.setAartPublicIdCode(actionArgs[1]);
                statusLog.setTestMessageId(actionArgs[2]);
                statusLog.setStatusMessage("Run command received");
                addToStatusLogList(statusLog);

                for (SendData sd1 : ConnectionManager.getSendDataList()) {
                  if (sd1.getConnector() != null && sd1.getConnector().getAartPublicIdCode()
                      .equals(statusLog.getAartPublicIdCode())) {
                    ConnectServlet.readNewConnection(this, sd1.getInternalId());
                    connector = sendData.getConnector();
                    break;
                  }
                }
                if (connector == null) {
                  statusLog.setProblemMessage("Connector was not intialized");
                  System.err.println("  + Connection information can not be found: "
                      + statusLog.getAartPublicIdCode());
                  continue;
                } else if (sendData.getTestParticipant() == null) {
                  statusLog.setProblemMessage(
                      "Test participant is not recognized and can't be reported to AART");
                  System.err.println(
                      "  + Unrecognized test participant: " + statusLog.getAartPublicIdCode());
                  continue;
                }
                statusLog.setOrganizationName(sendData.getTestParticipant().getOrganizationName());
                statusLog.setStatusMessage("Retrieving test case message");
                TestCaseMessage testCaseMessage = CertifyRunner.getTestCaseMessage(this);

                if (testCaseMessage == null) {
                  statusLog
                      .setProblemMessage("Couldn't load test case " + statusLog.getTestMessageId());
                  CertifyRunner.reportProgress(null, this);
                } else {
                  statusLog.setTestCaseDescription(testCaseMessage.getDescription());
                  TestRunner testRunner = new TestRunner();
                  testRunner.setValidateResponse(false);
                  statusLog.setStatusMessage("Running test case");
                  try {
                    testRunner.runTest(connector, testCaseMessage);
                    sendCount++;
                  } catch (Throwable t) {
                    statusLog
                        .setProblemMessage("Unable to run test case, exception: " + t.getMessage());
                    statusLog.setException(t);
                  }
                  statusLog.setStatusMessage("Reporting results");
                  CertifyRunner.reportProgress(testCaseMessage, this);
                  statusLog.setStatusMessage("Test completed");
                  System.out
                      .println("  + Tested " + sendData.getTestParticipant().getOrganizationName()
                          + ", result is " + testCaseMessage.getActualResultStatus().toUpperCase()
                          + " for test case " + testCaseMessage.getDescription() + "");
                }
              }
            }
          }
        }

        
      } catch (IOException e) {
        e.printStackTrace();
        return;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    System.out.println("Shutdown confirmed: " + aartUrl + " #" + threadId);
  }

  public static void main(String[] args) throws Exception {
    ConnectionManager.setScanDirectories(false);
    if (args.length < 1) {
      System.err.println("Usage: java  org.immregistries.smm.tester.CertifyClient config.txt");
      System.err.println("The following options are supported in the config file: ");
      System.err
          .println("  + AART Name: [Required: Name of this service as it will appear in AART]");
      System.err
          .println("  + Scan Folder: [Required: Root folder location to look for configurations]");
      System.err.println("  + Key Store: [Optional: Location of keystore]");
      System.err.println("  + Sun Security Allow Unsafe Renegotiation: [Optional: true | false]");
      return;
    }
    File configFile = new File(args[0]);
    if (!configFile.exists() || !configFile.isFile()) {
      System.err.println("Unable to find config file " + configFile);
      PrintWriter out = new PrintWriter(new FileOutputStream(configFile));
      {
        out.println("AART Name: ");
        out.println("AART URL: http://app.immregistries.org/aart/");
        out.println("Scan Folder: ");
        out.println("Key Store: ");
        out.println("Key Store Password: ");
        out.println("Sun Security Allow Unsafe Renegotiation: true");
      }
      out.close();
      System.err.println("  + Created default config file, please edit and retry ");
      return;
    }
    System.out.println("Starting Tester Client");
    System.out.println("  + Version: " + SoftwareVersion.VERSION);
    System.out.println("  + Initializing send data manager");
    ConnectionManager connectionManager = initManagerServlet(args);
    if (aartName == null) {
      System.err.println("Unable to start: AART Name is not defined in configuration file ");
      return;
    } else if (aartUrlList.size() == 0) {
      System.err.println("Unable to start: AART URL is not defined");
      return;
    }
    connectionManager.init();

    System.out.println("Service will start in 10 seconds...");
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException ie) {
      // just keep going
    }
    System.out.println("Service will start in 5 seconds...");
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException ie) {
      // just keep going
    }
    List<CertifyClient> certifyClientList = new ArrayList<CertifyClient>();
    for (AartUrl aartUrl : aartUrlList) {
      if (aartUrl.getSectionTypeList().size() > 0) {
        int count = 0;
        for (String sectionTypes : aartUrl.getSectionTypeList()) {
          count++;
          CertifyClient cc = new CertifyClient(aartUrl.getUrl());
          cc.sectionTypes = sectionTypes;
          cc.threadId = "" + count;
          certifyClientList.add(cc);
        }
      } else {
        CertifyClient cc = new CertifyClient(aartUrl.getUrl());
        certifyClientList.add(cc);
      }
    }
    System.out.println("Connecting to:");
    for (CertifyClient cc : certifyClientList) {
      System.out.println("  + " + cc.getAartUrl() + " #" + cc.getThreadId());
      cc.start();
    }
    System.out.println("Ready to go, available commands: ");
    printHelp();
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.print(">> ");
      String command = scanner.nextLine();
      if (command.equalsIgnoreCase("help")) {
        printHelp();
      } else if (command.equalsIgnoreCase("shutdown")) {
        break;
      } else if (command.equalsIgnoreCase("status")) {
        for (CertifyClient cc : certifyClientList) {
          System.out.print("   " + cc.aartUrl + " #" + cc.threadId);
          SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
          System.out
              .println(" - "
                  + (cc.getAartConnectStatusDate() == null ? ""
                      : sdf.format(cc.getAartConnectStatusDate()))
                  + " - " + cc.getAartConnectStatus());
          if (cc.statusLog != null) {
            printOutStatusLog(cc.statusLog);
          }
          if (cc.sendCount > 0) {
            System.out.println("     Count:   " + cc.sendCount);
          }
        }
      } else if (command.equalsIgnoreCase("recent")) {
        synchronized (statusLogList) {
          int start = statusLogList.size() - 25;
          if (start < 0) {
            start = 0;
          }
          for (int i = start; i < statusLogList.size(); i++) {
            StatusLog statusLog = statusLogList.get(i);
            System.out.println(
                "   " + statusLog.getTestMessageId() + " -- " + statusLog.getStatusMessage());
          }
        }
      } else {
        StatusLog statusLog = getStatusLog(command);
        if (statusLog == null) {
          System.out.println("Test case number not recognized");
        } else {
          System.out.println("Status for test case " + command);
          printOutStatusLog(statusLog);
        }
      }
    }
    scanner.close();
    System.out.println("Shutting down senders...");
    for (CertifyClient cc : certifyClientList) {
      cc.stopRunning();
      System.out.println("  + " + cc.aartUrl + " #" + cc.threadId);
    }
    System.out.println("Shutting down connection manager...");
    ShutdownInterceptor shutdown = new ShutdownInterceptor();
    shutdown.run();
  }

  public static void printHelp() {
    System.out.println(" + status");
    System.out.println(" + recent");
    System.out.println(" + shutdown");
    System.out.println(" + [test case number]");
  }

  public static void printOutStatusLog(StatusLog statusLog) {
    System.out.println("     + Status:    " + statusLog.getStatusMessage());
    System.out.println("     + IIS ID:    " + statusLog.getAartPublicIdCode());
    System.out.println("     + Test ID:   " + statusLog.getTestMessageId());
    System.out.println("     + Test Desc: " + statusLog.getTestCaseDescription());
    System.out.println("     + Org:       " + statusLog.getOrganizationName());;
    if (statusLog.getProblemMessage() != null) {
      System.err.println("     + Problem:   " + statusLog.getProblemMessage());
    }
    if (statusLog.getException() != null) {
      statusLog.getException().printStackTrace(System.err);
    }
  }


  public static ConnectionManager initManagerServlet(String[] args)
      throws FileNotFoundException, IOException {
    ConnectionManager connectionManager = new ConnectionManager();
    BufferedReader in = new BufferedReader(new FileReader(args[0]));
    String line;
    AartUrl aartUrl = null;
    while ((line = in.readLine()) != null) {
      int pos = line.indexOf(":");
      if (pos > 0) {
        String key = line.substring(0, pos).trim();
        String value = line.substring(pos + 1).trim();
        if (value.length() > 0) {
          if (key.equals("AART Name")) {
            aartName = value;
          } else if (key.equals("AART URL")) {
            aartUrl = new AartUrl(value);
            aartUrlList.add(aartUrl);
          } else if (key.equals("AART Thread")) {
            if (aartUrl != null) {
              aartUrl.getSectionTypeList().add(value);
            }
          } else if (key.equals("Key Store")) {
            connectionManager.setKeyStore(value);
          } else if (key.equals("Key Store Password")) {
            connectionManager.setKeyStorePassword(value);
          } else if (key.equals("Sun Security Allow Unsafe Renegotiation: ")) {
            connectionManager
                .setSunSecuritySslAllowUnsafeRenegotiation(value.equalsIgnoreCase("true"));
          } else if (key.equals("Scan Folder")) {
            connectionManager.setScanStartFolders(value);
          }
        }
      }
    }
    in.close();
    return connectionManager;

  }


}
