/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.transform.ScenarioManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

/**
 * 
 * @author nathan
 */
public class SetupServlet extends ClientServlet
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
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {
      
      String testSetSelected = request.getParameter("testSet");
      if (testSetSelected != null) {
        CreateTestCaseServlet.setTestSetSelected(testSetSelected, session);
      } else {
        testSetSelected = CreateTestCaseServlet.getTestSetSelected(session);
      }
      
      String testScript = request.getParameter("testScript");
      List<TestCaseMessage> selectedTestCaseMessageList = null;
      if (testScript == null) {
        selectedTestCaseMessageList = getSelectedTestCaseMessageList(testSetSelected, request, session);
        if (selectedTestCaseMessageList != null) {
          TestCaseServlet.sortTestCaseMessageList(selectedTestCaseMessageList);
          session.setAttribute("selectedTestCaseMessageList", selectedTestCaseMessageList);
        }
      }

      String action = request.getParameter("action");
      if (action != null) {
        if (action.equals("Load Test Cases")) {
          loadTestCases(request, session);
        } else if (action.equals("Download Script") || action.equals("Download HL7 Only")) {
          RequestDispatcher dispatcher = request.getRequestDispatcher("DownloadServlet");
          dispatcher.forward(request, response);
          return;
        } else if (action.equals("Edit")) {
          RequestDispatcher dispatcher = request.getRequestDispatcher("CreateTestCaseServlet");
          dispatcher.forward(request, response);
          return;
        } else if (action.equals("Test")) {
          RequestDispatcher dispatcher = request.getRequestDispatcher("testCase");
          dispatcher.forward(request, response);
          return;
        }
      }

      PrintWriter out = response.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_SETUP, request);
        out.println("<table border=\"0\">");
       
        Map<String, TestCaseMessage> testCaseMessageMap = CreateTestCaseServlet.getTestCaseMessageMap(testSetSelected,
            session);
        List<String> testCaseNumberList = new ArrayList<String>(testCaseMessageMap.keySet());
        List<String> testCaseSetList = new ArrayList<String>(
            CreateTestCaseServlet.getTestCaseMessageMapMap(session).keySet());
        if (testCaseNumberList.size() > 0 || testCaseSetList.size() > 1) {
          out.println("<h2>Test Cases Saved</h2>");
          out.println("<form action=\"SetupServlet\" method=\"POST\">");
          {
            out.println("  <tr>");
            out.println("    <td>Test Set</td>");
            out.println("    <td>");
            out.println("      <select name=\"testSet\" onChange=\"this.form.submit()\">");
            Collections.sort(testCaseSetList);
            out.println("              <option value=\"\"" + (testSetSelected == null ? " selected=\"true\"" : "")
                + ">-- Not Specified --</option>");
            for (String testCaseSet : testCaseSetList) {
              if (testCaseSet.equals("")) {
                continue;
              }
              boolean selected = testSetSelected != null && testSetSelected.equals(testCaseSet);
              out.println("              <option value=\"" + testCaseSet + "\"" + (selected ? " selected=\"true\"" : "")
                  + ">" + testCaseSet + "</option>");
            }
            out.println("      </select>");
            out.println("    </td>");
            out.println("  </tr>");
          }
          {
            out.println("  <tr>");
            out.println("    <td>Test Cases</td>");
            out.println("    <td>");
            if (testCaseNumberList.size() > 0) {
              out.println("      <select name=\"testCaseNumber\" multiple=\"true\" size=\"7\">");
              Collections.sort(testCaseNumberList);
              Set<String> testCaseNumberSelectedSet = (Set<String>) session.getAttribute("testCaseNumberSelectedList");
              if (testCaseNumberSelectedSet == null) {
                testCaseNumberSelectedSet = new HashSet<String>();
              }
              for (String testCaseNumber : testCaseNumberList) {
                TestCaseMessage tcm = testCaseMessageMap.get(testCaseNumber);
                String text = tcm.getTestCaseNumber() + ": " + (tcm.getDescription().length() > 80
                    ? tcm.getDescription().substring(0, 80) + "..." : tcm.getDescription());
                boolean selected = testCaseNumberSelectedSet.contains(testCaseNumber);
                out.println("              <option value=\"" + tcm.getTestCaseNumber() + "\""
                    + (selected ? " selected=\"true\"" : "") + ">" + text + "</option>");
              }
              out.println("      </select>");
            } else {
              out.println("No Test Cases Saved");
            }
            out.println("    </td>");
            out.println("  </tr>");
          }
          if (testCaseNumberList.size() > 0) {
            out.println("  <tr>");
            out.println("    <td colspan=\"2\" align=\"right\">");
            out.println("      <input type=\"submit\" name=\"action\" value=\"Edit\">");
            out.println("      <input type=\"submit\" name=\"action\" value=\"Test\">");
            out.println("      <input type=\"submit\" name=\"action\" value=\"Download Script\">");
            out.println("      <input type=\"submit\" name=\"action\" value=\"Download HL7 Only\">");
            out.println("    </td>");
            out.println("  </tr>");
          }
          out.println("</form>");
        }
        out.println("<h2>Load Test Cases</h2>");
        out.println("<form action=\"SetupServlet\" method=\"POST\">");
        out.println("  <tr>");
        out.println("    <td valign=\"top\">Script</td>");
        out.println("    <td><textarea name=\"testScript\" cols=\"60\" rows=\"7\" wrap=\"off\"></textarea></td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td colspan=\"2\" align=\"right\">");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Load Test Cases\">");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Download Script\">");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Download HL7 Only\">");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("</form>");
        out.println("</table>");

        out.println("<h2>Create new Test Case</h2>");
        out.println("<p>Create a sample test case based on a specific NIST certification test story. </p>");
        out.println("<form action=\"CreateTestCaseServlet\">");
        out.println("<table>");
        out.println("  <tr>");
        out.println("    <td>Scenario</td>");
        out.println("    <td>");
        out.println("      <select name=\"scenario\">");
        for (String scenario : ScenarioManager.SCENARIOS) {
          out.println("        <option value=\"" + scenario + "\">" + scenario + "</option>");
        }
        out.println("      </select>");
        out.println("      <input type=\"submit\" name=\"Start\" value=\"Create\"/>");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("</table>");
        out.println("</form>");
        printHtmlFoot(out);

      } finally {
        out.close();
      }
    }
  }

  protected static List<TestCaseMessage> getSelectedTestCaseMessageList(String testCaseSet, HttpServletRequest request,
      HttpSession session) {
    List<TestCaseMessage> testCaseMessageList = new ArrayList<TestCaseMessage>();
    Set<String> testCaseNumberSelectedSet = TestCaseServlet.setTestCaseNumberSelectedSet(request, session);
    Map<String, TestCaseMessage> testCaseMessageMap = CreateTestCaseServlet.getTestCaseMessageMap(testCaseSet, session);
    for (String testCaseNumber : testCaseNumberSelectedSet) {
      TestCaseMessage tcm = testCaseMessageMap.get(testCaseNumber);
      if (testCaseNumberSelectedSet.contains(tcm.getTestCaseNumber())) {
        testCaseMessageList.add(tcm);
      }
    }
    return testCaseMessageList;
  }

  protected void loadTestCases(HttpServletRequest request, HttpSession session) {
    String testScript = request.getParameter("testScript");
    try {
      List<TestCaseMessage> testCaseMessageList = TestCaseServlet.parseAndAddTestCases(testScript, session);
      for (TestCaseMessage testCaseMessage : testCaseMessageList) {
        if (!testCaseMessage.getTestCaseNumber().equals("")) {
          CreateTestCaseServlet.getTestCaseMessageMap(testCaseMessage.getTestCaseSet(), session)
              .put(testCaseMessage.getTestCaseNumber(), testCaseMessage);
        }
      }
    } catch (Throwable e) {
      String message = "Unable to load test script, exception ocurred: " + e.getMessage();
      request.setAttribute("message", message);
    }
  }

  // <editor-fold defaultstate="collapsed"
  // desc="HttpServlet methods. Click on the + sign on the left to edit the
  // code.">

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
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>
}
