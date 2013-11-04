/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.transform;

import org.immunizationsoftware.dqa.tester.TestCaseMessage;

/**
 * 
 * @author nathan/
 */
public class IssueCreatorPatient extends IssueCreator
{

  public static String createTransforms(TestCaseMessage testCaseMessage, Issue issue, String transforms, boolean not) {
    boolean is = !not;
    if (issue == Issue.PATIENT_ADDRESS_IS_MISSING) {
      if (is) {
        transforms += "PID-11.1=\n";
        transforms += "PID-11.2=\n";
        transforms += "PID-11.3=\n";
        transforms += "PID-11.4=\n";
        transforms += "PID-11.5=\n";
        transforms += "PID-11.6=\n";
        transforms += "PID-11.7=\n";
        transforms += "PID-11.8=\n";
        transforms += "PID-11.9=\n";
        transforms += "PID-11.10=\n";
        transforms += "PID-11.11=\n";
        transforms += "PID-11.12=\n";
        transforms += "PID-11.13=\n";
        transforms += "PID-11.14=\n";
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_ADDRESS_CITY_IS_INVALID) {
      if (is) {
        transforms += "PID-11.3=1\n";
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_ADDRESS_CITY_IS_MISSING) {
      if (is) {
        transforms += "PID-11.3=\n";
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_ADDRESS_CITY_IS_UNEXPECTEDLY_LONG) {
      if (is) {
        transforms += "PID-11.3=This is an unexpectedly long name for a city\n";
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_ADDRESS_CITY_IS_TOO_LONG) {
      if (is) {
        transforms += "PID-11.3=This name is way too long for a city so long that it might cause some systems to crash as the max number of chars is fifty\n";
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_ADDRESS_COUNTRY_IS_DEPRECATED) {
      if (is) {
        transforms += "PID-11.6=US\n";
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_ADDRESS_COUNTRY_IS_UNRECOGNIZED) {
      if (is) {
        transforms += "PID-11.6=US of A\n";
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_ADDRESS_COUNTRY_IS_MISSING) {
      if (is) {
        transforms += "PID-11.6=\n";
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_ADDRESS_COUNTRY_IS_UNRECOGNIZED) {
      if (is) {
        transforms += "PID-11.6=ZZZ\n";
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_ADDRESS_COUNTY_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.9=Apartment #25\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_COUNTY_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.9=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.9=26039\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_STATE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.4=103\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_STATE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.4=Michigan\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_STATE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.4=\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_STREET_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.1=\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_STREET2_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.2=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.2=Apartment A\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_TYPE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.7=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.7=P\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_TYPE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.7=PRIMARY\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_TYPE_IS_INVALID) {
      // not implemented
    } else if (issue == Issue.PATIENT_ADDRESS_ZIP_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.5=50\n";
      }
    } else if (issue == Issue.PATIENT_ADDRESS_ZIP_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-11.5=\n";
      }
    } else if (issue == Issue.PATIENT_ALIAS_IS_MISSING) {
      if (is)
      {
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_BIRTH_DATE_IS_AFTER_SUBMISSION) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-7=[FUTURE]\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_DATE_IS_IN_FUTURE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-7=[FUTURE]\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_DATE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-7=DOB\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_DATE_IS_UNDERAGE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
      }
    } else if (issue == Issue.PATIENT_BIRTH_DATE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-7=\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_DATE_IS_VERY_LONG_AGO) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-7=18950115\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_INDICATOR_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-24=X\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-24=N\n";
        transforms += "PID-25=\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_INDICATOR_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-24=\n";
        transforms += "PID-25=2\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-24=Y\n";
        transforms += "PID-25=2\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_ORDER_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-24=Y\n";
        transforms += "PID-25=Twin\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_ORDER_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-24=\n";
        transforms += "PID-25=\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_ORDER_IS_MISSING_AND_MULTIPLE_BIRTH_INDICATED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-24=Y\n";
        transforms += "PID-25=\n";
      }
    } else if (issue == Issue.PATIENT_BIRTH_PLACE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-23=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-23=Mercy Hospital\n";
      }
    } else if (issue == Issue.PATIENT_CLASS_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PV1-2=Recurring\n";
      }
    } else if (issue == Issue.PATIENT_CLASS_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PV1-2=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-2=R\n";
      }
    } else if (issue == Issue.PATIENT_DEATH_DATE_IS_BEFORE_BIRTH) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=19500101\n";
        transforms += "PID-30=Y\n";
      }
    } else if (issue == Issue.PATIENT_DEATH_DATE_IS_IN_FUTURE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=[FUTURE]\n";
        transforms += "PID-30=Y\n";
      }
    } else if (issue == Issue.PATIENT_DEATH_DATE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=Y\n";
        transforms += "PID-30=Y\n";
      }
    } else if (issue == Issue.PATIENT_DEATH_DATE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=\n";
        transforms += "PID-30=Y\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=[VAC3_DATE]\n";
        transforms += "PID-30=Y\n";
      }
    } else if (issue == Issue.PATIENT_DEATH_INDICATOR_IS_INCONSISTENT) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=[VAC3_DATE]\n";
        transforms += "PID-30=N\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=[VAC3_DATE]\n";
        transforms += "PID-30=Y\n";
      }
    } else if (issue == Issue.PATIENT_DEATH_INDICATOR_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=[VAC3_DATE]\n";
        transforms += "PID-30=\n";
      }
    } else if (issue == Issue.PATIENT_ETHNICITY_IS_DEPRECATED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-22=H\n";
      }
    } else if (issue == Issue.PATIENT_ETHNICITY_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-22=105 E Market St\n";
      }
    } else if (issue == Issue.PATIENT_ETHNICITY_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-22=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-22=[ETHNICITY]\n";
      }
    } else if (issue == Issue.PATIENT_ETHNICITY_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-22=Hispanic\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-22=[ETHNICITY]\n";
      }
    } else if (issue == Issue.PATIENT_GENDER_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-8=(404)555-3993\n";
      }
    } else if (issue == Issue.PATIENT_GENDER_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-8=\n";
      }
    } else if (issue == Issue.PATIENT_GENDER_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-8=Female\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-8=[GENDER]\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_ADDRESS_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.1=\n";
        transforms += "NK1-4.2=\n";
        transforms += "NK1-4.3=\n";
        transforms += "NK1-4.4=\n";
        transforms += "NK1-4.5=\n";
        transforms += "NK1-4.6=\n";
        transforms += "NK1-4.7=\n";
        transforms += "NK1-4.8=\n";
        transforms += "NK1-4.9=\n";
        transforms += "NK1-4.10=\n";
        transforms += "NK1-4.11=\n";
        transforms += "NK1-4.12=\n";
        transforms += "NK1-4.13=\n";
        transforms += "NK1-4.14=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.1=[STREET]\n";
        transforms += "NK1-4.3=[CITY]\n";
        transforms += "NK1-4.4=[STATE]\n";
        transforms += "NK1-4.5=[ZIP]\n";
        transforms += "NK1-4.6=USA\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_ADDRESS_CITY_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.3=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.3=[CITY]\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_ADDRESS_STATE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.4=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.4=[STATE]\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_ADDRESS_STREET_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.1=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.1=[STREET]\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_ADDRESS_ZIP_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.5=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-4.5=[ZIP]\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_NAME_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-2.1=\n";
        transforms += "NK1-2.2=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
        transforms += "NK1-2.2=[MOTHER]\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_NAME_IS_SAME_AS_UNDERAGE_PATIENT) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-2.1=[LAST]\n";
        transforms += "NK1-2.2=[BOY]\n";
        transforms += "PID-5.1=[LAST]\n";
        transforms += "PID-5.2=[BOY]\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-2.1=[LAST_DIFFERENT]\n";
        transforms += "NK1-2.2=[MOTHER]\n";
        transforms += "PID-5.1=[LAST]\n";
        transforms += "PID-5.2=[BOY]\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_NAME_FIRST_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-2.2=\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_NAME_LAST_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-2.1=\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_RESPONSIBLE_PARTY_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        testCaseMessage.prepareMessageRemoveSegment("NK1");
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_PHONE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-5.1=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-5.1=[PHONE]\n";
        transforms += "NK1-5.2=PRN\n";
        transforms += "NK1-5.3=PH\n";
        transforms += "NK1-5.6=[PHONE_AREA]\n";
        transforms += "NK1-5.7=[PHONE_LOCAL]\n";
      }
    } else if (issue == Issue.PATIENT_GUARDIAN_RELATIONSHIP_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "NK1-3.1=\n";
        transforms += "NK1-3.2=Mother\n";
        transforms += "NK1-3.3=HL70063\n";
      }
    } else if (issue == Issue.PATIENT_IMMUNIZATION_REGISTRY_STATUS_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-16=MMR\n";
      }
    } else if (issue == Issue.PATIENT_IMMUNIZATION_REGISTRY_STATUS_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-16=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-16=A\n";
      }
    } else if (issue == Issue.PATIENT_MEDICAID_NUMBER_IS_INVALID) {
      // TODO
    } else if (issue == Issue.PATIENT_MEDICAID_NUMBER_IS_MISSING) {
      // TODO
    } else if (issue == Issue.PATIENT_MIDDLE_NAME_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.3=\n";
      }
    } else if (issue == Issue.PATIENT_MIDDLE_NAME_IS_UNEXPECTEDLY_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.3=This middle name is long\n";
      }
    } else if (issue == Issue.PATIENT_MIDDLE_NAME_IS_TOO_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.3=The middle name is much to long as the field is defined to have no more than thirty characters\n";
      }
    } else if (issue == Issue.PATIENT_NAME_LAST_IS_UNEXPECTEDLY_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.3=This last name is longer than expected\n";
      }
    } else if (issue == Issue.PATIENT_NAME_LAST_IS_TOO_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.3=The last name is much to long as the field is defined to have no more than fifty characters so this will probably be rejected or truncated\n";
      }
    } else if (issue == Issue.PATIENT_NAME_FIRST_IS_UNEXPECTEDLY_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.3=This first name is to long\n";
      }
    } else if (issue == Issue.PATIENT_NAME_FIRST_IS_TOO_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.3=The first name is much to long as the field is defined to have no more than thirty characters so this will probably be rejected or truncated\n";
      }
    } else if (issue == Issue.PATIENT_MIDDLE_NAME_MAY_BE_INITIAL) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.3=[BOY_MIDDLE_INITIAL]\n";
      }
    } else if (issue == Issue.PATIENT_MOTHERS_MAIDEN_NAME_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-6=\n";
      }
    } else if (issue == Issue.PATIENT_MOTHERS_MAIDEN_NAME_IS_UNEXPECTEDLY_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-6=This mothers name is much longer than expected\n";
      }
    } else if (issue == Issue.PATIENT_MOTHERS_MAIDEN_NAME_IS_UNEXPECTEDLY_LONG) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-6=This mothers maiden name is much longer than expected and should be rejected or truncated on recieving systems as the maximum normative length in HL7 fifty characters\n";
      }
    } else if (issue == Issue.PATIENT_NAME_MAY_BE_TEMPORARY_NEWBORN_NAME) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.2=NEWBORN\n";
      }
    } else if (issue == Issue.PATIENT_NAME_MAY_BE_TEST_NAME) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.1=TEST\n";
      }
    } else if (issue == Issue.PATIENT_NAME_FIRST_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.2=Apt #5\n";
      }
    } else if (issue == Issue.PATIENT_NAME_FIRST_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.2=\n";
      }
    } else if (issue == Issue.PATIENT_NAME_FIRST_MAY_INCLUDE_MIDDLE_INITIAL) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.2=John A\n";
        transforms += "PID-5.3=\n";
      }
    } else if (issue == Issue.PATIENT_NAME_LAST_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.1=Apt #5\n";
      }
    } else if (issue == Issue.PATIENT_NAME_LAST_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.1=\n";
      }
    } else if (issue == Issue.PATIENT_NAME_TYPE_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.7=Samuel\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.2=L\n";
      }
    } else if (issue == Issue.PATIENT_NAME_TYPE_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.7=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.2=L\n";
      }
    } else if (issue == Issue.PATIENT_NAME_TYPE_CODE_IS_NOT_VALUED_LEGAL) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-5.7=A\n";
      }
    } else if (issue == Issue.PATIENT_PHONE_IS_INCOMPLETE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-13.1=[PHONE_LOCAL]\n";
        transforms += "PID-13.2=PRN\n";
        transforms += "PID-13.3=PH\n";
        transforms += "PID-13.6=\n";
        transforms += "PID-13.7=[PHONE_LOCAL]\n";
      }
    } else if (issue == Issue.PATIENT_PHONE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-13.1=104 E Market St\n";
        transforms += "PID-13.2=\n";
        transforms += "PID-13.3=Allegany\n";
        transforms += "PID-13.6=MI\n";
        transforms += "PID-13.7=04599\n";
      }
    } else if (issue == Issue.PATIENT_PHONE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-13.1=\n";
        transforms += "PID-13.2=\n";
        transforms += "PID-13.3=\n";
        transforms += "PID-13.6=\n";
        transforms += "PID-13.7=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-13.1=[PHONE]\n";
        transforms += "PID-13.2=PRN\n";
        transforms += "PID-13.3=PH\n";
        transforms += "PID-13.6=[PHONE_AREA]\n";
        transforms += "PID-13.7=[PHONE_LOCAL]\n";
      }
    } else if (issue == Issue.PATIENT_PHONE_TEL_USE_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-13.1=[PHONE]\n";
        transforms += "PID-13.2=Apt B\n";
        transforms += "PID-13.3=PH\n";
        transforms += "PID-13.6=[PHONE_AREA]\n";
        transforms += "PID-13.7=[PHONE_LOCAL]\n";
      }
    } else if (issue == Issue.PATIENT_PHONE_TEL_USE_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-13.1=[PHONE]\n";
        transforms += "PID-13.2=\n";
        transforms += "PID-13.3=PH\n";
        transforms += "PID-13.6=[PHONE_AREA]\n";
        transforms += "PID-13.7=[PHONE_LOCAL]\n";
      }
    } else if (issue == Issue.PATIENT_PHONE_TEL_EQUIP_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-13.1=[PHONE]\n";
        transforms += "PID-13.2=PRN\n";
        transforms += "PID-13.3=BAD\n";
        transforms += "PID-13.6=[PHONE_AREA]\n";
        transforms += "PID-13.7=[PHONE_LOCAL]\n";
      }
    } else if (issue == Issue.PATIENT_PHONE_TEL_EQUIP_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-13.1=[PHONE]\n";
        transforms += "PID-13.2=PRN\n";
        transforms += "PID-13.3=\n";
        transforms += "PID-13.6=[PHONE_AREA]\n";
        transforms += "PID-13.7=[PHONE_LOCAL]\n";
      }
    } else if (issue == Issue.PATIENT_PRIMARY_FACILITY_ID_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-3.1=\n";
        transforms += "PD1-3.2=\n";
        transforms += "PD1-3.3=\n";
        transforms += "PD1-3.4=\n";
        transforms += "PD1-3.5=\n";
        transforms += "PD1-3.6=\n";
        transforms += "PD1-3.7=\n";
        transforms += "PD1-3.8=\n";
        transforms += "PD1-3.9=\n";
        transforms += "PD1-3.10=\n";
      }
    } else if (issue == Issue.PATIENT_PRIMARY_FACILITY_NAME_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-3.1=\n";
        transforms += "PD1-3.2=\n";
        transforms += "PD1-3.3=\n";
        transforms += "PD1-3.4=\n";
        transforms += "PD1-3.5=\n";
        transforms += "PD1-3.6=\n";
        transforms += "PD1-3.7=\n";
        transforms += "PD1-3.8=\n";
        transforms += "PD1-3.9=\n";
        transforms += "PD1-3.10=\n";
      }
    } else if (issue == Issue.PATIENT_PRIMARY_LANGUAGE_IS_DEPRECATED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-15=En\n";
      }
    } else if (issue == Issue.PATIENT_PRIMARY_LANGUAGE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-15=(555)555-1123\n";
      }
    } else if (issue == Issue.PATIENT_PRIMARY_LANGUAGE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-15=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-15=eng\n";
      }
    } else if (issue == Issue.PATIENT_PRIMARY_PHYSICIAN_ID_IS_DEPRECATED) {
      // TODO
    } else if (issue == Issue.PATIENT_PRIMARY_PHYSICIAN_ID_IS_IGNORED) {
      // TODO
    } else if (issue == Issue.PATIENT_PRIMARY_PHYSICIAN_ID_IS_INVALID) {
      // TODO
    } else if (issue == Issue.PATIENT_PRIMARY_PHYSICIAN_ID_IS_MISSING) {
      // TODO
    } else if (issue == Issue.PATIENT_PRIMARY_PHYSICIAN_ID_IS_UNRECOGNIZED) {
      // TODO
    } else if (issue == Issue.PATIENT_PRIMARY_PHYSICIAN_NAME_IS_MISSING) {
      // TODO
    } else if (issue == Issue.PATIENT_PROTECTION_INDICATOR_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-12=(555)555-1234\n";
      }
    } else if (issue == Issue.PATIENT_PROTECTION_INDICATOR_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-12=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-12=N\n";
      }
    } else if (issue == Issue.PATIENT_PROTECTION_INDICATOR_IS_VALUED_AS_NO) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-12=N\n";
      }
    } else if (issue == Issue.PATIENT_PROTECTION_INDICATOR_IS_VALUED_AS_YES) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        testCaseMessage.prepareMessageAddSegment("PD1", "PID");
        transforms += "PD1-12=Y\n";
      }
    } else if (issue == Issue.PATIENT_PUBLICITY_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-11=(505)555-1234\n";
      }
    } else if (issue == Issue.PATIENT_PUBLICITY_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-11=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-11=02\n";
      }
    } else if (issue == Issue.PATIENT_RACE_IS_DEPRECATED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-10=I\n";
      }
    } else if (issue == Issue.PATIENT_RACE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-10=108 N Main St\n";
      }
    } else if (issue == Issue.PATIENT_RACE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-10=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-10=1002-5\n";
      }
    } else if (issue == Issue.PATIENT_REGISTRY_STATUS_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-16=Immu-compromised\n";
      }
    } else if (issue == Issue.PATIENT_REGISTRY_STATUS_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-16=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PD1-16=A\n";
      }
    } else if (issue == Issue.PATIENT_SSN_IS_INVALID) {
      if(is)
      {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-3.1#2=123456789\n";
        transforms += "PID-3.4#2=USA\n";
        transforms += "PID-3.5#2=SS\n";
      }
    } else if (issue == Issue.PATIENT_SSN_IS_MISSING) {
      // TODO
    } else if (issue == Issue.PATIENT_SUBMITTER_ID_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-3=\n";
        transforms += "PID-3.2=\n";
        transforms += "PID-3.3=\n";
        transforms += "PID-3.4=\n";
        transforms += "PID-3.5=\n";
        transforms += "PID-3.6=\n";
        transforms += "PID-3.7=\n";
        transforms += "PID-3.8=\n";
        transforms += "PID-3.9=\n";
      }
    } else if (issue == Issue.PATIENT_SUBMITTER_ID_AUTHORITY_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-3.4=\n";
      }
    } else if (issue == Issue.PATIENT_SUBMITTER_ID_TYPE_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-3.5=\n";
      }
    } else if (issue == Issue.PATIENT_VFC_EFFECTIVE_DATE_IS_BEFORE_BIRTH) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        testCaseMessage.prepareMessageAddSegment("PV1", "PID");
        transforms += "PV1-20.1=V02\n";
        transforms += "PV1-20.2=19700101\n";
      }
    } else if (issue == Issue.PATIENT_VFC_EFFECTIVE_DATE_IS_IN_FUTURE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        testCaseMessage.prepareMessageAddSegment("PV1", "PID");
        transforms += "PV1-20.1=V02\n";
        transforms += "PV1-20.2=[FUTURE]\n";
      }
    } else if (issue == Issue.PATIENT_VFC_EFFECTIVE_DATE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        testCaseMessage.prepareMessageAddSegment("PV1", "PID");
        transforms += "PV1-20.1=V02\n";
        transforms += "PV1-20.2=Y\n";
      }
    } else if (issue == Issue.PATIENT_VFC_EFFECTIVE_DATE_IS_MISSING) {
      if (is) {
        testCaseMessage.prepareMessageAddSegment("PV1", "PID");
        testCaseMessage.setHasIssue(true);
        transforms += "PV1-20.1=V02\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PV1-20.1=V02\n";
        transforms += "PV1-20.2=[VAC3_DATE]\n";
      }
    } else if (issue == Issue.PATIENT_VFC_STATUS_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PV1-20.1=MMR\n";
        transforms += "PV1-20.2=[VAC3_DATE]\n";
      }
    } else if (issue == Issue.PATIENT_VFC_STATUS_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PV1-20.1=V00\n";
      }
    } else if (issue == Issue.PATIENT_VFC_STATUS_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PV1-20.1=\n";
        transforms += "PV1-20.2=[VAC3_DATE]\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PV1-20.1=V02\n";
        transforms += "PV1-20.2=[VAC3_DATE]\n";
      }
    }
    return transforms;
  }
}
