package org.openimmunizationsoftware.dqa.transport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class CDCWSDLServer {
  private static final String CDATA_END = "]]>";
  private static final String CDATA_START = "<![CDATA[";

  private Processor processor = new Processor(this);

  public void setProcessorName(String processorName) {
    processor = ProcessorFactory.createProcessor(processorName, this);
  }

  public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String xmlMessage = getBody(req);
    resp.setContentType("application/soap+xml; charset=UTF-8; action=\"urn:cdc:iisb:2011:connectivityTest\"");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    try {
      if (isConnectivityTest(xmlMessage)) {
        String echoBack = getEchoBack(xmlMessage);
        processor.doConnectivityTest(out, echoBack);
      } else if (isSubmitSingleMessage(xmlMessage)) {
        SubmitSingleMessage ssm = getSubmitSingleMessage(xmlMessage);
        authorize(ssm);
        processor.doProcessMessage(out, ssm);
      } else {
        throw new UnsupportedOperationFault("Expected either connectivityTest or SubmitSingleMessage");
      }
    } catch (MessageTooLargeFault mtlf) {
      processor.doPrintException(out, mtlf);
    } catch (SecurityFault sf) {
      processor.doPrintException(out, sf);
    } catch (UnsupportedOperationFault uof) {
      processor.doPrintException(out, uof);
    } catch (UnknownFault uf) {
      processor.doPrintException(out, uf);
    } catch (Exception e) {
      UnknownFault uf = new UnknownFault("Exception ocurred", e);
      processor.doPrintException(out, uf);
    } finally {
      out.close();
    }
  }

  public abstract void authorize(SubmitSingleMessage ssm) throws SecurityFault;

  public abstract void process(SubmitSingleMessage ssm, PrintWriter out) throws Fault;

  public abstract String getEchoBackMessage(String message);

  private static String getBody(HttpServletRequest req) throws IOException {

    String body = null;
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;

    try {
      InputStream inputStream = req.getInputStream();
      if (inputStream != null) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[128];
        int bytesRead = -1;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
          stringBuilder.append(charBuffer, 0, bytesRead);
        }
      } else {
        stringBuilder.append("");
      }
    } catch (IOException ex) {
      throw ex;
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException ex) {
          throw ex;
        }
      }
    }

    body = stringBuilder.toString();
    return body;
  }

  private boolean isConnectivityTest(String xmlMessage) {
    if (xmlMessage.indexOf("<connectivityTest") > 0 || xmlMessage.indexOf(":connectivityTest") > 0) {
      return true;
    }
    return false;
  }

  private boolean isSubmitSingleMessage(String xmlMessage) {
    if (xmlMessage.indexOf("<submitSingleMessage") > 0 || xmlMessage.indexOf(":submitSingleMessage") > 0) {
      return true;
    }
    return false;
  }

  private String getEchoBack(String xmlMessage) {
    int startPos = xmlMessage.indexOf("<connectivityTest");
    if (startPos < 0) {
      startPos = xmlMessage.indexOf(":connectivityTest");
    }
    if (startPos > 0) {
      startPos = xmlMessage.indexOf("echoBack", startPos);
      if (startPos > 0) {
        startPos = xmlMessage.indexOf(">", startPos);
        if (startPos > 0) {
          startPos++;
          int endPos = xmlMessage.indexOf("</", startPos);
          if (endPos > 0) {
            return xmlMessage.substring(startPos, endPos);
          }
        }
      }
    }
    return "";
  }

  private SubmitSingleMessage getSubmitSingleMessage(String xmlMessage) {
    SubmitSingleMessage submitSingleMessage = new SubmitSingleMessage();
    int startPos = xmlMessage.indexOf("<connectivityTest");
    if (startPos < 0) {
      startPos = xmlMessage.indexOf(":submitSingleMessage");
    }
    if (startPos > 0) {
      submitSingleMessage.setUsername(readField(xmlMessage, startPos, "username"));
      submitSingleMessage.setPassword(readField(xmlMessage, startPos, "password"));
      submitSingleMessage.setFacilityID(readField(xmlMessage, startPos, "facilityID"));
      submitSingleMessage.setHl7Message(readField(xmlMessage, startPos, "hl7Message"));
    }
    return submitSingleMessage;
  }

  private String readField(String xmlMessage, int startPos, String field) {
    String value = "";
    int fieldStartPos = xmlMessage.indexOf(field, startPos);
    if (fieldStartPos > 0) {
      fieldStartPos = xmlMessage.indexOf(">", fieldStartPos);
      if (fieldStartPos > 0) {
        fieldStartPos++;
        int fieldEndPos = xmlMessage.indexOf("<", fieldStartPos);
        if (fieldEndPos > 0) {
          value = xmlMessage.substring(fieldStartPos, fieldEndPos).trim();
          if (value.startsWith(CDATA_START) && value.endsWith(CDATA_END)) {
            value = value.substring(CDATA_START.length(), value.length() - CDATA_END.length());
          } else {
            value = value.replaceAll("\\Q&amp;\\E", "&").replaceAll("\\Q&#xd;\\E", "\r");
          }
        }
      }
    }
    return value;
  }

}
