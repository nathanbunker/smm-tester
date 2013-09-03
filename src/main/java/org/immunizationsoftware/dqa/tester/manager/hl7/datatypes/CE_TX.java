package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class CE_TX extends HL7Component
{
  private HL7Component identifier;
  private HL7Component text;
  private HL7Component nameOfCodingSystem;
  private HL7Component alternateIdentifier;
  private HL7Component alternateText;
  private HL7Component nameofAlternateCodingSystem;
  
  public HL7Component getIdentifier() {
    return identifier;
  }

  public HL7Component getText() {
    return text;
  }

  public HL7Component getNameOfCodingSystem() {
    return nameOfCodingSystem;
  }

  public HL7Component getAlternateIdentifier() {
    return alternateIdentifier;
  }

  public HL7Component getAlternateText() {
    return alternateText;
  }

  public HL7Component getNameofAlternateCodingSystem() {
    return nameofAlternateCodingSystem;
  }

  @Override
  public HL7Component makeAnother() {
    return new CE_TX(this);
  }
  
  public CE_TX(CE_TX copy)
  {
    super(copy);
    init();
  }

  public CE_TX(String componentName)
  {
    super("CE_TX", "Coded element (text only in RXA-9)", componentName, 6);
    
    init();
  }

  public void init() {
    setChild(1, identifier = new ST("Identifier", UsageType.X));
    setChild(2, text = new ST("Text", UsageType.R, 999));
    setChild(3, nameOfCodingSystem = new ID("Name of Coding System", UsageType.X));
    setChild(4, alternateIdentifier = new ST("Alternate Identifier", UsageType.X));
    setChild(5, alternateText = new ST("Alternate Text", UsageType.X));
    setChild(6, nameofAlternateCodingSystem = new ID("Name of Alternate Coding System", UsageType.X));
  }
}
