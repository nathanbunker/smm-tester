/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.run;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.connectors.RunAgainstConnector;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.tester.manager.nist.NISTValidator;
import org.immunizationsoftware.dqa.tester.manager.nist.ValidationReport;
import org.immunizationsoftware.dqa.tester.manager.nist.ValidationResource;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.TestError;
import org.immunizationsoftware.dqa.transform.Transformer;

/**
 * 
 * @author nathan
 */
public class TestRunner
{

  public static final String ACTUAL_RESULT_STATUS_FAIL = "FAIL";
  public static final String ACTUAL_RESULT_STATUS_PASS = "PASS";
  private String ackMessageText = null;
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

  public TestCaseMessage runTestIfNew(Connector connector, TestCaseMessage testCaseMessage,
      Map<String, TestCaseMessage> testCaseMessageMap) throws Exception {
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
    wasRun = false;
    passedTest = false;
    ackMessageText = null;
    testCaseMessage.setMessageTextSent(message);

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

    errorList = new ArrayList<TestError>();
    if (!testCaseMessage.getAssertResult().equalsIgnoreCase("")) {
      if (ackMessageText == null || ackMessageText.equals("")) {
        if (testCaseMessage.getAssertResult().equalsIgnoreCase("Accept")) {
          passedTest = false;
        } else if (testCaseMessage.getAssertResult().equalsIgnoreCase("Accept and Warn")) {
          passedTest = false;
        } else if (testCaseMessage.getAssertResult().equalsIgnoreCase("Accept and Skip")) {
          passedTest = false;
        } else if (testCaseMessage.getAssertResult().equalsIgnoreCase("Error")
            || testCaseMessage.getAssertResult().equalsIgnoreCase("Reject")) {
          passedTest = true;
        } else if (testCaseMessage.getAssertResult().equalsIgnoreCase("Accept or Reject")
            || testCaseMessage.getAssertResult().equalsIgnoreCase("Accept or Error")) {
          passedTest = true;
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

              while (ackMessageReader.advanceToSegment("ERR")) {
                String severity = ackMessageReader.getValue(4);
                String userMessage = ackMessageReader.getValue(8);
                TestError error = new TestError();
                errorList.add(error);

                if (!severity.equals("")) {
                  severitySet = true;
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
              if (testCaseMessage.getAssertResult().equalsIgnoreCase("Accept")) {
                passedTest = ackAnalyzer.isPositive();
              } else if (testCaseMessage.getAssertResult().equalsIgnoreCase("Accept and Warn")) {
                passedTest = !rejected;
              } else if (testCaseMessage.getAssertResult().equalsIgnoreCase("Accept and Skip")) {
                passedTest = !rejected;
              } else if (testCaseMessage.getAssertResult().equalsIgnoreCase("Error")
                  || testCaseMessage.getAssertResult().equalsIgnoreCase("Reject")) {
                passedTest = !ackAnalyzer.isPositive();
              } else if (testCaseMessage.getAssertResult().equalsIgnoreCase("Accept or Reject")
                  || testCaseMessage.getAssertResult().equalsIgnoreCase("Accept or Error")) {
                passedTest = true;
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
    
    if (validateResponse) {
      ascertainValidationResource(testCaseMessage, ackMessageText);
      if (testCaseMessage.getValidationResource() != null) {
        ValidationReport validationReport = NISTValidator.validate(ackMessageText,
            testCaseMessage.getValidationResource());
        testCaseMessage.setValidationReport(validationReport);
        if (validationReport != null) {
          testCaseMessage
              .setValidationReportPass(validationReport.getHeaderReport().getValidationStatus().equals("Complete")
                  && validationReport.getHeaderReport().getErrorCount() == 0);
        }
      }
    }

    return passedTest;
  }

  public static void ascertainValidationResource(TestCaseMessage testCaseMessage, String messageText) {
    ValidationResource validationResource = null;
    HL7Reader hl7Reader = new HL7Reader(messageText);
    if (hl7Reader.advanceToSegment("MSH")) {
      String messageType = hl7Reader.getValue(9);
      String profileId = hl7Reader.getValue(21);
      if (profileId.equals("Z31")) {
        validationResource = ValidationResource.IZ_RSP_Z31;
      } else if (profileId.equals("Z32")) {
        validationResource = ValidationResource.IZ_RSP_Z32;
      } else if (profileId.equals("Z33")) {
        validationResource = ValidationResource.IZ_RSP_Z33;
      } else if (profileId.equals("Z34")) {
        validationResource = ValidationResource.IZ_RSP_Z42;
      } else if (profileId.equals("Z23") || messageType.equals("ACK")) {
        validationResource = ValidationResource.IZ_ACK_FOR_AIRA;
      }
    }
    testCaseMessage.setValidationResource(validationResource);
  }

}
