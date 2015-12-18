/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsage;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;

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
        List<Connector> connectors = ConnectServlet.getConnectors(session);
        int id = Integer.parseInt(request.getParameter("id"));
        String queryType = request.getParameter("queryType");

        certifyRunner = new CertifyRunner(connectors.get(id - 1), user.getSendData());
        certifyRunner.setParticipantResponse((ParticipantResponse) session.getAttribute("participantResponse"));
        certifyRunner.setQueryType(queryType);
        certifyRunner.setRun(true, CertifyRunner.SUITE_A_BASIC);
        certifyRunner.setRun(request.getParameter("runA") != null, CertifyRunner.SUITE_A_BASIC);
        certifyRunner.setRun(request.getParameter("runB") != null, CertifyRunner.SUITE_B_INTERMEDIATE);
        certifyRunner.setRun(request.getParameter("runC") != null, CertifyRunner.SUITE_C_ADVANCED);
        certifyRunner.setRun(request.getParameter("runD") != null, CertifyRunner.SUITE_D_EXCEPTIONAL);
        certifyRunner.setRun(request.getParameter("runF") != null, CertifyRunner.SUITE_E_FORECAST_PREP);
        certifyRunner.setRun(request.getParameter("runF") != null, CertifyRunner.SUITE_F_FORECAST);
        certifyRunner.setRun(request.getParameter("runH") != null, CertifyRunner.SUITE_H_CONFORMANCE);
        certifyRunner.setRun(request.getParameter("runI") != null, CertifyRunner.SUITE_I_PROFILING);
        certifyRunner.setRun(request.getParameter("runJ") != null, CertifyRunner.SUITE_J_ONC_2015);
        certifyRunner.setRun(request.getParameter("runK") != null, CertifyRunner.SUITE_K_NOT_ACCEPTED);
        certifyRunner.setRun(request.getParameter("runL") != null, CertifyRunner.SUITE_L_CONFORMANCE_2015);
        certifyRunner.setRun(request.getParameter("runM") != null, CertifyRunner.SUITE_M_QBP_SUPPORT);
        certifyRunner.setPauseBeforeQuerying(request.getParameter("pauseBeforeQuerying") != null);
        if (certifyRunner.isRun(CertifyRunner.SUITE_E_FORECAST_PREP)) {
          Map<Integer, ForecastTestPanel> forecastTestPanelIdMap = new HashMap<Integer, ForecastTestPanel>();
          for (ForecastTestPanel forecastTestPanel : ForecastTestPanel.values()) {
            forecastTestPanelIdMap.put(forecastTestPanel.getId(), forecastTestPanel);
          }
          for (String s : request.getParameterValues("forecastTestPanelId")) {
            ForecastTestPanel forecastTestPanel = forecastTestPanelIdMap.get(Integer.parseInt(s));
            certifyRunner.addForecastTestPanel(forecastTestPanel);
          }
        }
        if (certifyRunner.isRun(CertifyRunner.SUITE_I_PROFILING)) {
          initProfileManager(false);
          certifyRunner.setProfileManager(profileManager);
          String profileUsageIdString = request.getParameter("profileUsageId");
          String profileUsageIdForInteroperabilityString = request.getParameter("profileUsageIdForInteroperability");
          String profileUsageIdForConformanceString = request.getParameter("profileUsageIdForConformance");
          if (!profileUsageIdString.equals("")) {
            int profileUsageId = Integer.parseInt(profileUsageIdString);
            session.setAttribute("profileUsageId", profileUsageId);
            certifyRunner.setProfileUsage(profileManager.getProfileUsageList().get(profileUsageId - 1));
          }
          if (!profileUsageIdForInteroperabilityString.equals("")) {
            int profileUsageId = Integer.parseInt(profileUsageIdForInteroperabilityString);
            certifyRunner.setProfileUsageComparisonInteroperability(
                profileManager.getProfileUsageList().get(profileUsageId - 1));
          }
          if (!profileUsageIdForConformanceString.equals("")) {
            int profileUsageId = Integer.parseInt(profileUsageIdForConformanceString);
            certifyRunner
                .setProfileUsageComparisonConformance(profileManager.getProfileUsageList().get(profileUsageId - 1));
          }
        }

        String runAgainst = request.getParameter("runAgainst");
        if (runAgainst != null && !runAgainst.equals("")) {
          SendData sendData = user.getSendData();
          if (sendData != null) {
            File runAgainstFolder = new File(sendData.getRootDir(), runAgainst);
            if (runAgainstFolder.exists() && runAgainstFolder.isDirectory()) {
              certifyRunner.setRunAgainstFolder(runAgainstFolder);
            } else {
              File testCaseDir = sendData.getTestCaseDir(false);
              if (testCaseDir != null) {
                runAgainstFolder = new File(testCaseDir, runAgainst);
              }
            }
          }
        }
        session.setAttribute("certifyRunner", certifyRunner);
        certifyRunner.start();
      }
    } else if (action.equals("Stop")) {
      if (certifyRunner != null) {
        certifyRunner.stopRunning();
      }
    } else if (action.equals("Continue")) {
      if (certifyRunner != null) {
        synchronized (certifyRunner) {
          certifyRunner.setPauseBeforeQuerying(false);
          certifyRunner.interrupt();
        }
      }
    }
    doGet(request, response, session, problem);
  }

  // <editor-fold defaultstate="collapsed"
  // desc="HttpServlet methods. Click on the + sign on the left to edit the
  // code.">

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

    String username = (String) session.getAttribute("username");
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    }

    PrintWriter out = response.getWriter();

    try {
      printHtmlHead(out, MENU_HEADER_TEST, request);

      CertifyHistoryServlet.printViewMenu(out, "", true);

      if (problem != null) {
        out.println("<p>" + problem + "</p>");
      }
      CertifyRunner certifyRunner = (CertifyRunner) session.getAttribute("certifyRunner");
      boolean canStart = certifyRunner == null || certifyRunner.getStatus().equals(CertifyRunner.STATUS_COMPLETED)
          || certifyRunner.getStatus().equals(CertifyRunner.STATUS_STOPPED);

      if (certifyRunner != null) {
        if (!canStart) {
          out.println("<script>");
          out.println("  var refreshCount = 0;");
          out.println("  function checkRefresh()");
          out.println("  {");
          out.println("    refreshCount++;");
          out.println("    if (refreshCount > 30)");
          out.println("    {");
          out.println("      window.location.href=\"CertifyServlet\"");
          out.println("    }");
          out.println("    else");
          out.println("    {");
          out.println("      setTimeout('checkRefresh()', 1000);");
          out.println("    }");
          out.println("  }");
          out.println("  checkRefresh(); ");
          out.println("</script>");
        }

        out.println("    <h2>" + certifyRunner.getConnector().getLabel() + " Test Results</h2>");
        certifyRunner.printResults(out);
        out.println("    <form action=\"CertifyServlet\" method=\"POST\">");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Refresh\"/>");
        if (!canStart) {
          out.println("      <input type=\"submit\" name=\"action\" value=\"Stop\"/>");
        }
        if (certifyRunner.getStatus().equals(CertifyRunner.STATUS_PAUSED)) {
          out.println("      <input type=\"submit\" name=\"action\" value=\"Continue\"/>");
        }
        out.println("    </form>");
      }

      if (canStart) {
        initProfileManager(false);
        out.println("    <h2>Start Test</h2>");
        out.println("    <form action=\"CertifyServlet\" method=\"POST\">");
        out.println("      <table border=\"0\">");
        printServiceSelector(request, session, out);
        out.println("        <tr>");
        out.println("          <td>Query Type</td>");
        out.println("          <td>");
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"N\" checked=\"true\"> None");
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"Q\"> QBP");
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"V\"> VXQ");
        out.println("            <input type=\"checkbox\" name=\"pauseBeforeQuerying\" value=\"true\"/> Pause");
        out.println("          </td>");
        out.println("        </tr>");
        SendData sendData = user.getSendData();
        if (sendData != null) {
          out.println("        <tr>");
          out.println("          <td>Run Against</td>");
          out.println("          <td>");
          out.println("            <select name=\"runAgainst\">");
          out.println("              <option value=\"\">Realtime Interface</option>");
          String[] testNames = sendData.getTestReportNames();
          for (String testName : testNames) {
            out.println("              <option value=\"" + testName + "\">" + testName + "</option>");
          }
          out.println("          </td>");
          out.println("        </tr>");
        }

        out.println("        <tr>");
        out.println("          <td colspan=\"2\">Tests to Run</td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runA\" value=\"true\" checked=\"true\"/> Basic");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runJ\" value=\"true\" checked=\"true\"/> ONC 2015");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input type=\"checkbox\" name=\"runK\" value=\"true\" checked=\"true\"/> Not Accepted");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input type=\"checkbox\" name=\"runB\" value=\"true\" checked=\"true\"/> Intermediate");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runC\" value=\"true\" checked=\"true\"/> Advanced");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        if (profileManager != null) {
          out.println("        <tr>");
          out.println("          <td valign=\"top\">");
          out.println("            <input type=\"checkbox\" name=\"runI\" value=\"true\" checked=\"true\"/> Profiling");
          out.println("          </td>");
          int profileUsageId = profileManager.getProfileUsageList().size();
          if (session.getAttribute("profileUsageId") != null) {
            profileUsageId = (Integer) session.getAttribute("profileUsageId");
          }
          out.println("          <td>");
          out.println("            Verify requirements:");
          {
            out.println("            <select name=\"profileUsageId\">");
            out.println("              <option value=\"\">select</option>");
            int i = 0;
            for (ProfileUsage profileUsage : profileManager.getProfileUsageList()) {
              i++;
              if (profileUsageId == i) {
                out.println(
                    "              <option value=\"" + i + "\" selected=\"true\">" + profileUsage + "</option>");
              } else {
                out.println("              <option value=\"" + i + "\">" + profileUsage + "</option>");
              }
            }
          }
          out.println("            </select>");
          out.println("            <br/>");
          out.println("            Compare for conformance:");
          {
            out.println("            <select name=\"profileUsageIdForConformance\">");
            out.println("              <option value=\"\">select</option>");
            int i = 0;
            for (ProfileUsage profileUsage : profileManager.getProfileUsageList()) {
              i++;
              if (profileUsage.toString().equals("US - Base")) {
                out.println(
                    "              <option value=\"" + i + "\" selected=\"true\">" + profileUsage + "</option>");
              } else {
                out.println("              <option value=\"" + i + "\">" + profileUsage + "</option>");
              }
            }
          }
          out.println("            </select>");
          out.println("            <br/>");
          out.println("            Compare for interoperability: ");
          {
            out.println("            <select name=\"profileUsageIdForInteroperability\">");
            out.println("              <option value=\"\">select</option>");
            int i = 0;
            for (ProfileUsage profileUsage : profileManager.getProfileUsageList()) {
              i++;
              out.println("              <option value=\"" + i + "\">" + profileUsage + "</option>");
            }
          }
          out.println("            </select>");
          out.println("          </td>");
          out.println("        </tr>");
        }
        out.println("        <tr>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runD\" value=\"true\" checked=\"true\"/> Exceptional");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input type=\"checkbox\" name=\"runM\" value=\"true\" checked=\"true\"/> QBP Support");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td valign=\"top\">");
        out.println("            <input type=\"checkbox\" name=\"runF\" value=\"true\"/> Forecasting");
        List<ForecastTestPanel> forecastTestPanelList = Arrays.asList(ForecastTestPanel.values());
        out.println("          </td>");
        out.println("          <td>TCH Forecaster Test Panel<br/>");
        out.println("            <select multiple size=\"5\" name=\"forecastTestPanelId\">");
        for (ForecastTestPanel forecastTestPanel : forecastTestPanelList) {
          if (forecastTestPanel.isStandard()) {
            out.println("              <option value=\"" + forecastTestPanel.getId() + "\" selected" + ">"
                + forecastTestPanel.getLabel() + "</option>");
          } else {
            out.println("              <option value=\"" + forecastTestPanel.getId() + "\">"
                + forecastTestPanel.getLabel() + "</option>");
          }
        }
        out.println("            ");
        out.println("            </select>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input type=\"checkbox\" name=\"runG\" value=\"true\" checked=\"true\" disabled=\"disabled\"/> Performance");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runH\" value=\"true\" checked=\"true\"/> Conformance");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input type=\"checkbox\" name=\"runL\" value=\"true\" checked=\"true\"/> Conformance 2015");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td></td>");
        out.println("          <td colspan=\"2\" align=\"right\">");
        out.println("            <input type=\"submit\" name=\"action\" value=\"Start\"/>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("      </table>");
        out.println("    </form>");
      }
      if (certifyRunner != null) {
        out.println("    <h2>Test Details</h2>");
        certifyRunner.printProgressDetails(out, false);
      }
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }
}
