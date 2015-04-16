package org.immunizationsoftware.dqa.tester.profile;

import static org.immunizationsoftware.dqa.transform.ScenarioManager.SCENARIO_FULL_RECORD_FOR_PROFILING;
import static org.immunizationsoftware.dqa.transform.ScenarioManager.createTestCaseMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.tester.manager.CvsReader;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class ProfileManager
{

  public static final String USE_FULL_TEST_CASE = "Use Full Test Case";
  private static final String ABSENT = " Absent";
  private static final String PRESENT = " Present";

  private List<ProfileField> profileFieldList = null;
  private List<ProfileUsage> profileUsageList = null;

  public List<ProfileField> getProfileFieldList() {
    return profileFieldList;
  }

  public List<ProfileUsage> getProfileUsageList() {
    return profileUsageList;
  }

  public ProfileManager() throws IOException {
    profileFieldList = ProfileManager.readProfileFields(ManagerServlet.getRequirementTestFieldsFile());
    profileUsageList = ProfileManager.readProfileUsage(ManagerServlet.getRequirementTestProfilesFile(),
        profileFieldList);
    ProfileManager.readTransforms(ManagerServlet.getRequirementTestTransformsFile(), profileFieldList);
  }

  public List<ProfileLine> createProfileLines(ProfileUsage profileUsage) {
    return createProfileLines(profileFieldList, profileUsage);
  }

  public static List<ProfileLine> createProfileLines(List<ProfileField> profileFieldList, ProfileUsage profileUsage) {

    List<ProfileLine> profileLineList = new ArrayList<ProfileLine>();
    for (ProfileField profileField : profileFieldList) {
      ProfileUsageValue profileUsageValue = profileUsage.getProfileUsageValueMap().get(profileField);
      if (profileUsageValue == null) {
        profileUsageValue = new ProfileUsageValue();
        profileUsageValue.setUsage(Usage.NOT_DEFINED);
      }
      ProfileLine profileLine = new ProfileLine(profileUsageValue);
      profileLine.setField(profileField);
      profileLine.setUsage(profileUsageValue.getUsage());
      profileLineList.add(profileLine);
    }
    return profileLineList;
  }

  public static TestCaseMessage getPresentTestCase(ProfileField field, TestCaseMessage defaultTestCaseMessage) {
    if (field.getTransformsPresent().toUpperCase().startsWith(USE_FULL_TEST_CASE.toUpperCase())) {
      return defaultTestCaseMessage;
    }
    TestCaseMessage testCaseMessage = createTestCaseMessage(SCENARIO_FULL_RECORD_FOR_PROFILING);
    if (field.getTransformsPresent().equals("")) {
      testCaseMessage.setHasIssue(false);
      return testCaseMessage;
    } else {
      testCaseMessage.setAdditionalTransformations(field.getTransformsPresent());
      testCaseMessage.setHasIssue(true);
      return testCaseMessage;
    }
  }

  public static void convertHistoryToNonAdmin(TestCaseMessage testCaseMessage) {
    testCaseMessage.appendAdditionalTransformation("RXA#2-5.1=998");
    testCaseMessage.appendAdditionalTransformation("RXA#2-5.2=no vaccine administered");
    testCaseMessage.appendAdditionalTransformation("RXA#2-6=999");
    testCaseMessage.appendAdditionalTransformation("RXA#2-9.1=");
    testCaseMessage.appendAdditionalTransformation("RXA#2-9.2=");
    testCaseMessage.appendAdditionalTransformation("RXA#2-9.3=");
    testCaseMessage.appendAdditionalTransformation("RXA#2-20=NA");
    testCaseMessage.setHasIssue(true);
  }

  public static TestCaseMessage getAbsentTestCase(ProfileField field, TestCaseMessage defaultTestCaseMessage) {
    if (field.getTransformsAbsent().toUpperCase().startsWith(USE_FULL_TEST_CASE.toUpperCase())) {
      return defaultTestCaseMessage;
    }
    TestCaseMessage testCaseMessage = createTestCaseMessage(SCENARIO_FULL_RECORD_FOR_PROFILING);
    if (field.getTransformsAbsent().equals("")) {
      testCaseMessage.setHasIssue(false);
      return testCaseMessage;
    } else {
      testCaseMessage.setAdditionalTransformations(field.getTransformsAbsent());
      testCaseMessage.setHasIssue(true);
      return testCaseMessage;
    }
  }
  
  public void writeTransforms()
      throws FileNotFoundException, IOException {
    writeTransforms(ManagerServlet.getRequirementTestTransformsFile(), profileFieldList);
  }
  
  public static void writeTransforms(File profileTransformFile, List<ProfileField> profileFieldList)
      throws FileNotFoundException, IOException {
    PrintWriter out = new PrintWriter(new FileWriter(profileTransformFile));
    for (ProfileField profileField : profileFieldList)
    {
      out.println("======================================================================");
      out.println(profileField.getFieldName() + " Present");
      out.println("----------------------------------------------------------------------");
      out.println(profileField.getTransformsPresent());
      out.println("======================================================================");
      out.println(profileField.getFieldName() + " Absent");
      out.println("----------------------------------------------------------------------");
      out.println(profileField.getTransformsAbsent());
    }
    out.close();
  }

  public static void readTransforms(File profileTransformFile, List<ProfileField> profileFieldList)
      throws FileNotFoundException, IOException {
    Map<String, ProfileField> profileFieldMap = new HashMap<String, ProfileField>();
    for (ProfileField profileField : profileFieldList) {
      profileFieldMap.put(profileField.getFieldName().toUpperCase(), profileField);
    }
    BufferedReader in = new BufferedReader(new FileReader(profileTransformFile));
    String line;
    String transforms = "";
    ProfileField profileField = null;
    boolean present = false;
    boolean expectingFieldName = false;
    while ((line = in.readLine()) != null) {
      line = line.trim();
      if (line.length() > 0) {
        if (line.startsWith("=====")) {
          if (!transforms.equals("") && profileField != null) {
            if (present) {
              profileField.setTransformsPresent(transforms);
            } else {
              profileField.setTransformsAbsent(transforms);
            }
            transforms = "";
          }
          expectingFieldName = true;
        }
        else if (line.startsWith("-----")) {
          expectingFieldName = false;
        } else if (expectingFieldName && (line.endsWith(PRESENT) || line.endsWith(ABSENT))) {
          String fieldName;
          present = line.endsWith(PRESENT);
          if (present) {
            fieldName = line.substring(0, line.length() - PRESENT.length()).trim();
          } else {
            fieldName = line.substring(0, line.length() - ABSENT.length()).trim();
          }
          profileField = profileFieldMap.get(fieldName.toUpperCase());
        } else {
          transforms += line + "\n";
        }
      }
    }
    if (!transforms.equals("") && profileField != null) {
      if (present) {
        profileField.setTransformsPresent(transforms);
      } else {
        profileField.setTransformsAbsent(transforms);
      }
      transforms = "";
    }
    in.close();

  }

  public static List<ProfileUsage> readProfileUsage(File profileUsageFile, List<ProfileField> profileFieldList)
      throws FileNotFoundException, IOException {
    List<ProfileUsage> profileUsageList = new ArrayList<ProfileUsage>();
    BufferedReader in = new BufferedReader(new FileReader(profileUsageFile));
    String line = in.readLine();
    List<String> headerList = CvsReader.readValuesFromCsv(line);
    int posCategory = findPosition("Category", headerList);
    int posLabel = findPosition("Label", headerList);
    int posVersion = findPosition("Version", headerList);
    int posType = findPosition("Type", headerList);
    Map<ProfileField, Integer> posMap = new HashMap<ProfileField, Integer>();
    for (ProfileField profileField : profileFieldList) {
      int posProfileField = findPosition(profileField.getFieldName(), headerList);
      posMap.put(profileField, posProfileField);
    }
    ProfileUsage profileUsage = null;
    while ((line = in.readLine()) != null) {
      List<String> valueList = CvsReader.readValuesFromCsv(line);
      String categoryString = CvsReader.readValue(posCategory, valueList);
      String label = CvsReader.readValue(posLabel, valueList);
      if (categoryString.length() > 0 && label.length() > 0) {
        ProfileCategory category = ProfileCategory.valueOf(categoryString.toUpperCase());
        String version = CvsReader.readValue(posVersion, valueList);
        if (profileUsage == null || profileUsage.getCategory() != category || !profileUsage.getLabel().equals(label)
            || !profileUsage.getVersion().equals(version)) {
          profileUsage = new ProfileUsage();
          profileUsage.setCategory(category);
          profileUsage.setLabel(label);
          profileUsage.setVersion(version);
          profileUsageList.add(profileUsage);
        }
        String typeString = CvsReader.readValue(posType, valueList);
        if (typeString.length() > 0) {
          ProfileUsageType type = ProfileUsageType.valueOf(typeString.toUpperCase());
          for (ProfileField profileField : profileFieldList) {
            int posProfileField = posMap.get(profileField);
            String posValue = CvsReader.readValue(posProfileField, valueList);
            ProfileUsageValue profileUsageValue = profileUsage.getProfileUsageValueMap().get(profileField);
            if (profileUsageValue == null) {
              profileUsageValue = new ProfileUsageValue();
              profileUsage.getProfileUsageValueMap().put(profileField, profileUsageValue);
            }
            if (type == ProfileUsageType.USAGE) {
              profileUsageValue.setUsage(Usage.readUsage(posValue.toUpperCase()));
            } else if (type == ProfileUsageType.VALUE) {
              profileUsageValue.setValue(posValue);
            } else if (type == ProfileUsageType.COMMENTS) {
              profileUsageValue.setComments(posValue);
            }
          }
        }
      }

    }
    in.close();
    return profileUsageList;
  }

  public static List<ProfileField> readProfileFields(File fieldFile) throws FileNotFoundException, IOException {
    List<ProfileField> profileFieldList = new ArrayList<ProfileField>();
    BufferedReader in = new BufferedReader(new FileReader(fieldFile));
    String line = in.readLine();
    List<String> valueList = CvsReader.readValuesFromCsv(line);
    int posFieldName = findPosition("Field Name", valueList);
    int posType = findPosition("Type", valueList);
    int posSegmentName = findPosition("Segment", valueList);
    int posDescription = findPosition("Description", valueList);
    int posCodeValue = findPosition("Code Value", valueList);
    int posCodeLabel = findPosition("Code Label", valueList);
    while ((line = in.readLine()) != null) {
      valueList = CvsReader.readValuesFromCsv(line);
      String fieldName = CvsReader.readValue(posFieldName, valueList);
      String type = CvsReader.readValue(posType, valueList);
      String segmentName = CvsReader.readValue(posSegmentName, valueList);
      String description = CvsReader.readValue(posDescription, valueList);
      String codeValue = CvsReader.readValue(posCodeValue, valueList);
      String codeLabel = CvsReader.readValue(posCodeLabel, valueList);
      ProfileField profileField = new ProfileField();
      profileField.setFieldName(fieldName);
      profileField.setType(ProfileFieldType.valueOf(type.toUpperCase().replace(" ", "_")));
      profileField.setSegmentName(segmentName);
      profileField.setDescription(description);
      profileField.setCodeValue(codeValue);
      profileField.setCodeLabel(codeLabel);
      if (profileField.getType() == ProfileFieldType.FIELD_VALUE
          || profileField.getType() == ProfileFieldType.FIELD_PART_VALUE
          || profileField.getType() == ProfileFieldType.FIELD_SUB_PART_VALUE) {
        profileField.setFieldName((profileField.getFieldName() + " " + profileField.getCodeValue()).trim());
        profileField.setDescription((profileField.getDescription() + " valued " + profileField.getCodeLabel()).trim());
      }
      profileFieldList.add(profileField);
    }
    in.close();
    return profileFieldList;
  }

  public static void updateMessageAcceptStatus(List<ProfileLine> profileLineList) {
    MessageAcceptStatus masSegment = MessageAcceptStatus.ONLY_IF_PRESENT;
    MessageAcceptStatus masField = null;
    MessageAcceptStatus masFieldPart = null;
    MessageAcceptStatus masFieldSubPart = null;
    for (ProfileLine profileLine : profileLineList) {
      ProfileField field = profileLine.getField();
      if (field.getType() == ProfileFieldType.SEGMENT) {
        masSegment = determineMessageAcceptStatus(profileLine, MessageAcceptStatus.ONLY_IF_PRESENT);
        profileLine.setMessageAcceptStatus(masSegment);
      } else if (field.getType() == ProfileFieldType.FIELD) {
        masField = determineMessageAcceptStatus(profileLine, masSegment);
        profileLine.setMessageAcceptStatus(masField);
      } else if (field.getType() == ProfileFieldType.FIELD_PART) {
        masFieldPart = determineMessageAcceptStatus(profileLine, masField);
        profileLine.setMessageAcceptStatus(masFieldPart);
      } else if (field.getType() == ProfileFieldType.FIELD_SUB_PART) {
        masFieldSubPart = determineMessageAcceptStatus(profileLine, masFieldPart);
        profileLine.setMessageAcceptStatus(masFieldSubPart);
      } else if (field.getType() == ProfileFieldType.FIELD_VALUE) {
        profileLine.setMessageAcceptStatus(determineMessageAcceptStatus(profileLine, masField));
      } else if (field.getType() == ProfileFieldType.FIELD_PART_VALUE) {
        profileLine.setMessageAcceptStatus(determineMessageAcceptStatus(profileLine, masFieldPart));
      } else if (field.getType() == ProfileFieldType.FIELD_SUB_PART_VALUE) {
        profileLine.setMessageAcceptStatus(determineMessageAcceptStatus(profileLine, masFieldSubPart));
      }
    }
  }

  public static MessageAcceptStatus determineMessageAcceptStatus(ProfileLine profileLine, MessageAcceptStatus masHigher) {
    MessageAcceptStatus mas = null;
    if (masHigher == MessageAcceptStatus.ONLY_IF_ABSENT || profileLine.getUsage() == Usage.X) {
      mas = MessageAcceptStatus.ONLY_IF_ABSENT;
    } else if (masHigher == MessageAcceptStatus.ONLY_IF_PRESENT && profileLine.getUsage() == Usage.R) {
      mas = MessageAcceptStatus.ONLY_IF_PRESENT;
    } else {
      mas = MessageAcceptStatus.IF_PRESENT_OR_ABSENT;
    }
    return mas;
  }

  private static int findPosition(String headerName, List<String> valueList) {
    int pos = 0;
    for (String value : valueList) {
      if (value.equalsIgnoreCase(headerName)) {
        return pos;
      }
      pos++;
    }
    return -1;
  }

  public static CompatibilityConformance getCompatibilityConformance(ProfileUsageValue profileUsageValue,
      ProfileUsageValue profileUsageValueConformance) {

    switch (profileUsageValueConformance.getUsage()) {
    case R:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityConformance.COMPATIBLE;
      case RE:
        return CompatibilityConformance.ALLOWANCE;
      case O:
      case NOT_DEFINED:
        return CompatibilityConformance.ALLOWANCE;
      case X:
        return CompatibilityConformance.MAJOR_CONFLICT;
      case R_NOT_ENFORCED:
        return CompatibilityConformance.ALLOWANCE;
      case RE_NOT_USED:
        return CompatibilityConformance.ALLOWANCE;
      case O_NOT_USED:
        return CompatibilityConformance.ALLOWANCE;
      case X_NOT_ENFORCED:
        return CompatibilityConformance.CONFLICT;
      default:
        return CompatibilityConformance.UNABLE_TO_DETERMINE;
      }
    case RE:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityConformance.CONSTRAINT;
      case RE:
        return CompatibilityConformance.COMPATIBLE;
      case O:
      case NOT_DEFINED:
        return CompatibilityConformance.ALLOWANCE;
      case X:
        return CompatibilityConformance.MAJOR_CONFLICT;
      case R_NOT_ENFORCED:
        return CompatibilityConformance.CONSTRAINT;
      case RE_NOT_USED:
        return CompatibilityConformance.ALLOWANCE;
      case O_NOT_USED:
        return CompatibilityConformance.ALLOWANCE;
      case X_NOT_ENFORCED:
        return CompatibilityConformance.CONFLICT;
      default:
        return CompatibilityConformance.UNABLE_TO_DETERMINE;
      }
    case NOT_DEFINED:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityConformance.MAJOR_CONSTRAINT;
      case RE:
        return CompatibilityConformance.CONSTRAINT;
      case O:
      case NOT_DEFINED:
        return CompatibilityConformance.COMPATIBLE;
      case X:
        return CompatibilityConformance.MAJOR_CONSTRAINT;
      case R_NOT_ENFORCED:
        return CompatibilityConformance.CONSTRAINT;
      case RE_NOT_USED:
        return CompatibilityConformance.CONSTRAINT;
      case O_NOT_USED:
        return CompatibilityConformance.COMPATIBLE;
      case X_NOT_ENFORCED:
        return CompatibilityConformance.COMPATIBLE;
      default:
        return CompatibilityConformance.UNABLE_TO_DETERMINE;
      }
    case O:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityConformance.MAJOR_CONSTRAINT;
      case RE:
        return CompatibilityConformance.CONSTRAINT;
      case O:
      case NOT_DEFINED:
        return CompatibilityConformance.COMPATIBLE;
      case X:
        return CompatibilityConformance.MAJOR_CONSTRAINT;
      case R_NOT_ENFORCED:
        return CompatibilityConformance.CONSTRAINT;
      case RE_NOT_USED:
        return CompatibilityConformance.CONSTRAINT;
      case O_NOT_USED:
        return CompatibilityConformance.COMPATIBLE;
      case X_NOT_ENFORCED:
        return CompatibilityConformance.COMPATIBLE;
      default:
        return CompatibilityConformance.UNABLE_TO_DETERMINE;
      }
    case X:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityConformance.MAJOR_CONFLICT;
      case RE:
        return CompatibilityConformance.MAJOR_CONFLICT;
      case O:
        return CompatibilityConformance.CONFLICT;
      case NOT_DEFINED:
        return CompatibilityConformance.COMPATIBLE;
      case X:
        return CompatibilityConformance.COMPATIBLE;
      case R_NOT_ENFORCED:
        return CompatibilityConformance.CONFLICT;
      case RE_NOT_USED:
        return CompatibilityConformance.CONFLICT;
      case O_NOT_USED:
        return CompatibilityConformance.CONFLICT;
      case X_NOT_ENFORCED:
        return CompatibilityConformance.COMPATIBLE;
      default:
        return CompatibilityConformance.UNABLE_TO_DETERMINE;
      }
    default:
      return CompatibilityConformance.UNABLE_TO_DETERMINE;
    }
  }

  public static CompatibilityInteroperability getCompatibilityInteroperability(ProfileUsageValue profileUsageValue,
      ProfileUsageValue profileUsageValueInteroperability) {

    switch (profileUsageValueInteroperability.getUsage()) {
    case R:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityInteroperability.COMPATIBLE;
      case RE:
        return CompatibilityInteroperability.NO_PROBLEM;
      case O:
      case NOT_DEFINED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X:
        return CompatibilityInteroperability.MAJOR_PROBLEM;
      case R_NOT_ENFORCED:
        return CompatibilityInteroperability.DATA_LOSS;
      case RE_NOT_USED:
        return CompatibilityInteroperability.DATA_LOSS;
      case O_NOT_USED:
        return CompatibilityInteroperability.DATA_LOSS;
      case X_NOT_ENFORCED:
        return CompatibilityInteroperability.DATA_LOSS;
      default:
        return CompatibilityInteroperability.UNABLE_TO_DETERMINE;
      }
    case RE:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityInteroperability.IF_POPULATED;
      case RE:
        return CompatibilityInteroperability.COMPATIBLE;
      case O:
      case NOT_DEFINED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X:
        return CompatibilityInteroperability.PROBLEM;
      case R_NOT_ENFORCED:
        return CompatibilityInteroperability.DATA_LOSS;
      case RE_NOT_USED:
        return CompatibilityInteroperability.DATA_LOSS;
      case O_NOT_USED:
        return CompatibilityInteroperability.DATA_LOSS;
      case X_NOT_ENFORCED:
        return CompatibilityInteroperability.DATA_LOSS;
      default:
        return CompatibilityInteroperability.UNABLE_TO_DETERMINE;
      }
    case O:
    case NOT_DEFINED:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityInteroperability.PROBLEM;
      case RE:
        return CompatibilityInteroperability.PROBLEM;
      case O:
      case NOT_DEFINED:
        return CompatibilityInteroperability.COMPATIBLE;
      case X:
        return CompatibilityInteroperability.PROBLEM;
      case R_NOT_ENFORCED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case RE_NOT_USED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case O_NOT_USED:
        return CompatibilityInteroperability.COMPATIBLE;
      case X_NOT_ENFORCED:
        return CompatibilityInteroperability.NO_PROBLEM;
      default:
        return CompatibilityInteroperability.UNABLE_TO_DETERMINE;
      }
    case X:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityInteroperability.MAJOR_PROBLEM;
      case RE:
        return CompatibilityInteroperability.PROBLEM;
      case O:
      case NOT_DEFINED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X:
        return CompatibilityInteroperability.COMPATIBLE;
      case R_NOT_ENFORCED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case RE_NOT_USED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case O_NOT_USED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X_NOT_ENFORCED:
        return CompatibilityInteroperability.COMPATIBLE;
      default:
        return CompatibilityInteroperability.UNABLE_TO_DETERMINE;
      }
    case R_OR_RE:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityInteroperability.IF_CONFIGURED;
      case RE:
        return CompatibilityInteroperability.IF_CONFIGURED;
      case O:
      case NOT_DEFINED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X:
        return CompatibilityInteroperability.MAJOR_PROBLEM;
      case R_NOT_ENFORCED:
        return CompatibilityInteroperability.DATA_LOSS;
      case RE_NOT_USED:
        return CompatibilityInteroperability.DATA_LOSS;
      case O_NOT_USED:
        return CompatibilityInteroperability.DATA_LOSS;
      case X_NOT_ENFORCED:
        return CompatibilityInteroperability.DATA_LOSS;
      default:
        return CompatibilityInteroperability.UNABLE_TO_DETERMINE;
      }
    case R_OR_X:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityInteroperability.IF_CONFIGURED;
      case RE:
        return CompatibilityInteroperability.IF_CONFIGURED;
      case O:
      case NOT_DEFINED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X:
        return CompatibilityInteroperability.IF_CONFIGURED;
      case R_NOT_ENFORCED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case RE_NOT_USED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case O_NOT_USED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X_NOT_ENFORCED:
        return CompatibilityInteroperability.NO_PROBLEM;
      default:
        return CompatibilityInteroperability.UNABLE_TO_DETERMINE;
      }
    case RE_OR_O:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityInteroperability.IF_POPULATED;
      case RE:
        return CompatibilityInteroperability.IF_CONFIGURED;
      case O:
      case NOT_DEFINED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X:
        return CompatibilityInteroperability.PROBLEM;
      case R_NOT_ENFORCED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case RE_NOT_USED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case O_NOT_USED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X_NOT_ENFORCED:
        return CompatibilityInteroperability.NO_PROBLEM;
      default:
        return CompatibilityInteroperability.UNABLE_TO_DETERMINE;
      }
    case RE_OR_X:
      switch (profileUsageValue.getUsage()) {
      case R:
        return CompatibilityInteroperability.IF_POPULATED;
      case RE:
        return CompatibilityInteroperability.IF_CONFIGURED;
      case O:
      case NOT_DEFINED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X:
        return CompatibilityInteroperability.IF_CONFIGURED;
      case R_NOT_ENFORCED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case RE_NOT_USED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case O_NOT_USED:
        return CompatibilityInteroperability.NO_PROBLEM;
      case X_NOT_ENFORCED:
        return CompatibilityInteroperability.NO_PROBLEM;
      default:
        return CompatibilityInteroperability.UNABLE_TO_DETERMINE;
      }
    default:
      return CompatibilityInteroperability.UNABLE_TO_DETERMINE;
    }
  }

}
