package org.immregistries.smm.tester.manager.tximmtrac;

import java.util.Map;

public class A_Segment extends ImmTracSegment
{
  
  public A_Segment()
  {
    super("A", "Affirmation", 35);
  }
  
  public static final Field SEGMENT_CODE_1 = new Field("Segment Code", 2);
  public static final Field AFFIRMER_2 = new Field("Affirmer", 25);
  public static final Field AFFIRMATION_DATE_3 = new Field("Affirmation Date", 8);
  
  {
    fields.add(SEGMENT_CODE_1);
    fields.add(AFFIRMER_2);
    fields.add(AFFIRMATION_DATE_3);
  }

  public boolean isBlankSoDoNotSend(Map values)
  {
    // TODO Auto-generated method stub
    return false;
  }

}
