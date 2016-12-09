package org.immregistries.smm.tester.connectors.cdc;

@SuppressWarnings("serial")
public class MessageTooLargeFault extends Fault {
  public MessageTooLargeFault(String message) {
    super(message, FaultDetail.MESSAGE_TOO_LARGE);
  }
}
