package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.DT;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ID;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.IS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.JCC;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SI;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ST;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XAD;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XON;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XPN;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.XTN;

public class NK1 extends HL7Component
{
  private SI setIDNK1 = null;
  private XPN name = null;
  private CE relationship = null;
  private XAD address = null;
  private XTN phoneNumber = null;
  private XTN businessPhoneNumber = null;
  private CE contactRole = null;
  private DT startDate = null;
  private ST nextOfKinAssociatedPartiesJobTitle = null;
  private JCC nextOfKinAssociatedPartiesJobCodeClass = null;
  private CX nextOfKinAssociatedPartiesEmployeeNumber = null;
  private XON organizationNameNK1 = null;
  private CE maritalStatus = null;
  private IS administrativeSex = null;
  private TS dateTimeOfBirth = null;
  private IS livingDependency = null;
  private IS ambulatoryStatus = null;
  private IS citizenship = null;
  private CE primaryLanguage = null;
  private IS livingArrangment = null;
  private CE publicityCode = null;
  private ID protectionIndicator = null;
  private IS studentIndicator = null;
  private CE religion = null;
  private XPN mothersMaidenName = null;
  private CE nationality = null;
  private CE ethnicGroup = null;
  private CE contactReason = null;
  private XPN contactPersonsName = null;
  private XTN contactPersonsTelephoneNumber = null;
  private XAD contactPersonsAddress = null;
  private CX nextOfKinAssociatedPartysIdentifiers = null;
  private IS jobStatus = null;
  private CE race = null;
  
  
  @Override
  public HL7Component makeAnother() {
    return new NK1(this);
  }
  
  public NK1(NK1 copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // Nothing to init  
  }

  public NK1(UsageType usageType, Cardinality cardinality)
  {
    super("NK1", "Next of Kin Segment", "Next of Kin Segment", 39, usageType, cardinality);
    
    init();
  }
}
