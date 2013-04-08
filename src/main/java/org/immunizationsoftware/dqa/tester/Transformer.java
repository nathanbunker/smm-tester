/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.immunizationsoftware.dqa.tester.connectors.Connector;
import org.immunizationsoftware.dqa.tester.transform.IssueCreator;
import org.immunizationsoftware.dqa.tester.transform.Patient;

/**
 * 
 * @author nathan
 */
public class Transformer
{

  private static final String REP_PAT_PHONE = "[PHONE]";
  private static final String REP_PAT_PHONE_AREA = "[PHONE_AREA]";
  private static final String REP_PAT_PHONE_LOCAL = "[PHONE_LOCAL]";
  private static final String REP_PAT_VAC3_DATE = "[VAC3_DATE]";
  private static final String REP_PAT_LANGUAGE = "[LANGUAGE]";
  private static final String REP_PAT_ETHNICITY_LABEL = "[ETHNICITY_LABEL]";
  private static final String REP_PAT_ETHNICITY = "[ETHNICITY]";
  private static final String REP_PAT_RACE_LABEL = "[RACE_LABEL]";
  private static final String REP_PAT_RACE = "[RACE]";
  private static final String REP_PAT_SSN = "[SSN]";
  private static final String REP_PAT_MRN = "[MRN]";
  private static final String REP_PAT_BIRTH_ORDER = "[BIRTH_ORDER]";
  private static final String REP_PAT_BIRTH_MULTIPLE = "[BIRTH_MULTIPLE]";
  private static final String REP_PAT_DOB = "[DOB]";
  private static final String REP_PAT_MOTHER_MAIDEN = "[MOTHER_MAIDEN]";
  private static final String REP_PAT_MOTHER = "[MOTHER]";
  private static final String REP_PAT_SUFFIX = "[SUFFIX]";
  private static final String REP_PAT_FATHER = "[FATHER]";
  private static final String REP_PAT_GENDER = "[GENDER]";
  private static final String REP_PAT_BOY_OR_GIRL = "[BOY_OR_GIRL]";
  private static final String REP_PAT_GIRL = "[GIRL]";
  private static final String REP_PAT_BOY = "[BOY]";

  private static final String REP_CON_USERID = "[USERID]";
  private static final String REP_CON_PASSWORD = "[PASSWORD]";
  private static final String REP_CON_FACILITYID = "[FACILITYID]";
  private static final String REP_CON_FILENAME = "[FILENAME]";

  private static final String INSERT_SEGMENT = "insert segment ";
  private static final String REMOVE_SEGMENT = "remove segment ";
  private static final String CLEAN = "clean";

  private static final int VACCINE_CVX = 0;
  private static final int VACCINE_NAME = 1;
  private static final int VACCINE_TRADE_NAME = 2;
  private static final int VACCINE_LOT = 3;
  private static final int VACCINE_MVX = 4;
  private static final int VACCINE_MANUFACTURER = 5;
  private static final int VACCINE_AMOUNT = 6;
  private static final int VACCINE_ROUTE = 7;
  private static final int VACCINE_SITE = 8;
  private static final int VACCINE_VIS_PUB = 9;
  private static final int VACCINE_VIS_PUB_CODE = 10;
  private static final int VACCINE_VIS_PUB_DATE = 11;
  private static final int VACCINE_VIS2_PUB = 12;
  private static final int VACCINE_VIS2_PUB_CODE = 13;
  private static final int VACCINE_VIS2_PUB_DATE = 14;
  private static final int VACCINE_VIS3_PUB = 15;
  private static final int VACCINE_VIS3_PUB_CODE = 16;
  private static final int VACCINE_VIS3_PUB_DATE = 17;

  private static Map<String, List<String[]>> conceptMap = null;
  private static Random random = new Random();

  public String getValue(String concept) throws IOException
  {
    return getValue(concept, 0);
  }

  public String getValue(String concept, int pos) throws IOException
  {
    String value = "";
    if (conceptMap == null)
    {
      init();
    }
    List<String[]> valueList = conceptMap.get(concept);
    if (valueList != null)
    {
      String[] values = valueList.get(random.nextInt(valueList.size()));
      if (pos < values.length)
      {
        value = values[pos];
      }
    }
    return value;
  }

  public String[] getValueArray(String concept, int size) throws IOException
  {
    if (conceptMap == null)
    {
      init();
    }
    List<String[]> valueList = conceptMap.get(concept);
    String[] valueSourceList = null;
    if (valueList != null)
    {
      valueSourceList = valueList.get(random.nextInt(valueList.size()));
    }
    String[] values = new String[size];
    for (int i = 0; i < values.length; i++)
    {
      if (valueSourceList != null && i < valueSourceList.length)
      {
        values[i] = valueSourceList[i];
      } else
      {
        values[i] = "";
      }
    }
    return values;
  }

  protected boolean alsoHas(TestCaseMessage testCaseMessage, String checkString)
  {
    boolean alsoHas = false;
    for (String extra2 : testCaseMessage.getQuickTransformations())
    {
      if (extra2.equals(checkString))
      {
        alsoHas = true;
        break;
      }
    }
    return alsoHas;
  }

  public enum PatientType {
    ANY_CHILD, BABY, TODDLER, TWEEN, ADULT, NONE
  };

