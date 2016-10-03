package org.immunizationsoftware.dqa.tester.manager;

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
    int posRedactListResponses = CvsReader.findPosition("Redact List Responses", valueList);
    int posRecordRequirementsStatus = CvsReader.findPosition("Record Requirements Status", valueList);
    int posConnecttoIISStatus = CvsReader.findPosition("Connect to IIS Status", valueList);
    int posGuideName = CvsReader.findPosition("Guide Name", valueList);
    int posTransport = CvsReader.findPosition("Transport", valueList);
    int posQuerySupport = CvsReader.findPosition("Query Support", valueList);
    int posPublicIdCode = CvsReader.findPosition("Public Id Code", valueList);
    int posTchForecastSoftwareId = CvsReader.findPosition("TCH Forecast Software Id", valueList);

    while ((line = in.readLine()) != null) {
      valueList = CvsReader.readValuesFromCsv(line);
      TestParticipant testParticipant = new TestParticipant();
      testParticipant.setOrganizationName(CvsReader.readValue(posOrganizationName, valueList));
      testParticipant.setFolderName(CvsReader.readValue(posFolderName, valueList));
      testParticipant.setRecordRequirementsStatus(CvsReader.readValue(posRecordRequirementsStatus, valueList));
      testParticipant.setConnecttoIISStatus(CvsReader.readValue(posConnecttoIISStatus, valueList));
      testParticipant.setProfileUsageId(CvsReader.readValue(posGuideName, valueList));
      testParticipant.setTransport(CvsReader.readValue(posTransport, valueList));
      testParticipant.setQuerySupport(CvsReader.readValue(posQuerySupport, valueList));
      testParticipant.setPublicIdCode(CvsReader.readValue(posPublicIdCode, valueList));
      testParticipant.setTchForecastSoftwareId(rn(CvsReader.readValue(posTchForecastSoftwareId, valueList)));
      testParticipant.setRedactListResponses(!CvsReader.readValue(posRedactListResponses, valueList).equalsIgnoreCase("No"));
      return testParticipant;
    }
    }
    in.close();
    return null;
  }

  private static String rn(String s)
  {
    if (s == null || s.equals("null"))
    {
      return "";
    }
    return s;
  }
}
