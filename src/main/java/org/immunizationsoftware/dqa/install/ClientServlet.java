/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.install;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.immunizationsoftware.dqa.SoftwareVersion;

/**
 * 
 * @author nathan
 */
public class ClientServlet extends HttpServlet {

  protected static void printHtmlHead(PrintWriter out, String title, HttpServletRequest request) {
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
    out.println(makeMenu(request, title));
    String message = (String) request.getAttribute("message");
    if (message != null) {
      out.println("<p class=\"fail\">" + message + "</p>");
    }

  }

  public static String makeMenu(HttpServletRequest request) {
    return makeMenu(request, "&nbsp;");
  }

  private static final String[][] MENU = { { "index.html", "DQA" }, { "DownloadServlet", "4. Download" } };

  public static String makeMenu(HttpServletRequest request, String title) {
    StringBuilder result = new StringBuilder();
    result.append("    <table class=\"menu\"><tr><td>");
    for (int i = 0; i < MENU.length; i++) {
      if (i > 0) {
        // result.append(" &bull; ");
        result.append(" ");
      }
      String styleClass = "menuLink";
      if (MENU[i][1].equals(title)) {
        styleClass = "menuLinkSelected";
      }
      result.append("<a class=\"");
      result.append(styleClass);
      result.append("\" href=\"");
      result.append(MENU[i][0]);
      result.append("\">");
      result.append(MENU[i][1]);
      result.append("</a>");

    }
    result.append("</td><td align=\"right\">");
    result.append("&nbsp;");

    result.append("</td></tr></table><br>");
    return result.toString();
  }

  public static void printFooter(PrintWriter out) {
    out.println("    <p>American Immunization Registry Association - IIS HL7 Tester &amp; Simple Message Mover - Version " + SoftwareVersion.VERSION
        + "</p>");

  }

  public static void printHtmlFoot(PrintWriter out) {
    printFooter(out);
    out.println("  </body>");
    out.println("</html>");
  }
}
