package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.SoapConnector;

public class NE_SIIS_Template extends ConnectionTemplate
{
  public NE_SIIS_Template() {
    super("NE SIIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setType(ConnectorFactory.TYPE_SOAP);
    cc.setInstructions("Contact NE SIIS for connecting information.");
    if (templateName.endsWith(_PROD)) {
      cc.setUrl("https://nesiis-dhhs-webservice.ne.gov/prd-webservices/cdc");
    } else {
      cc.setUrl("https://testnesiis-dhhs-testwebservice.ne.gov/uat-webservices/cdc");
    }
    cc.setTypeShow(false);
    cc.setUseridLabel("User Name");
    cc.setUseridRequired(true);
    cc.setFacilityidShow(true);
    cc.setOtheridLabel("MSH-4");
    cc.setPasswordLabel("Password");
    cc.setPasswordRequired(true);
    cc.setOtheridShow(true);
    cc.setOtheridRequired(true);
    cc.setReceiverName("NESIIS");
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    SoapConnector soapConnector = (SoapConnector) connector;
    soapConnector.setCustomTransformations("MSH-4=[OTHERID]\n" + "remove repeat PID-3.5 valued MA\n");
    soapConnector.setAckType(AckAnalyzer.AckType.MIIC);
    soapConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
