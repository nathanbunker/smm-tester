/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.immregistries.smm.mover;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immregistries.smm.cdc.CDCWSDLServer;
import org.immregistries.smm.cdc.Fault;
import org.immregistries.smm.cdc.SecurityFault;
import org.immregistries.smm.cdc.SubmitSingleMessage;
import org.immregistries.smm.cdc.UnknownFault;
import org.immregistries.smm.tester.ClientServlet;
import org.immregistries.smm.tester.connectors.Connector;

public class CDCWSDLServlet extends ClientServlet {

  private static final String SEND_DATA = "sendData";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String wsdl = req.getParameter("wsdl");
    if (wsdl != null) {
      resp.setContentType("text/xml");
      PrintWriter out = new PrintWriter(resp.getOutputStream());
      CDCWSDLServer.printWSDL(out, "http://localhost:8282/wsdl");
      out.close();
    } else {
      resp.setContentType("text/html;charset=UTF-8");

      PrintWriter out = resp.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_HOME, req);
        out.println("<h1>SMM Realtime</h1>");
        out.println("<p>");
        out.println("This end point supports submission of real-time immunization messages in conforanmcne with the ");
        out.println("<a href=\"http://www.cdc.gov/vaccines/programs/iis/technical-guidance/soap/wsdl.html\">CDC ");
        out.println("WSDL</a>.");
        out.println("</p>");
        out.println("<h3>WSDL</h3>");
        out.println("<p>Download or view WSDL here: ");
        out.println("  <a href=\"wsdl-demo?wsdl=true\">http://ois-pt.org/tester/wsdl?wsdl=true</a></p>");
        printHtmlFoot(out);
      } finally {
        out.close();
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getPathInfo();
    final String label = path.startsWith("/") ? path.substring(1) : path;
    try {
      CDCWSDLServer server = new CDCWSDLServer() {
        @Override
        public void process(SubmitSingleMessage ssm, PrintWriter out) throws Fault {
          SendData sendData = (SendData) ssm.getAttribute(SEND_DATA);
          Connector connector;
          try {
            connector = sendData.createTempConnector();
            connector.setUserid(ssm.getUsername());
            connector.setPassword(ssm.getPassword());
            connector.setFacilityid(ssm.getFacilityID());
            // connector.setOtherid("");
            out.println(connector.submitMessage(ssm.getHl7Message(), false));
          } catch (Exception e) {
            throw new UnknownFault("Unable to relay to end point", e);
          }
        }

        @Override
        public String getEchoBackMessage(String message) {
          SendData sendData = ConnectionManager.getSendDatayByLabel(label);
          if (sendData == null || sendData.getConnector() == null) {
            return "Unrecognized end-point, or is not ready to receive messages. Echoing: " + message + "";
          } else {
            return sendData.getConnector().getLabelDisplay() + " transfer is ready to receive messages. Echoing: " + message + "";
          }
        }

        @Override
        public void authorize(SubmitSingleMessage ssm) throws SecurityFault {
          SendData sendData = ConnectionManager.getSendDatayByLabel(label);
          if (sendData == null || sendData.getConnector() == null) {
            throw new SecurityFault("End-point is not recognized.");
          }
          ssm.setAttribute(SEND_DATA, sendData);
        }
      };
      server.process(req, resp);
    } finally {
    }
  }

}
