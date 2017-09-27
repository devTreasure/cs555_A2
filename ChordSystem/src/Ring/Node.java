package Ring;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;

import Management.*;

public class Node implements Runnable {

	public String nodeIP;
	public int nodePORT;
	public Node predecessorNode;
	public int predecessorNodeID;
	public ArrayList<Integer> fingerTable = new ArrayList<Integer>();
	public static final String EXIT_COMMAND = "exit";
	public String discoveryIP;
	public int discoveryPORT;
	public Socket socket;
	public String str_REG_REQUEST = "REG_REQUEST";
	public String str_SUCC_REQUEST = "SUCC_REQUEST";
	public String str_SUCC_RESPONSE = "SUCC_RESPONSE";
	public static String str_RANDOM_REQUEST = "RANDOM_NODE_REQUEST";
	public static String str_PEER_PREDECESSOR_REQUEST = "PEER_NODE_PREDECESSOR_REQUEST";
	public boolean isRegisterd = false;
	public ServerSocket serversocket;
	public boolean isNodeAlive = false;
	public int fingertableSize = 3;
	public int nodeID;
	public Hashtable<Double, Integer> hashtable = new Hashtable<Double, Integer>();
	public successorNodeWorker successorWorker;
	public int randomNodeID = -1;
	public String randomNodeIP = "";
	public int randomNodePORT = -1;

	public Node() {

	}

	public void intiateNode() {
		System.out.println("Peer Node has been started");
	}

	public void findTheSuccessor(int succssorNODE) throws IOException {

		/*
		 * Socket socket = new Socket(this.discoveryIP, this.discoveryPORT);
		 * 
		 * String str_successor=str_SUCC_REQUEST;
		 * 
		 * byte[] dataToSend=str_successor.getBytes();
		 * 
		 * new successorNodeRequest().sendSuccesorRequest(socket, dataToSend);
		 */

	}

	private void resolveIDtoIP() {

		// TODO Auto-generated method stub
		// if it resolves to fail then it;s not available as your successor

	}

	public void buildfingerTable() throws IOException {

		// Use it in thread

		for (int i = 1; i <= this.fingertableSize; i++) {
			// hashtable.put(i, arg1)

			double succssorNODE = this.nodeID + Math.pow(2, (i - 1));

			// this.findTheSuccessor(succssorNODE);

			// new successorNodeRequest().sendrandomNodeIDfromDiscovery(socket, dataToSend,
			// successorID);

		}

	}

