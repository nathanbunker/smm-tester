package org.immunizationsoftware.dqa.mover;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immunizationsoftware.dqa.tester.Authenticate;
import org.immunizationsoftware.dqa.tester.ClientServlet;
import org.immunizationsoftware.dqa.tester.connectors.Connector;

public class ManagerServlet extends ClientServlet
{

  public static final String STANDARD_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
  public static final String STANDARD_TIME_FORMAT = "HH:mm:ss";

  private static long checkInterval = 5; // check every 5 seconds
  private static Set<File> registeredFolders = new HashSet<File>();
  private static Set<SendData> sendDataSet = new HashSet<SendData>();
  private static Map<Integer, SendData> sendDataMap = new HashMap<Integer, SendData>();
  private static int nextSendDataInternalId = 1;
  private static String instanceSystemId = "";
  private static String stableSystemId = "";
  private static InetAddress localHostIp;
  private static byte[] localHostMac;
  private static Date startDate;
  private static String randomId = "";
  private static String supportCenterUrl = null;
  private static String supportCenterCode = "";
  private static ShutdownInterceptor shutdownInterceptor;
  private static File softwareDir = null;

  public static File getSoftwareDir() {
    return softwareDir;
  }

  public static String getStableSystemId() {
    return stableSystemId;
  }

  public static void setStableSystemId(String stableSystemId) {
    ManagerServlet.stableSystemId = stableSystemId;
  }

  public static String getSupportCenterUrl() {
    return supportCenterUrl;
  }

  public static void setSupportCenterUrl(String supportCenterUrl) {
    ManagerServlet.supportCenterUrl = supportCenterUrl;
  }

  public static String getSupportCenterCode() {
    return supportCenterCode;
  }

  public static void setSupportCenterCode(String supportCenterCode) {
    ManagerServlet.supportCenterCode = supportCenterCode;
  }

  public static SendData authenticateSendData(String label, int sendDataId) {
    for (SendData sendData : sendDataSet) {
      if (sendData.getConnector() != null) {
        if (sendData.getConnector().getLabel().equalsIgnoreCase(label) && sendData.getRandomId() == sendDataId) {
          return sendData;
        }
      }
    }
    return null;
  }

  public static boolean isRegisteredFolder(File folder) {
    return registeredFolders.contains(folder);
  }

  public static long getCheckInterval() {
    return checkInterval;
  }

  public static Set<SendData> getSendDataSet() {
    return sendDataSet;
  }

  public static String getRandomId() {
    return randomId;
  }

  public static Date getStartDate() {
    return startDate;
  }

  public static String getInstanceSystemId() {
    return instanceSystemId;
  }

  public static InetAddress getLocalHostIp() {
    return localHostIp;
  }

  public static byte[] getLocalHostMac() {
    return localHostMac;
  }

  protected static void registerFolder(File folder) {
    SendData sendData = new SendData(folder);
    sendDataSet.add(sendData);
    synchronized (sendDataMap) {
      sendData.setInternalId(nextSendDataInternalId);
      sendDataMap.put(nextSendDataInternalId, sendData);
      nextSendDataInternalId++;
    }
    sendData.start();
  }

  public static SendData getSendData(int internalId) {
    return sendDataMap.get(internalId);
  }

  public static List<SendData> getSendDataList() {
    ArrayList<SendData> sendDataList = new ArrayList<SendData>(sendDataMap.values());
    Collections.sort(sendDataList, new Comparator<SendData>() {
      public int compare(SendData o1, SendData o2) {
        return o1.getRootDir().getName().compareTo(o2.getRootDir().getName());
      }
    });
    return sendDataList;
  }

  private static FolderScanner folderScanner = null;

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void init() throws ServletException {
    super.init();
    synchronized (sendDataSet) {
      if (folderScanner == null) {
        initializeManagerSettings();
      }
    }
  }

