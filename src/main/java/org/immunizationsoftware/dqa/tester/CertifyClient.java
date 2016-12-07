/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.immunizationsoftware.dqa.mover.ConnectionManager;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.certify.CertifyRunner;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.profile.ProfileManager;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsage;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;
import org.openimmunizationsoftware.dqa.tr.RecordServletInterface;

/**
 * 
 * @author nathan
 */
public class CertifyClient {

  private static ProfileManager profileManager = null;
  private static Map<String, Map<String, TestCaseMessage>> testMessageMapMap = new HashMap<String, Map<String, TestCaseMessage>>();
  private static SendData sendData = null;
  private static Connector connector = null;
  private static int profileUsageIdForConformance = 0;
  private static String aartName = null;
  private static String queryType = CertifyRunner.QUERY_TYPE_NONE;
  private static boolean runA = true;
  private static boolean runB = true;
  private static boolean runC = true;
  private static boolean runD = true;
  private static boolean runE = false;
  private static boolean runF = false;
  private static boolean runG = true;
  private static boolean runH = false;
  private static boolean runI = true;
  private static boolean runJ = true;
  private static boolean runK = true;
  private static boolean runL = true;
  private static boolean runM = true;
  private static boolean runN = true;
  private static boolean runO = true;
  private static boolean runP = true;
  private static boolean runQ = true;
  private static boolean runS = true;
  private static boolean runT = true;
  private static boolean pauseBeforeQuerying = false;
  private static boolean redactListResponses = true;
  private static boolean reportErrorsOnly = true;
  private static boolean condenseErrors = true;
  private static List<ForecastTestPanel> forecastTestPanelList = new ArrayList<ForecastTestPanel>();
  private static String runAgainst = null;
  private static CertifyRunner certifyRunner = null;

  static {
    for (ForecastTestPanel forecastTestPanel : ForecastTestPanel.values()) {
      if (forecastTestPanel.isStandard()) {
        forecastTestPanelList.add(forecastTestPanel);
      }
    }
  }

  private static void initRuns() {
    if (sendData.getTestParticipant() != null) {
      queryType = sendData.getTestParticipant().getQueryType();
      redactListResponses = sendData.getTestParticipant().isRedactListResponses();
    }
    runA = true;
    runB = true;
    runC = true;
    runD = true;
    runE = false;
    runF = !queryType.equals(CertifyRunner.QUERY_TYPE_NONE);
    runG = true;
    runH = false;
    runI = true;
    runJ = true;
    runK = true;
    runL = true;
    if (sendData != null && sendData.getConnector() != null) {
      if (sendData.getConnector().isVerify()) {
        runL = false;
      }
    }
    runM = !queryType.equals(CertifyRunner.QUERY_TYPE_NONE);
    runN = true;
    runO = true;
    runP = !queryType.equals(CertifyRunner.QUERY_TYPE_NONE);
    runQ = !queryType.equals(CertifyRunner.QUERY_TYPE_NONE);
    runS = true;
    runT = !queryType.equals(CertifyRunner.QUERY_TYPE_NONE);
  }

