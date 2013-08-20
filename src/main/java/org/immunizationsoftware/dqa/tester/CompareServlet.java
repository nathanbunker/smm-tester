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

import org.immunizationsoftware.dqa.tester.CertifyServlet.CertifyRunner;
import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.manager.CompareManager.Comparison;

/**
 * 
 * @author nathan
 */
public class CompareServlet extends ClientServlet
{

  public static final String VXU_MESSAGE = "vxuMessage";
  public static final String RSP_MESSAGE = "rspMessage";

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

      String vxuMessage = request.getParameter(VXU_MESSAGE);
      String rspMessage = request.getParameter(RSP_MESSAGE);
      
      String certifyServletBasicNum = request.getParameter("certifyServletBasicNum");
      if (certifyServletBasicNum != null)
      {
        CertifyRunner certifyRunner = (CertifyRunner) session.getAttribute("certifyRunner");
        TestCaseMessage tcm = certifyRunner.getStatusCheckTestCaseList().get(Integer.parseInt(certifyServletBasicNum));
        vxuMessage = tcm.getDerivedFromVXUMessage();
        rspMessage = tcm.getActualResponseMessage();
      }

      if (vxuMessage == null) {
        vxuMessage = (String) session.getAttribute(VXU_MESSAGE);
      } else {
        session.setAttribute(VXU_MESSAGE, vxuMessage);
      }
      if (rspMessage == null) {
        rspMessage = (String) session.getAttribute(RSP_MESSAGE);
      } else {
        session.setAttribute(RSP_MESSAGE, rspMessage);
      }

      List<CompareManager.Comparison> comparisonList = null;
      if (vxuMessage != null && rspMessage != null && !vxuMessage.equals("") && !rspMessage.equals("")) {
        comparisonList = CompareManager.compareMessages(vxuMessage, rspMessage);
      }

      PrintWriter out = new PrintWriter(response.getWriter());
      response.setContentType("text/html;charset=UTF-8");
      printHtmlHead(out, "Compare Message", request);
      out.println("    <form action=\"CompareServlet\" method=\"POST\">");
      out.println("      <table border=\"0\">");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">Original VXU</td>");
      out.println("          <td><textarea name=\"" + VXU_MESSAGE + "\" cols=\"70\" rows=\"10\" wrap=\"off\">"
          + vxuMessage + "</textarea></td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">RSP Returned</td>");
      out.println("          <td><textarea name=\"" + RSP_MESSAGE + "\" cols=\"70\" rows=\"10\" wrap=\"off\">"
          + rspMessage + "</textarea></td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td colspan=\"2\" align=\"right\">");
      out.println("            <input type=\"submit\" name=\"method\" value=\"Submit\"/>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("      </table>");
      out.println("    </form>");
      if (comparisonList != null) {
        out.println("    <h2>Comparison Results</h2>");
        out.println("      <table border=\"1\" cellspacing=\"0\">");
        String lastSegmentName = "XXX";
        for (CompareManager.Comparison comparison : comparisonList) {
          if (!comparison.getHl7FieldName().startsWith(lastSegmentName))
          {
            lastSegmentName = comparison.getHl7FieldName().substring(0, 3);
            out.println("        <tr>");
            out.println("          <th>" + lastSegmentName + "</th>");
            out.println("          <th>Field</th>");
            out.println("          <th>Core Data</th>");
            out.println("          <th>Original Value</th>");
            out.println("          <th>Returned Value</th>");
            out.println("          <th>Status</th>");
            out.println("        </tr>");
          }
          String passClass = " class=\"nottested\"";
          if (comparison.isTested()) {
            passClass = comparison.isPass() ? " class=\"pass\"" : " class=\"fail\"";
          }
          out.println("        <tr>");
          out.println("          <td" + passClass + ">" + n(comparison.getHl7FieldName()) + "</td>");
          out.println("          <td" + passClass + ">" + n(comparison.getFieldLabel()) + "</td>");
          out.println("          <td" + passClass + ">" + n(comparison.getPriorityLevelLabel() ) + "</td>");
          out.println("          <td" + passClass + ">" + n(comparison.getOriginalValue()) + "</td>");
          out.println("          <td" + passClass + ">" + n(comparison.getReturnedValue()) + "</td>");
          if (comparison.isTested()) {
            if (comparison.isPass()) {
              out.println("          <td" + passClass + ">Pass</td>");
            } else {
              out.println("          <td" + passClass + ">Fail</td>");
            }
          } else {
            out.println("          <td" + passClass + ">Not Tested</td>");
          }
          out.println("        </tr>");
        }
        out.println("      </table>");
      }

      out.println("<p><a href=\"TestCaseMessageViewerServlet?certifyServletBasicNum=" + certifyServletBasicNum + "\">View Test Case</a></p>");
      out.println("  <div class=\"help\">");
      out.println("  <h2>How To Use This Page</h2>");
      out.println("  </div>");
      printHtmlFoot(out);
      out.close();
    }
  }
  
  private static String n(String s)
  {
    if (s == null || s.equals(""))
    {
      return "&nbsp;";
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
