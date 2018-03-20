package org.immregistries.smm.tester.manager.query;

import java.util.List;

import org.immregistries.smm.tester.manager.response.ImmunizationMessage;

public class QueryResponse implements ImmunizationMessage {
  private QueryResponseType queryResponseType = null;
  private List<Patient> patientList = null;
  private Patient patient = null;
  private List<Vaccination> vaccinationList = null;

  public List<Vaccination> getVaccinationList() {
    return vaccinationList;
  }

  public void setVaccinationList(List<Vaccination> vaccinationList) {
    this.vaccinationList = vaccinationList;
  }

  public List<Patient> getPatientList() {
    return patientList;
  }

  public void setPatientList(List<Patient> patientList) {
    this.patientList = patientList;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public QueryResponseType getQueryResponseType() {
    return queryResponseType;
  }

  public void setQueryResponseType(QueryResponseType queryResponseType) {
    this.queryResponseType = queryResponseType;
  }
}
