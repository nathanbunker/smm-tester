package org.immregistries.smm.tester.connectors.cdc;

@SuppressWarnings("serial")
public class SecurityFault extends Fault {
  public SecurityFault(String message) {
    super(message, FaultDetail.SECURITY);
  }
}
