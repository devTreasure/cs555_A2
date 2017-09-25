package Management;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class successorNodeResponse_one {
	public void  response(Socket socket, int responseValue) throws IOException
	{
		DataOutputStream dout = null;
		Socket sc=null;
		try {
			
			
			sc= socket;
			dout = new DataOutputStream(sc.getOutputStream());					
			dout.writeInt(responseValue);
			dout.flush();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dout != null)
				dout.close();
		     	sc.close();
		}
	}

}
