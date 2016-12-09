package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import org.immregistries.smm.tester.connectors.HttpConnector;

public class MS_MIX_Template extends ConnectionTemplate
{
  public MS_MIX_Template() {
    super("MS MIX");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://immunizations.ms-hin.net/HttpPostForwarder/");
      cc.setInstructions("Before configuring please request credentials. ");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setFacilityidLabel("MSH-4 Sending Facility");
      cc.setTypeShow(false);
      cc.setReceiverName("MIX");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://immunizations.ms-hin.net/HttpPostForwarder/");
      cc.setInstructions("Before configuring please request credentials. ");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setFacilityidLabel("MSH-4 Sending Facility");
      cc.setTypeShow(false);
      cc.setReceiverName("MIX");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    HttpConnector httpConnector = (HttpConnector) connector;
    httpConnector.setCustomTransformations("MSH-4=[FACILITYID]\n");
  }

}
