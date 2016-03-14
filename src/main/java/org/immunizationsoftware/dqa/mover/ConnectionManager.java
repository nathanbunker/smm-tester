package org.immunizationsoftware.dqa.mover;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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

import org.immunizationsoftware.dqa.tester.Authenticate;
import org.immunizationsoftware.dqa.tester.profile.ProfileCategory;
import org.immunizationsoftware.dqa.tester.profile.ProfileUsage;

public class ConnectionManager
{
  private String keyStorePassword = "";
  private boolean sunSecuritySslAllowUnsafeRenegotiation = false;
  private String keyStore = "";
  private String adminPassword = "";
  private String adminUsername = "";
  private String scanStartFolders = "";

  public String getKeyStorePassword() {
    return keyStorePassword;
  }

  public void setKeyStorePassword(String keyStorePassword) {
    this.keyStorePassword = keyStorePassword;
  }

  public boolean isSunSecuritySslAllowUnsafeRenegotiation() {
    return sunSecuritySslAllowUnsafeRenegotiation;
  }

  public void setSunSecuritySslAllowUnsafeRenegotiation(boolean sunSecuritySslAllowUnsafeRenegotiation) {
    this.sunSecuritySslAllowUnsafeRenegotiation = sunSecuritySslAllowUnsafeRenegotiation;
  }

  public String getKeyStore() {
    return keyStore;
  }

  public void setKeyStore(String keyStore) {
    this.keyStore = keyStore;
  }

  public String getAdminPassword() {
    return adminPassword;
  }

  public void setAdminPassword(String adminPassword) {
    this.adminPassword = adminPassword;
  }

  public String getAdminUsername() {
    return adminUsername;
  }

  public void setAdminUsername(String adminUsername) {
    this.adminUsername = adminUsername;
  }

  public boolean isFolderScanEnabled() {
    return scanDirectories;
  }

  public void setFolderScanEnabled(boolean folderScanEnabled) {
    ConnectionManager.scanDirectories = folderScanEnabled;
  }

  public String getScanStartFolders() {
    return scanStartFolders;
  }

  public void setScanStartFolders(String scanStartFolders) {
    this.scanStartFolders = scanStartFolders;
  }

  public static final String STANDARD_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
  public static final String STANDARD_TIME_FORMAT = "HH:mm:ss";

  public static void setCheckInterval(long checkInterval) {
    ConnectionManager.checkInterval = checkInterval;
  }

  private static long checkInterval = 5; // check every 5 seconds
  private static Set<File> registeredFolders = new HashSet<File>();
  private static Set<SendData> sendDataSet = new HashSet<SendData>();
  private static Map<Integer, SendData> sendDataMap = new HashMap<Integer, SendData>();
  private static Map<String, SendData> sendDataMapByLabel = new HashMap<String, SendData>();
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
  private static File requirementTestFieldsFile = null;
  private static File iisParticipantResponsesAndAccountInfoFile = null;
  private static File requirementTestTransformsFile = null;
  private static Set<ProfileUsage> requirementTestProfileFileSet = new HashSet<ProfileUsage>();
  private static boolean scanDirectories = true;

  public static void setSoftwareDir(File softwareDir) {
    ConnectionManager.softwareDir = softwareDir;
  }

  private static final boolean ENABLE_SUPPORT_CENTER = false;

  public static boolean isScanDirectories() {
    return scanDirectories;
  }

  public static void setScanDirectories(boolean scanDirectories) {
    ConnectionManager.scanDirectories = scanDirectories;
  }

  private static final String REQUIREMENT_TEST_FIELDS_FILE = "SMM Requirement Test Fields.csv";
  private static final String IIS_PARTICIPANT_RESPONSES_AND_ACCOUNT_INFO = "IIS Participant Responses and Account Info.csv";
  private static final String REQUIREMENT_TEST_TRANSFORMS = "SMM Requirement Test Transforms.txt";
  private static final String REQUIREMENT_TEST_PROFILE_START = "SMM Requirement ";
  private static final String REQUIREMENT_TEST_PROFILE_END = ".csv";

  public static Set<ProfileUsage> getRequirementTestProfileFileSet() {
    return requirementTestProfileFileSet;
  }

  public static File getRequirementTestFieldsFile() {
    return requirementTestFieldsFile;
  }

  public static File getIisParticipantResponsesAndAccountInfoFile() {
    return iisParticipantResponsesAndAccountInfoFile;
  }

  public static File getRequirementTestTransformsFile() {
    return requirementTestTransformsFile;
  }

  public static File getSoftwareDir() {
    return softwareDir;
  }

  public static String getStableSystemId() {
    return stableSystemId;
  }

  public static void setStableSystemId(String stableSystemId) {
    ConnectionManager.stableSystemId = stableSystemId;
  }

  public static String getSupportCenterUrl() {
    return supportCenterUrl;
  }

  public static void setSupportCenterUrl(String supportCenterUrl) {
    ConnectionManager.supportCenterUrl = supportCenterUrl;
  }

