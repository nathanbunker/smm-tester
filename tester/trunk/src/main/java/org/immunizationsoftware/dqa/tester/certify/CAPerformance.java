package org.immunizationsoftware.dqa.tester.certify;

import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class CAPerformance extends CertifyArea
{

  private int passUpdateCount = 0;
  private int passQueryCount = 0;
  private long totalQueryTime = 0;
  private long totalUpdateTime = 0;
  private int totalQueryCount = 0;
  private int totalUpdateCount = 0;
  private long minQueryTime = Long.MAX_VALUE;
  private long minUpdateTime = Long.MAX_VALUE;
  private long maxQueryTime = 0;
  private long maxUpdateTime = 0;
  private TestCaseMessage minQueryTestCase = null;
  private TestCaseMessage minUpdateTestCase = null;
  private TestCaseMessage maxQueryTestCase = null;
  private TestCaseMessage maxUpdateTestCase = null;
  private List<Long> queryTimeList = new ArrayList<Long>();
  private List<Long> updateTimeList = new ArrayList<Long>();

  public long getTotalQueryTime() {
    return totalQueryTime;
  }

  public long getTotalUpdateTime() {
    return totalUpdateTime;
  }

  public double getQuerySDev() {
    if (queryTimeList.size() == 0) {
      return 0;
    }
    long averageQuery = getQueryAverage();
    double variance = 0;
    for (Long queryTime : queryTimeList) {
      double v = Math.pow(queryTime - averageQuery, 2);
      variance += v;
    }
    return Math.sqrt(variance / queryTimeList.size());
  }

  public long getQueryAverage() {
    long averageQuery = totalQueryTime / totalQueryCount;
    return averageQuery;
  }

  public double getUpdateSDev() {
    if (updateTimeList.size() == 0) {
      return 0;
    }
    long averageUpdate = getUpdateAverage();
    double variance = 0;
    for (Long updateTime : updateTimeList) {
      double v = Math.pow(updateTime - averageUpdate, 2);
      variance += v;
    }
    return Math.sqrt(variance / updateTimeList.size());
  }

  public long getUpdateAverage() {
    long averageUpdate = totalUpdateTime / totalUpdateCount;
    return averageUpdate;
  }

  public void addTotalQueryTime(long queryTime, TestCaseMessage tcm) {
    queryTimeList.add(queryTime);
    totalQueryTime += queryTime;
    totalQueryCount++;
    if (queryTime < minQueryTime) {
      minQueryTime = queryTime;
      minQueryTestCase = tcm;
    }
    if (queryTime > maxQueryTime) {
      maxQueryTime = queryTime;
      maxQueryTestCase = tcm;
    }
    if (queryTime < 3000)
    {
      passQueryCount++;
    }

  }

  public void addTotalUpdateTime(long updateTime, TestCaseMessage tcm) {
    updateTimeList.add(updateTime);
    totalUpdateTime += updateTime;
    totalUpdateCount++;
    if (updateTime < minUpdateTime) {
      minUpdateTime = updateTime;
      minUpdateTestCase = tcm;
    }
    if (updateTime > maxUpdateTime) {
      maxUpdateTime = updateTime;
      maxUpdateTestCase = tcm;
    }
    if (updateTime < 3000)
    {
      passUpdateCount++;
    }
  }

  public long getMinQueryTime() {
    return minQueryTime;
  }

  public long getMinUpdateTime() {
    return minUpdateTime;
  }

  public long getMaxQueryTime() {
    return maxQueryTime;
  }

  public long getMaxUpdateTime() {
    return maxUpdateTime;
  }

  public TestCaseMessage getMinQueryTestCase() {
    return minQueryTestCase;
  }

  public TestCaseMessage getMinUpdateTestCase() {
    return minUpdateTestCase;
  }

  public TestCaseMessage getMaxQueryTestCase() {
    return maxQueryTestCase;
  }

  public TestCaseMessage getMaxUpdateTestCase() {
    return maxUpdateTestCase;
  }

  public int getTotalQueryCount() {
    return totalQueryCount;
  }

  public int getTotalUpdateCount() {
    return totalUpdateCount;
  }
  
  public CAPerformance(CertifyRunner certifyRunner) {
    super("G", VALUE_TEST_SECTION_TYPE_PERFORMANCE, certifyRunner);
    performanceConformance = true;
  }

  @Override
  public void prepareUpdates() {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendUpdates() {
    if (totalUpdateCount > 0) {
      areaCount[0] = totalUpdateCount;
      areaProgressCount[0] = areaCount[0];
      areaScore[0] = makeScore(passUpdateCount, totalUpdateCount);
      areaProgress[0] = 100;
    }
  }

  @Override
  public void prepareQueries() {
    // TODO Auto-generated method stub

  }

  @Override
  public void sendQueries() {
    if (totalQueryCount > 0) {
      areaCount[1] = totalQueryCount;
      areaProgressCount[1] = areaCount[1];
      areaScore[1] = makeScore(passQueryCount, totalQueryCount);
      areaProgress[1] = 100;
    }
  }

}
