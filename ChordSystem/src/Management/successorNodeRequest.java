package Management;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class successorNodeRequest {

	public void sendSuccesorRequest(Socket socket,byte[] dataToSend) throws IOException {


	  DataOutputStream dout = null;
		try {

			System.out.println(socket.isClosed());
			
			dout = new DataOutputStream(socket.getOutputStream());
			
			int dataLength = dataToSend.length;
			
			dout.writeInt(dataLength);
			
			//REQUEST_NAME
			dout.write(dataToSend, 0, dataLength);
			
			//node ID
			//int node =(int)nodeID;
			
			//dout.writeInt(node);		
			
			dout.flush();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (dout != null)
			{
				//dout.close(); 
				
				//dont close socket as you are awaiting response from server
				
			   //socket.close();
			}
			
		}
	}

}
