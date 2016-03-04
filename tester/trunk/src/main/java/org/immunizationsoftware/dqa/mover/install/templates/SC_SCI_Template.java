package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.SCSoapConnector;

public class SC_SCI_Template extends ConnectionTemplate
{
  public SC_SCI_Template() {
    super("SC SCI");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("SC SCI");
    // Type
    cc.setType(ConnectorFactory.TYPE_SC_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://immun.dhec.sc.gov/HL7Service/MessageService.svc");
    } else if (templateName.endsWith(_PROD)) {
      cc.setUrl("");
    }
    // User Id
    cc.setUseridLabel("Username");
    cc.setUseridShow(true);
    cc.setUseridRequired(true);
    // Facility Id
    cc.setFacilityidShow(true);
    cc.setFacilityidRequired(true);
    // Other Id
    cc.setOtheridLabel("HL7 Facility ID (MSH-4)");
    cc.setOtheridShow(true);
    cc.setOtheridRequired(true);
    // Password
    cc.setPasswordShow(true);
    cc.setPasswordRequired(true);
    cc.setInstructions("");

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    SCSoapConnector con = (SCSoapConnector) connector;
    con.addCustomTransformation("MSH-4=[OTHERID]");
    con.addCustomTransformation("MSH-5=SCI");
    con.addCustomTransformation("MSH-6=SCI");
    con.setAckType(AckAnalyzer.AckType.MIIC);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);

  }

}
