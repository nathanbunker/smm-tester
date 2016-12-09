package org.immregistries.smm.tester.manager;

import java.util.List;

import org.immregistries.smm.tester.manager.CvsReader;

import junit.framework.TestCase;

public class CvsReaderTester extends TestCase
{
  private static String TEST_LINE1 = "MSH-3,\"private static final String FIELD_MSH_3=\"\"MSH-3\"\";\",Sending Application,,,R_OR_X,MSH-3=R_OR_X,,,,,,,,";
  public void testmain() throws Exception {
    List<String> valueList = CvsReader.readValuesFromCsv(TEST_LINE1);
    assertEquals("MSH-3", CvsReader.readValue(0, valueList));
    assertEquals("private static final String FIELD_MSH_3=\"MSH-3\";", CvsReader.readValue(1, valueList));
    assertEquals("Sending Application", CvsReader.readValue(2, valueList));
    assertEquals("MSH-3=R_OR_X", CvsReader.readValue(6, valueList));
  }
}
