package org.immregistries.smm.tester.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class ConvertErrorCodesToTestDefinitions {

  private static final String FOLDER = "C:\\Users\\N. Bunker\\Downloads\\";
  private static final String FILE_CSV =
      FOLDER + "Compiled Error Codes - Working Copy - Test Cases.csv";

  private static final String FILE_SQL =
      FOLDER + "Compiled Error Codes - Working Copy - Test Cases.sql";

  public static void main(String[] args) throws Exception {

    File file = new File(FILE_CSV);
    BufferedReader in = new BufferedReader(new FileReader(file));
    CsvReader cvsReader = new CsvReader();
    List<String> valueList;
    while ((valueList = cvsReader.readValuesFromCsv(in)) != null) {
      if (valueList.size() >= 7 && !valueList.get(6).equals("")) {
        
      }
    }


  }
}
