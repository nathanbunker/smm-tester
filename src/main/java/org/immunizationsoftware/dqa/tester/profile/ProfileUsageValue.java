package org.immunizationsoftware.dqa.tester.profile;

public class ProfileUsageValue
{
  private Usage usage = null;
  private String value = "";
  private String comments = "";
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
