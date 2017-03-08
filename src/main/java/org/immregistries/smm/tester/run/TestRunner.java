/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immregistries.smm.tester.run;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.Session;

import org.apache.cxf.test.TestApplicationContext;
import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.RunAgainstConnector;
import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.tester.manager.nist.NISTValidator;
import org.immregistries.smm.tester.manager.nist.ValidationReport;
import org.immregistries.smm.tester.manager.nist.ValidationResource;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.TestError;
import org.immregistries.smm.transform.Transform;
import org.immregistries.smm.transform.Transformer;

/**
 * 
 * @author nathan
 */
public class TestRunner {
  public static final String ASSERT_RESULT_ACCEPT_ACCEPT_OR_ERROR = "Accept or Error";
  public static final String ASSERT_RESULT_ACCEPT_ACCEPT_OR_REJECT = "Accept or Reject";
  public static final String ASSERT_RESULT_ACCEPT_REJECT = "Reject";
  public static final String ASSERT_RESULT_ACCEPT_ERROR = "Error";
  public static final String ASSERT_RESULT_ACCEPT_ACCEPT_AND_SKIP = "Accept and Skip";
  public static final String ASSERT_RESULT_ACCEPT_ACCEPT_AND_WARN = "Accept and Warn";
  public static final String ASSERT_RESULT_ACCEPT = "Accept";
  public static final String ASSERT_RESULT_ERROR_LOCATION_IS_ = "Error Location is ";

  public static final String ACTUAL_RESULT_STATUS_FAIL = "FAIL";
  public static final String ACTUAL_RESULT_STATUS_PASS = "PASS";

  protected String ackMessageText = null;
  private boolean passedTest = false;
  private String status = "";
  private List<TestError> errorList = null;
  private ErrorType errorType = ErrorType.UNKNOWN;
  private HL7Reader ackMessageReader;
  private long startTime = 0;
  private long endTime = 0;
  private boolean wasRun = false;
  private boolean validateResponse = false;
  private String testSectionType = "";

  public String getTestSectionType() {
    return testSectionType;
  }

  public void setTestSectionType(String testSectionType) {
    this.testSectionType = testSectionType;
  }

  public boolean isValidateResponse() {
    return validateResponse;
  }

  public void setValidateResponse(boolean validateResponse) {
    this.validateResponse = validateResponse;
  }

  public boolean isWasRun() {
    return wasRun;
  }

  public void setWasRun(boolean wasRun) {
    this.wasRun = wasRun;
  }

  public long getTotalRunTime() {
    return endTime - startTime;
  }

  public HL7Reader getAckMessageReader() {
    return ackMessageReader;
  }

  public ErrorType getErrorType() {
    return errorType;
  }