	@Override
	public void run() {

		DataInputStream din = null;
		DataOutputStream dout = null;

		Socket socket;

		while (true) {

			try {

				// This is blocking call

				int randomNode = -1;

				// randomNode=new peerNodeServerSocket().serverWaitAndAccept(this.serversocket);

				socket = serversocket.accept();

				System.out.println("..: peer Node server Socket acceepted :...");

				din = new DataInputStream(socket.getInputStream());

				dout = new DataOutputStream(socket.getOutputStream());

				// int number = din.readInt();
				int requestIdentifierLength = din.readInt();

				byte[] identifierBytes = new byte[requestIdentifierLength];

				din.readFully(identifierBytes);

				String strID = new String(identifierBytes);

				int nodeID = 0;

				int nodeListenningPort = 0;

				if (strID.equalsIgnoreCase("IP_PORT_RESPONSE")) {

					// int number = din.readInt();
					int requestIdentifierLength1 = din.readInt();

					byte[] identifierBytes1 = new byte[requestIdentifierLength1];

					din.readFully(identifierBytes1);

					String strID1 = new String(identifierBytes);

					int port = din.readInt();

					sendTheRequestTOresolvedNode(strID1, port);
				}

				/*
				 * if(strID.equalsIgnoreCase("SUCCESSOR_REQUEST")) { int predecessor =
				 * this.predecessorNode.nodeID;
				 * 
				 * int successor = -1;
				 * 
				 * if(this.hashtable.size() >0) successor =(int)
				 * this.hashtable.values().toArray()[0];
				 * 
				 * //new
				 * peerPredecessorResponse().response(socket,"SUCCESSOR_RESPONSE".getBytes(),
				 * predecessor,successor);
				 * 
				 * }
				 */

				if (strID.equalsIgnoreCase("PEER_NODE_PREDECESSOR_REQUEST")) {
					try {
						System.out.println("Request recevied for PEER_NODE_PREDECESSOR_REQUEST");
						int nodePredessor = -1;
						nodePredessor = this.predecessorNodeID;

						String IP = socket.getInetAddress().getLocalHost().getHostAddress();
						System.out.println(IP);
						int PORT = -1;
						PORT = socket.getLocalPort();
						Socket peerPredResponse = new Socket(IP, PORT);
						System.out.println(PORT);
						System.out.println("***********************");
						new peerPredecessorResponse().response(peerPredResponse,
								"PEER_NODE_PREDECESSOR_RESPONSE".getBytes(), nodePredessor);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}

				}

				if (strID.equalsIgnoreCase("PEER_NODE_PREDECESSOR_RESPONSE")) 
				{
					{
						try {
							System.out.println("Request recevied for PEER_NODE_PREDECESSOR_RESPONSE");

							int returnedPeerNODE = -1;
							returnedPeerNODE = din.readInt();

							System.out.println("Returned Peer from the other  perr is " + returnedPeerNODE);
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
						}
					}
					

					

				}
				
				if (strID.equalsIgnoreCase("RANDOM_NODE_RESPONSE")) 
				{
					System.out.println("....:RANDOM_NODE_RESPONSE on Peer Side:........");

					this.randomNodeID = din.readInt();

					System.out.println(this.randomNodeID);

					int strIPlength = din.readInt();

					byte[] strIPBytes = new byte[strIPlength];

					din.readFully(strIPBytes);

					String randomnodeIP = new String(strIPBytes);

					int random_NodePORT = 0;

					random_NodePORT = din.readInt();

					System.out.println("Radmon node IP");

					System.out.println(randomnodeIP);

					System.out.println("Radmon node PORT");

					System.out.println(random_NodePORT);

					System.out.println("....:::...........................:::....");

					this.randomNodeIP = randomnodeIP;
					this.randomNodePORT = random_NodePORT;

					if (this.nodeID == this.randomNodeID) {
						// No need to communicate with other node
						// You are the only Node
						// go ahead and update your finger table

						// 1. Predecessor
						// You are your predecessor

						this.predecessorNodeID = this.randomNodeID;

						// 2. This is you successor

						this.updateFingerTable();
					} else {
						// contact your peer
						// ask its predecessor
						if (this.randomNodeIP.length() > 0 && this.randomNodePORT > 0) {
							this.getthePredecessorofRandomNODE(this.randomNodeIP, this.randomNodePORT);
						} else {
							System.out.println("Discovery was unable to return the random node adn IP");
						}

					}

					if (this.nodeID !=this.randomNodeID) {
						
						//this.randomNodeID = randomNode;

						System.out.println("Serve sent the Random Node id for this node " + this.randomNodeID);

						if (this.nodeID == this.randomNodeID) {
							// This means we are the only one NODE
							updateFingerTable();
						} else {

							findPredecessorANDSuccessorOfRandomNode(this.randomNodeID);
						}
					}

				}
			}

			catch (IOException e) {
				System.out.println(e.getMessage());
			}

		}
	}

