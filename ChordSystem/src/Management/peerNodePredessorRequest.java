package Management;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class peerNodePredessorRequest {
	
	public void request(Socket socket, byte[] dataToSend)
			throws IOException {

		DataOutputStream dout = null;
		try {

			System.out.println(socket.isClosed());

			dout = new DataOutputStream(socket.getOutputStream());

			int dataLength = dataToSend.length;

			dout.writeInt(dataLength);

			// REQUEST_NAME
			dout.write(dataToSend, 0, dataLength);


		
			dout.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dout != null) {
				 dout.close();
				 socket.close();
			}

		}
	}

}
