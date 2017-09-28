package A2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import A2.message.AskSuccessorMessage;
import A2.message.Command;
import A2.message.NodeDetails;
import A2.message.RegisterReponse;
import A2.message.RegistgerCommand;
import A2.message.ReturnRandomNodeCommand;

public class ChordNode implements Node {

   public static final int SYSTEM_SIZE_IN_BITS = 3; 
   
   private String discoveryIP;
   private int discoveryPORT;

   private String nodeIP;
   private int nodePort;
   private String nodeName;
   private int nodeId;
   
   private TCPSender tcpSender = new TCPSender();


   private List<Integer> fingerTable = new ArrayList();

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
//      node.insertIntoChord();
//      node.buildFingerTable();
//      node.tarnsferData();

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
            node.receiverWorker.continueFlag = false;
            continueOperations = false;
         }
      }

      System.out.println("Bye.");

   }

   private void verifySuccessor() {
      // TODO Auto-generated method stub

   }

   private NodeDetails resolveTragetNode(int k) {
      
      return null;
   }

   private void tarnsferData() {
      // TODO Auto-generated method stub

   }

   private void buildFingerTable() {
      // TODO Auto-generated method stub

   }

   public NodeDetails findSuccessor(NodeDetails fromNode) {
      // 3. Ask for your successor
      AskSuccessorMessage asm = new AskSuccessorMessage(nodeIP, nodePort, nodeId);
      Command nodeDetail = tcpSender.sendAndReceiveData(fromNode.ipAddress, fromNode.port, asm.unpack());

      // 2.x Check very first node condition. Successor will be same as random node.

      // 2.x Has reached target??? receivedNode:ID >= myId:K
      // If no then repeat Point 2 with this receivedNode
      // If yes, go to step 3 (So we have found Successor, lets say Z

      // 3. Ask succcessor's predecessor Lets say X

      return null;

   }

   private void insertIntoChord() throws Exception {

      // 1. Ask discovery to return you a random node. (Send your id so that discovery returns you other node).
      ReturnRandomNodeCommand randomNodeRequest = new ReturnRandomNodeCommand(nodeIP, nodePort, nodeId);
      NodeDetails randomNode = (NodeDetails) tcpSender.sendAndReceiveData(discoveryIP, discoveryPORT, randomNodeRequest.unpack());
      System.out.println(randomNode);
      
      // 2. Check very first node condition. Successor will be same as random node.
      if(randomNode.id == nodeId) {
         buildInitialFingerTable();
      } else {
         NodeDetails successor = findSuccessor(randomNode);
         
      }
      

      // 4. Insert your self (lets say Y) as PRED of succ.
      // Pred(Z) was X now Pred(Z) will be this node Y
      // QUESTION: Can I Set mySelf as Successor of X the Pred(Z)

      // So now X(id) < Y(id) < Z(id)

      // Also TRANSFER data which is <= y(id) from Z to Y(this node).
   }

   private void buildInitialFingerTable() {
      for(int i=1; i<=SYSTEM_SIZE_IN_BITS;i++) {
//         double succOf = nodeId + Math.pow(2, (i-1));
         fingerTable.add(nodeId);
      }
      
   }

   private void registerWithDiscoveryNode() throws Exception {
      System.out.println("Registering with discovery node.");
      RegistgerCommand command = new RegistgerCommand(nodeIP, nodePort, nodeId);
      Command resp = tcpSender.sendAndReceiveData(discoveryIP, discoveryPORT, command.unpack());
      System.out.println(resp);
      
      RegisterReponse response = (RegisterReponse) resp;
      if(!response.isSuccess()) {
         throw new RuntimeException("Node Id already exist. Please use another id");
      }
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
   public Command notify(Command command) {
      if(command instanceof AskSuccessorMessage) {
         AskSuccessorMessage asm = (AskSuccessorMessage) command;
         NodeDetails node = resolveTragetNode(asm.id);
      }
      System.out.println("Received command >> " + command);
      return new NodeDetails("", -1, -1, true, "Nothing");
   }

}
