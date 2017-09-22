package Discovery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class DiscoveryNode implements Runnable {

	public String discoveryNodeIP;
	public int discoveryNodePORT;
	public static final String EXIT_COMMAND = "exit";
	public String discoveryNodeName;
	public ServerSocket serverSocket;
	public Map<Integer,Discovery.RingNodes> ringNodes = null;
	public MiddleWare objMiddleware;


	public String str_REG_REQUEST = "REG_REQUEST";

	public DiscoveryNode() {
		
		ringNodes = new HashMap<Integer,Discovery.RingNodes>();
		
		this.objMiddleware=new MiddleWare(this);
		
	}
	
	


	public Map<Integer, Discovery.RingNodes> getRingNodes() {
		return ringNodes;
	}




	public void setRingNodes(Map<Integer, Discovery.RingNodes> ringNodes) {
		this.ringNodes = ringNodes;
	}




	public boolean intiateRingNodeRegistration(int nodeNum,String nodeIP,int nodeserverSocketPORT,String strhostName) {

		Boolean regSucess = false;

		RingNodes ring = new RingNodes();

		ring.ringNodeID = nodeNum;
		
		ring.NodehostName = strhostName;
		
		ring.ringNodeIP=nodeIP;
		
		ring.ringnodeServerSocketPORT=nodeserverSocketPORT;
		
		
		System.out.println(ring.toString());

		if (!this.ringNodes.containsKey(nodeNum))

		{
			this.ringNodes.put(nodeNum, ring);

			System.out.println("Total registered nodes " + this.ringNodes.size());

			regSucess = true;

			//sendthePeerNodeHisPredessor();

		} else {

			regSucess = false;
		}

		return regSucess;
	}

	private void sendthePeerNodeHisPredessor(int nodeID) {

		int intPredessorNode = -1;
		int intSuccessorNode = -1;

		this.objMiddleware.getThepredessor(nodeID);

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
			
			} else if ("pull-traffic-summary".equalsIgnoreCase(exitStr)) {
				// collatorNode.trafficSummary();
			}
		}

		System.out.println("Bye.");

	}

	private void intializeDiscoverNode() throws IOException {
		// TODO Auto-generated method stub
		ServerSocket sc = new ServerSocket(0);

		System.out.println("Resolved Host name is :");
	
		System.out.println(InetAddress.getLocalHost().getHostName());

		this.discoveryNodeIP = InetAddress.getLocalHost().getHostAddress();

		this.discoveryNodePORT = sc.getLocalPort();

		this.serverSocket = sc;

		System.out.println(" Discovery node is hoasted at : " + this.discoveryNodeIP + "  " + " Listenning port : "
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
		DataOutputStream dout = null;

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
				int nodeListenningPort = 0;
				
				if (strID.equalsIgnoreCase(str_REG_REQUEST)) {

					nodeID = din.readInt();
					
					nodeListenningPort=din.readInt();
					
					System.out.println(strID);
					
					System.out.println(String.format("server has received the  number : %1$d ", nodeID));

					InetAddress address = socket.getInetAddress();
					
					String strHOSTNAME= address.getLocalHost().getHostName();
					
					System.out.println("Host of the ring :" + strHOSTNAME);
					
					
					String strIP = address.getLocalHost().getHostAddress();
					
					boolean regSuccess = this.intiateRingNodeRegistration(nodeID,strIP,nodeListenningPort,strHOSTNAME);
					
					if(regSuccess)
					{
						
						
					  RingNodes node = this.objMiddleware.getThepredessor(nodeID);
					  
					  this.objMiddleware.sendThenewlyRegisterdNodePoredessorInfo(node);
					  
					  
					}

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
