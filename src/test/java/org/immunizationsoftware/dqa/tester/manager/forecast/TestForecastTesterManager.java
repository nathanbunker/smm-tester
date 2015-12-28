package org.immunizationsoftware.dqa.tester.manager.forecast;

import static org.junit.Assert.*;

import java.util.List;

import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.junit.Test;

public class TestForecastTesterManager
{

  private static final String RSP_MESSAGE = "MSH|^~\\&|IZ Registry|DE0000||NB9999|20131121115910||RSP^K11^RSP_K11|20131121DE0000415796|P|2.5.1|||||||||Z32^CDCPHINVS\r"
      + "MSA|AA|1385060241123.8\r" + "QAK|1385060241123.8|OK|Z34^Request Immunization History^HL70471\r"
      + "QPD|Z34^Request Immunization History^HL70471|1385060241123.8|645740-F1.1^^^^MR|Humboldt^Hari^M^^^^L|Luce^Malkah^^^^^M|20090101|M|368 Jessen Ave^^Brownstown^MI^48164^USA^P|^PRN^PH^^^734^4078350\r"
      + "PID|1||496239^^^^SR~645740-F1.1^^^^MR||HUMBOLDT^HARI^M^^^^L~^^^^^^A|Luce^^^^^^M|20090101|M||1002-5^American Indian or Alaskan Native^HL70005|368 JESSEN AVE^^BROWNSTOWN^MI^48164^USA^P||^PRN^PH^^^734^4078350|||||||||2186-5^Not Hispanic or Latino^HL70189||N|||||||||20131121\r"
      + "PD1|||||||||||||000000|||A|20131121\r" + "NK1|1|KALKASKA^MALKAH^^^^^L|MTH^Mother^HL70063\r" + "PV1|1|R\r"
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

