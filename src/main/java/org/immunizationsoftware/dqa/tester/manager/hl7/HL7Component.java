package org.immunizationsoftware.dqa.tester.manager.hl7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.immunizationsoftware.dqa.tester.manager.hl7.analyze.HL7DateAnalyzer;
import org.immunizationsoftware.dqa.tester.manager.hl7.analyze.HL7FormatAnalyzer;
import org.immunizationsoftware.dqa.tester.manager.hl7.conformance.ConformanceStatement;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.ERL;
import org.immunizationsoftware.dqa.tester.manager.hl7.predicates.ConditionalPredicate;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.ERR;

public abstract class HL7Component
{

  public HL7Component getNextComponent() {
    return nextComponent;
  }

  public void setNextComponent(HL7Component nextComponent) {
    this.nextComponent = nextComponent;
  }

  protected HL7Component() {
    // default
  }

  public abstract void init();

  protected HL7Component(HL7Component copy) {

    this.componentNameSpecific = copy.componentNameSpecific;
    this.componentNameGeneric = copy.componentNameGeneric;
    this.componentCode = copy.componentCode;
    if (copy.childComponents != null && copy.childComponents.length > 0) {
      this.childComponents = new HL7Component[copy.childComponents.length + 1];
    } else {
      this.childComponents = null;
    }
    this.nextComponent = null;
    this.sequence = copy.sequence;
    this.lengthMin = copy.lengthMin;
    this.lengthMax = copy.lengthMax;
    this.cardinalityMin = copy.cardinalityMin;
    this.cardinalityMax = copy.cardinalityMax;
    this.value = "";
    this.defaultValueWhenBlank = copy.defaultValueWhenBlank;
    this.usageType = UsageType.O;
    this.conditionalPredicate = null;
    this.valueSet = copy.valueSet;
    this.rawTextReceived = "";
    this.cardinalityCount = copy.cardinalityCount + 1;
    this.cardinality = copy.cardinality;

  }

  protected HL7Component(String componentCode, String componentName, int childComponentCount) {
    this(componentCode, componentName, componentName, childComponentCount);
  }

  protected HL7Component(String componentCode, String componentNameGeneric, String componentNameSpecific,
      int childComponentCount) {
    this.componentCode = componentCode;
    this.componentNameGeneric = componentNameGeneric;
    this.componentNameSpecific = componentNameSpecific;
    if (childComponentCount > 0) {
      this.childComponents = new HL7Component[childComponentCount + 1];
    }
  }

  protected HL7Component(String componentCode, String componentNameGeneric, String componentNameSpecific,
      int childComponentCount, UsageType usageType) {
    this.componentCode = componentCode;
    this.componentNameGeneric = componentNameGeneric;
    this.componentNameSpecific = componentNameSpecific;
    if (childComponentCount > 0) {
      this.childComponents = new HL7Component[childComponentCount + 1];
    }
    this.usageType = usageType;
  }

  protected HL7Component(String componentCode, String componentNameGeneric, String componentNameSpecific,
      int childComponentCount, UsageType usageType, int lengthMax) {
    this.componentCode = componentCode;
    this.componentNameGeneric = componentNameGeneric;
    this.componentNameSpecific = componentNameSpecific;
    if (childComponentCount > 0) {
      this.childComponents = new HL7Component[childComponentCount + 1];
    }
    this.usageType = usageType;
    this.lengthMax = lengthMax;
  }

  protected HL7Component(String componentCode, String componentNameGeneric, String componentNameSpecific,
      int childComponentCount, UsageType usageType, Cardinality cardinality, int lengthMax) {
    this(componentCode, componentNameGeneric, componentNameSpecific, childComponentCount, usageType, cardinality);
    this.lengthMax = lengthMax;
  }

  protected HL7Component(String componentCode, String componentName, int childComponentCount, UsageType usageType,
      Cardinality cardinality) {
    this(componentCode, componentName, componentName, childComponentCount, usageType, cardinality);
  }

