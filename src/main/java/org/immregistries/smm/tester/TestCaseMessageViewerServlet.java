package org.immregistries.smm.tester;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immregistries.smm.tester.manager.nist.Assertion;
import org.immregistries.smm.tester.manager.nist.ValidationReport;
import org.immregistries.smm.transform.TestCaseMessage;

public class TestCaseMessageViewerServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
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
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {
      doGet(request, response, session, null);
    }
  }

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session,
      String problem) throws IOException {
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);

      if (problem != null) {
        out.println("<p>" + problem + "</p>");
      }
      TestCaseMessage testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
      String certifyServletBasicNum = request.getParameter("certifyServletBasicNum");

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

    if (testCaseMessage.getException() != null) {
      out.println("<h3>Unexpected Problem Occurred</h3>");
      out.println("<p>Exception occurred: " + testCaseMessage.getException().getMessage() + "</p>");
      out.print("<pre>");
      testCaseMessage.getException().printStackTrace(out);
      out.print("</pre>");
    }
    if (testCaseMessage.getDerivedFromVXUMessage() != null
        && !testCaseMessage.getDerivedFromVXUMessage().equals("")) {
      out.println("<h3>Request Derived From This VXU Message</h3>");
      out.println("<pre>" + testCaseMessage.getDerivedFromVXUMessage() + "</pre>");
    }
    testCaseMessage.getErrorList();
    testCaseMessage.getTestCaseNumber();

    if (testCaseMessage.getValidationReport() != null) {
      ValidationReport vr = testCaseMessage.getValidationReport();
      out.println("<h3>ONC 2015 Analysis of Message</h3>");
      out.println("      <table border=\"1\" cellspacing=\"0\">");
      out.println("        <tr>");
      out.println("          <th>Error</th>");
      out.println("          <td>" + vr.getHeaderReport().getErrorCount() + "</td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <th>Warning</th>");
      out.println("          <td>" + vr.getHeaderReport().getWarningCount() + "</td>");
      out.println("        </tr>");
      out.println("      </table>");
      out.println("      <br/>");
      out.println("      <table border=\"1\" cellspacing=\"0\">");
      out.println("        <tr>");
      out.println("          <th>Result</th>");
      out.println("          <th>Type</th>");
      out.println("          <th>Location</th>");
      out.println("          <th>Description</th>");
      out.println("        </tr>");
      for (Assertion assertion : testCaseMessage.getValidationReport().getAssertionList()) {
        String passClass =
            assertion.getResult().equalsIgnoreCase("ERROR") ? " class=\"fail\"" : " class=\"pass\"";
        out.println("        <tr>");
        out.println("          <td" + passClass + ">" + assertion.getResult() + "</td>");
        out.println("          <td" + passClass + ">" + assertion.getType() + "</td>");
        out.println("          <td" + passClass + ">" + assertion.getPath() + "</td>");
        out.println("          <td" + passClass + ">" + assertion.getDescription() + "</td>");
        out.println("        </tr>");
      }
      out.println("      </table>");
    }
  }
}
