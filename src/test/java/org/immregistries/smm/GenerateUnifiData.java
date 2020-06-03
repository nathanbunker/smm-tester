package org.immregistries.smm;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GenerateUnifiData {

  private static class Bracket {
    private int expectedStart = 0;
    private int expectedEnd = 0;
    private int expectedProb = 0;
    private int expectedProbDtap = 0;
    private int expectedProbTdap = 0;
    //    private int expectedProbTd = 0;
    private int alternativeStart = 0;
    private int alternativeEnd = 0;
    private int alternativeProb = 0;
    private int alternativeProbDtap = 0;
    private int alternativeProbTdap = 0;
    //    private int alternativeProbTd = 0;

    public Bracket(int[] v) {
      expectedStart = v[0];
      expectedEnd = v[1];
      expectedProb = v[2];
      expectedProbDtap = v[3];
      expectedProbTdap = v[4];
      //      expectedProbTd = v[5];
      alternativeStart = v[6];
      alternativeEnd = v[7];
      alternativeProb = v[8];
      alternativeProbDtap = v[9];
      alternativeProbTdap = v[10];
      //      alternativeProbTd = v[11];
      // DTaP 20
      // tdap 115
      // td 196
    }

  }

  public static class VaccinationEvent {
    private Date adminDate;
    private String cvxCode;
  }

  public static void main(String[] args) throws Exception {

    File patFile = new File("c:/dev/immregistries/smm-tester/cc4.pat.csv");
    File vacFile = new File("c:/dev/immregistries/smm-tester/cc4.vac.csv");

    PrintWriter patOut = new PrintWriter(new FileWriter(patFile));
    PrintWriter vacOut = new PrintWriter(new FileWriter(vacFile));

    patOut.println(
        "\"Grantee\",\"FPCode\",\"PatientID\",\"DOB\",\"DemEstDate\",\"Race1\",\"Race2\",\"Race3\",\"Race4\",\"Race5\",\"Race6\",\"Ethnicity\",\"Sex\",\"Status\",\"VaricellaImmune\",\"P_Eligibility\",\"P_Eligibility_Date\",\"ZipCode\"");
    vacOut.println(
        "\"PatientID\",\"VaxDate\",\"MVX\",\"CVX\",\"TradeName\",\"LotNum1\",\"LotNum2\",\"LotNum3\",\"Route\",\"Site\",\"RecordType\",\"VaxComplete\",\"VaxRefusal\",\"IISValidity\",\"V_Eligibility\",\"VaxEntDate\",\"ProvChar\"");

    List<Bracket> bracketList = new ArrayList<>();
    bracketList.add(new Bracket(new int[] {0, 1, 1, 100, 0, 0, 1, 6, 1, 100, 0, 0}));
    bracketList.add(new Bracket(new int[] {6, 9, 95, 100, 0, 0, 9, 16, 5, 100, 0, 0}));
    bracketList.add(new Bracket(new int[] {16, 20, 90, 100, 0, 0, 20, 26, 10, 100, 0, 0}));
    bracketList.add(new Bracket(new int[] {26, 30, 80, 100, 0, 0, 30, 68, 20, 100, 0, 0}));
    bracketList.add(new Bracket(new int[] {68, 78, 70, 99, 1, 0, 78, 208, 30, 100, 0, 0}));
    bracketList.add(new Bracket(new int[] {208, 364, 70, 100, 0, 0, 364, 572, 10, 0, 95, 5}));
    bracketList.add(new Bracket(new int[] {572, 676, 70, 1, 99, 0, 676, 936, 30, 1, 99, 0}));
    bracketList.add(new Bracket(new int[] {936, 1196, 70, 0, 100, 0, 1196, 1456, 30, 0, 100, 0}));
    bracketList.add(new Bracket(new int[] {1456, 1716, 70, 0, 100, 0, 1716, 1976, 30, 0, 100, 0}));
    bracketList.add(new Bracket(new int[] {1976, 2236, 70, 0, 100, 0, 2236, 2496, 30, 0, 100, 0}));
    bracketList.add(new Bracket(new int[] {2496, 2756, 70, 0, 100, 0, 2756, 3016, 30, 0, 100, 0}));
    bracketList.add(new Bracket(new int[] {3016, 3276, 70, 0, 100, 0, 3276, 3536, 30, 0, 100, 0}));
    bracketList.add(new Bracket(new int[] {3536, 3796, 70, 0, 100, 0, 3796, 4056, 30, 0, 100, 0}));

    Random random = new Random();

    String patientMrnStart = "M" + random.nextInt(100000) + "-";

    int patientCount = 500000;
    for (int i = 0; i < patientCount; i++) {
      int bracketPos = random.nextInt(bracketList.size());
      Bracket ageBracket = bracketList.get(bracketPos);
      Calendar calendarBirthDate = Calendar.getInstance();
      calendarBirthDate.add(Calendar.DAY_OF_MONTH, ageBracket.alternativeEnd * -7);
      calendarBirthDate.add(Calendar.DAY_OF_MONTH,
          random.nextInt((ageBracket.alternativeEnd - ageBracket.alternativeStart) * 7)
              + ageBracket.alternativeStart);
      Date dateOfBirth = calendarBirthDate.getTime();

      Date today = new Date();

      int zipPos = random.nextInt(ZIPS.length);
      double zipPer = (0.2 * zipPos / ZIPS.length) + 0.8;

      int zip = ZIPS[zipPos];

      List<VaccinationEvent> vaccinationEventList = new ArrayList<>();
      for (int j = bracketPos; j >= 0; j--) {
        Bracket vacBracket = bracketList.get(j);
        int vacDateProb = random.nextInt(100);
        int vacCodeProb = random.nextInt(100);
        int dtapProb = 0;
        int tdapProb = 0;
        int startWeek = 0;
        int endWeek = 0;
        if (vacDateProb < vacBracket.expectedProb) {
          startWeek = vacBracket.expectedStart;
          endWeek = vacBracket.expectedEnd;
          dtapProb = vacBracket.expectedProbDtap;
          tdapProb = vacBracket.expectedProbTdap;
        } else if (vacDateProb < (vacBracket.expectedProb + vacBracket.alternativeProb)) {
          startWeek = vacBracket.alternativeStart;
          endWeek = vacBracket.alternativeEnd;
          dtapProb = vacBracket.alternativeProbDtap;
          tdapProb = vacBracket.alternativeProbTdap;
        }
        if (endWeek > 0) {
          Calendar calendarAdminDate = Calendar.getInstance();
          calendarAdminDate.setTime(dateOfBirth);
          calendarAdminDate.add(Calendar.DAY_OF_MONTH, startWeek * 7);
          calendarAdminDate.add(Calendar.DAY_OF_MONTH, random.nextInt((endWeek - startWeek) * 7));
          if (calendarAdminDate.getTime().after(today)) {
            calendarAdminDate.setTime(today);
            calendarAdminDate.add(Calendar.DAY_OF_MONTH, -random.nextInt(30));
          }
          boolean recorded = true;
          int vacYear = calendarAdminDate.get(Calendar.YEAR);
          if (vacYear > 2010) {
            recorded = random.nextInt(100) < (zipPer * 100);
          } else if (vacYear > 2000) {
            recorded = random.nextInt(100) < (zipPer * 85);
          } else if (vacYear > 1990) {
            recorded = random.nextInt(100) < (zipPer * 50);
          } else {
            recorded = false;
          }

          if (recorded) {
            VaccinationEvent v = new VaccinationEvent();
            v.adminDate = calendarAdminDate.getTime();
            if (vacCodeProb < dtapProb) {
              v.cvxCode = "20";
            } else if (vacCodeProb < (dtapProb + tdapProb)) {
              v.cvxCode = "115";
            } else {
              v.cvxCode = "196";
            }

            vaccinationEventList.add(v);
          }
        }
      }

      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      if (false) {
        System.out.print(sdf.format(dateOfBirth));
        for (VaccinationEvent ve : vaccinationEventList) {
          System.out.print("|" + sdf.format(ve.adminDate));
        }
        System.out.println();
      } else {
        if (i % 1000 == 0) {
          System.out.println(i);
        }
      }
      String mrn = patientMrnStart + i;
      print(3, "AIRA", patOut);
      print(5, "", patOut);// FIPS code
      print(15, mrn, patOut);
      print(8, sdf.format(dateOfBirth), patOut);
      print(8, "", patOut); // Demographic record establishment date
      print(6, "", patOut); // Race 1
      print(6, "", patOut); // Race 2
      print(6, "", patOut); // Race 3
      print(6, "", patOut); // Race 4
      print(6, "", patOut); // Race 5
      print(6, "", patOut); // Race 6
      print(6, "", patOut); // Ethnicity
      print(1, random.nextBoolean() ? "M" : "F", patOut); // Sex
      print(1, "A", patOut); // Status
      print(1, "", patOut); // Varicella Immune
      print(3, "", patOut); // Person-level Eligibility 
      print(8, "", patOut); // Person-level Eligibility Date
      print(10, "" + zip, patOut);
      patOut.println();

      for (VaccinationEvent ve : vaccinationEventList) {
        print(15, mrn, vacOut);
        print(8, sdf.format(ve.adminDate), vacOut);
        print(3, "", vacOut);
        print(3, ve.cvxCode, vacOut);
        print(3, "", vacOut); // Tradename
        print(50, "", vacOut); // Lot Number 1
        print(50, "", vacOut); // Lot Number 2
        print(50, "", vacOut); // Lot Number 3
        print(3, "", vacOut); // Route
        print(4, "", vacOut); // Site
        print(2, "01", vacOut); // Record Type
        print(2, "CP", vacOut); // Complete Status
        print(2, "", vacOut); // Substance refusal reason
        print(1, "", vacOut); // IIS validity
        print(3, "", vacOut); // Vaccination level eligibility
        print(8, "", vacOut); // Date shot was entered
        print(3, "", vacOut); // Provider characteristics
        vacOut.println();
      }

    }
    vacOut.close();
    patOut.close();
  }

  static final boolean FIXED_WIDTH = false;

  private static void print(int length, String value, PrintWriter out) {
    if (FIXED_WIDTH) {
      while (value.length() < length) {
        value = value + " ";
      }
      if (value.length() > length) {
        value = value.substring(0, length);
      }
      out.print(value);
    } else {
      out.print("\"" + value + "\",");
    }
  }

  private static final int[] ZIPS =
      {48001, 48002, 48003, 48004, 48005, 48006, 48007, 48009, 48012, 48014, 48015, 48017, 48021,
          48022, 48023, 48025, 48026, 48027, 48028, 48030, 48032, 48033, 48034, 48035, 48036, 48037,
          48038, 48039, 48040, 48041, 48042, 48043, 48044, 48045, 48046, 48047, 48048, 48049, 48050,
          48051, 48054, 48059, 48060, 48061, 48062, 48063, 48064, 48065, 48066, 48067, 48068, 48069,
          48070, 48071, 48072, 48073, 48074, 48075, 48076, 48079, 48080, 48081, 48082, 48083, 48084,
          48085, 48086, 48088, 48089, 48090, 48091, 48092, 48093, 48094, 48095, 48096, 48097, 48098,
          48099, 48101, 48103, 48104, 48105, 48106, 48107, 48108, 48109, 48110, 48111, 48112, 48113,
          48114, 48115, 48116, 48117, 48118, 48120, 48121, 48122, 48123, 48124, 48125, 48126, 48127,
          48128, 48130, 48131, 48133, 48134, 48135, 48136, 48137, 48138, 48139, 48140, 48141, 48143,
          48144, 48145, 48146, 48150, 48151, 48152, 48153, 48154, 48157, 48158, 48159, 48160, 48161,
          48162, 48164, 48165, 48166, 48167, 48168, 48169, 48170, 48173, 48174, 48175, 48176, 48177,
          48178, 48179, 48180, 48182, 48183, 48184, 48185, 48186, 48187, 48188, 48189, 48190, 48191,
          48192, 48193, 48195, 48197, 48198, 48201, 48202, 48203, 48204, 48205, 48206, 48207, 48208,
          48209, 48210, 48211, 48212, 48213, 48214, 48215, 48216, 48217, 48218, 48219, 48220, 48221,
          48222, 48223, 48224, 48225, 48226, 48227, 48228, 48229, 48230, 48231, 48232, 48233, 48234,
          48235, 48236, 48237, 48238, 48239, 48240, 48242, 48243, 48244, 48255, 48260, 48264, 48265,
          48266, 48267, 48268, 48269, 48272, 48275, 48277, 48278, 48279, 48288, 48301, 48302, 48303,
          48304, 48306, 48307, 48308, 48309, 48310, 48311, 48312, 48313, 48314, 48315, 48316, 48317,
          48318, 48320, 48321, 48322, 48323, 48324, 48325, 48326, 48327, 48328, 48329, 48330, 48331,
          48332, 48333, 48334, 48335, 48336, 48340, 48341, 48342, 48343, 48346, 48347, 48348, 48350,
          48353, 48356, 48357, 48359, 48360, 48361, 48362, 48363, 48366, 48367, 48370, 48371, 48374,
          48375, 48376, 48377, 48380, 48381, 48382, 48383, 48386, 48387, 48390, 48391, 48393, 48397,
          48401, 48410, 48411, 48412, 48413, 48414, 48415, 48416, 48417, 48418, 48419, 48420, 48421,
          48422, 48423, 48426, 48427, 48428, 48429, 48430, 48432, 48433, 48434, 48435, 48436, 48437,
          48438, 48439, 48440, 48441, 48442, 48444, 48445, 48446, 48449, 48450, 48451, 48453, 48454,
          48455, 48456, 48457, 48458, 48460, 48461, 48462, 48463, 48464, 48465, 48466, 48467, 48468,
          48469, 48470, 48471, 48472, 48473, 48475, 48476, 48480, 48501, 48502, 48503, 48504, 48505,
          48506, 48507, 48509, 48519, 48529, 48531, 48532, 48550, 48551, 48552, 48553, 48554, 48555,
          48556, 48557, 48559, 48601, 48602, 48603, 48604, 48605, 48606, 48607, 48608, 48609, 48610,
          48611, 48612, 48613, 48614, 48615, 48616, 48617, 48618, 48619, 48620, 48621, 48622, 48623,
          48624, 48625, 48626, 48627, 48628, 48629, 48630, 48631, 48632, 48633, 48634, 48635, 48636,
          48637, 48638, 48640, 48641, 48642, 48647, 48649, 48650, 48651, 48652, 48653, 48654, 48655,
          48656, 48657, 48658, 48659, 48661, 48662, 48663, 48667, 48670, 48674, 48686, 48701, 48703,
          48705, 48706, 48707, 48708, 48710, 48720, 48721, 48722, 48723, 48724, 48725, 48726, 48727,
          48728, 48729, 48730, 48731, 48732, 48733, 48734, 48735, 48736, 48737, 48738, 48739, 48740,
          48741, 48742, 48743, 48744, 48745, 48746, 48747, 48748, 48749, 48750, 48754, 48755, 48756,
          48757, 48758, 48759, 48760, 48761, 48762, 48763, 48764, 48765, 48766, 48767, 48768, 48769,
          48770, 48787, 48801, 48802, 48804, 48805, 48806, 48807, 48808, 48809, 48811, 48812, 48813,
          48815, 48816, 48817, 48818, 48819, 48820, 48821, 48822, 48823, 48824, 48825, 48826, 48827,
          48829, 48830, 48831, 48832, 48833, 48834, 48835, 48836, 48837, 48838, 48840, 48841, 48842,
          48843, 48844, 48845, 48846, 48847, 48848, 48849, 48850, 48851, 48852, 48853, 48854, 48855,
          48856, 48857, 48858, 48859, 48860, 48861, 48862, 48863, 48864, 48865, 48866, 48867, 48870,
          48871, 48872, 48873, 48874, 48875, 48876, 48877, 48878, 48879, 48880, 48881, 48882, 48883,
          48884, 48885, 48886, 48887, 48888, 48889, 48890, 48891, 48892, 48893, 48894, 48895, 48896,
          48897, 48901, 48906, 48908, 48909, 48910, 48911, 48912, 48913, 48915, 48916, 48917, 48918,
          48919, 48921, 48922, 48924, 48929, 48930, 48933, 48937, 48950, 48951, 48956, 48980, 49001,
          49002, 49003, 49004, 49005, 49006, 49007, 49008, 49009, 49010, 49011, 49012, 49013, 49014,
          49015, 49016, 49017, 49018, 49019, 49020, 49021, 49022, 49023, 49024, 49026, 49027, 49028,
          49029, 49030, 49031, 49032, 49033, 49034, 49035, 49036, 49038, 49039, 49040, 49041, 49042,
          49043, 49045, 49046, 49047, 49048, 49050, 49051, 49052, 49053, 49055, 49056, 49057, 49058,
          49060, 49061, 49062, 49063, 49064, 49065, 49066, 49067, 49068, 49069, 49070, 49071, 49072,
          49073, 49074, 49075, 49076, 49077, 49078, 49079, 49080, 49081, 49082, 49083, 49084, 49085,
          49087, 49088, 49089, 49090, 49091, 49092, 49093, 49094, 49095, 49096, 49097, 49098, 49099,
          49101, 49102, 49103, 49104, 49106, 49107, 49111, 49112, 49113, 49115, 49116, 49117, 49119,
          49120, 49121, 49125, 49126, 49127, 49128, 49129, 49130, 49201, 49202, 49203, 49204, 49220,
          49221, 49224, 49227, 49228, 49229, 49230, 49232, 49233, 49234, 49235, 49236, 49237, 49238,
          49239, 49240, 49241, 49242, 49245, 49246, 49247, 49248, 49249, 49250, 49251, 49252, 49253,
          49254, 49255, 49256, 49257, 49258, 49259, 49261, 49262, 49263, 49264, 49265, 49266, 49267,
          49268, 49269, 49270, 49271, 49272, 49274, 49276, 49277, 49279, 49281, 49282, 49283, 49284,
          49285, 49286, 49287, 49288, 49289, 49301, 49302, 49303, 49304, 49305, 49306, 49307, 49309,
          49310, 49311, 49312, 49314, 49315, 49316, 49317, 49318, 49319, 49320, 49321, 49322, 49323,
          49325, 49326, 49327, 49328, 49329, 49330, 49331, 49332, 49333, 49335, 49336, 49337, 49338,
          49339, 49340, 49341, 49342, 49343, 49344, 49345, 49346, 49347, 49348, 49349, 49351, 49355,
          49356, 49357, 49401, 49402, 49403, 49404, 49405, 49406, 49408, 49409, 49410, 49411, 49412,
          49413, 49415, 49416, 49417, 49418, 49419, 49420, 49421, 49422, 49423, 49424, 49425, 49426,
          49427, 49428, 49429, 49430, 49431, 49434, 49435, 49436, 49437, 49440, 49441, 49442, 49443,
          49444, 49445, 49446, 49448, 49449, 49450, 49451, 49452, 49453, 49454, 49455, 49456, 49457,
          49458, 49459, 49460, 49461, 49463, 49464, 49468, 49501, 49502, 49503, 49504, 49505, 49506,
          49507, 49508, 49509, 49510, 49512, 49514, 49515, 49516, 49518, 49519, 49523, 49525, 49528,
          49530, 49534, 49544, 49546, 49548, 49550, 49555, 49560, 49588, 49599, 49601, 49610, 49611,
          49612, 49613, 49614, 49615, 49616, 49617, 49618, 49619, 49620, 49621, 49622, 49623, 49625,
          49626, 49627, 49628, 49629, 49630, 49631, 49632, 49633, 49634, 49635, 49636, 49637, 49638,
          49639, 49640, 49642, 49643, 49644, 49645, 49646, 49648, 49649, 49650, 49651, 49653, 49654,
          49655, 49656, 49657, 49659, 49660, 49663, 49664, 49665, 49666, 49667, 49668, 49670, 49673,
          49674, 49675, 49676, 49677, 49679, 49680, 49682, 49683, 49684, 49685, 49686, 49688, 49689,
          49690, 49696, 49701, 49705, 49706, 49707, 49709, 49710, 49711, 49712, 49713, 49715, 49716,
          49717, 49718, 49719, 49720, 49721, 49722, 49723, 49724, 49725, 49726, 49727, 49728, 49729,
          49730, 49733, 49734, 49735, 49736, 49737, 49738, 49739, 49740, 49743, 49744, 49745, 49746,
          49747, 49748, 49749, 49751, 49752, 49753, 49755, 49756, 49757, 49759, 49760, 49761, 49762,
          49764, 49765, 49766, 49768, 49769, 49770, 49774, 49775, 49776, 49777, 49779, 49780, 49781,
          49782, 49783, 49784, 49785, 49786, 49788, 49790, 49791, 49792, 49793, 49795, 49796, 49797,
          49799, 49801, 49802, 49805, 49806, 49807, 49808, 49812, 49814, 49815, 49816, 49817, 49818,
          49819, 49820, 49821, 49822, 49825, 49826, 49827, 49829, 49831, 49833, 49834, 49835, 49836,
          49837, 49838, 49839, 49840, 49841, 49845, 49847, 49848, 49849, 49852, 49853, 49854, 49855,
          49858, 49861, 49862, 49863, 49864, 49865, 49866, 49868, 49870, 49871, 49872, 49873, 49874,
          49876, 49877, 49878, 49879, 49880, 49881, 49883, 49884, 49885, 49886, 49887, 49891, 49892,
          49893, 49894, 49895, 49896, 49901, 49902, 49903, 49905, 49908, 49910, 49911, 49912, 49913,
          49915, 49916, 49917, 49918, 49919, 49920, 49921, 49922, 49925, 49927, 49929, 49930, 49931,
          49934, 49935, 49938, 49942, 49945, 49946, 49947, 49948, 49950, 49952, 49953, 49955, 49958,
          49959, 49960, 49961, 49962, 49963, 49964, 49965, 49967, 49968, 49969, 49970, 49971};
}
