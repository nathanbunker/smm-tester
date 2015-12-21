package org.openimmunizationsoftware.dqa.tr;

import java.text.SimpleDateFormat;

public interface RecordServletInterface {
  public static final SimpleDateFormat VALUE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss+z");

  public static final String VALUE_EXCEPTIONAL_PREFIX_CERTIFIED_MESSAGE = "Certified Message:";
  public static final String VALUE_EXCEPTIONAL_PREFIX_TOLERANCE_CHECK = "Tolerance Check:";

  
  public static final String VALUE_YES = "Y";
  public static final String VALUE_NO = "N";

  public static final String VALUE_TEST_TYPE_PREP = "prep";
  public static final String VALUE_TEST_TYPE_UPDATE = "update";
  public static final String VALUE_TEST_TYPE_QUERY = "query";

  public static final String VALUE_TEST_SECTION_TYPE_BASIC = "Basic";
  public static final String VALUE_TEST_SECTION_TYPE_INTERMEDIATE = "Intermediate";
  public static final String VALUE_TEST_SECTION_TYPE_ADVANCED = "Advanced";
  public static final String VALUE_TEST_SECTION_TYPE_EXCEPTIONAL = "Exceptional";
  public static final String VALUE_TEST_SECTION_TYPE_FORECAST_PREP = "Forecast Prep";
  public static final String VALUE_TEST_SECTION_TYPE_FORECAST = "Forecast";
  public static final String VALUE_TEST_SECTION_TYPE_PERFORMANCE = "Performance";
  public static final String VALUE_TEST_SECTION_TYPE_CONFORMANCE = "Conformance";
  public static final String VALUE_TEST_SECTION_TYPE_CONFORMANCE_2015 = "Conformance 2015";
  public static final String VALUE_TEST_SECTION_TYPE_PROFILING = "Profiling";
  public static final String VALUE_TEST_SECTION_TYPE_ONC_2015 = "ONC 2015";
  public static final String VALUE_TEST_SECTION_TYPE_NOT_ACCEPTED = "Not Accepted";
  public static final String VALUE_TEST_SECTION_TYPE_QBP_SUPPORT = "QBP Support";
  public static final String VALUE_TEST_SECTION_TYPE_QBP_TRANSFORM = "Transform";
  public static final String VALUE_TEST_SECTION_TYPE_QBP_EXTRA = "Extra";

  public static final String PARAM_TC_CONNECTION_LABEL = "tc_connectionLabel";
  public static final String PARAM_TC_CONNECTION_TYPE = "tc_connectionType";
  public static final String PARAM_TC_CONNECTION_URL = "tc_connectionUrl";
  public static final String PARAM_TC_CONNECTION_ACK_TYPE = "tc_connectionAckType";
  public static final String PARAM_TC_CONNECTION_CONFIG = "tc_connectionConfig";
  public static final String PARAM_TC_COMPLETE_TEST = "tc_copmleteTest";
  public static final String PARAM_TC_QUERY_TYPE = "tc_queryType";
  public static final String PARAM_TC_QUERY_ENABLED = "tc_queryEnabled";
  public static final String PARAM_TC_QUERY_PAUSE = "tc_queryPause";
  public static final String PARAM_TC_TEST_LOG = "tc_testLog";
  public static final String PARAM_TC_TEST_STATUS = "tc_testStatus";
  public static final String PARAM_TC_TEST_STARTED_TIME = "tc_testStartedTime";
  public static final String PARAM_TC_TEST_FINISHED_TIME = "tc_testFinishedTime";
  public static final String PARAM_TC_COUNT_UPDATE = "tc_countUpdate";
  public static final String PARAM_TC_COUNT_QUERY = "tc_countQuery";
  public static final String PARAM_TC_PROFILE_BASE_NAME = "tc_profileBaseName";
  public static final String PARAM_TC_PROFILE_COMPARE_NAME = "tc_profileCompareName";
  public static final String PARAM_TC_SCORE_OVERALL = "tc_scoreOverall";
  public static final String PARAM_TC_SCORE_INTEROP = "tc_scoreInterop";
  public static final String PARAM_TC_SCORE_CODED = "tc_scoreCoded";
  public static final String PARAM_TC_SCORE_LOCAL = "tc_scoreLocal";
  public static final String PARAM_TC_SCORE_NATIONAL = "tc_scoreNational";
  public static final String PARAM_TC_SCORE_TOLERANCE = "tc_scoreTolerance";
  public static final String PARAM_TC_SCORE_EHR = "tc_scoreEhr";
  public static final String PARAM_TC_SCORE_PERFORM = "tc_scorePerform";
  public static final String PARAM_TC_SCORE_ACK = "tc_scoreAck";
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

