package Ring;

import java.io.BufferedReader;
import java.io.DataInputStream;
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
	public ArrayList<Integer> fingerTable = new ArrayList<Integer>();
	public static final String EXIT_COMMAND = "exit";
	public String discoveryIP;
	public int discoveryPORT;
	public Socket socket;
	public String str_REG_REQUEST =   "REG_REQUEST";
	public String str_SUCC_REQUEST =  "SUCC_REQUEST";
	public String str_SUCC_RESPONSE = "SUCC_RESPONSE";
	public static String str_RANDOM_REQUEST = "RANDOM_NODE_REQUEST";
	public boolean isRegisterd = false;
	public ServerSocket serversocket;
	public boolean isNodeAlive = false;
	public int fingertableSize = 3;
	public int nodeID;
	public Hashtable<Double, Integer> hashtable = new Hashtable<Double, Integer>();
	public successorNodeWorker successorWorker;
	public int randomNode=0;

	public Node() {

	}

	public void intiateNode() {
		System.out.println("Peer Node has been started");
	}

	

	public void findTheSuccessor(int succssorNODE) throws IOException
	{
		
		/*
		Socket socket = new Socket(this.discoveryIP, this.discoveryPORT);
		
		String str_successor=str_SUCC_REQUEST;
		
		byte[] dataToSend=str_successor.getBytes();
		
		new successorNodeRequest().sendSuccesorRequest(socket, dataToSend);
		*/
		
	}
	private void resolveIDtoIP() {
		
		// TODO Auto-generated method stub
		//if it resolves to fail then it;s not available as your successor
		
	}

	public void buildfingerTable() throws IOException {
		
		//Use it in thread
		
		for (int i = 1; i <= this.fingertableSize; i++) {
			// hashtable.put(i, arg1)
			
			double succssorNODE =  this.nodeID + Math.pow(2,(i-1));
			
			
			//this.findTheSuccessor(succssorNODE);
		
			//new successorNodeRequest().sendrandomNodeIDfromDiscovery(socket, dataToSend, successorID);
			
		}

	}

	@Override
	public void run() {

		while (true) {

			try {
				
				//This is blocking call
				
				int randomNode = new randomNodeserverSocket().serverWaitAndAccept(this.serversocket);
				System.out.println("Serve sent the Random Node id for this node " + randomNode);
				
				this.randomNode = randomNode;
		
			}

			catch (IOException e) {
				System.out.println(e.getMessage());
			}

		}

	}

	public void sendMessage() throws UnknownHostException, IOException {
		// System.out.println(this.discoveryIP + ":" + this.discoveryPORT);
		// Socket socket = new Socket(this.discoveryIP, this.discoveryPORT);
		// new TCPSender().sendMessage(socket);
		// new RegistrationRequest().sendRegRequest(socket, dataToSend, nodeID);
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
				
				//This thread for the SUCCESSOR
				
				//successorNodeWorker  successorWORKER= new successorNodeWorker(node);
			
				///node.successorWorker=successorWORKER;
				
				//Thread t2 = new Thread(successorWORKER);
				//t2.run();
				
				
				
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

					new RegistrationRequest().sendRegRequest(socket, regrequest, Integer.parseInt(nodeID),node.serversocket.getLocalPort());

					DataInputStream dinn = new DataInputStream(socket.getInputStream());

					int resp = dinn.readInt();

					System.out.println(resp);

					if (resp == 111) {
						node.isRegisterd = true;
						// node.socket.close();
						
						node.nodeID = Integer.parseInt(nodeID);
								

						node.updateFingerTable();
					}

					dinn.close();
				}

				if (node.isRegisterd) {

					System.out.println("Node is registerd with the Server znd ID is" + node.nodeID );
					
					System.out.println("Node listenning on the port: " + node.serversocket.getLocalPort());

					Socket socket = new Socket(node.discoveryIP, node.discoveryPORT);
					
				
					new RandomNodeRequest().randomRequest(socket, str_RANDOM_REQUEST.getBytes() ,  node.nodeID);
					
					
					node.updateFingerTable();
					
				}

			} else if ("pull-traffic-summary".equalsIgnoreCase(exitStr)) {
				// collatorNode.trafficSummary();
			}
		}

		System.out.println("Bye.");

	}

	public void updateFingerTable() throws IOException {

		// logic node id + 2 *(i)-1
	
		//this.findTheSuccessor(this.nodeID);
		if(this.nodeID==this.randomNode)
		{
			
			for (int i = 1; i <= this.fingertableSize; i++) {
				// hashtable.put(i, arg1)
				
				//double succssorNODE =  this.nodeID + Math.pow(2,(i-1));
				this.hashtable.put(Math.pow(2,(i-1)), this.nodeID);
			}
		
			
		}
		else
		{
			
		}
		
		
	}

}
