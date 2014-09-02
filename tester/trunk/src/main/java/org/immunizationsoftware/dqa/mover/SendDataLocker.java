package org.immunizationsoftware.dqa.mover;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendDataLocker
{
  private File lockFile = null;
  private long lockTimeOut = 0l;

  public SendDataLocker(File lockFile, long lockTimeOut) {
    this.lockFile = lockFile;
    this.lockTimeOut = lockTimeOut;
  }

  public boolean obtainLock() throws IOException
  {

    // obtain lock
    if (lockFile.exists())
    {
      // another process is apparently already working in this folder. This
      // should not happen, but could if two versions
      // of Simple Message Mover are currently running.
      // Need to now check and see how long it has been since this lock was
      // placed.
      if ((System.currentTimeMillis() - lockFile.lastModified()) > lockTimeOut)
      {
        lockFile.delete();
      } else
      {
        return false;
      }
    }
    renewLock();
    return true;
  }

  public void renewLock() throws IOException
  {
    SimpleDateFormat sdf = new SimpleDateFormat(ManagerServlet.STANDARD_DATE_FORMAT);
    PrintWriter out = new PrintWriter(lockFile);
    out.println("Simple Message Mover (SMM) currently working in this directory");
    out.println("");
    out.println("SMM started:  " + sdf.format(ManagerServlet.getStartDate()));
    out.println("Lock set:     " + sdf.format(new Date()));
    out.println("Random id:    " + ManagerServlet.getRandomId());
    out.println("Local IP:     " + ManagerServlet.getLocalHostIp());
    out.println("Local MAC:    " + ManagerServlet.getLocalHostMac());
    out.close();
  }

  public void releaseLock()
  {
    try
    {
      lockFile.delete();
    } catch (Throwable t)
    {
      // ignore, tried to get rid of lock, but if can't then continue normally
    }
  }
}
