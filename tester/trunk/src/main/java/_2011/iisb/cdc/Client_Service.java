

/**
 * Client_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package _2011.iisb.cdc;

    /*
     *  Client_Service java interface
     */

    public interface Client_Service {
          

        /**
          * Auto generated method signature
          * submit single message
                    * @param submitSingleMessage0
                
             * @throws _2011.iisb.cdc.UnknownFault_Message : 
             * @throws _2011.iisb.cdc.SecurityFault_Message : 
             * @throws _2011.iisb.cdc.MessageTooLargeFault_Message : 
         */

         
                     public _2011.iisb.cdc.SubmitSingleMessageResponse submitSingleMessage(

                        _2011.iisb.cdc.SubmitSingleMessage submitSingleMessage0)
                        throws java.rmi.RemoteException
             
          ,_2011.iisb.cdc.UnknownFault_Message
          ,_2011.iisb.cdc.SecurityFault_Message
          ,_2011.iisb.cdc.MessageTooLargeFault_Message;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * submit single message
                * @param submitSingleMessage0
            
          */
        public void startsubmitSingleMessage(

            _2011.iisb.cdc.SubmitSingleMessage submitSingleMessage0,

            final _2011.iisb.cdc.Client_ServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * the connectivity test
                    * @param connectivityTest2
                
             * @throws _2011.iisb.cdc.UnsupportedOperationFault_Message : 
             * @throws _2011.iisb.cdc.UnknownFault_Message : 
         */

         
                     public _2011.iisb.cdc.ConnectivityTestResponse connectivityTest(

                        _2011.iisb.cdc.ConnectivityTest connectivityTest2)
                        throws java.rmi.RemoteException
             
          ,_2011.iisb.cdc.UnsupportedOperationFault_Message
          ,_2011.iisb.cdc.UnknownFault_Message;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * the connectivity test
                * @param connectivityTest2
            
          */
        public void startconnectivityTest(

            _2011.iisb.cdc.ConnectivityTest connectivityTest2,

            final _2011.iisb.cdc.Client_ServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    