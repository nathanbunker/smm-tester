package org.immregistries.smm;


public interface RecordServletInterface {
  public static final String VALUE_DATE_FORMAT = "yyyyMMddHHmmss+z";
  public static final String VALUE_DATE_NO_TIME_FORMAT = "yyyyMMdd";

  public static final String VALUE_EXCEPTIONAL_PREFIX_CERTIFIED_MESSAGE = "Certified Message:";
  public static final String VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK = "Tolerance Check:";


  public static final String PARAM_RESOURCE = "resource";
  public static final String RESOURCE_PROFILE = "profile";
  public static final String RESOURCE_PROFILES = "profiles";
  public static final String RESOURCE_TEST_PARTICIPANT = "testParticipant";
  public static final String PARAM_PROFILE_USAGE_ID = "profileUsageId";
  public static final String RESOURCE_TEST_FIELDS = "testFields";
  public static final String RESOURCE_TEST_TRANSFORMS = "testTransforms";

  public static final String VALUE_YES = "Y";
  public static final String VALUE_NO = "N";

  public static final String VALUE_TEST_TYPE_PREP = "prep";
  public static final String VALUE_TEST_TYPE_UPDATE = "update";
  public static final String VALUE_TEST_TYPE_QUERY = "query";

  public static final int SUITE_Q_FORECAST = 16;

  public static final String PARAM_TC_PUBLIC_ID_CODE = "tc_publicIdCode";
  public static final String PARAM_TC_ACCESS_PASSCODE = "tc_accessPasscode";
  public static final String PARAM_TC_CONNECTION_TYPE = "tc_connectionType";
  public static final String PARAM_TC_CONNECTION_URL = "tc_connectionUrl";
  public static final String PARAM_TC_CONNECTION_ACK_TYPE = "tc_connectionAckType";
  public static final String PARAM_TC_CONNECTION_CONFIG = "tc_connectionConfig";
  public static final String PARAM_TC_COMPLETE_TEST = "tc_copmleteTest";
  public static final String PARAM_TC_QUERY_TYPE = "tc_queryType";
  public static final String PARAM_TC_QUERY_ENABLED = "tc_queryEnabled";
  public static final String PARAM_TC_TEST_LOG = "tc_testLog";
  public static final String PARAM_TC_TEST_STATUS = "tc_testStatus";
  public static final String PARAM_TC_TEST_STARTED_TIME = "tc_testStartedTime";
  public static final String PARAM_TC_TEST_FINISHED_TIME = "tc_testFinishedTime";
  public static final String PARAM_TC_PROFILE_BASE_NAME = "tc_profileBaseName";
  public static final String PARAM_TC_PROFILE_COMPARE_NAME = "tc_profileCompareName";
  public static final String PARAM_TC_PER_QUERY_TOTAL = "tc_perQueryTotal";
  public static final String PARAM_TC_PER_QUERY_COUNT = "tc_perQueryCount";
  public static final String PARAM_TC_PER_QUERY_MIN = "tc_perQueryMin";
  public static final String PARAM_TC_PER_QUERY_MAX = "tc_perQueryMax";
  public static final String PARAM_TC_PER_QUERY_STD = "tc_perQueryStd";
  public static final String PARAM_TC_PER_UPDATE_TOTAL = "tc_perUpdateTotal";
  public static final String PARAM_TC_PER_UPDATE_COUNT = "tc_perUpdateCount";
  public static final String PARAM_TC_PER_UPDATE_MIN = "tc_perUpdateMin";
  public static final String PARAM_TC_PER_UPDATE_MAX = "tc_perUpdateMax";
  public static final String PARAM_TC_PER_UPDATE_STD = "tc_perUpdateStd";
  public static final String PARAM_TC_TRANSFORMS = "tc_transforms";

