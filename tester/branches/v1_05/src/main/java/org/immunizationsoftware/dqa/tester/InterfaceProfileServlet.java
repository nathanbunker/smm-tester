/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.transform.IssueCreatorItems;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author nathan
 */
public class InterfaceProfileServlet extends ClientServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void init() throws ServletException
  {
    super.init();

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
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");
    if (username == null)
    {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else
    {
      PrintWriter out = response.getWriter();
      try
      {
        printHtmlHead(out, "Interface Profile", request);
        int id = 0;
        List<Connector> connectors = SetupServlet.getConnectors(session);
        if (connectors.size() == 1)
        {
          id = 1;
        } else
        {
          if (request.getParameter("id") != null && request.getParameter("id").length() > 0)
          {
            id = Integer.parseInt(request.getParameter("id"));
          }
        }
        session.setAttribute("id", id);
        boolean goodToQuery = true;
        Connector connector = null;
        if (id > 0)
        {
          connector = SubmitServlet.getConnector(id, session);
        } else
        {
          out.println("<p>No connection specified, not running interface profile.</p>");
        }

        String testCase = request.getParameter("source");
        if (testCase == null || testCase.trim().length() == 0)
        {
          testCase = null;
        }
        Map<String, String> expectedStatusMap = null;
        String expectedText = request.getParameter("expected");
        if (expectedText != null && expectedText.length() > 0)
        {
          expectedStatusMap = new HashMap<String, String>();
          try
          {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(expectedText.getBytes()));
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getChildNodes();
            if (nodes.getLength() == 1)
            {
              String imbeddedTestCase = readSubParts(expectedStatusMap, nodes.item(0).getChildNodes());
              if (testCase == null)
              {
                testCase = imbeddedTestCase;
              }
            }
            if (expectedStatusMap.size() == 0)
            {
              throw new Exception("No expected results found, unable to profile interface");
            }
            goodToQuery = true;
          } catch (Exception e)
          {
            goodToQuery = false;
            out.println("Unable to read DQA Report Template XML: ");
            out.print("<pre>");
            e.printStackTrace(out);
            out.print("</pre>");
          }

        }
        
        int batchSize = 0;
        String batchSizeString = request.getParameter("batchSize");
        if (batchSizeString != null && !batchSizeString.equals(""))
        {
          try
          {
            batchSize = Integer.parseInt(batchSizeString);
          }
          catch (NumberFormatException nfe)
          {
batchSize = 0;
          }
        }

        List<TestCaseMessage> testCaseMessageList = null;
        if (testCase == null || testCase.equals(""))
        {
          goodToQuery = false;
          out.println("Test case message not set, unable to run test. ");
        } else
        {
          testCaseMessageList = parseAndAddTestCases(testCase, session);
        }

        TestRunner testRunner = new TestRunner();
        TestCaseMessage testCaseMessageBase = null;
        if (goodToQuery && testCaseMessageList.size() > 0)
        {
          testCaseMessageBase = testCaseMessageList.get(0);
          if (connector != null)
          {
            try
            {
              try
              {

                testRunner.runTest(connector, testCaseMessageBase);
              } catch (Throwable t)
              {
                t.printStackTrace(out);
              }

            } catch (Exception e)
            {
              out.println("Unable to run test: " + e.getMessage());
              out.print("<pre>");
              e.printStackTrace(out);
              out.println("</pre>");
            }
            if (testRunner.getStatus().equals("A"))
            {
              goodToQuery = true;
            } else
            {
              goodToQuery = false;
              out.println("Unable to interface profile, base message failed: ");
              out.print("<pre>" + testRunner.getAck() + "</pre>");
            }
          }

        } else if (testCaseMessageList.size() == 0)
        {
          goodToQuery = false;
          out.println("Unable to profile interface, Test Case Message size == 0 ");

        }

        Transformer transformer = new Transformer();

        SimpleDateFormat sdf = new SimpleDateFormat("msS");
        String mrnBase = (testCaseMessageBase == null ? "" : testCaseMessageBase.getTestCaseNumber()) + "" + sdf.format(new Date());
        String filenameBase = (testCaseMessageBase == null ? "" : testCaseMessageBase.getTestCaseNumber());

        PrintWriter sampleFileOut = null;
        if (goodToQuery)
        {

          if (user.hasSendData() && batchSize > 0)
          {
            try
            {
              TestCaseMessage testCaseMessage = new TestCaseMessage(testCaseMessageBase);

              File generatedDir = user.getSendData().getGeneratedDir();
              File file = new File(generatedDir, filenameBase + "-000 Base Messages.txt");
              PrintWriter fileOut = new PrintWriter(new FileWriter(file));
              file = new File(generatedDir, filenameBase + " Sample Messages.txt");
              sampleFileOut = new PrintWriter(new FileWriter(file));
              for (int i = 0; i <  batchSize ; i++)
              {
                testCaseMessage = new TestCaseMessage(testCaseMessageBase);
                testCaseMessage.setTestCaseNumber(mrnBase + (i < 10 ? "00" : (i < 100 ? "0" : "")) + i);
                transformer.transform(testCaseMessage);
                fileOut.print(testCaseMessage.getMessageText());
                if (i == 0)
                {
                  sampleFileOut.print(testCaseMessage.getMessageText());
                }
              }
              fileOut.close();
            } catch (Exception e)
            {
              e.printStackTrace();
            }

          }

          out.println("<table border=\"1\" cellspacing=\"0\">");
          out.println("  <tr>");
          out.println("    <th>#</th>");
          out.println("    <th>Issue</th>");
          if (expectedStatusMap != null)
          {
            out.println("    <th>Expect</th>");
          }
          out.println("    <th>Status Is</th>");
          out.println("    <th>Text</th>");
          out.println("    <th>Status Not</th>");
          out.println("    <th>Text</th>");
          out.println("  </tr>");
          List<String> issueList = IssueCreatorItems.getIssueList();
          int count = 0;
          for (String issueName : issueList)
          {
            count++;
            if (count % 25 == 0)
            {
              out.flush();
            }
            TestCaseMessage testCaseMessage = new TestCaseMessage(testCaseMessageBase);
            testCaseMessage.addCauseIssues(issueName);
            testCaseMessage.setTestCaseNumber(mrnBase + (count < 10 ? "00" : (count < 100 ? "0" : "")) + count + "0");
            TestCaseMessage testCaseMessageNot = new TestCaseMessage(testCaseMessageBase);
            testCaseMessageNot.addCauseIssues("NOT " + issueName);
            testCaseMessageNot.setTestCaseNumber(mrnBase + (count < 10 ? "00" : (count < 100 ? "0" : "")) + count + "1");
            transformer.transform(testCaseMessage);
            transformer.transform(testCaseMessageNot);
            out.println("  <tr>");
            out.println("    <td>" + count + "</td>");
            out.println("    <td>" + issueName + "</td>");
            String expectedStatus = "-";
            if (expectedStatusMap != null)
            {
              expectedStatus = expectedStatusMap.get(issueName);
              if (expectedStatus == null)
              {
                expectedStatus = "-";
              }
              out.println("    <td>" + expectedStatus + "</td>");
            }
            out.println("    <td>");
            String ack = null;
            if (!testCaseMessage.hasIssue())
            {
              out.println("-");
            } else
            {
              if (connector == null)
              {
                out.println("<div class=\"fail\">NOT RUN</div>");
              } else
              {
                try
                {
                  try
                  {
                    testRunner.runTest(connector, testCaseMessage);
                    ack = testRunner.getAck();
                  } catch (Throwable t)
                  {
                    t.printStackTrace(out);
                  }
                  if (expectedStatusMap != null && !expectedStatus.equals("-"))
                  {
                    if (expectedStatus.equals("S"))
                    {
                      expectedStatus = "A";
                    }
                    if (expectedStatus.equals(testRunner.getStatus()))
                    {
                      out.println("<div class=\"pass\">" + testRunner.getStatus() + "</div>");
                    } else
                    {
                      out.println("<div class=\"fail\">" + testRunner.getStatus() + "</div>");
                    }
                  } else
                  {
                    out.println(testRunner.getStatus());
                  }
                } catch (Exception e)
                {
                  out.println("Unable to run test: " + e.getMessage());
                  out.print("<pre>");
                  e.printStackTrace(out);
                  out.println("</pre>");
                }
              }
              if (user.hasSendData())
              {
                String countText = "" + count;
                if (count < 100)
                {
                  if (count > 9)
                  {
                    countText = "0" + count;
                  } else
                  {
                    countText = "00" + count;
                  }
                }
                try
                {
                  File file = new File(user.getSendData().getGeneratedDir(), filenameBase + "-" + countText + " " + issueName + ".txt");
                  PrintWriter fileOut = new PrintWriter(new FileWriter(file));
                  for (int i = 0; i < batchSize; i++)
                  {
                    testCaseMessage = new TestCaseMessage(testCaseMessageBase);
                    testCaseMessage.addCauseIssues(i < 20 ? issueName : "NOT " + issueName);
                    testCaseMessage.setTestCaseNumber(mrnBase + countText + (i < 10 ? "00" : (i < 100 ? "0" : "")) + i);
                    transformer.transform(testCaseMessage);
                    fileOut.print(testCaseMessage.getMessageText());
                    if (i == 0)
                    {
                      sampleFileOut.print(testCaseMessage.getMessageText());
                    }
                  }
                  fileOut.close();
                } catch (Exception e)
                {
                  e.printStackTrace();
                }
              }
            }
            out.println("    </td>");
            out.println("    <td>");
            out.println("      <a href=\"javascript:toggleLayer('text" + count + "');\" title=\"Show/Hide\">+/-</a></h2>");
            out.println("      <div id=\"text" + count + "\" style=\"display:none\">");
            out.println("        <div class=\"scrollbox\"><pre>" + testCaseMessage.getMessageText() + "</pre></div>");
            out.println("        <div class=\"scrollbox\"><pre>" + (ack == null ? "Not Run" : ack) + "</pre></div>");
            out.println("      </div>");
            out.println("    </td>");
            out.println("    <td>");
            ack = null;
            if (!testCaseMessageNot.hasIssue())
            {
              out.println("-");
            } else
            {
              if (connector == null)
              {
                out.println("<div class=\"fail\">NOT RUN</div>");
              } else
              {
                try
                {
                  try
                  {
                    testRunner.runTest(connector, testCaseMessageNot);
                    ack = testRunner.getAck();
                  } catch (Throwable t)
                  {
                    t.printStackTrace(out);
                  }
                  if (expectedStatusMap != null && !expectedStatus.equals("-"))
                  {
                    if ("A".equals(testRunner.getStatus()))
                    {
                      out.println("<div class=\"pass\">" + testRunner.getStatus() + "</div>");
                    } else
                    {
                      out.println("<div class=\"fail\">" + testRunner.getStatus() + "</div>");
                    }
                  } else
                  {
                    out.println(testRunner.getStatus());
                  }
                } catch (Exception e)
                {
                  out.println("Unable to run test: " + e.getMessage());
                  out.print("<pre>");
                  e.printStackTrace(out);
                  out.println("</pre>");
                }
              }
            }
            out.println("    </td>");
            out.println("    <td>");
            out.println("      <a href=\"javascript:toggleLayer('text" + count + "n');\" title=\"Show/Hide\">+/-</a></h2>");
            out.println("      <div id=\"text" + count + "n\" style=\"display:none\">");
            out.println("        <div class=\"scrollbox\"><pre>" + testCaseMessageNot.getMessageText() + "</pre></div>");
            out.println("        <div class=\"scrollbox\"><pre>" + (ack == null ? "Not Run" : ack) + "</pre></div>");
            out.println("      </div>");
            out.println("    </td>");
            out.println("  </tr>");
          }
          if (sampleFileOut != null)
          {
            sampleFileOut.close();
          }
        }
        out.println("</table>");
        printHtmlFoot(out);
      } finally
      {
        out.close();
      }
    }
  }

  protected static Set<String> setTestCaseNumberSelectedSet(HttpServletRequest request, HttpSession session)
  {
    Set<String> testCaseNumberSelectedSet = new HashSet<String>();
    String[] testCaseNumberSelected = request.getParameterValues("testCaseNumber");
    if (testCaseNumberSelected != null)
    {
      for (String s : testCaseNumberSelected)
      {
        if (s != null && s.length() > 0)
        {
          testCaseNumberSelectedSet.add(s);
        }
      }
    }
    session.setAttribute("testCaseNumberSelectedList", testCaseNumberSelectedSet);
    return testCaseNumberSelectedSet;
  }

  protected static List<TestCaseMessage> parseAndAddTestCases(String testCase, HttpSession session) throws ServletException
  {
    List<TestCaseMessage> testCaseMessageList = null;
    if (testCase == null)
    {
      testCaseMessageList = new ArrayList<TestCaseMessage>();
    } else
    {
      try
      {
        testCaseMessageList = TestCaseMessage.createTestCaseMessageList(testCase);
      } catch (Exception e)
      {
        throw new ServletException("Unable to read test case messages", e);
      }
    }
    return testCaseMessageList;
  }

  protected static void makeHideScript(PrintWriter out)
  {
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

  private static String readSubParts(Map<String, String> expectedStatusMap, NodeList nodes)
  {
    String testScript = null;
    for (int i = 0; i < nodes.getLength(); i++)
    {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE && "report-template".equals(node.getNodeName()))
      {
        nodes = nodes.item(i).getChildNodes();
        for (i = 0; i < nodes.getLength(); i++)
        {
          node = nodes.item(i);
          if ("base-profile".equals(node.getNodeName()))
          {
            readExpectedIssues(expectedStatusMap, nodes.item(i).getChildNodes());
          } else if ("test-case-script".equals(node.getNodeName()))
          {
            testScript = node.getTextContent();
          }
        }
        break;
      }
    }
    return testScript;
  }

  private static void readExpectedIssues(Map<String, String> expectedStatusMap, NodeList nodes)
  {
    for (int i = 0; i < nodes.getLength(); i++)
    {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE)
      {
        NamedNodeMap map = node.getAttributes();
        if ("potential-issue-status".equals(node.getNodeName()))
        {
          expectedStatusMap.put(safe(map.getNamedItem("issue")), safe(map.getNamedItem("actionCode")));
        }
      }
    }
  }

  protected static void sortTestCaseMessageList(List<TestCaseMessage> testCaseMessageList)
  {
    Collections.sort(testCaseMessageList, new Comparator<TestCaseMessage>() {
      public int compare(TestCaseMessage o1, TestCaseMessage o2)
      {
        return o1.getTestCaseNumber().compareTo(o2.getTestCaseNumber());
      }
    });
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
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null)
    {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else
    {
      PrintWriter out = response.getWriter();
      try
      {
        printHtmlHead(out, "Profile Interface", request);
        out.println("    <form action=\"interfaceProfile\" method=\"POST\">");
        out.println("      <table border=\"0\">");
        int id = 0;
        if (request.getParameter("id") != null)
        {
          id = Integer.parseInt(request.getParameter("id"));
        }
        if (session.getAttribute("id") != null)
        {
          id = (Integer) session.getAttribute("id");
        }
        TestCaseMessage testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
        if (testCaseMessage == null)
        {
          testCaseMessage = new TestCaseMessage();
        }
        out.println("        <tr>");
        out.println("          <td>Service</td>");
        out.println("          <td>");
        List<Connector> connectors = SetupServlet.getConnectors(session);
        if (connectors.size() == 1)
        {
          out.println("            " + connectors.get(0).getLabelDisplay());
          out.println("            <input type=\"hidden\" name=\"id\" value=\"1\"/>");
        } else
        {
          out.println("            <select name=\"id\">");
          out.println("              <option value=\"\">select</option>");
          int i = 0;
          for (Connector connector : connectors)
          {
            i++;
            if (id == i)
            {
              out.println("              <option value=\"" + i + "\" selected=\"true\">" + connector.getLabelDisplay() + "</option>");
            } else
            {
              out.println("              <option value=\"" + i + "\">" + connector.getLabelDisplay() + "</option>");
            }
          }
          out.println("            </select>");
        }
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>Test</td>");
        out.println("          <td><textarea name=\"source\" cols=\"70\" rows=\"10\" wrap=\"off\"></textarea></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td>DQA Report Template XML</td>");
        out.println("          <td><textarea name=\"expected\" cols=\"70\" rows=\"10\" wrap=\"off\"></textarea></td>");
        out.println("        </tr>");
        Authenticate.User user = (Authenticate.User) session.getAttribute("user");
        if (user.hasSendData())
        {
          out.println("        <tr>");
          out.println("          <td>Save Sample Count</td>");
          out.println("          <td><input name=\"batchSize\" type=\"text\" size=\"2\" value=\"0\"/> (Samples saved to " + user.getSendData().getGeneratedDir() + ")</td>");
          out.println("        </tr>");
        }
        out.println("        <tr>");
        out.println("          <td colspan=\"2\" align=\"right\">");
        out.println("            <input type=\"submit\" name=\"method\" value=\"Submit\"/>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("      </table>");
        out.println("    </form>");
        printHtmlFoot(out);
      } finally
      {
        out.close();
      }
    }
  }

  private static String safe(Node n)
  {
    if (n == null)
    {
      return "";
    }
    String s = n.getNodeValue();
    if (s == null)
    {
      return "";
    }
    return s;
  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo()
  {
    return "";
  }// </editor-fold>
}
