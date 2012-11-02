/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.connectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * 
 * @author nathan
 */
public abstract class Connector
{

  protected abstract void setupFields(List<String> fields);

  protected static Connector addConnector(String label, String type, String url, String userid, String facilityid, String password,
      String keyStorePassword, List<String> fields, String customTransformations, List<Connector> connectors) throws Exception
  {
    if (!label.equals("") && !type.equals(""))
    {
      Connector connector = null;
      if (type.equals("SOAP"))
      {
        connector = new SoapConnector(label, url);
      } else if (type.equals("POST"))
      {
        connector = new HttpConnector(label, url);
      } else
      {
        connector = new HttpConnector(label, url);
      }
      connector.setUserid(userid);
      connector.setFacilityid(facilityid);
      connector.setPassword(password);
      connector.setupFields(fields);
      connector.setCustomTransformations(customTransformations);
      connector.setKeyStorePassword(keyStorePassword);
      connectors.add(connector);
      return connector;
    }
    return null;
  }

  protected String label = "";
  protected String type = "";
  protected String userid = "";
  protected String password = "";
  protected String facilityid = "";
  protected String url = "";
  private String customTransformations = "";
  private String[] quickTransformations;
  private KeyStore keyStore = null;
  private File keyStoreFile = null;
  private String keyStorePassword = null;
  protected boolean throwExceptions = false;

  public boolean isThrowExceptions()
  {
    return throwExceptions;
  }

  public void setThrowExceptions(boolean throwExceptions)
  {
    this.throwExceptions = throwExceptions;
  }

  public String getKeyStorePassword()
  {
    return keyStorePassword;
  }

  public void setKeyStorePassword(String keyStorePassword)
  {
    this.keyStorePassword = keyStorePassword;
  }

  public KeyStore getKeyStore()
  {
    return keyStore;
  }

  public void setKeyStore(KeyStore keyStore)
  {
    this.keyStore = keyStore;
  }

  public String[] getQuickTransformations()
  {
    return quickTransformations;
  }

  public void setQuickTransformations(String[] quickTransformations)
  {
    this.quickTransformations = quickTransformations;
  }

  public String getCustomTransformations()
  {
    return customTransformations;
  }

  public void setCustomTransformations(String customTransformations)
  {
    this.customTransformations = customTransformations;
  }

  public String getUrl()
  {
    return url;
  }

  public String getUrlShort()
  {
    if (url != null && url.length() > 28)
    {
      return url.substring(0, 28) + "..";
    }
    return url;
  }