  public static final String[] PARAMS_TC = { PARAM_TC_CONNECTION_LABEL, PARAM_TC_CONNECTION_TYPE, PARAM_TC_CONNECTION_URL,
      PARAM_TC_CONNECTION_ACK_TYPE, PARAM_TC_CONNECTION_CONFIG, PARAM_TC_QUERY_TYPE, PARAM_TC_QUERY_ENABLED, PARAM_TC_QUERY_PAUSE, PARAM_TC_TEST_LOG,
      PARAM_TC_TEST_STATUS, PARAM_TC_TEST_STARTED_TIME, PARAM_TC_TEST_FINISHED_TIME, PARAM_TC_COUNT_UPDATE, PARAM_TC_COUNT_QUERY,
      PARAM_TC_PROFILE_BASE_NAME, PARAM_TC_PROFILE_COMPARE_NAME, PARAM_TC_SCORE_OVERALL, PARAM_TC_SCORE_INTEROP, PARAM_TC_SCORE_CODED,
      PARAM_TC_SCORE_LOCAL, PARAM_TC_SCORE_NATIONAL, PARAM_TC_SCORE_TOLERANCE, PARAM_TC_SCORE_EHR, PARAM_TC_SCORE_PERFORM, PARAM_TC_SCORE_ACK,
      PARAM_TC_PER_QUERY_TOTAL, PARAM_TC_PER_QUERY_COUNT, PARAM_TC_PER_QUERY_MIN, PARAM_TC_PER_QUERY_MAX, PARAM_TC_PER_QUERY_STD,
      PARAM_TC_PER_UPDATE_TOTAL, PARAM_TC_PER_UPDATE_COUNT, PARAM_TC_PER_UPDATE_MIN, PARAM_TC_PER_UPDATE_MAX, PARAM_TC_PER_UPDATE_STD };

  public static final String PARAM_TS_TEST_SECTION_TYPE = "ts_testSectionType";
  public static final String PARAM_TS_TEST_ENABLED = "ts_testEnabled";
  public static final String PARAM_TS_SCORE_LEVEL1 = "ts_scoreLevel1";
  public static final String PARAM_TS_SCORE_LEVEL2 = "ts_scoreLevel2";
  public static final String PARAM_TS_SCORE_LEVEL3 = "ts_scoreLevel3";
  public static final String PARAM_TS_PROGRESS_LEVEL1 = "ts_progressLevel1";
  public static final String PARAM_TS_PROGRESS_LEVEL2 = "ts_progressLevel2";
  public static final String PARAM_TS_PROGRESS_LEVEL3 = "ts_progressLevel3";
  public static final String PARAM_TS_COUNT_LEVEL1 = "ts_countLevel1";
  public static final String PARAM_TS_COUNT_LEVEL2 = "ts_countLevel2";
  public static final String PARAM_TS_COUNT_LEVEL3 = "ts_countLevel3";

  public static final String[] PARAMS_TS = { PARAM_TS_TEST_SECTION_TYPE, PARAM_TS_TEST_ENABLED, PARAM_TS_SCORE_LEVEL1, PARAM_TS_SCORE_LEVEL2,
      PARAM_TS_SCORE_LEVEL3, PARAM_TS_PROGRESS_LEVEL1, PARAM_TS_PROGRESS_LEVEL2, PARAM_TS_PROGRESS_LEVEL3, PARAM_TS_COUNT_LEVEL1,
      PARAM_TS_COUNT_LEVEL2, PARAM_TS_COUNT_LEVEL3 };
  
  public static final String VALUE_RESULT_ACK_CONFORMANCE_ERROR = "ERROR";
  public static final String VALUE_RESULT_ACK_CONFORMANCE_OK = "OK";
  public static final String VALUE_RESULT_ACK_CONFORMANCE_NOT_RUN = "NOT RUN";
  
  public static final String VALUE_RESULT_ACK_STORE_STATUS_ACCEPTED_RETURNED = "a-r";
  public static final String VALUE_RESULT_ACK_STORE_STATUS_ACCEPTED_NOT_RETURNED = "a-nr";
  public static final String VALUE_RESULT_ACK_STORE_STATUS_NOT_ACCEPTED_RETURNED = "na-r";
  public static final String VALUE_RESULT_ACK_STORE_STATUS_NOT_ACCEPTED_NOT_RETURNED = "na-nr";

