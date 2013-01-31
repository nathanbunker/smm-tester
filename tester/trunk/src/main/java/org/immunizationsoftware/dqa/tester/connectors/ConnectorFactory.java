package org.immunizationsoftware.dqa.tester.connectors;

public class ConnectorFactory
{
  
  public static final String TYPE_SOAP = "SOAP";
  public static final String TYPE_POST = "POST";
  public static final String TYPE_NM_SOAP = "NM SOAP";
  public static final String TYPE_HI_SOAP = "HI SOAP";
  
  public static final String[][] TYPES = {{TYPE_SOAP, "SOAP"}, {TYPE_POST, "POST"}, {TYPE_NM_SOAP, "NM SOAP"}, {TYPE_HI_SOAP, "HI SOAP (same standard as SOAP)"}};
  
  public static Connector getConnector(String type, String label, String url) throws Exception
  {
    Connector connector = null;
    if (type.equals(TYPE_SOAP))
    {
      connector = new SoapConnector(label, url);
    } else if (type.equals(TYPE_NM_SOAP))
    {
      connector = new NMSoapConnector(label, url);
    } else if (type.equals(TYPE_HI_SOAP))
    {
      connector = new HISoapConnector(label, url);
    } else if (type.equals(TYPE_POST))
    {
      connector = new HttpConnector(label, url);
    }
    return connector;
  }
}
