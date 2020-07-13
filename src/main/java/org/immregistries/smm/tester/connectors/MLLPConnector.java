/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immregistries.smm.tester.connectors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.immregistries.smm.mover.HL7;

/**
 * Each time the "Submit Message" method is called, a message is sent over the MLLP protocol, and then it waits until it receives 
 * a matching ACK back.  
 * @author Josh Hull
 */
public class MLLPConnector extends Connector {


  private static String EXAMPLE_MESSAGE =
      "MSH|^~\\&|||||20200617073733.994-0500||VXU^V04^VXU_V04|MYy-GM-1.1|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1||B79Q4^^^NIST-MPI-1^MR||ComalAIRA^WoodrowAIRA^Kardos^^^^L|CarlisleAIRA^^^^^^M|20200408|M||1002-5^American Indian or Alaska Native^CDCREC|1877 Hook en Middelaar Ln^^Waterford^MI^48327^USA^P||^PRN^PH^^^248^5536331|||||||||2186-5^Not Hispanic or Latino^CDCREC||N|1|||||N\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20200617|||A|20200408|20200617\r"
          + "NK1|1|ComalAIRA^CarlisleAIRA^Elizabeth^^^^L|MTH^Mother^HL70063|1877 Hook en Middelaar Ln^^Waterford^MI^48327^USA^P|^PRN^PH^^^248^5536331\r"
          + "NK1|2|ComalAIRA^LeonardAIRA^William^^^^L|FTH^Father^HL70063|1877 Hook en Middelaar Ln^^Waterford^MI^48327^USA^P|^PRN^CP^^^248^5536331\r"
          + "ORC|RE|AB79Q4.1^NIST-AA-IZ-2|BB79Q4.1^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||654^Thomas^Wilma^Elizabeth^^^^^NIST-PI-1^L^^^MD\r"
          + "RXA|0|1|20200617||49281-0560-05^Pentacel^NDC|0.5|mL^mL^UCUM||00^New Record^NIP001|7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|^^^NIST-Clinic-1||||526434|20200715|PMC^Sanofi Pasteur^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|RT^Right Thigh^HL70163\r"
          + "OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|VXC50^Public^CDCPHINVS||||||F|||20200617\r"
          + "OBX|2|CE|64994-7^Vaccine Funding Program Eligibility^LN|2|V04^VFC Eligible - American Indian/Alaska Native^HL70064||||||F|||20200617|||VXC40^per immunization^CDCPHINVS\r"
          + "OBX|3|CE|69764-9^Document Type^LN|3|253088698300017211160720^Polio VIS^cdcgs1vis||||||F|||20200617\r"
          + "OBX|4|DT|29769-7^Date Vis Presented^LN|3|20200617||||||F|||20200617\r"
          + "OBX|5|CE|69764-9^Document Type^LN|4|253088698300006611150402^Haemophilus Influenzae type b VIS^cdcgs1vis||||||F|||20200617\r"
          + "OBX|6|DT|29769-7^Date Vis Presented^LN|4|20200617||||||F|||20200617\r"
          + "OBX|7|CE|69764-9^Document Type^LN|5|253088698300003511070517^Diphtheria/Tetanus/Pertussis (DTaP) VIS^cdcgs1vis||||||F|||20200617\r"
          + "OBX|8|DT|29769-7^Date Vis Presented^LN|5|20200617||||||F|||20200617\r"
          + "ORC|RE|AB79Q4.2^NIST-AA-IZ-2|BB79Q4.2^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||654^Thomas^Wilma^Elizabeth^^^^^NIST-PI-1^L^^^MD|||||NISTEHRFAC^NISTEHRFacility^HL70362\r"
          + "RXA|0|1|20200617||00006-4047-20^RotaTeq^NDC|2.0|mL^mL^UCUM||00^New Record^NIP001|7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|^^^NIST-Clinic-1||||297961|20200909|MSD^Merck and Co., Inc.^MVX|||CP|A\r"
          + "RXR|C38288^Oral^NCIT\r"
          + "OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|VXC50^Public^CDCPHINVS||||||F|||20200617\r"
          + "OBX|2|CE|64994-7^Vaccine Funding Program Eligibility^LN|2|V04^VFC Eligible - American Indian/Alaska Native^HL70064||||||F|||20200617|||VXC40^per immunization^CDCPHINVS\r"
          + "OBX|3|CE|69764-9^Document Type^LN|3|253088698300019611150415^Rotavirus VIS^cdcgs1vis||||||F|||20200617\r"
          + "OBX|4|DT|29769-7^Date Vis Presented^LN|3|20200617||||||F|||20200617\r"
          + "ORC|RE|AB79Q4.3^NIST-AA-IZ-2|BB79Q4.3^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN||654^Thomas^Wilma^Elizabeth^^^^^NIST-PI-1^L^^^MD\r"
          + "RXA|0|1|20200617||00005-1971-01^Prevnar 13^NDC|0.5|mL^mL^UCUM||00^New Record^NIP001|7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN|^^^NIST-Clinic-1||||353480|20200722|PFR^Pfizer, Inc^MVX|||CP|A\r"
          + "RXR|C28161^Intramuscular^NCIT|LT^Left Thigh^HL70163\r"
          + "OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|VXC50^Public^CDCPHINVS||||||F|||20200617\r"
          + "OBX|2|CE|64994-7^Vaccine Funding Program Eligibility^LN|2|V04^VFC Eligible - American Indian/Alaska Native^HL70064||||||F|||20200617|||VXC40^per immunization^CDCPHINVS\r"
          + "OBX|3|CE|69764-9^Document Type^LN|3|253088698300015811151105^Pneumococcal Conjugate (PCV13) VIS^cdcgs1vis||||||F|||20200617\r"
          + "OBX|4|DT|29769-7^Date Vis Presented^LN|3|20200617||||||F|||20200617\r"
          + "ORC|RE||BB79Q4.4^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN\r"
          + "RXA|0|1|20200409||45^Hep B, unspecified formulation^CVX|999|||01^Historical Administration^NIP001|||||||||||CP|A\r"
          + "ORC|RE||BB79Q4.5^NIST-AA-IZ-2|||||||7824^Jackson^Lily^Suzanne^^^^^NIST-PI-1^L^^^PRN\r"
          + "RXA|0|1|20200508||45^Hep B, unspecified formulation^CVX|999|||01^Historical Administration^NIP001|||||||||||CP|A";

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println(
          "Usage: java org.immregistries.smm.tester.connectors.MLLPConnector [http://URL:Port]");
    }
    MLLPConnector conn = new MLLPConnector("MLLP", args[0]);
    String ack = conn.submitMessage(EXAMPLE_MESSAGE, false);
    System.out.println(ack);
  }

  /**
   * This socket is kept open for this instance, until the "shutdown" method
   * is called.
   */
  private Socket mllpSocket;

  private int port;
  private InetAddress host;

  /**
   * This Map is a sort of temporary holder since we don't actually know what
   * order the ACKs will come back in. IT's possible they will come back out
   * of order, and this map should help to create some amount of continuity.
   */
  private Map<String, String> ackMap = new HashMap<String, String>();

  protected MLLPConnector(String label, String url, String type) {
    super(label, type);
    this.url = url;
  }

  public MLLPConnector(String label, String urlPlusPort) throws IOException {
    super(label, "MLLP");
    URL aURL = new URL(urlPlusPort);
    this.port = aURL.getPort();
    this.host = InetAddress.getByName(aURL.getHost());
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    if (message == null || message.length() == 0) {
      return "Empty message.  Skipping.";
    }

    System.out.println("SUBMITTING message.  Length of message: " + message.length());
    // check to make sure the connection is open
    // if not, open it again.

    if (this.mllpSocket == null || !this.mllpSocket.isConnected() || !this.mllpSocket.isBound()) {
      this.mllpSocket = this.openConnection();
    }

    InputStream socketInputStream = this.mllpSocket.getInputStream();
    OutputStream socketOutputStream = this.mllpSocket.getOutputStream();
    // send the request over MLLP.
    // get message-control-id out of the ack message, and put it in the map.
    String controlId = HL7.readField(message, 10);
    System.out.println("message in has control id: " + controlId);
    boolean sent = this.sendAnMLLPMessage(message, socketOutputStream);

    // wait for a response.  
    byte[] byteBuffer = new byte[5000];
    // if you wait too long, figure out how to handle that problem, and
    // recover.
    // Step 1: set a timeout...
    System.out.println("Message sent.  now waiting for a response. ");

    int tries = 0;// get one hundred input streams

    while (this.ackMap.get(controlId) == null && tries++ <= 100) {
      System.out.println("reading socket input stream");
      //			int bytesReady = socketInputStream.available();// the number of
      //															// bytes available
      //															// without blocking.

      //This will block the thread until something is received back from the server. 
      socketInputStream.read(byteBuffer);
      String responseString = "";
      for (byte b : byteBuffer) {
        if (b == 13) {
          responseString += "\n\r";
        } else if (b >= 32) {
          responseString += (char) b;
        }
      }

      System.out.println("Received a response: ");
      System.out.println(responseString);
      // Will it always be the whole message? Could it be partial??? Not
      // sure.  Could be sure by reading for the MLLP start/stop.  This doesn't appear to be necessary at the moment.
      // In the future, this could be added and tested

      String[] lines = responseString.split("[\n\r]{1,2}");
      if (lines != null && lines.length > 0) {
        for (String line : lines) {
          System.out.println("Evaluating line: " + line);
          if (line.startsWith(HL7.MSA)) {
            System.out.println("Line starts with MSA.  Getting controlId");
            String ackControlId = HL7.readField(line, 2);
            System.out.println("Putting ACK into map with control ID: " + ackControlId);
            this.ackMap.put(ackControlId, responseString);
            break;
          }
        }
      }
    }


    String ack = this.ackMap.remove(controlId);
    if (ack == null) {
      System.out.println("No ACK returned");
    } else {
      System.out.println("ACK returned");
    }
    System.out.println("MLLP Ack Map size - " + this.ackMap.size());
    // Get message-control-id from sent message, and pull it from the map.
    return ack;

    // you're looking for a MCIR ACK.
    // if you get an HIE ack before then,
    // wait a bit longer to see if the MCIR one ends up coming eventually.
  }

  public boolean sendAnMLLPMessage(String message, OutputStream out) throws IOException {
    String mllpMsgOutgoing = this.buildMllpPacket(message);
    // Send the MLLP wrapped message to the server!
    out.write(mllpMsgOutgoing.getBytes());
    return true;
  }

  // static final char END_OF_BLOCK = '\u001c';
  // static final char START_OF_BLOCK = '\u000b';
  static final char RHAPSODY_MLLP_START = 0x0B;
  static final char RHAPSODY_MLLP_TAIL1 = 0x1C;
  static final char RHAPSODY_MLLP_TAIL2 = 0x0D;

  protected String buildMllpPacket(String message) {
    StringBuilder msg = new StringBuilder();
    msg.append(RHAPSODY_MLLP_START).append(message).append(RHAPSODY_MLLP_TAIL1)
        .append(RHAPSODY_MLLP_TAIL2);
    return msg.toString();
  }

  @Override
  public void shutdown() {
    //		super.shutdown();
    if (this.mllpSocket != null) {
      System.out.println("closing MLLP socket for - " + this.label);
      this.closeConnection(this.mllpSocket);
      //		} else {
      //			System.out.println("closing MLLP socket for - " + this.label + ": already closed");
    }
  }

  public void closeConnection(Socket inSocket) {
    try {
      inSocket.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public Socket openConnection() throws IOException {
    System.out.println("Opening Connection for " + this.label);
    return new Socket(this.host, this.port);
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    Socket testSocket = openConnection();
    if (testSocket != null && testSocket.isConnected()) {
      this.closeConnection(testSocket);
      return "MLLP Connection Test: SUCCESS";
    }
    return "MLLP Connection Test: FAIL";
  }

  @Override
  public boolean connectivityTestSupported() {
    return true;
  }



  @Override
  protected void setupFields(List<String> fields) {}

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {}
}