  public static final String PARAM_TM_TEST_POSITION = "tm_testPosition";
  public static final String PARAM_TM_TEST_TYPE = "tm_testType";
  public static final String PARAM_TM_TEST_CASE_SET = "tm_testCaseSet";
  public static final String PARAM_TM_TEST_CASE_NUMBER = "tm_testCaseNumber";
  public static final String PARAM_TM_TEST_CASE_CATEGORY = "tm_testCaseCategory";
  public static final String PARAM_TM_TEST_CASE_DESCRIPTION = "tm_testCaseDescription";
  public static final String PARAM_TM_TEST_CASE_ASSERT_RESULT = "tm_testCaseAssertResult";
  public static final String PARAM_TM_PREP_PATIENT_TYPE = "tm_prepPatientType";
  public static final String PARAM_TM_PREP_TRANSFORMS_QUICK = "tm_prepTransformsQuick";
  public static final String PARAM_TM_PREP_TRANSFORMS_CUSTOM = "tm_prepTransformsCustom";
  public static final String PARAM_TM_PREP_TRANSFORMS_ADDITION = "tm_prepTransformsAddition";
  public static final String PARAM_TM_PREP_TRANSFORMS_CAUSE_ISSUE = "tm_prepTransformsCauseIssue";
  public static final String PARAM_TM_PREP_CAUSE_ISSUE_NAMES = "tm_prepCauseIssueNames";
  public static final String PARAM_TM_PREP_HAS_ISSUE = "tm_prepHasIssue";
  public static final String PARAM_TM_PREP_MAJOR_CHANGES_MADE = "tm_prepMajorChangesMade";
  public static final String PARAM_TM_PREP_NOT_EXPECTED_TO_CONFORM = "tm_prepNotExpectedToConform";
  public static final String PARAM_TM_PREP_MESSAGE_ACCEPT_STATUS_DEBUG = "tm_prepMessageAcceptStatusDebug";
  public static final String PARAM_TM_PREP_SCENARIO_NAME = "tm_prepScenarioName";
  public static final String PARAM_TM_PREP_MESSAGE_DERIVED_FROM = "tm_prepMessageDerivedFrom";
  public static final String PARAM_TM_PREP_MESSAGE_ORIGINAL = "tm_prepMessageOriginal";
  public static final String PARAM_TM_PREP_MESSAGE_ORIGINAL_RESPONSE = "tm_prepMessageOriginalResponse";
  public static final String PARAM_TM_PREP_MESSAGE_ACTUAL = "tm_prepMessageActual";
  public static final String PARAM_TM_RESULT_MESSAGE_ACTUAL = "tm_resultMessageActual";
  public static final String PARAM_TM_RESULT_STATUS = "tm_resultStatus";
  public static final String PARAM_TM_RESULT_ACCEPTED = "tm_resultAccepted";
  public static final String PARAM_TM_RESULT_EXECEPTION_TEXT = "tm_resultExceptionText";
  public static final String PARAM_TM_RESULT_ACCEPTED_MESSAGE = "tm_resultAcceptedMessage";
  public static final String PARAM_TM_RESULT_RESPONSE_TYPE = "tm_resultResponseType";
  public static final String PARAM_TM_RESULT_ACK_TYPE = "tm_resultAckType";
  public static final String PARAM_TM_RESULT_ACK_CONFORMANCE = "tm_resultAckConformance";
  public static final String PARAM_TM_RESULT_ACK_STORE_STATUS = "tm_resultStoreStatus";
  public static final String PARAM_TM_FORECAST_TEST_PANEL_CASE_ID = "tm_forecastTestPanelCaseId";
  public static final String PARAM_TM_FORECAST_TEST_PANEL_ID = "tm_forecastTestPanelId";

  public static final String[] PARAMS_TM = { PARAM_TM_TEST_POSITION, PARAM_TM_TEST_TYPE, PARAM_TM_TEST_CASE_SET, PARAM_TM_TEST_CASE_NUMBER,
      PARAM_TM_TEST_CASE_CATEGORY, PARAM_TM_TEST_CASE_DESCRIPTION, PARAM_TM_TEST_CASE_ASSERT_RESULT, PARAM_TM_PREP_PATIENT_TYPE,
      PARAM_TM_PREP_TRANSFORMS_QUICK, PARAM_TM_PREP_TRANSFORMS_CUSTOM, PARAM_TM_PREP_TRANSFORMS_ADDITION, PARAM_TM_PREP_TRANSFORMS_CAUSE_ISSUE,
      PARAM_TM_PREP_CAUSE_ISSUE_NAMES, PARAM_TM_PREP_HAS_ISSUE, PARAM_TM_PREP_MAJOR_CHANGES_MADE, PARAM_TM_PREP_NOT_EXPECTED_TO_CONFORM,
      PARAM_TM_PREP_MESSAGE_ACCEPT_STATUS_DEBUG, PARAM_TM_PREP_SCENARIO_NAME, PARAM_TM_PREP_MESSAGE_DERIVED_FROM, PARAM_TM_PREP_MESSAGE_ORIGINAL,
      PARAM_TM_PREP_MESSAGE_ACTUAL, PARAM_TM_RESULT_MESSAGE_ACTUAL, PARAM_TM_RESULT_STATUS, PARAM_TM_RESULT_EXECEPTION_TEXT,
      PARAM_TM_RESULT_ACCEPTED_MESSAGE, PARAM_TM_RESULT_RESPONSE_TYPE, PARAM_TM_RESULT_ACK_TYPE, PARAM_TM_FORECAST_TEST_PANEL_CASE_ID,
      PARAM_TM_FORECAST_TEST_PANEL_ID };

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
  
