package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.EnvisionConnector;
import org.immunizationsoftware.dqa.tester.connectors.SoapConnector;

public class ID_IRIS_Template extends ConnectionTemplate
{
  public ID_IRIS_Template() {
    super("ID IRIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("ID IRIS");
    // Type
    cc.setType(ConnectorFactory.TYPE_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://iris.dhw.idaho.gov:6658/IRWS/webservicesUat/client_Service");
    } else if (templateName.endsWith(_PROD)) {
      cc.setUrl("");
    }
    // User Id
    cc.setUseridShow(true);
    cc.setUseridRequired(true);
    // Facility Id
    cc.setFacilityid("VXU");
    cc.setFacilityidShow(false);
    cc.setFacilityidRequired(true);
    // Other Id
    cc.setOtheridLabel("HL7 Facility ID (MSH-4.2)");
    cc.setOtheridShow(true);
    cc.setOtheridRequired(true);
    // Password
    cc.setPasswordShow(true);
    cc.setPasswordRequired(true);
    cc.setInstructions("Idaho requires the sender to setup a client certificate.");

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    SoapConnector con = (SoapConnector) connector;
    con.addCustomTransformation("MSH-4.2=[OTHERID]");
    con.addCustomTransformation("MSH-10=12345");
    con.addCustomTransformation("MSH-12=2.4");
    con.addCustomTransformation("RXA-4*=[RXA-3]");
    con.addCustomTransformation("remove observation 64994-7");
    con.addCustomTransformation("remove observation 30956-7");
    con.addCustomTransformation("remove observation 29768-9");
    con.addCustomTransformation("remove observation 29769-7");
    con.setAckType(AckAnalyzer.AckType.IRIS_ID);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
