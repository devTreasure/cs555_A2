package Ring;

import java.io.IOException;

import Management.successorNodeResponse;

public class successorNodeWorker implements Runnable{

	public Node node;
	
	
	public successorNodeWorker(Node node) {
		// TODO Auto-generated constructor stub
		this.node=node;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			int successorid = 0;
			try {
				successorid = new  successorNodeResponse().processRequestOnserver(this.node.serversocket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Serve sent the Successor id for this node " + successorid);
			
		}
		
	}

}
