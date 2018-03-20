package org.immregistries.smm.tester.manager.response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.tester.manager.query.QueryResponse;
import org.immregistries.smm.tester.manager.query.QueryResponseType;
import org.immregistries.smm.tester.manager.query.Vaccination;

public class ResponseReader {
  public static ImmunizationMessage readMessage(String message) {
    if (message == null || message.equals("")) {
      return null;
    }
    HL7Reader reader = new HL7Reader(message);
    if (reader.advanceToSegment("MSH")) {
      String messageType = reader.getValue(9);
      if (messageType.equals("ACK")) {
        // Process ack
      } else if (messageType.equals("QBP")) {
        // Process
      } else if (messageType.equals("RSP")) {
        QueryResponse queryResponse = new QueryResponse();

        String profile = reader.getValue(21);
        QueryResponseType queryResponseType = null;
        if (profile.equalsIgnoreCase("")) {
          queryResponseType = QueryResponseType.PROFILE_ID_MISSING;
        } else if (profile.equalsIgnoreCase("Z32") || profile.equalsIgnoreCase("Z42")) {
          queryResponseType = QueryResponseType.MATCH;
          
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
          List<Vaccination> vaccinationList = new ArrayList<Vaccination>();
          queryResponse.setVaccinationList(vaccinationList);
          while (reader.advanceToSegment("RXA"))
          {
            String adminDateString = reader.getValue(3);
            if (adminDateString.length() > 8){
              adminDateString = adminDateString.substring(0, 8);
            }
            String vaccineCvx = reader.getValue(5);
            String vaccineLabel = reader.getValue(5,2);
            String actionCode = reader.getValue(21);
            
            Vaccination vaccination = new Vaccination();
            try {
              vaccination.setAdministrationDate(sdf.parse(adminDateString));
            } catch (ParseException e) {
              // do nothing
            }
            vaccination.setVaccineCvx(vaccineCvx);
            vaccination.setVaccineLabel(vaccineLabel);
            vaccinationList.add(vaccination);
          }
        } else if (profile.equalsIgnoreCase("Z31")) {
          queryResponseType = QueryResponseType.LIST;
        } else if (profile.equalsIgnoreCase("Z33")) {
          if (reader.advanceToSegment("QAK")) {
            String responseStatus = reader.getValue(2);
            if (responseStatus.equals("NF")) {
              queryResponseType = QueryResponseType.NOT_FOUND;
            } else if (responseStatus.equals("TM")) {
              queryResponseType = QueryResponseType.TOO_MANY;
            } else if (responseStatus.equals("AE")) {
              queryResponseType = QueryResponseType.ERROR;
            }
          } else {
            queryResponseType = null;
          }
        }
        queryResponse.setQueryResponseType(queryResponseType);

        return queryResponse;
        // Process
      } else if (messageType.equals("ADT")) {
        // Process
      } else if (messageType.equals("VXU")) {
        // Process
      }

    }
    return null;
  }
}
