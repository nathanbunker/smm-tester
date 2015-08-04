/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponseManager;

/**
 * 
 * @author nathan
 */
public class DashboardServlet extends ClientServlet
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
    PrintWriter out = response.getWriter();

    try {
      printHtmlHead(out, MENU_HEADER_TEST, request);
      int maxCols = 12;
      int maxRows = 8;
      ParticipantResponse[][] participantResponseMap = new ParticipantResponse[maxCols][maxRows];
      ParticipantResponseManager.readFile(participantResponseMap);

      String baseLink = "dash?a=v";
      List<ParticipantResponse> participantResponseList = new ArrayList<ParticipantResponse>();
      {
        int i = 1;
        while (request.getParameter("c" + i) != null) {
          int col = Integer.parseInt(request.getParameter("c" + i));
          int row = Integer.parseInt(request.getParameter("r" + i));
          String accessPasscode = request.getParameter("a" + i);
          ParticipantResponse participantResponse = participantResponseMap[col][row];
          if (participantResponse.getAccessPasscode().equals(accessPasscode)) {
            participantResponseList.add(participantResponse);
            baseLink += "&c" + i + "=" + col + "&r" + i + "=" + row + "&a" + i + "=" + accessPasscode;
          }
          i++;
        }
        Collections.sort(participantResponseList, new Comparator<ParticipantResponse>() {
          public int compare(ParticipantResponse pr1, ParticipantResponse pr2) {
            return pr1.getOrganizationName().compareTo(pr2.getOrganizationName());
          }
        });
      }

      String accessPasscode = request.getParameter("accessPasscode");

      if (accessPasscode != null) {
        int col = Integer.parseInt(request.getParameter("col"));
        int row = Integer.parseInt(request.getParameter("row"));
        ParticipantResponse participantResponse = participantResponseMap[col][row];
        printViewMenu(out, participantResponseList, participantResponse, baseLink);

        if (participantResponse.getAccessPasscode().equals(accessPasscode)) {
          printDashboard(out, participantResponse);
        } else {
          out.println("<p>This dashboard is not available. Please request a new link to display this dashboard. </p>");
        }
      } else {
        printViewMenu(out, participantResponseList, null, baseLink);
      }

      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  private static void printViewMenu(PrintWriter out, List<ParticipantResponse> participantResponseList,
      ParticipantResponse participantResponseSelected, String baseLink) throws UnsupportedEncodingException {
    if (participantResponseList.size() > 0) {
      out.println("    <table class=\"viewMenu\"><tr><td>");
      boolean first = true;
      for (ParticipantResponse participantResponse : participantResponseList) {
        if (!first) {
          out.println(" ");
        }
        first = false;
        String styleClass = "menuLink";
        if (participantResponse == participantResponseSelected) {
          styleClass = "menuLinkSelected";
        }
        String link = baseLink + "&col=" + participantResponse.getCol() + "&row=" + participantResponse.getRow()
            + "&accessPasscode=" + participantResponse.getAccessPasscode();
        out.print("<a class=\"" + styleClass + "\" href=\"" + link + "\">");
        out.print(participantResponse.getOrganizationName());
        out.println("</a>");
      }
      out.println("</table>");
    }
  }

  public static void printDashboard(PrintWriter out, ParticipantResponse participantResponse) {
    out.println("<h2>" + participantResponse.getOrganizationName() + "</h2>");
    out.println("<table class=\"dashTable\">");
    out.println("  <tr>");
    out.println("    <th class=\"dashTable\">General Information</th>");
    out.println("    <th class=\"dashTable\">Phase 1</th>");
    out.println("    <th class=\"dashTable\">Phase 2</th>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <td class=\"dashTable\">");
    out.println("      <table> ");
    out.println("        <tr><td>Platform: </td><td>" + participantResponse.getPlatform() + "</td></tr>");
    out.println("        <tr><td>Vendor: </td><td>" + participantResponse.getVendor() + "</td></tr>");
    out.println("        <tr><td>Transport: </td><td>" + participantResponse.getTransport() + "</td></tr>");
    out.println("        <tr><td>Query Support: </td><td>" + participantResponse.getTransport() + "</td></tr>");
    if (participantResponse.getIHS().equalsIgnoreCase("Yes")) {
      out.println("        <tr><td colspan=\"2\">Serves IHS Clinic</td></tr>");
    }
    out.println("      </table>");
    out.println("    </td>");

    out.println("    <td class=\"dashTable\">");
    out.println("      <table> ");
    out.println("        <tr><td>Participation:  </td><td>" + participantResponse.getPhaseIParticipation()
        + "</td></tr>");
    out.println("        <tr><td>Status:  </td><td>" + participantResponse.getPhase1Status() + "</td></tr>");
    out.println("      </table>");
    out.println("    </td>");

    out.println("    <td class=\"dashTable\">");
    out.println("      <table> ");
    out.println("        <tr><td>Participation:  </td><td>" + participantResponse.getPhaseIIParticipation()
        + "</td></tr>");
    out.println("        <tr><td>Status:  </td><td>" + participantResponse.getPhaseIIStatus() + "</td></tr>");
    out.println("        <tr><td>IIS Guide:  </td><td>" + participantResponse.getRecordRequirementsStatus()
        + "</td></tr>");
    out.println("        <tr><td>IIS Connection:  </td><td>" + participantResponse.getConnecttoIISStatus()
        + "</td></tr>");
    out.println("      </table>");
    String folderName = participantResponse.getFolderName();
    if (!folderName.equals("")) {
      SendData sendData = ManagerServlet.getSendDatayByLabel(folderName);
      if (sendData != null && sendData.getConnector() != null) {
        List<File> fileList = CreateTestCaseServlet.listIISTestReports(sendData);
        if (fileList.size() > 0) {
          out.println("      <br/>");
          out.println("      <br/>Reports Available");
          for (File file : fileList) {
            String link = "CertifyHistoryServlet/" + sendData.getConnector().getLabel() + "/" + file.getName()
                + "/IIS Testing Report.html";
            String display = file.getName();
            if (display.startsWith("IIS Test Report ")) {
              display = display.substring("IIS Test Report ".length()).trim();
              if (display.startsWith("-")) {
                display = display.substring(1).trim();
              } else if (display.length() == 16) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                try {
                  Date reportDate = sdf.parse(display);
                  sdf = new SimpleDateFormat("MM/dd/yyyy");
                  display = sdf.format(reportDate);
                } catch (Exception e) {
                  // not good format, don't convert
                }
              }
            }
            if (!display.equals("Manual Phase 1")) {
              out.println("        <br/>&nbsp;- <a href=\"" + link + "\" target=\"_blank\">" + display + "</a>");
            }
          }
        }
      }
    }
    out.println("    </td>");
    out.println("  </tr>");
    out.println("</table>");

  }
}
