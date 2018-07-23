package org.immregistries.smm.tester.manager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CompareManagerTester
{

  private static final String BASE_ACK = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF1 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|E||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF2 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AE|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF3 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AE|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|E||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF4 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1.022.Fq|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String DIFF5 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1.022.Fq|\r"
      + "ERR|||10^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";
  private static final String SAME1 = "MSH|^~\\&|||||20151203133132||ACK^V04^ACK|2TRF-C.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems found in message but this interface does not save or forward the data|\r";

  private static final String SAME2 = "MSH|^~\\&|||||2015125133132||ACK^V04^ACK|2TRF-5.1.022.Fq|P|2.5.1|\r"
      + "MSA|AA|2TRF-C.1|\r"
      + "ERR|||0^Message accepted^HL70357|W||||No problems fosdfage but this interface does not save or forward the data|\r";

  @Test
  public void test() {
    assertTrue(CompareManager.acksAppearToBeTheSame(BASE_ACK, BASE_ACK));
    assertTrue(CompareManager.acksAppearToBeTheSame(BASE_ACK, SAME1));
    assertTrue(CompareManager.acksAppearToBeTheSame(BASE_ACK, SAME2));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF1));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF2));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF3));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF4));
    assertFalse(CompareManager.acksAppearToBeTheSame(BASE_ACK, DIFF5));
  }
  
  private static final String VXU = "MSH|^~\\&|||||20151217144717-0700||VXU^V04^VXU_V04|3uTl-A.01.01.1Xb|P|2.5.1|||||||||||\r" +
"PID|||3uTl-A.01.01^^^AIRA-TEST^MR||Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M||2131-1^Other Race^HL70005|356 Trimble Ln^^Tekonsha^MI^49092^USA^P||^PRN^PH^^^517^9212381|||||||||2186-5^not Hispanic or Latino^HL70005|\r" +
"PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20151217|20151217|\r" +
"NK1|1|Chambers^Elise^^^^^L|MTH^Mother^HL70063|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381|\r" +
"ORC|RE||J84D479.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r" +
"RXA|0|1|20151217||03^MMR^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||P3400YX||MSD^Merck and Co^MVX||||A|\r" +
"RXR|SC^^HL70162|LA^^HL70163|\r" +
"OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V03^VFC eligible - Uninsured^HL70064||||||F|||20151217|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r" +
"OBX|2|CE|30956-7^Vaccine Type^LN|2|03^MMR^CVX||||||F|\r" +
"OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120420||||||F|\r" +
"OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20151217||||||F|\r";

  private static final String RSP1 = "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20151217||RSP^K11^RSP_K11|1450391207965.3|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r" +
"MSA|AA|1450391207965.3||0||0^Message Accepted^HL70357\r" +
"QAK|1450391207965.3|OK|Z34\r" +
"QPD|Z34^Request Immunization History^HL70471|1450391207965.3|3uTl-A.01.01^^^AIRA-TEST^MR|Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381\r" +
"PID|1||7237424^^^ORA^SR~3uTl-A.01.01^^^ORA^MR||CHAMBERS^MAXIM^M^^^^L|JEFFERSON^ELISE|20111211|M||2131-1|356 TRIMBLE LN^^TEKONSHA^MI^49092^^H||^PRN^PH^^^517^9212381|||||||||2186-5||N|0\r" +
"PD1|||||||||||02|N||||A\r" +
"NK1|1|CHAMBERS^ELISE|MTH|356 TRIMBLE LN^^TEKONSHA^MI^49092^^P|^PRN^PH^^^517^9212381\r" +
"ORC|RE||134037533\r" +
"RXA|0|1|20151217|20151217|03^MMR^CVX|1.0|||00||^^^BUNKER CLINIC||||P3400YX||MSD|||CP\r" +
"RXR|SC|LA\r";
  
  private static final String RSP2 = "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20151217||RSP^K11^RSP_K11|1450391207965.3|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r" +
