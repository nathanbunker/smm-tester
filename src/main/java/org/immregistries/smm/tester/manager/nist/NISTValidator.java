package org.immregistries.smm.tester.manager.nist;

import org.immregistries.smm.SoftwareVersion;

import gov.nist.healthcare.hl7ws.client.MessageValidationV2SoapClient;

public class NISTValidator {
  private static MessageValidationV2SoapClient soapClient = null;

  private static synchronized MessageValidationV2SoapClient getSoapClient() {
    if (soapClient == null) {
      soapClient = new MessageValidationV2SoapClient(SoftwareVersion.EVS_URL);
    }
    return soapClient;
  }

  public static ValidationReport validate(String message, ValidationResource validationResource) {
    MessageValidationV2SoapClient soapClient = getSoapClient();
    synchronized (soapClient) {
      String result = soapClient.validate(message, validationResource.getOid(), "", "");
      ValidationReport validationReport = new ValidationReport(result);
      return validationReport;
    }
  }
}
