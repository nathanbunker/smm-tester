package org.immunizationsoftware.dqa.mover;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.immunizationsoftware.dqa.SoftwareVersion;
import org.immunizationsoftware.dqa.mover.SendData.ScanStatus;

public class StatusLogger
{

  private static final String RUNNING_FILE_NAME = "smm.running-log.txt";
  private static final String LOG_FILE_NAME = "smm.log.txt";

  private File rootFolder;
  private File statusLoggerFile;
  private ScanStatus scanStatus = null;
  private PrintWriter out;
  private SimpleDateFormat sdf = new SimpleDateFormat(ManagerServlet.STANDARD_DATE_FORMAT);
  private SimpleDateFormat sdfTime = new SimpleDateFormat(ManagerServlet.STANDARD_TIME_FORMAT);
  private boolean somethingInterestingHappened = false;

  public boolean isSomethingInterestingHappened()
  {
    return somethingInterestingHappened;
  }

  public void setSomethingInterestingHappened(boolean somethingInterestingHappened)
  {
    this.somethingInterestingHappened = somethingInterestingHappened;
  }

  public StatusLogger(File rootFolder, SendData sendData) throws IOException {
    this.rootFolder = rootFolder;
    this.scanStatus = sendData.getScanStatus();
    this.statusLoggerFile = new File(rootFolder, RUNNING_FILE_NAME);
    File oldLogFile = new File(rootFolder, LOG_FILE_NAME);
    if (oldLogFile.exists())
    {
      oldLogFile.delete();
    }
    logStatusFile();
    out = new PrintWriter(new FileWriter(statusLoggerFile));
    out.println("--- SIMPLE MESSAGE MOVER ----------------------------------------------------- ");
    String label = "";
    if (sendData.getConnector() != null)
    {
      label = sendData.getConnector().getLabel();
      out.println(sendData.getConnector().getLabelDisplay() + " - " + sdf.format(new Date()));
    }
    out.println();
    out.println("Software Version: " + SoftwareVersion.VERSION);
    out.println("Login Username: " + label);
    out.println("Login Password: " + sendData.getRandomId());
    out.println();
    out.println("--- Log ---");

  }

  private void writeStatusOrDelete(File file, ScanStatus scanStatusExpected, ScanStatus scanStatusActual) throws IOException
  {
    if (scanStatusExpected == scanStatusActual)
    {
      PrintWriter out = new PrintWriter(file);
      out.println(scanStatusActual);
      out.close();
    } else if (file.exists())
    {
      file.delete();
    }
  }

  private void logStatusFile() throws IOException
  {
    writeStatusOrDelete(new File(rootFolder, "smm-is-starting"), ScanStatus.STARTING, scanStatus);
    writeStatusOrDelete(new File(rootFolder, "smm-has-problem"), ScanStatus.PROBLEM, scanStatus);
    writeStatusOrDelete(new File(rootFolder, "smm-is-preparing"), ScanStatus.PREPARING, scanStatus);
    writeStatusOrDelete(new File(rootFolder, "smm-is-reading"), ScanStatus.READING, scanStatus);
    writeStatusOrDelete(new File(rootFolder, "smm-is-sending"), ScanStatus.SENDING, scanStatus);
    writeStatusOrDelete(new File(rootFolder, "smm-is-waiting"), ScanStatus.WAITING, scanStatus);
  }

  public void updateScanStatus(ScanStatus scanStatus)
  {
    this.scanStatus = scanStatus;
    try
    {
      logStatusFile();
    } catch (IOException ioe)
    {
      // try but if can't do it don't log an error, this is an FYI
    }
  }

  public void log(String message)
  {
    out.print(sdfTime.format(new Date()));
    out.print(" ");
    out.println(message);
    out.flush();
  }

  public void log(Throwable t)
  {
    out.println(sdfTime.format(new Date()) + " Unexpected exception occurred");
    t.printStackTrace(out);
    out.flush();
  }

  public void close()
  {
    out.println("--- CLOSE ---");
    out.close();
    File logFile = new File(rootFolder, LOG_FILE_NAME);
    if (!logFile.exists() || somethingInterestingHappened)
    {
      if (logFile.exists())
      {
        logFile.delete();
      }
      logFile = new File(rootFolder, LOG_FILE_NAME);
      statusLoggerFile.renameTo(logFile);
    } else
    {
      statusLoggerFile.delete();
    }
  }
}
