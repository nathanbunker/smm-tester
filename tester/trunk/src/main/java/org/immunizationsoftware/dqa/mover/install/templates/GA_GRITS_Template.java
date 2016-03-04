package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.SoapConnector;

public class GA_GRITS_Template extends ConnectionTemplate
{
  public GA_GRITS_Template() {
    super("GA GRITS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("GA GRITS");
    // Type
    cc.setType(ConnectorFactory.TYPE_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://www.gritstest.state.ga.us/gritsws/client_Service");
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
    SoapConnector con = (SoapConnector) connector;
    con.addCustomTransformation("MSH-4=[OTHERID]");
    con.addCustomTransformation("MSH-5=GRITS");
    con.addCustomTransformation("MSH-6=GRITS");
    con.addCustomTransformation("remove segment PV1");
    con.addCustomTransformation("OBX-5.3*=[MAP ''=>'Any Value']");
    con.setAckType(AckAnalyzer.AckType.MIIC);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
