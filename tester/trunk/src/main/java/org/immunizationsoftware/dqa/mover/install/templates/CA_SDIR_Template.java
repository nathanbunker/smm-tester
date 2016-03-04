package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;

public class CA_SDIR_Template extends ConnectionTemplate
{
  public CA_SDIR_Template() {
    super("CA SDIR");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("http://www.sdirtrain.org/other/receivehl7.jsp");
      cc.setFacilityidShow(true);
      cc.setTypeShow(false);
      cc.setInstructions("In order to connect to SDIR you will need a User Id, Password and Facility Id. ");
      cc.setReceiverName("SDIR");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidRequired(true);
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("");
      cc.setFacilityidShow(true);
      cc.setTypeShow(false);
      cc.setInstructions("In order to connect to SDIR you will need a User Id, Password and Facility Id and the URL. ");
      cc.setReceiverName("SDIR");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidRequired(true);
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    // nothing to do
  }

}
