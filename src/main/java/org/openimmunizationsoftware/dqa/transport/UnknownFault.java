package org.openimmunizationsoftware.dqa.transport;

@SuppressWarnings("serial")
public class UnknownFault extends Fault {
  public UnknownFault(String message, Throwable throwable) {
    super(message, throwable, FaultDetail.UNKNOWN);
  }

  public UnknownFault(String message) {
    super(message, FaultDetail.UNKNOWN);
  }

}