  protected HL7Component(String componentCode, String componentNameGeneric, String componentNameSpecific,
      int childComponentCount, UsageType usageType, Cardinality cardinality) {

    this.componentCode = componentCode;
    this.componentNameGeneric = componentNameGeneric;
    this.componentNameSpecific = componentNameSpecific;
    if (childComponentCount > 0) {
      this.childComponents = new HL7Component[childComponentCount + 1];
    }
    this.usageType = usageType;

    this.cardinality = cardinality;
    if (cardinality == null || cardinality == Cardinality.ZERO_OR_MORE) {
      // nothing to do
    } else if (cardinality == Cardinality.ZERO_OR_ONE) {
      cardinalityMax = 1;
    } else if (cardinality == Cardinality.ONE_OR_MORE_TIMES) {
      cardinalityMin = 1;
    } else if (cardinality == Cardinality.ONE_TIME_ONLY) {
      cardinalityMin = 1;
      cardinalityMax = 1;
    }
  }

  protected String componentNameSpecific = "";
  protected String componentNameGeneric = "";
  protected String componentCode = "";
  protected HL7Component[] childComponents;
  protected HL7Component nextComponent = null;
  protected int sequence = 0;
  protected int lengthMin = 1;
  protected int lengthMax = Integer.MAX_VALUE;
  protected int cardinalityMin = 0;
  protected int cardinalityMax = Integer.MAX_VALUE;
  protected String value = "";
  protected String defaultValueWhenBlank = "";
  protected UsageType usageType = UsageType.O;
  protected ConditionalPredicate conditionalPredicate = null;
  protected ValueSet valueSet = null;
  protected String rawTextReceived = "";
  protected boolean empty = true;
  protected int cardinalityCount = 1;
  protected Cardinality cardinality = null;
  protected String[] constrainedToValues = null;
  protected List<ConformanceStatement> conformanceStatementList = new ArrayList<ConformanceStatement>();
  protected ERL errorLocation = null;
  protected HL7FormatAnalyzer formatAnalyzer = null;

  public HL7FormatAnalyzer getFormatAnalyzer() {
    return formatAnalyzer;
  }

  public void setFormatAnalyzer(HL7FormatAnalyzer dateAnalyzer) {
    this.formatAnalyzer = dateAnalyzer;
  }

  public String getRawTextReceived() {
    return rawTextReceived;
  }

  public void setRawTextReceived(String text) {
    this.rawTextReceived = text;
  }

  public ValueSet getValueSet() {
    return valueSet;
  }

  public void setValueSet(ValueSet valueSet) {
    this.valueSet = valueSet;
  }

  public Cardinality getCardinality() {
    return cardinality;
  }

  public void setCardinality(Cardinality cardinality) {
    this.cardinality = cardinality;
  }

  public int getCardinalityCount() {
    return cardinalityCount;
  }

  public void setCardinalityCount(int cardinalityCount) {
    this.cardinalityCount = cardinalityCount;
  }

  public abstract HL7Component makeAnother();

  public void parseTextFromMessage(String text) {
    this.errorLocation = new ERL();
    this.rawTextReceived = text;
    this.value = text;
    if (childComponents.length > 0) {
      BufferedReader in = new BufferedReader(new StringReader(text));
      int pos = 0;
      String line;
      try {
        while ((line = in.readLine()) != null) {
          String segmentName = "";
          if (line.length() >= 3) {
            segmentName = line.substring(0, 3);
          }
          boolean found = false;
          if (pos > 0 && childComponents[pos].getComponentCode().equals(segmentName)) {
            // already reading this segment
            int segmentSequence = 2;
            HL7Component next = childComponents[pos];
            while (next.getNextComponent() != null) {
              next = next.getNextComponent();
              segmentSequence++;
            }
            next.setNextComponent(next.makeAnother());
            next = next.getNextComponent();
            next.errorLocation = new ERL(errorLocation);
            next.errorLocation.getSegmentID().setValue(segmentName);
            next.errorLocation.getSegmentSequence().setValue("" + segmentSequence);
            next.parseTextFromSegment(line);
          } else {
            int peak = pos + 1;

            while (peak < childComponents.length) {
              if (childComponents[peak].getComponentCode().equals(segmentName)) {
                found = true;
                pos = peak;
              } else if (childComponents[peak].getUsageType() == UsageType.R) {
                // can't pass a required component, it has to be sent
                break;
              }
              peak++;
            }
            if (found) {
              childComponents[pos].errorLocation = new ERL(errorLocation);
              childComponents[pos].errorLocation.getSegmentID().setValue(segmentName);
              childComponents[pos].errorLocation.getSegmentSequence().setValue("1");
              childComponents[pos].parseTextFromSegment(line);
            }
          }

        }
      } catch (IOException ioe) {
        // not sure why reading a string would cause IO =, but just in case
        ioe.printStackTrace();
      }
      for (int i = 1; i < childComponents.length; i++) {
        HL7Component next = childComponents[i];
        int count = 1;
        while (next != null) {
          if (next.errorLocation == null) {
            next.errorLocation = new ERL(errorLocation);
            next.errorLocation.getSegmentID().setValue(componentCode);
            next.errorLocation.getSegmentSequence().setValue("" + count);
          }
          next = next.nextComponent;
          count++;
        }
      }
    }
  }

