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
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.ClientServlet;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.openimmunizationsoftware.dqa.transport.CDCWSDLServer;
import org.openimmunizationsoftware.dqa.transport.Fault;
import org.openimmunizationsoftware.dqa.transport.MessageTooLargeFault;
import org.openimmunizationsoftware.dqa.transport.ProcessorFactory;
import org.openimmunizationsoftware.dqa.transport.SecurityFault;
import org.openimmunizationsoftware.dqa.transport.SubmitSingleMessage;
import org.openimmunizationsoftware.dqa.transport.UnknownFault;

@SuppressWarnings("serial")
public class WSDLDemoServlet extends ClientServlet
{

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String wsdl = req.getParameter("wsdl");
    if (wsdl != null) {
      resp.setContentType("text/xml");
      PrintWriter out = new PrintWriter(resp.getOutputStream());
      CDCWSDLServer.printWSDL(out, "http://localhost:8282/wsdl-demo");
      out.close();
    } else {
      resp.setContentType("text/html;charset=UTF-8");

      PrintWriter out = resp.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_HOME, req);
        out.println("<h1>CDC WSDL Demonstration</h1>");
        out.println("<p>");
        out.println("This servlet demostrates the use of the ");
        out.println("<a href=\"http://www.cdc.gov/vaccines/programs/iis/technical-guidance/soap/wsdl.html\">CDC ");
        out.println("WSDL</a>");
        out.println(" which has been defined to support the transport of HL7 messages ");
        out.println("sent to Immunization Information Systems (IIS).  ");
        out.println("</p>");
        out.println("<p>Please note that this service is NOT an IIS and does not process or act on data sent. ");
        out.println("The purpose of this end-point is to demonstrate the CDS WSDL transport layer and as such ");
        out.println("does not actually process or understand the HL7 submitted. </p>");
        out.println("<h2>Usage Instructions</h2>");
        out.println("<h3>WSDL</h3>");
        out.println("<p>Download or view WSDL here: ");
        out.println("  <a href=\"wsdl-demo?wsdl=true\">http://ois-pt.org/tester/wsdl-demo?wsdl=true</a></p>");
        out.println("<h3>Authentication</h3>");
        out.println("<p>By default all requests are considered authenticated. ");
        out.println("Any username/password combination is accepted, except for username/passwords: </p>");
        out.println("<ul>");
        out.println("  <li><b>Bad/Bad</b>: ");
        out.println("  Will generate an Security Fault indicating. Use this to test sending bad credentials. </li>");
        out.println("  <li><b>NPE/NPE</b>: ");
        out.println("  Causes an internal exception (Null Pointer Exception) which returns an Unknown Fault. ");
        out.println("  Use this to see what happens when there are unexpected problems.  </li>");
        out.println("</ul>");
        out.println("<h3>Content</h3>");
        out.println("<p>The HL7 content is expected to be an HL7 v2 VXU message. ");
        out.println("Other immunization messages, such as a QBP may be submitted but this ");
        out.println("demo system will respond as if it receive a VXU ");
        out.println("(no RSP will be returned, ACK will indicate it responded to VXU). ");
        out.println("The only requirement is that the HL7 v2 message contains at least one MSH segment. ");
        out.println("</p>");
        out.println("<h3>Multiple Messages</h3>");
        out.println("<p>If the message contains more than one MSH segment a Message Too Large Fault ");
        out.println("will be returned.  ");
        out.println("Use this feature to test situations where the IIS can not process more than one message. </p>");
        out.println("<h2>Alternative Behavior</h2>");
        out.println("<p>Additional end points are available, which provide different behaviors ");
        out.println("(some good and some bad). ");
        out.println("These can be used to demonstrate different or bad interactions. </p>");
        ProcessorFactory.printExplanations(out);
        printHtmlFoot(out);
      } finally {
        out.close();
      }
    }
  }

  private static int increment = 0;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getPathInfo();
    final String processorName = path == null ? "" : (path.startsWith("/") ? path.substring(1) : path);
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
            out.print("MSH|^~\\&|" + msh5 + "|" + msh6 + "|" + msh3 + "|" + msh4 + "|" + sdf.format(new Date())
                + "||ACK^V04^ACK|" + uniqueId + "|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS|" + msh23 + "|" + msh22 + "\r");
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
