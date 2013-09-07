package org.immunizationsoftware.dqa.tester.manager.hl7.messages;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.GT1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.IN1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.IN2;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.IN3;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.MSH;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.NK1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.NTE;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.OBX;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.ORC;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PD1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PID;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PV1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PV2;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.RXA;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.RXR;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.SFT;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.TQ1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.TQ2;

public class VXU extends HL7Component
{
  private MSH msh = null;
  private SFT sft = null;
  private PID pid = null;
  private PD1 pd1 = null;
  private NK1 nk1 = null;
  private PV1 pv1 = null;
  private PV2 pv2 = null;
  private GT1 gt1 = null;
  private InsuranceGroup insuranceGroup = null;
  private OrderGroup orderGroup = null;

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

  public PID getPid() {
    return pid;
  }

  public void setPid(PID pid) {
    this.pid = pid;
  }

  public PD1 getPd1() {
    return pd1;
  }

  public void setPd1(PD1 pd1) {
    this.pd1 = pd1;
  }

  public NK1 getNk1() {
    return nk1;
  }

  public void setNk1(NK1 nk1) {
    this.nk1 = nk1;
  }

  public PV1 getPv1() {
    return pv1;
  }

  public void setPv1(PV1 pv1) {
    this.pv1 = pv1;
  }

  public PV2 getPv2() {
    return pv2;
  }

  public void setPv2(PV2 pv2) {
    this.pv2 = pv2;
  }

  public GT1 getGt1() {
    return gt1;
  }

  public void setGt1(GT1 gt1) {
    this.gt1 = gt1;
  }

  public InsuranceGroup getInsuranceGroup() {
    return insuranceGroup;
  }

  public void setInsuranceGroup(InsuranceGroup insuranceGroup) {
    this.insuranceGroup = insuranceGroup;
  }

  public OrderGroup getOrderGroup() {
    return orderGroup;
  }

  public void setOrderGroup(OrderGroup orderGroup) {
    this.orderGroup = orderGroup;
  }
  
  @Override
  public HL7Component makeAnother() {
    return new VXU(this);
  }
  
  public VXU(VXU copy)
  {
    super(copy);
    init();
  }



  public VXU() {
    super(ItemType.MESSAGE, "VXU", "Send Immunization History", 10, UsageType.R, Cardinality.ONE_TIME_ONLY);
    init();
  }

