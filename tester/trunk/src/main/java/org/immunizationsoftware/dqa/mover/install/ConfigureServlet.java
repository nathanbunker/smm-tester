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
import org.immunizationsoftware.dqa.tester.connectors.CASoapConnector;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.HttpConnector;
import org.immunizationsoftware.dqa.tester.connectors.ILConnector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.SoapConnector;

public class ConfigureServlet extends ClientServlet
{

  public static final String TEMPLATE_DEFAULT_SOAP = "Default SOAP";
  public static final String TEMPLATE_DEFAULT_POST = "Default POST";
  public static final String TEMPLATE_AK_VACTRAK_TEST = "AK VacTrAK Test";
  public static final String TEMPLATE_AK_VACTRAK_PROD = "AK VacTrAK Prod";
  public static final String TEMPLATE_AZ_ASIIS_TEST = "AZ ASIIS Test";
  public static final String TEMPLATE_AZ_ASIIS_PROD = "AZ ASIIS Prod";
  public static final String TEMPLATE_CA_CAIR_TEST = "CA CAIR Test";
  public static final String TEMPLATE_CA_CAIR_PROD = "CA CAIR Prod";
  public static final String TEMPLATE_CA_SDIR_TEST = "CA SDIR Test";
  public static final String TEMPLATE_CA_SDIR_PROD = "CA SDIR Prod";
  public static final String TEMPLATE_IA_IRIS_TEST = "IA IRIS Test";
  public static final String TEMPLATE_IA_IRIS_PROD = "IA IRIS Prod";
  public static final String TEMPLATE_IL_ICARE_TEST = "IL I-Care Test";
  public static final String TEMPLATE_IL_ICARE_PROD = "IL I-Care Prod";
  public static final String TEMPLATE_MN_MIIC_TEST = "MN MIIC Test";
  public static final String TEMPLATE_MN_MIIC_PROD = "MN MIIC Prod";
  public static final String TEMPLATE_MS_MIX_TEST = "MS MIX Test";
  public static final String TEMPLATE_MS_MIX_PROD = "MS MIX Prod";
  public static final String TEMPLATE_MT_IMMTRAX_TEST = "MT imMTrax Test";
  public static final String TEMPLATE_MT_IMMTRAX_PROD = "MT imMTrax Prod";
  public static final String TEMPLATE_NM_SIIS_RAW_UAT = "NM SIIS Raw UAT";
  public static final String TEMPLATE_NMSIIS_RAW_PROD = "NM SIIS Raw Prod";
  public static final String TEMPLATE_NE_SIIS_TEST = "NE SIIS Test";
  public static final String TEMPLATE_NE_SIIS_PROD = "NE SIIS Prod";
  public static final String TEMPLATE_NV_WEBIZ_TEST = "NV WebIZ Test";
  public static final String TEMPLATE_NV_WEBIZ_PROD = "NV WebIZ Prod";
  public static final String TEMPLATE_RI_KIDSNET_TEST = "RI KIDSNET Test";
  public static final String TEMPLATE_RI_KIDSNET_PROD = "RI KIDSNET Prod";
  public static final String TEMPLATE_WA_IIS_TEST = "WA IIS Test";
  public static final String TEMPLATE_WA_IIS_PROD = "WA IIS Prod";

