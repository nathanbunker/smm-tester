package org.immunizationsoftware.dqa.mover;

import java.util.Date;

public class StatusLog
{
  private int logLevel;
  private String issueText = "";
  private Throwable exception = null;
  private Date reportedData = new Date();
  
  public Date getReportedData()
  {
    return reportedData;
  }
  public void setReportedData(Date reportedData)
  {
    this.reportedData = reportedData;
  }
  public Throwable getException()
  {
    return exception;
  }
  public void setException(Throwable exception)
  {
    this.exception = exception;
  }
  public int getLogLevel()
  {
    return logLevel;
  }
  public void setLogLevel(int logLevel)
  {
    this.logLevel = logLevel;
  }
  public String getIssueText()
  {
    return issueText;
  }
  public void setIssueText(String issueText)
  {
    this.issueText = issueText;
  }

  
  
}
