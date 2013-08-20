package org.immunizationsoftware.dqa.tester.manager;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class HL7AnalyzerTester extends TestCase
{

  public void testAckAnalyzer() {
    List<String> issueList = new ArrayList<String>();
    HL7Reader reader = new HL7Reader(
        "MSH|^~\\&|MCIR|MCIR|||20130817125650-0600||ACK^V04|20130817125650-0600.1461|P|2.5.1|\r"
            + "SFT|OIS|1.06dev|DQA||\r" + "MSA|AA|A1.1|\r" + "ERR|||0|I|||||Message accepted with 1 vaccination|\r");
    HL7Reader originalRequestReader = new HL7Reader(
        "MSH|^~\\&|||||20130817125649||VXU^V04^VXU_V04|A1.1|P|2.5.1|\rPID|1||A1.1^^^OIS-TEST^MR||Cimarron^Evanthe^Maddie^^^^L|\r");
    HL7Analyzer ackAnalyzer = new HL7Analyzer(issueList, reader, originalRequestReader);
    ackAnalyzer.analyzeAck();
    for (String issue : issueList)
    {
      System.out.println(issue);
    }
    assertTrue(ackAnalyzer.isPassedTest());
  }
}
