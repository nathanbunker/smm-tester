package org.immunizationsoftware.dqa.mover.install;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.install.templates.ConnectionTemplateFactory;

public class PrepareServlet extends ClientServlet
{
  protected static final String FOLDER_NAME = "folderName";
  protected static final String TEMPLATE_NAME = "templateName";
  protected static final String BASE_DIR = "baseDir";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(true);
    resp.setContentType("text/html;charset=UTF-8");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    SoftwareType softwareType = getSoftwareType(req);

    try {
      printHtmlHead(out, softwareType, "1. Prepare", req);
      out.println("<h2>Step 1: Prepare</h2>");
      out.println("<form action=\"ConfigureServlet\" method=\"GET\">");
      if (softwareType == SoftwareType.TESTER) {
        out.println("  <input type=\"hidden\" name=\"softwareType\" value=\"Tester\">");
      }
      out.println("  <table width=\"650\">");
      out.println("    <tr>");
      out.println("      <td>A.</td>");
      out.println("      <td><input type=\"checkbox\" name=\"\"/></td>");
      if (softwareType == SoftwareType.TESTER) {
        out.println("      <td>Create or identify the Root Data Folder. </td>");
      } else {
        out.println("      <td>Create or identify the <a href=\"https://openimmunizationsoftware.net/interfacing/smm/installation.html#SMMRootFolder\" target=\"blank\">SMM Root Folder</a>. </td>");
      }
      out.println("    </tr>");

      out.println("    <tr>");
      out.println("      <td>&nbsp;</td>");
      out.println("      <td>&nbsp;</td>");
      String exampleBaseDir = softwareType == SoftwareType.TESTER ? "C:\\tester\\" : "C:\\smm\\";
      out.println("      <td>Copy and paste full name of this folder: <input type=\"text\" name=\"" + BASE_DIR
          + "\" size=\"30\" value=\"" + exampleBaseDir + "\"></td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td>B.</td>");
      out.println("      <td><input type=\"checkbox\" name=\"\"/></td>");
      out.println("      <td>Create or identify the <a href=\"https://openimmunizationsoftware.net/interfacing/smm/installation.html#IISTransferFolder\" target=\"blank\">IIS Transfer Folder</a>.</td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td>&nbsp;</td>");
      out.println("      <td>&nbsp;</td>");
      out.println("      <td>Name of this folder is: <input type=\"text\" name=\"" + FOLDER_NAME
          + "\" value=\"transfer\"/></td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td>C.</td>");
      out.println("      <td>&nbsp;</td>");
      out.println("      <td>Choose a template for configuration:");
      out.println("        <select name=\"" + TEMPLATE_NAME + "\">");
      for (String option : ConnectionTemplateFactory.getConnectionTemplateNames()) {
        out.println("<option value=\"" + option + "\">" + option + "</option>");
      }
      out.println("        </select>");
      out.println("      </td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td colspan=\"3\">You should now be ready for the next step: <input type=\"submit\" value=\"Step 2: Configure\"/></td>");
      out.println("    </tr>");
      out.println("  </table>");
      out.println("</form>");

      printHtmlFoot(out);
    } catch (Exception e) {
      e.printStackTrace();
      out.println("<p>Problem encountered: </p><pre>");
      e.printStackTrace(out);
      out.println("</pre>");
    } finally {
      out.close();
    }
  }
}
