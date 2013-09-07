package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.SetID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DynamicComponent;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.EI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ReservedComponent;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XCN;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValuedAs;

public class OBX extends HL7Component
{
  private SI setIDOBX = null;
  private ID valueType = null;
  private CE observationIdentifier = null;
  private ST observationSubID = null;
  private HL7Component observationValue = null;
  private CE units = null;
  private ST referenceRange = null;
  private IS abnormalFlags = null;
  private NM probability = null;
  private ID natureOfAbnormalTest = null;
  private ID observationResultStatus = null;
  private TS effectiveDateOfReferenceRangeValues = null;
  private ST userDefinedAccessChecks = null;
  private TS dateTimeOfTheObservation = null;
  private CE producersReference = null;
  private XCN responsibleObserver = null;
  private CE observationMethod = null;
  private EI equipmentInstanceIdentifier = null;
  private TS dateTimeOfTheAnalysis = null;

  public SI getSetIDOBX() {
    return setIDOBX;
  }

  public void setSetIDOBX(SI setIDOBX) {
    this.setIDOBX = setIDOBX;
  }

  public ID getValueType() {
    return valueType;
  }

  public void setValueType(ID valueType) {
    this.valueType = valueType;
  }

  public CE getObservationIdentifier() {
    return observationIdentifier;
  }

  public void setObservationIdentifier(CE observationIdentifier) {
    this.observationIdentifier = observationIdentifier;
  }

  public ST getObservationSubID() {
    return observationSubID;
  }

  public void setObservationSubID(ST observationSubID) {
    this.observationSubID = observationSubID;
  }

  public HL7Component getObservationValue() {
    return observationValue;
  }

  public void setObservationValue(HL7Component observationValue) {
    this.observationValue = observationValue;
  }

  public CE getUnits() {
    return units;
  }

  public void setUnits(CE units) {
    this.units = units;
  }

  public ST getReferenceRange() {
    return referenceRange;
  }

  public void setReferenceRange(ST referenceRange) {
    this.referenceRange = referenceRange;
  }

  public IS getAbnormalFlags() {
    return abnormalFlags;
  }

  public void setAbnormalFlags(IS abnormalFlags) {
    this.abnormalFlags = abnormalFlags;
  }

  public NM getProbability() {
    return probability;
  }

  public void setProbability(NM probability) {
    this.probability = probability;
  }

  public ID getNatureOfAbnormalTest() {
    return natureOfAbnormalTest;
  }

  public void setNatureOfAbnormalTest(ID natureOfAbnormalTest) {
    this.natureOfAbnormalTest = natureOfAbnormalTest;
  }

  public ID getObservationResultStatus() {
    return observationResultStatus;
  }

  public void setObservationResultStatus(ID observationResultStatus) {
    this.observationResultStatus = observationResultStatus;
  }

  public TS getEffectiveDateOfReferenceRangeValues() {
    return effectiveDateOfReferenceRangeValues;
  }

  public void setEffectiveDateOfReferenceRangeValues(TS effectiveDateOfReferenceRangeValues) {
    this.effectiveDateOfReferenceRangeValues = effectiveDateOfReferenceRangeValues;
  }

  public ST getUserDefinedAccessChecks() {
    return userDefinedAccessChecks;
  }

  public void setUserDefinedAccessChecks(ST userDefinedAccessChecks) {
    this.userDefinedAccessChecks = userDefinedAccessChecks;
  }

  public TS getDateTimeOfTheObservation() {
    return dateTimeOfTheObservation;
  }

  public void setDateTimeOfTheObservation(TS dateTimeOfTheObservation) {
    this.dateTimeOfTheObservation = dateTimeOfTheObservation;
  }

  public CE getProducersReference() {
    return producersReference;
  }

  public void setProducersReference(CE producersReference) {
    this.producersReference = producersReference;
  }

  public XCN getResponsibleObserver() {
    return responsibleObserver;
  }

  public void setResponsibleObserver(XCN responsibleObserver) {
    this.responsibleObserver = responsibleObserver;
  }

  public CE getObservationMethod() {
    return observationMethod;
  }

  public void setObservationMethod(CE observationMethod) {
    this.observationMethod = observationMethod;
  }

  public EI getEquipmentInstanceIdentifier() {
    return equipmentInstanceIdentifier;
  }

  public void setEquipmentInstanceIdentifier(EI equipmentInstanceIdentifier) {
    this.equipmentInstanceIdentifier = equipmentInstanceIdentifier;
  }

  public TS getDateTimeOfTheAnalysis() {
    return dateTimeOfTheAnalysis;
  }

  public void setDateTimeOfTheAnalysis(TS dateTimeOfTheAnalysis) {
    this.dateTimeOfTheAnalysis = dateTimeOfTheAnalysis;
  }

  @Override
  public HL7Component makeAnother() {
    return new OBX(this);
  }

  public OBX(OBX copy) {
    super(copy);
    init();
  }

