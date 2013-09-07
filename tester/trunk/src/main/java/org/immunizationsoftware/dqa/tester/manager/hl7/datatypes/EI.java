package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.OID;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class EI extends HL7Component
{
  private ST entityIdentifier = null;
  private IS namespaceID = null;
  private ST universalID = null;
  private ID universalIDType = null;

  public IS getNamespaceID() {
    return namespaceID;
  }

  public void setNamespaceID(IS namespaceID) {
    this.namespaceID = namespaceID;
  }

  public ST getUniversalID() {
    return universalID;
  }

  public void setUniversalID(ST universalID) {
    this.universalID = universalID;
  }

  public ID getUniversalIDType() {
    return universalIDType;
  }

  public void setUniversalIDType(ID universalIDType) {
    this.universalIDType = universalIDType;
  }

  public EI(String componentNameSpecific, UsageType usageType) {
    super(ItemType.DATATYPE, "EI", "Entity Identifier", componentNameSpecific, 4, usageType);
    init();
  }

  public EI(String componentNameSpecific, UsageType usageType, Cardinality cardinality) {
    super(ItemType.DATATYPE, "EI", "Entity Identifier", componentNameSpecific, 4, usageType, cardinality);
    init();
  }

  @Override
  public HL7Component makeAnother() {
    return new EI(this);
  }

  public EI(EI copy) {
    super(copy);
    init();
  }

  public void init() {
    setChild(1, entityIdentifier = new ST("Entity Identifier", UsageType.R, 199));
    setChild(2, namespaceID = new IS("Namespace ID", UsageType.C, 20, ValueSet.HL70363));
    setChild(3, universalID = new ST("Universal ID", UsageType.C, 199));
    namespaceID.setConditionalPredicate(new IsValued(universalID, UsageType.O, UsageType.R));
    universalID.setConditionalPredicate(new IsValued(namespaceID, UsageType.O, UsageType.R));
    universalID
        .addConformanceStatement(new OID(
            "IZ-3 Conformance Statement: If populated E1.3 (Universal Id), it sahll be valued with a an ISO-compliant OID"));
    setChild(4, universalIDType = new ID("Universal ID Type", UsageType.C, 6, ValueSet.HL70301));
    universalIDType.setConditionalPredicate(new IsValued(universalID, UsageType.R, UsageType.O));
    universalIDType
        .addConformanceStatement(new ExactValue(
            "IZ-4 Conformance Statement: If populated E1.4 is popluated (Universal ID Type), it shall contain the value \"ISO\".",
            "ISO"));
  }
}
