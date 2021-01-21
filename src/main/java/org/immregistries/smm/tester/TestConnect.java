/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.immregistries.smm.tester;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.immregistries.smm.SoftwareVersion;
import org.immregistries.smm.mover.ConnectionManager;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.mover.ShutdownInterceptor;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.run.TestRunner;
import org.immregistries.smm.transform.TestCaseMessage;

/**
 * 
 * @author nathan
 */
public class TestConnect {


  public static void main(String[] args) throws Exception {
    ConnectionManager.setScanDirectories(false);
    if (args.length < 1) {
      System.err.println("Usage: java  org.immregistries.smm.tester.TestConnect <file root>");
      return;
    }
    File file = new File(args[0]);
    if (!file.exists())
    {
      System.err.println("Unable find file root, folder does not exist");
      return;
    }
    else if (!file.isDirectory())
    {
      System.err.println("Not a file folder");
      return;
    }
    
    System.setProperty("javax.net.debug", "all");

    System.out.println("Initializing Test Connect");
    System.out.println("  + Version: " + SoftwareVersion.VERSION);
    System.out.println("  + Initializing send data manager");
    ConnectionManager connectionManager = new ConnectionManager();
    connectionManager.setScanStartFolders(args[0]);
    connectionManager.init();
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException ie) {
      // just keep going
    }
    System.out.println("Testing will start in 3 seconds...");
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException ie) {
      // just keep going
    }
    
    for (SendData sendData : ConnectionManager.getSendDataList())
    {
      System.out.println("Sending test to " + sendData.getName());
      Connector connector = sendData.getConnector();
      System.out.println("  + URL: " + connector.getUrl());
      TestCaseMessage testCaseMessage = new TestCaseMessage();
      testCaseMessage.setPreparedMessage("MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|20120701082240-0500||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\rPID|\rORC|\rRXA|\r");
      TestRunner testRunner = new TestRunner();
      testRunner.setValidateResponse(false);
      try {
        testRunner.runTest(connector, testCaseMessage);
        System.out.println("  + message sent, response received");
        // need to print out response
      } catch (Throwable t) {
        System.out.println("  + message sent, exception encountered");
        t.printStackTrace(System.err);
      }
      System.out.println("Finished");
    }

    System.out.println("Shutting down connection manager...");
    ShutdownInterceptor shutdown = new ShutdownInterceptor();
    shutdown.run();
    
    System.out.println("Finished testing");

  }





}
