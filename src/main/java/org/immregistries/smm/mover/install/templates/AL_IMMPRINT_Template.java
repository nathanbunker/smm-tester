package org.immregistries.smm.mover.install.templates;

import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.mover.install.ConnectionConfiguration;
import org.immregistries.smm.tester.connectors.ALSoapConnector;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.Connector.TransferType;
import org.immregistries.smm.tester.connectors.ConnectorFactory;

public class AL_IMMPRINT_Template extends ConnectionTemplate {
  public AL_IMMPRINT_Template() {
    super("AL ImmPRINT");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("AL ImmPrint");
    // Type
    cc.setType(ConnectorFactory.TYPE_AL_SOAP);
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
    cc.setInstructions(
        "You must first register with ADPH and follow instructions on Portal to gain access. "
            + "This interface will require configuring a client certificate. The following command can be used "
            + "as a template to show you how to import the certificate into the Java Key Store (JKS) so that "
            + "SMM is able to connect. <br/><pre>keytool -import -trustcacerts -alias test-adph2 -file "
            + "hie.adph.state.al.us.crt -keystore \"C:\\Program Files\\Java\\jdk1.8.0_25\\jre\\lib\\security\\cacerts\"</pre>");
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    ALSoapConnector soapConnector = (ALSoapConnector) connector;
    soapConnector.addCustomTransformation("MSH-3=[USERID]");
    soapConnector.addCustomTransformation("MSH-4=[FACILITYID]");
    soapConnector.addCustomTransformation("MSH-5=AL-IIS");
    soapConnector.addCustomTransformation("MSH-6=AL-IIS");
    soapConnector.addCustomTransformation("MSH-8=[PASSWORD]");
    soapConnector.addCustomTransformation("MSH-15=ER");
    soapConnector.addCustomTransformation("MSH-16=AL");
    soapConnector.addCustomTransformation("ORC-17.1*=");
    soapConnector.addCustomTransformation("ORC-17.2*=");
    soapConnector.addCustomTransformation("ORC-17.3*=");
    soapConnector.setAckType(AckAnalyzer.AckType.DEFAULT);
    soapConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }
}
