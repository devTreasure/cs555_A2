package Ring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import Management.*;


public class Node  implements Runnable{
	
	public String nodeIP;
	public int nodePORT;
	public Node predecessorNode;
	public ArrayList<Integer> fingerTable= new ArrayList<Integer>();
	public static final String EXIT_COMMAND = "exit";
	public String discoveryIP;
	public int discoveryPORT;
		
	public Node()
	{
		
	}
	
	public void intiateNode()
	{
		System.out.println("Peer Node has been started");
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			
		}
		
	}

	public void sendMessage() throws UnknownHostException, IOException
	{
		System.out.println(this.discoveryIP +":"+this.discoveryPORT);
		Socket socket = new Socket(this.discoveryIP, this.discoveryPORT) ;
		new TCPSender(socket);
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		System.out.println("Enter \"start\" to start the Node ");


		System.out.println("Please pass the IP --SPACE-- UNIQUE PORT number for the Discovery Node");
		
		boolean continueOperations = true;
		Node node =null;
		while (continueOperations) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		

			String exitStr = br.readLine();
			System.out.println("Received command is:" + exitStr);

			String [] strIPandPort;
			strIPandPort = exitStr.split(" ");
			
			System.out.println(strIPandPort.length);
			
			if(strIPandPort !=null && strIPandPort.length<2)
			{
			  System.out.println("Error: Please pass the IP --SPACE-- UNIQUE PORT number for the Discovery");
			}
			if(strIPandPort.length == 2)
			{
				System.out.println("IP port intialized and sending message");
				
			    node= new Node();
				node.discoveryIP=strIPandPort[0];
				node.discoveryPORT= Integer.parseInt(strIPandPort[1]);
				node.sendMessage();
			}
	
			if (EXIT_COMMAND.equalsIgnoreCase(exitStr)) {
				System.out.println("Exiting.");
				continueOperations = false;
			} else if ("start".equalsIgnoreCase(exitStr)) {
				System.out.println("Enter Numeric number between 0 to 7 ");
				/*
				String nodeNumber = br.readLine();
				if (nodeNumber.length() > 0) {
					node.intiateNode();
				}
				System.out.println("Enterd number is: " + nodeNumber);
				*/
				

			} else if ("pull-traffic-summary".equalsIgnoreCase(exitStr)) {
				// collatorNode.trafficSummary();
			}
		}

		System.out.println("Bye.");

	}


	
}
