package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;

public class BTS extends HL7Component
{
  private ST batchMessageCount = null;
  private ST batchComment = null;
  private NM batchTotals = null;

  public ST getBatchMessageCount() {
    return batchMessageCount;
  }

  public void setBatchMessageCount(ST batchMessageCount) {
    this.batchMessageCount = batchMessageCount;
  }

  public ST getBatchComment() {
    return batchComment;
  }

  public void setBatchComment(ST batchComment) {
    this.batchComment = batchComment;
  }

  public NM getBatchTotals() {
    return batchTotals;
  }

  public void setBatchTotals(NM batchTotals) {
    this.batchTotals = batchTotals;
  }

  @Override
  public HL7Component makeAnother() {
    return new BTS(this);
  }
  
  public BTS(BTS copy)
  {
    super(copy);
    init();
  }

  public BTS(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "BTS", "Batch Trailer Segment", "Batch Trailer Segment", 3, usageType, cardinality);

    init();
  }

  public void init() {
    setChild(1, batchMessageCount = new ST("Batch Message Count", UsageType.O));
    setChild(2, batchComment = new ST("Batch Comment", UsageType.O));
    setChild(3, batchTotals = new NM("Batch Totals", UsageType.O));
  }
}
