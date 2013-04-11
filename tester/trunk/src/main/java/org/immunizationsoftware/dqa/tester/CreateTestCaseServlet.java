/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.tester.Transformer.PatientType;

/**
 * 
 * @author nathan
 */
public class CreateTestCaseServlet extends ClientServlet
{

  public static final String SCENARIO_BLANK = "Blank";
  public static final String SCENARIO_1_1_ADMIN_CHILD = "NIST MU2.1.1 - Admin Child";
  public static final String SCENARIO_1_R_ADMIN_CHILD = "NIST MU2.1.r - Admin Child (replica)";
  public static final String SCENARIO_2_1_ADMIN_ADULT = "NIST MU2.2.1 - Admin Adult";
  public static final String SCENARIO_2_R_ADMIN_ADULT = "NIST MU2.2.r - Admin Adult (replica)";
  public static final String SCENARIO_3_1_HISTORICAL_CHILD = "NIST MU2.3.1 - Historical Child";
  public static final String SCENARIO_3_R_HISTORICAL_CHILD = "NIST MU2.3.r - Historical Child (replica)";
  public static final String SCENARIO_4_1_CONSENTED_CHILD = "NIST MU2.4.1 - Consented Child";
  public static final String SCENARIO_4_R_CONSENTED_CHILD = "NIST MU2.4.r - Consented Child (replica)";
  public static final String SCENARIO_5_1_REFUSED_TODDLER = "NIST MU2.5.1 - Refused Toddler";
  public static final String SCENARIO_5_R_REFUSED_TODDLER = "NIST MU2.5.r - Refused Toddler (replica)";
  public static final String SCENARIO_6_1_VARICELLA_HISTORY_CHILD = "NIST MU2.6.1 - Varicella History Child";
  public static final String SCENARIO_6_R_VARICELLA_HISTORY_CHILD = "NIST MU2.6.r - Varicella History Child (replica)";
  public static final String SCENARIO_7_1_COMPLETE_RECORD = "NIST MU2.7.1 - Complete Record";
  public static final String SCENARIO_7_R_COMPLETE_RECORD = "NIST MU2.7.r - Complete Record (replica)";