  public static final String[] PARAMS_TC = {PARAM_TC_PUBLIC_ID_CODE, PARAM_TC_ACCESS_PASSCODE,
      PARAM_TC_CONNECTION_TYPE, PARAM_TC_CONNECTION_URL, PARAM_TC_CONNECTION_ACK_TYPE,
      PARAM_TC_CONNECTION_CONFIG, PARAM_TC_QUERY_TYPE, PARAM_TC_QUERY_ENABLED, PARAM_TC_TEST_LOG,
      PARAM_TC_TEST_STATUS, PARAM_TC_TEST_STARTED_TIME, PARAM_TC_TEST_FINISHED_TIME,
      PARAM_TC_PROFILE_BASE_NAME, PARAM_TC_PROFILE_COMPARE_NAME, PARAM_TC_PER_QUERY_TOTAL,
      PARAM_TC_PER_QUERY_COUNT, PARAM_TC_PER_QUERY_MIN, PARAM_TC_PER_QUERY_MAX,
      PARAM_TC_PER_QUERY_STD, PARAM_TC_PER_UPDATE_TOTAL, PARAM_TC_PER_UPDATE_COUNT,
      PARAM_TC_PER_UPDATE_MIN, PARAM_TC_PER_UPDATE_MAX, PARAM_TC_PER_UPDATE_STD};



  public static final String VALUE_RESULT_ACK_STORE_STATUS_ACCEPTED_RETURNED = "a-r";
  public static final String VALUE_RESULT_ACK_STORE_STATUS_ACCEPTED_NOT_RETURNED = "a-nr";
  public static final String VALUE_RESULT_ACK_STORE_STATUS_NOT_ACCEPTED_RETURNED = "na-r";
  public static final String VALUE_RESULT_ACK_STORE_STATUS_NOT_ACCEPTED_NOT_RETURNED = "na-nr";

  public static final String VALUE_RESULT_QUERY_TYPE_MATCH = "Match";
  public static final String VALUE_RESULT_QUERY_TYPE_MATCH_Z32 = "Match (Z32)";
  public static final String VALUE_RESULT_QUERY_TYPE_MATCH_Z42 = "Match (Z42)";
  public static final String VALUE_RESULT_QUERY_TYPE_LIST = "List";
  public static final String VALUE_RESULT_QUERY_TYPE_NOT_FOUND = "Not Found";
  public static final String VALUE_RESULT_QUERY_TYPE_NOT_FOUND_Z33 = "Not Found (Z33)";
  public static final String VALUE_RESULT_QUERY_TYPE_ERROR_Z33 = "Error (Z33)";
  public static final String VALUE_RESULT_QUERY_TYPE_ERROR_Z33_QUERY_REJECTED = "Error (Z33) Query Rejected";
  public static final String VALUE_RESULT_QUERY_TYPE_UNEXPECTED_IIS_RESPONSE = "Unexpected IIS Response";
  public static final String VALUE_RESULT_QUERY_TYPE_TOO_MANY = "Too Many";
  public static final String VALUE_RESULT_QUERY_TYPE_ERROR = "Error";
  public static final String VALUE_RESULT_QUERY_TYPE_PROFILE_ID_MISSING = "Profile Id Missing";
  public static final String VALUE_RESULT_QUERY_TYPE_PROFILE_ID_UNEXPECTED =
      "Profile Id Unexpected";
  public static final String VALUE_RESULT_QUERY_TYPE_MULTIPLE_Z31_Z33 =
      "List or Not Found or Too Many (Z31 or Z33)";
  public static final String VALUE_RESULT_QUERY_TYPE_NOT_FOUND_OR_TOO_MANY =
      "Not Found or Too Many (Z33)";

  public static final String VALUE_RESULT_FORECAST_STATUS_INCLUDED = "Included";
  public static final String VALUE_RESULT_FORECAST_STATUS_NOT_INCLUDED = "Not Included";


