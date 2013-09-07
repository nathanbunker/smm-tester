package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.HD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XCN;

public class EVN extends HL7Component
{
  
  private ID eventTypeCode = null;
  private TS recordedDateTime = null;
  private TS dateTimePlannedEvent = null;
  private IS eventReasonCode = null;
  private XCN operatorID = null;
  private TS eventOccurred = null;
  private HD eventFacility = null;
  
  public ID getEventTypeCode() {
    return eventTypeCode;
  }

  public void setEventTypeCode(ID eventTypeCode) {
    this.eventTypeCode = eventTypeCode;
  }

  public TS getRecordedDateTime() {
    return recordedDateTime;
  }

  public void setRecordedDateTime(TS recordedDateTime) {
    this.recordedDateTime = recordedDateTime;
  }

  public TS getDateTimePlannedEvent() {
    return dateTimePlannedEvent;
  }

  public void setDateTimePlannedEvent(TS dateTimePlannedEvent) {
    this.dateTimePlannedEvent = dateTimePlannedEvent;
  }

  public IS getEventReasonCode() {
    return eventReasonCode;
  }

  public void setEventReasonCode(IS eventReasonCode) {
    this.eventReasonCode = eventReasonCode;
  }

  public XCN getOperatorID() {
    return operatorID;
  }

  public void setOperatorID(XCN operatorID) {
    this.operatorID = operatorID;
  }

  public TS getEventOccurred() {
    return eventOccurred;
  }

  public void setEventOccurred(TS eventOccurred) {
    this.eventOccurred = eventOccurred;
  }

  public HD getEventFacility() {
    return eventFacility;
  }

  public void setEventFacility(HD eventFacility) {
    this.eventFacility = eventFacility;
  }

  @Override
  public HL7Component makeAnother() {
    return new EVN(this);
  }
  
  public EVN(EVN copy)
  {
    super(copy);
    init();
  }

  public EVN(UsageType usageType, Cardinality cardinality)
  {
    super(ItemType.SEGMENT, "EVN", "Event Type Segment", "Event Type Segment", 7, usageType, cardinality);
    
    init();
  }

  public void init() {
    setChild(1, eventTypeCode = new ID("Event Type Code", UsageType.O));
    setChild(2, recordedDateTime = new TS("Recorded Date/Time", UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(3, dateTimePlannedEvent = new TS("Date/Time Planned Event", UsageType.O));
    setChild(4, eventReasonCode = new IS("Event Reason Code", UsageType.O));
    setChild(5, operatorID = new XCN("Operator ID", UsageType.O));
    setChild(6, eventOccurred = new TS("Event Occurred", UsageType.O));
    setChild(7, eventFacility = new HD("Event Facility", UsageType.O));
  }
}
