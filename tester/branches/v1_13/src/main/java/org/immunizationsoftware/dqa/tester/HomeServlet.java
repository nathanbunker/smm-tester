/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.manager.ScenarioManager;

/**
 * 
 * @author nathan
 */
public class HomeServlet extends ClientServlet
{

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
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");

    PrintWriter out = response.getWriter();
    try
    {
      printHtmlHead(out, "Home", request);
      if (username == null)
      {
        out.println("<h1>Immunization Registry Tester &amp; Simple Message Mover</h1>");
      } else
      {
        out.println("<h1>What would you like to do?</h1>");
        out.println("<h2>Create an example message</h2>");
        out.println("<p>Create a sample message based on a specific NIST certification test story. </p>");
        out.println("<form action=\"CreateTestCaseServlet\">");
        out.println("<table>");
        out.println("  <tr>");
        out.println("    <td>Scenario</td>");
        out.println("    <td>");
        out.println("      <select name=\"scenario\">");
        for (String scenario : ScenarioManager.SCENARIOS)
        {
          out.println("        <option value=\"" + scenario + "\">" + scenario + "</option>");
        }
        out.println("      </select>");
        out.println("      <input type=\"submit\" name=\"Start\" value=\"Create\"/>");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("<h2>Send a Message</h2>");
        out.println("<h2>Run Test</h2>");
        out.println("<h2>Profile Interface</h2>");
        out.println("<h2>Stress Test</h2>");
        out.println("<a href=\"StressTestServlet\">Stress Test</a>");
        Authenticate.User user = (Authenticate.User) session.getAttribute("user");
        if (user.hasSendData())
        {
          out.println("<h2>Install Cert</h2>");
          out.println("<a href=\"InstallCertServlet\">Install Cert</a>");
        }
        out.println("<h2>Certify</h2>");
        out.println("<a href=\"CertifyServlet\">Certify DQA Interface</a>");
      }
      printHtmlFoot(out);

    } finally
    {
      out.close();
    }
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
    processRequest(request, response);
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
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo()
  {
    return "DQA Tester Home Page";
  }// </editor-fold>
}
