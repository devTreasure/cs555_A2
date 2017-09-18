package Discovery;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class DiscoveryWorker implements Runnable {

	public DiscoveryNode dn;
	public Socket s;

	public DiscoveryWorker(Socket sc, DiscoveryNode d) {
		this.dn = d;
		this.s = sc;
	}

	@Override
	public void run() {

		DataInputStream din = null;

		try {

			din = new DataInputStream(this.s.getInputStream());
			
			System.out.println("..: Socket acceepted :...");
			
			int number = din.readInt();

			System.out.println(String.format("server has received the  number : %1$d ", number));

			System.out.println("done");

		}

		catch (Exception e) {
			e.printStackTrace();

		}

		finally {
			try {
				if (din != null)
					din.close();
				if (this.s != null)
					this.s.close();
			} catch (IOException e) {
				System.out.println("Error while closing resources: " + e.getMessage());
			}
		}

	}

}
