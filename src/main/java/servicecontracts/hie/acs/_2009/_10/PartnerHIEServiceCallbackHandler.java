
/**
 * PartnerHIEServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.9  Built on : Nov 16, 2018 (12:05:37 GMT)
 */

    package servicecontracts.hie.acs._2009._10;

    /**
     *  PartnerHIEServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class PartnerHIEServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public PartnerHIEServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public PartnerHIEServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for sendHIEMessage method
            * override this method for handling normal response from sendHIEMessage operation
            */
           public void receiveResultsendHIEMessage(
                    servicecontracts.hie.acs._2009._10.SendHIEMessageResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendHIEMessage operation
           */
            public void receiveErrorsendHIEMessage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for sendHIEMessageWithTimeInsensitivity method
            * override this method for handling normal response from sendHIEMessageWithTimeInsensitivity operation
            */
           public void receiveResultsendHIEMessageWithTimeInsensitivity(
                    servicecontracts.hie.acs._2009._10.SendHIEMessageWithTimeInsensitivityResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from sendHIEMessageWithTimeInsensitivity operation
           */
            public void receiveErrorsendHIEMessageWithTimeInsensitivity(java.lang.Exception e) {
            }
                


    }
    