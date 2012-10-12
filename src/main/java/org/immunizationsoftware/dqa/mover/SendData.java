package org.immunizationsoftware.dqa.mover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.immunizationsoftware.dqa.tester.connectors.Connector;

public class SendData extends Thread
{

  public enum ScanStatus {
    STARTING, READING, SENDING, WAITING, PROBLEM;
  };

  public static final String SMM_PREFIX = "smm.";
  public static final String ERRORED_NAME = "errored";
  public static final String PROBLEM_NAME = "problem";

  public static final String CONFIG_FILE_NAME = "smm.config.txt";
  public static final String LOCK_FILE_NAME = "smm.lock";

  public static final String REQUEST_FOLDER = "request";
  public static final String RESPONSE_FOLDER = "response";
  public static final String REQUESTS_FOLDER = "requests";
  public static final String RESPONSES_FOLDER = "responses";
  public static final String SENT_FOLDER = "sent";
  public static final String UPDATE_FOLDER = "update";
  public static final String UPDATES_FOLDER = "updates";
  public static final String WORK_FOLDER = "work";

  private static final long LOCK_TIMEOUT = 2 * 60 * 60 * 1000; // two hours
  private static final long FILE_CHANGE_TIMEOUT = 60 * 1000;

  private ScanStatus scanStatus = ScanStatus.STARTING;

  private File rootDir = null;
  private File configFile = null;
  private File statusFile = null;
  private File lockFile = null;
  private FileOut problemFileOut = null;

  private File requestDir = null;
  private File requestFile = null;
  private FileOut errorFileOut = null;

  private File workDir = null;
  private File workFile = null;

  private File sentDir = null;
  private FileOut sentFileOut = null;

  private File responseDir = null;
  private FileOut responseFileOut = null;

  private File updateDir = null;
  private FileOut updateFileOut = null;

  private Connector connector = null;
  private StatusLogger statusLogger = null;
  private SendDataLocker sendDataLocker = null;
  private String filename = null;
  private String filenameStart = null;
  private String filenameEnd = null;
  private List<File> filesToProcessList = null;
  private List<File> filesToSendList = null;
  private int messageNumber = 0;

  public Connector getConnector()
  {
    return connector;
  }

  private Throwable lastException = null;

  public File getRootDir()
  {
    return rootDir;
  }

  public void setRootDir(File rootDir)
  {
    this.rootDir = rootDir;
  }

  public File getConfigFile()
  {
    return configFile;
  }

  public void setConfigFile(File configFile)
  {
    this.configFile = configFile;
  }

  public File getStatusFile()
  {
    return statusFile;
  }

  public void setStatusFile(File statusFile)
  {
    this.statusFile = statusFile;
  }

  public SendData(File rootDir) {
    this.rootDir = rootDir;
    this.configFile = new File(rootDir, CONFIG_FILE_NAME);
  }

  public ScanStatus getScanStatus()
  {
    return scanStatus;
  }

  private void setScanStatus(ScanStatus scanStatus)
  {
    this.scanStatus = scanStatus;
    if (statusLogger != null)
    {
      statusLogger.updateScanStatus(scanStatus);
    }
  }

