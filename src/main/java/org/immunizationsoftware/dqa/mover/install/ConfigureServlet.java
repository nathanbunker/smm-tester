package org.immunizationsoftware.dqa.mover.install;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.SendData;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.HttpConnector;

public class ConfigureServlet extends ClientServlet
{

  public static final String TEMPLATE_DEFAULT_SOAP = "Default SOAP";
  public static final String TEMPLATE_DEFAULT_POST = "Default POST";
  public static final String TEMPLATE_ASIIS_PROD = "ASIIS Production";
  public static final String TEMPLATE_ASIIS_TEST = "ASIIS Test";
  public static final String TEMPLATE_NMSIIS_RAW_PROD = "NMSIIS Raw Production";
  public static final String TEMPLATE_NMSIIS_RAW_UAT = "NMSIIS Raw UAT";
  public static final String TEMPLATE_NV_WEBIZ_PRODUCTION = "NV WebIZ Production";
  public static final String TEMPLATE_NV_WEBIZ_TESTING = "NV WebIZ Testing";

  public static final String[] TEMPLATES = { TEMPLATE_DEFAULT_SOAP, TEMPLATE_DEFAULT_POST, TEMPLATE_ASIIS_PROD,
      TEMPLATE_ASIIS_TEST, TEMPLATE_NMSIIS_RAW_PROD, TEMPLATE_NMSIIS_RAW_UAT, TEMPLATE_NV_WEBIZ_TESTING,
      TEMPLATE_NV_WEBIZ_PRODUCTION };

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

