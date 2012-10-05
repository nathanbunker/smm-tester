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

import org.immunizationsoftware.dqa.tester.transform.IssueCreator;
import org.immunizationsoftware.dqa.tester.transform.Patient;

/**
 *
 * @author nathan
 */
public class Transformer {

    private static Map<String, List<String[]>> conceptMap = null;
    private static Random random = new Random();

    public String getValue(String concept) throws IOException {
        return getValue(concept, 0);
    }

    public String getValue(String concept, int pos) throws IOException {
        String value = "";
        if (conceptMap == null) {
            init();
        }
        List<String[]> valueList = conceptMap.get(concept);
        if (valueList != null) {
            String[] values = valueList.get(random.nextInt(valueList.size()));
            if (pos < values.length) {
                value = values[pos];
            }
        }
        return value;
    }

    public String[] getValueArray(String concept, int size) throws IOException {
        if (conceptMap == null) {
            init();
        }
        List<String[]> valueList = conceptMap.get(concept);
        String[] valueSourceList = null;
        if (valueList != null) {
            valueSourceList = valueList.get(random.nextInt(valueList.size()));
        }
        String[] values = new String[size];
        for (int i = 0; i < values.length; i++) {
            if (valueSourceList != null && i < valueSourceList.length) {
                values[i] = valueSourceList[i];
            } else {
                values[i] = "";
            }
        }
        return values;
    }

    protected boolean alsoHas(TestCaseMessage testCaseMessage, String checkString) {
        boolean alsoHas = false;
        for (String extra2 : testCaseMessage.getQuickTransformations()) {
            if (extra2.equals(checkString)) {
                alsoHas = true;
                break;
            }
        }
        return alsoHas;
    }

