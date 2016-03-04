package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.EnvisionConnector;
import org.immunizationsoftware.dqa.tester.connectors.MAConnector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;

public class MA_MIIS_Template extends ConnectionTemplate
{
  public MA_MIIS_Template() {
    super("MA MIIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("MA MIIS");
    // Type
    cc.setType(ConnectorFactory.TYPE_MA_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://services.prtst.masshiwaystage.com/MIISCDCService");
    } else if (templateName.endsWith(_PROD)) {
      cc.setUrl("");
    }
    // User Id
    cc.setUseridShow(true);
    cc.setUseridRequired(true);
    // Facility Id
    cc.setFacilityidShow(true);
    cc.setFacilityidRequired(false);
    // Other Id
    cc.setOtheridLabel("Vaccine PIN (MSH-4)");
    cc.setOtheridShow(true);
    cc.setOtheridRequired(true);
    // Password
    cc.setPasswordShow(true);
    cc.setPasswordRequired(true);
    cc.setInstructions("");

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    MAConnector con = (MAConnector) connector;
    con.addCustomTransformation("MSH-4=[OTHERID]");
    con.addCustomTransformation("MSH-5=MIIS");
    con.addCustomTransformation("MSH-6=99990");
    con.addCustomTransformation("MSH-16=");
    con.addCustomTransformation("clean");
    con.addCustomTransformation("insert segment BHS first");
    con.addCustomTransformation("insert segment BTS last");
    con.setAckType(AckAnalyzer.AckType.DEFAULT);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);
  }

}
