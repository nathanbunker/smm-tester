package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;

public class AK_VACTRAK_Template extends ConnectionTemplate
{
  public AK_VACTRAK_Template() {
    super("AK VacTrAK");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://vactrakihub.alaska.gov/ihubtst/HL7Server");
      cc.setFacilityidShow(false);
      cc.setTypeShow(false);
      cc.setInstructions("Contact AK VacTrAK to obtain username and password. ");
      cc.setReceiverName("AK VacTrAK");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidRequired(false);
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://vactrakihub.alaska.gov/ihubtst/HL7Server");
      cc.setFacilityidShow(false);
      cc.setTypeShow(false);
      cc.setInstructions("Contact AK VacTrAK to obtain username and password. ");
      cc.setReceiverName("AK VacTrAK");
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
