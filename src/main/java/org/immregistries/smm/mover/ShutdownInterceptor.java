package org.immregistries.smm.mover;


public class ShutdownInterceptor extends Thread
{
  
  @Override
  public void run()
  {
    for (SendData sendData : ConnectionManager.getSendDataSet())
    {
      sendData.shutdown();
    }
  }
}
