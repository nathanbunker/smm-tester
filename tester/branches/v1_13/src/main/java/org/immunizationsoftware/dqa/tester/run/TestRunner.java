/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.run;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.tester.TestCaseMessage;
import org.immunizationsoftware.dqa.tester.connectors.Connector;

/**
 * 
 * @author nathan
 */
public class TestRunner
{

  public class Error
  {
    private ErrorType errorType = ErrorType.UNKNOWN;
    private String description = "";

    public ErrorType getErrorType()
    {
      return errorType;
    }

    public void setErrorType(ErrorType errorType)
    {
      this.errorType = errorType;
    }

    public String getDescription()
    {
      return description;
    }

    public void setDescription(String description)
    {
      this.description = description;
    }

  }

  private static final String ACTUAL_RESULT_STATUS_FAIL = "FAIL";
  public static final String ACTUAL_RESULT_STATUS_PASS = "PASS";
  private String ack = null;
  private boolean pass = false;
  private String status = "";
  private List<Error> errorList = null;
  private ErrorType errorType = ErrorType.UNKNOWN;

  public ErrorType getErrorType()
  {
    return errorType;
  }

  public List<Error> getErrorList()
  {
    return errorList;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public String getAck()
  {
    return ack;
  }

  public void setAck(String ack)
  {
    this.ack = ack;
  }

  public boolean isPass()
  {
    return pass;
  }

  public void setPass(boolean pass)
  {
    this.pass = pass;
  }

  public boolean runTest(Connector connector, TestCaseMessage testCaseMessage) throws Exception
  {
    pass = false;
    ack = null;
    ack = connector.submitMessage(testCaseMessage.getMessageText(), false);
    errorList = new ArrayList<Error>();
    if (!testCaseMessage.getAssertResult().equalsIgnoreCase(""))
    {
      String msa = "";
      int startPos = ack.indexOf("MSA|");
      pass = true;
      if (startPos == -1)
      {
        pass = false;
      } else
      {
        msa = ack.substring(startPos);
        int endPos = msa.indexOf("\r");
        if (endPos == -1)
        {
          endPos = ack.length();
        }
        msa = msa.substring(0, endPos);
        startPos = msa.indexOf("|");
        if (startPos != -1)
        {
          startPos++;
          endPos = msa.indexOf("|", startPos);
          if (endPos != -1)
          {
            testCaseMessage.setActualResultAckType(msa.substring(startPos, endPos));
            endPos++;
            startPos = msa.indexOf("|", endPos);
            if (startPos != -1)
            {
              startPos++;
              endPos = msa.indexOf("|", startPos);
              if (endPos != -1)
              {
                testCaseMessage.setActualResultAckMessage(msa.substring(startPos, endPos));
              }
            }
          }
        }
      }

      if (pass)
      {
        if (testCaseMessage.getAssertResultText().equals("*"))
        {
          if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept"))
          {
            pass = msa.startsWith("MSA|AA|");
          } else
          {
            pass = !msa.startsWith("MSA|AA|");
          }
        } else
        {
          if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept"))
          {
            pass = msa.startsWith("MSA|AA|") && (msa.indexOf("|" + testCaseMessage.getAssertResultText() + "|") != -1);
          } else if (testCaseMessage.getAssertResultStatus().startsWith("Accept and Skip"))
          {
            pass = msa.startsWith("MSA|AA|") && (ack.indexOf("|" + testCaseMessage.getAssertResultText() + "|") != -1);
          } else if (testCaseMessage.getAssertResultStatus().startsWith("Accept and Warn"))
          {
            pass = msa.startsWith("MSA|AA|") && (ack.indexOf("|" + testCaseMessage.getAssertResultText() + "|") != -1);
          } else
          {
            pass = !msa.startsWith("MSA|AA|") && (msa.indexOf("|" + testCaseMessage.getAssertResultText() + "|") != -1);
          }
        }
      }

      if (testCaseMessage.getActualResultAckType().equals("AA"))
      {
        errorType = ErrorType.ACCEPT;
      } else if (testCaseMessage.getActualResultAckType().equals("AE") || testCaseMessage.getActualResultAckType().equals("AR"))
      {
        errorType = ErrorType.ERROR;
      }

      BufferedReader reader = new BufferedReader(new StringReader(ack));
      String line;
      while ((line = reader.readLine()) != null)
      {
        if (line.startsWith("ERR|"))
        {
          Error error = new Error();
          errorList.add(error);
          startPos = 0;
          int endPos = line.indexOf("|", startPos);
          for (int i = 0; i < 4; i++)
          {
            if (startPos == -1)
            {
              break;
            }
            startPos = endPos + 1;
            endPos = line.indexOf("|", startPos);
          }
          if (startPos != -1 && endPos != -1)
          {
            String code = line.substring(startPos, endPos);
            if (code.equals("E"))
            {
              error.setErrorType(ErrorType.ERROR);
              errorType = ErrorType.ERROR;
            } else if (code.equals("W"))
            {
              error.setErrorType(ErrorType.WARNING);
              if (error.getErrorType() == ErrorType.ACCEPT || error.getErrorType() == ErrorType.WARNING)
              {
                errorType = ErrorType.WARNING;
              }
            } else if (code.equals("I"))
            {
              error.setErrorType(ErrorType.ACCEPT);
            }
            endPos = line.indexOf("|", startPos);
            for (int i = 0; i < 4; i++)
            {
              if (startPos == -1)
              {
                break;
              }
              startPos = endPos + 1;
              endPos = line.indexOf("|", startPos);
            }
            if (startPos != -1 && endPos != -1)
            {
              String description = line.substring(startPos, endPos);
              error.setDescription(description);
            }
          }
        }
      }

      if (pass)
      {
        status = "A";
        testCaseMessage.setActualResultStatus(ACTUAL_RESULT_STATUS_PASS);
        for (Error error : errorList)
        {
          if (status.equals("A") && error.getErrorType() == ErrorType.WARNING)
          {
            // ignore skip warnings
            if (!error.getDescription().startsWith("Skipped: "))
            {
              status = "W";
            }
          }
        }
      } else
      {
        testCaseMessage.setActualResultStatus(ACTUAL_RESULT_STATUS_FAIL);
        status = "E";
      }
    }
    testCaseMessage.setActualAck(ack);
    return pass;
  }
}
