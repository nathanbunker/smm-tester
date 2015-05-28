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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.immunizationsoftware.dqa.mover.ManagerServlet;
import org.immunizationsoftware.dqa.tester.manager.CvsReader;
import org.immunizationsoftware.dqa.transform.TestCaseMessage;

public class ProfileManager {

  public static final String USE_FULL_TEST_CASE = "Use Full Test Case";
  private static final String ABSENT = " Absent";
  private static final String PRESENT = " Present";

  private List<ProfileField> profileFieldList = null;
  private List<ProfileUsage> profileUsageList = null;
  private Map<String, List<ProfileField>> dataTypeMapList = null;

  public List<ProfileField> getProfileFieldList() {
    return profileFieldList;
  }

  public List<ProfileUsage> getProfileUsageList() {
    return profileUsageList;
  }

  public ProfileManager() throws IOException {
    readProfileFields(ManagerServlet.getRequirementTestFieldsFile());
    profileUsageList = ProfileManager.readProfileUsage(ManagerServlet.getRequirementTestProfileFileSet(), profileFieldList);
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
        profileUsage.getProfileUsageValueMap().put(profileField, profileUsageValue);
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

  public void writeTransforms() throws FileNotFoundException, IOException {
    writeTransforms(ManagerServlet.getRequirementTestTransformsFile(), profileFieldList);
  }

  public static void writeTransforms(File profileTransformFile, List<ProfileField> profileFieldList) throws FileNotFoundException, IOException {
    PrintWriter out = new PrintWriter(new FileWriter(profileTransformFile));
    for (ProfileField profileField : profileFieldList) {
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

  public static void readTransforms(File profileTransformFile, List<ProfileField> profileFieldList) throws FileNotFoundException, IOException {
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
        } else if (line.startsWith("-----")) {
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

  public void saveProfileUsage(ProfileUsage profileUsage, List<ProfileLine> profileLineList) throws IOException {
    PrintWriter out = new PrintWriter(profileUsage.getFile());
    out.println("Field Name,Usage,Value,Comments,Notes");
    try {
      for (ProfileLine profileLine : profileLineList) {
        
        ProfileUsageValue profileUsageValue = profileLine.getProfileUsageValue();
        if (profileUsageValue != null && profileUsageValue.getUsage() != Usage.NOT_DEFINED) {
          out.print("\"");
          out.print(profileLine.getField().getFieldName());
          out.print("\",");
          out.print("\"");
          out.print(profileUsageValue.getUsage());
          out.print("\",");
          out.print("\"");
          out.print(profileUsageValue.getValue());
          out.print("\",");
          out.print("\"");
          out.print(profileUsageValue.getComments().replace('"', '\''));
          out.println("\"");
          out.print("\"");
          out.print(profileUsageValue.getNotes().replace('"', '\''));
          out.println("\"");
        }
      }
    } finally {
      out.close();
    }
  }

  public static List<ProfileUsage> readProfileUsage(Set<ProfileUsage> requirementTestProfileFileSet, List<ProfileField> profileFieldList)
      throws FileNotFoundException, IOException {
    List<ProfileUsage> profileUsageList = new ArrayList<ProfileUsage>();

    List<ProfileUsage> profileUsageToLoad = new ArrayList<ProfileUsage>(requirementTestProfileFileSet);
    Collections.sort(profileUsageToLoad, new Comparator<ProfileUsage>() {
      public int compare(ProfileUsage profileUsage1, ProfileUsage profileUsage2) {
        if (profileUsage1.getCategory() == profileUsage2.getCategory()) {
          if (profileUsage1.getLabel().equals(profileUsage2.getLabel())) {
            return profileUsage1.getVersion().compareTo(profileUsage2.getVersion());
          }
          return profileUsage1.getLabel().compareTo(profileUsage2.getLabel());
        }
        return profileUsage1.getCategory().toString().compareTo(profileUsage2.getCategory().toString());
      }
    });

    for (ProfileUsage profileUsage : profileUsageToLoad) {
      BufferedReader in = new BufferedReader(new FileReader(profileUsage.getFile()));
      String line = in.readLine();
      Map<String, List<String>> valueListMap = new HashMap<String, List<String>>();
      while ((line = in.readLine()) != null) {
        List<String> valueList = CvsReader.readValuesFromCsv(line);
        String fieldNameString = CvsReader.readValue(0, valueList);
        valueListMap.put(fieldNameString, valueList);
      }
      in.close();
      
      for (ProfileField profileField : profileFieldList) {
        List<String> valueList = valueListMap.get(profileField.getFieldName());
        if (valueList != null) {
          String usageString = CvsReader.readValue(1, valueList);
          String usageValue = CvsReader.readValue(2, valueList);
          String usageComments = CvsReader.readValue(3, valueList);
          String usageNotes = CvsReader.readValue(4, valueList);
          ProfileUsageValue profileUsageValue = profileUsage.getProfileUsageValueMap().get(profileField);
          if (profileUsageValue == null) {
            profileUsageValue = new ProfileUsageValue();
            profileUsage.getProfileUsageValueMap().put(profileField, profileUsageValue);
          }
          profileUsageValue.setUsage(Usage.readUsage(usageString.toUpperCase()));
          profileUsageValue.setValue(usageValue);
          profileUsageValue.setComments(usageComments);
          profileUsageValue.setNotes(usageNotes);
        }
      }
      profileUsageList.add(profileUsage);
    }

    {
      // create default base profile
      ProfileUsage profileUsage = new ProfileUsage();
      profileUsage.setCategory(ProfileCategory.US);
      profileUsage.setLabel("Base");
      profileUsage.setVersion("");
      profileUsageList.add(profileUsage);
      for (ProfileField profileField : profileFieldList) {
        ProfileUsageValue profileUsageValue = new ProfileUsageValue();
        profileUsage.getProfileUsageValueMap().put(profileField, profileUsageValue);
        profileUsageValue.setUsage(Usage.readUsage(profileField.getTestUsage()));
      }
    }
    return profileUsageList;
  }

  private static void updateFieldName(ProfileField profileField) {
    String fieldName = "";
    switch (profileField.getType()) {
    case FIELD:
      fieldName = profileField.getSegmentName() + "-" + profileField.getPosInSegment();
      break;
    case FIELD_PART:
      fieldName = profileField.getSegmentName() + "-" + profileField.getPosInSegment() + "." + profileField.getPosInField();
      break;
    case FIELD_PART_VALUE:
      fieldName = (profileField.getSegmentName() + "-" + profileField.getPosInSegment() + "." + profileField.getPosInField() + " " + profileField
          .getCodeValue());
      break;
    case FIELD_SUB_PART:
      fieldName = (profileField.getSegmentName() + "-" + profileField.getPosInSegment() + "." + profileField.getPosInField() + "." + profileField
          .getPosInSubField());
      break;
    case FIELD_SUB_PART_VALUE:
      fieldName = (profileField.getSegmentName() + "-" + profileField.getPosInSegment() + "." + profileField.getPosInField() + "."
          + profileField.getPosInSubField() + " " + profileField.getCodeValue());
      break;
    case FIELD_VALUE:
      fieldName = (profileField.getSegmentName() + "-" + profileField.getPosInSegment() + " " + profileField.getCodeValue());
      break;
    case SEGMENT:
      fieldName = (profileField.getSegmentName());
      break;
    case SEGMENT_GROUP:
      fieldName = (profileField.getSegmentName() + " Group");
      break;
    case DATA_TYPE:
      fieldName = (profileField.getDataTypeDef());
      break;
    case DATA_TYPE_FIELD:
      fieldName = (profileField.getDataTypeDef() + "-" + profileField.getDataTypePos());
      break;
    }
    if (profileField.getSpecialName().length() > 0) {
      fieldName += " " + profileField.getSpecialName();
    }
    if (profileField.getSpecialSection().length() > 0) {
      fieldName = profileField.getSpecialSection() + " " + fieldName;
    }
    profileField.setFieldName(fieldName);
  }

  private void readProfileFields(File fieldFile) throws FileNotFoundException, IOException {
    profileFieldList = new ArrayList<ProfileField>();
    dataTypeMapList = new HashMap<String, List<ProfileField>>();
    List<ProfileField> dataTypeList = new ArrayList<ProfileField>();
    BufferedReader in = new BufferedReader(new FileReader(fieldFile));
    try {
      String line = in.readLine();
      List<String> valueList = CvsReader.readValuesFromCsv(line);

      int posPosInSegment = findPosition("Pos in Seg", valueList);
      int posPosInField = findPosition("Pos in Field", valueList);
      int posPosInSubField = findPosition("Pos in Sub Field", valueList);
      int posSpecialSection = findPosition("Special Section", valueList);
      int posSpecialName = findPosition("Special Name", valueList);
      int posDataType = findPosition("Data Type", valueList);
      int posDataTypeDef = findPosition("Data Type Def", valueList);
      int posDataTypePos = findPosition("Data Type Pos", valueList);
      int posTableName = findPosition("Table Name", valueList);
      int posType = findPosition("Type", valueList);
      int posSegmentName = findPosition("Segment", valueList);
      int posDescription = findPosition("Description", valueList);
      int posCodeValue = findPosition("Code Value", valueList);
      int posCodeLabel = findPosition("Code Label", valueList);
      int posTestUsage = findPosition("Test Usage", valueList);
      int posBaseUsage = findPosition("Base Usage", valueList);
      while ((line = in.readLine()) != null) {
        valueList = CvsReader.readValuesFromCsv(line);
        int posInSegment = CvsReader.readValueInt(posPosInSegment, valueList);
        int posInField = CvsReader.readValueInt(posPosInField, valueList);
        int posInSubField = CvsReader.readValueInt(posPosInSubField, valueList);
        String specialSection = CvsReader.readValue(posSpecialSection, valueList);
        String specialName = CvsReader.readValue(posSpecialName, valueList);
        String dataType = CvsReader.readValue(posDataType, valueList);
        String dataTypeDef = CvsReader.readValue(posDataTypeDef, valueList);
        int dataTypePos = CvsReader.readValueInt(posDataTypePos, valueList);
        String tableName = CvsReader.readValue(posTableName, valueList);
        String type = CvsReader.readValue(posType, valueList);
        String segmentName = CvsReader.readValue(posSegmentName, valueList);
        String description = CvsReader.readValue(posDescription, valueList);
        String codeValue = CvsReader.readValue(posCodeValue, valueList);
        String codeLabel = CvsReader.readValue(posCodeLabel, valueList);
        String testUsage = CvsReader.readValue(posTestUsage, valueList);
        String baseUsage = CvsReader.readValue(posBaseUsage, valueList);
        ProfileField profileField = new ProfileField();
        profileField.setPosInSegment(posInSegment);
        profileField.setPosInField(posInField);
        profileField.setPosInSubField(posInSubField);
        profileField.setSpecialSection(specialSection);
        profileField.setSpecialName(specialName);
        profileField.setDataType(dataType);
        profileField.setDataTypeDef(dataTypeDef);
        profileField.setDataTypePos(dataTypePos);
        profileField.setTableName(tableName);
        profileField.setTestUsage(testUsage);
        profileField.setBaseUsage(baseUsage);
        try {
          profileField.setType(ProfileFieldType.valueOf(type.toUpperCase().replace(" ", "_")));
        } catch (IllegalArgumentException iae) {
          throw new IllegalArgumentException("Unrecognized type '" + type + "'", iae);
        }
        profileField.setSegmentName(segmentName);
        profileField.setDescription(description);
        profileField.setCodeValue(codeValue);
        profileField.setCodeLabel(codeLabel);
        updateFieldName(profileField);
        profileFieldList.add(profileField);
        profileField.setPos(profileFieldList.size());
        if (profileField.isDataType()) {
          if (profileField.getType() == ProfileFieldType.DATA_TYPE) {
            dataTypeList = new ArrayList<ProfileField>();
            dataTypeMapList.put(profileField.getDataTypeDef(), dataTypeList);
          } else if (profileField.getType() == ProfileFieldType.DATA_TYPE_FIELD) {
            dataTypeList.add(profileField);
          }
        } else {
          addDataTypeProfileFields(profileField);
        }
      }
    } finally {
      in.close();
    }
  }

  private void addDataTypeProfileFields(ProfileField profileField) {
    if (!profileField.getDataType().equals("")) {
      List<ProfileField> dataTypeProfileFieldList = dataTypeMapList.get(profileField.getDataType());
      if (dataTypeProfileFieldList != null) {
        ProfileFieldType typeNew = null;
        if (profileField.getType() == ProfileFieldType.FIELD) {
          typeNew = ProfileFieldType.FIELD_PART;
        } else if (profileField.getType() == ProfileFieldType.FIELD_PART) {
          typeNew = ProfileFieldType.FIELD_SUB_PART;
        }
        if (typeNew != null) {
          for (ProfileField pfDataType : dataTypeProfileFieldList) {
            ProfileField pfNew = new ProfileField();
            pfNew.setPosInSegment(profileField.getPosInSegment());
            if (typeNew == ProfileFieldType.FIELD_PART) {
              pfNew.setPosInField(pfDataType.getDataTypePos());
            } else if (typeNew == ProfileFieldType.FIELD_SUB_PART) {
              pfNew.setPosInField(profileField.getPosInField());
              pfNew.setPosInSubField(pfDataType.getDataTypePos());
            }
            pfNew.setDataTypePos(pfDataType.getDataTypePos());
            pfNew.setDataTypeDef(pfDataType.getDataTypeDef());
            pfNew.setSpecialSection(profileField.getSpecialSection());
            if (pfDataType.getSpecialName().equals("")) {
              pfNew.setSpecialName(profileField.getSpecialName());
            } else if (profileField.getSpecialName().equals("")) {
              pfNew.setSpecialName(pfDataType.getSpecialName());
            } else {
              pfNew.setSpecialName(profileField.getSpecialName() + "-" + pfDataType.getSpecialName());
            }
            pfNew.setDataType(pfDataType.getDataType());
            pfNew.setTableName(pfDataType.getTableName());
            pfNew.setTestUsage(pfDataType.getTestUsage());
            pfNew.setBaseUsage(pfDataType.getBaseUsage());
            pfNew.setType(typeNew);
            pfNew.setSegmentName(profileField.getSegmentName());
            pfNew.setDescription(pfDataType.getDescription());
            pfNew.setCodeValue(pfDataType.getCodeValue());
            pfNew.setCodeLabel(pfDataType.getCodeLabel());
            updateFieldName(pfNew);
            profileFieldList.add(pfNew);
            pfNew.setPos(profileFieldList.size());
            if (typeNew == ProfileFieldType.FIELD_PART) {
              addDataTypeProfileFields(pfNew);
            }
          }
        }
      }
    }
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
        return CompatibilityConformance.NOT_DEFINED;
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
        return CompatibilityConformance.NOT_DEFINED;
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