/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.InstallCert;

/**
 * 
 * @author nathan
 */
public class InstallCertServlet extends ClientServlet
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
    if (username == null)
    {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else
    {
      Authenticate.User user = (Authenticate.User) session.getAttribute("user");
      if (user.getSendData() == null)
      {
        response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
        return;
      }
      SendData sendData = user.getSendData();
      InstallCert installCert = (InstallCert) session.getAttribute("installCert");
      if (installCert == null)
      {
        installCert = new InstallCert();
        File certFile = new File(sendData.getRootDir(), "smm.jks");
        installCert.setFile(certFile);
        URL url = new URL(sendData.getConnector().getUrl());
        installCert.setHost(url.getHost());
        if (url.getPort() > 0)
        {
          installCert.setPort(url.getPort());
        }
        session.setAttribute("installCert", installCert);
      }
      int id = 0;
      List<Connector> connectors = ConnectServlet.getConnectors(session);
      if (connectors.size() == 1)
      {
        id = 1;
      } else
      {
        if (request.getParameter("id") != null && !request.getParameter("id").equals(""))
        {
          id = Integer.parseInt(request.getParameter("id"));
        } else if (session.getAttribute("id") != null)
        {
          id = (Integer) session.getAttribute("id");
        }
      }

      String action = request.getParameter("action");

      PrintWriter out = response.getWriter();
      try
      {
        printHtmlHead(out, MENU_HEADER_SETUP, request);
        out.println("    <form action=\"InstallCertServlet\" method=\"POST\">");
        out.println("      <table border=\"0\">");
        out.println("        <tr>");
        out.println("          <td>Connection</td>");
        out.println("          <td>");
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
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td colspan=\"2\" align=\"right\">");
        out.println("            <input type=\"submit\" name=\"action\" value=\"Check\"/>");
        out.println("          </td>");
        out.println("        </tr>");
        if (action != null && action.equals("Check"))
        {
          out.println("        <tr>");
          out.println("          <td colspan=\"2\">");
          out.println("            <pre>");
          try
          {
            installCert.findCert(out);
          } catch (Exception e)
          {
            e.printStackTrace(out);
          }
          out.println("            </pre>");
          out.println("          </td>");
          out.println("        </tr>");
        }

        if (installCert.getChain() != null)
        {
          int pos = 0;
          for (X509Certificate cert : installCert.getChain())
          {
            out.println("        <tr>");
            out.println("          <td>");
            out.println("            <input type=\"submit\" name=\"action\" value=\"Install " + (pos + 1) + "\"/>");
            out.println("          </td>");
            out.println("        </tr>");
          }
        }
        if (action != null && action.startsWith("Install "))
        {
          out.println("        <tr>");
          out.println("          <td colspan=\"2\">");
          out.println("            <pre>");
          try
          {
            installCert.setChainPos(Integer.parseInt(action.substring("Install ".length())) - 1);
            installCert.saveCert(out);
          } catch (Exception e)
          {
            e.printStackTrace(out);
          }
          out.println("            </pre>");
          out.println("          </td>");
          out.println("        </tr>");
        }
        out.println("      </table>");
        out.println("    </form>");

        printHtmlFoot(out);

      } finally
      {
        out.close();
      }
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
    return "Short description";
  }// </editor-fold>
}
