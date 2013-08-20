/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.manager.HL7Analyzer;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.QueryConverter;
import org.immunizationsoftware.dqa.tester.manager.ScenarioManager;
import org.immunizationsoftware.dqa.tester.run.TestRunner;
import org.immunizationsoftware.dqa.tester.transform.Issue;

/**
 * 
 * @author nathan
 */
public class CertifyServlet extends ClientServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
   * 
   * @param request
   *          servlet request
   * @param response
   *          servlet response
   * @throws ServletException
   *           if a servlet-specific error occurs
   * @throws IOException
   *           if an I/O error occurs
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");
    String action = request.getParameter("action");
    String problem = null;
    CertifyRunner certifyRunner = (CertifyRunner) session.getAttribute("certifyRunner");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else if (action.equals("Start")) {
      if (certifyRunner != null && !certifyRunner.getStatus().equals(CertifyRunner.STATUS_COMPLETED)
          && !certifyRunner.getStatus().equals(CertifyRunner.STATUS_STOPPED)) {
        problem = "Unable to start new certification as current certification is still running.";
      } else {
        List<Connector> connectors = SetupServlet.getConnectors(session);
        int id = Integer.parseInt(request.getParameter("id"));
        certifyRunner = new CertifyRunner(connectors.get(id - 1), session);
        session.setAttribute("certifyRunner", certifyRunner);
        certifyRunner.start();
      }
    } else if (action.equals("Stop")) {
      if (certifyRunner != null) {
        certifyRunner.stopRunning();
      }
    }
    doGet(request, response, session, problem);
  }

  // <editor-fold defaultstate="collapsed"
  // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

  /**
   * Handles the HTTP <code>GET</code> method.
   * 
   * @param request
   *          servlet request
   * @param response
   *          servlet response
   * @throws ServletException
   *           if a servlet-specific error occurs
   * @throws IOException
   *           if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {
      doGet(request, response, session, null);
    }
  }

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session, String problem)
      throws IOException {
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, "Certify", request);

      if (problem != null) {
        out.println("<p>" + problem + "</p>");
      }
      CertifyRunner certifyRunner = (CertifyRunner) session.getAttribute("certifyRunner");
      boolean canStart = certifyRunner == null || certifyRunner.getStatus().equals(CertifyRunner.STATUS_COMPLETED)
          || certifyRunner.getStatus().equals(CertifyRunner.STATUS_STOPPED);

      if (certifyRunner != null) {
        out.println("    <h3>Certification Results</h3>");
        certifyRunner.printResults(out);
        out.println("    <form action=\"CertifyServlet\" method=\"POST\">");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Refresh\"/>");
        if (!canStart) {
          out.println("      <input type=\"submit\" name=\"action\" value=\"Stop\"/>");
        }
        out.println("    </form>");
      }

      if (canStart) {
        out.println("    <h3>Start Certification Evaluation</h3>");
        out.println("    <form action=\"CertifyServlet\" method=\"POST\">");
        out.println("      <table border=\"0\">");
        int id = 0;
        if (request.getParameter("id") != null) {
          id = Integer.parseInt(request.getParameter("id"));
        }
        if (session.getAttribute("id") != null) {
          id = (Integer) session.getAttribute("id");
        }
        out.println("        <tr>");
        out.println("          <td>Service</td>");
        out.println("          <td>");
        List<Connector> connectors = SetupServlet.getConnectors(session);
        if (connectors.size() == 1) {
          out.println("            " + connectors.get(0).getLabelDisplay());
          out.println("            <input type=\"hidden\" name=\"id\" value=\"1\"/>");
        } else {
          out.println("            <select name=\"id\">");
          out.println("              <option value=\"\">select</option>");
          int i = 0;
          for (Connector connector : connectors) {
            i++;
            if (id == i) {
              out.println("              <option value=\"" + i + "\" selected=\"true\">" + connector.getLabelDisplay()
                  + "</option>");
            } else {
              out.println("              <option value=\"" + i + "\">" + connector.getLabelDisplay() + "</option>");
            }
          }
          out.println("            </select>");
        }
        out.println("          </td>");
        out.println("          <td>");
        out.println("            <input type=\"submit\" name=\"action\" value=\"Start\"/>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("      </table>");
        out.println("    </form>");
      }
      if (certifyRunner != null) {
        out.println("    <h3>Certification Details</h3>");
        certifyRunner.printProgressDetails(out);
      }
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  public static class CertifyRunner extends Thread
  {
    public static final String STATUS_INITIALIZED = "Initialized";
    public static final String STATUS_STARTED = "Started";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_STOPPED = "Stopped";

    private String status = "";
    private boolean keepRunning = true;
    private TestCaseMessage testCaseMessageBase = null;

    private int areaALevel1Score = -1;
    private int areaALevel2Score = -1;
    private int areaALevel3Score = -1;

    private int areaBLevel1Score = -1;
    private int areaBLevel2Score = -1;
    private int areaBLevel3Score = -1;

    private int[] areaCLevelScore = new int[3];
    {
      areaCLevelScore[0] = -1;
      areaCLevelScore[1] = -1;
      areaCLevelScore[2] = -1;
    }

    private int areaDLevel1Score = -1;
    private int areaDLevel2Score = -1;
    private int areaDLevel3ScoreU = -1;
    private int areaDLevel3ScoreQ = -1;

    private int areaALevel1Progress = -1;
    private int areaALevel2Progress = -1;
    private int areaALevel3Progress = -1;

    private int areaBLevel1Progress = -1;
    private int areaBLevel2Progress = -1;
    private int areaBLevel3Progress = -1;

    private int[] areaCLevelProgress = new int[3];
    {
      areaCLevelProgress[0] = -1;
      areaCLevelProgress[1] = -1;
      areaCLevelProgress[2] = -1;
    }

    private int areaDLevel1Progress = -1;
    private int areaDLevel2Progress = -1;
    private int areaDLevel3Progress = -1;

    private long totalQueryTime = 0;
    private long totalUpdateTime = 0;
    private int totalQueryCount = 0;
    private int totalUpdateCount = 0;

    private String testCaseSet = "";
    private HttpSession session = null;

    private List<TestCaseMessage> statusCheckTestCaseList = new ArrayList<TestCaseMessage>();

    private List<TestCaseMessage> statusCheckTestCaseBasicList = new ArrayList<TestCaseMessage>();
    private List<TestCaseMessage> statusCheckTestCaseIntermediateList = new ArrayList<TestCaseMessage>();

    private List<TestCaseMessage> statusCheckQueryTestCaseBasicList = new ArrayList<TestCaseMessage>();
    private List<TestCaseMessage> statusCheckQueryTestCaseIntermediateList = new ArrayList<TestCaseMessage>();

    private List<TestCaseMessage> ackAnalysisList = new ArrayList<TestCaseMessage>();
    private List<TestCaseMessage> rspAnalysisList = new ArrayList<TestCaseMessage>();

    private void register(TestCaseMessage tcm) {
      tcm.setTestCaseId(statusCheckTestCaseList.size());
      statusCheckTestCaseList.add(tcm);
    }

    public List<TestCaseMessage> getStatusCheckTestCaseList() {
      return statusCheckTestCaseList;
    }

    private String[] statusCheckScenarios = { ScenarioManager.SCENARIO_1_R_ADMIN_CHILD,
        ScenarioManager.SCENARIO_2_R_ADMIN_ADULT, ScenarioManager.SCENARIO_3_R_HISTORICAL_CHILD,
        ScenarioManager.SCENARIO_4_R_CONSENTED_CHILD, ScenarioManager.SCENARIO_5_R_REFUSED_TODDLER,
        ScenarioManager.SCENARIO_6_R_VARICELLA_HISTORY_CHILD, ScenarioManager.SCENARIO_7_R_COMPLETE_RECORD };

    private List<TestCaseMessage>[] profileTestCaseLists = new ArrayList[3];

    Connector connector;

    public CertifyRunner(Connector connector, HttpSession session) {
      this.connector = connector;
      this.session = session;
      status = STATUS_INITIALIZED;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
      testCaseSet = "Certification " + sdf.format(new Date());
    }

    @Override
    public void run() {
      status = STATUS_STARTED;

      prepareBasic();

      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      updateBasic();
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      prepareIntermediate();

      updateIntermediate();
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      if (testCaseMessageBase != null) {
        Map<Integer, List<Issue>> issueMap = new HashMap<Integer, List<Issue>>();
        prepareAdvanced(issueMap);
        updateAdvanced(issueMap);

        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }

      prepareExceptionalUpdateAnalysis();
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      analyzeExceptionalUpdates();
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      queryBasic();
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      queryIntermediate();
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      prepareExceptionalQueryAnalysis();
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      analyzeExceptionalQueries();
      if (!keepRunning) {
        status = STATUS_STOPPED;
        return;
      }

      if (totalQueryCount > 0 && totalUpdateCount > 0) {
        areaDLevel3ScoreQ = (int) totalQueryTime / totalQueryCount;
        areaDLevel3ScoreU = (int) totalUpdateTime / totalUpdateCount;
        areaDLevel3Progress = 100;
      }
      
      if (testCaseMessageBase != null) {
      File testCaseDir = CreateTestCaseServlet.getTestCaseDir(testCaseMessageBase, session);
      if (testCaseDir != null)
      {
        File certificationReportFile = new File(testCaseDir, "Certification Report.html");
        try
        {
          PrintWriter out = new PrintWriter(new FileWriter(certificationReportFile));
          out.println("<html>");
          out.println("  <head>");
          out.println("    <title>Certification Report</title>");
          out.println("    <style>");
          out.println("       body {font-family: Tahoma, Geneva, sans-serif; background:#D5E1DD}");
          out.println("       .pass {background:#77BED2; padding-left:5px;}");
          out.println("       .fail {background:#FF9999; padding-left:5px;}");
          out.println("       .nottested { padding-left:5px;}");
          out.println("       ");
          out.println("    </style>");
          out.println("  </head>");
          out.println("  <body>");
          out.println("    <h3>Certification Results</h3>");
          printResults(out);
          out.println("    <h3>Certification Details</h3>");
          printProgressDetails(out);
          out.println("  </body>");
          out.println("</html>");
          out.close();
        } catch (IOException ioe)
        {
          ioe.printStackTrace();
          // unable to save, continue as normal
        }
      }
      }

      status = STATUS_COMPLETED;
    }

    private void prepareExceptionalUpdateAnalysis() {
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseList) {
        ackAnalysisList.add(testCaseMessage);
      }
    }

    private void prepareExceptionalQueryAnalysis() {
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseList) {
        if (testCaseMessage.getDerivedFromVXUMessage() != null) {
          rspAnalysisList.add(testCaseMessage);
        }
      }
    }

    private void analyzeExceptionalUpdates() {
      int count = 0;
      int pass = 0;
      for (TestCaseMessage testCaseMessage : ackAnalysisList) {
        count++;
        if (testCaseMessage.isHasRun()) {
          List<String> il = new ArrayList<String>();
          HL7Reader ackReader = new HL7Reader(testCaseMessage.getActualResponseMessage());
          HL7Reader vxuReader = new HL7Reader(testCaseMessage.getMessageText());
          HL7Analyzer analyzer = new HL7Analyzer(il, ackReader, vxuReader);
          if (analyzer.analyzeAck()) {
            pass++;
          }
          testCaseMessage.setAckAnalyzer(analyzer);
          areaDLevel1Progress = makeScore(count, ackAnalysisList.size());
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }
      areaDLevel1Progress = makeScore(count, ackAnalysisList.size());
      areaDLevel1Score = makeScore(pass, ackAnalysisList.size());
    }

    private void analyzeExceptionalQueries() {
      int count = 0;
      int pass = 0;
      for (TestCaseMessage testCaseMessage : rspAnalysisList) {
        count++;
        if (testCaseMessage.isHasRun()) {
          List<String> il = new ArrayList<String>();
          HL7Reader ackReader = new HL7Reader(testCaseMessage.getActualResponseMessage());
          HL7Reader vxuReader = new HL7Reader(testCaseMessage.getMessageText());
          HL7Analyzer analyzer = new HL7Analyzer(il, ackReader, vxuReader);
          if (analyzer.analyzeRsp()) {
            pass++;
          }
          testCaseMessage.setAckAnalyzer(analyzer);
          areaDLevel2Progress = makeScore(count, rspAnalysisList.size());
        }
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }
      areaDLevel2Progress = makeScore(count, rspAnalysisList.size());
      areaDLevel2Score = makeScore(pass, rspAnalysisList.size());
    }

    private void updateAdvanced(Map<Integer, List<Issue>> issueMap) {
      TestRunner testRunner = new TestRunner();
      Transformer transformer = new Transformer();
      int count;
      for (int i = 0; i < areaCLevelScore.length; i++) {
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
        profileTestCaseLists[i] = new ArrayList<TestCaseMessage>();
        int priority = i + 1;
        List<Issue> issueList = issueMap.get(priority);
        if (issueList != null && issueList.size() > 0) {
          int countPass = 0;
          count = 0;
          for (Issue issue : issueList) {
            count++;
            TestCaseMessage testCaseMessage = new TestCaseMessage(testCaseMessageBase);
            testCaseMessage.setTestCaseSet(testCaseSet);
            testCaseMessage.setTestCaseNumber("C1." + paddWithZeros(count, 3));
            profileTestCaseLists[i].add(testCaseMessage);
            testCaseMessage.setDescription(issue.getName());
            testCaseMessage.addCauseIssues(issue.getName());
            testCaseMessage.appendCustomTransformation("MSH-11=D");
            transformer.transform(testCaseMessage);
            register(testCaseMessage);
            testCaseMessage.setAssertResultText(issue.getName());
            testCaseMessage.setAssertResultStatus("Accept or Reject");
            if (testCaseMessage.hasIssue()) {
              try {
                testRunner.runTest(connector, testCaseMessage);
                totalUpdateCount++;
                totalUpdateTime += testRunner.getTotalRunTime();
                if (testCaseMessage.isPassedTest()) {
                  countPass++;
                }
              } catch (Throwable t) {
                testCaseMessage.setException(t);
              }
            }
            testCaseMessage.setHasRun(true);
            CreateTestCaseServlet.saveTestCase(testCaseMessage, session);
            areaCLevelProgress[i] = makeScore(count, issueList.size());
            if (!keepRunning) {
              status = STATUS_STOPPED;
              return;
            }
          }
          areaCLevelScore[i] = makeScore(countPass, issueList.size());
        }

      }
    }

    private void prepareAdvanced(Map<Integer, List<Issue>> issueMap) {
      for (Issue issue : Issue.values()) {
        int priority = issue.getPriority();
        List<Issue> issueList = issueMap.get(priority);
        if (issueList == null) {
          issueList = new ArrayList<Issue>();
          issueMap.put(priority, issueList);
        }
        issueList.add(issue);
      }
    }

    private void prepareBasic() {
      Transformer transformer = new Transformer();
      int count = 0;
      for (String scenario : statusCheckScenarios) {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(scenario);
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("A1." + count);
        statusCheckTestCaseBasicList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

    }

    private void updateBasic() {
      int count;
      int testPass = 0;
      TestRunner testRunner = new TestRunner();
      count = 0;
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
        count++;
        try {
          boolean pass = testRunner.runTest(connector, testCaseMessage);
          totalUpdateCount++;
          totalUpdateTime += testRunner.getTotalRunTime();
          if (pass) {
            testPass++;
            testCaseMessageBase = testCaseMessage;
          } else {
            testCaseMessageBase = null;
          }
          testCaseMessage.setErrorList(testRunner.getErrorList());

        } catch (Throwable t) {
          testCaseMessage.setException(t);
        }
        areaALevel1Progress = makeScore(count, statusCheckTestCaseBasicList.size());
        CreateTestCaseServlet.saveTestCase(testCaseMessage, session);
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }
      areaALevel1Score = makeScore(testPass, statusCheckTestCaseBasicList.size());
    }

    private void prepareIntermediate() {

      Transformer transformer = new Transformer();
      int count;
      List<Certify.CertifyItem> certifyItems;
      Certify certify = new Certify();

      certifyItems = certify.getCertifyItemList(Certify.FIELD_SEX);
      count = 0;
      for (Certify.CertifyItem certifyItem : certifyItems) {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessage.setDescription("Sex is " + certifyItem.getLabel());
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("PID-8=" + certifyItem.getCode());
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      certifyItems = certify.getCertifyItemList(Certify.FIELD_RACE);
      count = 0;
      for (Certify.CertifyItem certifyItem : certifyItems) {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessage.setDescription("Race is " + certifyItem.getLabel());
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("PID-10=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PID-10.2=" + certifyItem.getLabel());
        testCaseMessage.appendCustomTransformation("PID-10.3=" + certifyItem.getTable());
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      certifyItems = certify.getCertifyItemList(Certify.FIELD_ETHNICITY);
      count = 0;
      for (Certify.CertifyItem certifyItem : certifyItems) {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessage.setDescription("Ethnicity is " + certifyItem.getLabel());
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("PID-22=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PID-22.2=" + certifyItem.getLabel());
        testCaseMessage.appendCustomTransformation("PID-22.3=" + certifyItem.getTable());
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      certifyItems = certify.getCertifyItemList(Certify.FIELD_HISTORICAL);
      count = 0;
      for (Certify.CertifyItem certifyItem : certifyItems) {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_3_R_HISTORICAL_CHILD);
        testCaseMessage.setDescription("Information Source is " + certifyItem.getLabel());
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("RXA-9=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("RXA-9.2=" + certifyItem.getLabel());
        testCaseMessage.appendCustomTransformation("RXA-9.3=" + certifyItem.getTable());
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      certifyItems = certify.getCertifyItemList(Certify.FIELD_VFC);
      count = 0;
      for (Certify.CertifyItem certifyItem : certifyItems) {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessage.setDescription("VFC Status is " + certifyItem.getLabel());
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("OBX-5=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("OBX-5.2=" + certifyItem.getLabel());
        testCaseMessage.appendCustomTransformation("OBX-5.3=" + certifyItem.getTable());
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      certifyItems = certify.getCertifyItemList(Certify.FIELD_HOD);
      count = 0;
      for (Certify.CertifyItem certifyItem : certifyItems) {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_6_R_VARICELLA_HISTORY_CHILD);
        testCaseMessage.setDescription("History of Disease is " + certifyItem.getLabel());
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("OBX-5=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("OBX-5.2=" + certifyItem.getLabel());
        testCaseMessage.appendCustomTransformation("OBX-5.3=" + certifyItem.getTable());
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      certifyItems = certify.getCertifyItemList(Certify.FIELD_REGISTRY_STATUS);
      count = 0;
      for (Certify.CertifyItem certifyItem : certifyItems) {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessage.setDescription("Registry Status is " + certifyItem.getLabel());
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("PD1-16=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PD1-17=[NOW]");
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      certifyItems = certify.getCertifyItemList(Certify.FIELD_REFUSAL_REASON);
      count = 0;
      for (Certify.CertifyItem certifyItem : certifyItems) {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_5_R_REFUSED_TODDLER);
        testCaseMessage.setDescription("Refusal Reason is " + certifyItem.getLabel());
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("RXA-18=" + certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("RXA-18.2=" + certifyItem.getLabel());
        testCaseMessage.appendCustomTransformation("RXA-18.3=" + certifyItem.getTable());
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      // Compatibility tests

      {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessage.appendOriginalMessage("OBX|5|NM|6287-7^Baker's yeast IgE Ab in Serum^LN||1945||||||F\n");
        testCaseMessage.setDescription("Tolerance Check: Message includes observation not typically sent to IIS");
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessage.appendOriginalMessage("YES|This|is|a|segment^you^should^never^see|in|production\n");
        testCaseMessage.setDescription("Tolerance Check: Message includes segment not defined by HL7");
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessage.setDescription("Tolerance Check: Hospital service code is set to a non-standard value");
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("PV1-10=AMB");
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

      {
        count++;
        TestCaseMessage testCaseMessage = ScenarioManager
            .createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
        testCaseMessage.setDescription("Tolerance Check: Observation at patient level (HL7 2.8 capability)");
        testCaseMessage.setTestCaseSet(testCaseSet);
        testCaseMessage.setTestCaseNumber("B1." + count);
        testCaseMessage.appendCustomTransformation("insert segment OBX after NK1");
        testCaseMessage.appendCustomTransformation("OBX-1=1");
        testCaseMessage.appendCustomTransformation("OBX-2=59784-9");
        testCaseMessage.appendCustomTransformation("OBX-2.2=Disease with presumed immunity");
        testCaseMessage.appendCustomTransformation("OBX-2.3=LN");
        testCaseMessage.appendCustomTransformation("OBX-3=1");
        testCaseMessage.appendCustomTransformation("OBX-5=38907003");
        testCaseMessage.appendCustomTransformation("OBX-5.2=Varicella infection");
        testCaseMessage.appendCustomTransformation("OBX-5.3=SCT");
        testCaseMessage.appendCustomTransformation("OBX-11=F");
        statusCheckTestCaseIntermediateList.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
        register(testCaseMessage);
      }

    }

    private void updateIntermediate() {
      int count;
      int testPass = 0;
      TestRunner testRunner = new TestRunner();
      count = 0;
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
        count++;
        try {
          boolean pass = testRunner.runTest(connector, testCaseMessage);
          totalUpdateCount++;
          totalUpdateTime += testRunner.getTotalRunTime();
          testCaseMessage.setHasRun(true);
          if (pass) {
            testPass++;
          }
          testCaseMessage.setErrorList(testRunner.getErrorList());
        } catch (Throwable t) {
          testCaseMessage.setException(t);
        }
        areaBLevel1Progress = makeScore(count, statusCheckTestCaseIntermediateList.size());
        CreateTestCaseServlet.saveTestCase(testCaseMessage, session);
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }
      areaBLevel1Score = makeScore(testPass, statusCheckTestCaseIntermediateList.size());
    }

    private void queryBasic() {
      int count;
      int testQueryPassRequired = 0;
      int testQueryPassOptional = 0;
      int testQueryCountRequired = 0;
      int testQueryCountOptional = 0;
      count = 0;
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
        count++;
        String vxuMessage = testCaseMessage.getMessageText();

        TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
        queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
        queryTestCaseMessage.setDescription("Query for " + testCaseMessage.getDescription());
        queryTestCaseMessage.setMessageText(QueryConverter.convertVXUtoQBP(testCaseMessage.getMessageText()));
        queryTestCaseMessage.setTestCaseSet(testCaseSet);
        queryTestCaseMessage.setTestCaseNumber("A3." + count);
        statusCheckQueryTestCaseBasicList.add(queryTestCaseMessage);
        register(queryTestCaseMessage);
        try {
          long startTime = System.currentTimeMillis();
          String rspMessage = connector.submitMessage(queryTestCaseMessage.getMessageText(), false);
          totalQueryCount++;
          totalQueryTime += System.currentTimeMillis() - startTime;
          queryTestCaseMessage.setHasRun(true);
          queryTestCaseMessage.setActualResponseMessage(rspMessage);
          List<CompareManager.Comparison> comparisonList = CompareManager.compareMessages(vxuMessage, rspMessage);
          queryTestCaseMessage.setComparisonList(comparisonList);
          for (CompareManager.Comparison comparison : comparisonList) {
            if (comparison.isTested()
                && comparison.getPriorityLevel() <= CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL) {
              testQueryCountOptional++;
              if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                testQueryCountRequired++;
              }

              if (comparison.isPass()) {
                testQueryPassOptional++;
                if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                  testQueryPassRequired++;
                }
              }
            }
          }
        } catch (Throwable t) {
          queryTestCaseMessage.setException(t);
        }
        areaALevel3Progress = makeScore(count, statusCheckTestCaseBasicList.size());
        areaALevel2Progress = areaALevel3Progress;
        CreateTestCaseServlet.saveTestCase(queryTestCaseMessage, session);
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }
      areaALevel2Score = makeScore(testQueryPassRequired, testQueryCountRequired);
      areaALevel3Score = makeScore(testQueryPassOptional, testQueryCountOptional);
    }

    private void queryIntermediate() {
      int count;
      int testQueryPassRequired = 0;
      int testQueryPassOptional = 0;
      int testQueryCountRequired = 0;
      int testQueryCountOptional = 0;
      count = 0;
      for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
        count++;
        String vxuMessage = testCaseMessage.getMessageText();

        TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
        queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
        queryTestCaseMessage.setDescription("Query " + testCaseMessage.getDescription());
        queryTestCaseMessage.setMessageText(QueryConverter.convertVXUtoQBP(testCaseMessage.getMessageText()));
        queryTestCaseMessage.setTestCaseSet(testCaseSet);
        queryTestCaseMessage.setTestCaseNumber("B3." + count);
        statusCheckQueryTestCaseIntermediateList.add(queryTestCaseMessage);
        register(queryTestCaseMessage);
        try {
          long startTime = System.currentTimeMillis();
          String rspMessage = connector.submitMessage(queryTestCaseMessage.getMessageText(), false);
          totalQueryCount++;
          totalQueryTime += System.currentTimeMillis() - startTime;
          queryTestCaseMessage.setHasRun(true);
          queryTestCaseMessage.setActualResponseMessage(rspMessage);
          List<CompareManager.Comparison> comparisonList = CompareManager.compareMessages(vxuMessage, rspMessage);
          queryTestCaseMessage.setComparisonList(comparisonList);
          queryTestCaseMessage.setPassedTest(true);
          for (CompareManager.Comparison comparison : comparisonList) {
            if (comparison.isTested()
                && comparison.getPriorityLevel() <= CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL) {
              testQueryCountOptional++;
              if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                testQueryCountRequired++;
              }

              if (comparison.isPass()) {
                testQueryPassOptional++;
                if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                  testQueryPassRequired++;
                }
              } else if (comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                queryTestCaseMessage.setPassedTest(false);
              }
            }
          }
        } catch (Throwable t) {
          queryTestCaseMessage.setException(t);
        }
        areaBLevel3Progress = makeScore(count, statusCheckTestCaseIntermediateList.size());
        areaBLevel2Progress = areaBLevel3Progress;
        CreateTestCaseServlet.saveTestCase(queryTestCaseMessage, session);
        if (!keepRunning) {
          status = STATUS_STOPPED;
          return;
        }
      }
      areaBLevel2Score = makeScore(testQueryPassRequired, testQueryCountRequired);
      areaBLevel3Score = makeScore(testQueryPassOptional, testQueryCountOptional);
    }

    public void printResults(PrintWriter out) {
      out.println("<table border=\"1\" cellspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th width=\"12%\"></th>");
      out.println("    <th width=\"22%\"><font size=\"+1\">Basic</font></th>");
      out.println("    <th width=\"22%\"><font size=\"+1\">Intermediate</font></th>");
      out.println("    <th width=\"22%\"><font size=\"+1\">Advanced</font></th>");
      out.println("    <th width=\"22%\"><font size=\"+1\">Exceptional</font></th>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td><em>The IIS can...</em></td>");
      out.println("    <td><em>accept updates from EHR</em></td>");
      out.println("    <td><em>recognize valid codes</em></td>");
      out.println("    <td><em>identify quality issues</em></td>");
      out.println("    <td><em>respond correctly to requests</em></td>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Level 1</th>");
      if (areaALevel1Score >= 100) {
        out.println("    <td class=\"pass\">All NIST 2014 scenarios are accepted.</td>");
      } else if (areaALevel1Score >= 0) {
        out.println("    <td class=\"fail\">Not all NIST 2014 scenarios are accepted. (" + areaALevel1Score
            + "% accepted)</td>");
      } else {
        if (areaALevel1Progress > -1) {
          out.println("    <td>running now ... " + areaALevel1Progress + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      if (areaBLevel1Score >= 100) {
        out.println("    <td class=\"pass\">All messages with core coded data elements were accepted.</td>");
      } else if (areaBLevel1Score >= 0) {
        out.println("    <td class=\"fail\">Some messages with core coded data elements were NOT accepted. ("
            + areaBLevel1Score + "% messages accepted)</td>");
      } else {
        if (areaBLevel1Progress > -1) {
          out.println("    <td>running now ... " + areaBLevel1Progress + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      if (areaCLevelScore[0] >= 100) {
        out.println("    <td class=\"pass\">All high priority issues were identified. </td>");
      } else if (areaCLevelScore[0] >= 0) {
        out.println("    <td class=\"fail\">Not all high priority issues were identified. (" + areaCLevelScore[0]
            + "% of issues identified)</td>");
      } else {
        if (areaCLevelProgress[0] > -1) {
          out.println("    <td>running now ... " + areaCLevelProgress[0] + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      if (areaDLevel1Score >= 100) {
        out.println("    <td class=\"pass\">All acknowledgement (ACK) responses meet HL7 and CDC standards.</td>");
      } else if (areaDLevel1Score >= 0) {
        out.println("    <td class=\"fail\">Not all acknowledgement (ACK) responses meet HL7 and CDC standards. ("
            + areaDLevel1Score + "% of messages met standard)</td>");
      } else {
        if (areaDLevel1Progress > -1) {
          out.println("    <td>running now ... " + areaDLevel1Progress + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Level 2</th>");
      if (areaALevel2Score >= 100) {
        out.println("    <td class=\"pass\">All required IIS core fields are supported. </td>");
      } else if (areaALevel2Score >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields are supported. (" + areaALevel2Score
            + "% of tests passed)</td>");
      } else {
        if (areaALevel2Progress > -1) {
          out.println("    <td>running now ... " + areaALevel2Progress + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      if (areaBLevel2Score >= 100) {
        out.println("    <td class=\"pass\">All required IIS core fields were returned.</td>");
      } else if (areaBLevel2Score >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields were returned. (" + areaBLevel2Score
            + "% core fields returned)</td>");
      } else {
        if (areaBLevel2Progress > -1) {
          out.println("    <td>running now ... " + areaBLevel2Progress + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      if (areaCLevelScore[1] >= 100) {
        out.println("    <td class=\"pass\">All medium priority issues were identified.</td>");
      } else if (areaCLevelScore[1] >= 0) {
        out.println("    <td class=\"fail\">Not all medium priority issues were identified. (" + areaCLevelScore[1]
            + "% issues identified)</td>");
      } else {
        if (areaCLevelProgress[1] > -1) {
          out.println("    <td>running now ... " + areaCLevelProgress[1] + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      if (areaDLevel2Score >= 100) {
        out.println("    <td class=\"pass\">All query response (RSP) messages met HL7 and CDC standards. </td>");
      } else if (areaDLevel2Score >= 0) {
        out.println("    <td class=\"fail\">Not all query response (RSP) messages met HL7 and CDC standards. ("
            + areaDLevel2Score + "% of fields returned)</td>");
      } else {
        if (areaDLevel2Progress > -1) {
          out.println("    <td>running now ... " + areaDLevel2Progress + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Level 3</th>");
      if (areaALevel3Score >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core data were returned when queried.</td>");
      } else if (areaALevel3Score >= 0) {
        out.println("    <td class=\"fail\">Not all required or optional IIS core data were returned when queried ("
            + areaALevel3Score + "% of fields returned)</td>");
      } else {
        if (areaALevel3Progress > -1) {
          out.println("    <td>running now ... " + areaALevel3Progress + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      if (areaBLevel3Score >= 100) {
        out.println("    <td class=\"pass\">All required and optional IIS core fields were returned.</td>");
      } else if (areaBLevel3Score >= 0) {
        out.println("    <td class=\"fail\">Not all required IIS core fields were returned. (" + areaBLevel3Score
            + "% required and optional were returned)</td>");
      } else {
        if (areaBLevel3Progress > -1) {
          out.println("    <td>running now ... " + areaBLevel3Progress + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      if (areaCLevelScore[2] >= 100) {
        out.println("    <td class=\"pass\">All low priority issues were identified.</td>");
      } else if (areaCLevelScore[2] >= 0) {
        out.println("    <td class=\"fail\">Not all low priority issues were identified. (" + areaCLevelScore[2]
            + "% issues were identified)</td>");
      } else {
        if (areaCLevelProgress[2] > -1) {
          out.println("    <td>running now ... " + areaCLevelProgress[2] + "% complete</td>");
        } else {
          out.println("    <td>not run yet</td>");
        }
      }
      if (areaDLevel3Progress == -1) {
        if (areaDLevel3Progress == -1) {
          out.println("    <td>not run yet</td>");
        }
      } else {
        if (areaDLevel3ScoreU < 3000 && areaDLevel3ScoreQ < 5000) {
          out.println("    <td class=\"pass\">Performance was acceptable: " + areaDLevel3ScoreU + "ms for updates and "
              + areaDLevel3ScoreQ + "ms for queries.</td>");
        } else {
          out.println("    <td class=\"fail\">Performance was below acceptable levels: " + areaDLevel3ScoreU
              + "ms for updates and " + areaDLevel3ScoreQ + "ms for queries.</td>");
        }

      }
      out.println("  </tr>");
      out.println("</table>");
    }

    public void printProgressDetails(PrintWriter out) {

      int testNum = 0;
      if (areaALevel1Progress > 0) {
        for (TestCaseMessage testCaseMessage : statusCheckTestCaseBasicList) {
          testNum++;
          out.println("<table border=\"1\" cellspacing=\"0\">");
          out.println("  <tr>");
          out.println("    <th>Basic Test #" + testNum + "</th>");
          out.println("    <td><em>" + testCaseMessage.getDescription() + "</em></td>");
          out.println("  </tr>");
          out.println("  <tr>");
          out.println("    <th>Level 1</th>");
          if (testCaseMessage.isPassedTest()) {
            out.println("    <td class=\"pass\">Message was accepted. <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                + testCaseMessage.getTestCaseId() + "\">details</a></td>");
          } else if (testCaseMessage.isHasRun()) {
            out.println("    <td class=\"fail\">Message was not accepted. <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                + testCaseMessage.getTestCaseId() + "\">details</a</td>");
          } else {
            out.println("    <td class=\"nottested\">not run yet</td>");
          }
          out.println("  </tr>");
          if (statusCheckQueryTestCaseBasicList.size() >= testNum) {
            testCaseMessage = statusCheckQueryTestCaseBasicList.get(testNum - 1);
            out.println("  <tr>");
            out.println("    <th>Level 2</th>");
            {
              boolean hasRun = false;
              boolean hasPassed = true;
              if (testCaseMessage.getComparisonList() != null) {
                hasRun = true;
                for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                  if (comparison.isTested()
                      && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED) {
                    if (!comparison.isPass()) {
                      hasPassed = false;
                      break;
                    }
                  }
                }
              }
              if (hasPassed) {
                out.println("    <td class=\"pass\">All required fields returned. <a href=\"CompareServlet?certifyServletBasicNum="
                    + testCaseMessage.getTestCaseId() + "\">details</a></td>");
              } else if (hasRun) {
                out.println("    <td class=\"fail\">Required fields not returned:");
                out.println("      <ul>");
                for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                  if (comparison.isTested()
                      && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_REQUIRED
                      && !comparison.isPass()) {

                    out.println("      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel()
                        + "</li>");
                  }
                }
                out.println("      </ul>");
                out.println("      <a href=\"CompareServlet?certifyServletBasicNum=" + testCaseMessage.getTestCaseId()
                    + "\">details</a>");
                out.println("    </td>");
              } else {
                out.println("    <td class=\"nottested\">not run yet</td>");
              }
            }
            out.println("  </tr>");
            out.println("  <tr>");
            out.println("    <th>Level 3</th>");

            {
              boolean hasRun = false;
              boolean hasPassed = true;
              if (testCaseMessage.getComparisonList() != null) {
                hasRun = true;
                for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                  if (comparison.isTested()
                      && comparison.getPriorityLevel() <= CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL) {
                    if (!comparison.isPass()) {
                      hasPassed = false;
                      break;
                    }
                  }
                }
              }
              if (hasPassed) {
                out.println("    <td class=\"pass\">All required and optional fields returned. <a href=\"CompareServlet?certifyServletBasicNum="
                    + testCaseMessage.getTestCaseId() + "\">details</a></td>");
              } else if (hasRun) {
                boolean hasOptionalFields = false;
                for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                  if (comparison.isTested()
                      && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL
                      && !comparison.isPass()) {
                    hasOptionalFields = true;
                    break;
                  }
                }
                if (hasOptionalFields) {
                  out.println("    <td class=\"fail\">Optional fields not returned:");
                  out.println("      <ul>");
                  for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
                    if (comparison.isTested()
                        && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL
                        && !comparison.isPass()) {
                      out.println("      <li>" + comparison.getHl7FieldName() + " - " + comparison.getFieldLabel()
                          + "</li>");
                    }
                  }
                  out.println("      </ul>");
                } else {
                  out.println("    <td class=\"fail\">Level 2 not passed. ");
                }
                out.println("      <a href=\"CompareServlet?certifyServletBasicNum=" + testCaseMessage.getTestCaseId()
                    + "\">details</a>");
                out.println("    </td>");
              } else {
                out.println("    <td class=\"nottested\">not run yet</td>");
              }
            }
            out.println("  </tr>");
          }
          out.println("</table>");
          out.println("</br>");
        }
      }

      if (areaBLevel1Progress > 0) {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th colspan=\"2\">Intermediate Tests - Level 1 - Accepting Core Data</th>");
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : statusCheckTestCaseIntermediateList) {
          String classText = "nottested";
          if (testCaseMessage.isHasRun()) {
            classText = testCaseMessage.isPassedTest() ? "pass" : "fail";
          }
          out.println("  <tr>");
          out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
          if (testCaseMessage.isHasRun()) {
            if (testCaseMessage.isPassedTest()) {
              out.println("    <td class=\"" + classText
                  + "\">Code accepted. <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                  + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
            } else {
              out.println("    <td class=\"" + classText
                  + "\">Code was NOT accepted. <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                  + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
            }
          } else {
            out.println("    <td class=\"" + classText + "\">not run yet</td>");
          }
          out.println("  </tr>");
        }
        out.println("</table>");
        out.println("</br>");
      }
      if (areaBLevel2Progress > 0) {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th colspan=\"2\">Intermediate Tests - Level 2 - Supports Required</th>");
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseIntermediateList) {
          String classText = "nottested";
          if (testCaseMessage.isHasRun()) {
            classText = testCaseMessage.isPassedTest() ? "pass" : "fail";
          }
          out.println("  <tr>");
          out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
          if (testCaseMessage.isHasRun()) {
            if (testCaseMessage.isPassedTest()) {
              out.println("    <td class=\"" + classText
                  + "\">All required fields were returned. <a href=\"CompareServlet?certifyServletBasicNum="
                  + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
            } else {
              out.println("    <td class=\"" + classText
                  + "\">Not all  required fields were returned. <a href=\"CompareServlet?certifyServletBasicNum="
                  + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
            }
          } else {
            out.println("    <td class=\"" + classText + "\">not run yet</td>");
          }
          out.println("  </tr>");
        }
        out.println("</table>");
        out.println("</br>");
      }
      if (areaBLevel3Progress > 0) {

        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th colspan=\"2\">Intermediate Tests - Level 3 - Supports Optional</th>");
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : statusCheckQueryTestCaseIntermediateList) {
          String classText = "nottested";
          boolean passedAllOptional = false;
          if (testCaseMessage.isHasRun() && testCaseMessage.getComparisonList() != null) {
            passedAllOptional = true;
            for (CompareManager.Comparison comparison : testCaseMessage.getComparisonList()) {
              if (comparison.isTested()
                  && comparison.getPriorityLevel() == CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL
                  && !comparison.isPass()) {
                passedAllOptional = false;
                break;
              }
            }
          }
          if (testCaseMessage.isHasRun()) {
            classText = testCaseMessage.isPassedTest() && passedAllOptional ? "pass" : "fail";
          }
          out.println("  <tr>");
          out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
          if (testCaseMessage.isHasRun()) {
            if (testCaseMessage.isPassedTest()) {

              if (passedAllOptional) {
                out.println("    <td class=\"" + classText
                    + "\">All required and optional fields returned. <a href=\"CompareServlet?certifyServletBasicNum="
                    + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
              } else {

                out.println("    <td class=\"" + classText
                    + "\">Required fields were not returned. <a href=\"CompareServlet?certifyServletBasicNum="
                    + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
              }
            } else {
              out.println("    <td class=\""
                  + classText
                  + "\">Not all optional and/or required fields were returned. <a href=\"CompareServlet?certifyServletBasicNum="
                  + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
            }
          } else {
            out.println("    <td class=\"" + classText + "\">not run yet</td>");
          }
          out.println("  </tr>");
        }
        out.println("</table>");
        out.println("</br>");
      }

      for (int i = 0; i < areaCLevelScore.length; i++) {
        if (profileTestCaseLists[i] == null || areaCLevelProgress[i] < 100) {
          continue;
        }

        int priority = i + 1;
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        if (i == 0) {
          out.println("    <th colspan=\"2\">Advanced Tests - Level " + priority + " - High Priority</th>");
        } else if (i == 1) {
          out.println("    <th colspan=\"2\">Advanced Tests - Level " + priority + " - Medium Priority</th>");

        } else {

          out.println("    <th colspan=\"2\">Advanced Tests - Level " + priority + " - Low Priority </th>");
        }
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : profileTestCaseLists[i]) {
          String classText = "nottested";
          if (testCaseMessage.hasIssue() && testCaseMessage.isHasRun()) {
            classText = testCaseMessage.isPassedTest() ? "pass" : "fail";
          }
          out.println("  <tr>");
          out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
          if (!testCaseMessage.hasIssue()) {
            out.println("    <td class=\"" + classText + "\">issue is not supported by this tester</td>");
          } else {
            if (testCaseMessage.isHasRun()) {
              if (testCaseMessage.isPassedTest()) {
                out.println("    <td class=\"" + classText
                    + "\">Issue was identified. <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                    + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
              } else {
                out.println("    <td class=\"" + classText
                    + "\">Issue was NOT identified. <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                    + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
              }
            } else {
              out.println("    <td class=\"" + classText + "\">not run yet</td>");
            }
          }
          out.println("  </tr>");
        }
        out.println("</table>");
        out.println("</br>");
      }
      if (areaDLevel1Progress > 0) {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th colspan=\"2\">Exceptional Tests - Level 1 - ACK Correctly Formatted</th>");
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : ackAnalysisList) {
          String classText = "nottested";
          HL7Analyzer ackAnalyzer = testCaseMessage.getAckAnalyzer();
          if (ackAnalyzer != null) {
            classText = ackAnalyzer.isPassedTest() ? "pass" : "fail";
          }
          out.println("  <tr>");
          out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
          if (ackAnalyzer != null) {
            if (ackAnalyzer.isPassedTest()) {
              out.println("    <td class=\""
                  + classText
                  + "\">Ack meets HL7 and CDC standards.  <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                  + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
            } else {
              out.println("    <td class=\""
                  + classText
                  + "\">Ack did not meet HL7 or CDC standards. <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                  + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
            }
          } else {
            out.println("    <td class=\"" + classText + "\">not run yet</td>");
          }
          out.println("  </tr>");
        }
        out.println("</table>");
        out.println("</br>");
      }

      if (areaDLevel2Progress > 0) {
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th colspan=\"2\">Exceptional Tests - Level 2 - RSP Correctly Formatted</th>");
        out.println("  </tr>");
        for (TestCaseMessage testCaseMessage : rspAnalysisList) {
          String classText = "nottested";
          HL7Analyzer ackAnalyzer = testCaseMessage.getAckAnalyzer();
          if (ackAnalyzer != null) {
            classText = ackAnalyzer.isPassedTest() ? "pass" : "fail";
          }
          out.println("  <tr>");
          out.println("    <td class=\"" + classText + "\"><em>" + testCaseMessage.getDescription() + "</em></td>");
          if (ackAnalyzer != null) {
            if (ackAnalyzer.isPassedTest()) {
              out.println("    <td class=\""
                  + classText
                  + "\">RSP meets HL7 and CDC standards.  <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                  + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
            } else {
              out.println("    <td class=\""
                  + classText
                  + "\">RSP did not meet HL7 or CDC standards. <a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum="
                  + testCaseMessage.getTestCaseId() + "\">details</a></a></td>");
            }
          } else {
            out.println("    <td class=\"" + classText + "\">not run yet</td>");
          }
          out.println("  </tr>");
        }
        out.println("</table>");
        out.println("</br>");
      }

    }

    private int makeScore(int num, int denom) {
      return (int) (100.0 * num / denom + 0.5);
    }

    public String getStatus() {
      return status;
    }

    public void stopRunning() {
      keepRunning = false;
    }

    private String paddWithZeros(int i, int zeros) {
      String s = "000000" + i;
      return s.substring(s.length() - zeros);
    }
  }
}
