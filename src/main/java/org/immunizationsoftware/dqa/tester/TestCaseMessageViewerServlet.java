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
      printHtmlHead(out, MENU_HEADER_HOME, request);

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

  public static void printTestCaseMessage(PrintWriter out, TestCaseMessage testCaseMessage) {
    out.println("<h2>" + testCaseMessage.getDescription() + "</h2>");

    if (testCaseMessage.getOriginalMessage() != null && !testCaseMessage.getOriginalMessage().equals("") && !testCaseMessage.getOriginalMessage().equals(testCaseMessage.getMessageText())) {
      out.println("<h3>Original Message</h3>");
      out.println("<p>The starting point for the message, before transformations applied. </p>");
      out.println("<pre>" + testCaseMessage.getOriginalMessage() + "</pre>");
    }
    if (testCaseMessage.getQuickTransformations() != null && testCaseMessage.getQuickTransformations().length > 0) {
      out.println("<p>The following quick transformations applied to original message:</p>");
      out.println("<pre>");
      for (String s : testCaseMessage.getQuickTransformations()) {
        out.println("" + s + "");
      }
      out.println("</pre>");
    }
    if (testCaseMessage.getCustomTransformations() != null && !testCaseMessage.getCustomTransformations().equals("")) {
      out.println("<p>The following custom transformations applied to original message:</p>");
      out.println("<pre>" + testCaseMessage.getCustomTransformations() + "</pre>");
    }
    out.println("<h3>Request</h3>");
    out.println("<p>This is the base text for the request. No local transformations have been applied.</p>");
    out.println("<pre>" + testCaseMessage.getMessageText() + "</pre>");

    if (testCaseMessage.getMessageTextSent() != null && !testCaseMessage.getMessageTextSent().equals("")
        && !testCaseMessage.getMessageTextSent().equals(testCaseMessage.getMessageText())) {
      out.println("<h3>Message Actually Sent</h3>");
      out.println("<p>The final message message sent was different than the request, local transformations were applied. "
          + "These are specified in the connection settings of the connector that was used to run this test. </p>");
      out.println("<pre>" + testCaseMessage.getMessageTextSent() + "</pre>");

      List<CompareManager.Comparison> comparisonList = CompareManager.compareMessages(testCaseMessage.getMessageText(),
          testCaseMessage.getMessageTextSent());
      if (comparisonList.size() > 0) {
        boolean foundImportantChanges = false;
        for (CompareManager.Comparison comparison : comparisonList) {
          if (comparison.isTested() && !comparison.isPass() && comparison.getPriorityLevel() <= CompareManager.Comparison.PRIORITY_LEVEL_OPTIONAL) {
            foundImportantChanges = true;
            break;
          }
        }
        if (foundImportantChanges) {
          out.println("<div id=\"changesMade\"/>");
          out.println("<h3>Substantial Changes Made To Message Actually Sent</h3>");
          CompareServlet.printComparison(comparisonList, out);
        }
      }
    }

    if (testCaseMessage.isHasRun()) {
      if (testCaseMessage.getActualMessageResponseType().equals("ACK")) {
        if (testCaseMessage.isAccepted()) {
          out.println("<h3>Message Accepted</h3>");
        } else {
          out.println("<h3>Message Rejected</h3>");
        }
      } else {
        out.println("<h3>Response Received</h3>");
      }
      out.println("<pre>" + testCaseMessage.getActualResponseMessage() + "</pre>");
    }
    if (testCaseMessage.getException() != null) {
      out.println("<h3>Unexpected Problem Occurred</h3>");
      out.println("<p>Exception occurred: " + testCaseMessage.getException().getMessage() + "</p>");
      out.print("<pre>");
      testCaseMessage.getException().printStackTrace(out);
      out.print("</pre>");
    }
    if (testCaseMessage.getDerivedFromVXUMessage() != null && !testCaseMessage.getDerivedFromVXUMessage().equals("")) {
      out.println("<h3>Request Derived From This VXU Message</h3>");
      out.println("<pre>" + testCaseMessage.getDerivedFromVXUMessage() + "</pre>");
    }
    if (testCaseMessage.getCustomTransformations() != null && !testCaseMessage.getCustomTransformations().equals("")) {
      out.println("<h3>Custom Transformations Applied</h3>");
      out.println("<pre>" + testCaseMessage.getCustomTransformations() + "</pre>");
    }
    testCaseMessage.getErrorList();
    testCaseMessage.getTestCaseNumber();

    out.println("<table>");
    out.println("  <tr>");
    out.println("    <th>Assert Result</th>");
    out.println("    <td>" + testCaseMessage.getAssertResult() + "</td>");
    out.println("  </tr>");
    if (testCaseMessage.getCauseIssues() != null && !testCaseMessage.getCauseIssues().equals("")) {
      out.println("  <tr>");
      out.println("    <th>Cause Issues</th>");
      out.println("    <td>" + testCaseMessage.getCauseIssues() + "</td>");
      out.println("  </tr>");
    }

    out.println("  <tr>");
    out.println("    <th>Ack Type</th>");
    out.println("    <td>" + testCaseMessage.getActualResultAckType() + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Accepted</th>");
    out.println("    <td>" + (testCaseMessage.isAccepted() ? "Yes" : "No") + "</td>");
    out.println("  </tr>");
    out.println("  <tr>");
    out.println("    <th>Test Result Status</th>");
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

    if (testCaseMessage.getComparisonList() != null) {
      out.println("    <div id=\"compareDetails\"/>");
      out.println("    <h3>Comparison Results</h3>");
      CompareServlet.printComparison(testCaseMessage.getComparisonList(), out);
    }

    HL7Analyzer ackAnalyser = testCaseMessage.getHL7Analyzer();
    if (ackAnalyser == null) {
      ackAnalyser = new HL7Analyzer(testCaseMessage);
    }

    out.println("<h3>HL7 Analysis of Message</h3>");
    if (ackAnalyser.isPassedTest()) {
      out.println("<p>No problems found</p>");
    } else {
      out.println("<p>The following issues were found: </p>");
      out.println("<ul>");
      for (String issue : ackAnalyser.getIssueList()) {
        out.println("  <li>" + issue + "</li>");
      }
      out.println("</ul>");
      out.println("issue count = " + ackAnalyser.getIssueList().size());
    }

  }
}
