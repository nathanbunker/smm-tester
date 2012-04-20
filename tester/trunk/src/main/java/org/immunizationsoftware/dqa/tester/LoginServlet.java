/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author nathan
 */
public class LoginServlet extends ClientServlet
{

  private static String DEFAULT_CONNECTION_SCRIPT = "Connection\n" + "Label: OIS DQA\n" + "Type: SOAP\n"
      + "URL: http://openimmunizationsoftware.us:6394/ImmMessagingServer/client_Service?wsdl\n" + "User Id: CDCTLWG\n" + "Password: H3490\n"
      + "Facility Id: 1200\n";

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
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    HttpSession session = request.getSession(true);
    String action = request.getParameter("action");
    boolean loginSuccess = false;
    List<TestCaseMessage> oldTestCaseMessageList = null;
    if (action != null)
    {
      if (action.equals("Login"))
      {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String message = null;
        if (username != null && username.length() > 0 && password != null && password.length() > 0)
        {
          if (Authenticate.isValid(username, password))
          {
            session.setAttribute("username", username);
            loginSuccess = true;
            Authenticate.User user = Authenticate.getUser(username);
            if (!user.getName().equals(""))
            {
              message = "Welcome " + user.getName();
            } else
            {
              message = "Welcome " + user.getUsername();
            }
            try
            {
              StringBuilder sb = new StringBuilder();
              BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("defaultConnections.txt")));
              String line;
              while ((line = in.readLine()) != null)
              {
                sb.append(line);
                sb.append("\n");
              }
              SetupServlet.addConnectors(sb.toString(), session);
            } catch (Exception e)
            {
              message = "Unable to create default connections: " + e.getMessage();
            }
          } else
          {
            message = "Invalid username or password";
          }
        } else
        {
          message = "Username and password required";
        }
        if (message != null)
        {
          request.setAttribute("message", message);
        }
      } else if (action.equals("Logout"))
      {

        Map<String, TestCaseMessage> testCaseMessageMap = CreateTestCaseServlet.getTestCaseMessageMap(session);
        oldTestCaseMessageList = new ArrayList<TestCaseMessage>(testCaseMessageMap.values());
        TestCaseServlet.sortTestCaseMessageList(oldTestCaseMessageList);
        session.invalidate();
        session = request.getSession(true);
      }
    }
    String username = (String) session.getAttribute("username");
    if (!loginSuccess)
    {
      username = request.getParameter("username");
      if (username == null)
      {
        username = "";
      }
      String password = request.getParameter("password");
      if (password == null)
      {
        password = "";
      }
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();
      try
      {
        printHtmlHead(out, "Login", request);
        out.println("<p>&nbsp;</p>");
        out.println("<form action=\"LoginServlet\" method=\"POST\">");
        out.println("<table>");
        out.println("  <tr>");
        out.println("    <td>Username</td>");
        out.println("    <td><input type=\"text\" name=\"username\" value=\"" + username + "\"></td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td>Password</td>");
        out.println("    <td><input type=\"text\" name=\"password\" value=\"" + password + "\"></td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td colspan=\"2\" align=\"right\"><input type=\"submit\" name=\"action\" value=\"Login\"></td>");
        out.println("  </tr>");
        out.println("</table>");
        out.println("<p>&nbsp;</p>");

        if (oldTestCaseMessageList != null && !oldTestCaseMessageList.isEmpty())
        {
          out.println("<h3>Test Cases Used in Previous Session</h3>");
          out.println("<pre>");
          for (TestCaseMessage testCaseMessage : oldTestCaseMessageList)
          {
            out.println(testCaseMessage.createText());
            out.println();
          }
          out.println("</pre>");
        }
        out.println("</form>");
        printHtmlFoot(out);
      } finally
      {
        out.close();
      }
    } else
    {
      RequestDispatcher dispatcher = request.getRequestDispatcher("SetupServlet");
      dispatcher.forward(request, response);
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
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
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo()
  {
    return "Short description";
  }// </editor-fold>
}
