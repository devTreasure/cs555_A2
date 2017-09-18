package Management;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class RegistrationRequest {




	public void sendRegRequest(Socket socket,byte[] dataToSend,int nodeID) throws IOException {


	  DataOutputStream dout = null;
		try {

			System.out.println(socket.isClosed());
			
			dout = new DataOutputStream(socket.getOutputStream());
			int dataLength = dataToSend.length;
			dout.writeInt(dataLength);
			dout.write(dataToSend, 0, dataLength);
			
			//node ID
			dout.writeInt(nodeID);			
			dout.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dout != null)
			{
				//dout.close();
				
				//dont close socket as you are awaiting response from server
			   //socket.close();
			}
			
		}
	}

}