	public void getthePredecessorofRandomNODE(String nodeIP, int port) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("Inside getthePredecessorofRandomNODE");
		System.out.println(nodeIP);
		System.out.println(port);
		Socket newSocket = new Socket(nodeIP, port);
		new peerNodePredessorRequest().request(newSocket, "PEER_NODE_PREDECESSOR_REQUEST".getBytes());
	}

	public void sendTheRequestTOresolvedNode(String ip, int port) throws IOException {

		Socket socket = new Socket(ip, port);
		new peerPredecessorRequest().request(socket, "SUCCESSOR_REQUEST".getBytes());

	}

	private void getThePredessorAndSuccessor() {
		// TODO Auto-generated method stub

	}

	public void findPredecessorANDSuccessorOfRandomNode(int randomNodeID) throws IOException {

		// 1 get the IP port of random NODE
		// Initiate request using this IP PORT
		// Compare the nodeID's
		Socket socket = new Socket(this.nodeIP, this.nodePORT);

		System.out.println("Sending request to Discovery for the : " + randomNodeID);

		new peerIPandPORTRequest().peer_IP_PORT_Request(socket, "IP_PORT_REQUEST".getBytes(), randomNodeID);

	}

	public void sendMessage() throws UnknownHostException, IOException {
		// System.out.println(this.discoveryIP + ":" + this.discoveryPORT);
		// Socket socket = new Socket(this.discoveryIP, this.discoveryPORT);
		// new TCPSender().sendMessage(socket);
		// new RegistrationRequest().sendRegRequest(socket, dataToSend, nodeID);
	}

	public void printDHT() {

		System.out.println("Hashtable for :" + this.nodeID);
		{
			int i = 1;

			for (Double key : hashtable.keySet()) {
				System.out.println(i + ".   " + hashtable.get(key));
				i += 1;
			}

		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		System.out.println("Enter \"start\" to start the Node ");

		System.out.println("Please pass the IP --SPACE-- UNIQUE PORT number for the Discovery Node");

		boolean continueOperations = true;

		Node node = null;

		while (continueOperations) {

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String exitStr = br.readLine();

			System.out.println("Received command is:" + exitStr);

			String[] strIPandPort;

			strIPandPort = exitStr.split(" ");

			System.out.println(strIPandPort.length);

			if (strIPandPort.length == 2) {

				System.out.println("IP and port value recieved and sending REGISTRATON message");

				node = new Node();

				ServerSocket sereverSock = new ServerSocket(0);

				node.serversocket = sereverSock;

				node.discoveryIP = strIPandPort[0];

				node.discoveryPORT = Integer.parseInt(strIPandPort[1]);

				// TODO: New Thread for the receiving response

				Thread t = new Thread(node);
				t.start();

				// This thread for the SUCCESSOR

				// successorNodeWorker successorWORKER= new successorNodeWorker(node);

				/// node.successorWorker=successorWORKER;

				// Thread t2 = new Thread(successorWORKER);
				// t2.run();

			}

			if (EXIT_COMMAND.equalsIgnoreCase(exitStr)) {
				System.out.println("Exiting.");

				continueOperations = false;

			} else if ("start".equalsIgnoreCase(exitStr)) {

				boolean regSuccess = false;

				while (!node.isRegisterd) {

					System.out.println("Enter Numeric number between 0 to 7 ");

					String nodeID = br.readLine();

					Socket socket = new Socket(node.discoveryIP, node.discoveryPORT);

					// node.socket = socket;

					byte[] regrequest = node.str_REG_REQUEST.getBytes();

					new RegistrationRequest().sendRegRequest(socket, regrequest, Integer.parseInt(nodeID),
							node.serversocket.getLocalPort());

					DataInputStream dinn = new DataInputStream(socket.getInputStream());

					int resp = dinn.readInt();

					System.out.println(resp);

					if (resp == 111) {
						node.isRegisterd = true;
						// node.socket.close();

						node.nodeID = Integer.parseInt(nodeID);
						socket.close();

						dinn.close();
						// node.updateFingerTable();
					}

				}

				if (node.isRegisterd) {

					System.out.println("Node is registerd with the Server znd ID is" + node.nodeID);

					System.out.println("Node listenning on the port: " + node.serversocket.getLocalPort());

					// ask for your position
					// 1
					Socket randomnodeRequest = new Socket(node.discoveryIP, node.discoveryPORT);

					new RandomNodeRequest().randomRequest(randomnodeRequest, "RANDOM_NODE_REQUEST".getBytes(),	node.nodeID);

					// System.out.println("Random node received");
					// System.out.println(node.randomNodeID);
					// Socket socket = new Socket(node.discoveryIP, node.discoveryPORT);

					// new RandomNodeRequest().randomRequest(socket, str_RANDOM_REQUEST.getBytes() ,
					// node.nodeID);

					// node.updateFingerTable();
					node.updateFingerTable();
				}

			} else if ("print-random-node".equalsIgnoreCase(exitStr)) {
				// collatorNode.trafficSummary();
				System.out.println(node.randomNodeID);
			}
		}

		System.out.println("Bye.");

	}

	public void updateFingerTable() throws IOException {

		// Below condition means you are the only one
		if (this.nodeID == this.randomNodeID) {

			this.predecessorNode = this;

			for (int i = 1; i <= this.fingertableSize; i++) {
				// hashtable.put(i, arg1)

				// double succssorNODE = this.nodeID + Math.pow(2,(i-1));
				this.hashtable.put(Math.pow(2, (i - 1)), this.nodeID);
			}

		}

		else {

		}

		printDHT();
	}

}
