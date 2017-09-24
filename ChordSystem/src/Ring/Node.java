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
	public String str_REG_REQUEST = "REG_REQUEST";
	public boolean isRegisterd = false;
	public ServerSocket serversocket;
	public boolean isNodeAlive = false;
	public int fingertableSize = 3;
	public double nodeID;
	public Hashtable<Integer, Integer> hashtable = new Hashtable<Integer, Integer>();

	public Node() {

	}

	public void intiateNode() {
		System.out.println("Peer Node has been started");
	}

	public void findSuccessor() {

	}

	public void findTheSuccessor(double succssorNODE)
	{
		
		Socket socket = new Socket(this.discoveryIP, this.discoveryPORT);
		String str_successor="successor";
		byte[] dataToSend=str_successor.getBytes();
		
		int id = new successorNodeRequest().sendSuccesorRequest(socket, dataToSend, succssorNODE);
		
	}
	private void resolveIDtoIP() {
		
		// TODO Auto-generated method stub
		//if it resolves to fail then it;s not available as your successor
		
	}

	public void buildfingerTable() {
		
		//Use it in thread
		
		for (int i = 1; i <= this.fingertableSize; i++) {
			// hashtable.put(i, arg1)
			double succssorNODE =  this.nodeID + Math.pow(2,(i-1));
			this.findTheSuccessor(succssorNODE);
			
		}

	}

	@Override
	public void run() {

		while (true) {

			try {

				int predessorid = new predessorRequest().processRequestOnserver(this.serversocket);
				System.out.println("Serve sent the Predessor id for this node " + predessorid);
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

						node.updateFingerTable();
					}

					dinn.close();
				}

				if (node.isRegisterd) {

					System.out.println("Node is registerd with the Server");
					System.out.println("Node listenning on the port: " + node.serversocket.getLocalPort());
				}

			} else if ("pull-traffic-summary".equalsIgnoreCase(exitStr)) {
				// collatorNode.trafficSummary();
			}
		}

		System.out.println("Bye.");

	}

	public void updateFingerTable() {

		// logic node id + 2 *(i)-1

	}

}
