package Discovery;

import java.awt.List;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

	public DiscoveryNode() {
		ringNodes = new ArrayList<Discovery.RingNodes>();
	}

	public void intiateRingNodeRegistration(String nodeNum) {

		RingNodes ring = new RingNodes();

		ring.rinNodename = nodeNum;

		if (!this.ringNodes.contains(ring))

		{
			this.ringNodes.add(ring);
		} else {
			System.out.println("Please use/register with diffrent number");
		}

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
					discoveryNode.intiateRingNodeRegistration(nodeNumber);
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
      
		this.serverSocket=sc;
		// System.out.println(this.discoveryNodeIP);
		System.out.println("Discovery node is hoasted at : " + this.discoveryNodeIP + "  " + " Listenning port : "
				+ sc.getLocalPort());

	}

	@Override
	public void run() {
		
			receiveMessage();
	}

	public void receiveMessage() {
		
		System.out.println("Started Discovery node receiver thread;");
		Socket Socket =null;
		DataInputStream din =null;
		
		while (true) {
			
					
			try {
				
				Socket = this.serverSocket.accept();
				
				System.out.println("..: Socket acceepted :...");
				
				din = new DataInputStream(Socket.getInputStream());
				
				if(din.available() > 0)
				{
					
				 int number = din.readInt();
				 
				 System.out.println(String.format("server has received the  number : %1$d ", number));
				 
				 System.out.println("done");
				
				}
				
			} 
			
			catch (Exception e) 
			{
				e.printStackTrace();
				
			}
			
			finally 
			{
				try {
					if (din != null)
						din.close();
					if (Socket != null)
						Socket.close();
				} catch (IOException e) {
					System.out.println("Error while closing resources: " + e.getMessage());
				}
			}
		}

	}

}
