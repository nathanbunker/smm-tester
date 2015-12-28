package org.immunizationsoftware.dqa.tester.certify;

import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.transform.forecast.ForecastTestPanel;

public class CAForecasterEngaged extends CAForecast
{

  public CAForecasterEngaged(CertifyRunner certifyRunner) {
    super("Q", VALUE_TEST_SECTION_TYPE_FORECASTER_ENGAGED, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    List<ForecastTestPanel> ftpList = new ArrayList<ForecastTestPanel>();
    ftpList.add(ForecastTestPanel.AIRA_INTEROP_TESTING);
    prepareForecastUpdates(ftpList);
  }

}
