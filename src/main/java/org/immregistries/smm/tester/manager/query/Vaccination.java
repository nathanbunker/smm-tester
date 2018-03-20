package org.immregistries.smm.tester.manager.query;

import java.util.Date;

public class Vaccination {
  private Date administrationDate = null;
  private String vaccineCvx = "";
  private String vaccineLabel = "";
  
  public Date getAdministrationDate() {
    return administrationDate;
  }
  public void setAdministrationDate(Date administrationDate) {
    this.administrationDate = administrationDate;
  }
  public String getVaccineCvx() {
    return vaccineCvx;
  }
  public void setVaccineCvx(String vaccineCvx) {
    this.vaccineCvx = vaccineCvx;
  }
  public String getVaccineLabel() {
    return vaccineLabel;
  }
  public void setVaccineLabel(String vaccineLabel) {
    this.vaccineLabel = vaccineLabel;
  }

}
