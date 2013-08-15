/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.transform;

import java.util.Arrays;

import org.immunizationsoftware.dqa.tester.TestCaseMessage;

/**
 *
 * @author nathan
 */
public class IssueCreatorHeader extends IssueCreator {

    public static String createTransforms(TestCaseMessage testCaseMessage, Issue issue, String transforms, boolean not) {
        boolean is = !not;
        if (issue == Issue.HL7_SEGMENT_IS_UNRECOGNIZED) {
            if (is) {
                testCaseMessage.prepareMessageAddSegment("T3P", "PID");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_ACCEPT_ACK_TYPE_IS_INVALID) {
            if (is) {
                transforms += "MSH-15=Friday\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_ACCEPT_ACK_TYPE_IS_MISSING) {
            if (is) {
                transforms += "MSH-15=\n";
                testCaseMessage.setHasIssue(true);
            }
            if (not) {
                transforms += "MSH-15=AL\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_ACCEPT_ACK_TYPE_IS_UNRECOGNIZED) {
            if (is) {
                transforms += "MSH-15=A\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_ALT_CHARACTER_SET_IS_INVALID) {
            // TODO
        } else if (issue == Issue.HL7_MSH_ALT_CHARACTER_SET_IS_MISSING) {
            // TODO
        } else if (issue == Issue.HL7_MSH_ALT_CHARACTER_SET_IS_UNRECOGNIZED) {
            // TODO
        } else if (issue == Issue.HL7_MSH_APP_ACK_TYPE_IS_INVALID) {
            if (is) {
                transforms += "MSH-16=Monday\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_APP_ACK_TYPE_IS_MISSING) {
            if (is) {
                transforms += "MSH-16=\n";
                testCaseMessage.setHasIssue(true);
            }
            if (is) {
                transforms += "MSH-16=AL\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_CHARACTER_SET_IS_DEPRECATED) {
            // TODO
        } else if (issue == Issue.HL7_MSH_CHARACTER_SET_IS_INVALID) {
            // TODO
        } else if (issue == Issue.HL7_MSH_CHARACTER_SET_IS_MISSING) {
            // TODO
        } else if (issue == Issue.HL7_MSH_CHARACTER_SET_IS_UNRECOGNIZED) {
            // TODO
        } else if (issue == Issue.HL7_MSH_COUNTRY_CODE_IS_DEPRECATED) {
            if (is) {
                transforms += "MSH-17=US\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_COUNTRY_CODE_IS_INVALID) {
            if (is) {
                transforms += "MSH-17=20110708\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_COUNTRY_CODE_IS_MISSING) {
            if (is) {
                transforms += "MSH-17=\n";
                testCaseMessage.setHasIssue(true);
            }
            if (not) {
                transforms += "MSH-17=USA\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_COUNTRY_CODE_IS_UNRECOGNIZED) {
            if (is) {
                transforms += "MSH-17=US of A\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_ENCODING_CHARACTER_IS_INVALID) {
            if (is) {
                transforms += "MSH-2=BADD\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_ENCODING_CHARACTER_IS_MISSING) {
            if (is) {
                transforms += "MSH-2=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_ENCODING_CHARACTER_IS_NON_STANDARD) {
            // TODO
        } else if (issue == Issue.HL7_MSH_MESSAGE_CONTROL_ID_IS_MISSING) {
            if (is) {
                transforms += "MSH-10=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_DATE_IS_IN_FUTURE) {
            if (is) {
                transforms += "MSH-7=[FUTURE]\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_DATE_IS_INVALID) {
            if (is) {
                transforms += "MSH-7=VXU\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_DATE_IS_MISSING) {
            if (is) {
                transforms += "MSH-7=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_PROFILE_ID_IS_INVALID) {
            // TODO
        } else if (issue == Issue.HL7_MSH_MESSAGE_PROFILE_ID_IS_MISSING) {
            // TODO
        } else if (issue == Issue.HL7_MSH_MESSAGE_PROFILE_ID_IS_UNRECOGNIZED) {
            // TODO
        } else if (issue == Issue.HL7_MSH_MESSAGE_STRUCTURE_IS_MISSING) {
            if (is) {
                transforms += "MSH-9.3=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_STRUCTURE_IS_UNRECOGNIZED) {
            if (is) {
                transforms += "MSH-9.3=STRUCTURE\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_TRIGGER_IS_MISSING) {
            if (is) {
                transforms += "MSH-9.2=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_TRIGGER_IS_UNRECOGNIZED) {
            if (is) {
                transforms += "MSH-9.2=TRIGGER\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_TYPE_IS_MISSING) {
            if (is) {
                transforms += "MSH-9.1=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_TYPE_IS_UNRECOGNIZED) {
            if (is) {
                transforms += "MSH-9.1=TYPE\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_MESSAGE_TYPE_IS_UNSUPPORTED) {
            if (is) {
                transforms += "MSH-9.1=ZZZ\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_PROCESSING_ID_IS_INVALID) {
            if (is) {
                transforms += "MSH-11.1=SlOW\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_PROCESSING_ID_IS_MISSING) {
            if (is) {
                transforms += "MSH-11.1=\n";
                testCaseMessage.setHasIssue(true);
            }
            if (not) {
                transforms += "MSH-11.1=P\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_PROCESSING_ID_IS_UNRECOGNIZED) {
            if (is) {
                transforms += "MSH-11.1=PROD\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_PROCESSING_ID_IS_VALUED_AS_DEBUG) {
            if (is) {
                transforms += "MSH-11.1=D\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_PROCESSING_ID_IS_VALUED_AS_PRODUCTION) {
            if (is) {
                transforms += "MSH-11.1=P\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_PROCESSING_ID_IS_VALUED_AS_TRAINING) {
            if (is) {
                transforms += "MSH-11.1=T\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_RECEIVING_APPLICATION_IS_MISSING) {
            if (is) {
                transforms += "MSH-5.1=\n";
                transforms += "MSH-5.2=\n";
                transforms += "MSH-5.3=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_RECEIVING_FACILITY_IS_MISSING) {
            if (is) {
                transforms += "MSH-6.1=\n";
                transforms += "MSH-6.2=\n";
                transforms += "MSH-6.3=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_SEGMENT_IS_MISSING) {
            if (is) {
                testCaseMessage.prepareMessageRemoveSegment("MSH");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_SENDING_APPLICATION_IS_MISSING) {
            if (is) {
                transforms += "MSH-3.1=\n";
                transforms += "MSH-3.2=\n";
                transforms += "MSH-3.3=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_SENDING_FACILITY_IS_MISSING) {
            if (is) {
                transforms += "MSH-4.1=\n";
                transforms += "MSH-4.2=\n";
                transforms += "MSH-4.3=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_VERSION_IS_MISSING) {
            if (is) {
                transforms += "MSH-12=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_VERSION_IS_UNRECOGNIZED) {
            if (is) {
                transforms += "MSH-12=13.4\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_VERSION_IS_VALUED_AS_2_3_1) {
            if (is) {
                transforms += "MSH-12=2.3.1\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_VERSION_IS_VALUED_AS_2_4) {
            if (is) {
                transforms += "MSH-12=2.4\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_MSH_VERSION_IS_VALUED_AS_2_5) {
            if (is) {
                transforms += "MSH-12=2.5.1\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_NK1_SEGMENT_IS_MISSING) {
            if (is) {
                testCaseMessage.prepareMessageRemoveSegment("NK1");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_NK1_SEGMENT_IS_REPEATED) {
            if (is) {
                testCaseMessage.prepareMessageAddSegment("NK1", "NK1");
                transforms += "NK1#2-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
                transforms += "NK1#2-2.2=[FATHER]\n";
                transforms += "NK1#2-3=FTH\n";
                transforms += "NK1#2-3.2=Father\n";
                transforms += "NK1#2-3.3=HL70063\n";
                transforms += "NK1#2-4.1=[STREET]\n";
                transforms += "NK1#2-4.3=[CITY]\n";
                transforms += "NK1#2-4.4=[STATE]\n";
                transforms += "NK1#2-4.5=[ZIP]\n";
                transforms += "NK1#2-4.6=USA\n";
                transforms += "NK1#2-4.7=L\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_NK1_SET_ID_IS_MISSING) {
            if (is) {
                transforms += "NK1-1=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_OBX_SEGMENT_IS_MISSING) {
            if (is) {
                testCaseMessage.prepareMessageRemoveSegment("OBX");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_ORC_SEGMENT_IS_MISSING) {
            if (is) {
                testCaseMessage.prepareMessageRemoveSegment("ORC");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_PD1_SEGMENT_IS_MISSING) {
            if (is) {
                testCaseMessage.prepareMessageRemoveSegment("PD1");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_PID_SEGMENT_IS_MISSING) {
            if (is) {
                testCaseMessage.prepareMessageRemoveSegment("PID");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_PID_SEGMENT_IS_REPEATED) {
            if (is) {
                testCaseMessage.prepareMessageAddSegment("PID", "PID");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_PV1_SEGMENT_IS_MISSING) {
            if (is) {
                testCaseMessage.prepareMessageRemoveSegment("PV1");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_PV1_SEGMENT_IS_REPEATED) {
            if (is) {
                testCaseMessage.prepareMessageAddSegment("PV1", "PV1");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_RXA_ADMIN_SUB_ID_COUNTER_IS_MISSING) {
            if (is) {
                transforms += "RXA-2=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_RXA_GIVE_SUB_ID_IS_MISSING) {
            if (is) {
                transforms += "RXA-1=\n";
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_RXA_SEGMENT_IS_MISSING) {
            if (is) {
                testCaseMessage.prepareMessageRemoveSegment("RXA");
                testCaseMessage.prepareMessageRemoveSegment("RXA");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_RXA_SEGMENT_IS_REPEATED) {
            // TODO
        } else if (issue == Issue.HL7_RXR_SEGMENT_IS_MISSING) {
            if (is) {
                testCaseMessage.prepareMessageRemoveSegment("RXR");
                testCaseMessage.setHasIssue(true);
            }
        } else if (issue == Issue.HL7_RXR_SEGMENT_IS_REPEATED) {
            if (is) {
                testCaseMessage.prepareMessageAddSegment("RXR", "RXR");
                testCaseMessage.setHasIssue(true);
            }
        }
        return transforms;
    }
}
