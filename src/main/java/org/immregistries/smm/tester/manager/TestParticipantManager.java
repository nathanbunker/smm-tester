package org.immregistries.smm.tester.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class TestParticipantManager {
  public static TestParticipant readString(String response) throws IOException {
    BufferedReader in = new BufferedReader(new StringReader(response));
    String line = in.readLine();
    if (line != null) {
      List<String> valueList = CsvReader.readValuesFromCsv(line);

      int posOrganizationName = CsvReader.findPosition("Organization Name", valueList);
      int posFolderName = CsvReader.findPosition("Folder Name", valueList);
      int posRecordRequirementsStatus =
          CsvReader.findPosition("Record Requirements Status", valueList);
      int posConnecttoIISStatus = CsvReader.findPosition("Connect to IIS Status", valueList);
      int posGuideName = CsvReader.findPosition("Guide Name", valueList);
      int posTransport = CsvReader.findPosition("Transport", valueList);
      int posPublicIdCode = CsvReader.findPosition("Public Id Code", valueList);

      while ((line = in.readLine()) != null) {
        valueList = CsvReader.readValuesFromCsv(line);
        TestParticipant testParticipant = new TestParticipant();
        testParticipant.setOrganizationName(CsvReader.readValue(posOrganizationName, valueList));
        testParticipant.setFolderName(CsvReader.readValue(posFolderName, valueList));
        testParticipant.setRecordRequirementsStatus(
            CsvReader.readValue(posRecordRequirementsStatus, valueList));
        testParticipant
            .setConnecttoIISStatus(CsvReader.readValue(posConnecttoIISStatus, valueList));
        testParticipant.setProfileUsageId(CsvReader.readValue(posGuideName, valueList));
        testParticipant.setTransport(CsvReader.readValue(posTransport, valueList));
        testParticipant.setPublicIdCode(CsvReader.readValue(posPublicIdCode, valueList));
        return testParticipant;
      }
    }
    in.close();
    return null;
  }

}
