package org.immunizationsoftware.dqa.mover;

public class AckAnalyzer
{

  private boolean ackMessage = false;
  private boolean positive = false;
  private String ackCode = "";

  public boolean isAckMessage()
  {
    return ackMessage;
  }

  public boolean isPositive()
  {
    return positive;
  }

  public void setPositive(boolean positive)
  {
    this.positive = positive;
  }

  public String getAckCode()
  {
    return ackCode;
  }

  public void setAckCode(String ackCode)
  {
    this.ackCode = ackCode;
  }

  public AckAnalyzer(String ack) {

    
    int ackPos = ack.indexOf("|ACK");
    int msaPos = ack.indexOf("MSH|");
    if (!ack.startsWith("MSH|") || ackPos == -1 || msaPos == -1 || msaPos < ackPos)
    {
      ackMessage = false;
    } else
    {
      String msaSegment = ack.substring(msaPos);
      int endPos = msaSegment.indexOf("\r");
      if (endPos == -1)
      {
        endPos = ack.length();
      }
      msaSegment = msaSegment.substring(0, endPos);
      msaPos = msaSegment.indexOf("|");
      if (msaPos != -1)
      {
        msaPos++;
        endPos = msaSegment.indexOf("|", msaPos);
        if (endPos != -1)
        {
          ackCode = msaSegment.substring(msaPos, endPos);
        }
      }
      positive = msaSegment.startsWith("MSA|AA|") || msaSegment.equals("MSH|AA");
    }
  }

}
