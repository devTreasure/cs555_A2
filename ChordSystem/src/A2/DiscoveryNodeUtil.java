package A2;

import A2.message.NodeDetails;

public class DiscoveryNodeUtil {

   private String discoveryIP;
   private int discoveryPORT;
   
   public DiscoveryNodeUtil(String discoveryIP, int discoveryPORT) {
      this.discoveryIP = discoveryIP;
      this.discoveryPORT = discoveryPORT;
   }

   public String getDiscoveryIP() {
      return discoveryIP;
   }

   public int getDiscoveryPORT() {
      return discoveryPORT;
   }
   
   //Discover node should return node whose id is note same as myId.
   public NodeDetails askRandomNode(int myId) {
      return null;
   }
   
   
   
   
}