  protected PatientType createDates(String[] dates, PatientType type)
  {
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      if (type == PatientType.BABY || (type == PatientType.ANY_CHILD && random.nextBoolean()))
      {
        // Setting up baby, 6 months old today
        // 6 month appointment
        Calendar cal6Month = Calendar.getInstance();
        dates[3] = sdf.format(cal6Month.getTime());

        // Born about 6 months before
        Calendar calBorn = Calendar.getInstance();
        calBorn.add(Calendar.MONTH, -6);
        calBorn.add(Calendar.DAY_OF_MONTH, 3 - random.nextInt(17));
        dates[0] = sdf.format(calBorn.getTime());

        // 4 month appointment
        Calendar cal4Month = Calendar.getInstance();
        cal4Month.setTime(calBorn.getTime());
        cal4Month.add(Calendar.MONTH, 4);
        cal4Month.add(Calendar.DAY_OF_MONTH, random.nextInt(12) - 3);
        dates[2] = sdf.format(cal4Month.getTime());

        // 2 month appointment
        Calendar cal2Month = Calendar.getInstance();
        cal2Month.setTime(calBorn.getTime());
        cal2Month.add(Calendar.MONTH, 2);
        cal4Month.add(Calendar.DAY_OF_MONTH, random.nextInt(10) - 3);
        dates[1] = sdf.format(cal2Month.getTime());

        return PatientType.BABY;
      } else
      {
        if (type == PatientType.TODDLER || (type == PatientType.ANY_CHILD && random.nextBoolean()))
        {
          // Setting up toddler
          Calendar calendar = Calendar.getInstance();
          // 4 years (today) - 48 months
          dates[3] = sdf.format(calendar.getTime());
          // 19 months
          calendar.add(Calendar.MONTH, 19 - 48);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[2] = sdf.format(calendar.getTime());
          // 12 months
          calendar.add(Calendar.MONTH, 12 - 19);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[1] = sdf.format(calendar.getTime());
          // birth
          calendar.add(Calendar.MONTH, -12);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[0] = sdf.format(calendar.getTime());
          return PatientType.TODDLER;
        } else if (type == PatientType.ANY_CHILD || type == PatientType.TWEEN)
        {
          // Setting up tween
          Calendar calendar = Calendar.getInstance();
          // 13 years (today)
          dates[3] = sdf.format(calendar.getTime());
          // 11 years
          calendar.add(Calendar.YEAR, -2);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[2] = sdf.format(calendar.getTime());
          // 9 years
          calendar.add(Calendar.YEAR, -2);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[1] = sdf.format(calendar.getTime());
          // birth
          calendar.add(Calendar.YEAR, -9);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[0] = sdf.format(calendar.getTime());
          return PatientType.TWEEN;
        } else
        {
          // Setting up adult
          Calendar calendar = Calendar.getInstance();
          // 67 years (today)
          dates[3] = sdf.format(calendar.getTime());
          // last year
          calendar.add(Calendar.YEAR, -1);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[2] = sdf.format(calendar.getTime());
          // two years before that
          calendar.add(Calendar.YEAR, -2);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[1] = sdf.format(calendar.getTime());
          // birth
          calendar.add(Calendar.YEAR, -64);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[0] = sdf.format(calendar.getTime());
          return PatientType.ADULT;

        }
      }
    }
  }

  protected void handleSometimes(Transform t)
  {
    int sometimes = 0;
    if (t.value.startsWith("~") && t.value.indexOf("%") != -1)
    {
      int perPos = t.value.indexOf("%");
      try
      {
        sometimes = Integer.parseInt(t.value.substring(1, perPos));
        t.value = t.value.substring(perPos + 1);
      } catch (NumberFormatException nfe)
      {
        // ignore
      }
    }
    if (sometimes > 0)
    {
      String part1 = t.value;
      String part2 = "";
      int colonPos = t.value.indexOf(":");
      if (colonPos != -1)
      {
        part1 = t.value.substring(0, colonPos);
        part2 = t.value.substring(colonPos + 1);
      }
      if (random.nextInt(100) >= sometimes)
      {
        t.value = part2;
        handleSometimes(t);
      } else
      {
        t.value = part1;
      }
    }
  }

  protected void init() throws IOException
  {
    conceptMap = new HashMap<String, List<String[]>>();
    BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("transform.txt")));
    String line;
    while ((line = in.readLine()) != null)
    {
      int equals = line.indexOf("=");
      if (equals != -1)
      {
        String concept = line.substring(0, equals);
        String[] values = line.substring(equals + 1).split("\\,");
        List<String[]> valueList = conceptMap.get(concept);
        if (valueList == null)
        {
          valueList = new ArrayList<String[]>();
          conceptMap.put(concept, valueList);
        }
        valueList.add(values);
      }
    }
  }

  public String transform(Connector connector, String messageText)
  {
    String quickTransforms = "";

    if (connector.getQuickTransformations() != null)
    {
      for (String extra : connector.getQuickTransformations())
      {
        if (extra.equals("2.5.1"))
        {
          quickTransforms += "MSH-12=2.5.1\n";
        } else if (extra.equals("2.3.1"))
        {
          quickTransforms += "MSH-12=2.3.1\n";
        }
      }
    }

    String transforms = quickTransforms + "\n" + connector.getCustomTransformations() + "\n";
    return transform(messageText, transforms, PatientType.NONE, connector);
  }

  public void transform(TestCaseMessage testCaseMessage)
  {
    String actualTestCase = testCaseMessage.getTestCaseNumber();
    if (actualTestCase.equals(""))
    {
      actualTestCase = REP_PAT_MRN;
    }
    String quickTransforms = "";

    if (testCaseMessage.getQuickTransformations() != null)
    {
      for (String extra : testCaseMessage.getQuickTransformations())
      {
        if (extra.equals("BOY"))
        {
          quickTransforms += "PID-5=~90%[LAST]:[MOTHER_MAIDEN]\n";
          quickTransforms += "PID-5.2=[BOY]\n";
          quickTransforms += "PID-5.3=~60%[BOY_MIDDLE]:~70%[BOY_MIDDLE_INITIAL]\n";
          quickTransforms += "PID-5.4=~10%[SUFFIX]\n";
          quickTransforms += "PID-5.7=L\n";
          if (alsoHas(testCaseMessage, "GIRL"))
          {
            quickTransforms += "PID-8=~50%M:F\n";

          } else
          {
            quickTransforms += "PID-8=M\n";
          }
        } else if (extra.equals("GIRL"))
        {
          quickTransforms += "PID-5=~90%[LAST]:[MOTHER_MAIDEN]\n";
          quickTransforms += "PID-5.2=[GIRL]\n";
          quickTransforms += "PID-5.3=~60%[GIRL_MIDDLE]:~70%[GIRL_MIDDLE_INITIAL]\n";
          quickTransforms += "PID-5.7=L\n";
          quickTransforms += "PID-8=F\n";
        } else if (extra.equals("BOY_OR_GIRL"))
        {
          quickTransforms += "PID-5=~90%[LAST]:[MOTHER_MAIDEN]\n";
          quickTransforms += "PID-5.2=[BOY_OR_GIRL]\n";
          quickTransforms += "PID-5.3=~60%[BOY_OR_GIRL_MIDDLE]:~70%[GIRL_MIDDLE_INITIAL]\n";
          quickTransforms += "PID-5.7=L\n";
          quickTransforms += "PID-8=[GENDER]\n";
        } else if (extra.equals("FATHER"))
        {
          if (alsoHas(testCaseMessage, "MOTHER"))
          {
            quickTransforms += "NK1#2-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
            quickTransforms += "NK1#2-2.2=[FATHER]\n";
            quickTransforms += "NK1#2-3=FTH\n";
            quickTransforms += "NK1#2-3.2=Father\n";
            quickTransforms += "NK1#2-3.3=HL70063\n";
          } else
          {
            quickTransforms += "NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
            quickTransforms += "NK1-2.2=[FATHER]\n";
            quickTransforms += "NK1-3=FTH\n";
            quickTransforms += "NK1-3.2=Father\n";
            quickTransforms += "NK1-3.3=HL70063\n";
          }
        } else if (extra.equals("MOTHER"))
        {
          quickTransforms += "PID-6=~50%[MOTHER_MAIDEN]:[LAST_DIFFERENT]\n";
          quickTransforms += "PID-6.2=[MOTHER]\n";
          quickTransforms += "NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
          quickTransforms += "NK1-2.2=[MOTHER]\n";
          quickTransforms += "NK1-3=MTH\n";
          quickTransforms += "NK1-3.2=Mother\n";
          quickTransforms += "NK1-3.3=HL70063\n";
        } else if (extra.equals("DOB"))
        {
          quickTransforms += "PID-7=[DOB]\n";
        } else if (extra.equals("TWIN_POSSIBLE"))
        {
          quickTransforms += "PID-24=[BIRTH_MULTIPLE]\n";
          quickTransforms += "PID-25=[BIRTH_ORDER]\n";
        } else if (extra.equals("VAC1_ADMIN"))
        {
          int count = 3;
          count = count - (alsoHas(testCaseMessage, "VAC2_ADMIN") || alsoHas(testCaseMessage, "VAC2_HIST") ? 1 : 0);
          count = count - (alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0);
          quickTransforms = addAdmin(quickTransforms, "1", count);
        } else if (extra.equals("VAC2_ADMIN"))
        {
          int count = 3;
          count -= alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0;
          quickTransforms = addAdmin(quickTransforms, "2", count);
        } else if (extra.equals("VAC3_ADMIN"))
        {
          quickTransforms = addAdmin(quickTransforms, "3", 3);
        } else if (extra.equals("VAC1_NA"))
        {
          int count = 3;
          count -= alsoHas(testCaseMessage, "VAC2_ADMIN") || alsoHas(testCaseMessage, "VAC2_HIST") ? 1 : 0;
          count -= alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0;
          quickTransforms += "ORC-3=[VAC" + count + "_ID]\n";
          quickTransforms += "RXA-3=[VAC" + count + "_DATE]\n";
          quickTransforms += "RXA-5.1=[VAC" + count + "_CVX]\n";
          quickTransforms += "RXA-5.2=[VAC" + count + "_CVX_LABEL]\n";
          quickTransforms += "RXA-5.3=CVX\n";
          quickTransforms += "RXA-6=999\n";
          quickTransforms += "RXA-21=A\n";
        } else if (extra.equals("VAC1_HIST"))
        {
          int count = 3;
          count -= alsoHas(testCaseMessage, "VAC2_ADMIN") || alsoHas(testCaseMessage, "VAC2_HIST") ? 1 : 0;
          count -= alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0;
          quickTransforms += "ORC-3=[VAC" + count + "_ID]\n";
          quickTransforms += "RXA-3=[VAC" + count + "_DATE]\n";
          quickTransforms += "RXA-5.1=[VAC" + count + "_CVX]\n";
          quickTransforms += "RXA-5.2=[VAC" + count + "_CVX_LABEL]\n";
          quickTransforms += "RXA-5.3=CVX\n";
          quickTransforms += "RXA-6=999\n";
          quickTransforms += "RXA-9.1=01\n";
          quickTransforms += "RXA-9.2=Historical\n";
          quickTransforms += "RXA-9.3=NIP001\n";
          quickTransforms += "RXA-21=A\n";
        } else if (extra.equals("VAC2_HIST"))
        {
          int count = 3;
          count -= alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0;
          quickTransforms += "ORC#2-3=[VAC" + count + "_ID]\n";
          quickTransforms += "RXA#2-3=[VAC" + count + "_DATE]\n";
          quickTransforms += "RXA#2-5.1=[VAC" + count + "_CVX]\n";
          quickTransforms += "RXA#2-5.2=[VAC" + count + "_CVX_LABEL]\n";
          quickTransforms += "RXA#2-5.3=CVX\n";
          quickTransforms += "RXA#2-9.1=01\n";
          quickTransforms += "RXA#2-6=999\n";
          quickTransforms += "RXA#2-9.2=Historical\n";
          quickTransforms += "RXA#2-9.3=NIP001\n";
          quickTransforms += "RXA#2-21=A\n";
        } else if (extra.equals("VAC3_HIST"))
        {
          quickTransforms += "ORC#2-3=[VAC3_ID]\n";
          quickTransforms += "RXA#3-3=[VAC3_DATE]\n";
          quickTransforms += "RXA#3-5.1=[VAC3_CVX]\n";
          quickTransforms += "RXA#3-5.2=[VAC3_CVX_LABEL]\n";
          quickTransforms += "RXA#3-5.3=CVX\n";
          quickTransforms += "RXA#3-9.1=01\n";
          quickTransforms += "RXA#3-6=999\n";
          quickTransforms += "RXA#3-9.2=Historical\n";
          quickTransforms += "RXA#3-9.3=NIP001\n";
          quickTransforms += "RXA#3-21=A\n";
        } else if (extra.equals("VAC2_NA"))
        {
          int count = 3;
          count -= alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0;
          quickTransforms += "ORC#2-3=[VAC" + count + "_ID]\n";
          quickTransforms += "RXA#2-3=[VAC" + count + "_DATE]\n";
          quickTransforms += "RXA#2-5.1=[VAC" + count + "_CVX]\n";
          quickTransforms += "RXA#2-5.2=[VAC" + count + "_CVX_LABEL]\n";
          quickTransforms += "RXA#2-5.3=CVX\n";
          quickTransforms += "RXA#2-9.1=01\n";
          quickTransforms += "RXA#2-6=999\n";
          quickTransforms += "RXA#2-21=A\n";
        } else if (extra.equals("VAC3_NA"))
        {
          quickTransforms += "ORC#2-3=[VAC3_ID]\n";
          quickTransforms += "RXA#3-3=[VAC3_DATE]\n";
          quickTransforms += "RXA#3-5.1=[VAC3_CVX]\n";
          quickTransforms += "RXA#3-5.2=[VAC3_CVX_LABEL]\n";
          quickTransforms += "RXA#3-5.3=CVX\n";
          quickTransforms += "RXA#3-9.1=01\n";
          quickTransforms += "RXA#3-6=999\n";
          quickTransforms += "RXA#3-21=A\n";
        } else if (extra.equals("ADDRESS"))
        {
          quickTransforms += "PID-11.1=[STREET]\n";
          quickTransforms += "PID-11.3=[CITY]\n";
          quickTransforms += "PID-11.4=[STATE]\n";
          quickTransforms += "PID-11.5=[ZIP]\n";
          quickTransforms += "PID-11.6=USA\n";
          quickTransforms += "PID-11.7=P\n";
        } else if (extra.equals("PHONE"))
        {
          quickTransforms += "PID-13.1=\n";
          quickTransforms += "PID-13.2=PRN\n";
          quickTransforms += "PID-13.3=PH\n";
          quickTransforms += "PID-13.6=[PHONE_AREA]\n";
          quickTransforms += "PID-13.7=[PHONE_LOCAL]\n";
        } else if (extra.equals("RACE"))
        {
          quickTransforms += "PID-10.1=[RACE]\n";
          quickTransforms += "PID-10.2=[RACE_LABEL]\n";
          quickTransforms += "PID-10.3=HL70005\n";
        } else if (extra.equals("ETHNICITY"))
        {
          quickTransforms += "PID-22.1=[ETHNICITY]\n";
          quickTransforms += "PID-22.2=[ETHNICITY_LABEL]\n";
          quickTransforms += "PID-22.3=CDCREC\n";
        } else if (extra.equals("2.5.1"))
        {
          quickTransforms += "MSH-2=^~\\&\n";
          quickTransforms += "MSH-7=[NOW]\n";
          quickTransforms += "MSH-9.1=VXU\n";
          quickTransforms += "MSH-9.2=V04\n";
          quickTransforms += "MSH-9.3=VXU_V04\n";
          quickTransforms += "MSH-10=" + actualTestCase + "\n";
          quickTransforms += "MSH-11=P\n";
          quickTransforms += "MSH-12=2.5.1\n";
          quickTransforms += "PID-1=1\n";
          quickTransforms += "PID-3.1=" + actualTestCase + "\n";
          quickTransforms += "PID-3.4=OIS-TEST\n";
          quickTransforms += "PID-3.5=MR\n";
          quickTransforms += "NK1-1=1\n";
          quickTransforms += "NK1#2-1=2\n";
          quickTransforms += "PV1-1=1\n";
          quickTransforms += "PV1-2=R\n";
          quickTransforms += "ORC-1=RE\n";
          quickTransforms += "ORC-3.2=OIS\n";
          quickTransforms += "RXA-1=0\n";
          quickTransforms += "RXA-2=1\n";
          quickTransforms += "RXA-6=999\n";
          quickTransforms += "ORC#2-1=RE\n";
          quickTransforms += "ORC#2-3.2=OIS\n";
          quickTransforms += "RXA#2-1=0\n";
          quickTransforms += "RXA#2-2=1\n";
          quickTransforms += "RXA#2-6=999\n";
          quickTransforms += "ORC#3-1=RE\n";
          quickTransforms += "ORC#3-3.2=OIS\n";
          quickTransforms += "RXA#3-1=0\n";
          quickTransforms += "RXA#3-2=1\n";
          quickTransforms += "RXA#3-6=999\n";
          quickTransforms += "RXR-1=OTH\n";
          quickTransforms += "RXR#2-1=OTH\n";
          quickTransforms += "RXR#3-1=OTH\n";
        } else if (extra.equals("2.3.1"))
        {
          quickTransforms += "MSH-2=^~\\&\n";
          quickTransforms += "MSH-7=[NOW]\n";
          quickTransforms += "MSH-9.1=VXU\n";
          quickTransforms += "MSH-9.2=V04\n";
          quickTransforms += "MSH-10=" + actualTestCase + "\n";
          quickTransforms += "MSH-11=P\n";
          quickTransforms += "MSH-12=2.3.1\n";
          quickTransforms += "PID-1=1\n";
          quickTransforms += "PID-3.1=" + actualTestCase + "\n";
          quickTransforms += "PID-3.5=MR\n";
          quickTransforms += "NK1-1=1\n";
          quickTransforms += "NK1#2-1=2\n";
          quickTransforms += "ORC-1=RE\n";
          quickTransforms += "RXA-1=0\n";
          quickTransforms += "RXA-2=1\n";
          quickTransforms += "RXA-6=999\n";
          quickTransforms += "RXA#2-1=0\n";
          quickTransforms += "RXA#2-2=1\n";
          quickTransforms += "RXA#2-6=999\n";
          quickTransforms += "RXA#3-1=0\n";
          quickTransforms += "RXA#3-2=1\n";
          quickTransforms += "RXA#3-6=999\n";
          quickTransforms += "RXR-1=OTH\n";
          quickTransforms += "RXR#2-1=OTH\n";
          quickTransforms += "RXR#3-1=OTH\n";
        }
      }
    }

    String causeIssueTransforms = IssueCreator.createTransforms(testCaseMessage);
    String transforms = quickTransforms + "\n" + testCaseMessage.getCustomTransformations() + "\n" + causeIssueTransforms;
    String result = transform(testCaseMessage.getPreparedMessage(), transforms, testCaseMessage.getPatientType(), null);
    testCaseMessage.setMessageText(result);
    testCaseMessage.setQuickTransformationsConverted(quickTransforms);
  }

  private String addAdmin(String quickTransforms, String vaccineNumber, int count)
  {
    quickTransforms += "ORC#" + vaccineNumber + "-3=[VAC" + count + "_ID]\n";
    String front = "RXA#" + vaccineNumber;
    quickTransforms += front + "-3=[VAC" + count + "_DATE]\n";
    quickTransforms += front + "-9.1=00\n";
    quickTransforms += front + "-9.2=Administered\n";
    quickTransforms += front + "-9.3=NIP001\n";
    quickTransforms += front + "-5.1=[VAC" + count + "_CVX]\n";
    quickTransforms += front + "-5.2=[VAC" + count + "_CVX_LABEL]\n";
    quickTransforms += front + "-5.3=CVX\n";
    quickTransforms += front + "-6=[VAC2_AMOUNT]\n";
    quickTransforms += front + "-7.1=mL\n";
    quickTransforms += front + "-7.2=milliliters\n";
    quickTransforms += front + "-7.3=UCUM\n";
    quickTransforms += front + "-15=[VAC" + count + "_LOT]\n";
    quickTransforms += front + "-17.1=[VAC" + count + "_MVX]\n";
    quickTransforms += front + "-17.2=[VAC" + count + "_MVX_LABEL]\n";
    quickTransforms += front + "-17.3=MVX\n";
    quickTransforms += front + "-21=A\n";
    quickTransforms += front + ":RXR-1.1=[VAC" + count + "_ROUTE]\n";
    quickTransforms += front + ":RXR-1.2=\n";
    quickTransforms += front + ":RXR-1.3=HL70162\n";
    quickTransforms += front + ":RXR-2.1=[VAC" + count + "_SITE]\n";
    quickTransforms += front + ":RXR-2.2=\n";
    quickTransforms += front + ":RXR-2.3=HL70163\n";
    quickTransforms = addObx(quickTransforms, vaccineNumber, count);
    return quickTransforms;
  }

  private String addObx(String quickTransforms, String vaccineNumber, int count)
  {
    String start = "RXA#" + vaccineNumber + ":OBX#";
    quickTransforms += start + "1-1=1\n";
    quickTransforms += start + "1-2=CE\n";
    quickTransforms += start + "1-3.1=64994-7\n";
    quickTransforms += start + "1-3.2=Vaccine funding program eligibility category\n";
    quickTransforms += start + "1-3.3=LN\n";
    quickTransforms += start + "1-4=1\n";
    quickTransforms += start + "1-5.1=[VFC]\n";
    quickTransforms += start + "1-5.2=[VFC_LABEL]\n";
    quickTransforms += start + "1-5.3=HL70064\n";
    quickTransforms += start + "1-11=F\n";
    quickTransforms += start + "1-14=[TODAY]\n";
    quickTransforms += start + "1-17.1=VXC40\n";
    quickTransforms += start + "1-17.2=Eligibility captured at the immunization level\n";
    quickTransforms += start + "1-17.3=CDCPHINVS\n";
    quickTransforms += start + "2-1=2\n";
    quickTransforms += start + "2-2=CE\n";
    quickTransforms += start + "2-3.1=30956-7\n";
    quickTransforms += start + "2-3.2=Vaccine Type\n";
    quickTransforms += start + "2-3.3=LN\n";
    quickTransforms += start + "2-4=2\n";
    quickTransforms += start + "2-5.1=[VAC" + count + "_VIS_PUB_CODE]\n";
    quickTransforms += start + "2-5.2=[VAC" + count + "_VIS_PUB_NAME]\n";
    quickTransforms += start + "2-5.3=CVX\n";
    quickTransforms += start + "2-11=F\n";
    quickTransforms += start + "3-1=3\n";
    quickTransforms += start + "3-2=TS\n";
    quickTransforms += start + "3-3.1=29768-9\n";
    quickTransforms += start + "3-3.2=Date vaccine information statement published\n";
    quickTransforms += start + "3-3.3=LN\n";
    quickTransforms += start + "3-4=2\n";
    quickTransforms += start + "3-5.1=[VAC" + count + "_VIS_PUB_DATE]\n";
    quickTransforms += start + "3-11=F\n";
    quickTransforms += start + "4-1=4\n";
    quickTransforms += start + "4-2=TS\n";
    quickTransforms += start + "4-3.1=29769-7\n";
    quickTransforms += start + "4-3.2=Date vaccine information statement presented\n";
    quickTransforms += start + "4-3.3=LN\n";
    quickTransforms += start + "4-4=2\n";
    quickTransforms += start + "4-5.1=[VAC" + count + "_DATE]\n";
    quickTransforms += start + "4-11=F\n";
    return quickTransforms;
  }

  protected String transform(String baseText, String transformText, PatientType patientType, Connector connector)
  {
    String resultText = baseText;
    try
    {
      if (!resultText.endsWith("\r"))
      {
        resultText += "\r";
      }
      BufferedReader inTransform = new BufferedReader(new StringReader(transformText));
      Patient patient = null;
      if (patientType != PatientType.NONE)
      {
        patient = setupPatient(patientType);
      }
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      String today = sdf.format(new Date());
      sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      String now = sdf.format(new Date());
      String line;
      while ((line = inTransform.readLine()) != null)
      {
        line = line.trim();
        if (line.length() > 0)
        {
          if (line.toLowerCase().startsWith(INSERT_SEGMENT))
          {
            line = line.substring(INSERT_SEGMENT.length()).trim();
            if (line.length() > 3 && line.indexOf(" ") == 3)
            {
              String newSegmentName = line.substring(0, 3);
              if (newSegmentName.equals("FHS") || newSegmentName.equals("BHS"))
              {
                BufferedReader inResult = new BufferedReader(new StringReader(resultText));
                String lineResult;
                String msh = null;
                while ((lineResult = inResult.readLine()) != null)
                {
                  lineResult = lineResult.trim();
                  if (lineResult.length() > 0)
                  {
                    if (lineResult.startsWith("MSH|"))
                    {
                      msh = lineResult;
                    }
                  }
                }
                if (msh != null && msh.startsWith("MSH|"))
                {
                  String[] mshFields = msh.split("\\|");
                  if (mshFields.length > 1 && mshFields[1] != null)
                  {
                    newSegmentName += "|" + mshFields[1];
                    // MSH-3
                    if (mshFields.length > 2 && mshFields[2] != null)
                    {
                      newSegmentName += "|" + mshFields[2];
                      // MSH-4
                      if (mshFields.length > 3 && mshFields[3] != null)
                      {
                        newSegmentName += "|" + mshFields[3];
                        // MSH-5
                        if (mshFields.length > 4 && mshFields[4] != null)
                        {
                          newSegmentName += "|" + mshFields[4];
                          // MSH-6
                          if (mshFields.length > 5 && mshFields[5] != null)
                          {
                            newSegmentName += "|" + mshFields[5];
                            // MSH-7
                            if (mshFields.length > 6 && mshFields[6] != null)
                            {
                              newSegmentName += "|" + mshFields[6];
                              // MSH-10
                              if (mshFields.length > 9 && mshFields[9] != null)
                              {
                                newSegmentName += "||||" + mshFields[9];
                              }
                            }
                          }
                        }
                      }
                    }
                  } else
                  {
                    newSegmentName += "|^~\\&";
                  }
                } else
                {
                  newSegmentName += "|^~\\&";
                }
              }
              line = line.substring(3).trim();
              int nextSpace = line.indexOf(" ");
              if (nextSpace == -1)
              {
                nextSpace = line.length();
              }
              String insertAction = line.substring(0, nextSpace);
              line = line.substring(nextSpace).trim();
              if (insertAction.equalsIgnoreCase("first"))
              {
                resultText = newSegmentName + "|\r" + resultText;
              } else if (insertAction.equalsIgnoreCase("last"))
              {
                resultText = resultText + newSegmentName + "|\r";
              } else if (insertAction.equalsIgnoreCase("after") || insertAction.equalsIgnoreCase("before"))
              {
                int repeatPos = 0;
                int poundPos = line.indexOf("#");
                if (poundPos == -1)
                {
                  repeatPos = 1;
                } else
                {
                  try
                  {
                    repeatPos = Integer.parseInt(line.substring(poundPos + 1).trim());

                  } catch (NumberFormatException nfe)
                  {
                    repeatPos = 1;
                  }
                  line = line.substring(0, poundPos);
                }
                BufferedReader inResult = new BufferedReader(new StringReader(resultText));
                resultText = "";
                String lineResult;
                int repeatCount = 0;
                while ((lineResult = inResult.readLine()) != null)
                {
                  lineResult = lineResult.trim();
                  if (lineResult.length() > 0)
                  {
                    if (insertAction.equalsIgnoreCase("after"))
                    {
                      resultText += lineResult + "\r";
                    }
                    if (lineResult.startsWith(line))
                    {
                      repeatCount++;
                      if (repeatCount == repeatPos)
                      {
                        resultText += newSegmentName + "|\r";
                      }
                    }
                    if (insertAction.equalsIgnoreCase("before"))
                    {
                      resultText += lineResult + "\r";
                    }
                  }
                }
              }
            }
          } else if (line.toLowerCase().startsWith(REMOVE_SEGMENT))
          {
            line = line.substring(REMOVE_SEGMENT.length()).trim();
            if (line.length() >= 3)
            {
              int nextSpace = line.indexOf(" ");
              if (nextSpace == -1)
              {
                nextSpace = line.length();
              }
              String removeSegmentName = line.substring(0, nextSpace);
              line = line.substring(removeSegmentName.length()).trim();
              int repeatPos = 0;
              int poundPos = removeSegmentName.indexOf("#");
              if (poundPos == -1)
              {
                repeatPos = 1;
              } else
              {
                try
                {
                  repeatPos = Integer.parseInt(removeSegmentName.substring(poundPos + 1).trim());
                } catch (NumberFormatException nfe)
                {
                  repeatPos = 1;
                }
                removeSegmentName = removeSegmentName.substring(0, poundPos);
              }

              String removeAction = line.trim();
              boolean all = false;
              if (removeAction.equalsIgnoreCase("all"))
              {
                all = true;
              }

              BufferedReader inResult = new BufferedReader(new StringReader(resultText));
              resultText = "";
              String lineResult;
              int repeatCount = 0;
              while ((lineResult = inResult.readLine()) != null)
              {
                lineResult = lineResult.trim();
                if (lineResult.length() > 0)
                {
                  if (lineResult.startsWith(removeSegmentName))
                  {
                    repeatCount++;
                    if (!all && repeatCount != repeatPos)
                    {
                      resultText += lineResult + "\r";
                    }
                  } else
                  {
                    resultText += lineResult + "\r";
                  }
                }
              }
            }
          } else if (line.toLowerCase().trim().equals(CLEAN))
          {
            BufferedReader inResult = new BufferedReader(new StringReader(resultText));
            resultText = "";
            String lineResult;
            int repeatCount = 0;
            while ((lineResult = inResult.readLine()) != null)
            {
              lineResult = lineResult.trim();
              if (lineResult.length() > 0)
              {
                String finalLine = "";
                int writtenPos = 0;
                String possibleLine = "";

                boolean foundFieldData = false;
                boolean foundCompData = false;
                boolean foundRepData = false;

                for (int i = lineResult.length() - 1; i >= 0; i--)
                {
                  char c = lineResult.charAt(i);

                  if (!foundFieldData)
                  {
                    if (c != '|' && c != '^' && c != '~')
                    {
                      foundFieldData = true;
                    }
                  } else if (!foundRepData)
                  {
                    if (c != '^' && c != '~')
                    {
                      foundRepData = true;
                    }
                  } else if (!foundCompData)
                  {
                    if (c != '^')
                    {
                      foundCompData = true;
                    }
                  }
                  if (foundFieldData)
                  {
                    if (c == '|')
                    {
                      foundRepData = false;
                      foundCompData = false;
                      finalLine = c + finalLine;
                    } else if (c == '~')
                    {
                      if (foundRepData)
                      {
                        finalLine = c + finalLine;
                      }
                      foundCompData = false;
                    } else if (c == '^')
                    {
                      if (foundCompData)
                      {
                        finalLine = c + finalLine;
                      }
                    } else
                    {
                      finalLine = c + finalLine;
                    }
                  }
                }
                resultText += finalLine + "|\r";

              }
            }
          } else
          {
            int posEqual = line.indexOf("=");
            int posDash = line.indexOf("-");
            int posDot = line.indexOf(".");
            if (posDash > posEqual)
            {
              posDash = -1;
            }
            if (posDot > posEqual)
            {
              posDot = -1;
            }
            if (posEqual != -1 && posDash != -1 && posDash < posEqual && (posDot == -1 || (posDot > posDash && posDot < posEqual)))
            {
              Transform t = new Transform();
              t.segment = line.substring(0, posDash).trim();
              int posBound = t.segment.indexOf(":");
              if (posBound != -1)
              {
                t.boundSegment = t.segment.substring(0, posBound);
                t.segment = t.segment.substring(posBound + 1);
                int posHash = t.boundSegment.indexOf("#");
                if (posHash != -1)
                {
                  t.boundRepeat = Integer.parseInt(t.boundSegment.substring(posHash + 1));
                  t.boundSegment = t.boundSegment.substring(0, posHash);
                }
              }
              int posHash = t.segment.indexOf("#");
              if (posHash != -1)
              {
                t.repeat = Integer.parseInt(t.segment.substring(posHash + 1));
                t.segment = t.segment.substring(0, posHash);
              }
              if (posDot == -1)
              {
                t.field = Integer.parseInt(line.substring(posDash + 1, posEqual).trim());
              } else
              {
                t.field = Integer.parseInt(line.substring(posDash + 1, posDot).trim());
                t.subfield = Integer.parseInt(line.substring(posDot + 1, posEqual).trim());
              }
              t.value = line.substring(posEqual + 1).trim();
              handleSometimes(t);

              doReplacements(patientType, patient, today, now, connector, t);

              BufferedReader inResult = new BufferedReader(new StringReader(resultText));
              boolean foundBoundStart = false;
              boolean foundBoundEnd = false;
              int boundCount = 0;
              resultText = "";
              String lineResult;
              int repeatCount = 0;
              while ((lineResult = inResult.readLine()) != null)
              {
                lineResult = lineResult.trim();
                if (lineResult.length() > 0)
                {
                  if (t.boundSegment != null && !foundBoundEnd)
                  {
                    boolean skip = false;
                    if (lineResult.startsWith(t.boundSegment + "|"))
                    {
                      boundCount++;
                      if (!foundBoundStart)
                      {
                        if (boundCount == t.boundRepeat)
                        {
                          foundBoundStart = true;
                        }
                      } else if (foundBoundStart)
                      {
                        foundBoundEnd = true;
                      }
                      skip = true;
                    } else if (foundBoundStart)
                    {
                      if (!lineResult.startsWith(t.segment + "|"))
                      {
                        skip = true;
                      }
                    } else
                    {
                      skip = true;
                    }
                    if (skip)
                    {
                      resultText += lineResult + "\r";
                      continue;
                    }
                  }
                  if (lineResult.startsWith(t.segment + "|"))
                  {
                    repeatCount++;
                    if (t.repeat == repeatCount)
                    {
                      int pos = lineResult.indexOf("|");
                      int count = (lineResult.startsWith("MSH|") || lineResult.startsWith("FHS|") || lineResult.startsWith("BHS|")) ? 2 : 1;
                      while (pos != -1 && count < t.field)
                      {
                        pos = lineResult.indexOf("|", pos + 1);
                        count++;
                      }
                      if (pos == -1)
                      {
                        while (count < t.field)
                        {
                          lineResult += "|";
                          count++;
                        }
                        pos = lineResult.length();
                        lineResult += "||";
                      }

                      pos++;
                      count = 1;
                      boolean isMSH2 = lineResult.startsWith("MSH|") && t.field == 2;
                      while (pos != -1 && count < t.subfield)
                      {
                        int posCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                        int endPosBar = lineResult.indexOf("|", pos);
                        if (endPosBar == -1)
                        {
                          endPosBar = lineResult.length();
                        }
                        int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                        if (endPosTilde == -1)
                        {
                          endPosTilde = lineResult.length();
                        }
                        if (posCaret == -1 || (posCaret > endPosBar || posCaret > endPosTilde))
                        {
                          // there's no caret, so add it to value, keep same
                          // position
                          while (count < t.subfield)
                          {
                            t.value = "^" + t.value;
                            count++;
                          }
                          if (endPosTilde < endPosBar)
                          {
                            pos = endPosTilde;
                          } else
                          {
                            pos = endPosBar;
                          }
                          break;
                        } else
                        {
                          pos = posCaret + 1;
                        }
                        count++;
                      }
                      if (pos != -1)
                      {
                        int endPosBar = lineResult.indexOf("|", pos);
                        if (endPosBar == -1)
                        {
                          endPosBar = lineResult.length();
                          lineResult += "|";
                        }
                        int endPosCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                        int endPosRepeat = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                        int endPos = endPosBar;
                        if (endPosRepeat != -1 && endPosRepeat < endPos)
                        {
                          endPos = endPosRepeat;
                        }
                        if (endPosCaret != -1 && endPosCaret < endPos)
                        {
                          endPos = endPosCaret;
                        }
                        String lineNew = lineResult.substring(0, pos);
                        if (t.value.toUpperCase().startsWith("[MAP "))
                        {
                          String oldValue = lineResult.substring(pos, endPos);
                          mapValue(t, oldValue);
                        }
                        lineNew += t.value;
                        lineNew += lineResult.substring(endPos);
                        lineResult = lineNew;
                      }
                    }
                  }
                  resultText += lineResult + "\r";
                }
              }
            }
          }
        }
      }

    } catch (Exception e)
    {
      StringWriter stringWriter = new StringWriter();
      PrintWriter out = new PrintWriter(stringWriter);
      e.printStackTrace(out);
      resultText = "Unable to transform: " + e.getMessage() + "\n" + stringWriter.toString();

    }
    return resultText;
  }

  private void mapValue(Transform t, String oldValue)
  {
    int mapPos = t.value.toUpperCase().indexOf("'" + oldValue.toUpperCase() + "'=>");
    if (mapPos == -1)
    {
      mapPos = t.value.toUpperCase().indexOf("DEFAULT=>");
    }
    if (mapPos != -1)
    {
      mapPos = t.value.indexOf("=>", mapPos) + 2;
      int mapPosLast = t.value.indexOf(",", mapPos);
      if (mapPosLast == -1)
      {
        mapPosLast = t.value.lastIndexOf("]");
        if (mapPosLast == -1)
        {
          mapPosLast = t.value.length();
        }
      }
      String newValue = t.value.substring(mapPos, mapPosLast).trim();
      if (newValue.startsWith("'"))
      {
        newValue = newValue.substring(1);
      }
      if (newValue.endsWith("'"))
      {
        newValue = newValue.substring(0, newValue.length() - 1);
      }
      t.value = newValue;
    } else
    {
      t.value = oldValue;
    }
  }

  private void doReplacements(PatientType patientType, Patient patient, String today, String now, Connector connector, Transform t)
  {
    if (patientType != PatientType.NONE)
    {
      doPatientReplacements(patient, t);
    }
    if (t.value.equalsIgnoreCase("[NOW]"))
    {
      t.value = now;
    } else if (t.value.equalsIgnoreCase("[TODAY]"))
    {
      t.value = today;
    } else if (t.value.equalsIgnoreCase("[CONTROL_ID]"))
    {
      t.value = connector.getCurrentControlId();
    }
    if (connector != null)
    {
      doConnectionReplacements(connector, t);
    }
  }

  private void doConnectionReplacements(Connector connector, Transform t)
  {
    if (t.value.equals(REP_CON_USERID))
    {
      t.value = connector.getUserid();
    } else if (t.value.equals(REP_CON_FACILITYID))
    {
      t.value = connector.getFacilityid();
    } else if (t.value.equals(REP_CON_PASSWORD))
    {
      t.value = connector.getPassword();
    } else if (t.value.equals(REP_CON_FILENAME))
    {
      t.value = connector.getCurrentFilename();
    }
  }

  private void doPatientReplacements(Patient patient, Transform t)
  {
    if (t.value.equals(REP_PAT_BOY))
    {
      t.value = patient.getBoyName();
    } else if (t.value.equals(REP_PAT_GIRL))
    {
      t.value = patient.getGirlName();
    } else if (t.value.equals(REP_PAT_BOY_OR_GIRL))
    {
      t.value = patient.getGender().equals("F") ? patient.getGirlName() : patient.getBoyName();
    } else if (t.value.equals(REP_PAT_GENDER))
    {
      t.value = patient.getGender();
    } else if (t.value.equals(REP_PAT_FATHER))
    {
      t.value = patient.getFatherName();
    } else if (t.value.equals(REP_PAT_SUFFIX))
    {
      t.value = patient.getSuffix();
    } else if (t.value.equals(REP_PAT_MOTHER))
    {
      t.value = patient.getMotherName();
    } else if (t.value.equals(REP_PAT_MOTHER_MAIDEN))
    {
      t.value = patient.getMotherMaidenName();
    } else if (t.value.equals(REP_PAT_DOB))
    {
      t.value = patient.getDates()[0];
    } else if (t.value.equals(REP_PAT_BIRTH_MULTIPLE))
    {
      t.value = patient.getBirthCount() > 1 ? "Y" : "N";
    } else if (t.value.equals(REP_PAT_BIRTH_ORDER))
    {
      t.value = "" + patient.getBirthCount();
    } else if (t.value.equals(REP_PAT_MRN))
    {
      t.value = patient.getMedicalRecordNumber();
    } else if (t.value.equals(REP_PAT_SSN))
    {
      t.value = patient.getSsn();
    } else if (t.value.equals(REP_PAT_RACE))
    {
      t.value = patient.getRace()[0];
    } else if (t.value.equals(REP_PAT_RACE_LABEL))
    {
      t.value = patient.getRace()[1];
    } else if (t.value.equals(REP_PAT_ETHNICITY))
    {
      t.value = patient.getEthnicity()[0];
    } else if (t.value.equals(REP_PAT_ETHNICITY_LABEL))
    {
      t.value = patient.getEthnicity()[1];
    } else if (t.value.equals(REP_PAT_LANGUAGE))
    {
      t.value = patient.getLanguage()[0];
    } else if (t.value.equals("[LANGUAGE_LABEL]"))
    {
      t.value = patient.getLanguage()[1];
    } else if (t.value.equals("[VFC]"))
    {
      t.value = patient.getVfc()[0];
    } else if (t.value.equals("[VFC_LABEL]"))
    {
      t.value = patient.getVfc()[1];
    } else if (t.value.equals("[LAST]"))
    {
      t.value = patient.getLastName();
    } else if (t.value.equals("[FUTURE]"))
    {
      t.value = patient.getFuture();
    } else if (t.value.equals("[LAST_DIFFERENT]"))
    {
      t.value = patient.getDifferentLastName();
    } else if (t.value.equals("[GIRL_MIDDLE]"))
    {
      t.value = patient.getMiddleNameGirl();
    } else if (t.value.equals("[BOY_MIDDLE]"))
    {
      t.value = patient.getMiddleNameBoy();
    } else if (t.value.equals("[BOY_OR_GIRL_MIDDLE]"))
    {
      t.value = patient.getGender().equals("F") ? patient.getMiddleNameGirl() : patient.getMiddleNameBoy();
    } else if (t.value.equals("[GIRL_MIDDLE_INITIAL]"))
    {
      t.value = patient.getMiddleNameGirl().substring(0, 1);
    } else if (t.value.equals("[BOY_MIDDLE_INITIAL]"))
    {
      t.value = patient.getMiddleNameBoy().substring(0, 1);
    } else if (t.value.equals("[VAC1_ID]"))
    {
      t.value = patient.getMedicalRecordNumber() + ".1";
    } else if (t.value.equals("[VAC2_ID]"))
    {
      t.value = patient.getMedicalRecordNumber() + ".2";
    } else if (t.value.equals("[VAC3_ID]"))
    {
      t.value = patient.getMedicalRecordNumber() + ".3";
    } else if (t.value.equals("[VAC1_DATE]"))
    {
      t.value = patient.getDates()[1];
    } else if (t.value.equals("[VAC2_DATE]"))
    {
      t.value = patient.getDates()[2];
    } else if (t.value.equals(REP_PAT_VAC3_DATE))
    {
      t.value = patient.getDates()[3];
    } else if (t.value.equals("[VAC1_CVX]"))
    {
      t.value = patient.getVaccine1()[VACCINE_CVX];
    } else if (t.value.equals("[VAC1_CVX_LABEL]"))
    {
      t.value = patient.getVaccine1()[VACCINE_NAME];
    } else if (t.value.equals("[VAC1_LOT]"))
    {
      t.value = patient.getVaccine1()[VACCINE_LOT];
    } else if (t.value.equals("[VAC1_MVX]"))
    {
      t.value = patient.getVaccine1()[VACCINE_MVX];
    } else if (t.value.equals("[VAC1_MVX_LABEL]"))
    {
      t.value = patient.getVaccine1()[VACCINE_MANUFACTURER];
    } else if (t.value.equals("[VAC1_TRADE_NAME]"))
    {
      t.value = patient.getVaccine1()[VACCINE_TRADE_NAME];
    } else if (t.value.equals("[VAC1_AMOUNT]"))
    {
      t.value = patient.getVaccine1()[VACCINE_AMOUNT];
    } else if (t.value.equals("[VAC1_ROUTE]"))
    {
      t.value = patient.getVaccine1()[VACCINE_ROUTE];
    } else if (t.value.equals("[VAC1_SITE]"))
    {
      t.value = patient.getVaccine1()[VACCINE_SITE];
    } else if (t.value.equals("[VAC1_VIS_PUB_NAME]"))
    {
      t.value = patient.getVaccine1()[VACCINE_VIS_PUB];
    } else if (t.value.equals("[VAC1_VIS_PUB_CODE]"))
    {
      t.value = patient.getVaccine1()[VACCINE_VIS_PUB_CODE];
    } else if (t.value.equals("[VAC1_VIS_PUB_DATE]"))
    {
      t.value = patient.getVaccine1()[VACCINE_VIS_PUB_DATE];
    } else if (t.value.equals("[COMBO_VIS1_PUB_NAME]"))
    {
      t.value = patient.getCombo()[VACCINE_VIS_PUB];
    } else if (t.value.equals("[COMBO_VIS1_PUB_CODE]"))
    {
      t.value = patient.getCombo()[VACCINE_VIS_PUB_CODE];
    } else if (t.value.equals("[COMBO_VIS1_PUB_DATE]"))
    {
      t.value = patient.getCombo()[VACCINE_VIS_PUB_DATE];
    } else if (t.value.equals("[COMBO_VIS2_PUB_NAME]"))
    {
      t.value = patient.getCombo()[VACCINE_VIS2_PUB];
    } else if (t.value.equals("[COMBO_VIS2_PUB_CODE]"))
    {
      t.value = patient.getCombo()[VACCINE_VIS2_PUB_CODE];
    } else if (t.value.equals("[COMBO_VIS2_PUB_DATE]"))
    {
      t.value = patient.getCombo()[VACCINE_VIS2_PUB_DATE];
    } else if (t.value.equals("[COMBO_VIS3_PUB_NAME]"))
    {
      t.value = patient.getCombo()[VACCINE_VIS3_PUB];
    } else if (t.value.equals("[COMBO_VIS3_PUB_CODE]"))
    {
      t.value = patient.getCombo()[VACCINE_VIS3_PUB_CODE];
    } else if (t.value.equals("[COMBO_VIS3_PUB_DATE]"))
    {
      t.value = patient.getCombo()[VACCINE_VIS3_PUB_DATE];
    } else if (t.value.equals("[VAC2_CVX]"))
    {
      t.value = patient.getVaccine2()[VACCINE_CVX];
    } else if (t.value.equals("[VAC2_CVX_LABEL]"))
    {
      t.value = patient.getVaccine2()[VACCINE_NAME];
    } else if (t.value.equals("[VAC2_LOT]"))
    {
      t.value = patient.getVaccine2()[VACCINE_LOT];
    } else if (t.value.equals("[VAC2_MVX]"))
    {
      t.value = patient.getVaccine2()[VACCINE_MVX];
    } else if (t.value.equals("[VAC2_MVX_LABEL]"))
    {
      t.value = patient.getVaccine2()[VACCINE_MANUFACTURER];
    } else if (t.value.equals("[VAC2_TRADE_NAME]"))
    {
      t.value = patient.getVaccine2()[VACCINE_TRADE_NAME];
    } else if (t.value.equals("[VAC2_AMOUNT]"))
    {
      t.value = patient.getVaccine2()[VACCINE_AMOUNT];
    } else if (t.value.equals("[VAC1_ROUTE]"))
    {
      t.value = patient.getVaccine2()[VACCINE_ROUTE];
    } else if (t.value.equals("[VAC1_SITE]"))
    {
      t.value = patient.getVaccine2()[VACCINE_SITE];
    } else if (t.value.equals("[VAC2_VIS_PUB_NAME]"))
    {
      t.value = patient.getVaccine2()[VACCINE_VIS_PUB];
    } else if (t.value.equals("[VAC2_VIS_PUB_CODE]"))
    {
      t.value = patient.getVaccine2()[VACCINE_VIS_PUB_CODE];
    } else if (t.value.equals("[VAC2_VIS_PUB_DATE]"))
    {
      t.value = patient.getVaccine2()[VACCINE_VIS_PUB_DATE];
    } else if (t.value.equals("[VAC3_CVX]"))
    {
      t.value = patient.getVaccine3()[0];
    } else if (t.value.equals("[VAC3_CVX_LABEL]"))
    {
      t.value = patient.getVaccine3()[1];
    } else if (t.value.equals("[VAC3_LOT]"))
    {
      t.value = patient.getVaccine3()[2];
    } else if (t.value.equals("[VAC3_MVX]"))
    {
      t.value = patient.getVaccine3()[3];
    } else if (t.value.equals("[VAC3_MVX_LABEL]"))
    {
      t.value = patient.getVaccine3()[4];
    } else if (t.value.equals("[VAC3_TRADE_NAME]"))
    {
      t.value = patient.getVaccine3()[VACCINE_TRADE_NAME];
    } else if (t.value.equals("[VAC3_AMOUNT]"))
    {
      t.value = patient.getVaccine3()[VACCINE_AMOUNT];
    } else if (t.value.equals("[VAC3_ROUTE]"))
    {
      t.value = patient.getVaccine3()[VACCINE_ROUTE];
    } else if (t.value.equals("[VAC3_SITE]"))
    {
      t.value = patient.getVaccine3()[VACCINE_SITE];
    } else if (t.value.equals("[VAC3_VIS_PUB_NAME]"))
    {
      t.value = patient.getVaccine3()[VACCINE_VIS_PUB];
    } else if (t.value.equals("[VAC3_VIS_PUB_CODE]"))
    {
      t.value = patient.getVaccine3()[VACCINE_VIS_PUB_CODE];
    } else if (t.value.equals("[VAC3_VIS_PUB_DATE]"))
    {
      t.value = patient.getVaccine3()[VACCINE_VIS_PUB_DATE];
    } else if (t.value.equals("[CITY]"))
    {
      t.value = patient.getCity();
    } else if (t.value.equals("[STREET]"))
    {
      t.value = patient.getStreet();
    } else if (t.value.equals("[STATE]"))
    {
      t.value = patient.getState();
    } else if (t.value.equals("[ZIP]"))
    {
      t.value = patient.getZip();
    } else if (t.value.equals(REP_PAT_PHONE))
    {
      t.value = patient.getPhone();
    } else if (t.value.equals(REP_PAT_PHONE_AREA))
    {
      t.value = patient.getPhoneArea();
    } else if (t.value.equals(REP_PAT_PHONE_LOCAL))
    {
      t.value = patient.getPhoneLocal();
    } else if (t.value.equals(REP_PAT_VAC3_DATE))
    {
      t.value = patient.getDates()[3];
    }
  }

  private static int medicalRecordNumberInc = 0;

  protected Patient setupPatient(PatientType patientType) throws IOException
  {
    Patient patient = new Patient();
    if (patientType == PatientType.NONE)
    {
      return patient;
    }
    medicalRecordNumberInc++;
    patient.setMedicalRecordNumber("" + (char) (random.nextInt(26) + 'A') + random.nextInt(10) + random.nextInt(10)
        + (char) (random.nextInt(26) + 'A') + medicalRecordNumberInc);
    patient.setSsn("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
    patient.setBoyName(getValue("BOY"));
    patient.setGirlName(getValue("GIRL"));
    patient.setMotherName(getValue("GIRL"));
    patient.setMotherMaidenName(getValue("LAST_NAME"));
    patient.setFatherName(getValue("BOY"));
    patient.setLastName(getValue("LAST_NAME"));
    patient.setDifferentLastName(getValue("LAST_NAME"));
    patient.setMiddleNameBoy(getValue("BOY"));
    patient.setMiddleNameGirl(getValue("GIRL"));
    String[] dates = new String[4];
    patient.setDates(dates);
    patient.setVaccineType(createDates(dates, patientType));
    patient.setGender(random.nextBoolean() ? "F" : "M");
    patient.setVaccine1(getValueArray("VACCINE_" + patient.getVaccineType(), VACCINE_VIS_PUB_DATE + 1));
    patient.setVaccine2(getValueArray("VACCINE_" + patient.getVaccineType(), VACCINE_VIS_PUB_DATE + 1));
    patient.setVaccine3(getValueArray("VACCINE_" + patient.getVaccineType(), VACCINE_VIS_PUB_DATE + 1));
    patient.setCombo(getValueArray("VACCINE_COMBO", VACCINE_VIS3_PUB_DATE + 1));
    patient.setRace(getValueArray("RACE", 2));
    patient.setEthnicity(getValueArray("ETHNICITY", 2));
    patient.setLanguage(getValueArray("LANGUAGE", 2));
    patient.setAddress(getValueArray("ADDRESS", 4));
    patient.setVfc(getValueArray("VFC", 2));
    patient.setSuffix(getValue("SUFFIX"));
    patient.setStreet((random.nextInt(400) + 1) + " " + getValue("LAST_NAME") + " " + getValue("STREET_ABBREVIATION"));
    patient.setCity(patient.getAddress()[0]);
    patient.setState(patient.getAddress()[1]);
    patient.setZip(patient.getAddress()[2]);
    patient.setPhoneArea(patient.getAddress()[3]);
    patient.setPhoneLocal("" + (random.nextInt(8) + 2) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10));
    patient.setPhone("(" + patient.getPhoneArea() + ")" + patient.getPhoneLocal());
    patient.setBirthCount(makeBirthCount());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, 1);
    patient.setFuture(sdf.format(calendar.getTime()));
    return patient;
  }

  protected int makeBirthCount()
  {
    int birthCount = 1;
    int hat = random.nextInt(100000);
    if (hat < 3220 + 149)
    {
      // chances for twin are 32.2 in 1,000 or 3220 in 100,000
      birthCount = 2;
      if (hat < 149)
      {
        // chances for triplet or higher is is 148.9 in 100,000
        birthCount = 3;
        if (hat < 10)
        {
          birthCount = 4;
          if (hat < 2)
          {
            birthCount = 5;
          }
        }
      }
    }
    return birthCount;
  }
}
