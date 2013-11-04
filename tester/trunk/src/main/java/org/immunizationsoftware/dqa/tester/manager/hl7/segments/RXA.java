package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.MatchValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CWE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.LA2;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XCN;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsNotValuedAs;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValuedAs;

public class RXA extends HL7Component
{

  private NM giveSubIDCounter = null;
  private NM administrationSubIDCounter = null;
  private TS dateTimeStartOfAdministration = null;
  private TS dateTimeEndOfAdministration = null;
  private CE administeredCode = null;
  private NM administeredAmount = null;
  private CE administeredUnits = null;
  private CE administeredDosageForm = null;
  private CE administrationNotes = null;
  private XCN administeringProvider = null;
  private LA2 administeredAtLocation = null;
  private ST administeredPerTimeUnit = null;
  private NM administeredStrength = null;
  private CE administeredStrengthUnits = null;
  private ST substanceLotNumber = null;
  private TS substanceExpirationDate = null;
  private CE substanceManufacturerName = null;
  private CE substanceTreatmentRefusalReason = null;
  private CE indication = null;
  private ID completionStatus = null;
  private ID actionCodeRXA = null;
  private TS systemEntryDateTime = null;
  private NM administeredDrugStrengthVolume = null;
  private CWE administeredDrugStrengthVolumeUnits = null;
  private CWE administeredBarcodeIdentifier = null;
  private ID pharmacyOrderType = null;

  public NM getGiveSubIDCounter() {
    return giveSubIDCounter;
  }

  public void setGiveSubIDCounter(NM giveSubIDCounter) {
    this.giveSubIDCounter = giveSubIDCounter;
  }

  public NM getAdministrationSubIDCounter() {
    return administrationSubIDCounter;
  }

  public void setAdministrationSubIDCounter(NM administrationSubIDCounter) {
    this.administrationSubIDCounter = administrationSubIDCounter;
  }

  public TS getDateTimeStartOfAdministration() {
    return dateTimeStartOfAdministration;
  }

  public void setDateTimeStartOfAdministration(TS dateTimeStartOfAdministration) {
    this.dateTimeStartOfAdministration = dateTimeStartOfAdministration;
  }

  public TS getDateTimeEndOfAdministration() {
    return dateTimeEndOfAdministration;
  }

  public void setDateTimeEndOfAdministration(TS dateTimeEndOfAdministration) {
    this.dateTimeEndOfAdministration = dateTimeEndOfAdministration;
  }

  public CE getAdministeredCode() {
    return administeredCode;
  }

  public void setAdministeredCode(CE administeredCode) {
    this.administeredCode = administeredCode;
  }

  public NM getAdministeredAmount() {
    return administeredAmount;
  }

  public void setAdministeredAmount(NM administeredAmount) {
    this.administeredAmount = administeredAmount;
  }

  public CE getAdministeredUnits() {
    return administeredUnits;
  }

  public void setAdministeredUnits(CE administeredUnits) {
    this.administeredUnits = administeredUnits;
  }

  public CE getAdministeredDosageForm() {
    return administeredDosageForm;
  }

  public void setAdministeredDosageForm(CE administeredDosageForm) {
    this.administeredDosageForm = administeredDosageForm;
  }

  public CE getAdministrationNotes() {
    return administrationNotes;
  }

  public void setAdministrationNotes(CE administrationNotes) {
    this.administrationNotes = administrationNotes;
  }

  public XCN getAdministeringProvider() {
    return administeringProvider;
  }

  public void setAdministeringProvider(XCN administeringProvider) {
    this.administeringProvider = administeringProvider;
  }

  public LA2 getAdministeredAtLocation() {
    return administeredAtLocation;
  }

  public void setAdministeredAtLocation(LA2 administeredAtLocation) {
    this.administeredAtLocation = administeredAtLocation;
  }

  public ST getAdministeredPerTimeUnit() {
    return administeredPerTimeUnit;
  }

  public void setAdministeredPerTimeUnit(ST administeredPerTimeUnit) {
    this.administeredPerTimeUnit = administeredPerTimeUnit;
  }

  public NM getAdministeredStrength() {
    return administeredStrength;
  }

  public void setAdministeredStrength(NM administeredStrength) {
    this.administeredStrength = administeredStrength;
  }

  public CE getAdministeredStrengthUnits() {
    return administeredStrengthUnits;
  }

