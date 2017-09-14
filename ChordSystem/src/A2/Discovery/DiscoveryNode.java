package A2.Discovery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.omg.CORBA.PUBLIC_MEMBER;

public class DiscoveryNode {

	
	   public String discoveryNodeIP;
	   public int discoveryNodePORT;
	   public static final String EXIT_COMMAND = "exit";
	   public String discoveryNodeName;
	   public Socket socket;
	   
	   
	   
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		 if (args.length < 1) {
	         System.out.println(
	               "Error: Please pass the hostName/Machine name");
	         System.exit(0);
	      }

	      String strHostName = "";
	      int nodePort = 0;
	      
	      try {
	    	  strHostName = (args[0]);
	

	      } catch (Exception e) {
	         System.out.println("Error: Please provide hostName for the DiscoveryNode.");
	         System.exit(0);
	      }

	      String[] strSplit = null;
	      // TODO Auto-generated method stub
	      // strSplit = strIP.split(",");
	      /*
	       * System.out.println(
	       * "************************************\n 1.) Please select the ip SPACE port and enter in one line.\n 2.)"
	       * + " Enter the START to start the node \n 3.) " + "Complete all the config IP entries" +
	       * "\n 4.) veriy all the nodes stood up. \n 5.) Enter  \"RANDOM\" to select the communication node \n 6.) Enter \"START\" to start the round"
	       * +
	       * "\n 7.) Enter \"EXIT\" to exit  and stop the node \n****************************************"
	       * );
	       */

	      System.out.println("Enter \"Start/start\" to intiate message sending ");


	   

	      InetAddress address = InetAddress.getByName(strHostName); 
	      System.out.println(address.getHostAddress()); 
	      System.out.println("Resolved Host name is :"); 
	      System.out.println(address.getHostName()); 

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
	         } else if ("start".equalsIgnoreCase(exitStr)) {
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
		
	    this.discoveryNodeIP = InetAddress.getLocalHost().getHostAddress();
	    

		this.discoveryNodePORT=sc.getLocalPort();
		
		System.out.println(this.discoveryNodeIP);
		System.out.println("Listenning port : " + sc.getLocalPort());
		
	}

}
