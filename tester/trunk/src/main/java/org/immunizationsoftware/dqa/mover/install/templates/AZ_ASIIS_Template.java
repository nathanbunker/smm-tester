package org.immunizationsoftware.dqa.mover.install.templates;

import org.immunizationsoftware.dqa.mover.install.ConnectionConfiguration;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.ConnectorFactory;
import org.immunizationsoftware.dqa.tester.connectors.HttpConnector;

public class AZ_ASIIS_Template extends ConnectionTemplate
{
  public AZ_ASIIS_Template() {
    super("AZ ASIIS");
  }

  @Override
  public void setupConfiguration(String templateName, ConnectionConfiguration cc) {
    if (templateName.endsWith(_PROD)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://app.azdhs.gov/phs/asiis/hl7post/");
      cc.setFacilityidShow(true);
      cc.setFacilityidLabel("IRMS ID");
      cc.setTypeShow(false);
      cc.setInstructions(
          "In order to connect to ASIIS Production you will need to request a Username, Password and IRMS ID from the <a href=\"https://www.asiis.state.az.us/\" target=\"_blank\">ASIIS User Support help desk</a>. Please provide the User Id, Password, and IRMS ID before continuing. ");
      cc.setReceiverName("ASIIS");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidRequired(true);
    } else if (templateName.endsWith(_TEST)) {
      cc.setType(ConnectorFactory.TYPE_POST);
      cc.setUrl("https://appqa.azdhs.gov/phs/asiis/hl7post/");
      cc.setFacilityidShow(true);
      cc.setFacilityidLabel("IRMS ID");
      cc.setTypeShow(false);
      cc.setInstructions(
          "In order to connect to ASIIS Test you will need to request a Username, Password and IRMS ID from the <a href=\"https://test-asiis.azdhs.gov/\" target=\"_blank\">ASIIS User Support help desk</a>. Please provide the User Id, Password and IRMS ID before continuing.");
      cc.setReceiverName("ASIIS");
      cc.setUseridRequired(true);
      cc.setPasswordRequired(true);
      cc.setFacilityidRequired(true);
    }

  }

  @Override
  public void setupConnection(String templateName, Connector connector) {
    HttpConnector httpConnector = (HttpConnector) connector;
    httpConnector.setCustomTransformations(
        "MSH-3=RPMS\n" + "MSH-4=[FACILITYID]\n" + "MSH-5=ASIIS\n" + "PV1-10=\n" + "fix ampersand\n");
  }

}
