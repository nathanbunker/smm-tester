package org.immunizationsoftware.dqa.transform.procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import org.immunizationsoftware.dqa.transform.TransformRequest;
import org.immunizationsoftware.dqa.transform.Transformer;

public class RemoveVaccinationGroupsProcedure implements ProcedureInterface
{
  // run procedure Remove_Vaccination_Groups where RXA-20 equals 'RE'
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList) throws IOException {
    String token = "";
    while (!token.equalsIgnoreCase("where") && !tokenList.isEmpty()) {
      token = tokenList.removeFirst();
    }
    if (token.equalsIgnoreCase("where") && !tokenList.isEmpty()) {
      String hl7Ref = tokenList.removeFirst();
      if (!tokenList.isEmpty()) {
        token = tokenList.removeFirst();
        if (token.equalsIgnoreCase("equals") && !tokenList.isEmpty()) {
          String valueSearch = tokenList.removeFirst();
          BufferedReader inResult = new BufferedReader(new StringReader(transformRequest.getResultText()));
          String lineResult;
          String finalMessage = "";
          String vaccinationGroup = "";
          while ((lineResult = inResult.readLine()) != null) {
            lineResult = lineResult.trim();
            if (lineResult.startsWith("ORC|")) {
              if (!vaccinationGroup.equals("")) {
                String valueActual = Transformer.getValueFromHL7(hl7Ref, vaccinationGroup);
                if (!valueSearch.equalsIgnoreCase(valueActual)) {
                  finalMessage += vaccinationGroup;
                }
              }
              vaccinationGroup = lineResult + "\r";
            } else if (!vaccinationGroup.equals("")) {
              vaccinationGroup += lineResult + "\r";
            } else {
              finalMessage += lineResult + "\r";
            }
          }
          if (!vaccinationGroup.equals("")) {
            String valueActual = Transformer.getValueFromHL7(hl7Ref, vaccinationGroup);
            if (!valueSearch.equalsIgnoreCase(valueActual)) {
              finalMessage += vaccinationGroup;
            }
          }
          transformRequest.setResultText(finalMessage);
        }
      }
    }

  }
}
