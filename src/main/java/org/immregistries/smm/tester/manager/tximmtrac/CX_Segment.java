// Copyright © 2007 Texas Children's Hospital.  All rights reserved.

package org.immregistries.smm.tester.manager.tximmtrac;

import java.util.Map;

/**
 * @author Nathan Bunker
 */
public class CX_Segment extends ImmTracSegment
{
  public CX_Segment()
  {
    super("CX", "Client Extended Demographic", 366);
  }

  public static final Field SEGMENT_CODE_1 = new Field("Segment Code", 2);
  public static final Field RESERVED_2 = new Field("Reserved", 6);
  public static final Field CLIENT_SUFFIX_3 = new Field("Client Suffix", 4);
  public static final Field MOTHERS_LAST_NAME_4 = new Field("Mother's Last name", 20);
  public static final Field MOTHERS_DOB_5 = new Field("Mother's DOB", 8);
  public static final Field RESERVED_6 = new Field("Reserved", 4);
  public static final Field RELATIONSHIP_TO_CLIENT_7 = new Field("Relationship to Client", 2);
  public static final Field RESERVED_8 = new Field("Reserved", 1);
  public static final Field GUARDIAN_LAST_NAME_9 = new Field("Guardian Last Name", 20);
  public static final Field GUARDIAN_FIRST_NAME_10 = new Field("Guardian First Name", 20);
  public static final Field GUARDIAN_MIDDLE_NAME_11 = new Field("Guardian Middle Name", 20);
  public static final Field GUARDIAN_SUFFIX_12 = new Field("Guardian Suffix", 4);
  public static final Field COMMENTS_13 = new Field("Comments", 255);

  {
    fields.add(SEGMENT_CODE_1);
    fields.add(RESERVED_2);
    fields.add(CLIENT_SUFFIX_3);
    fields.add(MOTHERS_LAST_NAME_4);
    fields.add(MOTHERS_DOB_5);
    fields.add(RESERVED_6);
    fields.add(RELATIONSHIP_TO_CLIENT_7);
    fields.add(RESERVED_8);
    fields.add(GUARDIAN_LAST_NAME_9);
    fields.add(GUARDIAN_FIRST_NAME_10);
    fields.add(GUARDIAN_MIDDLE_NAME_11);
    fields.add(GUARDIAN_SUFFIX_12);
    fields.add(COMMENTS_13);
  }

  public boolean isBlankSoDoNotSend(Map values)
  {
    return isBlank(CLIENT_SUFFIX_3, values)
        && isBlank(MOTHERS_LAST_NAME_4, values)
          && isBlank(MOTHERS_DOB_5, values)
          && isBlank(GUARDIAN_LAST_NAME_9, values)
          && isBlank(GUARDIAN_FIRST_NAME_10, values)
          && isBlank(GUARDIAN_MIDDLE_NAME_11, values)
          && isBlank(GUARDIAN_SUFFIX_12, values)
          && isBlank(COMMENTS_13, values);
  }

  private boolean isBlank(Field field, Map values)
  {
    String value = (String) values.get(field);
    return value == null || value.equals("");
  }
}
