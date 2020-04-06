

/**
 * PartnerHIEService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.9  Built on : Nov 16, 2018 (12:05:37 GMT)
 */

    package servicecontracts.hie.acs._2009._10;

    /*
     *  PartnerHIEService java interface
     */

    public interface PartnerHIEService {
          

        /**
          * Auto generated method signature
          * 
                    * @param sendHIEMessage0
                
             * @throws servicecontracts.hie.acs._2009._10.IPartnerHIEService_SendHIEMessage_DefaultFaultContractFault_FaultMessage : 
         */

         
                     public servicecontracts.hie.acs._2009._10.SendHIEMessageResponse sendHIEMessage(

                        servicecontracts.hie.acs._2009._10.SendHIEMessage sendHIEMessage0)
                        throws java.rmi.RemoteException
             
          ,servicecontracts.hie.acs._2009._10.IPartnerHIEService_SendHIEMessage_DefaultFaultContractFault_FaultMessage;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param sendHIEMessage0
            
          */
        public void startsendHIEMessage(

            servicecontracts.hie.acs._2009._10.SendHIEMessage sendHIEMessage0,

            final servicecontracts.hie.acs._2009._10.PartnerHIEServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param sendHIEMessageWithTimeInsensitivity2
                
             * @throws servicecontracts.hie.acs._2009._10.IPartnerHIEService_SendHIEMessageWithTimeInsensitivity_DefaultFaultContractFault_FaultMessage : 
         */

         
                     public servicecontracts.hie.acs._2009._10.SendHIEMessageWithTimeInsensitivityResponse sendHIEMessageWithTimeInsensitivity(

                        servicecontracts.hie.acs._2009._10.SendHIEMessageWithTimeInsensitivity sendHIEMessageWithTimeInsensitivity2)
                        throws java.rmi.RemoteException
             
          ,servicecontracts.hie.acs._2009._10.IPartnerHIEService_SendHIEMessageWithTimeInsensitivity_DefaultFaultContractFault_FaultMessage;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param sendHIEMessageWithTimeInsensitivity2
            
          */
        public void startsendHIEMessageWithTimeInsensitivity(

            servicecontracts.hie.acs._2009._10.SendHIEMessageWithTimeInsensitivity sendHIEMessageWithTimeInsensitivity2,

            final servicecontracts.hie.acs._2009._10.PartnerHIEServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    