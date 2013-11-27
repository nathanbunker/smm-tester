package org.immunizationsoftware.dqa.tester.manager.forecast;

public enum ForecastTestPanel {
  
  TCH_INITIAL(2, "TCH Initial"),
  CDSI_TEST_CASES_V1_0(13, "CDSI v1"),
  IHS_ROLLOUT_2013(18, "IHS Rollout"),
  IHS_FROM_RPMS(12, "IHS from RPMS"),
  VDH(15, "VDH")
  ;
  
  private int id = 0;
  private String label = "";
  public int getId() {
    return id;
  }
  public String getLabel() {
    return label;
  }
  private ForecastTestPanel(int id, String label)
  {
    this.id = id;
    this.label = label;
  }
}
