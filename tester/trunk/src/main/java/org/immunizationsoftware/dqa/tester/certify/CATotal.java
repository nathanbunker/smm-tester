package org.immunizationsoftware.dqa.tester.certify;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CATotal extends CertifyArea {

  private Date startUpdates = null;
  private Date startQueries = null;

  public void startingUpdates() {
    startUpdates = new Date();
  }

  public void startingQueries() {
    startQueries = new Date();
  }

  public static class ETC {
    private Date date = null;
    private long elapsedTime;
    private long averageTime;
    private long totalTime;

    public Date getDate() {
      return date;
    }

    public long getElapsedTime() {
      return elapsedTime;
    }

    public long getAverageTime() {
      return averageTime;
    }

    public long getTotalTime() {
      return totalTime;
    }

    public ETC() {
      date = null;
    }

    public ETC(int areaProgressCount, int areaProgress, int areaCount, Date startDate) {
      if (areaProgressCount == 0 || startDate == null) {
        return;
      } else if (areaProgress >= 100) {
        date = new Date();
      }
      elapsedTime = System.currentTimeMillis() - startDate.getTime();
      averageTime = elapsedTime / areaProgressCount;
      totalTime = averageTime * areaCount;
      date = new Date(startDate.getTime() + totalTime);
    }

    @Override
    public String toString() {
      if (date != null) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
        return " - ETC " + timeFormat.format(date) + " - average " + averageTime + "ms";
      }
      return "";
    }
  }

  public ETC estimatedUpdateCompletion() {
    return new ETC(areaProgressCount[0], areaProgress[0], areaCount[0], startUpdates);
  }

  public ETC estimatedQueryCompletion() {
    return new ETC(areaProgressCount[1], areaProgress[1], areaCount[1], startQueries);
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
