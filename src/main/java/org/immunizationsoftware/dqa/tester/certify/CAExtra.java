package org.immunizationsoftware.dqa.tester.certify;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.transform.ScenarioManager;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.*;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;

public class CAExtra extends CertifyArea
{

  public CAExtra(CertifyRunner certifyRunner) {
    super("O", VALUE_TEST_SECTION_TYPE_EXTRA, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    int count = 0;
    register(++count, 1, createVaccineDedupTest1());
    register(++count, 1, createVaccineDedupTest2());
    register(++count, 1, createVaccineDedupTest3a());
    register(++count, 1, createVaccineDedupTest3b(updateList.get(updateList.size() - 1)));
    
    // gotta build these in reverse order to make sure data for the admin is of good quality.
    // The Admin message will be used to create the historical message
    // Then they are registered in the order for testing of Historical -> Admin
    TestCaseMessage base = createVaccineDedupTest4Base();
    certifyRunner.transformer.transform(base);
    register(++count, 1, createVaccineDedupTest4a(base));
    register(++count, 1, createVaccineDedupTest4b(base));
    
    register(++count, 1, createVaccineDedupTest5a());
    register(++count, 1, createVaccineDedupTest5b(updateList.get(updateList.size() - 1), -2));

    register(++count, 1, createVaccineDedupTest5a());
    register(++count, 1, createVaccineDedupTest5b(updateList.get(updateList.size() - 1), -4));

    register(++count, 1, createVaccineDedupTest5a());
    register(++count, 1, createVaccineDedupTest5b(updateList.get(updateList.size() - 1), -7));

    register(++count, 1, createVaccineDedupTest5a());
    register(++count, 1, createVaccineDedupTest5b(updateList.get(updateList.size() - 1), -10));

    register(++count, 1, createVaccineDedupTest5a());
    register(++count, 1, createVaccineDedupTest5b(updateList.get(updateList.size() - 1), -14));

    register(++count, 1, createVaccineDedupTest5a());
    register(++count, 1, createVaccineDedupTest5b(updateList.get(updateList.size() - 1), -21));

    register(++count, 1, createVaccineDedupTest5a());
    register(++count, 1, createVaccineDedupTest5b(updateList.get(updateList.size() - 1), -24));

    register(++count, 1, createVaccineDedupTest5a());
    register(++count, 1, createVaccineDedupTest5b(updateList.get(updateList.size() - 1), -28));
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
  
  // This test case contains 1 VXU with 2 immunizations.
  // The two immunizations are exact copies of each other in 
  // ORC, RXA, RXR, and 4 OBX segments.
  private TestCaseMessage createVaccineDedupTest1() {
    TestCaseMessage testCaseMessage = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    testCaseMessage.setDescription("Exact Match in Message");
    // It's long, but works.  Need to talk to Nathan to see if there is a better way
    String transforms = 
      "Insert segment ORC last\r" +
      "Insert segment RXA last\r" +
      "Insert segment RXR last\r" +
      "Insert segment OBX last\r" +
      "Insert segment OBX last\r" +
      "Insert segment OBX last\r" +
      "Insert segment OBX last\r" +
      "ORC-1*=RE\r" +
      "ORC-3*=[VAC3_ID]\r" +
      "ORC-3.2*=AIRA\r" +
      "ORC-10.1*=I-23432\r" +
      "ORC-10.2*=Burden\r" +
      "ORC-10.3*=Donna\r" +
      "ORC-10.4*=A\r" +
      "ORC-10.9*=NIST-AA-1\r" +
      "ORC-12.1*=57422\r" +
      "ORC-12.2*=RADON\r" +
      "ORC-12.3*=NICHOLAS\r" +
      "ORC-12.9*=NIST-AA-1\r" +
      "ORC-12.10*=L\r" +
      "RXA-1*=0\r" +
      "RXA-2*=1\r" +
      "RXA-3*=[VAC3_DATE]\r" +
      "RXA-9.1*=00\r" +
      "RXA-9.2*=Administered\r" +
      "RXA-9.3*=NIP001\r" +
      "RXA-5.1*=[VAC3_CVX]\r" +
      "RXA-5.2*=[VAC3_CVX_LABEL]\r" +
      "RXA-5.3*=CVX\r" +
      "RXA-6*=[VAC3_AMOUNT]\r" +
      "RXA-7.1*=mL\r" +
      "RXA-7.2*=milliliters\r" +
      "RXA-7.3*=UCUM\r" +
      "RXA-15*=[VAC3_LOT]\r" +
      "RXA-17.1*=[VAC3_MVX]\r" +
      "RXA-17.2*=[VAC3_MVX_LABEL]\r" +
      "RXA-17.3*=MVX\r" +
      "RXA-20*=CP\r" +
      "RXA-21*=A\r" +
      "RXA*:RXR-1.1=[VAC3_ROUTE]\r" +
      "RXA*:RXR-1.2=\r" +
      "RXA*:RXR-1.3=HL70162\r" +
      "RXA*:RXR-2.1=[VAC3_SITE]\r" +
      "RXA*:RXR-2.2=\r" +
      "RXA*:RXR-2.3=HL70163\r" +
      "RXA#1:OBX#1-1=1\r" +
      "RXA#1:OBX#1-2=CE\r" +
      "RXA#1:OBX#1-3.1=64994-7\r" +
      "RXA#1:OBX#1-3.2=Vaccine funding program eligibility category\r" +
      "RXA#1:OBX#1-3.3=LN\r" +
      "RXA#1:OBX#1-4=1\r" +
      "RXA#1:OBX#1-5.1=[VFC]\r" +
      "RXA#1:OBX#1-5.2=[VFC_LABEL]\r" +
      "RXA#1:OBX#1-5.3=HL70064\r" +
      "RXA#1:OBX#1-11=F\r" +
      "RXA#1:OBX#1-14=[TODAY]\r" +
      "RXA#1:OBX#1-17.1=VXC40\r" +
      "RXA#1:OBX#1-17.2=Eligibility captured at the immunization level\r" +
      "RXA#1:OBX#1-17.3=CDCPHINVS\r" +
      "RXA#1:OBX#2-1=2\r" +
      "RXA#1:OBX#2-2=CE\r" +
      "RXA#1:OBX#2-3.1=30956-7\r" +
      "RXA#1:OBX#2-3.2=Vaccine Type\r" +
      "RXA#1:OBX#2-3.3=LN\r" +
      "RXA#1:OBX#2-4=2\r" +
      "RXA#1:OBX#2-5.1=[VAC3_VIS_PUB_CODE]\r" +
      "RXA#1:OBX#2-5.2=[VAC3_VIS_PUB_NAME]\r" +
      "RXA#1:OBX#2-5.3=CVX\r" +
      "RXA#1:OBX#2-11=F\r" +
      "RXA#1:OBX#3-1=3\r" +
      "RXA#1:OBX#3-2=TS\r" +
      "RXA#1:OBX#3-3.1=29768-9\r" +
      "RXA#1:OBX#3-3.2=Date vaccine information statement published\r" +
      "RXA#1:OBX#3-3.3=LN\r" +
      "RXA#1:OBX#3-4=2\r" +
      "RXA#1:OBX#3-5.1=[VAC3_VIS_PUB_DATE]\r" +
      "RXA#1:OBX#3-11=F\r" +
      "RXA#1:OBX#4-1=4\r" +
      "RXA#1:OBX#4-2=TS\r" +
      "RXA#1:OBX#4-3.1=29769-7\r" +
      "RXA#1:OBX#4-3.2=Date vaccine information statement presented\r" +
      "RXA#1:OBX#4-3.3=LN\r" +
      "RXA#1:OBX#4-4=2\r" +
      "RXA#1:OBX#4-5.1=[VAC3_DATE]\r" +
      "RXA#1:OBX#4-11=F\r" +
      "RXA#2:OBX#1-1=1\r" +
      "RXA#2:OBX#1-2=CE\r" +
      "RXA#2:OBX#1-3.1=64994-7\r" +
      "RXA#2:OBX#1-3.2=Vaccine funding program eligibility category\r" +
      "RXA#2:OBX#1-3.3=LN\r" +
      "RXA#2:OBX#1-4=5\r" +
      "RXA#2:OBX#1-5.1=[VFC]\r" +
      "RXA#2:OBX#1-5.2=[VFC_LABEL]\r" +
      "RXA#2:OBX#1-5.3=HL70064\r" +
      "RXA#2:OBX#1-11=F\r" +
      "RXA#2:OBX#1-14=[TODAY]\r" +
      "RXA#2:OBX#1-17.1=VXC40\r" +
      "RXA#2:OBX#1-17.2=Eligibility captured at the immunization level\r" +
      "RXA#2:OBX#1-17.3=CDCPHINVS\r" +
      "RXA#2:OBX#2-1=6\r" +
      "RXA#2:OBX#2-2=CE\r" +
      "RXA#2:OBX#2-3.1=30956-7\r" +
      "RXA#2:OBX#2-3.2=Vaccine Type\r" +
      "RXA#2:OBX#2-3.3=LN\r" +
      "RXA#2:OBX#2-4=2\r" +
      "RXA#2:OBX#2-5.1=[VAC3_VIS_PUB_CODE]\r" +
      "RXA#2:OBX#2-5.2=[VAC3_VIS_PUB_NAME]\r" +
      "RXA#2:OBX#2-5.3=CVX\r" +
      "RXA#2:OBX#2-11=F\r" +
      "RXA#2:OBX#3-1=7\r" +
      "RXA#2:OBX#3-2=TS\r" +
      "RXA#2:OBX#3-3.1=29768-9\r" +
      "RXA#2:OBX#3-3.2=Date vaccine information statement published\r" +
      "RXA#2:OBX#3-3.3=LN\r" +
      "RXA#2:OBX#3-4=2\r" +
      "RXA#2:OBX#3-5.1=[VAC3_VIS_PUB_DATE]\r" +
      "RXA#2:OBX#3-11=F\r" +
      "RXA#2:OBX#4-1=8\r" +
      "RXA#2:OBX#4-2=TS\r" +
      "RXA#2:OBX#4-3.1=29769-7\r" +
      "RXA#2:OBX#4-3.2=Date vaccine information statement presented\r" +
      "RXA#2:OBX#4-3.3=LN\r" +
      "RXA#2:OBX#4-4=2\r" +
      "RXA#2:OBX#4-5.1=[VAC3_DATE]\r" +
      "RXA#2:OBX#4-11=F";
      
    testCaseMessage.appendCustomTransformation(transforms);
    return testCaseMessage;
  }
  
  // This test case contains 1 VXU with 2 immunizations.
  // The two immunizations are exact copies of each other in 
  // ORC, RXA, RXR, and 4 OBX segments with the following exceptions
  // ORC-3 is different
  // RXA-20 on the second is Partial Admin (PA)
  private TestCaseMessage createVaccineDedupTest2() {
    TestCaseMessage tcm = createVaccineDedupTest1();
    tcm.setDescription("Identical, but one complete and one Partial Administration");
    tcm.appendCustomTransformation("ORC#2-3=[VAC2_ID]");
    tcm.appendCustomTransformation("RXA#2-20=PA");
    return tcm;
  }
  
  // This test case contains 2 VXU's each with 1 immunization
  // The two immunizations are identical in every way, just duplicate submissions
  // The two messages will have 2 different MSH-10 (Message IDs)
  // This is the first message.
  private TestCaseMessage createVaccineDedupTest3a() {
    TestCaseMessage tcm = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    tcm.setDescription("Exact Match in two separate Messages #1");
    return tcm;
  }
  // This test case contains 2 VXU's each with 1 immunization
  // The two immunizations are identical in every way, just duplicate submissions
  // The two messages will have 2 different MSH-10 (Message IDs)
  // This is the second message.
  private TestCaseMessage createVaccineDedupTest3b(TestCaseMessage tcm) {
      TestCaseMessage tcm2 = new TestCaseMessage();
      tcm2.setOriginalMessage(tcm.getMessageText());
      tcm2.setDescription("Exact Match in two separate Messages #2");
      String uniqueId = tcm.getTestCaseNumber();
      tcm2.appendCustomTransformation(
          "MSH-10=" + uniqueId + "." + Transformer.makeBase62Number(System.currentTimeMillis() % 10000));
    return tcm2;
  }
  
  // This test case contains 2 VXU's each with 1 immunization
  // The two immunizations are identical, but one is a historical report and one an administered report
  // This is the base message (admin) which will be used to ensure consistent data across to submitted messages
  private TestCaseMessage createVaccineDedupTest4Base() {
    TestCaseMessage base = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_TWO_YEARS_OLD);
    base.setDescription("Dedup Test Case Base - Should Not be Registered");
    return base;
  }
  // This historical will be built from data in the passed in administered test case
  private TestCaseMessage createVaccineDedupTest4a(TestCaseMessage base) {
    TestCaseMessage hist = new TestCaseMessage();
    hist.setOriginalMessage(base.getMessageText());
    hist.setDescription("Identical, but one Historical VXU followed by Admin VXU #1");

    hist.appendCustomTransformation("remove segment PD1");
    hist.appendCustomTransformation("remove segment NK1");
    hist.appendCustomTransformation("remove segment RXR");
    hist.appendCustomTransformation("remove segment OBX#4");
    hist.appendCustomTransformation("remove segment OBX#3");
    hist.appendCustomTransformation("remove segment OBX#2");
    hist.appendCustomTransformation("remove segment OBX#1");
    hist.appendCustomTransformation("clear ORC");
    hist.appendCustomTransformation("ORC-1=RE");
    hist.appendCustomTransformation("ORC-3=[VAC2_ID]");
    hist.appendCustomTransformation("RXA-6=999");
    hist.appendCustomTransformation("clear RXA-7");
    hist.appendCustomTransformation("RXA-9.1=01");
    hist.appendCustomTransformation("RXA-9.2=Historical");
    hist.appendCustomTransformation("clear RXA-15");
    hist.appendCustomTransformation("clear RXA-17");
    return hist;
  }
  // This is the administered message
  private TestCaseMessage createVaccineDedupTest4b(TestCaseMessage base) {
    TestCaseMessage admin = new TestCaseMessage();
    admin.setOriginalMessage(base.getMessageText());
    admin.setDescription("Identical, but one Historical VXU followed by Admin VXU #2");
    return admin;
  }

  // This test case contains 2 VXU's each with 1 immunization
  // This first is and administered immunization created at random
  // The second is the same immunization with the same ORC-3, but 10 days earlier with a U action code
  private TestCaseMessage createVaccineDedupTest5a() {
    TestCaseMessage tcm = ScenarioManager.createTestCaseMessage(SCENARIO_1_R_ADMIN_CHILD);
    tcm.setDescription("Date change due to documentation error in 2 VXU's #1");
    return tcm;
  }

  private TestCaseMessage createVaccineDedupTest5b(TestCaseMessage tcm, int days) {
    TestCaseMessage tcm2 = new TestCaseMessage();
    tcm2.setOriginalMessage(tcm.getMessageText());
    tcm2.setDescription("Date change due to documentation error in 2 VXU's #2");
    HL7Reader vxuReader = new HL7Reader(tcm.getMessageText());
    vxuReader.advanceToSegment("RXA");
    String strDate = vxuReader.getValue(3);
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    Date adminDate = null;
    try {
      adminDate = fmt.parse(strDate);
    }
    catch (ParseException pe) {
      adminDate = new Date();
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(adminDate);
    cal.add(Calendar.DATE, days);
    tcm2.appendCustomTransformation("RXA-3=" + fmt.format(cal.getTime()));
    tcm2.appendCustomTransformation("RXA-21=U");
    
    return tcm2;
  }
  
}
