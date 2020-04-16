package org.immregistries.smm.tester.run;

import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.transform.TestCaseMessage;

public class FindMatchingSegment {

  private static final String _OR_ = " -OR- ";
  private static final String _AND_ = " -AND- ";
  private HL7Reader segmentReturnedReader;
  private TestCaseMessage testCaseMessage;
  private FindMatchingSegment nextFindMatchingSegment = null;
  private boolean nextIsAnd = false;

  public FindMatchingSegment(TestCaseMessage testCaseMessage) {
    this.testCaseMessage = testCaseMessage;
    String assertResultParameter = testCaseMessage.getAssertResultParameter();
    createSegmentReturnedReader(assertResultParameter);
  }

  private FindMatchingSegment(TestCaseMessage testCaseMessage, String assertResultParameter) {
    this.testCaseMessage = testCaseMessage;
    createSegmentReturnedReader(assertResultParameter);
  }

  private void createSegmentReturnedReader(String assertResultParameter) {
    String arpUpper = assertResultParameter.toUpperCase();
    int posAnd = arpUpper.indexOf(_AND_);
    int posOr = arpUpper.indexOf(_OR_);
    if (posAnd > 0 && (posOr < 0 || posOr > posAnd)) {
      // next item is an and
      nextIsAnd = true;
      nextFindMatchingSegment = new FindMatchingSegment(testCaseMessage,
          assertResultParameter.substring(posAnd + _AND_.length()).trim());
      assertResultParameter = assertResultParameter.substring(0, posAnd).trim();
    } else if (posOr > 0) {
      nextIsAnd = false;
      nextFindMatchingSegment = new FindMatchingSegment(testCaseMessage,
          assertResultParameter.substring(posOr + _OR_.length()).trim());
      assertResultParameter = assertResultParameter.substring(0, posOr).trim();
    }
    segmentReturnedReader = new HL7Reader(assertResultParameter);
    if (!segmentReturnedReader.advance()) {
      segmentReturnedReader = null;
    } else {
      testCaseMessage
          .log("Will be looking for a segment that matches this one: " + assertResultParameter);
    }
  }

  public boolean checkForMatch() {
    boolean foundMatch = false;
    HL7Reader hl7Reader = new HL7Reader(testCaseMessage.getActualResponseMessage());
    String segmentName = segmentReturnedReader.getSegmentName();
    int fieldCount = segmentReturnedReader.getFieldCount();
    testCaseMessage.log("Will search these segments to find a match: " + segmentName);
    while (hl7Reader.advanceToSegment(segmentName)) {
      boolean matches = true;
      testCaseMessage.log("  + Found segment at position: " + hl7Reader.getSegmentPosition());
      for (int fieldNum = 1; fieldNum <= fieldCount; fieldNum++) {
        int componentCount = segmentReturnedReader.getComponentCount(fieldNum);


        boolean foundMatchingRepeat = false;
        int repeatCount = hl7Reader.getRepeatCount(fieldNum);
        for (int repeatNum = 1; repeatNum <= repeatCount; repeatNum++) {
          boolean foundMatchInRepeatComponents = true;
          for (int componentNum = 1; componentNum <= componentCount; componentNum++) {
            String valueCheck = segmentReturnedReader.getValue(fieldNum, componentNum);
            if (!valueCheck.equals("")) {
              if (valueCheck.equalsIgnoreCase("<EMPTY>")) {
                valueCheck = "";
              }
              String valueActual = hl7Reader.getValueRepeat(fieldNum, componentNum, repeatNum);
              if (!valueActual.equalsIgnoreCase(valueCheck)) {
                foundMatchInRepeatComponents = false;
                testCaseMessage.log("    Not a match, value in " + segmentName + "-" + fieldNum
                    + (repeatNum == 1 ? "" : ("[" + repeatNum + "]")) + "." + componentNum + " '"
                    + valueActual + "' <> '" + valueCheck + "'");
                break;
              }
            }
          }
          if (foundMatchInRepeatComponents) {
            foundMatchingRepeat = true;
            break;
          }
        }
        if (!foundMatchingRepeat) {
          matches = false;
          break;
        }
      }
      if (matches) {
        foundMatch = true;
        testCaseMessage.log("    Match found");
        break;
      }
    }
    if (nextFindMatchingSegment != null) {
      if (nextIsAnd) {
        if (foundMatch) {
          return nextFindMatchingSegment.checkForMatch();
        } else {
          return false;
        }
      } else {
        if (foundMatch) {
          return true;
        } else {
          return nextFindMatchingSegment.checkForMatch();
        }
      }
    }
    return foundMatch;
  }

}
