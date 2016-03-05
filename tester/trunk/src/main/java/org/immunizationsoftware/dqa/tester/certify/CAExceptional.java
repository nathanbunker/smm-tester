package org.immunizationsoftware.dqa.tester.certify;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.transform.ScenarioManager;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;

public class CAExceptional extends CertifyArea
{

  public CAExceptional(CertifyRunner certifyRunner) {
    super("D", VALUE_TEST_SECTION_TYPE_EXCEPTIONAL, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    Transformer transformer = certifyRunner.transformer;
    int count = 100;
    count = createToleranceCheck("MSH-10 Message Control Id is very long", "MSH-10=" + somewhatRandomLongString(198),
        count);
    count = createToleranceCheck("MSH-300 is empty with bars all the way out", "MSH-300=1\nMSH-300=", count);
    count = createToleranceCheck("MSH-300 set to a value of 1", "MSH-300=1", count);
    count = createToleranceCheck("PID-3.4 Patient Id Assigning Authority is very long",
        "PID-3.4=" + somewhatRandomLongString(226), count);
    count = createToleranceCheck("PID-3.5 Patient Id is PI instead of MR", "PID-3.5=PI", count);
    count = createToleranceCheck("PID-7 Date/Time of Birth includes a time", "PID-7=20131003113759-0700", count);
    count = createToleranceCheck("PID-10 Race contains invalid race",
        "PID-10.1=KLINGON\nPID-10.2=Klingon\nPID-10.3=CDCREC", count);
    count = createToleranceCheck("PID-22 Ethnicity contains invalid ethnicity",
        "PID-22.1=KLINGON\nPID-22.2=Klingon\nPID-22.3=CDCREC", count);
    count = createToleranceCheck("PID-300 is empty with bars all the way out", "PID-300=1\nPID-300=", count);
    count = createToleranceCheck("PID-300 set to a value of 1", "PID-300=1", count);
    count = createToleranceCheck("PV1-10 Hospital service code is set to a non-standard value", "PV1-10=AMB", count);
    count = createToleranceCheck("Message has no vaccinations",
        "remove segment ORC all\nremove segment RXA all\nremove segment RXR all\nremove segment OBX all", count);
    count = createToleranceCheck("RXA-3 Date/Time Start of Administration includes time", "RXA-3=[NOW]", count);
    count = createToleranceCheck("RXA-4 Date/Time End of Administration is empty", "RXA-4=", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped field separator in it",
        "RXA-5.2=This \\F\\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped component in it", "RXA-5.2=This \\S\\ That",
        count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped sub component separator in it",
        "RXA-5.2=This \\T\\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped repetition separator in it",
        "RXA-5.2=This \\R\\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped escape separator in it",
        "RXA-5.2=This \\E\\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label as unescaped escape separator in it",
        "RXA-5.2=This \\ That", count);
    count = createToleranceCheck("RXA-5.2 Vaccination Label is set to a very long value",
        "RXA-5.2=This is a very long description for a vaccine, that normally you shouldn't expect to see, but since this field should not be read by the receiver it should be accepted, HL7 allows up to 199 chars. ",
        count);
    count = createToleranceCheck("RXA-17.2 Manufacturer Label is set to a very long value",
        "RXA-17.2=This is a very long description for a manufac, that normally you shouldn't expect to see, but since this field should not be read by the receiver it should be accepted, HL7 allows up to 199 chars. ",
        count);
    count = createToleranceCheck("RXA-17.2 Manufacturer Label includes an un-encoded ampersand", "RXA-17.2=Merk & Co",
        count);
    count = createToleranceCheck("RXA-300 is empty with bars all the way out", "RXA-300=1\nRXA-300=", count);
    count = createToleranceCheck("RXA-300 set to a value of 1", "RXA-300=1", count);
    {
      count++;
      TestCaseMessage testCaseMessage1 = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage1.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Same message to be sent again");
      register(count, 1, testCaseMessage1);
      
      count++;
      TestCaseMessage testCaseMessage2 = new TestCaseMessage();
      testCaseMessage2.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Same message as before");
      testCaseMessage2.setMessageText(testCaseMessage1.getMessageText());
      register(count, 1, testCaseMessage2);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Message separated by CR+LF");
      testCaseMessage.setLineEnding("\r\n");
      register(count, 1, testCaseMessage);
    }
    count = createToleranceCheck("PID-13 Phone number is missing area code", "PID-13.6=", count);
    count = createToleranceCheck("PID-13 Phone number is a valid international phone number", "PID-13.5=44\nPID-13.6=1304\nPID-13.6=827585", count);
    count = createToleranceCheck("PID-5.2 Patient last name is only one character", "PID-5.1=[TRUNC 1]", count);
    count = createToleranceCheck("PID-5.2 Patient first name is only one character", "PID-5.2=[TRUNC 1]", count);
    count = createToleranceCheck("PID-5.2 Patient first name is BOYD", "PID-5.2=BOYD", count);
    count = createToleranceCheck("PID-5.1 Patient last name is TESTA", "PID-5.1=Testa", count);
    count = createToleranceCheck("PID-5.1 Patient last name is NONE", "PID-5.1=None", count);
    count = createToleranceCheck("PID-5.1 Patient last name has dash", "PID-5.1=Thomas-Smith", count);
    count = createToleranceCheck("PID-5.1 Patient last name has period", "PID-5.1=St. Thomas", count);
    count = createToleranceCheck("PID-5.1 Patient last name has apostrophe", "PID-5.1=O'Reilly", count);
    count = createToleranceCheck("PID-11 Patient address is valid international address", "PID-11.1=93 Wyandotte St E\nPID-11.3=Windsor\nPID-11.4=ON\nPID-11.5=N9A 3H1\nPID-11.6=CAN", count);
    count = 200;
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendOriginalMessage("OBX|5|NM|6287-7^Baker's yeast IgE Ab in Serum^LN||1945||||||F\n");
      testCaseMessage.setDescription(
          VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Message includes observation not typically sent to IIS");
      register(count, 1, testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendOriginalMessage("YES|This|is|a|segment^you^should^never^see|in|production\n");
      testCaseMessage
          .setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Message includes segment not defined by HL7");
      register(count, 1, testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Future Functionality Check: Observation at patient level (HL7 2.8 capability)");
      testCaseMessage.appendCustomTransformation("insert segment OBX after NK1");
      testCaseMessage.appendCustomTransformation("OBX-1=1");
      testCaseMessage.appendCustomTransformation("OBX-2=59784-9");
      testCaseMessage.appendCustomTransformation("OBX-2.2=Disease with presumed immunity");
      testCaseMessage.appendCustomTransformation("OBX-2.3=LN");
      testCaseMessage.appendCustomTransformation("OBX-3=1");
      testCaseMessage.appendCustomTransformation("OBX-5=38907003");
      testCaseMessage.appendCustomTransformation("OBX-5.2=Varicella infection");
      testCaseMessage.appendCustomTransformation("OBX-5.3=SCT");
      testCaseMessage.appendCustomTransformation("OBX-11=F");
      register(count, 1, testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Future Functionality Check: NDC and CVX code");
      testCaseMessage.appendCustomTransformation("RXA-5.1=00006-4047-20");
      testCaseMessage.appendCustomTransformation("RXA-5.2=RotaTeq");
      testCaseMessage.appendCustomTransformation("RXA-5.3=NDC");
      testCaseMessage.appendCustomTransformation("RXA-5.4=116");
      testCaseMessage.appendCustomTransformation("RXA-5.5=rotavirus, pentavalent");
      testCaseMessage.appendCustomTransformation("RXA-5.6=CVX");
      testCaseMessage.appendCustomTransformation("RXA-17.1=MSD");
      testCaseMessage.appendCustomTransformation("RXA-17.2=Merck and Co., Inc.");
      testCaseMessage.appendCustomTransformation("RXA-17.3=MVX");
      register(count, 1, testCaseMessage);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Future Functionality Check: CVX and NDC code");
      testCaseMessage.appendCustomTransformation("RXA-5.1=116");
      testCaseMessage.appendCustomTransformation("RXA-5.2=rotavirus, pentavalent");
      testCaseMessage.appendCustomTransformation("RXA-5.3=CVX");
      testCaseMessage.appendCustomTransformation("RXA-5.4=00006-4047-20");
      testCaseMessage.appendCustomTransformation("RXA-5.5=RotaTeq");
      testCaseMessage.appendCustomTransformation("RXA-5.6=NDC");
      testCaseMessage.appendCustomTransformation("RXA-17.1=MSD");
      testCaseMessage.appendCustomTransformation("RXA-17.2=Merck and Co., Inc.");
      testCaseMessage.appendCustomTransformation("RXA-17.3=MVX");
      register(count, 1, testCaseMessage);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Future Functionality Check: NDC only (no CVX)");
      testCaseMessage.appendCustomTransformation("RXA-5.1=00006-4047-20");
      testCaseMessage.appendCustomTransformation("RXA-5.2=RotaTeq");
      testCaseMessage.appendCustomTransformation("RXA-5.3=NDC");
      testCaseMessage.appendCustomTransformation("RXA-17.1=MSD");
      testCaseMessage.appendCustomTransformation("RXA-17.2=Merck and Co., Inc.");
      testCaseMessage.appendCustomTransformation("RXA-17.3=MVX");
      register(count, 1, testCaseMessage);
    }
    // TODO add examples of message construction issues that should be ignored

    try {
      count = 0;
      BufferedReader in = new BufferedReader(
          new InputStreamReader(getClass().getResourceAsStream("exampleCertifiedMessages.txt")));
      String line;
      StringBuilder sb = new StringBuilder();
      String previousDescription = null;
      String description = null;
      while ((line = in.readLine()) != null) {
        line = line.trim();
        if (line.startsWith("--")) {
          description = line.substring(2).trim();
        } else if (line.length() > 3) {
          if (line.startsWith("MSH|^~\\&|")) {
            if (sb != null && sb.length() > 0) {
              count = createCertfiedMessageTestCaseMessage(transformer, count, sb, previousDescription);
            }
            sb = new StringBuilder();
            previousDescription = description;
            description = null;
          }
          if (sb != null) {
            sb.append(line);
            sb.append("\r");
          }
        }
      }
      if (sb != null && sb.length() > 0) {
        count = createCertfiedMessageTestCaseMessage(transformer, count, sb, previousDescription);
      }

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public int createToleranceCheck(String label, String customTransformation, int count) {
    count++;
    TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    testCaseMessage.setAdditionalTransformations(customTransformation + "\n");
    testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " " + label);
    register(count, 1, testCaseMessage);
    return count;
  }

  private int createCertfiedMessageTestCaseMessage(Transformer transformer, int count, StringBuilder sb,
      String previousDescription) {
    count++;
    String messageText = sb.toString();
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(messageText);
    changePatientIdentifyingInformation(messageText, testCaseMessage);
    testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_CERTIFIED_MESSAGE + " "
        + (previousDescription == null ? "Unidentified EHR" : previousDescription));
    register(count, 2, testCaseMessage);
    return count;

  }

  private void changePatientIdentifyingInformation(String messageText, TestCaseMessage testCaseMessage) {
    HL7Reader hl7Reader = new HL7Reader(messageText);
    testCaseMessage
        .appendCustomTransformation("MSH-10=" + messageText.hashCode() + "." + (System.currentTimeMillis() % 10000));
    if (hl7Reader.advanceToSegment("PID")) {
      String dob = hl7Reader.getValue(7);
      if (dob.length() >= 8) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
          Date d = sdf.parse(dob.substring(0, 8));
          Calendar c = Calendar.getInstance();
          c.setTime(d);
          int dateOffset = (int) (System.currentTimeMillis() % 100);
          c.add(Calendar.DAY_OF_MONTH, -dateOffset);
          dob = sdf.format(c.getTime());
          testCaseMessage.appendCustomTransformation("PID-7=" + dob);
        } catch (ParseException pe) {
          // ignore
        }
      }
      String sex = hl7Reader.getValue(8);
      testCaseMessage.appendCustomTransformation("PID-5.1=~90%[LAST]:[MOTHER_MAIDEN]");
      if (sex.equals("M")) {
        testCaseMessage.appendCustomTransformation("PID-5.2=[BOY]");
      } else {
        testCaseMessage.appendCustomTransformation("PID-5.2=[GIRL]");
      }
      String middleName = hl7Reader.getValue(5, 3);
      if (!middleName.equals("")) {
        if (middleName.length() > 1) {
          if (sex.equals("M")) {
            testCaseMessage.appendCustomTransformation("PID-5.3=[BOY_MIDDLE]");
          } else {
            testCaseMessage.appendCustomTransformation("PID-5.3=[GIRL_MIDDLE]");
          }
        } else {
          testCaseMessage.appendCustomTransformation("PID-5.3=[BOY_MIDDLE_INITIAL]");
        }
      }
      testCaseMessage.appendCustomTransformation("PID-5.3");
      String streetAdd = hl7Reader.getValue(11);
      if (!streetAdd.equals("")) {
        testCaseMessage.appendCustomTransformation("PID-11.1=[STREET]");
      }
      String motherMaiden = hl7Reader.getValue(6);
      if (!motherMaiden.equals("")) {
        testCaseMessage.appendCustomTransformation("PID-6=~50%[MOTHER_MAIDEN]:[LAST_DIFFERENT]");
      }
      if (!hl7Reader.getValue(6, 2).equals("")) {
        testCaseMessage.appendCustomTransformation("PID-6.2=[MOTHER]");
      }
      String phone = hl7Reader.getValue(13, 7);
      if (!phone.equals("")) {
        if (!hl7Reader.getValue(13, 6).equals("")) {
          testCaseMessage.appendCustomTransformation("PID-13.6=[PHONE_AREA]");
        }
        testCaseMessage.appendCustomTransformation("PID-13.7=[PHONE_LOCAL]");
      }
      String mrnType;
      int i = 1;
      while (!(mrnType = hl7Reader.getValueRepeat(3, 5, i)).equals("")) {
        if (mrnType.equals("SS")) {
          testCaseMessage.appendCustomTransformation("PID-3#" + i + "=[SSN]");
        } else if (mrnType.equals("MA")) {
          testCaseMessage.appendCustomTransformation("PID-3#" + i + "=[MEDICAID]");
        } else {
          testCaseMessage.appendCustomTransformation("PID-3#" + i + "=[MRN]");
        }
        i++;
      }
      while (hl7Reader.advanceToSegment("NK1")) {
        String type = hl7Reader.getValue(3);
        if (type.equals("") || type.equals("MTH") || type.equals("GRD")) {
          if (!hl7Reader.getValue(2).equals("")) {
            testCaseMessage.appendCustomTransformation("NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]");
          }
          if (!hl7Reader.getValue(2, 1).equals("")) {
            testCaseMessage.appendCustomTransformation("NK1-2.2=[MOTHER]");
          }
        } else {
          if (!hl7Reader.getValue(2).equals("")) {
            testCaseMessage.appendCustomTransformation("NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]");
          }
          if (!hl7Reader.getValue(2, 1).equals("")) {
            testCaseMessage.appendCustomTransformation("NK1-2.2=[FATHER]");
          }
        }
      }
    }
  }

  private static String somewhatRandomLongString(int length) {
    String s = "";
    int count = 0;
    while (s.length() < length) {
      long r = System.currentTimeMillis();
      r = (r << count) - r;
      s = s + r;
      count++;
    }
    return s.substring(0, length);
  }

  @Override
  public void sendUpdates() {
    runUpdates();
  }

  @Override
  public void prepareQueries() {
    doPrepareQueries();
  }

  @Override
  public void sendQueries() {
    runQueries();
  }
}
