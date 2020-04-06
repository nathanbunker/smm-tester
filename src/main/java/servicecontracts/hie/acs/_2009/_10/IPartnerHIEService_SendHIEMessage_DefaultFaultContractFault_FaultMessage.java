
/**
 * IPartnerHIEService_SendHIEMessage_DefaultFaultContractFault_FaultMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.9  Built on : Nov 16, 2018 (12:05:37 GMT)
 */

package servicecontracts.hie.acs._2009._10;

public class IPartnerHIEService_SendHIEMessage_DefaultFaultContractFault_FaultMessage extends java.lang.Exception{

    private static final long serialVersionUID = 1584976305311L;
    
    private faultcontracts.facade.ccd.acs._2008._02.DefaultFaultContractE faultMessage;

    
        public IPartnerHIEService_SendHIEMessage_DefaultFaultContractFault_FaultMessage() {
            super("IPartnerHIEService_SendHIEMessage_DefaultFaultContractFault_FaultMessage");
        }

        public IPartnerHIEService_SendHIEMessage_DefaultFaultContractFault_FaultMessage(java.lang.String s) {
           super(s);
        }

        public IPartnerHIEService_SendHIEMessage_DefaultFaultContractFault_FaultMessage(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public IPartnerHIEService_SendHIEMessage_DefaultFaultContractFault_FaultMessage(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(faultcontracts.facade.ccd.acs._2008._02.DefaultFaultContractE msg){
       faultMessage = msg;
    }
    
    public faultcontracts.facade.ccd.acs._2008._02.DefaultFaultContractE getFaultMessage(){
       return faultMessage;
    }
}
    