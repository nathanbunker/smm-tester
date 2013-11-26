package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.OID;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsNotValued;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.IsValued;

public class HD extends HL7Component
{
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

  public HD(String componentName, UsageType usageType) {
    this(componentName, usageType, null, null);
  }

  public HD(String componentName, UsageType usageType, ValueSet valueSet) {
    this(componentName, usageType, null, valueSet);
  }

  @Override
  public HL7Component makeAnother() {
    return new HD(this);
  }
  
  public HD(HD copy)
  {
    super(copy);
    init();
  }


  
  public HD(String componentName, UsageType usageType, Cardinality cardinality, ValueSet valueSet) {
    super(ItemType.DATATYPE, "HD", "Hierarchic Designator", componentName, 3, usageType, cardinality);
    setLengthMax(227);
    setValueSet(valueSet);
    init();
  }

  public void init() {
    setChild(1, namespaceID = new IS("Namespace ID", UsageType.C, 20, getValueSet()));
    setChild(2, universalID = new ST("Universal ID", UsageType.C, 199));
    namespaceID.setConditionalPredicate(new IsNotValued(universalID, UsageType.R, UsageType.O));
    universalID.setConditionalPredicate(new IsNotValued(namespaceID, UsageType.R, UsageType.O));
    universalID.addConformanceStatement(new OID(
        "IZ-5: If populated, HD.2 (Universal ID) it SHALL be valued with an ISO_compliant OID"));
    setChild(3, universalIDType = new ID("Universal ID Type", UsageType.C, 6, ValueSet.HL70301));
    universalIDType.setConditionalPredicate(new IsValued(universalID, UsageType.R, UsageType.X));
    universalIDType.addConformanceStatement(new ExactValue(
        "IZ-6: If populated,  HD.3 (Universal ID Type) SHALL be valued the literal value: \"ISO\"", "ISO"));
  }

}
