package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.hl7.ConformanceIssue;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.QBP;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.VXU;

public class VerifyServlet extends ClientServlet {
  public static final String FIELD_MESSAGEDATA = "MESSAGEDATA";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html;charset=UTF-8");
    PrintWriter out = resp.getWriter();
    try {
      HttpSession session = req.getSession(true);
      String message = (String) session.getAttribute("message");

      if (message == null) {
        message = "";
      }

      printHtmlHead(out, MENU_HEADER_HOME, req);
      out.println("<form action=\"VerifyServlet\" method=\"POST\">");
      out.println("<textarea name=\"" + FIELD_MESSAGEDATA + "\" cols=\"70\" rows=\"10\" wrap=\"off\">" + message + "</textarea>");
      out.println("<br/>");
      out.println("<input type=\"submit\" name=\"submit\" value=\"Submit\">");
      out.println("</form>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String message = req.getParameter(FIELD_MESSAGEDATA);
    resp.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = resp.getWriter();
    try {
      HttpSession session = req.getSession(true);
      session.setAttribute("message", message);

      String messageType = "";
      String controlId = "";
      HL7Reader messageReader = new HL7Reader(message);
      if (messageReader.advanceToSegment("MSH")) {
        messageType = messageReader.getValue(9);
        controlId = messageReader.getValue(10);
      }
      String problem = null;
      HL7Component comp = null;
      if (messageType.equals("")) {
        problem = "Unrecognized Message";
      } else if (messageType.equals("VXU")) {
        comp = new VXU();
      } else if (messageType.equals("QBP")) {
        comp = new QBP();
      } else {
        problem = "Unsupported Message";
      }
      String dateString = "";
      {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        dateString = sdf.format(new Date());
      }
      if (problem != null) {
        out.print("MSH|^~\\&|||||" + dateString + "||ACK^V04^ACK|" + controlId + "|P|2.5.1|\r");
        out.print("MSA|AR|" + controlId + "|\r");
        out.print("ERR|||200^Unsupported message type^HL70357|E||||" + problem + "|\r");
      } else {
        out.print("MSH|^~\\&|||||" + dateString + "||ACK^V04^ACK|" + controlId + "|P|2.5.1|\r");
        comp.parseTextFromMessage(message);
        comp.checkConformance();
        if (comp.hasNoErrors()) {
          out.print("MSA|AA|" + controlId + "|\r");
          out.print("ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r");
        } else {
          out.print("MSA|AE|" + controlId + "|\r");
        }
        for (ConformanceIssue conformanceIssue : comp.getConformanceIssueList()) {
          if (conformanceIssue.getSeverity().getValue().equals("E")) {
            out.print("ERR||");
            out.print(conformanceIssue.getErrorLocation().getSegmentID().getValue() + "^");
            out.print(conformanceIssue.getErrorLocation().getSegmentSequence().getValue() + "^");
            out.print(conformanceIssue.getErrorLocation().getFieldPosition().getValue() + "^");
            out.print(conformanceIssue.getErrorLocation().getFieldRepetition().getValue() + "^");
            out.print(conformanceIssue.getErrorLocation().getComponentNumber().getValue() + "^");
            out.print(conformanceIssue.getErrorLocation().getSubComponentNumber().getValue());
            out.print("|");
            out.print(conformanceIssue.getHl7ErrorCode().getIdentifier().getValue() + "^");
            out.print(conformanceIssue.getHl7ErrorCode().getText().getValue() + "^");
            out.print(conformanceIssue.getHl7ErrorCode().getNameOfCodingSystem().getValue());
            out.print("|" + conformanceIssue.getSeverity().getValue());
            out.print("|");
            out.print(conformanceIssue.getApplicationErrorCode().getIdentifier().getValue() + "^");
            out.print(conformanceIssue.getApplicationErrorCode().getText().getValue() + "^");
            out.print(conformanceIssue.getApplicationErrorCode().getNameOfCodingSystem().getValue());
            out.print("|||");
            out.print(conformanceIssue.getUserMessage().getValue());
            out.print("|\r");
          }
        }
      }
    } finally {
      out.close();
    }
  }
}
