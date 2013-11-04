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
public class IssueCreatorNextOfKin extends IssueCreator
{

  public static String createTransforms(TestCaseMessage testCaseMessage, Issue issue, String transforms, boolean not) {
    boolean is = !not;
    if (issue == Issue.NEXT_OF_KIN_ADDRESS_IS_DIFFERENT_FROM_PATIENT_ADDRESS) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.1=[STREET] Apt 21\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.1=\n";
        transforms += "NK1-4.2=\n";
        transforms += "NK1-4.3=\n";
        transforms += "NK1-4.4=\n";
        transforms += "NK1-4.5=\n";
        transforms += "NK1-4.6=\n";
        transforms += "NK1-4.7=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_CITY_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.3=ANYTOWN\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_CITY_IS_UNEXPECTEDLY_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.3=This is an unexpectedly long name for a city\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_CITY_IS_TOO_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.3=This name is way too long for a city so long that it might cause some systems to crash as the max number of chars is fifty\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_CITY_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.3=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTRY_IS_DEPRECATED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.6=US\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTRY_IS_IGNORED) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTRY_IS_INVALID) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTRY_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.6=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTRY_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.6=SOMETHING\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTY_IS_DEPRECATED) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTY_IS_IGNORED) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTY_IS_INVALID) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTY_IS_MISSING) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_COUNTY_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.9=County\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_STATE_IS_DEPRECATED) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_STATE_IS_IGNORED) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_STATE_IS_INVALID) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_STATE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.4=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_STATE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.4=Mass\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_STREET_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.4=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_STREET2_IS_MISSING) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_TYPE_IS_DEPRECATED) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_TYPE_IS_IGNORED) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_TYPE_IS_INVALID) {
         // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_TYPE_IS_VALUED_BAD_ADDRESS) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.7=BA\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_TYPE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.7=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_TYPE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.7=PRIMARY\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_ZIP_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.5=9o21o\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_ADDRESS_ZIP_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-4.5=9o21o";
      }
    } else if (issue == Issue.NEXT_OF_KIN_NAME_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-2.1=\n";
        transforms += "NK1-2.2=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_NAME_FIRST_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-2.2=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_NAME_LAST_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-2.1=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_PHONE_NUMBER_IS_INCOMPLETE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-5.6=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_PHONE_NUMBER_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-5.6=APPLE GATE\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_PHONE_NUMBER_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-5.1=\n";
        transforms += "NK1-5.6=\n";
        transforms += "NK1-5.7=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_RELATIONSHIP_IS_DEPRECATED) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_RELATIONSHIP_IS_IGNORED) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_RELATIONSHIP_IS_INVALID) {
      // not implemented
    } else if (issue == Issue.NEXT_OF_KIN_RELATIONSHIP_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-3.1=\n";
        transforms += "NK1-3.2=\n";
        transforms += "NK1-3.3=\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_RELATIONSHIP_IS_NOT_RESPONSIBLE_PARTY) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-3.1=CHD\n";
        transforms += "NK1-3.2=Child\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_RELATIONSHIP_IS_UNEXPECTED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-3.1=CHD\n";
        transforms += "NK1-3.2=Child\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_RELATIONSHIP_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-3.1=DS\n";
        transforms += "NK1-3.2=Dog Sitter\n";
      }
    } else if (issue == Issue.NEXT_OF_KIN_SSN_IS_MISSING) {
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms = addNK1Segment(testCaseMessage, transforms);
        transforms += "NK1-37=[SSN]\n";
      }
    }
    return transforms;
  }

  public static String addNK1Segment(TestCaseMessage testCaseMessage, String transforms) {
    testCaseMessage.prepareMessageAddSegment("NK1", "PID");
    transforms += "NK1-1=1\n";
    transforms += "NK1-2.1=[MOTHER_MAIDEN]\n";
    transforms += "NK1-2.2=[MOTHER]\n";
    transforms += "NK1-3.1=MTH";
    transforms += "NK1-3.2=Mother";
    transforms += "NK1-3.3=HL70063";
    transforms += "NK1-4.1=[STREET]";
    transforms += "NK1-4.3=[CITY]";
    transforms += "NK1-4.4=[STATE]";
    transforms += "NK1-4.5=[ZIP]";
    transforms += "NK1-4.6=USA";
    transforms += "NK1-4.6=P";
    transforms += "NK1-5.1=\n";
    transforms += "NK1-5.2=PRN\n";
    transforms += "NK1-5.3=PH\n";
    transforms += "NK1-5.6=[PHONE_AREA]\n";
    transforms += "NK1-5.7=[PHONE_LOCAL]\n";
    return transforms;
  }
}
