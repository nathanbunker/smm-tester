package org.immunizationsoftware.dqa.tester.profile;

import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class ProfileLine
{
  private ProfileField field;
  private Usage usage = Usage.O;
  private TestCaseMessage testCaseMessagePresent = null;
  private TestCaseMessage testCaseMessageAbsent = null;
  private boolean passed = false;
  private boolean hasRun = false;
  private MessageAcceptStatus messageAcceptStatus = null;
  private MessageAcceptStatus messageAcceptStatusDetected = null;

  public MessageAcceptStatus getMessageAcceptStatusDetected() {
    return messageAcceptStatusDetected;
  }

  public void setMessageAcceptStatusDetected(MessageAcceptStatus messageAcceptStatusDetected) {
    this.messageAcceptStatusDetected = messageAcceptStatusDetected;
  }

  public MessageAcceptStatus getMessageAcceptStatus() {
    return messageAcceptStatus;
  }

  public void setMessageAcceptStatus(MessageAcceptStatus messageAcceptStatus) {
    this.messageAcceptStatus = messageAcceptStatus;
  }

  public boolean isPassed() {
    return passed;
  }

  public void setPassed(boolean passed) {
    this.passed = passed;
  }

  public boolean isHasRun() {
    return hasRun;
  }

  public void setHasRun(boolean hasRun) {
    this.hasRun = hasRun;
  }

  public TestCaseMessage getTestCaseMessagePresent() {
    return testCaseMessagePresent;
  }

  public void setTestCaseMessagePresent(TestCaseMessage testCaseMessagePresent) {
    this.testCaseMessagePresent = testCaseMessagePresent;
  }

  public TestCaseMessage getTestCaseMessageAbsent() {
    return testCaseMessageAbsent;
  }

  public void setTestCaseMessageAbsent(TestCaseMessage testCaseMessageAbsent) {
    this.testCaseMessageAbsent = testCaseMessageAbsent;
  }

  public ProfileField getField() {
    return field;
  }

  public void setField(ProfileField field) {
    this.field = field;
  }

  public Usage getUsage() {
    return usage;
  }

  public void setUsage(Usage usage) {
    this.usage = usage;
  }

  public void setUsage(String usageString) {
    this.usage = readUsage(usageString);
  }

  

  public static Usage readUsage(String usageString) {
    if (usageString == null) {
      return Usage.O;
    } else if (usageString.equalsIgnoreCase("R")) {
      return Usage.R;
    } else if (usageString.equalsIgnoreCase("RE")) {
      return Usage.RE;
    } else if (usageString.equalsIgnoreCase("O")) {
      return Usage.O;
    } else if (usageString.equalsIgnoreCase("X")) {
      return Usage.X;
    } else if (usageString.equalsIgnoreCase("RE_OR_O")) {
      return Usage.RE_OR_O;
    } else if (usageString.equalsIgnoreCase("R_OR_X")) {
      return Usage.R_OR_X;
    } else if (usageString.equalsIgnoreCase("RE_OR_X")) {
      return Usage.RE_OR_X;
    } else if (usageString.equalsIgnoreCase("R_OR_RE")) {
      return Usage.R_OR_RE;
    } else if (usageString.equalsIgnoreCase("R*")) {
      return Usage.R_NOT_ENFORCED;
    } else if (usageString.equalsIgnoreCase("RE*")) {
      return Usage.RE_NOT_USED;
    } else if (usageString.equalsIgnoreCase("O*")) {
      return Usage.O_NOT_USED;
    } else if (usageString.equalsIgnoreCase("X*")) {
      return Usage.X_NOT_ENFORCED;
    } else {
      return Usage.O;
    }
  }

}
