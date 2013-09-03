package org.immunizationsoftware.dqa.tester.manager.hl7.messages;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.MSG;

public class RSP extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new RSP(this);
  }
  
  public RSP(RSP copy)
  {
    super(copy);
    init();
  }
  
  @Override
  public void init() {
    // TODO Auto-generated method stub
    
  }


}
