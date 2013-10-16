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

import org.immunizationsoftware.dqa.tester.connectors.Connector;

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
        certifyRunner = new CertifyRunner(connectors.get(id - 1), session);
        certifyRunner.setQueryType(queryType);
        certifyRunner.setRunB(request.getParameter("runB") != null);
        certifyRunner.setRunC(request.getParameter("runC") != null);
        certifyRunner.setRunD(request.getParameter("runD") != null);
        certifyRunner.setRunF(request.getParameter("runF") != null);
        certifyRunner.setPauseBeforeQuerying(request.getParameter("pauseBeforeQuerying") != null);
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
      printHtmlHead(out, MENU_HEADER_TEST, request);

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

        out.println("    <h2>Test Results</h2>");
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
        out.println("    <h2>Start Test</h2>");
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
        List<Connector> connectors = ConnectServlet.getConnectors(session);
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
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>Query Type</td>");
        out.println("          <td>");
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"Q\"> QBP");
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"V\"> VXQ");
        out.println("            <input type=\"radio\" name=\"queryType\" value=\"N\" checked=\"true\"> None");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td></td>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"pauseBeforeQuerying\" value=\"true\" checked=\"true\"/> Pause Before Querying");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>Tests to Run</td>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runA\" value=\"true\" checked=\"true\" disabled=\"disabled\"/> Basic");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td></td>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runB\" value=\"true\"/> Intermediate");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td></td>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runC\" value=\"true\"/> Advanced");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td></td>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runD\" value=\"true\"/> Exceptional");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td></td>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runE\" value=\"true\" checked=\"true\" disabled=\"disabled\"/> Performance");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td></td>");
        out.println("          <td>");
        out.println("            <input type=\"checkbox\" name=\"runF\" value=\"true\" checked=\"true\"/> Conformance");
        out.println("          </td>");
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
