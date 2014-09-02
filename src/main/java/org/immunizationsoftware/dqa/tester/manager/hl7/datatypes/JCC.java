package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;

public class JCC extends HL7Component
{
  private IS jobCode = null;
  public IS getJobCode() {
    return jobCode;
  }

  public void setJobCode(IS jobCode) {
    this.jobCode = jobCode;
  }

  public IS getJobClass() {
    return jobClass;
  }

  public void setJobClass(IS jobClass) {
    this.jobClass = jobClass;
  }

  public TX getJobDescriptionText() {
    return jobDescriptionText;
  }

  public void setJobDescriptionText(TX jobDescriptionText) {
    this.jobDescriptionText = jobDescriptionText;
  }

  private IS jobClass = null;
  private TX jobDescriptionText = null;

  @Override
  public HL7Component makeAnother() {
    return new JCC(this);
  }
  
  public JCC(JCC copy)
  {
    super(copy);
    init();
  }

  public JCC(String componentName, UsageType usageType) {
    super(ItemType.DATATYPE, "JCC", "job code/class", componentName, 3, usageType);
    init();
  }

  public void init() {
    setChild(1, jobCode = new IS("Job Code", UsageType.O));
    setChild(2, jobClass = new IS("Job Class", UsageType.O));
    setChild(3, jobDescriptionText = new TX("Job Description Text", UsageType.O));
  }
}
