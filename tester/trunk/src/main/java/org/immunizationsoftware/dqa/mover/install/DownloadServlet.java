package org.immunizationsoftware.dqa.mover.install;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.SoftwareVersion;
import org.immunizationsoftware.dqa.mover.ConnectionManager;
import org.immunizationsoftware.dqa.mover.ManagerServlet;

public class DownloadServlet extends ClientServlet
{

  private static final String FIELD_VERSION = "version";
  private static final String FIELD_DATA_DIR = "dataDir";
  private static final String FIELD_SC_CODE = "scCode";
  private static final String FIELD_SC_LOCATION = "scLocation";
  private static final String FIELD_ADMIN_USERNAME = "adminUsername";
  private static final String FIELD_ADMIN_PASSWORD = "adminPassword";

  public static final boolean ENABLE_SUPPORT_CENTER = false;

  private static final String[][] SUPPORT_CENTER_CODE = { { "general", "General" },
      { "IHS", "Indian Health Service" } };
  private static final String[][] SUPPORT_CENTER_LOCATION = { { "http://ois-dqa.net/dqa/remote", "OIS" },
      { "none", "none" } };

  private static Random random = new Random();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(true);
    String folderName = (String) session.getAttribute(PrepareServlet.BASE_DIR);
    if (folderName == null) {
      folderName = "";
    }

    SoftwareType softwareType = getSoftwareType(req);

    String tomcatHome = req.getParameter("tomcatHome");
    if (tomcatHome != null) {
      session.setAttribute("tomcatHome", tomcatHome);
    }
    if (tomcatHome == null) {
      tomcatHome = (String) session.getAttribute("tomcatHome");
    }

