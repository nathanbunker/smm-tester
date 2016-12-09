package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import org.immregistries.smm.tester.connectors.SoapConnector;
import org.immregistries.smm.tester.connectors.WIConnector;
import org.immregistries.smm.tester.connectors.Connector.TransferType;

public class WI_WIR_Template extends ConnectionTemplate
{
  public WI_WIR_Template() {
    super("WI WIR");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("WI WIR");
    // Type
    cc.setType(ConnectorFactory.TYPE_WI_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://wir.dhswir.org/trn-webservices/cdc?wsdl");
    } else if (templateName.endsWith(_PROD)) {
      cc.setUrl("");
    }
    // User Id
    cc.setUseridShow(true);
    cc.setUseridRequired(true);
    // Facility Id
    cc.setFacilityidShow(true);
    cc.setFacilityidRequired(true);
    // Other Id
    cc.setOtheridLabel("HL7 Facility ID (MSH-4.2)");
    cc.setOtheridShow(true);
    cc.setOtheridRequired(true);
    // Password
    cc.setPasswordShow(true);
    cc.setPasswordRequired(true);
    cc.setInstructions("WIR requires the sender to setup a client certificate.");
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    WIConnector con = (WIConnector) connector;
    con.addCustomTransformation("MSH-4=[OTHERID]");
    con.setAckType(AckAnalyzer.AckType.DEFAULT);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
