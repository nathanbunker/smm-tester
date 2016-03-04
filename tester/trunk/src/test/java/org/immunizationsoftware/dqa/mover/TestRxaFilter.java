package org.immunizationsoftware.dqa.mover;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.HttpConnector;
import org.junit.Test;

public class TestRxaFilter
{

  public static final String TEST1 = "MSH|^~\\&|||||20160229122916-0500||VXU^V04^VXU_V04|O87V1.1M2|P|2.5.1|\r"
      + "PID|||O87V1^^^AIRA-TEST^MR||Keya Paha^Gwendolyn^Hava^^^^L||20120224|F||2054-5^Black or African-American^HL70005|276 Lokken St^^Oakville^MI^48160^USA^P||^PRN^PH^^^734^9693218|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
      + "ORC|RE||O87V1.1^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20130227||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||^^^PP098||||U6329BP||MSD^Merck and Co^MVX||||A|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160229|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20130227||||||F|\r"
      + "ORC|RE||O87V1.2^AIRA|\r" + "RXA|0|1|20130925||94^MMRV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||O87V1.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20160229||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||^^^KOOSHAREM||||N3783EO||MSD^Merck and Co^MVX||||A|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160229|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160229||||||F|\r";
  
  public static final String TEST1_PART1 = "MSH|^~\\&|||||20160229122916-0500||VXU^V04^VXU_V04|O87V1.1M2|P|2.5.1|\r"
      + "PID|||O87V1^^^AIRA-TEST^MR||Keya Paha^Gwendolyn^Hava^^^^L||20120224|F||2054-5^Black or African-American^HL70005|276 Lokken St^^Oakville^MI^48160^USA^P||^PRN^PH^^^734^9693218|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
      + "ORC|RE||O87V1.1^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20130227||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||^^^PP098||||U6329BP||MSD^Merck and Co^MVX||||A|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160229|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20130227||||||F|\r"
      + "ORC|RE||O87V1.2^AIRA|\r" + "RXA|0|1|20130925||94^MMRV^CVX|999|||01^Historical^NIP001||||||||||||A|\r";
  public static final String TEST1_PART2 = "MSH|^~\\&|||||20160229122916-0500||VXU^V04^VXU_V04|O87V1.1M2|P|2.5.1|\r"
      + "PID|||O87V1^^^AIRA-TEST^MR||Keya Paha^Gwendolyn^Hava^^^^L||20120224|F||2054-5^Black or African-American^HL70005|276 Lokken St^^Oakville^MI^48160^USA^P||^PRN^PH^^^734^9693218|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
      + "ORC|RE||O87V1.2^AIRA|\r" + "RXA|0|1|20130925||94^MMRV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||O87V1.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20160229||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||^^^KOOSHAREM||||N3783EO||MSD^Merck and Co^MVX||||A|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160229|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160229||||||F|\r";

  
  public static final String TEST2 = "MSH|^~\\&|||||20160229122916-0500||VXU^V04^VXU_V04|O87V1.1M2|P|2.5.1|\r"
      + "PID|||O87V1^^^AIRA-TEST^MR||Keya Paha^Gwendolyn^Hava^^^^L||20120224|F||2054-5^Black or African-American^HL70005|276 Lokken St^^Oakville^MI^48160^USA^P||^PRN^PH^^^734^9693218|||||||||2186-5^not Hispanic or Latino^HL70005|\r"
      + "ORC|RE||O87V1.1^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20130227||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||^^^PP098||||U6329BP||MSD^Merck and Co^MVX||||A|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160229|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20130227||||||F|\r"
      + "ORC|RE||O87V1.2^AIRA|\r" + "RXA|0|1|20130925||94^MMRV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
      + "ORC|RE||O87V1.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
      + "RXA|0|1|20160229||21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||^^^KOOSHAREM2||||N3783EO||MSD^Merck and Co^MVX||||A|\r"
      + "RXR|SC^^HL70162|RA^^HL70163|\r"
      + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20160229|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
      + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
      + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
      + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20160229||||||F|\r";
  
  @Test
  public void testFilter1() {
    Connector c1 = new HttpConnector("", "");
    Connector c2 = new HttpConnector("", "");
    c1.setRxaFilterFacilityId("PP098");
    c2.setRxaFilterFacilityId("KOOSHAREM");
    List<Connector> connectorList = new ArrayList<Connector>();
    connectorList.add(c1);
    connectorList.add(c2);
    RxaFilter rxaFilter = new RxaFilter();
    Map<Connector, String> connectorMap = rxaFilter.filter(TEST1, connectorList);
    assertEquals(2, connectorMap.size());
    assertEquals(TEST1_PART1, connectorMap.get(c1));
    assertEquals(TEST1_PART2, connectorMap.get(c2));
  }

  @Test
  public void testFilter2() {
    Connector c1 = new HttpConnector("", "");
    Connector c2 = new HttpConnector("", "");
    c1.setRxaFilterFacilityId("PP098");
    c2.setRxaFilterFacilityId("KOOSHAREM");
    List<Connector> connectorList = new ArrayList<Connector>();
    connectorList.add(c1);
    connectorList.add(c2);
    RxaFilter rxaFilter = new RxaFilter();
    Map<Connector, String> connectorMap = rxaFilter.filter(TEST2, connectorList);
    assertEquals(1, connectorMap.size());
    assertEquals(TEST2, connectorMap.get(c1));
  }

}
