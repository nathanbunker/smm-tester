package org.immunizationsoftware.dqa.tester.profile;

public class ProfileField {
  private int pos = 0;
  private String fieldName = "";
  private ProfileFieldType type = null;
  private String segmentName = "";
  private String description = "";
  private String codeValue = "";
  private String codeLabel = "";
  private String transformsPresent = "";
  private String transformsAbsent = "";
  private int posInSegment = 0;
  private int posInField = 0;
  private int posInSubField = 0;
  private String specialName = "";
  private String specialSection = "";
  private String dataType = "";
  private String tableName = "";
  private String dataTypeDef = "";
  private int dataTypePos = 0;
  private String testUsage = "";
  private String baseUsage = "";

  public String getTestUsage() {
    return testUsage;
  }

  public int getPos() {
    return pos;
  }

  public void setPos(int pos) {
    this.pos = pos;
  }

  public void setTestUsage(String testUsage) {
    this.testUsage = testUsage;
  }

  public String getBaseUsage() {
    return baseUsage;
  }

  public void setBaseUsage(String baseUsage) {
    this.baseUsage = baseUsage;
  }

  public String getDataTypeDef() {
    return dataTypeDef;
  }

  public void setDataTypeDef(String dataTypeDef) {
    this.dataTypeDef = dataTypeDef;
  }

  public int getDataTypePos() {
    return dataTypePos;
  }

  public void setDataTypePos(int dataTypePos) {
    this.dataTypePos = dataTypePos;
  }

  public boolean isDataType() {
    return type == ProfileFieldType.DATA_TYPE || type == ProfileFieldType.DATA_TYPE_FIELD;
  }

  public String getSpecialSection() {
    return specialSection;
  }

  public void setSpecialSection(String specialSection) {
    this.specialSection = specialSection;
  }

  public int getPosInSegment() {
    return posInSegment;
  }

  public void setPosInSegment(int posInSegment) {
    this.posInSegment = posInSegment;
  }

  public int getPosInField() {
    return posInField;
  }

  public void setPosInField(int posInField) {
    this.posInField = posInField;
  }

  public int getPosInSubField() {
    return posInSubField;
  }

  public void setPosInSubField(int posInSubField) {
    this.posInSubField = posInSubField;
  }

  public String getSpecialName() {
    return specialName;
  }

  public void setSpecialName(String specialName) {
    this.specialName = specialName;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getTransformsPresent() {
    return transformsPresent;
  }

  public boolean isTransformPresentFullTestCase() {
    return transformsPresent.toUpperCase().startsWith(ProfileManager.USE_FULL_TEST_CASE.toUpperCase());
  }

  public boolean isTransformPresentDefined() {
    return transformsPresent.length() > 0;
  }

  public boolean isTransformAbsentFullTestCase() {
    return transformsAbsent.toUpperCase().startsWith(ProfileManager.USE_FULL_TEST_CASE.toUpperCase());
  }

  public boolean isTransformAbsentDefined() {
    return transformsAbsent.length() > 0;
  }

  public void setTransformsPresent(String transformsPresent) {
    this.transformsPresent = transformsPresent;
  }

  public String getTransformsAbsent() {
    return transformsAbsent;
  }

  public void setTransformsAbsent(String transformsAbsent) {
    this.transformsAbsent = transformsAbsent;
  }

  public String getCodeValue() {
    return codeValue;
  }

  public void setCodeValue(String codeValue) {
    this.codeValue = codeValue;
  }

  public String getCodeLabel() {
    return codeLabel;
  }

  public void setCodeLabel(String codeLabel) {
    this.codeLabel = codeLabel;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public void setType(ProfileFieldType type) {
    this.type = type;
  }

  public void setSegmentName(String segmentName) {
    this.segmentName = segmentName;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFieldName() {
    return fieldName;
  }

  public ProfileFieldType getType() {
    return type;
  }

  public String getSegmentName() {
    return segmentName;
  }

  public String getDescription() {
    return description;
  }

}
