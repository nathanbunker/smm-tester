package org.immunizationsoftware.dqa.transform.procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import org.immunizationsoftware.dqa.transform.TransformRequest;
import org.immunizationsoftware.dqa.transform.Transformer;

public class AddFundingEligibilityToAllRxa implements ProcedureInterface
{
  private static final String OBX_TO_ADD = "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150817|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r";

  // run procedure ADD_FUNDING_ELGIBILITY_TO_ALL_RXA
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList) throws IOException {
    BufferedReader inResult = new BufferedReader(new StringReader(transformRequest.getResultText()));
    String lineResult;
    String finalMessage = "";
    boolean needToAddOBX = false;
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      if (needToAddOBX) {
        if (lineResult.startsWith("OBX|")) {
          if (lineResult.indexOf("|64994-7") > 0) {
            needToAddOBX = false;
          }
        } else if (lineResult.startsWith("ORC|") || lineResult.startsWith("RXA|")) {
          finalMessage += OBX_TO_ADD;
          needToAddOBX = false;
        }
      } else {
        if (lineResult.startsWith("RXA|")) {
          needToAddOBX = true;
        }
      }
      finalMessage += lineResult + "\r";
    }
    if (needToAddOBX) {
      finalMessage += OBX_TO_ADD;
    }

    transformRequest.setResultText(finalMessage);
  }
}