              connector.setUserid(cc.getUserid());
              connector.setOtherid(cc.getOtherid());
              connector.setPassword(cc.getPassword());
              connector.setFacilityid(cc.getFacilityid());
              connector.setEnableTimeStart(safe(req.getParameter(ConnectionConfiguration.FIELD_ENABLE_TIME_START)));
              connector.setEnableTimeEnd(safe(req.getParameter(ConnectionConfiguration.FIELD_ENABLE_TIME_END)));
              if (cc.isUseridRequired() && connector.getUserid().equals("")) {
                message = "You must indicate a " + cc.getUseridLabel();
              } else if (cc.isPasswordRequired() && connector.getPassword().equals("")) {
                message = "You must indicate a " + cc.getPasswordLabel();
              } else if (cc.isFacilityidRequired() && connector.getFacilityid().equals("")) {
                message = "You must indicate a " + cc.getFacilityidLabel();
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
      printHtmlHead(out, "2. Configure", req);

      if (folderName != null) {
        cc.setFolderName(folderName);
      }
      if (baseDir != null) {
        cc.setBaseDir(baseDir);
      }

      out.println("<h2>Step 2: Configure</h2>");
      cc.printForm(out);
      out.println("<form method=\"GET\" action=\"InstallServlet\">");
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
      if (templateName.equals(TEMPLATE_NMSIIS_RAW_UAT) || templateName.equals(TEMPLATE_NMSIIS_RAW_PROD)) {
        HttpConnector httpConnector = (HttpConnector) connector;
        httpConnector.setFacilityid("NMSIIS");
        httpConnector.setAuthenticationMethod(HttpConnector.AuthenticationMethod.HEADER);
        httpConnector.setAckType(AckAnalyzer.AckType.NMSIIS);
        httpConnector.setFieldName(HttpConnector.USERID, "orgCode");
        httpConnector.setFieldName(HttpConnector.PASSWORD, "pinCode");
        httpConnector.setFieldName(HttpConnector.FACILITYID, "service");
        httpConnector.setAuthenticationMethod(HttpConnector.AuthenticationMethod.HEADER);
        httpConnector.setCustomTransformations("MSH-3=RPMS\n" + "MSH-4=[OTHERID]\n" + "MSH-6=NMSIIS\n"
            + "insert segment BHS first\n" + "insert segment BTS last\n" + "insert segment FHS first\n"
            + "insert segment FTS last\n" + "FHS-8=CR\n" + "BSH-8=CR\n" + "FHS-9=[FILENAME]\n" + "FTS-1=1\n"
            + "BTS-1=1\n" + "FTS-2=CR\n" + "BTS-2=CR\n" + "FHS-4=[USERID]\n" + "BHS-4=[USERID]\n");
      } else if (templateName.equals(TEMPLATE_ASIIS_PROD) || templateName.equals(TEMPLATE_ASIIS_TEST)) {
        HttpConnector httpConnector = (HttpConnector) connector;
        httpConnector.setCustomTransformations("MSH-3=RPMS\n" + "MSH-4=[FACILITYID]\n" + "PV1-10=\n");
      } else if (templateName.equals(TEMPLATE_NV_WEBIZ_TESTING) || templateName.equals(TEMPLATE_NV_WEBIZ_PRODUCTION)) {
        HttpConnector httpConnector = (HttpConnector) connector;
        httpConnector.stripXML();
        httpConnector.setFieldName(HttpConnector.USERID, "userName");
        httpConnector.setFieldName(HttpConnector.PASSWORD, "password");
        httpConnector.setFieldName(HttpConnector.MESSAGEDATA, "flatWire");
        httpConnector.setCustomTransformations("MSH-4=[OTHERID]\n" + "MSH-12=2.3.1" + "MSH-6=NV0000\n" + "NK1-2.7=L\n"
            + "PID-11.7=P\n");
        httpConnector.setAckType(AckAnalyzer.AckType.WEBIZ);
      }
    }
  }

  private void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName != null) {
      cc.setLabel(templateName);
      if (templateName.equals(TEMPLATE_DEFAULT_SOAP)) {
        cc.setType(ConnectorFactory.TYPE_SOAP);
      } else if (templateName.equals(TEMPLATE_DEFAULT_POST)) {
        cc.setType(ConnectorFactory.TYPE_POST);
      } else if (templateName.equals(TEMPLATE_ASIIS_PROD)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://app.azdhs.gov/phs/asiis/hl7post/");
        cc.setFacilityidShow(true);
        cc.setFacilityidLabel("IRMS ID");
        cc.setTypeShow(false);
        cc.setInstructions("In order to connect to ASIIS Production you will need to request a Username, Password and IRMS ID from the <a href=\"https://www.asiis.state.az.us/\" target=\"_blank\">ASIIS User Support help desk</a>. Please provide the User Id, Password, and IRMS ID before continuing. ");
        cc.setReceiverName("ASIIS");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(true);
      } else if (templateName.equals(TEMPLATE_ASIIS_TEST)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://appqa.azdhs.gov/phs/asiis/hl7post/");
        cc.setFacilityidShow(true);
        cc.setFacilityidLabel("IRMS ID");
        cc.setTypeShow(false);
        cc.setInstructions("In order to connect to ASIIS Test you will need to request a Username, Password and IRMS ID from the <a href=\"https://test-asiis.azdhs.gov/\" target=\"_blank\">ASIIS User Support help desk</a>. Please provide the User Id, Password and IRMS ID before continuing.");
        cc.setReceiverName("ASIIS");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(true);
      } else if (templateName.equals(TEMPLATE_NMSIIS_RAW_UAT)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://www.nmhit.org/nmsiistest/rhapsody/receive");
        cc.setTypeShow(false);
        cc.setOtheridShow(true);
        cc.setOtheridRequired(true);
        cc.setUseridLabel("Parent Org Code");
        cc.setOtheridLabel("Child Org Code");
        cc.setPasswordLabel("Pin Code");
        cc.setInstructions("Contact NMSIIS for connecting information.");
        cc.setReceiverName("NMSIIS");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidShow(false);
      } else if (templateName.equals(TEMPLATE_NMSIIS_RAW_PROD)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://www.nmhit.org/nmsiis/rhapsody/receive");
        cc.setTypeShow(false);
        cc.setOtheridShow(true);
        cc.setOtheridRequired(true);
        cc.setUseridLabel("Parent Org Code");
        cc.setOtheridLabel("Child Org Code");
        cc.setPasswordLabel("Pin Code");
        cc.setInstructions("Contact NMSIIS for connecting information.");
        cc.setReceiverName("NMSIIS");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidShow(false);
        cc.setEnableTimeShow(true);
        cc.setEnableTimeEnd("18:00");
        cc.setEnableTimeStart("06:00");
      } else if (templateName.equals(TEMPLATE_NV_WEBIZ_TESTING) || templateName.equals(TEMPLATE_NV_WEBIZ_PRODUCTION)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setInstructions("Contact NV WebIZ for connecting information.");
        if (templateName.equals(TEMPLATE_NV_WEBIZ_PRODUCTION)) {
          cc.setUrl("https://webiz.nv.gov/HL7EngineAuthentication/Service.asmx/ExecuteHL7Message");
        } else {
          cc.setUrl("https://webiztest.nv.gov/HL7EngineAuthentication/Service.asmx/ExecuteHL7Message");
        }
        cc.setTypeShow(false);
        cc.setUseridLabel("User Name");
        cc.setUseridRequired(true);
        cc.setFacilityidShow(false);
        cc.setOtheridLabel("Facility Id");
        cc.setPasswordLabel("Password");
        cc.setPasswordRequired(true);
        cc.setOtheridShow(true);
        cc.setOtheridRequired(true);
        cc.setReceiverName("NV WebIZ");
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
