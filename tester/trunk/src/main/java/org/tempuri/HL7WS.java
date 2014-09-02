

/**
 * HL7WS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.tempuri;

    /*
     *  HL7WS java interface
     */

    public interface HL7WS {
          

        /**
          * Auto generated method signature
          * Consumer supplies userid, password, agency, and HL7 message and the web service response with OK or ERROR. Production Version 1.0.0.0
                    * @param request_x0020_or_x0020_Post_x0020_Information_x0020_from_x0020_the_x0020_Immunization_x0020_Registry0
                
         */

         
                     public org.tempuri.IR_Response processHL7Message(

                        org.tempuri.IR_Request request_x0020_or_x0020_Post_x0020_Information_x0020_from_x0020_the_x0020_Immunization_x0020_Registry0)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * Consumer supplies userid, password, agency, and HL7 message and the web service response with OK or ERROR. Production Version 1.0.0.0
                * @param request_x0020_or_x0020_Post_x0020_Information_x0020_from_x0020_the_x0020_Immunization_x0020_Registry0
            
          */
        public void startprocessHL7Message(

            org.tempuri.IR_Request request_x0020_or_x0020_Post_x0020_Information_x0020_from_x0020_the_x0020_Immunization_x0020_Registry0,

            final org.tempuri.HL7WSCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    