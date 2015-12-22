package org.immunizationsoftware.dqa.tester.certify;

import java.util.Date;

public class CATotal extends CertifyArea
{

  private Date startUpdates = null;
  private Date startQueries = null;

  public void startingUpdates() {
    startUpdates = new Date();
  }

  public void startingQueries() {
    startQueries = new Date();
  }

  public Date estimatedUpdateCompletion() {
    if (areaProgressCount[0] == 0 || startUpdates == null) {
      return null;
    } else if (areaProgress[0] >= 100) {
      return new Date();
    }
    long elapsedTime = System.currentTimeMillis() - startUpdates.getTime();
    long averageTime = elapsedTime / areaProgressCount[0];
    long totalTime = averageTime * areaCount[0];
    return new Date(startUpdates.getTime() + totalTime);
  }

  public Date estimatedQueryCompletion() {
    if (areaProgressCount[1] == 0 || startQueries == null) {
      return null;
    } else if (areaProgress[1] >= 100) {
      return new Date();
    }
    long elapsedTime = System.currentTimeMillis() - startQueries.getTime();
    long averageTime = elapsedTime / areaProgressCount[1];
    long totalTime = averageTime * areaCount[1];
    return new Date(startQueries.getTime() + totalTime);
  }

  public CATotal(CertifyRunner certifyRunner) {
    super("", "Total", certifyRunner);
    this.run = true;
    this.performanceConformance = true;
  }

  @Override
  public void prepareUpdates() {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendUpdates() {
    // TODO Auto-generated method stub

  }

  @Override
  public void prepareQueries() {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendQueries() {
    // TODO Auto-generated method stub

  }

}
