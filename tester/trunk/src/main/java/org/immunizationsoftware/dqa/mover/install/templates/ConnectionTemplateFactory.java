package org.immunizationsoftware.dqa.mover.install.templates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionTemplateFactory
{
  private static Map<String, ConnectionTemplate> connectionTemplateMap = null;

  protected static void register(String name, ConnectionTemplate connectionTemplate) {
    connectionTemplateMap.put(name, connectionTemplate);
  }

  private static void init() {
    if (connectionTemplateMap == null) {
      connectionTemplateMap = new HashMap<String, ConnectionTemplate>();
      new DefaultTemplate();
      new AK_VACTRAK_Template();
      new AL_IMMPRINT_Template();
      new AR_WEBIZ_Template();
      new AZ_ASIIS_Template();
      new CA_CAIR_Template();
      new CA_SDIR_Template();
      new CO_CIIS_Template();
      new DE_DELVAX_Template();
      new GA_GRITS_Template();
      new IA_IRIS_Template();
      new ID_IRIS_Template();
      new IL_ICARE_Template();
      new IN_CHIRP_Template();
      new KS_WEBIZ_Template();
      new LA_LINKS_Template();
      new MA_MIIS_Template();
      new MD_IMMUNET_Template();
      new ME_PHCHUB_Template();
      new MN_MIIC_Template();
      new MO_SHOWMEVAX_Template();
      new MS_MIX_Template();
      new MT_IMMTRAX_Template();
      new NC_NCIR_Template();
      new ND_NDIIS_Template();
      new NE_SIIS_Template();
      new NJ_NJSIIS_Template();
      new NM_SIIS_RAW_UAT();
      new NV_WEBIZ_Template();
      new NY_CIR_Template();
      new OR_ALERT_Template();
      new PA_PHIL_Template();
      new PA_SIIS_Template();
      new RI_KIDSNET_Template();
      new SC_SCI_Template();
      new SD_SDIIS_Template();
      new UT_USIIS_Template();
      new VA_VIIS_Template();
      new WA_IIS_Template();
      new WI_WIR_Template();
      new WY_WYIR_Template();
    }
  }

  public static ConnectionTemplate getConnectionTemplate(String name) {
    init();
    return connectionTemplateMap.get(name);
  }

  public static List<String> getConnectionTemplateNames() {
    init();
    ArrayList<String> connectionTemplateNamesList = new ArrayList<String>(connectionTemplateMap.keySet());
    Collections.sort(connectionTemplateNamesList);
    return connectionTemplateNamesList;
  }
}
