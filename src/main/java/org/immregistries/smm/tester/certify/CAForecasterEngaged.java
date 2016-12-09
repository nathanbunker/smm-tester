package org.immregistries.smm.tester.certify;

import java.util.ArrayList;
import java.util.List;

import org.immregistries.smm.transform.forecast.ForecastTestPanel;

public class CAForecasterEngaged extends CAForecast {

  public CAForecasterEngaged(CertifyRunner certifyRunner) {
    super("Q", VALUE_TEST_SECTION_TYPE_FORECASTER_ENGAGED, certifyRunner);
  }

  @Override
  public void prepareUpdates() {
    List<ForecastTestPanel> ftpList = new ArrayList<ForecastTestPanel>();
    if (CAForecast.TEST_AGAINST_PRODUCTION) {
      ftpList.add(ForecastTestPanel.AIRA_INTEROP_TESTING);
    } else {
      ftpList.add(ForecastTestPanel.LOCAL_DEV_TESTING);
    }
    prepareForecastUpdates(ftpList);
  }

}
