package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CX;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.SI;

public class PID extends HL7Component
{

  private HL7Component setID;
  private HL7Component patientID;
  private HL7Component patientIdentiferList;
  private HL7Component alternatePatientID;
  private HL7Component patientName;
  private HL7Component mothersMaidenName;
  private HL7Component dateTimeOfBirth;
  private HL7Component administrativeSex;
  private HL7Component patientAlias;
  private HL7Component race;
  private HL7Component patientAddress;
  private HL7Component countyCode;
  private HL7Component phoneNumberHome;
  private HL7Component PhoneNumberBusiness;
  private HL7Component primaryLanguage;
  private HL7Component maritalStatus;
  private HL7Component religion;
  private HL7Component patientAccountNumber;
  private HL7Component ssnNumberPatient;
  private HL7Component driversLicenseNumberPatient;
  private HL7Component mothersIdentifer;
  private HL7Component ethnicGroup;
  private HL7Component birthPlace;
  private HL7Component multipleBirthIndicator;
  private HL7Component birthOrder;
  private HL7Component citizenship;
  private HL7Component veteransMilitaryStatus;
  private HL7Component nationality;
  private HL7Component patientDeathDateAndTime;
  private HL7Component patientDeathIndicator;
  private HL7Component identifyUnknownIndicator;
  private HL7Component identifyReliabilityCode;
  private HL7Component lastUpdateDateTime;
  private HL7Component lastUpdateFacility;
  private HL7Component speciesCode;
  private HL7Component breedCode;
  private HL7Component strain;
  private HL7Component productionClassCode;
  private HL7Component tribalcitizenship;

  @Override
  public HL7Component makeAnother() {
    return new PID(this);
  }
  
  public PID(PID copy)
  {
    super(copy);
    init();
  }

  public PID(UsageType usageType, Cardinality cardinality) {
    super("PID", "Patient Identifier Segment", "Patient Identifier Segment", 39, usageType, cardinality);

    init();
  }

  public void init() {
    setChild(1, new SI("Set ID - PID", UsageType.RE, Cardinality.ZERO_OR_ONE));
    setChild(2, new CX("Patient ID", UsageType.X, null));
  }
}
