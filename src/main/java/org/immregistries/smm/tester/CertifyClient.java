/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.immregistries.smm.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.immregistries.smm.RecordServletInterface;
import org.immregistries.smm.mover.ConnectionManager;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.tester.certify.CertifyRunner;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.run.TestRunner;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.forecast.ForecastTestPanel;

/**
 * 
 * @author nathan
 */
public class CertifyClient {

  private static Map<String, Map<String, TestCaseMessage>> testMessageMapMap =
      new HashMap<String, Map<String, TestCaseMessage>>();
  private static SendData sendData = null;
  private static Connector connector = null;
  private static String aartName = null;

  private static List<ForecastTestPanel> forecastTestPanelList = new ArrayList<ForecastTestPanel>();

  static {
    for (ForecastTestPanel forecastTestPanel : ForecastTestPanel.values()) {
      if (forecastTestPanel.isStandard()) {
        forecastTestPanelList.add(forecastTestPanel);
      }
    }
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
    System.out.println("  + Initializing send data manager");
    initManagerServlet(args);
    if (aartName == null) {
      System.err.println("AART name is not defined in configuration file ");
      return;
    }
    System.out.println("  + Initializing profile manager");
    try {
      TimeUnit.SECONDS.sleep(10);
    } catch (InterruptedException ie) {
      // just keep going
    }
    System.out.println("  + Ready to go");
    while (true) {
      try {

        String aartAction = null;
        String autoTestNameSelect = null;
         {
          aartAction = CertifyRunner.reportStatus(aartName,
              RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_READY, null);
          if (aartAction.equals("")) {
            aartAction = null;
          } else {
            if (aartAction.startsWith(RecordServletInterface.PARAM_TESTER_ACTION_RUN)) {
              String[] actionArgs = aartAction.split("\\s");
              if (actionArgs.length > 2) {
                String aartPublicIdCode = actionArgs[1];
                String testMessageId = actionArgs[2];
                System.out.println("Run command received");
                System.out.println("  + AART Public Id Code: " + aartPublicIdCode);
                System.out.println("  + Test Message Id:     " + testMessageId);
                for (SendData sd1 : ConnectionManager.getSendDataList()) {
                  if (sd1.getConnector() != null
                      && sd1.getConnector().getAartPublicIdCode().equals(aartPublicIdCode)) {
                    sendData = ConnectServlet.addNewConnection(null, sd1.getInternalId(), true);
                    connector = sendData.getConnector();
                    break;
                  }
                }
                if (connector == null) {
                  System.err.println("  + Problem, connector was not intialized ");
                  continue;
                } else if (sendData.getTestParticipant() == null) {
                  System.err.println(
                      "  + Problem, test participant is not recognized and can't be reported to AART ");
                  continue;
                }

                System.out.println("  + Test Participant Organization: "
                    + sendData.getTestParticipant().getOrganizationName());
                System.out.println("Retrieving test case message");
                TestCaseMessage testCaseMessage = CertifyRunner.getTestCaseMessage(testMessageId);
                if (testCaseMessage == null) {
                  System.err.println("  + Couldn't load test case " + testMessageId);
                  System.err.println("Not able to run test");
                } else {
                  System.out.println("  + Found " + testCaseMessage.getDescription());
                  TestRunner testRunner = new TestRunner();
                  testRunner.setValidateResponse(false);
                  System.out.println("Running test");
                  boolean passed = false;
                  try {
                    passed = testRunner.runTest(connector, testCaseMessage);
                    System.out.println("  + result " + testCaseMessage.getActualResultStatus());
                    if (passed) {
                      System.out.println("  + test PASSED");
                    } else {
                      System.out.println("  + test FAILED");
                    }
                  } catch (Throwable t) {
                    System.err.println("Exception running test case message: " + t.getMessage());
                    t.printStackTrace(System.err);
                  }
                  System.out.println("Reporting results");
                  CertifyRunner.reportProgress(testCaseMessage, testMessageId, sendData);
                  System.out.println("Completed");
                }

              }
            }
          }
        } 

        if (aartAction == null
            || !aartAction.startsWith(RecordServletInterface.PARAM_TESTER_ACTION_RUN)) {
          try {
            TimeUnit.SECONDS.sleep(30);
          } catch (InterruptedException ie) {
            // just keep going
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
        return;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

 
  public static void initManagerServlet(String[] args) throws FileNotFoundException, IOException {
    ConnectionManager connectionManager = new ConnectionManager();
    BufferedReader in = new BufferedReader(new FileReader(args[0]));
    String line;
    while ((line = in.readLine()) != null) {
      int pos = line.indexOf(":");
      if (pos > 0) {
        String key = line.substring(0, pos).trim();
        String value = line.substring(pos + 1).trim();
        if (value.length() > 0) {
          if (key.equals("AART Name")) {
            aartName = value;
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
    connectionManager.init();
  }

  protected static void setupKeystore() throws IOException {
    if (connector.getKeyStorePassword() != null && !connector.getKeyStorePassword().equals("")) {
      File keyStoreFile = new File(sendData.getRootDir(), SendData.KEYSTORE_FILE_NAME);
      if (keyStoreFile.exists()) {
        System.setProperty("javax.net.ssl.keyStore", keyStoreFile.getCanonicalPath());
        System.setProperty("javax.net.ssl.keyStorePassword", connector.getKeyStorePassword());
        System.out.println("Set keystore to be: " + keyStoreFile.getCanonicalPath());
      } else {
        System.err
            .println("Unable to find key store file here: " + keyStoreFile.getCanonicalPath());
      }
    }
  }

  protected static void loadTestCases() throws IOException {
    {
      File testCaseDir = sendData.getTestCaseDir(false);
      if (testCaseDir != null) {
        CreateTestCaseServlet.readTestCases(testMessageMapMap, testCaseDir, null, false);
        {
          File[] dirs = testCaseDir.listFiles(new FileFilter() {
            public boolean accept(File arg0) {
              return arg0.isDirectory() && !arg0.getName()
                  .startsWith(CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX);
            }
          });
          if (dirs != null) {
            for (File dir : dirs) {
              CreateTestCaseServlet.readTestCases(testMessageMapMap, dir, dir.getName(), false);
            }
          }
        }
      }
      CreateTestCaseServlet.setupGlobalTestCases(testMessageMapMap);
    }
  }

}