  public static final String PARAM_TM_TEST_POSITION = "tm_testPosition";
  public static final String PARAM_TM_TEST_TYPE = "tm_testType";
  public static final String PARAM_TM_TEST_CASE_NUMBER = "tm_testCaseNumber";
  public static final String PARAM_TM_TEST_CASE_DESCRIPTION = "tm_testCaseDescription";
  public static final String PARAM_TM_TEST_CASE_ASSERT_RESULT = "tm_testCaseAssertResult";
  public static final String PARAM_TM_TEST_CASE_FIELD_NAME = "tm_testCaseFieldName";
  public static final String PARAM_TM_PREP_MAJOR_CHANGES_MADE = "tm_prepMajorChangesMade";
  public static final String PARAM_TM_PREP_MESSAGE_DERIVED_FROM = "tm_prepMessageDerivedFrom";
  public static final String PARAM_TM_PREP_DERIVED_FROM_TEST_CASE_CATEGORY =
      "tm_prepDerivedFromTestCategory";
  public static final String PARAM_TM_PREP_MESSAGE_ORIGINAL_RESPONSE =
      "tm_prepMessageOriginalResponse";
  public static final String PARAM_TM_PREP_MESSAGE_ACTUAL = "tm_prepMessageActual";
  public static final String PARAM_TM_RESULT_MESSAGE_ACTUAL = "tm_resultMessageActual";
  public static final String PARAM_TM_RESULT_STATUS = "tm_resultStatus";
  public static final String PARAM_TM_RESULT_ACCEPTED = "tm_resultAccepted";
  public static final String PARAM_TM_RESULT_EXECEPTION_TEXT = "tm_resultExceptionText";
  public static final String PARAM_TM_RESULT_ACK_TYPE = "tm_resultAckType";
  public static final String PARAM_TM_RESULT_ACK_CONFORMANCE = "tm_resultAckConformance";
  public static final String PARAM_TM_RESULT_QUERY_TYPE = "tm_resultQueryType";
  public static final String PARAM_TM_RESULT_FORECAST_STATUS = "tm_resultForecastStatus";
  public static final String PARAM_TM_FORECAST_TEST_PANEL_CASE_ID = "tm_forecastTestPanelCaseId";
  public static final String PARAM_TM_FORECAST_TEST_PANEL_ID = "tm_forecastTestPanelId";
  public static final String PARAM_TM_RESULT_RUN_TIME_MS = "tm_runTimeMs";
  public static final String PARAM_TM_TEST_RUN_LOG = "tm_testRunLog";

  public static final String[] PARAMS_TM = {PARAM_TM_TEST_POSITION, PARAM_TM_TEST_TYPE,
      PARAM_TM_TEST_CASE_NUMBER, PARAM_TM_TEST_CASE_DESCRIPTION, PARAM_TM_TEST_CASE_ASSERT_RESULT,
      PARAM_TM_PREP_MAJOR_CHANGES_MADE, PARAM_TM_PREP_MESSAGE_DERIVED_FROM,
      PARAM_TM_PREP_MESSAGE_ACTUAL, PARAM_TM_RESULT_MESSAGE_ACTUAL, PARAM_TM_RESULT_STATUS,
      PARAM_TM_RESULT_EXECEPTION_TEXT, PARAM_TM_RESULT_ACK_TYPE,
      PARAM_TM_FORECAST_TEST_PANEL_CASE_ID, PARAM_TM_FORECAST_TEST_PANEL_ID};

  public static final String VALUE_PROFILE_TYPE_PRESENT = "present";
  public static final String VALUE_PROFILE_TYPE_ABSENT = "absent";

  public static final String PARAM_TP_TEST_PROFILE_STATUS = "tp_testProfileStatus";
  public static final String PARAM_TP_TEST_PROFILE_TYPE = "tp_testProfileType";
  public static final String PARAM_TP_PROFILE_FIELD_POS = "tp_profileFieldPos";
  public static final String PARAM_TP_PROFILE_FIELD_NAME = "tp_profileFieldName";
  public static final String PARAM_TP_USAGE_EXPECTED = "tp_usageExpected";
  public static final String PARAM_TP_USAGE_DETECTED = "tp_usageDetected";
  public static final String PARAM_TP_ACCEPT_EXPECTED = "tp_acceptExpected";
  public static final String PARAM_TP_ACCEPT_DETECTED = "tp_acceptDetected";
  public static final String PARAM_TP_TEST_MESSAGE_PRESENT_POS = "tp_testMessagePresentPos";
  public static final String PARAM_TP_TEST_MESSAGE_ABSENT_POS = "tp_testMessageAbsentPos";
  public static final String PARAM_TP_MESSAGE_ACCEPT_STATUS_DEBUG = "tp_messageAcceptStatusDebug";
  public static final String PARAM_TP_COMPATIBILITY_CONFORMANCE = "tp_compatibilityConformance";

  public static final String[] PARAMS_TP = {PARAM_TP_TEST_PROFILE_STATUS,
      PARAM_TP_PROFILE_FIELD_POS, PARAM_TP_PROFILE_FIELD_NAME, PARAM_TP_USAGE_EXPECTED,
      PARAM_TP_USAGE_DETECTED, PARAM_TP_ACCEPT_EXPECTED, PARAM_TP_ACCEPT_DETECTED,
      PARAM_TP_TEST_MESSAGE_PRESENT_POS, PARAM_TP_TEST_MESSAGE_ABSENT_POS,
      PARAM_TP_MESSAGE_ACCEPT_STATUS_DEBUG, PARAM_TP_COMPATIBILITY_CONFORMANCE};

