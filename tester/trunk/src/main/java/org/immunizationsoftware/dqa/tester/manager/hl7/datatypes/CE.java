package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class CE extends HL7Component
{
  
  private ST identifier;
  private ST text;
  private ID nameOfCodingSystem;
  private ST alternateIdentifier;
  private ST alternateText;
  private ID nameofAlternateCodingSystem;
  
  public void setIdentifier(ST identifier) {
    this.identifier = identifier;
  }

  public void setNameOfCodingSystem(ID nameOfCodingSystem) {
    this.nameOfCodingSystem = nameOfCodingSystem;
  }

  public void setAlternateIdentifier(ST alternateIdentifier) {
    this.alternateIdentifier = alternateIdentifier;
  }

  public void setAlternateText(ST alternateText) {
    this.alternateText = alternateText;
  }

  public void setNameofAlternateCodingSystem(ID nameofAlternateCodingSystem) {
    this.nameofAlternateCodingSystem = nameofAlternateCodingSystem;
  }

  public ST getIdentifier() {
    return identifier;
  }

  public ST getText() {
    return text;
  }

  public ID getNameOfCodingSystem() {
    return nameOfCodingSystem;
  }

  public ST getAlternateIdentifier() {
    return alternateIdentifier;
  }

  public ST getAlternateText() {
    return alternateText;
  }

  public ID getNameofAlternateCodingSystem() {
    return nameofAlternateCodingSystem;
  }

  public CE(String componentName, UsageType usageType)
  {
    this(componentName, usageType, null);
  }
  
  @Override
  public HL7Component makeAnother() {
    return new CE(this);
  }
  
  public CE(CE copy)
  {
    super(copy);
    init();
  }

  public CE(String componentName, UsageType usageType, ValueSet valueSet)
  {
    super(ItemType.DATATYPE, "CE", "Coded element", componentName, 6, usageType);
    this.valueSet = valueSet;
    init();
  }

  public CE(String componentName, UsageType usageType, Cardinality cardinality, ValueSet valueSet)
  {
    super(ItemType.DATATYPE, "CE", "Coded element", componentName, 6, usageType, cardinality);
    this.valueSet = valueSet;
    init();
  }

  public void init() {
    setChild(1, identifier = new ST("Identifier", UsageType.R, 50));
    setChild(2, text = new ST("Text", UsageType.RE, 999));
    setChild(3, nameOfCodingSystem = new ID("Name of Coding System", UsageType.RE, 20, ValueSet.HL70396));
    setChild(4, alternateIdentifier = new ST("Alternate Identifier", UsageType.RE, 50));
    setChild(5, alternateText = new ST("Alternate Text", UsageType.RE, 999));
    setChild(6, nameofAlternateCodingSystem = new ID("Name of Alternate Coding System", UsageType.C, 20, ValueSet.HL70396));
    nameofAlternateCodingSystem.setConditionalPredicate(new IsValued(alternateIdentifier, UsageType.R, UsageType.X));
  }
}
