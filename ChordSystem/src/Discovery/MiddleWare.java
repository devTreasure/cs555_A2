package Discovery;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;

import Management.predessorResponse;

public class MiddleWare {

	public DiscoveryNode discNode;

	public MiddleWare(DiscoveryNode node) {
		this.discNode = node;
	}

	public DiscoveryNode discoveryNode;

	public void predessorNode() {

	}

	public int getTheRanomNode() {
		
		return 0;
	}

	public void successorNode() {

	}
	

	public void sendThenewlyRegisterdNodePoredessorInfo(RingNodes ringNode) throws IOException {

		RingNodes ring = ringNode;

		Socket socket = new Socket(ring.ringNodeIP, ring.ringnodeServerSocketPORT);

		RingNodes predRing = getThepredessor(ringNode.ringNodeID);

		// Sending registered node it's predessor..Horray !!!

		new predessorResponse().response(socket, predRing.ringNodeID);

	}

	public RingNodes getThepredessor(int ringNodeID) {

		RingNodes Ring = null;

		if (discNode.ringNodes.size() == 1) {
			Ring = discNode.ringNodes.get(ringNodeID);

		} else {
			int findMinNodeID = 0;

			// for (RingNodes r : discNode.ringNodes) {
			for (Integer key : discNode.ringNodes.keySet()) {

				if (key < findMinNodeID) {
					findMinNodeID = key;
				}

			}

			Ring = discNode.ringNodes.get(findMinNodeID);

		}

		return Ring;

	}
}