"MSA|AA|1450391207965.3||0||0^Message Accepted^HL70357\r" +
"QAK|1450391207965.3|OK|Z34\r" +
"QPD|Z34^Request Immunization History^HL70471|1450391207965.3|3uTl-A.01.01^^^AIRA-TEST^MR|Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381\r" +
"PID|1||7237424^^^ORA^SR~3uTl-A.01.01^^^ORA^MR||CHAMBERS^MAXIM^M^^^^L|JEFFERSON^ELISE|20111211|M||2131-1|356 TRIMBLE LN^^TEKONSHA^MI^49092^^H||^PRN^PH^^^517^9212381|||||||||2186-5||N|0\r" +
"PD1|||||||||||02|N||||A\r" +
"NK1|1|CHAMBERS^ELISE|MTH|356 TRIMBLE LN^^TEKONSHA^MI^49092^^P|^PRN^PH^^^517^9212381\r";

  private static final String RSP3 = "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20151217||RSP^K11^RSP_K11|1450391207965.3|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r" +
"MSA|AA|1450391207965.3||0||0^Message Accepted^HL70357\r" +
"QAK|1450391207965.3|OK|Z34\r" +
"QPD|Z34^Request Immunization History^HL70471|1450391207965.3|3uTl-A.01.01^^^AIRA-TEST^MR|Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381\r" +
"PID|1||7237424^^^ORA^SR~3uTl-A.01.01^^^ORA^MR||CHAMBERS^MAXIM^M^^^^L|JEFFERSON^ELISE|20111211|M||2131-1|356 TRIMBLE LN^^TEKONSHA^MI^49092^^H||^PRN^PH^^^517^9212381|||||||||2186-5||N|0\r" +
"PD1|||||||||||02|N||||A\r" +
"NK1|1|CHAMBERS^ELISE|MTH|356 TRIMBLE LN^^TEKONSHA^MI^49092^^P|^PRN^PH^^^517^9212381\r" +
"ORC|RE||134037533\r" +
"RXA|0|1|20151217|20151217|140^MMR^CVX|1.0|||00||^^^BUNKER CLINIC||||P3400YX||MSD|||CP\r" +
"RXR|SC|LA\r";
  private static final String RSP4 = "MSH|^~\\&|ALERT IIS|ALERT IIS||AL9997|20151217||RSP^K11^RSP_K11|1450391207965.3|P|2.5.1|||||||||Z32^CDCPHINVS|ALERT IIS|AL9997\r" +
"MSA|AA|1450391207965.3||0||0^Message Accepted^HL70357\r" +
"QAK|1450391207965.3|OK|Z34\r" +
"QPD|Z34^Request Immunization History^HL70471|1450391207965.3|3uTl-A.01.01^^^AIRA-TEST^MR|Chambers^Maxim^M^^^^L|Jefferson^Elise|20111211|M|356 Trimble Ln^^Tekonsha^MI^49092^USA^P|^PRN^PH^^^517^9212381\r" +
"PID|1||7237424^^^ORA^SR~3uTl-A.01.01^^^ORA^MR||CHAMBERS^EDGAR^M^^^^L|JEFFERSON^ELISE|20111211|M||2131-1|356 TRIMBLE LN^^TEKONSHA^MI^49092^^H||^PRN^PH^^^517^9212381|||||||||2186-5||N|0\r" +
"PD1|||||||||||02|N||||A\r" +
"NK1|1|CHAMBERS^ELISE|MTH|356 TRIMBLE LN^^TEKONSHA^MI^49092^^P|^PRN^PH^^^517^9212381\r" +
"ORC|RE||134037533\r" +
"RXA|0|1|20151217|20151217|03^MMR^CVX|1.0|||00||^^^BUNKER CLINIC||||P3400YX||MSD|||CP\r" +
"RXR|SC|LA\r";
  
  private static final String VXU_NDC = "MSH|^~\\&|1270|9250|AL-IIS|AL-IIS|20180627073733.994-0500|43781BF5-16FE-440E-8955-CD4CD4538422|VXU^V04^VXU_V04|Qq8-GM-1.1|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\n" + 
