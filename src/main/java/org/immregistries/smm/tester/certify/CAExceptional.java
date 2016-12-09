package org.immregistries.smm.tester.certify;

import static org.immregistries.smm.transform.ScenarioManager.SCENARIO_1_R_ADMIN_CHILD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.transform.ScenarioManager;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;

public class CAExceptional extends CertifyArea {

  public CAExceptional(CertifyRunner certifyRunner) {
    super("D", VALUE_TEST_SECTION_TYPE_EXCEPTIONAL, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    Transformer transformer = certifyRunner.transformer;
    int count = 100;
    count = createToleranceCheck("MSH-10 Message Control Id is very long", "MSH-10=" + somewhatRandomLongString(198), count, "MSH-10");
    count = createToleranceCheck("MSH-300 is empty with bars all the way out", "MSH-300=1\nMSH-300=", count, "MSH-300");
    count = createToleranceCheck("MSH-300 set to a value of 1", "MSH-300=1", count, "MSH-300");
    count = createToleranceCheck("PID-3.4 Patient Id Assigning Authority is very long", "PID-3.4=" + somewhatRandomLongString(226), count, "PID-3.4");
    count = createToleranceCheck("PID-3.5 Patient Id is PI instead of MR", "PID-3.5=PI", count, "PID-3.5");
    count = createToleranceCheck("PID-7 Date/Time of Birth includes a time", "PID-7=20131003113759-0700", count, "PID-7");
    count = createToleranceCheck("PID-10 Race contains invalid race", "PID-10.1=KLINGON\nPID-10.2=Klingon\nPID-10.3=CDCREC", count, "PID-10");
    count = createToleranceCheck("PID-22 Ethnicity contains invalid ethnicity", "PID-22.1=KLINGON\nPID-22.2=Klingon\nPID-22.3=CDCREC", count,
        "PID-22");
    count = createToleranceCheck("PID-300 is empty with bars all the way out", "PID-300=1\nPID-300=", count, "PID-300");
    count = createToleranceCheck("PID-300 set to a value of 1", "PID-300=1", count, "PID-300");
    count = createToleranceCheck("PV1-10 Hospital service code is set to a non-standard value", "PV1-10=AMB", count, "PV1-10");
    count = createToleranceCheck("Message has no vaccinations",
        "remove segment ORC all\nremove segment RXA all\nremove segment RXR all\nremove segment OBX all", count, "");
    count = createToleranceCheck("RXA-3 Date/Time Start of Administration includes time", "RXA-3=[NOW]", count, "RXA-3");
    count = createToleranceCheck("RXA-4 Date/Time End of Administration is empty", "RXA-4=", count, "RXA-4");
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped field separator in it", "RXA-5.2=This \\F\\ That", count, "RXA-5.2");
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped component in it", "RXA-5.2=This \\S\\ That", count, "RXA-5.2");
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped sub component separator in it", "RXA-5.2=This \\T\\ That", count, "RXA-5.2");
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped repetition separator in it", "RXA-5.2=This \\R\\ That", count, "RXA-5.2");
    count = createToleranceCheck("RXA-5.2 Vaccination Label as escaped escape separator in it", "RXA-5.2=This \\E\\ That", count, "RXA-5.2");
    count = createToleranceCheck("RXA-5.2 Vaccination Label as unescaped escape separator in it", "RXA-5.2=This \\ That", count, "RXA-5.2");
    count = createToleranceCheck(
        "RXA-5.2 Vaccination Label is set to a very long value",
        "RXA-5.2=This is a very long description for a vaccine, that normally you shouldn't expect to see, but since this field should not be read by the receiver it should be accepted, HL7 allows up to 199 chars. ",
        count, "RXA-5.2");
    count = createToleranceCheck(
        "RXA-17.2 Manufacturer Label is set to a very long value",
        "RXA-17.2=This is a very long description for a manufac, that normally you shouldn't expect to see, but since this field should not be read by the receiver it should be accepted, HL7 allows up to 199 chars. ",
        count, "RXA-17.2");
    count = createToleranceCheck("RXA-17.2 Manufacturer Label includes an un-encoded ampersand", "RXA-17.2=Merk & Co", count, "RXA-17.2");
    count = createToleranceCheck("RXA-300 is empty with bars all the way out", "RXA-300=1\nRXA-300=", count, "RXA-300");
    count = createToleranceCheck("RXA-300 set to a value of 1", "RXA-300=1", count, "RXA-300");
    {
      count++;
      TestCaseMessage testCaseMessage1 = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage1.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Same message to be sent again");
      testCaseMessage1.setFieldName("");
      register(count, 1, testCaseMessage1);

      count++;
      TestCaseMessage testCaseMessage2 = new TestCaseMessage();
      testCaseMessage2.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Same message as before");
      testCaseMessage2.setFieldName("");
      testCaseMessage2.setMessageText(testCaseMessage1.getMessageText());
      testCaseMessage2.setUpdateTestCaseMessage(testCaseMessage1);
      testCaseMessage2.setOriginalMessage(testCaseMessage1.getMessageText());
      register(count, 1, testCaseMessage2);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Message separated by CR+LF");
      testCaseMessage.setLineEnding("\r\n");
      register(count, 1, testCaseMessage);
    }
    count = createToleranceCheck("PID-13 Phone number is missing area code", "PID-13.6=", count, "PD1-13");
    count = createToleranceCheck("PID-13 Phone number is a valid international phone number", "PID-13.5=44\nPID-13.6=1304\nPID-13.6=827585", count,
        "PD1-13");
    count = createToleranceCheck("PID-5.2 Patient last name is only one character", "PID-5.1=[TRUNC 1]", count, "PID-5.2");
    count = createToleranceCheck("PID-5.2 Patient first name is only one character", "PID-5.2=[TRUNC 1]", count, "PID-5.2");
    count = createToleranceCheck("PID-5.2 Patient first name is BOYD", "PID-5.2=BOYD", count, "PID-5.2");
    count = createToleranceCheck("PID-5.1 Patient last name is TESTA", "PID-5.1=Testa", count, "PID-5.1");
    count = createToleranceCheck("PID-5.1 Patient last name is NONE", "PID-5.1=None", count, "PID-5.1");
    count = createToleranceCheck("PID-5.1 Patient last name has dash", "PID-5.1=Thomas-Smith", count, "PID-5.1");
    count = createToleranceCheck("PID-5.1 Patient last name has period", "PID-5.1=St. Thomas", count, "PID-5.1");
    count = createToleranceCheck("PID-5.1 Patient last name has apostrophe", "PID-5.1=O'Reilly", count, "PID-5.1");
    count = createToleranceCheck("PID-11 Patient address is valid international address",
        "PID-11.1=93 Wyandotte St E\nPID-11.3=Windsor\nPID-11.4=ON\nPID-11.5=N9A 3H1\nPID-11.6=CAN", count, "PID-11");
    count = createToleranceCheck("PID-5.1 Patient last name is NEW", "PID-5.1=New", count, "PID-5.1");
    count = createToleranceCheck("PID-5.1 Patient last name is TEST", "PID-5.1=Test", count, "PID-5.1");
    count = createToleranceCheck("PID-5.1 Patient last name is BAD", "PID-5.1=Bad", count, "PID-5.1");

    count = 200;
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendOriginalMessage("OBX|5|NM|6287-7^Baker's yeast IgE Ab in Serum^LN||1945||||||F\n");
      testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Message includes observation not typically sent to IIS");
      register(count, 1, testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.appendOriginalMessage("YES|This|is|a|segment^you^should^never^see|in|production\n");
      testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " Message includes segment not defined by HL7");
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
      testCaseMessage.setDescription("Repetition out-of-order: Patient alias sent first");
      testCaseMessage.appendCustomTransformation("PID-5.1#2=[PID-5.1]");
      testCaseMessage.appendCustomTransformation("PID-5.2#2=[PID-5.2]");
      testCaseMessage.appendCustomTransformation("PID-5.3#2=[PID-5.3]");
      testCaseMessage.appendCustomTransformation("PID-5.7#2=[PID-5.7]");
      testCaseMessage.appendCustomTransformation("PID-5.1=[PID-6.1]");
      testCaseMessage.appendCustomTransformation("PID-5.2=Avery");
      testCaseMessage.appendCustomTransformation("PID-5.3=");
      testCaseMessage.appendCustomTransformation("PID-5.7=A");
      register(count, 1, testCaseMessage);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Repetition out-of-order: Patient bad address sent first");
      testCaseMessage.appendCustomTransformation("PID-11.1#2=[PID-11.1]");
      testCaseMessage.appendCustomTransformation("PID-11.2#2=[PID-11.2]");
      testCaseMessage.appendCustomTransformation("PID-11.3#2=[PID-11.3]");
      testCaseMessage.appendCustomTransformation("PID-11.4#2=[PID-11.4]");
      testCaseMessage.appendCustomTransformation("PID-11.5#2=[PID-11.5]");
      testCaseMessage.appendCustomTransformation("PID-11.6#2=[PID-11.6]");
      testCaseMessage.appendCustomTransformation("PID-11.7#2=[PID-11.7]");
      testCaseMessage.appendCustomTransformation("PID-11.1=123 Main Street");
      testCaseMessage.appendCustomTransformation("PID-11.2=BAD ADDRESS");
      testCaseMessage.appendCustomTransformation("PID-11.3=Anytown");
      testCaseMessage.appendCustomTransformation("PID-11.4=MI");
      testCaseMessage.appendCustomTransformation("PID-11.5=12345");
      testCaseMessage.appendCustomTransformation("PID-11.6=USA");
      testCaseMessage.appendCustomTransformation("PID-11.7=BA");
      register(count, 1, testCaseMessage);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Repetition out-of-order: Patient bad address sent first, current or temporary next");
      testCaseMessage.appendCustomTransformation("PID-11.1#2=[PID-11.1]");
      testCaseMessage.appendCustomTransformation("PID-11.2#2=[PID-11.2]");
      testCaseMessage.appendCustomTransformation("PID-11.3#2=[PID-11.3]");
      testCaseMessage.appendCustomTransformation("PID-11.4#2=[PID-11.4]");
      testCaseMessage.appendCustomTransformation("PID-11.5#2=[PID-11.5]");
      testCaseMessage.appendCustomTransformation("PID-11.6#2=[PID-11.6]");
      testCaseMessage.appendCustomTransformation("PID-11.7#2=C");
      testCaseMessage.appendCustomTransformation("PID-11.1=123 Main Street");
      testCaseMessage.appendCustomTransformation("PID-11.2=BAD ADDRESS");
      testCaseMessage.appendCustomTransformation("PID-11.3=Anytown");
      testCaseMessage.appendCustomTransformation("PID-11.4=MI");
      testCaseMessage.appendCustomTransformation("PID-11.5=12345");
      testCaseMessage.appendCustomTransformation("PID-11.6=USA");
      testCaseMessage.appendCustomTransformation("PID-11.7=BA");
      register(count, 1, testCaseMessage);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Repetition out-of-order: Patient bad address sent first, legal next");
      testCaseMessage.appendCustomTransformation("PID-11.1#2=[PID-11.1]");
      testCaseMessage.appendCustomTransformation("PID-11.2#2=[PID-11.2]");
      testCaseMessage.appendCustomTransformation("PID-11.3#2=[PID-11.3]");
      testCaseMessage.appendCustomTransformation("PID-11.4#2=[PID-11.4]");
      testCaseMessage.appendCustomTransformation("PID-11.5#2=[PID-11.5]");
      testCaseMessage.appendCustomTransformation("PID-11.6#2=[PID-11.6]");
      testCaseMessage.appendCustomTransformation("PID-11.7#2=L");
      testCaseMessage.appendCustomTransformation("PID-11.1=123 Main Street");
      testCaseMessage.appendCustomTransformation("PID-11.2=BAD ADDRESS");
      testCaseMessage.appendCustomTransformation("PID-11.3=Anytown");
      testCaseMessage.appendCustomTransformation("PID-11.4=MI");
      testCaseMessage.appendCustomTransformation("PID-11.5=12345");
      testCaseMessage.appendCustomTransformation("PID-11.6=USA");
      testCaseMessage.appendCustomTransformation("PID-11.7=BA");
      register(count, 1, testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Repetition out-of-order: Patient fax number sent first");
      testCaseMessage.appendCustomTransformation("PID-13.1#2=[PID-13.1]");
      testCaseMessage.appendCustomTransformation("PID-13.2#2=[PID-13.2]");
      testCaseMessage.appendCustomTransformation("PID-13.3#2=[PID-13.3]");
      testCaseMessage.appendCustomTransformation("PID-13.4#2=[PID-13.4]");
      testCaseMessage.appendCustomTransformation("PID-13.5#2=[PID-13.5]");
      testCaseMessage.appendCustomTransformation("PID-13.6#2=[PID-13.6]");
      testCaseMessage.appendCustomTransformation("PID-13.7#2=[PID-13.7]");
      testCaseMessage.appendCustomTransformation("PID-13.2=ORN");
      testCaseMessage.appendCustomTransformation("PID-13.3=FX");
      testCaseMessage.appendCustomTransformation("PID-13.7=7218556");
      register(count, 1, testCaseMessage);
    }

    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Repetition out-of-order: Vaccination comment sent first");
      testCaseMessage.appendCustomTransformation("RXA-9.1#2=[RXA-9.1]");
      testCaseMessage.appendCustomTransformation("RXA-9.2#2=[RXA-9.2]");
      testCaseMessage.appendCustomTransformation("RXA-9.3#2=[RXA-9.3]");
      testCaseMessage.appendCustomTransformation("RXA-9.1=");
      testCaseMessage.appendCustomTransformation("RXA-9.2=This is a comment about the vacccination given to the patient");
      testCaseMessage.appendCustomTransformation("RXA-9.3=");
      register(count, 1, testCaseMessage);
    }
    {
      count++;
      TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
      testCaseMessage.setDescription("Repetition out-of-order: More than one lot number sent");
      testCaseMessage.appendCustomTransformation("RXA-15#2=[RXA-15]");
      register(count, 1, testCaseMessage);
    }

    count = createToleranceCheck("NK1-35 Race is valued with code table HL70005", "NK1-35=[PID-10]\nNK1-35.2=[PID-10.2]\nNK1-35.3=HL70005", count,
        "NK1-35");
    count = createToleranceCheck("NK1-35 Race is valued with code table CDCREC", "NK1-35=[PID-10]\nNK1-35.2=[PID-10.2]\nNK1-35.3=CDCREC", count,
        "NK1-35");

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
      BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("exampleCertifiedMessages.txt")));
      String line;
      StringBuilder sb = new StringBuilder();
      String previousDescription = null;
      String description = null;
      int masterCount = 0;
      while ((line = in.readLine()) != null) {
        line = line.trim();
        if (line.startsWith("--")) {
          description = line.substring(2).trim();
          int posSpace = description.indexOf(" ");
          int posColon = description.indexOf(":");
          if (posSpace < 0 || posColon < 0 || posSpace < posColon) {
            count++;
          } else {
            masterCount = Integer.parseInt(description.substring(0, posColon).trim());
            count = Integer.parseInt(description.substring(posColon + 1, posSpace).trim());
            description = description.substring(posSpace).trim();
          }
        } else if (line.length() > 3) {
          if (line.startsWith("MSH|^~\\&|")) {
            if (sb != null && sb.length() > 0) {
              createCertfiedMessageTestCaseMessage(transformer, masterCount, count, sb, previousDescription);
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
        createCertfiedMessageTestCaseMessage(transformer, masterCount, count, sb, previousDescription);
      }

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public int createToleranceCheck(String label, String customTransformation, int count, String fieldName) {
    count++;
    TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    testCaseMessage.setAdditionalTransformations(customTransformation + "\n");
    testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK + " " + label);
    testCaseMessage.setFieldName(fieldName);
    register(count, 1, testCaseMessage);
    return count;
  }

  private void createCertfiedMessageTestCaseMessage(Transformer transformer, int masterCount, int count, StringBuilder sb, String previousDescription) {
    String messageText = sb.toString();
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(messageText);
    changePatientIdentifyingInformation(messageText, testCaseMessage);
    testCaseMessage.setDescription(VALUE_EXCEPTIONAL_PREFIX_CERTIFIED_MESSAGE + " "
        + (previousDescription == null ? "Unidentified EHR" : previousDescription));
    register(count, masterCount, testCaseMessage);
  }

  private void changePatientIdentifyingInformation(String messageText, TestCaseMessage testCaseMessage) {
    HL7Reader hl7Reader = new HL7Reader(messageText);
    testCaseMessage.appendCustomTransformation("MSH-10=" + messageText.hashCode() + "." + (System.currentTimeMillis() % 10000));
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
