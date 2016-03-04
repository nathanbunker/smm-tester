package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.EnvisionConnector;
import org.immunizationsoftware.dqa.tester.connectors.MOConnector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;

public class MO_SHOWMEVAX_Template extends ConnectionTemplate
{
  public MO_SHOWMEVAX_Template() {
    super("MO ShowMeVax");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("MO ShowMeVax");
    // Type
    cc.setType(ConnectorFactory.TYPE_MO_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://hl7smv.dhss.mo.gov/Services/SMVAX_ProviderInterface_EXT_WS/ProviderInterface_EXT_WS.asmx");
    } else if (templateName.endsWith(_PROD)) {
      cc.setUrl("");
    }
    // User Id
    cc.setUseridLabel("Username");
    cc.setUseridShow(true);
    cc.setUseridRequired(true);
    // Facility Id
    cc.setFacilityidShow(true);
    cc.setFacilityidRequired(true);
    // Other Id
    cc.setOtheridLabel("Password 2");
    cc.setOtheridShow(true);
    cc.setOtheridRequired(true);
    // Password
    cc.setPasswordLabel("Password 1");
    cc.setPasswordShow(true);
    cc.setPasswordRequired(true);
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    MOConnector con = (MOConnector) connector;
    con.addCustomTransformation("");
    con.addCustomTransformation("MSH-4=[FACILITYID]");
    con.addCustomTransformation("MSH-5=SHOWMEVAX");
    con.addCustomTransformation("MSH-6=MODHSS");
    con.addCustomTransformation("MSH-7=[TRUNC 14]");
    con.addCustomTransformation("PID-3.5=[MAP 'MR'=>'PI']");
    con.addCustomTransformation("RXA-4*=[RXA-3]");
    con.setAckType(AckAnalyzer.AckType.DEFAULT);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
