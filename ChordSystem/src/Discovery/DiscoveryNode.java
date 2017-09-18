package Discovery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DiscoveryNode implements Runnable {

	public String discoveryNodeIP;
	public int discoveryNodePORT;
	public static final String EXIT_COMMAND = "exit";
	public String discoveryNodeName;
	public ServerSocket serverSocket;
	public ArrayList<Discovery.RingNodes> ringNodes = null;
	public String str_REG_REQUEST = "REG_REQUEST";

	public DiscoveryNode() {
		ringNodes = new ArrayList<RingNodes>();
	}

	public boolean intiateRingNodeRegistration(int nodeNum) {

		Boolean regSucess = false;

		RingNodes ring = new RingNodes();

		ring.ringNodeID = nodeNum;

		if (!this.ringNodes.contains(ring))

		{
			this.ringNodes.add(ring);
			
			System.out.println("Total registered nodes " + this.ringNodes.size());
			
			regSucess = true;
		} else {

			regSucess = false;
		}

		return regSucess;
	}

	public static void main(String[] args) throws IOException {

		System.out.println("Enter register tot register new node");

		String[] strSplit = null;

		// collatorNode.collatorIP = strIP;
		// collatorNode.collatorPORT = nodePort;

		// collatorNode.initializeCollatorNode(collatorNode.collatorPORT);
		// Thread thread = new Thread(collatorNode);
		// thread.start();

		DiscoveryNode discoveryNode = new DiscoveryNode();

		discoveryNode.intializeDiscoverNode();

		Thread t = new Thread(discoveryNode);
		t.start();

		boolean continueOperations = true;

		while (continueOperations) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String exitStr = br.readLine();
			System.out.println("Received command is:" + exitStr);

			if (EXIT_COMMAND.equalsIgnoreCase(exitStr)) {
				System.out.println("Exiting.");
				continueOperations = false;
			} else if ("register".equalsIgnoreCase(exitStr)) {
				System.out.println("Enter Numeric number between 0 to 7 ");
				String nodeNumber = br.readLine();
				if (nodeNumber.length() > 0) {

				}
				System.out.println("Enterd number is: " + nodeNumber);
				// collatorNode.sendMessages();

			} else if ("pull-traffic-summary".equalsIgnoreCase(exitStr)) {
				// collatorNode.trafficSummary();
			}
		}

		System.out.println("Bye.");

	}

	private void intializeDiscoverNode() throws IOException {
		// TODO Auto-generated method stub
		ServerSocket sc = new ServerSocket(0);
		// InetAddress address =
		// InetAddress.getByName(sc.getInetAddress().getHostName());
		System.out.println("Resolved Host name is :");
		System.out.println(InetAddress.getLocalHost().getHostName());

		// System.out.println(address.getHostName());
		this.discoveryNodeIP = InetAddress.getLocalHost().getHostAddress();

		this.discoveryNodePORT = sc.getLocalPort();

		this.serverSocket = sc;

		System.out.println("Discovery node is hoasted at : " + this.discoveryNodeIP + "  " + " Listenning port : "
				+ sc.getLocalPort());

	}

	@Override
	public void run() {

		try {
			receiveMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receiveMessage() throws IOException {

		System.out.println("Started Discovery node receiver thread;");
		DataInputStream din = null;
		DataOutputStream dout=null;
		
		Socket socket;
		while (true) {

			socket = serverSocket.accept();
			System.out.println("..: RegistrationRequest Socket acceepted :...");
			
			try {
			
				din = new DataInputStream(socket.getInputStream());
				dout = new DataOutputStream(socket.getOutputStream());
				
				// int number = din.readInt();
				int requestIdentifierLength = din.readInt();
				byte[] identifierBytes = new byte[requestIdentifierLength];
				din.readFully(identifierBytes);
				
				String strID = new String(identifierBytes);
				int nodeID = 0;
				
				if (strID.equalsIgnoreCase(str_REG_REQUEST)) {
					
					nodeID = din.readInt();
					System.out.println(strID);
					System.out.println(String.format("server has received the  number : %1$d ", nodeID));

					boolean regSuccess = this.intiateRingNodeRegistration(nodeID);
					
					System.out.println(strID);
					int strResposnse = 0;

					if (regSuccess) {
						strResposnse = 111;
					} else {
						strResposnse = 222;
					}

					dout.writeInt(strResposnse);			
					dout.flush();

				
				}

				System.out.println("done");

			}

			catch (Exception e) {
				e.printStackTrace();

			}
		}

	}

}