    protected String createDates(String[] dates) {
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (random.nextBoolean()) {
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

                return "BABY";
            } else {
                if (random.nextBoolean()) {
                    //Setting up toddler
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
                    return "TODDLER";
                } else {
                    //Setting up toddler
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
                    return "TWEEN";
                }
            }
        }
    }

    protected void handleSometimes(Transform t) {
        int sometimes = 0;
        if (t.value.startsWith("~") && t.value.indexOf("%") != -1) {
            int perPos = t.value.indexOf("%");
            try {
                sometimes = Integer.parseInt(t.value.substring(1, perPos));
                t.value = t.value.substring(perPos + 1);
            } catch (NumberFormatException nfe) {
                // ignore
            }
        }
        if (sometimes > 0) {
            String part1 = t.value;
            String part2 = "";
            int colonPos = t.value.indexOf(":");
            if (colonPos != -1) {
                part1 = t.value.substring(0, colonPos);
                part2 = t.value.substring(colonPos + 1);
            }
            if (random.nextInt(100) >= sometimes) {
                t.value = part2;
                handleSometimes(t);
            } else {
                t.value = part1;
            }
        }
    }

    protected void init() throws IOException {
        conceptMap = new HashMap<String, List<String[]>>();
        BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("transform.txt")));
        String line;
        while ((line = in.readLine()) != null) {
            int equals = line.indexOf("=");
            if (equals != -1) {
                String concept = line.substring(0, equals);
                String[] values = line.substring(equals + 1).split("\\,");
                List<String[]> valueList = conceptMap.get(concept);
                if (valueList == null) {
                    valueList = new ArrayList<String[]>();
                    conceptMap.put(concept, valueList);
                }
                valueList.add(values);
            }
        }
    }

    public void transform(TestCaseMessage testCaseMessage) {
        String actualTestCase = testCaseMessage.getTestCaseNumber();
        if (actualTestCase.equals("")) {
            actualTestCase = "NONE";
        }
        String quickTransforms = "";

        if (testCaseMessage.getQuickTransformations() != null) {
            for (String extra : testCaseMessage.getQuickTransformations()) {
                if (extra.equals("BOY")) {
                    quickTransforms += "PID-5=~90%[LAST]:[MOTHER_MAIDEN]\n";
                    quickTransforms += "PID-5.2=[BOY]\n";
                    quickTransforms += "PID-5.3=~60%[BOY_MIDDLE]:~70%[BOY_MIDDLE_INITIAL]\n";
                    quickTransforms += "PID-5.4=~10%[SUFFIX]\n";
                    quickTransforms += "PID-5.7=L\n";
                    if (alsoHas(testCaseMessage, "GIRL")) {
                        quickTransforms += "PID-8=~50%M:F\n";

                    } else {
                        quickTransforms += "PID-8=M\n";
                    }
                } else if (extra.equals("GIRL")) {
                    quickTransforms += "PID-5=~90%[LAST]:[MOTHER_MAIDEN]\n";
                    quickTransforms += "PID-5.2=[GIRL]\n";
                    quickTransforms += "PID-5.3=~60%[GIRL_MIDDLE]:~70%[GIRL_MIDDLE_INITIAL]\n";
                    quickTransforms += "PID-5.7=L\n";
                    quickTransforms += "PID-8=F\n";
                } else if (extra.equals("BOY_OR_GIRL")) {
                    quickTransforms += "PID-5=~90%[LAST]:[MOTHER_MAIDEN]\n";
                    quickTransforms += "PID-5.2=[BOY_OR_GIRL]\n";
                    quickTransforms += "PID-5.3=~60%[BOY_OR_GIRL_MIDDLE]:~70%[GIRL_MIDDLE_INITIAL]\n";
                    quickTransforms += "PID-5.7=L\n";
                    quickTransforms += "PID-8=[GENDER]\n";
                } else if (extra.equals("FATHER")) {
                    if (alsoHas(testCaseMessage, "MOTHER")) {
                        quickTransforms += "NK1#2-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
                        quickTransforms += "NK1#2-2.2=[FATHER]\n";
                        quickTransforms += "NK1#2-3=FTH\n";
                        quickTransforms += "NK1#2-3.2=Father\n";
                        quickTransforms += "NK1#2-3.3=HL70063\n";
                    } else {
                        quickTransforms += "NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
                        quickTransforms += "NK1-2.2=[FATHER]\n";
                        quickTransforms += "NK1-3=FTH\n";
                        quickTransforms += "NK1-3.2=Father\n";
                        quickTransforms += "NK1-3.3=HL70063\n";
                    }
                } else if (extra.equals("MOTHER")) {
                    quickTransforms += "PID-6=~50%[MOTHER_MAIDEN]:[LAST_DIFFERENT]\n";
                    quickTransforms += "NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
                    quickTransforms += "NK1-2.2=[MOTHER]\n";
                    quickTransforms += "NK1-3=MTH\n";
                    quickTransforms += "NK1-3.2=Mother\n";
                    quickTransforms += "NK1-3.3=HL70063\n";
                } else if (extra.equals("DOB")) {
                    quickTransforms += "PID-7=[DOB]\n";
                } else if (extra.equals("TWIN_POSSIBLE")) {
                    quickTransforms += "PID-24=[BIRTH_MULTIPLE]\n";
                    quickTransforms += "PID-25=[BIRTH_ORDER]\n";
                } else if (extra.equals("VAC1_ADMIN")) {
                    int count = 3;
                    count = count - (alsoHas(testCaseMessage, "VAC2_ADMIN") || alsoHas(testCaseMessage, "VAC2_HIST") ? 1 : 0);
                    count = count - (alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0);
                    quickTransforms += "RXA-3=[VAC" + count + "_DATE]\n";
                    quickTransforms += "RXA-9.1=00\n";
                    quickTransforms += "RXA-9.2=Administered\n";
                    quickTransforms += "RXA-9.3=NIP0001\n";
                    quickTransforms += "RXA-5.1=[VAC" + count + "_CVX]\n";
                    quickTransforms += "RXA-5.2=[VAC" + count + "_CVX_LABEL]\n";
                    quickTransforms += "RXA-5.3=CVX\n";
                    quickTransforms += "RXA-6=0.5\n";
                    quickTransforms += "RXA-7=ML\n";
                    quickTransforms += "RXA-15=[VAC" + count + "_LOT]\n";
                    quickTransforms += "RXA-17.1=[VAC" + count + "_MVX]\n";
                    quickTransforms += "RXA-17.2=[VAC" + count + "_MVX_LABEL]\n";
                    quickTransforms += "RXA-17.3=MVX\n";
                    quickTransforms += "RXA-21=A\n";
                    quickTransforms += "RXA:RXR-1.1=IM\n";
                    quickTransforms += "RXA:RXR-1.2=Intramuscular\n";
                    quickTransforms += "RXA:RXR-1.3=HL70162\n";
                    quickTransforms += "RXA:OBX-1=1\n";
                    quickTransforms += "RXA:OBX-2=CE\n";
                    quickTransforms += "RXA:OBX-3.1=64994-7\n";
                    quickTransforms += "RXA:OBX-3.2=Vaccine funding program eligibility category\n";
                    quickTransforms += "RXA:OBX-3.3=LN\n";
                    quickTransforms += "RXA:OBX-5.1=[VFC]\n";
                    quickTransforms += "RXA:OBX-5.2=[VFC_LABEL]\n";
                    quickTransforms += "RXA:OBX-5.3=HL70064\n";
                    quickTransforms += "RXA:OBX-10=F\n";
                    quickTransforms += "RXA:OBX-13=[NOW]\n";
                } else if (extra.equals("VAC2_ADMIN")) {
                    int count = 3;
                    count -= alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0;
                    quickTransforms += "RXA#2-3=[VAC" + count + "_DATE]\n";
                    quickTransforms += "RXA#2-9.1=00\n";
                    quickTransforms += "RXA#2-9.2=Administered\n";
                    quickTransforms += "RXA#2-9.3=NIP0001\n";
                    quickTransforms += "RXA#2-5.1=[VAC" + count + "_CVX]\n";
                    quickTransforms += "RXA#2-5.2=[VAC" + count + "_CVX_LABEL]\n";
                    quickTransforms += "RXA#2-5.3=CVX\n";
                    quickTransforms += "RXA#2-6=0.5\n";
                    quickTransforms += "RXA#2-7=ML\n";
                    quickTransforms += "RXA#2-15=[VAC" + count + "_LOT]\n";
                    quickTransforms += "RXA#2-17.1=[VAC" + count + "_MVX]\n";
                    quickTransforms += "RXA#2-17.2=[VAC" + count + "_MVX_LABEL]\n";
                    quickTransforms += "RXA#2-17.3=MVX\n";
                    quickTransforms += "RXA#2-21=A\n";
                    quickTransforms += "RXA#2:RXR-1.1=IM\n";
                    quickTransforms += "RXA#2:RXR-1.2=Intramuscular\n";
                    quickTransforms += "RXA#2:RXR-1.3=HL70162\n";
                    quickTransforms += "RXA#2:OBX-1=1\n";
                    quickTransforms += "RXA#2:OBX-2=CE\n";
                    quickTransforms += "RXA#2:OBX-3.1=64994-7\n";
                    quickTransforms += "RXA#2:OBX-3.2=Vaccine funding program eligibility category\n";
                    quickTransforms += "RXA#2:OBX-3.3=LN\n";
                    quickTransforms += "RXA#2:OBX-5.1=[VFC]\n";
                    quickTransforms += "RXA#2:OBX-5.2=[VFC_LABEL]\n";
                    quickTransforms += "RXA#2:OBX-5.3=HL70064\n";
                    quickTransforms += "RXA#2:OBX-10=F\n";
                    quickTransforms += "RXA#2:OBX-13=[NOW]\n";
                } else if (extra.equals("VAC3_ADMIN")) {
                    quickTransforms += "RXA#3-3=[VAC3_DATE]\n";
                    quickTransforms += "RXA#3-9.1=00\n";
                    quickTransforms += "RXA#3-9.2=Administered\n";
                    quickTransforms += "RXA#3-9.3=NIP0001\n";
                    quickTransforms += "RXA#3-5.1=[VAC3_CVX]\n";
                    quickTransforms += "RXA#3-5.2=[VAC3_CVX_LABEL]\n";
                    quickTransforms += "RXA#3-5.3=CVX\n";
                    quickTransforms += "RXA#3-6=0.5\n";
                    quickTransforms += "RXA#3-7=ML\n";
                    quickTransforms += "RXA#3-15=[VAC3_LOT]\n";
                    quickTransforms += "RXA#3-17.1=[VAC3_MVX]\n";
                    quickTransforms += "RXA#3-17.2=[VAC3_MVX_LABEL]\n";
                    quickTransforms += "RXA#3-17.3=MVX\n";
                    quickTransforms += "RXA#3-21=A\n";
                    quickTransforms += "RXA#3:RXR-1.1=IM\n";
                    quickTransforms += "RXA#3:RXR-1.2=Intramuscular\n";
                    quickTransforms += "RXA#3:RXR-1.3=HL70162\n";
                    quickTransforms += "RXA#3:OBX-1=1\n";
                    quickTransforms += "RXA#3:OBX-2=CE\n";
                    quickTransforms += "RXA#3:OBX-3.1=64994-7\n";
                    quickTransforms += "RXA#3:OBX-3.2=Vaccine funding program eligibility category\n";
                    quickTransforms += "RXA#3:OBX-3.3=LN\n";
                    quickTransforms += "RXA#3:OBX-5.1=[VFC]\n";
                    quickTransforms += "RXA#3:OBX-5.2=[VFC_LABEL]\n";
                    quickTransforms += "RXA#3:OBX-5.3=HL70064\n";
                    quickTransforms += "RXA#3:OBX-10=F\n";
                    quickTransforms += "RXA#3:OBX-13=[NOW]\n";
                } else if (extra.equals("VAC1_HIST")) {
                    int count = 3;
                    count -= alsoHas(testCaseMessage, "VAC2_ADMIN") || alsoHas(testCaseMessage, "VAC2_HIST") ? 1 : 0;
                    count -= alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0;
                    quickTransforms += "RXA-3=[VAC" + count + "_DATE]\n";
                    quickTransforms += "RXA-5.1=[VAC" + count + "_CVX]\n";
                    quickTransforms += "RXA-5.2=[VAC" + count + "_CVX_LABEL]\n";
                    quickTransforms += "RXA-5.3=CVX\n";
                    quickTransforms += "RXA-6=999\n";
                    quickTransforms += "RXA-9.1=01\n";
                    quickTransforms += "RXA-9.2=Historical\n";
                    quickTransforms += "RXA-9.3=NIP0001\n";
                    quickTransforms += "RXA-21=A\n";
                } else if (extra.equals("VAC2_HIST")) {
                    int count = 3;
                    count -= alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1 : 0;
                    quickTransforms += "RXA#2-3=[VAC" + count + "_DATE]\n";
                    quickTransforms += "RXA#2-5.1=[VAC" + count + "_CVX]\n";
                    quickTransforms += "RXA#2-5.2=[VAC" + count + "_CVX_LABEL]\n";
                    quickTransforms += "RXA#2-5.3=CVX\n";
                    quickTransforms += "RXA#2-9.1=01\n";
                    quickTransforms += "RXA#2-6=999\n";
                    quickTransforms += "RXA#2-9.2=Historical\n";
                    quickTransforms += "RXA#2-9.3=NIP0001\n";
                    quickTransforms += "RXA#2-21=A\n";
                } else if (extra.equals("VAC3_HIST")) {
                    quickTransforms += "RXA#3-3=[VAC2_DATE]\n";
                    quickTransforms += "RXA#3-5.1=[VAC3_CVX]\n";
                    quickTransforms += "RXA#3-5.2=[VAC3_CVX_LABEL]\n";
                    quickTransforms += "RXA#3-5.3=CVX\n";
                    quickTransforms += "RXA#3-9.1=01\n";
                    quickTransforms += "RXA#3-6=999\n";
                    quickTransforms += "RXA#3-9.2=Historical\n";
                    quickTransforms += "RXA#3-9.3=NIP0001\n";
                    quickTransforms += "RXA#3-21=A\n";
                } else if (extra.equals("ADDRESS")) {
                    quickTransforms += "PID-11.1=[STREET]\n";
                    quickTransforms += "PID-11.3=[CITY]\n";
                    quickTransforms += "PID-11.4=[STATE]\n";
                    quickTransforms += "PID-11.5=[ZIP]\n";
                    quickTransforms += "PID-11.6=USA\n";
                } else if (extra.equals("PHONE")) {
                    quickTransforms += "PID-13.1=[PHONE]\n";
                    quickTransforms += "PID-13.2=PRN\n";
                    quickTransforms += "PID-13.3=PH\n";
                    quickTransforms += "PID-13.6=[PHONE_AREA]\n";
                    quickTransforms += "PID-13.7=[PHONE_LOCAL]\n";
                } else if (extra.equals("RACE")) {
                    quickTransforms += "PID-10.1=[RACE]\n";
                    quickTransforms += "PID-10.2=[RACE_LABEL]\n";
                    quickTransforms += "PID-10.3=HL7005\n";
                } else if (extra.equals("ETHNICITY")) {
                    quickTransforms += "PID-22.1=[ETHNICITY]\n";
                    quickTransforms += "PID-22.2=[ETHNICITY_LABEL]\n";
                    quickTransforms += "PID-22.3=HL70189\n";
                } else if (extra.equals("2.5.1")) {
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
                    quickTransforms += "ORC-3=" + actualTestCase + ".1\n";
                    quickTransforms += "ORC-3.2=OIS\n";
                    quickTransforms += "RXA-1=0\n";
                    quickTransforms += "RXA-2=1\n";
                    quickTransforms += "RXA-6=999\n";
                    quickTransforms += "ORC#2-1=RE\n";
                    quickTransforms += "ORC#2-3=" + actualTestCase + ".2\n";
                    quickTransforms += "ORC#2-3.2=OIS\n";
                    quickTransforms += "RXA#2-1=0\n";
                    quickTransforms += "RXA#2-2=1\n";
                    quickTransforms += "RXA#2-6=999\n";
                    quickTransforms += "ORC#3-1=RE\n";
                    quickTransforms += "ORC#3-3=" + actualTestCase + ".3\n";
                    quickTransforms += "ORC#3-3.2=OIS\n";
                    quickTransforms += "RXA#3-1=0\n";
                    quickTransforms += "RXA#3-2=1\n";
                    quickTransforms += "RXA#3-6=999\n";
                    quickTransforms += "RXR-1=OTH\n";
                    quickTransforms += "RXR#2-1=OTH\n";
                    quickTransforms += "RXR#3-1=OTH\n";
                } else if (extra.equals("2.3.1")) {
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
        String result = transform(testCaseMessage.getPreparedMessage(), transforms);
        testCaseMessage.setMessageText(result);
        testCaseMessage.setQuickTransformationsConverted(quickTransforms);
    }

    protected String transform(String baseText, String transformText) {
        String resultText = baseText;
        try {
            List<Transform> transforms = new ArrayList<Transform>();
            BufferedReader inTransform = new BufferedReader(new StringReader(transformText));
            Patient patient = setupPatient();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String today = sdf.format(new Date());
            sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String now = sdf.format(new Date());
            String line;
            while ((line = inTransform.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    int posEqual = line.indexOf("=");
                    int posDash = line.indexOf("-");
                    int posDot = line.indexOf(".");
                    if (posDash > posEqual) {
                        posDash = -1;
                    }
                    if (posDot > posEqual) {
                        posDot = -1;
                    }
                    if (posEqual != -1 && posDash != -1 && posDash < posEqual && (posDot == -1 || (posDot > posDash && posDot < posEqual))) {
                        Transform t = new Transform();
                        t.segment = line.substring(0, posDash).trim();
                        int posBound = t.segment.indexOf(":");
                        if (posBound != -1) {
                            t.boundSegment = t.segment.substring(0, posBound);
                            t.segment = t.segment.substring(posBound + 1);
                            int posHash = t.boundSegment.indexOf("#");
                            if (posHash != -1) {
                                t.boundRepeat = Integer.parseInt(t.boundSegment.substring(posHash + 1));
                                t.boundSegment = t.boundSegment.substring(0, posHash);
                            }
                        }
                        int posHash = t.segment.indexOf("#");
                        if (posHash != -1) {
                            t.repeat = Integer.parseInt(t.segment.substring(posHash + 1));
                            t.segment = t.segment.substring(0, posHash);
                        }
                        if (posDot == -1) {
                            t.field = Integer.parseInt(line.substring(posDash + 1, posEqual).trim());
                        } else {
                            t.field = Integer.parseInt(line.substring(posDash + 1, posDot).trim());
                            t.subfield = Integer.parseInt(line.substring(posDot + 1, posEqual).trim());
                        }
                        t.value = line.substring(posEqual + 1).trim();
                        handleSometimes(t);
                        if (t.value.equals("[BOY]")) {
                            t.value = patient.getBoyName();
                        } else if (t.value.equals("[GIRL]")) {
                            t.value = patient.getGirlName();
                        } else if (t.value.equals("[BOY_OR_GIRL]")) {
                            t.value = patient.getGender().equals("F") ? patient.getGirlName() : patient.getBoyName();
                        } else if (t.value.equals("[GENDER]")) {
                            t.value = patient.getGender();
                        } else if (t.value.equals("[FATHER]")) {
                            t.value = patient.getFatherName();
                        } else if (t.value.equals("[SUFFIX]")) {
                            t.value = patient.getSuffix();
                        } else if (t.value.equals("[MOTHER]")) {
                            t.value = patient.getMotherName();
                        } else if (t.value.equals("[MOTHER_MAIDEN]")) {
                            t.value = patient.getMotherMaidenName();
                        } else if (t.value.equals("[DOB]")) {
                            t.value = patient.getDates()[0];
                        } else if (t.value.equals("[BIRTH_MULTIPLE]")) {
                            t.value = patient.getBirthCount() > 1 ? "Y" : "N";
                        } else if (t.value.equals("[BIRTH_ORDER]")) {
                            t.value = "" + patient.getBirthCount();
                        } else if (t.value.equals("[NOW]")) {
                            t.value = now;
                        } else if (t.value.equals("[MRN]")) {
                            t.value = patient.getMedicalRecordNumber();
                        } else if (t.value.equals("[SSN]")) {
                          t.value = patient.getSsn();
                        } else if (t.value.equals("[RACE]")) {
                            t.value = patient.getRace()[0];
                        } else if (t.value.equals("[RACE_LABEL]")) {
                            t.value = patient.getRace()[1];
                        } else if (t.value.equals("[ETHNICITY]")) {
                            t.value = patient.getEthnicity()[0];
                        } else if (t.value.equals("[ETHNICITY_LABEL]")) {
                            t.value = patient.getEthnicity()[1];
                        } else if (t.value.equals("[LANGUAGE]")) {
                            t.value = patient.getLanguage()[0];
                        } else if (t.value.equals("[LANGUAGE_LABEL]")) {
                            t.value = patient.getLanguage()[1];
                        } else if (t.value.equals("[VFC]")) {
                            t.value = patient.getVfc()[0];
                        } else if (t.value.equals("[VFC_LABEL]")) {
                            t.value = patient.getVfc()[1];
                        } else if (t.value.equals("[TODAY]")) {
                            t.value = today;
                        } else if (t.value.equals("[LAST]")) {
                            t.value = patient.getLastName();
                        } else if (t.value.equals("[FUTURE]")) {
                            t.value = patient.getFuture();
                        } else if (t.value.equals("[LAST_DIFFERENT]")) {
                            t.value = patient.getDifferentLastName();
                        } else if (t.value.equals("[GIRL_MIDDLE]")) {
                            t.value = patient.getMiddleNameGirl();
                        } else if (t.value.equals("[BOY_MIDDLE]")) {
                            t.value = patient.getMiddleNameBoy();
                        } else if (t.value.equals("[BOY_OR_GIRL_MIDDLE]")) {
                            t.value = patient.getGender().equals("F") ? patient.getMiddleNameGirl() : patient.getMiddleNameBoy();
                        } else if (t.value.equals("[GIRL_MIDDLE_INITIAL]")) {
                            t.value = patient.getMiddleNameGirl().substring(0, 1);
                        } else if (t.value.equals("[BOY_MIDDLE_INITIAL]")) {
                            t.value = patient.getMiddleNameBoy().substring(0, 1);
                        } else if (t.value.equals("[VAC1_DATE]")) {
                            t.value = patient.getDates()[1];
                        } else if (t.value.equals("[VAC2_DATE]")) {
                            t.value = patient.getDates()[2];
                        } else if (t.value.equals("[VAC3_DATE]")) {
                            t.value = patient.getDates()[3];
                        } else if (t.value.equals("[VAC1_CVX]")) {
                            t.value = patient.getVaccine1()[0];
                        } else if (t.value.equals("[VAC1_CVX_LABEL]")) {
                            t.value = patient.getVaccine1()[1];
                        } else if (t.value.equals("[VAC1_LOT]")) {
                            t.value = patient.getVaccine1()[2];
                        } else if (t.value.equals("[VAC1_MVX]")) {
                            t.value = patient.getVaccine1()[3];
                        } else if (t.value.equals("[VAC1_MVX_LABEL]")) {
                            t.value = patient.getVaccine1()[4];
                        } else if (t.value.equals("[VAC2_CVX]")) {
                            t.value = patient.getVaccine2()[0];
                        } else if (t.value.equals("[VAC2_CVX_LABEL]")) {
                            t.value = patient.getVaccine2()[1];
                        } else if (t.value.equals("[VAC2_LOT]")) {
                            t.value = patient.getVaccine2()[2];
                        } else if (t.value.equals("[VAC2_MVX]")) {
                            t.value = patient.getVaccine2()[3];
                        } else if (t.value.equals("[VAC2_MVX_LABEL]")) {
                            t.value = patient.getVaccine2()[4];
                        } else if (t.value.equals("[VAC3_CVX]")) {
                            t.value = patient.getVaccine3()[0];
                        } else if (t.value.equals("[VAC3_CVX_LABEL]")) {
                            t.value = patient.getVaccine3()[1];
                        } else if (t.value.equals("[VAC3_LOT]")) {
                            t.value = patient.getVaccine3()[2];
                        } else if (t.value.equals("[VAC3_MVX]")) {
                            t.value = patient.getVaccine3()[3];
                        } else if (t.value.equals("[VAC3_MVX_LABEL]")) {
                            t.value = patient.getVaccine3()[4];
                        } else if (t.value.equals("[CITY]")) {
                            t.value = patient.getCity();
                        } else if (t.value.equals("[STREET]")) {
                            t.value = patient.getStreet();
                        } else if (t.value.equals("[STATE]")) {
                            t.value = patient.getState();
                        } else if (t.value.equals("[ZIP]")) {
                            t.value = patient.getZip();
                        } else if (t.value.equals("[PHONE]")) {
                            t.value = patient.getPhone();
                        } else if (t.value.equals("[PHONE_AREA]")) {
                            t.value = patient.getPhoneArea();
                        } else if (t.value.equals("[PHONE_LOCAL]")) {
                            t.value = patient.getPhoneLocal();
                        } else if (t.value.equals("[VAC3_DATE]")) {
                            t.value = patient.getDates()[3];
                        }

                        BufferedReader inResult = new BufferedReader(new StringReader(resultText));
                        boolean foundBoundStart = false;
                        boolean foundBoundEnd = false;
                        int boundCount = 0;
                        resultText = "";
                        String lineResult;
                        int repeatCount = 0;
                        while ((lineResult = inResult.readLine()) != null) {
                            lineResult = lineResult.trim();
                            if (lineResult.length() > 0) {
                                if (t.boundSegment != null && !foundBoundEnd) {
                                    boolean skip = false;
                                    if (lineResult.startsWith(t.boundSegment + "|")) {
                                        boundCount++;
                                        if (!foundBoundStart) {
                                            if (boundCount == t.boundRepeat) {
                                                foundBoundStart = true;
                                            }
                                        } else if (foundBoundStart) {
                                            foundBoundEnd = true;
                                        }
                                        skip = true;
                                    } else if (foundBoundStart) {
                                        if (!lineResult.startsWith(t.segment + "|")) {
                                            skip = true;
                                        }
                                    } else {
                                        skip = true;
                                    }
                                    if (skip) {
                                        resultText += lineResult + "\r";
                                        continue;
                                    }
                                }
                                if (lineResult.startsWith(t.segment + "|")) {
                                    repeatCount++;
                                    if (t.repeat == repeatCount) {
                                        int pos = lineResult.indexOf("|");
                                        int count = lineResult.startsWith("MSH|") ? 2 : 1;
                                        while (pos != -1 && count < t.field) {
                                            pos = lineResult.indexOf("|", pos + 1);
                                            count++;
                                        }
                                        if (pos == -1) {
                                            while (count < t.field) {
                                                lineResult += "|";
                                                count++;
                                            }
                                            pos = lineResult.length();
                                            lineResult += "||";
                                        }

                                        pos++;
                                        count = 1;
                                        boolean isMSH2 = lineResult.startsWith("MSH|") && t.field == 2;
                                        while (pos != -1 && count < t.subfield) {
                                            int posCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                                            int endPosBar = lineResult.indexOf("|", pos);
                                            if (endPosBar == -1) {
                                                endPosBar = lineResult.length();
                                            }
                                            int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                                            if (endPosTilde == -1) {
                                                endPosTilde = lineResult.length();
                                            }
                                            if (posCaret == -1 || (posCaret > endPosBar || posCaret > endPosTilde)) {
                                                // there's no caret, so add it to value, keep same position
                                                while (count < t.subfield) {
                                                    t.value = "^" + t.value;
                                                    count++;
                                                }
                                                if (endPosTilde < endPosBar) {
                                                    pos = endPosTilde;
                                                } else {
                                                    pos = endPosBar;
                                                }
                                                break;
                                            } else {
                                                pos = posCaret + 1;
                                            }
                                            count++;
                                        }
                                        if (pos != -1) {
                                            int endPosBar = lineResult.indexOf("|", pos);
                                            if (endPosBar == -1) {
                                                endPosBar = lineResult.length();
                                                lineResult += "|";
                                            }
                                            int endPosCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                                            int endPosRepeat = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                                            int endPos = endPosBar;
                                            if (endPosRepeat != -1 && endPosRepeat < endPos) {
                                                endPos = endPosRepeat;
                                            }
                                            if (endPosCaret != -1 && endPosCaret < endPos) {
                                                endPos = endPosCaret;
                                            }
                                            String lineNew = lineResult.substring(0, pos);
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

        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter out = new PrintWriter(stringWriter);
            e.printStackTrace(out);
            resultText = "Unable to transform: " + e.getMessage() + "\n" + stringWriter.toString();

        }
        return resultText;
    }
    private static int medicalRecordNumberInc = 0;

    protected Patient setupPatient() throws IOException {
        Patient patient = new Patient();
        medicalRecordNumberInc++;
        patient.setMedicalRecordNumber("" + (char)(random.nextInt(26) + 'A') + medicalRecordNumberInc);
        patient.setSsn("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
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
        patient.setVaccineType(createDates(dates));
        patient.setGender(random.nextBoolean() ? "F" : "M");
        patient.setVaccine1(getValueArray("VACCINE_" + patient.getVaccineType(), 5));
        patient.setVaccine2(getValueArray("VACCINE_" + patient.getVaccineType(), 5));
        patient.setVaccine3(getValueArray("VACCINE_" + patient.getVaccineType(), 5));
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
        patient.setPhoneLocal("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + "-" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
        patient.setPhone("(" + patient.getPhoneArea() + ")" + patient.getPhoneLocal());
        patient.setBirthCount(makeBirthCount());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        patient.setFuture(sdf.format(calendar.getTime()));
        return patient;
    }

    protected int makeBirthCount() {
        int birthCount = 1;
        int hat = random.nextInt(100000);
        if (hat < 3220 + 149) {
            // chances for twin are 32.2 in 1,000 or 3220 in 100,000
            birthCount = 2;
            if (hat < 149) {
                // chances for triplet or higher is is 148.9 in  100,000
                birthCount = 3;
                if (hat < 10) {
                    birthCount = 4;
                    if (hat < 2) {
                        birthCount = 5;
                    }
                }
            }
        }
        return birthCount;
    }
}
