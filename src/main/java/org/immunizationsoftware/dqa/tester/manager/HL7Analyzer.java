package org.immunizationsoftware.dqa.tester.manager;

import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.tester.TestCaseMessage;

public class HL7Analyzer
{
  private List<String> il;
  private HL7Reader reader;
  private HL7Reader originalRequestReader;

  public List<String> getIssueList() {
    return il;
  }

  private boolean passedTest = false;

  public boolean isPassedTest() {
    return passedTest;
  }

  public HL7Analyzer(TestCaseMessage testCaseMessage) {
    il = new ArrayList<String>();
    reader = new HL7Reader(testCaseMessage.getActualResponseMessage());
    originalRequestReader = new HL7Reader(testCaseMessage.getMessageText());
  }

  public HL7Analyzer(List<String> issueList, HL7Reader reader, HL7Reader originalRequestReader) {
    this.il = issueList;
    this.reader = reader;
    this.originalRequestReader = originalRequestReader;
  }

  public boolean analyzeRsp() {
    passedTest = true;
    analyzeMSH();

    if (!passedTest) {
      return passedTest;
    }

    analyzeMSA();

    passedTest = true;
    return passedTest;
  }

  public boolean analyzeAck() {
    passedTest = true;
    analyzeMSH();

    if (!passedTest) {
      return passedTest;
    }

    analyzeMSA();

    passedTest = il.size() == 0;
    return passedTest;

  }

  private void analyzeMSA() {
    if (!reader.advanceToSegment("MSA")) {
      il.add("MSA segment is required but was not found");
    } else {

      originalRequestReader.advanceToSegment("MSH");

      validateValue(1, "Acknowledgment Code", true, new String[] { "AA", "AE", "AR" });
      validateCardinality(1, "Acknowledgment Code", 1, 1);
      if (validateRequired(2, "Message Control Id")) {
        String controlId = originalRequestReader.getValue(10);
        if (!controlId.equals(reader.getValue(2))) {
          il.add("MSA-2 must echo the value submitted in MSH-10: " + controlId);
        }
      }
      validateCardinality(1, "Message Control Id", 1, 1);
      validateEmpty(3, "Text Message");
      validateNM(4, "Expected Sequence Number", false);
      validateEmpty(6, "Error Condition");
    }
  }

  private void analyzeMSH() {
    if (!reader.advanceToSegment("MSH")) {
      il.add("ACK does not start with MSH segment");
      passedTest = false;
    } else if (!reader.getFieldSeparator().equals("|")) {
      il.add("Violation of IZ-12: The MSH-1 (Field Separator) field SHALL be valued \"|\"");
      passedTest = false;
    } else if (validateRequired(2, "Encoding Characters")) {
      if (!reader.getValue(2).equals("^~\\&")) {
        il.add("Violation of IZ-13: The MSH-2 (Encoding Characters) field SHALL be valued \"^~\\&\"");
        passedTest = false;
      }
    }

    if (passedTest) {
      validateTS(7, "Date/Time of Message", true, 12);
      if (reader.getValue(7).length() < 12) {
        il.add("Violation of IZ-14: MSH-7 (Date/time of Message) SHALL have a degree of precision that must be at least to the minute. (Format YYYYMMDDHHMM).");
      }
    }
  }

  private boolean validateCardinality(int pos, String label, int minCard, int maxCard) {
    int card = reader.getRepeatCount(pos);
    if (card < minCard || card > maxCard) {
      il.add(ml(pos, label) + "Has a cardinality from " + minCard + " to " + maxCard);
      return false;
    }
    return true;
  }

  private boolean validateNM(int pos, String label, boolean required) {
    String value = reader.getValue(pos);
    if (required && value.equals("")) {
      il.add(ml(pos, label) + "Required field missing");
      return false;
    }
    if (value.equals("")) {
      return true;
    }
    try {
      Double.parseDouble(value);
    } catch (NumberFormatException nfe) {
      il.add(ml(pos, label) + "Field does not follow NM format");
    }
    return true;
  }

  private boolean validateRequired(int pos, String label) {
    String value = reader.getValue(pos);
    if (value.equals("")) {
      il.add(ml(pos, label) + "Required field missing");
    }
    return true;
  }

