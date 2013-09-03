package org.immunizationsoftware.dqa.tester.manager.hl7.messages;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;

public class ACK extends HL7Component
{

  @Override
  public HL7Component makeAnother() {
    return new ACK(this);
  }
  
  public ACK(ACK copy)
  {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub 
  }
}
