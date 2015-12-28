package org.immunizationsoftware.dqa.tester.manager.forecast;

import org.immunizationsoftware.dqa.transform.forecast.ForecastExpected;

public class ForecastActual extends ForecastExpected
{
  private String scheduleName = "";
  private String seriesName = "";
  private String seriesDoseCount = "";
  private String seriesStatus = "";
  private String reasonCode = "";

  public String getScheduleName() {
    return scheduleName;
  }

  public void setScheduleName(String scheduleName) {
    this.scheduleName = scheduleName;
  }

  public String getSeriesName() {
    return seriesName;
  }

  public void setSeriesName(String seriesName) {
    this.seriesName = seriesName;
  }

  public String getSeriesDoseCount() {
    return seriesDoseCount;
  }

  public void setSeriesDoseCount(String seriesDoseCount) {
    this.seriesDoseCount = seriesDoseCount;
  }

  public String getSeriesStatus() {
    return seriesStatus;
  }

  public void setSeriesStatus(String seriesStatus) {
    this.seriesStatus = seriesStatus;
  }

  public String getReasonCode() {
    return reasonCode;
  }

  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }

}