  public static final String[] SCENARIOS = { SCENARIO_BLANK, SCENARIO_1_1_ADMIN_CHILD, SCENARIO_1_R_ADMIN_CHILD, SCENARIO_2_1_ADMIN_ADULT,
      SCENARIO_2_R_ADMIN_ADULT, SCENARIO_3_1_HISTORICAL_CHILD, SCENARIO_3_R_HISTORICAL_CHILD, SCENARIO_4_1_CONSENTED_CHILD,
      SCENARIO_4_R_CONSENTED_CHILD, SCENARIO_5_1_REFUSED_TODDLER, SCENARIO_5_R_REFUSED_TODDLER, SCENARIO_6_1_VARICELLA_HISTORY_CHILD,
      SCENARIO_6_R_VARICELLA_HISTORY_CHILD, SCENARIO_7_1_COMPLETE_RECORD, SCENARIO_7_R_COMPLETE_RECORD };

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
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
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);

    String username = (String) session.getAttribute("username");
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");
    if (username == null)
    {
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else
    {
      PrintWriter out = response.getWriter();
      List<TestCaseMessage> selectedTestCaseMessageList = null;

      String action = request.getParameter("action");
      int testCasePos = 0;
      if (action != null)
      {
        selectedTestCaseMessageList = (List<TestCaseMessage>) session.getAttribute("selectedTestCaseMessageList");
        if (selectedTestCaseMessageList.isEmpty())
        {
          selectedTestCaseMessageList = null;
        } else
        {
          if (request.getParameter("testCasePos") != null)
          {
            testCasePos = Integer.parseInt(request.getParameter("testCasePos"));
            if (action.equals("Prev"))
            {
              testCasePos--;
            }
            if (action.equals("Next"))
            {
              testCasePos++;
            }
          }
        }
      }

      String testCaseNumber;
      TestCaseMessage testCaseMessage = null;
      if (selectedTestCaseMessageList != null)
      {
        testCaseMessage = selectedTestCaseMessageList.get(testCasePos);
        testCaseNumber = testCaseMessage.getTestCaseNumber();
      } else
      {
        testCaseNumber = request.getParameter("testCase");
        if (testCaseNumber != null && testCaseNumber.length() > 0)
        {
          testCaseMessage = getTestCaseMessageMap(session).get(testCaseNumber);
        } else
        {
          testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
          if (testCaseMessage != null)
          {
            testCaseNumber = testCaseMessage.getTestCaseNumber();
          }
        }
      }
      int runTimes = 0;
      if (session.getAttribute("runTimes") != null)
      {
        runTimes = (Integer) session.getAttribute("runTimes");
      } else if (request.getParameter("runTimes") != null)
      {
        runTimes = Integer.parseInt(request.getParameter("runTimes"));
      }
      runTimes++;
      session.setAttribute("runTimes", runTimes);
      String actualTestCase = testCaseNumber;
      if (testCaseNumber == null || testCaseNumber.equals(""))
      {
        testCaseNumber = "";
        actualTestCase = "TC-" + runTimes;
      }
      {
        String scenario = request.getParameter("scenario");
        if (scenario == null)
        {
          String originalMessage = request.getParameter("base");
          String customTransformations = request.getParameter("customTransforms");
          String description = request.getParameter("description");
          String expectedResult = request.getParameter("expectedResult");
          String assertResultText = request.getParameter("assertResultText");
          String assertResultStatus = request.getParameter("assertResultStatus");
          String testCaseSet = request.getParameter("testCaseSet");
          String[] quickTransformations = request.getParameterValues("extra");
          PatientType patientType = PatientType.ANY_CHILD;
          if (request.getParameter("patientType") != null)
          {
            patientType = PatientType.valueOf(request.getParameter("patientType"));
          }
          if (quickTransformations == null && request.getParameter("settingQuickTransformations") == null)
          {
            if (testCaseMessage != null)
            {
              quickTransformations = testCaseMessage.getQuickTransformations();
            } else
            {
              quickTransformations = new String[] { "2.5.1", "BOY", "DOB", "ADDRESS", "PHONE", "MOTHER", "VAC1_HIST", "VAC2_HIST", "VAC3_ADMIN" };
            }
          }
          if (assertResultText == null && testCaseMessage != null)
          {
            assertResultText = testCaseMessage.getAssertResultText();
          }
          if (assertResultText == null)
          {
            assertResultText = "";
          }
          if (assertResultStatus == null && testCaseMessage != null)
          {
            assertResultStatus = testCaseMessage.getAssertResultStatus();
          }
          if (testCaseSet == null && testCaseMessage != null)
          {
            testCaseSet = testCaseMessage.getTestCaseSet();
          }
          if (testCaseSet == null)
          {
            testCaseSet = "";
          }
          if (assertResultStatus == null)
          {
            assertResultStatus = "";
          }
          if (description == null && testCaseMessage != null)
          {
            description = testCaseMessage.getDescription();
          }
          if (description == null)
          {
            description = "";
          }
          if (expectedResult == null && testCaseMessage != null)
          {
            expectedResult = testCaseMessage.getExpectedResult();
          }
          if (expectedResult == null)
          {
            expectedResult = "";
          }
          if (originalMessage == null && testCaseMessage != null)
          {
            originalMessage = testCaseMessage.getOriginalMessage();
          }
          if (originalMessage == null)
          {
            originalMessage = "MSH|\nPID|\nNK1|\nPV1|\nORC|\nRXA|\nORC|\nRXA|\nORC|\nRXA|\nOBX|\nOBX|\nOBX|\nOBX|\n";
          }
          if (customTransformations == null && testCaseMessage != null)
          {
            customTransformations = testCaseMessage.getCustomTransformations();
          }
          if (customTransformations == null)
          {
            customTransformations = "";
          }
          if (testCaseMessage == null)
          {
            testCaseMessage = new TestCaseMessage();
          }

          session.setAttribute("baseMessage", originalMessage);
          testCaseMessage.setTestCaseNumber(testCaseNumber);
          testCaseMessage.setTestCaseSet(testCaseSet);
          testCaseMessage.setAssertResultStatus(assertResultStatus);
          testCaseMessage.setAssertResultText(assertResultText);
          testCaseMessage.setCustomTransformations(customTransformations);
          testCaseMessage.setDescription(description);
          testCaseMessage.setExpectedResult(expectedResult);
          testCaseMessage.setOriginalMessage(originalMessage);
          testCaseMessage.setQuickTransformations(quickTransformations);
          testCaseMessage.setPatientType(patientType);

        } else
        {
          testCaseMessage = new TestCaseMessage();
          String originalMessage = "";
          String customTransformations = "";
          String description = "";
          String expectedResult = "";
          String assertResultText = "";
          String assertResultStatus = "";
          String testCaseSet = "";
          String[] quickTransformations = {};
          PatientType patientType = PatientType.ANY_CHILD;

          if (scenario.equals(SCENARIO_BLANK))
          {
            description = "Admininistered for Child";
            originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
            quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE", "ETHNICITY", "VAC1_ADMIN" };
            patientType = PatientType.TODDLER;
          } else if (scenario.equals(SCENARIO_1_R_ADMIN_CHILD))
          {
            description = "NIST IZ #1: Admininistered for Child (replica)";
            originalMessage = "MSH|\nPID|\nPD1|\nNK1|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
            quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "MOTHER", "RACE", "ETHNICITY", "VAC1_ADMIN" };
            patientType = PatientType.TODDLER;
          } else if (scenario.equals(SCENARIO_2_R_ADMIN_ADULT))
          {
            description = "NIST IZ #2: Administered for Adult (replica)";
            originalMessage = "MSH|\nPID|\nORC|\nRXA|\nOBX|\nOBX|\nOBX|\nOBX|\n";
            quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY", "VAC1_ADMIN" };
            patientType = PatientType.ADULT;
          } else if (scenario.equals(SCENARIO_3_R_HISTORICAL_CHILD))
          {
            description = "NIST IZ #3: Historical for Child (replica)";
            originalMessage = "MSH|\nPID|\nORC|\nRXA|\n";
            quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY", "VAC1_HIST" };
            patientType = PatientType.TWEEN;
          } else if (scenario.equals(SCENARIO_4_R_CONSENTED_CHILD))
          {
            description = "NIST IZ #4: Consented for Child (replica)";
            originalMessage = "MSH|\nPID|\nPD1|\nORC|\nRXA|\n";
            quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY", "VAC1_HIST" };
            patientType = PatientType.TWEEN;
            customTransformations = "PD1-12=N\n";
          } else if (scenario.equals(SCENARIO_5_R_REFUSED_TODDLER))
          {
            description = "NIST IZ #5: Refused for Toddler (replica)";
            originalMessage = "MSH|\nPID|\nORC|\nRXA|\n";
            patientType = PatientType.TODDLER;
            quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY", "VAC1_NA" };
            customTransformations = "RXA-18.1=00\n" + "RXA-18.2=Parental Decision\n" + "RXA-18.3=NIP002\n" + "RXA-20=RE\n";
          } else if (scenario.equals(SCENARIO_6_R_VARICELLA_HISTORY_CHILD))
          {
            description = "NIST IZ #6: Varicella History for Child (replica)";
            originalMessage = "MSH|\nPID|\nORC|\nRXA|\nOBX|\n";
            quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY", "VAC1_NA" };
            customTransformations = "RXA-5.1=998\n" + "RXA-5.2=No vaccine administered\n" + "RXA-5.1=998\n" + "OBX-1=1\n" + "OBX-2=CE\n"
                + "OBX-3.1=59784-9\n" + "OBX-3.2=Disease with presumed immunity\n" + "OBX-3.3=LN\n" + "OBX-4=1\n" + "OBX-5.1=38907003\n"
                + "OBX-5.2=Varicella infection\n" + "OBX-5.3=SCT\n" + "OBX-11=F\n";
            patientType = PatientType.TWEEN;
          } else if (scenario.equals(SCENARIO_7_R_COMPLETE_RECORD))
          {
            description = "NIST IZ #7: Complete Record (replica)";
            originalMessage = "MSH|\nPID|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\nORC|\nRXA|\nORC|\nRXA|\nRXR|\nOBX|\nOBX|\nOBX|\nOBX|\n";
            quickTransformations = new String[] { "2.5.1", "BOY_OR_GIRL", "DOB", "ADDRESS", "PHONE", "RACE", "ETHNICITY", "VAC1_ADMIN", "VAC2_HIST",
                "VAC3_ADMIN" };
            patientType = PatientType.TODDLER;
          } else if (scenario.equals(SCENARIO_1_1_ADMIN_CHILD))
          {
            description = "NIST IZ #1.1: Admininistered for Child";
            originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||AL|ER\n"
                + "PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^Ainsley^^^^L|Lam^Morgan|20100706|F||2076-8^Native Hawaiian or Other Pacific Islander^HL70005|32 Prescott Street Ave^^Warwick^MA^02452^USA^L||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\n"
                + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120701|20120701\n"
                + "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\n"
                + "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L\n"
                + "RXA|0|1|20120814||140^Influenza, seasonal, injectable, preservative free^CVX|0.25|mL^milliliters^UCUM||00^New immunization record^NIP001|7832-1^Lemon^Mike^A^^^^^NIST-AA-1|^^^X68||||Z0860BB|20121104|CSL^CSL Behring^MVX|||CP|A\n"
                + "RXR|IM^Intramuscular^HL70162|LA^Left Arm^HL70163\n"
                + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\n"
                + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\n"
                + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\n"
                + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\n";
            quickTransformations = new String[] {};
            patientType = PatientType.TODDLER;
          } else if (scenario.equals(SCENARIO_2_1_ADMIN_ADULT))
          {
            description = "NIST IZ #2.1: Administered for Adult";
            originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-004.00|P|2.5.1|||AL|ER\n"
                + "PID|1||MR-76732^^^NIST MPI^MR~184-36-9200^^^MAA^SS||Vally^Nitika^^^^^L||19410813|F|||||^NET^^nvally@fastmail.com\n"
                + "ORC|RE||IZ-783275^NDA|||||||||57422^RADON^NICHOLAS^^^^^^NDA^L\n"
                + "RXA|0|1|20120814||52^Hep A^CVX|1|mL^milliliters^UCUM||00^New immunization record^NIP001||^^^F28||||I90FV|20121214|MSD^Merck and Co^MVX|||CP|A\n"
                + "RXR|IM^Intramuscular^HL70162|RA^Right Arm^HL70163\n"
                + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20120814|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\n"
                + "OBX|2|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F\n"
                + "OBX|3|CE|30956-7^vaccine type^LN|2|85^Hepatitis A^CVX||||||F\n"
                + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\n";
            quickTransformations = new String[] {};
            patientType = PatientType.ADULT;
          } else if (scenario.equals(SCENARIO_3_R_HISTORICAL_CHILD))
          {
            description = "NIST IZ #3.1: Historical for Child";
            originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-007.00|P|2.5.1|||AL|ER\n"
                + "PID|1||MR-99922^^^NIST MPI^MR||Montgomery^Lewis^^^^^L||20010821|M\n" + "ORC|RE||IZ-783276^NDA\n"
                + "RXA|0|1|20110215||62^HPV^CVX|999|||01^Historical information - source unspecified^NIP001\n";
            quickTransformations = new String[] {};
            patientType = PatientType.TWEEN;
          } else if (scenario.equals(SCENARIO_4_R_CONSENTED_CHILD))
          {
            description = "NIST IZ #4.1: Consented for Child";
            originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-010.00|P|2.5.1|||AL|ER\n"
                + "PID|1||X-6523^^^NIST MPI^MR||Newton^Lea^^^^^L||20010905|F\n" + "PD1||||||||||||N|20120701\n" + "ORC|RE||IZ-783277^NDA\n"
                + "RXA|0|1|20110214||115^Tdap^CVX|999|||01^Historical information - source unspecified^NIP001\n";
            quickTransformations = new String[] {};
            patientType = PatientType.TWEEN;
          } else if (scenario.equals(SCENARIO_5_1_REFUSED_TODDLER))
          {
            description = "NIST IZ #5.1: Refused for Toddler";
            originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-013.00|P|2.5.1|||AL|ER\n"
                + "PID|1||MR-67323^^^NIST MPI^MR||Fleming^Chad^^^^^L||20100830|M\n" + "ORC|RE||9999^CDC\n"
                + "RXA|0|1|20120815||03^MMR^CVX|999||||||||||||00^Parental Refusal^NIP002||RE\n";
            patientType = PatientType.TODDLER;
            quickTransformations = new String[] {};
          } else if (scenario.equals(SCENARIO_6_R_VARICELLA_HISTORY_CHILD))
          {
            description = "NIST IZ #6.1: Varicella History for Child";
            originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-016.00|P|2.5.1|||AL|ER\n"
                + "PID|1||MR-11891^^^NIST MPI^MR||Wolfe^Aron^^^^^L||20010907|M\n" + "ORC|RE||9999^CDC\n"
                + "RXA|0|1|20110215||998^No vaccine administered^CVX|999||||||||||||||NA\n"
                + "OBX|1|CE|59784-9^Disease with presumed immunity^LN|1|38907003^Varicella infection^SCT||||||F\n";
            quickTransformations = new String[] {};
            patientType = PatientType.TWEEN;
          } else if (scenario.equals(SCENARIO_7_1_COMPLETE_RECORD))
          {
            description = "NIST IZ #7.1: Complete Record";
            originalMessage = "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|201207010822||VXU^V04^VXU_V04|NIST-IZ-019.00|P|2.5.1|||AL|ER\n"
                + "PID|1||Q-73221^^^NIST MPI^MR||Mercer^Jirra^Emmanuelle^^^^L||20100907|F\n"
                + "ORC|RE||IZ-783278^NDA|||||||||57422^RADON^NICHOLAS^^^^^^NDA^L\n"
                + "RXA|0|1|20120816||141^Influenza^CVX|0.25|mL^milliliters^UCUM||00^New immunization record^NIP001||||||K5094SC|20121216|SKB^GlaxoSmithKline^MVX|||CP|A\n"
                + "RXR|IM^Intramuscular^HL70162|RA^Right Arm^HL70163\n"
                + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\n"
                + "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\n"
                + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\n"
                + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\n"
                + "ORC|RE||IZ-783281^NDA|||||||||57422^RADON^NICHOLAS^^^^^^NDA^L\n"
                + "RXA|0|1|20110216||10^IPV^CVX|999|||01^Historical information - source unspecified^NIP001\n"
                + "ORC|RE||IZ-783282^NDA|||||||||57422^RADON^NICHOLAS^^^^^^NDA^L\n"
                + "RXA|0|1|20120816||120^DTaP-Hib-IPV^CVX|0.5|mL^milliliters^UCUM||00^New immunization record^NIP001||||||568AHK11|20121216|PMC^sanofi pasteur^MVX|||CP|A\n"
                + "RXR|IM^Intramuscular^HL70162|RA^Right Arm^HL70163\n"
                + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120701|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\n"
                + "OBX|2|CE|30956-7^vaccine type^LN|2|107^DTaP^CVX||||||F\n"
                + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20070517||||||F\n"
                + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120816||||||F\n"
                + "OBX|5|CE|30956-7^vaccine type^LN|3|89^Polio^CVX||||||F\n"
                + "OBX|6|TS|29768-9^Date vaccine information statement published^LN|3|20111108||||||F\n"
                + "OBX|7|TS|29769-7^Date vaccine information statement presented^LN|3|20120816||||||F\n"
                + "OBX|8|CE|30956-7^vaccine type^LN|4|17^Hib^CVX||||||F\n"
                + "OBX|9|TS|29768-9^Date vaccine information statement published^LN|4|20111108||||||F\n"
                + "OBX|10|TS|29769-7^Date vaccine information statement presented^LN|4|20120816||||||F\n";
            quickTransformations = new String[] {};
            patientType = PatientType.TODDLER;
          }

          testCaseMessage.setTestCaseNumber(testCaseNumber);
          testCaseMessage.setTestCaseSet(testCaseSet);
          testCaseMessage.setAssertResultStatus(assertResultStatus);
          testCaseMessage.setAssertResultText(assertResultText);
          testCaseMessage.setCustomTransformations(customTransformations);
          testCaseMessage.setDescription(description);
          testCaseMessage.setExpectedResult(expectedResult);
          testCaseMessage.setOriginalMessage(originalMessage);
          testCaseMessage.setQuickTransformations(quickTransformations);
          testCaseMessage.setPatientType(patientType);

          session.setAttribute("baseMessage", originalMessage);
        }
        if (testCaseMessage.getTestCaseNumber().length() > 0)
        {
          getTestCaseMessageMap(session).put(testCaseNumber, testCaseMessage);
        }
        saveTestCase(testCaseMessage, session);
      }

      Transformer transformer = new Transformer();
      testCaseMessage.setPreparedMessage(null);
      transformer.transform(testCaseMessage);
      session.setAttribute("message", testCaseMessage.getMessageText());
      try
      {
        printHtmlHead(out, "Edit", request);
        out.println("    <form action=\"CreateTestCaseServlet\" method=\"POST\">");
        out.println("      <table>");
        out.println("        <tr>");
        out.println("          <td valign=\"top\">Test Case Num</td>");
        out.println("          <td><input type=\"text\" name=\"testCase\" value=\"" + testCaseMessage.getTestCaseNumber()
            + "\" size=\"15\"> Set <input type=\"text\" name=\"testCaseSet\" value=\"" + testCaseMessage.getTestCaseSet() + "\" size=\"15\"></td>");
        out.println("          <input type=\"hidden\" name=\"runTimes\" value=\"" + runTimes + "\"></td>");
        out.println("          <td align=\"right\">");
        makeButtons(selectedTestCaseMessageList, out, testCasePos);
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td valign=\"top\">Description</td>");
        out.println("          <td colspan=\"2\"><input type=\"text\" name=\"description\" value=\"" + testCaseMessage.getDescription()
            + "\" size=\"70\"></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td valign=\"top\">Expected Result</td>");
        out.println("          <td colspan=\"2\"><input type=\"text\" name=\"expectedResult\" value=\"" + testCaseMessage.getExpectedResult()
            + "\" size=\"70\"></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td valign=\"top\">Assert Result</td>");
        out.println("          <td colspan=\"2\">");
        out.println("            <select name=\"assertResultStatus\">");
        out.println("              <option value=\"\">select</option>");
        out.println("              <option value=\"Accept\"" + (testCaseMessage.getAssertResultStatus().equals("Accept") ? " selected=\"true\"" : "")
            + ">Accept</option>");
        out.println("              <option value=\"Accept and Warn\""
            + (testCaseMessage.getAssertResultStatus().equals("Accept and Warn") ? " selected=\"true\"" : "") + ">Accept and Warn</option>");
        out.println("              <option value=\"Reject\"" + (testCaseMessage.getAssertResultStatus().equals("Reject") ? " selected=\"true\"" : "")
            + ">Reject</option>");
        out.println("            </select>");
        out.println("            Value <input type=\"text\" name=\"assertResultText\" value=\"" + testCaseMessage.getAssertResultText()
            + "\" size=\"50\">");
        out.println("          </td>");
        out.println("        </tr>");
        if (!testCaseMessage.getActualResultAckType().equals(""))
        {
          out.println("        <tr>");
          out.println("          <td>Actual Result</td>");
          out.println("          <td colspan=\"2\">" + testCaseMessage.getActualResultAckType() + ": " + testCaseMessage.getActualResultAckMessage()
              + "</td>");
          out.println("        </tr>");
        }
        out.println("        <tr>");
        out.println("          <td valign=\"top\">Start Message</td>");
        out.println("          <td colspan=\"2\"><textarea name=\"base\" cols=\"70\" rows=\"7\" wrap=\"off\">" + testCaseMessage.getOriginalMessage()
            + "</textarea></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td valign=\"top\">Transform</td>");
        out.println("          <td colspan=\"2\" align=\"left\" valign=\"top\">");
        out.println("            <table>");
        out.println("              <tr>");
        out.println("                <td>");
        out.println("                  <div class=\"smallTitle\">Patient Type</div>");
        out.println("                  <input type=\"radio\" name=\"patientType\" value=\"" + PatientType.ANY_CHILD + "\""
            + isChecked(PatientType.ANY_CHILD, testCaseMessage.getPatientType()) + "/> Any Child ");
        out.println("                  <input type=\"radio\" name=\"patientType\" value=\"" + PatientType.ADULT + "\""
            + isChecked(PatientType.ADULT, testCaseMessage.getPatientType()) + "/> Adult <br/>");
        out.println("                  <input type=\"radio\" name=\"patientType\" value=\"" + PatientType.BABY + "\""
            + isChecked(PatientType.BABY, testCaseMessage.getPatientType()) + "/> Baby ");
        out.println("                  <input type=\"radio\" name=\"patientType\" value=\"" + PatientType.TODDLER + "\""
            + isChecked(PatientType.TODDLER, testCaseMessage.getPatientType()) + "/> Toddler");
        out.println("                  <input type=\"radio\" name=\"patientType\" value=\"" + PatientType.TWEEN + "\""
            + isChecked(PatientType.TWEEN, testCaseMessage.getPatientType()) + "/> Tween <br/>");
        out.println("                  <div class=\"smallTitle\">Quick Transforms</div>");
        out.println("                  <input type=\"hidden\" name=\"settingQuickTransformations\" value=\"true\">");
        out.println("                  <input type=\"checkbox\" name=\"extra\" value=\"2.5.1\""
            + isChecked("2.5.1", testCaseMessage.getQuickTransformations()) + "> 2.5.1 <input type=\"checkbox\" name=\"extra\" value=\"2.3.1\""
            + isChecked("2.3.1", testCaseMessage.getQuickTransformations()) + "> 2.3.1<br>");
        out.println("                  <input type=\"checkbox\" name=\"extra\" value=\"BOY\""
            + isChecked("BOY", testCaseMessage.getQuickTransformations()) + "> Boy <input type=\"checkbox\" name=\"extra\" value=\"GIRL\""
            + isChecked("GIRL", testCaseMessage.getQuickTransformations()) + "> Girl <input type=\"checkbox\" name=\"extra\" value=\"BOY_OR_GIRL\""
            + isChecked("BOY_OR_GIRL", testCaseMessage.getQuickTransformations()) + "> Either<br>");
        out.println("                  <input type=\"checkbox\" name=\"extra\" value=\"DOB\""
            + isChecked("DOB", testCaseMessage.getQuickTransformations())
            + "> Date of Birth <input type=\"checkbox\" name=\"extra\" value=\"TWIN_POSSIBLE\""
            + isChecked("TWIN_POSSIBLE", testCaseMessage.getQuickTransformations()) + "> Twin Possible<br>");
        out.println("                  <input type=\"checkbox\" name=\"extra\" value=\"ADDRESS\""
            + isChecked("ADDRESS", testCaseMessage.getQuickTransformations()) + "> Address <input type=\"checkbox\" name=\"extra\" value=\"PHONE\""
            + isChecked("PHONE", testCaseMessage.getQuickTransformations()) + "> Phone<br>");
        out.println("                  <input type=\"checkbox\" name=\"extra\" value=\"MOTHER\""
            + isChecked("MOTHER", testCaseMessage.getQuickTransformations()) + "> Mother <input type=\"checkbox\" name=\"extra\" value=\"FATHER\""
            + isChecked("FATHER", testCaseMessage.getQuickTransformations()) + "> Father<br>");
        out.println("                  <input type=\"checkbox\" name=\"extra\" value=\"RACE\""
            + isChecked("RACE", testCaseMessage.getQuickTransformations()) + "> Race <input type=\"checkbox\" name=\"extra\" value=\"ETHNICITY\""
            + isChecked("ETHNICITY", testCaseMessage.getQuickTransformations()) + "> Ethnicity<br>");
        out.println("                  Vacc #1 <input type=\"checkbox\" name=\"extra\" value=\"VAC1_ADMIN\""
            + isChecked("VAC1_ADMIN", testCaseMessage.getQuickTransformations())
            + "> Admin <input type=\"checkbox\" name=\"extra\" value=\"VAC1_HIST\""
            + isChecked("VAC1_HIST", testCaseMessage.getQuickTransformations()) + "> Hist<br>");
        out.println("                  Vacc #2 <input type=\"checkbox\" name=\"extra\" value=\"VAC2_ADMIN\""
            + isChecked("VAC2_ADMIN", testCaseMessage.getQuickTransformations())
            + "> Admin <input type=\"checkbox\" name=\"extra\" value=\"VAC2_HIST\""
            + isChecked("VAC2_HIST", testCaseMessage.getQuickTransformations()) + "> Hist<br>");
        out.println("                  Vacc #3 <input type=\"checkbox\" name=\"extra\" value=\"VAC3_ADMIN\""
            + isChecked("VAC3_ADMIN", testCaseMessage.getQuickTransformations())
            + "> Admin <input type=\"checkbox\" name=\"extra\" value=\"VAC3_HIST\""
            + isChecked("VAC3_HIST", testCaseMessage.getQuickTransformations()) + "> Hist");
        out.println("                </td>");
        out.println("                <td valign=\"top\">");
        out.println("                  <div class=\"smallTitle\">Quick Transforms Applied</div> ");
        out.println("                  <pre style=\"text-align: left; height: 170px; width: 250px;overflow:auto;\">"
            + testCaseMessage.getQuickTransformationsConverted() + "</pre><br>");
        out.println("                  <div class=\"smallTitle\">Custom Transforms</div>");
        out.println("                  <textarea name=\"customTransforms\" cols=\"30\" rows=\"4\" wrap=\"off\">"
            + testCaseMessage.getCustomTransformations() + "</textarea>");
        out.println("                </td>");
        out.println("              </tr>");
        out.println("            </table>");
        out.println("          </td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td valign=\"top\">Test Case</td>");
        out.println("          <td colspan=\"2\"><pre style=\"text-align: left; height: 250px; width: 520px;overflow:auto;\">"
            + testCaseMessage.createText(true) + "</pre></td>");
        out.println("        </tr>");
        out.println("        <tr>");
        out.println("          <td colspan=\"3\" align=\"right\">");
        if (selectedTestCaseMessageList != null)
        {
          out.println("            <input type=\"hidden\" name=\"testCasePos\" value=\"" + testCasePos + "\"/>");
          if (testCasePos > 0)
          {
            out.println("            <input type=\"submit\" name=\"action\" value=\"Prev\"/>");
          }
          out.println("            <input type=\"submit\" name=\"action\" value=\"Update\"/>");
          if ((testCasePos + 1) < selectedTestCaseMessageList.size())
          {
            out.println("            <input type=\"submit\" name=\"action\" value=\"Next\"/>");
          }
        } else
        {
          out.println("            <input type=\"submit\" name=\"method\" value=\"Update\"/>");
        }
        out.println("          </td>");
        out.println("        </tr>");
        out.println("      </table>");
        session.setAttribute("testCaseMessage", testCaseMessage);
        if (!testCaseMessage.getTestCaseNumber().equals(""))
        {
          getTestCaseMessageMap(session).put(testCaseMessage.getTestCaseNumber(), testCaseMessage);
        }
        out.println("    </form>");
        out.println("  <div class=\"help\">");
        out.println("  <h2>How To Use This Page</h2>");
        out.println("  <p>This page is built to help you quickly create a valid test message. "
            + "The result of this process is an HL7 message that can be tested plus the headers "
            + "that the Data Quality Test tool uses to make a pretty test case report. Here are " + "the steps to creating a test case:</p>");
        out.println("   <ol>");
        out.println("     <li><b>Test Case Num and Set</b> Indicate the test case number you wish to use for this test case. If you don't supply a number then one will be automatically assigned. (This assigned number will increment by 1 every time you submit this page.) The set is for grouping test cases into more manageble sections. </li>");
        out.println("     <li><b>Description</b> Write a human readable description that describes what you are trying to test. </li>");
        out.println("     <li><b>Expected Result</b> Write a human readable description of describes what you expect to happen when you submit this message.</li>");
        out.println("     <li><b>Actual Result</b> Select what kind of result you are expecting and the exact value of either the acknowledgement text or warning text");
        out.println("       <ul>");
        out.println("         <li><b>Accept</b> The message should return with a positive ACK. In the value write the exact acknowledgement text you expect.</li>");
        out.println("         <li><b>Accept and Warn</b> The message should return with a positive ACK but should have a warning listed. In the value write the exact warning text you expect.</li>");
        out.println("         <li><b>Reject</b> The message should return with a negative ACK (not a positive ACK). The value should be the exact negative acknolwedgment text returned.</li>");
        out.println("       </ul>");
        out.println("     </li>");
        out.println("     <li><b>Start Message</b> The base message that you wish to start with. All that is required is that every segment you wish to be in the final message is listed. This process will not create new segments or reorder segments, but it can set field values. You do not have to supply a start message, the default one will work.</li>");
        out.println("     <li><b>Transform</b> The transform section indicates the changes you want to make to the base message. If you are using the default base message you will have to indicate transforms in order to make a vaid HL7 message.");
        out.println("       <ul>");
        out.println("         <li><b>Quick Transforms</b> This is a list of basic transforms that you will commonly do. Select each option you wish to use. Items on the same line are normaly mutually exclusive. Checking a box from each line will result in a valid HL7 message. Please remember: if you don't have the segment defined in the original message these quick transforms do not automatically create the segment!");
        out.println("           <ul>");
        out.println("             <li><b>2.5.1 or 2.3.1</b> Select what kind of message you are generating and the required fields not listed below will be filled in. </li>");
        out.println("             <li><b>Boy or Girl</b> Selecting boy or girl will populate the last name, first name, middle name and birth date of the patient. The last name is a random last name out of over 1000 last names derived from US county names. The first and middle name are a random selection of the top 1000 most popular baby names for either girl or boy. The gender is M for boy and F for girl. </li>");
        out.println("             <li><b>Date of Birth</b> The birth date is a random date in 2009 or 2010. </li>");
        out.println("             <li><b>Address</b> The street number is randomly generated number betewen 1 and 400, the street name a randomly selected county name, the street type a randomly selected value of 5 common street types. The city, state and zip code are correct for a real city in the state of Michigan.</li>");
        out.println("             <li><b>Phone</b> The area code is the correct area code for the address randomly selected and represents a real area code used in Michigan. The rest of the number is in the format 555-xxxx where the last 4 digits are randomly generated.</li>");
        out.println("             <li><b>Mother or Father</b> Sets the NK1 last name as the same as the patients. Sets the first name as a randomly chosen baby girl or baby boy name. Sets the NK1 type to either mother or father.</li>");
        out.println("             <li><b>Vacc #1 Admin or Hist</b> Sets the first vaccination date to a random date about 2 months after the randomly selected patient's date of birth. Sets RXA-9 to indiate the whether this is administered or historical.</li>");
        out.println("             <li><b>Vacc #2 Admin or Hist</b> Sets the second vaccination date to a random date about 2 months after the randomly selected patient's date of birth. Sets RXA-9 to indiate the whether this is administered or historical.</li>");
        out.println("           </ul>");
        out.println("         </li>");
        out.println("         <li><b>Quick Transforms Applied</b> These are the transformed that are currently being applied. Selecting a check box does not automatically update this area. To update this area, simply click Submit. The transforms listed here may be copied to the Custom Transforms area and changed as desired. </li>");
        out.println("         <li><b>Custom Transforms</b> You can indicate your own transforms by using the same format as the quick transforms. The format is this: <code>{SEG_NAME}[#{SEG_REP_NUM}]-{FIELD_NUM}[.{SUB_FIELD{NUM}]={value}</code>. The custom transforms will be run after the quick transforms. You may set any value here and it will be placed in the message exactly as you write it. It is okay to use HL7 special characters but they will not be escaped. Putting a blank value will blank out a specific field. Values such as PID#1-1 and PID-1 are equivalant, and PID-3.1 and PID-3 are also equivalant. There are also a set of defined values that you can access by placing the defined value code with bracket around it. Note: These values are generate each time a new message is generated, but stay the same while transforming the entire message, which means that if you use [BOY] a randomly choosen baby boy's name will be choosen but it will be the same throughout a transformation of a single message. The following defind value codes are supported:");
        out.println("           <ul>");
        out.println("             <li><b>[BOY]</b> A randomly choosen name for a list of 1000 most common male baby names in 2010.</li>");
        out.println("             <li><b>[GIRL]</b> A randomly choosen name for a list of 1000 most common female baby names in 2010.</li>");
        out.println("             <li><b>[FATHER]</b> A randomly choosen name for a list of 1000 most common male baby names in 2010.</li>");
        out.println("             <li><b>[MOTHER]</b> A randomly choosen name for a list of 1000 most common female baby names in 2010.</li>");
        out.println("             <li><b>[MOTHER_MAIDEN]</b> A randomly choosen name for a list of over 1000 names derived from current US county names.</li>");
        out.println("             <li><b>[DOB]</b> A randomly choosen date in 2009 or 2010.</li>");
        out.println("             <li><b>[NOW]</b> An HL7 formatted date and time representing the current time now.</li>");
        out.println("             <li><b>[TODAY]</b> An HL7 formatted date with no time representing the current date today.</li>");
        out.println("             <li><b>[LAST]</b> A randomly choosen name for a list of over 1000 names derived from current US county names.</li>");
        out.println("             <li><b>[GIRL_MIDDLE]</b> A randomly choosen name for a list of 1000 most common female baby names in 2010.</li>");
        out.println("             <li><b>[BOY_MIDDLE]</b> A randomly choosen name for a list of 1000 most common male baby names in 2010.</li>");
        out.println("             <li><b>[GIRL_MIDDLE_INITIAL] The first initial of [GIRL_MIDDLE]</b> </li>");
        out.println("             <li><b>[BOY_MIDDLE_INITIAL]</b> The first initial of [BOY_MIDDLE]</li>");
        out.println("             <li><b>[VAC1_DATE]</b> A randomly choosen date about 2 months after [DOB]</li>");
        out.println("             <li><b>[VAC2_DATE]</b> A randomly choosen date about 2 months after [VAC1_DATE]</li>");
        out.println("             <li><b>[VAC3_DATE]</b> A randomly choosen date about 2 months after [VAC2_DATE]</li>");
        out.println("             <li><b>[CITY]</b> A randomly choosen name of a city in Michigan.</li>");
        out.println("             <li><b>[STREET]</b> A randomly generated street with a street number between 1 and 400 a street name from a randomly choosen US county name, and the street type from a list of 5 commmon street types.</li>");
        out.println("             <li><b>[STATE]</b> The correct state for the real city picked [CITY]</li>");
        out.println("             <li><b>[ZIP]</b> The correct zip for the real city picked for [CITY]</li>");
        out.println("             <li><b>[PHONE]</b> A randomly generated phone, but with a area code correct for city picked for [CITY]. </li>");
        out.println("           </ul>");
        out.println("         </li>");
        out.println("       </ul>");
        out.println("     </li>");
        out.println("     <li><b>Test Case</b> After hitting the submit button a test case will be generated. This can be copied and then pasted in the data quality tester to verify the test and the immunization registry. </li>");
        out.println("   </ol>");
        out.println("  </div>");
        ClientServlet.printHtmlFoot(out);
      } catch (Exception e)
      {
        out.println("<p>Exception Occurred: " + e.getMessage() + "</p><pre>");
        e.printStackTrace(out);
        out.println("</pre>");
      } finally
      {
        out.close();
      }
    }
  }

  protected void makeButtons(List<TestCaseMessage> selectedTestCaseMessageList, PrintWriter out, int testCasePos)
  {
    if (selectedTestCaseMessageList != null)
    {
      boolean showPrev = testCasePos > 0;
      boolean showNext = (testCasePos + 1) < selectedTestCaseMessageList.size();
      out.println("            <input type=\"hidden\" name=\"testCasePos\" value=\"" + testCasePos + "\"/>");
      out.println("            <input type=\"submit\" name=\"action\" value=\"Prev\"" + (showPrev ? "" : "disabled=\"disabled\"") + "/>");
      out.println("            <input type=\"submit\" name=\"action\" value=\"Update\"/>");
      out.println("            <input type=\"submit\" name=\"action\" value=\"Next\"" + (showNext ? "" : "disabled=\"disabled\"") + "/>");
    } else
    {
      out.println("            <input type=\"submit\" name=\"method\" value=\"Update\"/>");
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest(request, response);

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
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest(request, response);

  }

  private static String isChecked(PatientType patientType1, PatientType patientType2)
  {
    if (patientType1 == patientType2)
    {
      return " checked";
    }
    return "";

  }

  private static String isChecked(String s, String[] extras)
  {
    if (extras != null)
    {
      for (String extra : extras)
      {
        if (s.equals(extra))
        {
          return " checked";
        }
      }
    }
    return "";

  }

  protected static Map<String, TestCaseMessage> getTestCaseMessageMap(HttpSession session)
  {
    Map<String, TestCaseMessage> testMessageMap = (Map<String, TestCaseMessage>) session.getAttribute("testCaseMessageMap");
    if (testMessageMap == null)
    {
      testMessageMap = new HashMap<String, TestCaseMessage>();
      session.setAttribute("testCaseMessageMap", testMessageMap);
    }
    return testMessageMap;
  }

  protected static void saveTestCase(TestCaseMessage testCaseMessage, HttpSession session)
  {
    if (testCaseMessage.getTestCaseNumber() != null && !testCaseMessage.getTestCaseNumber().equals(""))
    {
      Authenticate.User user = (Authenticate.User) session.getAttribute("user");
      if (user != null && user.hasSendData())
      {
        File testCaseDir = user.getSendData().getTestCaseDir();
        if (!testCaseMessage.getTestCaseSet().equals(""))
        {
          testCaseDir = new File(testCaseDir, testCaseMessage.getTestCaseSet());
          if (!testCaseDir.exists())
          {
            testCaseDir.mkdir();
          }
        }
        File testCaseFile = new File(testCaseDir, "TC-" + testCaseMessage.getTestCaseNumber() + ".txt");
        try
        {
          PrintWriter out = new PrintWriter(new FileWriter(testCaseFile));
          out.print(testCaseMessage.createText());
          out.close();
        } catch (IOException ioe)
        {
          ioe.printStackTrace();
          // unable to save, continue as normal
        }

      }
    }
  }

  protected static void loadTestCases(HttpSession session) throws ServletException, IOException
  {
    Authenticate.User user = (Authenticate.User) session.getAttribute("user");
    if (user != null && user.hasSendData())
    {
      File testCaseDir = user.getSendData().getTestCaseDir();
      readTestCases(session, testCaseDir);
      File[] dirs = testCaseDir.listFiles(new FileFilter() {
        public boolean accept(File arg0)
        {
          return arg0.isDirectory();
        }
      });
      if (dirs != null)
      {
        for (File dir : dirs)
        {
          readTestCases(session, dir);
        }
      }
    }
  }

  private static void readTestCases(HttpSession session, File testCaseDir) throws FileNotFoundException, IOException, ServletException
  {
    String[] filenames = testCaseDir.list(new FilenameFilter() {

      public boolean accept(File file, String arg1)
      {
        return arg1.startsWith("TC-") && arg1.endsWith(".txt");
      }
    });
    if (filenames != null)
    {
      for (String filename : filenames)
      {
        BufferedReader in = new BufferedReader(new FileReader(new File(testCaseDir, filename)));
        String line;
        StringBuilder testScript = new StringBuilder();
        while ((line = in.readLine()) != null)
        {
          testScript.append(line);
          testScript.append("\n");
        }
        in.close();

        List<TestCaseMessage> testCaseMessageList = TestCaseServlet.parseAndAddTestCases(testScript.toString(), session);
        for (TestCaseMessage testCaseMessage : testCaseMessageList)
        {
          if (!testCaseMessage.getTestCaseNumber().equals(""))
          {
            CreateTestCaseServlet.getTestCaseMessageMap(session).put(testCaseMessage.getTestCaseNumber(), testCaseMessage);
          }
        }

      }
    }
  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo()
  {
    return "Short description";

  }// </editor-fold>
}