package org.immunizationsoftware.dqa.mover;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immunizationsoftware.dqa.tester.ClientServlet;
import org.immunizationsoftware.dqa.tester.connectors.Connector;

public class ManagerServlet extends ClientServlet
{

  public static final String INIT_PARAM_KEY_STORE_PASSWORD = "keyStorePassword";
  public static final String INIT_PARAM_SUN_SECURITY_SSL_ALLOW_UNSAFE_RENEGOTIATION = "sun.security.ssl.allowUnsafeRenegotiation";
  public static final String INIT_PARAM_KEY_STORE = "keyStore";
  public static final String INIT_PARAM_ADMIN_PASSWORD = "admin.password";
  public static final String INIT_PARAM_ADMIN_USERNAME = "admin.username";
  public static final String INIT_PARAM_SOFTWARE_DIR = "software.dir";
  public static final String INIT_PARAM_SUPPORT_CENTER_CODE = "support_center.code";
  public static final String INIT_PARAM_SUPPORT_CENTER_URL = "support_center.url";
  public static final String INIT_PARAM_FOLDER_SCAN_ENABLED = "folderScanEnabled";
  public static final String INIT_PARAM_SCAN_START_FOLDERS = "scan.start.folders";

  private static ConnectionManager connectionManager = null;

  public static ConnectionManager getConnectionManager() {
    return connectionManager;
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void init() throws ServletException {
    super.init();
    if (connectionManager == null) {
        initializeConnectionManager();
    }
  }

  private synchronized void initializeConnectionManager() {
    if (connectionManager != null)
    {
      return;
    }
    connectionManager = new ConnectionManager();
    connectionManager.setScanStartFolders(getInitParameter(INIT_PARAM_SCAN_START_FOLDERS));
    System.out.println("SMM Initializing Manager Servlet");
    String checkIntervalInSeconds = getInitParameter("checkIntervalInSeconds");
    if (checkIntervalInSeconds != null) {
      long checkInterval = Long.parseLong(checkIntervalInSeconds);
      if (checkInterval <= 0) {
        checkInterval = 5;
      }
      ConnectionManager.setCheckInterval(checkInterval);
    }
    String folderScanEnabled = getInitParameter(INIT_PARAM_FOLDER_SCAN_ENABLED);
    if (folderScanEnabled != null) {
      ConnectionManager.setScanDirectories(!folderScanEnabled.equalsIgnoreCase("false"));
    }
    ConnectionManager.setSupportCenterCode(getInitParameter(INIT_PARAM_SUPPORT_CENTER_CODE));
    ConnectionManager.setSupportCenterUrl(getInitParameter(INIT_PARAM_SUPPORT_CENTER_URL));

    String softwareDirString = getInitParameter(INIT_PARAM_SOFTWARE_DIR);
    if (softwareDirString != null && softwareDirString.length() > 0) {
      ConnectionManager.setSoftwareDir(new File(softwareDirString));
    }

    connectionManager.setAdminUsername(getInitParameter(INIT_PARAM_ADMIN_USERNAME));
    connectionManager.setAdminPassword(getInitParameter(INIT_PARAM_ADMIN_PASSWORD));
    connectionManager.setKeyStore(getInitParameter(INIT_PARAM_KEY_STORE));
    connectionManager.setKeyStorePassword(getInitParameter(INIT_PARAM_KEY_STORE_PASSWORD));

    connectionManager.setSunSecuritySslAllowUnsafeRenegotiation(
        getInitParameter(INIT_PARAM_SUN_SECURITY_SSL_ALLOW_UNSAFE_RENEGOTIATION) != null);

    ShutdownInterceptor shutdownInterceptor = new ShutdownInterceptor();
    Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
    connectionManager.init();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, "Simple Message Mover", request);
      out.println("<h1>Simple Message Mover</h1>");
      if (ConnectionManager.getFolderScanner() != null) {
        out.println("<h3>Automatic Data Folder Scanning</h3>");
        if (ConnectionManager.getFolderScanner().isScanning()) {
          out.println("<p><font color=\"red\">Scanner is currently looking for data folders.</font></p>");
        }
        out.println("<p>Scan status: " + ConnectionManager.getFolderScanner().getScanningStatus() + "</p>");
      }
      out.println("");
      out.println("<h3>Send Data Folders</h3>");
      out.println("<table>");
      out.println("  <tr>");
      out.println("    <th>Label</th>");
      out.println("    <th>Status</th>");
      out.println("    <th>Folder</th>");
      out.println("  </tr>");
      for (SendData sendData : ConnectionManager.getSendDataSet()) {
        out.println("  <tr>");
        Connector connector = sendData.getConnector();
        if (connector != null) {
          out.println("    <td>" + connector.getLabelDisplay() + "</td>");
        } else {
          out.println("    <td>-</td>");
        }
        out.println("    <td>" + sendData.getScanStatus() + "</td>");
        out.println("    <td>" + sendData.getRootDir() + "</td>");
        out.println("  </tr>");
      }
      out.println("</table>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
    super.doPost(req, resp);
  }
}
