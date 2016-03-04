package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.HttpConnector;

public class NM_SIIS_RAW_UAT extends ConnectionTemplate
{
  private static final String _UAT = " UAT";

  public NM_SIIS_RAW_UAT() {
    super("NM SIIS Raw", new String[] { _UAT, _PROD });
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_UAT)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://www.nmhit.org/nmsiistest/rhapsody/receive");
      cc.setTypeShow(false);
      cc.setOtheridShow(true);
      cc.setOtheridRequired(true);
      cc.setUseridLabel("Parent Org Code");
      cc.setOtheridLabel("Child Org Code");
      cc.setPasswordLabel("Pin Code");
      cc.setInstructions("Contact NMSIIS for connecting information.");
      cc.setReceiverName("NMSIIS");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidShow(false);
    } else if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://www.nmhit.org/nmsiis/rhapsody/receive");
      cc.setTypeShow(false);
      cc.setOtheridShow(true);
      cc.setOtheridRequired(true);
      cc.setUseridLabel("Parent Org Code");
      cc.setOtheridLabel("Child Org Code");
      cc.setPasswordLabel("Pin Code");
      cc.setInstructions("Contact NMSIIS for connecting information.");
      cc.setReceiverName("NMSIIS");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidShow(false);
      cc.setEnableTimeShow(true);
      cc.setEnableTimeEnd("18:00");
      cc.setEnableTimeStart("06:00");
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    HttpConnector httpConnector = (HttpConnector) connector;
    httpConnector.setFacilityid("NMSIIS");
    httpConnector.setAuthenticationMethod(HttpConnector.AuthenticationMethod.HEADER);
    httpConnector.setAckType(AckAnalyzer.AckType.NMSIIS);
    httpConnector.setFieldName(HttpConnector.USERID, "orgCode");
    httpConnector.setFieldName(HttpConnector.PASSWORD, "pinCode");
    httpConnector.setFieldName(HttpConnector.FACILITYID, "service");
    httpConnector.setAuthenticationMethod(HttpConnector.AuthenticationMethod.HEADER);
    httpConnector.setCustomTransformations("MSH-3=RPMS\n" + "MSH-4=[OTHERID]\n" + "MSH-6=NMSIIS\n"
        + "insert segment BHS first\n" + "insert segment BTS last\n" + "insert segment FHS first\n"
        + "insert segment FTS last\n" + "FHS-8=CR\n" + "BSH-8=CR\n" + "FHS-9=[FILENAME]\n" + "FTS-1=1\n" + "BTS-1=1\n"
        + "FTS-2=CR\n" + "BTS-2=CR\n" + "FHS-4=[USERID]\n" + "BHS-4=[USERID]\n"
        + "insert segment IN1,IN2 before ORC if missing\n" + "insert segment IN2 after IN1 if missing\n" + "IN1-1=1\n"
        + "fix missing mother maiden first\n" + "remove observation 64994-7 if 18+\n");
  }

}
