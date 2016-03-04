package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;

public abstract class ConnectionTemplate
{
  protected static final String _PROD = " Prod";
  protected static final String _TEST = " Test";

  public ConnectionTemplate(String name) {
    ConnectionTemplateFactory.register(name + _PROD, this);
    ConnectionTemplateFactory.register(name + _TEST, this);
  }

  public ConnectionTemplate(String name, String[] types) {
    for (String type : types) {
      ConnectionTemplateFactory.register(name + type, this);
    }
  }

  public abstract void setupConfiguration(String templateName, ConnectionConfiguration cc);

  public abstract void setupConnection(String templateName, Connector connector);

}
