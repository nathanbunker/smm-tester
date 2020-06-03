package org.immregistries.smm;

import org.immregistries.smm.tester.transform.Patient;
import org.immregistries.smm.transform.PatientType;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;


public class GenerateRandomPatientData {

  @Test
  public void generateRandomPatientData() throws Exception {
    Transformer transformer = new Transformer();
    Patient patient = transformer.setupPatient(PatientType.BABY);
    System.out.println("First name (boy): " + patient.getBoyName());
    System.out.println("First name (girl): " + patient.getGirlName());

  }
}
