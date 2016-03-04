package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.HttpConnector;

public class NV_WEBIZ_Template extends ConnectionTemplate
{
  public NV_WEBIZ_Template() {
    super("NV WebIZ");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {

    cc.setType(ConnectorFactory.TYPE_POST);
    cc.setInstructions("Contact NV WebIZ for connecting information.");
    if (templateName.endsWith(_PROD)) {
      cc.setUrl("https://webiz.nv.gov/HL7EngineAuthentication/Service.asmx/ExecuteHL7Message");
    } else {
      cc.setUrl("https://webizqa.nv.gov/HL7EngineAuthentication/Service.asmx/ExecuteHL7Message");
    }
    cc.setTypeShow(false);
    cc.setUseridLabel("User Name");
    cc.setUseridRequired(true);
    cc.setFacilityidShow(false);
    cc.setOtheridLabel("Facility Id");
    cc.setPasswordLabel("Password");
    cc.setPasswordRequired(true);
    cc.setOtheridShow(true);
    cc.setOtheridRequired(true);
    cc.setReceiverName("NV WebIZ");

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    HttpConnector httpConnector = (HttpConnector) connector;
    httpConnector.stripXML();
    httpConnector.setFieldName(HttpConnector.USERID, "userName");
    httpConnector.setFieldName(HttpConnector.PASSWORD, "password");
    httpConnector.setFieldName(HttpConnector.MESSAGEDATA, "flatWire");
    httpConnector.setCustomTransformations("MSH-4=[OTHERID]\n" + "MSH-6=NV0000 \n" + "PID-3.4=IHS_FACILITY\n"
        + "PID-6.7=M\n" + "PID-11.7=P\n" + "PID-22.3=[MAP 'CDCREC'=>'HL70189']\n" + "NK1-2.7*=L\n" + "NK1-7.1*=\n"
        + "NK1-7.2*=\n" + "NK1-7.3*=\n" + "PD1-11.1=\n" + "PD1-18.1=\n" + "ORC-2.2*=\n" + "ORC-3.1*=[TRUNC 20]\n"
        + "ORC-3.2*=\n" + "ORC-12.1*=\n" + "ORC-17.1*=\n" + "ORC-17.2*=\n" + "ORC-17.3*=\n" + "ORC-17.4*=\n"
        + "RXA-7.1*=[MAP 'mL'=>'ML']\n" + "RXA-7.3*=[MAP 'UCUM'=>'ISO+']\n" + "RXA-10.1*=\n"
        + "RXR-2.1*=[MAP 'LI'=>'LLFA']\n" + "remove observation 30956-7\n" + "remove empty observations\n");
    httpConnector.setAckType(AckAnalyzer.AckType.WEBIZ);
    httpConnector.setTransferType(TransferType.NEAR_REAL_TIME_LINK);

  }

}
