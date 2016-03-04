package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.NDSoapConnector;

public class ND_NDIIS_Template extends ConnectionTemplate
{
  public ND_NDIIS_Template() {
    super("ND NDIIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("NC NCIR");
    // Type
    cc.setType(ConnectorFactory.TYPE_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://ncc416.its.state.nc.us/uatservice/immsws");
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
    NDSoapConnector con = (NDSoapConnector) connector;
    con.addCustomTransformation("MSH-4=[OTHERID]");
    con.addCustomTransformation("MSH-5=NDIIS");
    con.addCustomTransformation("MSH-6=ND0000");
    con.addCustomTransformation("remove segment PV1");
    con.addCustomTransformation("clear NK1-7");
    con.setAckType(AckAnalyzer.AckType.DEFAULT);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);

  }

}
