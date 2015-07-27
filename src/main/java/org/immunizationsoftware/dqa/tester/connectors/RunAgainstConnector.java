package org.immunizationsoftware.dqa.tester.connectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.immunizationsoftware.dqa.tester.manager.HL7Reader;

public class RunAgainstConnector extends Connector {

  private File testFolder = null;
  private String originalRequestMessage = "";

  // private Connector originalConnector = null;

  public String getOriginalRequestMessage() {
    return originalRequestMessage;
  }

  public RunAgainstConnector(Connector originalConnector, File testFolder) {
    super(originalConnector);
    // this.originalConnector = originalConnector;
    this.testFolder = testFolder;
  }

  @Override
  protected void setupFields(List<String> fields) {
    // nothing to do
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    String messageControlId = "";
    HL7Reader r = new HL7Reader(message);
    if (r.advanceToSegment("MSH")) {
      messageControlId = r.getValue(10);
      if (messageControlId.equals("")) {
        return "No MSH-7 is empty, it must be sent in order to respond";
      }
    } else {
      return "No MSH found unable to respond";
    }
    int dashPos = messageControlId.indexOf("-");
    if (dashPos > 0) {
      messageControlId = messageControlId.substring(dashPos);
      int lastPeriod = messageControlId.lastIndexOf(".");
      if (lastPeriod > 0) {
        messageControlId = messageControlId.substring(0, lastPeriod);
      }
    }
    findOriginalMessage(messageControlId);
    if (originalRequestMessage == null) {
      if (messageControlId.startsWith("-A.")) {
        if (messageControlId.equals("-A.01.01")) {
          messageControlId = "NIST-IZ-001.00";
          findOriginalMessage(messageControlId);
        } else if (messageControlId.equals("-A.01.02")) {
          messageControlId = "NIST-IZ-004.00";
          findOriginalMessage(messageControlId);
        } else if (messageControlId.equals("-A.01.03")) {
          messageControlId = "NIST-IZ-007.00";
          findOriginalMessage(messageControlId);
        } else if (messageControlId.equals("-A.01.04")) {
          messageControlId = "NIST-IZ-010.00";
          findOriginalMessage(messageControlId);
        } else if (messageControlId.equals("-A.01.05")) {
          messageControlId = "NIST-IZ-013.00";
          findOriginalMessage(messageControlId);
        } else if (messageControlId.equals("-A.01.06")) {
          messageControlId = "NIST-IZ-016.00";
          findOriginalMessage(messageControlId);
        } else if (messageControlId.equals("-A.01.07")) {
          messageControlId = "NIST-IZ-019.00";
          findOriginalMessage(messageControlId);
        }
      }
    }
    String[] ackFileNames = testFolder.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".ack.hl7");
      }
    });
    for (String ackFileName : ackFileNames) {
      File ackFile = new File(testFolder, ackFileName);
      if (ackFile.isFile()) {
        BufferedReader in = new BufferedReader(new FileReader(ackFile));
        String line = "";
        String response = "";
        while ((line = in.readLine()) != null) {
          if (line.startsWith("FHS|")) {
            response = line + "\r";
          } else if (line.startsWith("MSH|")) {
            if (response.indexOf("MSH|") >= 0) {
              response = "";
            }
            response += line + "\r";
          } else if (line.startsWith("MSA|")) {
            if (line.indexOf(messageControlId) > 0) {
              // found it!
              while (line != null && !line.startsWith("MSH") && !line.startsWith("FHS")) {
                response += line + "\r";
                line = in.readLine();
              }
              in.close();
              return response;
            }
          } else {
            response += line + "\r";
          }
        }
        in.close();
      }
    }
    return "No previous response found for message control id '" + messageControlId + "'";
  }

  private void findOriginalMessage(String messageControlId) throws IOException {
    originalRequestMessage = null;
    String[] ackFileNames = testFolder.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".hl7");
      }
    });
    for (String ackFileName : ackFileNames) {
      File ackFile = new File(testFolder, ackFileName);
      if (ackFile.isFile()) {
        BufferedReader in = new BufferedReader(new FileReader(ackFile));
        String line = "";
        while ((line = in.readLine()) != null) {
          if (line.startsWith("MSH|") && line.indexOf(messageControlId) > 0) {
            // found it!
            String response = "";
            while (line != null && (response.equals("") || (!line.startsWith("MSH") && !line.startsWith("FHS")))) {
              response += line + "\r";
              line = in.readLine();
            }
            in.close();
            originalRequestMessage = response;
            return;
          }
        }
        in.close();
      }
    }

  }

  @Override
  public String connectivityTest(String message) throws Exception {
    return "Not supported";
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {
    // nothing to do
  }

}
