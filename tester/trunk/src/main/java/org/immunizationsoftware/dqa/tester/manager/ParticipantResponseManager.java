package org.immunizationsoftware.dqa.tester.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.mover.ManagerServlet;

public class ParticipantResponseManager {
  public static List<ParticipantResponse> readFile(ParticipantResponse[][] participantResponseMap) throws IOException {
    List<ParticipantResponse> participantResponseList = new ArrayList<ParticipantResponse>();
    BufferedReader in = new BufferedReader(new FileReader(ManagerServlet.getIisParticipantResponsesAndAccountInfoFile()));
    String line = in.readLine();
    List<String> valueList = CvsReader.readValuesFromCsv(line);

    int posOrganizationName = CvsReader.findPosition("Organization Name", valueList);
    int posFolderName = CvsReader.findPosition("Folder Name", valueList);
    int posMap = CvsReader.findPosition("Map", valueList);
    int posPlatform = CvsReader.findPosition("Platform", valueList);
    int posVendor = CvsReader.findPosition("Vendor", valueList);
    int posRedactListResponses = CvsReader.findPosition("Redact List Responses", valueList);
    int posInternalComments = CvsReader.findPosition("Internal Comments", valueList);
    int posPhaseIParticipation = CvsReader.findPosition("Phase I Participation", valueList);
    int posPhase1Status = CvsReader.findPosition("Phase 1 Status", valueList);
    int posPhase1Comments = CvsReader.findPosition("Phase 1 Comments", valueList);
    int posPhaseIIParticipation = CvsReader.findPosition("Phase II Participation", valueList);
    int posPhaseIIStatus = CvsReader.findPosition("Phase II Status", valueList);
    int posPhaseIIComments = CvsReader.findPosition("Phase II Comments", valueList);
    int posIHS = CvsReader.findPosition("IHS", valueList);
    int posRecordRequirementsStatus = CvsReader.findPosition("Record Requirements Status", valueList);
    int posConnecttoIISStatus = CvsReader.findPosition("Connect to IIS Status", valueList);
    int posComments = CvsReader.findPosition("Comments", valueList);
    int posGuideName = CvsReader.findPosition("Guide Name", valueList);
    int posTransport = CvsReader.findPosition("Transport", valueList);
    int posQuerySupport = CvsReader.findPosition("Query Support", valueList);
    int posNistStatus = CvsReader.findPosition("NIST Status", valueList);
    int posAccessPasscode = CvsReader.findPosition("Access Passcode", valueList);
    int posPublicIdCode = CvsReader.findPosition("Public Id Code", valueList);

    while ((line = in.readLine()) != null) {
      valueList = CvsReader.readValuesFromCsv(line);
      ParticipantResponse participantResponse = new ParticipantResponse();
      participantResponseList.add(participantResponse);
      participantResponse.setOrganizationName(CvsReader.readValue(posOrganizationName, valueList));
      participantResponse.setFolderName(CvsReader.readValue(posFolderName, valueList));
      participantResponse.setMap(CvsReader.readValue(posMap, valueList));
      participantResponse.setPlatform(CvsReader.readValue(posPlatform, valueList));
      participantResponse.setVendor(CvsReader.readValue(posVendor, valueList));
      participantResponse.setInternalComments(CvsReader.readValue(posInternalComments, valueList));
      participantResponse.setPhaseIParticipation(CvsReader.readValue(posPhaseIParticipation, valueList));
      participantResponse.setPhase1Status(CvsReader.readValue(posPhase1Status, valueList));
      participantResponse.setPhase1Comments(CvsReader.readValue(posPhase1Comments, valueList));
      participantResponse.setPhaseIIParticipation(CvsReader.readValue(posPhaseIIParticipation, valueList));
      participantResponse.setPhaseIIStatus(CvsReader.readValue(posPhaseIIStatus, valueList));
      participantResponse.setPhaseIIComments(CvsReader.readValue(posPhaseIIComments, valueList));
      participantResponse.setIHS(CvsReader.readValue(posIHS, valueList));
      participantResponse.setRecordRequirementsStatus(CvsReader.readValue(posRecordRequirementsStatus, valueList));
      participantResponse.setConnecttoIISStatus(CvsReader.readValue(posConnecttoIISStatus, valueList));
      participantResponse.setComments(CvsReader.readValue(posComments, valueList));
      participantResponse.setGuideName(CvsReader.readValue(posGuideName, valueList));
      participantResponse.setTransport(CvsReader.readValue(posTransport, valueList));
      participantResponse.setQuerySupport(CvsReader.readValue(posQuerySupport, valueList));
      participantResponse.setNistStatus(CvsReader.readValue(posNistStatus, valueList));
      participantResponse.setAccessPasscode(CvsReader.readValue(posAccessPasscode, valueList));
      participantResponse.setPublicIdCode(CvsReader.readValue(posPublicIdCode, valueList));
      participantResponse.setRedactListResponses(!CvsReader.readValue(posRedactListResponses, valueList).equalsIgnoreCase("No"));
      if (participantResponseMap != null) {
        String mapPos = participantResponse.getMap();
        if (!mapPos.equals("")) {
          int posCol = 0;
          int posRow = 0;
          int commaPos = mapPos.indexOf(",");
          if (commaPos > 0) {
            try {
              posCol = Integer.parseInt(mapPos.substring(0, commaPos).trim());
              posRow = Integer.parseInt(mapPos.substring(commaPos + 1).trim());
            } catch (NumberFormatException nfe) {
              // ignore, can't read so can't set
            }
          }
          if (posCol > 0 && posRow > 0) {
            posCol--;
            posRow--;
            if (participantResponseMap.length > posCol && participantResponseMap[posCol].length > posRow) {
              participantResponseMap[posCol][posRow] = participantResponse;
              participantResponse.setCol(posCol);
              participantResponse.setRow(posRow);
            }
          }
        }
      }
    }
    in.close();
    return participantResponseList;
  }

}
