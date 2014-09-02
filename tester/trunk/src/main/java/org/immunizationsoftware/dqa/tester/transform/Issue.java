package org.immunizationsoftware.dqa.tester.transform;

import org.immunizationsoftware.dqa.tester.run.ErrorType;

public enum Issue {
  HL7_MSH_ACCEPT_ACK_TYPE_IS_DEPRECATED("HL7 MSH accept ack type is deprecated", ErrorType.WARNING, 4, "DQA0415"), HL7_MSH_ACCEPT_ACK_TYPE_IS_IGNORED(
      "HL7 MSH accept ack type is ignored", ErrorType.UNKNOWN, 4, "DQA0417"), HL7_MSH_ACCEPT_ACK_TYPE_IS_INVALID(
      "HL7 MSH accept ack type is invalid", ErrorType.WARNING, 4, "DQA0416"), HL7_MSH_ACCEPT_ACK_TYPE_IS_MISSING(
      "HL7 MSH accept ack type is missing", ErrorType.ACCEPT, 3, "DQA0006"), HL7_MSH_ACCEPT_ACK_TYPE_IS_UNRECOGNIZED(
      "HL7 MSH accept ack type is unrecognized", ErrorType.WARNING, 2, "DQA0007"), HL7_MSH_ACCEPT_ACK_TYPE_IS_VALUED_AS_ALWAYS(
      "HL7 MSH accept ack type is valued as always", ErrorType.ACCEPT, 3, "DQA0008"), HL7_MSH_ACCEPT_ACK_TYPE_IS_VALUED_AS_NEVER(
      "HL7 MSH accept ack type is valued as never", ErrorType.ACCEPT, 3, "DQA0009"), HL7_MSH_ACCEPT_ACK_TYPE_IS_VALUED_AS_ONLY_ON_ERRORS(
      "HL7 MSH accept ack type is valued as only on errors", ErrorType.ACCEPT, 3, "DQA0010"), HL7_MSH_ALT_CHARACTER_SET_IS_DEPRECATED(
      "HL7 MSH alt character set is deprecated", ErrorType.WARNING, 4, "DQA0431"), HL7_MSH_ALT_CHARACTER_SET_IS_IGNORED(
      "HL7 MSH alt character set is ignored", ErrorType.UNKNOWN, 4, "DQA0432"), HL7_MSH_ALT_CHARACTER_SET_IS_INVALID(
      "HL7 MSH alt character set is invalid", ErrorType.WARNING, 4, "DQA0433"), HL7_MSH_ALT_CHARACTER_SET_IS_MISSING(
      "HL7 MSH alt character set is missing", ErrorType.ACCEPT, 4, "DQA0434"), HL7_MSH_ALT_CHARACTER_SET_IS_UNRECOGNIZED(
      "HL7 MSH alt character set is unrecognized", ErrorType.WARNING, 4, "DQA0435"), HL7_MSH_APP_ACK_TYPE_IS_DEPRECATED(
      "HL7 MSH app ack type is deprecated", ErrorType.WARNING, 4, "DQA0418"), HL7_MSH_APP_ACK_TYPE_IS_IGNORED(
      "HL7 MSH app ack type is ignored", ErrorType.UNKNOWN, 4, "DQA0420"), HL7_MSH_APP_ACK_TYPE_IS_INVALID(
      "HL7 MSH app ack type is invalid", ErrorType.WARNING, 4, "DQA0419"), HL7_MSH_APP_ACK_TYPE_IS_MISSING(
      "HL7 MSH app ack type is missing", ErrorType.ACCEPT, 3, "DQA0410"), HL7_MSH_APP_ACK_TYPE_IS_UNRECOGNIZED(
      "HL7 MSH app ack type is unrecognized", ErrorType.WARNING, 2, "DQA0411"), HL7_MSH_APP_ACK_TYPE_IS_VALUED_AS_ALWAYS(
      "HL7 MSH app ack type is valued as always", ErrorType.ACCEPT, 3, "DQA0412"), HL7_MSH_APP_ACK_TYPE_IS_VALUED_AS_NEVER(
      "HL7 MSH app ack type is valued as never", ErrorType.ACCEPT, 3, "DQA0413"), HL7_MSH_APP_ACK_TYPE_IS_VALUED_AS_ONLY_ON_ERRORS(
      "HL7 MSH app ack type is valued as only on errors", ErrorType.ACCEPT, 3, "DQA0414"), HL7_MSH_CHARACTER_SET_IS_DEPRECATED(
      "HL7 MSH character set is deprecated", ErrorType.WARNING, 4, "DQA0426"), HL7_MSH_CHARACTER_SET_IS_IGNORED(
      "HL7 MSH character set is ignored", ErrorType.UNKNOWN, 4, "DQA0427"), HL7_MSH_CHARACTER_SET_IS_INVALID(
      "HL7 MSH character set is invalid", ErrorType.WARNING, 4, "DQA0428"), HL7_MSH_CHARACTER_SET_IS_MISSING(
      "HL7 MSH character set is missing", ErrorType.ACCEPT, 4, "DQA0429"), HL7_MSH_CHARACTER_SET_IS_UNRECOGNIZED(
      "HL7 MSH character set is unrecognized", ErrorType.WARNING, 4, "DQA0430"), HL7_MSH_COUNTRY_CODE_IS_DEPRECATED(
      "HL7 MSH country code is deprecated", ErrorType.WARNING, 4, "DQA0421"), HL7_MSH_COUNTRY_CODE_IS_IGNORED(
      "HL7 MSH country code is ignored", ErrorType.UNKNOWN, 4, "DQA0422"), HL7_MSH_COUNTRY_CODE_IS_INVALID(
      "HL7 MSH country code is invalid", ErrorType.WARNING, 4, "DQA0423"), HL7_MSH_COUNTRY_CODE_IS_MISSING(
      "HL7 MSH country code is missing", ErrorType.ACCEPT, 4, "DQA0424"), HL7_MSH_COUNTRY_CODE_IS_UNRECOGNIZED(
      "HL7 MSH country code is unrecognized", ErrorType.WARNING, 4, "DQA0425"), HL7_MSH_ENCODING_CHARACTER_IS_INVALID(
      "HL7 MSH encoding character is invalid", ErrorType.ERROR, 4, "DQA0011"), HL7_MSH_ENCODING_CHARACTER_IS_MISSING(
      "HL7 MSH encoding character is missing", ErrorType.ERROR, 4, "DQA0012"), HL7_MSH_ENCODING_CHARACTER_IS_NON_STANDARD(
      "HL7 MSH encoding character is non-standard", ErrorType.ERROR, 4, "DQA0013"), HL7_MSH_MESSAGE_CONTROL_ID_IS_MISSING(
      "HL7 MSH message control id is missing", ErrorType.ERROR, 2, "DQA0014"), HL7_MSH_MESSAGE_DATE_IS_IN_FUTURE(
      "HL7 MSH message date is in future", ErrorType.ERROR, 1, "DQA0015"), HL7_MSH_MESSAGE_DATE_IS_INVALID(
      "HL7 MSH message date is invalid", ErrorType.ERROR, 1, "DQA0016"), HL7_MSH_MESSAGE_DATE_IS_MISSING(
      "HL7 MSH message date is missing", ErrorType.ERROR, 1, "DQA0017"), HL7_MSH_MESSAGE_DATE_IS_MISSING_TIMEZONE(
      "HL7 MSH message date is missing timezone", ErrorType.WARNING, 2, "DQA0527"), HL7_MSH_MESSAGE_DATE_IS_NOT_PRECISE(
      "HL7 MSH message date is not precise", ErrorType.ACCEPT, 2, "DQA0526"), HL7_MSH_MESSAGE_PROFILE_ID_IS_DEPRECATED(
      "HL7 MSH message profile id is deprecated", ErrorType.WARNING, 4, "DQA0436"), HL7_MSH_MESSAGE_PROFILE_ID_IS_IGNORED(
      "HL7 MSH message profile id is ignored", ErrorType.UNKNOWN, 4, "DQA0437"), HL7_MSH_MESSAGE_PROFILE_ID_IS_INVALID(
      "HL7 MSH message profile id is invalid", ErrorType.WARNING, 4, "DQA0438"), HL7_MSH_MESSAGE_PROFILE_ID_IS_MISSING(
      "HL7 MSH message profile id is missing", ErrorType.ACCEPT, 3, "DQA0439"), HL7_MSH_MESSAGE_PROFILE_ID_IS_UNRECOGNIZED(
      "HL7 MSH message profile id is unrecognized", ErrorType.WARNING, 4, "DQA0440"), HL7_MSH_MESSAGE_STRUCTURE_IS_MISSING(
      "HL7 MSH message structure is missing", ErrorType.ERROR, 2, "DQA0391"), HL7_MSH_MESSAGE_STRUCTURE_IS_UNRECOGNIZED(
      "HL7 MSH message structure is unrecognized", ErrorType.ERROR, 2, "DQA0392"), HL7_MSH_MESSAGE_TRIGGER_IS_MISSING(
      "HL7 MSH message trigger is missing", ErrorType.ERROR, 2, "DQA0018"), HL7_MSH_MESSAGE_TRIGGER_IS_UNRECOGNIZED(
      "HL7 MSH message trigger is unrecognized", ErrorType.ERROR, 1, "DQA0019"), HL7_MSH_MESSAGE_TRIGGER_IS_UNSUPPORTED(
      "HL7 MSH message trigger is unsupported", ErrorType.ERROR, 4, "DQA0525"), HL7_MSH_MESSAGE_TYPE_IS_MISSING(
      "HL7 MSH message type is missing", ErrorType.ERROR, 1, "DQA0020"), HL7_MSH_MESSAGE_TYPE_IS_UNRECOGNIZED(
      "HL7 MSH message type is unrecognized", ErrorType.ERROR, 1, "DQA0021"), HL7_MSH_MESSAGE_TYPE_IS_UNSUPPORTED(
      "HL7 MSH message type is unsupported", ErrorType.ERROR, 4, "DQA0022"), HL7_MSH_PROCESSING_ID_IS_DEPRECATED(
      "HL7 MSH processing id is deprecated", ErrorType.WARNING, 4, "DQA0403"), HL7_MSH_PROCESSING_ID_IS_IGNORED(
      "HL7 MSH processing id is ignored", ErrorType.UNKNOWN, 3, "DQA0404"), HL7_MSH_PROCESSING_ID_IS_INVALID(
      "HL7 MSH processing id is invalid", ErrorType.ERROR, 4, "DQA0402"), HL7_MSH_PROCESSING_ID_IS_MISSING(
      "HL7 MSH processing id is missing", ErrorType.ERROR, 3, "DQA0023"), HL7_MSH_PROCESSING_ID_IS_UNRECOGNIZED(
      "HL7 MSH processing id is unrecognized", ErrorType.ERROR, 2, "DQA0401"), HL7_MSH_PROCESSING_ID_IS_UNSUPPORTED(
      "HL7 MSH processing id is unsupported", ErrorType.ERROR, 4, "DQA0524"), HL7_MSH_PROCESSING_ID_IS_VALUED_AS_DEBUG(
      "HL7 MSH processing id is valued as debug", ErrorType.WARNING, 4, "DQA0024"), HL7_MSH_PROCESSING_ID_IS_VALUED_AS_PRODUCTION(
      "HL7 MSH processing id is valued as production", ErrorType.ACCEPT, 4, "DQA0025"), HL7_MSH_PROCESSING_ID_IS_VALUED_AS_TRAINING(
      "HL7 MSH processing id is valued as training", ErrorType.WARNING, 4, "DQA0026"), HL7_MSH_RECEIVING_APPLICATION_IS_INVALID(
      "HL7 MSH receiving application is invalid", ErrorType.WARNING, 4, "DQA0029"), HL7_MSH_RECEIVING_APPLICATION_IS_MISSING(
      "HL7 MSH receiving application is missing", ErrorType.ACCEPT, 4, "DQA0030"), HL7_MSH_RECEIVING_FACILITY_IS_INVALID(
      "HL7 MSH receiving facility is invalid", ErrorType.WARNING, 4, "DQA0031"), HL7_MSH_RECEIVING_FACILITY_IS_MISSING(
      "HL7 MSH receiving facility is missing", ErrorType.ACCEPT, 4, "DQA0032"), HL7_MSH_SEGMENT_IS_MISSING(
      "HL7 MSH segment is missing", ErrorType.ERROR, 1, "DQA0033"), HL7_MSH_SENDING_APPLICATION_IS_INVALID(
      "HL7 MSH sending application is invalid", ErrorType.WARNING, 4, "DQA0034"), HL7_MSH_SENDING_APPLICATION_IS_MISSING(
      "HL7 MSH sending application is missing", ErrorType.ACCEPT, 4, "DQA0035"), HL7_MSH_SENDING_FACILITY_IS_INVALID(
      "HL7 MSH sending facility is invalid", ErrorType.WARNING, 4, "DQA0036"), HL7_MSH_SENDING_FACILITY_IS_MISSING(
      "HL7 MSH sending facility is missing", ErrorType.ACCEPT, 4, "DQA0037"), HL7_MSH_VERSION_IS_INVALID(
      "HL7 MSH version is invalid", ErrorType.WARNING, 4, "DQA0523"), HL7_MSH_VERSION_IS_MISSING(
      "HL7 MSH version is missing", ErrorType.ERROR, 2, "DQA0038"), HL7_MSH_VERSION_IS_UNRECOGNIZED(
      "HL7 MSH version is unrecognized", ErrorType.ERROR, 2, "DQA0039"), HL7_MSH_VERSION_IS_VALUED_AS_2_3_1(
      "HL7 MSH version is valued as 2.3.1", ErrorType.ACCEPT, 2, "DQA0040"), HL7_MSH_VERSION_IS_VALUED_AS_2_4(
      "HL7 MSH version is valued as 2.4", ErrorType.ACCEPT, 2, "DQA0041"), HL7_MSH_VERSION_IS_VALUED_AS_2_5(
      "HL7 MSH version is valued as 2.5", ErrorType.ACCEPT, 2, "DQA0042"), HL7_NK1_SEGMENT_IS_MISSING(
      "HL7 NK1 segment is missing", ErrorType.WARNING, 2, "DQA0043"), HL7_NK1_SEGMENT_IS_REPEATED(
      "HL7 NK1 segment is repeated", ErrorType.ACCEPT, 3, "DQA0044"), HL7_NK1_SET_ID_IS_MISSING(
      "HL7 NK1 set id is missing", ErrorType.UNKNOWN, 3, "DQA0368"), HL7_OBX_SEGMENT_IS_MISSING(
      "HL7 OBX segment is missing", ErrorType.ACCEPT, 1, "DQA0045"), HL7_ORC_SEGMENT_IS_MISSING(
      "HL7 ORC segment is missing", ErrorType.WARNING, 3, "DQA0046"), HL7_ORC_SEGMENT_IS_REPEATED(
      "HL7 ORC segment is repeated", ErrorType.ERROR, 1, "DQA0047"), HL7_PD1_SEGMENT_IS_MISSING(
      "HL7 PD1 segment is missing", ErrorType.ACCEPT, 3, "DQA0048"), HL7_PID_SEGMENT_IS_MISSING(
      "HL7 PID segment is missing", ErrorType.ERROR, 1, "DQA0049"), HL7_PID_SEGMENT_IS_REPEATED(
      "HL7 PID segment is repeated", ErrorType.WARNING, 1, "DQA0050"), HL7_PV1_SEGMENT_IS_MISSING(
      "HL7 PV1 segment is missing", ErrorType.ACCEPT, 3, "DQA0051"), HL7_PV1_SEGMENT_IS_REPEATED(
      "HL7 PV1 segment is repeated", ErrorType.WARNING, 1, "DQA0400"), HL7_RXA_ADMIN_SUB_ID_COUNTER_IS_MISSING(
      "HL7 RXA admin sub id counter is missing", ErrorType.UNKNOWN, 3, "DQA0390"), HL7_RXA_GIVE_SUB_ID_IS_MISSING(
      "HL7 RXA give sub id is missing", ErrorType.UNKNOWN, 2, "DQA0389"), HL7_RXA_SEGMENT_IS_MISSING(
      "HL7 RXA segment is missing", ErrorType.UNKNOWN, 1, "DQA0052"), HL7_RXA_SEGMENT_IS_REPEATED(
      "HL7 RXA segment is repeated", ErrorType.ACCEPT, 4, "DQA0053"), HL7_RXR_SEGMENT_IS_MISSING(
      "HL7 RXR segment is missing", ErrorType.ACCEPT, 2, "DQA0054"), HL7_RXR_SEGMENT_IS_REPEATED(
      "HL7 RXR segment is repeated", ErrorType.ACCEPT, 2, "DQA0055"), HL7_SEGMENT_IS_INVALID("HL7 segment is invalid",
      ErrorType.WARNING, 4, "DQA0464"), HL7_SEGMENT_IS_UNRECOGNIZED("HL7 segment is unrecognized", ErrorType.WARNING,
      4, "DQA0463"), HL7_SEGMENTS_OUT_OF_ORDER("HL7 segments out of order", ErrorType.WARNING, 4, "DQA0452"), NEXT_OF_KIN_ADDRESS_CITY_IS_INVALID(
      "Next-of-kin address city is invalid", ErrorType.WARNING, 2, "DQA0058"), NEXT_OF_KIN_ADDRESS_CITY_IS_MISSING(
      "Next-of-kin address city is missing", ErrorType.ACCEPT, 2, "DQA0059"), NEXT_OF_KIN_ADDRESS_CITY_IS_TOO_LONG(
      "Next-of-kin address city is too long", ErrorType.UNKNOWN, 3, "DQA0572"), NEXT_OF_KIN_ADDRESS_CITY_IS_TOO_SHORT(
      "Next-of-kin address city is too short", ErrorType.UNKNOWN, 3, "DQA0569"), NEXT_OF_KIN_ADDRESS_CITY_IS_UNEXPECTEDLY_LONG(
      "Next-of-kin address city is unexpectedly long", ErrorType.UNKNOWN, 2, "DQA0571"), NEXT_OF_KIN_ADDRESS_CITY_IS_UNEXPECTEDLY_SHORT(
      "Next-of-kin address city is unexpectedly short", ErrorType.UNKNOWN, 3, "DQA0570"), NEXT_OF_KIN_ADDRESS_COUNTRY_IS_DEPRECATED(
      "Next-of-kin address country is deprecated", ErrorType.WARNING, 4, "DQA0060"), NEXT_OF_KIN_ADDRESS_COUNTRY_IS_IGNORED(
      "Next-of-kin address country is ignored", ErrorType.UNKNOWN, 4, "DQA0061"), NEXT_OF_KIN_ADDRESS_COUNTRY_IS_INVALID(
      "Next-of-kin address country is invalid", ErrorType.WARNING, 4, "DQA0062"), NEXT_OF_KIN_ADDRESS_COUNTRY_IS_MISSING(
      "Next-of-kin address country is missing", ErrorType.ACCEPT, 3, "DQA0063"), NEXT_OF_KIN_ADDRESS_COUNTRY_IS_UNRECOGNIZED(
      "Next-of-kin address country is unrecognized", ErrorType.WARNING, 2, "DQA0064"), NEXT_OF_KIN_ADDRESS_COUNTY_IS_DEPRECATED(
      "Next-of-kin address county is deprecated", ErrorType.WARNING, 4, "DQA0065"), NEXT_OF_KIN_ADDRESS_COUNTY_IS_IGNORED(
      "Next-of-kin address county is ignored", ErrorType.UNKNOWN, 4, "DQA0066"), NEXT_OF_KIN_ADDRESS_COUNTY_IS_INVALID(
      "Next-of-kin address county is invalid", ErrorType.WARNING, 4, "DQA0067"), NEXT_OF_KIN_ADDRESS_COUNTY_IS_MISSING(
      "Next-of-kin address county is missing", ErrorType.ACCEPT, 3, "DQA0068"), NEXT_OF_KIN_ADDRESS_COUNTY_IS_UNRECOGNIZED(
      "Next-of-kin address county is unrecognized", ErrorType.ACCEPT, 2, "DQA0069"), NEXT_OF_KIN_ADDRESS_IS_DIFFERENT_FROM_PATIENT_ADDRESS(
      "Next-of-kin address is different from patient address", ErrorType.ACCEPT, 3, "DQA0056"), NEXT_OF_KIN_ADDRESS_IS_MISSING(
      "Next-of-kin address is missing", ErrorType.ACCEPT, 2, "DQA0057"), NEXT_OF_KIN_ADDRESS_STATE_IS_DEPRECATED(
      "Next-of-kin address state is deprecated", ErrorType.WARNING, 4, "DQA0070"), NEXT_OF_KIN_ADDRESS_STATE_IS_IGNORED(
      "Next-of-kin address state is ignored", ErrorType.UNKNOWN, 4, "DQA0071"), NEXT_OF_KIN_ADDRESS_STATE_IS_INVALID(
      "Next-of-kin address state is invalid", ErrorType.WARNING, 4, "DQA0072"), NEXT_OF_KIN_ADDRESS_STATE_IS_MISSING(
      "Next-of-kin address state is missing", ErrorType.ACCEPT, 3, "DQA0073"), NEXT_OF_KIN_ADDRESS_STATE_IS_UNRECOGNIZED(
      "Next-of-kin address state is unrecognized", ErrorType.WARNING, 2, "DQA0074"), NEXT_OF_KIN_ADDRESS_STREET_IS_MISSING(
      "Next-of-kin address street is missing", ErrorType.ACCEPT, 3, "DQA0075"), NEXT_OF_KIN_ADDRESS_STREET2_IS_MISSING(
      "Next-of-kin address street2 is missing", ErrorType.ACCEPT, 3, "DQA0076"), NEXT_OF_KIN_ADDRESS_TYPE_IS_DEPRECATED(
      "Next-of-kin address type is deprecated", ErrorType.WARNING, 4, "DQA0395"), NEXT_OF_KIN_ADDRESS_TYPE_IS_IGNORED(
      "Next-of-kin address type is ignored", ErrorType.UNKNOWN, 4, "DQA0396"), NEXT_OF_KIN_ADDRESS_TYPE_IS_INVALID(
      "Next-of-kin address type is invalid", ErrorType.WARNING, 4, "DQA0397"), NEXT_OF_KIN_ADDRESS_TYPE_IS_MISSING(
      "Next-of-kin address type is missing", ErrorType.WARNING, 3, "DQA0398"), NEXT_OF_KIN_ADDRESS_TYPE_IS_UNRECOGNIZED(
      "Next-of-kin address type is unrecognized", ErrorType.WARNING, 2, "DQA0399"), NEXT_OF_KIN_ADDRESS_TYPE_IS_VALUED_BAD_ADDRESS(
      "Next-of-kin address type is valued bad address", ErrorType.UNKNOWN, 2, "DQA0522"), NEXT_OF_KIN_ADDRESS_ZIP_IS_INVALID(
      "Next-of-kin address zip is invalid", ErrorType.WARNING, 2, "DQA0077"), NEXT_OF_KIN_ADDRESS_ZIP_IS_MISSING(
      "Next-of-kin address zip is missing", ErrorType.ACCEPT, 2, "DQA0078"), NEXT_OF_KIN_NAME_FIRST_IS_MISSING(
      "Next-of-kin name first is missing", ErrorType.WARNING, 2, "DQA0080"), NEXT_OF_KIN_NAME_FIRST_IS_TOO_LONG(
      "Next-of-kin name first is too long", ErrorType.UNKNOWN, 4, "DQA0568"), NEXT_OF_KIN_NAME_FIRST_IS_TOO_SHORT(
      "Next-of-kin name first is too short", ErrorType.UNKNOWN, 4, "DQA0565"), NEXT_OF_KIN_NAME_FIRST_IS_UNEXPECTEDLY_LONG(
      "Next-of-kin name first is unexpectedly long", ErrorType.UNKNOWN, 4, "DQA0567"), NEXT_OF_KIN_NAME_FIRST_IS_UNEXPECTEDLY_SHORT(
      "Next-of-kin name first is unexpectedly short", ErrorType.UNKNOWN, 4, "DQA0566"), NEXT_OF_KIN_NAME_IS_MISSING(
      "Next-of-kin name is missing", ErrorType.UNKNOWN, 2, "DQA0079"), NEXT_OF_KIN_NAME_LAST_IS_MISSING(
      "Next-of-kin name last is missing", ErrorType.WARNING, 2, "DQA0081"), NEXT_OF_KIN_NAME_LAST_IS_TOO_LONG(
      "Next-of-kin name last is too long", ErrorType.UNKNOWN, 4, "DQA0564"), NEXT_OF_KIN_NAME_LAST_IS_TOO_SHORT(
      "Next-of-kin name last is too short", ErrorType.UNKNOWN, 4, "DQA0561"), NEXT_OF_KIN_NAME_LAST_IS_UNEXPECTEDLY_LONG(
      "Next-of-kin name last is unexpectedly long", ErrorType.UNKNOWN, 4, "DQA0563"), NEXT_OF_KIN_NAME_LAST_IS_UNEXPECTEDLY_SHORT(
      "Next-of-kin name last is unexpectedly short", ErrorType.UNKNOWN, 4, "DQA0562"), NEXT_OF_KIN_PHONE_NUMBER_IS_INCOMPLETE(
      "Next-of-kin phone number is incomplete", ErrorType.WARNING, 2, "DQA0082"), NEXT_OF_KIN_PHONE_NUMBER_IS_INVALID(
      "Next-of-kin phone number is invalid", ErrorType.WARNING, 2, "DQA0083"), NEXT_OF_KIN_PHONE_NUMBER_IS_MISSING(
      "Next-of-kin phone number is missing", ErrorType.ACCEPT, 3, "DQA0084"), NEXT_OF_KIN_RELATIONSHIP_IS_DEPRECATED(
      "Next-of-kin relationship is deprecated", ErrorType.WARNING, 4, "DQA0085"), NEXT_OF_KIN_RELATIONSHIP_IS_IGNORED(
      "Next-of-kin relationship is ignored", ErrorType.UNKNOWN, 3, "DQA0086"), NEXT_OF_KIN_RELATIONSHIP_IS_INVALID(
      "Next-of-kin relationship is invalid", ErrorType.UNKNOWN, 4, "DQA0087"), NEXT_OF_KIN_RELATIONSHIP_IS_MISSING(
      "Next-of-kin relationship is missing", ErrorType.UNKNOWN, 2, "DQA0088"), NEXT_OF_KIN_RELATIONSHIP_IS_NOT_RESPONSIBLE_PARTY(
      "Next-of-kin relationship is not responsible party", ErrorType.UNKNOWN, 2, "DQA0089"), NEXT_OF_KIN_RELATIONSHIP_IS_UNEXPECTED(
      "Next-of-kin relationship is unexpected", ErrorType.WARNING, 2, "DQA0485"), NEXT_OF_KIN_RELATIONSHIP_IS_UNRECOGNIZED(
      "Next-of-kin relationship is unrecognized", ErrorType.UNKNOWN, 2, "DQA0090"), NEXT_OF_KIN_SSN_IS_MISSING(
      "Next-of-kin SSN is missing", ErrorType.ACCEPT, 3, "DQA0091"), OBSERVATION_DATE_TIME_OF_OBSERVATION_IS_INVALID(
      "Observation date time of observation is invalid", ErrorType.UNKNOWN, 4, "DQA0482"), OBSERVATION_DATE_TIME_OF_OBSERVATION_IS_MISSING(
      "Observation date time of observation is missing", ErrorType.UNKNOWN, 2, "DQA0481"), OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_DEPRECATED(
      "Observation observation identifier code is deprecated", ErrorType.WARNING, 4, "DQA0475"), OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_IGNORED(
      "Observation observation identifier code is ignored", ErrorType.UNKNOWN, 3, "DQA0476"), OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_INVALID(
      "Observation observation identifier code is invalid", ErrorType.UNKNOWN, 4, "DQA0477"), OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_MISSING(
      "Observation observation identifier code is missing", ErrorType.UNKNOWN, 2, "DQA0478"), OBSERVATION_OBSERVATION_IDENTIFIER_CODE_IS_UNRECOGNIZED(
      "Observation observation identifier code is unrecognized", ErrorType.UNKNOWN, 2, "DQA0479"), OBSERVATION_OBSERVATION_VALUE_IS_MISSING(
      "Observation observation value is missing", ErrorType.UNKNOWN, 2, "DQA0480"), OBSERVATION_VALUE_TYPE_IS_DEPRECATED(
      "Observation value type is deprecated", ErrorType.WARNING, 4, "DQA0470"), OBSERVATION_VALUE_TYPE_IS_IGNORED(
      "Observation value type is ignored", ErrorType.UNKNOWN, 3, "DQA0471"), OBSERVATION_VALUE_TYPE_IS_INVALID(
      "Observation value type is invalid", ErrorType.UNKNOWN, 4, "DQA0472"), OBSERVATION_VALUE_TYPE_IS_MISSING(
      "Observation value type is missing", ErrorType.UNKNOWN, 2, "DQA0473"), OBSERVATION_VALUE_TYPE_IS_UNRECOGNIZED(
      "Observation value type is unrecognized", ErrorType.UNKNOWN, 2, "DQA0474"), PATIENT_ADDRESS_CITY_IS_INVALID(
      "Patient address city is invalid", ErrorType.WARNING, 1, "DQA0093"), PATIENT_ADDRESS_CITY_IS_MISSING(
      "Patient address city is missing", ErrorType.WARNING, 1, "DQA0094"), PATIENT_ADDRESS_CITY_IS_TOO_LONG(
      "Patient address city is too long", ErrorType.UNKNOWN, 4, "DQA0560"), PATIENT_ADDRESS_CITY_IS_TOO_SHORT(
      "Patient address city is too short", ErrorType.UNKNOWN, 4, "DQA0557"), PATIENT_ADDRESS_CITY_IS_UNEXPECTEDLY_LONG(
      "Patient address city is unexpectedly long", ErrorType.UNKNOWN, 2, "DQA0559"), PATIENT_ADDRESS_CITY_IS_UNEXPECTEDLY_SHORT(
      "Patient address city is unexpectedly short", ErrorType.UNKNOWN, 4, "DQA0558"), PATIENT_ADDRESS_COUNTRY_IS_DEPRECATED(
      "Patient address country is deprecated", ErrorType.WARNING, 4, "DQA0095"), PATIENT_ADDRESS_COUNTRY_IS_IGNORED(
      "Patient address country is ignored", ErrorType.UNKNOWN, 4, "DQA0096"), PATIENT_ADDRESS_COUNTRY_IS_INVALID(
      "Patient address country is invalid", ErrorType.WARNING, 4, "DQA0097"), PATIENT_ADDRESS_COUNTRY_IS_MISSING(
      "Patient address country is missing", ErrorType.ACCEPT, 3, "DQA0098"), PATIENT_ADDRESS_COUNTRY_IS_UNRECOGNIZED(
      "Patient address country is unrecognized", ErrorType.WARNING, 2, "DQA0099"), PATIENT_ADDRESS_COUNTY_IS_DEPRECATED(
      "Patient address county is deprecated", ErrorType.WARNING, 4, "DQA0100"), PATIENT_ADDRESS_COUNTY_IS_IGNORED(
      "Patient address county is ignored", ErrorType.UNKNOWN, 4, "DQA0101"), PATIENT_ADDRESS_COUNTY_IS_INVALID(
      "Patient address county is invalid", ErrorType.WARNING, 2, "DQA0102"), PATIENT_ADDRESS_COUNTY_IS_MISSING(
      "Patient address county is missing", ErrorType.ACCEPT, 3, "DQA0103"), PATIENT_ADDRESS_COUNTY_IS_UNRECOGNIZED(
      "Patient address county is unrecognized", ErrorType.ACCEPT, 3, "DQA0104"), PATIENT_ADDRESS_IS_MISSING(
      "Patient address is missing", ErrorType.WARNING, 1, "DQA0092"), PATIENT_ADDRESS_STATE_IS_DEPRECATED(
      "Patient address state is deprecated", ErrorType.WARNING, 4, "DQA0105"), PATIENT_ADDRESS_STATE_IS_IGNORED(
      "Patient address state is ignored", ErrorType.UNKNOWN, 4, "DQA0106"), PATIENT_ADDRESS_STATE_IS_INVALID(
      "Patient address state is invalid", ErrorType.WARNING, 4, "DQA0107"), PATIENT_ADDRESS_STATE_IS_MISSING(
      "Patient address state is missing", ErrorType.WARNING, 1, "DQA0108"), PATIENT_ADDRESS_STATE_IS_UNRECOGNIZED(
      "Patient address state is unrecognized", ErrorType.WARNING, 1, "DQA0109"), PATIENT_ADDRESS_STREET_IS_MISSING(
      "Patient address street is missing", ErrorType.WARNING, 1, "DQA0110"), PATIENT_ADDRESS_STREET2_IS_MISSING(
      "Patient address street2 is missing", ErrorType.ACCEPT, 3, "DQA0111"), PATIENT_ADDRESS_TYPE_IS_DEPRECATED(
      "Patient address type is deprecated", ErrorType.WARNING, 4, "DQA0517"), PATIENT_ADDRESS_TYPE_IS_IGNORED(
      "Patient address type is ignored", ErrorType.UNKNOWN, 3, "DQA0518"), PATIENT_ADDRESS_TYPE_IS_INVALID(
      "Patient address type is invalid", ErrorType.ERROR, 4, "DQA0519"), PATIENT_ADDRESS_TYPE_IS_MISSING(
      "Patient address type is missing", ErrorType.ACCEPT, 2, "DQA0451"), PATIENT_ADDRESS_TYPE_IS_UNRECOGNIZED(
      "Patient address type is unrecognized", ErrorType.WARNING, 2, "DQA0520"), PATIENT_ADDRESS_TYPE_IS_VALUED_BAD_ADDRESS(
      "Patient address type is valued bad address", ErrorType.UNKNOWN, 2, "DQA0521"), PATIENT_ADDRESS_ZIP_IS_INVALID(
      "Patient address zip is invalid", ErrorType.WARNING, 2, "DQA0112"), PATIENT_ADDRESS_ZIP_IS_MISSING(
      "Patient address zip is missing", ErrorType.WARNING, 1, "DQA0113"), PATIENT_ALIAS_IS_MISSING(
      "Patient alias is missing", ErrorType.ACCEPT, 2, "DQA0114"), PATIENT_BIRTH_DATE_IS_AFTER_SUBMISSION(
      "Patient birth date is after submission", ErrorType.ERROR, 1, "DQA0115"), PATIENT_BIRTH_DATE_IS_IN_FUTURE(
      "Patient birth date is in future", ErrorType.ERROR, 1, "DQA0116"), PATIENT_BIRTH_DATE_IS_INVALID(
      "Patient birth date is invalid", ErrorType.ERROR, 1, "DQA0117"), PATIENT_BIRTH_DATE_IS_MISSING(
      "Patient birth date is missing", ErrorType.ERROR, 1, "DQA0118"), PATIENT_BIRTH_DATE_IS_UNDERAGE(
      "Patient birth date is underage", ErrorType.ACCEPT, 2, "DQA0119"), PATIENT_BIRTH_DATE_IS_VERY_LONG_AGO(
      "Patient birth date is very long ago", ErrorType.ERROR, 1, "DQA0120"), PATIENT_BIRTH_INDICATOR_IS_INVALID(
      "Patient birth indicator is invalid", ErrorType.WARNING, 2, "DQA0121"), PATIENT_BIRTH_INDICATOR_IS_MISSING(
      "Patient birth indicator is missing", ErrorType.ACCEPT, 3, "DQA0122"), PATIENT_BIRTH_ORDER_IS_INVALID(
      "Patient birth order is invalid", ErrorType.WARNING, 2, "DQA0123"), PATIENT_BIRTH_ORDER_IS_MISSING(
      "Patient birth order is missing", ErrorType.ACCEPT, 3, "DQA0124"), PATIENT_BIRTH_ORDER_IS_MISSING_AND_MULTIPLE_BIRTH_INDICATED(
      "Patient birth order is missing and multiple birth indicated", ErrorType.WARNING, 2, "DQA0125"), PATIENT_BIRTH_PLACE_IS_MISSING(
      "Patient birth place is missing", ErrorType.ACCEPT, 2, "DQA0126"), PATIENT_BIRTH_PLACE_IS_TOO_LONG(
      "Patient birth place is too long", ErrorType.UNKNOWN, 4, "DQA0556"), PATIENT_BIRTH_PLACE_IS_TOO_SHORT(
      "Patient birth place is too short", ErrorType.UNKNOWN, 4, "DQA0553"), PATIENT_BIRTH_PLACE_IS_UNEXPECTEDLY_LONG(
      "Patient birth place is unexpectedly long", ErrorType.UNKNOWN, 4, "DQA0555"), PATIENT_BIRTH_PLACE_IS_UNEXPECTEDLY_SHORT(
      "Patient birth place is unexpectedly short", ErrorType.UNKNOWN, 4, "DQA0554"), PATIENT_BIRTH_REGISTRY_ID_IS_INVALID(
      "Patient birth registry id is invalid", ErrorType.WARNING, 4, "DQA0127"), PATIENT_BIRTH_REGISTRY_ID_IS_MISSING(
      "Patient birth registry id is missing", ErrorType.ACCEPT, 2, "DQA0128"), PATIENT_CLASS_IS_DEPRECATED(
      "Patient class is deprecated", ErrorType.WARNING, 4, "DQA0374"), PATIENT_CLASS_IS_IGNORED(
      "Patient class is ignored", ErrorType.UNKNOWN, 4, "DQA0375"), PATIENT_CLASS_IS_INVALID(
      "Patient class is invalid", ErrorType.ERROR, 3, "DQA0376"), PATIENT_CLASS_IS_MISSING("Patient class is missing",
      ErrorType.UNKNOWN, 2, "DQA0377"), PATIENT_CLASS_IS_UNRECOGNIZED("Patient class is unrecognized",
      ErrorType.WARNING, 3, "DQA0378"), PATIENT_DEATH_DATE_IS_BEFORE_BIRTH("Patient death date is before birth",
      ErrorType.ERROR, 1, "DQA0129"), PATIENT_DEATH_DATE_IS_IN_FUTURE("Patient death date is in future",
      ErrorType.ERROR, 1, "DQA0130"), PATIENT_DEATH_DATE_IS_INVALID("Patient death date is invalid", ErrorType.ERROR,
      1, "DQA0131"), PATIENT_DEATH_DATE_IS_MISSING("Patient death date is missing", ErrorType.ACCEPT, 2, "DQA0132"), PATIENT_DEATH_INDICATOR_IS_INCONSISTENT(
      "Patient death indicator is inconsistent", ErrorType.WARNING, 1, "DQA0133"), PATIENT_DEATH_INDICATOR_IS_MISSING(
      "Patient death indicator is missing", ErrorType.ACCEPT, 1, "DQA0134"), PATIENT_ETHNICITY_IS_DEPRECATED(
      "Patient ethnicity is deprecated", ErrorType.WARNING, 2, "DQA0135"), PATIENT_ETHNICITY_IS_IGNORED(
      "Patient ethnicity is ignored", ErrorType.UNKNOWN, 4, "DQA0136"), PATIENT_ETHNICITY_IS_INVALID(
      "Patient ethnicity is invalid", ErrorType.WARNING, 4, "DQA0137"), PATIENT_ETHNICITY_IS_MISSING(
      "Patient ethnicity is missing", ErrorType.ACCEPT, 1, "DQA0138"), PATIENT_ETHNICITY_IS_UNRECOGNIZED(
      "Patient ethnicity is unrecognized", ErrorType.WARNING, 1, "DQA0139"), PATIENT_GENDER_IS_DEPRECATED(
      "Patient gender is deprecated", ErrorType.WARNING, 4, "DQA0143"), PATIENT_GENDER_IS_IGNORED(
      "Patient gender is ignored", ErrorType.UNKNOWN, 4, "DQA0144"), PATIENT_GENDER_IS_INVALID(
      "Patient gender is invalid", ErrorType.ERROR, 4, "DQA0145"), PATIENT_GENDER_IS_MISSING(
      "Patient gender is missing", ErrorType.WARNING, 1, "DQA0146"), PATIENT_GENDER_IS_UNRECOGNIZED(
      "Patient gender is unrecognized", ErrorType.WARNING, 1, "DQA0147"), PATIENT_GUARDIAN_ADDRESS_CITY_IS_MISSING(
      "Patient guardian address city is missing", ErrorType.ACCEPT, 2, "DQA0149"), PATIENT_GUARDIAN_ADDRESS_IS_MISSING(
      "Patient guardian address is missing", ErrorType.ACCEPT, 2, "DQA0148"), PATIENT_GUARDIAN_ADDRESS_STATE_IS_MISSING(
      "Patient guardian address state is missing", ErrorType.ACCEPT, 2, "DQA0150"), PATIENT_GUARDIAN_ADDRESS_STREET_IS_MISSING(
      "Patient guardian address street is missing", ErrorType.ACCEPT, 2, "DQA0151"), PATIENT_GUARDIAN_ADDRESS_ZIP_IS_MISSING(
      "Patient guardian address zip is missing", ErrorType.ACCEPT, 2, "DQA0152"), PATIENT_GUARDIAN_NAME_FIRST_IS_MISSING(
      "Patient guardian name first is missing", ErrorType.WARNING, 2, "DQA0153"), PATIENT_GUARDIAN_NAME_IS_MISSING(
      "Patient guardian name is missing", ErrorType.WARNING, 2, "DQA0155"), PATIENT_GUARDIAN_NAME_IS_SAME_AS_UNDERAGE_PATIENT(
      "Patient guardian name is same as underage patient", ErrorType.ACCEPT, 3, "DQA0156"), PATIENT_GUARDIAN_NAME_LAST_IS_MISSING(
      "Patient guardian name last is missing", ErrorType.WARNING, 2, "DQA0154"), PATIENT_GUARDIAN_PHONE_IS_MISSING(
      "Patient guardian phone is missing", ErrorType.ACCEPT, 2, "DQA0158"), PATIENT_GUARDIAN_RELATIONSHIP_IS_MISSING(
      "Patient guardian relationship is missing", ErrorType.WARNING, 2, "DQA0159"), PATIENT_GUARDIAN_RESPONSIBLE_PARTY_IS_MISSING(
      "Patient guardian responsible party is missing", ErrorType.WARNING, 2, "DQA0157"), PATIENT_IMMUNIZATION_REGISTRY_STATUS_IS_DEPRECATED(
      "Patient immunization registry status is deprecated", ErrorType.WARNING, 4, "DQA0160"), PATIENT_IMMUNIZATION_REGISTRY_STATUS_IS_IGNORED(
      "Patient immunization registry status is ignored", ErrorType.UNKNOWN, 4, "DQA0161"), PATIENT_IMMUNIZATION_REGISTRY_STATUS_IS_INVALID(
      "Patient immunization registry status is invalid", ErrorType.WARNING, 4, "DQA0162"), PATIENT_IMMUNIZATION_REGISTRY_STATUS_IS_MISSING(
      "Patient immunization registry status is missing", ErrorType.ACCEPT, 3, "DQA0163"), PATIENT_IMMUNIZATION_REGISTRY_STATUS_IS_UNRECOGNIZED(
      "Patient immunization registry status is unrecognized", ErrorType.WARNING, 2, "DQA0164"), PATIENT_MEDICAID_NUMBER_IS_INVALID(
      "Patient Medicaid number is invalid", ErrorType.WARNING, 4, "DQA0167"), PATIENT_MEDICAID_NUMBER_IS_MISSING(
      "Patient Medicaid number is missing", ErrorType.ACCEPT, 3, "DQA0168"), PATIENT_MIDDLE_NAME_IS_MISSING(
      "Patient middle name is missing", ErrorType.ACCEPT, 2, "DQA0169"), PATIENT_MIDDLE_NAME_IS_TOO_LONG(
      "Patient middle name is too long", ErrorType.UNKNOWN, 4, "DQA0552"), PATIENT_MIDDLE_NAME_IS_TOO_SHORT(
      "Patient middle name is too short", ErrorType.UNKNOWN, 4, "DQA0549"), PATIENT_MIDDLE_NAME_IS_UNEXPECTEDLY_LONG(
      "Patient middle name is unexpectedly long", ErrorType.UNKNOWN, 2, "DQA0551"), PATIENT_MIDDLE_NAME_IS_UNEXPECTEDLY_SHORT(
      "Patient middle name is unexpectedly short", ErrorType.UNKNOWN, 4, "DQA0550"), PATIENT_MIDDLE_NAME_MAY_BE_INITIAL(
      "Patient middle name may be initial", ErrorType.ACCEPT, 2, "DQA0170"), PATIENT_MOTHERS_MAIDEN_NAME_IS_MISSING(
      "Patient mother''s maiden name is missing", ErrorType.ACCEPT, 3, "DQA0171"), PATIENT_MOTHERS_MAIDEN_NAME_IS_TOO_LONG(
      "Patient mother''s maiden name is too long", ErrorType.UNKNOWN, 4, "DQA0548"), PATIENT_MOTHERS_MAIDEN_NAME_IS_TOO_SHORT(
      "Patient mother''s maiden name is too short", ErrorType.UNKNOWN, 4, "DQA0545"), PATIENT_MOTHERS_MAIDEN_NAME_IS_UNEXPECTEDLY_LONG(
      "Patient mother''s maiden name is unexpectedly long", ErrorType.UNKNOWN, 2, "DQA0547"), PATIENT_MOTHERS_MAIDEN_NAME_IS_UNEXPECTEDLY_SHORT(
      "Patient mother''s maiden name is unexpectedly short", ErrorType.UNKNOWN, 4, "DQA0546"), PATIENT_NAME_FIRST_IS_INVALID(
      "Patient name first is invalid", ErrorType.ERROR, 1, "DQA0140"), PATIENT_NAME_FIRST_IS_MISSING(
      "Patient name first is missing", ErrorType.ERROR, 1, "DQA0141"), PATIENT_NAME_FIRST_IS_TOO_LONG(
      "Patient name first is too long", ErrorType.UNKNOWN, 4, "DQA0543"), PATIENT_NAME_FIRST_IS_TOO_SHORT(
      "Patient name first is too short", ErrorType.UNKNOWN, 3, "DQA0540"), PATIENT_NAME_FIRST_IS_UNEXPECTEDLY_LONG(
      "Patient name first is unexpectedly long", ErrorType.UNKNOWN, 2, "DQA0542"), PATIENT_NAME_FIRST_IS_UNEXPECTEDLY_SHORT(
      "Patient name first is unexpectedly short", ErrorType.UNKNOWN, 4, "DQA0541"), PATIENT_NAME_FIRST_MAY_INCLUDE_MIDDLE_INITIAL(
      "Patient name first may include middle initial", ErrorType.ACCEPT, 1, "DQA0142"), PATIENT_NAME_IS_A_KNOWN_TEST_NAME(
      "Patient name is a known test name", ErrorType.UNKNOWN, 4, "DQA0544"), PATIENT_NAME_LAST_IS_INVALID(
      "Patient name last is invalid", ErrorType.ERROR, 1, "DQA0165"), PATIENT_NAME_LAST_IS_MISSING(
      "Patient name last is missing", ErrorType.ERROR, 1, "DQA0166"), PATIENT_NAME_LAST_IS_TOO_LONG(
      "Patient name last is too long", ErrorType.UNKNOWN, 4, "DQA0539"), PATIENT_NAME_LAST_IS_TOO_SHORT(
      "Patient name last is too short", ErrorType.UNKNOWN, 3, "DQA0536"), PATIENT_NAME_LAST_IS_UNEXPECTEDLY_LONG(
      "Patient name last is unexpectedly long", ErrorType.UNKNOWN, 2, "DQA0538"), PATIENT_NAME_LAST_IS_UNEXPECTEDLY_SHORT(
      "Patient name last is unexpectedly short", ErrorType.UNKNOWN, 4, "DQA0537"), PATIENT_NAME_MAY_BE_TEMPORARY_NEWBORN_NAME(
      "Patient name may be temporary newborn name", ErrorType.ACCEPT, 1, "DQA0172"), PATIENT_NAME_MAY_BE_TEST_NAME(
      "Patient name may be test name", ErrorType.ACCEPT, 1, "DQA0173"), PATIENT_NAME_TYPE_CODE_IS_DEPRECATED(
      "Patient name type code is deprecated", ErrorType.WARNING, 4, "DQA0405"), PATIENT_NAME_TYPE_CODE_IS_IGNORED(
      "Patient name type code is ignored", ErrorType.UNKNOWN, 4, "DQA0406"), PATIENT_NAME_TYPE_CODE_IS_INVALID(
      "Patient name type code is invalid", ErrorType.WARNING, 4, "DQA0407"), PATIENT_NAME_TYPE_CODE_IS_MISSING(
      "Patient name type code is missing", ErrorType.WARNING, 3, "DQA0408"), PATIENT_NAME_TYPE_CODE_IS_NOT_VALUED_LEGAL(
      "Patient name type code is not valued legal", ErrorType.WARNING, 2, "DQA0516"), PATIENT_NAME_TYPE_CODE_IS_UNRECOGNIZED(
      "Patient name type code is unrecognized", ErrorType.WARNING, 2, "DQA0409"), PATIENT_PHONE_IS_INCOMPLETE(
      "Patient phone is incomplete", ErrorType.WARNING, 2, "DQA0174"), PATIENT_PHONE_IS_INVALID(
      "Patient phone is invalid", ErrorType.WARNING, 2, "DQA0175"), PATIENT_PHONE_IS_MISSING(
      "Patient phone is missing", ErrorType.ACCEPT, 3, "DQA0176"), PATIENT_PHONE_TEL_EQUIP_CODE_IS_DEPRECATED(
      "Patient phone tel equip code is deprecated", ErrorType.WARNING, 4, "DQA0458"), PATIENT_PHONE_TEL_EQUIP_CODE_IS_IGNORED(
      "Patient phone tel equip code is ignored", ErrorType.UNKNOWN, 4, "DQA0459"), PATIENT_PHONE_TEL_EQUIP_CODE_IS_INVALID(
      "Patient phone tel equip code is invalid", ErrorType.WARNING, 4, "DQA0460"), PATIENT_PHONE_TEL_EQUIP_CODE_IS_MISSING(
      "Patient phone tel equip code is missing", ErrorType.WARNING, 3, "DQA0461"), PATIENT_PHONE_TEL_EQUIP_CODE_IS_UNRECOGNIZED(
      "Patient phone tel equip code is unrecognized", ErrorType.WARNING, 2, "DQA0462"), PATIENT_PHONE_TEL_USE_CODE_IS_DEPRECATED(
      "Patient phone tel use code is deprecated", ErrorType.WARNING, 4, "DQA0453"), PATIENT_PHONE_TEL_USE_CODE_IS_IGNORED(
      "Patient phone tel use code is ignored", ErrorType.UNKNOWN, 4, "DQA0454"), PATIENT_PHONE_TEL_USE_CODE_IS_INVALID(
      "Patient phone tel use code is invalid", ErrorType.WARNING, 4, "DQA0455"), PATIENT_PHONE_TEL_USE_CODE_IS_MISSING(
      "Patient phone tel use code is missing", ErrorType.WARNING, 3, "DQA0456"), PATIENT_PHONE_TEL_USE_CODE_IS_UNRECOGNIZED(
      "Patient phone tel use code is unrecognized", ErrorType.WARNING, 2, "DQA0457"), PATIENT_PRIMARY_FACILITY_ID_IS_DEPRECATED(
      "Patient primary facility id is deprecated", ErrorType.WARNING, 4, "DQA0177"), PATIENT_PRIMARY_FACILITY_ID_IS_IGNORED(
      "Patient primary facility id is ignored", ErrorType.UNKNOWN, 4, "DQA0178"), PATIENT_PRIMARY_FACILITY_ID_IS_INVALID(
      "Patient primary facility id is invalid", ErrorType.ERROR, 4, "DQA0179"), PATIENT_PRIMARY_FACILITY_ID_IS_MISSING(
      "Patient primary facility id is missing", ErrorType.ACCEPT, 4, "DQA0180"), PATIENT_PRIMARY_FACILITY_ID_IS_UNRECOGNIZED(
      "Patient primary facility id is unrecognized", ErrorType.WARNING, 4, "DQA0181"), PATIENT_PRIMARY_FACILITY_NAME_IS_MISSING(
      "Patient primary facility name is missing", ErrorType.ACCEPT, 3, "DQA0182"), PATIENT_PRIMARY_LANGUAGE_IS_DEPRECATED(
      "Patient primary language is deprecated", ErrorType.ACCEPT, 4, "DQA0183"), PATIENT_PRIMARY_LANGUAGE_IS_IGNORED(
      "Patient primary language is ignored", ErrorType.UNKNOWN, 4, "DQA0184"), PATIENT_PRIMARY_LANGUAGE_IS_INVALID(
      "Patient primary language is invalid", ErrorType.WARNING, 4, "DQA0185"), PATIENT_PRIMARY_LANGUAGE_IS_MISSING(
      "Patient primary language is missing", ErrorType.ACCEPT, 3, "DQA0186"), PATIENT_PRIMARY_LANGUAGE_IS_UNRECOGNIZED(
      "Patient primary language is unrecognized", ErrorType.WARNING, 2, "DQA0187"), PATIENT_PRIMARY_PHYSICIAN_ID_IS_DEPRECATED(
      "Patient primary physician id is deprecated", ErrorType.WARNING, 4, "DQA0188"), PATIENT_PRIMARY_PHYSICIAN_ID_IS_IGNORED(
      "Patient primary physician id is ignored", ErrorType.UNKNOWN, 4, "DQA0189"), PATIENT_PRIMARY_PHYSICIAN_ID_IS_INVALID(
      "Patient primary physician id is invalid", ErrorType.WARNING, 4, "DQA0190"), PATIENT_PRIMARY_PHYSICIAN_ID_IS_MISSING(
      "Patient primary physician id is missing", ErrorType.ACCEPT, 4, "DQA0191"), PATIENT_PRIMARY_PHYSICIAN_ID_IS_UNRECOGNIZED(
      "Patient primary physician id is unrecognized", ErrorType.WARNING, 4, "DQA0192"), PATIENT_PRIMARY_PHYSICIAN_NAME_IS_MISSING(
      "Patient primary physician name is missing", ErrorType.ACCEPT, 3, "DQA0193"), PATIENT_PROTECTION_INDICATOR_IS_DEPRECATED(
      "Patient protection indicator is deprecated", ErrorType.WARNING, 4, "DQA0194"), PATIENT_PROTECTION_INDICATOR_IS_IGNORED(
      "Patient protection indicator is ignored", ErrorType.UNKNOWN, 4, "DQA0195"), PATIENT_PROTECTION_INDICATOR_IS_INVALID(
      "Patient protection indicator is invalid", ErrorType.WARNING, 4, "DQA0196"), PATIENT_PROTECTION_INDICATOR_IS_MISSING(
      "Patient protection indicator is missing", ErrorType.ACCEPT, 2, "DQA0197"), PATIENT_PROTECTION_INDICATOR_IS_UNRECOGNIZED(
      "Patient protection indicator is unrecognized", ErrorType.WARNING, 2, "DQA0198"), PATIENT_PROTECTION_INDICATOR_IS_VALUED_AS_NO(
      "Patient protection indicator is valued as no", ErrorType.WARNING, 3, "DQA0199"), PATIENT_PROTECTION_INDICATOR_IS_VALUED_AS_YES(
      "Patient protection indicator is valued as yes", ErrorType.WARNING, 1, "DQA0200"), PATIENT_PUBLICITY_CODE_IS_DEPRECATED(
      "Patient publicity code is deprecated", ErrorType.WARNING, 4, "DQA0201"), PATIENT_PUBLICITY_CODE_IS_IGNORED(
      "Patient publicity code is ignored", ErrorType.UNKNOWN, 4, "DQA0202"), PATIENT_PUBLICITY_CODE_IS_INVALID(
      "Patient publicity code is invalid", ErrorType.WARNING, 4, "DQA0203"), PATIENT_PUBLICITY_CODE_IS_MISSING(
      "Patient publicity code is missing", ErrorType.ACCEPT, 3, "DQA0204"), PATIENT_PUBLICITY_CODE_IS_UNRECOGNIZED(
      "Patient publicity code is unrecognized", ErrorType.WARNING, 2, "DQA0205"), PATIENT_RACE_IS_DEPRECATED(
      "Patient race is deprecated", ErrorType.WARNING, 2, "DQA0206"), PATIENT_RACE_IS_IGNORED(
      "Patient race is ignored", ErrorType.UNKNOWN, 4, "DQA0207"), PATIENT_RACE_IS_INVALID("Patient race is invalid",
      ErrorType.WARNING, 4, "DQA0208"), PATIENT_RACE_IS_MISSING("Patient race is missing", ErrorType.ACCEPT, 2,
      "DQA0209"), PATIENT_RACE_IS_UNRECOGNIZED("Patient race is unrecognized", ErrorType.WARNING, 2, "DQA0210"), PATIENT_REGISTRY_ID_IS_MISSING(
      "Patient registry id is missing", ErrorType.ACCEPT, 3, "DQA0211"), PATIENT_REGISTRY_ID_IS_UNRECOGNIZED(
      "Patient registry id is unrecognized", ErrorType.ACCEPT, 4, "DQA0212"), PATIENT_REGISTRY_STATUS_IS_DEPRECATED(
      "Patient registry status is deprecated", ErrorType.WARNING, 4, "DQA0213"), PATIENT_REGISTRY_STATUS_IS_IGNORED(
      "Patient registry status is ignored", ErrorType.UNKNOWN, 4, "DQA0214"), PATIENT_REGISTRY_STATUS_IS_INVALID(
      "Patient registry status is invalid", ErrorType.WARNING, 4, "DQA0215"), PATIENT_REGISTRY_STATUS_IS_MISSING(
      "Patient registry status is missing", ErrorType.ACCEPT, 3, "DQA0216"), PATIENT_REGISTRY_STATUS_IS_UNRECOGNIZED(
      "Patient registry status is unrecognized", ErrorType.WARNING, 2, "DQA0217"), PATIENT_SSN_IS_INVALID(
      "Patient SSN is invalid", ErrorType.WARNING, 2, "DQA0218"), PATIENT_SSN_IS_MISSING("Patient SSN is missing",
      ErrorType.ACCEPT, 3, "DQA0219"), PATIENT_SUBMITTER_ID_AUTHORITY_IS_MISSING(
      "Patient submitter id authority is missing", ErrorType.ERROR, 2, "DQA0393"), PATIENT_SUBMITTER_ID_IS_MISSING(
      "Patient submitter id is missing", ErrorType.ERROR, 1, "DQA0220"), PATIENT_SUBMITTER_ID_TYPE_CODE_IS_DEPRECATED(
      "Patient submitter id type code is deprecated", ErrorType.WARNING, 4, "DQA0512"), PATIENT_SUBMITTER_ID_TYPE_CODE_IS_IGNORED(
      "Patient submitter id type code is ignored", ErrorType.UNKNOWN, 4, "DQA0515"), PATIENT_SUBMITTER_ID_TYPE_CODE_IS_INVALID(
      "Patient submitter id type code is invalid", ErrorType.ERROR, 4, "DQA0513"), PATIENT_SUBMITTER_ID_TYPE_CODE_IS_MISSING(
      "Patient submitter id type code is missing", ErrorType.ERROR, 2, "DQA0394"), PATIENT_SUBMITTER_ID_TYPE_CODE_IS_UNRECOGNIZED(
      "Patient submitter id type code is unrecognized", ErrorType.ACCEPT, 4, "DQA0514"), PATIENT_VFC_EFFECTIVE_DATE_IS_BEFORE_BIRTH(
      "Patient VFC effective date is before birth", ErrorType.ERROR, 1, "DQA0221"), PATIENT_VFC_EFFECTIVE_DATE_IS_IN_FUTURE(
      "Patient VFC effective date is in future", ErrorType.ERROR, 1, "DQA0222"), PATIENT_VFC_EFFECTIVE_DATE_IS_INVALID(
      "Patient VFC effective date is invalid", ErrorType.ERROR, 1, "DQA0223"), PATIENT_VFC_EFFECTIVE_DATE_IS_MISSING(
      "Patient VFC effective date is missing", ErrorType.ACCEPT, 3, "DQA0224"), PATIENT_VFC_STATUS_IS_DEPRECATED(
      "Patient VFC status is deprecated", ErrorType.WARNING, 4, "DQA0225"), PATIENT_VFC_STATUS_IS_IGNORED(
      "Patient VFC status is ignored", ErrorType.UNKNOWN, 4, "DQA0226"), PATIENT_VFC_STATUS_IS_INVALID(
      "Patient VFC status is invalid", ErrorType.WARNING, 2, "DQA0227"), PATIENT_VFC_STATUS_IS_MISSING(
      "Patient VFC status is missing", ErrorType.ACCEPT, 2, "DQA0228"), PATIENT_VFC_STATUS_IS_UNRECOGNIZED(
      "Patient VFC status is unrecognized", ErrorType.WARNING, 2, "DQA0229"), PATIENT_WIC_ID_IS_INVALID(
      "Patient WIC id is invalid", ErrorType.ACCEPT, 4, "DQA0230"), PATIENT_WIC_ID_IS_MISSING(
      "Patient WIC id is missing", ErrorType.ACCEPT, 3, "DQA0231"), VACCINATION_ACTION_CODE_IS_DEPRECATED(
      "Vaccination action code is deprecated", ErrorType.WARNING, 4, "DQA0232"), VACCINATION_ACTION_CODE_IS_IGNORED(
      "Vaccination action code is ignored", ErrorType.UNKNOWN, 4, "DQA0233"), VACCINATION_ACTION_CODE_IS_INVALID(
      "Vaccination action code is invalid", ErrorType.ERROR, 4, "DQA0234"), VACCINATION_ACTION_CODE_IS_MISSING(
      "Vaccination action code is missing", ErrorType.ACCEPT, 3, "DQA0235"), VACCINATION_ACTION_CODE_IS_UNRECOGNIZED(
      "Vaccination action code is unrecognized", ErrorType.WARNING, 2, "DQA0236"), VACCINATION_ACTION_CODE_IS_VALUED_AS_ADD(
      "Vaccination action code is valued as add", ErrorType.ACCEPT, 3, "DQA0237"), VACCINATION_ACTION_CODE_IS_VALUED_AS_ADD_OR_UPDATE(
      "Vaccination action code is valued as add or update", ErrorType.ACCEPT, 3, "DQA0238"), VACCINATION_ACTION_CODE_IS_VALUED_AS_DELETE(
      "Vaccination action code is valued as delete", ErrorType.ACCEPT, 3, "DQA0239"), VACCINATION_ACTION_CODE_IS_VALUED_AS_UPDATE(
      "Vaccination action code is valued as update", ErrorType.ACCEPT, 3, "DQA0240"), VACCINATION_ADMIN_CODE_IS_DEPRECATED(
      "Vaccination admin code is deprecated", ErrorType.WARNING, 4, "DQA0241"), VACCINATION_ADMIN_CODE_IS_IGNORED(
      "Vaccination admin code is ignored", ErrorType.UNKNOWN, 4, "DQA0242"), VACCINATION_ADMIN_CODE_IS_INVALID(
      "Vaccination admin code is invalid", ErrorType.ERROR, 4, "DQA0243"), VACCINATION_ADMIN_CODE_IS_INVALID_FOR_DATE_ADMINISTERED(
      "Vaccination admin code is invalid for date administered", ErrorType.ERROR, 1, "DQA0491"), VACCINATION_ADMIN_CODE_IS_MISSING(
      "Vaccination admin code is missing", ErrorType.UNKNOWN, 1, "DQA0244"), VACCINATION_ADMIN_CODE_IS_NOT_SPECIFIC(
      "Vaccination admin code is not specific", ErrorType.WARNING, 2, "DQA0245"), VACCINATION_ADMIN_CODE_IS_NOT_VACCINE(
      "Vaccination admin code is not vaccine", ErrorType.WARNING, 2, "DQA0246"), VACCINATION_ADMIN_CODE_IS_UNEXPECTED_FOR_DATE_ADMINISTERED(
      "Vaccination admin code is unexpected for date administered", ErrorType.WARNING, 2, "DQA0490"), VACCINATION_ADMIN_CODE_IS_UNRECOGNIZED(
      "Vaccination admin code is unrecognized", ErrorType.ERROR, 1, "DQA0247"), VACCINATION_ADMIN_CODE_IS_VALUED_AS_NOT_ADMINISTERED(
      "Vaccination admin code is valued as not administered", ErrorType.UNKNOWN, 3, "DQA0248"), VACCINATION_ADMIN_CODE_IS_VALUED_AS_UNKNOWN(
      "Vaccination admin code is valued as unknown", ErrorType.WARNING, 2, "DQA0249"), VACCINATION_ADMIN_CODE_MAY_BE_VARIATION_OF_PREVIOUSLY_REPORTED_CODES(
      "Vaccination admin code may be variation of previously reported codes", ErrorType.WARNING, 4, "DQA0250"), VACCINATION_ADMIN_CODE_TABLE_IS_INVALID(
      "Vaccination admin code table is invalid", ErrorType.WARNING, 2, "DQA0484"), VACCINATION_ADMIN_CODE_TABLE_IS_MISSING(
      "Vaccination admin code table is missing", ErrorType.WARNING, 2, "DQA0483"), VACCINATION_ADMIN_DATE_END_IS_DIFFERENT_FROM_START_DATE(
      "Vaccination admin date end is different from start date", ErrorType.WARNING, 2, "DQA0266"), VACCINATION_ADMIN_DATE_END_IS_MISSING(
      "Vaccination admin date end is missing", ErrorType.ACCEPT, 3, "DQA0267"), VACCINATION_ADMIN_DATE_IS_AFTER_LOT_EXPIRATION_DATE(
      "Vaccination admin date is after lot expiration date", ErrorType.WARNING, 2, "DQA0251"), VACCINATION_ADMIN_DATE_IS_AFTER_MESSAGE_SUBMITTED(
      "Vaccination admin date is after message submitted", ErrorType.ERROR, 1, "DQA0252"), VACCINATION_ADMIN_DATE_IS_AFTER_PATIENT_DEATH_DATE(
      "Vaccination admin date is after patient death date", ErrorType.ERROR, 1, "DQA0253"), VACCINATION_ADMIN_DATE_IS_AFTER_SYSTEM_ENTRY_DATE(
      "Vaccination admin date is after system entry date", ErrorType.ERROR, 1, "DQA0254"), VACCINATION_ADMIN_DATE_IS_BEFORE_BIRTH(
      "Vaccination admin date is before birth", ErrorType.ERROR, 1, "DQA0255"), VACCINATION_ADMIN_DATE_IS_BEFORE_OR_AFTER_EXPECTED_VACCINE_USAGE_RANGE(
      "Vaccination admin date is before or after expected vaccine usage range", ErrorType.WARNING, 2, "DQA0256"), VACCINATION_ADMIN_DATE_IS_BEFORE_OR_AFTER_LICENSED_VACCINE_RANGE(
      "Vaccination admin date is before or after licensed vaccine range", ErrorType.ERROR, 1, "DQA0257"), VACCINATION_ADMIN_DATE_IS_BEFORE_OR_AFTER_WHEN_EXPECTED_FOR_PATIENT_AGE(
      "Vaccination admin date is before or after when expected for patient age", ErrorType.WARNING, 2, "DQA0258"), VACCINATION_ADMIN_DATE_IS_BEFORE_OR_AFTER_WHEN_VALID_FOR_PATIENT_AGE(
      "Vaccination admin date is before or after when valid for patient age", ErrorType.ERROR, 4, "DQA0259"), VACCINATION_ADMIN_DATE_IS_INVALID(
      "Vaccination admin date is invalid", ErrorType.ERROR, 1, "DQA0260"), VACCINATION_ADMIN_DATE_IS_MISSING(
      "Vaccination admin date is missing", ErrorType.UNKNOWN, 1, "DQA0261"), VACCINATION_ADMIN_DATE_IS_ON_15TH_DAY_OF_MONTH(
      "Vaccination admin date is on 15th day of month", ErrorType.ACCEPT, 3, "DQA0262"), VACCINATION_ADMIN_DATE_IS_ON_FIRST_DAY_OF_MONTH(
      "Vaccination admin date is on first day of month", ErrorType.ACCEPT, 3, "DQA0263"), VACCINATION_ADMIN_DATE_IS_ON_LAST_DAY_OF_MONTH(
      "Vaccination admin date is on last day of month", ErrorType.ACCEPT, 3, "DQA0264"), VACCINATION_ADMIN_DATE_IS_REPORTED_LATE(
      "Vaccination admin date is reported late", ErrorType.WARNING, 2, "DQA0265"), VACCINATION_ADMINISTERED_AMOUNT_IS_INVALID(
      "Vaccination administered amount is invalid", ErrorType.WARNING, 2, "DQA0268"), VACCINATION_ADMINISTERED_AMOUNT_IS_MISSING(
      "Vaccination administered amount is missing", ErrorType.ACCEPT, 3, "DQA0269"), VACCINATION_ADMINISTERED_AMOUNT_IS_VALUED_AS_UNKNOWN(
      "Vaccination administered amount is valued as unknown", ErrorType.ACCEPT, 3, "DQA0271"), VACCINATION_ADMINISTERED_AMOUNT_IS_VALUED_AS_ZERO(
      "Vaccination administered amount is valued as zero", ErrorType.ACCEPT, 3, "DQA0270"), VACCINATION_ADMINISTERED_UNIT_IS_DEPRECATED(
      "Vaccination administered unit is deprecated", ErrorType.WARNING, 4, "DQA0447"), VACCINATION_ADMINISTERED_UNIT_IS_IGNORED(
      "Vaccination administered unit is ignored", ErrorType.UNKNOWN, 4, "DQA0448"), VACCINATION_ADMINISTERED_UNIT_IS_INVALID(
      "Vaccination administered unit is invalid", ErrorType.WARNING, 4, "DQA0449"), VACCINATION_ADMINISTERED_UNIT_IS_MISSING(
      "Vaccination administered unit is missing", ErrorType.ACCEPT, 3, "DQA0272"), VACCINATION_ADMINISTERED_UNIT_IS_UNRECOGNIZED(
      "Vaccination administered unit is unrecognized", ErrorType.WARNING, 2, "DQA0450"), VACCINATION_BODY_ROUTE_IS_DEPRECATED(
      "Vaccination body route is deprecated", ErrorType.WARNING, 2, "DQA0273"), VACCINATION_BODY_ROUTE_IS_IGNORED(
      "Vaccination body route is ignored", ErrorType.UNKNOWN, 4, "DQA0274"), VACCINATION_BODY_ROUTE_IS_INVALID(
      "Vaccination body route is invalid", ErrorType.WARNING, 4, "DQA0275"), VACCINATION_BODY_ROUTE_IS_INVALID_FOR_VACCINE_INDICATED(
      "Vaccination body route is invalid for vaccine indicated", ErrorType.WARNING, 2, "DQA0276"), VACCINATION_BODY_ROUTE_IS_MISSING(
      "Vaccination body route is missing", ErrorType.ACCEPT, 3, "DQA0277"), VACCINATION_BODY_ROUTE_IS_UNRECOGNIZED(
      "Vaccination body route is unrecognized", ErrorType.WARNING, 2, "DQA0278"), VACCINATION_BODY_SITE_IS_DEPRECATED(
      "Vaccination body site is deprecated", ErrorType.WARNING, 4, "DQA0279"), VACCINATION_BODY_SITE_IS_IGNORED(
      "Vaccination body site is ignored", ErrorType.UNKNOWN, 4, "DQA0280"), VACCINATION_BODY_SITE_IS_INVALID(
      "Vaccination body site is invalid", ErrorType.WARNING, 4, "DQA0281"), VACCINATION_BODY_SITE_IS_INVALID_FOR_VACCINE_INDICATED(
      "Vaccination body site is invalid for vaccine indicated", ErrorType.WARNING, 2, "DQA0282"), VACCINATION_BODY_SITE_IS_MISSING(
      "Vaccination body site is missing", ErrorType.ACCEPT, 3, "DQA0283"), VACCINATION_BODY_SITE_IS_UNRECOGNIZED(
      "Vaccination body site is unrecognized", ErrorType.WARNING, 2, "DQA0284"), VACCINATION_COMPLETION_STATUS_IS_DEPRECATED(
      "Vaccination completion status is deprecated", ErrorType.WARNING, 4, "DQA0285"), VACCINATION_COMPLETION_STATUS_IS_IGNORED(
      "Vaccination completion status is ignored", ErrorType.WARNING, 4, "DQA0286"), VACCINATION_COMPLETION_STATUS_IS_INVALID(
      "Vaccination completion status is invalid", ErrorType.ERROR, 4, "DQA0287"), VACCINATION_COMPLETION_STATUS_IS_MISSING(
      "Vaccination completion status is missing", ErrorType.ACCEPT, 3, "DQA0288"), VACCINATION_COMPLETION_STATUS_IS_UNRECOGNIZED(
      "Vaccination completion status is unrecognized", ErrorType.WARNING, 1, "DQA0289"), VACCINATION_COMPLETION_STATUS_IS_VALUED_AS_COMPLETED(
      "Vaccination completion status is valued as completed", ErrorType.ACCEPT, 3, "DQA0290"), VACCINATION_COMPLETION_STATUS_IS_VALUED_AS_NOT_ADMINISTERED(
      "Vaccination completion status is valued as not administered", ErrorType.WARNING, 3, "DQA0291"), VACCINATION_COMPLETION_STATUS_IS_VALUED_AS_PARTIALLY_ADMINISTERED(
      "Vaccination completion status is valued as partially administered", ErrorType.WARNING, 3, "DQA0292"), VACCINATION_COMPLETION_STATUS_IS_VALUED_AS_REFUSED(
      "Vaccination completion status is valued as refused", ErrorType.WARNING, 3, "DQA0293"), VACCINATION_CONFIDENTIALITY_CODE_IS_DEPRECATED(
      "Vaccination confidentiality code is deprecated", ErrorType.WARNING, 4, "DQA0294"), VACCINATION_CONFIDENTIALITY_CODE_IS_IGNORED(
      "Vaccination confidentiality code is ignored", ErrorType.UNKNOWN, 4, "DQA0295"), VACCINATION_CONFIDENTIALITY_CODE_IS_INVALID(
      "Vaccination confidentiality code is invalid", ErrorType.ERROR, 4, "DQA0296"), VACCINATION_CONFIDENTIALITY_CODE_IS_MISSING(
      "Vaccination confidentiality code is missing", ErrorType.ACCEPT, 3, "DQA0297"), VACCINATION_CONFIDENTIALITY_CODE_IS_UNRECOGNIZED(
      "Vaccination confidentiality code is unrecognized", ErrorType.WARNING, 2, "DQA0298"), VACCINATION_CONFIDENTIALITY_CODE_IS_VALUED_AS_RESTRICTED(
      "Vaccination confidentiality code is valued as restricted", ErrorType.WARNING, 2, "DQA0299"), VACCINATION_CPT_CODE_IS_DEPRECATED(
      "Vaccination CPT code is deprecated", ErrorType.WARNING, 4, "DQA0300"), VACCINATION_CPT_CODE_IS_IGNORED(
      "Vaccination CPT code is ignored", ErrorType.UNKNOWN, 4, "DQA0301"), VACCINATION_CPT_CODE_IS_INVALID(
      "Vaccination CPT code is invalid", ErrorType.WARNING, 2, "DQA0302"), VACCINATION_CPT_CODE_IS_INVALID_FOR_DATE_ADMINISTERED(
      "Vaccination CPT code is invalid for date administered", ErrorType.WARNING, 2, "DQA0489"), VACCINATION_CPT_CODE_IS_MISSING(
      "Vaccination CPT code is missing", ErrorType.ACCEPT, 3, "DQA0303"), VACCINATION_CPT_CODE_IS_UNEXPECTED_FOR_DATE_ADMINISTERED(
      "Vaccination CPT code is unexpected for date administered", ErrorType.ACCEPT, 3, "DQA0488"), VACCINATION_CPT_CODE_IS_UNRECOGNIZED(
      "Vaccination CPT code is unrecognized", ErrorType.WARNING, 2, "DQA0304"), VACCINATION_CVX_CODE_AND_CPT_CODE_ARE_INCONSISTENT(
      "Vaccination CVX code and CPT code are inconsistent", ErrorType.UNKNOWN, 2, "DQA0310"), VACCINATION_CVX_CODE_IS_DEPRECATED(
      "Vaccination CVX code is deprecated", ErrorType.WARNING, 4, "DQA0305"), VACCINATION_CVX_CODE_IS_IGNORED(
      "Vaccination CVX code is ignored", ErrorType.UNKNOWN, 4, "DQA0306"), VACCINATION_CVX_CODE_IS_INVALID(
      "Vaccination CVX code is invalid", ErrorType.WARNING, 4, "DQA0307"), VACCINATION_CVX_CODE_IS_INVALID_FOR_DATE_ADMINISTERED(
      "Vaccination CVX code is invalid for date administered", ErrorType.WARNING, 2, "DQA0487"), VACCINATION_CVX_CODE_IS_MISSING(
      "Vaccination CVX code is missing", ErrorType.ACCEPT, 1, "DQA0308"), VACCINATION_CVX_CODE_IS_UNEXPECTED_FOR_DATE_ADMINISTERED(
      "Vaccination CVX code is unexpected for date administered", ErrorType.ACCEPT, 3, "DQA0486"), VACCINATION_CVX_CODE_IS_UNRECOGNIZED(
      "Vaccination CVX code is unrecognized", ErrorType.WARNING, 2, "DQA0309"), VACCINATION_FACILITY_ID_IS_DEPRECATED(
      "Vaccination facility id is deprecated", ErrorType.WARNING, 4, "DQA0311"), VACCINATION_FACILITY_ID_IS_IGNORED(
      "Vaccination facility id is ignored", ErrorType.UNKNOWN, 4, "DQA0312"), VACCINATION_FACILITY_ID_IS_INVALID(
      "Vaccination facility id is invalid", ErrorType.WARNING, 4, "DQA0313"), VACCINATION_FACILITY_ID_IS_MISSING(
      "Vaccination facility id is missing", ErrorType.ACCEPT, 1, "DQA0314"), VACCINATION_FACILITY_ID_IS_UNRECOGNIZED(
      "Vaccination facility id is unrecognized", ErrorType.WARNING, 4, "DQA0315"), VACCINATION_FACILITY_NAME_IS_MISSING(
      "Vaccination facility name is missing", ErrorType.ACCEPT, 3, "DQA0316"), VACCINATION_FILLER_ORDER_NUMBER_IS_DEPRECATED(
      "Vaccination filler order number is deprecated", ErrorType.WARNING, 4, "DQA0379"), VACCINATION_FILLER_ORDER_NUMBER_IS_IGNORED(
      "Vaccination filler order number is ignored", ErrorType.UNKNOWN, 4, "DQA0380"), VACCINATION_FILLER_ORDER_NUMBER_IS_INVALID(
      "Vaccination filler order number is invalid", ErrorType.ERROR, 4, "DQA0381"), VACCINATION_FILLER_ORDER_NUMBER_IS_MISSING(
      "Vaccination filler order number is missing", ErrorType.UNKNOWN, 3, "DQA0382"), VACCINATION_FILLER_ORDER_NUMBER_IS_UNRECOGNIZED(
      "Vaccination filler order number is unrecognized", ErrorType.WARNING, 4, "DQA0383"), VACCINATION_FINANCIAL_ELIGIBILITY_CODE_IS_DEPRECATED(
      "Vaccination financial eligibility code is deprecated", ErrorType.WARNING, 4, "DQA0465"), VACCINATION_FINANCIAL_ELIGIBILITY_CODE_IS_IGNORED(
      "Vaccination financial eligibility code is ignored", ErrorType.UNKNOWN, 4, "DQA0466"), VACCINATION_FINANCIAL_ELIGIBILITY_CODE_IS_INVALID(
      "Vaccination financial eligibility code is invalid", ErrorType.WARNING, 1, "DQA0467"), VACCINATION_FINANCIAL_ELIGIBILITY_CODE_IS_MISSING(
      "Vaccination financial eligibility code is missing", ErrorType.ACCEPT, 1, "DQA0468"), VACCINATION_FINANCIAL_ELIGIBILITY_CODE_IS_UNRECOGNIZED(
      "Vaccination financial eligibility code is unrecognized", ErrorType.WARNING, 1, "DQA0469"), VACCINATION_GIVEN_BY_IS_DEPRECATED(
      "Vaccination given by is deprecated", ErrorType.WARNING, 4, "DQA0317"), VACCINATION_GIVEN_BY_IS_IGNORED(
      "Vaccination given by is ignored", ErrorType.UNKNOWN, 4, "DQA0318"), VACCINATION_GIVEN_BY_IS_INVALID(
      "Vaccination given by is invalid", ErrorType.WARNING, 4, "DQA0319"), VACCINATION_GIVEN_BY_IS_MISSING(
      "Vaccination given by is missing", ErrorType.ACCEPT, 3, "DQA0320"), VACCINATION_GIVEN_BY_IS_UNRECOGNIZED(
      "Vaccination given by is unrecognized", ErrorType.WARNING, 4, "DQA0321"), VACCINATION_ID_IS_MISSING(
      "Vaccination id is missing", ErrorType.ACCEPT, 3, "DQA0322"), VACCINATION_ID_OF_RECEIVER_IS_MISSING(
      "Vaccination id of receiver is missing", ErrorType.ACCEPT, 3, "DQA0323"), VACCINATION_ID_OF_RECEIVER_IS_UNRECOGNIZED(
      "Vaccination id of receiver is unrecognized", ErrorType.WARNING, 4, "DQA0324"), VACCINATION_ID_OF_SENDER_IS_MISSING(
      "Vaccination id of sender is missing", ErrorType.ACCEPT, 3, "DQA0325"), VACCINATION_ID_OF_SENDER_IS_UNRECOGNIZED(
      "Vaccination id of sender is unrecognized", ErrorType.WARNING, 4, "DQA0326"), VACCINATION_INFORMATION_SOURCE_IS_ADMINISTERED_BUT_APPEARS_TO_HISTORICAL(
      "Vaccination information source is administered but appears to historical", ErrorType.WARNING, 2, "DQA0327"), VACCINATION_INFORMATION_SOURCE_IS_DEPRECATED(
      "Vaccination information source is deprecated", ErrorType.WARNING, 4, "DQA0328"), VACCINATION_INFORMATION_SOURCE_IS_HISTORICAL_BUT_APPEARS_TO_BE_ADMINISTERED(
      "Vaccination information source is historical but appears to be administered", ErrorType.WARNING, 2, "DQA0329"), VACCINATION_INFORMATION_SOURCE_IS_IGNORED(
      "Vaccination information source is ignored", ErrorType.UNKNOWN, 4, "DQA0330"), VACCINATION_INFORMATION_SOURCE_IS_INVALID(
      "Vaccination information source is invalid", ErrorType.WARNING, 4, "DQA0331"), VACCINATION_INFORMATION_SOURCE_IS_MISSING(
      "Vaccination information source is missing", ErrorType.WARNING, 1, "DQA0332"), VACCINATION_INFORMATION_SOURCE_IS_UNRECOGNIZED(
      "Vaccination information source is unrecognized", ErrorType.WARNING, 1, "DQA0333"), VACCINATION_INFORMATION_SOURCE_IS_VALUED_AS_ADMINISTERED(
      "Vaccination information source is valued as administered", ErrorType.ACCEPT, 3, "DQA0334"), VACCINATION_INFORMATION_SOURCE_IS_VALUED_AS_HISTORICAL(
      "Vaccination information source is valued as historical", ErrorType.ACCEPT, 3, "DQA0335"), VACCINATION_LOT_EXPIRATION_DATE_IS_INVALID(
      "Vaccination lot expiration date is invalid", ErrorType.WARNING, 2, "DQA0336"), VACCINATION_LOT_EXPIRATION_DATE_IS_MISSING(
      "Vaccination lot expiration date is missing", ErrorType.ACCEPT, 3, "DQA0337"), VACCINATION_LOT_NUMBER_IS_INVALID(
      "Vaccination lot number is invalid", ErrorType.WARNING, 2, "DQA0338"), VACCINATION_LOT_NUMBER_IS_MISSING(
      "Vaccination lot number is missing", ErrorType.WARNING, 2, "DQA0339"), VACCINATION_MANUFACTURER_CODE_IS_DEPRECATED(
      "Vaccination manufacturer code is deprecated", ErrorType.WARNING, 4, "DQA0340"), VACCINATION_MANUFACTURER_CODE_IS_IGNORED(
      "Vaccination manufacturer code is ignored", ErrorType.UNKNOWN, 4, "DQA0341"), VACCINATION_MANUFACTURER_CODE_IS_INVALID(
      "Vaccination manufacturer code is invalid", ErrorType.WARNING, 4, "DQA0342"), VACCINATION_MANUFACTURER_CODE_IS_INVALID_FOR_DATE_ADMINISTERED(
      "Vaccination manufacturer code is invalid for date administered", ErrorType.WARNING, 2, "DQA0495"), VACCINATION_MANUFACTURER_CODE_IS_MISSING(
      "Vaccination manufacturer code is missing", ErrorType.WARNING, 2, "DQA0343"), VACCINATION_MANUFACTURER_CODE_IS_UNEXPECTED_FOR_DATE_ADMINISTERED(
      "Vaccination manufacturer code is unexpected for date administered", ErrorType.WARNING, 2, "DQA0494"), VACCINATION_MANUFACTURER_CODE_IS_UNRECOGNIZED(
      "Vaccination manufacturer code is unrecognized", ErrorType.WARNING, 2, "DQA0344"), VACCINATION_ORDER_CONTROL_CODE_IS_DEPRECATED(
      "Vaccination order control code is deprecated", ErrorType.WARNING, 4, "DQA0373"), VACCINATION_ORDER_CONTROL_CODE_IS_IGNORED(
      "Vaccination order control code is ignored", ErrorType.UNKNOWN, 4, "DQA0369"), VACCINATION_ORDER_CONTROL_CODE_IS_INVALID(
      "Vaccination order control code is invalid", ErrorType.WARNING, 2, "DQA0370"), VACCINATION_ORDER_CONTROL_CODE_IS_MISSING(
      "Vaccination order control code is missing", ErrorType.UNKNOWN, 3, "DQA0371"), VACCINATION_ORDER_CONTROL_CODE_IS_UNRECOGNIZED(
      "Vaccination order control code is unrecognized", ErrorType.UNKNOWN, 2, "DQA0372"), VACCINATION_ORDER_FACILITY_ID_IS_DEPRECATED(
      "Vaccination order facility id is deprecated", ErrorType.ACCEPT, 4, "DQA0442"), VACCINATION_ORDER_FACILITY_ID_IS_IGNORED(
      "Vaccination order facility id is ignored", ErrorType.UNKNOWN, 4, "DQA0443"), VACCINATION_ORDER_FACILITY_ID_IS_INVALID(
      "Vaccination order facility id is invalid", ErrorType.WARNING, 4, "DQA0444"), VACCINATION_ORDER_FACILITY_ID_IS_MISSING(
      "Vaccination order facility id is missing", ErrorType.ACCEPT, 3, "DQA0445"), VACCINATION_ORDER_FACILITY_ID_IS_UNRECOGNIZED(
      "Vaccination order facility id is unrecognized", ErrorType.WARNING, 4, "DQA0446"), VACCINATION_ORDER_FACILITY_NAME_IS_MISSING(
      "Vaccination order facility name is missing", ErrorType.WARNING, 3, "DQA0441"), VACCINATION_ORDERED_BY_IS_DEPRECATED(
      "Vaccination ordered by is deprecated", ErrorType.WARNING, 4, "DQA0345"), VACCINATION_ORDERED_BY_IS_IGNORED(
      "Vaccination ordered by is ignored", ErrorType.UNKNOWN, 4, "DQA0346"), VACCINATION_ORDERED_BY_IS_INVALID(
      "Vaccination ordered by is invalid", ErrorType.WARNING, 4, "DQA0347"), VACCINATION_ORDERED_BY_IS_MISSING(
      "Vaccination ordered by is missing", ErrorType.ACCEPT, 3, "DQA0348"), VACCINATION_ORDERED_BY_IS_UNRECOGNIZED(
      "Vaccination ordered by is unrecognized", ErrorType.WARNING, 4, "DQA0349"), VACCINATION_PLACER_ORDER_NUMBER_IS_DEPRECATED(
      "Vaccination placer order number is deprecated", ErrorType.WARNING, 4, "DQA0384"), VACCINATION_PLACER_ORDER_NUMBER_IS_IGNORED(
      "Vaccination placer order number is ignored", ErrorType.UNKNOWN, 4, "DQA0385"), VACCINATION_PLACER_ORDER_NUMBER_IS_INVALID(
      "Vaccination placer order number is invalid", ErrorType.ERROR, 4, "DQA0386"), VACCINATION_PLACER_ORDER_NUMBER_IS_MISSING(
      "Vaccination placer order number is missing", ErrorType.ACCEPT, 3, "DQA0387"), VACCINATION_PLACER_ORDER_NUMBER_IS_UNRECOGNIZED(
      "Vaccination placer order number is unrecognized", ErrorType.WARNING, 4, "DQA0388"), VACCINATION_PRODUCT_IS_DEPRECATED(
      "Vaccination product is deprecated", ErrorType.WARNING, 4, "DQA0350"), VACCINATION_PRODUCT_IS_INVALID(
      "Vaccination product is invalid", ErrorType.ERROR, 4, "DQA0351"), VACCINATION_PRODUCT_IS_INVALID_FOR_DATE_ADMINISTERED(
      "Vaccination product is invalid for date administered", ErrorType.ERROR, 1, "DQA0493"), VACCINATION_PRODUCT_IS_MISSING(
      "Vaccination product is missing", ErrorType.ACCEPT, 3, "DQA0352"), VACCINATION_PRODUCT_IS_UNEXPECTED_FOR_DATE_ADMINISTERED(
      "Vaccination product is unexpected for date administered", ErrorType.ACCEPT, 2, "DQA0492"), VACCINATION_PRODUCT_IS_UNRECOGNIZED(
      "Vaccination product is unrecognized", ErrorType.ERROR, 2, "DQA0353"), VACCINATION_RECORDED_BY_IS_DEPRECATED(
      "Vaccination recorded by is deprecated", ErrorType.WARNING, 4, "DQA0354"), VACCINATION_RECORDED_BY_IS_IGNORED(
      "Vaccination recorded by is ignored", ErrorType.UNKNOWN, 4, "DQA0355"), VACCINATION_RECORDED_BY_IS_INVALID(
      "Vaccination recorded by is invalid", ErrorType.WARNING, 4, "DQA0356"), VACCINATION_RECORDED_BY_IS_MISSING(
      "Vaccination recorded by is missing", ErrorType.ACCEPT, 3, "DQA0357"), VACCINATION_RECORDED_BY_IS_UNRECOGNIZED(
      "Vaccination recorded by is unrecognized", ErrorType.WARNING, 4, "DQA0358"), VACCINATION_REFUSAL_REASON_CONFLICTS_COMPLETION_STATUS(
      "Vaccination refusal reason conflicts completion status", ErrorType.ERROR, 1, "DQA0359"), VACCINATION_REFUSAL_REASON_IS_DEPRECATED(
      "Vaccination refusal reason is deprecated", ErrorType.WARNING, 4, "DQA0360"), VACCINATION_REFUSAL_REASON_IS_IGNORED(
      "Vaccination refusal reason is ignored", ErrorType.UNKNOWN, 4, "DQA0361"), VACCINATION_REFUSAL_REASON_IS_INVALID(
      "Vaccination refusal reason is invalid", ErrorType.ERROR, 4, "DQA0362"), VACCINATION_REFUSAL_REASON_IS_MISSING(
      "Vaccination refusal reason is missing", ErrorType.UNKNOWN, 3, "DQA0363"), VACCINATION_REFUSAL_REASON_IS_UNRECOGNIZED(
      "Vaccination refusal reason is unrecognized", ErrorType.WARNING, 3, "DQA0364"), VACCINATION_SYSTEM_ENTRY_TIME_IS_IN_FUTURE(
      "Vaccination system entry time is in future", ErrorType.ERROR, 1, "DQA0365"), VACCINATION_SYSTEM_ENTRY_TIME_IS_INVALID(
      "Vaccination system entry time is invalid", ErrorType.ERROR, 1, "DQA0366"), VACCINATION_SYSTEM_ENTRY_TIME_IS_MISSING(
      "Vaccination system entry time is missing", ErrorType.ACCEPT, 3, "DQA0367"), VACCINATION_VIS_CVX_CODE_IS_DEPRECATED(
      "Vaccination VIS CVX Code is deprecated", ErrorType.UNKNOWN, 4, "DQA0531"), VACCINATION_VIS_CVX_CODE_IS_IGNORED(
      "Vaccination VIS CVX Code is ignored", ErrorType.UNKNOWN, 4, "DQA0532"), VACCINATION_VIS_CVX_CODE_IS_INVALID(
      "Vaccination VIS CVX Code is invalid", ErrorType.UNKNOWN, 4, "DQA0533"), VACCINATION_VIS_CVX_CODE_IS_MISSING(
      "Vaccination VIS CVX Code is missing", ErrorType.UNKNOWN, 4, "DQA0534"), VACCINATION_VIS_CVX_CODE_IS_UNRECOGNIZED(
      "Vaccination VIS CVX Code is unrecognized", ErrorType.UNKNOWN, 4, "DQA0535"), VACCINATION_VIS_DOCUMENT_TYPE_IS_DEPRECATED(
      "Vaccination vis document type is deprecated", ErrorType.WARNING, 4, "DQA0496"), VACCINATION_VIS_DOCUMENT_TYPE_IS_IGNORED(
      "Vaccination vis document type is ignored", ErrorType.UNKNOWN, 4, "DQA0497"), VACCINATION_VIS_DOCUMENT_TYPE_IS_INCORRECT(
      "Vaccination vis document type is incorrect", ErrorType.WARNING, 2, "DQA0498"), VACCINATION_VIS_DOCUMENT_TYPE_IS_INVALID(
      "Vaccination vis document type is invalid", ErrorType.WARNING, 4, "DQA0499"), VACCINATION_VIS_DOCUMENT_TYPE_IS_MISSING(
      "Vaccination vis document type is missing", ErrorType.WARNING, 2, "DQA0500"), VACCINATION_VIS_DOCUMENT_TYPE_IS_OUT_OF_DATE(
      "Vaccination vis document type is out-of-date", ErrorType.WARNING, 2, "DQA0502"), VACCINATION_VIS_DOCUMENT_TYPE_IS_UNRECOGNIZED(
      "Vaccination vis document type is unrecognized", ErrorType.WARNING, 2, "DQA0501"), VACCINATION_VIS_IS_DEPRECATED(
      "Vaccination VIS is deprecated", ErrorType.UNKNOWN, 4, "DQA0530"), VACCINATION_VIS_IS_MISSING(
      "Vaccination VIS is missing", ErrorType.UNKNOWN, 4, "DQA0528"), VACCINATION_VIS_IS_UNRECOGNIZED(
      "Vaccination VIS is unrecognized", ErrorType.UNKNOWN, 4, "DQA0529"), VACCINATION_VIS_PRESENTED_DATE_IS_AFTER_ADMIN_DATE(
      "Vaccination vis presented date is after admin date", ErrorType.WARNING, 2, "DQA0511"), VACCINATION_VIS_PRESENTED_DATE_IS_BEFORE_PUBLISHED_DATE(
      "Vaccination vis presented date is before published date", ErrorType.WARNING, 2, "DQA0510"), VACCINATION_VIS_PRESENTED_DATE_IS_INVALID(
      "Vaccination vis presented date is invalid", ErrorType.WARNING, 2, "DQA0507"), VACCINATION_VIS_PRESENTED_DATE_IS_MISSING(
      "Vaccination vis presented date is missing", ErrorType.WARNING, 2, "DQA0508"), VACCINATION_VIS_PRESENTED_DATE_IS_NOT_ADMIN_DATE(
      "Vaccination vis presented date is not admin date", ErrorType.WARNING, 2, "DQA0509"), VACCINATION_VIS_PUBLISHED_DATE_IS_IN_FUTURE(
      "Vaccination vis published date is in future", ErrorType.WARNING, 2, "DQA0506"), VACCINATION_VIS_PUBLISHED_DATE_IS_INVALID(
      "Vaccination vis published date is invalid", ErrorType.WARNING, 2, "DQA0503"), VACCINATION_VIS_PUBLISHED_DATE_IS_MISSING(
      "Vaccination vis published date is missing", ErrorType.WARNING, 2, "DQA0504"), VACCINATION_VIS_PUBLISHED_DATE_IS_UNRECOGNIZED(
      "Vaccination vis published date is unrecognized", ErrorType.WARNING, 2, "DQA0505"), ;

  private int priority = 0;
  private String name = "";
  private String appErrorCode = "";

  public String getAppErrorCode() {
    return appErrorCode;
  }

  public void setAppErrorCode(String appErrorCode) {
    this.appErrorCode = appErrorCode;
  }

  private ErrorType defaultErrorType = null;

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ErrorType getDefaultErrorType() {
    return defaultErrorType;
  }

  public void setDefaultErrorType(ErrorType defaultErrorType) {
    this.defaultErrorType = defaultErrorType;
  }

  Issue(String name, ErrorType defaultErrorType, int priority, String appErrorCode) {
    this.name = name;
    this.defaultErrorType = defaultErrorType;
    this.priority = priority;
    this.appErrorCode = appErrorCode;
  }
}
