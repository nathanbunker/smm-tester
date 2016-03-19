package org.openimmunizationsoftware.dqa.transport;

import java.io.PrintWriter;

public class ProcessorFactory
{
  public static final String PROCESSOR_DEFAULT = "default";
  public static final String PROCESSOR_NO_WHITESPACE = "nowhitespace";

  public static Processor createProcessor(String processorName, CDCWSDLServer server) {
    if (processorName.equals(PROCESSOR_NO_WHITESPACE)) {
      return new ProcessorNoWhitespace(server);
    }
    return new Processor(server);
  }

  public static void printExplanations(PrintWriter out) {
    Processor.printExplanation(out, PROCESSOR_DEFAULT);
    addLink(out, PROCESSOR_DEFAULT);
    ProcessorNoWhitespace.printExplanation(out, PROCESSOR_NO_WHITESPACE);
    addLink(out, PROCESSOR_NO_WHITESPACE);
  }

  public static void addLink(PrintWriter out, String processorName) {
    String link = "wsdl-demo/" + processorName + "?wsdl=true";
    out.println("<p><a href=\"" + link + "\">" + link + "</a></p>");
  }

}
