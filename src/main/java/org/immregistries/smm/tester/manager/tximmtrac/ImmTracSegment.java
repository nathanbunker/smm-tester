// Copyright © 2007 Texas Children's Hospital.  All rights reserved.

package org.immregistries.smm.tester.manager.tximmtrac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Nathan Bunker
 */
public abstract class ImmTracSegment
{
  protected List fields = new ArrayList();
  protected String segmentName = "";
  protected String segmentDescription = "";
  protected int segmentLength = 0;

  public ImmTracSegment(String name, String description, int length)
  {
    this.segmentName = name;
    this.segmentDescription = description;
    this.segmentLength = length;
  }

  public Map deSerialize(String line, int start)
  {
    Map values = new HashMap();
    for (Iterator it = fields.iterator(); it.hasNext();)
    {
      Field field = (Field) it.next();
      int end = field.getFieldLength() + start;
      if (end > line.length())
      {
        throw new IllegalArgumentException("Segment is too short");
      }
      String value = line.substring(start, end);
      values.put(field, value.trim());
      start = end;
    }
    return values;
  }

  public String serialize(Map values)
  {
    if (isBlankSoDoNotSend(values))
    {
      return "";
    }
    StringBuffer line = new StringBuffer();
    boolean first = true;
    for (Iterator it = fields.iterator(); it.hasNext();)
    {
      Field field = (Field) it.next();
      String value = (String) values.get(field);
        if (first)
        {
          value = segmentName;
        }
        line.append(field.serialize(value));
      first = false;
    }
    if (line.length() != segmentLength)
    {
      throw new RuntimeException("Problem during segment serialization, segment length is "
          + line.length()
            + ", should be "
            + segmentLength);
    }
    return line.toString();
  }

  public static class Field
  {
    private String description = "";
    private int fieldLength = 0;

    public Field(String description, int fieldLength)
    {
      this.description = description;
      this.fieldLength = fieldLength;
    }

    public String getDescription()
    {
      return this.description;
    }

    public void setDescription(String description)
    {
      this.description = description;
    }

    public int getFieldLength()
    {
      return this.fieldLength;
    }

    public void setFieldLength(int fieldLength)
    {
      this.fieldLength = fieldLength;
    }

    public String serialize(String value)
    {
      if (value == null)
      {
        value = "";
      }
      int length = value.length();
      if (length > fieldLength)
      {
        return value.substring(0, fieldLength);
      }
      return value + getPad(fieldLength - length);
    }
  }
  private static String pads[] = new String[100];

  protected static String getPad(int length)
  {
    String pad = null;
    if (length < pads.length)
    {
      pad = pads[length];
    }
    if (pad == null)
    {
      pad = "";
      for (int i = 0; i < length; i++)
      {
        pad = pad + " ";
      }
      if (length < pads.length)
      {
        pads[length] = pad;
      }
    }
    return pad;
  }

  public String getSegmentName()
  {
    return this.segmentName;
  }

  public void setSegmentName(String segmentName)
  {
    this.segmentName = segmentName;
  }

  public String getSegmentDescription()
  {
    return this.segmentDescription;
  }

  public void setSegmentDescription(String segmentDescription)
  {
    this.segmentDescription = segmentDescription;
  }

  public int getSegmentLength()
  {
    return this.segmentLength;
  }

  public void setSegmentLength(int segmentLength)
  {
    this.segmentLength = segmentLength;
  }

  public static void main(String[] args)
  {
    System.out.println("-----------------------------------------------------------------");
    System.out.println(" GET PAD 0");
    System.out.println(" '" + getPad(0) + "'");
    System.out.println("-----------------------------------------------------------------");
    System.out.println(" GET PAD 1");
    System.out.println(" '" + getPad(1) + "'");
    System.out.println("-----------------------------------------------------------------");
    System.out.println(" GET PAD 0");
    System.out.println(" '" + getPad(0) + "'");
    System.out.println("-----------------------------------------------------------------");
    System.out.println(" GET PAD 10");
    System.out.println(" '" + getPad(10) + "'");
    System.out.println("-----------------------------------------------------------------");
  }

  public abstract boolean isBlankSoDoNotSend(Map values);
}
