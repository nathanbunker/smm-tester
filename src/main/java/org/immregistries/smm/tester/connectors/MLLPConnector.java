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
 * @author nathan
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
	  
	  //send the request over MLLP.
	  //wait for a reponse. 
	  
	  //if you wait too long, figure out how to handle that problem, and recover.
	  
	  //you're looking for a MCIR ACK.  
	  //if you get an HIE ack before then, 
	  //wait a bit longer to see if the MCIR one ends up coming eventually. 
	  return sendAnMLLPMessage(message);
  }
	static final char END_OF_BLOCK = '\u001c';
	static final char START_OF_BLOCK = '\u000b';
	private static final char CARRIAGE_RETURN = 13;
  
  public String sendAnMLLPMessage(String message) throws IOException {
		System.out.println("Connected to: localhost:1080");
		Socket mllpSocket = openConnection(); 
		StringBuilder msg = new StringBuilder();
		msg.append(START_OF_BLOCK)
				.append(message).append(END_OF_BLOCK).append(CARRIAGE_RETURN);

		InputStream in = mllpSocket.getInputStream();
		OutputStream out = mllpSocket.getOutputStream();

		// Send the MLLP wrapped message to the server!
		out.write(msg.toString().getBytes());

		byte[] byteBuffer = new byte[200];
		in.read(byteBuffer);
		
		String responseString = new String(byteBuffer);
		System.out.println("Received a response! " + responseString);

		System.out.println("Closing socket!");

		mllpSocket.close();
		return responseString;
  }
  
  public Socket openConnection() throws IOException {
	    return new Socket(this.url, this.port);
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    return "Connectivity test not supported for HTTPS POST connections";
  }

  @Override
  protected void setupFields(List<String> fields) {
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {
  }
}
