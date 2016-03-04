package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.ALSoapConnector;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.EnvisionConnector;

public class AR_WEBIZ_Template extends ConnectionTemplate
{
  public AR_WEBIZ_Template() {
    super("AR WebIZ");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("AR WebIZ");
    // Type
    cc.setType(ConnectorFactory.TYPE_ENVISION_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://adhimmreg.arkansas.gov/HL7Engine/WebServices/Default.asmx");
    } else if (templateName.endsWith(_PROD)) {
      cc.setUrl("");
    }
    // User Id
    cc.setUseridLabel("Username");
    cc.setUseridShow(true);
    cc.setUseridRequired(true);
    // Facility Id
    cc.setFacilityidShow(false);
    cc.setFacilityidRequired(false);
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
    EnvisionConnector con = (EnvisionConnector) connector;
    con.addCustomTransformation("MSH-4=[OTHERID]");
    con.addCustomTransformation("MSH-6=AR0000");
    con.addCustomTransformation("MSH-10=[TRUNC 20]");
    con.addCustomTransformation("RXA-22*=[TRUNC 14]");
    con.addCustomTransformation("ORC-17.1*=");
    con.addCustomTransformation("ORC-17.2*=");
    con.addCustomTransformation("ORC-17.3*=");
    con.setAckType(AckAnalyzer.AckType.WEBIZ);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
