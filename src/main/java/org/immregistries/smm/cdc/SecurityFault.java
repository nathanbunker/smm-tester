package org.immregistries.smm.cdc;

@SuppressWarnings("serial")
public class SecurityFault extends Fault {
  public SecurityFault(String message) {
    super(message, FaultDetail.SECURITY);
  }
}
