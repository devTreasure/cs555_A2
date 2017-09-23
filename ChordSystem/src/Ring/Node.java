package Ring;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

	public Node() {

	}

	public void intiateNode() {
		System.out.println("Peer Node has been started");
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

			if (EXIT_COMMAND.equalsIgnoreCase(exitStr)) 
			{
				System.out.println("Exiting.");
				
				continueOperations = false;
				
			}
			else if ("start".equalsIgnoreCase(exitStr)) 
			{

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
