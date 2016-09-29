

/**
 * InterOp_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package _2011.iisb.cdc;

    /*
     *  InterOp_Service java interface
     */

    public interface InterOp_Service {
          

        /**
          * Auto generated method signature
          * 
                    * @param connectivityTestFL0
                
         */

         
                     public _2011.iisb.cdc.ConnectivityTestFLResponse connectivityTestFL(

                        _2011.iisb.cdc.ConnectivityTestFL connectivityTestFL0)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param connectivityTestFL0
            
          */
        public void startconnectivityTestFL(

            _2011.iisb.cdc.ConnectivityTestFL connectivityTestFL0,

            final _2011.iisb.cdc.InterOp_ServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param submitSingleMessage2
                
         */

         
                     public _2011.iisb.cdc.SubmitSingleMessageResponse submitSingleMessage(

                        _2011.iisb.cdc.SubmitSingleMessage submitSingleMessage2)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param submitSingleMessage2
            
          */
        public void startsubmitSingleMessage(

            _2011.iisb.cdc.SubmitSingleMessage submitSingleMessage2,

            final _2011.iisb.cdc.InterOp_ServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    