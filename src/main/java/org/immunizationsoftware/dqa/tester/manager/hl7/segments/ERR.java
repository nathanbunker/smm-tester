package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CWE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ELD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ERL;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XTN;

public class ERR extends HL7Component
{
  public static String SEVERITY_ERROR = "E";
  public static String SEVERITY_WARNING = "W";
  public static String SEVERITY_INFORMATION = "I";
  public static String SEVERITY_NONE = "N";
  public static String SEVERITY_NOT_SUPPORTED = "X";

  private ELD errorCodeAndLocation = null;
  private ERL errorLocation = null;
  private CWE hl7ErrorCode = null;
  private ID severity = null;
  private CWE applicationErrorCode = null;
  private ST applicationErrorParameter = null;
  private TX diagnosticInformation = null;
  private TX userMessage = null;
  private IS informPersonIndicator = null;
  private CWE overrideType = null;
  private CWE overrideReasonCode = null;
  private XTN helpDeskContactPoint = null;

  public ELD getErrorCodeAndLocation() {
    return errorCodeAndLocation;
  }

  public void setErrorCodeAndLocation(ELD errorCodeAndLocation) {
    this.errorCodeAndLocation = errorCodeAndLocation;
  }

  public ERL getErrorLocation() {
    return errorLocation;
  }

  public void setErrorLocation(ERL errorLocation) {
    this.errorLocation = errorLocation;
  }

  public CWE getHl7ErrorCode() {
    return hl7ErrorCode;
  }

  public void setHl7ErrorCode(CWE hl7ErrorCode) {
    this.hl7ErrorCode = hl7ErrorCode;
  }

  public ID getSeverity() {
    return severity;
  }

  public void setSeverity(ID severity) {
    this.severity = severity;
  }

  public CWE getApplicationErrorCode() {
    return applicationErrorCode;
  }

  public void setApplicationErrorCode(CWE applicationErrorCode) {
    this.applicationErrorCode = applicationErrorCode;
  }

  public ST getApplicationErrorParameter() {
    return applicationErrorParameter;
  }

  public void setApplicationErrorParameter(ST applicationErrorParameter) {
    this.applicationErrorParameter = applicationErrorParameter;
  }

  public TX getDiagnosticInformation() {
    return diagnosticInformation;
  }

  public void setDiagnosticInformation(TX diagnosticInformation) {
    this.diagnosticInformation = diagnosticInformation;
  }

  public TX getUserMessage() {
    return userMessage;
  }

  public void setUserMessage(TX userMessage) {
    this.userMessage = userMessage;
  }

  public IS getInformPersonIndicator() {
    return informPersonIndicator;
  }

  public void setInformPersonIndicator(IS informPersonIndicator) {
    this.informPersonIndicator = informPersonIndicator;
  }

  public CWE getOverrideType() {
    return overrideType;
  }

  public void setOverrideType(CWE overrideType) {
    this.overrideType = overrideType;
  }

  public CWE getOverrideReasonCode() {
    return overrideReasonCode;
  }

  public void setOverrideReasonCode(CWE overrideReasonCode) {
    this.overrideReasonCode = overrideReasonCode;
  }

  public XTN getHelpDeskContactPoint() {
    return helpDeskContactPoint;
  }

  public void setHelpDeskContactPoint(XTN helpDeskContactPoint) {
    this.helpDeskContactPoint = helpDeskContactPoint;
  }

  @Override
  public HL7Component makeAnother() {
    return new ERR(this);
  }

  public ERR(ERR copy) {
    super(copy);
    init();
  }

  public ERR(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "ERR", "Error Segment", "Error Segment", 12, usageType, cardinality);

    init();
  }

  public ERR() {
    super(ItemType.SEGMENT, "ERR", "Error Segment", "Error Segment", 12);
    init();
  }

  public void init() {
    setChild(1, errorCodeAndLocation = new ELD("Error Code and Location", UsageType.X));
    setChild(2, errorLocation = new ERL("Error Location", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(3, hl7ErrorCode = new CWE("HL7 Error Code", UsageType.R, Cardinality.ONE_TIME_ONLY, ValueSet.HL70357));
    setChild(4, severity = new ID("Severity", UsageType.R, Cardinality.ONE_TIME_ONLY, ValueSet.HL70516));
    setChild(5, applicationErrorCode = new CWE("Application Error Code", UsageType.O));
    setChild(6, applicationErrorParameter = new ST("Application Error Parameter", UsageType.O));
    setChild(7, diagnosticInformation = new TX("Diagnostic Information", UsageType.O));
    setChild(8, userMessage = new TX("User Message", UsageType.O));
    setChild(9, informPersonIndicator = new IS("Inform Person Indicator", UsageType.O));
    setChild(10, overrideType = new CWE("Overrride Type", UsageType.O));
    setChild(11, overrideReasonCode = new CWE("Override Reason Code", UsageType.O));
    setChild(12, helpDeskContactPoint = new XTN("Help Desk Contact Point", UsageType.O));
  }
}