  public void parseTextFromSegment(String text) {
    if (errorLocation == null) {
      errorLocation = new ERL();
      errorLocation.getSegmentID().setValue(componentCode);
      errorLocation.getSegmentSequence().setValue("1");
    }
    this.rawTextReceived = text;
    this.value = text;
    if (childComponents.length > 0) {
      String[] fields = text.split("\\|");
      boolean isHeader = fields[0].equals("MSH") || fields[0].equals("BHS") || fields[0].equals("FHS");
      if (isHeader) {
        String[] headerFields = new String[fields.length + 1];
        headerFields[0] = fields[0];
        headerFields[1] = String.valueOf(text.charAt(3));
        for (int i = 1; i < fields.length; i++) {
          headerFields[i + 1] = fields[i];
        }
        fields = headerFields;
      }
      for (int i = 1; i < fields.length && i < childComponents.length; i++) {
        String fieldText = fields[i];
        HL7Component field = childComponents[i];
        if (field != null) {
          if (isHeader && i <= 2) {
            field.setRawTextReceived(fieldText);
            field.setValue(fieldText);
          } else {
            int repeatPos = -1;
            int fieldRepetition = 1;
            while ((repeatPos = fieldText.indexOf("~")) != -1) {
              field.errorLocation = new ERL(errorLocation);
              field.errorLocation.copyValues(errorLocation);
              field.errorLocation.getFieldPosition().setValue("" + i);
              field.errorLocation.getFieldRepetition().setValue("" + fieldRepetition);
              field.parseTextFromField(fieldText.substring(0, repeatPos));
              if (field.getNextComponent() == null) {
                field.setNextComponent(field.makeAnother());
              }
              field = field.getNextComponent();
              fieldText = fieldText.substring(repeatPos + 1);
              fieldRepetition++;
            }
            field.errorLocation = new ERL(errorLocation);
            field.errorLocation.copyValues(errorLocation);
            field.errorLocation.getFieldPosition().setValue("" + i);
            field.errorLocation.getFieldRepetition().setValue("" + fieldRepetition);
            field.parseTextFromField(fieldText);
          }
        }
      }
      for (int i = 1; i < childComponents.length; i++) {
        HL7Component next = childComponents[i];
        int count = 1;
        while (next != null) {
          if (next.errorLocation == null) {
            next.errorLocation = new ERL(errorLocation);
            next.errorLocation.copyValues(errorLocation);
            next.errorLocation.getFieldPosition().setValue("" + i);
            next.errorLocation.getFieldRepetition().setValue("" + count);
          }
          next = next.nextComponent;
          count++;
        }
      }
    }
  }

  public void parseTextFromField(String text) {
    this.rawTextReceived = text;
    this.value = text;
    if (childComponents != null && childComponents.length > 0) {
      String[] subFields = text.split("\\^");
      for (int i = 1; (i - 1) < subFields.length && i < childComponents.length; i++) {
        childComponents[i].errorLocation = new ERL(errorLocation);
        childComponents[i].errorLocation.copyValues(errorLocation);
        childComponents[i].errorLocation.getComponentNumber().setValue("" + i);
        childComponents[i].parseTextFromComponent(subFields[i - 1]);
      }
      for (int i = subFields.length; i < childComponents.length; i++) {
        HL7Component next = childComponents[i];
        if (next.errorLocation == null) {
          next.errorLocation = new ERL(errorLocation);
          next.errorLocation.copyValues(errorLocation);
          next.errorLocation.getComponentNumber().setValue("" + i);
        }
        next = next.nextComponent;
      }
    }
  }

