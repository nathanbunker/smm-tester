/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.connectors;

import java.util.List;

import _2011.iisb.cdc.Client_Service;
import _2011.iisb.cdc.Client_ServiceStub;
import _2011.iisb.cdc.ConnectivityTest;
import _2011.iisb.cdc.ConnectivityTestRequestType;
import _2011.iisb.cdc.ConnectivityTestResponse;
import _2011.iisb.cdc.SubmitSingleMessage;
import _2011.iisb.cdc.SubmitSingleMessageRequestType;
import _2011.iisb.cdc.SubmitSingleMessageResponse;

/**
 * 
 * @author nathan
 */
public class SoapConnector extends HttpConnector
{

  private Client_Service clientService = null;


  public SoapConnector(String label, String url) throws Exception {
    super(label, "SOAP");
    this.url = url;
    clientService = new Client_ServiceStub(this.url);
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception
  {
    SubmitSingleMessage submitSingleMessage = new SubmitSingleMessage();
    SubmitSingleMessageRequestType request = new SubmitSingleMessageRequestType();
    request.setFacilityID(this.facilityid);
    request.setHl7Message(message);
    request.setPassword(this.password);
    request.setUsername(this.userid);
    submitSingleMessage.setSubmitSingleMessage(request);
    SubmitSingleMessageResponse response = clientService.submitSingleMessage(submitSingleMessage);
    return response.getSubmitSingleMessageResponse().get_return();

  }

  @Override
  public String connectivityTest(String message) throws Exception
  {
    ConnectivityTestRequestType request = new ConnectivityTestRequestType();
    request.setEchoBack(message);
    ConnectivityTest connectivityTest = new ConnectivityTest();
    connectivityTest.setConnectivityTest(request);
    ConnectivityTestResponse response = clientService.connectivityTest(connectivityTest);
    return response.getConnectivityTestResponse().get_return();
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb)
  {
    // do nothing
  }

  @Override
  protected void setupFields(List<String> fields)
  {
    super.setupFields(fields);

  }

}
