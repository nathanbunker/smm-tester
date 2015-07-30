package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.manager.CompareManager;
import org.immunizationsoftware.dqa.tester.manager.TestCaseMessageManager;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.transform.Comparison;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.TestError;

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

    out.println("<h3>Message Sent</h3>");
    out.println("<pre>" + testCaseMessage.getMessageTextSent() + "</pre>");

    if (!testCaseMessage.getAdditionalTransformations().equals("")) {
      out.println("<h4>Additional Transformations Applied</h4>");
      out.println("<pre>" + testCaseMessage.getAdditionalTransformations() + "</pre>");
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
      if (!testCaseMessage.getMessageAcceptStatusDebug().equals("")) {
        out.println("<h4>Logic For Expectation</h4>");
        out.println("<pre>" + testCaseMessage.getMessageAcceptStatusDebug() + "</pre>");
      }

    }

    List<Comparison> comparisonList = CompareManager.compareMessages(testCaseMessage.getMessageText(),
        testCaseMessage.getMessageTextSent());
    if (comparisonList.size() > 0) {
      boolean foundImportantChanges = false;
      for (Comparison comparison : comparisonList) {
        if (comparison.isTested() && !comparison.isPass()
            && comparison.getPriorityLevel() <= Comparison.PRIORITY_LEVEL_OPTIONAL) {
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
    testCaseMessage.getErrorList();
    testCaseMessage.getTestCaseNumber();

    if (testCaseMessage.getComparisonList() != null) {
      out.println("    <div id=\"compareDetails\"/>");
      out.println("    <h3>Comparison Results</h3>");
      CompareServlet.printComparison(testCaseMessage.getComparisonList(), out);
    }

    HL7Component actualResponseMessageComponent = TestCaseMessageManager.createHL7Component(testCaseMessage);

    out.println("<h3>HL7 Analysis of Message</h3>");
    if (actualResponseMessageComponent != null) {
      if (actualResponseMessageComponent.hasNoErrors()) {
        out.println("<p>Message conforms</p>");
      } else {
        out.println("<p>Message does not conform</p>");
      }
      ConformanceServlet.printConformanceIssues(out, actualResponseMessageComponent.getConformanceIssueList());
    } else {
      out.println("<p>No analysis performed</p>");
    }
  }
}
