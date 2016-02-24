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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immunizationsoftware.dqa.tester.ClientServlet;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.openimmunizationsoftware.dqa.transport.CDCWSDLServer;
import org.openimmunizationsoftware.dqa.transport.SubmitSingleMessage;

public class CDCWSDLServlet extends ClientServlet
{

  private static final String SEND_DATA = "sendData";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    out.println("<html>");
    out.println("  <head>");
    out.println("    <title>SMM Incoming Interface</title>");
    out.println("  </head>");
    out.println("  <body>");
    out.println("    <h1>CDC WSDL Endpoint</h1>");
    out.println("    <hr>");
    out.println("  </body>");
    out.println("</html>");
    out.close();
    out = null;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getPathInfo();
    final String label = path.startsWith("/") ? path.substring(1) : path;
    try {
      CDCWSDLServer server = new CDCWSDLServer() {
        @Override
        public void process(SubmitSingleMessage ssm, PrintWriter out) throws IOException {
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
            throw new IOException("Unable to connect: " + e.getMessage());
          }
        }

        @Override
        public String getEchoBackMessage(String message) {
          SendData sendData = ManagerServlet.getSendDatayByLabel(label);
          if (sendData == null || sendData.getConnector() == null) {
            return "Unrecognized end-point, or is not ready to receive messages. Echoing: " + message + "";
          } else {
            return sendData.getConnector().getLabelDisplay() + " transfer is ready to receive messages. Echoing: "
                + message + "";
          }
        }

        @Override
        public void authorize(SubmitSingleMessage ssm) {
          SendData sendData = ManagerServlet.getSendDatayByLabel(label);
          if (sendData == null || sendData.getConnector() == null) {
            ssm.setAccessDenied("End-point is not recognized.");
          } else {
            ssm.setAttribute(SEND_DATA, sendData);
          }
        }
      };
      server.process(req, resp);
    } finally {
    }
  }

}
