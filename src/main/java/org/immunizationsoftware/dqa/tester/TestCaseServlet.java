/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.CertifyRunner;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.run.TestRunner;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

/**
 *
 * @author nathan
 */
public class TestCaseServlet extends ClientServlet {

    @Override
    public void init() throws ServletException {
        super.init();

    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
        } else {
            PrintWriter out = response.getWriter();
            try {
                printHtmlHead(out, MENU_HEADER_HOME, request);
                int id = 0;
                List<Connector> connectors = ConnectServlet.getConnectors(session);
                if (connectors.size() == 1) {
                    id = 1;
                } else {
                    if (request.getParameter("id") != null && request.getParameter("id").length() > 0) {
                        id = Integer.parseInt(request.getParameter("id"));
                    }
                }
                session.setAttribute("id", id);
                boolean goodToQuery = id > 0;
                Connector connector = null;
                if (goodToQuery) {
                    connector = SubmitServlet.getConnector(id, session);
                } else {
                    out.println("<p>No connection specified, displaying but not running tests.</p>");
                }
                Set<String> testCaseNumberSelectedSet = setTestCaseNumberSelectedSet(request, session);

                String testCase = request.getParameter("source");
                List<TestCaseMessage> testCaseMessageList = parseAndAddTestCases(testCase, session);

                Map<String, TestCaseMessage> testCaseMessageMap = CreateTestCaseServlet.getTestCaseMessageMap(session);
                for (String testCaseNumber : testCaseNumberSelectedSet) {
                    TestCaseMessage tcm = testCaseMessageMap.get(testCaseNumber);
                    if (tcm != null) {
                        boolean alreadyBeingTested = false;
                        for (TestCaseMessage testing : testCaseMessageList) {
                            if (testing.getTestCaseNumber().equals(tcm.getTestCaseNumber())) {
                                alreadyBeingTested = true;
                                break;
                            }
                        }
                        if (!alreadyBeingTested) {
                            testCaseMessageList.add(tcm);
                        }
                    }
                }

                Collections.sort(testCaseMessageList, new Comparator<TestCaseMessage>() {
                    public int compare(TestCaseMessage o1, TestCaseMessage o2) {
                        return o1.getTestCaseNumber().compareTo(o2.getTestCaseNumber());
                    }
                });
                TestRunner testRunner = new TestRunner();
                for (TestCaseMessage testCaseMessage : testCaseMessageList) {
                    try {
                        String ack = null;
                        boolean pass = false;
                        if (goodToQuery) {
                            try {
                                pass = testRunner.runTest(connector, testCaseMessage);
                                ack = testRunner.getAckMessageText();
                            } catch (Throwable t) {
                                t.printStackTrace(out);
                            }
                        }
                        out.println("<h2>Test Case: " + testCaseMessage.getTestCaseNumber() + "</h2>");
                        out.println("<table border=\"0\" width=\"700\">");
                        out.println("<tr>");
                        if (goodToQuery) {
                            if (pass) {
                                out.println("<td  class=\"pass\" width=\"70%\">Pass");
                            } else {
                                out.println("<td  class=\"fail\" width=\"70%\">Fail");
                            }
                        } else {
                            out.println("<td width=\"70%\">Test Was Not Run");
                        }

                        out.println("</td>");
                        out.println("<td width=\"30%\" align=\"center\">");
                        out.println("  <div class=\"submenu\"><a class=\"menuLink\" href=\"javascript:toggleLayer('" + testCaseMessage.getTestCaseNumber() + "');\" title=\"Show/Hide\">Close/Open</a> &bull; <a class=\"menuLink\" href=\"CreateTestCaseServlet?testCase=" + URLEncoder.encode(testCaseMessage.getTestCaseNumber(), "UTF-8") + "\">Edit</a></div>");
                        out.println("</td>");
                        out.println("</tr>");
                        out.println("</table>");
                        out.println("<div id=\"" + testCaseMessage.getTestCaseNumber() + "\" " + (testCaseMessageList.size() == 1 ? "" : "style=\"display:none\"") + ">");
                        out.println("<table width=\"700\">");
                        out.println("<tr>");
                        out.println("<th nowrap align=\"left\" valign=\"top\">Description</th><td>" + testCaseMessage.getDescription() + "</td>");
                        out.println("</td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<th nowrap align=\"left\" valign=\"top\">Expected Result</th><td> " + testCaseMessage.getExpectedResult() + "</td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<th nowrap align=\"left\" valign=\"top\">Assert Result</th><td> " + testCaseMessage.getAssertResult() + "</td>");
                        out.println("</tr>");
                        if (!testCaseMessage.getCustomTransformations().equals("")) {
                            out.println("<tr>");
                            out.println("<th nowrap align=\"left\" valign=\"top\">Changes</th><td><pre>" + testCaseMessage.getCustomTransformations() + "</pre></td>");
                            out.println("</tr>");
                        }
                        if (!testCaseMessage.getCauseIssues().equals("")) {
                            out.println("<tr>");
                            out.println("<th nowrap align=\"left\" valign=\"top\">Issues</th><td><pre>" + testCaseMessage.getCauseIssues() + "</pre></td>");
                            out.println("</tr>");
                        }
                        out.println("</table>");
                        if (testCaseMessage.getComments().size() > 0) {
                            out.println("<h3>Comments</h3>");
                            out.println("<table width=\"700\">");
                            for (TestCaseMessage.Comment comment : testCaseMessage.getComments()) {
                                out.println("<tr>");
                                out.println("<th nowrap align=\"left\" valign=\"top\">" + comment.getName() + "</th><td>" + comment.getText() + "</td>");
                                out.println("</tr>");
                            }
                            out.println("</table>");
                        }
                        out.print("<div class=\"scrollbox\"><pre>");
                        out.print(testCaseMessage.getMessageText());
                        out.println("</pre></div>");
                        if (ack != null) {
                            out.print("<div class=\"scrollbox\"><pre>");
                            out.print(ack);
                            out.println("</pre></div>");
                        }
                        out.println("</div>");
                        if (!testCaseMessage.getTestCaseNumber().equals("")) {
                            CreateTestCaseServlet.getTestCaseMessageMap(session).put(testCaseMessage.getTestCaseNumber(), testCaseMessage);
                        }
                    } catch (Exception e) {
                        out.println("Unable to run test: " + e.getMessage());
                        out.print("<pre>");
                        e.printStackTrace(out);
                        out.println("</pre>");
                    }
                }
                if (testCaseMessageList.size() == 1) {
                    session.setAttribute("testCaseMessage", testCaseMessageList.get(0));
                }
                out.println("<h2>Test Result Summary <a href=\"javascript:toggleLayer('testResultSummary');\" title=\"Show/Hide\">+/-</a></h2>");
                out.println("<div id=\"testResultSummary\" style=\"display:none\">");
                out.println("<table border=\"1\" cellspacing=\"0\">");
                out.println("<tr>");
                out.println("<th>Test Case</th>");
                out.println("<th>Test Status</th>");
                out.println("<th>Expected Result</th>");
                out.println("<th>Expected Message</th>");
                out.println("<th>Actual Result</th>");
                out.println("<th>Actual Message</th>");
                out.println("</tr>");
                for (TestCaseMessage testCaseMessage : testCaseMessageList) {
                    out.println("<tr>");
                    out.println("<td>" + testCaseMessage.getTestCaseNumber() + "</td>");
                    if (testCaseMessage.getActualResultStatus().equals("PASS")) {
                        out.println("<td class=\"pass\">Pass</td>");
                    } else if (testCaseMessage.getActualResultStatus().equals("FAIL")) {
                        out.println("<td class=\"fail\">Fail</td>");
                    } else {
                        out.println("<td>" + testCaseMessage.getActualResultStatus() + "</td>");
                    }
                    out.println("<td>" + testCaseMessage.getAssertResultStatus() + "</td>");
                    out.println("<td>" + testCaseMessage.getAssertResultText() + "</td>");
                    out.println("<td>" + testCaseMessage.getActualResultAckType() + "</td>");
                    out.println("<td>" + testCaseMessage.getActualResultAckMessage() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("</div>");
                out.println("<h2>Test Script <a href=\"javascript:toggleLayer('script');\" title=\"Show/Hide\">+/-</a></h2>");
                out.println("<div id=\"script\" style=\"display:none\">");
                out.println("<pre>");
                for (TestCaseMessage testCaseMessage : testCaseMessageList) {
                    out.println(testCaseMessage.createText());
                    out.println();
                }
                out.println("</pre>");
                out.println("</div>");
                out.println("<h2>Messages Only <a href=\"javascript:toggleLayer('messagesOnly');\" title=\"Show/Hide\">+/-</a></h2>");
                out.println("<div id=\"messagesOnly\" style=\"display:none\">");
                out.println("<pre>");
                for (TestCaseMessage testCaseMessage : testCaseMessageList) {
                    out.println(testCaseMessage.getMessageText());
                    out.println();
                }
                out.println("</pre>");
                out.println("</div>");
                printHtmlFoot(out);
            } finally {
                out.close();
            }
        }
    }

    protected static Set<String> setTestCaseNumberSelectedSet(HttpServletRequest request, HttpSession session) {
        Set<String> testCaseNumberSelectedSet = new HashSet<String>();
        String[] testCaseNumberSelected = request.getParameterValues("testCaseNumber");
        if (testCaseNumberSelected != null) {
            for (String s : testCaseNumberSelected) {
                if (s != null && s.length() > 0) {
                    testCaseNumberSelectedSet.add(s);
                }
            }
        }
        session.setAttribute("testCaseNumberSelectedList", testCaseNumberSelectedSet);
        return testCaseNumberSelectedSet;
    }

    protected static List<TestCaseMessage> parseAndAddTestCases(String testCase, HttpSession session) throws ServletException {
        List<TestCaseMessage> testCaseMessageList = null;
        if (testCase == null) {
            testCaseMessageList = new ArrayList<TestCaseMessage>();
        } else {
            try {
                testCaseMessageList = TestCaseMessage.createTestCaseMessageList(testCase);
            } catch (Exception e) {
                throw new ServletException("Unable to read test case messages", e);
            }
        }
        return testCaseMessageList;
    }

    protected static void makeHideScript(PrintWriter out) {
        out.println("    <script>");
        out.println("      function toggleLayer(whichLayer) ");
        out.println("      {");
        out.println("        var elem, vis;");
        out.println("        if (document.getElementById) ");
        out.println("          elem = document.getElementById(whichLayer);");
        out.println("        else if (document.all) ");
        out.println("          elem = document.all[whichLayer] ");
        out.println("        else if (document.layers) ");
        out.println("          elem = document.layers[whichLayer]");
        out.println("        vis = elem.style;");
        out.println("        if (vis.display == '' && elem.offsetWidth != undefined && elem.offsetHeight != undefined) ");
        out.println("          vis.display = (elem.offsetWidth != 0 && elem.offsetHeight != 0) ? 'block' : 'none';");
        out.println("        vis.display = (vis.display == '' || vis.display == 'block') ? 'none' : 'block';");
        out.println("      }");
        out.println("    </script>");
    }

    protected static void sortTestCaseMessageList(List<TestCaseMessage> testCaseMessageList) {
        Collections.sort(testCaseMessageList, new Comparator<TestCaseMessage>() {

            public int compare(TestCaseMessage o1, TestCaseMessage o2) {
                return o1.getTestCaseNumber().compareTo(o2.getTestCaseNumber());
            }
        });
    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
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
            PrintWriter out = response.getWriter();
            try {
                printHtmlHead(out, MENU_HEADER_HOME, request);
                out.println("    <form action=\"testCase\" method=\"POST\">");
                out.println("      <table border=\"0\">");
                int id = 0;
                if (request.getParameter("id") != null) {
                    id = Integer.parseInt(request.getParameter("id"));
                }
                if (session.getAttribute("id") != null) {
                    id = (Integer) session.getAttribute("id");
                }
                TestCaseMessage testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
                String certifyServletBasicNum  = request.getParameter("certifyServletBasicNum");
                if (certifyServletBasicNum != null)
                {
                  CertifyRunner certifyRunner = (CertifyRunner) session.getAttribute("certifyRunner");
                  if (certifyRunner != null) {
                    testCaseMessage = certifyRunner.getStatusCheckTestCaseList().get(Integer.parseInt(certifyServletBasicNum));
                  }
                }
                if (testCaseMessage == null) {
                  testCaseMessage = new TestCaseMessage();
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
                            out.println("              <option value=\"" + i + "\" selected=\"true\">" + connector.getLabelDisplay() + "</option>");
                        } else {
                            out.println("              <option value=\"" + i + "\">" + connector.getLabelDisplay() + "</option>");
                        }
                    }
                    out.println("            </select>");
                }
                out.println("          </td>");
                out.println("        </tr>");
                out.println("        <tr>");
                out.println("          <td>Test</td>");
                out.println("          <td><textarea name=\"source\" cols=\"70\" rows=\"10\" wrap=\"off\">" + testCaseMessage.createText() + "</textarea></td>");
                out.println("        </tr>");
                out.println("        <tr>");
                out.println("          <td colspan=\"2\" align=\"right\">");
                out.println("            <input type=\"submit\" name=\"method\" value=\"Submit\"/>");
                out.println("          </td>");
                out.println("        </tr>");
                out.println("      </table>");
                out.println("    </form>");
                out.println("  <h2>How To Use This Page</h2>");
                out.println("  <div class=\"help\">");
                out.println("  <p>Enter in the user authentication parameters supplied by the target system and "
                        + "then select the target system service. (For more information please see Test Transport "
                        + "section, this page works the same way.) Then enter one or more test case descriptions "
                        + "into the test area. "
                        + "</p>");
                out.println("    <p>After submitting you will see the results for each test case. "
                        + "If there are more than one test cases listed then the results "
                        + "will be collapsed so that only the Test Case id and "
                        + "the final PASS or FAIL status is shown. Simply click on the "
                        + "+/- link to view the details of each test. </p>");
                out.println("  </div>");
                printHtmlFoot(out);
            } finally {
                out.close();
            }
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "";
    }// </editor-fold>
}