    String message = null;
    String action = req.getParameter("action");
    if (action != null && action.equals("Download")) {
      String version = req.getParameter(FIELD_VERSION);
      String dataDir = req.getParameter(FIELD_DATA_DIR);
      String scLocation = req.getParameter(FIELD_SC_LOCATION);
      String scCode = req.getParameter(FIELD_SC_CODE);
      String adminUsername = req.getParameter(FIELD_ADMIN_USERNAME);
      String adminPassword = req.getParameter(FIELD_ADMIN_PASSWORD);
      if (adminUsername == null) {
        adminUsername = "";
      }
      if (adminPassword == null) {
        adminPassword = "";
      }
      if (!ENABLE_SUPPORT_CENTER)
      {
        scLocation = "none";
        scCode = "";
      }
      if (softwareType == SoftwareType.TESTER) {
        if (version == null || version.equals("")) {
          message = "Software Version is required";
        }
      } else {
        if (version == null || version.equals("")) {
          message = "Software Version is required";
        } else if (dataDir == null || dataDir.equals("")) {
          message = "SMM Root Folder is required";
        } else {
          if (ENABLE_SUPPORT_CENTER) {
            if (scLocation == null || scLocation.equals("")) {
              message = "Support Center is required";
            } else if (scCode == null || scCode.equals("")) {
              message = "Support Center Code is required";
            }
          }
        }
      }
      if (message == null) {
        File rootDir = ConnectionManager.getSoftwareDir();
        File versionWar = new File(rootDir, "tester-" + version + ".war");
        File expandDir = null;
        while (expandDir == null) {
          String uniqueId = String.valueOf(Math.abs(session.hashCode())) + String.valueOf(random.nextInt(100));
          expandDir = new File(rootDir, "tester-" + uniqueId);
          if (expandDir.exists()) {
            expandDir = null;
          }
        }
        List<String> filenameList = new ArrayList<String>();
        byte[] buffer = new byte[1024];
        try {
          expandDir.mkdir();
          ZipInputStream zis = new ZipInputStream(new FileInputStream(versionWar));
          ZipEntry ze = zis.getNextEntry();
          while (ze != null) {
            String filename = ze.getName();
            File newFile = new File(expandDir, filename);
            if (ze.isDirectory()) {
              newFile.mkdirs();
            } else {
              new File(newFile.getParent()).mkdirs();
              filenameList.add(filename);
              if (filename.equals("WEB-INF/web.xml")) {
                final String paramNameTagStart = "<param-name>";
                final String paramValueTagStart = "<param-value>";
                final String paramVAlueTagEnd = "</param-value>";
                final String[] p = ENABLE_SUPPORT_CENTER
                    ? new String[] { "scan.start.folder", "support_center.code", "support_center.url", "software.dir",
                        "admin.username", "admin.password" }
                    : new String[] { "scan.start.folder", "software.dir", "admin.username", "admin.password" };
                @SuppressWarnings("resource")
                BufferedReader br = new BufferedReader(new InputStreamReader(zis));
                PrintWriter out = new PrintWriter(newFile);
                String line;
                int trigger = -1;
                while ((line = br.readLine()) != null) {
                  if (line.indexOf(paramNameTagStart) != -1) {
                    trigger = -1;
                    for (int i = 0; i < p.length; i++) {
                      if (line.indexOf(p[i]) != -1) {
                        trigger = i;
                      }
                    }
                    out.println(line);
                  } else if (line.indexOf(paramValueTagStart) != -1 && trigger != -1) {
                    String value = "";
                    if (trigger == 0) {
                      value = dataDir;
                    } else if (trigger == 1) {
                      value = scCode;
                    } else if (trigger == 2 && !scLocation.equals("none")) {
                      value = scLocation;
                    } else if (trigger == 3) {
                      value = "";
                    } else if (trigger == 4) {
                      value = adminUsername;
                    } else if (trigger == 5) {
                      value = adminPassword;
                    }
                    out.println("      " + paramValueTagStart + value + paramVAlueTagEnd);
                  } else {
                    out.println(line);
                  }
                }
                out.close();
              } else {
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                  fos.write(buffer, 0, len);
                }
                fos.close();
              }
            }
            ze = zis.getNextEntry();
          }
          zis.closeEntry();
          zis.close();

          String warFilename = softwareType == SoftwareType.TESTER ? "tester.war" : "smm.war";
          resp.setContentType("application/x-zip");
          resp.setHeader("Content-Disposition",
              "attachment; filename=\"" + URLEncoder.encode(warFilename, "UTF-8") + "\"");

          OutputStream out = resp.getOutputStream();
          ZipOutputStream zos = new ZipOutputStream(out);

          for (String filename : filenameList) {
            ze = new ZipEntry(filename);
            zos.putNextEntry(ze);
            File file = new File(expandDir, filename);
            FileInputStream in = new FileInputStream(file);
            int len;
            while ((len = in.read(buffer)) > 0) {
              zos.write(buffer, 0, len);
            }
            in.close();
            zos.closeEntry();
            file.delete();
          }

          zos.close();

          deleteFolder(expandDir);

          return;

        } catch (Exception e) {
          message = "Unable to unzip: " + e.getMessage();
          e.printStackTrace();
        }
      }
    }
    if (message != null) {
      req.setAttribute("message", message);
    }

    resp.setContentType("text/html;charset=UTF-8");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    List<String> versionList = new ArrayList<String>();
    File[] warFiles = null;
    if (ConnectionManager.getSoftwareDir() != null) {
      File file = ConnectionManager.getSoftwareDir();
      if (file.exists() && file.isDirectory()) {
        warFiles = file.listFiles(new FileFilter() {
          public boolean accept(File file) {
            if (file.getName().startsWith("tester-") && file.getName().endsWith(".war")) {
              return true;
            }
            return false;
          }
        });
        for (File warFile : warFiles) {
          String filename = warFile.getName();
          versionList.add(filename.substring("tester-".length(), filename.length() - ".war".length()));
        }
      }
    }
    Collections.sort(versionList);

    try {
      if (softwareType == SoftwareType.TESTER) {
        printHtmlHead(out, softwareType, "4. Download Tester", req);
        out.println("<h2>Step 4: Download IIS HL7 Interface Tester</h2>");
        out.println("<form action=\"DownloadServlet\" method=\"GET\">");
        out.println("  <input type=\"hidden\" name=\"softwareType\" value=\"Tester\">");
        out.println("  <input type=\"hidden\" name=\"" + FIELD_SC_LOCATION + "\" value=\"none\">");
        out.println("  <input type=\"hidden\" name=\"" + FIELD_SC_CODE + "\" value=\"\">");
        out.println("  <table class=\"boxed\">");
        out.println("  <tr class=\"boxed\">");
        out.println("    <th class=\"boxed\">Software Version</th>");
        out.println("    <td class=\"boxed\">");
        out.println("      <select name=\"" + FIELD_VERSION + "\">");
        out.println("        <option value=\"\">select</option>");
        for (String option : versionList) {
          out.println("        <option value=\"" + option + "\""
              + (SoftwareVersion.VERSION_FOR_TESTER_DOWNLOAD.equals(option) ? " selected=\"true\"" : "") + ">" + option
              + "</option>");
        }
        out.println("      </select>");
        out.println("    </td>");
        out.println("    <td class=\"boxed\">The version of the software to download.</td>");
        out.println("  </tr>");
        out.println("  <tr class=\"boxed\">");
        out.println("    <th class=\"boxed\">Root Data Folder</th>");
        out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_DATA_DIR + "\" size=\"40\" value=\""
            + folderName + "\"></td>");
        out.println(
            "    <td class=\"boxed\">A local data folder that has been created to hold data, sent messages, and test results.</td>");
        out.println("  </tr>");
        out.println("  <tr class=\"boxed\">");
        out.println("    <th class=\"boxed\">Username</th>");
        out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_ADMIN_USERNAME
            + "\" size=\"15\" value=\"admin\"></td>");
        out.println("    <td class=\"boxed\">Admin username to login with.</td>");
        out.println("  </tr>");
        out.println("  <tr class=\"boxed\">");
        out.println("    <th class=\"boxed\">Password</th>");
        out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_ADMIN_PASSWORD
            + "\" size=\"15\" value=\"admin\"></td>");
        out.println("    <td class=\"boxed\">Set the password you want to use to login with.</td>");
        out.println("  </tr>");
        out.println("  <tr class=\"boxed\">");
        out.println("    <th class=\"boxed\">&nbsp;</th>");
        out.println("    <td class=\"boxed\"><input type=\"submit\" name=\"action\" value=\"Download\"></td>");
        if (tomcatHome == null) {
          out.println(
              "    <td class=\"boxed\">Download and save as <code>tester.war</code> in the <code>webapps</code> folder"
                  + "<br/> located in your Tomcat installation directory.</td>");
        } else {
          out.println(
              "    <td class=\"boxed\">Download and save as <code>tester.war</code> in the <code>webapps</code> folder"
                  + "<br/> located in your Tomcat installation directory, <code>" + tomcatHome + "</code>.</td>");
        }
        out.println("  </tr>");
        out.println("</table>");
        out.println("</form>");
        printHtmlFoot(out);
      } else {
        printHtmlHead(out, softwareType, "4. Download SMM", req);
        out.println("<h2>Step 4: Download SMM</h2>");
        out.println("<form action=\"DownloadServlet\" method=\"GET\">");
        out.println("  <table class=\"boxed\">");
        out.println("  <tr class=\"boxed\">");
        out.println("    <th class=\"boxed\">Software Version</th>");
        out.println("    <td class=\"boxed\">");
        out.println("      <select name=\"" + FIELD_VERSION + "\">");
        out.println("        <option value=\"\">select</option>");
        for (String option : versionList) {
          out.println("        <option value=\"" + option + "\""
              + (SoftwareVersion.VERSION_FOR_SMM_DOWNLOAD.equals(option) ? " selected=\"true\"" : "") + ">" + option
              + "</option>");
        }
        out.println("      </select>");
        out.println("    </td>");
        out.println("    <td class=\"boxed\">The version of the software to download.</td>");
        out.println("  </tr>");
        out.println("  <tr class=\"boxed\">");
        out.println("    <th class=\"boxed\">SMM Root Folder</th>");
        out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_DATA_DIR + "\" size=\"20\" value=\""
            + folderName + "\"></td>");
        out.println("    <td class=\"boxed\">The root folder for the SMM data and configuration.</td>");
        out.println("  </tr>");
        if (ENABLE_SUPPORT_CENTER) {
          out.println("  <tr class=\"boxed\">");
          out.println("    <th class=\"boxed\">Support Center</th>");
          out.println("    <td class=\"boxed\">");
          out.println("      <select name=\"" + FIELD_SC_LOCATION + "\">");
          out.println("        <option value=\"\">select</option>");
          for (String[] option : SUPPORT_CENTER_LOCATION) {
            out.println("        <option value=\"" + option[0] + "\">" + option[1] + "</option>");
          }
          out.println("      </select>");
          out.println("    </td>");
          out.println(
              "    <td class=\"boxed\">Organization responsible for remotely logging and support this SMM. The organization selected will receive regular updates on the current status of the SMM.</td>");
          out.println("  </tr>");

          out.println("  <tr class=\"boxed\">");
          out.println("    <th class=\"boxed\">Support Center Code</th>");
          out.println("    <td class=\"boxed\">");
          out.println("      <select name=\"" + FIELD_SC_CODE + "\">");
          out.println("        <option value=\"\">select</option>");
          for (String[] option : SUPPORT_CENTER_CODE) {
            out.println("        <option value=\"" + option[0] + "\">" + option[1] + "</option>");
          }
          out.println("      </select>");
          out.println("    </td>");
          out.println("    <td class=\"boxed\">The code assigned by the support center to prioritize assistance.</td>");
          out.println("  </tr>");
        }
        out.println("  <tr class=\"boxed\">");
        out.println("    <th class=\"boxed\">&nbsp;</th>");
        out.println("    <td class=\"boxed\"><input type=\"submit\" name=\"action\" value=\"Download\"></td>");
        if (tomcatHome == null) {
          out.println(
              "    <td class=\"boxed\">Download and save as <code>smm.war</code> in the <code>webapps</code> folder"
                  + "<br/> located in your Tomcat installation directory.</td>");
        } else {
          out.println(
              "    <td class=\"boxed\">Download and save as <code>smm.war</code> in the <code>webapps</code> folder"
                  + "<br/> located in your Tomcat installation directory, <code>" + tomcatHome + "</code>.</td>");
        }
        out.println("  </tr>");
        out.println("</table>");
        out.println("</form>");
        printHtmlFoot(out);
      }
    } catch (Exception e) {
      e.printStackTrace();
      out.println("<p>Problem encountered: </p><pre>");
      e.printStackTrace(out);
      out.println("</pre>");
    } finally {
      out.close();
    }
  }

  private void deleteFolder(File node) {
    if (node.isDirectory()) {
      File[] files = node.listFiles();
      for (File file : files) {
        deleteFolder(file);
      }
    }
    node.delete();
  }
}
