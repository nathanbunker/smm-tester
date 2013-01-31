package org.immunizationsoftware.dqa.tester.connectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.Hashtable;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.axis.components.net.JSSESocketFactory;
import org.apache.axis.components.net.SecureSocketFactory;
import org.immunizationsoftware.dqa.tester.connectors.Connector.SavingTrustManager;

public class TesterSSLSocketFactory extends JSSESocketFactory  implements SecureSocketFactory
{

  private KeyStore keyStore = null;

  public TesterSSLSocketFactory(Hashtable attributes) {
    super(attributes);
  }

  /**
   * Initialize the SSLSocketFactory
   * 
   * @throws IOException
   */
  protected void initFactory() throws IOException
  {
    System.out.println("--> Factory being initialized! ");
    if (getKeyStore() != null)
    {
      try
      {
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(getKeyStore());
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[] { tm }, null);
        sslFactory = context.getSocketFactory();
      } catch (Exception e)
      {
        throw new IOException("Unable to initialize key store: " + e.getMessage(), e);
      }
    } else
    {
      sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    }
  }

  private KeyStore getKeyStore() throws IOException
  {
    if (keyStore == null)
    {
      readKeyStore();
    }
    return keyStore;
  }

  private void readKeyStore() throws IOException
  {
    String keystoreFile = (String) attributes.get("keystore");
    if (keystoreFile != null)
    {
      String keyStorePassword = (String) attributes.get("keypass");
      if (keyStorePassword == null || keyStorePassword.equals(""))
      {
        keyStorePassword = "changeit";
      }
      File keyStoreFile = new File(keystoreFile);
      if (keyStoreFile.exists() && keyStoreFile.isFile())
      {
        try
        {
          keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
          FileInputStream instream = new FileInputStream(keyStoreFile);
          try
          {
            keyStore.load(instream, keyStorePassword.toCharArray());
          } finally
          {
            instream.close();
          }
        } catch (Exception e)
        {
          keyStore = null;
          throw new IOException("Unable to load key store: " + e.getMessage(), e);
        }
      }
    }
  }
}
