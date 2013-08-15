package org.immunizationsoftware.dqa.install;

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

import org.immunizationsoftware.dqa.mover.ManagerServlet;

public class DownloadServlet extends ClientServlet
{

  private static final String FIELD_VERSION = "version";
  private static final String FIELD_URL = "url";
  private static final String FIELD_USERNAME = "username";
  private static final String FIELD_PASSWORD = "password";

  public static class VersionType
  {
    private String id = "";
    private String driver = "";
    private String urlExample = "";
    private String dialect = "";

    public String getId()
    {
      return id;
    }

    public void setId(String id)
    {
      this.id = id;
    }

    public String getDriver()
    {
      return driver;
    }

    public void setDriver(String driver)
    {
      this.driver = driver;
    }

    public String getUrlExample()
    {
      return urlExample;
    }

    public void setUrlExample(String urlExample)
    {
      this.urlExample = urlExample;
    }

    public String getDialect()
    {
      return dialect;
    }

    public void setDialect(String dialect)
    {
      this.dialect = dialect;
    }

    public VersionType(String id, String dialect, String driver, String urlExample) {
      this.id = id;
      this.driver = driver;
      this.urlExample = urlExample;
      this.dialect = dialect;
    }
  }

  private static final VersionType ORACLE_VERSION = new VersionType("oracle", "org.hibernate.dialect.Oracle10gDialect",
      "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@localhost:1521:dqa");
  private static final VersionType MYSQL_VERSION = new VersionType("mysql", "org.hibernate.dialect.MySQLDialect", "com.mysql.jdbc.Driver",
      "jdbc:mysql://localhost/dqa");
  private static final VersionType HSQLDB_VERSION = new VersionType("hsqldb", "org.hibernate.dialect.HSQLDialect", "org.hsqldb.jdbcDriver",
      "jdbc:hsqldb:data/dqa");

  private static final VersionType[] VERSION_TYPES = { HSQLDB_VERSION, MYSQL_VERSION, ORACLE_VERSION };

  private static Random random = new Random();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    HttpSession session = req.getSession(true);

    String tomcatHome = req.getParameter("tomcatHome");
    if (tomcatHome != null)
    {
      session.setAttribute("tomcatHome", tomcatHome);
    }
    if (tomcatHome == null)
    {
      tomcatHome = (String) session.getAttribute("tomcatHome");
    }

