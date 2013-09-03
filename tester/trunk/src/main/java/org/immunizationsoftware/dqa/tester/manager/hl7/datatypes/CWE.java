package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class CWE extends HL7Component
{

  private ST identifier;
  private ST text;

  public ST getCodingSystemVersionId() {
    return codingSystemVersionId;
  }

  public void setCodingSystemVersionId(ST codingSystemVersionId) {
    this.codingSystemVersionId = codingSystemVersionId;
  }

  public ST getAlternateCodingSystemVersionId() {
    return alternateCodingSystemVersionId;
  }

  public void setAlternateCodingSystemVersionId(ST alternateCodingSystemVersionId) {
    this.alternateCodingSystemVersionId = alternateCodingSystemVersionId;
  }

  public ST getOriginalText() {
    return originalText;
  }

  public void setOriginalText(ST originalText) {
    this.originalText = originalText;
  }

  private ID nameOfCodingSystem;
  private ST alternateIdentifier;
  private ST alternateText;
  private ID nameofAlternateCodingSystem;
  private ST codingSystemVersionId;
  private ST alternateCodingSystemVersionId;
  private ST originalText;

  private ValueSet valueSet = null;

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

  public CWE(String componentName, UsageType usageType) {
    this(componentName, usageType, null);
  }

  public CWE(String componentName, UsageType usageType, ValueSet valueSet) {
    this(componentName, usageType, null, valueSet);
  }


  @Override
  public HL7Component makeAnother() {
    return new CWE(this);
  }
  
  public CWE(CWE copy)
  {
    super(copy);
    init();
  }


  public CWE(String componentName, UsageType usageType, Cardinality cardinality, ValueSet valueSet) {
    super("CWE", "Coded With Exceptions", componentName, 9, usageType, cardinality);
    this.valueSet = valueSet;

    init();
  }

  public void init() {
    setChild(1, identifier = new ST("Identifier", UsageType.RE, 999));
    setChild(2, text = new ST("Text", UsageType.RE, 999));
    setChild(3, nameOfCodingSystem = new ID("Name of Coding System", UsageType.C, 20, ValueSet.HL70396));
    nameOfCodingSystem.setConditionalPredicate(new IsValued(identifier, UsageType.R, UsageType.X));
    setChild(4, alternateIdentifier = new ST("Alternate Identifier", UsageType.RE, 999));
    setChild(5, alternateText = new ST("Alternate Text", UsageType.C, 999));
    alternateText.setConditionalPredicate(new IsValued(alternateIdentifier, UsageType.RE, UsageType.X));
    setChild(6, nameofAlternateCodingSystem = new ID("Name of Alternate Coding System", UsageType.C, 20,
        ValueSet.HL70396));
    nameofAlternateCodingSystem.setConditionalPredicate(new IsValued(alternateIdentifier, UsageType.R, UsageType.X));
    setChild(7, codingSystemVersionId = new ST("Coding System Version Id", UsageType.O));
    setChild(8, alternateCodingSystemVersionId = new ST("Alternate Coding System Version Id", UsageType.O));
    setChild(9, originalText = new ST("Original Text", UsageType.O));
  }

}
