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
public class IssueCreatorObservation extends IssueCreator {

    public static String createTransforms(TestCaseMessage testCaseMessage, String line, String transforms, boolean not) {
        boolean is = !not;
        if (line.equalsIgnoreCase(OBSERVATION_VALUE_TYPE_IS_DEPRECATED)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_VALUE_TYPE_IS_IGNORED)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_VALUE_TYPE_IS_INVALID)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_VALUE_TYPE_IS_MISSING)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_VALUE_TYPE_IS_UNRECOGNIZED)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_DEPRECATED)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_IGNORED)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_INVALID)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_MISSING)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_UNRECOGNIZED)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_OBSERVATION_VALUE_IS_MISSING)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_DATE_TIME_OF_OBSERVATION_IS_MISSING)) {
        } else if (line.equalsIgnoreCase(OBSERVATION_DATE_TIME_OF_OBSERVATION_IS_INVALID)) {
        }
        return transforms;
    }
}
