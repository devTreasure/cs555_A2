package Management;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class peerIPandPORTRequest {
	
	public void peer_IP_PORT_Request(Socket socket, byte[] dataToSend, int nodeID)
			throws IOException {

		DataOutputStream dout = null;
		try {

			System.out.println(socket.isClosed());

			dout = new DataOutputStream(socket.getOutputStream());

			int dataLength = dataToSend.length;

			dout.writeInt(dataLength);

			// REQUEST_NAME
			dout.write(dataToSend, 0, dataLength);

			// node ID
			System.out.println("Peer is writng down the node ID " + nodeID);
			
			dout.writeInt(nodeID);

		
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
