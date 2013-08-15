package org.immunizationsoftware.dqa.mover;

import java.util.Date;

public class StatusFile
{
  private String fileName = "";
  private String statusLabel = "";
  private int sentCount = 0;
  private int errorCount = 0;
  private int messageCount = 0;

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public String getStatusLabel()
  {
    return statusLabel;
  }

  public void setStatusLabel(String statusLabel)
  {
    this.statusLabel = statusLabel;
  }

  public int getSentCount()
  {
    return sentCount;
  }

  public void setSentCount(int sentCount)
  {
    this.sentCount = sentCount;
  }

  public int getErrorCount()
  {
    return errorCount;
  }

  public void setErrorCount(int errorCount)
  {
    this.errorCount = errorCount;
  }

  public int getMessageCount()
  {
    return messageCount;
  }

  public void setMessageCount(int messageCount)
  {
    this.messageCount = messageCount;
  }

}
