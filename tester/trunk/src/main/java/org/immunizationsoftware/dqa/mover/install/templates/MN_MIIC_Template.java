package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.SoapConnector;

public class MN_MIIC_Template extends ConnectionTemplate
{
  public MN_MIIC_Template() {
    super("MN MIIC");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setType(ConnectorFactory.TYPE_SOAP);
    cc.setInstructions("Contact MN MIIC for connecting information.");
    if (templateName.endsWith(_PROD)) {
      cc.setUrl("");
    } else {
      cc.setUrl("https://miic.health.state.mn.us/miic-ws-test/client_Service?wsdl");
    }
    cc.setTypeShow(false);
    cc.setUseridLabel("User Name");
    cc.setUseridRequired(true);
    cc.setFacilityidShow(true);
    cc.setOtheridLabel("MSH-4");
    cc.setPasswordLabel("Password");
    cc.setPasswordRequired(true);
    cc.setOtheridShow(true);
    cc.setOtheridRequired(true);
    cc.setReceiverName("MN MIIC");
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    SoapConnector soapConnector = (SoapConnector) connector;
    soapConnector.setCustomTransformations(
        "MSH-4=[OTHERID]\n" + "MSH-5=MIIC \n" + "MSH-6=MIIC \n" + "fix missing mother maiden first \n");
    soapConnector.setAckType(AckAnalyzer.AckType.MIIC);
    soapConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
