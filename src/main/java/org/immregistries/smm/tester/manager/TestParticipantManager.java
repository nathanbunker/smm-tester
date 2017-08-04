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
      List<String> valueList = CvsReader.readValuesFromCsv(line);

      int posOrganizationName = CvsReader.findPosition("Organization Name", valueList);
      int posFolderName = CvsReader.findPosition("Folder Name", valueList);
      int posRecordRequirementsStatus =
          CvsReader.findPosition("Record Requirements Status", valueList);
      int posConnecttoIISStatus = CvsReader.findPosition("Connect to IIS Status", valueList);
      int posGuideName = CvsReader.findPosition("Guide Name", valueList);
      int posTransport = CvsReader.findPosition("Transport", valueList);
      int posPublicIdCode = CvsReader.findPosition("Public Id Code", valueList);

      while ((line = in.readLine()) != null) {
        valueList = CvsReader.readValuesFromCsv(line);
        TestParticipant testParticipant = new TestParticipant();
        testParticipant.setOrganizationName(CvsReader.readValue(posOrganizationName, valueList));
        testParticipant.setFolderName(CvsReader.readValue(posFolderName, valueList));
        testParticipant.setRecordRequirementsStatus(
            CvsReader.readValue(posRecordRequirementsStatus, valueList));
        testParticipant
            .setConnecttoIISStatus(CvsReader.readValue(posConnecttoIISStatus, valueList));
        testParticipant.setProfileUsageId(CvsReader.readValue(posGuideName, valueList));
        testParticipant.setTransport(CvsReader.readValue(posTransport, valueList));
        testParticipant.setPublicIdCode(CvsReader.readValue(posPublicIdCode, valueList));
        return testParticipant;
      }
    }
    in.close();
    return null;
  }

}
