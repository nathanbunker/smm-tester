package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;

public class PA_SIIS_Template extends ConnectionTemplate
{
  public PA_SIIS_Template() {
    super("PA SIIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("PA SIIS");
    // Type
    cc.setType(ConnectorFactory.TYPE_POST);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://siis.health.beta.state.pa.us/hl7/HL7I.ashx");
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
    // TODO Auto-generated method stub

  }

}
