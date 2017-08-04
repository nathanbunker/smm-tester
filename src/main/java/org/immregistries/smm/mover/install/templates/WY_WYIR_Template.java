package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;

public class WY_WYIR_Template extends ConnectionTemplate {
  public WY_WYIR_Template() {
    super("WY WyIR");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("WY WyIR");
    // Type
    cc.setType(ConnectorFactory.TYPE_POST);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://wyir.health.wyo.gov/siistst/HL7Server");
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
    cc.setInstructions("");

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    // TODO Auto-generated method stub

  }

}
