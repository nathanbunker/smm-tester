package org.immunizationsoftware.dqa.tester.manager.nist;

import gov.nist.healthcare.hl7ws.client.MessageValidationV2SoapClient;

public class NISTValidator
{
  private static final String EVS_URL = "http://hit-testing2.nist.gov:8090/hl7v2ws/services/soap/MessageValidationV2";
  
  public static ValidationReport validate(String message, ValidationResource validationResource) {
    MessageValidationV2SoapClient soapClient = new MessageValidationV2SoapClient(EVS_URL);
    String result = soapClient.validate(message, validationResource.getOid(), "", "");
    ValidationReport validationReport = new ValidationReport(result);
    return validationReport;
  }
}
