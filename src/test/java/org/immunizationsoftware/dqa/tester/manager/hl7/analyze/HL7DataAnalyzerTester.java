package org.immunizationsoftware.dqa.tester.manager.hl7.analyze;

import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.TS;

import junit.framework.TestCase;

public class HL7DataAnalyzerTester extends TestCase
{
  public void testDateAnalyzer()
  {
    HL7Component component = new TS("", UsageType.R);
    component.setValue("20130808");
    HL7DateAnalyzer dateAnalyzer = new HL7DateAnalyzer(component);
    dateAnalyzer.analyze();
    for (String error : dateAnalyzer.getErrorMessageList())
    {
      System.out.println("--> error = " + error);
    }
    assertTrue(dateAnalyzer.isPrecisionYear());
    assertTrue(dateAnalyzer.isPrecisionMonth());
    assertTrue(dateAnalyzer.isPrecisionDay());
    assertFalse(dateAnalyzer.isPrecisionHour());
    assertFalse(dateAnalyzer.isPrecisionMin());
    assertFalse(dateAnalyzer.isPrecisionSec());
    
    component.setValue("20130906141610");
    dateAnalyzer = new HL7DateAnalyzer(component);
    dateAnalyzer.analyze();
    for (String error : dateAnalyzer.getErrorMessageList())
    {
      System.out.println("--> error = " + error);
    }
    assertTrue(dateAnalyzer.isPrecisionYear());
    assertTrue(dateAnalyzer.isPrecisionMonth());
    assertTrue(dateAnalyzer.isPrecisionDay());
    assertTrue(dateAnalyzer.isPrecisionHour());
    assertTrue(dateAnalyzer.isPrecisionMin());
    assertTrue(dateAnalyzer.isPrecisionSec());
    

  }
}
