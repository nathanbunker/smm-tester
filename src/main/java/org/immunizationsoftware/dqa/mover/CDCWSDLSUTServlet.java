/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.immunizationsoftware.dqa.mover;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immunizationsoftware.dqa.tester.ClientServlet;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.openimmunizationsoftware.dqa.transport.CDCWSDLServer;
import org.openimmunizationsoftware.dqa.transport.Fault;
import org.openimmunizationsoftware.dqa.transport.MessageTooLargeFault;
import org.openimmunizationsoftware.dqa.transport.SecurityFault;
import org.openimmunizationsoftware.dqa.transport.SubmitSingleMessage;
import org.openimmunizationsoftware.dqa.transport.UnknownFault;

@SuppressWarnings("serial")
public class CDCWSDLSUTServlet extends ClientServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    out.println("<html>");
    out.println("  <head>");
    out.println("    <title>CDC WSDL SUT</title>");
    out.println("  </head>");
    out.println("  <body>");
    out.println("    <h1>CDC WSDL SUT</h1>");
    out.println("    <hr>");
    out.println("  </body>");
    out.println("</html>");
    out.close();
    out = null;
  }

  private static int increment = 0;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getPathInfo();
    final String processorName = path.startsWith("/") ? path.substring(1) : path;
    try {
      CDCWSDLServer server = new CDCWSDLServer() {
        @Override
        public void process(SubmitSingleMessage ssm, PrintWriter out) throws Fault {
          if (ssm.getPassword().equalsIgnoreCase("BAD") && ssm.getUsername().equalsIgnoreCase("BAD")) {
            throw new SecurityException("Username/password combination is unrecognized");
          } else if (ssm.getPassword().equalsIgnoreCase("NPE") && ssm.getUsername().equalsIgnoreCase("NPE")) {
            NullPointerException npe = new NullPointerException("Trying to get a resource, but found null");
            throw new UnknownFault("Something bad happened when processing", npe);
          }
          HL7Reader hl7Reader = new HL7Reader(ssm.getHl7Message());
          String problem = null;
          if (hl7Reader.advanceToSegment("MSH")) {
            String messageId = hl7Reader.getValue(10);
            String msh3 = hl7Reader.getValue(3);
            String msh4 = hl7Reader.getValue(4);
            String msh5 = hl7Reader.getValue(5);
            String msh6 = hl7Reader.getValue(6);
            String msh22 = hl7Reader.getValue(22);
            String msh23 = hl7Reader.getValue(23);
            if (messageId.equals("")) {
              messageId = "NOT-SENT";
            }
            if (hl7Reader.advanceToSegment("MSH")) {
              throw new MessageTooLargeFault("Found more than one MSH segment");
            }
            String uniqueId = System.currentTimeMillis() + "." + (++increment);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss.sssZ");
            out.print("MSH|^~\\&|" + msh5 + "|" + msh6 + "|" + msh3 + "|" + msh4 + "|" + sdf.format(new Date()) + "||ACK^V04^ACK|" + uniqueId
                + "|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|" + msh23 + "|" + msh22 + "\r");
            out.print("MSA|AA|" + messageId + "\r");
          } else {
            problem = "Unable to find MSH segment, message does not appear to be HL7 v2 formatted data";
          }
          if (problem != null) {
            throw new UnknownFault(problem);
          }

        }

        @Override
        public String getEchoBackMessage(String message) {
          return "End-point is ready. Echoing: " + message + "";
        }

        @Override
        public void authorize(SubmitSingleMessage ssm) throws SecurityFault {
          if (ssm.getPassword().equalsIgnoreCase("BAD") && ssm.getUsername().equalsIgnoreCase("BAD")) {
            throw new SecurityFault("Username/password combination is unrecognized");
          }
        }
      };
      server.setProcessorName(processorName);
      server.process(req, resp);
    } finally {
    }
  }

}