  public static final String PARAM_C_FIELD_NAME = "c_comparisonId";
  public static final String PARAM_C_FIELD_LABEL = "c_comparisonFieldId";
  public static final String PARAM_C_PRIORITY_LABEL = "c_priorityLabel";
  public static final String PARAM_C_VALUE_ORIGINAL = "c_valueOriginal";
  public static final String PARAM_C_VALUE_COMPARE = "c_valueCompare";
  public static final String PARAM_C_COMPARISON_STATUS = "c_comparisonStatus";

  public static final String VALUE_COMPARISON_STATUS_PASS = "pass";
  public static final String VALUE_COMPARISON_STATUS_FAIL = "fail";
  public static final String VALUE_COMPARISON_STATUS_NOT_TESTED = "not tested";

  public static final String[] PARAMS_C =
      {PARAM_C_FIELD_NAME, PARAM_C_FIELD_LABEL, PARAM_C_PRIORITY_LABEL, PARAM_C_VALUE_ORIGINAL,
          PARAM_C_VALUE_COMPARE, PARAM_C_COMPARISON_STATUS};

  public static final String PARAM_TPAR_ORGANIZATION_NAME = "tpar_organizationName";
  public static final String PARAM_TPAR_PUBLIC_ID_CODE = "tpar_publicIdCode";
  public static final String PARAM_TPAR_ACCESS_PASSCODE = "tpar_accessPasscode";
  public static final String PARAM_TPAR_MAP_ROW = "tpar_mapRow";
  public static final String PARAM_TPAR_MAP_COL = "tpar_mapCol";
  public static final String PARAM_TPAR_PLATFORM_LABEL = "tpar_platformLabel";
  public static final String PARAM_TPAR_VENDOR_LABEL = "tpar_vendorLabel";
  public static final String PARAM_TPAR_INTERNAL_COMMENTS = "tpar_internalComments";
  public static final String PARAM_TPAR_PHASE1_PARTICIPATION = "tpar_phase1Participation";
  public static final String PARAM_TPAR_PHASE1_STATUS = "tpar_phase1Status";
  public static final String PARAM_TPAR_PHASE1_COMMENTS = "tpar_phase1Comments";
  public static final String PARAM_TPAR_PHASE2_PARTICIPATION = "tpar_phase2Participation";
  public static final String PARAM_TPAR_PHASE2_STATUS = "tpar_phase2Status";
  public static final String PARAM_TPAR_PHASE2_COMMENTS = "tpar_phase2Comments";
  public static final String PARAM_TPAR_IHS_STATUS = "tpar_ihsStatus";
  public static final String PARAM_TPAR_GUIDE_STATUS = "tpar_guideStatus";
  public static final String PARAM_TPAR_GUIDE_NAME = "tpar_guideName";
  public static final String PARAM_TPAR_CONNECT_STATUS = "tpar_connectStatus";
  public static final String PARAM_TPAR_GENERAL_COMMENTS = "tpar_generalComments";
  public static final String PARAM_TPAR_TRANSPORT_TYPE = "tpar_transportType";
  public static final String PARAM_TPAR_QUERY_SUPPORT = "tpar_querySupport";
  public static final String PARAM_TPAR_NIST_STATUS = "tpar_nistStatus";

  public static final String[] PARAMS_TPAR =
      {PARAM_TPAR_ORGANIZATION_NAME, PARAM_TPAR_MAP_ROW, PARAM_TPAR_MAP_COL,
          PARAM_TPAR_PLATFORM_LABEL, PARAM_TPAR_VENDOR_LABEL, PARAM_TPAR_INTERNAL_COMMENTS,
          PARAM_TPAR_PHASE1_PARTICIPATION, PARAM_TPAR_PHASE1_STATUS, PARAM_TPAR_PHASE1_COMMENTS,
          PARAM_TPAR_PHASE2_PARTICIPATION, PARAM_TPAR_PHASE2_STATUS, PARAM_TPAR_PHASE2_COMMENTS,
          PARAM_TPAR_IHS_STATUS, PARAM_TPAR_GUIDE_STATUS, PARAM_TPAR_GUIDE_NAME,
          PARAM_TPAR_CONNECT_STATUS, PARAM_TPAR_GENERAL_COMMENTS, PARAM_TPAR_TRANSPORT_TYPE,
          PARAM_TPAR_QUERY_SUPPORT, PARAM_TPAR_NIST_STATUS, PARAM_TPAR_ACCESS_PASSCODE};