  public void setAdministeredStrengthUnits(CE administeredStrengthUnits) {
    this.administeredStrengthUnits = administeredStrengthUnits;
  }

  public ST getSubstanceLotNumber() {
    return substanceLotNumber;
  }

  public void setSubstanceLotNumber(ST substanceLotNumber) {
    this.substanceLotNumber = substanceLotNumber;
  }

  public TS getSubstanceExpirationDate() {
    return substanceExpirationDate;
  }

  public void setSubstanceExpirationDate(TS substanceExpirationDate) {
    this.substanceExpirationDate = substanceExpirationDate;
  }

  public CE getSubstanceManufacturerName() {
    return substanceManufacturerName;
  }

  public void setSubstanceManufacturerName(CE substanceManufacturerName) {
    this.substanceManufacturerName = substanceManufacturerName;
  }

  public CE getSubstanceTreatmentRefusalReason() {
    return substanceTreatmentRefusalReason;
  }

  public void setSubstanceTreatmentRefusalReason(CE substanceTreatmentRefusalReason) {
    this.substanceTreatmentRefusalReason = substanceTreatmentRefusalReason;
  }

  public CE getIndication() {
    return indication;
  }

  public void setIndication(CE indication) {
    this.indication = indication;
  }

  public ID getCompletionStatus() {
    return completionStatus;
  }

  public void setCompletionStatus(ID completionStatus) {
    this.completionStatus = completionStatus;
  }

  public ID getActionCodeRXA() {
    return actionCodeRXA;
  }

  public void setActionCodeRXA(ID actionCodeRXA) {
    this.actionCodeRXA = actionCodeRXA;
  }

  public TS getSystemEntryDateTime() {
    return systemEntryDateTime;
  }

  public void setSystemEntryDateTime(TS systemEntryDateTime) {
    this.systemEntryDateTime = systemEntryDateTime;
  }

  public NM getAdministeredDrugStrengthVolume() {
    return administeredDrugStrengthVolume;
  }

  public void setAdministeredDrugStrengthVolume(NM administeredDrugStrengthVolume) {
    this.administeredDrugStrengthVolume = administeredDrugStrengthVolume;
  }

  public CWE getAdministeredDrugStrengthVolumeUnits() {
    return administeredDrugStrengthVolumeUnits;
  }

  public void setAdministeredDrugStrengthVolumeUnits(CWE administeredDrugStrengthVolumeUnits) {
    this.administeredDrugStrengthVolumeUnits = administeredDrugStrengthVolumeUnits;
  }

  public CWE getAdministeredBarcodeIdentifier() {
    return administeredBarcodeIdentifier;
  }

  public void setAdministeredBarcodeIdentifier(CWE administeredBarcodeIdentifier) {
    this.administeredBarcodeIdentifier = administeredBarcodeIdentifier;
  }

  public ID getPharmacyOrderType() {
    return pharmacyOrderType;
  }

  public void setPharmacyOrderType(ID pharmacyOrderType) {
    this.pharmacyOrderType = pharmacyOrderType;
  }

  @Override
  public HL7Component makeAnother() {
    return new RXA(this);
  }

  public RXA(RXA copy) {
    super(copy);
    init();
  }

