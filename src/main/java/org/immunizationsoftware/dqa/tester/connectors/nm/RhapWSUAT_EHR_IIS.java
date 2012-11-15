

/**
 * RhapWSUAT_EHR_IIS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.immunizationsoftware.dqa.tester.connectors.nm;

    /*
     *  RhapWSUAT_EHR_IIS java interface
     */

    public interface RhapWSUAT_EHR_IIS {
          

        /**
          * Auto generated method signature
          * submit single message
                    * @param submitSingleMessage0
                
             * @throws org.immunizationsoftware.dqa.tester.connectors.nm.SubmitSingleMessageFault : 
         */

         
                     public _2011.iisb.cdc.SubmitSingleMessageResponse submitSingleMessage(

                        _2011.iisb.cdc.SubmitSingleMessage submitSingleMessage0)
                        throws java.rmi.RemoteException
             
          ,org.immunizationsoftware.dqa.tester.connectors.nm.SubmitSingleMessageFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * submit single message
                * @param submitSingleMessage0
            
          */
        public void startsubmitSingleMessage(

            _2011.iisb.cdc.SubmitSingleMessage submitSingleMessage0,

            final org.immunizationsoftware.dqa.tester.connectors.nm.RhapWSUAT_EHR_IISCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * the connectivity test
                    * @param connectivityTest2
                
             * @throws org.immunizationsoftware.dqa.tester.connectors.nm.ConnectivityTestFault : 
         */

         
                     public _2011.iisb.cdc.ConnectivityTestResponse connectivityTest(

                        _2011.iisb.cdc.ConnectivityTest connectivityTest2)
                        throws java.rmi.RemoteException
             
          ,org.immunizationsoftware.dqa.tester.connectors.nm.ConnectivityTestFault;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * the connectivity test
                * @param connectivityTest2
            
          */
        public void startconnectivityTest(

            _2011.iisb.cdc.ConnectivityTest connectivityTest2,

            final org.immunizationsoftware.dqa.tester.connectors.nm.RhapWSUAT_EHR_IISCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    