  @Override
  public void run()
  {
    while (true)
    {
      if (scanStatus == ScanStatus.PROBLEM)
      {
        // What should we do if there was an error last time?
        continue;
      }
      scanStatus = ScanStatus.READING;
      if (configFile.exists() && configFile.isFile() && configFile.canRead())
      {
        try
        {
          // obtain lock
          lockFile = new File(rootDir, LOCK_FILE_NAME);
          sendDataLocker = new SendDataLocker(lockFile, LOCK_TIMEOUT);

          if (!sendDataLocker.obtainLock())
          {
            continue;
          }
          statusLogger = new StatusLogger(rootDir, this);

          List<Connector> connectors = Connector.makeConnectors(readScript());
          if (connectors.size() == 0)
          {
            throw new Exception("Script does not define connection");
          }
          connector = connectors.get(0);
          statusLogger.log("Looking for data to send to: " + connector.getLabel());
          createWorkingDirs();

          //

          File[] filesInWorking = workDir.listFiles(new FileFilter() {

            public boolean accept(File file)
            {
              return file.isFile();
            }
          });
          if (filesInWorking.length == 0)
          {
            lookForFilesToProcess();

            if (filesToProcessList.size() > 0)
            {
              statusLogger.log("Sending data");
              setScanStatus(ScanStatus.SENDING);
              for (File fileToSend : filesToProcessList)
              {
                requestFile = fileToSend;
                try
                {
                  sendDataLocker.renewLock();
                  statusLogger.log("Reading " + requestFile.getName());

                  createWorkingFiles();

                  // Create individual files to send
                  {

                    // good to go, there are no working files so need to find
                    // another file to analyze
                    messageNumber = 0;
                    StringBuilder message = new StringBuilder();
                    BufferedReader in = new BufferedReader(new FileReader(requestFile));
                    String line = readRealFirstLine(in);
                    String messageType = null;
                    if (line != null && (line.startsWith(HL7.MSH) || line.startsWith(HL7.FHS) || line.startsWith(HL7.BHS)))
                    {
                      do
                      {
                        line = line.trim();
                        if (line.startsWith("--- "))
                        {
                          // Is SMM comment
                          continue;
                        }
                        if (line.startsWith(HL7.MSH))
                        {
                          moveMessageToWorking(message, messageType);
                          message.setLength(0);
                          messageType = HL7.readField(line, 9);
                        } else if (line.startsWith(HL7.FHS) || line.startsWith(HL7.BHS) || line.startsWith(HL7.BTS) || line.startsWith(HL7.FTS))
                        {
                          continue;
                        }
                        message.append(line);
                        message.append("\r");
                      } while ((line = in.readLine()) != null);
                    }
                    moveMessageToWorking(message, messageType);
                    in.close();

                  }

                  // read working files

                } catch (Throwable t)
                {
                  statusLogger.log("Unable to send the data: " + t.getMessage());
                  statusLogger.log(t);
                  problemFileOut = new FileOut(new File(filenameStart + "." + PROBLEM_NAME + "." + filenameEnd), false);
                  problemFileOut.println("Unable to send the data: " + t.getMessage());
                  problemFileOut.print(t);
                  break;
                } finally
                {
                  closeOutputs();
                }
              }
            }
            statusLogger.log("Finished looking for data to send");
          }
          statusLogger.log("Sending data");
          {

            statusLogger.log("Looking for files to send from working directory");
            String[] filesToSend = workDir.list();
            Arrays.sort(filesToSend);
            for (String fileToSend : filesToSend)
            {
              workFile = new File(workDir, fileToSend);
              if (workFile.isFile())
              {
                openSendingMessageInputs();
                try
                {

                  StringBuilder sb = new StringBuilder();
                  String line = null;
                  BufferedReader in = new BufferedReader(new FileReader(workFile));
                  while ((line = in.readLine()) != null)
                  {
                    sb.append(line);
                    sb.append("\r");
                  }
                  in.close();
                  String messageText = sb.toString();
                  String responseText = connector.submitMessage(messageText, false);
                  StringBuilder responseMessage = new StringBuilder();
                  String responseMessageType = "";
                  String acknowledgmentCode = null;
                  String errorDescription = "";
                  BufferedReader responseIn = new BufferedReader(new StringReader(responseText));
                  while ((line = responseIn.readLine()) != null)
                  {
                    line = line.trim();
                    if (line.length() < 3 || HL7.isFileBatchHeaderFooterSegment(line))
                    {
                      continue;
                    }
                    if (line.startsWith(HL7.MSH))
                    {
                      if (responseMessage.length() > 0)
                      {
                        if (responseMessageType.startsWith(HL7.ACK))
                        {
                          responseFileOut.print(responseMessage.toString());
                        } else if (responseMessageType.startsWith(HL7.VXU))
                        {
                          updateFileOut.print(responseMessage.toString());
                        } else
                        {
                          responseFileOut.print(responseMessage.toString());
                        }
                      }
                      responseMessage.setLength(0);
                      responseMessageType = HL7.readField(line, 9);
                    } else if (line.startsWith(HL7.MSA))
                    {
                      if (acknowledgmentCode == null)
                      {
                        acknowledgmentCode = HL7.readField(line, 1);
                      }
                      errorDescription = addToErrorDescription(errorDescription, HL7.readField(line, 3));
                    } else if (line.startsWith(HL7.ERR))
                    {
                      String severity = HL7.readField(line, 4);
                      if (severity.equals("E"))
                      {
                        errorDescription = addToErrorDescription(errorDescription, HL7.readField(line, 8));
                      }
                    }
                    responseMessage.append(line);
                    responseMessage.append("\r");
                  }

                  sentFileOut.print(messageText);
                  if (acknowledgmentCode == null || acknowledgmentCode.equals(HL7.AE)|| acknowledgmentCode.equals(HL7.AR))
                  {
                    errorFileOut.printCommentLn("Message was not accepted");
                    errorFileOut.printCommentLn("Reason: " + errorDescription);
                    errorFileOut.printCommentLn("");
                    errorFileOut.printCommentLn("Message Sent:");
                    errorFileOut.print(messageText);
                    errorFileOut.printCommentLn("");
                    errorFileOut.printCommentLn("Acknowledgement Returned:");
                    errorFileOut.print(responseMessage.toString());
                    errorFileOut.printCommentLn("");
                    errorFileOut.printCommentLn("");
                  }
                } finally
                {
                  closeSendingMethodInputs();
                }
              }
            }

          }
          setScanStatus(ScanStatus.WAITING);
        } catch (Throwable e)
        {
          setScanStatus(ScanStatus.PROBLEM);
          lastException = e;
          if (statusLogger != null)
          {
            statusLogger.log(e);
          }
        } finally
        {
          if (statusLogger != null)
          {
            statusLogger.close();
          }
          if (sendDataLocker != null)
          {
            sendDataLocker.releaseLock();
            sendDataLocker = null;
          }
        }
      }
      synchronized (this)
      {
        try
        {
          this.wait(ManagerServlet.getCheckInterval() * 1000);
        } catch (InterruptedException ie)
        {
          // continue
        }
      }
    }
  }

