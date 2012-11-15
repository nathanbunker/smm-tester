
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

        
            package org.immunizationsoftware.dqa.tester.connectors.nm._2011.iisb.cdc;
        
            /**
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "urn:cdc:iisb:2011".equals(namespaceURI) &&
                  "soapFaultType".equals(typeName)){
                   
                            return  _2011.iisb.cdc.SoapFaultType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "urn:cdc:iisb:2011".equals(namespaceURI) &&
                  "submitSingleMessageResponseType".equals(typeName)){
                   
                            return  _2011.iisb.cdc.SubmitSingleMessageResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "urn:cdc:iisb:2011".equals(namespaceURI) &&
                  "connectivityTestRequestType".equals(typeName)){
                   
                            return  _2011.iisb.cdc.ConnectivityTestRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "urn:cdc:iisb:2011".equals(namespaceURI) &&
                  "SecurityFaultType".equals(typeName)){
                   
                            return  _2011.iisb.cdc.SecurityFaultType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "urn:cdc:iisb:2011".equals(namespaceURI) &&
                  "submitSingleMessageRequestType".equals(typeName)){
                   
                            return  _2011.iisb.cdc.SubmitSingleMessageRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "urn:cdc:iisb:2011".equals(namespaceURI) &&
                  "MessageTooLargeFaultType".equals(typeName)){
                   
                            return  _2011.iisb.cdc.MessageTooLargeFaultType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "urn:cdc:iisb:2011".equals(namespaceURI) &&
                  "connectivityTestResponseType".equals(typeName)){
                   
                            return  _2011.iisb.cdc.ConnectivityTestResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "urn:cdc:iisb:2011".equals(namespaceURI) &&
                  "UnsupportedOperationFaultType".equals(typeName)){
                   
                            return  _2011.iisb.cdc.UnsupportedOperationFaultType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    