"PID|1||A84S31219^^^NIST-MPI-1^MR||Clay^Conor^Nevada^^^^L|Pottawatomie^^^^^^M|20180418|M||1002-5^American Indian or Alaska Native^CDCREC|370 Gartenlaub Ln^^Chassell^MI^49916^USA^P||^PRN^PH^^^906^8460327|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|1|||||N\n" + 
"PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20180627|||A|20180418|20180627\n" + 
"NK1|1|Clay^Pottawatomie^Elizabeth^^^^L|MTH^Mother^HL70063|370 Gartenlaub Ln^^Chassell^MI^49916^USA^P|^PRN^PH^^^906^8460327\n" + 
"NK1|2|Clay^Clallam^William^^^^L|FTH^Father^HL70063|370 Gartenlaub Ln^^Chassell^MI^49916^USA^P|^PRN^CP^^^906^8460327\n" + 
"ORC|RE|AA84S31219.1^NIST-AA-IZ-2|BA84S31219.1^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||1164407656^Thomas^Wilma^Elizabeth^^^^^NPI^L^^^MD||||||\n" + 
"RXA|0|1|20180627||49281-0560-05^Pentacel^NDC|0.5|mL^mL^UCUM||00^New Record^NIP001|7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|^^^NIST-Clinic-1||||526434|20180725|PMC^Sanofi Pasteur^MVX|||CP|A\n" + 
"RXR|C28161^Intramuscular^NCIT|RT^Right Thigh^HL70163\n" + 
"OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|VXC50^Public^CDCPHINVS||||||F|||20180627\n" + 
"OBX|2|CE|64994-7^Vaccine Funding Program Eligibility^LN|2|V04^VFC Eligible - American Indian/Alaska Native^HL70064||||||F|||20180627|||VXC40^per immunization^CDCPHINVS\n" + 
"OBX|3|CE|69764-9^Document Type^LN|3|253088698300017211160720^Polio VIS^cdcgs1vis||||||F|||20180627\n" + 
"OBX|4|DT|29769-7^Date Vis Presented^LN|3|20180627||||||F|||20180627\n" + 
"OBX|5|CE|69764-9^Document Type^LN|4|253088698300006611150402^Haemophilus Influenzae type b VIS^cdcgs1vis||||||F|||20180627\n" + 
"OBX|6|DT|29769-7^Date Vis Presented^LN|4|20180627||||||F|||20180627\n" + 
"OBX|7|CE|69764-9^Document Type^LN|5|253088698300003511070517^Diphtheria/Tetanus/Pertussis (DTaP) VIS^cdcgs1vis||||||F|||20180627\n" + 
"OBX|8|DT|29769-7^Date Vis Presented^LN|5|20180627||||||F|||20180627\n" + 
"ORC|RE|AA84S31219.2^NIST-AA-IZ-2|BA84S31219.2^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||1164407656^Thomas^Wilma^Elizabeth^^^^^NPI^L^^^MD|||||^^|\n" + 
"RXA|0|1|20180627||00006-4047-20^RotaTeq^NDC|2.0|mL^mL^UCUM||00^New Record^NIP001|7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|^^^NIST-Clinic-1||||297961|20180919|MSD^Merck and Co., Inc.^MVX|||CP|A\n" + 
"RXR|C38288^Oral^NCIT\n" + 
"OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|VXC50^Public^CDCPHINVS||||||F|||20180627\n" + 
"OBX|2|CE|64994-7^Vaccine Funding Program Eligibility^LN|2|V04^VFC Eligible - American Indian/Alaska Native^HL70064||||||F|||20180627|||VXC40^per immunization^CDCPHINVS\n" + 
"OBX|3|CE|69764-9^Document Type^LN|3|253088698300019611150415^Rotavirus VIS^cdcgs1vis||||||F|||20180627\n" + 
"OBX|4|DT|29769-7^Date Vis Presented^LN|3|20180627||||||F|||20180627\n" + 
"ORC|RE|AA84S31219.3^NIST-AA-IZ-2|BA84S31219.3^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||1164407656^Thomas^Wilma^Elizabeth^^^^^NPI^L^^^MD||||||\n" + 
"RXA|0|1|20180627||00005-1971-01^Prevnar 13^NDC|0.5|mL^mL^UCUM||00^New Record^NIP001|7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|^^^NIST-Clinic-1||||353480|20180801|PFR^Pfizer, Inc^MVX|||CP|A\n" + 
"RXR|C28161^Intramuscular^NCIT|LT^Left Thigh^HL70163\n" + 
"OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|VXC50^Public^CDCPHINVS||||||F|||20180627\n" + 
"OBX|2|CE|64994-7^Vaccine Funding Program Eligibility^LN|2|V04^VFC Eligible - American Indian/Alaska Native^HL70064||||||F|||20180627|||VXC40^per immunization^CDCPHINVS\n" + 
"OBX|3|CE|69764-9^Document Type^LN|3|253088698300015811151105^Pneumococcal Conjugate (PCV13) VIS^cdcgs1vis||||||F|||20180627\n" + 
"OBX|4|DT|29769-7^Date Vis Presented^LN|3|20180627||||||F|||20180627\n" + 
"ORC|RE||BA84S31219.4^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||1164407656^^^^^^^^NPI||||||\n" + 
"RXA|0|1|20180419||45^Hep B, unspecified formulation^CVX|999|||01^Historical Administration^NIP001|||||||||||CP|A\n" + 
"ORC|RE||BA84S31219.5^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||1164407656^^^^^^^^NPI||||||\n" + 
"RXA|0|1|20180518||45^Hep B, unspecified formulation^CVX|999|||01^Historical Administration^NIP001|||||||||||CP|A";

  private static final String RSP_NDC = "MSH|^~\\&|AL-IIS|AL-IIS|1270|9250|20180628002106-0600||RSP^K11^RSP_K11|20180628002106|P|2.5.1|||NE|NE|||||Z42^CDCPHINVS\n" +
