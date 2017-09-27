package Management;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class randomNodeResponse {
	
	public void  response(Socket socket,byte[] dataToSend, int responseRandomNodeValue,byte[] nodeIP,int nodePORT) throws IOException
	{
		DataOutputStream dout = null;
		Socket socket1=null;
		try {
			
			
			socket1= socket;
			
			dout = new DataOutputStream(socket1.getOutputStream());	
									
			int dataLength = dataToSend.length;
			
			dout.writeInt(dataLength);
			
			//RESPONSE_TYPE_NAME
			dout.write(dataToSend, 0, dataLength);
			
			//random node ID
			dout.writeInt(responseRandomNodeValue);
			
			//			
			int nodeIPLength = nodeIP.length;
			
			//length of IP
			dout.writeInt(nodeIPLength);
			
			//NODE_ID_
			dout.write(nodeIP, 0, nodeIPLength);

			//NodePORT
			dout.writeInt(nodePORT);
			
			
			
			dout.flush();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dout != null)
				dout.close();
			    socket1.close();
		}
	}

}