  public RXA(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "RXA", "Pharmacy/Treatment Adminsitration Segment", "Pharmacy/Treatment Administration Segment", 26,
        usageType, cardinality);
    init();
  }

  @Override
  public void init() {
    setChild(1, giveSubIDCounter = new NM("Give Sub-ID Counter", UsageType.R, Cardinality.ONE_TIME_ONLY));
    giveSubIDCounter.setLengthMax(4);
    giveSubIDCounter.addConformanceStatement(new ExactValue("IZ-28: RXA-1 (Give Sub-id Counter) SHALL be valued \"0\"",
        "0"));
    setChild(2, administrationSubIDCounter = new NM("Administration Sub-ID Counter", UsageType.R,
        Cardinality.ONE_TIME_ONLY));
    administrationSubIDCounter.setLengthMax(4);
    administrationSubIDCounter.addConformanceStatement(new ExactValue(
        "IZ-29: RXA-2 (admin Sub-id) SHALL be valued \"1\"", "1"));
    setChild(3, dateTimeStartOfAdministration = new TS("Date/Time STart of Administration", UsageType.R,
        Cardinality.ONE_TIME_ONLY));
    setChild(4, dateTimeEndOfAdministration = new TS("Date/Time End of Administration", UsageType.RE,
        Cardinality.ZERO_OR_ONE));
    dateTimeEndOfAdministration.addConformanceStatement(new MatchValue(
        "IZ-30: If RXA-4 (Date time of admin edn) is populated, then it SHALL be the same as Start time (RXA-3)",
        dateTimeStartOfAdministration));
    setChild(5, administeredCode = new CE("Administered Code", UsageType.R, Cardinality.ONE_TIME_ONLY, ValueSet.CVX));
    setChild(6, administeredAmount = new NM("Administered Amount", UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(7, administeredUnits = new CE("Administered Units", UsageType.C, Cardinality.ZERO_OR_ONE, ValueSet.UCUM));
    administeredUnits.setConditionalPredicate(new IsNotValuedAs(administeredAmount, "999", UsageType.R, UsageType.O));
    setChild(8, administeredDosageForm = new CE("Administered Dosage Form", UsageType.O, Cardinality.ZERO_OR_ONE, null));
    setChild(9, administrationNotes = new CE("Administration Notes", UsageType.C, Cardinality.ONE_OR_MORE_TIMES,
        ValueSet.NIP001));
    setChild(10, administeringProvider = new XCN("Administering Provider", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(11, administeredAtLocation = new LA2("Administered-at Location", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(12, administeredPerTimeUnit = new ST("Administered Per (Time Unit)", UsageType.O));
    setChild(13, administeredStrength = new NM("Administered Strength", UsageType.O));
    setChild(14, administeredStrengthUnits = new CE("Administered Strength Units", UsageType.O));
    setChild(15, substanceLotNumber = new ST("Substance Lot Number", UsageType.C, Cardinality.ONE_OR_MORE_TIMES));
    substanceLotNumber.setConditionalPredicate(new IsValuedAs(administrationNotes.getIdentifier(), "00", UsageType.R,
        UsageType.O));
    setChild(16, substanceExpirationDate = new TS("Substance Expiration Date", UsageType.C, Cardinality.ZERO_OR_ONE));
    substanceExpirationDate.setConditionalPredicate(new IsValuedAs(administrationNotes.getIdentifier(), "00",
        UsageType.RE, UsageType.O));
    setChild(17, substanceManufacturerName = new CE("Substance Manufacturer Name", UsageType.C,
        Cardinality.ZERO_OR_MORE, ValueSet.MVX));
    substanceManufacturerName.setConditionalPredicate(new IsValuedAs(administrationNotes.getIdentifier(), "00",
        UsageType.R, UsageType.O));
    setChild(18, substanceTreatmentRefusalReason = new CE("Substance Treatment Refusal Reason", UsageType.C,
        Cardinality.ZERO_OR_MORE, ValueSet.NIP002));
    setChild(19, indication = new CE("Indication", UsageType.O));
    setChild(20,
        completionStatus = new ID("Completion Status", UsageType.RE, Cardinality.ZERO_OR_ONE, ValueSet.HL70322));
    completionStatus.setLengthMax(2);
    substanceTreatmentRefusalReason.setConditionalPredicate(new IsValuedAs(completionStatus, "RE", UsageType.R,
        UsageType.X));
    ExactValue exactValue = new ExactValue(
        "IZ-32 If the RXA-18 (Refusal Reason) is populated, this field shall be valued to \"RE\"", "RE");
    exactValue.setConditionalPredicate(new IsValued(substanceTreatmentRefusalReason));
    completionStatus.addConformanceStatement(exactValue);
    administrationNotes.setConditionalPredicate(new IsValuedAs(completionStatus, new String[] { "CP", "PA" },
        UsageType.R, UsageType.O));
    setChild(21, actionCodeRXA = new ID("Action Code - RXA", UsageType.RE, Cardinality.ZERO_OR_ONE, ValueSet.HL70323));
    actionCodeRXA.setLengthMax(2);
    setChild(22, systemEntryDateTime = new TS("System Entry Date/Time", UsageType.O));
    setChild(23, administeredDrugStrengthVolume = new NM("Administered Drug Strength Volumne", UsageType.O));
    setChild(24, administeredDrugStrengthVolumeUnits = new CWE("Administered Drug Strength Volume Units", UsageType.O));
    setChild(25, administeredBarcodeIdentifier = new CWE("Administered Barcode Identifier", UsageType.O));
    setChild(26, pharmacyOrderType = new ID("Pharmacy Order Type", UsageType.O));

  }
}