  private static final String STC1 = "MSH|^~\\&|^^|^^|^^|^^|20151226032840||RSP^K11^RSP_K11|5313059237.100096449|P|2.5.1|||||||||Z32^CDCPHINVS^^|\r"
      + "MSA|AA|1451127697806.1002||\r" + "QAK|1451127697806.1002|OK|Z34^Request Immunization History^HL70471|\r"
      + "QPD|Z34^Request Immunization History^HL70471|1451127697806.1002|SIJ-M.01.01^^^AIRA-TEST^MR|Franklin^Gryffyn^Abbott|Dallam^Reina|20111229|M|35 Nemaha Pl^^Watersmeet^MI^49969^USA^P|\r"
      + "PID|1||21223^^^^SR~~~~~SIJ-M.01.01||FRANKLIN^GRYFFYN^ABBOTT^^^^L|DALLAM|20111229|M|||35 NEMAHA PL^^WATERSMEET^MICHIGAN^49969^United States^M^^GOGEBIC||(906)933-3406^^PH^^^906^9333406^|||||||||2186-5^not Hispanic or Latino^HL70189||||||||N|\r"
      + "PD1|||^^^^^^SR|^^^^^^^^^^^^SR|||||||02^Reminder/recall -any method^HL70215|||||A^Active^HL70441|\r"
      + "NK1|1|FRANKLIN^REINA|GRD^Guardian^HL70063||(906)933-3406^^PH^^^906^9333406^|\r" + "PV1||R|\r"
      + "ORC|RE||21223.54.20151226|\r"
      + "RXA|0|999|20151226|20151226|94^MMRV^CVX^90710^MMRV^CPT~54^MMR/Varicella^STC0292|.5|ML^mL^ISO+||00^New immunization record^NIP001||IRMS-1010||||V7737HT||MSD^Merck and Co., Inc.^HL70227||||A|20151226032840|\r"
      + "RXR|SC^Subcutaneous^HL70162|RA^Right Arm^HL70163|\r"
      + "OBX|1|TS|29769-7^VIS Presentation Date^LN|1|20151226||||||F|\r"
      + "OBX|1|CE|VFC-STATUS^VFC Status^STC|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20151226|\r"
      + "OBX|1|CE|64994-7^vaccine fund pgm elig cat^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20151226|||CVX40^per imm^CDCPHINVS|\r"
      + "OBX|1|TS|29768-9^VIS Publication Date^LN|1|20100521||||||F|\r"
      + "OBX|1|CE|30963-3^Vaccine purchased with^LN|1|||||||F|||20151226|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20151226032840|20151226032840|998^no vaccine administered^CVX|0||||||||||||||NA||20151226032840|\r"
      + "RXR|OTH^Other/Miscellaneous^HL70162|\r" + "OBX|1|CE|30956-7^vaccine type^LN|1|45^HepB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20111229||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20111229||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21311229||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20120329||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20151226032840|20151226032840|998^no vaccine administered^CVX|0||||||||||||||NA||20151226032840|\r"
      + "RXR|OTH^Other/Miscellaneous^HL70162|\r" + "OBX|1|CE|30956-7^vaccine type^LN|1|107^DTAP^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20120229||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20120209||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21311229||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20120330||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20151226032840|20151226032840|998^no vaccine administered^CVX|0||||||||||||||NA||20151226032840|\r"
      + "RXR|OTH^Other/Miscellaneous^HL70162|\r" + "OBX|1|CE|30956-7^vaccine type^LN|1|17^HIB^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20120229||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20120209||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21311229||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20120330||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20151226032840|20151226032840|998^no vaccine administered^CVX|0||||||||||||||NA||20151226032840|\r"
      + "RXR|OTH^Other/Miscellaneous^HL70162|\r" + "OBX|1|CE|30956-7^vaccine type^LN|1|133^PneumoPCV^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20120229||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20120209||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|20161229||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20120330||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20151226032840|20151226032840|998^no vaccine administered^CVX|0||||||||||||||NA||20151226032840|\r"
      + "RXR|OTH^Other/Miscellaneous^HL70162|\r" + "OBX|1|CE|30956-7^vaccine type^LN|1|89^POLIO^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20120229||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20120209||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21311229||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20120330||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20151226032840|20151226032840|998^no vaccine administered^CVX|0||||||||||||||NA||20151226032840|\r"
      + "RXR|OTH^Other/Miscellaneous^HL70162|\r" + "OBX|1|CE|30956-7^vaccine type^LN|1|88^FLU^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20120629||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20120629||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21311229||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20120729||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|P^Past Due^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20151226032840|20151226032840|998^no vaccine administered^CVX|0||||||||||||||NA||20151226032840|\r"
      + "RXR|OTH^Other/Miscellaneous^HL70162|\r" + "OBX|1|CE|30956-7^vaccine type^LN|1|3^MMR^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|2||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20160123||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20160123||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21311229||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20181229||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20151226032840|20151226032840|998^no vaccine administered^CVX|0||||||||||||||NA||20151226032840|\r"
      + "RXR|OTH^Other/Miscellaneous^HL70162|\r" + "OBX|1|CE|30956-7^vaccine type^LN|1|21^VARICELLA^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|2||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20160319||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20160319||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21311229||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20181229||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r" + "ORC|RE||9999|\r"
      + "RXA|0|999|20151226032840|20151226032840|998^no vaccine administered^CVX|0||||||||||||||NA||20151226032840|\r"
      + "RXR|OTH^Other/Miscellaneous^HL70162|\r" + "OBX|1|CE|30956-7^vaccine type^LN|1|147^MENING^CVX||||||F|\r"
      + "OBX|1|CE|59779-9^Immunization Schedule used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|\r"
      + "OBX|1|NM|30973-2^Dose number in series^LN|1|1||||||F|\r"
      + "OBX|1|TS|30980-7^Date vaccination due^LN|1|20221229||||||F|\r"
      + "OBX|1|TS|30981-5^Earliest date to give^LN|1|20221229||||||F|\r"
      + "OBX|1|TS|59777-3^Latest date next dose should be given^LN|1|21311229||||||F|\r"
      + "OBX|1|TS|59778-1^Date dose is overdue^LN|1|20241229||||||F|\r"
      + "OBX|1|CE|59783-1^Status in immunization series^LN|1|U^Up to Date^STC0002||||||F|\r";
  
