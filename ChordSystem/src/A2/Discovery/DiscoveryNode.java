package A2.Discovery;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class DiscoveryNode  implements Runnable  {

	
	   public String discoveryNodeIP;
	   public int discoveryNodePORT;
	   public static final String EXIT_COMMAND = "exit";
	   public String discoveryNodeName;
	   public Socket socket;
	   public ArrayList<A2.Discovery.RingNodes> ringNodes=null;
	   
	   
	   public DiscoveryNode()
	   {
		   ringNodes= new  ArrayList<A2.Discovery.RingNodes>();
	   }
	   
	   public void intiateRingNodeRegistration(String nodeNum)
	   {
		   
		   RingNodes ring= new RingNodes();
		   ring.rinNodename = nodeNum;
		   
		   if(! this.ringNodes.contains(ring.rinNodename ))
			   
		   {
			   this.ringNodes.add(ring);
		   }
		   else
		   {
			   System.out.println("Please use/register with diffrent number");
		   }
		
	   }
	   
	   
	public static void main(String[] args) throws IOException {

	      String strHostName = "";
	     
	      String[] strSplit = null;

	      //collatorNode.collatorIP = strIP;
	      //collatorNode.collatorPORT = nodePort;

	      //collatorNode.initializeCollatorNode(collatorNode.collatorPORT);
	     // Thread thread = new Thread(collatorNode);
	     // thread.start();
	      
	      DiscoveryNode discoveryNode = new DiscoveryNode();
	    
	      
	      discoveryNode.intializeDiscoverNode();
	      
	      boolean continueOperations = true;

	      while (continueOperations) {
	         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	         String exitStr = br.readLine();
	         System.out.println("Received command is:" + exitStr);

	         if (EXIT_COMMAND.equalsIgnoreCase(exitStr)) {
	            System.out.println("Exiting.");
	            continueOperations = false;
	         } else if ("register".equalsIgnoreCase(exitStr)) {
	        	 System.out.println("Enter Numeric number between 0 to 7 " );
	        	 String nodeNumber = br.readLine();
	             System.out.println("Enterd number is: " + nodeNumber);
	            // collatorNode.sendMessages();
	             
	         } else if ("pull-traffic-summary".equalsIgnoreCase(exitStr)) {
	           // collatorNode.trafficSummary();
	         }
	      }

	      System.out.println("Bye.");

	}



	private  void intializeDiscoverNode() throws IOException {
		// TODO Auto-generated method stub
		ServerSocket sc = new ServerSocket(0);
			   // InetAddress address = InetAddress.getByName(sc.getInetAddress().getHostName()); 
	    System.out.println("Resolved Host name is :"); 
	    System.out.println(InetAddress.getLocalHost().getHostName()); 

	    //System.out.println(address.getHostName()); 
	    this.discoveryNodeIP = InetAddress.getLocalHost().getHostAddress();

		this.discoveryNodePORT=sc.getLocalPort();
		
		//System.out.println(this.discoveryNodeIP);
		System.out.println("Discovery node is hoasted at : " + this.discoveryNodeIP + "  "+ " Listenning port : " + sc.getLocalPort());
		
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			
		}
	}

}
