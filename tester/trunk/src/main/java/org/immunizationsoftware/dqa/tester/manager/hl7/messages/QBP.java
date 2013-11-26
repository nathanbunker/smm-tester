package org.immunizationsoftware.dqa.tester.manager.hl7.messages;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.DSC;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.MSH;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.QPD;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.RCP;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.SFT;

public class QBP extends HL7Component
{
  
  private MSH msh = null;
  private SFT sft = null;
  private QPD qpd = null;
  private RCP rcp = null;
  private DSC dsc = null;
  
  public MSH getMsh() {
    return msh;
  }

  public void setMsh(MSH msh) {
    this.msh = msh;
  }

  public SFT getSft() {
    return sft;
  }

  public void setSft(SFT sft) {
    this.sft = sft;
  }

  public QPD getQpd() {
    return qpd;
  }

  public void setQpd(QPD qpd) {
    this.qpd = qpd;
  }

  public RCP getRcp() {
    return rcp;
  }

  public void setRcp(RCP rcp) {
    this.rcp = rcp;
  }

  public DSC getDsc() {
    return dsc;
  }

  public void setDsc(DSC dsc) {
    this.dsc = dsc;
  }

  @Override
  public HL7Component makeAnother() {
    return new QBP(this);
  }

  public QBP(QBP copy) {
    super(copy);
    init();
  }
  
  public QBP() {
    super(ItemType.MESSAGE, "QBP", "Query By Parameter", 5, UsageType.R, Cardinality.ONE_TIME_ONLY);
    init();
  }

  @Override
  public void init() {
    setChild(1, msh = new MSH(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(2, sft = new SFT(UsageType.O, Cardinality.ZERO_OR_MORE));
    setChild(3, qpd = new QPD(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(4, rcp = new RCP(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(5, dsc = new DSC(UsageType.X, Cardinality.ZERO_OR_ONE));
  }
  
  
}
