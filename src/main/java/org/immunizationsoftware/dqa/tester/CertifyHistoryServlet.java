/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.connectors.Connector;

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
        for (SendData sendData : ManagerServlet.getSendDataList()) {
          if (sendData.getConnector() != null) {

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

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session, String problem)
      throws IOException, ServletException {
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_TEST, request);

      if (problem != null) {
        out.println("<p>" + problem + "</p>");
      }

      for (SendData sendData : ManagerServlet.getSendDataList()) {
        if (sendData.getConnector() != null) {
          List<File> fileList = CreateTestCaseServlet.listIISTestReports(sendData);
          if (fileList.size() > 0) {
            // sendData.getInternalId();
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
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

}
