package org.openimmunizationsoftware.dqa.transport;

public class ProcessorFactory {
  public static final String PROCESSOR_DEFAULT = "default";
  
  public static Processor createProcessor(String processorName, CDCWSDLServer server)
  {
    return new Processor(server);
  }
}
