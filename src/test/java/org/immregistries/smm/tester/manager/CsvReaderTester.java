package org.immregistries.smm.tester.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import org.junit.Test;
import junit.framework.TestCase;

public class CsvReaderTester extends TestCase {
  private static String TEST_LINE1 =
      "MSH-3,\"private static final String FIELD_MSH_3=\"\"MSH-3\"\";\",Sending Application,,,R_OR_X,MSH-3=R_OR_X,,,,,,,,";

  public void testmain() throws Exception {
    List<String> valueList = CsvReader.readValuesFromCsv(TEST_LINE1);
    assertEquals("MSH-3", CsvReader.readValue(0, valueList));
    assertEquals("private static final String FIELD_MSH_3=\"MSH-3\";",
        CsvReader.readValue(1, valueList));
    assertEquals("Sending Application", CsvReader.readValue(2, valueList));
    assertEquals("MSH-3=R_OR_X", CsvReader.readValue(6, valueList));
  }

  private static final String FILE_LOCATION =
      "C:\\Users\\N. Bunker\\Downloads\\Compiled Error Codes - Working Copy - Test Cases.csv";

  @Test
  public void testReadCvsMultiLine() throws Exception {
    File file = new File(FILE_LOCATION);
    BufferedReader in = new BufferedReader(new FileReader(file));
    CsvReader cvsReader = new CsvReader();
    List<String> valueList;
    while ((valueList = cvsReader.readValuesFromCsv(in)) != null) {
      if (valueList.size() >= 7 && !valueList.get(6).equals("")) {
        System.out.println("=============================== " + valueList.get(0));
        System.out.println(valueList.get(6));
      }
    }
  }


}
