package org.immunizationsoftware.dqa.tester.manager.hl7;

import java.util.List;

import junit.framework.TestCase;

import org.immunizationsoftware.dqa.tester.manager.hl7.messages.VXU;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.MSH;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PID;

public class ConformanceTest extends TestCase
{
  private static final String MSH_SEGMENT = "MSH|^~\\&|||||20130823111809||VXU^V04^VXU_V04|X94P18|P|2.5.1|";
  private static final String MSH_SEGMENT_BAD_DATE = "MSH|^~\\&|||||201308231118AA||VXU^V04^VXU_V04|X94P18|P|2.5.1|";
  private static final String PID_SEGMENT = "PID|1||X94P18^^^OIS-TEST^MR||Court^Nye^^^^^L|Brazos^Maia|20130224|M|||321 Dundy St^^Haslett^MI^48840^USA^P||^PRN^PH^^^517^5489090|\r";
  private static final String VXU_MESSAGE = "MSH|^~\\&|||||20130823111809||VXU^V04^VXU_V04|X94P18|P|2.5.1|\r"
      + "PID|1||X94P18^^^OIS-TEST^MR||Court^Nye^^^^^L|Brazos^Maia|20130224|M|||321 Dundy St^^Haslett^MI^48840^USA^P||^PRN^PH^^^517^5489090|\r"
      + "NK1|1|Court^Maia|MTH^Mother^HL70063|\r";

  public void testConformance() {
    PID pid = new PID(UsageType.R, Cardinality.ONE_TIME_ONLY);
    pid.parseTextFromSegment(PID_SEGMENT);

    {
      MSH msh = new MSH(UsageType.R, Cardinality.ONE_TIME_ONLY);
      msh.parseTextFromSegment(MSH_SEGMENT);
      assertEquals("20130823111809", msh.getDateTimeOfMessage().getRawTextReceived());

      System.out.println(msh.createSegment());
      List<ConformanceIssue> conformanceIssueList = msh.checkConformance();
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
      System.out.println("Conformance of MSH");
      // msh.printValues(System.out);
    }
    {

      System.out.println("Testing VXU");
      VXU vxu = new VXU();
      vxu.parseTextFromMessage(VXU_MESSAGE);

      System.out.println(vxu.createMessage());
      List<ConformanceIssue> conformanceIssueList = vxu.checkConformance();
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
      System.out.println("Conformance of VXU");
    }

    {
      
      MSH msh = new MSH(UsageType.R, Cardinality.ONE_TIME_ONLY);
      msh.parseTextFromSegment(MSH_SEGMENT_BAD_DATE);
      assertEquals("201308231118AA", msh.getDateTimeOfMessage().getRawTextReceived());

      System.out.println(msh.createSegment());
      List<ConformanceIssue> conformanceIssueList = msh.checkConformance();
      assertEquals("", msh.getDateTimeOfMessage().getTime().getValue());
      for (ConformanceIssue conformanceIssue : conformanceIssueList) {
        System.out.println(conformanceIssue.createSegment());
      }
      System.out.println("Conformance of MSH");
      // msh.printValues(System.out);
    }
  }

}
