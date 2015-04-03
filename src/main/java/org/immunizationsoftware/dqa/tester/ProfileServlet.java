package org.immunizationsoftware.dqa.tester;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_FULL_RECORD_FOR_PROFILING;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.profile.ProfileLine;
import org.immunizationsoftware.dqa.tester.profile.ProfileManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;

public class ProfileServlet extends ClientServlet
{
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {
      PrintWriter out = response.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_TEST, request);

        out.println("    <h2>View Profile</h2>");
        out.println("    <form action=\"ProfileServlet\" method=\"GET\">");
        out.println("      <table border=\"0\">");
        Connector connector = printServiceSelector(request, session, out);
        out.println("        <tr>");
        out.println("          <td></td>");
        out.println("          <td colspan=\"2\" align=\"right\">");
        out.println("            <input type=\"submit\" name=\"action\" value=\"Select\"/>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("      </table>");
        out.println("    </form>");
        if (connector != null) {
          File profileFile = CreateTestCaseServlet.getProfileFile((Authenticate.User) session.getAttribute("user"));
          if (profileFile != null) {
            out.println("<h2>Profile for " + connector.getLabelDisplay() + "</h2>");
            List<ProfileLine> profileLineList = ProfileManager.readProfileLines(profileFile);
            ProfileManager.updateMessageAcceptStatus(profileLineList);
            TestCaseMessage tcmFull = createTestCaseMessage(SCENARIO_FULL_RECORD_FOR_PROFILING);
            Transformer transformer = new Transformer();
            transformer.transform(tcmFull);
            out.println("<h3>Full Test Case</h3>");
            out.println("<p>Before beginning a profile test, the tester submits the following full message to ensure "
                + "that the IIS can process successfully a message with nearly all fields that a sending system might "
                + "possibily fill out. This message is further modified for each test to verify what the IIS will do "
                + "when certain fields are present or absent. Using this information the tester can determine which fields"
                + "must be present and which must be absent. In this way the tester can verify the documented standards for"
                + "the IIS. </p>");
            out.println("<h4>Standard Transformations</h4>");
            out.println("<pre>" + tcmFull.getQuickTransformationsConverted() + "</pre>");
            out.println("<h4>Custom Transformations</h4>");
            out.println("<pre>" + tcmFull.getCustomTransformations() + "</pre>");
            out.println("<h4>Example Message</h4>");
            out.println("<pre>" + tcmFull.getMessageText() + "</pre>");
            out.println("<h4>Field Tests</h4>");

            out.println("<table border=\"1\" cellspacing=\"0\">");
            out.println("  <tr>");
            out.println("    <th>Field</th>");
            out.println("    <th>Description</th>");
            out.println("    <th>Usage</th>");
            out.println("    <th>Message Accepted</th>");
            out.println("    <th>Present Test</th>");
            out.println("    <th>Absent Test</th>");
            out.println("  </tr>");
            for (ProfileLine profileLine : profileLineList) {
              TestCaseMessage testCaseMessagePresent = ProfileManager.getPresentTestCase(profileLine.getField(),
                  tcmFull);
              TestCaseMessage testCaseMessageAbsent = ProfileManager.getAbsentTestCase(profileLine.getField(), tcmFull);
              out.println("  <tr>");
              out.println("    <td>" + profileLine.getField().getFieldName() + "</td>");
              out.println("    <td>" + profileLine.getField().getDescription() + "</td>");
              out.println("    <td>" + profileLine.getUsage() + "</td>");
              out.println("    <td>" + profileLine.getMessageAcceptStatus() + "</td>");
              if (testCaseMessagePresent == tcmFull) {
                out.println("    <td>Full Test Case</td>");
              } else if (!testCaseMessagePresent.hasIssue()) {
                out.println("    <td class=\"fail\">Not Defined</td>");
              } else {
                out.println("    <td><pre>" + testCaseMessagePresent.getAdditionalTransformations() + "</pre></td>");
              }
              if (testCaseMessageAbsent == tcmFull) {
                out.println("    <td>Full Test Case</td>");
              } else if (!testCaseMessageAbsent.hasIssue()) {
                out.println("    <td class=\"fail\">Not Defined</td>");
              } else {
                out.println("    <td><pre>" + testCaseMessageAbsent.getAdditionalTransformations() + "</pre></td>");
              }
              out.println("  </tr>");
            }
            out.println("</table>");
          }
        }

        printHtmlFoot(out);
      } finally {
        out.close();
      }
    }
  }
}
