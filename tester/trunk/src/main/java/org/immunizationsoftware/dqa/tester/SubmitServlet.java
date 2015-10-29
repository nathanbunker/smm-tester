/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.HttpConnector.AuthenticationMethod;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.QueryConverter;
import org.immunizationsoftware.dqa.tester.manager.TestCaseMessageManager;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7ComponentManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;

/**
 * 
 * @author nathan
 */
public class SubmitServlet extends ClientServlet
{

  protected static Connector getConnector(int id, HttpSession session) throws ServletException {
    List<Connector> connectors = ConnectServlet.getConnectors(session);
    id--;
    if (id < connectors.size()) {
      return connectors.get(id);
    }
    throw new IllegalArgumentException("Unable to find connection " + id);
  }

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
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {
      // For example purposes, determine what method to perform based on
      // a "method" request parameter in the URL.
      int id = 0;
      if (request.getParameter("id") != null) {
        id = Integer.parseInt(request.getParameter("id"));
      }
      String str = request.getParameter("method");
      if (str == null || !str.equalsIgnoreCase("Submit") || id == 0) {
        return;
      }
      boolean debug = request.getParameter("debug") != null;
      boolean transform = request.getParameter("transform") != null;
      Connector connector = getConnector(id, session);
      String message = request.getParameter("message");

      if (transform) {
        TestCaseMessage testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
        String scenarioTransforms = null;
        String additionalTransformations = "";
        if (testCaseMessage != null) {
          scenarioTransforms = connector.getScenarioTransformationsMap().get(testCaseMessage.getScenario());
          additionalTransformations = testCaseMessage.getAdditionalTransformations();
          if (additionalTransformations.equals("")) {
            additionalTransformations = null;
          }
        }
        if (!connector.getCustomTransformations().equals("") || scenarioTransforms != null
            || additionalTransformations != null) {
          Transformer transformer = new Transformer();
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
          connector.setCurrentFilename("dqa-tester-request" + sdf.format(new Date()) + ".hl7");
          message = transformer.transform(connector, message, scenarioTransforms, additionalTransformations);
        }
      }
      message = cleanMessage(message);
      request.setAttribute("requestText", message);
      String responseText = connector.submitMessage(message, debug);
      request.setAttribute("responseText", responseText);
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {
      int id = 0;
      List<Connector> connectors = ConnectServlet.getConnectors(session);
      if (connectors.size() == 1) {
        id = 1;
      } else {
        if (request.getParameter("id") != null && !request.getParameter("id").equals("")) {
          id = Integer.parseInt(request.getParameter("id"));
        } else if (session.getAttribute("id") != null) {
          id = (Integer) session.getAttribute("id");
        }
      }

      String userId = request.getParameter("userid");
      String password = request.getParameter("password");
      String facilityId = request.getParameter("facilityid");
      String message = request.getParameter("message");
      if (userId == null) {
        userId = (String) session.getAttribute("userId");
        if (userId == null) {
          userId = "";
        }
      }
      if (password == null) {
        password = (String) session.getAttribute("password");
        if (password == null) {
          password = "";
        }
      }
      if (facilityId == null) {
        facilityId = (String) session.getAttribute("facilityId");
        if (facilityId == null) {
          facilityId = "";
        }
      }
      if (message == null) {
        message = (String) session.getAttribute("message");
        if (message == null) {
          message = "";
        }
      }
      session.setAttribute("userId", userId);
      session.setAttribute("facilityId", facilityId);
      session.setAttribute("password", password);
      session.setAttribute("id", id);
      session.setAttribute("message", message);
      PrintWriter out = new PrintWriter(response.getWriter());
      response.setContentType("text/html;charset=UTF-8");
      printHtmlHead(out, MENU_HEADER_SEND, request);
      printForm(id, connectors, message, out);
      String responseText = null;
      if (id != 0) {
        try {
          Connector connector = getConnector(id, session);
          responseText = (String) request.getAttribute("responseText");
          AckAnalyzer ackAnalyzer = null;
          if (responseText != null) {
            String title = "Response Received";
            HL7Reader ackMessageReader = new HL7Reader(responseText);
            if (ackMessageReader.advanceToSegment("MSH")) {
              String messageType = ackMessageReader.getValue(9);
              if (messageType.equals("RSP") || messageType.equals("VXR") || messageType.equals("VXX")) {
                title = "Query Response Received";
              } else {
                ackAnalyzer = new AckAnalyzer(responseText, connector.getAckType());
                if (ackAnalyzer.isPositive()) {
                  title = "Message Accepted";
                } else {
                  title = "Message Rejected";
                }
              }
            }
            out.println("<h3>" + title + "</h3>");
            out.print("<pre>");
            out.print(responseText.replace("<", "&lt;").replace(">", "&gt;"));
            out.println("</pre>");
          }
          String requestText = (String) request.getAttribute("requestText");
          if (responseText != null) {
            out.println("<p>What was actually sent to " + connector.getLabel() + ": ");
            out.print("<pre>");
            out.print(requestText);
            out.println("</pre>");
          }

          String host = "";
          try {
            InetAddress addr = InetAddress.getLocalHost();
            host = addr.getHostName();
          } catch (UnknownHostException e) {
            host = "[unknown]";
          }
          try {
            out.println("<p>Status for " + connector.getLabel() + ": <br><font color=\"blue\">"
                + connector.connectivityTest("Sent from client '" + host + "'") + "</font></p>");
          } catch (Exception t) {
            out.println("<p>Unable to test against remote server: " + t.getMessage() + "</p>");
            out.println("<pre>");
            t.printStackTrace(out);
            out.println("</pre>");
          }
        } catch (Throwable re) {
          re.printStackTrace(out);
        }
      }

      if (message != null) {
        if (message.indexOf("|VXU^") > 0) {
          String qbpMessage = QueryConverter.convertVXUtoQBP(message);
          session.setAttribute(CompareServlet.VXU_MESSAGE, message);
          out.println("<p>Submit QBP query message based from VXU displayed above</p>");
          printForm(id, connectors, qbpMessage, out);
          String vxqMessage = QueryConverter.convertVXUtoVXQ(message);
          out.println("<p>Submit VXQ query message based from VXU displayed above</p>");
          printForm(id, connectors, vxqMessage, out);
        }
      }
      if (responseText != null && responseText.indexOf("|RSP^") > 0) {
        session.setAttribute(CompareServlet.RSP_MESSAGE, responseText);
        if (session.getAttribute(CompareServlet.VXU_MESSAGE) != null) {
          out.println("<p><a href=\"CompareServlet\">Compare response with original VXU</a></p>");
        }
      }

      boolean showWSDL = request.getParameter("showWSDL") != null;
      if (showWSDL && id != 0) {
        out.println("<h3>WSDL</h3>");
        out.println("<pre>");
        Connector connector = getConnector(id, session);
        try {
          HttpURLConnection urlConn;
          InputStreamReader input = null;
          URL url = new URL(connector.getUrl());

          urlConn = (HttpURLConnection) url.openConnection();
          urlConn.setRequestMethod("GET");
          urlConn.setDoInput(true);
          urlConn.setUseCaches(false);

          input = new InputStreamReader(urlConn.getInputStream());
          BufferedReader in = new BufferedReader(input);
          boolean escape = !urlConn.getContentType().startsWith("text/html");
          String line;
          while ((line = in.readLine()) != null) {
            if (escape) {
              out.println(escapeHTML(line));
            } else {
              out.println(line);
            }
          }
          input.close();
        } catch (IOException e) {
          e.printStackTrace(out);
        }
        out.println("</pre>");
      }

      out.println("  <div class=\"help\">");
      out.println("  <h2>How To Use This Page</h2>");
      out.println("  <p>This page supports a simple test of the connectivity to another system. "
          + "The login parameters (username, password, and facility id) must be obtained "
          + "from the system you wish to test against. Once you have the login parameters "
          + "you should select the appropriate service and then paste a test message. "
          + "After clicking Submit you will see the results of your query. </p>");
      out.println("<p>If you wish to only ping the server, then you only need to select the service "
          + "and then click Refresh. This will give the results of the ping below. </p>");
      out.println("  </div>");
      // testTestCaseMessage(out);
      printHtmlFoot(out);
      out.close();
    }
  }

