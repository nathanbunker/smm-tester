/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester.connectors;

import java.util.List;

/**
 * 
 * @author nathan
 */
public class SoapConnector extends HttpConnector
{
  
  

  public SoapConnector(String label, String serviceName) throws Exception {
    super(label, "SOAP");
      throw new IllegalArgumentException("Not implemented");
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception
  {
    throw new IllegalArgumentException("Not implemented");
  }

  @Override
  public String connectivityTest(String message) throws Exception
  {
    throw new IllegalArgumentException("Not implemented");
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb)
  {
    // do nothing
  }

  @Override
  protected void setupFields(List<String> fields)
  {
    super.setupFields(fields);
    
  }
  
  

}
