/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.connectors;

import java.util.List;

import org.immunizationsoftware.dqa.tester.connectors.co.Client_ServiceStub;

import _2011.iisb.cdc.InterOp_ServiceStub;
import _2011.iisb.cdc.SubmitSingleMessage;
import _2011.iisb.cdc.SubmitSingleMessageResponse;

/**
 * 
 * @author nathan
 */
public class FLSoapConnector extends HttpConnector
{

  private InterOp_ServiceStub clientService = null;

  public FLSoapConnector(String label, String url) throws Exception {
    super(label, url, ConnectorFactory.TYPE_FL_SOAP);
    clientService = new InterOp_ServiceStub(this.url);
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    
    SubmitSingleMessage submitSingleMessage = new SubmitSingleMessage();
    submitSingleMessage.setFacilityID(this.facilityid);
    submitSingleMessage.setPassword(this.password);
    submitSingleMessage.setUsername(this.userid);
    submitSingleMessage.setHl7Message(message.replaceAll("\\r", "\n"));
    SubmitSingleMessageResponse response  = clientService.submitSingleMessage(submitSingleMessage);
    return response.get_return();
    
//    Client_ServiceStub.SubmitSingleMessage submitSingleMessage = new Client_ServiceStub.SubmitSingleMessage();
//    Client_ServiceStub.SubmitSingleMessageRequestType request = new Client_ServiceStub.SubmitSingleMessageRequestType();
//    request.setFacilityID(this.facilityid);
//    request.setHl7Message(message.replaceAll("\\r", "\n"));
//    request.setPassword(this.password);
//    request.setUsername(this.userid);
//    submitSingleMessage.setSubmitSingleMessage(request);
//    Client_ServiceStub.SubmitSingleMessageResponse response = clientService.submitSingleMessage(submitSingleMessage);
//
//    return response.getSubmitSingleMessageResponse().get_return();
    
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    return "Unable to run connectivity test. FL Connectivity requires username and password and so is not compatible with CDC WSDL. ";
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {
    // do nothing
  }

  @Override
  protected void setupFields(List<String> fields) {
    super.setupFields(fields);

  }

}
