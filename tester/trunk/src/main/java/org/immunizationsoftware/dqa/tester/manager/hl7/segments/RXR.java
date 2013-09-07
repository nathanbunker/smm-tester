package org.immunizationsoftware.dqa.tester.manager.hl7.segments;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.ValueSet;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CWE;

public class RXR extends HL7Component
{
  
  private CE route = null;
  private CWE administrationSite = null;
  private CE administrationDevice = null;
  private CWE administrationMethod = null;
  private CE routingInstructions = null;
  private CWE administrationSiteModifier = null;
  
  public CE getRoute() {
    return route;
  }

  public void setRoute(CE route) {
    this.route = route;
  }

  public CWE getAdministrationSite() {
    return administrationSite;
  }

  public void setAdministrationSite(CWE administrationSite) {
    this.administrationSite = administrationSite;
  }

  public CE getAdministrationDevice() {
    return administrationDevice;
  }

  public void setAdministrationDevice(CE administrationDevice) {
    this.administrationDevice = administrationDevice;
  }

  public CWE getAdministrationMethod() {
    return administrationMethod;
  }

  public void setAdministrationMethod(CWE administrationMethod) {
    this.administrationMethod = administrationMethod;
  }

  public CE getRoutingInstructions() {
    return routingInstructions;
  }

  public void setRoutingInstructions(CE routingInstructions) {
    this.routingInstructions = routingInstructions;
  }

  public CWE getAdministrationSiteModifier() {
    return administrationSiteModifier;
  }

  public void setAdministrationSiteModifier(CWE administrationSiteModifier) {
    this.administrationSiteModifier = administrationSiteModifier;
  }

  @Override
  public HL7Component makeAnother() {
    return new RXR(this);
  }
  
  public RXR(RXR copy)
  {
    super(copy);
    init();
  }

  public RXR(UsageType usageType, Cardinality cardinality) {
    super(ItemType.SEGMENT, "RXR", "Pharmacy/Treatment Route Segment", "Pharmacy/Treatment Route Segment", 6, usageType, cardinality);
    init();
  }
  
  @Override
  public void init() {
      setChild(1, route = new CE("Route", UsageType.R, Cardinality.ONE_TIME_ONLY, ValueSet.HL70162));
      setChild(2, administrationSite = new CWE("Administration Site", UsageType.RE, Cardinality.ZERO_OR_ONE, ValueSet.HL70163));
      setChild(3, administrationDevice = new CE("Administration Device", UsageType.O));
      setChild(4, administrationMethod = new CWE("Administration Method", UsageType.O));
      setChild(5, routingInstructions = new CE("Routing Instructions", UsageType.O));
      setChild(6, administrationSiteModifier = new CWE("Administration Site Modifier", UsageType.O));
  }

}
