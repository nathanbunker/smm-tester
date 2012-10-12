package org.immunizationsoftware.dqa.mover;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.immunizationsoftware.dqa.mover.SendData.ScanStatus;

public class StatusLogger
{
  private File rootFolder;
  private File statusLoggerFile;
  private ScanStatus scanStatus = null;
  private PrintWriter out;
  private SimpleDateFormat sdf = new SimpleDateFormat(ManagerServlet.STANDARD_DATE_FORMAT);
  private SimpleDateFormat sdfTime = new SimpleDateFormat(ManagerServlet.STANDARD_TIME_FORMAT);

  public StatusLogger(File rootFolder, SendData sendData) throws IOException {
    this.rootFolder = rootFolder;
    this.scanStatus = sendData.getScanStatus();
    this.statusLoggerFile = createStatusFile(rootFolder, sendData.getScanStatus());
    out = new PrintWriter(new FileWriter(statusLoggerFile));
    out.println("------------------------------------------------------ SIMPLE MESSAGE MOVER ---");
    out.println("                                                        " + sdf.format(new Date()));
    out.println();
    out.println();
    out.println("LOG OF EVENTS");
    out.println();
  }

  private File createStatusFile(File rootFolder, ScanStatus scanStatus)
  {
    String filename = "smm-";
    if (scanStatus == ScanStatus.STARTING)
    {
      filename += "is-starting.txt";
    } else if (scanStatus == ScanStatus.PROBLEM)
    {
      filename += "has-problem.txt";
    } else if (scanStatus == ScanStatus.READING)
    {
      filename += "is-reading.txt";
    } else if (scanStatus == ScanStatus.SENDING)
    {
      filename += "is-sending.txt";
    } else if (scanStatus == ScanStatus.WAITING)
    {
      filename += "is-waiting.txt";
    }
    File file = new File(rootFolder, filename);
    if (file.exists())
    {
      file.delete();
    }
    return file;
  }

  public void updateScanStatus(ScanStatus scanStatus)
  {
    if (this.scanStatus != scanStatus)
    {
      try
      {
        out.close();
        statusLoggerFile.renameTo(createStatusFile(rootFolder, scanStatus));
        out = new PrintWriter(new FileWriter(statusLoggerFile, true));
      } catch (IOException ioe)
      {
        // Unable to rename, just ignore
      }
    }
    this.scanStatus = scanStatus;
  }

  public void log(String message)
  {
    out.print(sdfTime.format(new Date()));
    out.print(" ");
    out.println(message);
  }

  public void log(Throwable t)
  {
    out.println(sdfTime.format(new Date()) + " Unexpected exception occurred");
    t.printStackTrace(out);
  }

  public void close()
  {
    out.close();
  }
}
