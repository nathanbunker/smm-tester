package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CNE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CWE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.EI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.EIP;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.PL;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TQ;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XAD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XCN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XON;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XTN;

public class ORC extends HL7Component
{
  private ID orderControl = null;
  private EI placerOrderNumber = null;
  private EI fillerOrderNumber = null;
  private EI placerGroupNumber = null;
  private ID orderStatus = null;
  private ID responseFlag = null;
  private TQ quantityTiming = null;
  private EIP parent = null;
  private TS dateTimeOfTransaction = null;
  private XCN enteredBy = null;
  private XCN verifiedBy = null;
  private XCN orderingProvider = null;
  private PL enterersLocation = null;
  private XTN callBackPhoneNumber = null;
  private TS orderEffectiveDateTime = null;
  private CE orderControlCodeReason = null;
  private CE enteringOrganization = null;
  private CE enteringDevice = null;
  private XCN actionBy = null;
  private CE advancedBeneficiaryNoticeCode = null;
  private XON orderingFacilityName = null;
  private XAD orderingFacilityAddress = null;
  private XTN orderingFacilityPhoneNumber = null;
  private XAD orderingProviderAddress = null;
  private CWE orderStatusModifier = null;
  private CWE advancedBeneficiaryNoticeOverrideReason = null;
  private TS fillersExpectedAvailabilityDateTime = null;
  private CWE confidentialityCode = null;
  private CWE orderType = null;
  private CNE entererAuthorizationMode = null;
  private CWE parentUniversalServiceIdentifier = null;

  public ID getOrderControl() {
    return orderControl;
  }

  public void setOrderControl(ID orderControl) {
    this.orderControl = orderControl;
  }

  public EI getPlacerOrderNumber() {
    return placerOrderNumber;
  }

  public void setPlacerOrderNumber(EI placerOrderNumber) {
    this.placerOrderNumber = placerOrderNumber;
  }

  public EI getFillerOrderNumber() {
    return fillerOrderNumber;
  }

  public void setFillerOrderNumber(EI fillerOrderNumber) {
    this.fillerOrderNumber = fillerOrderNumber;
  }

  public EI getPlacerGroupNumber() {
    return placerGroupNumber;
  }

  public void setPlacerGroupNumber(EI placerGroupNumber) {
    this.placerGroupNumber = placerGroupNumber;
  }

  public ID getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(ID orderStatus) {
    this.orderStatus = orderStatus;
  }

  public ID getResponseFlag() {
    return responseFlag;
  }

  public void setResponseFlag(ID responseFlag) {
    this.responseFlag = responseFlag;
  }

  public TQ getQuantityTiming() {
    return quantityTiming;
  }

  public void setQuantityTiming(TQ quantityTiming) {
    this.quantityTiming = quantityTiming;
  }

  public EIP getParent() {
    return parent;
  }

  public void setParent(EIP parent) {
    this.parent = parent;
  }

  public TS getDateTimeOfTransaction() {
    return dateTimeOfTransaction;
  }

  public void setDateTimeOfTransaction(TS dateTimeOfTransaction) {
    this.dateTimeOfTransaction = dateTimeOfTransaction;
  }

  public XCN getEnteredBy() {
    return enteredBy;
  }

  public void setEnteredBy(XCN enteredBy) {
    this.enteredBy = enteredBy;
  }

  public XCN getVerifiedBy() {
    return verifiedBy;
  }

  public void setVerifiedBy(XCN verifiedBy) {
    this.verifiedBy = verifiedBy;
  }

  public XCN getOrderingProvider() {
    return orderingProvider;
  }

  public void setOrderingProvider(XCN orderingProvider) {
    this.orderingProvider = orderingProvider;
  }

  public PL getEnterersLocation() {
    return enterersLocation;
  }

  public void setEnterersLocation(PL enterersLocation) {
    this.enterersLocation = enterersLocation;
  }

