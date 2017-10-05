/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immregistries.smm.tester.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Josh Hull
 */
public class MLLPConnector extends Connector
{
  private Socket mllpSocket;
  private int port;

  protected MLLPConnector(String label, String url, String type) {
    super(label, type);
    this.url = url;
  }

  public MLLPConnector(String label, String urlPlusPort) throws IOException {
    super(label, "MLLP");
    //split the URL on a colon to get the port so you can build a socket:
    String[] parts = urlPlusPort.split(":");
    this.url = parts[0];
    this.port = new Integer(parts[1]);
    openConnection();
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {

	  //check to make sure the connection is open
	  //if not, open it again.  Once.
    if (this.mllpSocket == null || !this.mllpSocket.isConnected()) {
      this.mllpSocket = this.openConnection();
    }

	  //send the request over MLLP.
    boolean sent = this.sendAnMLLPMessage(message);

    //wait for a reponse.
    byte[] byteBuffer = new byte[200];
//    socketInputStream.read(byteBuffer);
    //if you wait too long, figure out how to handle that problem, and recover.
    //Step 1: set a timeout...

    String responseString = new String(byteBuffer);
    System.out.println("Received a response! " + responseString);

	  //you're looking for a MCIR ACK.
	  //if you get an HIE ack before then,
	  //wait a bit longer to see if the MCIR one ends up coming eventually.
	  return responseString;
  }

	static final char END_OF_BLOCK = '\u001c';
	static final char START_OF_BLOCK = '\u000b';
	private static final char CARRIAGE_RETURN = 13;

  public boolean sendAnMLLPMessage(String message) throws IOException {

    String mllpMsgOutgoing = this.buildMllpPacket(message);

		InputStream socketInputStream = this.mllpSocket.getInputStream();
		OutputStream socketOutputStream = this.mllpSocket.getOutputStream();

		// Send the MLLP wrapped message to the server!
		socketOutputStream.write(mllpMsgOutgoing.getBytes());

    return true;
  }

  protected String buildMllpPacket(String message) {
    StringBuilder msg = new StringBuilder();
		msg.append(START_OF_BLOCK).append(message).append(END_OF_BLOCK).append(CARRIAGE_RETURN);
    return msg.toString();
  }

  public void closeConnection() {
    System.out.println("Closing socket!");
//    this.mllpSocket.close();
  }

  public Socket openConnection() throws IOException {
	    return new Socket(this.url, this.port);
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    Socket testSocket = openConnection();
    if (testSocket != null && testSocket.isConnected()) {
      return "MLLP Connection Test: SUCCESS";
    }
    return "MLLP Connection Test: FAIL";
  }

  @Override
  protected void setupFields(List<String> fields) {
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {
  }
}
