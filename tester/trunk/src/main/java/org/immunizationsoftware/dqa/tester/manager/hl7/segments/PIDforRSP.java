package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.DatePrecision;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CWE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DLN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.HD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.NM;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XAD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XPN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XPN_maiden;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XTN;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValuedAs;

public class PIDforRSP extends PID
{

  @Override
  public HL7Component makeAnother() {
    return new PIDforRSP(this);
  }

  public PIDforRSP(PIDforRSP copy) {
    super(copy);
  }

  public PIDforRSP(UsageType usageType, Cardinality cardinality) {
    super(usageType, cardinality);
  }

  public void init() {
    super.init();
    mothersMaidenName.setUsageType(UsageType.O);
    race.setUsageType(UsageType.O);
    patientAddress.setUsageType(UsageType.O);
    phoneNumberHome.setUsageType(UsageType.O);
    ethnicGroup.setUsageType(UsageType.O);
    multipleBirthIndicator.setUsageType(UsageType.O);
    birthOrder.setUsageType(UsageType.O);
    patientDeathDateAndTime.setUsageType(UsageType.O);
    patientDeathIndicator.setUsageType(UsageType.O);
    mothersMaidenName.setUsageType(UsageType.O);
    mothersMaidenName.setUsageType(UsageType.O);
  }
}
