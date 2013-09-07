/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.manager.hl7.ConformanceIssue;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7ComponentManager;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ERL;

/**
 * 
 * @author nathan
 */
public class ConformanceServlet extends ClientServlet
{

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
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {

      String text = request.getParameter("text");
      if (text == null) {
        text = "";
      }
      int pos = 0;
      if (request.getParameter("objectType") != null) {
        pos = Integer.parseInt(request.getParameter("objectType"));
      }

      PrintWriter out = new PrintWriter(response.getWriter());
      response.setContentType("text/html;charset=UTF-8");
      printHtmlHead(out, MENU_HEADER_HOME, request);
      out.println("    <form action=\"ConformanceServlet\" method=\"POST\">");
      out.println("      <table border=\"0\">");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">HL7 Object Type</td>");
      out.println("          <td><select name=\"objectType\">");
      List<HL7Component> componentList = HL7ComponentManager.getComponentList();
      for (int i = 0; i < componentList.size(); i++) {
        HL7Component component = componentList.get(i);
        out.println("              <option value=\"" + i + "\"" + (i == pos ? "selected=\"true\"" : "") + ">"
            + component.getComponentReference() + "</option>");
      }
      out.println("              ");
      out.println("            </select>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">HL7 Text</td>");
      out.println("          <td><textarea name=\"text\" cols=\"70\" rows=\"10\" wrap=\"off\">" + text
          + "</textarea></td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td colspan=\"2\" align=\"right\">");
      out.println("            <input type=\"submit\" name=\"method\" value=\"Submit\"/>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("      </table>");
      out.println("    </form>");
      if (!text.equals("")) {
        out.println("    <h2>Conformance Test</h2>");
        HL7Component component = componentList.get(pos);
        if (component.getItemType() == ItemType.MESSAGE) {
          component.parseTextFromMessage(text);
        } else if (component.getItemType() == ItemType.SEGMENT) {
          component.parseTextFromSegment(text);
        } else if (component.getItemType() == ItemType.DATATYPE) {
          component.parseTextFromField(text);
        }
        List<ConformanceIssue> conformanceIssueList = component.checkConformance();

        printConformanceIssues(out, conformanceIssueList);
        out.println("      <p>Cleaned up Message</p>");
        out.print("    <pre>");
        if (component.getItemType() == ItemType.MESSAGE) {
          out.print(component.createMessage());
        } else if (component.getItemType() == ItemType.SEGMENT) {
          out.print(component.createSegment());
        } else if (component.getItemType() == ItemType.DATATYPE) {
          out.print(component.createField());
        }

        out.println("</pre>");

      }

      out.println("  <div class=\"help\">");
      out.println("  <h2>How To Use This Page</h2>");
      out.println("  </div>");
      printHtmlFoot(out);
      out.close();
    }
  }

  public static void printConformanceIssues(PrintWriter out, List<ConformanceIssue> conformanceIssueList) {
    out.println("      <table border=\"1\" cellspacing=\"0\">");
    out.println("        <tr>");
    out.println("          <th>Severity</th>");
    out.println("          <th>HL7 Component</th>");
    out.println("          <th>Description</th>");
    out.println("        </tr>");

    for (ConformanceIssue conformanceIssue : conformanceIssueList) {
      String passClass = conformanceIssue.getSeverity().getValue().equals("E") ? " class=\"fail\""
          : " class=\"pass\"";
      out.println("        <tr>");
      out.println("          <td" + passClass + ">" + n(conformanceIssue.getSeverity().getValue()) + "</td>");
      out.println("          <td" + passClass + ">" + n(conformanceIssue.getErrorLocation()) + "</td>");
      out.println("          <td" + passClass + ">" + n(conformanceIssue.getUserMessage().getValue()) + "</td>");
      out.println("        </tr>");
    }
    out.println("      </table>");
  }

  private static String n(String s) {
    if (s == null || s.equals("")) {
      return "&nbsp;";
    }
    return s;
  }

  private static String n(ERL errorLocation) {
    if (errorLocation == null) {
      return "&nbsp;";
    }
    if (errorLocation.getSegmentID().getValue().equals("")) {
      return "&nbsp;";
    }

    String s = errorLocation.getSegmentID().getValue();
    if (!errorLocation.getSegmentSequence().getValue().equals("")
        && !errorLocation.getSegmentSequence().getValue().equals("1")) {
      s = s + "#" + errorLocation.getSegmentSequence().getValue();
    }
    if (!errorLocation.getFieldPosition().getValue().equals("")) {
      s = s + "-" + errorLocation.getFieldPosition().getValue();
      if (!errorLocation.getFieldRepetition().getValue().equals("")
          && !errorLocation.getFieldRepetition().getValue().equals("1")) {
        s = s + "#" + errorLocation.getFieldRepetition().getValue();
      }
    }
    return s;

  }

  /**
   * Handles the HTTP <code>POST</code> method.
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
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      doGet(request, response);
    } catch (Exception e) {
      request.setAttribute("responseText", e.getMessage());
    }
    doGet(request, response);
  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Compare response with original submission";
  }// </editor-fold>

}