  public void parseTextFromComponent(String text) {

    this.rawTextReceived = text;
    this.value = text;
    if (childComponents != null && childComponents.length > 0) {
      String[] subFields = text.split("\\&");
      for (int i = 1; (i - 1) < subFields.length && i < childComponents.length; i++) {
        childComponents[i].errorLocation = new ERL(errorLocation);
        childComponents[i].errorLocation.copyValues(errorLocation);
        childComponents[i].errorLocation.getSubComponentNumber().setValue("" + i);
        childComponents[i].parseTextFromSubComponent(subFields[i - 1]);
      }
      for (int i = subFields.length; i < childComponents.length; i++) {
        HL7Component next = childComponents[i];
        if (next.errorLocation == null) {
          next.errorLocation = new ERL(errorLocation);
          next.errorLocation.copyValues(errorLocation);
          next.errorLocation.getSubComponentNumber().setValue("" + i);
        }
        next = next.nextComponent;
      }
    }
  }

  public void parseTextFromSubComponent(String text) {
    this.rawTextReceived = text;
    this.value = text;
    // end of the road
  }

  public List<ConformanceIssue> checkConformance() {
    List<ConformanceIssue> conformanceIssueList = new ArrayList<ConformanceIssue>();
    checkConformance(conformanceIssueList, ERR.SEVERITY_ERROR);
    return conformanceIssueList;
  }

