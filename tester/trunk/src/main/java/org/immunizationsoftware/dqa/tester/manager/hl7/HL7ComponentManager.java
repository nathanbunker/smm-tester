package org.immunizationsoftware.dqa.tester.manager.hl7;

import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE_TX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CNE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CQ;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CWE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DLN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DR;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DTM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.EI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.EIP;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ELD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ERL;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.FN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.FT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.HD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.JCC;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.LA2;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.MSG;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.PL;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.PT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SAD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SRT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TQ;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.VID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XAD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XCN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XON;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XPN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XTN;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.ACK;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.RSP;
import org.immunizationsoftware.dqa.tester.manager.hl7.messages.VXU;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.BHS;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.BTS;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.ERR;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.EVN;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.FHS;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.FTS;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.GT1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.IN1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.IN2;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.IN3;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.MSA;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.MSH;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.NK1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.NTE;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.OBX;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.ORC;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PD1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PID;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PV1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PV2;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.QAK;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.QPD;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.RCP;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.RXA;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.RXR;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.SFT;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.TQ1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.TQ2;

public class HL7ComponentManager
{
  private static List<HL7Component> componentList = null;

  public static List<HL7Component> getComponentList() {
    init();
    return componentList;
  }
  
  public static HL7Component getComponent(String componentCode)
  {
    init();
    for (HL7Component hl7Component : componentList)
    {
      if (hl7Component.getComponentCode().equals(componentCode))
      {
        return hl7Component;
      }
    }
    return null;
  }
  
  public static int getComponentPosition(HL7Component hl7Component)
  {
    init();
    for (int i = 0; i < componentList.size(); i++)
    {
      if (hl7Component == componentList.get(i))
      {
        return i;
      }
    }
    return 0;
  }
  

  public static void init() {
    if (componentList == null) {
      componentList = new ArrayList<HL7Component>();
      // Messages
      componentList.add(new ACK());
      componentList.add(new RSP());
      // componentList.add(new RSP(true));
      componentList.add(new VXU());
      // Segments
      componentList.add(new BHS(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new BTS(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new ERR(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new EVN(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new FHS(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new FTS(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new GT1(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new IN1(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new IN2(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new IN3(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new MSA(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new MSH(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new NK1(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new NTE(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new OBX(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new ORC(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new PD1(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new PID(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new PV1(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new PV2(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new QAK(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new QPD(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new RCP(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new RXA(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new RXR(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new SFT(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new TQ1(UsageType.R, Cardinality.ONE_TIME_ONLY));
      componentList.add(new TQ2(UsageType.R, Cardinality.ONE_TIME_ONLY));
      // Data Types
      componentList.add(new CE_TX("", UsageType.R));
      componentList.add(new CE("", UsageType.R));
      componentList.add(new CNE("", UsageType.R));
      componentList.add(new CQ("", UsageType.R));
      componentList.add(new CWE("", UsageType.R));
      componentList.add(new CX("", UsageType.R));
      componentList.add(new DLN("", UsageType.R));
      componentList.add(new DR("", UsageType.R));
      componentList.add(new DT("", UsageType.R));
      componentList.add(new DTM("", UsageType.R));
      componentList.add(new EI("", UsageType.R));
      componentList.add(new EIP("", UsageType.R));
      componentList.add(new ELD("", UsageType.R));
      componentList.add(new ERL("", UsageType.R));
      componentList.add(new FN("", UsageType.R));
      componentList.add(new FT("", UsageType.R));
      componentList.add(new HD("", UsageType.R));
      componentList.add(new ID("", UsageType.R));
      componentList.add(new IS("", UsageType.R));
      componentList.add(new JCC("", UsageType.R));
      componentList.add(new LA2("", UsageType.R));
      componentList.add(new MSG("", UsageType.R));
      componentList.add(new NM("", UsageType.R));
      componentList.add(new PL("", UsageType.R));
      componentList.add(new PT("", UsageType.R));
      componentList.add(new SAD("", UsageType.R));
      componentList.add(new SI("", UsageType.R));
      componentList.add(new SRT("", UsageType.R));
      componentList.add(new ST("", UsageType.R));
      componentList.add(new TQ("", UsageType.R));
      componentList.add(new TS("", UsageType.R));
      componentList.add(new TX("", UsageType.R));
      componentList.add(new VID("", UsageType.R));
      componentList.add(new XAD("", UsageType.R));
      componentList.add(new XCN("", UsageType.R));
      componentList.add(new XON("", UsageType.R));
      componentList.add(new XPN("", UsageType.R));
      componentList.add(new XTN("", UsageType.R));
    }
  }
}
