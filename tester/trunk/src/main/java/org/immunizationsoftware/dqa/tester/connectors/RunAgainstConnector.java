package org.immunizationsoftware.dqa.tester.connectors;

import java.util.List;

import org.immunizationsoftware.dqa.tester.certify.CertifyRunner;
import org.immunizationsoftware.dqa.tester.manager.HL7Reader;

public class RunAgainstConnector extends Connector
{

  private String runAgainstTestStartTime = null;
  private String originalRequestMessage = "";
  private String responseMessage = "";
  private String testSectionType = "";
  private String testCaseCategory = "";

  public String getTestSectionType() {
    return testSectionType;
  }

  public void setTestSectionType(String testSectionType) {
    this.testSectionType = testSectionType;
  }

  public String getRunAgainstTestStartTime() {
    return runAgainstTestStartTime;
  }

  public void setRunAgainstTestStartTime(String runAgainstTestStartTime) {
    this.runAgainstTestStartTime = runAgainstTestStartTime;
  }

  public String getTestCaseCategory() {
    return testCaseCategory;
  }

  public void setTestCaseCategory(String testCaseCategory) {
    this.testCaseCategory = testCaseCategory;
  }

  public String getOriginalRequestMessage() {
    return originalRequestMessage;
  }

  public RunAgainstConnector(Connector originalConnector, String runAgainstTestStartTime) {
    super(originalConnector);
    this.runAgainstTestStartTime = runAgainstTestStartTime;
  }

  @Override
  protected void setupFields(List<String> fields) {
    // nothing to do
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    
    originalRequestMessage = CertifyRunner.getManualMessage(label, runAgainstTestStartTime, testSectionType,
        testCaseCategory, "request");
    responseMessage = CertifyRunner.getManualMessage(label, runAgainstTestStartTime, testSectionType, testCaseCategory,
        "response");
    return responseMessage;
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    return "Not supported";
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {
    // nothing to do
  }

}
