package org.immunizationsoftware.dqa.mover;


public class ShutdownInterceptor extends Thread
{
  
  @Override
  public void run()
  {
    for (SendData sendData : ManagerServlet.getSendDataSet())
    {
      sendData.shutdown();
    }
  }
}
