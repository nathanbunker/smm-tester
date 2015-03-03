/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immunizationsoftware.dqa.transform.PatientType;
import org.immunizationsoftware.dqa.transform.ScenarioManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;

/**
 * 
 * @author nathan
 */
public class ModifyMessageServlet extends ClientServlet
{

  private static final String COMMAND_ADD_QUICK_TRANSFORMS = "QUICK TRANSFORMS";
  private static final String COMMAND_SET_PATIENT_TYPE = "PATIENT TYPE";
  private static final String COMMAND_SET_CONTEXT = "CONTEXT ";
  private static final String COMMAND_SELECT_SCENARIO = "SCENARIO ";
  private static final String COMMAND_ADD = "ADD ";
  private static final String COMMAND_SET = "SET ";
  private static final String COMMAND_SELECT = "SELECT ";
  public static final String IIS_TEST_REPORT_FILENAME_PREFIX = "IIS Test Report";

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
  protected void processRequest(HttpServletRequest request, HttpServletResponse response, String finalMessage)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();

    String script = request.getParameter("script");
    if (script == null) {
      script = "";
    }

    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);
      out.println("    <form action=\"ModifyMessageServlet\" method=\"POST\">");
      out.println("      <table>");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">Start Message</td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td colspan=\"2\"><textarea name=\"script\" cols=\"120\" rows=\"30\" wrap=\"off\">"
          + script + "</textarea></td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td align=\"right\">");
      out.println("            <input type=\"submit\" name=\"action\" value=\"Run\"/>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("      </table>");
      out.println("    </form>");
      if (finalMessage != null) {
        out.println("<h2>Final Message</h2>");
        out.println("<pre>");
        out.print(finalMessage);
        out.println("</pre>");
      }
      ClientServlet.printHtmlFoot(out);
    } catch (Exception e) {
      out.println("<p>Exception Occurred: " + e.getMessage() + "</p><pre>");
      e.printStackTrace(out);
      out.println("</pre>");
    } finally {
      out.close();
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
    processRequest(request, response, null);

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

    String script = request.getParameter("script");
    if (script != null) {
      Transformer transformer = new Transformer();
      TestCaseMessage testCaseMessage = new TestCaseMessage();
      StringBuilder originalMessage = new StringBuilder();
      BufferedReader in = new BufferedReader(new StringReader(script));
      String line;
      while ((line = in.readLine()) != null) {
        line = line.trim();
        String lineUpper = line.toUpperCase();
        if (line.length() > 3 && line.charAt(3) == '|') {
          originalMessage.append(line);
          originalMessage.append("\r");
        } else if (lineUpper.startsWith(COMMAND_SELECT)) {
          line = line.substring(COMMAND_SELECT.length()).trim();
          lineUpper = line.toUpperCase();
          if (lineUpper.startsWith(COMMAND_SELECT_SCENARIO)) {
            String scenario = line.substring(COMMAND_SELECT_SCENARIO.length()).trim();
            if (!scenario.equals("")) {
              testCaseMessage = ScenarioManager.createTestCaseMessage(scenario);
            }
          }
        } else if (lineUpper.startsWith(COMMAND_SET)) {
          line = line.substring(COMMAND_SET.length()).trim();
          lineUpper = line.toUpperCase();
          if (lineUpper.startsWith(COMMAND_SET_CONTEXT)) {
            // ignore for now, immunization by default
          } else if (lineUpper.startsWith(COMMAND_SET_PATIENT_TYPE)) {
            PatientType patientType = PatientType.valueOf(lineUpper.substring(
                COMMAND_SET_PATIENT_TYPE.length()).trim());
            testCaseMessage.setPatientType(patientType);
          }
        } else if (lineUpper.startsWith(COMMAND_ADD)) {
          line = line.substring(COMMAND_ADD.length()).trim();
          lineUpper = line.toUpperCase();
          if (lineUpper.startsWith(COMMAND_ADD_QUICK_TRANSFORMS)) {
            String[] qt;
            String[] qtOriginal = testCaseMessage.getQuickTransformations();
            if (qtOriginal == null) {
              qt = new String[1];
            } else {
              qt = Arrays.copyOf(qtOriginal, qtOriginal.length + 1);
            }
            qt[qt.length - 1] = lineUpper.substring(COMMAND_ADD_QUICK_TRANSFORMS.length()).trim();
            testCaseMessage.setQuickTransformations(qt);
          }
        } else {
          testCaseMessage.appendCustomTransformation(line);
        }
      }
      in.close();
      if (originalMessage.length() > 0) {
        testCaseMessage.setOriginalMessage(originalMessage.toString());
      }
      transformer.transform(testCaseMessage);
      String finalMessage = testCaseMessage.getMessageText();
      processRequest(request, response, finalMessage);
      // response.setContentType("text/plain;charset=UTF-8");
      // PrintWriter out = response.getWriter();
      // out.println(finalMessage);
      // out.close();
      // in = new BufferedReader(new StringReader(finalMessage));
      // while ((line = in.readLine()) != null) {
      // out.println("-- " + line);
      // }
      // in.close();
    } else {
      processRequest(request, response, null);
    }

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
