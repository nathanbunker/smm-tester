package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;

public class WA_IIS_Template extends ConnectionTemplate
{
  public WA_IIS_Template() {
    super("WA IIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://test-fortress.wa.gov/doh/cpir/iweb/HL7Server");
      cc.setFacilityidShow(false);
      cc.setTypeShow(false);
      cc.setInstructions("Contact WA IIS to obtain username and password. ");
      cc.setReceiverName("WA IIS");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidRequired(false);
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://fortress.wa.gov/doh/cpir/iweb/HL7Server");
      cc.setFacilityidShow(false);
      cc.setTypeShow(false);
      cc.setInstructions("Contact WA IIS to obtain username and password. ");
      cc.setReceiverName("WA IIS");
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