  private void printForm(int id, List<Connector> connectors, String message, PrintWriter out) {
    out.println("    <form action=\"SubmitServlet\" method=\"POST\">");
    out.println("      <table border=\"0\">");
    out.println("        <tr>");
    out.println("          <td>Connection</td>");
    out.println("          <td>");
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
          out.println("              <option value=\"" + i + "\" selected=\"true\">" + connector.getLabelDisplay()
              + "</option>");
        } else {
          out.println("              <option value=\"" + i + "\">" + connector.getLabelDisplay() + "</option>");
        }
      }
      out.println("            </select>");
    }
    out.println("          </td>");
    out.println("        </tr>");
    out.println("        <tr>");
    out.println("          <td valign=\"top\">Message</td>");
    out.println("          <td><textarea name=\"message\" cols=\"70\" rows=\"10\" wrap=\"off\">" + message
        + "</textarea></td>");
    out.println("        </tr>");
    out.println("        <tr>");
    out.println("          <td>Transform</td>");
    out.println("          <td><input type=\"checkbox\" name=\"transform\" value=\"true\" checked=\"true\"/> <em>Apply connection specific transforms to message before sending.</em></td>");
    out.println("        </tr>");
    out.println("        <tr>");
    out.println("          <td>Debug</td>");
    out.println("          <td><input type=\"checkbox\" name=\"debug\" value=\"true\" /></td>");
    out.println("        </tr>");
    out.println("        <tr>");
    out.println("          <td>Show WSDL</td>");
    out.println("          <td><input type=\"checkbox\" name=\"showWSDL\" value=\"true\" /></td>");
    out.println("        </tr>");
    out.println("        <tr>");
    out.println("          <td colspan=\"2\" align=\"right\">");
    out.println("            <input type=\"submit\" name=\"method\" value=\"Refresh\"/>");
    out.println("            <input type=\"submit\" name=\"method\" value=\"Submit\"/>");
    out.println("          </td>");
    out.println("        </tr>");
    out.println("      </table>");
    out.println("    </form>");
  }

  protected void testTestCaseMessage(PrintWriter out) {
    TestCaseMessage tcm = new TestCaseMessage();
    tcm.setAssertResultStatus("Accept");
    tcm.setAssertResultText("Way good!");
    tcm.setComment("NAB", "Okay");
    tcm.setCustomTransformations("PID-4=HAPPY\nPID-5=SAD\n");
    tcm.setDescription("This is a description");
    tcm.setExpectedResult("This is an expected result text");
    tcm.setMessageText("MSH|\rPID|1|\rRXA|1|\rRXA|2\r");
    tcm.setOriginalMessage("MSH\rPID\rRXA\rRXR\r");
    tcm.setQuickTransformations(new String[] { "2.5.1", "BOY" });
    tcm.setTestCaseNumber("1.1");
    tcm.setTestCaseSet("CDC");
    out.print("<pre>");
    String text = tcm.createText();
    out.print(text);
    try {
      List<TestCaseMessage> tcmList = TestCaseMessageManager.createTestCaseMessageList(text);
      for (TestCaseMessage tcmIt : tcmList) {
        out.print(tcmIt.createText());
      }
    } catch (Exception e) {
      e.printStackTrace(out);
    }
    out.println("</pre>");
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
    try {
      processRequest(request, response);
    } catch (Exception e) {
      request.setAttribute("responseText", e.getMessage());
    }
    doGet(request, response);
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

  private static String cleanMessage(String message) {
    if (message != null) {
      StringBuilder sb = new StringBuilder();
      BufferedReader reader = new BufferedReader(new StringReader(message));
      String line;
      try {
        while ((line = reader.readLine()) != null) {
          sb.append(line);
          sb.append("\r");
        }
      } catch (IOException ioe) {
        sb.append(ioe.getMessage());
      }
      return sb.toString();
    }

    return message;

  }

  private static String escapeHTML(String line) {
    return line.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;");
  }
}
