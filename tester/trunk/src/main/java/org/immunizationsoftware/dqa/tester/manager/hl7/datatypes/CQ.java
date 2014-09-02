package org.immunizationsoftware.dqa.tester.manager.hl7.datatypes;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ExactValue;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.PositiveInteger;

public class CQ extends HL7Component
{
  private NM quantity = null;
  private CE units = null;

  public NM getQuantity() {
    return quantity;
  }

  public void setQuantity(NM quantity) {
    this.quantity = quantity;
  }

  public CE getUnits() {
    return units;
  }

  public void setUnits(CE units) {
    this.units = units;
  }

  @Override
  public HL7Component makeAnother() {
    return new CQ(this);
  }

  public CQ(CQ copy) {
    super(copy);
    init();
  }

  public CQ(String componentName, UsageType usageType) {
    super(ItemType.DATATYPE, "CQ", "Composite Quantity with Units", componentName, 2, usageType);
    setLengthMax(500);
    init();
  }

  public CQ(String componentName, UsageType usageType, Cardinality cardinality) {
    super(ItemType.DATATYPE, "CQ", "Composite Quantity with Units", componentName, 2, usageType, cardinality);
    setLengthMax(500);
    init();
  }

  public void init() {
    setChild(1, quantity = new NM("Quantity"));
    setChild(2, units = new CE("Units", UsageType.R, ValueSet.HL70126));
    quantity.addConformanceStatement(new PositiveInteger("IZ-1: CQ-1 (Quantity) shall be a positive integer."));
    units.addConformanceStatement(new ExactValue("IZ-2: CQ-2 (Units) shall be the literal value \"RD\".", "RD"));
  }
}