  public void init() {
    setChild(1, msh = new MSH(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(2, sft = new SFT(UsageType.O, Cardinality.ZERO_OR_MORE));
    setChild(3, pid = new PID(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(4, pd1 = new PD1(UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(5, nk1 = new NK1(UsageType.RE, Cardinality.ZERO_OR_MORE));
    setChild(6, pv1 = new PV1(UsageType.O, Cardinality.ZERO_OR_ONE));
    setChild(7, pv2 = new PV2(UsageType.O, Cardinality.ZERO_OR_ONE));
    setChild(8, gt1 = new GT1(UsageType.O, Cardinality.ZERO_OR_MORE));
    setChild(9, insuranceGroup = new InsuranceGroup(UsageType.O, Cardinality.ZERO_OR_MORE));
    setChild(10, orderGroup = new OrderGroup(UsageType.O, Cardinality.ZERO_OR_MORE));
  }

  public class InsuranceGroup extends HL7Component
  {
    private IN1 in1 = null;
    private IN2 in2 = null;
    private IN3 in3 = null;

    public IN1 getIn1() {
      return in1;
    }

    public void setIn1(IN1 in1) {
      this.in1 = in1;
    }

    public IN2 getIn2() {
      return in2;
    }

    public void setIn2(IN2 in2) {
      this.in2 = in2;
    }

    public IN3 getIn3() {
      return in3;
    }

    public void setIn3(IN3 in3) {
      this.in3 = in3;
    }

    @Override
    public HL7Component makeAnother() {
      return new InsuranceGroup(this);
    }
    
    public InsuranceGroup(InsuranceGroup copy)
    {
      super(copy);
      init();
    }

    public InsuranceGroup(UsageType usageType, Cardinality cardinality) {
      super(ItemType.MESSAGE, "IN1", "PID - Insurance Group", 3, usageType, cardinality);
      init();
    }

    public void init() {
      setChild(1, in1 = new IN1(UsageType.O, Cardinality.ZERO_OR_ONE));
      setChild(2, in2 = new IN2(UsageType.O, Cardinality.ZERO_OR_ONE));
      setChild(3, in3 = new IN3(UsageType.O, Cardinality.ZERO_OR_ONE));
    }

  }

  public class OrderGroup extends HL7Component
  {
    private ORC orc = null;
    private TQ1 tq1 = null;
    private TQ2 tq2 = null;
    private RXA rxa = null;
    private RXR rxr = null;
    private ObservationGroup observationGroup = null;

    public ORC getOrc() {
      return orc;
    }

    public void setOrc(ORC orc) {
      this.orc = orc;
    }

    public TQ1 getTq1() {
      return tq1;
    }

    public void setTq1(TQ1 tq1) {
      this.tq1 = tq1;
    }

    public TQ2 getTq2() {
      return tq2;
    }

    public void setTq2(TQ2 tq2) {
      this.tq2 = tq2;
    }

    public RXA getRxa() {
      return rxa;
    }

    public void setRxa(RXA rxa) {
      this.rxa = rxa;
    }

    public RXR getRxr() {
      return rxr;
    }

    public void setRxr(RXR rxr) {
      this.rxr = rxr;
    }

    public ObservationGroup getObservationGroup() {
      return observationGroup;
    }

    public void setObservationGroup(ObservationGroup observationGroup) {
      this.observationGroup = observationGroup;
    }

    @Override
    public HL7Component makeAnother() {
      return new OrderGroup(this);
    }
    
    public OrderGroup(OrderGroup copy)
    {
      super(copy);
      init();
    }

    public OrderGroup(UsageType usageType, Cardinality cardinality) {
      super(ItemType.MESSAGE_PART, "ORC", "PID - Order Group", 6, usageType, cardinality);
      init();
    }

    public void init() {
      setChild(1, orc = new ORC(UsageType.R, Cardinality.ONE_OR_MORE_TIMES));
      setChild(2, tq1 = new TQ1(UsageType.O, Cardinality.ZERO_OR_ONE));
      setChild(3, tq2 = new TQ2(UsageType.O, Cardinality.ZERO_OR_ONE));
      setChild(4, rxa = new RXA(UsageType.R, Cardinality.ONE_TIME_ONLY));
      setChild(5, rxr = new RXR(UsageType.RE, Cardinality.ZERO_OR_ONE));
      setChild(6, observationGroup = new ObservationGroup(UsageType.RE, Cardinality.ZERO_OR_MORE));
    }

    public class ObservationGroup extends HL7Component
    {
      private OBX obx = null;
      private NTE nte = null;

      public OBX getObx() {
        return obx;
      }

      public void setObx(OBX obx) {
        this.obx = obx;
      }

      public NTE getNte() {
        return nte;
      }

      public void setNte(NTE nte) {
        this.nte = nte;
      }

      @Override
      public HL7Component makeAnother() {
        return new ObservationGroup(this);
      }
      
      public ObservationGroup(ObservationGroup copy)
      {
        super(copy);
        init();
      }

      public ObservationGroup(UsageType usageType, Cardinality cardinality) {
        super(ItemType.MESSAGE_PART, "OBX", "PID - Order Group - Observation Group", 2, usageType, cardinality);
        init();
      }

      public void init() {
        setChild(1, obx = new OBX(UsageType.R, Cardinality.ONE_TIME_ONLY));
        setChild(2, nte = new NTE(UsageType.RE, Cardinality.ZERO_OR_MORE));
      }
    }
  }
}
