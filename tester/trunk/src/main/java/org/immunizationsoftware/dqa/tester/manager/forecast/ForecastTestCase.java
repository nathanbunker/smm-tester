package org.immunizationsoftware.dqa.tester.manager.forecast;

import java.util.ArrayList;
import java.util.List;

public class ForecastTestCase
{
  private int testPanelCaseId = 0;
  private String categoryName = "";
  private String testCaseNumber = "";
  private int testCaseId = 0;
  private String label = "";
  private String description = "";
  private String evalDate = "";
  private String patientFirst = "";
  private String patientLast = "";
  private String patientSex = "";
  private String patientDob = "";
  
  private List<ForecastExpected> forecastExpectedList = new ArrayList<ForecastExpected>();
  private List<ForecastTestEvent> forecastTestEventList = new ArrayList<ForecastTestEvent>();
  
  public List<ForecastExpected> getForecastExpectedList() {
    return forecastExpectedList;
  }
  public void setForecastExpectedList(List<ForecastExpected> forecastExpectedList) {
    this.forecastExpectedList = forecastExpectedList;
  }
  public List<ForecastTestEvent> getForecastTestEventList() {
    return forecastTestEventList;
  }
  public void setForecastTestEventList(List<ForecastTestEvent> forecastTestEventLits) {
    this.forecastTestEventList = forecastTestEventLits;
  }
  public int getTestPanelCaseId() {
    return testPanelCaseId;
  }
  public void setTestPanelCaseId(int testPanelCaseId) {
    this.testPanelCaseId = testPanelCaseId;
  }
  public String getCategoryName() {
    return categoryName;
  }
  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }
  public String getTestCaseNumber() {
    return testCaseNumber;
  }
  public void setTestCaseNumber(String testCaseNumber) {
    this.testCaseNumber = testCaseNumber;
  }
  public int getTestCaseId() {
    return testCaseId;
  }
  public void setTestCaseId(int testCaseId) {
    this.testCaseId = testCaseId;
  }
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getEvalDate() {
    return evalDate;
  }
  public void setEvalDate(String evalDate) {
    this.evalDate = evalDate;
  }
  public String getPatientFirst() {
    return patientFirst;
  }
  public void setPatientFirst(String patientFirst) {
    this.patientFirst = patientFirst;
  }
  public String getPatientLast() {
    return patientLast;
  }
  public void setPatientLast(String patientLast) {
    this.patientLast = patientLast;
  }
  public String getPatientSex() {
    return patientSex;
  }
  public void setPatientSex(String patientSex) {
    this.patientSex = patientSex;
  }
  public String getPatientDob() {
    return patientDob;
  }
  public void setPatientDob(String patientDob) {
    this.patientDob = patientDob;
  }
}
