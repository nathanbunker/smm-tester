
/**
 * SubmitSingleMessageFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package org.immunizationsoftware.dqa.tester.connectors.ca;

public class SubmitSingleMessageFault extends java.lang.Exception{

    private static final long serialVersionUID = 1425643671207L;
    
    private org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.MessageTooLargeFault faultMessage;

    
        public SubmitSingleMessageFault() {
            super("SubmitSingleMessageFault");
        }

        public SubmitSingleMessageFault(java.lang.String s) {
           super(s);
        }

        public SubmitSingleMessageFault(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public SubmitSingleMessageFault(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.MessageTooLargeFault msg){
       faultMessage = msg;
    }
    
    public org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.MessageTooLargeFault getFaultMessage(){
       return faultMessage;
    }
}
    