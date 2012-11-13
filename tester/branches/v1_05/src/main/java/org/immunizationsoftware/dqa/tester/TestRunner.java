/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.StringReader;

import org.immunizationsoftware.dqa.tester.connectors.Connector;

/**
 *
 * @author nathan
 */
public class TestRunner {

    private String ack = null;
    private boolean pass = false;
    private String status = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public boolean runTest(Connector connector, TestCaseMessage testCaseMessage) throws Exception {
        pass = false;
        ack = null;
        ack = connector.submitMessage(testCaseMessage.getMessageText(), false);
        if (!testCaseMessage.getAssertResult().equalsIgnoreCase("")) {
            String msa = "";
            int startPos = ack.indexOf("MSA|");
            pass = true;
            if (startPos == -1) {
                pass = false;
            } else {
                msa = ack.substring(startPos);
                int endPos = msa.indexOf("\r");
                if (endPos == -1) {
                    endPos = ack.length();
                }
                msa = msa.substring(0, endPos);
                startPos = msa.indexOf("|");
                if (startPos != -1) {
                    startPos++;
                    endPos = msa.indexOf("|", startPos);
                    if (endPos != -1) {
                        testCaseMessage.setActualResultAckType(msa.substring(startPos, endPos));
                        endPos++;
                        startPos = msa.indexOf("|", endPos);
                        if (startPos != -1) {
                            startPos++;
                            endPos = msa.indexOf("|", startPos);
                            if (endPos != -1) {
                                testCaseMessage.setActualResultAckMessage(msa.substring(startPos, endPos));
                            }
                        }
                    }
                }
            }
            if (pass) {
                if (testCaseMessage.getAssertResultText().equals("*")) {
                    if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept")) {
                        pass = msa.startsWith("MSA|AA|");
                    } else {
                        pass = !msa.startsWith("MSA|AA|");
                    }
                } else {
                    if (testCaseMessage.getAssertResultStatus().equalsIgnoreCase("Accept")) {
                        pass = msa.startsWith("MSA|AA|") && (msa.indexOf("|" + testCaseMessage.getAssertResultText() + "|") != -1);
                    } else if (testCaseMessage.getAssertResultStatus().startsWith("Accept and Skip")) {
                        pass = msa.startsWith("MSA|AA|") && (ack.indexOf("|" + testCaseMessage.getAssertResultText() + "|") != -1);
                    } else if (testCaseMessage.getAssertResultStatus().startsWith("Accept and Warn")) {
                        pass = msa.startsWith("MSA|AA|") && (ack.indexOf("|" + testCaseMessage.getAssertResultText() + "|") != -1);
                    } else {
                        pass = !msa.startsWith("MSA|AA|") && (msa.indexOf("|" + testCaseMessage.getAssertResultText() + "|") != -1);
                    }
                }
            }
            if (pass) {
                status = "A";
                testCaseMessage.setActualResultStatus("PASS");
                BufferedReader reader = new BufferedReader(new StringReader(ack));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("ERR|")) {

                        startPos = 0;
                        int endPos = line.indexOf("|", startPos);
                        for (int i = 0; i < 4; i++) {
                            if (startPos == -1) {
                                break;
                            }
                            startPos = endPos + 1;
                            endPos = line.indexOf("|", startPos);
                        }
                        if (startPos != -1 && endPos != -1) {
                            String code = line.substring(startPos, endPos);
                            if (status.equals("A") && code.equals("W")) {
                                // ignore skip warnings
                                if (line.indexOf("|Skipped: ") == -1) {
                                    status = "W";
                                }
                            }
                        }
                    }
                }
            } else {
                testCaseMessage.setActualResultStatus("FAIL");
                status = "E";
            }
        }
        return pass;
    }
}
