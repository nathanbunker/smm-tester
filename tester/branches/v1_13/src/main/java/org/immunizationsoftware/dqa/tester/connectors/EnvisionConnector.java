/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.connectors;

import java.io.StringReader;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.axis2.context.ConfigurationContext;
import org.immunizationsoftware.dqa.tester.connectors.tlep.Client_Service;
import org.immunizationsoftware.dqa.tester.connectors.tlep.Client_ServiceStub;
import org.immunizationsoftware.dqa.tester.connectors.tlep.ConnectivityTest;
import org.immunizationsoftware.dqa.tester.connectors.tlep.ConnectivityTestRequestType;
import org.immunizationsoftware.dqa.tester.connectors.tlep.ConnectivityTestResponse;
import org.immunizationsoftware.dqa.tester.connectors.tlep.SubmitSingleMessage;
import org.immunizationsoftware.dqa.tester.connectors.tlep.SubmitSingleMessageRequestType;
import org.immunizationsoftware.dqa.tester.connectors.tlep.SubmitSingleMessageResponse;
import org.tempuri.HL7WSStub;
import org.tempuri.IR_Request;
import org.tempuri.IR_Response;


/**
 * 
 * @author nathan
 */
public class EnvisionConnector extends HttpConnector
{

  private HL7WSStub hl7Ws = null;

  public EnvisionConnector(String label, String url) throws Exception {
    super(label, url, ConnectorFactory.TYPE_ENVISION_SOAP);
   
    hl7Ws = new HL7WSStub(this.url);
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception
  {

    IR_Request ir = new IR_Request();
    ir.setPASSWORD(this.password);
    ir.setFACILITYID(this.facilityid);
    
    DataHandler dataHandler = new DataHandler(message, "text/plain");
    ir.setMESSAGEDATA(dataHandler);
    IR_Response respose = hl7Ws.processHL7Message(ir);
    
    return respose.getRequest_Result();

  }

  @Override
  public String connectivityTest(String message) throws Exception
  {
    return "Connectivity test not supported by Envision SOAP protocol";
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
