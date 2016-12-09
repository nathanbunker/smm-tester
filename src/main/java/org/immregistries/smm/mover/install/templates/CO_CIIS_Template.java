package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.ALSoapConnector;
import org.immregistries.smm.tester.connectors.COSoapConnector;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import org.immregistries.smm.tester.connectors.Connector.TransferType;

public class CO_CIIS_Template extends ConnectionTemplate
{
  public CO_CIIS_Template() {
    super("CO CIIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("CO CIIS");
    // Type
    cc.setType(ConnectorFactory.TYPE_CO_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    cc.setUrl("");
    // User Id
    cc.setUseridShow(true);
    cc.setUseridRequired(true);
    // Facility Id
    cc.setFacilityidShow(true);
    cc.setFacilityidRequired(true);
    // Password
    cc.setPasswordShow(true);
    cc.setPasswordRequired(true);
    cc.setInstructions("");
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    COSoapConnector soapConnector = (COSoapConnector) connector;
    soapConnector.addCustomTransformation("MSH-4=[FACILITYID]");
    soapConnector.addCustomTransformation("MSH-5.1=CIIS");
    soapConnector.addCustomTransformation("MSH-5.2=2.16.840.1.114222.4.1.144.2.4");
    soapConnector.addCustomTransformation("MSH-5.3=ISO");
    soapConnector.addCustomTransformation("MSH-6.1=CDPHE");
    soapConnector.addCustomTransformation("MSH-6.2=2.16.840.1.114222.4.1.144");
    soapConnector.addCustomTransformation("MSH-6.3=ISO");
    soapConnector.setAckType(AckAnalyzer.AckType.DEFAULT);
    soapConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