  private void initializeManagerSettings() {
    {
      // Set a unique system id by combining start time, a random 4 digit
      // number, the local ip, and the mac address
      startDate = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
      Random random = new Random();
      randomId = "" + (random.nextInt(9000) + 1000);
      stableSystemId = getIpMacAddress();
      instanceSystemId = "date" + sdf.format(startDate) + ":rand" + randomId + stableSystemId;

      stableSystemId = doHash(stableSystemId);
    }
    String scanStartFolders = getInitParameter("scan.start.folders");
    System.out.println("SMM Initializing Manager Servlet");
    if (scanStartFolders != null) {
      String[] scanStartFolderNames = scanStartFolders.split("\\;");
      List<File> foldersToScan = new ArrayList<File>();
      for (String scanStartFolderName : scanStartFolderNames) {
        if (scanStartFolderName != null) {
          scanStartFolderName = scanStartFolderName.trim();
          if (scanStartFolderName.length() > 0) {
            System.out.println("SMM Looking for folder " + scanStartFolderName);
            File scanStartFile = new File(scanStartFolderName);
            if (scanStartFile.exists() && scanStartFile.isDirectory()) {
              System.out.println("SMM fold exists, adding to scan directory");
              foldersToScan.add(scanStartFile);
            }
          }
        }
      }
      if (scanStartFolders.length() > 0) {
        folderScanner = new FolderScanner(foldersToScan);
        folderScanner.start();
      }
    }
    supportCenterUrl = getInitParameter("support_center.url");
    supportCenterCode = getInitParameter("support_center.code");

    String softwareDirString = getInitParameter("software.dir");
    if (softwareDirString != null && softwareDirString.length() > 0) {
      softwareDir = new File(softwareDirString);
    }

    String adminUsername = getInitParameter("admin.username");
    String adminPassword = getInitParameter("admin.password");

    if (adminUsername != null && !adminUsername.equals("") && adminPassword != null && !adminPassword.equals("")) {
      Authenticate.setupAdminUser(adminUsername, adminPassword);
    }

    String keyStore = getInitParameter("keyStore");
    if (keyStore != null && keyStore.length() > 0) {
      String keyStorePassword = getInitParameter("keyStorePassword");
      File file = new File(keyStore);
      if (file.exists() && file.isFile()) {
        try {
          System.setProperty("javax.net.ssl.keyStore", file.getCanonicalPath());
          System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
          System.out.println("Set keystore to be: " + file.getCanonicalPath());
        } catch (IOException ioe) {
          System.err.println("Unable to setup keystore: " + ioe.getMessage());
          ioe.printStackTrace();
        }
      } else {
        System.out.println("Keystore file not found: " + keyStore);
      }
    }

    String allowUnsafeRenegotiation = getInitParameter("sun.security.ssl.allowUnsafeRenegotiation");
    if (allowUnsafeRenegotiation != null) {
      System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
      System.out.println("Setting option to allow unsafe renegotiation ");
    }

    ShutdownInterceptor shutdownInterceptor = new ShutdownInterceptor();
    Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
  }

  protected static String doHash(String valueIn) {
    String valueOut = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] hashed = md.digest(valueIn.getBytes());
      valueOut = "";
      for (byte b : hashed) {
        valueOut += String.format("%02X", b);
      }
    } catch (NoSuchAlgorithmException nsae) {
      valueOut = valueIn;
    }
    return valueOut;
  }

  private static String getIpMacAddress() {
    StringBuilder sb = new StringBuilder();
    try {
      localHostIp = InetAddress.getLocalHost();
      sb.append(":ip");
      sb.append(localHostIp.getHostAddress());
      NetworkInterface network = NetworkInterface.getByInetAddress(localHostIp);
      localHostMac = network.getHardwareAddress();
      sb.append(":mac");
      for (int i = 0; i < localHostMac.length; i++) {
        sb.append(String.format("%02X%s", localHostMac[i], (i < localHostMac.length - 1) ? "-" : ""));
      }
    } catch (UnknownHostException e) {
      sb.append(":ip{UnknownHostException:" + e.getMessage() + "}");
    } catch (SocketException e) {
      sb.append(":ip{SocketException:" + e.getMessage() + "}");
    } catch (Exception e) {
      sb.append(":ip{Exception:" + e.getMessage() + "}");
    }
    return sb.toString();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, "Simple Message Mover", request);
      out.println("<h1>Simple Message Mover</h1>");
      if (folderScanner != null) {
        out.println("<h3>Automatic Data Folder Scanning</h3>");
        if (folderScanner.isScanning()) {
          out.println("<p><font color=\"red\">Scanner is currently looking for data folders.</font></p>");
        }
        out.println("<p>Scan status: " + folderScanner.getScanningStatus() + "</p>");
      }
      out.println("");
      out.println("<h3>Send Data Folders</h3>");
      out.println("<table>");
      out.println("  <tr>");
      out.println("    <th>Label</th>");
      out.println("    <th>Status</th>");
      out.println("    <th>Folder</th>");
      out.println("  </tr>");
      for (SendData sendData : sendDataSet) {
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
