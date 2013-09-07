package org.immunizationsoftware.dqa.tester.manager.hl7.messages;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.ERR;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.MSA;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.SFT;

public class ACK extends HL7Component
{

  private SFT sft = null;
  private MSA msa = null;
  private ERR err = null;
  
  public SFT getSft() {
    return sft;
  }

  public void setSft(SFT sft) {
    this.sft = sft;
  }

  public MSA getMsa() {
    return msa;
  }

  public void setMsa(MSA msa) {
    this.msa = msa;
  }

  public ERR getErr() {
    return err;
  }

  public void setErr(ERR err) {
    this.err = err;
  }

  @Override
  public HL7Component makeAnother() {
    return new ACK(this);
  }
  
  public ACK(ACK copy)
  {
    super(copy);
    init();
  }
  
  public ACK()
  {
    super(ItemType.MESSAGE, "ACK", "Acknowledgment Message", 3, UsageType.R, Cardinality.ONE_TIME_ONLY);
    init();
  }

  @Override
  public void init() {
    setChild(1, sft = new SFT(UsageType.O, Cardinality.ZERO_OR_MORE));
    setChild(2, msa = new MSA(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(3, err = new ERR(UsageType.R, Cardinality.ZERO_OR_MORE));
  }
}
