package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.EnvisionConnector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;

public class MD_IMMUNET_Template extends ConnectionTemplate
{
  public MD_IMMUNET_Template() {
    super("MD IMMUNET");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("MD IMMUNET");
    // Type
    cc.setType(ConnectorFactory.TYPE_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://immunet.dhmh.maryland.gov/mdws/client_Service?wsdl");
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
    cc.setOtheridShow(false);
    cc.setOtheridRequired(false);
    // Password
    cc.setPasswordShow(true);
    cc.setPasswordRequired(true);
    cc.setInstructions("");
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    EnvisionConnector con = (EnvisionConnector) connector;
    con.addCustomTransformation("MSH-4.2=[FACILITYID]");
    con.setAckType(AckAnalyzer.AckType.MIIC);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
