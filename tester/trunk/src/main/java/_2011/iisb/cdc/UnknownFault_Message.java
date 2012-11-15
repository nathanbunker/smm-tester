
/**
 * UnknownFault_Message.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package _2011.iisb.cdc;

public class UnknownFault_Message extends java.lang.Exception{

    private static final long serialVersionUID = 1352987427388L;
    
    private _2011.iisb.cdc.Fault faultMessage;

    
        public UnknownFault_Message() {
            super("UnknownFault_Message");
        }

        public UnknownFault_Message(java.lang.String s) {
           super(s);
        }

        public UnknownFault_Message(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public UnknownFault_Message(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(_2011.iisb.cdc.Fault msg){
       faultMessage = msg;
    }
    
    public _2011.iisb.cdc.Fault getFaultMessage(){
       return faultMessage;
    }
}
    