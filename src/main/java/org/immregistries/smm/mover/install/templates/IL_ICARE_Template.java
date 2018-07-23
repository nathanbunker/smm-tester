package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.Connector.TransferType;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import org.immregistries.smm.tester.connectors.ILConnector;

public class IL_ICARE_Template extends ConnectionTemplate {
  public IL_ICARE_Template() {
    super("IL I-Care");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_IL_WS);
      cc.setUrl("https://icarehl7.dph.illinois.gov");
      cc.setInstructions("Before configuring please request account and password. ");
      cc.setFacilityidShow(false);
      cc.setFacilityidRequired(false);
      cc.setTypeShow(false);
      cc.setReceiverName("I-Care");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
      cc.setOtheridRequired(true);
      cc.setOtheridShow(true);
      cc.setOtheridLabel("MSH-4");
      cc.setOtherid("TEST");
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_IL_WS);
      cc.setUrl("https://icarehl7.dph.illinois.gov");
      cc.setInstructions("Before configuring please request account and password. ");
      cc.setFacilityidShow(false);
      cc.setFacilityidRequired(false);
      cc.setTypeShow(false);
      cc.setReceiverName("I-Care");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
      cc.setOtheridRequired(true);
      cc.setOtheridShow(true);
      cc.setOtheridLabel("MSH-4");
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    ILConnector ilConnector = (ILConnector) connector;
    ilConnector.setCustomTransformations("MSH-4=[OTHERID]\n");
    ilConnector.setAckType(AckAnalyzer.AckType.DEFAULT);
    ilConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
