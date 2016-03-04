package org.immunizationsoftware.dqa.mover.install;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.mover.install.templates.ConnectionTemplate;
import org.immunizationsoftware.dqa.mover.install.templates.ConnectionTemplateFactory;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;

public class ConfigureServlet extends ClientServlet
{

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    SoftwareType softwareType = getSoftwareType(req);

    HttpSession session = req.getSession(true);
    String folderName = req.getParameter(PrepareServlet.FOLDER_NAME);
    if (folderName != null) {
      if (folderName.startsWith("\\") || folderName.startsWith("/")) {
        folderName = folderName.substring(1);
      }
      session.setAttribute(PrepareServlet.FOLDER_NAME, folderName);
    }
    String baseDir = req.getParameter(PrepareServlet.BASE_DIR);
    if (baseDir != null) {
      if (!baseDir.endsWith("\\") && !baseDir.endsWith("/")) {
        if (baseDir.indexOf("/") == -1) {
          baseDir = baseDir + "\\";
        } else {
          baseDir = baseDir + "/";
        }
      }
      session.setAttribute(PrepareServlet.BASE_DIR, baseDir);
    }
    String templateName = req.getParameter(PrepareServlet.TEMPLATE_NAME);
    if (templateName != null) {
      session.setAttribute(PrepareServlet.TEMPLATE_NAME, templateName);
    } else {
      templateName = (String) session.getAttribute(PrepareServlet.TEMPLATE_NAME);
    }
    ConnectionConfiguration cc = new ConnectionConfiguration();
    setupConfiguration(templateName, cc);
    String action = req.getParameter("action");
    if (action != null) {
      if (action.equals("Step 2: Download and Save")) {
        cc.setType(req.getParameter(ConnectionConfiguration.FIELD_TYPE));
        cc.setLabel(req.getParameter(ConnectionConfiguration.FIELD_LABEL));
        cc.setUrl(req.getParameter(ConnectionConfiguration.FIELD_URL));
        String message = null;
        if (cc.getType().equals("")) {
          message = "You must select a transport method type in order to create a connection";
        } else if (cc.getLabel().equals("")) {
          message = "You must give a label for this connection";
        } else if (cc.getUrl().equals("")) {
          message = "You must indicate a " + cc.getUrlLabel() + " for this connection";
        }

        if (message == null) {
          try {
            Connector connector = ConnectorFactory.getConnector(cc.getType(), cc.getLabel(), cc.getUrl());

            if (connector == null) {
              message = "Unrecognized connection type: " + cc.getType();
            } else {
              cc.setFacilityid(req.getParameter(ConnectionConfiguration.FIELD_FACILITYID));
              cc.setPassword(req.getParameter(ConnectionConfiguration.FIELD_PASSWORD));
              cc.setUserid(req.getParameter(ConnectionConfiguration.FIELD_USERID));
              cc.setOtherid(req.getParameter(ConnectionConfiguration.FIELD_OTHERID));
              cc.setKeyStorePassword(req.getParameter(ConnectionConfiguration.FIELD_KEY_STORE_PASSWORD));

              connector.setUserid(cc.getUserid());
              connector.setOtherid(cc.getOtherid());
              connector.setPassword(cc.getPassword());
              connector.setFacilityid(cc.getFacilityid());
              connector.setKeyStorePassword(cc.getKeyStorePassword());
              connector.setEnableTimeStart(safe(req.getParameter(ConnectionConfiguration.FIELD_ENABLE_TIME_START)));
              connector.setEnableTimeEnd(safe(req.getParameter(ConnectionConfiguration.FIELD_ENABLE_TIME_END)));
              if (cc.isUseridRequired() && connector.getUserid().equals("")) {
                message = "You must indicate a " + cc.getUseridLabel();
              } else if (cc.isPasswordRequired() && connector.getPassword().equals("")) {
                message = "You must indicate a " + cc.getPasswordLabel();
              } else if (cc.isFacilityidRequired() && connector.getFacilityid().equals("")) {
                message = "You must indicate a " + cc.getFacilityidLabel();
              } else if (cc.isKeyStorePasswordRequired() && connector.getKeyStorePassword().equals("")) {
                message = "You must indicate a " + cc.getKeyStorePasswordLabel();
              }

              if (cc.isEnableTimeRequired()) {
                if (connector.getEnableTimeEnd().equals("")) {
                  message = "You must indicate a SMM enabled end time";
                } else {
                  if (SendData.makeDate(connector.getEnableTimeEnd()) == null) {
                    message = "The SMM enabled end time is invalid format, please use HH:MM with the HH indicating a value between 0 and 23";
                  }

                }
                if (connector.getEnableTimeStart().equals("")) {
                  message = "You must indicate a SMM enabled start time";
                } else {
                  if (SendData.makeDate(connector.getEnableTimeStart()) == null) {
                    message = "The SMM enabled start time is invalid format, please use HH:MM with the HH indicating a value between 0 and 23";
                  }
                }
              }

              if (message == null) {
                setupConnection(templateName, connector);
                resp.setContentType("text/plain;charset=UTF-8");
                resp.setHeader("Content-Disposition",
                    "attachment; filename=\"" + URLEncoder.encode("smm.config.txt", "UTF-8") + "\"");
                PrintWriter out = new PrintWriter(resp.getOutputStream());
                out.print(connector.getScript());
                out.close();
                return;
              }
            }
          } catch (Exception e) {
            message = "Unable to configure connector: " + e.getMessage();
            e.printStackTrace();
          }
        }
        if (message != null) {
          req.setAttribute("message", message);
        }
      }
    }
    resp.setContentType("text/html;charset=UTF-8");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    try {
      printHtmlHead(out, softwareType, "2. Configure", req);

      if (folderName != null) {
        cc.setFolderName(folderName);
      }
      if (baseDir != null) {
        cc.setBaseDir(baseDir);
      }

      out.println("<h2>Step 2: Configure</h2>");
      cc.printForm(out);
      out.println("<form method=\"GET\" action=\"InstallServlet\">");
      if (softwareType == SoftwareType.TESTER) {
        out.println("  <input type=\"hidden\" name=\"softwareType\" value=\"Tester\">");
      }
      out.println("    <p>After you have saved <code>smm.config.txt</code> you are ready for <input type=\"submit\" value=\"Step 3: Install\" name=\"action\"></p>");
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

  private void setupConnection(String templateName, Connector connector) {
    if (templateName != null) {
      ConnectionTemplate connectionTemplate = ConnectionTemplateFactory.getConnectionTemplate(templateName);
      if (connectionTemplate != null)
      {
        connectionTemplate.setupConnection(templateName, connector);
      }
    }
  }

  private void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName != null) {
      cc.setLabel(templateName);
      ConnectionTemplate connectionTemplate = ConnectionTemplateFactory.getConnectionTemplate(templateName);
      if (connectionTemplate != null)
      {
        connectionTemplate.setupConfiguration(templateName, cc);
      }
    } else {
      cc.setInstructions("This is the default configuration with no preset values. If you would like specific instruction based on the system you are working to connect with, please return to Step 1: Prepare and follow the instructions. ");
    }
  }

  private static String safe(String s) {
    if (s == null) {
      return "";
    }
    return s;
  }
}
