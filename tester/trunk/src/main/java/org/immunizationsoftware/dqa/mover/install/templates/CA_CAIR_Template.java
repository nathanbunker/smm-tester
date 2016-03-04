package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.CASoapConnector;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;

public class CA_CAIR_Template extends ConnectionTemplate
{
  public CA_CAIR_Template() {
    super("CA CAIR");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_CA_SOAP);
      cc.setUrl("https://igsstag.cdph.ca.gov/submit/services/client_Service.client_ServiceHttpsSoap11Endpoint");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setTypeShow(false);
      cc.setInstructions(
          "Contact CAIR for Username, password and facilityID. The Registry Region Code identifies the region the immunization data should be sent to and is defined by CAIR. In addition, CAIR requires that a certificate be used, a Java Key Store must be installed in the smm transmission folder with the name of smm.jks and the password to the key store must be entered here.  ");
      cc.setReceiverName("CAIR");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
      cc.setOtheridShow(true);
      cc.setOtheridRequired(true);
      cc.setOtheridLabel("Registry Region Code");
      cc.setKeyStorePasswordRequired(true);
      cc.setKeyStorePasswordShow(true);
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_CA_SOAP);
      cc.setUrl("https://igs.cdph.ca.gov/submit/client_Servic.client_ServiceHttpsSoap11Endpoint");
      cc.setFacilityidShow(true);
      cc.setFacilityidRequired(true);
      cc.setTypeShow(false);
      cc.setInstructions(
          "Contact CAIR for Username, password and facilityID. The Registry Region Code identifies the region the immunization data should be sent to and is defined by CAIR. In addition, CAIR requires that a certificate be used, a Java Key Store must be installed in the smm transmission folder with the name of smm.jks and the password to the key store must be entered here.  ");
      cc.setReceiverName("CAIR");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setUseridLabel("Username");
      cc.setOtheridShow(true);
      cc.setOtheridRequired(true);
      cc.setOtheridLabel("Registry Region Code");
      cc.setKeyStorePasswordRequired(true);
      cc.setKeyStorePasswordShow(true);
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    CASoapConnector caSoapConnector = (CASoapConnector) connector;
    caSoapConnector.setCustomTransformations("MSH-4=[USERID]\n" + "MSH-6=[OTHERID]\n"
        + "insert segment PD1 after PID if missing\n" + "PD1-12=[MAP ''=>'N']\n" + "MSH-7=[TRUNC 14]\n"
        + "RXA-9=[MAP ''=>'01']\n" + "run procedure Remove_Vaccination_Groups where RXA-20 equals 'RE'\n");
  }

}