  public OBX(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "OBX", "Observation Result Segment", "Observation Result Segment", 25, usageType, cardinality);
    init();
  }

  @Override
  public void init() {
    setChild(1, setIDOBX = new SI("Set ID - OBX", UsageType.R, Cardinality.ONE_TIME_ONLY));
    setIDOBX.setLength(1, 4);
    if (parentComponent != null) {
      setIDOBX
          .addConformanceStatement(new SetID(
              "IZ-20: The Value of OBX-1 (Set ID-OBX) SHALL be valued sequentially starting with the value \"1\" within a given segment group.",
              parentComponent));
    }
    setChild(2, valueType = new ID("Value Type", UsageType.R, Cardinality.ONE_TIME_ONLY, ValueSet.HL70125));
    valueType.addConformanceStatement(new ExactValue(
        "IZ-21: The value of OBX-2 (Value Type) SHALL be one of the following: CE, NM, ST, DT, ID or TS", new String[] {
            "CE", "NM", "ST", "DT", "ID", "TS" }));
    setChild(3, observationIdentifier = new CE("Observation Identifier", UsageType.R, Cardinality.ONE_TIME_ONLY,
        ValueSet.NIP003));
    setChild(4, observationSubID = new ST("Observation Sub-ID", UsageType.R, Cardinality.ONE_TIME_ONLY, 20));

    setChild(5, observationValue = new DynamicComponent("Observation Value", UsageType.R, Cardinality.ONE_TIME_ONLY) {
      @Override
      public HL7Component makeComponent() {
        String vt = valueType.getValue();
        ValueSet vs = null;
        String obsId = observationIdentifier.getValue();
        if (obsId.equals("64994-7")) {
          vs = ValueSet.HL70064;
        } else if (obsId.equals("69764-9")) {
          vs = ValueSet.CDCGI1VIS;
        } else if (obsId.equals("")) {
          vs = ValueSet.CVX;
        } 
        if (vt.equals("CE")) {
          observationValue = new CE(getComponentNameSpecific(), usageType, cardinality, vs);
        } else if (vt.equals("NM")) {
          observationValue = new NM(getComponentNameSpecific(), usageType, cardinality);
        } else if (vt.equals("ST")) {
          observationValue = new ST(getComponentNameSpecific(), usageType, cardinality);
        } else if (vt.equals("DT")) {
          observationValue = new DT(getComponentNameSpecific(), usageType, cardinality);
        } else if (vt.equals("ID")) {
          observationValue = new ID(getComponentNameSpecific(), usageType, cardinality, valueSet);
        } else if (vt.equals("TS")) {
          observationValue = new TS(getComponentNameSpecific(), usageType, cardinality);
        }
        return observationValue;
      }
    });

    setChild(6, units = new CE("Units", UsageType.C, Cardinality.ZERO_OR_ONE, ValueSet.ISO));
    units.setConditionalPredicate(new IsValuedAs(valueType, new String[] { "NM", "SN" }, usageType.R, usageType.RE));
    setChild(7, referenceRange = new ST("Reference Range", UsageType.O));
    setChild(8, abnormalFlags = new IS("Abnormal Flags", UsageType.O));
    setChild(9, probability = new NM("Probability", UsageType.O));
    setChild(10, natureOfAbnormalTest = new ID("Nature of Abnormal Test", UsageType.O));
    setChild(11, observationResultStatus = new ID("Observation Result Status", UsageType.R, Cardinality.ONE_TIME_ONLY,
        ValueSet.HL70085));
    observationResultStatus.addConformanceStatement(new ExactValue(
        "IZ-22: The value of OBX-11 (Observation Result Status) SHALL be \"F\".", "F"));
    observationResultStatus.setLength(1, 1);
    setChild(12, effectiveDateOfReferenceRangeValues = new TS("Effective Date of Reference Range Values", UsageType.O));
    setChild(13, userDefinedAccessChecks = new ST("User Defined Access Checks", UsageType.O));
    setChild(14, dateTimeOfTheObservation = new TS("Date/Time of the Observation", UsageType.RE,
        Cardinality.ZERO_OR_ONE));
    setChild(15, producersReference = new CE("Producer's Reference", UsageType.O));
    setChild(16, responsibleObserver = new XCN("Responsible Observer", UsageType.O));
    setChild(17, observationMethod = new CE("Observation Method", UsageType.C, Cardinality.ZERO_OR_ONE,
        ValueSet.CDCPHINVS));
    observationMethod
        .setConditionalPredicate(new IsValuedAs(observationIdentifier, "64994-7", UsageType.R, UsageType.O));
    setChild(18, equipmentInstanceIdentifier = new EI("Equipment Instance Identifier", UsageType.O));
    setChild(19, dateTimeOfTheAnalysis = new TS("Date/Time of the Analysis", UsageType.O));
    for (int i = 20; i <= 25; i++) {
      setChild(i, new ReservedComponent("Reserved for harmonization with V2.6", UsageType.O));
    }
  }

}
