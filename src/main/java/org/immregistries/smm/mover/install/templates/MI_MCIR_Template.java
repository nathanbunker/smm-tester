package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;

public class MI_MCIR_Template extends ConnectionTemplate
{
  public MI_MCIR_Template() {
    super("MI MCIR MLLP");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_MLLP);
      cc.setUrl("http://host-name-or-ip:port");
      cc.setFacilityidShow(false);
      cc.setUseridShow(false);
      cc.setPasswordShow(false);
      cc.setTypeShow(false);
      cc.setInstructions("Please update the URL to indicate the local host name and port that you are connecting to. ");
      cc.setReceiverName("MI MCIR");
      cc.setUseridRequired(false);
      cc.setPasswordRequired(false);
      cc.setFacilityidRequired(false);
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_MLLP);
      cc.setUrl("http://host-name-or-ip:port");
      cc.setFacilityidShow(false);
      cc.setUseridShow(false);
      cc.setPasswordShow(false);
      cc.setTypeShow(false);
      cc.setInstructions("Please update the URL to indicate the local host name and port that you are connecting to. ");
      cc.setReceiverName("MI MCIR");
      cc.setUseridRequired(false);
      cc.setPasswordRequired(false);
      cc.setFacilityidRequired(false);
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    if (templateName.endsWith(_TEST)) {
      connector.setCustomTransformations("MSH-11=T\n");
    } else if (templateName.endsWith(_PROD)) {
      connector.setCustomTransformations("MSH-11=P\n");
    }
  }

}
