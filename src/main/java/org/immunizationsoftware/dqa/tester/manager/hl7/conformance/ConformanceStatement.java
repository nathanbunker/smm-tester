package org.immunizationsoftware.dqa.tester.manager.hl7.conformance;

import java.util.List;

import org.immunizationsoftware.dqa.tester.manager.hl7.ConformanceIssue;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.ConditionalPredicate;

public abstract class ConformanceStatement
{
  protected HL7Component component = null;
  protected String text = "";
  protected String[] requiredValues = null;
  protected ConditionalPredicate conditionalPredicate = null;

  public HL7Component getComponent() {
    return component;
  }
  
  public void setComponent(HL7Component component) {
    this.component = component;
  }
  
  public ConditionalPredicate getConditionalPredicate() {
    return conditionalPredicate;
  }

  public void setConditionalPredicate(ConditionalPredicate conditionalPredicate) {
    this.conditionalPredicate = conditionalPredicate;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getRequiredValue() {
    return requiredValues.length > 0 ? requiredValues[0] : "";
  }

  public void setRequiredValue(String requiredValue) {
    this.requiredValues = new String[] { requiredValue };
  }

  public void setRequiredValues(String[] requiredValues) {
    this.requiredValues = requiredValues;
  }

  public ConformanceStatement(String text) {
    this.text = text;
  }
  
  public abstract boolean conforms();
}