    String message = null;
    String action = req.getParameter("action");
    String version = req.getParameter(FIELD_VERSION);
    if (version == null)
    {
      version = "";
    }
    String url = req.getParameter(FIELD_URL);
    if (url == null)
    {
      url = "";
    }
    String username = req.getParameter(FIELD_USERNAME);
    if (username == null)
    {
      username = "";
    }
    String password = req.getParameter(FIELD_PASSWORD);
    if (password == null)
    {
      password = "";
    }
    if (action != null && action.equals("Download"))
    {
      if (version == null || version.equals(""))
      {
        message = "Software Version is required";
      } else if (url.equals(""))
      {
        message = "URL is required";
      }
      if (message == null)
      {
        File rootDir = ManagerServlet.getSoftwareDir();
        File versionWar = new File(rootDir, "dqa-" + version + ".war");
        File expandDir = null;
        while (expandDir == null)
        {
          String uniqueId = String.valueOf(Math.abs(session.hashCode())) + String.valueOf(random.nextInt(100));
          expandDir = new File(rootDir, "dqa-" + uniqueId);
          if (expandDir.exists())
          {
            expandDir = null;
          }
        }
        List<String> filenameList = new ArrayList<String>();
        byte[] buffer = new byte[1024];
        try
        {
          expandDir.mkdir();
          ZipInputStream zis = new ZipInputStream(new FileInputStream(versionWar));
          ZipEntry ze = zis.getNextEntry();
          while (ze != null)
          {
            String filename = ze.getName();
            File newFile = new File(expandDir, filename);
            if (ze.isDirectory())
            {
              newFile.mkdirs();
            } else
            {
              new File(newFile.getParent()).mkdirs();
              filenameList.add(filename);
              if (filename.equals("WEB-INF/classes/hibernate.cfg.xml"))
              {
                final String paramDriver = "hibernate.connection.driver_class";
                final String paramUrl = "hibernate.connection.url";
                final String paramUsername = "hibernate.connection.username";
                final String paramDialect = "hibernate.connection.url";
                final String paramPassword = "connection.password";

                final String[] params = { paramDriver, paramUrl, paramUsername, paramDialect, paramPassword };

                @SuppressWarnings("resource")
                BufferedReader br = new BufferedReader(new InputStreamReader(zis));
                PrintWriter out = new PrintWriter(newFile);
                String line;
                while ((line = br.readLine()) != null)
                {
                  boolean replaced = false;
                  for (String param : params)
                  {
                    int startPos;
                    String startCheck = "<property name=\"" + param + "\">";
                    String endCheck = "</property>";
                    if ((startPos = line.indexOf(startCheck)) != -1)
                    {
                      startPos += startCheck.length();
                      int endPos = line.indexOf(endCheck, startPos);
                      if (endPos != -1)
                      {
                        if (param.equals(paramDriver))
                        {
                          out.println("    " + startCheck + "" + endCheck);
                          replaced = true;
                        } else if (param.equals(paramUrl))
                        {
                          out.println("    " + startCheck + "" + endCheck);
                          replaced = true;
                        } else if (param.equals(paramUsername))
                        {
                          out.println("    " + startCheck + "" + endCheck);
                          replaced = true;
                        } else if (param.equals(paramDialect))
                        {
                          out.println("    " + startCheck + "" + endCheck);
                          replaced = true;
                        } else if (param.equals(paramPassword))
                        {
                          out.println("    " + startCheck + "" + endCheck);
                          replaced = true;
                        }
                      }
                      break;
                    }
                  }
                  if (!replaced)
                  {
                    out.println(line);
                  }
                }
                out.close();
              } else
              {
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0)
                {
                  fos.write(buffer, 0, len);
                }
                fos.close();
              }
            }
            ze = zis.getNextEntry();
          }
          zis.closeEntry();
          zis.close();

          resp.setContentType("application/x-zip");
          resp.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode("dqa.war", "UTF-8") + "\"");

          OutputStream out = resp.getOutputStream();
          ZipOutputStream zos = new ZipOutputStream(out);

          for (String filename : filenameList)
          {
            ze = new ZipEntry(filename);
            zos.putNextEntry(ze);
            File file = new File(expandDir, filename);
            FileInputStream in = new FileInputStream(file);
            int len;
            while ((len = in.read(buffer)) > 0)
            {
              zos.write(buffer, 0, len);
            }
            in.close();
            zos.closeEntry();
            file.delete();
          }

          zos.close();

          deleteFolder(expandDir);

          return;

        } catch (Exception e)
        {
          message = "Unable to unzip: " + e.getMessage();
          e.printStackTrace();
        }
      }
    }
    if (message != null)
    {
      req.setAttribute("message", message);
    }

    resp.setContentType("text/html;charset=UTF-8");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    List<String> versionList = new ArrayList<String>();
    File[] warFiles = null;
    if (ManagerServlet.getSoftwareDir() != null)
    {
      File file = ManagerServlet.getSoftwareDir();
      if (file.exists() && file.isDirectory())
      {
        warFiles = file.listFiles(new FileFilter() {
          public boolean accept(File file)
          {
            if (file.getName().startsWith("dqa-"))
            {
              for (VersionType versionType : VERSION_TYPES)
              {
                if (file.getName().endsWith("-" + versionType.getId() + ".war"))
                {
                  return true;
                }
              }
            }
            return false;
          }
        });
        for (File warFile : warFiles)
        {
          String filename = warFile.getName();
          versionList.add(filename.substring("dqa-".length(), filename.length() - ".war".length()));
        }
      }
    }
    Collections.sort(versionList);

    try
    {
      printHtmlHead(out, "4. Download", req);
      out.println("<h2>Step 4: Download</h2>");
      out.println("<form action=\"DownloadServlet\" method=\"GET\">");
      out.println("  <table class=\"boxed\">");
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">Software Version</th>");
      out.println("    <td class=\"boxed\">");
      out.println("      <select name=\"" + FIELD_VERSION + "\">");
      out.println("        <option value=\"\">select</option>");
      for (String option : versionList)
      {
        out.println("        <option value=\"" + option + "\"" + (version.equals(option) ? " selected=\"true\"" : "") + ">" + option + "</option>");
      }
      out.println("      </select>");
      out.println("    </td>");
      out.println("    <td class=\"boxed\">The version of the software to download.</td>");
      out.println("  </tr>");
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">Database Url</th>");
      out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_URL + "\" value=\"" + url + "\" size=\"50\"></td>");
      out.println("    <td class=\"boxed\">The URL to the database. Examples:");
      for (VersionType versionType : VERSION_TYPES)
      {
        out.println("<br/>" + versionType.getDriver() + ": <code>"  + versionType.getUrlExample() + "</code>");
      }
      out.println("    </td>");
      out.println("  </tr>");
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">Database Username</th>");
      out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_USERNAME + "\" value=\"" + username + "\"></td>");
      out.println("    <td class=\"boxed\">The username to access the database.</td>");
      out.println("  </tr>");
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">Database Password</th>");
      out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_PASSWORD + "\" value=\"" + password + "\"></td>");
      out.println("    <td class=\"boxed\">The password to access the database.</td>");
      out.println("  </tr>");
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">&nbsp;</th>");
      out.println("    <td class=\"boxed\"><input type=\"submit\" name=\"action\" value=\"Download\"></td>");
      out.println("    <td class=\"boxed\">Download and save as <code>dqa.war</code> in the <code>webapps</code> folder"
          + "<br/> located in your Tomcat installation directory.</td>");
      out.println("  </tr>");
      out.println("</table>");
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

  private void deleteFolder(File node)
  {
    if (node.isDirectory())
    {
      File[] files = node.listFiles();
      for (File file : files)
      {
        deleteFolder(file);
      }
    }
    node.delete();
  }
}
