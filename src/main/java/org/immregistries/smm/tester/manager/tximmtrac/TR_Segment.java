// Copyright � 2007 Texas Children's Hospital.  All rights reserved.

package org.immregistries.smm.tester.manager.tximmtrac;

import java.util.Map;

/**
 * @author Nathan Bunker
 */
public class TR_Segment extends ImmTracSegment
{
  public TR_Segment()
  {
    super("TR", "Terminating Record", 2);
  }
  
  public static final Field SEGMENT_CODE_1 = new Field("Segment Code", 2);
  
  {
    fields.add(SEGMENT_CODE_1);
  }
  public boolean isBlankSoDoNotSend(Map values)
  {
    return false;
  }

}
