package org.immunizationsoftware.dqa.tester.profile;

public class ProfileUsageValue
{
  private Usage usage = null;
  private Usage usageDetected = null;
  private String value = "";
  private String comments = "";
  private String notes = "";
  private Enforcement enforcement = Enforcement.NOT_DEFINED;
  private Implementation implementation = Implementation.NOT_DEFINED;
  
  public Enforcement getEnforcement() {
    return enforcement;
  }
  public void setEnforcement(Enforcement enforcement) {
    this.enforcement = enforcement;
  }
  public Implementation getImplementation() {
    return implementation;
  }
  public void setImplementation(Implementation implementation) {
    this.implementation = implementation;
  }
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