  private static final String OR_ALERT1 = "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20151228||RSP^K11^RSP_K11|1451316056796.19|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r" + 
"MSA|AA|1451316056796.19||0||0^Message Accepted^HL70357\r" + 
"QAK|1451316056796.19|OK|Z44\r" + 
"QPD|Z44^Request Immunization History^HL70471|1451316056796.19|L26E4^^^AIRA-TEST^MR|Clay^Trefusis^Jayce^^^^L|Jepsen^Ruchika|20150622|M|269 Maricopa Pl^^Bessemer^MI^49911^USA^P|^PRN^PH^^^906^5136155\r" + 
"PID|1||7242667^^^ORA^SR~L26E4^^^ORA^MR||CLAY^TREFUSIS^JAYCE^^^^L|JEPSEN^RUCHIKA|20150622|M|||269 MARICOPA PL^^BESSEMER^MI^49911^^P||^PRN^PH^^^906^5136155|||||||||||N|0\r" + 
"PD1|||||||||||02|N||||A\r" + 
"NK1|1|CLAY^RUCHIKA|MTH|269 MARICOPA PL^^BESSEMER^MI^49911^^P|^PRN^PH^^^906^5136155\r" + 
"ORC|RE||134055895\r" + 
"RXA|0|1|20151022|20151022|20^DTaP^CVX|1.0|||01|||||||||||CP\r" + 
"OBX|1|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|107^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r" + 
"OBX|2|NM|30973-2^Dose number in series^LN|1|1||||||F\r" + 
"ORC|RE||134055896\r" + 
"RXA|0|1|20151228|20151228|48^Hib-PRP-T^CVX|1.0|||00||^^^BUNKER CLINIC||||U5213IX||PMC|||CP\r" + 
"OBX|3|CE|38890-0^COMPONENT VACCINE TYPE^LN|1|17^Hib^CVX^90737^Hib^CPT||||||F\r" + 
"OBX|4|NM|30973-2^Dose number in series^LN|1|1||||||F\r" + 
"ORC|RE||0\r" + 
"RXA|0|1|20151228|20151228|998^No Vaccine Administered^CVX|999\r" + 
"OBX|5|CE|30979-9^Vaccines Due Next^LN|0|107^DTP/aP^CVX^90700^DTP/aP^CPT||||||F\r" + 
"OBX|6|TS|30980-7^Date Vaccine Due^LN|0|20151119||||||F\r" + 
"OBX|7|NM|30973-2^Vaccine due next dose number^LN|0|2||||||F\r" + 
"OBX|8|TS|30981-5^Earliest date to give^LN|0|20151119||||||F\r" + 
"OBX|9|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|0|ACIP schedule||||||F\r" + 
"OBX|10|CE|30979-9^Vaccines Due Next^LN|1|85^HepA^CVX^90730^HepA^CPT||||||F\r" + 
"OBX|11|TS|30980-7^Date Vaccine Due^LN|1|20160622||||||F\r" + 
"OBX|12|NM|30973-2^Vaccine due next dose number^LN|1|1||||||F\r" + 
"OBX|13|TS|30981-5^Earliest date to give^LN|1|20160622||||||F\r" + 
"OBX|14|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|1|ACIP schedule||||||F\r" + 
"OBX|15|CE|30979-9^Vaccines Due Next^LN|2|45^HepB^CVX^90731^HepB^CPT||||||F\r" + 
"OBX|16|TS|30980-7^Date Vaccine Due^LN|2|20150622||||||F\r" + 
"OBX|17|NM|30973-2^Vaccine due next dose number^LN|2|1||||||F\r" + 
"OBX|18|TS|30981-5^Earliest date to give^LN|2|20150622||||||F\r" + 
"OBX|19|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|2|ACIP schedule||||||F\r" + 
"OBX|20|CE|30979-9^Vaccines Due Next^LN|3|17^Hib^CVX^90737^Hib^CPT||||||F\r" + 
"OBX|21|TS|30980-7^Date Vaccine Due^LN|3|20160125||||||F\r" + 
"OBX|22|NM|30973-2^Vaccine due next dose number^LN|3|2||||||F\r" + 
"OBX|23|TS|30981-5^Earliest date to give^LN|3|20160125||||||F\r" + 
"OBX|24|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|3|ACIP schedule||||||F\r" + 
"OBX|25|CE|30979-9^Vaccines Due Next^LN|4|88^Influenza-seasnl^CVX^90724^Influenza-seasnl^CPT||||||F\r" + 
"OBX|26|TS|30980-7^Date Vaccine Due^LN|4|20151222||||||F\r" + 
"OBX|27|NM|30973-2^Vaccine due next dose number^LN|4|1||||||F\r" + 
"OBX|28|TS|30981-5^Earliest date to give^LN|4|20151222||||||F\r" + 
"OBX|29|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|4|ACIP schedule||||||F\r" + 
"OBX|30|CE|30979-9^Vaccines Due Next^LN|5|03^MMR^CVX^90707^MMR^CPT||||||F\r" + 
"OBX|31|TS|30980-7^Date Vaccine Due^LN|5|20160622||||||F\r" + 
"OBX|32|NM|30973-2^Vaccine due next dose number^LN|5|0||||||F\r" + 
"OBX|33|TS|30981-5^Earliest date to give^LN|5|20160622||||||F\r" + 
"OBX|34|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|5|ACIP schedule||||||F\r" + 
"OBX|35|CE|30979-9^Vaccines Due Next^LN|6|100^PneumoConjugate^CVX^90669^PneumoConjugate^CPT||||||F\r" + 
"OBX|36|TS|30980-7^Date Vaccine Due^LN|6|20150822||||||F\r" + 
"OBX|37|NM|30973-2^Vaccine due next dose number^LN|6|1||||||F\r" + 
"OBX|38|TS|30981-5^Earliest date to give^LN|6|20150803||||||F\r" + 
"OBX|39|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|6|ACIP schedule||||||F\r" + 
"OBX|40|CE|30979-9^Vaccines Due Next^LN|7|89^Polio^CVX||||||F\r" + 
"OBX|41|TS|30980-7^Date Vaccine Due^LN|7|20150822||||||F\r" + 
"OBX|42|NM|30973-2^Vaccine due next dose number^LN|7|1||||||F\r" + 
"OBX|43|TS|30981-5^Earliest date to give^LN|7|20150803||||||F\r" + 
"OBX|44|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|7|ACIP schedule||||||F\r" + 
"OBX|45|CE|30979-9^Vaccines Due Next^LN|8|21^Varicella^CVX^90716^Varicella^CPT||||||F\r" + 
"OBX|46|TS|30980-7^Date Vaccine Due^LN|8|20160622||||||F\r" + 
"OBX|47|NM|30973-2^Vaccine due next dose number^LN|8|1||||||F\r" + 
"OBX|48|TS|30981-5^Earliest date to give^LN|8|20160622||||||F\r" + 
"OBX|49|CE|30982-3^Reason applied by forecast logic to project this vaccine^LN|8|ACIP schedule||||||F\r";

