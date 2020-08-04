package org.immregistries.smm.transform.procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AddFundingSourceToAllRxa implements ProcedureInterface {
  private static final String OBX_TO_ADD =
      "OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|VXC50^Public^CDCPHINVS||||||F|||20150624";

  public void setTransformer(Transformer transformer) {
    // not needed
  }


  // run procedure ADD_FUNDING_SOURCE_TO_ALL_RXA
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    BufferedReader inResult =
        new BufferedReader(new StringReader(transformRequest.getResultText()));
    String lineResult;
    String finalMessage = "";
    boolean needToAddOBX = false;
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      if (needToAddOBX) {
        if (lineResult.startsWith("OBX|")) {
          if (lineResult.indexOf("|30963-3") > 0) {
            needToAddOBX = false;
          }
        } else if (lineResult.startsWith("ORC|") || lineResult.startsWith("RXA|")) {
          finalMessage += OBX_TO_ADD + transformRequest.getSegmentSeparator();
          needToAddOBX = false;
        }
      } else {
        if (lineResult.startsWith("RXA|")) {
          needToAddOBX = true;
        }
      }
      finalMessage += lineResult + transformRequest.getSegmentSeparator();
    }
    if (needToAddOBX) {
      finalMessage += OBX_TO_ADD + transformRequest.getSegmentSeparator();
    }

    transformRequest.setResultText(finalMessage);
  }
}
