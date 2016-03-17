package org.openimmunizationsoftware.dqa.transport;

@SuppressWarnings("serial")
public class SecurityFault extends Fault {
  public SecurityFault(String message) {
    super(message, FaultDetail.SECURITY);
  }
}
