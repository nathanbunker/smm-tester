package org.immunizationsoftware.dqa.tester.profile;

import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class ProfileLine
{
  private ProfileField field;
  private Usage usage = Usage.NOT_DEFINED;
  private TestCaseMessage testCaseMessagePresent = null;
  private TestCaseMessage testCaseMessageAbsent = null;
  private boolean passed = false;
  private boolean hasRun = false;
  private MessageAcceptStatus messageAcceptStatus = null;
  private MessageAcceptStatus messageAcceptStatusDetected = null;
  private ProfileUsageValue profileUsageValue = null;
  
  public ProfileLine(ProfileUsageValue profileUsageValue)
  {
    this.profileUsageValue = profileUsageValue;
  }

  public ProfileUsageValue getProfileUsageValue() {
    return profileUsageValue;
  }

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
    this.usage = Usage.readUsage(usageString);
  }

  

  

}
