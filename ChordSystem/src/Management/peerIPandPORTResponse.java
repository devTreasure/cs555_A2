package Management;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class peerIPandPORTResponse {

	
	
	public void peer_IP_PORT_Response(Socket socket, byte[] dataToSend, byte[] strIP, int port)
			throws IOException {

		DataOutputStream dout = null;
		try {

			System.out.println(socket.isClosed());

			dout = new DataOutputStream(socket.getOutputStream());

			int dataLength = dataToSend.length;

			dout.writeInt(dataLength);

			// RESPONSE NAME
			dout.write(dataToSend, 0, dataLength);
			
			
			//IP String 
			dout.writeInt(strIP.length);
			
			dout.write(strIP, 0, strIP.length);

			
			//PORT
			dout.writeInt(port);
	

		
			dout.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dout != null) {
				// dout.close();

				// dont close socket as you are awaiting response from server

				// socket.close();
			}

		}
	}
}
