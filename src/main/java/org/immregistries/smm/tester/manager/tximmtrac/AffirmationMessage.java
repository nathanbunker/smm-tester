// Copyright © 2007 Texas Children's Hospital. All rights reserved.

package org.immregistries.smm.tester.manager.tximmtrac;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.immregistries.smm.tester.manager.query.Patient;

/**
 * @author Nathan Bunker
 */
public class AffirmationMessage extends ImmTracMessage {
  public AffirmationMessage() {
    super(new ImmTracSegment[] {new C_Segment(), new CX_Segment(), new A_Segment(),
        new TR_Segment()});
  }

  public AffirmationMessage(Patient patient) {
    this();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");
    segments = new ArrayList<ImmTracSegment>();
    segmentValues = new ArrayList<Map<ImmTracSegment.Field, String>>();
    Map<ImmTracSegment.Field, String> values = new HashMap<ImmTracSegment.Field, String>();
    addSegment(new C_Segment(), values);
    values.put(C_Segment.LAST_NAME_3, patient.getNameLast());
    values.put(C_Segment.FIRST_NAME_4, patient.getNameFirst());
    values.put(C_Segment.MIDDLE_NAME_5, patient.getNameMiddle());
    values.put(C_Segment.SSN_6, "");
    values.put(C_Segment.GENDER_7, patient.getSex());
    values.put(C_Segment.RACE_8, "");
    values.put(C_Segment.MEDICAID_NUMBER_9, "");
    if (patient.getBirthDate() != null) {
      values.put(C_Segment.DATE_OF_BIRTH_10, sdf.format(patient.getBirthDate()));
    }
    values.put(C_Segment.MOTHERS_FIRST_NAME_11, patient.getMotherNameFirst());
    values.put(C_Segment.MOTHERS_MIDDLE_NAME_12, patient.getMotherNameMiddle());
    values.put(C_Segment.MOTHERS_MAIDEN_NAME_13, patient.getMotherNameMaiden());
    if (!patient.getMotherNameLast().equals("") && !patient.getMotherNameFirst().equals("")) {
      addSegment(new CX_Segment(), values);
      values.put(CX_Segment.RELATIONSHIP_TO_CLIENT_7, "M");
      values.put(CX_Segment.GUARDIAN_LAST_NAME_9, patient.getMotherNameLast());
      values.put(CX_Segment.GUARDIAN_FIRST_NAME_10, patient.getMotherNameFirst());
      values.put(CX_Segment.GUARDIAN_MIDDLE_NAME_11, patient.getMotherNameMiddle());
      values.put(CX_Segment.GUARDIAN_SUFFIX_12, "");
    }
    String consent = "A"; // or Y for child
    {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.YEAR, -18);
      if (patient.getBirthDate() != null && patient.getBirthDate().after(calendar.getTime())) {
        consent = "Y";
      }
    }
    values.put(C_Segment.CONSENT_17, consent);
    values.put(C_Segment.RESIDENCE_ADDRESS_LINE_1_18, patient.getAddressStreet1());
    values.put(C_Segment.RESIDENCE_ADDRESS_LINE_2_19, patient.getAddressStreet2());
    values.put(C_Segment.RESIDENCE_CITY_20, patient.getAddressCity());
    values.put(C_Segment.RESIDENCE_STATE_21, patient.getAddressState());
    patient.setAddressState("");
    values.put(C_Segment.RESIDENCE_ZIP_22, getZip(patient.getAddressZip()));
    values.put(C_Segment.RESIDENCE_ZIP4_23, getZip4(patient.getAddressZip()));
    values.put(C_Segment.RESIDENCE_COUNTY_24, "");
    values.put(C_Segment.RESIDENCE_COUNTRY_25, patient.getAddressCountry());
    values.put(C_Segment.PHONE_26, patient.getPhoneArea() + patient.getPhoneLocal());
    values.put(C_Segment.SOURCE_SYSTEM_CLIENT_ID_27, patient.getIdNumber());

    values = new HashMap<ImmTracSegment.Field, String>();
    {
      addSegment(new A_Segment(), values);
      values.put(A_Segment.AFFIRMER_2, "1111433000");
      values.put(A_Segment.AFFIRMATION_DATE_3, sdf.format(new Date()));
    }
    values = new HashMap<ImmTracSegment.Field, String>();
    addSegment(new TR_Segment(), values);
  }


  private static String getZip(String zip) {
    if (zip.length() > 5) {
      return zip.substring(0, 5);
    }
    return zip;
  }

  private static String getZip4(String zip) {
    if (zip.length() > 5) {
      return zip.substring(5);
    }
    return "";
  }

}
