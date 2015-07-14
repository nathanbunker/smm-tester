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
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.CvsReader;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponseManager;

/**
 * 
 * @author nathan
 */
public class CertifyHistoryServlet extends ClientServlet {

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
  private static final String VIEW_PHASE_1 = "Phase 1";
  private static final String VIEW_PHASE_2 = "Phase 2";
  private static final String VIEW_PHASE_2_IIS_GUIDE = "IIS Guide";
  private static final String VIEW_PHASE_2_TESTING = "Automated Testing";
  private static final String VIEW_DETAIL = "Detail";

  public static final String[] VIEW = { VIEW_ALL_REPORTS, VIEW_PHASE_1, VIEW_PHASE_2, VIEW_PHASE_2_IIS_GUIDE, VIEW_PHASE_2_TESTING };

  // types of view
  // + Phase 1
  // + Phase 2
  // + Phase 2 - IIS Guide
  // + Phase 2 - Testing

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session, String problem) throws IOException,
      ServletException {
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
          out.println("<br/>Vendor: " + participantResponse.getVendor() + "</p>");
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
        } else {
          out.println("<h2>" + view + "</h2>");
          out.println("<table>");
          for (int row = 0; row < maxRows; row++) {
            out.println("<tr>");
            for (int col = 0; col < maxCols; col++) {
              if (participantResponseMap[col][row] == null) {
                out.println("<td>");
                out.println("</td>");
              } else {
                String organizationName = participantResponseMap[col][row].getOrganizationName();
                String status = "pass";
                String comment = "";
                if (view.equals(VIEW_PHASE_1)) {
                  String phaseIParticipation = participantResponseMap[col][row].getPhaseIParticipation();
                  comment = participantResponseMap[col][row].getPhase1Comments();
                  if (phaseIParticipation.equals("")) {
                    status = "";
                  } else if (phaseIParticipation.equals("Yes - Direct") || phaseIParticipation.equals("Yes - Report Only")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                } else if (view.equals(VIEW_PHASE_2)) {
                  String phaseIIParticipation = participantResponseMap[col][row].getPhaseIIParticipation();
                  comment = participantResponseMap[col][row].getPhaseIIComments();
                  if (phaseIIParticipation.equals("")) {
                    status = "";
                  } else if (phaseIIParticipation.equals("Yes - AIRA & NIST") || phaseIIParticipation.equals("Yes - AIRA Only")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                } else if (view.equals(VIEW_PHASE_2_IIS_GUIDE)) {
                  String recordRequirementsStatus = participantResponseMap[col][row].getRecordRequirementsStatus();
                  if (recordRequirementsStatus.equals("")) {
                    status = "";
                  } else if (recordRequirementsStatus.equals("IIS Guide Recorded") || recordRequirementsStatus.equals("Use National Guide")
                      || recordRequirementsStatus.equals("See Envision Guide")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                } else if (view.equals(VIEW_PHASE_2_TESTING)) {
                  String connectToIISStatus = participantResponseMap[col][row].getConnecttoIISStatus();
                  comment = participantResponseMap[col][row].getComments();
                  if (connectToIISStatus.equals("")) {
                    status = "";
                  } else if (connectToIISStatus.equals("Connected")) {
                    status = "pass";
                  } else {
                    status = "fail";
                  }
                }

                String link = "<a href=\"CertifyHistoryServlet?view=" + VIEW_DETAIL + "&row=" + row + "&col=" + col + "\" title=\"" + comment + "\">";
                if (organizationName.length() <= 2) {
                  out.println("<td class=\"" + status + "\" width=\"90\" style=\"border-style:solid; border-width: 1px; vertical-align: top; \">");
                  out.println("<center><b><font size=\"+2\">" + link + participantResponseMap[col][row].getOrganizationName()
                      + "</a></font></b></center>");
                } else {
                  out.println("<td class=\"" + status + "\" width=\"90\" style=\"border-style:solid; border-width: 1px; vertical-align: top;\">");
                  out.println("<center><b>" + link + participantResponseMap[col][row].getOrganizationName() + "</a></b></center>");
                }

                if (view.equals(VIEW_PHASE_1)) {
                  String phaseIParticipation = participantResponseMap[col][row].getPhaseIParticipation();
                  if (!phaseIParticipation.equals("")) {
                    out.println("<br/><center>" + link + phaseIParticipation + "</a></center>");
                  }
                } else if (view.equals(VIEW_PHASE_2)) {
                  String phaseIIParticipation = participantResponseMap[col][row].getPhaseIIParticipation();
                  if (!phaseIIParticipation.equals("")) {
                    out.println("<br/><center>" + link + phaseIIParticipation + "</a></center>");
                  }
                } else if (view.equals(VIEW_PHASE_2_IIS_GUIDE)) {
                  String recordRequirementsStatus = participantResponseMap[col][row].getRecordRequirementsStatus();
                  if (!recordRequirementsStatus.equals("")) {
                    out.println("<br/><center>" + link + recordRequirementsStatus + "</center>");
                  }
                } else if (view.equals(VIEW_PHASE_2_TESTING)) {
                  String connectToIISStatus = participantResponseMap[col][row].getConnecttoIISStatus();
                  if (!connectToIISStatus.equals("")) {
                    out.println("<br/><center>" + link + connectToIISStatus + "</a></center>");
                  }
                  String folderName = participantResponseMap[col][row].getFolderName();
                  if (!folderName.equals("")) {
                    SendData sendData = ManagerServlet.getSendDatayByLabel(folderName);
                    if (sendData != null && sendData.getConnector() != null) {
                      {
                        File testCaseDir = sendData.getTestCaseDir();
                        String[] testNames = testCaseDir.list(new FilenameFilter() {
                          public boolean accept(File dir, String name) {
                            File file = new File(dir, name);
                            return file.isDirectory() && name.startsWith("IIS Test Report ") && name.length() == 32;
                          }
                        });
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
                          link = "CertifyHistoryServlet/" + folderName + "/" + latestTestName + "/IIS Testing Report.html";
                          out.println("<br/><center><a href=\"" + link + "\" target=\"_blank\">" + latestTestNameDate + "</a></center>");
                        }
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
        }

        // filter by
        // + platform
        // + vendor
        // + IHS

      } else {
        for (SendData sendData : ManagerServlet.getSendDataList()) {
          if (sendData.getConnector() != null) {
            List<File> fileList = CreateTestCaseServlet.listIISTestReports(sendData);
            if (fileList.size() > 0) {
              // sendData.getInternalId();
              out.println("<h3>" + sendData.getConnector().getLabel() + "</h3>");
              out.println("<ul>");

              for (File file : fileList) {
                String link = "CertifyHistoryServlet/" + sendData.getConnector().getLabel() + "/" + file.getName() + "/IIS Testing Report.html";
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

  public static void printViewMenu(PrintWriter out, String view, boolean isRunTestNow) throws UnsupportedEncodingException {
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
      out.print("<a class=\"" + styleClass + "\" href=\"CertifyHistoryServlet?view=" + URLEncoder.encode(VIEW[i], "UTF-8") + "\">");
      out.print(VIEW[i]);
      out.println("</a>");
      if (ManagerServlet.getIisParticipantResponsesAndAccountInfoFile() == null) {
        break;
      }

    }
    out.println("</table>");
  }

}
