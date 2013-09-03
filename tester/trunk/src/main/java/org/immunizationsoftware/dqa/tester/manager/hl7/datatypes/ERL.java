package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class ERL extends HL7Component
{

  private ST segmentID = null;
  private NM segmentSequence = null;
  private NM fieldPosition = null;
  private NM fieldRepetition = null;
  private NM componentNumber = null;
  private NM subComponentNumber = null;

  public ST getSegmentID() {
    return segmentID;
  }

  public void setSegmentID(ST segmentID) {
    this.segmentID = segmentID;
  }

  public NM getSegmentSequence() {
    return segmentSequence;
  }

  public void setSegmentSequence(NM segmentSequence) {
    this.segmentSequence = segmentSequence;
  }

  public NM getFieldPosition() {
    return fieldPosition;
  }

  public void setFieldPosition(NM fieldPosition) {
    this.fieldPosition = fieldPosition;
  }

  public NM getFieldRepetition() {
    return fieldRepetition;
  }

  public void setFieldRepetition(NM fieldRepetition) {
    this.fieldRepetition = fieldRepetition;
  }

  public NM getComponentNumber() {
    return componentNumber;
  }

  public void setComponentNumber(NM componentNumber) {
    this.componentNumber = componentNumber;
  }

  public NM getSubComponentNumber() {
    return subComponentNumber;
  }

  public void setSubComponentNumber(NM subComponentNumber) {
    this.subComponentNumber = subComponentNumber;
  }

  public ERL(String componentNameSpecific, UsageType usageType) {
    this(componentNameSpecific, usageType, null);
  }

  public ERL(String componentNameSpecific, UsageType usageType, Cardinality cardinality) {
    super("ERL", "Error Location", componentNameSpecific, 6, usageType, cardinality);
    init();
  }

  @Override
  public HL7Component makeAnother() {
    return new ERL(this);
  }

  public ERL(ERL copy) {
    super(copy);
    init();
  }
  
  public ERL()
  {
    super("ERL", "Error Location", 6);
    init();
  }

  public void init() {
    setChild(1, segmentID = new ST("Segment ID", UsageType.R, 3));
    segmentID.setLength(3, 3);
    setChild(2, segmentSequence = new NM("Segment Sequence", UsageType.R, 2));
    setChild(3, fieldPosition = new NM("Field Position", UsageType.RE, 2));
    setChild(4, fieldRepetition = new NM("Field Repetition", UsageType.C, 2));
    fieldRepetition.setConditionalPredicate(new IsValued(fieldPosition, UsageType.R, UsageType.X));
    setChild(5, componentNumber = new NM("Component Number", UsageType.RE, 2));
    setChild(6, subComponentNumber = new NM("Sub-Component Number", UsageType.RE, 2));
  }
}
