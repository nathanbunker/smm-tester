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

  private static final String COMMAND_QUICK_TRANSFORMS = "QUICK TRANSFORMS";
  private static final String COMMAND_QUICK_TRANSFORM = "QUICK TRANSFORM";
  private static final String COMMAND_QUICK_TRANFORM = "QUICK TRANFORM";
  private static final String COMMAND_QUICK_TRANFORMS = "QUICK TRANFORMS";
  public static final String COMMAND_SET_PATIENT_TYPE = "PATIENT TYPE";
  public static final String COMMAND_SET_CONTEXT = "CONTEXT ";
  public static final String COMMAND_SELECT_SCENARIO = "SCENARIO ";
  public static final String COMMAND_ADD = "ADD ";
  public static final String COMMAND_SET = "SET ";
  public static final String COMMAND_SELECT = "SELECT ";

  public void transform(ModifyMessageRequest request) {

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
          String quickTransform = null;
          if (lineUpper.startsWith(COMMAND_QUICK_TRANSFORMS)) {
            quickTransform = COMMAND_QUICK_TRANSFORMS;
          } else if (lineUpper.startsWith(COMMAND_QUICK_TRANSFORM)) {
            quickTransform = COMMAND_QUICK_TRANSFORM;
          } else if (lineUpper.startsWith(COMMAND_QUICK_TRANFORMS)) {
            quickTransform = COMMAND_QUICK_TRANFORMS;
          } else if (lineUpper.startsWith(COMMAND_QUICK_TRANFORM)) {
            quickTransform = COMMAND_QUICK_TRANFORM;
          }  
          if (quickTransform != null) {
            String[] qt;
            String[] qtOriginal = testCaseMessage.getQuickTransformations();
            if (qtOriginal == null) {
              qt = new String[1];
            } else {
              qt = Arrays.copyOf(qtOriginal, qtOriginal.length + 1);
            }
            qt[qt.length - 1] = lineUpper.substring(quickTransform.length()).trim();
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
