/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.run;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.mover.AckAnalyzer;
import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.TestError;
import org.immunizationsoftware.dqa.transform.Transform;
import org.immunizationsoftware.dqa.transform.Transformer;

/**
 * 
 * @author nathan
 */
public class TestRunner {

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
    wasRun = false;
    passedTest = false;
    ackMessageText = null;
    testCaseMessage.setMessageTextSent(message);

    startTime = System.currentTimeMillis();
    ackMessageText = connector.submitMessage(message, false);
    endTime = System.currentTimeMillis();

    errorList = new ArrayList<TestError>();
    if (!testCaseMessage.getAssertResult().equalsIgnoreCase("")) {
      String assertResultText = testCaseMessage.getAssertResultText().toUpperCase();
      if (assertResultText.equals("")) {
        assertResultText = "*";
      }
      if (ackMessageText == null || ackMessageText.equals("")) {
        if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept")) {
          passedTest = false;
        } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept and Warn")) {
          passedTest = false;
        } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept and Skip")) {
          passedTest = false;
        } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Error")
            || testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Reject")) {
          passedTest = true;
        } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept or Reject")
            || testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept or Error")) {
          passedTest = true;
        }
      } else {
        ackMessageReader = new HL7Reader(ackMessageText);
        if (ackMessageReader.advanceToSegment("MSH")) {
          testCaseMessage.setActualMessageResponseType(ackMessageReader.getValue(9));
          if (testCaseMessage.getActualMessageResponseType().equals("ACK")) {
            AckAnalyzer ackAnalyzer = new AckAnalyzer(ackMessageText, connector.getAckType());
            testCaseMessage.setAccepted(ackAnalyzer.isPositive());

            if (ackMessageReader.advanceToSegment("MSA")) {
              testCaseMessage.setActualResultAckType(ackMessageReader.getValue(1));
              testCaseMessage.setActualResultAckMessage(ackMessageReader.getValue(2));
              boolean accepted = false;
              boolean rejected = false;
              boolean issueFound = false;
              boolean severitySet = false;
              errorType = ErrorType.UNKNOWN;

              if (testCaseMessage.getActualResultAckType().equals("AA")) {
                accepted = true;
              } else if (testCaseMessage.getActualResultAckType().equals("AR")) {
                rejected = true;
              }

              if (assertResultText.equals("*")) {
                issueFound = true;
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

                if (!assertResultText.equals("*") && !testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept")) {
                  if (userMessage.toUpperCase().startsWith(assertResultText)) {
                    errorType = error.getErrorType();
                    if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept and Warn")) {
                      if (severity.equals("W")) {
                        issueFound = true;
                      }
                    } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept and Skip")) {
                      if (severity.equals("I")) {
                        issueFound = true;
                      }
                    } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Error")) {
                      if (severity.equals("E")) {
                        issueFound = true;
                      }
                    } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept or Reject")) {
                      issueFound = true;
                    }
                  }
                }

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
              if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept")) {
                passedTest = issueFound && ackAnalyzer.isPositive();
              } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept and Warn")) {
                passedTest = issueFound && !rejected;
              } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept and Skip")) {
                passedTest = issueFound && !rejected;
              } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Error")
                  || testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Reject")) {
                passedTest = issueFound && !ackAnalyzer.isPositive();
              } else if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept or Reject")
                  || testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept or Error")) {
                passedTest = issueFound;
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

    return passedTest;
  }

}