  public static final String[] TEMPLATES = { TEMPLATE_DEFAULT_SOAP, TEMPLATE_DEFAULT_POST, TEMPLATE_AK_VACTRAK_TEST,
      TEMPLATE_AK_VACTRAK_PROD, TEMPLATE_AZ_ASIIS_TEST, TEMPLATE_AZ_ASIIS_PROD, TEMPLATE_CA_CAIR_TEST,
      TEMPLATE_CA_CAIR_PROD, TEMPLATE_CA_SDIR_TEST, TEMPLATE_CA_SDIR_PROD, TEMPLATE_IA_IRIS_TEST,
      TEMPLATE_IA_IRIS_PROD, TEMPLATE_IL_ICARE_TEST, TEMPLATE_IL_ICARE_PROD, TEMPLATE_MT_IMMTRAX_TEST,
      TEMPLATE_MT_IMMTRAX_PROD, TEMPLATE_MN_MIIC_TEST, TEMPLATE_MN_MIIC_PROD, TEMPLATE_MS_MIX_TEST,
      TEMPLATE_MS_MIX_PROD, TEMPLATE_NM_SIIS_RAW_UAT, TEMPLATE_NMSIIS_RAW_PROD, TEMPLATE_NE_SIIS_TEST,
      TEMPLATE_NE_SIIS_PROD, TEMPLATE_NV_WEBIZ_TEST, TEMPLATE_NV_WEBIZ_PROD, TEMPLATE_RI_KIDSNET_TEST,
      TEMPLATE_RI_KIDSNET_PROD, TEMPLATE_WA_IIS_TEST, TEMPLATE_WA_IIS_PROD };

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
      if (templateName.equals(TEMPLATE_NM_SIIS_RAW_UAT) || templateName.equals(TEMPLATE_NMSIIS_RAW_PROD)) {
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
            + "BTS-1=1\n" + "FTS-2=CR\n" + "BTS-2=CR\n" + "FHS-4=[USERID]\n" + "BHS-4=[USERID]\n"
            + "insert segment IN1 before ORC if missing\n" + "insert segment IN2 after IN1 if missing\n" + "IN1-1=1\n"
            + "fix missing mother maiden first\n" + "remove observation 64994-7 if 18+\n");
      } else if (templateName.equals(TEMPLATE_AZ_ASIIS_PROD) || templateName.equals(TEMPLATE_AZ_ASIIS_TEST)) {
        HttpConnector httpConnector = (HttpConnector) connector;
        httpConnector.setCustomTransformations("MSH-3=RPMS\n" + "MSH-4=[FACILITYID]\n" + "MSH-5=ASIIS\n" + "PV1-10=\n"
            + "fix ampersand\n");
      } else if (templateName.equals(TEMPLATE_CA_CAIR_TEST) || templateName.equals(TEMPLATE_CA_CAIR_PROD)) {
        CASoapConnector caSoapConnector = (CASoapConnector) connector;
        caSoapConnector.setCustomTransformations("MSH-4=[USERID]\n" + "MSH-6=[OTHERID]\n"
            + "insert segment PD1 after PID if missing\n" + "PD1-12=[MAP ''=>'N']\n" + "MSH-7=[TRUNC 14]\n"
            + "RXA-9=[MAP ''=>'01']\n" + "run procedure Remove_Vaccination_Groups where RXA-20 equals 'RE'\n");
      } else if (templateName.equals(TEMPLATE_CA_SDIR_TEST) || templateName.equals(TEMPLATE_CA_SDIR_PROD)) {
        // HttpConnector httpConnector = (HttpConnector) connector;
        // nothing to do
      } else if (templateName.equals(TEMPLATE_IA_IRIS_TEST) || templateName.equals(TEMPLATE_IA_IRIS_PROD)) {
        SoapConnector soapConnector = (SoapConnector) connector;
        soapConnector.setAckType(AckAnalyzer.AckType.MIIC);
        soapConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
      } else if (templateName.equals(TEMPLATE_IL_ICARE_PROD) || templateName.equals(TEMPLATE_IL_ICARE_TEST)) {
        ILConnector ilConnector = (ILConnector) connector;
        ilConnector.setCustomTransformations("MSH-4=[OTHERID]\n");
        ilConnector.setAckType(AckAnalyzer.AckType.DEFAULT);
        ilConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
      } else if (templateName.equals(TEMPLATE_NV_WEBIZ_TEST) || templateName.equals(TEMPLATE_NV_WEBIZ_PROD)) {
        HttpConnector httpConnector = (HttpConnector) connector;
        httpConnector.stripXML();
        httpConnector.setFieldName(HttpConnector.USERID, "userName");
        httpConnector.setFieldName(HttpConnector.PASSWORD, "password");
        httpConnector.setFieldName(HttpConnector.MESSAGEDATA, "flatWire");
        httpConnector.setCustomTransformations("MSH-4=[OTHERID]\n" + "MSH-6=NV0000 \n" + "PID-3.4=IHS_FACILITY\n"
            + "PID-6.7=M\n" + "PID-11.7=P\n" + "PID-22.3=[MAP 'CDCREC'=>'HL70189']\n" + "NK1-2.7*=L\n" + "NK1-7.1*=\n"
            + "NK1-7.2*=\n" + "NK1-7.3*=\n" + "PD1-11.1=\n" + "PD1-18.1=\n" + "ORC-2.2*=\n" + "ORC-3.1*=[TRUNC 20]\n"
            + "ORC-3.2*=\n" + "ORC-12.1*=\n" + "ORC-17.1*=\n" + "ORC-17.2*=\n" + "ORC-17.3*=\n" + "ORC-17.4*=\n"
            + "RXA-7.1*=[MAP 'mL'=>'ML']\n" + "RXA-7.3*=[MAP 'UCUM'=>'ISO+']\n" + "RXA-10.1*=\n"
            + "RXR-2.1*=[MAP 'LI'=>'LLFA']\n" + "remove observation 30956-7\n" + "remove empty observations\n");
        httpConnector.setAckType(AckAnalyzer.AckType.WEBIZ);
        httpConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
      } else if (templateName.equals(TEMPLATE_MN_MIIC_PROD) || templateName.equals(TEMPLATE_MN_MIIC_TEST)) {
        SoapConnector soapConnector = (SoapConnector) connector;
        soapConnector.setCustomTransformations("MSH-4=[OTHERID]\n" + "MSH-5=MIIC \n" + "MSH-6=MIIC \n"
            + "fix missing mother maiden first \n");
        soapConnector.setAckType(AckAnalyzer.AckType.MIIC);
        soapConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
      } else if (templateName.equals(TEMPLATE_NE_SIIS_PROD) || templateName.equals(TEMPLATE_NE_SIIS_TEST)) {
        SoapConnector soapConnector = (SoapConnector) connector;
        soapConnector.setCustomTransformations("MSH-4=[OTHERID]\n" + "remove repeat PID-3.5 valued MA\n");
        soapConnector.setAckType(AckAnalyzer.AckType.MIIC);
        soapConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
      } else if (templateName.equals(TEMPLATE_RI_KIDSNET_TEST) || templateName.equals(TEMPLATE_RI_KIDSNET_PROD)) {
        HttpConnector httpConnector = (HttpConnector) connector;
        httpConnector.setCustomTransformations("MSH-4=[FACILITYID]\n" + "MSH-22=[OTHERID]\n");
      } else if (templateName.equals(TEMPLATE_MS_MIX_TEST) || templateName.equals(TEMPLATE_MS_MIX_PROD)) {
        HttpConnector httpConnector = (HttpConnector) connector;
        httpConnector.setCustomTransformations("MSH-4=[FACILITYID]\n");
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
      } else if (templateName.equals(TEMPLATE_AK_VACTRAK_TEST)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://vactrakihub.alaska.gov/ihubtst/HL7Server");
        cc.setFacilityidShow(false);
        cc.setTypeShow(false);
        cc.setInstructions("Contact AK VacTrAK to obtain username and password. ");
        cc.setReceiverName("AK VacTrAK");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(false);
      } else if (templateName.equals(TEMPLATE_AK_VACTRAK_PROD)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://vactrakihub.alaska.gov/ihubtst/HL7Server");
        cc.setFacilityidShow(false);
        cc.setTypeShow(false);
        cc.setInstructions("Contact AK VacTrAK to obtain username and password. ");
        cc.setReceiverName("AK VacTrAK");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(false);
      } else if (templateName.equals(TEMPLATE_AZ_ASIIS_PROD)) {
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
      } else if (templateName.equals(TEMPLATE_AZ_ASIIS_TEST)) {
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
      } else if (templateName.equals(TEMPLATE_CA_CAIR_TEST)) {
        cc.setType(ConnectorFactory.TYPE_CA_SOAP);
        cc.setUrl("https://igsstag.cdph.ca.gov/submit/services/client_Service.client_ServiceHttpsSoap11Endpoint");
        cc.setFacilityidShow(true);
        cc.setFacilityidRequired(true);
        cc.setTypeShow(false);
        cc.setInstructions("Contact CAIR for Username, password and facilityID. The Registry Region Code identifies the region the immunization data should be sent to and is defined by CAIR. In addition, CAIR requires that a certificate be used, a Java Key Store must be installed in the smm transmission folder with the name of smm.jks and the password to the key store must be entered here.  ");
        cc.setReceiverName("CAIR");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
        cc.setOtheridShow(true);
        cc.setOtheridRequired(true);
        cc.setOtheridLabel("Registry Region Code");
        cc.setKeyStorePasswordRequired(true);
        cc.setKeyStorePasswordShow(true);
      } else if (templateName.equals(TEMPLATE_CA_CAIR_PROD)) {
        cc.setType(ConnectorFactory.TYPE_CA_SOAP);
        cc.setUrl("https://igs.cdph.ca.gov/submit/client_Servic.client_ServiceHttpsSoap11Endpoint");
        cc.setFacilityidShow(true);
        cc.setFacilityidRequired(true);
        cc.setTypeShow(false);
        cc.setInstructions("Contact CAIR for Username, password and facilityID. The Registry Region Code identifies the region the immunization data should be sent to and is defined by CAIR. In addition, CAIR requires that a certificate be used, a Java Key Store must be installed in the smm transmission folder with the name of smm.jks and the password to the key store must be entered here.  ");
        cc.setReceiverName("CAIR");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
        cc.setOtheridShow(true);
        cc.setOtheridRequired(true);
        cc.setOtheridLabel("Registry Region Code");
        cc.setKeyStorePasswordRequired(true);
        cc.setKeyStorePasswordShow(true);
      } else if (templateName.equals(TEMPLATE_CA_SDIR_TEST)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("http://www.sdirtrain.org/other/receivehl7.jsp");
        cc.setFacilityidShow(true);
        cc.setTypeShow(false);
        cc.setInstructions("In order to connect to SDIR you will need a User Id, Password and Facility Id. ");
        cc.setReceiverName("SDIR");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(true);
      } else if (templateName.equals(TEMPLATE_CA_SDIR_PROD)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("");
        cc.setFacilityidShow(true);
        cc.setTypeShow(false);
        cc.setInstructions("In order to connect to SDIR you will need a User Id, Password and Facility Id and the URL. ");
        cc.setReceiverName("SDIR");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(true);
      } else if (templateName.equals(TEMPLATE_IA_IRIS_TEST)) {
        cc.setType(ConnectorFactory.TYPE_SOAP);
        cc.setUrl("https://secure.iris.iowa.gov/webservices_trn/cdc");
        cc.setInstructions("Before configuring please request keystore, keystore password, SOAP credentials, and SOAP password. SMM must be specially configured to support keystore.  ");
        cc.setFacilityidShow(true);
        cc.setFacilityidRequired(true);
        cc.setFacilityidLabel("Facility");
        cc.setTypeShow(false);
        cc.setReceiverName("CAIR");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
      } else if (templateName.equals(TEMPLATE_IA_IRIS_PROD)) {
        cc.setType(ConnectorFactory.TYPE_SOAP);
        cc.setUrl("");
        cc.setInstructions("Before configuring please request keystore, keystore password, SOAP credentials, SOAP password, and URL. SMM must be specially configured to support keystore.  ");
        cc.setFacilityidShow(true);
        cc.setFacilityidRequired(true);
        cc.setFacilityidLabel("Facility");
        cc.setTypeShow(false);
        cc.setReceiverName("IRIS");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
      } else if (templateName.equals(TEMPLATE_IL_ICARE_TEST)) {
        cc.setType(ConnectorFactory.TYPE_IL_WS);
        cc.setUrl("https://icarehl7.dph.illinois.gov");
        cc.setInstructions("Before configuring please request account and password. ");
        cc.setFacilityidShow(false);
        cc.setFacilityidRequired(false);
        cc.setTypeShow(false);
        cc.setReceiverName("I-Care");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
        cc.setOtheridRequired(true);
        cc.setOtheridShow(true);
        cc.setOtheridLabel("MSH-4");
        cc.setOtherid("TEST");
      } else if (templateName.equals(TEMPLATE_IL_ICARE_PROD)) {
        cc.setType(ConnectorFactory.TYPE_IL_WS);
        cc.setUrl("https://icarehl7.dph.illinois.gov");
        cc.setInstructions("Before configuring please request account and password. ");
        cc.setFacilityidShow(false);
        cc.setFacilityidRequired(false);
        cc.setTypeShow(false);
        cc.setReceiverName("I-Care");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
        cc.setOtheridRequired(true);
        cc.setOtheridShow(true);
        cc.setOtheridLabel("MSH-4");
      } else if (templateName.equals(TEMPLATE_MN_MIIC_TEST) || templateName.equals(TEMPLATE_MN_MIIC_PROD)) {
        cc.setType(ConnectorFactory.TYPE_SOAP);
        cc.setInstructions("Contact MN MIIC for connecting information.");
        if (templateName.equals(TEMPLATE_MN_MIIC_PROD)) {
          cc.setUrl("");
        } else {
          cc.setUrl("https://miic.health.state.mn.us/miic-ws-test/client_Service?wsdl");
        }
        cc.setTypeShow(false);
        cc.setUseridLabel("User Name");
        cc.setUseridRequired(true);
        cc.setFacilityidShow(true);
        cc.setOtheridLabel("MSH-4");
        cc.setPasswordLabel("Password");
        cc.setPasswordRequired(true);
        cc.setOtheridShow(true);
        cc.setOtheridRequired(true);
        cc.setReceiverName("MN MIIC");
      } else if (templateName.equals(TEMPLATE_MT_IMMTRAX_TEST)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://ejs-test.hhs.mt.gov:8443/phchub/HL7Server");
        cc.setFacilityidShow(false);
        cc.setTypeShow(false);
        cc.setInstructions("Contact MT imMTrax to obtain username and password. ");
        cc.setReceiverName("MT imMTrax");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(false);
      } else if (templateName.equals(TEMPLATE_MT_IMMTRAX_PROD)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://ejs.hhs.mt.gov:8443/phchub/HL7Server");
        cc.setFacilityidShow(false);
        cc.setTypeShow(false);
        cc.setInstructions("Contact  MT imMTrax to obtain username and password. ");
        cc.setReceiverName("MT imMTrax");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(false);
      } else if (templateName.equals(TEMPLATE_MS_MIX_TEST)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://immunizations.ms-hin.net/HttpPostForwarder/");
        cc.setInstructions("Before configuring please request credentials. ");
        cc.setFacilityidShow(true);
        cc.setFacilityidRequired(true);
        cc.setFacilityidLabel("MSH-4 Sending Facility");
        cc.setTypeShow(false);
        cc.setReceiverName("MIX");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
      } else if (templateName.equals(TEMPLATE_MS_MIX_PROD)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://immunizations.ms-hin.net/HttpPostForwarder/");
        cc.setInstructions("Before configuring please request credentials. ");
        cc.setFacilityidShow(true);
        cc.setFacilityidRequired(true);
        cc.setFacilityidLabel("MSH-4 Sending Facility");
        cc.setTypeShow(false);
        cc.setReceiverName("MIX");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
      } else if (templateName.equals(TEMPLATE_NE_SIIS_TEST) || templateName.equals(TEMPLATE_NE_SIIS_PROD)) {
        cc.setType(ConnectorFactory.TYPE_SOAP);
        cc.setInstructions("Contact NE SIIS for connecting information.");
        if (templateName.equals(TEMPLATE_NE_SIIS_PROD)) {
          cc.setUrl("https://nesiis-dhhs-webservice.ne.gov/prd-webservices/cdc");
        } else {
          cc.setUrl("https://testnesiis-dhhs-testwebservice.ne.gov/uat-webservices/cdc");
        }
        cc.setTypeShow(false);
        cc.setUseridLabel("User Name");
        cc.setUseridRequired(true);
        cc.setFacilityidShow(true);
        cc.setOtheridLabel("MSH-4");
        cc.setPasswordLabel("Password");
        cc.setPasswordRequired(true);
        cc.setOtheridShow(true);
        cc.setOtheridRequired(true);
        cc.setReceiverName("NESIIS");
      } else if (templateName.equals(TEMPLATE_NM_SIIS_RAW_UAT)) {
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
      } else if (templateName.equals(TEMPLATE_NV_WEBIZ_TEST) || templateName.equals(TEMPLATE_NV_WEBIZ_PROD)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setInstructions("Contact NV WebIZ for connecting information.");
        if (templateName.equals(TEMPLATE_NV_WEBIZ_PROD)) {
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
      } else if (templateName.equals(TEMPLATE_RI_KIDSNET_TEST)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://kidsnet.health.ri.gov/hl7processor-play/recv.hl7");
        cc.setInstructions("Before configuring please request credentials. ");
        cc.setFacilityidShow(true);
        cc.setFacilityidRequired(true);
        cc.setFacilityidLabel("MSH-4 Sending Facility");
        cc.setTypeShow(false);
        cc.setReceiverName("KIDSNET");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
        cc.setOtheridRequired(true);
        cc.setOtheridShow(true);
        cc.setOtheridLabel("MSH-22 Responsible Business Organization");
      } else if (templateName.equals(TEMPLATE_RI_KIDSNET_PROD)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("");
        cc.setInstructions("Before configuring please request credentials. ");
        cc.setFacilityidShow(true);
        cc.setFacilityidRequired(true);
        cc.setFacilityidLabel("MSH-4 Sending Facility");
        cc.setTypeShow(false);
        cc.setReceiverName("KIDSNET");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setUseridLabel("Username");
        cc.setOtheridRequired(true);
        cc.setOtheridShow(true);
        cc.setOtheridLabel("MSH-22 Responsible Business Organization");
      } else if (templateName.equals(TEMPLATE_WA_IIS_TEST)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://test-fortress.wa.gov/doh/cpir/iweb/HL7Server");
        cc.setFacilityidShow(false);
        cc.setTypeShow(false);
        cc.setInstructions("Contact WA IIS to obtain username and password. ");
        cc.setReceiverName("WA IIS");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(false);
      } else if (templateName.equals(TEMPLATE_WA_IIS_PROD)) {
        cc.setType(ConnectorFactory.TYPE_POST);
        cc.setUrl("https://fortress.wa.gov/doh/cpir/iweb/HL7Server");
        cc.setFacilityidShow(false);
        cc.setTypeShow(false);
        cc.setInstructions("Contact WA IIS to obtain username and password. ");
        cc.setReceiverName("WA IIS");
        cc.setUseridRequired(true);
        cc.setPasswordRequired(true);
        cc.setFacilityidRequired(false);
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