  public String getLabelDisplay()
  {
    return label + " (" + type + ")";
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getFacilityid()
  {
    return facilityid;
  }

  public void setFacilityid(String facilityid)
  {
    this.facilityid = facilityid;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getUserid()
  {
    return userid;
  }

  public void setUserid(String userid)
  {
    this.userid = userid;
  }

  public String getLabel()
  {
    return label;
  }

  public Connector(String label, String type) {
    this.label = label;
    this.type = type;
  }

  public abstract String submitMessage(String message, boolean debug) throws Exception;

  public abstract String connectivityTest(String message) throws Exception;

  public String getScript()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("-----------------------------------------\n");
    sb.append("Connection\n");
    sb.append("Label: " + label + "\n");
    sb.append("Type: " + type + "\n");
    sb.append("URL: " + url + "\n");
    sb.append("User Id: " + userid + "\n");
    sb.append("Password: \n");
    sb.append("Facility Id: " + facilityid + "\n");
    if (customTransformations != null && customTransformations.length() > 0)
    {
      sb.append("Custom Transformations:");
      try
      {
        BufferedReader inTransform = new BufferedReader(new StringReader(customTransformations));
        String line;
        while ((line = inTransform.readLine()) != null)
        {
          line = line.trim();
          sb.append(" + " + line + "\n");
        }
      } catch (IOException ioe)
      {
        // IOException not expected when reading a string
        throw new RuntimeException("Exception while reading string", ioe);
      }
    }
    makeScriptAdditions(sb);
    return sb.toString();
  }

  protected abstract void makeScriptAdditions(StringBuilder sb);

  public static List<Connector> makeConnectors(String script) throws Exception
  {
    List<Connector> connectors = new ArrayList<Connector>();
    String label = "";
    String type = "";
    String userid = "";
    String password = "";
    String facilityid = "";
    String url = "";
    String customTransformations = "";
    String keyStorePassword = "";
    List<String> fields = new ArrayList<String>();
    BufferedReader in = new BufferedReader(new StringReader(script));
    String line;
    String lastList = "";
    while ((line = in.readLine()) != null)
    {
      line = line.trim();
      if (line.startsWith("Connection"))
      {
        addConnector(label, type, url, userid, facilityid, password, keyStorePassword, fields, customTransformations, connectors);
        label = "";
        type = "";
        url = "";
        userid = "";
        facilityid = "";
        password = "";
        customTransformations = "";
        keyStorePassword = "";
        fields = new ArrayList<String>();
      } else if (line.startsWith("Label:"))
      {
        label = readValue(line);
      } else if (line.startsWith("Type:"))
      {
        type = readValue(line);
      } else if (line.startsWith("URL:"))
      {
        url = readValue(line);
      } else if (line.startsWith("User Id:"))
      {
        userid = readValue(line);
      } else if (line.startsWith("Password:"))
      {
        password = readValue(line);
      } else if (line.startsWith("Facility Id:"))
      {
        facilityid = readValue(line);
      } else if (line.startsWith("Key Store Password:"))
      {
        keyStorePassword = readValue(line);
      } else if (line.startsWith("Cause Issues:"))
      {
        lastList = "CI";
      } else if (line.startsWith("Custom Transformations:"))
      {
        lastList = "CT";
      } else if (line.startsWith("+"))
      {
        if (lastList.equals("CT"))
        {
          customTransformations += line.substring(1).trim() + "\n";
        }
      } else
      {
        fields.add(line);
      }

    }
    addConnector(label, type, url, userid, facilityid, password, keyStorePassword, fields, customTransformations, connectors);
    return connectors;
  }

  protected static String readValue(String line)
  {
    int pos = line.indexOf(":");
    if (pos == -1)
    {
      return "";
    }
    return line.substring(pos + 1).trim();
  }

//  private SSLServerSocket createServerSocketFromKeyStore()
//  {
//    SSLServerSocketFactory ssf; // server socket factory
//    SSLServerSocket skt; // server socket
//
//    // LOAD EXTERNAL KEY STORE
//    KeyStore mstkst;
//    try
//    {
//      mstkst = KeyStore.getInstance("jks");
//      mstkst.load(new FileInputStream(keyStoreFile), keyStorePassword.toCharArray());
//    } catch (java.security.GeneralSecurityException thr)
//    {
//      throw new IOException("Cannot load keystore (" + thr + ")");
//    }
//
//    // CREATE EPHEMERAL KEYSTORE FOR THIS SOCKET USING DESIRED CERTIFICATE
//    try
//    {
//      SSLContext ctx = SSLContext.getInstance("TLS");
//      KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//      KeyStore sktkst;
//      char[] blkpwd = new char[0];
//
//      sktkst = KeyStore.getInstance("jks");
//      sktkst.load(null, blkpwd);
//      sktkst.setKeyEntry(svrctfals, mstkst.getKey(svrctfals, blkpwd), blkpwd, mstkst.getCertificateChain(svrctfals));
//      kmf.init(sktkst, blkpwd);
//      ctx.init(kmf.getKeyManagers(), null, null);
//      ssf = ctx.getServerSocketFactory();
//    } catch (java.security.GeneralSecurityException thr)
//    {
//      throw new IOException("Cannot create secure socket (" + thr + ")");
//    }
//
//    // CREATE AND INITIALIZE SERVER SOCKET
//    skt = (SSLServerSocket) ssf.createServerSocket(prt, bcklog, adr);
//    return skt;
//  }
}
