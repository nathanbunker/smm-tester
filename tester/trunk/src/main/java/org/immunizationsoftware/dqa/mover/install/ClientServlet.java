/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.mover.install;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.immunizationsoftware.dqa.SoftwareVersion;

/**
 * 
 * @author nathan
 */
public class ClientServlet extends HttpServlet
{

  protected static SoftwareType getSoftwareType(HttpServletRequest request) {
    String softwareType = request.getParameter("softwareType");
    if (softwareType == null || softwareType.equalsIgnoreCase("SMM")) {
      return SoftwareType.SMM;
    } else if (softwareType.equalsIgnoreCase("TESTER")) {
      return SoftwareType.TESTER;
    }
    return SoftwareType.SMM;
  }

  protected static void printHtmlHead(PrintWriter out, SoftwareType softwareType, String title,
      HttpServletRequest request) {
    out.println("<html>");
    out.println("  <head>");
    out.println("    <title>" + title + "</title>");
    out.println("    <link rel=\"stylesheet\" type=\"text/css\" href=\"../index.css\" />");
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
    out.println("  </head>");
    out.println("  <body>");
    out.println(makeMenu(request, title, softwareType));
    String message = (String) request.getAttribute("message");
    if (message != null) {
      out.println("<p class=\"fail\">" + message + "</p>");
    }

  }

  public static String makeMenu(HttpServletRequest request, SoftwareType softwareType) {
    return makeMenu(request, "&nbsp;", softwareType);
  }

  private static final String[][] MENU_SMM = { { "index.html", "Simple Message Mover" },
      { "PrepareServlet", "1. Prepare" }, { "ConfigureServlet", "2. Configure" }, { "InstallServlet", "3. Install" },
      { "DownloadServlet", "4. Download SMM" } };

  private static final String[][] MENU_TESTER = { { "indexTester.html?", "IIS HL7 Tester" },
      { "PrepareServlet?softwareType=Tester", "1. Prepare" },
      { "ConfigureServlet?softwareType=Tester", "2. Configure" },
      { "InstallServlet?softwareType=Tester", "3. Install" },
      { "DownloadServlet?softwareType=Tester", "4. Download Tester" } };

  public static String makeMenu(HttpServletRequest request, String title, SoftwareType softwareType) {
    StringBuilder result = new StringBuilder();
    result.append("    <table class=\"menu\"><tr><td>");
    String[][] menu = softwareType == SoftwareType.TESTER ? MENU_TESTER : MENU_SMM;
    for (int i = 0; i < menu.length; i++) {
      if (i > 0) {
        // result.append(" &bull; ");
        result.append(" ");
      }
      String styleClass = "menuLink";
      if (menu[i][1].equals(title)) {
        styleClass = "menuLinkSelected";
      }
      result.append("<a class=\"");
      result.append(styleClass);
      result.append("\" href=\"");
      result.append(menu[i][0]);
      result.append("\">");
      result.append(menu[i][1]);
      result.append("</a>");

    }
    result.append("</td><td align=\"right\">");
    result.append("&nbsp;");

    result.append("</td></tr></table><br>");
    return result.toString();
  }

  public static void printFooter(PrintWriter out) {
    out.println("    <p>Open Immunization Software - IIS HL7 Tester &amp; Simple Message Mover - Version "
        + SoftwareVersion.VERSION
        + "<br>"
        + "For questions or support please contact <a href=\"http://openimmunizationsoftare.org/\">Nathan Bunker</a>.</p>");

  }

  public static void printHtmlFoot(PrintWriter out) {
    printFooter(out);
    out.println("  </body>");
    out.println("</html>");
  }
}
