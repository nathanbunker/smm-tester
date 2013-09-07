package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.FT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SI;

public class NTE extends HL7Component
{

  private SI setIDNTE = null;
  private ID sourceOfComment = null;
  private FT comment = null;
  private CE commentType = null;

  @Override
  public HL7Component makeAnother() {
    return new NTE(this);
  }

  public NTE(NTE copy) {
    super(copy);
    init();
  }

  public NTE(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "NTE", "Note Segment", "Note Segment", 4, usageType, cardinality);
    init();
  }

  @Override
  public void init() {
    setChild(1, setIDNTE = new SI("Set ID - NTE", UsageType.O));
    setChild(2, sourceOfComment = new ID("Source of Comment", UsageType.O));
    setChild(3, comment = new FT("Comment", UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(4, commentType = new CE("Comment Type", UsageType.O));
  }

  public SI getSetIDNTE() {
    return setIDNTE;
  }

  public void setSetIDNTE(SI setIDNTE) {
    this.setIDNTE = setIDNTE;
  }

  public ID getSourceOfComment() {
    return sourceOfComment;
  }

  public void setSourceOfComment(ID sourceOfComment) {
    this.sourceOfComment = sourceOfComment;
  }

  public FT getComment() {
    return comment;
  }

  public void setComment(FT comment) {
    this.comment = comment;
  }

  public CE getCommentType() {
    return commentType;
  }

  public void setCommentType(CE commentType) {
    this.commentType = commentType;
  }

}
