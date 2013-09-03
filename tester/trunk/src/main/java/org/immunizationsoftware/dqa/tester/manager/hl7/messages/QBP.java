package org.immunizationsoftware.dqa.tester.manager.hl7.messages;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;

public class QBP extends HL7Component
{
  @Override
  public HL7Component makeAnother() {
    return new QBP(this);
  }

  public QBP(QBP copy) {
    super(copy);
    init();
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }
}
