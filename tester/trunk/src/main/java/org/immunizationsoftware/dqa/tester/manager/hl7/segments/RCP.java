package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CQ;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SRT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;

public class RCP extends HL7Component
{

  private ID queryPriority = null;
  private CQ quantityLimitedRequest = null;
  private CE responseModality = null;
  private TS executionAndDeliveryTime = null;
  private ID modifyIndicator = null;
  private SRT sortByField = null;
  private ID segmentGroupInclusion = null;

  public ID getQueryPriority() {
    return queryPriority;
  }

  public void setQueryPriority(ID queryPriority) {
    this.queryPriority = queryPriority;
  }

  public CQ getQuantityLimitedRequest() {
    return quantityLimitedRequest;
  }

  public void setQuantityLimitedRequest(CQ quantityLimitedRequest) {
    this.quantityLimitedRequest = quantityLimitedRequest;
  }

  public CE getResponseModality() {
    return responseModality;
  }

  public void setResponseModality(CE responseModality) {
    this.responseModality = responseModality;
  }

  public TS getExecutionAndDeliveryTime() {
    return executionAndDeliveryTime;
  }

  public void setExecutionAndDeliveryTime(TS executionAndDeliveryTime) {
    this.executionAndDeliveryTime = executionAndDeliveryTime;
  }

  public ID getModifyIndicator() {
    return modifyIndicator;
  }

  public void setModifyIndicator(ID modifyIndicator) {
    this.modifyIndicator = modifyIndicator;
  }

  public SRT getSortByField() {
    return sortByField;
  }

  public void setSortByField(SRT sortByField) {
    this.sortByField = sortByField;
  }

  public ID getSegmentGroupInclusion() {
    return segmentGroupInclusion;
  }

  public void setSegmentGroupInclusion(ID segmentGroupInclusion) {
    this.segmentGroupInclusion = segmentGroupInclusion;
  }

  @Override
  public HL7Component makeAnother() {
    return new RCP(this);
  }

  public RCP(RCP copy) {
    super(copy);
    init();
  }

  public RCP(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "RCP", "Response Control Parameter Segment", "Response Control Parameter Segment", 7, usageType, cardinality);
    init();
  }

  @Override
  public void init() {
    setChild(1, queryPriority = new ID("Query Priority", UsageType.RE, Cardinality.ZERO_OR_ONE, ValueSet.HL70091));
    queryPriority.addConformanceStatement(new ExactValue("IZ-27: Constrain RCP-1 (Query Priority) to empty or \"I\". Immediate priority is expected.", "I"));
    setChild(2, quantityLimitedRequest = new CQ("Quantity Limited Request", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(3, responseModality = new CE("Response Modality", UsageType.O));
    setChild(4, executionAndDeliveryTime = new TS("Execution and Delivery Time", UsageType.O));
    setChild(5, modifyIndicator = new ID("Modify Indicator", UsageType.O));
    setChild(6, sortByField = new SRT("Sort-by Field", UsageType.O));
    setChild(7, segmentGroupInclusion = new ID("Segment group inclusion", UsageType.O));

  }

}