  public static final String PARAM_A_ASSERTION_RESULT = "a_assertionResult";
  public static final String PARAM_A_LOCATION_PATH = "a_locationPath";
  public static final String PARAM_A_ASSERTION_TYPE = "a_assertionType";
  public static final String PARAM_A_ASSERTION_DESCRIPTION = "a_assertionDescription";

  public static final String[] PARAMS_A = {PARAM_A_ASSERTION_RESULT, PARAM_A_LOCATION_PATH,
      PARAM_A_ASSERTION_TYPE, PARAM_A_ASSERTION_DESCRIPTION};

  public static final String VALUE_FORECAST_TYPE_ACTUAL = "Actual";
  public static final String VALUE_FORECAST_TYPE_EXPECTED = "Expected";

  public static final String PARAM_F_VACCINE_CODE = "f_vaccineCode";
  public static final String PARAM_F_FORECAST_TYPE = "f_forecastType";
  public static final String PARAM_F_SCHEDULE_NAME = "f_scheduleName";
  public static final String PARAM_F_SERIES_NAME = "f_seriesName";
  public static final String PARAM_F_SERIES_DOSE_COUNT = "f_seriesDoseCount";
  public static final String PARAM_F_DOSE_NUMBER = "f_doseNumber";
  public static final String PARAM_F_DATE_EARLIEST = "f_dateEarliest";
  public static final String PARAM_F_DATE_DUE = "f_dateDue";
  public static final String PARAM_F_DATE_OVERDUE = "f_dateOverdue";
  public static final String PARAM_F_DATE_LATEST = "f_dateLatest";
  public static final String PARAM_F_SERIES_STATUS = "f_seriesStatus";
  public static final String PARAM_F_REASON_CODE = "f_reasonCode";

  public static final String VALUE_EVALUATION_TYPE_ACTUAL = "Actual";
  public static final String VALUE_EVALUATION_TYPE_EXPECTED = "Expected";

  public static final String PARAM_E_COMPONENT_CODE = "e_componentCode";
  public static final String PARAM_E_VACCINE_CODE = "e_vaccineCode";
  public static final String PARAM_E_VACCINE_DATE = "e_vaccineDate";
  public static final String PARAM_E_EVALUATION_TYPE = "e_evaluationType";
  public static final String PARAM_E_SCHEDULE_NAME = "e_scheduleName";
  public static final String PARAM_E_DOSE_NUMBER = "e_doseNumber";
  public static final String PARAM_E_DOSE_VALIDITY = "e_doseValidity";
  public static final String PARAM_E_SERIES_NAME = "e_seriesName";
  public static final String PARAM_E_SERIES_DOSE_COUNT = "e_seriesDoseCount";
  public static final String PARAM_E_SERIES_STATUS = "e_seriesStatus";
  public static final String PARAM_E_REASON_CODE = "e_reasonCode";


  public static final String PARAM_TESTER_STATUS_UPDATE = "testerStatus_update";
  public static final String PARAM_TESTER_STATUS_TESTER_NAME = "testerStatus_testerName";
  public static final String PARAM_TESTER_STATUS_SECTION_TYPES = "testerStatus_sectionTypes";
  public static final String PARAM_TESTER_STATUS_READY_STATUS = "testerStatus_readyStatus";
  public static final String PARAM_TESTER_STATUS_PUBLIC_ID_CODE = "testerStatus_publicIdCode";
  public static final String PARAM_TESTER_STATUS_ACCESS_PASSCODE = "testerStatus_accessPasscode";

  public static final String PARAM_TESTER_STATUS_TESTER_STATUS_READY = "Ready";
  public static final String PARAM_TESTER_STATUS_TESTER_STATUS_ERROR = "Error";
  public static final String PARAM_TESTER_STATUS_TESTER_STATUS_STOPPED = "Stopped";
  public static final String PARAM_TESTER_STATUS_TESTER_STATUS_TESTING = "Testing";

  public static final String PARAM_TESTER_ACTION_RUN = "Run";

  public static final String OPTION_AUTO_TEST_NAME_SELECT = "--autoTestNameSelect ";

  public static final String PARAM_TEST_MESSAGE_ID = "testMessageId";
}
