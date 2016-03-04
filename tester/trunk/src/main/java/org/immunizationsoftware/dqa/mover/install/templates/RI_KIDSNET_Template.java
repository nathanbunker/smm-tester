package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.HttpConnector;

public class RI_KIDSNET_Template extends ConnectionTemplate
{
  public RI_KIDSNET_Template() {
    super("RI KIDSNET");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {

    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://kidsnet.health.ri.gov/hl7processor-play/recv.hl7");
      cc.setInstructions("Before configuring please request credentials. ");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setFacilityidLabel("MSH-4 Sending Facility");
      cc.setTypeShow(false);
      cc.setReceiverName("KIDSNET");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
      cc.setOtheridRequired(true);
      cc.setOtheridShow(true);
      cc.setOtheridLabel("MSH-22 Responsible Business Organization");
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("");
      cc.setInstructions("Before configuring please request credentials. ");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setFacilityidLabel("MSH-4 Sending Facility");
      cc.setTypeShow(false);
      cc.setReceiverName("KIDSNET");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
      cc.setOtheridRequired(true);
      cc.setOtheridShow(true);
      cc.setOtheridLabel("MSH-22 Responsible Business Organization");
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    HttpConnector httpConnector = (HttpConnector) connector;
    httpConnector.setCustomTransformations("MSH-4=[FACILITYID]\n" + "MSH-22=[OTHERID]\n");
  }

}
