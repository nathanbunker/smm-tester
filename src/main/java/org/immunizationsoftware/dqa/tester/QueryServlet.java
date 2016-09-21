package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.certify.CertifyRunner;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.TestParticipant;
import org.immunizationsoftware.dqa.tester.query.QueryRunner;

public class QueryServlet extends ClientServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    this.doGet(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
      return;
    }
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");

    String queryType = CertifyRunner.QUERY_TYPE_NONE;
    if (request.getParameter("queryType") != null) {
      queryType = request.getParameter("queryType");
    } else if (user.getSendData() != null && user.getSendData().getTestParticipant() != null) {
      queryType = user.getSendData().getTestParticipant().getQueryType();
    }

    Set<String> filenamesSelectedSet = new HashSet<String>();
    String[] filenamesSelected = request.getParameterValues(QueryRunner.FILE_NAME);
    if (filenamesSelected != null) {
      for (String filenameSelected : filenamesSelected) {
        filenamesSelectedSet.add(filenameSelected);
      }
    }
    String userName = request.getParameter(QueryRunner.USER_NAME);
    String password = request.getParameter(QueryRunner.PASSWORD);

    QueryRunner queryRunner = (QueryRunner) session.getAttribute("queryRunner");

    String action = request.getParameter("action");
    if (action != null) {
      if (action.equals("Start") && isReadyToStart(queryRunner)) {
        List<Connector> connectors = ConnectServlet.getConnectors(session);
        int id = Integer.parseInt(request.getParameter("id"));
        queryRunner = new QueryRunner(connectors.get(id - 1), user.getSendData(), queryType, filenamesSelectedSet, userName, password);
        queryRunner.start();
        session.setAttribute("queryRunner", queryRunner);
      } else if (action.equals("Stop") && queryRunner != null) {
        queryRunner.stopRunning();
      }
    }

    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_TEST, request);
      out.println("<h2>Query IIS</h2>");
      out.println("<form id=\"queryForm\" action=\"QueryServlet\" method=\"POST\">");
      out.println("  <table border=\"0\">");
      printServiceSelector(request, session, out);
      out.println("        <tr>");
      out.println("          <td>Query Type</td>");
      out.println("          <td>");
      out.println("            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_NONE + "\""
          + (queryType.equals(CertifyRunner.QUERY_TYPE_NONE) ? " checked=\"true\"" : "") + "> " + CertifyRunner.QUERY_TYPE_NONE + "");
      out.println("            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_QBP_Z34 + "\""
          + (queryType.equals(CertifyRunner.QUERY_TYPE_QBP_Z34) ? " checked=\"true\"" : "") + "> " + CertifyRunner.QUERY_TYPE_QBP_Z34);
      out.println("            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_QBP_Z34_Z44 + "\""
          + (queryType.equals(CertifyRunner.QUERY_TYPE_QBP_Z34_Z44) ? " checked=\"true\"" : "") + "> " + CertifyRunner.QUERY_TYPE_QBP_Z34_Z44);
      out.println("            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_QBP_Z44 + "\""
          + (queryType.equals(CertifyRunner.QUERY_TYPE_QBP_Z44) ? " checked=\"true\"" : "") + "> " + CertifyRunner.QUERY_TYPE_QBP_Z44);
      out.println("            <input type=\"radio\" name=\"queryType\" value=\"" + CertifyRunner.QUERY_TYPE_VXQ + "\""
          + (queryType.equals(CertifyRunner.QUERY_TYPE_VXQ) ? " checked=\"true\"" : "") + "> " + CertifyRunner.QUERY_TYPE_VXQ);
      out.println("          </td>");
      out.println("        </tr>");
      out.println("    <tr>");
      out.println("      <td>Files</td>");
      out.println("      <td>");
      {
        SendData sendData = user.getSendData();
        sendData.setupQueryDir();
        if (sendData.getQueryDir().exists()) {
          String[] filenames = QueryRunner.getListOfFiles(sendData);
          for (String filename : filenames) {
            out.println("      <input type=\"checkbox\" name=\"" + QueryRunner.FILE_NAME + "\" value=\"" + filename + "\""
                + (filenamesSelectedSet.size() == 0 || filenamesSelectedSet.contains(filename) ? " checked=\"true\"" : "") + "/>" + filename);
          }
        }
        out.println("      </td>");
        out.println("    </tr>");
      }
      out.println("    <tr>");
      out.println("      <td>TCH User Name</td>");
      out.println(
          "      <td><input type=\"text\" name=\"" + QueryRunner.USER_NAME + "\" value=\"" + (userName == null ? "" : userName) + "\"/></td>");
      out.println("    </tr>");

      out.println("    <tr>");
      out.println("      <td>TCH Password</td>");
      out.println(
          "      <td><input type=\"password\" name=\"" + QueryRunner.PASSWORD + "\" value=\"" + (password == null ? "" : password) + "\"/></td>");
      out.println("    </tr>");

      out.println("    <tr>");
      out.println("      <td align=\"right\">");
      if (isReadyToStart(queryRunner)) {
        out.println("        <input type=\"submit\" name=\"action\" value=\"Start\"/>");
      } else {
        out.println(queryRunner.getStatus());
        out.println("        <input type=\"submit\" name=\"action\" value=\"Stop\"/>");
      }
      out.println("        <input type=\"submit\" name=\"action\" value=\"Refresh\"/>");
      out.println("      </td>");
      out.println("    </tr>");
      out.println("  </table>");
      out.println("</form>");
      if (queryRunner != null) {
        out.println("<pre>");
        for (String statusMessage : queryRunner.getStatusMessageList()) {
          out.println(statusMessage);
        }
        out.println("</pre>");
      }
      printHtmlFoot(out);
    } finally {
      out.close();
    }

  }

  private boolean isReadyToStart(QueryRunner queryRunner) {
    return queryRunner == null || queryRunner.getStatus().equals(QueryRunner.STATUS_COMPLETED)
        || queryRunner.getStatus().equals(QueryRunner.STATUS_PROBLEM) || queryRunner.getStatus().equals(QueryRunner.STATUS_STOPPED);
  }
}