  public void checkConformance(List<ConformanceIssue> conformanceIssueList, String severity) {
    if (hasNoChildren()) {
      determineConditionalUsage();

      if (value.length() > 0 && value.length() < lengthMin) {
        if (!severity.equals(ERR.SEVERITY_NONE)) {
          ConformanceIssue ci = new ConformanceIssue();
          conformanceIssueList.add(ci);
          ci.getErrorLocation().copyValues(errorLocation);
          ci.getHl7ErrorCode(); // todo, assign error code
          ci.getSeverity().setValue(severity);
          ci.getUserMessage().setValue("Length of value is less than required minimum " + lengthMin);
        }
      } else if (value.length() > lengthMax) {
        if (!severity.equals(ERR.SEVERITY_NONE)) {
          ConformanceIssue ci = new ConformanceIssue();
          conformanceIssueList.add(ci);
          ci.getErrorLocation().copyValues(errorLocation);
          ci.getHl7ErrorCode(); // todo, assign error code
          ci.getSeverity().setValue(severity);
          ci.getUserMessage().setValue(
              "Length of value is more than required maximum " + lengthMax + ". Truncating data.");
        }
        value = value.substring(0, lengthMax);
      }
      if (value.equals("")) {
        value = defaultValueWhenBlank;
      }
      if (constrainedToValues != null) {
        boolean foundValue = false;
        for (String s : constrainedToValues) {
          if (s.equals(value)) {
            foundValue = true;
          }
        }
        if (!foundValue) {
          if (!severity.equals(ERR.SEVERITY_NONE)) {
            ConformanceIssue ci = new ConformanceIssue();
            conformanceIssueList.add(ci);
            ci.getErrorLocation().copyValues(errorLocation);
            ci.getHl7ErrorCode(); // todo, assign error code
            ci.getSeverity().setValue(severity);
            ci.getUserMessage()
                .setValue("Value is not allowed or recognized. Field is constrained to specific values.");
          }
          value = "";
        }
      }
      if (valueSet != null) {
        // TODO Look up values in table
      }
      if (formatAnalyzer != null) {
        formatAnalyzer.analyze();
        if (!severity.equals(ERR.SEVERITY_NONE)) {
          for (String problem : formatAnalyzer.getErrorMessageList()) {
            ConformanceIssue ci = new ConformanceIssue();
            conformanceIssueList.add(ci);
            ci.getErrorLocation().copyValues(errorLocation);
            ci.getHl7ErrorCode(); // todo, assign error code
            ci.getSeverity().setValue(severity);
            ci.getUserMessage().setValue(getComponentNameFull() + " has invalid format: " + problem);
          }
        }
        if (formatAnalyzer.hasError())
        {
          value = "";
        }
      }

      empty = value.equals("");
      if (usageType == UsageType.R) {
        if (value.equals("")) {
          if (!severity.equals(ERR.SEVERITY_NONE)) {
            ConformanceIssue ci = new ConformanceIssue();
            conformanceIssueList.add(ci);
            ci.getErrorLocation().copyValues(errorLocation);
            ci.getHl7ErrorCode(); // todo, assign error code
            ci.getSeverity().setValue(severity);
            ci.getUserMessage().setValue(getComponentNameFull() + " is required but was found empty");
          }
        }
      } else if (usageType == UsageType.X) {
        if (!value.equals("")) {
          value = "";
          if (!severity.equals(ERR.SEVERITY_NONE)) {
            ConformanceIssue ci = new ConformanceIssue();
            conformanceIssueList.add(ci);
            ci.getErrorLocation().copyValues(errorLocation);
            ci.getHl7ErrorCode(); // todo, assign error code
            ci.getSeverity().setValue(ERR.SEVERITY_ERROR);
            ci.getUserMessage().setValue(getComponentNameFull() + " must be left empty");
          }
        }
      }

    } else {
      determineConditionalUsage();
      boolean allRequiredFieldsValued = true;
      boolean hasRequiredFields = false;
      boolean atLeastOneOptionFieldValued = false;
      List<HL7Component> missingRequired = new ArrayList<HL7Component>();
      for (int i = 1; i < childComponents.length; i++) {
        HL7Component child = childComponents[i];
        int count = 0;
        {
          HL7Component next = child;
          while (next != null) {
            if (next.errorLocation == null) {
              next.errorLocation = errorLocation;
            }
            String nextSeverity = severity;
            if (severity == ERR.SEVERITY_ERROR) {
              if (usageType == UsageType.RE) {
                nextSeverity = ERR.SEVERITY_WARNING;
              } else if (usageType == UsageType.O) {
                nextSeverity = ERR.SEVERITY_NONE;
              }
            } else if (severity == ERR.SEVERITY_WARNING) {
              if (usageType == UsageType.O) {
                nextSeverity = ERR.SEVERITY_NONE;
              }
            }
            next.checkConformance(conformanceIssueList, nextSeverity);
            for (ConformanceStatement conformanceStatement : next.conformanceStatementList) {
              ConditionalPredicate cp = conformanceStatement.getConditionalPredicate();
              if ((cp == null || cp.isMet()) && !conformanceStatement.conforms()) {
                ConformanceIssue ci = new ConformanceIssue();
                conformanceIssueList.add(ci);
                ci.getErrorLocation().copyValues(next.errorLocation);
                ci.getHl7ErrorCode(); // todo, assign error code
                ci.getSeverity().setValue(nextSeverity);
                ci.getUserMessage().setValue(conformanceStatement.getText());
              }
            }
            if (next.usageType == UsageType.R) {
              hasRequiredFields = true;
              if (next.empty) {
                allRequiredFieldsValued = false;
                missingRequired.add(child);
              }
            } else if (next.usageType != UsageType.X && !next.empty) {
              atLeastOneOptionFieldValued = true;
            }

            count++;
            next = next.getNextComponent();
          }
        }
        if (!severity.equals(ERR.SEVERITY_NONE)) {
          if (count > 1 && count < cardinalityMin) {
            ConformanceIssue ci = new ConformanceIssue();
            conformanceIssueList.add(ci);
            ci.getErrorLocation().copyValues(errorLocation);
            ci.getHl7ErrorCode(); // todo, assign error code
            ci.getSeverity().setValue(severity);
            ci.getUserMessage().setValue(
                getComponentNameFull() + " has less repetitions that what is required: " + cardinalityMin);
          } else if (count > cardinalityMax) {
            ConformanceIssue ci = new ConformanceIssue();
            conformanceIssueList.add(ci);
            ci.getErrorLocation().copyValues(errorLocation);
            ci.getHl7ErrorCode(); // todo, assign error code
            ci.getSeverity().setValue(severity);
            ci.getUserMessage().setValue(
                getComponentNameFull() + "has more repetitions that what is allowed: " + cardinalityMax);
          }
        }

      }
      if (hasRequiredFields) {
        empty = !allRequiredFieldsValued;
      } else {
        empty = !atLeastOneOptionFieldValued;
      }

      if (usageType == UsageType.R) {
        if (empty) {
          if (!severity.equals(ERR.SEVERITY_NONE)) {
            ConformanceIssue ci = new ConformanceIssue();
            conformanceIssueList.add(ci);
            ci.getErrorLocation().copyValues(errorLocation);
            ci.getHl7ErrorCode(); // todo, assign error code
            ci.getSeverity().setValue(severity);
            StringBuffer description = new StringBuffer();
            description.append(getComponentNameFull());
            description.append(" is empty because not all required fields are valued");
            if (missingRequired.size() > 0) {
              description.append(", expecting " + missingRequired.get(0).getComponentNameFull());
            }
            for (int j = 1; j < missingRequired.size(); j++) {
              description.append(", ");
              description.append(missingRequired.get(1).getComponentNameFull());
            }
            ci.getUserMessage().setValue(description.toString());
          }
        }
      } else if (usageType == UsageType.X) {
        if (!empty) {
          ConformanceIssue ci = new ConformanceIssue();
          conformanceIssueList.add(ci);
          ci.getErrorLocation().copyValues(errorLocation);
          ci.getHl7ErrorCode(); // todo, assign error code
          ci.getSeverity().setValue(ERR.SEVERITY_ERROR);
          ci.getUserMessage().setValue(getComponentNameFull() + " must be empty");
        }
      }
    }
  }

