/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.manager.hl7.ConformanceIssue;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7ComponentManager;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ERL;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.VXU;

/**
 * 
 * @author nathan
 */
public class ConformanceServlet extends ClientServlet
{

  private static class Example
  {
    private String title = null;
    private HL7Component component = null;
    private String messageText = null;

    public Example(String title, String componentCode, String messageText) {
      this.title = title;
      this.component = HL7ComponentManager.getComponent(componentCode);
      if (component == null) {
        throw new IllegalArgumentException("Unrecognized component code " + componentCode);
      }
      this.messageText = messageText;
    }
  }

  private static List<Example> exampleList = null;

  private void initExamples() {
    if (exampleList == null) {
      exampleList = new ArrayList<ConformanceServlet.Example>();
      exampleList
          .add(new Example(
              "NIST Test Message #1",
              "VXU",
              "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
                  + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
                  + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
                  + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
                  + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
                  + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
                  + "RXR|IM^Intramuscular^HL70162|LD^Left Arm^HL70163\r"
                  + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
                  + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
                  + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
                  + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"));
      exampleList
          .add(new Example(
              "PID is missing required field",
              "PID",
              "PID|1||D26376273^^^NIST MPI^MR||Snow^^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC"));
      exampleList
          .add(new Example(
              "R1 - VXU missing PID segment",
              "VXU",
              "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
                  + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
                  + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
                  + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
                  + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
                  + "RXR|IM^Intramuscular^HL70162|LD^Left Arm^HL70163\r"
                  + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
                  + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
                  + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
                  + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"));
      exampleList
          .add(new Example(
              "R2 - VXU with BAD segment",
              "VXU",
              "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
                  + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
                  + "BAD|1|Snow^Madelynn^Ainsley|\r"
                  + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
                  + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
                  + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
                  + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
                  + "RXR|IM^Intramuscular^HL70162|LD^Left Arm^HL70163\r"
                  + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
                  + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
                  + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
                  + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"));
      exampleList
          .add(new Example(
              "R3 - PID with out-of-order segment",
              "VXU",
              "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
                  + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
                  + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
                  + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
                  + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
                  + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
                  + "RXR|IM^Intramuscular^HL70162|LD^Left Arm^HL70163\r"
                  + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
                  + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
                  + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
                  + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"));
      exampleList
          .add(new Example(
              "R4 - PID with extra data fields",
              "PID",
              "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC||||||||||||||||||||45^Height^INCHES"));
      exampleList
          .add(new Example(
              "R5 - PID with bad date",
              "PID",
              "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|BAD DATE|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC|"));
      exampleList
          .add(new Example(
              "R6 - VXU with PID that is missing a required field",
              "VXU",
              "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
                  + "PID|1||D26376273^^^NIST MPI^MR|||Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
                  + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
                  + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
                  + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
                  + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
                  + "RXR|IM^Intramuscular^HL70162|LD^Left Arm^HL70163\r"
                  + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
                  + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
                  + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
                  + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"));
      exampleList
          .add(new Example(
              "R7 - VXU that is missing an RE field (PID-10 Race)",
              "VXU",
              "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
                  + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F|||32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
                  + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
                  + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
                  + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
                  + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
                  + "RXR|IM^Intramuscular^HL70162|LD^Left Arm^HL70163\r"
                  + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
                  + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
                  + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
                  + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"));
      exampleList
          .add(new Example(
              "R8 - VXU with an empty required segment (PID)",
              "VXU",
              "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
                  + "PID|\r"
                  + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
                  + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
                  + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\r"
                  + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
                  + "RXR|IM^Intramuscular^HL70162|LD^Left Arm^HL70163\r"
                  + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
                  + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
                  + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
                  + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"));
      exampleList
          .add(new Example(
              "R9 - VXU with RXA but no ORC",
              "VXU",
              "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\r"
                  + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20070706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r"
                  + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\r"
                  + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r"
                  + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.5|mL^MilliLiter [SI Volume Units]^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\r"
                  + "RXR|IM^Intramuscular^HL70162|LD^Left Arm^HL70163\r"
                  + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r"
                  + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r"
                  + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r"
                  + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r"));

      exampleList.add(new Example("ACK Spec 1 - Send acknowledgement of success in ACK", "ACK",
          "MSH|^~\\&|DCS|MYIIS|MYIIS||2009060400||ACK^V04^ACK|1234567 |P|2.5.1\r" + "MSA|AA|9299381\r"));

      exampleList.add(new Example("ACK Spec 2 - Acknowledging An Error Where There is an AR (application reject)",
          "ACK", "MSH|^~\\&||^~\\&|DCS|MYIIS|MYIIS||2009060400||ACK^V04^ACK|12343467|P|2.5.1|||\r" + "MSA|AR|9299381\r"
              + "ERR||MSH^12|203^unsupported version id^^HL70357|E|\r"));

      exampleList.add(new Example("ACK Spec  - ", "ACK", "\r"));

      exampleList.add(new Example("ACK Spec  - ", "ACK", "\r"));

      exampleList.add(new Example("ACK Spec  - ", "ACK", "\r"));

      exampleList.add(new Example("ACK Spec  - ", "ACK", "\r"));

      exampleList.add(new Example("ACK Spec  - ", "ACK", "\r"));

    }
  }

  // <editor-fold defaultstate="collapsed"
  // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP <code>GET</code> method.
   * 
   * @param request
   *          servlet request
   * @param response
   *          servlet response
   * @throws ServletException
   *           if a servlet-specific error occurs
   * @throws IOException
   *           if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {

      String text = request.getParameter("text");
      if (text == null) {
        text = "";
      }
      int pos = 0;
      if (request.getParameter("objectType") != null) {
        pos = Integer.parseInt(request.getParameter("objectType"));
      }

      if (request.getParameter("examplePos") != null) {
        Example example = exampleList.get(Integer.parseInt(request.getParameter("examplePos")));
        pos = HL7ComponentManager.getComponentPosition(example.component);
        text = example.messageText;
      }

      PrintWriter out = new PrintWriter(response.getWriter());
      response.setContentType("text/html;charset=UTF-8");
      printHtmlHead(out, MENU_HEADER_HOME, request);
      out.println("    <form action=\"ConformanceServlet\" method=\"POST\">");
      out.println("      <table border=\"0\">");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">HL7 Object Type</td>");
      out.println("          <td><select name=\"objectType\">");
      List<HL7Component> componentList = HL7ComponentManager.getComponentList();
      for (int i = 0; i < componentList.size(); i++) {
        HL7Component component = componentList.get(i);
        out.println("              <option value=\"" + i + "\"" + (i == pos ? "selected=\"true\"" : "") + ">"
            + component.getComponentReference() + "</option>");
      }
      out.println("              ");
      out.println("            </select>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">HL7 Text</td>");
      out.println("          <td><textarea name=\"text\" cols=\"70\" rows=\"10\" wrap=\"off\">" + text
          + "</textarea></td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td colspan=\"2\" align=\"right\">");
      out.println("            <input type=\"submit\" name=\"method\" value=\"Submit\"/>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("      </table>");
      out.println("    </form>");
      if (!text.equals("")) {
        HL7Component component = componentList.get(pos).makeAnother();
        component.setUsageType(UsageType.R);
        component.setCardinalityCount(1);
        out.println("    <h2>Conformance Test for " + component.getComponentNameFull() + "</h2>");
        if (component.getItemType() == ItemType.MESSAGE) {
          component.parseTextFromMessage(text);
        } else if (component.getItemType() == ItemType.SEGMENT) {
          component.parseTextFromSegment(text);
        } else if (component.getItemType() == ItemType.DATATYPE) {
          component.parseTextFromField(text);
        }

        List<ConformanceIssue> conformanceIssueList = component.checkConformance();
        printConformanceIssues(out, conformanceIssueList);
        out.println("      <p>Error segments generated</p>");
        out.print("    <pre>");
        for (ConformanceIssue conformanceIssue : conformanceIssueList) {
          out.println(conformanceIssue.createSegment());
        }
        out.println("</pre>");
        out.println("      <p>Cleaned up Message</p>");
        out.print("    <pre>");
        if (component.getItemType() == ItemType.MESSAGE) {
          out.print(component.createMessage());
        } else if (component.getItemType() == ItemType.SEGMENT) {
          out.print(component.createSegment());
        } else if (component.getItemType() == ItemType.DATATYPE) {
          out.print(component.createField());
        }
        out.println("</pre>");

      }

      out.println("<h2>Examples</h2>");
      out.println("<ul>");
      initExamples();
      for (int i = 0; i < exampleList.size(); i++) {
        Example example = exampleList.get(i);
        out.println("<li><a href=\"ConformanceServlet?examplePos=" + i + "\">" + example.title + "</a></li>");
      }
      out.println("</ul>");

      out.println("  <div class=\"help\">");
      out.println("  <h2>How To Use This Page</h2>");
      out.println("  </div>");
      printHtmlFoot(out);
      out.close();
    }
  }

  public static void printConformanceIssues(PrintWriter out, List<ConformanceIssue> conformanceIssueList) {
    out.println("      <table border=\"1\" cellspacing=\"0\">");
    out.println("        <tr>");
    out.println("          <th>Severity</th>");
    out.println("          <th>HL7 Component</th>");
    out.println("          <th>Description</th>");
    out.println("        </tr>");

    for (ConformanceIssue conformanceIssue : conformanceIssueList) {
      String passClass = conformanceIssue.getSeverity().getValue().equals("E") ? " class=\"fail\"" : " class=\"pass\"";
      out.println("        <tr>");
      out.println("          <td" + passClass + ">" + n(conformanceIssue.getSeverity().getValue()) + "</td>");
      out.println("          <td" + passClass + ">" + n(conformanceIssue.getErrorLocation()) + "</td>");
      out.println("          <td" + passClass + ">" + n(conformanceIssue.getUserMessage().getValue()) + "</td>");
      out.println("        </tr>");
    }
    out.println("      </table>");
  }

  private static String n(String s) {
    if (s == null || s.equals("")) {
      return "&nbsp;";
    }
    return s;
  }

  private static String n(ERL errorLocation) {
    if (errorLocation == null) {
      return "&nbsp;";
    }
    if (errorLocation.getSegmentID().getValue().equals("")) {
      return "&nbsp;";
    }

    String s = errorLocation.getSegmentID().getValue();
    if (!errorLocation.getSegmentSequence().getValue().equals("")
        && !errorLocation.getSegmentSequence().getValue().equals("1")) {
      s = s + "#" + errorLocation.getSegmentSequence().getValue();
    }
    if (!errorLocation.getFieldPosition().getValue().equals("")) {
      s = s + "-" + errorLocation.getFieldPosition().getValue();
      if (!errorLocation.getFieldRepetition().getValue().equals("")
          && !errorLocation.getFieldRepetition().getValue().equals("1")) {
        s = s + "#" + errorLocation.getFieldRepetition().getValue();
      }
      if (!errorLocation.getComponentNumber().getValue().equals("")) {
        s = s + "." + errorLocation.getComponentNumber().getValue();
        if (!errorLocation.getSubComponentNumber().getValue().equals("")) {
          s = s + "." + errorLocation.getSubComponentNumber().getValue();
        }
      }
    }
    return s;

  }

  /**
   * Handles the HTTP <code>POST</code> method.
   * 
   * @param request
   *          servlet request
   * @param response
   *          servlet response
   * @throws ServletException
   *           if a servlet-specific error occurs
   * @throws IOException
   *           if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      doGet(request, response);
    } catch (Exception e) {
      request.setAttribute("responseText", e.getMessage());
    }
    doGet(request, response);
  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Compare response with original submission";
  }// </editor-fold>

}