  private String addToErrorDescription(String currentDescription, String addition)
  {
    if (addition == null || addition.equals(""))
    {
      return currentDescription;
    }
    if (currentDescription.equals(""))
    {
      return addition;
    }
    return currentDescription + "; " + addition;
  }

  private void closeSendingMethodInputs()
  {
    if (errorFileOut != null)
    {
      errorFileOut.close();
      errorFileOut = null;
    }
    if (sentFileOut != null)
    {
      sentFileOut.close();
      sentFileOut = null;
    }
    if (updateFileOut != null)
    {
      updateFileOut.close();
      updateFileOut = null;
    }
    if (responseFileOut != null)
    {
      responseFileOut.close();
      responseFileOut = null;
    }
  }

  private void openSendingMessageInputs()
  {
    errorFileOut = new FileOut(new File(requestDir, filenameStart + ".errored." + filenameEnd), true);
    sentFileOut = new FileOut(new File(sentDir, filename), true);
    responseFileOut = new FileOut(new File(responseDir, filename), true);
    updateFileOut = new FileOut(new File(updateDir, filename), true);
  }

  private void moveMessageToWorking(StringBuilder message, String messageType) throws Exception, FileNotFoundException, TransmissionException
  {
    if (messageType != null && !messageType.startsWith(HL7.ACK) && message.length() > 0)
    {
      messageNumber++;
      String messageText = message.toString();

      File workFile = new File(workDir, filename + "-m" + getMessageNumberString());
      PrintWriter out = new PrintWriter(workFile);
      out.print(messageText);
      out.close();
    }
  }

  private void createWorkingDirs()
  {
    requestDir = createDir(rootDir, REQUEST_FOLDER, REQUESTS_FOLDER);
    workDir = createDir(requestDir, WORK_FOLDER);
    responseDir = createDir(rootDir, RESPONSE_FOLDER, RESPONSES_FOLDER);
    updateDir = createDir(rootDir, UPDATE_FOLDER, UPDATES_FOLDER);
    sentDir = createDir(rootDir, SENT_FOLDER);
  }

  private void createWorkingFiles()
  {
    readFilename();
    String newFilename = null;
    File newRequestFile = null;
    File sentFile = new File(sentDir, filename);
    File responseFile = new File(responseDir, filename);
    File updateFile = new File(updateDir, filename);
    int count = 1;
    while (sentFile.exists() || responseFile.exists() || updateFile.exists() || (newRequestFile != null && newRequestFile.exists()))
    {
      count++;
      newFilename = filenameStart + "(" + count + ")." + filenameEnd;
      newRequestFile = new File(requestDir, newFilename);
      sentFile = new File(sentDir, newFilename);
      responseFile = new File(responseDir, newFilename);
      updateFile = new File(updateDir, newFilename);
    }
    if (newRequestFile != null)
    {
      requestFile.renameTo(newRequestFile);
      filename = newFilename;
      filenameStart = filenameStart + "(" + count + ")";
    }

  }

  private void closeOutputs()
  {
    if (errorFileOut != null)
    {
      errorFileOut.close();
      errorFileOut = null;
    }
    if (problemFileOut != null)
    {
      problemFileOut.close();
      problemFileOut = null;
    }
  }

  private void readFilename()
  {
    filename = requestFile.getName();
    int pos = filename.lastIndexOf('.');
    if (pos == -1)
    {
      filenameStart = filename;
      filenameEnd = "";
    } else
    {
      filenameStart = filename.substring(0, pos);
      pos++;
      if (pos >= filename.length())
      {
        filenameEnd = "";
      } else
      {
        filenameEnd = filename.substring(pos);
      }
    }
  }

