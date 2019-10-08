package org.immregistries.smm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsiisLogReader {

  private static final String fileLocation = "C:\\test\\usiis";

  // ignore
  private static final String SOAP_INIT = "SOAP INIT";
  private static final String POST_INIT = "POST INIT";

  // use
  private static final String PROFILE_BEGIN_SOAP = "PROFILE BEGIN SOAP";
  private static final String BEGIN_POST_HL7SERVLET = "BEGIN POST Hl7Servlet";
  private static final String BEGIN_GETAUTHROUT = "BEGIN getAuthRouth";
  private static final String END_GETAUTHROUT = "END getAuthRouth";
  private static final String IISSERVICEIMPL_SUBMITSINGLEMESSAGE =
      "IisServiceImpl.submitSingleMessage";
  private static final String BEGIN_CALLING_CONNECT_ON_SOCKET_PORT_MIRTH_OR_OTHER =
      "BEGIN calling connect on socket Port (Mirth or other)";
  private static final String END_CALLING_CONNECT_ON_SOCKET_PORT_MIRTH_OR_OTHER =
      "END calling connect on socket Port (Mirth or other)";
  private static final String HL7_SUBMITTED = "HL7 SUBMITTED";
  private static final String END_IISSERVICEIMPL_SUBMITSINGLEMESSAGE =
      "END IisServiceImpl.submitSingleMessage";
  private static final String EHR_SERVICE = "EHR_SERVICE";
  private static final String PROFILE_END_SOAP_HL7WEBSERVICE = "PROFILE END SOAP Hl7WebService";
  private static final String END_POST_HL7SERVLET = "END POST Hl7Servlet";

  private static final String[] FIELDS_TO_IGNORE = {SOAP_INIT, POST_INIT, EHR_SERVICE};

  private static final String[] FIELDS_TO_PROCESS = {PROFILE_BEGIN_SOAP, BEGIN_POST_HL7SERVLET,
      BEGIN_GETAUTHROUT, END_GETAUTHROUT, IISSERVICEIMPL_SUBMITSINGLEMESSAGE,
      BEGIN_CALLING_CONNECT_ON_SOCKET_PORT_MIRTH_OR_OTHER,
      END_CALLING_CONNECT_ON_SOCKET_PORT_MIRTH_OR_OTHER, HL7_SUBMITTED,
      END_IISSERVICEIMPL_SUBMITSINGLEMESSAGE, PROFILE_END_SOAP_HL7WEBSERVICE, END_POST_HL7SERVLET};

  private static Map<String, Map<String, String>> messageIdFieldMap = new HashMap<>();

  private static class MinuteStat {
    Date minute;
    int messageCount = 0;
    long elapsedTotal = 0;
  }

  private static Map<String, MinuteStat> minuteStatMap = new HashMap<>();


  public static void main(String[] args) throws IOException {
    File file = new File(fileLocation);
    String[] logFilenames = file.list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".log");
      }
    });
    int problemCount = 0;
    for (String logFilename : logFilenames) {
      minuteStatMap = new HashMap<>();
      System.out.println("Reading " + logFilename);
      File logFile = new File(file, logFilename);
      BufferedReader in = new BufferedReader(new FileReader(logFile));
      File summaryFile = new File(file, logFilename + ".csv");
      PrintWriter out = new PrintWriter(new FileWriter(summaryFile));
      try {
        out.print("Log File Id");
        for (String f : FIELDS_TO_PROCESS) {
          out.print("|");
          out.print(f);
        }
        out.println();
        String line;
        int lineCount = 0;
        while ((line = in.readLine()) != null) {
          lineCount++;
          if (line.length() < 40 || !line.substring(24, 31).equals(" INFO [")) {
            continue;
          }

          boolean ignore = false;
          for (String f : FIELDS_TO_IGNORE) {
            if (line.contains(f)) {
              ignore = true;
              continue;
            }
          }
          if (ignore) {
            continue;
          }

          if (line.contains(PROFILE_BEGIN_SOAP) || line.contains(BEGIN_POST_HL7SERVLET)) {
            String messageId = readMessageId(line, lineCount);
            if (messageId != null) {
              messageIdFieldMap.put(messageId, new HashMap<String, String>());
            }
          }

          boolean foundValue = false;
          for (String f : FIELDS_TO_PROCESS) {
            if (line.contains(f)) {
              foundValue = true;
              boolean foundMessageId = false;
              String idsCurrentlyOpen = "";
              for (String messageId : messageIdFieldMap.keySet()) {
                if (!idsCurrentlyOpen.equals("")) {
                  idsCurrentlyOpen += ", ";
                }
                idsCurrentlyOpen += "'" + messageId + "'";
              }
              for (String messageId : messageIdFieldMap.keySet()) {
                int i = messageId.indexOf("[");
                String messageIdPart1 = messageId.substring(0, i);
                String messageIdPart2 = messageId.substring(i);
                // System.out.println("Looking for line containing '" + messageIdPart1 + "' and '" + messageIdPart2 + "'");
                if (line.contains(messageIdPart1) && line.contains(messageIdPart2)) {

                  messageIdFieldMap.get(messageId).put(f, line.substring(0, 24));
                  foundMessageId = true;
                  break;
                }
              }
              if (!foundMessageId) {
                System.err.println(lineCount + ": Unrecognized message id: " + line);
                System.err
                    .println(lineCount + ":   Looking for one of these ids: " + idsCurrentlyOpen);
                problemCount++;
                if (problemCount > 70) {
                  return;
                }
              }
              break;
            }
          }
          if (!foundValue) {
            System.err.println("Unrecognized line: " + line);
          }

          if (line.contains(PROFILE_END_SOAP_HL7WEBSERVICE) || line.contains(END_POST_HL7SERVLET)) {
            String messageId = readMessageId(line, lineCount);
            if (messageId != null) {
              printMessage(out, messageId, lineCount);
              messageIdFieldMap.remove(messageId);
            }

          }
        }
        for (String messageId : messageIdFieldMap.keySet()) {
          printMessage(out, messageId, lineCount);
        }

        File minuteFile = new File(file, logFilename + ".minute.csv");
        PrintWriter outMinute = new PrintWriter(new FileWriter(minuteFile));
        outMinute.println("Minute,Count,Total Elapsed");
        List<String> minuteList = new ArrayList<>(minuteStatMap.keySet());
        Collections.sort(minuteList);
        if (minuteList.size() > 0) {
          MinuteStat ms1 = minuteStatMap.get(minuteList.get(0));
          MinuteStat ms2 = minuteStatMap.get(minuteList.get(minuteList.size() - 1));
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(ms1.minute);
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
          while (calendar.getTime().before(ms2.minute)) {
            Date s = calendar.getTime();
            calendar.add(Calendar.MINUTE, 1);
            String minute = sdf.format(s);
            MinuteStat minuteStat = minuteStatMap.get(minute);
            if (minuteStat == null) {
              minuteStat = new MinuteStat();
              minuteStat.minute = s;
              minuteStatMap.put(minute, minuteStat);
            }
          }
          minuteList = new ArrayList<>(minuteStatMap.keySet());
          Collections.sort(minuteList);
          for (String minute : minuteList) {
            MinuteStat minuteStat = minuteStatMap.get(minute);
            outMinute
                .println(minute + "," + minuteStat.messageCount + "," + minuteStat.elapsedTotal);
          }
        }
        outMinute.close();
      } finally {
        in.close();
        out.close();
      }
    }
  }

  public static void printMessage(PrintWriter out, String messageId, int lineCount) {
    out.print(messageId);
    Map<String, String> m = messageIdFieldMap.get(messageId);
    for (String f : FIELDS_TO_PROCESS) {
      out.print("|");
      String v = null;
      if (m != null) {
        v = m.get(f);
      }
      if (v != null) {
        out.print(v);
      }
    }
    out.println();
    if (m != null) {
      String startTime = m.get(PROFILE_BEGIN_SOAP);
      if (startTime == null) {
        startTime = m.get(BEGIN_POST_HL7SERVLET);
      }
      String endTime = m.get(PROFILE_END_SOAP_HL7WEBSERVICE);
      if (endTime == null) {
        endTime = m.get(END_POST_HL7SERVLET);
      }
      if (startTime != null && endTime != null) {
        Date s = null;
        Date e = null;
        try {
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
          s = sdf.parse(startTime);
          e = sdf.parse(endTime);
        } catch (ParseException pe) {
          pe.printStackTrace();
        }
        if (s != null && e != null && !s.after(e)) {
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
          String minute = sdf.format(s);
          MinuteStat minuteStat = minuteStatMap.get(minute);
          if (minuteStat == null) {
            minuteStat = new MinuteStat();
            minuteStat.minute = s;
            minuteStatMap.put(minute, minuteStat);
          }
          minuteStat.messageCount++;
          minuteStat.elapsedTotal += e.getTime() - s.getTime();
        }
      }
    }
  }

  public static String readMessageId(String line, int lineCount) {
    String messageId = null;
    int i = line.indexOf(" - id=");
    if (i > 0) {
      messageId = line.substring(i + 6).trim();
      i = messageId.indexOf(" time=");
      if (i > 0) {
        messageId = messageId.substring(0, i).trim();
      }
      int p1 = line.indexOf("[");
      int p2 = line.indexOf("]");
      if (p1 > 0 & p1 < p2) {
        messageId += line.substring(p1, p2);
      } else {
        System.err.println(lineCount + ": Badly formed line, expecting [] after info");
        messageId += "[";
      }
    } else {
      System.err.println(lineCount + ": Unable to read id from this line: " + line);
    }
    return messageId;
  }

  private static class Event {
    String timestamp = "";

  }
}