  public List<TestError> getErrorList() {
    return errorList;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAckMessageText() {
    return ackMessageText;
  }

  public void setAckMessageText(String ack) {
    this.ackMessageText = ack;
  }

  public boolean isPassedTest() {
    return passedTest;
  }

  public void setPassedTest(boolean pass) {
    this.passedTest = pass;
  }

  public boolean runTest(Connector connector, TestCaseMessage testCaseMessage) throws Exception {
    testCaseMessage.setActualResponseMessage("");
    testCaseMessage.setPassedTest(false);
    testCaseMessage.setHasRun(false);

    String message = Transformer.transform(connector, testCaseMessage);
    wasRun = false;
    return runTest(connector, testCaseMessage, message);
  }

  public TestCaseMessage runTestIfNew(Connector connector, TestCaseMessage testCaseMessage, Map<String, TestCaseMessage> testCaseMessageMap)
      throws Exception {
    wasRun = false;
    testCaseMessage.setActualResponseMessage("");
    testCaseMessage.setPassedTest(false);
    testCaseMessage.setHasRun(false);
    String message = Transformer.transform(connector, testCaseMessage);
    if (testCaseMessageMap.containsKey(message)) {
      return testCaseMessageMap.get(message);
    }
    runTest(connector, testCaseMessage, message);
    testCaseMessageMap.put(message, testCaseMessage);
    return testCaseMessage;
  }

  public boolean runTest(Connector connector, TestCaseMessage testCaseMessage, String message) throws Exception {
    setupForRunTest(testCaseMessage, message);
    doRunTest(connector, testCaseMessage, message);
    evaluateRunTest(connector, testCaseMessage);
    if (validateResponse) {
      validateResponseWithNIST(testCaseMessage, ackMessageText);
    }
    return passedTest;
  }

  protected void evaluateRunTest(Connector connector, TestCaseMessage testCaseMessage) {
    errorList = new ArrayList<TestError>();
    String assertResult = testCaseMessage.getAssertResult();
    if (!assertResult.equalsIgnoreCase("")) {
      if (ackMessageText == null || ackMessageText.equals("")) {
        if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT)) {
          passedTest = false;
        } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_AND_WARN)) {
          passedTest = false;
        } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_AND_SKIP)) {
          passedTest = false;
        } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ERROR) || assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_REJECT)) {
          passedTest = true;
        } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_OR_REJECT)
            || assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_OR_ERROR)) {
          passedTest = true;
        } else if (assertResult.toUpperCase().startsWith(ASSERT_RESULT_ERROR_LOCATION_IS_.toUpperCase())) {
          passedTest = false;
        }
      } else {
        ackMessageReader = AckAnalyzer.getMessageReader(ackMessageText, connector.getAckType());
        if (ackMessageReader != null) {
          testCaseMessage.setActualMessageResponseType(ackMessageReader.getValue(9));
        }
        {
          if (ackMessageReader != null || !connector.getAckType().isInHL7Format()) {
            AckAnalyzer ackAnalyzer = new AckAnalyzer(ackMessageText, connector.getAckType());
            testCaseMessage.setAccepted(ackAnalyzer.isPositive());

            if (ackMessageReader != null && ackMessageReader.advanceToSegment("MSA")) {
              testCaseMessage.setActualResultAckType(ackMessageReader.getValue(1));
              testCaseMessage.setActualResultAckMessage(ackMessageReader.getValue(2));
              boolean accepted = false;
              boolean rejected = false;
              boolean severitySet = false;
              errorType = ErrorType.UNKNOWN;

              if (testCaseMessage.getActualResultAckType().equals("AA")) {
                accepted = true;
              } else if (testCaseMessage.getActualResultAckType().equals("AR")) {
                rejected = true;
              }

              List<Transform> refList = null;
              boolean foundRef = false;
              if (assertResult.toUpperCase().startsWith(ASSERT_RESULT_ERROR_LOCATION_IS_.toUpperCase())) {
                String refString = assertResult.substring(ASSERT_RESULT_ERROR_LOCATION_IS_.length()).trim();
                refList = new ArrayList<Transform>();
                String[] refs = refString.split("\\Qor\\E");
                for (String r : refs) {
                  Transform ref = null;
                  ref = Transformer.readHL7Reference(r.trim());
                  if (ref.getSegment().equals("") || ref.getField() <= 0) {
                    ref = null;
                  }
                  if (ref != null) {
                    refList.add(ref);
                  }
                }
              }

              while (ackMessageReader.advanceToSegment("ERR")) {
                String severity = ackMessageReader.getValue(4);
                String userMessage = ackMessageReader.getValue(8);
                TestError error = new TestError();
                errorList.add(error);

                if (!severity.equals("")) {
                  severitySet = true;
                }

                if (refList != null) {
                  String segmentName = ackMessageReader.getValue(2, 1);
                  String fieldPos = ackMessageReader.getValue(2, 3);
                  for (Transform ref : refList) {
                    if (ref.getSegment().equalsIgnoreCase(segmentName) && fieldPos.equals("" + ref.getField())) {
                      foundRef = true;
                      break;
                    }
                  }
                }

                if (severity.equals("E")) {
                  rejected = true;
                  error.setErrorType(ErrorType.ERROR);
                } else if (severity.equals("W")) {
                  error.setErrorType(ErrorType.WARNING);
                } else if (severity.equals("I")) {
                  error.setErrorType(ErrorType.INFORMATION);
                } else {
                  error.setErrorType(ErrorType.UNKNOWN);
                }
                error.setDescription(userMessage);

              }
              if (testCaseMessage.getActualResultAckType().equals("AE") && !rejected) {
                if (severitySet) {
                  accepted = true;
                  rejected = false;
                } else {
                  accepted = false;
                  rejected = true;
                }
              }

              passedTest = false;
              if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT)) {
                passedTest = ackAnalyzer.isPositive();
              } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_AND_WARN)) {
                passedTest = !rejected;
              } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_AND_SKIP)) {
                passedTest = !rejected;
              } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ERROR) || assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_REJECT)) {
                passedTest = !ackAnalyzer.isPositive();
              } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_OR_REJECT)
                  || assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_OR_ERROR)) {
                passedTest = true;
              } else if (assertResult.toUpperCase().startsWith(ASSERT_RESULT_ERROR_LOCATION_IS_.toUpperCase())) {
                if (refList == null) {
                  passedTest = false;
                } else {
                  passedTest = foundRef;
                }
              }
              if (errorType == ErrorType.UNKNOWN) {
                if (accepted) {
                  errorType = ErrorType.ACCEPT;
                } else if (rejected) {
                  errorType = ErrorType.ERROR;
                }
              }
            }
          }
        }

        if (passedTest) {
          status = "A";
          testCaseMessage.setActualResultStatus(ACTUAL_RESULT_STATUS_PASS);
          for (TestError error : errorList) {
            if (status.equals("A") && error.getErrorType() == ErrorType.WARNING) {
              // ignore skip warnings
              if (error.getErrorType() != ErrorType.INFORMATION) {
                status = "W";
              }
            }
          }
        } else {
          testCaseMessage.setActualResultStatus(ACTUAL_RESULT_STATUS_FAIL);
          status = "E";
        }
      }
    }
    testCaseMessage.setActualResponseMessage(ackMessageText);
    testCaseMessage.setPassedTest(passedTest);
    testCaseMessage.setHasRun(true);
    wasRun = true;
  }

  private void doRunTest(Connector connector, TestCaseMessage testCaseMessage, String message) throws Exception {
    if (connector instanceof RunAgainstConnector) {
      RunAgainstConnector rac = (RunAgainstConnector) connector;
      rac.setTestCaseCategory(testCaseMessage.getTestCaseCategoryId());
      rac.setTestSectionType(testSectionType);
    }

    startTime = System.currentTimeMillis();
    ackMessageText = connector.submitMessage(message, false);
    endTime = System.currentTimeMillis();

    if (connector instanceof RunAgainstConnector) {
      RunAgainstConnector rac = (RunAgainstConnector) connector;
      if (rac.getOriginalRequestMessage() != null) {
        testCaseMessage.setMessageTextSent(rac.getOriginalRequestMessage());
        testCaseMessage.setMessageText(rac.getOriginalRequestMessage());
      }
    }
  }

  private void setupForRunTest(TestCaseMessage testCaseMessage, String message) {
    wasRun = false;
    passedTest = false;
    ackMessageText = null;
    testCaseMessage.setMessageTextSent(message);
  }

  public static void validateResponseWithNIST(TestCaseMessage testCaseMessage, String messageText) {
    ascertainValidationResource(testCaseMessage, messageText);
    if (testCaseMessage.getValidationResource() != null) {
      ValidationReport validationReport = NISTValidator.validate(messageText, testCaseMessage.getValidationResource());
      testCaseMessage.setValidationReport(validationReport);
      if (validationReport != null) {
        testCaseMessage.setValidationReportPass(
            validationReport.getHeaderReport().getValidationStatus().equals("Complete") && validationReport.getHeaderReport().getErrorCount() == 0);
      }
    }
  }

  public static void ascertainValidationResource(TestCaseMessage testCaseMessage, String messageText) {
    ValidationResource validationResource = null;
    HL7Reader hl7Reader = new HL7Reader(messageText);
    if (hl7Reader.advanceToSegment("MSH")) {
      String messageType = hl7Reader.getValue(9);
      String profileId = hl7Reader.getValue(21);
      if (profileId.equals("Z31") && messageType.equals("RSP")) {
        validationResource = ValidationResource.IZ_RSP_Z31;
      } else if (profileId.equals("Z32") && messageType.equals("RSP")) {
        validationResource = ValidationResource.IZ_RSP_Z32;
      } else if (profileId.equals("Z42") && messageType.equals("RSP")) {
        validationResource = ValidationResource.IZ_RSP_Z42;
      } else if (profileId.equals("Z33") && messageType.equals("RSP")) {
        validationResource = ValidationResource.IZ_RSP_Z33;
      } else if (profileId.equals("Z34") && messageType.equals("QBP")) {
        validationResource = ValidationResource.IZ_QBP_Z34;
      } else if (profileId.equals("Z44") && messageType.equals("QBP")) {
        validationResource = ValidationResource.IZ_QBP_Z44;
      } else if (profileId.equals("Z22") && messageType.equals("VXU")) {
        validationResource = ValidationResource.IZ_VXU_Z22;
      } else if (profileId.equals("") && messageType.equals("VXU")) {
        validationResource = ValidationResource.IZ_VXU;
      } else if (profileId.equals("Z23") || messageType.equals("ACK")) {
        validationResource = ValidationResource.IZ_ACK_FOR_AIRA;
        // validationResource = ValidationResource.IZ_ACK_Z23;
      }
    }
    testCaseMessage.setValidationResource(validationResource);
  }

}
