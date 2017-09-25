package Management;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class randomNodeserverSocket {

	ServerSocket serverSocket = null;

	public int  serverWaitAndAccept(ServerSocket serversocket) throws IOException

	{
		Socket socket = null;
		
		int nodeID = 0;
		
		System.out.println("Waiting for the Request for the Peer Node ID ");
		
		DataInputStream din = null;
		
		DataOutputStream dout = null;

		socket = serversocket.accept();

		System.out.println("..: randomNode request Socket acceepted :...");

		try {

			din = new DataInputStream(socket.getInputStream());
			
			//we are not sending here anyting
			
			dout = new DataOutputStream(socket.getOutputStream());

			nodeID = din.readInt();

		}

		catch (Exception ex) {
			
			System.out.println(ex.toString());
		}

		finally {
		
			//Please close all the Streams
			//Don't make a leak 

		}
		
		return  nodeID;

	}
}
