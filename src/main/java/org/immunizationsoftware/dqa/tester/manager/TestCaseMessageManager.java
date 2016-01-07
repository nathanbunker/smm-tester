package org.immunizationsoftware.dqa.tester.manager;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.ACK;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.RSP;
import org.immunizationsoftware.dqa.transform.PatientType;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;
import org.immunizationsoftware.dqa.transform.Transformer;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class TestCaseMessageManager {

  public static List<TestCaseMessage> createTestCaseMessageList(String source) throws Exception {
    List<TestCaseMessage> testCaseMessageList = new ArrayList<TestCaseMessage>();

    try {
      BufferedReader in = new BufferedReader(new StringReader(source));
      String line = null;
      StringBuffer message = new StringBuffer();
      TestCaseMessage testCaseMessage = new TestCaseMessage();
      int number = 0;
      boolean readingHL7 = false;
      String lastList = "";
      while ((line = in.readLine()) != null) {
        line = line.trim();
        if (line.length() > 0) {
          if (line.startsWith("TC:") || line.startsWith(TestCaseMessage.TEST_CASE_NUMBER)) {
            number = createTestCase(testCaseMessage, message, number, testCaseMessageList);
            testCaseMessage = new TestCaseMessage();
            readingHL7 = false;
            lastList = "";
          } else if (line.startsWith("MSH|")) {
            if (readingHL7) {
              // found test case without a proper header
              number = createTestCase(testCaseMessage, message, number, testCaseMessageList);
              testCaseMessage = new TestCaseMessage();
              readingHL7 = false;
              lastList = "";
            }
          }
          if (line.startsWith("TC:") || line.startsWith(TestCaseMessage.TEST_CASE_NUMBER)) {
            testCaseMessage.setTestCaseNumber(readValue(line));
          } else if (line.startsWith(TestCaseMessage.COMMENT)) {
            String[] parts = split(line);
            if (parts.length == 2 && parts[0] != null && parts[1] != null) {
              testCaseMessage.setComment(parts[0].trim(), parts[1].trim());
            }
          } else if (line.startsWith(TestCaseMessage.DESCRIPTION)) {
            testCaseMessage.setDescription(readValue(line));
          } else if (line.startsWith(TestCaseMessage.PATIENT_TYPE)) {
            testCaseMessage.setPatientType(PatientType.valueOf(readValue(line)));
          } else if (line.startsWith(TestCaseMessage.SCENARIO)) {
            testCaseMessage.setScenario(readValue(line));
          } else if (line.startsWith(TestCaseMessage.TEST_CASE_SET)) {
            testCaseMessage.setTestCaseSet(readValue(line));
          } else if (line.startsWith(TestCaseMessage.EXPECTED_RESULT)) {
            testCaseMessage.setExpectedResult(readValue(line));
          } else if (line.startsWith(TestCaseMessage.ORIGINAL_MESSAGE)) {
            testCaseMessage.setOriginalMessage(readValue(line).replaceAll("\\Q<CR>\\E", "\r"));
          } else if (line.startsWith(TestCaseMessage.ACTUAL_RESPONSE_MESSAGE)) {
            testCaseMessage.setActualResponseMessage(readValue(line).replaceAll("\\Q<CR>\\E", "\r"));
          } else if (line.startsWith(TestCaseMessage.DERIVED_FROM_VXU_MESSAGE)) {
            testCaseMessage.setDerivedFromVXUMessage(readValue(line).replaceAll("\\Q<CR>\\E", "\r"));
          } else if (line.startsWith(TestCaseMessage.QUICK_TRANSFORMATIONS)) {
            testCaseMessage.setQuickTransformations(readValues(line));
          } else if (line.startsWith(TestCaseMessage.ASSERT_RESULT)) {
            testCaseMessage.setAssertResult(readValue(line));
          } else if (line.startsWith(TestCaseMessage.CUSTOM_TRANSFORMATIONS)) {
            testCaseMessage.setCustomTransformations(readValue(line).replaceAll("\\Q<CR>\\E", "\r"));
            lastList = "CT";
          } else if (line.startsWith(TestCaseMessage.EXCLUDE_TRANSFORMATIONS)) {
            testCaseMessage.setExcludeTransformations(readValue(line).replaceAll("\\Q<CR>\\E", "\r"));
            lastList = "ET";
          } else if (line.startsWith(TestCaseMessage.CAUSE_ISSUES)) {
            testCaseMessage.setCauseIssues(readValue(line).replaceAll("\\Q<CR>\\E", "\r"));
            lastList = "CI";
          } else if (line.startsWith(TestCaseMessage.ADDITIONAL_TRANSFORMATIONS)) {
            testCaseMessage.setAdditionalTransformations(readValue(line).replaceAll("\\Q<CR>\\E", "\r"));
            lastList = "AT";
          } else if (line.startsWith("+") && line.length() > 1) {
            if (lastList.equals("CT")) {
              testCaseMessage.appendCustomTransformation(line.substring(1).trim());
            } else if (lastList.equals("ET")) {
              testCaseMessage.appendExcludeTransformation(line.substring(1).trim());
            } else if (lastList.equals("CI")) {
              testCaseMessage.appendCauseIssue(line.substring(1).trim());
            } else if (lastList.equals("AT")) {
              testCaseMessage.appendAdditionalTransformation(line.substring(1).trim());
            }
          } else if (line.startsWith("--") || line.startsWith("//")) {
            // ignore, this line is a comment
          } else if (line.startsWith("MSH|")) {
            // Looks like part of an HL7 message
            message.append(line);
            message.append("\r");
            readingHL7 = true;
          } else if (readingHL7) {
            if (line.length() > 3 && line.charAt(3) == '|') {
              message.append(line);
              message.append("\r");
            }
          } else {
            lastList = "";
          }
        }
      }
      if (message.length() > 0) {
        number = createTestCase(testCaseMessage, message, number, testCaseMessageList);
      }
    } catch (Exception e) {
      throw new Exception("Unable to intantiate test case messages", e);
    }
    return testCaseMessageList;
  }

  protected static int createTestCase(TestCaseMessage testCaseMessage, StringBuffer message, int number, List<TestCaseMessage> testCaseMessageList) {
    String messageText = message.toString();
    if (messageText.length() > 0) {
      testCaseMessage.setMessageText(messageText);
      if (testCaseMessage.getTestCaseNumber().equals("")) {
        // try to find in message
        int pos = messageText.indexOf("|");
        int count = 1;
        while (pos != -1 && count < 9) {
          pos = messageText.indexOf("|", pos + 1);
          count++;
        }
        if (pos != -1) {
          int endPos = messageText.indexOf("|", pos + 1);
          if (endPos != -1) {
            String messageId = messageText.substring(pos + 1, endPos);
            endPos = messageId.indexOf("~");
            if (endPos != -1) {
              messageId = messageId.substring(0, endPos);
            }
            endPos = messageId.indexOf("^");
            if (endPos != -1) {
              messageId = messageId.substring(0, endPos);
            }
            testCaseMessage.setTestCaseNumber(messageId);
          }
        }
      }
      // couldn't find it htere, now looking in PID-3
      if (testCaseMessage.getTestCaseNumber().equals("")) {
        // try to find in message
        int pos = messageText.indexOf("PID|");
        pos = pos + 3;
        int count = 1;
        while (pos != -1 && count < 3) {
          pos = messageText.indexOf("|", pos + 1);
          count++;
        }
        if (pos != -1) {
          int endPos = messageText.indexOf("|", pos + 1);
          if (endPos != -1) {
            String messageId = messageText.substring(pos + 1, endPos);
            endPos = messageId.indexOf("~");
            if (endPos != -1) {
              messageId = messageId.substring(0, endPos);
            }
            endPos = messageId.indexOf("^");
            if (endPos != -1) {
              messageId = messageId.substring(0, endPos);
            }
            testCaseMessage.setTestCaseNumber(messageId);
          }
        }
      }
      number++;
      if (testCaseMessage.getTestCaseNumber().equals("")) {
        testCaseMessage.setTestCaseNumber("{" + number + "}");
      }
      boolean hasRange = false;
      String tcn = testCaseMessage.getTestCaseNumber();
      int openBracket = tcn.indexOf("[");
      if (openBracket != -1) {
        int closeBracket = tcn.indexOf("]", openBracket);
        if (closeBracket != -1) {
          String part1 = tcn.substring(0, openBracket);
          String range = tcn.substring(openBracket + 1, closeBracket);
          closeBracket++;
          String part2 = "";
          if (closeBracket < tcn.length()) {
            part2 = tcn.substring(closeBracket);
          }
          {
            int dotdot = range.indexOf("..");
            if (dotdot != -1) {
              hasRange = true;
              String startNum = range.substring(0, dotdot).trim();
              if (startNum.equals("")) {
                startNum = "1";
              }
              String endNum = range.substring(dotdot + 2).trim();
              if (endNum.equals("")) {
                endNum = "100";
              }
              int count = 1;
              number--;
              while (count < 1000 && !startNum.equals(endNum)) {
                number++;
                TestCaseMessage copy = new TestCaseMessage(testCaseMessage);
                copy.setTestCaseNumber(part1 + startNum + part2);
                addTestMessageToList(testCaseMessageList, copy);
                startNum = addOne(startNum);
                count++;
              }
              TestCaseMessage copy = new TestCaseMessage(testCaseMessage);
              copy.setTestCaseNumber(part1 + startNum + part2);
              addTestMessageToList(testCaseMessageList, copy);
            }
          }
        }
      }
      if (!hasRange) {
        addTestMessageToList(testCaseMessageList, testCaseMessage);
      }
      message.setLength(0);
    }
    return number;
  }

  protected static void addTestMessageToList(List<TestCaseMessage> testCaseMessageList, TestCaseMessage testCaseMessage) {
    if (testCaseMessage.getMessageText().startsWith("MSH|TRANSFORM")) {
      Transformer transformer = new Transformer();
      transformer.transform(testCaseMessage);
    }
    if (testCaseMessage.getOriginalMessage().equals("")) {
      testCaseMessage.setOriginalMessage(testCaseMessage.getMessageText());
    }

    for (int i = 0; i < testCaseMessageList.size(); i++) {
      if (testCaseMessage.getTestCaseNumber().equals(testCaseMessageList.get(i).getTestCaseNumber())) {
        testCaseMessageList.get(i).merge(testCaseMessage);
        testCaseMessage = null;
        break;
      }
    }
    if (testCaseMessage != null) {
      testCaseMessageList.add(testCaseMessage);
    }
  }

  protected static String addOne(String s) {
    String result = "";
    if (s.equals("")) {
      return "1";
    }
    int carry = 1;
    for (int i = s.length() - 1; i >= 0; i--) {
      char c = s.charAt(i);
      if (c < '0' || c > '9') {
        result = s.substring(0, i) + carry + result;
        carry = 0;
        break;
      }
      int num = (c - '0') + carry;
      if (num >= 10) {
        carry = num / 10;
        num = num - (carry * 10);
      } else {
        carry = 0;
      }
      result = num + result;
    }
    if (carry > 0) {
      result = carry + result;
    }
    return result;
  }

  private static String readValue(String s) {
    int pos = s.indexOf(":");
    if (pos == -1) {
      return "";
    }
    pos++;
    if (pos == s.length()) {
      return "";
    }
    return s.substring(pos).trim();
  }

  private static String[] readValues(String s) {
    String[] values = readValue(s).split("\\,");
    if (values == null) {
      values = new String[] {};
    }
    for (int i = 0; i < values.length; i++) {
      values[i] = values[i].trim();
    }
    return values;
  }

  private static String[] split(String s) {
    int pos = s.indexOf(":");
    if (pos == -1 || (++pos == s.length())) {
      return new String[] {};
    }
    s = s.substring(pos).trim();
    pos = s.indexOf("-");
    if (pos == -1 || (++pos == s.length())) {
      return new String[] {};
    }
    return new String[] { s.substring(0, pos - 1), s.substring(pos) };
  }

  public static HL7Component createHL7Component(TestCaseMessage testCaseMessage) {
    try {
      HL7Component comp = null;
      if (testCaseMessage.getActualMessageResponseType().equals("")) {
        if (testCaseMessage.getActualResponseMessage() != null && !testCaseMessage.getActualResponseMessage().equalsIgnoreCase("")) {
          HL7Reader ackMessageReader = new HL7Reader(testCaseMessage.getActualResponseMessage());
          if (ackMessageReader.advanceToSegment("MSH")) {
            testCaseMessage.setActualMessageResponseType(ackMessageReader.getValue(9));
          }
        }
      }
      if (testCaseMessage.getActualMessageResponseType().equals("ACK")) {
        comp = new ACK();
      } else if (testCaseMessage.getActualMessageResponseType().equals("RSP")) {
        comp = new RSP();
      }
      if (comp != null) {
        comp.parseTextFromMessage(testCaseMessage.getActualResponseMessage());
        comp.checkConformance();
        return comp;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