  public XTN getCallBackPhoneNumber() {
    return callBackPhoneNumber;
  }

  public void setCallBackPhoneNumber(XTN callBackPhoneNumber) {
    this.callBackPhoneNumber = callBackPhoneNumber;
  }

  public TS getOrderEffectiveDateTime() {
    return orderEffectiveDateTime;
  }

  public void setOrderEffectiveDateTime(TS orderEffectiveDateTime) {
    this.orderEffectiveDateTime = orderEffectiveDateTime;
  }

  public CE getOrderControlCodeReason() {
    return orderControlCodeReason;
  }

  public void setOrderControlCodeReason(CE orderControlCodeReason) {
    this.orderControlCodeReason = orderControlCodeReason;
  }

  public CE getEnteringOrganization() {
    return enteringOrganization;
  }

  public void setEnteringOrganization(CE enteringOrganization) {
    this.enteringOrganization = enteringOrganization;
  }

  public CE getEnteringDevice() {
    return enteringDevice;
  }

  public void setEnteringDevice(CE enteringDevice) {
    this.enteringDevice = enteringDevice;
  }

  public XCN getActionBy() {
    return actionBy;
  }

  public void setActionBy(XCN actionBy) {
    this.actionBy = actionBy;
  }

  public CE getAdvancedBeneficiaryNoticeCode() {
    return advancedBeneficiaryNoticeCode;
  }

  public void setAdvancedBeneficiaryNoticeCode(CE advancedBeneficiaryNoticeCode) {
    this.advancedBeneficiaryNoticeCode = advancedBeneficiaryNoticeCode;
  }

  public XON getOrderingFacilityName() {
    return orderingFacilityName;
  }

  public void setOrderingFacilityName(XON orderingFacilityName) {
    this.orderingFacilityName = orderingFacilityName;
  }

  public XAD getOrderingFacilityAddress() {
    return orderingFacilityAddress;
  }

  public void setOrderingFacilityAddress(XAD orderingFacilityAddress) {
    this.orderingFacilityAddress = orderingFacilityAddress;
  }

  public XTN getOrderingFacilityPhoneNumber() {
    return orderingFacilityPhoneNumber;
  }

  public void setOrderingFacilityPhoneNumber(XTN orderingFacilityPhoneNumber) {
    this.orderingFacilityPhoneNumber = orderingFacilityPhoneNumber;
  }

  public XAD getOrderingProviderAddress() {
    return orderingProviderAddress;
  }

  public void setOrderingProviderAddress(XAD orderingProviderAddress) {
    this.orderingProviderAddress = orderingProviderAddress;
  }

  public CWE getOrderStatusModifier() {
    return orderStatusModifier;
  }

  public void setOrderStatusModifier(CWE orderStatusModifier) {
    this.orderStatusModifier = orderStatusModifier;
  }

  public CWE getAdvancedBeneficiaryNoticeOverrideReason() {
    return advancedBeneficiaryNoticeOverrideReason;
  }

  public void setAdvancedBeneficiaryNoticeOverrideReason(CWE advancedBeneficiaryNoticeOverrideReason) {
    this.advancedBeneficiaryNoticeOverrideReason = advancedBeneficiaryNoticeOverrideReason;
  }

  public TS getFillersExpectedAvailabilityDateTime() {
    return fillersExpectedAvailabilityDateTime;
  }

  public void setFillersExpectedAvailabilityDateTime(TS fillersExpectedAvailabilityDateTime) {
    this.fillersExpectedAvailabilityDateTime = fillersExpectedAvailabilityDateTime;
  }

  public CWE getConfidentialityCode() {
    return confidentialityCode;
  }

  public void setConfidentialityCode(CWE confidentialityCode) {
    this.confidentialityCode = confidentialityCode;
  }

  public CWE getOrderType() {
    return orderType;
  }

  public void setOrderType(CWE orderType) {
    this.orderType = orderType;
  }

  public CNE getEntererAuthorizationMode() {
    return entererAuthorizationMode;
  }

