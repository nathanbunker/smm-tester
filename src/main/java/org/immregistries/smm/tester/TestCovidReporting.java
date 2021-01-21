/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.immregistries.smm.tester;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.immregistries.smm.SoftwareVersion;
import org.immregistries.smm.mover.ConnectionManager;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.mover.ShutdownInterceptor;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.run.TestRunner;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;

/**
 * 
 * @author nathan
 */
public class TestCovidReporting {

  private static boolean okayToKeepSending = true;

  public static void main(String[] args) throws Exception {
    ConnectionManager.setScanDirectories(false);
    if (args.length < 1) {
      System.err.println("Usage: java  org.immregistries.smm.tester.TestConnect <file root>");
      return;
    }
    {
      File file = new File(args[0]);
      if (!file.exists()) {
        System.err.println("Unable find file root, folder does not exist");
        return;
      } else if (!file.isDirectory()) {
        System.err.println("Not a file folder");
        return;
      }
    }
    TestCovidReporting testCovidReporting = new TestCovidReporting(args[0]);
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.print(">> ");
      String command = scanner.nextLine();

      if (command.equals("shutdown")) {
        break;
      } else {
        try {
          int i = Integer.parseInt(command);
          testCovidReporting.trigger(i);
        } catch (NumberFormatException nfe) {
          // ignore
        }
      }
    }
    scanner.close();
    okayToKeepSending = false;
    testCovidReporting.closeClinic();
  }

  private List<CovidClinic> covidClinicList;
  private List<SendData> sendDataList;
  private static Random random = new Random();

  public TestCovidReporting(String rootFolder) {
    System.out.println("Initializing Test Covid Reporting");
    System.out.println("  + Version: " + SoftwareVersion.VERSION);
    System.out.println("  + Initializing send data manager");
    ConnectionManager connectionManager = new ConnectionManager();
    connectionManager.setScanStartFolders(rootFolder);
    connectionManager.init();
    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException ie) {
      // just keep going
    }
    System.out.println("Covid Clinic will open in 3 seconds...");
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException ie) {
      // just keep going
    }

    int i = 0;
    sendDataList = ConnectionManager.getSendDataList();
    covidClinicList = new ArrayList<>();
    for (SendData sendData : sendDataList) {
      i++;
      CovidClinic covidClinic = new CovidClinic(sendData);
      covidClinic.start();
      System.out.println("  [" + i + "] " + sendData.getConnector().getLabel() + " is open");
      covidClinicList.add(covidClinic);
    }
  }

  private void trigger(int i) {
    if (i > 0 && i <= covidClinicList.size()) {
      covidClinicList.get(i - 1).triggerEvent();
    }
  }

  public void closeClinic() {

    System.out.println("Shutting down connection manager...");
    ShutdownInterceptor shutdown = new ShutdownInterceptor();
    shutdown.run();

    System.out.println("Clinics are closed");

  }



  private class CovidClinic extends Thread {
    private SendData sendData;
    private Options options;

    public CovidClinic(SendData sendData) {
      this.sendData = sendData;
      options = new Options();
      options.setAdministeredPercentage(0.7);
      options.setRefusedPercentage(0.5);
    }

    @Override
    public void run() {
      while (okayToKeepSending) {
        try {
          TimeUnit.SECONDS.sleep(random.nextInt(12));
        } catch (InterruptedException ie) {
          // just keep going
        }
        if (okayToKeepSending) {
          triggerEvent();
        }
      }
    }

    public void triggerEvent() {
      Connector connector = sendData.getConnector();

      TestCaseMessage testCaseMessage = new TestCaseMessage();
      testCaseMessage.setOriginalMessage(createHL7Message(options));
      testCaseMessage.setMessageText(testCaseMessage.getOriginalMessage());
      System.out.print(testCaseMessage.getMessageText());
      TestRunner testRunner = new TestRunner();
      testRunner.setValidateResponse(false);
      try {
        testRunner.runTest(connector, testCaseMessage);
        System.out.println("Message sent to " + sendData.getConnector().getLabel());
      } catch (Throwable t) {
        System.out.println("Exception encountered");
        t.printStackTrace(System.err);
      }
    }


  }


  private static final int CVX = 0;
  private static final int CVX_LABEL = 1;
  private static final int NDC = 2;
  private static final int MVX = 3;
  private static final int MVX_LABEL = 4;
  private static final String[][] vaccineCodes = new String[][] {
      new String[] {"900", "COVID Vaccine A", "99999-9999-00", "AAA", "Vaccine Manufacture A"},
      new String[] {"901", "COVID Vaccine B", "99999-9999-01", "BBB", "Vaccine Manufacture B"},
      new String[] {"902", "COVID Unspecified", null, null, null}};

  private static final String[][] raceCodes = new String[][] {
      new String[] {"1002-5", "American Indian or Alaska Native"}, new String[] {"2028-9", "Asian"},
      new String[] {"2076-8", "Native Hawaiian or Other Pacific Islander"},
      new String[] {"2054-5", "Black or African American"}, new String[] {"2106-3", "White"},
      new String[] {"2131-1", "Other Race"}, new String[] {"", ""}};

  private static final String[][] clinicans =
      new String[][] {new String[] {"50778", "Lemon", "Mike", "A", "MD"},
          new String[] {"60500", "Kiwi", "Harold", "Thomas", "RN"},
          new String[] {"45588", "Orange", "Sarah", "Kane", "NP"},
          new String[] {"10887", "Pear", "Patricia", "", "PA"},
          new String[] {"60503", "Cunningham", "Sarah", "E", "LPN"},
          new String[] {"20033", "Power", "Krill", "T", "M.D."}};

  private static final String[][] bodySites = new String[][] {new String[] {"LT", "Left thigh"},
      new String[] {"LA", "Left arm"}, new String[] {"LD", "Left deltoid"},
      new String[] {"LG", "Left gluteus medius"}, new String[] {"LVL", "left vastus lateralis"},
      new String[] {"LLFA", "left lower forearm"}, new String[] {"RT", "right thigh"},
      new String[] {"RA", "right arm"}, new String[] {"RD", "right deltoid"},
      new String[] {"RG", "right gluteus medius"}, new String[] {"RVL", "right vastus lateralis"},
      new String[] {"RLFA", "right lower forearm"}};

  private static final String[][] bodyRoutes = new String[][] {
      new String[] {"C38238", "Intradermal"}, new String[] {"C28161", "Intramuscular"},
      new String[] {"C38284", "Nasal"}, new String[] {"C38276", "Intravenous"},
      new String[] {"C38288", "Oral"}, new String[] {"C38676", "Percutaneous"},
      new String[] {"C38299", "Subcutaneous"}, new String[] {"C38305", "Transdermal"}};



  public static String createHL7Message(Options options) {

    StringBuffer message = new StringBuffer();
    // MSH
    message.append(
        "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|20120814082240-0500||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    // PID
    String race = generateRace();
    String sex = random.nextBoolean() ? "M" : "F";
    String middleName = random.nextBoolean() ? "Ainsley" : random.nextBoolean() ? "A" : "";
    message.append("PID|1||D26376273^^^NIST MPI^MR||Snow^Madelynn^" + middleName
        + "^^^^L|Lam^Morgan^^^^^M|20070706|" + sex + "||" + race
        + "|32 Prescott Street Ave^^Warwick^MA^02452^USA^L^^49503||^PRN^PH^^^657^5558563|||||||||2186-5^non Hispanic or Latino^CDCREC\r");
    // PD1
    message
        .append("PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20120814|20120814\r");

    // NK1
    message.append(
        "NK1|1|Lam^Morgan^^^^^L|MTH^Mother^HL70063|32 Prescott Street Ave^^Warwick^MA^02452^USA^L|^PRN^PH^^^657^5558563\r");

    String[] vaccineCode = vaccineCodes[random.nextInt(vaccineCodes.length)];

    if (random.nextDouble() < options.getAdministeredPercentage()) {
      // Administered vaccination
      printAdministeredVaccination(message, vaccineCode, "20120814");
      addComorbidityAndSerology(options, message, 4);
      if (random.nextBoolean()) {
        printAdministeredVaccination(message, vaccineCode, "20120717");
      }
    } else {
      if (random.nextDouble() < options.getRefusedPercentage()) {
        message.append(
            "ORC|RE||9999^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|||||||NISTEHRFAC^NISTEHRFacility^HL70362|\r");
        message.append("RXA|0|1|20120814||" + vaccineCode[CVX] + "^" + vaccineCode[CVX_LABEL]
            + "^CVX|999||||||||||||00^Parental decision^NIP002||RE|A\r");
        addComorbidityAndSerology(options, message, 1);
      } else {
        addDummyRXA(message);
        int pos = 0;
        if (random.nextDouble() < options.getComorbidityPercentage()) {
          pos++;
          addComorbidity(message, pos);
        }
        if (random.nextDouble() < options.getMissedPercentage()) {
          pos++;
          addMissedAppointment(message, pos);
        }
        if (random.nextDouble() < options.getSerologyPercentage() || pos == 0) {
          pos++;
          addSerology(message, pos);
        }

      }
    }

    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(message.toString());
    testCaseMessage.appendCustomTransformation("run procedure ANONYMIZE_AND_UPDATE_RECORD");
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    return testCaseMessage.getMessageText();
  }

  private static void addComorbidityAndSerology(Options options, StringBuffer message, int pos) {
    if (random.nextDouble() < options.getComorbidityPercentage()) {
      pos++;
      addComorbidity(message, pos);
    }
    if (random.nextDouble() < options.getSerologyPercentage()) {
      pos++;
      addSerology(message, pos);
    }
  }

  private static void addSerology(StringBuffer message, int pos) {
    message.append("OBX|" + pos
        + "|CE|75505-8^Disease with serological evidence of immunity^LN|3|840535000^Antibody to SARS-CoV-2^SCT||||||F|||20120814\r");
  }

  private static void addComorbidity(StringBuffer message, int pos) {
    String comorbidity = random.nextBoolean() ? "Y^Yes^HL70136" : "N^No^HL70136";
    message.append("OBX|" + pos
        + "|CE|covid_reporting_morbid_status^Covid-19 Reporting of Morbidity Status^CDCPHINVS|4|"
        + comorbidity + "||||||F|||20120814\r");
  }

  private static void addMissedAppointment(StringBuffer message, int pos) {
    String missedAppointment = random.nextBoolean() ? "Y^Yes^HL70136" : "N^No^HL70136";
    message.append("OBX|" + pos
        + "|CE|covid_reporting_recip_missed_appt^Covid-19 Reporting of Missed Appointment^CDCPHINVS|5|"
        + missedAppointment + "||||||F|||20120814\r");
  }

  private static void addDummyRXA(StringBuffer message) {
    message.append(
        "ORC|RE||9999^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|||||||NISTEHRFAC^NISTEHRFacility^HL70362|\r");
    message.append("RXA|0|1|20120814||998^No Vaccine Administered^CVX|999||||||||||||||NA|A\r");
  }

  private static String generateRace() {
    String race = "";
    {
      List<String[]> raceList = new ArrayList<>(Arrays.asList(raceCodes));
      for (int i = 0; i < 6; i++) {
        if (random.nextDouble() < 0.5 || raceList.size() == 0) {
          break;
        }
        if (i > 0) {
          race += "~";
        }
        int pos = random.nextInt(raceList.size());
        String[] r = raceList.get(pos);
        raceList.remove(pos);
        race += r[0] + "^" + r[1] + "^CDCREC";
      }
    }
    return race;
  }

  private static void printAdministeredVaccination(StringBuffer message, String[] vaccineCode,
      String vaccinationDate) {
    message.append(
        "ORC|RE||IZ-783274^NDA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^MD\r");
    message.append("RXA|0|1|" + vaccinationDate + "||");
    message.append(vaccineCode[CVX] + "^" + vaccineCode[CVX_LABEL] + "^CVX");
    if (vaccineCode[NDC] != null) {
      message.append("^" + vaccineCode[NDC] + "^" + vaccineCode[CVX_LABEL] + "^NDC");
    }
    String clinician = "";
    {
      String s[] = clinicans[random.nextInt(clinicans.length)];
      clinician =
          s[0] + "^" + s[1] + "^" + s[2] + "^" + s[3] + "^^^^^NIST-AA-1^^^^PRN^^^^^^^^" + s[4];
    }
    String informationSource = vaccineCode[NDC] == null ? "01^Historical record^NIP001"
        : "00^New immunization record^NIP001";
    message.append("|0.5|mL^MilliLiter [SI Volume Units]^UCUM||" + informationSource + "|"
        + clinician + "|^^^X68||||");
    if (vaccineCode[MVX] == null) {
      message.append("||");
    } else {
      String lotNumber = vaccineCode[MVX].substring(0, 1) + "9999-";
      SimpleDateFormat sdf = new SimpleDateFormat("dd");
      lotNumber += sdf.format(new Date());
      message.append(
          lotNumber + "|20130604|" + vaccineCode[MVX] + "^" + vaccineCode[MVX_LABEL] + "^MVX");
    }
    message.append("|||CP|A\r");
    String bodyRoute = generateBodyRoute();
    String bodySite = generateBodySite(bodyRoute);
    message.append("RXR|" + bodyRoute + "|" + bodySite + "\r");
    message.append(
        "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V05^VFC eligible - Federally Qualified Health Center Patient (under-insured)^HL70064||||||F|||20120814|||VXC40^Eligibility captured at the immunization level^CDCPHINVS\r");
    message.append(
        "OBX|2|CE|30956-7^vaccine type^LN|2|88^Influenza, unspecified formulation^CVX||||||F\r");
    message.append(
        "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F\r");
    message.append(
        "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120814||||||F\r");
  }

  private static String generateBodySite(String bodyRoute) {
    String bodySite = "";
    // Oral doesn't have a body site
    if (!bodyRoute.startsWith("C38288")) {
      String s[] = bodySites[random.nextInt(bodySites.length)];
      bodySite = s[0] + "^" + s[1] + "^HL70163";
    }
    return bodySite;
  }

  private static String generateBodyRoute() {
    String bodyRoute = "";
    {
      String s[] = bodyRoutes[random.nextInt(bodyRoutes.length)];
      bodyRoute = s[0] + "^" + s[1] + "^NCIT";
    }
    return bodyRoute;
  }

  public static class Options {
    private double administeredPercentage = 0.5;
    private double refusedPercentage = 0.5;
    private double comorbidityPercentage = 0.4;
    private double missedPercentage = 0.4;
    private double serologyPercentage = 0.4;

    public double getComorbidityPercentage() {
      return comorbidityPercentage;
    }

    public void setComorbidityPercentage(double comorbidityPercentage) {
      this.comorbidityPercentage = comorbidityPercentage;
    }

    public double getMissedPercentage() {
      return missedPercentage;
    }

    public void setMissedPercentage(double missedPercentage) {
      this.missedPercentage = missedPercentage;
    }

    public double getAdministeredPercentage() {
      return administeredPercentage;
    }

    public void setAdministeredPercentage(double administeredPercentage) {
      this.administeredPercentage = administeredPercentage;
    }

    public double getRefusedPercentage() {
      return refusedPercentage;
    }

    public void setRefusedPercentage(double refusedPercentage) {
      this.refusedPercentage = refusedPercentage;
    }

    public double getSerologyPercentage() {
      return serologyPercentage;
    }

    public void setSerologyPercentage(double serologyPercentage) {
      this.serologyPercentage = serologyPercentage;
    }

  }

}

