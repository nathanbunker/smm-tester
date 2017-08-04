package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.Connector.TransferType;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import org.immregistries.smm.tester.connectors.ORConnector;

public class OR_ALERT_Template extends ConnectionTemplate {
  public OR_ALERT_Template() {
    super("OR ALERT");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("OR ALERT");
    // Type
    cc.setType(ConnectorFactory.TYPE_OR_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://soa.alertiis.org/webservicestrn/VaccinationBService");
    } else if (templateName.endsWith(_PROD)) {
      cc.setUrl("");
    }
    // User Id
    cc.setUseridShow(true);
    cc.setUseridRequired(true);
    // Facility Id
    cc.setFacilityidShow(true);
    cc.setFacilityidRequired(false);
    // Other Id
    cc.setOtheridShow(false);
    cc.setOtheridRequired(false);
    // Password
    cc.setPasswordShow(true);
    cc.setPasswordRequired(true);
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    ORConnector con = (ORConnector) connector;
    con.setAckType(AckAnalyzer.AckType.ALERT);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
