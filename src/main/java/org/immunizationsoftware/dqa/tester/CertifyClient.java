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
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.certify.CertifyRunner;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponseManager;
import org.immunizationsoftware.dqa.tester.profile.ProfileManager;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsage;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;
import org.openimmunizationsoftware.dqa.tr.RecordServletInterface;

/**
 * 
 * @author nathan
 */
public class CertifyClient
{

  private static ProfileManager profileManager = null;
  private static Map<String, Map<String, TestCaseMessage>> testMessageMapMap = new HashMap<String, Map<String, TestCaseMessage>>();
  private static SendData sendData = null;
  private static Connector connector = null;
  private static int profileUsageId = 0;
  private static int profileUsageIdForInteroperability = 0;
  private static int profileUsageIdForConformance = 0;
  private static String aartName = null;
  private static ParticipantResponse participantResponse = null;
  private static ParticipantResponse[][] participantResponseMap = null;
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
  private static boolean pauseBeforeQuerying = false;
  private static boolean redactListResponses = true;
  private static boolean reportErrorsOnly = true;
  private static boolean condenseErrors = true;
  private static List<ForecastTestPanel> forecastTestPanelList = new ArrayList<ForecastTestPanel>();
  private static ManagerServletConfig servletConfig = null;
  private static ManagerServlet managerServlet = null;
  private static String runAgainst = null;
  private static CertifyRunner certifyRunner = null;

  static {
    for (ForecastTestPanel forecastTestPanel : forecastTestPanelList) {
      if (forecastTestPanel.isStandard()) {
        forecastTestPanelList.add(forecastTestPanel);
      }
    }
  }

  private static void initRuns() {
    if (participantResponse != null) {
      queryType = participantResponse.getQueryType();
      redactListResponses = participantResponse.isRedactListResponses();
    }
    runA = true;
    runB = true;
    runC = true;
    runD = true;
    runE = false;
    runF = false;
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
  }

  private static class ManagerServletContext implements ServletContext
  {

    public ServletContext getContext(String uripath) {
      // TODO Auto-generated method stub
      return null;
    }

    public int getMajorVersion() {
      // TODO Auto-generated method stub
      return 0;
    }

    public int getMinorVersion() {
      // TODO Auto-generated method stub
      return 0;
    }

    public String getMimeType(String file) {
      // TODO Auto-generated method stub
      return null;
    }

    public URL getResource(String path) throws MalformedURLException {
      // TODO Auto-generated method stub
      return null;
    }

    public InputStream getResourceAsStream(String path) {
      // TODO Auto-generated method stub
      return null;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
      // TODO Auto-generated method stub
      return null;
    }

    public RequestDispatcher getNamedDispatcher(String name) {
      // TODO Auto-generated method stub
      return null;
    }

    public Servlet getServlet(String name) throws ServletException {
      // TODO Auto-generated method stub
      return null;
    }

    public Enumeration getServlets() {
      // TODO Auto-generated method stub
      return null;
    }

    public Enumeration getServletNames() {
      // TODO Auto-generated method stub
      return null;
    }

    public void log(String msg) {
      System.out.println(msg);
    }

    public void log(Exception exception, String msg) {
      // TODO Auto-generated method stub

    }

    public void log(String message, Throwable throwable) {
      // TODO Auto-generated method stub

    }

    public String getRealPath(String path) {
      // TODO Auto-generated method stub
      return null;
    }

    public String getServerInfo() {
      // TODO Auto-generated method stub
      return null;
    }

    public String getInitParameter(String name) {
      // TODO Auto-generated method stub
      return null;
    }

    public Enumeration getInitParameterNames() {
      // TODO Auto-generated method stub
      return null;
    }

    public Object getAttribute(String name) {
      // TODO Auto-generated method stub
      return null;
    }

    public Enumeration getAttributeNames() {
      // TODO Auto-generated method stub
      return null;
    }

    public Set getResourcePaths(String arg0) {
      // TODO Auto-generated method stub
      return null;
    }

    public String getServletContextName() {
      // TODO Auto-generated method stub
      return null;
    }

    public void setAttribute(String name, Object object) {
      // TODO Auto-generated method stub

    }

    public void removeAttribute(String name) {
      // TODO Auto-generated method stub

    }

  }

