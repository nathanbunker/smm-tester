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
public class IssueCreatorVaccination extends IssueCreator
{

  public static String createTransforms(TestCaseMessage testCaseMessage, Issue issue, String transforms, boolean not) {
    boolean is = !not;
    if (issue == Issue.VACCINATION_ACTION_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-21=[DOB]\n";
      }
    } else if (issue == Issue.VACCINATION_ACTION_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-21=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-21=A\n";
      }
    } else if (issue == Issue.VACCINATION_ACTION_CODE_IS_VALUED_AS_ADD) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-21=A\n";
      }
    } else if (issue == Issue.VACCINATION_ACTION_CODE_IS_VALUED_AS_DELETE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-21=D\n";
      }
    } else if (issue == Issue.VACCINATION_ACTION_CODE_IS_VALUED_AS_UPDATE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-21=U\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_DEPRECATED) {
      // TODO
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_IGNORED) {
      // TODO
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-5=PCV\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-5=\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_NOT_SPECIFIC) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=45\n";
        transforms += "RXA-5.2=Hep B, unspecified formulation\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_NOT_VACCINE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=71\n";
        transforms += "RXA-5.2=RSV-IGIV\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=DTaP\n";
        transforms += "RXA-5.2=\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_TABLE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.3=CVX CODE\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_TABLE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.3=\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_INVALID_FOR_DATE_ADMINISTERED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=01\n";
        transforms += "RXA-5.2=DTP\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_UNEXPECTED_FOR_DATE_ADMINISTERED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=100\n";
        transforms += "RXA-5.2=pneumococcal conjugate PCV 7\n";
        transforms += "RXA-3=20100301";
        transforms += "PID-7=20011010";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_BEFORE_OR_AFTER_LICENSED_VACCINE_RANGE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=01\n";
        transforms += "RXA-5.2=DTP\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_VALUED_AS_NOT_ADMINISTERED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-5=998\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_IS_VALUED_AS_UNKNOWN) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-5=999\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_CODE_MAY_BE_VARIATION_OF_PREVIOUSLY_REPORTED_CODES) {
      // TODO
    }  else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_REPORTED_LATE) {
      // TODO
    }else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_AFTER_LOT_EXPIRATION_DATE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-16=[DOB]\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_AFTER_MESSAGE_SUBMITTED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-3=[FUTURE]\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_AFTER_PATIENT_DEATH_DATE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=[DOB]\n";
        transforms += "PID-30=Y\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "PID-29=[TODAY]\n";
        transforms += "PID-30=Y\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_AFTER_SYSTEM_ENTRY_DATE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-22=[DOB]\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_BEFORE_BIRTH) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-3=19700101\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_BEFORE_OR_AFTER_EXPECTED_VACCINE_USAGE_RANGE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=100\n";
        transforms += "RXA-5.2=pneumococcal conjugate PCV 7\n";
        transforms += "RXA-3=20100301";
        transforms += "PID-7=20011010";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_BEFORE_OR_AFTER_WHEN_EXPECTED_FOR_PATIENT_AGE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=118\n";
        transforms += "RXA-5.2=HPV, bivalent\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_BEFORE_OR_AFTER_WHEN_VALID_FOR_PATIENT_AGE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=118\n";
        transforms += "RXA-5.2=HPV, bivalent\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-3=MMR\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-3=\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_END_IS_DIFFERENT_FROM_START_DATE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-4=[DOB]\n";
      }
    } else if (issue == Issue.VACCINATION_ADMIN_DATE_END_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-4=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-3=[VAC3_DATE]\n";
        transforms += "RXA#2-4=[VAC3_DATE]\n";
      }
    } else if (issue == Issue.VACCINATION_ADMINISTERED_AMOUNT_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=MMR\n";
        transforms += "RXA#2-7=ML\n";
        transforms += "RXA#2-7.3=ISO+\n";
      }
    } else if (issue == Issue.VACCINATION_ADMINISTERED_AMOUNT_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=\n";
        transforms += "RXA#2-7=ML\n";
        transforms += "RXA#2-7.3=ISO+\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=0.5\n";
        transforms += "RXA#2-7=ML\n";
        transforms += "RXA#2-7.3=ISO+\n";
      }
    } else if (issue == Issue.VACCINATION_ADMINISTERED_AMOUNT_IS_VALUED_AS_ZERO) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=0\n";
        transforms += "RXA#2-7=ML\n";
        transforms += "RXA#2-7.3=ISO+\n";
      }
    } else if (issue == Issue.VACCINATION_ADMINISTERED_AMOUNT_IS_VALUED_AS_UNKNOWN) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=999\n";
      }
    } else if (issue == Issue.VACCINATION_ADMINISTERED_UNIT_IS_DEPRECATED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=MMR\n";
        transforms += "RXA#2-7=CC\n";
      }
    } else if (issue == Issue.VACCINATION_ADMINISTERED_UNIT_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=0.5\n";
        transforms += "RXA#2-7=CM\n";
        transforms += "RXA#2-7.3=ISO+\n";
      }
    } else if (issue == Issue.VACCINATION_ADMINISTERED_UNIT_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=0.5\n";
        transforms += "RXA#2-7=\n";
        transforms += "RXA#2-7.2=\n";
        transforms += "RXA#2-7.3=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=0.5\n";
        transforms += "RXA#2-7=ML\n";
        transforms += "RXA#2-7.3=ISO+\n";
      }
    } else if (issue == Issue.VACCINATION_ADMINISTERED_UNIT_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-6=0.5\n";
        transforms += "RXA#2-7=mililiter\n";
        transforms += "RXA#2-7.3=ISO+\n";
      }
    } else if (issue == Issue.VACCINATION_BODY_ROUTE_IS_DEPRECATED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXR-1=IN\n";
        transforms += "RXR-1.2=Intranasal\n";
        transforms += "RXR-1.3=HL70162\n";
      }
    } else if (issue == Issue.VACCINATION_BODY_ROUTE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXR-1=MMR\n";
        transforms += "RXR-1.3=HL70162\n";
      }
    } else if (issue == Issue.VACCINATION_BODY_ROUTE_IS_INVALID_FOR_VACCINE_INDICATED) {
      // TODO
    } else if (issue == Issue.VACCINATION_BODY_ROUTE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXR-1=\n";
        transforms += "RXR-1.3=HL70162\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXR-1=IM\n";
        transforms += "RXR-1.2=Intramuscular\n";
        transforms += "RXR-1.3=HL70162\n";
      }
    } else if (issue == Issue.VACCINATION_BODY_SITE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXR-2=Jones Clinic\n";
        transforms += "RXR-2.2=\n";
        transforms += "RXR-2.3=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXR-2=LA\n";
        transforms += "RXR-2.2=Left Arm\n";
        transforms += "RXR-2.3=HL70163\n";
      }
    } else if (issue == Issue.VACCINATION_BODY_SITE_IS_INVALID_FOR_VACCINE_INDICATED) {
      // TODO
    } else if (issue == Issue.VACCINATION_BODY_SITE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXR-2=\n";
        transforms += "RXR-2.2=\n";
        transforms += "RXR-2.3=HL70163\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXR-2=LA\n";
        transforms += "RXR-2.2=Left Arm\n";
        transforms += "RXR-2.3=HL70163\n";
      }
    } else if (issue == Issue.VACCINATION_BODY_SITE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXR-2=Left Arm\n";
        transforms += "RXR-2.2=Left Arm\n";
        transforms += "RXR-2.3=HL70163\n";
      }
    } else if (issue == Issue.VACCINATION_COMPLETION_STATUS_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=MMR\n";
        transforms += "RXA#2-18=\n";
      }
    } else if (issue == Issue.VACCINATION_COMPLETION_STATUS_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=\n";
        transforms += "RXA#2-18=00\n";
        transforms += "RXA#2-18.2=Parental Decision\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=RE\n";
        transforms += "RXA#2-18=00\n";
        transforms += "RXA#2-18.2=Parental Decision\n";
      }
    } else if (issue == Issue.VACCINATION_COMPLETION_STATUS_IS_VALUED_AS_COMPLETED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=CP\n";
        transforms += "RXA#2-18=\n";
        transforms += "RXA#2-18.2=\n";
      }
    } else if (issue == Issue.VACCINATION_COMPLETION_STATUS_IS_VALUED_AS_NOT_ADMINISTERED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=NA\n";
        transforms += "RXA#2-18=\n";
        transforms += "RXA#2-18.2=\n";
      }
    } else if (issue == Issue.VACCINATION_COMPLETION_STATUS_IS_VALUED_AS_PARTIALLY_ADMINISTERED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=PA\n";
        transforms += "RXA#2-18=\n";
        transforms += "RXA#2-18.2=\n";
      }
    } else if (issue == Issue.VACCINATION_COMPLETION_STATUS_IS_VALUED_AS_REFUSED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=RE\n";
        transforms += "RXA#2-18=\n";
        transforms += "RXA#2-18.2=\n";
      }
    } else if (issue == Issue.VACCINATION_CONFIDENTIALITY_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC#2-28=Block\n";
        transforms += "ORC#2-28.2=\n";
        transforms += "ORC#2-28.3=HL70177\n";
      }
    } else if (issue == Issue.VACCINATION_CONFIDENTIALITY_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC#2-28=\n";
        transforms += "ORC#2-28.2=\n";
        transforms += "ORC#2-28.3=HL70177\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC#2-28=U\n";
        transforms += "ORC#2-28.2=Usual\n";
        transforms += "ORC#2-28.3=HL70177\n";
      }
    } else if (issue == Issue.VACCINATION_CONFIDENTIALITY_CODE_IS_VALUED_AS_RESTRICTED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC#2-28=R\n";
        transforms += "ORC#2-28.2=\n";
        transforms += "ORC#2-28.3=HL70177\n";
      }
    } else if (issue == Issue.VACCINATION_CPT_CODE_IS_DEPRECATED) {
      // TODO
    } else if (issue == Issue.VACCINATION_CPT_CODE_IS_IGNORED) {
      // TODO
    } else if (issue == Issue.VACCINATION_CPT_CODE_IS_INVALID) {
      // TODO
    } else if (issue == Issue.VACCINATION_CPT_CODE_IS_MISSING) {
      // TODO
    } else if (issue == Issue.VACCINATION_CPT_CODE_IS_UNRECOGNIZED) {
      // TODO
    } else if (issue == Issue.VACCINATION_CVX_CODE_IS_DEPRECATED) {
      // TODO
    } else if (issue == Issue.VACCINATION_CVX_CODE_IS_IGNORED) {
      // TODO
    } else if (issue == Issue.VACCINATION_CVX_CODE_IS_INVALID) {
      // TODO
    } else if (issue == Issue.VACCINATION_CVX_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=\n";
      }
      // TODO
    } else if (issue == Issue.VACCINATION_CVX_CODE_IS_UNRECOGNIZED) {
      // TODO
    } else if (issue == Issue.VACCINATION_CVX_CODE_AND_CPT_CODE_ARE_INCONSISTENT) {
      // TODO
    } else if (issue == Issue.VACCINATION_FACILITY_ID_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-11.1=\n";
        transforms += "RXA#2-11.4=\n";
      }
    } else if (issue == Issue.VACCINATION_FACILITY_ID_IS_UNRECOGNIZED) {
      // TODO
    } else if (issue == Issue.VACCINATION_FACILITY_NAME_IS_MISSING) {
      // TODO
    } else if (issue == Issue.VACCINATION_FILLER_ORDER_NUMBER_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC-3=\n";
      }
    } else if (issue == Issue.VACCINATION_FINANCIAL_ELIGIBILITY_CODE_IS_DEPRECATED) {
      // TODO
    } else if (issue == Issue.VACCINATION_FINANCIAL_ELIGIBILITY_CODE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "OBX-5.1=V00\n";
        transforms += "OBX-5.2=VFC eligibility unknown\n";
      }
    } else if (issue == Issue.VACCINATION_FINANCIAL_ELIGIBILITY_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "OBX-5.1=\n";
        transforms += "OBX-5.2=\n";
        transforms += "OBX-5.3=\n";
      }
    } else if (issue == Issue.VACCINATION_FINANCIAL_ELIGIBILITY_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "OBX-5.1=ZZZ01\n";
        transforms += "OBX-5.2=VFC Eligible\n";
        transforms += "OBX-5.3=\n";
      }
    } else if (issue == Issue.VACCINATION_GIVEN_BY_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-10.1=\n";
        transforms += "RXA#2-10.2=\n";
        transforms += "RXA#2-10.3=\n";
        transforms += "RXA#2-10.4=\n";
        transforms += "RXA#2-10.5=\n";
        transforms += "RXA#2-10.6=\n";
        transforms += "RXA#2-10.7=\n";
        transforms += "RXA#2-10.8=\n";
        transforms += "RXA#2-10.9=\n";
        transforms += "RXA#2-10.10=\n";
        transforms += "RXA#2-10.11=\n";
        transforms += "RXA#2-10.12=\n";
        transforms += "RXA#2-10.13=\n";
        transforms += "RXA#2-10.14=\n";
        transforms += "RXA#2-10.15=\n";
        transforms += "RXA#2-10.16=\n";
        transforms += "RXA#2-10.17=\n";
        transforms += "RXA#2-10.18=\n";
        transforms += "RXA#2-10.19=\n";
        transforms += "RXA#2-10.20=\n";
        transforms += "RXA#2-10.21=\n";
        transforms += "RXA#2-10.22=\n";
        transforms += "RXA#2-10.23=\n";
      }

    } else if (issue == Issue.VACCINATION_ID_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC-2=\n";
        transforms += "ORC-3=\n";
      }
    } else if (issue == Issue.VACCINATION_ID_OF_RECEIVER_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC-3=\n";
      }
    } else if (issue == Issue.VACCINATION_ID_OF_SENDER_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC-2=\n";
      }
    } else if (issue == Issue.VACCINATION_INFORMATION_SOURCE_IS_ADMINISTERED_BUT_APPEARS_TO_HISTORICAL) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-9=00\n";
        transforms += "RXA-9.2=New immunization record\n";
        transforms += "RXA-9.3=NIP001\n";
      }
    } else if (issue == Issue.VACCINATION_INFORMATION_SOURCE_IS_HISTORICAL_BUT_APPEARS_TO_BE_ADMINISTERED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-9=01\n";
        transforms += "RXA#2-9.2=Historical information - source unspecified\n";
        transforms += "RXA#2-9.3=NIP001\n";
      }
    } else if (issue == Issue.VACCINATION_INFORMATION_SOURCE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-9=LA-SC\n";
      }
    } else if (issue == Issue.VACCINATION_INFORMATION_SOURCE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-9=\n";
        transforms += "RXA#2-9.2=\n";
        transforms += "RXA#2-9.3=\n";
      }
    } else if (issue == Issue.VACCINATION_INFORMATION_SOURCE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-9=Given\n";
      }
    } else if (issue == Issue.VACCINATION_LOT_EXPIRATION_DATE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-16=Never\n";
      }
    } else if (issue == Issue.VACCINATION_LOT_EXPIRATION_DATE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-16=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-16=[FUTURE]\n";
      }
    } else if (issue == Issue.VACCINATION_LOT_NUMBER_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-15=Lot\n";
      }
    } else if (issue == Issue.VACCINATION_LOT_NUMBER_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-15=\n";
      }
    } else if (issue == Issue.VACCINATION_MANUFACTURER_CODE_IS_DEPRECATED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-17.1=BA\n";
        transforms += "RXA#2-17.2=Baxter\n";
        transforms += "RXA#2-17.3=MVX\n";
      }
    } else if (issue == Issue.VACCINATION_MANUFACTURER_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-17.1=Lot #34TTY9\n";
      }
    } else if (issue == Issue.VACCINATION_MANUFACTURER_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-17.1=\n";
        transforms += "RXA#2-17.2=\n";
        transforms += "RXA#2-17.3=\n";
      }
    } else if (issue == Issue.VACCINATION_ORDER_CONTROL_CODE_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC-1=20110506\n";
      }
    } else if (issue == Issue.VACCINATION_ORDER_CONTROL_CODE_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC-1=\n";
      }
    } else if (issue == Issue.VACCINATION_ORDER_CONTROL_CODE_IS_UNRECOGNIZED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "ORC-1=UR\n";
      }
    } else if (issue == Issue.VACCINATION_ORDER_FACILITY_ID_IS_MISSING) {
      // TODO
    } else if (issue == Issue.VACCINATION_ORDER_FACILITY_NAME_IS_MISSING) {
      // TODO
    } else if (issue == Issue.VACCINATION_ORDERED_BY_IS_MISSING) {
      // TODO
    } else if (issue == Issue.VACCINATION_PLACER_ORDER_NUMBER_IS_MISSING) {
      // TODO
    } else if (issue == Issue.VACCINATION_PRODUCT_IS_DEPRECATED) {
      // TODO
    } else if (issue == Issue.VACCINATION_PRODUCT_IS_INVALID_FOR_DATE_ADMINISTERED) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA-5.1=02\n";
        transforms += "RXA-5.2=OPV\n";
        transforms += "RXA-17.1=WAL\n";
        transforms += "RXA-17.2=Wyeth\n";
      }
    } else if (issue == Issue.VACCINATION_PRODUCT_IS_MISSING) {
      // TODO
    } else if (issue == Issue.VACCINATION_PRODUCT_IS_UNRECOGNIZED) {
      // TODO
    } else if (issue == Issue.VACCINATION_RECORDED_BY_IS_MISSING) {
      // TODO
    } else if (issue == Issue.VACCINATION_REFUSAL_REASON_CONFLICTS_COMPLETION_STATUS) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=CP\n";
        transforms += "RXA#2-18=00\n";
        transforms += "RXA#2-18.2=Parental Decision\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=RE\n";
        transforms += "RXA#2-18=00\n";
        transforms += "RXA#2-18.2=Parental Decision\n";
      }

    } else if (issue == Issue.VACCINATION_REFUSAL_REASON_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=RE\n";
        transforms += "RXA#2-18=\n";
        transforms += "RXA#2-18.2=\n";

      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-20=RE\n";
        transforms += "RXA#2-18=00\n";
        transforms += "RXA#2-18.2=Parental Decision\n";
      }
    } else if (issue == Issue.VACCINATION_SYSTEM_ENTRY_TIME_IS_IN_FUTURE) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-22=[FUTURE]\n";
      }
    } else if (issue == Issue.VACCINATION_SYSTEM_ENTRY_TIME_IS_INVALID) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-22=Harvey\n";
      }
    } else if (issue == Issue.VACCINATION_SYSTEM_ENTRY_TIME_IS_MISSING) {
      if (is) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-22=\n";
      }
      if (not) {
        testCaseMessage.setHasIssue(true);
        transforms += "RXA#2-22=[NOW]\n";
      }
    }
    return transforms;
  }
}
