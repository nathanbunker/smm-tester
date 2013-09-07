package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;

public class QAK extends HL7Component
{

  private ST queryTag = null;
  private ID queryResponseStatus = null;
  private CE messageQueryName = null;
  private NM hitCount = null;
  private NM thisPayload = null;
  private NM hitsRemaining = null;

  public ST getQueryTag() {
    return queryTag;
  }

  public void setQueryTag(ST queryTag) {
    this.queryTag = queryTag;
  }

  public ID getQueryResponseStatus() {
    return queryResponseStatus;
  }

  public void setQueryResponseStatus(ID queryResponseStatus) {
    this.queryResponseStatus = queryResponseStatus;
  }

  public CE getMessageQueryName() {
    return messageQueryName;
  }

  public void setMessageQueryName(CE messageQueryName) {
    this.messageQueryName = messageQueryName;
  }

  public NM getHitCount() {
    return hitCount;
  }

  public void setHitCount(NM hitCount) {
    this.hitCount = hitCount;
  }

  public NM getThisPayload() {
    return thisPayload;
  }

  public void setThisPayload(NM thisPayload) {
    this.thisPayload = thisPayload;
  }

  public NM getHitsRemaining() {
    return hitsRemaining;
  }

  public void setHitsRemaining(NM hitsRemaining) {
    this.hitsRemaining = hitsRemaining;
  }

  @Override
  public HL7Component makeAnother() {
    return new QAK(this);
  }

  public QAK(QAK copy) {
    super(copy);
    init();
  }

  public QAK(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "QAK", "Query Acknowledgement Segment", "Query Acknowledgement Segment", 6, usageType, cardinality);
    init();
  }

  @Override
  public void init() {
    setChild(1, queryTag = new ST("Query Tag", UsageType.R, Cardinality.ONE_TIME_ONLY, 32));
    setChild(2, queryResponseStatus = new ID("Query Response Status", UsageType.RE, Cardinality.ZERO_OR_ONE,
        ValueSet.HL70208));
    setChild(3, messageQueryName = new CE("Message Query Name", UsageType.R, Cardinality.ONE_TIME_ONLY, null));
    setChild(4, hitCount = new NM("Hit Count", UsageType.O, Cardinality.ZERO_OR_ONE));
    setChild(5, thisPayload = new NM("This payload", UsageType.O, Cardinality.ZERO_OR_ONE));
    setChild(6, hitsRemaining = new NM("Hits remaining", UsageType.O, Cardinality.ZERO_OR_ONE));
  }

}
