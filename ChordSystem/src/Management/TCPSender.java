package Management;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPSender {

	private Socket socket;
	private DataOutputStream dout;

	public TCPSender(Socket socket) throws IOException {
		this.socket = socket;
		
		dout = new DataOutputStream(this.socket.getOutputStream());
	}

	public void sendData(byte[] dataToSend) throws IOException {
		int dataLength = dataToSend.length;
		dout.writeInt(dataLength);
		dout.write(dataToSend, 0, dataLength);
		dout.flush();
	}

	public void sendMessage() throws IOException {
		try {
		
			int randomN = 191;
			dout.writeInt(randomN);
			dout.flush();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dout != null)
				dout.close();
		}
	}

}
