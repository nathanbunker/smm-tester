package org.immunizationsoftware.dqa.tester.profile;

public class ProfileField {
  private String fieldName = "";
  private ProfileFieldType type = null;
  private String segmentName = "";
  private String description = "";
  private String codeValue = "";
  private String codeLabel = "";
  private String transformsPresent = "";
  private String transformsAbsent = "";

  public String getTransformsPresent() {
    return transformsPresent;
  }
  
  public boolean isTransformPresentFullTestCase()
  {
    return transformsPresent.toUpperCase().startsWith(ProfileManager.USE_FULL_TEST_CASE.toUpperCase());
  }

  public boolean isTransformPresentDefined()
  {
    return transformsPresent.length() > 0;
  }

  public boolean isTransformAbsentFullTestCase()
  {
    return transformsAbsent.toUpperCase().startsWith(ProfileManager.USE_FULL_TEST_CASE.toUpperCase());
  }

  public boolean isTransformAbsentDefined()
  {
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
