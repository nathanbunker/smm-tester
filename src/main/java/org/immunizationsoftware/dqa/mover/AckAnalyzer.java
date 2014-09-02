package org.immunizationsoftware.dqa.mover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class AckAnalyzer
{

  public static enum ErrorType {
    UNKNOWN, AUTHENTICATION, SENDER_PROBLEM, RECEIVER_PROBLEM
  };

  public static enum AckType {
    DEFAULT, NMSIIS, ALERT, WEBIZ
  };

  private ErrorType errorType = null;
  private boolean ackMessage = false;
  private boolean positive = false;
  private boolean temporarilyUnavailable = false;
  private boolean setupProblem = false;
  private String setupProblemDescription = "";
  private AckType ackType = null;
  private List<String> errorMessages = new ArrayList<String>();
  private List<String> segments;
  private FileOut errorFileOut = null;

  private void log(String s) {
    if (errorFileOut != null) {
      try {
        errorFileOut.printCommentLn(s);
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  public String getSetupProblemDescription() {
    return setupProblemDescription;
  }

  public void setSetupProblemDescription(String setupProblemDescription) {
    this.setupProblemDescription = setupProblemDescription;
  }

  public boolean isTemporarilyUnavailable() {
    return temporarilyUnavailable;
  }

  public void setTemporarilyUnavailable(boolean temporarilyUnavailable) {
    this.temporarilyUnavailable = temporarilyUnavailable;
  }

  public boolean hasSetupProblem() {
    return setupProblem;
  }

  public void setSetupProblem(boolean setupProblem) {
    this.setupProblem = setupProblem;
  }

  public ErrorType getErrorType() {
    return errorType;
  }

  public void setErrorType(ErrorType errorType) {
    this.errorType = errorType;
  }

  private String ackCode = "";

  public boolean isAckMessage() {
    return ackMessage;
  }

  public boolean isPositive() {
    return positive;
  }

  public void setPositive(boolean positive) {
    this.positive = positive;
  }

  public String getAckCode() {
    return ackCode;
  }

  public void setAckCode(String ackCode) {
    this.ackCode = ackCode;
  }

  public AckAnalyzer(String ack) {
    this(ack, AckType.DEFAULT, null);
  }

  public AckAnalyzer(String ack, AckType ackType) {
    this(ack, ackType, null);
  }

  public AckAnalyzer(String ack, AckType ackType, FileOut errorFileOut) {
    while (ack != null && ack.length() > 0 && ack.charAt(0) <= ' ') {
      ack = ack.substring(1);
    }
    this.ackType = ackType;
    this.errorFileOut = errorFileOut;

    convertToSegments(ack);

    boolean isNotAck = false;
    if (ack == null) {
      isNotAck = true;
      log("Returned result is not an acknowledgement message: no acknowledgement message returned");
    } else if (!ack.startsWith("MSH|")) {
      isNotAck = true;
      log("Returned result is not an acknowledgement message: first line does not start with MSH|");
    } else if (!getFieldValue("MSH", 9).equals("ACK")) {
      isNotAck = true;
      log("Returned result is not an acknowledgement message: MSH-9 is not ACK, it is '" + getFieldValue("MSH", 9)
          + "'");
    }
    if (isNotAck) {
      ackMessage = false;
      positive = false;
      setupProblem = true;
      if (ack != null && ackType == AckType.NMSIIS) {
        if (ack.length() < 240) {
          setupProblemDescription = ack;
        } else {
          setupProblemDescription = "Unexpected non-HL7 response, please verify connection URL";
        }
      } else {
        setupProblemDescription = "Unexpected non-HL7 response, please verify connection URL";
      }
    } else {
      ackMessage = true;
      ackCode = getFieldValue("MSA", 1);

      if (ackType.equals(AckType.NMSIIS)) {
        setupProblem = ack.indexOf("|BAD MESSAGE|") != -1 || ack.indexOf("File Rejected.") != -1;
        if (setupProblem) {
          log("Setup problem found, message contains phrase |BAD MESSAGE| or File Rejected.");
        }
        boolean recordNotRejected = ack.indexOf("Record Rejected") == -1 && ack.indexOf("Message Rejected") == -1;
        if (!recordNotRejected) {
          log("Record was rejected, message contains phrase Record Rejected");
        }
        positive = !setupProblem && recordNotRejected;
      } else if (ackType.equals(AckType.ALERT)) {
        positive = true;
        int pos = 1;
        String[] values = null;
        while ((values = getFieldValues("MSA", pos, 1)) != null) {
          if (values.length > 0) {
            if (values[0].equals("AE")) {
              positive = false;
              break;
            }
          }
          pos++;
        }
        if (!positive) {
          log("At least one MSA-1 field was found with a value of AE so message was rejected");
        }
      } else if (ackType.equals(AckType.WEBIZ)) {
        if (ackCode.equals("AA")) {
          positive = true;
        } else if (ackCode.equals("AE")) {
          positive = true;
          if (ack.indexOf("Not processing order group") != -1) {
            positive = false;
          } else if (ack.indexOf("Application internal error") != -1) {
            positive = false;
          }
        } else {
          positive = false;
        }
      } else {
        if (ackCode.equals("AA")) {
          positive = true;
        } else if (ackCode.equals("AR")) {
          positive = false;
        } else {
          positive = true;
          boolean noSeverity = true;
          int pos = 1;
          String[] values = null;
          while ((values = getFieldValues("ERR", pos, 4)) != null) {
            if (values.length > 0) {
              if (!values[0].equals("")) {
                noSeverity = false;
              }
              if (values[0].equals("E")) {
                positive = false;
                break;
              }
            }
            pos++;
          }
          if (noSeverity) {
            positive = false;
          }
        }

      }
    }
  }

  private void convertToSegments(String ack) {
    segments = new ArrayList<String>();
    if (ack != null) {
      BufferedReader in = new BufferedReader(new StringReader(ack));
      String line;
      try {
        while ((line = in.readLine()) != null) {
          segments.add(line);
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  private boolean hasSegment(String segmentName) {
    for (String segment : segments) {
      if (segment.startsWith(segmentName + "|")) {
        return true;
      }
    }
    return false;
  }

  private String getFieldValue(String segmentName, int pos) {
    String[] values = getFieldValues(segmentName, 1, pos);
    if (values != null && values.length > 0 && values[0] != null) {
      return values[0];
    }
    return "";
  }

  private String[] getFieldValues(String segmentName, int segmentCount, int pos) {
    if (!segmentName.equals("MSH")) {
      pos++;
    }
    for (String segment : segments) {
      if (segment.startsWith(segmentName + "|")) {
        segmentCount--;
        if (segmentCount == 0) {
          int startPos = -1;
          int endPos = -1;
          while (pos > 0) {
            pos--;
            if (endPos < segment.length()) {
              startPos = endPos + 1;
              endPos = segment.indexOf("|", startPos);
              if (endPos == -1) {
                endPos = segment.length();
              }
            }
          }
          String value = segment.substring(startPos, endPos);
          int repeatPos = value.indexOf("~");
          if (repeatPos != -1) {
            value = value.substring(0, repeatPos);
          }
          return value.split("\\^");
        }
      }
    }

    return null;
  }

}
