package org.immregistries.smm.tester.certify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immregistries.smm.RecordServletInterface;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.forecast.ForecastTestPanel;

public abstract class CertifyArea implements RecordServletInterface {
  protected List<TestCaseMessage> updateList = new ArrayList<TestCaseMessage>();
  protected List<TestCaseMessage> queryList = new ArrayList<TestCaseMessage>();
  protected boolean run = false;
  protected int[] areaScore = new int[3];
  protected int[] areaProgressCount = new int[3];
  protected int[] areaProgress = new int[3];
  protected int[] areaCount = new int[3];
  protected String areaLabel = "";
  protected String areaLetter = "";
  protected boolean performanceConformance = false;
  protected Map<String, TestCaseMessage> updateMap = new HashMap<String, TestCaseMessage>();
  protected boolean doSafeQuery = true;

 
  protected List<ForecastTestPanel> forecastTestPanelList = new ArrayList<ForecastTestPanel>();

  public int[] getAreaProgressCount() {
    return areaProgressCount;
  }

  public void setAreaProgressCount(int[] areaProgressCount) {
    this.areaProgressCount = areaProgressCount;
  }

  public boolean isPerformanceConformance() {
    return performanceConformance;
  }

  protected static enum PassCriteria {
    PASS_AND_NO_MAJOR_CHANGES, PASS_ONLY, NO_ACK_CHANGES
  }

  public boolean isRun() {
    return run;
  }

  public void setRun(boolean run) {
    this.run = run;
  }

  public int[] getAreaScore() {
    return areaScore;
  }

  public void setAreaScore(int[] areaScore) {
    this.areaScore = areaScore;
  }

  public int[] getAreaProgress() {
    return areaProgress;
  }

  public void setAreaProgress(int[] areaProgress) {
    this.areaProgress = areaProgress;
  }

  public int[] getAreaCount() {
    return areaCount;
  }

  public void setAreaCount(int[] areaCount) {
    this.areaCount = areaCount;
  }

  public String getAreaLabel() {
    return areaLabel;
  }

  public void setAreaLabel(String areaLabel) {
    this.areaLabel = areaLabel;
  }

  protected CertifyRunner certifyRunner;

  public CertifyArea(String areaLetter, String areaLabel, CertifyRunner certifyRunner) {
    this.areaLabel = areaLabel;
    this.areaLetter = areaLetter;
    this.certifyRunner = certifyRunner;
    areaScore[0] = -1;
    areaScore[1] = -1;
    areaScore[2] = -1;
    areaProgress[0] = -1;
    areaProgress[1] = -1;
    areaProgress[2] = -1;
    areaCount[0] = -1;
    areaCount[1] = -1;
    areaCount[2] = -1;
    areaProgressCount[0] = 0;
    areaProgressCount[1] = 0;
    areaProgressCount[2] = 0;
  }

  public List<TestCaseMessage> getUpdateList() {
    return updateList;
  }

  public void setUpdateList(List<TestCaseMessage> updateList) {
    this.updateList = updateList;
  }

  public List<TestCaseMessage> getQueryList() {
    return queryList;
  }

  public void setQueryList(List<TestCaseMessage> queryList) {
    this.queryList = queryList;
  }

  protected static String makeTwoDigits(int i) {
    if (i < 10) {
      return "0" + i;
    } else {
      return "" + i;
    }
  }



  public abstract void prepareUpdates();

  public abstract void sendUpdates();




  public abstract void prepareQueries();

  public abstract void sendQueries();





}
