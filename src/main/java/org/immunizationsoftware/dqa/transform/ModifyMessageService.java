/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.transform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;


/**
 * 
 * @author nathan
 */
public class ModifyMessageService
{

  public static final String COMMAND_ADD_QUICK_TRANSFORMS = "QUICK TRANSFORMS";
  public static final String COMMAND_SET_PATIENT_TYPE = "PATIENT TYPE";
  public static final String COMMAND_SET_CONTEXT = "CONTEXT ";
  public static final String COMMAND_SELECT_SCENARIO = "SCENARIO ";
  public static final String COMMAND_ADD = "ADD ";
  public static final String COMMAND_SET = "SET ";
  public static final String COMMAND_SELECT = "SELECT ";

  protected void transform(ModifyMessageRequest request) {

    String script = request.getTransformScript();
    Transformer transformer = new Transformer();
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    BufferedReader in = new BufferedReader(new StringReader(script));
    try {
      String line;
      while ((line = in.readLine()) != null) {
        line = line.trim();
        String lineUpper = line.toUpperCase();
        if (lineUpper.startsWith(COMMAND_SELECT)) {
          line = line.substring(COMMAND_SELECT.length()).trim();
          lineUpper = line.toUpperCase();
          if (lineUpper.startsWith(COMMAND_SELECT_SCENARIO)) {
            String scenario = line.substring(COMMAND_SELECT_SCENARIO.length()).trim();
            if (!scenario.equals("")) {
              testCaseMessage = ScenarioManager.createTestCaseMessage(scenario);
            }
          }
        } else if (lineUpper.startsWith(COMMAND_SET)) {
          line = line.substring(COMMAND_SET.length()).trim();
          lineUpper = line.toUpperCase();
          if (lineUpper.startsWith(COMMAND_SET_CONTEXT)) {
            // ignore for now, immunization by default
          } else if (lineUpper.startsWith(COMMAND_SET_PATIENT_TYPE)) {
            PatientType patientType = PatientType.valueOf(lineUpper.substring(
                COMMAND_SET_PATIENT_TYPE.length()).trim());
            testCaseMessage.setPatientType(patientType);
          }
        } else if (lineUpper.startsWith(COMMAND_ADD)) {
          line = line.substring(COMMAND_ADD.length()).trim();
          lineUpper = line.toUpperCase();
          if (lineUpper.startsWith(COMMAND_ADD_QUICK_TRANSFORMS)) {
            String[] qt;
            String[] qtOriginal = testCaseMessage.getQuickTransformations();
            if (qtOriginal == null) {
              qt = new String[1];
            } else {
              qt = Arrays.copyOf(qtOriginal, qtOriginal.length + 1);
            }
            qt[qt.length - 1] = lineUpper.substring(COMMAND_ADD_QUICK_TRANSFORMS.length()).trim();
            testCaseMessage.setQuickTransformations(qt);
          }
        } else {
          testCaseMessage.appendCustomTransformation(line);
        }
      }
      in.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      // shouldn't happen when reading a string.
    }
    if (!request.getMessageOriginal().equals("")) {
      testCaseMessage.setOriginalMessage(request.getMessageOriginal());
    }
    transformer.transform(testCaseMessage);
    String finalMessage = testCaseMessage.getMessageText();
    request.setMessageFinal(finalMessage);
  }

}
