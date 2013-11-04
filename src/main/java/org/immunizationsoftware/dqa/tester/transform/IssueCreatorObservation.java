/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.transform;

import org.immunizationsoftware.dqa.tester.TestCaseMessage;

/**
 * 
 * @author nathan
 */
public class IssueCreatorObservation extends IssueCreator
{

  public static String createTransforms(TestCaseMessage testCaseMessage, Issue issue, String transforms, boolean not) {
    boolean is = !not;
    if (issue == Issue.OBSERVATION_VALUE_TYPE_IS_DEPRECATED) {
    } else if (issue == Issue.OBSERVATION_VALUE_TYPE_IS_IGNORED) {
    } else if (issue == Issue.OBSERVATION_VALUE_TYPE_IS_INVALID) {
    } else if (issue == Issue.OBSERVATION_VALUE_TYPE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "OBX-2.1=\n";
      }
    } else if (issue == Issue.OBSERVATION_VALUE_TYPE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "OBX-2.1=CODED VALUE\n";
      }
    } else if (issue == Issue.OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_DEPRECATED) {
    } else if (issue == Issue.OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_IGNORED) {
    } else if (issue == Issue.OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_INVALID) {
    } else if (issue == Issue.OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "OBX-3.1=LOINC GOES HERE\n";
      }
    } else if (issue == Issue.OBSERVATION_OBSERVATION_VALUE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "OBX-5.1=\n";
      }
    } else if (issue == Issue.OBSERVATION_DATE_TIME_OF_OBSERVATION_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.OBSERVATION_DATE_TIME_OF_OBSERVATION_IS_INVALID) {
    }
    return transforms;
  }
}
