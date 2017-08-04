package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.Connector.TransferType;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import org.immregistries.smm.tester.connectors.SoapConnector;

public class VA_VIIS_Template extends ConnectionTemplate {
  public VA_VIIS_Template() {
    super("VA VIIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("VA VIIS");
    // Type
    cc.setType(ConnectorFactory.TYPE_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://viistraining.vdh.virginia.gov/webservices_uat/client_Service?wsdl");
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
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    SoapConnector con = (SoapConnector) connector;
    con.addCustomTransformation("MSH-4=[OTHERID]");
    con.addCustomTransformation("MSH-16=AL");
    con.setAckType(AckAnalyzer.AckType.HP_WIR_DEFAULT);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