  private void lookForFilesToProcess() throws IOException
  {
    statusLogger.log("Looking for files to process");
    File[] filesToRead = requestDir.listFiles(new FileFilter() {
      public boolean accept(File file)
      {
        String name = file.getName();
        if (file.isFile() && isNotGeneratedName(name))
        {
          return true;
        }
        return false;
      }

      private boolean isNotGeneratedName(String name)
      {
        return name.indexOf("." + ERRORED_NAME + ".") == -1;
      }
    });

    filesToProcessList = new ArrayList<File>();

    for (File fileToRead : filesToRead)
    {
      long timeSinceLastChange = System.currentTimeMillis() - fileToRead.lastModified();
      statusLogger.log(" + Considering " + fileToRead.getName());
      long fileChangeTimeout = FILE_CHANGE_TIMEOUT;
      if (!fileToRead.canRead())
      {
        statusLogger.log("   Not allowed to read file");
      } else if (timeSinceLastChange > fileChangeTimeout)
      {
        statusLogger.log("   File was recently modified, not processing yet");
      } else if (!fileContainsHL7(fileToRead))
      {
        statusLogger.log("   File does not contain HL7, not processing");
      } else
      {
        filesToProcessList.add(fileToRead);
      }
    }
  }

  /**
   * Read the file before processing and ensure it looks like what we expect.
   * First non blank line should be file header segment or message header
   * segment. In addition if there is a file header segment then the last non
   * blank line is expected to be the trailing segment. Otherwise the file is
   * assumed to contain HL7 messages. It is important to note that this check
   * does not validate HL7 format, but is built to ensure that the entire file
   * has been transmitted when batch header/footers are sent and that the file
   * doesn't contain obvious non-HL7 content.
   * 
   * @param inFile
   * @return
   * @throws IOException
   */
  private boolean fileContainsHL7(File inFile) throws IOException
  {
    BufferedReader in = new BufferedReader(new FileReader(inFile));
    String line = readRealFirstLine(in);
    boolean okay;
    if (line.startsWith(HL7.FHS))
    {
      String lastLine = line;
      while ((line = in.readLine()) != null)
      {
        if (line.trim().length() > 0)
        {
          lastLine = line;
        }
      }
      okay = lastLine.startsWith(HL7.FTS);
      if (!okay)
      {
        statusLogger.log("   ERROR: File does not end with FTS segment as expected, not processing");
      }
    } else if (line.startsWith(HL7.MSH))
    {
      statusLogger.log("    WARNING: File does not start with FHS segment as expected. ");
      okay = true;
    } else
    {
      okay = false;
      statusLogger.log("   ERROR: File does not appear to contain HL7. Must start with FHS or MSH segment.");
    }
    in.close();
    return okay;
  }

  private File createDir(File rootDir, String preferredName, String alternateName)
  {
    File file = new File(rootDir, preferredName);
    if (!file.exists())
    {
      File alternateFile = new File(rootDir, alternateName);
      if (alternateFile.exists())
      {
        file = alternateFile;
      } else
      {
        file.mkdir();
        statusLogger.log("Creating new folder " + file.getName());
      }
    }
    return file;
  }

  private File createDir(File rootDir, String filename)
  {
    File file = new File(rootDir, filename);
    if (!file.exists())
    {
      file.mkdir();
      statusLogger.log("Creating new folder " + file.getName());
    }
    return file;
  }

  /**
   * Reads the first lines of the file until it comes to a non empty line. This
   * is to handle situations where the first few lines are empty and the HL7
   * message does not immediately start.
   * 
   * @param in
   * @return
   * @throws IOException
   */
  private String readRealFirstLine(BufferedReader in) throws IOException
  {
    String line = null;
    while ((line = in.readLine()) != null)
    {
      line = line.trim();
      if (line.length() > 0 && !line.startsWith("--- "))
      {
        break;
      }
    }
    return line;
  }

  private String readScript() throws FileNotFoundException, IOException
  {
    StringBuilder script = new StringBuilder();
    BufferedReader in = new BufferedReader(new FileReader(configFile));
    String line = null;
    while ((line = in.readLine()) != null)
    {
      script.append(line);
      script.append("\n");
    }
    in.close();
    return script.toString();
  }

  private String getMessageNumberString()
  {
    String messageNumberString = String.valueOf(messageNumber);
    if (messageNumberString.length() > 5)
    {
      return messageNumberString;
    } else
    {
      messageNumberString = "0000" + messageNumberString;
      return messageNumberString.substring(messageNumberString.length() - 5);
    }
  }
}
