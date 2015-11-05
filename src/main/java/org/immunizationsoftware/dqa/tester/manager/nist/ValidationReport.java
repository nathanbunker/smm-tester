package org.immunizationsoftware.dqa.tester.manager.nist;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ValidationReport
{
  private HeaderReport headerReport = null;
  private List<Assertion> assertionList = new ArrayList<Assertion>();

  public HeaderReport getHeaderReport() {
    return headerReport;
  }

  public List<Assertion> getAssertionList() {
    return assertionList;
  }

  public ValidationReport(String xml) {
    headerReport = new HeaderReport();
    if (xml.startsWith("{")) {
      try {
        // read as Json
        JSONObject obj = new JSONObject(xml);
        JSONObject metaData = obj.getJSONObject("metaData");
        {
          JSONObject counts = metaData.getJSONObject("counts");
          headerReport.setAlertCount(counts.getInt("alert"));
          headerReport.setWarningCount(counts.getInt("warning"));
          headerReport.setInformCount(counts.getInt("informational"));
          headerReport.setErrorCount(counts.getInt("error"));
          headerReport.setAffirmCount(counts.getInt("affirmative"));
        }
        {
          JSONObject detections = obj.getJSONObject("detections");
          String[] detectionNames = JSONObject.getNames(detections);
          for (String detectionName : detectionNames) {
            JSONObject detection = detections.getJSONObject(detectionName);
            String[] usageNames = JSONObject.getNames(detection);
            for (String usageName : usageNames) {
              JSONArray usageArray = detection.getJSONArray(usageName);
              for (int i = 0; i < usageArray.length(); i++) {
                JSONObject usage = usageArray.getJSONObject(i);
                Assertion assertion = new Assertion();
                assertionList.add(assertion);
                assertion.setPath(usage.getString("path"));
                assertion.setLine(usage.getInt("line"));
                assertion.setColumn(usage.getInt("column"));
                assertion.setDescription(usage.getString("description"));
                assertion.setResult(detectionName);
                assertion.setType(usageName);
              }
            }
          }
        }

        headerReport.setValidationStatus("Complete");
      } catch (JSONException je) {
        je.printStackTrace();
      }
    } else {
      // read as XML

      try {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("HL7V2MessageValidationReport");
        for (int i = 0; i < nList.getLength(); i++) {
          Node nNode = nList.item(i);
          if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList documentNodeList = nNode.getChildNodes();
            for (int d = 0; d < documentNodeList.getLength(); d++) {
              Node documentNode = documentNodeList.item(d);
              if (documentNode.getNodeType() == Node.ELEMENT_NODE) {
                if (documentNode.getNodeName().equals("HeaderReport")) {
                  NodeList headerNodeList = documentNode.getChildNodes();
                  for (int j = 0; j < headerNodeList.getLength(); j++) {
                    Node headerNode = headerNodeList.item(j);
                    if (headerNode.getNodeType() == Node.ELEMENT_NODE) {
                      if (headerNode.getNodeName().equals("mes:ValidationStatus")) {
                        headerReport.setValidationStatus(headerNode.getTextContent());
                      } else if (headerNode.getNodeName().equals("mes:ServiceName")) {
                        headerReport.setServiceName(headerNode.getTextContent());
                        // <mes:ServiceName>NIST HL7V2 Message
                        // Validation</mes:ServiceName>
                      } else if (headerNode.getNodeName().equals("mes:ServiceProvider")) {
                        headerReport.setServiceProvider(headerNode.getTextContent());
                        // <mes:ServiceProvider>NIST</mes:ServiceProvider>
                      } else if (headerNode.getNodeName().equals("mes:ServiceVersion")) {
                        headerReport.setServiceVersion(headerNode.getTextContent());
                        // <mes:ServiceVersion>1.0</mes:ServiceVersion>
                      } else if (headerNode.getNodeName().equals("mes:StandardType")) {
                        headerReport.setStandardType(headerNode.getTextContent());
                        // <mes:StandardType>HL7 V2</mes:StandardType>
                      } else if (headerNode.getNodeName().equals("mes:StandardVersion")) {
                        headerReport.setStandardVersion(headerNode.getTextContent());
                        // <mes:StandardVersion>2.5.1</mes:StandardVersion>
                      } else if (headerNode.getNodeName().equals("mes:ValidationType")) {
                        headerReport.setValidationType(headerNode.getTextContent());
                        // <mes:ValidationType>Automated</mes:ValidationType>
                      } else if (headerNode.getNodeName().equals("mes:TestIdentifier")) {
                        headerReport.setTestIdentifier(headerNode.getTextContent());
                        // <mes:TestIdentifier/>
                      } else if (headerNode.getNodeName().equals("mes:DateOfTest")) {
                        headerReport.setDateOfTest(headerNode.getTextContent());
                        // <mes:DateOfTest>2015-11-04-05:00</mes:DateOfTest>
                      } else if (headerNode.getNodeName().equals("mes:TimeOfTest")) {
                        headerReport.setTimeOfTest(headerNode.getTextContent());
                        // <mes:TimeOfTest>10:13:50.546-05:00</mes:TimeOfTest>
                      } else if (headerNode.getNodeName().equals("mes:ValidationObjectReferenceList")) {
                        headerReport.setValidationObjectReferenceList(headerNode.getTextContent());
                        // <mes:ValidationObjectReferenceList/>
                      } else if (headerNode.getNodeName().equals("mes:TestObjectReferenceList")) {
                        headerReport.setTestObjectReferenceList(headerNode.getTextContent());
                        // <mes:TestObjectReferenceList/>
                      } else if (headerNode.getNodeName().equals("mes:PositiveAssertionIndicator")) {
                        headerReport.setPositiveAssertionIndicator(makeBoolean(headerNode.getTextContent()));
                        // <mes:PositiveAssertionIndicator>true</mes:PositiveAssertionIndicator>
                      } else if (headerNode.getNodeName().equals("mes:ResultOfTest")) {
                        headerReport.setResultOfTest(headerNode.getTextContent());
                        // <mes:ResultOfTest>Failed</mes:ResultOfTest>
                      } else if (headerNode.getNodeName().equals("mes:AffirmCount")) {
                        headerReport.setAffirmCount(makeInt(headerNode.getTextContent()));
                        // <mes:AffirmCount>6</mes:AffirmCount>
                      } else if (headerNode.getNodeName().equals("mes:ErrorCount")) {
                        headerReport.setErrorCount(makeInt(headerNode.getTextContent()));
                        // <mes:ErrorCount>8</mes:ErrorCount>
                      } else if (headerNode.getNodeName().equals("mes:WarningCount")) {
                        headerReport.setWarningCount(makeInt(headerNode.getTextContent()));
                        // <mes:WarningCount>0</mes:WarningCount>
                      } else if (headerNode.getNodeName().equals("mes:IgnoreCount")) {
                        headerReport.setIgnoreCount(makeInt(headerNode.getTextContent()));
                        // <mes:IgnoreCount>0</mes:IgnoreCount>
                      } else if (headerNode.getNodeName().equals("mes:AlertCount")) {
                        headerReport.setAlertCount(makeInt(headerNode.getTextContent()));
                        // <mes:AlertCount>2</mes:AlertCount>
                      }
                    }
                  }
                } else if (documentNode.getNodeName().equals("SpecificReport")) {
                  NodeList specificNodeList = documentNode.getChildNodes();
                  for (int i3 = 0; i3 < specificNodeList.getLength(); i3++) {
                    Node specificNode = specificNodeList.item(i3);
                    if (specificNode.getNodeName().equals("AssertionList")) {
                      NodeList assertionListNodeList = specificNode.getChildNodes();
                      for (int i4 = 0; i4 < assertionListNodeList.getLength(); i4++) {
                        Node assertionListNode = assertionListNodeList.item(i4);
                        if (assertionListNode.getNodeName().equals("Assertion")) {
                          Assertion assertion = new Assertion();
                          assertionList.add(assertion);
                          assertion.setType(makeString(assertionListNode.getAttributes().getNamedItem("Type")));
                          assertion.setResult(makeString(assertionListNode.getAttributes().getNamedItem("Result")));
                          assertion.setSeverity(makeString(assertionListNode.getAttributes().getNamedItem("Severity")));
                          NodeList assertionNodeList = assertionListNode.getChildNodes();
                          for (int i5 = 0; i5 < assertionNodeList.getLength(); i5++) {
                            Node assertionNode = assertionNodeList.item(i5);
                            if (assertionNode.getNodeName().equals("Description")) {
                              assertion.setDescription(assertionNode.getTextContent());
                            } else if (assertionNode.getNodeName().equals("Location")) {
                              NodeList locationNodeList = assertionNode.getChildNodes();
                              for (int i6 = 0; i6 < locationNodeList.getLength(); i6++) {
                                Node locationNode = locationNodeList.item(i6);
                                if (locationNode.getNodeName().equals("Line")) {
                                  assertion.setLine(makeInt(locationNode.getTextContent()));
                                } else if (locationNode.getNodeName().equals("Column")) {
                                  assertion.setColumn(makeInt(locationNode.getTextContent()));
                                } else if (locationNode.getNodeName().equals("Path")) {
                                  assertion.setPath(locationNode.getTextContent());
                                } else if (locationNode.getNodeName().equals("ProfileElement")) {
                                  NodeList profileElementNodeList = locationNode.getChildNodes();
                                  for (int i7 = 0; i7 < profileElementNodeList.getLength(); i7++) {
                                    Node profileElementNode = profileElementNodeList.item(i7);
                                    if (profileElementNode.getNodeName().equals("prof:Segment")) {
                                      assertion.setSegment(
                                          makeString(profileElementNode.getAttributes().getNamedItem("Name")));
                                      NodeList segmentNodeList = profileElementNode.getChildNodes();
                                      for (int i8 = 0; i8 < segmentNodeList.getLength(); i8++) {
                                        Node segmentNode = segmentNodeList.item(i8);
                                        if (segmentNode.getNodeName().equals("prof:Field")) {
                                          assertion
                                              .setField(makeString(segmentNode.getAttributes().getNamedItem("Name")));
                                          NodeList fieldNodeList = segmentNode.getChildNodes();
                                          for (int i9 = 0; i9 < fieldNodeList.getLength(); i9++) {
                                            Node fieldNode = fieldNodeList.item(i9);
                                            if (fieldNode.getNodeName().equals("prof:Component")) {
                                              assertion.setComponent(
                                                  makeString(fieldNode.getAttributes().getNamedItem("Name")));
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }

                      }
                    }
                  }
                }
              }
            }
          }
        }
      } catch (Exception e) {
        System.out.println("Unable to process this: ");
        System.out.println(xml);
        e.printStackTrace();
      }
    }
  }

  private boolean makeBoolean(String s) {
    if (s == null) {
      return false;
    }
    return s.equalsIgnoreCase("true");
  }

  private int makeInt(String s) {
    if (s == null) {
      return 0;
    }
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException nfe) {
      return 0;
    }
  }

  private String makeString(Node n) {
    if (n == null) {
      return "";
    }
    return n.getNodeValue();
  }

}