  @Test
  public void test() {
    {
      TestCaseMessage tcm = new TestCaseMessage();
      tcm.setActualResponseMessage(RSP_MESSAGE);
      ForecastTesterManager.readForecastActual(tcm);
      List<ForecastActual> forecastActualList = tcm.getForecastActualList();
      assertEquals(14, forecastActualList.size());
      assertEquals("20", forecastActualList.get(0).getVaccineCvx());
      assertEquals("20131121", forecastActualList.get(0).getDueDate());
      assertEquals("133", forecastActualList.get(13).getVaccineCvx());
      assertEquals("20740101", forecastActualList.get(13).getDueDate());
    }
    {
      TestCaseMessage tcm = new TestCaseMessage();
      tcm.setActualResponseMessage(STC1);
      ForecastTesterManager.readForecastActual(tcm);
      List<ForecastActual> forecastActualList = tcm.getForecastActualList();
      assertEquals(9, forecastActualList.size());
      assertEquals("45", forecastActualList.get(0).getVaccineCvx());
      assertEquals("20111229", forecastActualList.get(0).getDueDate());
    }
    {
      TestCaseMessage tcm = new TestCaseMessage();
      tcm.setActualResponseMessage(OR_ALERT1);
      ForecastTesterManager.readForecastActual(tcm);
      List<ForecastActual> forecastActualList = tcm.getForecastActualList();
      assertEquals(9, forecastActualList.size());
      assertEquals("107", forecastActualList.get(0).getVaccineCvx());
      assertEquals("20151119", forecastActualList.get(0).getDueDate());
    }
  }

}
