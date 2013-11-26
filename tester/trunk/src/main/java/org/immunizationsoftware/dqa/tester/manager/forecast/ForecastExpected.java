package org.immunizationsoftware.dqa.tester.manager.forecast;

public class ForecastExpected
{
  private int forecastExpectedId;
  private String doseNumber = "";
  private String validDate = "";
  private String dueDate = "";
  private String overdueDate = "";
  private String finishedDate = "";
  private String vaccineCvx = "";

  public int getForecastExpectedId() {
    return forecastExpectedId;
  }

  public void setForecastExpectedId(int forecastExpectedId) {
    this.forecastExpectedId = forecastExpectedId;
  }

  public String getDoseNumber() {
    return doseNumber;
  }

  public void setDoseNumber(String doseNumber) {
    this.doseNumber = doseNumber;
  }

  public String getValidDate() {
    return validDate;
  }

  public void setValidDate(String validDate) {
    this.validDate = validDate;
  }

  public String getDueDate() {
    return dueDate;
  }

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }

  public String getOverdueDate() {
    return overdueDate;
  }

  public void setOverdueDate(String overdueDate) {
    this.overdueDate = overdueDate;
  }

  public String getFinishedDate() {
    return finishedDate;
  }

  public void setFinishedDate(String finishedDate) {
    this.finishedDate = finishedDate;
  }

  public String getVaccineCvx() {
    return vaccineCvx;
  }

  public void setVaccineCvx(String vaccineCvx) {
    this.vaccineCvx = vaccineCvx;
  }
}