  public static void main(String[] args) throws Exception {
    ConnectionManager.setScanDirectories(false);
    if (args.length < 1) {
      System.err.println("Usage: java org.immunizationsoftware.dqa.tester.CertifyClient config.txt");
      System.err.println("The following options are supported in the config file: ");
      System.err.println("  + AART Name: [Required: Name of this service as it will appear in AART]");
      System.err.println("  + Scan Folder: [Required: Root folder location to look for configurations]");
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
    profileManager = new ProfileManager();
    certifyRunner = null;
    try {
      TimeUnit.SECONDS.sleep(10);
    } catch (InterruptedException ie) {
      // just keep going
    }
    System.out.println("  + Ready to go");
    while (true) {
      try {

        boolean canStart = certifyRunner == null || certifyRunner.getStatus().equals(CertifyRunner.STATUS_COMPLETED)
            || certifyRunner.getStatus().equals(CertifyRunner.STATUS_STOPPED) || certifyRunner.getStatus().equals(CertifyRunner.STATUS_PROBLEM);

        String aartAction = null;
        String autoTestNameSelect = null;
        if (canStart) {
          if (certifyRunner != null) {
            String testerStatus = RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_FINISHED;
            if (certifyRunner.getStatus().equals(CertifyRunner.STATUS_COMPLETED)) {
              System.out.println("  + Testing has completed");
              testerStatus = RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_FINISHED;
            } else if (certifyRunner.getStatus().equals(CertifyRunner.STATUS_PROBLEM)) {
              System.out.println("  + Testing has had a problem, error being reported");
              testerStatus = RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_ERROR;
            } else if (certifyRunner.getStatus().equals(CertifyRunner.STATUS_STOPPED)) {
              System.out.println("  + Testing was stopped");
              testerStatus = RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_STOPPED;
            }
            aartAction = CertifyRunner.reportStatus(aartName, testerStatus, certifyRunner.getConnector(), certifyRunner.getTestStarted(),
                certifyRunner.getUpdateEtc().getDate(), certifyRunner.getQueryEtc().getDate());
            certifyRunner = null;
          }
          aartAction = CertifyRunner.reportStatus(aartName, RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_READY, null, null, null, null);
          if (aartAction.equals("")) {
            aartAction = null;
          } else {
            if (aartAction.startsWith(RecordServletInterface.PARAM_TESTER_ACTION_START)) {
              {
                int pos = aartAction.indexOf(RecordServletInterface.OPTION_AUTO_TEST_NAME_SELECT);
                if (pos > 0) {
                  autoTestNameSelect = aartAction.substring(pos + RecordServletInterface.OPTION_AUTO_TEST_NAME_SELECT.length()).trim();
                  aartAction = aartAction.substring(0, pos).trim();
                }
              }
              String aartPublicIdCode = aartAction.substring(6);
              System.out.println("Start command received");
              System.out.println("  + AART Public Id Code: " + aartPublicIdCode);
              for (SendData sd1 : ConnectionManager.getSendDataList()) {
                if (sd1.getConnector() != null && sd1.getConnector().getAartPublicIdCode().equals(aartPublicIdCode)) {
                  sendData = ConnectServlet.addNewConnection(null, sd1.getInternalId(), true);
                  connector = sendData.getConnector();
                  break;
                }
              }
              if (connector == null) {
                System.err.println("  + Problem, connector was not intialized ");
                continue;
              } else if (sendData.getTestParticipant() == null) {
                System.err.println("  + Problem, test participant is not recognized and can't be reported to AART ");
                continue;
              } else if (sendData.getTestParticipant().getProfileUsage() == null)
              {
                System.err.println("  + Problem, test participant has no profile usage, unable to run ");
                continue;
              }

              System.out.println("  + Test Participant Organization: " + sendData.getTestParticipant().getOrganizationName());
              System.out.println("  + Initializing certify runner");
              initRuns();
              setupCertifyRunner();
              certifyRunner.start();
              System.out.println("  + Certified runner started");
              try {
                // give the certifyRunner 10 seconds to get started before
                // showing
                // status
                TimeUnit.SECONDS.sleep(10);
              } catch (InterruptedException ie) {
                // just keep going
              }
            }
          }
        } else {
          certifyRunner.printTextUpdate(System.out);
          aartAction = CertifyRunner.reportStatus(aartName, RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_TESTING,
              (certifyRunner == null ? null : certifyRunner.getConnector()), certifyRunner.getTestStarted(), certifyRunner.getUpdateEtc().getDate(),
              certifyRunner.getQueryEtc().getDate());

          if (aartAction.equals("")) {
            aartAction = null;
          } else {
            if (aartAction.equals(RecordServletInterface.PARAM_TESTER_ACTION_STOP)) {
              System.out.println("Stop command received");
              if (certifyRunner != null) {
                certifyRunner.switchStatus(CertifyRunner.STATUS_STOPPED, "Process stopped by user");
                certifyRunner.stopRunning();
              }
            }
          }
        }

        if (certifyRunner != null) {
          if (certifyRunner.getStatus().equals(CertifyRunner.STATUS_STARTED)) {
            // Stop the certify runner if it has spent more than 15 minutes and
            // appears to be stuck
            if ((System.currentTimeMillis() - certifyRunner.getLastLogStatus()) > 15 * 60 * 1000) {
              SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
              String lastUpdate = sdf.format(new Date(certifyRunner.getLastLogStatus()));
              certifyRunner.switchStatus(CertifyRunner.STATUS_PROBLEM, "Stopping process, no update logged since " + lastUpdate);
              System.out.println("## PROBLEM: Process frozen since " + lastUpdate);
              certifyRunner.stopRunning();
            }
          }
        }

        try {
          TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException ie) {
          // just keep going
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void setupCertifyRunner() {
    certifyRunner = new CertifyRunner(connector, sendData, queryType);
    certifyRunner.setRun(runA, CertifyRunner.SUITE_A_BASIC);
    certifyRunner.setRun(runB, CertifyRunner.SUITE_B_INTERMEDIATE);
    certifyRunner.setRun(runC, CertifyRunner.SUITE_C_ADVANCED);
    certifyRunner.setRun(runD, CertifyRunner.SUITE_D_EXCEPTIONAL);
    certifyRunner.setRun(runE, CertifyRunner.SUITE_E_FORECAST_PREP);
    certifyRunner.setRun(runF, CertifyRunner.SUITE_F_FORECAST);
    certifyRunner.setRun(runG, CertifyRunner.SUITE_G_PERFORMANCE);
    certifyRunner.setRun(runH, CertifyRunner.SUITE_H_CONFORMANCE);
    certifyRunner.setRun(runI, CertifyRunner.SUITE_I_PROFILING);
    certifyRunner.setRun(runJ, CertifyRunner.SUITE_J_ONC_2015);
    certifyRunner.setRun(runK, CertifyRunner.SUITE_K_NOT_ACCEPTED);
    certifyRunner.setRun(runL, CertifyRunner.SUITE_L_CONFORMANCE_2015);
    certifyRunner.setRun(runM, CertifyRunner.SUITE_M_QBP_SUPPORT);
    certifyRunner.setRun(runN, CertifyRunner.SUITE_N_TRANSFORM);
    certifyRunner.setRun(runO, CertifyRunner.SUITE_O_EXTRA);
    certifyRunner.setRun(runP, CertifyRunner.SUITE_P_DEDUPLICATION_ENGAGED);
    certifyRunner.setRun(runQ, CertifyRunner.SUITE_Q_FORECASTER_ENGAGED);
    certifyRunner.setRun(runS, CertifyRunner.SUITE_S_ASSESSMENT_FOR_SUBMISSION);
    certifyRunner.setRun(runT, CertifyRunner.SUITE_T_ASSESSMENT_FOR_QUERY);
    certifyRunner.setPauseBeforeQuerying(pauseBeforeQuerying);
    if (certifyRunner.isPauseBeforeQuerying()) {
      System.out.println("     - Pause before querying");
    }
    certifyRunner.setRedactListResponses(redactListResponses);
    if (certifyRunner.isRedactListResponses()) {
      System.out.println("     - Redact List Responses");
    }
    certifyRunner.setReportErrorsOnly(reportErrorsOnly);
    if (certifyRunner.isReportErrorsOnly()) {
      System.out.println("     - Report Errors Only");
    }
    certifyRunner.setCondenseErrors(condenseErrors);
    if (certifyRunner.isCondenseErrors()) {
      System.out.println("     - Condense Errors");
    }
    certifyRunner.setTestMessageMapMap(testMessageMapMap);
    if (certifyRunner.isRun(CertifyRunner.SUITE_E_FORECAST_PREP)) {
      System.out.println("     - run suite Forecast Prep");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_Q_FORECASTER_ENGAGED)) {
      System.out.println("     - run suite Forecaster Engaged");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_F_FORECAST)) {
      System.out.println("     - run suite Forecast with " + forecastTestPanelList.size() + " test panel(s)");
      for (ForecastTestPanel forecastTestPanel : forecastTestPanelList) {
        certifyRunner.addForecastTestPanel(forecastTestPanel);
        System.out.println("        forecast test panel: " + forecastTestPanel.getLabel());
      }
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_I_PROFILING)) {
      System.out.println("     - run suite Profiling");
      certifyRunner.setProfileManager(profileManager);
      if (sendData.getTestParticipant().getProfileUsage() == null) {
        System.out.println("         profile usage is not defined for " + sendData.getTestParticipant().getProfileUsageId());
      } else {
        certifyRunner.setProfileUsage(sendData.getTestParticipant().getProfileUsage());
        System.out.println("          profile usage '" + sendData.getTestParticipant().getProfileUsage() + "'");
      }
      for (ProfileUsage profileUsage : profileManager.getProfileUsageList()) {
        if (profileUsage.toString().equals("US - Base")) {
          certifyRunner.setProfileUsageComparisonConformance(profileUsage);
          System.out.println("         comparing to '" + profileUsage + "'");
        }
      }
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_A_BASIC)) {
      System.out.println("     - run suite Basic");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_K_NOT_ACCEPTED)) {
      System.out.println("     - run suite Not Accepted");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_J_ONC_2015)) {
      System.out.println("     - run suite ONC 2015");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_B_INTERMEDIATE)) {
      System.out.println("     - run suite Intermediate");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_C_ADVANCED)) {
      System.out.println("     - run suite Advanced");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_D_EXCEPTIONAL)) {
      System.out.println("     - run suite Exceptional");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_N_TRANSFORM)) {
      System.out.println("     - run suite Transform");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_M_QBP_SUPPORT)) {
      System.out.println("     - run suite QBP Support");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_O_EXTRA)) {
      System.out.println("     - run suite Extra");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_P_DEDUPLICATION_ENGAGED)) {
      System.out.println("     - run suite Deduplication Engaged");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_G_PERFORMANCE)) {
      System.out.println("     - run suite Performance");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_H_CONFORMANCE)) {
      System.out.println("     - run suite Conformance");
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_L_CONFORMANCE_2015)) {
      System.out.println("     - run suite Conformance 2015");
    }

    if (runAgainst != null && !runAgainst.equals("")) {
      certifyRunner.setRunAgainstTestStartTime(runAgainst);
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
            connectionManager.setSunSecuritySslAllowUnsafeRenegotiation(value.equalsIgnoreCase("true"));
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
        System.err.println("Unable to find key store file here: " + keyStoreFile.getCanonicalPath());
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
              return arg0.isDirectory() && !arg0.getName().startsWith(CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX);
            }
          });
          if (dirs != null) {
            for (File dir : dirs) {
              CreateTestCaseServlet.readTestCases(testMessageMapMap, dir, dir.getName(), false);
            }
          }
        }
      }
      {
        File globalDir = new File(sendData.getRootDir().getParentFile(), "_global");
        if (globalDir.exists() && globalDir.isDirectory()) {
          CreateTestCaseServlet.readTestCases(testMessageMapMap, globalDir, null, true);
          File[] dirs = globalDir.listFiles(new FileFilter() {
            public boolean accept(File arg0) {
              return arg0.isDirectory() && !arg0.getName().startsWith(CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX);
            }
          });
          if (dirs != null) {
            for (File dir : dirs) {
              CreateTestCaseServlet.readTestCases(testMessageMapMap, dir, dir.getName(), true);
            }
          }
        }
      }
    }
  }

}
