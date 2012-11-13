package org.immunizationsoftware.dqa.mover;

import static org.immunizationsoftware.dqa.mover.RemoteConnectionReportingInterface.LOG_LEVEL_DEBUG;
import static org.immunizationsoftware.dqa.mover.RemoteConnectionReportingInterface.LOG_LEVEL_ERROR;
import static org.immunizationsoftware.dqa.mover.RemoteConnectionReportingInterface.LOG_LEVEL_INFO;
import static org.immunizationsoftware.dqa.mover.RemoteConnectionReportingInterface.LOG_LEVEL_WARNING;

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
  private SendData sendData;
  private File statusLoggerFile;
  private ScanStatus scanStatus = null;
  private PrintWriter out;
  private SimpleDateFormat sdf = new SimpleDateFormat(ManagerServlet.STANDARD_DATE_FORMAT);
  private SimpleDateFormat sdfTime = new SimpleDateFormat(ManagerServlet.STANDARD_TIME_FORMAT);
  private boolean somethingInterestingHappened = false;
  private int logLevel = LOG_LEVEL_DEBUG;
  
  private StatusReporter statusReporter = null;
  
  protected PrintWriter getOut()
  {
    return out;
  }
  
  public boolean isSomethingInterestingHappened()
  {
    return somethingInterestingHappened;
  }

  public void setSomethingInterestingHappened(boolean somethingInterestingHappened)
  {
    this.somethingInterestingHappened = somethingInterestingHappened;
    statusReporter.setSendStatus();
  }

  public StatusLogger(File rootFolder, SendData sendData) throws IOException {
    this.rootFolder = rootFolder;
    this.sendData = sendData;
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
    out.println("Connection Id: " + sendData.getStableSystemId());
    out.println();
    out.println("--- Log ---");
    statusReporter = sendData.getStatusReporter();
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
    writeStatusOrDelete(new File(rootFolder, "smm-is-looking"), ScanStatus.LOOKING, scanStatus);
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

  public void logInfo(String message)
  {
    log(message, LOG_LEVEL_INFO);
  }

  public void logError(String message)
  {
    log(message, LOG_LEVEL_ERROR);
  }

  public void logWarn(String message)
  {
    log(message, LOG_LEVEL_WARNING);
  }

  public void logDebug(String message)
  {
    log(message, LOG_LEVEL_DEBUG);
  }
  
  public void logFile(String filename, ScanStatus scanStatus, int messageCount)
  {
    statusReporter.registerFile(filename, scanStatus, messageCount, 0, 0);
  }
  
  public void logFile(String filename, ScanStatus scanStatus, int sentCount, int errorCount)
  {
    statusReporter.registerFile(filename, scanStatus, 0, sentCount, errorCount);
  }
  

  public void log(String message, int logLevel)
  {
    if (this.logLevel >= logLevel)
    {
      out.print(sdfTime.format(new Date()));
      out.print(" ");
      out.println(message);
      out.flush();
      statusReporter.registerIssue(message, logLevel, null);
    }
  }

  public void logError(String message, Throwable t)
  {
    if (this.logLevel >= LOG_LEVEL_ERROR)
    {
      out.print(sdfTime.format(new Date()));
      out.print(" ");
      out.println(message);
      out.flush();
      t.printStackTrace(out);
      out.flush();
      statusReporter.registerIssue(message, LOG_LEVEL_ERROR, t);
    }
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