  public static final String[] PARAMS_TP = { PARAM_TP_TEST_PROFILE_STATUS, PARAM_TP_PROFILE_FIELD_POS, PARAM_TP_PROFILE_FIELD_NAME,
      PARAM_TP_USAGE_EXPECTED, PARAM_TP_USAGE_DETECTED, PARAM_TP_ACCEPT_EXPECTED, PARAM_TP_ACCEPT_DETECTED, PARAM_TP_TEST_MESSAGE_PRESENT_POS,
      PARAM_TP_TEST_MESSAGE_ABSENT_POS, PARAM_TP_MESSAGE_ACCEPT_STATUS_DEBUG, PARAM_TP_COMPATIBILITY_CONFORMANCE };

  public static final String PARAM_C_FIELD_NAME = "c_comparisonId";
  public static final String PARAM_C_FIELD_LABEL = "c_comparisonFieldId";
  public static final String PARAM_C_PRIORITY_LABEL = "c_priorityLabel";
  public static final String PARAM_C_VALUE_ORIGINAL = "c_valueOriginal";
  public static final String PARAM_C_VALUE_COMPARE = "c_valueCompare";
  public static final String PARAM_C_COMPARISON_STATUS = "c_comparisonStatus";

  public static final String VALUE_COMPARISON_STATUS_PASS = "pass";
  public static final String VALUE_COMPARISON_STATUS_FAIL = "fail";
  public static final String VALUE_COMPARISON_STATUS_NOT_TESTED = "not tested";

  public static final String[] PARAMS_C = { PARAM_C_FIELD_NAME, PARAM_C_FIELD_LABEL, PARAM_C_PRIORITY_LABEL, PARAM_C_VALUE_ORIGINAL,
      PARAM_C_VALUE_COMPARE, PARAM_C_COMPARISON_STATUS };
  
  public static final int MAP_COLS_MAX = 12;
  public static final int MAP_ROWS_MAX = 8;

  public static final String PARAM_TPAR_ORGANIZATION_NAME = "tpar_organizationName";
  public static final String PARAM_TPAR_CONNECTION_LABEL = "tpar_connectionLabel";
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
  public static final String PARAM_TPAR_ACCESS_PASSCODE = "tpar_accessPasscode";

  public static final String[] PARAMS_TPAR = { PARAM_TPAR_ORGANIZATION_NAME, PARAM_TPAR_CONNECTION_LABEL, PARAM_TPAR_MAP_ROW, PARAM_TPAR_MAP_COL,
      PARAM_TPAR_PLATFORM_LABEL, PARAM_TPAR_VENDOR_LABEL, PARAM_TPAR_INTERNAL_COMMENTS, PARAM_TPAR_PHASE1_PARTICIPATION, PARAM_TPAR_PHASE1_STATUS,
      PARAM_TPAR_PHASE1_COMMENTS, PARAM_TPAR_PHASE2_PARTICIPATION, PARAM_TPAR_PHASE2_STATUS, PARAM_TPAR_PHASE2_COMMENTS, PARAM_TPAR_IHS_STATUS,
      PARAM_TPAR_GUIDE_STATUS, PARAM_TPAR_GUIDE_NAME, PARAM_TPAR_CONNECT_STATUS, PARAM_TPAR_GENERAL_COMMENTS, PARAM_TPAR_TRANSPORT_TYPE,
      PARAM_TPAR_QUERY_SUPPORT, PARAM_TPAR_NIST_STATUS, PARAM_TPAR_ACCESS_PASSCODE };

  
  public static final String PARAM_A_ASSERTION_RESULT = "a_assertionResult";
  public static final String PARAM_A_LOCATION_PATH = "a_locationPath";
  public static final String PARAM_A_ASSERTION_TYPE = "a_assertionType";
  public static final String PARAM_A_ASSERTION_DESCRIPTION = "a_assertionDescription";
  
  public static final String[] PARAMS_A = {PARAM_A_ASSERTION_RESULT, PARAM_A_LOCATION_PATH, PARAM_A_ASSERTION_TYPE, PARAM_A_ASSERTION_DESCRIPTION};
}
