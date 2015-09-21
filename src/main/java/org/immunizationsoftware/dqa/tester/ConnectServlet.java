package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.HISoapConnector;
import org.immunizationsoftware.dqa.tester.connectors.HttpConnector;
import org.immunizationsoftware.dqa.tester.connectors.NMSoapConnector;
import org.immunizationsoftware.dqa.tester.connectors.SoapConnector;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

/**
 * 
 * @author nathan
 */
public class ConnectServlet extends ClientServlet
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
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null)
    {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else
    {
      Authenticate.User user = (Authenticate.User) session.getAttribute("user");
      String testScript = request.getParameter("testScript");
      List<TestCaseMessage> selectedTestCaseMessageList = null;
      if (testScript == null)
      {
        selectedTestCaseMessageList = getSelectedTestCaseMessageList(request, session);
        if (selectedTestCaseMessageList != null)
        {
          TestCaseServlet.sortTestCaseMessageList(selectedTestCaseMessageList);
          session.setAttribute("selectedTestCaseMessageList", selectedTestCaseMessageList);
        }
      }

      String action = request.getParameter("action");
      String message = null;
      if (action != null)
      {
        if (action.equals("Delete"))
        {
          deleteConnection(request, session);
        } else if (action.equals("Load Connections"))
        {
          runConnectionScript(request, session);
        } else if (action.equals("Add"))
        {
          addConnection(request, session);
        } else if (action.equals("Switch"))
        {
          int internalId = Integer.parseInt(request.getParameter("sendDataInternalId"));
          message = addNewConnection(session, user, message, internalId, false);
        }
      }
      if (message != null)
      {
        request.setAttribute("message", message);
      }

      PrintWriter out = response.getWriter();
      try
      {
        printHtmlHead(out, MENU_HEADER_CONNECT, request);
        if (user.isAdmin())
        {
          out.println("<h2>Select Account</h2>");
          out.println("<form action=\"ConnectServlet\" method=\"POST\">");
          out.println("<table border=\"0\">");
          out.println("  <tr>");
          out.println("    <td valign=\"top\">Account</td>");
          out.println("    <td>");
          out.println("      <select name=\"sendDataInternalId\" >");
          boolean selected = !user.hasSendData();
          out.println("              <option value=\"0\"" + (selected ? " selected=\"true\"" : "") + ">-- none selected --</option>");
          for (SendData sendData : ManagerServlet.getSendDataList())
          {
            if (sendData.getConnector() != null)
            {
              selected = user.hasSendData() && user.getSendData().getInternalId() == sendData.getInternalId();
              out.println("              <option value=\"" + sendData.getInternalId() + "\"" + (selected ? " selected=\"true\"" : "") + ">"
                  + sendData.getConnector().getLabel() + "</option>");
            }
          }
          out.println("      </select>");
          out.println("    </td>");
          out.println("    <td align=\"right\">");
          out.println("      <input type=\"submit\" name=\"action\" value=\"Switch\">");
          out.println("    </td>");
          out.println("  </tr>");
          out.println("</table>");
          out.println("</form>");
        }
        
        out.println("<h2>Connections</h2>");
        out.println("<table class=\"boxed\">");
        out.println("  <tr class=\"boxed\">");
        out.println("    <th class=\"boxed\">Label</th>");
        out.println("    <th class=\"boxed\">Type</th>");
        out.println("    <th class=\"boxed\">URL</th>");
        out.println("    <th class=\"boxed\">User Id</th>");
        out.println("    <th class=\"boxed\">Password</th>");
        out.println("    <th class=\"boxed\">Facility Id</th>");
        out.println("    <th class=\"boxed\">&nbsp;</th>");
        out.println("  </tr>");
        List<Connector> connectors = getConnectors(session);
        int id = 0;
        for (Connector connector : connectors)
        {
          id++;
          out.println("<form action=\"ConnectServlet\" method=\"POST\">");
          out.println("  <tr class=\"boxed\">");
          out.println("    <td class=\"boxed\">" + connector.getLabel() + "</td>");
          out.println("    <td class=\"boxed\">" + connector.getType() + "</td>");
          out.println("    <td class=\"boxed\">" + connector.getUrlShort() + "</td>");
          out.println("    <td class=\"boxed\">" + connector.getUserid() + "</td>");
          out.println("    <td class=\"boxed\">&nbsp;</td>");
          out.println("    <td class=\"boxed\">" + connector.getFacilityid() + "</td>");
          out.println("    <td class=\"boxed\"><input type=\"submit\" name=\"action\" value=\"Delete\"></td>");
          out.println("  </tr>");
          out.println("  <input type=\"hidden\" name=\"id\" value=\"" + id + "\">");
          out.println("</form>");
        }
        String label = request.getParameter("label");
        if (label == null)
        {
          label = "";
        }
        String type = request.getParameter("type");
        if (type == null)
        {
          type = "";
        }
        String url = request.getParameter("url");
        if (url == null)
        {
          url = "";
        }
        String userid = request.getParameter("userid");
        if (userid == null)
        {
          userid = "";
        }
        String password = "";
        String facilityid = request.getParameter("facilityid");
        if (facilityid == null)
        {
          facilityid = "";
        }
        out.println("<form action=\"ConnectServlet\" method=\"POST\">");
        out.println("  <tr class=\"boxed\">");
        out.println("    <td class=\"boxed\"><input type=\"text\" name=\"label\" size=\"12\" value=\"" + label + "\"></td>");
        out.println("    <td class=\"boxed\">");
        out.println("      <select name=\"type\">");
        out.println("        <option value=\"\">select</option>");
        for (String[] option : ConnectorFactory.TYPES)
        {
          out.println("        <option value=\"" + option[0] + "\"" + (type.equals(option[0]) ? " selected=\"true\"" : "") + ">" + option[1]
              + "</option>");
        }
        out.println("      </select>");
        out.println("    </td>");
        out.println("    <td class=\"boxed\"><input type=\"text\" name=\"url\" size=\"20\" value=\"" + url + "\"></td>");
        out.println("    <td class=\"boxed\"><input type=\"text\" name=\"userid\" size=\"7\" value=\"" + userid + "\"></td>");
        out.println("    <td class=\"boxed\"><input type=\"text\" name=\"password\" size=\"7\" value=\"" + password + "\"></td>");
        out.println("    <td class=\"boxed\"><input type=\"text\" name=\"facilityid\" size=\"7\" value=\"" + facilityid + "\"></td>");
        out.println("    <td class=\"boxed\"><input type=\"submit\" name=\"action\" value=\"Add\"></td>");
        out.println("  </tr>");
        out.println("  <input type=\"hidden\" name=\"id\" value=\"" + id + "\">");
        out.println("</form>");
        String connectorScript = request.getParameter("connectorScript");
        if (connectorScript == null)
        {
          connectorScript = (String) request.getAttribute("connectorScript");
          if (connectorScript == null)
          {
            connectorScript = "";
          }
        }
        out.println("</table>");
        out.println("<form action=\"ConnectServlet\" method=\"POST\">");
        out.println("<table border=\"0\">");
        out.println("  <tr>");
        out.println("    <td valign=\"top\">Script</td>");
        out.println("    <td>");
        out.println("        <textarea name=\"connectorScript\" cols=\"60\" rows=\"7\" wrap=\"off\">" + connectorScript + "</textarea>");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td colspan=\"2\" align=\"right\">");
        out.println("        <input type=\"submit\" name=\"action\" value=\"Load Connections\">");
        out.println("    </td>");
        out.println("  </tr>");
        out.println("</table>");
        out.println("</form>");

        printHtmlFoot(out);

      } finally
      {
        out.close();
      }
    }
  }

  public static String addNewConnection(HttpSession session, Authenticate.User user, String message, int internalId, boolean removeOtherConnections) {
    CreateTestCaseServlet.getTestCaseMessageMap(session).clear();
    if (internalId == 0)
    {
      user.setSendData(null);
    } else
    {
      SendData sendData = ManagerServlet.getSendData(internalId);
      user.setSendData(sendData);
      if (removeOtherConnections)
      {
        getConnectors(session).clear();
      }
      ConnectServlet.addConnector(sendData.getConnector(), session);
      try
      {
        CreateTestCaseServlet.loadTestCases(session);
      } catch (Exception e)
      {
        e.printStackTrace();
        message = "Unable to load test cases: " + e.getMessage();
      }
    }
    return message;
  }

  protected static List<TestCaseMessage> getSelectedTestCaseMessageList(HttpServletRequest request, HttpSession session)
  {
    List<TestCaseMessage> testCaseMessageList = new ArrayList<TestCaseMessage>();
    Set<String> testCaseNumberSelectedSet = TestCaseServlet.setTestCaseNumberSelectedSet(request, session);
    Map<String, TestCaseMessage> testCaseMessageMap = CreateTestCaseServlet.getTestCaseMessageMap(session);
    for (String testCaseNumber : testCaseNumberSelectedSet)
    {
      TestCaseMessage tcm = testCaseMessageMap.get(testCaseNumber);
      if (testCaseNumberSelectedSet.contains(tcm.getTestCaseNumber()))
      {
        testCaseMessageList.add(tcm);
      }
    }
    return testCaseMessageList;
  }

  protected void addConnection(HttpServletRequest request, HttpSession session)
  {
    String label = request.getParameter("label");
    if (label == null)
    {
      label = "";
    }
    String type = request.getParameter("type");
    if (type == null)
    {
      type = "";
    }
    String url = request.getParameter("url");
    if (url == null)
    {
      url = "";
    }
    String userid = request.getParameter("userid");
    if (userid == null)
    {
      userid = "";
    }
    String password = request.getParameter("password");
    if (password == null)
    {
      password = null;
    }
    String facilityid = request.getParameter("facilityid");
    if (facilityid == null)
    {
      facilityid = null;
    }
    String message = "Creating connection";
    if (type.equals(""))
    {
      message = "Type must be specified to add new connection";
    } else if (label.equals(""))
    {
      message = "Label must be specified to add new connection";
    }
    if (message != null)
    {
      Connector connector = null;
      try
      {
        connector = ConnectorFactory.getConnector(type, label, url);
        if (connector == null)
        {
          message = "Unable to find connection type";
        } else
        {
          connector.setUserid(userid);
          connector.setPassword(password);
          connector.setFacilityid(facilityid);
          request.setAttribute("connectorScript", connector.getScript());
          List<Connector> connectors = getConnectors(session);
          connectors.add(connector);
          message = "Added new connection";
        }
      } catch (Exception e)
      {
        message = "Unable to create connector: " + e.getMessage();
        e.printStackTrace();
      }
    }
    if (message != null)
    {
      request.setAttribute("message", message);
    }
  }

  protected void runConnectionScript(HttpServletRequest request, HttpSession session)
  {
    try
    {
      String connectorScript = request.getParameter("connectorScript");
      addConnectors(connectorScript, session);
    } catch (Exception e)
    {
      String message = "Unable to run script, exception ocurred: " + e.getMessage();
      request.setAttribute("message", message);
    }
  }

  protected void deleteConnection(HttpServletRequest request, HttpSession session) throws NumberFormatException
  {
    int id = Integer.parseInt(request.getParameter("id"));
    List<Connector> connectors = getConnectors(session);
    Connector removedConnector = connectors.remove(id - 1);
    request.setAttribute("connectorScript", removedConnector.getScript());
  }

  protected static void addConnectors(String connectorScript, HttpSession session) throws Exception
  {
    List<Connector> newConnectors = Connector.makeConnectors(connectorScript);
    List<Connector> oldConnectors = getConnectors(session);
    for (Connector newConnector : newConnectors)
    {
      for (Iterator<Connector> it = oldConnectors.iterator(); it.hasNext();)
      {
        Connector oldConnector = it.next();
        if (oldConnector.getLabel().equals(newConnector.getLabel()) && oldConnector.getType().equals(newConnector.getType()))
        {
          it.remove();
        }
      }
    }
    oldConnectors.addAll(newConnectors);
  }

  protected static void addConnector(Connector newConnector, HttpSession session)
  {
    List<Connector> oldConnectors = getConnectors(session);
    for (Iterator<Connector> it = oldConnectors.iterator(); it.hasNext();)
    {
      Connector oldConnector = it.next();
      if (oldConnector.getLabel().equals(newConnector.getLabel()) && oldConnector.getType().equals(newConnector.getType()))
      {
        it.remove();
      }
    }
    oldConnectors.add(newConnector);
    if (session.getAttribute("id") == null)
    {
      session.setAttribute("id", oldConnectors.size());
    }
  }

  public static List<Connector> getConnectors(HttpSession session)
  {
    List<Connector> connectors = (List<Connector>) session.getAttribute("connectors");
    if (connectors == null)
    {
      connectors = new ArrayList<Connector>();
      session.setAttribute("connectors", connectors);
    }
    return connectors;
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