  public void setEntererAuthorizationMode(CNE entererAuthorizationMode) {
    this.entererAuthorizationMode = entererAuthorizationMode;
  }

  public CWE getParentUniversalServiceIdentifier() {
    return parentUniversalServiceIdentifier;
  }

  public void setParentUniversalServiceIdentifier(CWE parentUniversalServiceIdentifier) {
    this.parentUniversalServiceIdentifier = parentUniversalServiceIdentifier;
  }

  @Override
  public HL7Component makeAnother() {
    return new ORC(this);
  }

  public ORC(ORC copy) {
    super(copy);
    init();
  }

  public ORC(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "ORC", "Order Request Segment", "Order Request Segment", 31, usageType, cardinality);
    init();
  }

  @Override
  public void init() {
    setChild(1, orderControl = new ID("Order Control", UsageType.R, Cardinality.ONE_TIME_ONLY, ValueSet.HL70119));
    orderControl.addConformanceStatement(new ExactValue("IZ-25: ORC.1 (Order Control) SHALL contain the value \"RE\"", "RE"));
    setChild(2, placerOrderNumber = new EI("Placer Order Number", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(3, fillerOrderNumber = new EI("Filler Order Number", UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(4, placerGroupNumber = new EI("Placer Group NUmber", UsageType.O));
    setChild(5, orderStatus = new ID("Order Status", UsageType.O));
    setChild(6, responseFlag = new ID("Response Flag", UsageType.O));
    setChild(7, quantityTiming = new TQ("Quantity/Timing", UsageType.X));
    setChild(8, parent = new EIP("Parent", UsageType.O));
    setChild(9, dateTimeOfTransaction = new TS("Date/Time of Transaction", UsageType.O));
    setChild(10, enteredBy = new XCN("Entered By", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(11, verifiedBy = new XCN("Verified By", UsageType.O));
    setChild(12, orderingProvider = new XCN("Ordering Provider", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(13, enterersLocation = new PL("Enterer's Location", UsageType.O));
    setChild(14, callBackPhoneNumber = new XTN("Call Back Phone Number", UsageType.O));
    setChild(15, orderEffectiveDateTime = new TS("Order Effective Date/Time", UsageType.O));
    setChild(16, orderControlCodeReason = new CE("Order Control Code Reason", UsageType.O));
    setChild(17, enteringOrganization = new CE("Entering Organization", UsageType.O));
    setChild(18, enteringDevice = new CE("Entering Device", UsageType.O));
    setChild(19, actionBy = new XCN("Action By", UsageType.O));
    setChild(20, advancedBeneficiaryNoticeCode = new CE("Advanced Beneficiary Notice Code", UsageType.O));
    setChild(21, orderingFacilityName = new XON("Ordering Facility Name", UsageType.O));
    setChild(22, orderingFacilityAddress = new XAD("Ordering Facility Address", UsageType.O));
    setChild(23, orderingFacilityPhoneNumber = new XTN("Ordering Facility Phone Number", UsageType.O));
    setChild(24, orderingProviderAddress = new XAD("Ordering Provider Address", UsageType.O));
    setChild(25, orderStatusModifier = new CWE("Order Status Modifier", UsageType.O));
    setChild(26, advancedBeneficiaryNoticeOverrideReason = new CWE("Advanced Beneficiary Notice Override Reason",
        UsageType.O));
    setChild(27, fillersExpectedAvailabilityDateTime = new TS("Filler's Expected Availability Date/Time", UsageType.O));
    setChild(28, confidentialityCode = new CWE("Confidentiality Code", UsageType.O));
    setChild(29, orderType = new CWE("Order Type", UsageType.O));
    setChild(30, entererAuthorizationMode = new CNE("Enterer Authorization Mode", UsageType.O));
    setChild(31, parentUniversalServiceIdentifier = new CWE("Parent Universal Service Identifier", UsageType.O));

  }

}
