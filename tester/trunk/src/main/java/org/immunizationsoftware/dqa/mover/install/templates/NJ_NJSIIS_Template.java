package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.Connector.TransferType;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.NDSoapConnector;
import org.immunizationsoftware.dqa.tester.connectors.NJConnector;

public class NJ_NJSIIS_Template extends ConnectionTemplate
{
  public NJ_NJSIIS_Template() {
    super("NJ NJSIIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    cc.setReceiverName("NJ NJSIIS");
    // Type
    cc.setType(ConnectorFactory.TYPE_NJ_SOAP);
    cc.setTypeShow(false);
    // URL
    cc.setUrlShow(true);
    if (templateName.endsWith(_TEST)) {
      cc.setUrl("https://njiis-train.nj.gov/ims/service");
    } else if (templateName.endsWith(_PROD)) {
      cc.setUrl("");
    }
    // User Id
    cc.setUseridShow(true);
    cc.setUseridRequired(true);
    // Facility Id
    cc.setFacilityidShow(true);
    cc.setFacilityidRequired(true);
    // Other Id
    cc.setOtheridShow(false);
    cc.setOtheridRequired(false);
    // Password
    cc.setPasswordShow(true);
    cc.setPasswordRequired(true);
    cc.setInstructions("");
  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    NJConnector con = (NJConnector) connector;
    con.addCustomTransformation("MSH-4=[FACILITYID]");
    con.addCustomTransformation("MSH-5=NJIIS");
    con.addCustomTransformation("MSH-6=NJDOH");
    if (templateName.endsWith(_TEST)) {
      con.addCustomTransformation("MSH-11=T");
    } 
    con.addCustomTransformation("PID-3.4=[FACILITYID]");
    con.setAckType(AckAnalyzer.AckType.DEFAULT);
    con.setTransferType(TransferType.NEAR_REAL_TIME_LINK);

  }

}
