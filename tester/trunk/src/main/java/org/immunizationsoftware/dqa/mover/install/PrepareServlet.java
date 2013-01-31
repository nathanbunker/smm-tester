package org.immunizationsoftware.dqa.mover.install;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PrepareServlet extends ClientServlet
{
  protected static final String FOLDER_NAME = "folderName";
  protected static final String TEMPLATE_NAME = "templateName";
  protected static final String BASE_DIR = "baseDir";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    HttpSession session = req.getSession(true);
    resp.setContentType("text/html;charset=UTF-8");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    try
    {
      printHtmlHead(out, "1. Prepare", req);
      out.println("<h2>Step 1: Prepare</h2>");
      out.println("<form action=\"ConfigureServlet\" method=\"GET\">");
      out.println("  <table width=\"650\">");
      out.println("    <tr>");
      out.println("      <td>A.</td>");
      out.println("      <td><input type=\"checkbox\" name=\"\"/></td>");
      out.println("      <td>Create or identify a local folder where the data to be transmitted will be staged. </td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td>&nbsp;</td>");
      out.println("      <td>&nbsp;</td>");
      out.println("      <td>Copy and past full name of this directory: <input type=\"text\" name=\"" + BASE_DIR
          + "\" size=\"30\" value=\"C:\\smm\\\"></td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td>B.</td>");
      out.println("      <td><input type=\"checkbox\" name=\"\"/></td>");
      out.println("      <td>Create or identify a folder within the primary folder to represent the specific transport interface.</td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td>&nbsp;</td>");
      out.println("      <td>&nbsp;</td>");
      out.println("      <td>Name of the transfer folder is: <input type=\"text\" name=\"" + FOLDER_NAME + "\" value=\"transfer\"/></td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td>C.</td>");
      out.println("      <td>&nbsp;</td>");
      out.println("      <td>Choose a template for configuration:");
      out.println("        <select name=\"" + TEMPLATE_NAME + "\">");
      for (String option : ConfigureServlet.TEMPLATES)
      {
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
      out.println("<h2>Comments</h2>");
      out.println("<p>The simple message mover transports HL7 files from a local computer to an immunization registry. In order for the Simple Message Mover to work the following requirements must be met: </p>");
      out.println("<ul>");
      out.println("  <li>The data to be sent must be saved into a file and placed in a file directory located on the local system. </li>");
      out.println("  <li>The data in the file must be HL7 version 2 VXU, VXQ or QBP messages. </li>");
      out.println("  <li>Each file may contain one or more messages. The Simple Message Mover will automatically break batches of messages into single message packets for transmission. </li>");
      out.println("  <li>It is recommended that files with more than one message begin with a FHS segment and end with a FTS. As a safety precaution SMM will not process files that begin with a FHS segment but don't end with a FTS segment. </li>");
      out.println("  <li>You will need to create or identify a primary directory for holding the data files and the supporting files that SMM will use to transmit the data. This directory is called the <em>Primary SMM Directory</em> and should be dedicated for transmitting data. </li>");
      out.println("  <li>In the <em>Primary SMM Directory</em> you will need to make or identify a directory to represent the connection the external system. If you plan to connect to more than one system, you will need to create a different directory for each system. </li>");
      out.println("</ul>");

      printHtmlFoot(out);
    } catch (Exception e)
    {
      e.printStackTrace();
      out.println("<p>Problem encountered: </p><pre>");
      e.printStackTrace(out);
      out.println("</pre>");
    } finally
    {
      out.close();
    }
  }
}
