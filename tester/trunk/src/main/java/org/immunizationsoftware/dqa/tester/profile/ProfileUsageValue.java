package org.immunizationsoftware.dqa.tester.profile;

public class ProfileUsageValue
{
  private Usage usage = null;
  private Usage usageDetected = null;
  private String value = "";
  private String comments = "";
  private String notes = "";
  
  public Usage getUsageDetected() {
    return usageDetected;
  }
  public void setUsageDetected(Usage usageDetected) {
    this.usageDetected = usageDetected;
  }

  public String getNotes() {
    return notes;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  public Usage getUsage() {
    return usage;
  }
  public void setUsage(Usage usage) {
    this.usage = usage;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public String getComments() {
    return comments;
  }
  public void setComments(String comments) {
    this.comments = comments;
  }
}