  public static String getSupportCenterCode() {
    return supportCenterCode;
  }

  public static void setSupportCenterCode(String supportCenterCode) {
    ConnectionManager.supportCenterCode = supportCenterCode;
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

  public static void registerLabel(SendData sendData) {
    if (sendData.getConnector() != null) {
      sendDataMapByLabel.put(sendData.getConnector().getLabel(), sendData);
    }
  }

  public static SendData getSendDatayByLabel(String label) {
    return sendDataMapByLabel.get(label);
  }

  public static Map<String, SendData> getSendDataMapByLabel() {
    return sendDataMapByLabel;
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

  protected static FolderScanner getFolderScanner() {
    return folderScanner;
  }

  public void init() {
    if (folderScanner == null) {
      initializeManagerSettings();
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
    System.out.println("SMM Initializing Manager Servlet");
    System.out.println("Folder scan has been " + (scanDirectories ? "enabled" : "disabled") + ".");
    if (scanStartFolders == null || scanStartFolders.length() == 0) {
      System.err.println("Scan start folders were not defined");
    } else {
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
              if (requirementTestFieldsFile == null) {
                requirementTestFieldsFile = new File(scanStartFile, REQUIREMENT_TEST_FIELDS_FILE);
                if (!requirementTestFieldsFile.exists()) {
                  requirementTestFieldsFile = null;
                } else {
                  System.out.println("Found " + requirementTestFieldsFile);
                }
              }
              if (iisParticipantResponsesAndAccountInfoFile == null) {
                iisParticipantResponsesAndAccountInfoFile = new File(scanStartFile,
                    IIS_PARTICIPANT_RESPONSES_AND_ACCOUNT_INFO);
                if (!iisParticipantResponsesAndAccountInfoFile.exists()) {
                  iisParticipantResponsesAndAccountInfoFile = null;
                } else {
                  System.out.println("Found " + iisParticipantResponsesAndAccountInfoFile);
                }
              }

              if (requirementTestTransformsFile == null) {
                requirementTestTransformsFile = new File(scanStartFile, REQUIREMENT_TEST_TRANSFORMS);
                if (!requirementTestTransformsFile.exists()) {
                  requirementTestTransformsFile = null;
                } else {
                  System.out.println("Found " + requirementTestTransformsFile);
                }
              }
            }
          }
        }
      }
      if (scanStartFolders.length() > 0) {
        folderScanner = new FolderScanner(foldersToScan, this);
        folderScanner.start();
      }
    }
    if (!ENABLE_SUPPORT_CENTER) {
      supportCenterUrl = null;
      supportCenterCode = null;
    }

    if (adminUsername != null && !adminUsername.equals("") && adminPassword != null && !adminPassword.equals("")) {
      Authenticate.setupAdminUser(adminUsername, adminPassword);
    }

    if (keyStore != null && keyStore.length() > 0) {
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

    if (sunSecuritySslAllowUnsafeRenegotiation) {
      System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
      System.out.println("Setting option to allow unsafe renegotiation ");
    }

    ShutdownInterceptor shutdownInterceptor = new ShutdownInterceptor();
    Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
  }

  public void scanForFieldDefinitions(File scanStartFile) {
    File profileFileList[] = scanStartFile.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.startsWith(REQUIREMENT_TEST_PROFILE_START) && name.endsWith(REQUIREMENT_TEST_PROFILE_END);
      }
    });
    for (File profileFile : profileFileList) {
      String name = profileFile.getName();
      if (name.startsWith(REQUIREMENT_TEST_PROFILE_START) && name.endsWith(REQUIREMENT_TEST_PROFILE_END)) {
        name = name
            .substring(REQUIREMENT_TEST_PROFILE_START.length(), name.length() - REQUIREMENT_TEST_PROFILE_END.length())
            .trim();
        int pos = name.indexOf("-");
        if (pos > 0) {
          try {
            ProfileUsage profileUsage = new ProfileUsage();
            ProfileCategory category;
            try {
              category = ProfileCategory.valueOf(name.substring(0, pos).trim().toUpperCase());
            } catch (IllegalArgumentException iae) {
              throw new Exception("Unable to read requirements file named '" + name + "' because the profile catagory '"
                  + name.substring(0, pos).trim() + "' is not recognized. ");
            }
            profileUsage.setCategory(category);
            name = name.substring(pos + 1).trim();
            pos = name.indexOf("-");
            if (pos > 0) {
              profileUsage.setVersion(name.substring(pos + 1).trim());
              name = name.substring(0, pos).trim();
            }
            profileUsage.setLabel(name);
            profileUsage.setFile(profileFile);
            synchronized (requirementTestProfileFileSet) {
              requirementTestProfileFileSet.add(profileUsage);
            }
          } catch (Exception e) {
            System.out.println("Unable to load profile requirements file");
            e.printStackTrace();
          }
        }
      }
    }
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

}
