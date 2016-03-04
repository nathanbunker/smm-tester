package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.SoapConnector;

public class IA_IRIS_Template extends ConnectionTemplate
{
  public IA_IRIS_Template() {
    super("IA IRIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_SOAP);
      cc.setUrl("https://secure.iris.iowa.gov/webservices_trn/cdc");
      cc.setInstructions(
          "Before configuring please request keystore, keystore password, SOAP credentials, and SOAP password. SMM must be specially configured to support keystore.  ");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setFacilityidLabel("Facility");
      cc.setTypeShow(false);
      cc.setReceiverName("CAIR");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_SOAP);
      cc.setUrl("");
      cc.setInstructions(
          "Before configuring please request keystore, keystore password, SOAP credentials, SOAP password, and URL. SMM must be specially configured to support keystore.  ");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setFacilityidLabel("Facility");
      cc.setTypeShow(false);
      cc.setReceiverName("IRIS");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
    }
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    SoapConnector soapConnector = (SoapConnector) connector;
    soapConnector.setAckType(AckAnalyzer.AckType.MIIC);
    soapConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
