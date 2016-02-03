/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.certify.CertifyRunner;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsage;
import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;
import org.openimmunizationsoftware.dqa.tr.RecordServletInterface;

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
    } else if (action != null) {

      if (action.equals("Start")) {
        if (request.getParameter("id") == null) {
          problem = "Unable to start service is not selected";
        } else if (certifyRunner != null && !certifyRunner.getStatus().equals(CertifyRunner.STATUS_COMPLETED)
            && !certifyRunner.getStatus().equals(CertifyRunner.STATUS_STOPPED)
            && !certifyRunner.getStatus().equals(CertifyRunner.STATUS_PROBLEM)) {
          problem = "Unable to start new certification as current certification is still running.";
        } else {
          List<Connector> connectors = ConnectServlet.getConnectors(session);
          int id = Integer.parseInt(request.getParameter("id"));
          String queryType = request.getParameter("queryType");

          certifyRunner = new CertifyRunner(connectors.get(id - 1), user.getSendData(), queryType);
          certifyRunner.setParticipantResponse((ParticipantResponse) session.getAttribute("participantResponse"));
          certifyRunner.setRun(true, CertifyRunner.SUITE_A_BASIC);
          certifyRunner.setRun(request.getParameter("runA") != null, CertifyRunner.SUITE_A_BASIC);
          certifyRunner.setRun(request.getParameter("runB") != null, CertifyRunner.SUITE_B_INTERMEDIATE);
          certifyRunner.setRun(request.getParameter("runC") != null, CertifyRunner.SUITE_C_ADVANCED);
          certifyRunner.setRun(request.getParameter("runD") != null, CertifyRunner.SUITE_D_EXCEPTIONAL);
          certifyRunner.setRun(request.getParameter("runF") != null, CertifyRunner.SUITE_F_FORECAST);
          certifyRunner.setRun(true, CertifyRunner.SUITE_G_PERFORMANCE);
          certifyRunner.setRun(request.getParameter("runH") != null, CertifyRunner.SUITE_H_CONFORMANCE);
          certifyRunner.setRun(request.getParameter("runI") != null, CertifyRunner.SUITE_I_PROFILING);
          certifyRunner.setRun(request.getParameter("runJ") != null, CertifyRunner.SUITE_J_ONC_2015);
          certifyRunner.setRun(request.getParameter("runK") != null, CertifyRunner.SUITE_K_NOT_ACCEPTED);
          certifyRunner.setRun(request.getParameter("runL") != null, CertifyRunner.SUITE_L_CONFORMANCE_2015);
          certifyRunner.setRun(request.getParameter("runM") != null, CertifyRunner.SUITE_M_QBP_SUPPORT);
          certifyRunner.setRun(request.getParameter("runN") != null, CertifyRunner.SUITE_N_TRANSFORM);
          certifyRunner.setRun(request.getParameter("runO") != null, CertifyRunner.SUITE_O_EXTRA);
          certifyRunner.setRun(request.getParameter("runP") != null, CertifyRunner.SUITE_P_DEDUPLICATION_ENGAGED);
          certifyRunner.setRun(request.getParameter("runQ") != null, CertifyRunner.SUITE_Q_FORECASTER_ENGAGED);
          certifyRunner.setPauseBeforeQuerying(request.getParameter("pauseBeforeQuerying") != null);
          certifyRunner.setRedactListResponses(request.getParameter("redactListResponses") != null);
          certifyRunner.setReportErrorsOnly(request.getParameter("reportErrorsOnly") != null);
          certifyRunner.setCondenseErrors(request.getParameter("condenseErrors") != null);
          certifyRunner.setTestMessageMapMap(CreateTestCaseServlet.getTestCaseMessageMapMap(session));
          if (certifyRunner.isRun(CertifyRunner.SUITE_F_FORECAST)) {
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
            certifyRunner.setRunAgainstTestStartTime(runAgainst);
          }
          session.setAttribute("certifyRunner", certifyRunner);
          certifyRunner.start();
          try {
            // give the certifyRunner 10 seconds to get started before showing
            // status
            TimeUnit.SECONDS.sleep(10);
          } catch (InterruptedException ie) {
            // just keep going
          }
        }
      } else if (action.equals("Stop")) {
        if (certifyRunner != null) {
          certifyRunner.switchStatus(CertifyRunner.STATUS_STOPPED, "Process stopped by user");
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
          || certifyRunner.getStatus().equals(CertifyRunner.STATUS_STOPPED)
          || certifyRunner.getStatus().equals(CertifyRunner.STATUS_PROBLEM);

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

      String aartName = request.getParameter("aartName");
      if (aartName == null) {
        aartName = (String) session.getAttribute("aartName");
      }
      if (aartName != null) {
        session.setAttribute("aartName", aartName);
      }

      if ((certifyRunner != null && !canStart) || (aartName != null && !aartName.equals(""))) {
        printRefreshScript(out);
      }

      String aartAction = null;
      String autoAction = null;
      String autoTestNameSelect = null;

      if (aartName != null && !aartName.equals("")) {
        if (canStart) {
          if (certifyRunner != null) {
            String testerStatus = RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_FINISHED;
            if (certifyRunner.getStatus().equals(CertifyRunner.STATUS_COMPLETED)) {
              testerStatus = RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_FINISHED;
            } else if (certifyRunner.getStatus().equals(CertifyRunner.STATUS_PROBLEM)) {
              testerStatus = RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_ERROR;
            } else if (certifyRunner.getStatus().equals(CertifyRunner.STATUS_STOPPED)) {
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
              ParticipantResponse[][] participantResponseMap = CertifyHistoryServlet.getParticipantResponseMap(session);
              for (int i = 0; i < participantResponseMap.length; i++) {
                for (int j = 0; j < participantResponseMap[i].length; j++) {
                  if (participantResponseMap[i][j] != null
                      && participantResponseMap[i][j].getFolderName().equals(connectionLabel)) {
                    switchParticipantResponse(session, user, participantResponseMap[i][j]);
                    autoAction = "Start";
                    i = participantResponseMap.length;
                    break;
                  }
                }
              }
            }
          }
        } else {
          aartAction = CertifyRunner.reportStatus(aartName,
              RecordServletInterface.PARAM_TESTER_STATUS_TESTER_STATUS_TESTING, certifyRunner.getConnector().getLabel(),
              certifyRunner.getTestStarted(), certifyRunner.getUpdateEtc(), certifyRunner.getQueryEtc());
          if (aartAction.equals("")) {
            aartAction = null;
          } else {
            if (aartAction.equals(RecordServletInterface.PARAM_TESTER_ACTION_STOP)) {
              autoAction = "Stop";
            }
          }
        }
      }

      if (certifyRunner != null) {
        if (autoAction != null && autoAction.equals("Stop")) {
          out.println("    <h2 style=\"color: red; background-color: #FFFF99; \">Test Will Be Stopped</h2>");
        } else {
          out.println("    <h2>" + certifyRunner.getConnector().getLabel() + " " + certifyRunner.getStatus() + "</h2>");
        }

        certifyRunner.printResults(out);
        out.println("    <form id=\"certifyForm\" action=\"CertifyServlet\" method=\"POST\">");

        if (autoAction == null) {
          out.println("      <input type=\"submit\" name=\"action\" value=\"Refresh\"/>");
          if (!canStart) {
            out.println("      <input type=\"submit\" name=\"action\" value=\"Stop\"/>");
          }
        } else if (autoAction.equals("Stop")) {
          out.println("Will Auto Stop in 30 Seconds");
          out.println("            <input type=\"submit\" name=\"action\" value=\"Cancel\"/>");
          out.println("            <input type=\"hidden\" name=\"action\" value=\"Stop\"/>");
        }
        if (certifyRunner.getStatus().equals(CertifyRunner.STATUS_PAUSED)) {
          out.println("      <input type=\"submit\" name=\"action\" value=\"Continue\"/>");
        }
        out.println("    </form>");
      }

      if (canStart) {
        initProfileManager(false);
        SendData sendData = user.getSendData();
        if (autoAction != null && autoAction.equals("Start")) {
          out.println("    <h2 style=\"color: red; background-color: #FFFF99; \">Test Will Auto Start</h2>");
        } else {
          out.println("    <h2>Start Test</h2>");
        }
        out.println("    <form id=\"certifyForm\" action=\"CertifyServlet\" method=\"POST\">");
        out.println("      <table border=\"0\">");
        out.println("        <tr>");
        out.println("          <td>AART Connect</td>");
        out.println("          <td><input type=\"text\" name=\"aartName\" value=\"" + (aartName == null ? "" : aartName)
            + "\" size=\"30\"/></td>");
        out.println("        <tr>");

        printServiceSelector(request, session, out);
        String queryType = CertifyRunner.QUERY_TYPE_NONE;
        boolean readactListResponses = true;
        ParticipantResponse participantResponse = (ParticipantResponse) session.getAttribute("participantResponse");
        if (participantResponse != null) {
          queryType = participantResponse.getQueryType();
          readactListResponses = participantResponse.isRedactListResponses();
        }
        out.println("        <tr>");
        out.println("          <td>Query Type</td>");
        out.println("          <td>");
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_NONE
            + "\"" + (queryType.equals(CertifyRunner.QUERY_TYPE_NONE) ? " checked=\"true\"" : "")
            + " onClick=\"checkQueryOptions(false);\"> " + CertifyRunner.QUERY_TYPE_NONE + "");
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_QBP_Z34
            + "\"" + (queryType.equals(CertifyRunner.QUERY_TYPE_QBP_Z34) ? " checked=\"true\"" : "")
            + " onClick=\"checkQueryOptions(true);\"> " + CertifyRunner.QUERY_TYPE_QBP_Z34);
        out.println(
            "            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_QBP_Z34_Z44
                + "\"" + (queryType.equals(CertifyRunner.QUERY_TYPE_QBP_Z34_Z44) ? " checked=\"true\"" : "")
                + " onClick=\"checkQueryOptions(true);\"> " + CertifyRunner.QUERY_TYPE_QBP_Z34_Z44);
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_QBP_Z44
            + "\"" + (queryType.equals(CertifyRunner.QUERY_TYPE_QBP_Z44) ? " checked=\"true\"" : "")
            + " onClick=\"checkQueryOptions(true);\"> " + CertifyRunner.QUERY_TYPE_QBP_Z44);
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_VXQ
            + "\"" + (queryType.equals(CertifyRunner.QUERY_TYPE_VXQ) ? " checked=\"true\"" : "")
            + " onClick=\"checkQueryOptions(true);\"> " + CertifyRunner.QUERY_TYPE_VXQ);
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>Query Options</td>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"pauseBeforeQuerying\" value=\"true\"/> Pause");
        out.println("            <input type=\"checkbox\" name=\"redactListResponses\" value=\"true\""
            + (readactListResponses ? " checked=\"true\"" : "") + "/> Redact List Responses");
        out.println("          </td>");
        out.println("        </tr>");
        if (sendData != null) {
          List<String> manualReportList = CertifyRunner.getManualReportList(sendData.getConnector().getLabel());
          if (manualReportList.size() > 0) {
            out.println("        <tr>");
            out.println("          <td>Run Against</td>");
            out.println("          <td>");
            out.println("            <select name=\"runAgainst\">");
            out.println("              <option value=\"\">Realtime Interface</option>");
            for (String testName : manualReportList) {
              out.println("              <option value=\"" + testName + "\""
                  + (autoTestNameSelect != null && autoTestNameSelect.equals(testName) ? " selected=\"true\"" : "")
                  + ">" + testName + "</option>");
            }
            out.println("          </td>");
            out.println("        </tr>");
          }
        }

        out.println("        <tr>");
        out.println("          <td colspan=\"2\">Tests to Run</td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input id=\"ChkBasic\" type=\"checkbox\" name=\"runA\" value=\"true\" checked=\"true\"/> Basic");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input id=\"ChkONC2015\" type=\"checkbox\" name=\"runJ\" value=\"true\" checked=\"true\"/> ONC 2015");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input id=\"ChkNotAccepted\" type=\"checkbox\" name=\"runK\" value=\"true\" checked=\"true\"/> Not Accepted");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input id=\"ChkIntermediate\" type=\"checkbox\" name=\"runB\" value=\"true\" checked=\"true\"/> Intermediate");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input id=\"ChkAdvanced\" type=\"checkbox\" name=\"runC\" value=\"true\" checked=\"true\"/> Advanced");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        if (profileManager != null) {
          out.println("        <tr>");
          out.println("          <td valign=\"top\">");
          out.println(
              "            <input id=\"ChkProfiling\" type=\"checkbox\" name=\"runI\" value=\"true\" checked=\"true\"/> Profiling");
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
        out.println(
            "            <input id=\"ChkExceptional\" type=\"checkbox\" name=\"runD\" value=\"true\" checked=\"true\"/> Exceptional");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println("            <input id=\"ChkQBPSupport\" type=\"checkbox\" name=\"runM\" value=\"true\""
            + (!queryType.equals(CertifyRunner.QUERY_TYPE_NONE) ? " checked=\"true\"" : "") + "/> QBP Support");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input id=\"ChkTransformConfirmation\" type=\"checkbox\" name=\"runN\" value=\"true\" checked=\"true\"/> Transform Confirmation");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input id=\"ChkExtraTests\" type=\"checkbox\" name=\"runO\" value=\"true\" checked=\"true\"/> Extra Tests");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println("            <input id=\"ChkDeduplicationEngaged\" type=\"checkbox\" name=\"runP\" value=\"true\""
            + (!queryType.equals(CertifyRunner.QUERY_TYPE_NONE) ? " checked=\"true\"" : "")
            + "/> Deduplication Engaged");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println("            <input id=\"ChkForecasterEngaged\" type=\"checkbox\" name=\"runQ\" value=\"true\""
            + (!queryType.equals(CertifyRunner.QUERY_TYPE_NONE) ? " checked=\"true\"" : "") + "/> Forecaster Engaged");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td valign=\"top\">");
        out.println(
            "            <input id=\"ChkForecasting\" type=\"checkbox\" name=\"runF\" value=\"true\"/> Forecasting");
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
            "            <input id=\"ChkPerformance\" type=\"checkbox\" name=\"runG\" value=\"true\" checked=\"true\" disabled=\"disabled\"/> Performance");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input id=\"ChkConformance\" type=\"checkbox\" name=\"runH\" value=\"true\"/> Conformance (Deprecated)");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        boolean runConformance = true;
        if (sendData != null && sendData.getConnector() != null) {
          runConformance = !sendData.getConnector().isVerify();
        }
        out.println("            <input id=\"ChkNISTConformance2015\" type=\"checkbox\" name=\"runL\" value=\"true\""
            + (runConformance ? " checked=\"true\"" : "") + "/> NIST Conformance 2015");
        out.println(
            "            <input type=\"checkbox\" name=\"reportErrorsOnly\" value=\"true\" checked=\"true\"/> Report Errors Only");
        out.println(
            "            <input type=\"checkbox\" name=\"condenseErrors\" value=\"true\" checked=\"true\"/> Condense Errors");
        out.println("          </td>");
        out.println("          <td></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>");
        out.println(
            "            <input type=\"submit\" name=\"uncheck\" onclick=\"return toggleCheckboxes(false);\" value=\"Uncheck All\"/>");
        out.println(
            "            <input type=\"submit\" name=\"check\" onclick=\"return toggleCheckboxes(true);\" value=\"Check All\"/>");
        out.println("            <script language=\"javascript\">");
        out.println("              function toggleCheckboxes(toggle) {");
        out.println("                document.getElementById(\"ChkBasic\").checked = toggle;");
        out.println("                document.getElementById(\"ChkONC2015\").checked = toggle;");
        out.println("                document.getElementById(\"ChkNotAccepted\").checked = toggle;");
        out.println("                document.getElementById(\"ChkIntermediate\").checked = toggle;");
        out.println("                document.getElementById(\"ChkAdvanced\").checked = toggle;");
        out.println("                document.getElementById(\"ChkProfiling\").checked = toggle;");
        out.println("                document.getElementById(\"ChkExceptional\").checked = toggle;");
        out.println("                document.getElementById(\"ChkQBPSupport\").checked = toggle;");
        out.println("                document.getElementById(\"ChkTransformConfirmation\").checked = toggle;");
        out.println("                document.getElementById(\"ChkExtraTests\").checked = toggle;");
        out.println("                document.getElementById(\"ChkDeduplicationEngaged\").checked = toggle;");
        out.println("                document.getElementById(\"ChkForecasterEngaged\").checked = toggle;");
        out.println("                document.getElementById(\"ChkForecasting\").checked = toggle;");
        out.println("                document.getElementById(\"ChkConformance\").checked = toggle;");
        out.println("                document.getElementById(\"ChkNISTConformance2015\").checked = toggle;");
        out.println("                return false;");
        out.println("              }");
        out.println("              function checkQueryOptions(toggle) {");
        out.println("                document.getElementById(\"ChkQBPSupport\").checked = toggle;");
        out.println("                document.getElementById(\"ChkDeduplicationEngaged\").checked = toggle;");
        out.println("                document.getElementById(\"ChkForecasterEngaged\").checked = toggle;");
        out.println("                return false;");
        out.println("              }");
        out.println("            </script>");
        out.println("          </td>");
        out.println("          <td align=\"right\">");
        if (autoAction == null) {
          out.println("            <input type=\"submit\" name=\"action\" value=\"Start\"/>");
          out.println("            <input type=\"submit\" name=\"action\" value=\"Refresh\"/>");
        } else if (autoAction.equals("Start")) {
          out.println("Will Auto Start in 30 Seconds");
          out.println("            <input type=\"submit\" name=\"action\" value=\"Cancel\"/>");
          out.println("            <input type=\"hidden\" name=\"action\" value=\"Start\"/>");
        }
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
    } catch (Throwable t) {
      t.printStackTrace(System.out);
    } finally {
      out.close();
    }
  }

  public void printRefreshScript(PrintWriter out) {
    out.println("<script>");
    out.println("  var refreshCount = 0;");
    out.println("  function checkRefresh()");
    out.println("  {");
    out.println("    refreshCount++;");
    out.println("    if (refreshCount > 30)");
    out.println("    {");
    out.println("      document.getElementById('certifyForm').submit();");
    out.println("    }");
    out.println("    else");
    out.println("    {");
    out.println("      setTimeout('checkRefresh()', 1000);");
    out.println("    }");
    out.println("  }");
    out.println("  checkRefresh(); ");
    out.println("</script>");
  }

}
