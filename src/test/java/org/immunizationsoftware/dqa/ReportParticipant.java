package org.immunizationsoftware.dqa;

import java.util.List;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.tester.CertifyRunner;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponse;
import org.immunizationsoftware.dqa.tester.manager.ParticipantResponseManager;
import org.openimmunizationsoftware.dqa.tr.RecordServletInterface;

public class ReportParticipant
{

  public static void main(String[] args) throws Exception {
    ManagerServlet managerServlet = new ManagerServlet();
    
    int maxCols = RecordServletInterface.MAP_COLS_MAX;
    int maxRows = RecordServletInterface.MAP_ROWS_MAX;
    ParticipantResponse[][] participantResponseMap = new ParticipantResponse[maxCols][maxRows];
    List<ParticipantResponse> participantResponseList = ParticipantResponseManager.readFile(participantResponseMap);
    for (ParticipantResponse participantResponse : participantResponseList) {
      CertifyRunner.reportParticipant(participantResponse);
    }
    System.out.println("Reported");
  }
}
