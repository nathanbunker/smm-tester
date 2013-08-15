

/**
 * Client_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.immunizationsoftware.dqa.tester.connectors.hi;

    /*
     *  Client_Service java interface
     */

    public interface Client_Service {
          

        /**
          * Auto generated method signature
          * submit single message
                    * @param submitSingleMessage0
                
             * @throws org.immunizationsoftware.dqa.tester.connectors.hi.UnknownFault_Message : 
             * @throws org.immunizationsoftware.dqa.tester.connectors.hi.SecurityFault_Message : 
             * @throws org.immunizationsoftware.dqa.tester.connectors.hi.MessageTooLargeFault_Message : 
         */

         
                     public org.immunizationsoftware.dqa.tester.connectors.hi.SubmitSingleMessageResponse submitSingleMessage(

                        org.immunizationsoftware.dqa.tester.connectors.hi.SubmitSingleMessage submitSingleMessage0)
                        throws java.rmi.RemoteException
             
          ,org.immunizationsoftware.dqa.tester.connectors.hi.UnknownFault_Message
          ,org.immunizationsoftware.dqa.tester.connectors.hi.SecurityFault_Message
          ,org.immunizationsoftware.dqa.tester.connectors.hi.MessageTooLargeFault_Message;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * submit single message
                * @param submitSingleMessage0
            
          */
        public void startsubmitSingleMessage(

            org.immunizationsoftware.dqa.tester.connectors.hi.SubmitSingleMessage submitSingleMessage0,

            final org.immunizationsoftware.dqa.tester.connectors.hi.Client_ServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * the connectivity test
                    * @param connectivityTest2
                
             * @throws org.immunizationsoftware.dqa.tester.connectors.hi.UnsupportedOperationFault_Message : 
             * @throws org.immunizationsoftware.dqa.tester.connectors.hi.UnknownFault_Message : 
         */

         
                     public org.immunizationsoftware.dqa.tester.connectors.hi.ConnectivityTestResponse connectivityTest(

                        org.immunizationsoftware.dqa.tester.connectors.hi.ConnectivityTest connectivityTest2)
                        throws java.rmi.RemoteException
             
          ,org.immunizationsoftware.dqa.tester.connectors.hi.UnsupportedOperationFault_Message
          ,org.immunizationsoftware.dqa.tester.connectors.hi.UnknownFault_Message;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * the connectivity test
                * @param connectivityTest2
            
          */
        public void startconnectivityTest(

            org.immunizationsoftware.dqa.tester.connectors.hi.ConnectivityTest connectivityTest2,

            final org.immunizationsoftware.dqa.tester.connectors.hi.Client_ServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    