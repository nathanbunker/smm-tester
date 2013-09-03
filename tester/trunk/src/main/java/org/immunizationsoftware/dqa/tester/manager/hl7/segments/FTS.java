package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;

public class FTS extends HL7Component
{
  private NM fileBatchCount = null;
  private ST fileTrailerComment = null;

  public NM getFileBatchCount() {
    return fileBatchCount;
  }

  public void setFileBatchCount(NM fileMessageCount) {
    this.fileBatchCount = fileMessageCount;
  }

  public ST getFileTrailerComment() {
    return fileTrailerComment;
  }

  public void setFileTrailerComment(ST fileComment) {
    this.fileTrailerComment = fileComment;
  }

  @Override
  public HL7Component makeAnother() {
    return new FTS(this);
  }

  public FTS(FTS copy) {
    super(copy);
    init();
  }

  public FTS(UsageType usageType, Cardinality cardinality) {
    super("FTS", "File Trailer Segment", "File Trailer Segment", 2, usageType, cardinality);

    init();
  }

  public void init() {
    setChild(1, fileBatchCount = new NM("File Batch Count", UsageType.O));
    setChild(2, fileTrailerComment = new ST("File Trailer Comment", UsageType.O));
  }
}
