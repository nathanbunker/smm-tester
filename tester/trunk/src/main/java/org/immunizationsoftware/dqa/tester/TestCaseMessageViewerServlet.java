package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.CertifyServlet.CertifyRunner;
import org.immunizationsoftware.dqa.tester.manager.HL7Analyzer;
import org.immunizationsoftware.dqa.tester.run.TestRunner;

public class TestCaseMessageViewerServlet extends ClientServlet
{
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
    String problem = null;

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
      printHtmlHead(out, "Test Case Message Viewer", request);

      if (problem != null) {
        out.println("<p>" + problem + "</p>");
      }
      TestCaseMessage testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
      String certifyServletBasicNum = request.getParameter("certifyServletBasicNum");
      if (certifyServletBasicNum != null) {
        CertifyRunner certifyRunner = (CertifyRunner) session.getAttribute("certifyRunner");
        if (certifyRunner != null) {
          testCaseMessage = certifyRunner.getStatusCheckTestCaseList().get(Integer.parseInt(certifyServletBasicNum));
        }
      }

      out.println("    <h3>TestCase Message</h3>");
      if (testCaseMessage != null) {
        printTestCaseMessage(out, testCaseMessage);
        out.println("<p><a href=\"testCase?certifyServletBasicNum=" + certifyServletBasicNum
            + "\">Run Test Case</a></p>");
      }

      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  public void printTestCaseMessage(PrintWriter out, TestCaseMessage testCaseMessage) {
    out.println("<h2>" + testCaseMessage.getDescription() + "</h2>");
    out.println("<pre>" + testCaseMessage.getMessageText() + "</pre>");
    out.println("<table>");
    out.println("  <tr>");
    out.println("    <td>Assert Result</td>");
    out.println("    <td>" + testCaseMessage.getAssertResult() + "</td>");
    out.println("  </tr>");
    if (testCaseMessage.getCauseIssues() != null && testCaseMessage.getCauseIssues().equals("")) {
      out.println("  <tr>");
      out.println("    <td>Cause Issues</td>");
      out.println("    <td>" + testCaseMessage.getCauseIssues() + "</td>");
      out.println("  </tr>");
    }
    out.println("</table>");
    if (testCaseMessage.getException() != null) {
      out.println("<p>Exception occurred: " + testCaseMessage.getException().getMessage() + "</p>");
      out.print("<pre>");
      testCaseMessage.getException().printStackTrace(out);
      out.print("</pre>");
    }
    out.println("<pre>" + testCaseMessage.getActualResponseMessage() + "</pre>");
    out.println("<table>");
    out.println("  <tr>");
    out.println("    <td>Ack Message</td>");
    out.println("    <td>" + testCaseMessage.getActualResultAckMessage() + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <td>Ack Type</td>");
    out.println("    <td>" + testCaseMessage.getActualResultAckType() + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <td>Result Status</td>");
    out.println("    <td>" + testCaseMessage.getActualResultStatus() + "</td>");
    out.println("  </tr>");
    List<TestRunner.Error> errorList = testCaseMessage.getErrorList();
    if (errorList != null) {
      for (TestRunner.Error error : errorList) {
        out.println("  <tr>");
        out.println("    <td>" + error.getErrorType() + "</td>");
        out.println("    <td>" + error.getDescription() + "</td>");
        out.println("  </tr>");
      }
    }
    out.println("</table>");
    out.println("<br/>");
    HL7Analyzer ackAnalyser = testCaseMessage.getAckAnalyzer();
    if (ackAnalyser == null) {
      ackAnalyser = new HL7Analyzer(testCaseMessage);
    }

    out.println("<h3>HL7 Analysis of Message</h3>");
    if (ackAnalyser.isPassedTest()) {
      out.println("<p>No problems found</p>");
    } else {
      out.println("<p>The following issues were found</p>");
      out.println("<ul>");
      for (String issue : ackAnalyser.getIssueList()) {
        out.println("  <li>" + issue + "</li>");
      }
      out.println("</ul>");
    }

  }
}