  public String createMessage() {
    if (hasNoChildren()) {
      return "";
    }
    String messageText = "";
    for (int i = 1; i < childComponents.length; i++) {
      HL7Component next = childComponents[i];
      while (next != null) {
        String segmentText = next.createSegment();
        if (!segmentText.equals("")) {
          messageText = messageText + segmentText + "\r";
        }
        next = next.getNextComponent();
      }
    }
    return messageText;
  }

  public String createSegment() {
    if (hasNoChildren()) {
      return "";
    }
    boolean isHeader = componentCode.equals("MSH") || componentCode.equals("BHS") || componentCode.equals("FHS");
    String segmentText = "|";
    boolean foundData = false;
    for (int i = childComponents.length - 1; i > (isHeader ? 1 : 0); i--) {
      String fieldText = "";
      boolean hasRepeat = false;
      HL7Component next = childComponents[i];
      while (next != null) {
        if (hasRepeat) {
          fieldText = fieldText + "~";
        }
        fieldText = fieldText + next.createField();
        hasRepeat = true;
        next = next.nextComponent;
      }
      if (!fieldText.equals("")) {
        segmentText = fieldText + segmentText;
        foundData = true;
      }
      if (foundData) {
        segmentText = "|" + segmentText;
      }
    }
    return componentCode + segmentText;
  }

  public String createField() {
    if (hasNoChildren()) {
      return value;
    }
    String fieldText = "";
    boolean foundData = false;
    for (int i = childComponents.length - 1; i > 0; i--) {
      String subText = childComponents[i].createField();
      if (foundData) {
        fieldText = "^" + fieldText;
      }
      if (!subText.equals("")) {
        fieldText = subText + fieldText;
        foundData = true;
      }
    }
    return fieldText;
  }

  public String createSubField() {
    if (hasNoChildren()) {
      return value;
    }
    String subText = "";
    boolean foundData = false;
    for (int i = childComponents.length - 1; i > 0; i--) {
      String text = childComponents[i].createField();
      if (!text.equals("")) {
        subText = text + subText;
        foundData = true;
      } else if (foundData) {
        subText = "&" + subText;
      }
    }
    return subText;
  }

  public void determineConditionalUsage() {
    if (usageType == UsageType.C) {
      if (conditionalPredicate.isMet()) {
        usageType = conditionalPredicate.getUsageTypeMet();
      } else {
        usageType = conditionalPredicate.getUsageTypeNotMet();
      }
    }
  }

  public boolean hasNoChildren() {
    return childComponents == null || childComponents.length == 0;
  }

  public String getDefaultValueWhenBlank() {
    return defaultValueWhenBlank;
  }

  public void setDefaultValueWhenBlank(String defaultValueWhenBlank) {
    this.defaultValueWhenBlank = defaultValueWhenBlank;
  }

