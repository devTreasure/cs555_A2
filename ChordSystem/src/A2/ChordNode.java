package A2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Hashtable;

import A2.message.Command;
import A2.message.NodeDetails;
import A2.message.RegistgerCommand;

public class ChordNode implements Node {

   private String discoveryIP;
   private int discoveryPORT;

   private String nodeIP;
   private int nodePort;
   private String nodeName;
   private int nodeId;

   private TCPSender tcpSender = new TCPSender();


   private Hashtable<Integer, Integer> fingerTable = new Hashtable<>();

   private ReceiverWorker receiverWorker;
   private Thread receiverWorkerThread;

   public static void main(String[] args) throws Exception {
      int discoverPORT = 0;
      String discoverIP = "";
      int nodeId = 0;

      if (args.length < 2) {
         System.out.println(
               "Exa: java A2.Node <DISCOVERY NODE IP> <DISCOVERY NODE PORT> <ID Optional>");
         System.exit(0);
      }

      try {
         discoverIP = args[0];
         discoverPORT = Integer.parseInt(args[1]);
         if (args.length == 3)
            nodeId = Integer.parseInt(args[2]);
      } catch (Exception e) {
         System.out.println("Error: Please provide numneric argument.");
         System.exit(0);
      }

      ChordNode node = new ChordNode();
      node.discoveryIP = discoverIP;
      node.discoveryPORT = discoverPORT;
      node.nodeId = nodeId;
      node.intializeServerNode();
      node.registerWithDiscoveryNode();
      node.insertIntoChord();
      node.buildFingerTable();
      node.tarnsferData();

      // ---------------
      int k = 0;
      // Will find smallest id >= K
      node.resolveTragetNode(k);
      node.verifySuccessor();

      boolean continueOperations = true;

      while (continueOperations) {
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         String exitStr = br.readLine();
         System.out.println("Received command is:" + exitStr);

         if ("exit".equalsIgnoreCase(exitStr)) {
            System.out.println("Exiting.");
            node.receiverWorkerThread.stop();
            continueOperations = false;
         }
      }

      System.out.println("Bye.");

   }

   private void verifySuccessor() {
      // TODO Auto-generated method stub

   }

   private void resolveTragetNode(int k) {

   }

   private void tarnsferData() {
      // TODO Auto-generated method stub

   }

   private void buildFingerTable() {
      // TODO Auto-generated method stub

   }

   public NodeDetails findCuccessor(NodeDetails randomNode) {
      // 2. Ask for your successor
      AskSuccessorMessage asm = new AskSuccessorMessage(nodeId, randomNode);
      NodeDetails nodeDetail = asm.send();

      // 2.x Check very first node condition. Successor will be same as random node.

      // 2.x Has reached target??? receivedNode:ID >= myId:K
      // If no then repeat Point 2 with this receivedNode
      // If yes, go to step 3 (So we have found Successor, lets say Z

      // 3. Ask succcessor's predecessor Lets say X

      return nodeDetail;

   }

   private void insertIntoChord() {

      // 1. Ask discovery to return you a random node. (Send your id so that discovery returns you
      // other node).
      DiscoveryNodeUtil discoveryNodeUtil = new DiscoveryNodeUtil(discoveryIP, discoveryPORT);
      NodeDetails randomNode = discoveryNodeUtil.askRandomNode(nodeId);

      NodeDetails successor = findCuccessor(randomNode);

      // 4. Insert your self (lets say Y) as PRED of succ.
      // Pred(Z) was X now Pred(Z) will be this node Y
      // QUESTION: Can I Set mySelf as Successor of X the Pred(Z)

      // So now X(id) < Y(id) < Z(id)

      // Also TRANSFER data which is <= y(id) from Z to Y(this node).
   }



   private void registerWithDiscoveryNode() throws Exception {
      System.out.println("Registering with discovery node.");
      RegistgerCommand command = new RegistgerCommand(nodeIP, nodePort, nodeId);
      tcpSender.sendData(discoveryIP, discoveryPORT, command.unpack());
      // Discovery will do Id Collision check and then return TRUE/FALSE as response
   }

   // private void sendStatistics() throws Exception {
   // Socket socket = null;
   // try {
   // System.out.println("Connecting to Collator @:" + discoveryIP + ":" + discoveryPORT);
   // socket = new Socket(discoveryIP, discoveryPORT);
   // sendStatistics(socket);
   // } catch (Exception e) {
   // e.printStackTrace();
   // } finally {
   // if (socket != null)
   // socket.close();
   // }
   // }

   // private void sendStatistics(Socket socket) throws Exception {
   // DataOutputStream dout = null;
   // try {
   // dout = new DataOutputStream(socket.getOutputStream());
   // int length = getNodeName().length();
   // byte[] nodeNameAsBytes = getNodeName().getBytes();
   // dout.writeInt(length);
   // dout.write(nodeNameAsBytes);
   // dout.writeInt(sender.getSendCounter());
   // dout.writeDouble(sender.getSumOfDataSent());
   // dout.flush();
   // } catch (Exception e) {
   // e.printStackTrace();
   // } finally {
   // if (dout != null)
   // dout.close();
   // }
   // printStatistics();
   // }
   //
   // private void printStatistics() {
   // System.out.println("sent....");
   // System.out.println(sender.getSendCounter());
   // System.out.println(sender.getSumOfDataSent());
   // System.out.println(receiverWorker.getReceiveCounter());
   // System.out.println(receiverWorker.getPayloadTotal());
   //
   // }

   public void intializeServerNode() throws IOException {
      System.out.println("Initializing Node ...");

      ServerSocket serversocket = new ServerSocket(0);
      this.nodePort = serversocket.getLocalPort();

      InetAddress ip = InetAddress.getLocalHost();
      this.nodeIP = (ip.getHostAddress()).trim();
      this.nodeName = this.nodeIP + ":" + this.nodePort;

      receiverWorker = new ReceiverWorker(serversocket, this);
      receiverWorkerThread = new Thread(receiverWorker);
      receiverWorkerThread.start();

      System.out.println(this);
      System.out.println("node started ...");

   }

   public int getCollatorPORT() {
      return discoveryPORT;
   }

   public String getCollatorIP() {
      return discoveryIP;
   }

   public int getNodePort() {
      return nodePort;
   }

   public String getNodeIP() {
      return nodeIP;
   }

   public int getNodeId() {
      return nodeId;
   }

   public String getNodeName() {
      return nodeName;
   }

   @Override
   public String toString() {
      return "Node [nodeIP=" + nodeIP + ", nodePort=" + nodePort + ", nodeId=" + nodeId
            + ", nodeName=" + nodeName + " discoveryIP=" + discoveryIP + ", discoveryPORT="
            + discoveryPORT + "]";
   }

   @Override
   public void notify(Command command) {
      System.out.println("Received command >> " + command);
   }

}
