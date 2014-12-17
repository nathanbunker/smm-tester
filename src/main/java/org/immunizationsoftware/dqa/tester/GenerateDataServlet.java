/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import static org.immunizationsoftware.dqa.tester.manager.ScenarioManager.SCENARIO_MCIR_MPI;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.Transformer.PatientType;
import org.immunizationsoftware.dqa.tester.manager.QueryConverter;
import org.immunizationsoftware.dqa.tester.manager.ScenarioManager;

/**
 * 
 * @author nathan
 */
public class GenerateDataServlet extends ClientServlet
{

  /**
   * 
   */
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
    String username = (String) session.getAttribute("username");
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");
    String action = request.getParameter("action");
    String problem = null;
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    }
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

      Transformer transformer = new Transformer();

      List<TestCaseMessage> testCaseMessageList = new ArrayList<TestCaseMessage>();
      for (int i = 0; i < 200; i++) {
        TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_MCIR_MPI);
        // testCaseMessage.setDescription("Sex is " + certifyItem.getLabel());
        // testCaseMessage.setTestCaseSet(testCaseSet);
        // testCaseMessage.setTestCaseNumber(uniqueMRNBase + "B" +
        // makeTwoDigits(masterCount) + "." + count);
        // testCaseMessage.appendCustomTransformation("PID-8=" +
        // certifyItem.getCode());
        testCaseMessage.appendCustomTransformation("PID-3#2=[SSN]");
        testCaseMessage.appendCustomTransformation("PID-3.4#2=US");
        testCaseMessage.appendCustomTransformation("PID-3.5#2=SS");
        testCaseMessage.appendCustomTransformation("PID-3#3=[MEDICAID]");
        testCaseMessage.appendCustomTransformation("PID-3.4#3=MI");
        testCaseMessage.appendCustomTransformation("PID-3.5#3=MA");
        testCaseMessage.appendCustomTransformation("PID-3#4=[WIC]");
        testCaseMessage.appendCustomTransformation("PID-3.4#4=MI");
        testCaseMessage.appendCustomTransformation("PID-3.5#4=WC");
        transformer.transform(testCaseMessage);
        testCaseMessageList.add(testCaseMessage);
      }
      out.println("<h2>Sample Data</h2>");
      out.println("<pre>");

      for (TestCaseMessage testCaseMessage : testCaseMessageList) {
        out.print(testCaseMessage.getMessageText());
      }
      out.println("</pre>");

      out.println("<h2>Same Data Changed</h2>");
      out.println("<pre>");

      Random random = new Random();

      for (TestCaseMessage testCaseMessage : testCaseMessageList) {
        TestCaseMessage testCaseMessageChange = new TestCaseMessage();
        testCaseMessageChange.setPatientType(PatientType.TODDLER);
        testCaseMessageChange.setOriginalMessage(testCaseMessage.getMessageText());
        int numberOfChanges = random.nextInt(2) + 1;
        String changeMade = "";
        for (int change = 0; change < numberOfChanges; change++) {
          int changeToMake = random.nextInt(15);
          switch (changeToMake) {
          case 0:
            testCaseMessageChange.appendCustomTransformation("PID-5=[LAST]");
            changeMade = changeMade + "NEW last name ";
            break;
          case 1:
            testCaseMessageChange.appendCustomTransformation("PID-5.2=[BOY_OR_GIRL]");
            changeMade = changeMade + "NEW first name ";
            break;
          case 2:
            testCaseMessageChange
                .appendCustomTransformation("PID-5.3=~60%[BOY_OR_GIRL_MIDDLE]:~70%[GIRL_MIDDLE_INITIAL]");
            changeMade = changeMade + "NEW middle name or initial ";
            break;
          case 3:
            testCaseMessageChange.appendCustomTransformation("PID-5.4=[SUFFIX]");
            changeMade = changeMade + "NEW suffix ";
            break;
          case 4:
            testCaseMessageChange.appendCustomTransformation("PID-7=[DOB]");
            changeMade = changeMade + "NEW date of birth ";
            break;
          case 5:
            testCaseMessageChange.appendCustomTransformation("PID-13.6=[PHONE_AREA]");
            changeMade = changeMade + "NEW phone area code ";
            break;
          case 6:
            testCaseMessageChange.appendCustomTransformation("PID-13.7=[PHONE_LOCAL]");
            changeMade = changeMade + "NEW phone local number ";
            break;
          case 7:
            testCaseMessage.appendCustomTransformation("PID-3#2=[SSN]");
            testCaseMessage.appendCustomTransformation("PID-3.4#2=US");
            testCaseMessage.appendCustomTransformation("PID-3.5#2=SS");
            changeMade = changeMade + "NEW ssn ";
            break;
          case 8:
            testCaseMessageChange.appendCustomTransformation("PID-8=[GENDER]");
            changeMade = changeMade + "NEW sex ";
            break;
          case 9:
            testCaseMessageChange.appendCustomTransformation("PID-11.1=[STREET]");
            changeMade = changeMade + "NEW address line 1 ";
            break;
          case 10:
            testCaseMessageChange.appendCustomTransformation("PID-11.3=[CITY]");
            changeMade = changeMade + "NEW address city ";
            break;
          case 11:
            testCaseMessageChange.appendCustomTransformation("PID-11.4=WI");
            changeMade = changeMade + "WRONG address state ";
            break;
          case 12:
            testCaseMessageChange.appendCustomTransformation("PID-11.5=[ZIP]");
            changeMade = changeMade + "NEW address zip ";
            break;
          case 13:
            testCaseMessage.appendCustomTransformation("PID-3#3=[MEDICAID]");
            testCaseMessage.appendCustomTransformation("PID-3.4#3=MI");
            testCaseMessage.appendCustomTransformation("PID-3.5#3=MA");
            changeMade = changeMade + "NEW medicaid ";
            break;
          case 14:
            testCaseMessage.appendCustomTransformation("PID-3#4=[WIC]");
            testCaseMessage.appendCustomTransformation("PID-3.4#4=MI");
            testCaseMessage.appendCustomTransformation("PID-3.5#4=WC");
            changeMade = changeMade + "NEW wic ";
            break;
          }
        }
        testCaseMessageChange.appendCustomTransformation("MSH-6=" + changeMade);
        transformer.transform(testCaseMessageChange);
        out.print(testCaseMessageChange.getMessageText());
      }
      out.println("</pre>");

      out.println("<h2>QBP's for Sample Data</h2>");
      out.println("<pre>");

      for (TestCaseMessage testCaseMessage : testCaseMessageList) {
        String qbpMessage = QueryConverter.convertVXUtoQBP(testCaseMessage.getMessageText());
        out.print(qbpMessage);
      }
      out.println("</pre>");


      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

}