  public void addConformanceStatement(ConformanceStatement conformanceStatement) {
    conformanceStatementList.add(conformanceStatement);
    conformanceStatement.setComponent(this);
  }

  public void setConstrainedToValue(String constrainedToValue) {
    constrainedToValues = new String[1];
    constrainedToValues[0] = constrainedToValue;
  }

  public String[] getConstrainedToValues() {
    return constrainedToValues;
  }

  public void setConstrainedToValues(String[] constrainedToValues) {
    this.constrainedToValues = constrainedToValues;
  }

  public ConditionalPredicate getConditionalPredicate() {
    return conditionalPredicate;
  }

  public void setConditionalPredicate(ConditionalPredicate conditionalPredicate) {
    this.conditionalPredicate = conditionalPredicate;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public UsageType getUsageType() {
    return usageType;
  }

  public void setUsageType(UsageType usageType) {
    this.usageType = usageType;
  }

  public String getComponentNameSpecific() {
    return componentNameSpecific;
  }

  public void setComponentNameSpecific(String componentNameSpecific) {
    this.componentNameSpecific = componentNameSpecific;
  }

  public HL7Component[] getChildComponent() {
    return childComponents;
  }

  public HL7Component getChildComponent(int pos) {
    return childComponents[pos];
  }

  protected void setChild(int pos, HL7Component childComponent) {
    childComponents[pos] = childComponent;
    childComponent.setSequence(pos);
  }

  public String getComponentNameGeneric() {
    return componentNameGeneric;
  }

  protected void setComponentNameGeneric(String componentName) {
    this.componentNameGeneric = componentName;
  }

  public String getComponentCode() {
    return componentCode;
  }

  protected void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

  public int getSequence() {
    return sequence;
  }

  protected void setSequence(int sequence) {
    this.sequence = sequence;
  }

  public int getLengthMin() {
    return lengthMin;
  }

  public void setLengthMin(int lengthMin) {
    this.lengthMin = lengthMin;
  }

  public int getLengthMax() {
    return lengthMax;
  }

  public void setLengthMax(int lengthMax) {
    this.lengthMax = lengthMax;
  }

  public void setLength(int lengthMin, int lengthMax) {
    this.lengthMin = lengthMin;
    this.lengthMax = lengthMax;
  }

  public int getCardinalityMin() {
    return cardinalityMin;
  }

  public void setCardinalityMin(int cardinalityMin) {
    this.cardinalityMin = cardinalityMin;
  }

  public int getCardinalityMax() {
    return cardinalityMax;
  }

  public void setCardinalityMax(int cardinalityMax) {
    this.cardinalityMax = cardinalityMax;
  }

  public void setCardinality(int cardinalityMin, int cardinalityMax) {
    this.cardinalityMin = cardinalityMin;
    this.cardinalityMax = cardinalityMax;
  }

  public void copyValues(HL7Component copy) {
    this.value = copy.value;
    if (hasNoChildren()) {
      return;
    }
    if (this.childComponents != null && copy.childComponents != null) {
      for (int i = 1; i < this.childComponents.length && i < copy.childComponents.length; i++) {
        if (this.childComponents[i] != null && copy.childComponents[i] != null) {
          this.childComponents[i].copyValues(copy.childComponents[i]);
        }
      }
    }
  }

  public String getComponentNameFull() {
    if (errorLocation != null) {
      String s = errorLocation.getSegmentID().getValue() + "-" + errorLocation.getFieldPosition().getValue();
      if (!errorLocation.getComponentNumber().getValue().equals("")) {
        s = s + "." + errorLocation.getComponentNumber().getValue();
        if (!errorLocation.getSubComponentNumber().getValue().equals("")) {
          s = s + "." + errorLocation.getSubComponentNumber().getValue();
        }
      }
      return componentNameSpecific + " (" + s + ")";
    }
    return componentNameSpecific;
  }

  public void printValues(PrintStream out) {
    printValues(out, "");
  }

  private void printValues(PrintStream out, String indent) {
    out.println(indent + " + " + getComponentNameFull() + " = " + value);
    if (!hasNoChildren()) {
      for (int i = 1; i < childComponents.length; i++) {
        HL7Component child = childComponents[i];
        child.printValues(out, indent + "  ");
      }
    }
  }
}
