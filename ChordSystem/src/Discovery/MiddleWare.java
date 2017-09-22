package Discovery;

import java.io.IOException;
import java.net.Socket;

import Management.predessorResponse;

public class MiddleWare {

	public DiscoveryNode discNode;

	public MiddleWare(DiscoveryNode node) {
		this.discNode = node;
	}

	public DiscoveryNode discoveryNode;

	public void predessorNode() {

	}

	public void successorNode() {

	}

	public void sendThenewlyRegisterdNodePoredessorInfo(RingNodes ringNode) throws IOException {
		RingNodes ring=ringNode;
		Socket socket = new Socket(ring.ringNodeIP,ring.ringnodeServerSocketPORT);
		new predessorResponse().response(socket,0);

	}

	public RingNodes getThepredessor(int ringNodeID) {
		RingNodes Ring = null;
		
		if(discNode.ringNodes.size()==1)
		{
			Ring = discNode.ringNodes.get(ringNodeID);

		}
		else
		{
		   int findMinNodeID=0;
		   
		//  for (RingNodes r :  discNode.ringNodes) {
			
		}
			
		
		

		
		return Ring;

	}

}
