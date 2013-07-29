/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.ScenarioManager;
import org.immunizationsoftware.dqa.tester.run.ErrorType;
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
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");
    String action = request.getParameter("action");
    String problem = null;
    CertifyRunner certifyRunner = (CertifyRunner) session.getAttribute("certifyRunner");
    if (username == null)
    {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else if (action.equals("Start"))
    {
      if (certifyRunner != null && !certifyRunner.getStatus().equals(CertifyRunner.STATUS_COMPLETED))
      {
        problem = "Unable to start new certification as current certification is still running.";
      } else
      {
        List<Connector> connectors = SetupServlet.getConnectors(session);
        int id = Integer.parseInt(request.getParameter("id"));
        certifyRunner = new CertifyRunner(connectors.get(id - 1));
        session.setAttribute("certifyRunner", certifyRunner);
        certifyRunner.start();
      }
    } else if (action.equals("Stop"))
    {
      if (certifyRunner != null)
      {
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null)
    {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else
    {
      doGet(request, response, session, null);
    }
  }

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session, String problem) throws IOException
  {
    PrintWriter out = response.getWriter();
    try
    {
      printHtmlHead(out, "Certify", request);

      if (problem != null)
      {
        out.println("<p>" + problem + "</p>");
      }

      CertifyRunner certifyRunner = (CertifyRunner) session.getAttribute("certifyRunner");
      if (certifyRunner != null)
      {
        out.println("    <h3>Certification Results</h3>");
        certifyRunner.printResults(out);
        out.println("    <form action=\"CertifyServlet\" method=\"POST\">");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Refresh\"/>");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Stop\"/>");
        out.println("    </form>");
      }

      boolean canStart = certifyRunner == null || certifyRunner.getStatus().equals(CertifyRunner.STATUS_COMPLETED);
      if (canStart)
      {

        out.println("    <h3>Start Certification Evaluation</h3>");
        out.println("    <form action=\"CertifyServlet\" method=\"POST\">");
        out.println("      <table border=\"0\">");
        int id = 0;
        if (request.getParameter("id") != null)
        {
          id = Integer.parseInt(request.getParameter("id"));
        }
        if (session.getAttribute("id") != null)
        {
          id = (Integer) session.getAttribute("id");
        }
        out.println("        <tr>");
        out.println("          <td>Service</td>");
        out.println("          <td>");
        List<Connector> connectors = SetupServlet.getConnectors(session);
        if (connectors.size() == 1)
        {
          out.println("            " + connectors.get(0).getLabelDisplay());
          out.println("            <input type=\"hidden\" name=\"id\" value=\"1\"/>");
        } else
        {
          out.println("            <select name=\"id\">");
          out.println("              <option value=\"\">select</option>");
          int i = 0;
          for (Connector connector : connectors)
          {
            i++;
            if (id == i)
            {
              out.println("              <option value=\"" + i + "\" selected=\"true\">" + connector.getLabelDisplay() + "</option>");
            } else
            {
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
      if (certifyRunner != null)
      {
        out.println("    <h3>Certification Details</h3>");
        certifyRunner.printProgressDetails(out);
      }
      printHtmlFoot(out);
    } finally
    {
      out.close();
    }
  }

  private class CertifyRunner extends Thread
  {
    public static final String STATUS_INITIALIZED = "Initialized";
    public static final String STATUS_STARTED = "Started";
    public static final String STATUS_COMPLETED = "Completed";

    private String status = "";
    private boolean keepRunning = true;
    private TestCaseMessage testCaseMessageBase = null;

    private int areaALevel1Score = -1;
    private int areaALevel2Score = -1;
    private int areaALevel3Score = -1;

    private int areaBLevel1Score = -1;
    private int areaBLevel2Score = -1;
    private int areaBLevel3Score = -1;

    private int areaCLevel1Score = -1;
    private int areaCLevel2Score = -1;
    private int areaCLevel3Score = -1;

    private int areaDLevel1Score = -1;
    private int areaDLevel2Score = -1;
    private int areaDLevel3Score = -1;

    private int areaALevel1Progress = -1;
    private int areaALevel2Progress = -1;
    private int areaALevel3Progress = -1;

    private int areaBLevel1Progress = -1;
    private int areaBLevel2Progress = -1;
    private int areaBLevel3Progress = -1;

    private int areaCLevel1Progress = -1;
    private int areaCLevel2Progress = -1;
    private int areaCLevel3Progress = -1;

    private int areaDLevel1Progress = -1;
    private int areaDLevel2Progress = -1;
    private int areaDLevel3Progress = -1;

    private List<TestCaseMessage> statusCheckTestCases = new ArrayList<TestCaseMessage>();
    private String[] statusCheckScenarios = { ScenarioManager.SCENARIO_1_R_ADMIN_CHILD, ScenarioManager.SCENARIO_2_R_ADMIN_ADULT,
        ScenarioManager.SCENARIO_3_R_HISTORICAL_CHILD, ScenarioManager.SCENARIO_4_R_CONSENTED_CHILD, ScenarioManager.SCENARIO_5_R_REFUSED_TODDLER,
        ScenarioManager.SCENARIO_6_R_VARICELLA_HISTORY_CHILD, ScenarioManager.SCENARIO_7_R_COMPLETE_RECORD };

    private List<TestCaseMessage> profileTestCases = new ArrayList<TestCaseMessage>();

    Connector connector;

    public CertifyRunner(Connector connector) {
      this.connector = connector;
      status = STATUS_INITIALIZED;
    }

    @Override
    public void run()
    {
      status = STATUS_STARTED;
      Transformer transformer = new Transformer();
      for (String scenario : statusCheckScenarios)
      {
        TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(scenario);
        statusCheckTestCases.add(testCaseMessage);
        transformer.transform(testCaseMessage);
        testCaseMessage.setAssertResult("Accept - *");
      }

      int testPass = 0;
      int testNoWarningsOrErrors = 0;
      TestRunner testRunner = new TestRunner();
      int count = 0;
      for (TestCaseMessage testCaseMessage : statusCheckTestCases)
      {
        count++;
        try
        {
          boolean pass = testRunner.runTest(connector, testCaseMessage);
          if (pass)
          {
            testPass++;
            testCaseMessageBase = testCaseMessage;
          } else
          {
            testCaseMessageBase = null;
          }
          boolean foundProblem = false;
          testCaseMessage.setErrorList(testRunner.getErrorList());
          for (TestRunner.Error error : testRunner.getErrorList())

          {
            if (error.getErrorType() == org.immunizationsoftware.dqa.tester.run.ErrorType.UNKNOWN
                || error.getErrorType() == org.immunizationsoftware.dqa.tester.run.ErrorType.ERROR
                || error.getErrorType() == org.immunizationsoftware.dqa.tester.run.ErrorType.WARNING)
            {
              foundProblem = true;
              break;
            }
          }
          if (!foundProblem)
          {
            testNoWarningsOrErrors++;
          }
          areaALevel1Progress = makeScore(count, statusCheckTestCases.size());
          areaALevel2Progress = areaBLevel1Progress;
        } catch (Throwable t)
        {
          testCaseMessage.setException(t);
        }
      }
      areaALevel1Score = makeScore(testPass, statusCheckTestCases.size());
      areaALevel2Score = makeScore(testNoWarningsOrErrors, statusCheckTestCases.size());

      if (testCaseMessageBase != null)
      {

        int countPass = 0;
        count = 0;
        for (Issue issue : Issue.values())
        {
          count++;
          TestCaseMessage testCaseMessage = new TestCaseMessage(testCaseMessageBase);
          profileTestCases.add(testCaseMessage);
          testCaseMessage.setDescription(issue.getName());
          testCaseMessage.addCauseIssues(issue.getName());
          transformer.transform(testCaseMessage);
          String ack;
          try
          {
            testRunner.runTest(connector, testCaseMessage);
            ack = testRunner.getAck();
            if (issue.getDefaultErrorType() == testRunner.getErrorType())
            {
              countPass++;
            }
          } catch (Throwable t)
          {
            testCaseMessage.setException(t);
          }
          areaCLevel1Progress = makeScore(count, Issue.values().length);
        }
        areaCLevel1Score = makeScore(countPass, Issue.values().length);

      }

      status = STATUS_COMPLETED;
    }

    public void printResults(PrintWriter out)
    {
      out.println("<table border=\"1\" colspacing=\"0\">");
      out.println("  <tr>");
      out.println("    <th width=\"12%\"></th>");
      out.println("    <th width=\"22%\"><font size=\"+1\">Area A</font><br/>Support for NIST 2014 <br/>Certification Scenarios</th>");
      out.println("    <th width=\"22%\"><font size=\"+1\">Area B</font><br/>Semantic <br/>Interoperability</th>");
      out.println("    <th width=\"22%\"><font size=\"+1\">Area C</font><br/>Sensitivity to Known <br/>Data Quality Issues</th>");
      out.println("    <th width=\"22%\"><font size=\"+1\">Area D</font><br/>Response Format of <br/>Acks and Queries</th>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Level 1</th>");
      if (areaALevel1Score >= 100)
      {
        out.println("    <td class=\"pass\">All scenarios accepted<br/>Pass: Score = " + areaALevel1Score + "%</td>");
      } else if (areaALevel1Score >= 0)
      {
        out.println("    <td class=\"fail\">All scenarios accepted<br/>Fail: Score = " + areaALevel1Score + "%</td>");
      } else
      {
        if (areaALevel1Progress > -1)
        {
          out.println("    <td>Running, " + areaALevel1Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      if (areaBLevel1Score >= 100)
      {
        out.println("    <td class=\"pass\"><br/>Pass: Score = " + areaBLevel1Score + "%</td>");
      } else if (areaBLevel1Score >= 0)
      {
        out.println("    <td class=\"fail\"><br/>Fail: Score = " + areaBLevel1Score + "%</td>");
      } else
      {
        if (areaBLevel1Progress > -1)
        {
          out.println("    <td>Running, " + areaBLevel1Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      if (areaCLevel1Score >= 100)
      {
        out.println("    <td class=\"pass\">Class I issues are noted and warnings or errors are generated corretly.<br/>Pass: Score = "
            + areaCLevel1Score + "%</td>");
      } else if (areaCLevel1Score >= 0)
      {
        out.println("    <td class=\"fail\">Class I issues are noted and warnings or errors are generated corretly.<br/>Fail: Score = "
            + areaCLevel1Score + "%</td>");
      } else
      {
        if (areaCLevel1Progress > -1)
        {
          out.println("    <td>Running, " + areaCLevel1Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      if (areaDLevel1Score >= 100)
      {
        out.println("    <td class=\"pass\"><br/>Pass: Score = " + areaDLevel1Score + "%</td>");
      } else if (areaDLevel1Score >= 0)
      {
        out.println("    <td class=\"fail\"><br/>Fail: Score = " + areaDLevel1Score + "%</td>");
      } else
      {
        if (areaDLevel1Progress > -1)
        {
          out.println("    <td>Running, " + areaDLevel1Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Level 2</th>");
      if (areaALevel2Score >= 100)
      {
        out.println("    <td class=\"pass\">All scenarios accepted without warnings<br/>Pass: Score = " + areaALevel2Score + "%</td>");
      } else if (areaALevel2Score >= 0)
      {
        out.println("    <td class=\"fail\">All scenarios accepted without warnings<br/>Fail: Score = " + areaALevel2Score + "%</td>");
      } else
      {
        if (areaALevel2Progress > -1)
        {
          out.println("    <td>Running, " + areaALevel2Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      if (areaBLevel2Score >= 100)
      {
        out.println("    <td class=\"pass\"><br/>Pass: Score = " + areaBLevel2Score + "%</td>");
      } else if (areaBLevel2Score >= 0)
      {
        out.println("    <td class=\"fail\"><br/>Fail: Score = " + areaBLevel2Score + "%</td>");
      } else
      {
        if (areaBLevel2Progress > -1)
        {
          out.println("    <td>Running, " + areaBLevel2Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      if (areaCLevel2Score >= 100)
      {
        out.println("    <td class=\"pass\"><br/>Pass: Score = " + areaCLevel2Score + "%</td>");
      } else if (areaCLevel2Score >= 0)
      {
        out.println("    <td class=\"fail\"><br/>Fail: Score = " + areaCLevel2Score + "%</td>");
      } else
      {
        if (areaCLevel2Progress > -1)
        {
          out.println("    <td>Running, " + areaCLevel2Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      if (areaDLevel2Score >= 100)
      {
        out.println("    <td class=\"pass\"><br/>Pass: Score = " + areaDLevel2Score + "%</td>");
      } else if (areaDLevel2Score >= 0)
      {
        out.println("    <td class=\"fail\"><br/>Fail: Score = " + areaDLevel2Score + "%</td>");
      } else
      {
        if (areaDLevel2Progress > -1)
        {
          out.println("    <td>Running, " + areaDLevel2Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <th>Level 3</th>");
      if (areaALevel3Score >= 100)
      {
        out.println("    <td class=\"pass\">All scenarios accepted<br/>Pass: Score = " + areaALevel3Score + "%</td>");
      } else if (areaALevel3Score >= 0)
      {
        out.println("    <td class=\"fail\">All scenarios accepted<br/>Fail: Score = " + areaALevel3Score + "%</td>");
      } else
      {
        if (areaALevel3Progress > -1)
        {
          out.println("    <td>Running, " + areaALevel3Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      if (areaBLevel3Score >= 100)
      {
        out.println("    <td class=\"pass\"><br/>Pass: Score = " + areaBLevel3Score + "%</td>");
      } else if (areaBLevel3Score >= 0)
      {
        out.println("    <td class=\"fail\"><br/>Fail: Score = " + areaBLevel3Score + "%</td>");
      } else
      {
        if (areaBLevel3Progress > -1)
        {
          out.println("    <td>Running, " + areaBLevel3Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      if (areaCLevel3Score >= 100)
      {
        out.println("    <td class=\"pass\"><br/>Pass: Score = " + areaCLevel3Score + "%</td>");
      } else if (areaCLevel3Score >= 0)
      {
        out.println("    <td class=\"fail\"><br/>Fail: Score = " + areaCLevel3Score + "%</td>");
      } else
      {
        if (areaCLevel3Progress > -1)
        {
          out.println("    <td>Running, " + areaCLevel3Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      if (areaDLevel3Score >= 100)
      {
        out.println("    <td class=\"pass\"><br/>Pass: Score = " + areaDLevel3Score + "%</td>");
      } else if (areaDLevel3Score >= 0)
      {
        out.println("    <td class=\"fail\"><br/>Fail: Score = " + areaDLevel3Score + "%</td>");
      } else
      {
        if (areaDLevel3Progress > -1)
        {
          out.println("    <td>Running, " + areaDLevel3Progress + "% complete.</td>");
        } else
        {
          out.println("    <td>Not Run Yet</td>");
        }
      }
      out.println("  </tr>");
      out.println("</table>");
    }

    public void printProgressDetails(PrintWriter out)
    {
      for (TestCaseMessage testCaseMessage : statusCheckTestCases)
      {

        printTestCaseMessage(out, testCaseMessage);

      }
      for (TestCaseMessage testCaseMessage : profileTestCases)
      {

        printTestCaseMessage(out, testCaseMessage);

      }
    }

    public void printTestCaseMessage(PrintWriter out, TestCaseMessage testCaseMessage)
    {
      out.println("<p>" + testCaseMessage.getDescription() + "</p>");
      out.println("<pre>" + testCaseMessage.getMessageText() + "</pre>");
      out.println("<table>");
      out.println("  <tr>");
      out.println("    <td>Assert Result</td>");
      out.println("    <td>" + testCaseMessage.getAssertResult() + "</td>");
      out.println("  </tr>");
      if (testCaseMessage.getCauseIssues() != null && testCaseMessage.getCauseIssues().equals(""))
      {
        out.println("  <tr>");
        out.println("    <td>Cause Issues</td>");
        out.println("    <td>" + testCaseMessage.getCauseIssues() + "</td>");
        out.println("  </tr>");
      }
      out.println("</table>");
      if (testCaseMessage.getException() != null)
      {
        out.println("<p>Exception occurred: " + testCaseMessage.getException().getMessage() + "</p>");
        out.print("<pre>");
        testCaseMessage.getException().printStackTrace(out);
        out.print("</pre>");
      }
      out.println("<pre>" + testCaseMessage.getActualAck() + "</pre>");
      out.println("<table>");
      out.println("  <tr>");
      out.println("    <td>Ack Message</td>");
      out.println("    <td>" + testCaseMessage.getActualResultAckMessage() + "</td>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td>Ack Type</td>");
      out.println("    <td>" + testCaseMessage.getActualResultAckType() + "</td>");
      out.println("  </tr>");
      out.println("  <tr>");
      out.println("    <td>Result Status</td>");
      out.println("    <td>" + testCaseMessage.getActualResultStatus() + "</td>");
      out.println("  </tr>");
      List<TestRunner.Error> errorList = testCaseMessage.getErrorList();
      if (errorList != null)
      {
        for (TestRunner.Error error : errorList)
        {
          out.println("  <tr>");
          out.println("    <td>" + error.getErrorType() + "</td>");
          out.println("    <td>" + error.getDescription() + "</td>");
          out.println("  </tr>");
        }
      }
      out.println("</table>");
    }

    private int makeScore(int num, int denom)
    {
      return (int) (100.0 * num / denom + 0.5);
    }

    public String getStatus()
    {
      return status;
    }

    public void stopRunning()
    {
      keepRunning = false;
    }
  }
}
