package org.immunizationsoftware.dqa.tester.manager.forecast;

public class ForecastTestEvent
{

  private int testEventId = 0;
  private String label = "";
  private String eventTypeCode = "";
  private String vaccineCvx = "";
  private String vaccineMvx = "";
  private String eventDate = "";

  public int getTestEventId() {
    return testEventId;
  }

  public void setTestEventId(int testEventId) {
    this.testEventId = testEventId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getEventTypeCode() {
    return eventTypeCode;
  }

  public void setEventTypeCode(String eventTypeCode) {
    this.eventTypeCode = eventTypeCode;
  }

  public String getVaccineCvx() {
    return vaccineCvx;
  }

  public void setVaccineCvx(String vaccineCvx) {
    this.vaccineCvx = vaccineCvx;
  }

  public String getVaccineMvx() {
    return vaccineMvx;
  }

  public void setVaccineMvx(String vaccineMvx) {
    this.vaccineMvx = vaccineMvx;
  }

  public String getEventDate() {
    return eventDate;
  }

  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }
}
