package Management;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class peerPredecessorResponse {
	
	
	public void response(Socket socket, byte[] dataToSend ,int pred )
			throws IOException {

		DataOutputStream dout = null;
		try {

			System.out.println(socket.isClosed());

			dout = new DataOutputStream(socket.getOutputStream());

			int dataLength = dataToSend.length;

			dout.writeInt(dataLength);

			// REQUEST_NAME
			dout.write(dataToSend, 0, dataLength);
			
			
			dout.writeInt(pred);
			
			
			//dout.writeInt(succ);
				
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
