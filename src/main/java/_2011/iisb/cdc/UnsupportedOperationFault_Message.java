
/**
 * UnsupportedOperationFault_Message.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package _2011.iisb.cdc;

public class UnsupportedOperationFault_Message extends java.lang.Exception{

    private static final long serialVersionUID = 1352987427376L;
    
    private _2011.iisb.cdc.UnsupportedOperationFault faultMessage;

    
        public UnsupportedOperationFault_Message() {
            super("UnsupportedOperationFault_Message");
        }

        public UnsupportedOperationFault_Message(java.lang.String s) {
           super(s);
        }

        public UnsupportedOperationFault_Message(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public UnsupportedOperationFault_Message(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(_2011.iisb.cdc.UnsupportedOperationFault msg){
       faultMessage = msg;
    }
    
    public _2011.iisb.cdc.UnsupportedOperationFault getFaultMessage(){
       return faultMessage;
    }
}
    