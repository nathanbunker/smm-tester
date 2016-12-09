package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import org.immregistries.smm.tester.connectors.SoapConnector;
import org.immregistries.smm.tester.connectors.Connector.TransferType;

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
    soapConnector.setAckType(AckAnalyzer.AckType.NESIIS);
    soapConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
