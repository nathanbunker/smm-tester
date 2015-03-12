
/**
 * ConnectivityTestFault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package org.immunizationsoftware.dqa.tester.connectors.ca;

public class ConnectivityTestFault extends java.lang.Exception{

    private static final long serialVersionUID = 1425643671196L;
    
    private org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.UnsupportedOperationFault faultMessage;

    
        public ConnectivityTestFault() {
            super("ConnectivityTestFault");
        }

        public ConnectivityTestFault(java.lang.String s) {
           super(s);
        }

        public ConnectivityTestFault(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ConnectivityTestFault(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.UnsupportedOperationFault msg){
       faultMessage = msg;
    }
    
    public org.immunizationsoftware.dqa.tester.connectors.ca.Client_ServiceStub.UnsupportedOperationFault getFaultMessage(){
       return faultMessage;
    }
}
    