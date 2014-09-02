package org.immunizationsoftware.dqa.tester.manager.hl7.analyze;

import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;

public abstract class HL7FormatAnalyzer
{
  public boolean hasError() {
    return errorMessageList.size() > 0;
  }

  public List<String> getErrorMessageList() {
    return errorMessageList;
  }

  public void setErrorMessageList(List<String> errorMessageList) {
    this.errorMessageList = errorMessageList;
  }

  protected List<String> errorMessageList = new ArrayList<String>();

  protected HL7Component component = null;

  public HL7FormatAnalyzer(HL7Component component) {
    this.component = component;
  }

  public abstract void analyze() ;
}