"MSA|AA|Qq8-GM-1.1-Q\n" +
"QAK|1530160201548.2426|OK|Z44^Request Evaluated History and Forecast^CDCPHINVS\n" +
"QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|1530160201548.2426|A84S31219^^^NIST-MPI-1^MR|Clay^Conor^Nevada^^^^L|Pottawatomie^^^^^^M|20180418|M|370 Gartenlaub Ln^^Chassell^MI^49916^USA^P|^PRN^PH^^^906^8460327|N|1|||\n" +
"PID|1||A84S31219^^^NIST-MPI-1^MR~14159763^^^ALA^SR||CLAY^CONOR^^^^^U||20180418|M|||\n" +
"ORC|RE||20180628^AL-IIS|\n" +
"RXA|0|1|20180627|20180627|120^DTAP-HIB-IPV^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\n" +
"OBX|1|CE|30956-7^vaccine type^LN|1|10^IPV^CVX||||||F|\n" +
"OBX|2|NM|30973-2^Dose number in series^LN|1|1|NA^NA^HL70353|||||F|\n" +
"OBX|3|ID|59781-5^Dose Validity^LN|1|Y||||||F|\n" +
"OBX|4|CE|30956-7^vaccine type^LN|2|20^DTAP^CVX||||||F|\n" +
"OBX|5|NM|30973-2^Dose number in series^LN|2|1|NA^NA^HL70353|||||F|\n" +
"OBX|6|ID|59781-5^Dose Validity^LN|2|Y||||||F|\n" +
"OBX|7|CE|30956-7^vaccine type^LN|3|48^HIB (PRP-T)^CVX||||||F|\n" +
"OBX|8|NM|30973-2^Dose number in series^LN|3|1|NA^NA^HL70353|||||F|\n" +
"OBX|9|ID|59781-5^Dose Validity^LN|3|Y||||||F|\n" +
"ORC|RE||20180628^AL-IIS|\n" +
"RXA|0|1|20180419|20180419|45^HEP B, UNSPECIFIED FORMULATION^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\n" +
"OBX|1|CE|30956-7^vaccine type^LN|1|45^HEP B, UNSPECIFIED FORMULATION^CVX||||||F|\n" +
"OBX|2|NM|30973-2^Dose number in series^LN|1|1|NA^NA^HL70353|||||F|\n" +
"OBX|3|ID|59781-5^Dose Validity^LN|1|Y||||||F|\n" +
"ORC|RE||20180628^AL-IIS|\n" +
"RXA|0|1|20180518|20180518|45^HEP B, UNSPECIFIED FORMULATION^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\n" +
"OBX|1|CE|30956-7^vaccine type^LN|1|45^HEP B, UNSPECIFIED FORMULATION^CVX||||||F|\n" +
"OBX|2|NM|30973-2^Dose number in series^LN|1|2|NA^NA^HL70353|||||F|\n" +
"OBX|3|ID|59781-5^Dose Validity^LN|1|Y||||||F|\n" +
"ORC|RE||20180628^AL-IIS|\n" +
"RXA|0|1|20180627|20180627|133^PNEUMOCOCCAL CONJUGATE PCV 13^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\n" +
"OBX|1|CE|30956-7^vaccine type^LN|1|133^PNEUMOCOCCAL CONJUGATE PCV 13^CVX||||||F|\n" +
"OBX|2|NM|30973-2^Dose number in series^LN|1|1|NA^NA^HL70353|||||F|\n" +
"OBX|3|ID|59781-5^Dose Validity^LN|1|Y||||||F|\n" +
"ORC|RE||20180628^AL-IIS|\n" +
"RXA|0|1|20180627|20180627|116^ROTAVIRUS, PENTAVALENT^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\n" +
"OBX|1|CE|30956-7^vaccine type^LN|1|116^ROTAVIRUS, PENTAVALENT^CVX||||||F|\n" +
"OBX|2|NM|30973-2^Dose number in series^LN|1|1|NA^NA^HL70353|||||F|\n" +
"OBX|3|ID|59781-5^Dose Validity^LN|1|Y||||||F|\n" +
"ORC|RE||9999^AL-IIS|\n" +
"RXA|0|1|20180628|20180628|998^No vaccine administered^CVX|999||||||||||||||NA|A|\n" +
"OBX|1|CE|30979-9^Vaccine due next^LN|1|89^POLIO, UNSPECIFIED FORMULATION^CVX||||||F|||20180628|\n" +
"OBX|2|DT|30980-7^Date vaccine due^LN|1|20180818||||||F|||20180628|\n" +
"OBX|3|NM|30973-2^Vaccine due next dose number^LN|1|2|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|4|CE|59783-1^Status in immunization series^LN|1|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|5|CE|59779-9^Immunization Schedule Used^LN|1|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|6|CE|30979-9^Vaccine due next^LN|2|107^DTAP UNSPECIFIED FORMULATION^CVX||||||F|||20180628|\n" +
"OBX|7|DT|30980-7^Date vaccine due^LN|2|20180818||||||F|||20180628|\n" +
"OBX|8|NM|30973-2^Vaccine due next dose number^LN|2|2|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|9|CE|59783-1^Status in immunization series^LN|2|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|10|CE|59779-9^Immunization Schedule Used^LN|2|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|11|CE|30979-9^Vaccine due next^LN|3|152^PNEUMOCOCCAL CONJUGATE, UNSPECIFIED FORMULATION^CVX||||||F|||20180628|\n" +
"OBX|12|DT|30980-7^Date vaccine due^LN|3|20180818||||||F|||20180628|\n" +
"OBX|13|NM|30973-2^Vaccine due next dose number^LN|3|2|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|14|CE|59783-1^Status in immunization series^LN|3|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|15|CE|59779-9^Immunization Schedule Used^LN|3|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|16|CE|30979-9^Vaccine due next^LN|4|17^HIB, UNSPECIFIED FORMULATION^CVX||||||F|||20180628|\n" +
"OBX|17|DT|30980-7^Date vaccine due^LN|4|20180818||||||F|||20180628|\n" +
"OBX|18|NM|30973-2^Vaccine due next dose number^LN|4|2|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|19|CE|59783-1^Status in immunization series^LN|4|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|20|CE|59779-9^Immunization Schedule Used^LN|4|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|21|CE|30979-9^Vaccine due next^LN|5|122^ROTAVIRUS, UNSPECIFIED FORMULATION^CVX||||||F|||20180628|\n" +
"OBX|22|DT|30980-7^Date vaccine due^LN|5|20180818||||||F|||20180628|\n" +
"OBX|23|NM|30973-2^Vaccine due next dose number^LN|5|2|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|24|CE|59783-1^Status in immunization series^LN|5|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|25|CE|59779-9^Immunization Schedule Used^LN|5|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|26|CE|30979-9^Vaccine due next^LN|6|45^HEP B, UNSPECIFIED FORMULATION^CVX||||||F|||20180628|\n" +
"OBX|27|DT|30980-7^Date vaccine due^LN|6|20181018||||||F|||20180628|\n" +
"OBX|28|NM|30973-2^Vaccine due next dose number^LN|6|3|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|29|CE|59783-1^Status in immunization series^LN|6|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|30|CE|59779-9^Immunization Schedule Used^LN|6|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|31|CE|30979-9^Vaccine due next^LN|7|03^MMR^CVX||||||F|||20180628|\n" +
"OBX|32|DT|30980-7^Date vaccine due^LN|7|20190418||||||F|||20180628|\n" +
"OBX|33|NM|30973-2^Vaccine due next dose number^LN|7|1|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|34|CE|59783-1^Status in immunization series^LN|7|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|35|CE|59779-9^Immunization Schedule Used^LN|7|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|36|CE|30979-9^Vaccine due next^LN|8|31^HEP A, PEDIATRIC, UNSPECIFIED FORMULATION^CVX||||||F|||20180628|\n" +
"OBX|37|DT|30980-7^Date vaccine due^LN|8|20190418||||||F|||20180628|\n" +
"OBX|38|NM|30973-2^Vaccine due next dose number^LN|8|1|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|39|CE|59783-1^Status in immunization series^LN|8|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|40|CE|59779-9^Immunization Schedule Used^LN|8|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|41|CE|30979-9^Vaccine due next^LN|9|21^VARICELLA^CVX||||||F|||20180628|\n" +
"OBX|42|DT|30980-7^Date vaccine due^LN|9|20190418||||||F|||20180628|\n" +
"OBX|43|NM|30973-2^Vaccine due next dose number^LN|9|1|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|44|CE|59783-1^Status in immunization series^LN|9|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|45|CE|59779-9^Immunization Schedule Used^LN|9|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|46|CE|30979-9^Vaccine due next^LN|10|114^MENINGOCOCCAL MCV4P^CVX||||||F|||20180628|\n" +
"OBX|47|DT|30980-7^Date vaccine due^LN|10|20290418||||||F|||20180628|\n" +
"OBX|48|NM|30973-2^Vaccine due next dose number^LN|10|1|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|49|CE|59783-1^Status in immunization series^LN|10|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|50|CE|59779-9^Immunization Schedule Used^LN|10|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|51|CE|30979-9^Vaccine due next^LN|11|137^HPV, UNSPECIFIED FORMULATION^CVX||||||F|||20180628|\n" +
"OBX|52|DT|30980-7^Date vaccine due^LN|11|20290418||||||F|||20180628|\n" +
"OBX|53|NM|30973-2^Vaccine due next dose number^LN|11|1|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|54|CE|59783-1^Status in immunization series^LN|11|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|55|CE|59779-9^Immunization Schedule Used^LN|11|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n" +
"OBX|56|CE|30979-9^Vaccine due next^LN|12|33^PNEUMOCOCCAL POLYSACCHARIDE PPV23^CVX||||||F|||20180628|\n" +
"OBX|57|DT|30980-7^Date vaccine due^LN|12|20830418||||||F|||20180628|\n" +
"OBX|58|NM|30973-2^Vaccine due next dose number^LN|12|1|NA^NA^HL70353|||||F|||20180628|\n" +
"OBX|59|CE|59783-1^Status in immunization series^LN|12|LA13422-3^On schedule^LN||||||F|\n" +
"OBX|60|CE|59779-9^Immunization Schedule Used^LN|12|VXC16^ACIP^CDCPHINVS||||||F|||20180628|\n";
  
  @Test
  public void testQueryReturnedMostImportantData()
  {
    assertTrue(CompareManager.queryReturnedMostImportantData(VXU, RSP1));
    assertFalse(CompareManager.queryReturnedMostImportantData(VXU, RSP2));
    assertFalse(CompareManager.queryReturnedMostImportantData(VXU, RSP3));
    assertFalse(CompareManager.queryReturnedMostImportantData(VXU, RSP4));
  }
  
  @Test
  public void testQueryReturnedMostImportantDataForNDC()
  {
    assertTrue(CompareManager.queryReturnedMostImportantData(VXU_NDC, RSP_NDC));
  }
}
