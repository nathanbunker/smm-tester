package org.immunizationsoftware.dqa.tester.manager.hl7.conformance;

import org.immunizationsoftware.dqa.tester.manager.hl7.analyze.HL7DateAnalyzer;

public class DatePrecision extends ConformanceStatement
{
  public static final String PRECISION_MINUTE = "YYYYMMDDHHMM";
  public static final String PRECISION_HOUR = "YYYYMMDDHH";
  public static final String PRECISION_DAY = "YYYYMMDD";
  public static final String PRECISION_MONTH = "YYYYMM";

  private String precision = null;

  public String getPrecision() {
    return precision;
  }

  public void setPrecision(String precision) {
    this.precision = precision;
  }

  public DatePrecision(String text, String precision) {
    super(text);
    this.precision = precision;
  }

  @Override
  public boolean conforms() {
    if (component.getValue() == null || component.getValue().equals("")) {
      return true;
    }
    HL7DateAnalyzer dateAnalyzer = (HL7DateAnalyzer) component.getFormatAnalyzer();
    if (dateAnalyzer != null) {
      dateAnalyzer.analyze();
      if (precision.equals(PRECISION_MINUTE)) {
        return dateAnalyzer.isPrecisionMin();
      }
      if (precision.equals(PRECISION_HOUR)) {
        return dateAnalyzer.isPrecisionHour();
      }
      if (precision.equals(PRECISION_DAY)) {
        return dateAnalyzer.isPrecisionDay();
      }
      if (precision.equals(PRECISION_MONTH)) {
        return dateAnalyzer.isPrecisionMonth();
      }
    }
    return false;
  }

}
