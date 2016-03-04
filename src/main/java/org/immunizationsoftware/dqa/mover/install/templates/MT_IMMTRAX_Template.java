package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;

public class MT_IMMTRAX_Template extends ConnectionTemplate
{
  public MT_IMMTRAX_Template() {
    super("MT imMTrax");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://ejs-test.hhs.mt.gov:8443/phchub/HL7Server");
      cc.setFacilityidShow(false);
      cc.setTypeShow(false);
      cc.setInstructions("Contact MT imMTrax to obtain username and password. ");
      cc.setReceiverName("MT imMTrax");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidRequired(false);
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://ejs.hhs.mt.gov:8443/phchub/HL7Server");
      cc.setFacilityidShow(false);
      cc.setTypeShow(false);
      cc.setInstructions("Contact  MT imMTrax to obtain username and password. ");
      cc.setReceiverName("MT imMTrax");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidRequired(false);
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    // TODO Auto-generated method stub

  }

}
