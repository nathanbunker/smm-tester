package org.immunizationsoftware.dqa.mover.install;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class InstallServlet extends ClientServlet
{
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    HttpSession session = req.getSession(true);
    String tomcatHome = (String) session.getAttribute("tomcatHome");
    resp.setContentType("text/html;charset=UTF-8");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    try
    {
      printHtmlHead(out, "3. Install", req);
      out.println("<h2>Step 3: Install</h2>");
      out.println("<p>The Simple Message Mover operates within a Java Servlet environment. The instructions below show you how to install Apache Tomcat, a commonly used Java Servlet environment, on a Windows system. </p>");
      out.println("<form action=\"DownloadServlet\" method=\"GET\">");
      out.println("  <table width=\"650\">");
      out.println("    <tr>");
      out.println("      <td>A.</td>");
      out.println("      <td><input type=\"checkbox\" name=\"\"/></td>");
      out.println("      <td>Open and review the installation instructions for Tomcat. </td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td></td>");
      out.println("      <td></td>");
      out.println("      <td><a href=\"http://tomcat.apache.org/tomcat-7.0-doc/appdev/installation.html\" target=\"_blank\">http://tomcat.apache.org/tomcat-7.0-doc/appdev/installation.html</a></td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td>B.</td>");
      out.println("      <td><input type=\"checkbox\" name=\"\"/></td>");
      out.println("      <td>Ensure that you have Java SE version 6.0 or later installed.</td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td></td>");
      out.println("      <td></td>");
      out.println("      <td>To confirm the version of Java installed on your system, follow these steps:<ul><li>Open a command window (in Windows click Start >> Run and type cmd, press enter) </li><li>Type <code>java -version</code> on the command line</li><li>Verify the version number displayed</li></ul></td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td></td>");
      out.println("      <td></td>");
      out.println("      <td>If Java is not installed or the installed version is to old, please follow the instructions on the Tomcat installation document to download a compatible JDK. </td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td>C.</td>");
      out.println("      <td><input type=\"checkbox\" name=\"\"/></td>");
      out.println("      <td>Download and install Tomcat. (For Windows choose the 31-bit/64-bit Windows Service Installer)</td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td></td>");
      out.println("      <td></td>");
      out.println("      <td><ul><li>Tomcat may be installed in any location on your local system.</li><li>Tomcat is an application that runs in the background.</li><li>In Windows, Tomcat can be started and stopped using the Services window.</li></ul></td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td></td>");
      out.println("      <td></td>");
      out.println("      <td>Indicate where your Tomcat is installed <input type=\"text\" name=\"tomcatHome\" value=\""
          + (tomcatHome == null ? "" : tomcatHome) + "\" size=\"40\"/></td>");
      out.println("    </tr>");
      out.println("    <tr>");
      out.println("      <td colspan=\"3\">You should now be ready for the next step: <input type=\"submit\" value=\"Step 4: Download SMM\"/></td>");
      out.println("    </tr>");
      out.println("  </table>");
      out.println("</form>");

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
