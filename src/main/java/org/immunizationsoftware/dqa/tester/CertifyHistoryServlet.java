/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponseManager;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsage;

/**
 * 
 * @author nathan
 */
public class CertifyHistoryServlet extends ClientServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static String currentYear;

  static {
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    currentYear = "" + year;
  }

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
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");
    String pathInfo = request.getPathInfo();

    if (pathInfo != null && pathInfo.length() > 1) {
      String[] paths = pathInfo.split("\\/");
      if (paths.length == 4 && !pathInfo.contains("..")) {
        String iis = paths[1];
        String report = paths[2];
        String fileName = paths[3];

        SendData sendData = ManagerServlet.getSendDatayByLabel(iis);
        if (sendData != null && sendData.getConnector() != null) {
          if (sendData.getConnector().getLabel().equals(iis)) {
            File dir = new File(sendData.getTestCaseDir(), report);
            if (dir.exists()) {
              File file = new File(dir, fileName);
              if (file.exists()) {
                BufferedReader in = new BufferedReader(new FileReader(file));
                PrintWriter out = response.getWriter();
                String line;
                while ((line = in.readLine()) != null) {
                  if (fileName.endsWith(".txt")) {
                    response.setContentType("text/plain");
                  } else {
                    response.setContentType("text/html");
                  }
                  out.println(line);
                }
                out.close();
                in.close();
                return;
              }
            }
          }
        }
      }
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    if (username == null || user == null || !user.isAdmin()) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {
      doGet(request, response, session, null);
    }
  }

  private static final String VIEW_ALL_REPORTS = "All Test Reports";
  private static final String VIEW_PHASE_1_PARTICIPATION = "Phase 1 Participation";
  private static final String VIEW_PHASE_1_STATUS = "Phase 1 Status";
  private static final String VIEW_PHASE_2_PARTICIPATION = "Phase 2 Participation";
  private static final String VIEW_PHASE_2_STATUS = "Phase 2 Status";
  private static final String VIEW_PHASE_2_IIS_GUIDE = "IIS Guide";
  private static final String VIEW_PHASE_2_TESTING = "Automated Testing";
  private static final String VIEW_DETAIL = "Detail";

  public static final String[] VIEW = { VIEW_ALL_REPORTS, VIEW_PHASE_1_PARTICIPATION, VIEW_PHASE_1_STATUS,
      VIEW_PHASE_2_PARTICIPATION, VIEW_PHASE_2_STATUS, VIEW_PHASE_2_IIS_GUIDE, VIEW_PHASE_2_TESTING };

  private static final String ALL = "All";

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session, String problem)
      throws IOException, ServletException {

    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
      return;
    }
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");

    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_TEST, request);

      if (problem != null) {
        out.println("<p>" + problem + "</p>");
      }
      String view = request.getParameter("view");
      if (view == null) {
        view = VIEW_ALL_REPORTS;
      }

      printViewMenu(out, view, false);

      if (!view.equals(VIEW_ALL_REPORTS) && ManagerServlet.getIisParticipantResponsesAndAccountInfoFile() != null) {
        int maxCols = 12;
        int maxRows = 8;
        ParticipantResponse[][] participantResponseMap = new ParticipantResponse[maxCols][maxRows];
        ParticipantResponseManager.readFile(participantResponseMap);
        if (view.equals(VIEW_DETAIL)) {
          int col = Integer.parseInt(request.getParameter("col"));
          int row = Integer.parseInt(request.getParameter("row"));
          ParticipantResponse participantResponse = participantResponseMap[col][row];
          out.println("<h2>" + participantResponse.getOrganizationName() + "</h2>");
          out.println("<p>Platform: " + participantResponse.getPlatform());
          out.println("<br/>Vendor: " + participantResponse.getVendor());
          out.println("<br/>Transport: " + participantResponse.getTransport());
          out.println("<br/>Query Support: " + participantResponse.getTransport() + "</p>");
          out.println("<p>" + participantResponse.getInternalComments() + "</p>");
          out.println("<h3>Phase 1</h3>");
          out.println("<p>Participation: " + participantResponse.getPhaseIParticipation());
          out.println("<br/>Status: " + participantResponse.getPhase1Status() + "</p>");
          out.println("<p>" + participantResponse.getPhase1Comments() + "</p>");
          out.println("<h3>Phase 2</h3>");
          out.println("<p>Participation: " + participantResponse.getPhaseIIParticipation());
          out.println("<br/>Status: " + participantResponse.getPhaseIIStatus() + "</p>");
          out.println("<p>" + participantResponse.getPhaseIIComments() + "</p>");
          out.println("<p>Record Requirements Status: " + participantResponse.getRecordRequirementsStatus() + "</p>");
          out.println("<p>Connect to IIS Status: " + participantResponse.getConnecttoIISStatus() + "</p>");
          out.println("<p>" + participantResponse.getComments() + "</p>");
          if (participantResponse.getIHS().equalsIgnoreCase("Yes")) {
            out.println("<p>IIS serves IHS population</p>");
          }
          switchParticipantResponse(session, user, participantResponse);
          // List<ParticipantResponse> participantResponseScheduledList =
          // (List<ParticipantResponse>) session
          // .getAttribute("participantResponseScheduledList");
          // if (participantResponseScheduledList != null) {
          // boolean participantResponseScheduled = false;
          // for (ParticipantResponse pr : participantResponseScheduledList) {
          // if
          // (pr.getOrganizationName().equals(participantResponse.getOrganizationName()))
          // {
          // participantResponseScheduled = true;
          // break;
          // }
          // }
          // if (participantResponseScheduled) {
          // out.println("<p>Scheduled to Run</p>");
          // }
          // }

        } else {
          String platform = request.getParameter("platform");
          String vendor = request.getParameter("vendor");
          String phase1 = request.getParameter("phase1");
          String phase2 = request.getParameter("phase2");
          String ihs = request.getParameter("ihs");
          String transport = request.getParameter("transport");
          String querySupport = request.getParameter("querySupport");
          String nistStatus = request.getParameter("nistStatus");
          List<String> platformList = new ArrayList<String>();
          List<String> vendorList = new ArrayList<String>();
          List<String> phase1List = new ArrayList<String>();
          List<String> phase2List = new ArrayList<String>();
          List<String> ihsList = new ArrayList<String>();
          List<String> transportList = new ArrayList<String>();
          List<String> querySupportList = new ArrayList<String>();
          List<String> nistStatusList = new ArrayList<String>();
          if (platform == null) {
            platform = ALL;
          }
          if (vendor == null) {
            vendor = ALL;
          }
          if (phase1 == null) {
            phase1 = ALL;
          }
          if (phase2 == null) {
            phase2 = ALL;
          }
          if (ihs == null) {
            ihs = ALL;
          }
          if (transport == null) {
            transport = ALL;
          }
          if (querySupport == null) {
            querySupport = ALL;
          }
          if (nistStatus == null) {
            nistStatus = ALL;
          }
          out.println("<h2>" + view + "</h2>");
          out.println("<table>");
          Map<String, Integer> statusLabelCountMap = new HashMap<String, Integer>();
          for (int row = 0; row < maxRows; row++) {
            out.println("<tr>");
            for (int col = 0; col < maxCols; col++) {
              ParticipantResponse participantResponse = participantResponseMap[col][row];
              boolean greyOut = false;
              if (participantResponse != null) {
                if (!platformList.contains(participantResponse.getPlatform())) {
                  platformList.add(participantResponse.getPlatform());
                }
                if (!vendorList.contains(participantResponse.getVendor())) {
                  vendorList.add(participantResponse.getVendor());
                }
                if (!phase1List.contains(participantResponse.getPhaseIParticipation())) {
                  phase1List.add(participantResponse.getPhaseIParticipation());
                }
                if (!phase2List.contains(participantResponse.getPhaseIIParticipation())) {
                  phase2List.add(participantResponse.getPhaseIIParticipation());
                }
                if (!ihsList.contains(participantResponse.getIHS())) {
                  ihsList.add(participantResponse.getIHS());
                }
                if (!platform.equals(ALL) && !platform.equals(participantResponse.getPlatform())) {
                  greyOut = true;
                } else if (!vendor.equals(ALL) && !vendor.equals(participantResponse.getVendor())) {
                  greyOut = true;
                } else if (!phase1.equals(ALL) && !phase1.equals(participantResponse.getPhaseIParticipation())) {
                  greyOut = true;
                } else if (!phase2.equals(ALL) && !phase2.equals(participantResponse.getPhaseIIParticipation())) {
                  greyOut = true;
                } else if (!ihs.equals(ALL) && !ihs.equals(participantResponse.getIHS())) {
                  greyOut = true;
                } else if (!transport.equals(ALL) && !transport.equals(participantResponse.getTransport())) {
                  greyOut = true;
                } else if (!querySupport.equals(ALL) && !querySupport.equals(participantResponse.getQuerySupport())) {
                  greyOut = true;
                } else if (!nistStatus.equals(ALL) && !nistStatus.equals(participantResponse.getNistStatus())) {
                  greyOut = true;
                }
              }

              if (participantResponse == null) {
                out.println("<td>");
                out.println("</td>");
              } else if (greyOut) {
                out.println("<td width=\"90\" style=\"border-style:solid; border-width: 1px; \">&nbsp;</td>");
              } else {
                String organizationName = participantResponse.getOrganizationName();
                String status = "pass";
                String comment = "";
                String statusLabel = "";
                if (view.equals(VIEW_PHASE_1_PARTICIPATION)) {
                  statusLabel = participantResponse.getPhaseIParticipation();
                  comment = participantResponse.getPhase1Comments();
                  if (statusLabel.equals("")) {
                    status = "";
                  } else if (statusLabel.equalsIgnoreCase("Yes - Direct")
                      || statusLabel.equalsIgnoreCase("Yes - Report Only")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                } else if (view.equals(VIEW_PHASE_1_STATUS)) {
                  statusLabel = participantResponse.getPhase1Status();
                  comment = participantResponse.getPhase1Comments();
                  if (statusLabel.equals("")) {
                    status = "";
                  } else if (statusLabel.equalsIgnoreCase("Complete")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                } else if (view.equals(VIEW_PHASE_2_PARTICIPATION)) {
                  statusLabel = participantResponse.getPhaseIIParticipation();
                  comment = participantResponse.getPhaseIIComments();
                  if (statusLabel.equals("")) {
                    status = "";
                  } else if (statusLabel.equalsIgnoreCase("Yes - AIRA & NIST")
                      || statusLabel.equalsIgnoreCase("Yes - AIRA Only")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                } else if (view.equals(VIEW_PHASE_2_STATUS)) {
                  statusLabel = participantResponse.getPhaseIIStatus();
                  comment = participantResponse.getPhaseIIComments();
                  if (statusLabel.equals("")) {
                    status = "";
                  } else if (statusLabel.equalsIgnoreCase("Complete")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                } else if (view.equals(VIEW_PHASE_2_IIS_GUIDE)) {
                  statusLabel = participantResponse.getRecordRequirementsStatus();
                  if (statusLabel.equals("")) {
                    status = "";
                  } else if (statusLabel.equalsIgnoreCase("IIS Guide Recorded")
                      || statusLabel.equalsIgnoreCase("Use National Guide")
                      || statusLabel.equalsIgnoreCase("See Envision Guide")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                } else if (view.equals(VIEW_PHASE_2_TESTING)) {
                  statusLabel = participantResponse.getConnecttoIISStatus();
                  comment = participantResponse.getComments();
                  if (statusLabel.equals("")) {
                    status = "blank";
                  } else if (statusLabel.equalsIgnoreCase("Connected")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                }

                String link = "<a href=\"CertifyHistoryServlet?view=" + VIEW_DETAIL + "&row=" + row + "&col=" + col
                    + "\" title=\"" + comment + "\">";
                if (organizationName.length() <= 2) {
                  out.println("<td class=\"" + status
                      + "\" width=\"90\" style=\"border-style:solid; border-width: 1px; vertical-align: top; \">");
                  out.println("<center><b><font size=\"+2\">" + link + participantResponse.getOrganizationName()
                      + "</a></font></b></center>");
                } else {
                  out.println("<td class=\"" + status
                      + "\" width=\"90\" style=\"border-style:solid; border-width: 1px; vertical-align: top;\">");
                  out.println("<center><b>" + link + participantResponse.getOrganizationName() + "</a></b></center>");
                }
                if (!statusLabel.equals("")) {
                  out.println("<br/><center>" + link + statusLabel + "</a></center>");
                  Integer count = statusLabelCountMap.get(statusLabel);
                  if (count == null) {
                    count = new Integer(1);
                  } else {
                    count = new Integer(count + 1);
                  }
                  statusLabelCountMap.put(statusLabel, count);
                }

                if (view.equals(VIEW_PHASE_2_TESTING)) {
                  String folderName = participantResponse.getFolderName();
                  if (!folderName.equals("")) {
                    SendData sendData = ManagerServlet.getSendDatayByLabel(folderName);
                    if (sendData != null && sendData.getConnector() != null) {
                      String[] testNames = sendData.getTestReportNames();
                      String latestTestName = "";
                      for (String testName : testNames) {
                        if (testName.compareTo(latestTestName) > 0) {
                          latestTestName = testName;
                        }
                      }
                      if (!latestTestName.equals("")) {
                        String latestTestNameDate = latestTestName.substring(16, 26);
                        if (latestTestNameDate.startsWith(currentYear + "-")) {
                          latestTestNameDate = latestTestNameDate.substring(currentYear.length() + 1);
                        }
                        link = "CertifyHistoryServlet/" + folderName + "/" + latestTestName
                            + "/IIS Testing Report.html";
                        out.println("<br/><center><a href=\"" + link + "\" target=\"_blank\">" + latestTestNameDate
                            + "</a></center>");
                      }
                    }
                  }
                }
                out.println("</td>");
              }
            }
            out.println("</tr>");
          }
          out.println("</table>");
          List<String> statusLabelList = new ArrayList<String>(statusLabelCountMap.keySet());
          if (statusLabelList.size() > 0) {
            Collections.sort(statusLabelList);
            out.println("<h3>Status Counts</h3>");
            out.println("<table class=\"boxed\">");
            out.println("  <tr>");
            out.println("    <th>Status</th>");
            out.println("    <th>Count</th>");
            out.println("  </tr>");
            for (String statusLabel : statusLabelList) {
              out.println("  <tr>");
              out.println("    <td>" + statusLabel + "</td>");
              out.println("    <td>" + statusLabelCountMap.get(statusLabel) + "</td>");
              out.println("  </tr>");
            }
            out.println("</table>");
          }
          Collections.sort(platformList);
          Collections.sort(vendorList);
          platformList.add(0, ALL);
          vendorList.add(0, ALL);
          phase1List.add(0, ALL);
          phase2List.add(0, ALL);
          ihsList.add(0, ALL);
          transportList.add(0, ALL);
          querySupportList.add(0, ALL);
          nistStatusList.add(0, ALL);
          out.println("<h3>Filter By</h3>");
          out.println("<form action=\"CertifyHistoryServlet\" method=\"GET\">");
          out.println("<p>Platform <select name=\"platform\">");
          for (String platformSelect : platformList) {
            if (platform.equals(platformSelect)) {
              out.println("    <option value=\"" + platformSelect + "\" selected=\"true\">" + platformSelect
                  + "</option>");
            } else {
              out.println("    <option value=\"" + platformSelect + "\">" + platformSelect + "</option>");
            }
          }
          out.println("  </select>");
          out.println("Vendor <select name=\"vendor\">");
          for (String vendorSelect : vendorList) {
            if (vendor.equals(vendorSelect)) {
              out.println("    <option value=\"" + vendorSelect + "\" selected=\"true\">" + vendorSelect + "</option>");
            } else {
              out.println("    <option value=\"" + vendorSelect + "\">" + vendorSelect + "</option>");
            }
          }
          out.println("  </select>");
          out.println("<br/>");
          out.println("Transport <select name=\"transport\">");
          for (String transportSelect : transportList) {
            if (transport.equals(transportSelect)) {
              out.println("    <option value=\"" + transportSelect + "\" selected=\"true\">" + transportSelect
                  + "</option>");
            } else {
              out.println("    <option value=\"" + transportSelect + "\">" + transportSelect + "</option>");
            }
          }
          out.println("  </select>");
          out.println("Query Support <select name=\"querySupport\">");
          for (String querySupportSelect : querySupportList) {
            if (querySupport.equals(querySupportSelect)) {
              out.println("    <option value=\"" + querySupportSelect + "\" selected=\"true\">" + querySupportSelect
                  + "</option>");
            } else {
              out.println("    <option value=\"" + querySupportSelect + "\">" + querySupportSelect + "</option>");
            }
          }
          out.println("  </select>");
          out.println("<br/>");
          out.println("Phase 1 <select name=\"phase1\">");
          for (String phase1Select : phase1List) {
            if (phase1.equals(phase1Select)) {
              out.println("    <option value=\"" + phase1Select + "\" selected=\"true\">" + phase1Select + "</option>");
            } else {
              out.println("    <option value=\"" + phase1Select + "\">" + phase1Select + "</option>");
            }
          }
          out.println("  </select>");
          out.println("Phase 2 <select name=\"phase2\">");
          for (String phase2Select : phase2List) {
            if (phase2.equals(phase2Select)) {
              out.println("    <option value=\"" + phase2Select + "\" selected=\"true\">" + phase2Select + "</option>");
            } else {
              out.println("    <option value=\"" + phase2Select + "\">" + phase2Select + "</option>");
            }
          }
          out.println("  </select>");
          out.println("<br/>");
          out.println("NIST Status <select name=\"nistStatus\">");
          for (String nistStatusSelect : nistStatusList) {
            if (nistStatus.equals(nistStatusSelect)) {
              out.println("    <option value=\"" + nistStatus + "\" selected=\"true\">" + nistStatus + "</option>");
            } else {
              out.println("    <option value=\"" + nistStatus + "\">" + nistStatus + "</option>");
            }
          }
          out.println("  </select>");
          out.println("IHS <select name=\"ihs\">");
          for (String ihsSelect : ihsList) {
            if (ihs.equals(ihsSelect)) {
              out.println("    <option value=\"" + ihsSelect + "\" selected=\"true\">" + ihsSelect + "</option>");
            } else {
              out.println("    <option value=\"" + ihsSelect + "\">" + ihsSelect + "</option>");
            }
          }
          out.println("  </select>");
          out.println("<br/>");
          out.println("<input type=\"submit\" name=\"submit\" value=\"Refresh\"/></p>");
          out.println("<input type=\"hidden\" name=\"view\" value=\"" + view + "\"/>");
          out.println("</form>");
        }
      } else {
        for (SendData sendData : ManagerServlet.getSendDataList()) {
          if (sendData.getConnector() != null) {
            List<File> fileList = CreateTestCaseServlet.listIISTestReports(sendData);
            if (fileList.size() > 0) {
              out.println("<h3>" + sendData.getConnector().getLabel() + "</h3>");
              out.println("<ul>");
              for (File file : fileList) {
                String link = "CertifyHistoryServlet/" + sendData.getConnector().getLabel() + "/" + file.getName()
                    + "/IIS Testing Report.html";
                out.println("  <li><a href=\"" + link + "\" target=\"_blank\">" + file.getName() + "</a></li>");
              }
              out.println("</ul>");
            }
          }
        }
      }
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  public static void printViewMenu(PrintWriter out, String view, boolean isRunTestNow)
      throws UnsupportedEncodingException {
    out.println("    <table class=\"viewMenu\"><tr><td>");
    if (isRunTestNow) {
      out.print("<a class=\"menuLinkSelected\" href=\"CertifyServlet\">Run Test Now</a>");
    } else {
      out.print("<a class=\"menuLink\" href=\"CertifyServlet\">Run Test Now</a>");
    }
    for (int i = 0; i < VIEW.length; i++) {
      if (i > 0) {
        out.println(" ");
      }
      String styleClass = "menuLink";
      if (VIEW[i].equals(view)) {
        styleClass = "menuLinkSelected";
      }
      out.print("<a class=\"" + styleClass + "\" href=\"CertifyHistoryServlet?view="
          + URLEncoder.encode(VIEW[i], "UTF-8") + "\">");
      out.print(VIEW[i]);
      out.println("</a>");
      if (ManagerServlet.getIisParticipantResponsesAndAccountInfoFile() == null) {
        break;
      }

    }
    out.println("</table>");
  }

}