  private boolean validateEmpty(int pos, String label) {
    String value = reader.getValue(pos);
    if (!value.equals("")) {
      il.add(ml(pos, label) + "Required is no longer supported and must be empty");
    }
    return true;
  }

  private boolean validateValue(int pos, String label, boolean required, String[] allowedValues) {
    String value = reader.getValue(pos);
    if (required && value.equals("")) {
      il.add(ml(pos, label) + "Required field missing");
      return false;
    } else if (value.equals("")) {
      return true;
    }
    for (String s : allowedValues) {
      if (s.equals(value)) {
        return true;
      }
    }
    il.add(ml(pos, label) + "Value is not valid: " + value);
    return true;
  }

  private boolean validateTS(int pos, String label, boolean required, int minLength) {
    String time = reader.getValue(pos);
    if (required && time.equals("")) {
      il.add(ml(pos, label) + "Required field missing");
    } else {
      String degreeOfPrecision = reader.getValue(pos, 2);
      if (!degreeOfPrecision.equals("")) {
        il.add(ml(pos, label) + "Degree of precision no longer supported, do not send");
      }
      try {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int min = 0;
        int sec = 0;
        int ms = 0;
        int timezone = 0;
        int i = time.indexOf("-");
        if (i == -1) {
          i = time.indexOf("+");
        }
        if (i != -1) {
          timezone = Integer.parseInt(time.substring(i + 1));
          time = time.substring(0, i);
        }
        if (timezone > 2400 || timezone < -2400) {
          il.add(ml(pos, label) + "Time zone is not within the range of 2400 to -2400");
        }
        if (time.length() < 4) {
          il.add(ml(pos, label) + "Year is required by DTM definition");
        } else {
          year = Integer.parseInt(time.substring(0, 4));
        }
        if (time.length() >= 6) {
          month = Integer.parseInt(time.substring(4, 6));
          if (time.length() >= 8) {
            day = Integer.parseInt(time.substring(6, 8));
            if (time.length() >= 10) {
              hour = Integer.parseInt(time.substring(8, 10));
              if (time.length() >= 12) {
                min = Integer.parseInt(time.substring(10, 12));
                if (time.length() > 14) {
                  if (time.charAt(14) != '.') {
                    il.add(ml(pos, label)
                        + "Invalid format for DTM data type, value in position 15 must be one of these . + - ");
                  } else {
                    sec = Integer.parseInt(time.substring(15));
                    if (time.length() > 15) {
                      ms = Integer.parseInt(time.substring(15));
                      int digits = time.length() - 15;
                      if (digits > 4) {
                        il.add(ml(pos, label) + "Invalid format for DTM data type, ms can only be 4 digits long ");
                      } else {
                        ms = ms * 10 ^ (4 - digits);
                      }
                    }
                  }
                }
              }
            }
          }
        }
        if (time.length() < minLength) {
          il.add(ml(pos, label) + "Time must include at least " + minLength + " digits");
        }
        if (month > 12) {
          il.add(ml(pos, label) + "Month is not valid US month");
        }
        if (day > 31) {
          il.add(ml(pos, label) + "Day is not a valid US day");
        }
        if (hour > 23) {
          il.add(ml(pos, label) + "Hour is not valid, must be between 0 and 24");
        }
        if (min > 59) {
          il.add(ml(pos, label) + "Minute is not valid, must be between 0 and 59");
        }
        if (sec > 59) {
          il.add(ml(pos, label) + "Second is not valid, must be between 0 and 59");
        }
        if (sec > 59) {
          il.add(ml(pos, label) + "Second is not valid, must be between 0 and 59");
        }
        if (ms > 9999) {
          il.add(ml(pos, label) + "Millisecond is not valid, must be between 0 and 9999");
        }

      } catch (NumberFormatException nfe) {
        il.add(ml(pos, label)
            + "Format of date/time does not conform to DTM definition, cannot be converted into numeric values");
      }
    }
    return true;
  }

  private String ml(int position, String name) {
    return reader.getValue(0) + "-" + position + " " + name + ": ";
  }

}
