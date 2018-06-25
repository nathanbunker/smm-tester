// Copyright © 2007 Texas Children's Hospital.  All rights reserved.

package org.immregistries.smm.tester.manager.tximmtrac;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Nathan Bunker
 */
public abstract class ImmTracMessage
{
  protected ImmTracSegment[] segmentOrder;
  protected List<ImmTracSegment> segments;
  protected List<Map<ImmTracSegment.Field, String>> segmentValues;

  protected void addSegment(ImmTracSegment segment, Map<ImmTracSegment.Field, String> values)
  {
    segments.add(segment);
    segmentValues.add(values);
  }

  public ImmTracMessage(ImmTracSegment[] segmentOrder)
  {
    this.segmentOrder = segmentOrder;
  }

  

  public String serialize()
  {
    StringBuffer line = new StringBuffer();
    for (Iterator it = segments.iterator(), vit = segmentValues.iterator(); it.hasNext()
        && vit.hasNext();)
    {
      ImmTracSegment segment = (ImmTracSegment) it.next();
      Map values = (Map) vit.next();
      line.append(segment.serialize(values));
    }
    return line.toString();
  }


}
