package Management;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class successorNodeResponse {

	ServerSocket serverSocket = null;

	public int  processRequestOnserver(ServerSocket serversocket) throws IOException

	{
		Socket socket = null;
		
		int nodeID = 0;
		
		System.out.println("Waiting for the successorNode response to be sent by the Discovery node ");
		
		DataInputStream din = null;
		
		DataOutputStream dout = null;

		socket = serversocket.accept();

		System.out.println("..: Successor Node Socket acceepted :...");

		try {

			din = new DataInputStream(socket.getInputStream());

			//dout = new DataOutputStream(socket.getOutputStream());

			nodeID = din.readInt();
			
			System.out.println("Peer has got he successor as " + nodeID);

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