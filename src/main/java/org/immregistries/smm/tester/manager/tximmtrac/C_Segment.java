// Copyright � 2007 Texas Children's Hospital.  All rights reserved.

package org.immregistries.smm.tester.manager.tximmtrac;

import java.util.Map;

/**
 * @author Nathan Bunker
 */
public class C_Segment extends ImmTracSegment
{
  public C_Segment()
  {
    super("C", "Client Basic Demographic", 336);
  }
  
  public static final Field SEGMENT_CODE_1 = new Field("Segment Code", 2);
  public static final Field RESERVED_2 = new Field("Reserved", 10);
  public static final Field LAST_NAME_3 = new Field("Last Name", 20);
  public static final Field FIRST_NAME_4 = new Field("First Name", 20);
  public static final Field MIDDLE_NAME_5 = new Field("Middle Name", 20);
  public static final Field SSN_6 = new Field("SSN", 9);
  public static final Field GENDER_7 = new Field("Gender", 1);
  public static final Field RACE_8 = new Field("Race", 2);
  public static final Field MEDICAID_NUMBER_9 = new Field("Medicaid Number", 9);
  public static final Field DATE_OF_BIRTH_10 = new Field("Date of Birth", 8);
  public static final Field MOTHERS_FIRST_NAME_11 = new Field("Mother's First Name", 20);
  public static final Field MOTHERS_MIDDLE_NAME_12 = new Field("Mother's Middle Name", 20);
  public static final Field MOTHERS_MAIDEN_NAME_13 = new Field("Mother's Maiden Name", 20);
  public static final Field FATHERS_LAST_NAME_14 = new Field("Father's Last Name", 20);
  public static final Field FATHERS_FIRST_NAME_15 = new Field("Father's First Name", 20);
  public static final Field FATHERS_MIDDLE_NAME_16 = new Field("Father's Middle Name", 20);
  public static final Field CONSENT_17 = new Field("Consent", 1);
  public static final Field RESIDENCE_ADDRESS_LINE_1_18 = new Field("Residence Address Line 1", 32);
  public static final Field RESIDENCE_ADDRESS_LINE_2_19 = new Field("Residence Address Line 2", 20);
  public static final Field RESIDENCE_CITY_20 = new Field("Residence City", 20);
  public static final Field RESIDENCE_STATE_21 = new Field("Residence State", 2);
  public static final Field RESIDENCE_ZIP_22 = new Field("Residence Zip", 5);
  public static final Field RESIDENCE_ZIP4_23 = new Field("Residence Zip4", 4);
  public static final Field RESIDENCE_COUNTY_24 = new Field("Residence County", 3);
  public static final Field RESIDENCE_COUNTRY_25 = new Field("Residence Country", 2);
  public static final Field PHONE_26 = new Field("Phone", 10);
  public static final Field SOURCE_SYSTEM_CLIENT_ID_27 = new Field("Source System Client ID", 16);

  {
    fields.add(SEGMENT_CODE_1);
    fields.add(RESERVED_2);
    fields.add(LAST_NAME_3);
    fields.add(FIRST_NAME_4);
    fields.add(MIDDLE_NAME_5);
    fields.add(SSN_6);
    fields.add(GENDER_7);
    fields.add(RACE_8);
    fields.add(MEDICAID_NUMBER_9);
    fields.add(DATE_OF_BIRTH_10);
    fields.add(MOTHERS_FIRST_NAME_11);
    fields.add(MOTHERS_MIDDLE_NAME_12);
    fields.add(MOTHERS_MAIDEN_NAME_13);
    fields.add(FATHERS_LAST_NAME_14);
    fields.add(FATHERS_FIRST_NAME_15);
    fields.add(FATHERS_MIDDLE_NAME_16);
    fields.add(CONSENT_17);
    fields.add(RESIDENCE_ADDRESS_LINE_1_18);
    fields.add(RESIDENCE_ADDRESS_LINE_2_19);
    fields.add(RESIDENCE_CITY_20);
    fields.add(RESIDENCE_STATE_21);
    fields.add(RESIDENCE_ZIP_22);
    fields.add(RESIDENCE_ZIP4_23);
    fields.add(RESIDENCE_COUNTY_24);
    fields.add(RESIDENCE_COUNTRY_25);
    fields.add(PHONE_26);
    fields.add(SOURCE_SYSTEM_CLIENT_ID_27);
  }
  
  public boolean isBlankSoDoNotSend(Map values)
  {
    return false;
  }


}
