package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;

public class ME_PHCHUB_Template extends ConnectionTemplate
{
  public ME_PHCHUB_Template() {
    super("ME Maine");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://portaltest.maine.gov/phchub/HL7Server");
      cc.setInstructions("Before configuring please request credentials. ");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setFacilityidLabel("Profile Id");
      cc.setTypeShow(false);
      cc.setReceiverName("PHCHUB");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("");
      cc.setInstructions("Before configuring please request credentials. ");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setFacilityidLabel("Profile Id");
      cc.setTypeShow(false);
      cc.setReceiverName("PHCHUB");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    // TODO Auto-generated method stub
  }

}
