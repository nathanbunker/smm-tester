package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;

public class DefaultTemplate extends ConnectionTemplate {
  private static final String _POST = " POST";
  private static final String _SOAP = " SOAP";

  public DefaultTemplate() {
    super("Default", new String[] {_SOAP, _POST});
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_SOAP)) {
      cc.setType(ConnectorFactory.TYPE_SOAP);
    } else if (templateName.endsWith(_POST)) {
      cc.setType(ConnectorFactory.TYPE_POST);
    }
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    // TODO Auto-generated method stub
  }
}
