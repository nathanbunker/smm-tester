package org.immunizationsoftware.dqa.tester.manager.forecast;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class TestForecastTesterManager
{

  private static final String RSP_MESSAGE = "MSH|^~\\&|IZ Registry|DE0000||NB9999|20131121115910||RSP^K11^RSP_K11|20131121DE0000415796|P|2.5.1|||||||||Z32^CDCPHINVS\r"
      + "MSA|AA|1385060241123.8\r"
      + "QAK|1385060241123.8|OK|Z34^Request Immunization History^HL70471\r"
      + "QPD|Z34^Request Immunization History^HL70471|1385060241123.8|645740-F1.1^^^^MR|Humboldt^Hari^M^^^^L|Luce^Malkah^^^^^M|20090101|M|368 Jessen Ave^^Brownstown^MI^48164^USA^P|^PRN^PH^^^734^4078350\r"
      + "PID|1||496239^^^^SR~645740-F1.1^^^^MR||HUMBOLDT^HARI^M^^^^L~^^^^^^A|Luce^^^^^^M|20090101|M||1002-5^American Indian or Alaskan Native^HL70005|368 JESSEN AVE^^BROWNSTOWN^MI^48164^USA^P||^PRN^PH^^^734^4078350|||||||||2186-5^Not Hispanic or Latino^HL70189||N|||||||||20131121\r"
      + "PD1|||||||||||||000000|||A|20131121\r"
      + "NK1|1|KALKASKA^MALKAH^^^^^L|MTH^Mother^HL70063\r"
      + "PV1|1|R\r"
      + "ORC|RE||110711|||||||||^^^^^^^^^L\r"
      + "RXA|0|999|20090101|20090101|45^Hep B, UF^CVX|0|ML^^ISO+||01^HISTORICAL INFORMATION - SOURCE UNSPECIFIED^NIP001||||||||||||A|20131121\r"
      + "ORC|RE||110712|||||||||^^^^^^^^^L\r"
      + "RXA|0|999|20090218|20090218|51^Hib-Hep B (Comvax)^CVX|0|ML^^ISO+||01^HISTORICAL INFORMATION - SOURCE UNSPECIFIED^NIP001||||||||||||A|20131121\r"
      + "ORC|RE||110713|||||||||^^^^^^^^^L\r"
      + "RXA|0|999|20090401|20090401|51^Hib-Hep B (Comvax)^CVX|0|ML^^ISO+||01^HISTORICAL INFORMATION - SOURCE UNSPECIFIED^NIP001||||||||||||A|20131121\r"
      + "OBX|1|CE|30979-9^Vaccine Due Next^LN|1|20^DTaP^CVX||||||F\r"
      + "OBX|2|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131121||||||F\r"
      + "OBX|3|CE|30979-9^Vaccine Due Next^LN|1|83^Hep A, ped/adol, 2D^CVX||||||F\r"
      + "OBX|4|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131121||||||F\r"
      + "OBX|5|CE|30979-9^Vaccine Due Next^LN|1|08^Hep B, ped/adol^CVX||||||F\r"
      + "OBX|6|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131121||||||F\r"
      + "OBX|7|CE|30979-9^Vaccine Due Next^LN|1|03^MMR^CVX||||||F\r"
      + "OBX|8|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131121||||||F\r"
      + "OBX|9|CE|30979-9^Vaccine Due Next^LN|1|21^Varicella^CVX||||||F\r"
      + "OBX|10|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131121||||||F\r"
      + "OBX|11|CE|30979-9^Vaccine Due Next^LN|1|141^Influenza, Seasonal^CVX||||||F\r"
      + "OBX|12|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131121||||||F\r"
      + "OBX|13|CE|30979-9^Vaccine Due Next^LN|1|114^MCV4 (Menactra)^CVX||||||F\r"
      + "OBX|14|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131121||||||F\r"
      + "OBX|15|CE|30979-9^Vaccine Due Next^LN|1|21^Varicella^CVX||||||F\r"
      + "OBX|16|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131219||||||F\r"
      + "OBX|17|CE|30979-9^Vaccine Due Next^LN|1|141^Influenza, Seasonal^CVX||||||F\r"
      + "OBX|18|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131219||||||F\r"
      + "OBX|19|CE|30979-9^Vaccine Due Next^LN|1|20^DTaP^CVX||||||F\r"
      + "OBX|20|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131219||||||F\r"
      + "OBX|21|CE|30979-9^Vaccine Due Next^LN|1|83^Hep A, ped/adol, 2D^CVX||||||F\r"
      + "OBX|22|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20131221||||||F\r"
      + "OBX|23|CE|30979-9^Vaccine Due Next^LN|1|114^MCV4 (Menactra)^CVX||||||F\r"
      + "OBX|24|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20140121||||||F\r"
      + "OBX|25|CE|30979-9^Vaccine Due Next^LN|1|62^HPV4 (Gardasil)^CVX||||||F\r"
      + "OBX|26|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20200101||||||F\r"
      + "OBX|27|CE|30979-9^Vaccine Due Next^LN|1|133^PCV13^CVX||||||F\r"
      + "OBX|28|TS|30979-9&30980-7^Date Vaccine Due^LN|1|20740101||||||F\r";

  @Test
  public void test() {
    List<ForecastActual> forecastActualList = ForecastTesterManager.readForecastActual(RSP_MESSAGE);
    assertEquals(14, forecastActualList.size());
    assertEquals("20", forecastActualList.get(0).getVaccineCvx());
    assertEquals("20131121", forecastActualList.get(0).getDueDate());
    assertEquals("133", forecastActualList.get(13).getVaccineCvx());
    assertEquals("20740101", forecastActualList.get(13).getDueDate());
  }

}
