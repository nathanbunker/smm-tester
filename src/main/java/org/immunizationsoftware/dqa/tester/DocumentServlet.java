/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.manager.hl7.ConformanceIssue;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7ComponentManager;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ERL;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.VXU;

/**
 * 
 * @author nathan
 */
public class DocumentServlet extends ClientServlet
{

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

      int pos = -1;
      if (request.getParameter("objectType") != null) {
        pos = Integer.parseInt(request.getParameter("objectType"));
      }

      String[] childStep = null;
      if (request.getParameter("childStep") != null) {
        childStep = request.getParameter("childStep").split("\\.");
      }

      PrintWriter out = new PrintWriter(response.getWriter());
      response.setContentType("text/html;charset=UTF-8");
      printHtmlHead(out, MENU_HEADER_HOME, request);
      out.println("    <form action=\"DocumentServlet\" method=\"GET\">");
      out.println("      <table border=\"0\">");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">HL7 Object Type</td>");
      out.println("          <td><select name=\"objectType\">");
      List<HL7Component> componentList = HL7ComponentManager.getComponentList();
      for (int i = 0; i < componentList.size(); i++) {
        HL7Component component = componentList.get(i);
        out.println("              <option value=\"" + i + "\"" + (i == pos ? "selected=\"true\"" : "") + ">"
            + component.getComponentReference() + "</option>");
      }
      out.println("              ");
      out.println("            </select>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td colspan=\"2\" align=\"right\">");
      out.println("            <input type=\"submit\" name=\"method\" value=\"Submit\"/>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("      </table>");
      out.println("    </form>");
      if (pos > -1) {
        HL7Component component = componentList.get(pos).makeAnother();
        String childStepLink = "0";
        String parentStepLink = "0";
        if (childStep != null) {
          int i = 1;
          while (i < childStep.length && childStep[i] != null && childStep[i].length() > 0 && component != null) {
            if (i > 1) {
              parentStepLink += "." + childStep[i - 1];
            }
            component = component.getChildComponent(Integer.parseInt(childStep[i]));
            childStepLink += "." + childStep[i];
            i++;
          }
        }
        out.println("    <h2>Documentation for " + component.getComponentNameFull() + "</h2>");
        out.println("");
        if (component.getItemType() == ItemType.MESSAGE) {

          out.println("      <table border=\"1\" cellspacing=\"0\">");
          out.println("        <tr>");
          out.println("          <th>Segment</th>");
          out.println("          <th>Cardinality</th>");
          out.println("          <th>Usage</th>");
          out.println("          <th>Comment</th>");
          out.println("        </tr>");
          printMessagePart(out, component, childStepLink, pos);
          out.println("      </table>");

        } else if (component.getItemType() == ItemType.SEGMENT) {
          out.println("    <p>This is segment is part of a <a href=\"DocumentServlet?objectType=" + pos + "&childStep="
              + parentStepLink + "\">" + n(component.getParentComponent().getComponentCode()) + "</a> segment.</p>");

          if (component.getChildComponent() != null) {
            out.println("      <table border=\"1\" cellspacing=\"0\">");
            out.println("        <tr>");
            out.println("          <th>Seq</th>");
            out.println("          <th>Element Name</th>");
            out.println("          <th>Data Type</th>");
            out.println("          <th>Usage</th>");
            out.println("          <th>Cardinality</th>");
            out.println("          <th>Len</th>");
            out.println("          <th>Conditional Predicate</th>");
            out.println("          <th>Value Set</th>");
            out.println("          <th>Description/Comment</th>");
            out.println("        </tr>");
            for (int i = 1; i < component.getChildComponent().length; i++) {
              HL7Component child = component.getChildComponent(i);
              String passClass = (child.getUsageType() == UsageType.R || child.getUsageType() == UsageType.RE) ? " class=\"pass\""
                  : " class=\"nottested\"";
              out.println("        <tr>");

              out.println("          <td" + passClass + ">" + i + "</td>");
              out.println("          <td" + passClass + "><a href=\"DocumentServlet?objectType=" + pos + "&childStep="
                  + (childStepLink + "." + i) + "\">" + n(child.getComponentNameSpecific()) + "</a></td>");
              out.println("          <td" + passClass + "><a href=\"DocumentServlet?objectType=" + pos + "&childStep="
                  + (childStepLink + "." + i) + "\">" + n(child.getComponentCode()) + "</a></td>");
              out.println("          <td" + passClass + ">" + n(printUsage(child)) + "</td>");
              if (child.getCardinality() != null) {
                out.println("          <td" + passClass + ">" + n(child.getCardinalityMin(), child.getCardinalityMax())
                    + "</td>");
              } else {
                out.println("          <td" + passClass + ">&nbsp;</td>");
              }
              if (child.getLengthMin() > 1 || child.getLengthMax() < Integer.MAX_VALUE) {
                if (child.getLengthMax() == Integer.MAX_VALUE) {
                  out.println("          <td" + passClass + ">" + child.getLengthMin() + "..*</td>");
                } else {
                  out.println("          <td" + passClass + ">" + child.getLengthMin() + ".." + child.getLengthMax()
                      + "</td>");
                }
              } else {
                out.println("          <td" + passClass + ">&nbsp;</td>");
              }
              if (child.getConditionalPredicate() != null) {
                out.println("          <td" + passClass + ">" + n(child.getConditionalPredicate().printDocument())
                    + "</td>");
              } else {
                out.println("          <td" + passClass + ">&nbsp;</td>");
              }
              if (child.getValueSet() != null) {
                out.println("          <td" + passClass + ">" + child.getValueSet() + "</td>");
              } else {
                out.println("          <td" + passClass + ">&nbsp;</td>");
              }
              out.println("          <td" + passClass + ">" + n(child.getComments()) + "</td>");
              out.println("        </tr>");
            }

            out.println("      </table>");
          }
        } else if (component.getItemType() == ItemType.DATATYPE) {
          out.println("    <p>This is field is part of a <a href=\"DocumentServlet?objectType=" + pos + "&childStep="
              + parentStepLink + "\">" + n(component.getComponentReferenceShort())
              + "</a> field.</p>");

          if (component.getChildComponent() != null) {
            out.println("      <table border=\"1\" cellspacing=\"0\">");
            out.println("        <tr>");
            out.println("          <th>Seq</th>");
            out.println("          <th>Component Name</th>");
            out.println("          <th>Data Type</th>");
            out.println("          <th>Usage</th>");
            out.println("          <th>Len</th>");
            out.println("          <th>Conditional Predicate</th>");
            out.println("          <th>Value Set</th>");
            out.println("          <th>Comments</th>");
            out.println("        </tr>");
            for (int i = 1; i < component.getChildComponent().length; i++) {
              HL7Component child = component.getChildComponent(i);
              String passClass = (child.getUsageType() == UsageType.R || child.getUsageType() == UsageType.RE) ? " class=\"pass\""
                  : " class=\"nottested\"";
              out.println("        <tr>");

              out.println("          <td" + passClass + ">" + i + "</td>");
              out.println("          <td" + passClass + "><a href=\"DocumentServlet?objectType=" + pos + "&childStep="
                  + (childStepLink + "." + i) + "\">" + n(child.getComponentNameSpecific()) + "</a></td>");
              out.println("          <td" + passClass + "><a href=\"DocumentServlet?objectType=" + pos + "&childStep="
                  + (childStepLink + "." + i) + "\">" + n(child.getComponentCode()) + "</a></td>");
              out.println("          <td" + passClass + ">" + n(printUsage(child)) + "</td>");
              if (child.getLengthMin() > 1 || child.getLengthMax() < Integer.MAX_VALUE) {
                if (child.getLengthMax() == Integer.MAX_VALUE) {
                  out.println("          <td" + passClass + ">" + child.getLengthMin() + "..*</td>");
                } else {
                  out.println("          <td" + passClass + ">" + child.getLengthMin() + ".." + child.getLengthMax()
                      + "</td>");
                }
              } else {
                out.println("          <td" + passClass + ">&nbsp;</td>");
              }
              if (child.getConditionalPredicate() != null) {
                out.println("          <td" + passClass + ">" + n(child.getConditionalPredicate().printDocument())
                    + "</td>");
              } else {
                out.println("          <td" + passClass + ">&nbsp;</td>");
              }
              if (child.getValueSet() != null) {
                out.println("          <td" + passClass + ">" + child.getValueSet() + "</td>");
              } else {
                out.println("          <td" + passClass + ">&nbsp;</td>");
              }
              out.println("          <td" + passClass + ">" + n(child.getComments()) + "</td>");
              out.println("        </tr>");
            }
            out.println("      </table>");
          }

        }
      }

      out.println("  <div class=\"help\">");
      out.println("  <h2>How To Use This Page</h2>");
      out.println("  </div>");
      printHtmlFoot(out);
      out.close();
    }
  }

  public String printUsage(HL7Component child) {
    if (child.getUsageType() == UsageType.C) {
      return child.getUsageType().toString() + "(" + child.getConditionalPredicate().getUsageTypeMet().toString() + "/"
          + child.getConditionalPredicate().getUsageTypeNotMet().toString() + ")";
    }
    return child.getUsageType().toString();
  }

  public void printMessagePart(PrintWriter out, HL7Component component, String childStepLink, int pos) {
    for (int i = 1; i < component.getChildComponent().length; i++) {
      HL7Component child = component.getChildComponent(i);
      String passClass = (child.getUsageType() == UsageType.R || child.getUsageType() == UsageType.RE) ? " class=\"pass\""
          : " class=\"nottested\"";
      if (child.getItemType() == ItemType.MESSAGE_PART) {
        out.println("        <tr>");
        out.println("          <td" + passClass + ">Begin " + n(child.getComponentNameSpecific()) + "</td>");
        out.println("          <td" + passClass + ">" + n(child.getCardinalityMin(), child.getCardinalityMax())
            + "</td>");
        out.println("          <td" + passClass + ">" + n(printUsage(child)) + "</td>");
        out.println("          <td" + passClass + ">" + n(child.getComments()) + "</td>");
        out.println("        </tr>");
        printMessagePart(out, child, childStepLink + "." + i, pos);
        out.println("        <tr>");
        out.println("          <td" + passClass + " colspan=\"4\">End " + n(child.getComponentNameSpecific()) + "</td>");
        out.println("        </tr>");
      } else {
        out.println("        <tr>");

        out.println("          <td" + passClass + "><a href=\"DocumentServlet?objectType=" + pos + "&childStep="
            + (childStepLink + "." + i) + "\">" + n(child.getComponentCode()) + "</a></td>");
        out.println("          <td" + passClass + ">" + n(child.getCardinalityMin(), child.getCardinalityMax())
            + "</td>");
        out.println("          <td" + passClass + ">" + n(printUsage(child)) + "</td>");
        out.println("          <td" + passClass + ">" + n(child.getComments()) + "</td>");
        out.println("        </tr>");
      }
    }
  }

  public static void printConformanceIssues(PrintWriter out, List<ConformanceIssue> conformanceIssueList) {

    for (ConformanceIssue conformanceIssue : conformanceIssueList) {
      String passClass = conformanceIssue.getSeverity().getValue().equals("E") ? " class=\"fail\"" : " class=\"pass\"";
      out.println("        <tr>");
      out.println("          <td" + passClass + ">" + n(conformanceIssue.getSeverity().getValue()) + "</td>");
      out.println("          <td" + passClass + ">" + n(conformanceIssue.getErrorLocation()) + "</td>");
      out.println("          <td" + passClass + ">" + n(conformanceIssue.getUserMessage().getValue()) + "</td>");
      out.println("        </tr>");
    }
    out.println("      </table>");
  }

  private static String n(String s) {
    if (s == null || s.equals("")) {
      return "&nbsp;";
    }
    return s;
  }

  private static String n(int min, int max) {
    if (max == Integer.MAX_VALUE) {
      return "[" + min + "..*]";
    }
    return "[" + min + ".." + max + "]";
  }

  private static String n(ERL errorLocation) {
    if (errorLocation == null) {
      return "&nbsp;";
    }
    if (errorLocation.getSegmentID().getValue().equals("")) {
      return "&nbsp;";
    }

    String s = errorLocation.getSegmentID().getValue();
    if (!errorLocation.getSegmentSequence().getValue().equals("")
        && !errorLocation.getSegmentSequence().getValue().equals("1")) {
      s = s + "#" + errorLocation.getSegmentSequence().getValue();
    }
    if (!errorLocation.getFieldPosition().getValue().equals("")) {
      s = s + "-" + errorLocation.getFieldPosition().getValue();
      if (!errorLocation.getFieldRepetition().getValue().equals("")
          && !errorLocation.getFieldRepetition().getValue().equals("1")) {
        s = s + "#" + errorLocation.getFieldRepetition().getValue();
      }
      if (!errorLocation.getComponentNumber().getValue().equals("")) {
        s = s + "." + errorLocation.getComponentNumber().getValue();
        if (!errorLocation.getSubComponentNumber().getValue().equals("")) {
          s = s + "." + errorLocation.getSubComponentNumber().getValue();
        }
      }
    }
    return s;

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
      doGet(request, response);
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
    return "Compare response with original submission";
  }// </editor-fold>

}