  private static class ManagerServletConfig implements ServletConfig
  {

    private Map<String, String> parameterMap = new HashMap<String, String>();
    private ServletContext servletContext = new ManagerServletContext();

    public ServletContext getServletContext() {
      return servletContext;
    }

    public void setInitParameter(String key, String name) {
      parameterMap.put(key, name);
    }

    public String getInitParameter(String name) {
      return parameterMap.get(name);
    }

    public Enumeration getInitParameterNames() {
      return null;
    }

    public String getServletName() {
      return "Tester";
    }

  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("Usage: java org.immunizationsoftware.dqa.tester.CeritfyClient config.txt");
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
    if (servletConfig.getInitParameter(ManagerServlet.INIT_PARAM_SCAN_START_FOLDERS) == null) {
      System.err.println("Scan Folder is not defined in configuration file");
      return;
    }

    if (ManagerServlet.getRequirementTestFieldsFile() == null) {
      System.out.println("  + Requirements test fields file is missing, unable to initialize");
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
            || certifyRunner.getStatus().equals(CertifyRunner.STATUS_STOPPED)
            || certifyRunner.getStatus().equals(CertifyRunner.STATUS_PROBLEM);

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
            aartAction = CertifyRunner.reportStatus(aartName, testerStatus, certifyRunner.getConnector().getLabel(),
                certifyRunner.getTestStarted(), certifyRunner.getUpdateEtc(), certifyRunner.getQueryEtc());
            certifyRunner = null;
          }
          aartAction = CertifyRunner.reportStatus(aartName,
              RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_READY, null, null, null, null);
          if (aartAction.equals("")) {
            aartAction = null;
          } else {
            if (aartAction.startsWith(RecordServletInterface.PARAM_TESTER_ACTION_START)) {
              {
                int pos = aartAction.indexOf(RecordServletInterface.OPTION_AUTO_TEST_NAME_SELECT);
                if (pos > 0) {
                  autoTestNameSelect = aartAction
                      .substring(pos + RecordServletInterface.OPTION_AUTO_TEST_NAME_SELECT.length()).trim();
                  aartAction = aartAction.substring(0, pos).trim();
                }
              }
              String connectionLabel = aartAction.substring(6);
              System.out.println("Start command received");
              System.out.println("  + Connection label: " + connectionLabel);
              System.out.println("  + Initializing participant response map");
              inittParticipantResponseMap();
              for (int i = 0; i < participantResponseMap.length; i++) {
                for (int j = 0; j < participantResponseMap[i].length; j++) {
                  if (participantResponseMap[i][j] != null
                      && participantResponseMap[i][j].getFolderName().equals(connectionLabel)) {
                    participantResponse = participantResponseMap[i][j];
                    switchParticipantResponse();
                    i = participantResponseMap.length;
                    System.out.println("  + Found participant details");
                    break;
                  }
                }
              }
              if (connector == null) {
                System.err.println("  + Problem, connector was not intialized ");
                continue;
              }
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
          aartAction = CertifyRunner.reportStatus(aartName,
              RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_TESTING, certifyRunner.getConnector().getLabel(),
              certifyRunner.getTestStarted(), certifyRunner.getUpdateEtc(), certifyRunner.getQueryEtc());

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
              certifyRunner.switchStatus(CertifyRunner.STATUS_PROBLEM,
                  "Stopping process, no update logged since " + lastUpdate);
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
    certifyRunner.setParticipantResponse(participantResponse);
    certifyRunner.setRun(true, CertifyRunner.SUITE_A_BASIC);
    certifyRunner.setRun(true, CertifyRunner.SUITE_A_BASIC);
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
    certifyRunner.setPauseBeforeQuerying(pauseBeforeQuerying);
    certifyRunner.setRedactListResponses(redactListResponses);
    certifyRunner.setReportErrorsOnly(reportErrorsOnly);
    certifyRunner.setCondenseErrors(condenseErrors);
    certifyRunner.setTestMessageMapMap(testMessageMapMap);
    if (certifyRunner.isRun(CertifyRunner.SUITE_F_FORECAST)) {
      for (ForecastTestPanel forecastTestPanel : forecastTestPanelList) {
        certifyRunner.addForecastTestPanel(forecastTestPanel);
      }
    }
    if (certifyRunner.isRun(CertifyRunner.SUITE_I_PROFILING)) {
      certifyRunner.setProfileManager(profileManager);
      if (profileUsageId > 0) {
        certifyRunner.setProfileUsage(profileManager.getProfileUsageList().get(profileUsageId - 1));
      }
      if (profileUsageIdForInteroperability > 0) {
        certifyRunner.setProfileUsageComparisonInteroperability(
            profileManager.getProfileUsageList().get(profileUsageIdForInteroperability - 1));
      }
      if (profileUsageIdForConformance < 0) {
        certifyRunner.setProfileUsageComparisonConformance(
            profileManager.getProfileUsageList().get(profileUsageIdForConformance - 1));
      }
    }

    if (runAgainst != null && !runAgainst.equals("")) {
      certifyRunner.setRunAgainstTestStartTime(runAgainst);
    }
  }

  public static void initManagerServlet(String[] args) throws FileNotFoundException, IOException, ServletException {
    servletConfig = new ManagerServletConfig();
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
            servletConfig.setInitParameter(ManagerServlet.INIT_PARAM_KEY_STORE, value);
          } else if (key.equals("Key Store Password")) {
            servletConfig.setInitParameter(ManagerServlet.INIT_PARAM_KEY_STORE_PASSWORD, value);
          } else if (key.equals("Sun Security Allow Unsafe Renegotiation: ")) {
            servletConfig.setInitParameter(ManagerServlet.INIT_PARAM_SUN_SECURITY_SSL_ALLOW_UNSAFE_RENEGOTIATION,
                value);
          } else if (key.equals("Scan Folder")) {
            servletConfig.setInitParameter(ManagerServlet.INIT_PARAM_SCAN_START_FOLDERS, value);
          }
        }
      }
    }
    in.close();
    servletConfig.setInitParameter(ManagerServlet.INIT_PARAM_SUN_SECURITY_SSL_ALLOW_UNSAFE_RENEGOTIATION, "true");
    servletConfig.setInitParameter(ManagerServlet.INIT_PARAM_FOLDER_SCAN_ENABLED, "false");
    ManagerServlet managerServlet = new ManagerServlet();
    managerServlet.init(servletConfig);
  }

  private static void switchParticipantResponse() throws IOException {
    String folderName = participantResponse.getFolderName();
    if (folderName.equals("")) {
      System.err.println("Sender does not have a folder name it is connected to");
    } else {
      sendData = ManagerServlet.getSendDatayByLabel(folderName);
      if (sendData != null && sendData.getConnector() != null) {
        connector = sendData.getConnector();
        if (connector == null) {
          System.err.println("Connection is not setup properly, can't find connection");
        }
        testMessageMapMap.clear();
        try {
          loadTestCases();
        } catch (Exception e) {
          System.err.println("Unable to load test cases: " + e.getMessage());
          e.printStackTrace();
        }
      } else {
        System.err.println("Connection is not setup properly");
      }
    }
    String guideName = participantResponse.getGuideName();
    if (!guideName.equals("")) {
      profileUsageId = 0;
      int i = 0;
      for (ProfileUsage profileUsage : profileManager.getProfileUsageList()) {
        i++;
        if (profileUsage.toString().equalsIgnoreCase(guideName)) {
          profileUsageId = i;
          break;
        }
      }
    }
  }

  protected static void loadTestCases() throws ServletException, IOException {
    {
      File testCaseDir = sendData.getTestCaseDir(false);
      if (testCaseDir != null) {
        CreateTestCaseServlet.readTestCases(testMessageMapMap, testCaseDir, null, false);
        {
          File[] dirs = testCaseDir.listFiles(new FileFilter() {
            public boolean accept(File arg0) {
              return arg0.isDirectory()
                  && !arg0.getName().startsWith(CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX);
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
              return arg0.isDirectory()
                  && !arg0.getName().startsWith(CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX);
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

  private static void inittParticipantResponseMap() throws IOException {
    if (participantResponseMap == null) {
      participantResponseMap = new ParticipantResponse[RecordServletInterface.MAP_COLS_MAX][RecordServletInterface.MAP_ROWS_MAX];
      ParticipantResponseManager.readFile(participantResponseMap);
    }
  }

}
