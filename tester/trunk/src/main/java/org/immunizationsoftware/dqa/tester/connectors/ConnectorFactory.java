package org.immunizationsoftware.dqa.tester.connectors;

public class ConnectorFactory
{

  public static final String TYPE_SOAP = "SOAP";
  public static final String TYPE_POST = "POST";
  public static final String TYPE_NM_SOAP = "NM SOAP";
  public static final String TYPE_HI_SOAP = "HI SOAP";
  public static final String TYPE_ENVISION_SOAP = "Envision SOAP";
  public static final String TYPE_OR_SOAP = "OR ALERT SOAP";
  public static final String TYPE_CA_SOAP = "CA SOAP";
  public static final String TYPE_CA_SOAP2 = "CA SOAP2";

  public static final String[][] TYPES = { { TYPE_SOAP, "SOAP" }, { TYPE_POST, "POST" }, { TYPE_CA_SOAP, "CA SOAP" },
      { TYPE_CA_SOAP2, "CA SOAP2" }, { TYPE_NM_SOAP, "NM SOAP" }, { TYPE_ENVISION_SOAP, "Envision SOAP" },
      { TYPE_HI_SOAP, "HI SOAP (same standard as SOAP)" } };

  public static Connector getConnector(String type, String label, String url) throws Exception {
    Connector connector = null;
    if (type.equals(TYPE_SOAP)) {
      connector = new SoapConnector(label, url);
    } else if (type.equals(TYPE_NM_SOAP)) {
      connector = new NMSoapConnector(label, url);
    } else if (type.equals(TYPE_HI_SOAP)) {
      connector = new HISoapConnector(label, url);
    } else if (type.equals(TYPE_POST)) {
      connector = new HttpConnector(label, url);
    } else if (type.equals(TYPE_ENVISION_SOAP)) {
      connector = new EnvisionConnector(label, url);
    } else if (type.equals(TYPE_OR_SOAP)) {
      connector = new ORConnector(label, url);
    } else if (type.equals(TYPE_CA_SOAP)) {
      connector = new CASoapConnector(label, url);
    } else if (type.equals(TYPE_CA_SOAP2)) {
      connector = new CASoap2Connector(label, url);
    }
    return connector;
  }
}
