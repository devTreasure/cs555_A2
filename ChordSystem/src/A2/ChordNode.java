package A2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import A2.message.Command;
import A2.message.GetSuccessor;
import A2.message.NodeDetails;
import A2.message.PredecessorDetail;
import A2.message.RegistgerCommand;
import A2.message.ResolveSuccessorInFingerTableMessage;
import A2.message.Response;
import A2.message.ReturnRandomNodeCommand;
import A2.message.SetMeAsPredecessor;
import A2.message.SetMeAsSuccessor;
import A2.message.UpdateFingerTable;

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
   private Hashtable<Integer, NodeDetails> nodeDetails = new Hashtable<>();

   private ReceiverWorker receiverWorker;
   private Thread receiverWorkerThread;

   private NodeDetails successor;
   private NodeDetails predecessor;

   private static ChordNode node;

   private Thread neighbourUpdaterThread;

   private NeighbourUpdaterWorker neighbourUpdaterWorker;

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

      node = new ChordNode();
      node.discoveryIP = discoverIP;
      node.discoveryPORT = discoverPORT;
      node.nodeId = nodeId;
      node.intializeServerNode();
      node.registerWithDiscoveryNode();
      node.insertIntoChord();
      node.buildFingerTable();
      node.askSuccessorTOUpdateFingerTable();
      //may inform successor/predecessor to update it's finger table      
      
      // node.tarnsferData();

      // ---------------
      int k = 0;
      // Will find smallest id >= K
//      node.verifySuccessor();

      boolean continueOperations = true;
      
      while (continueOperations) {
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         String exitStr = br.readLine();
         System.out.println("Received command is:" + exitStr);

         if ("exit".equalsIgnoreCase(exitStr)) {
            System.out.println("Exiting.");
            node.receiverWorker.continueFlag = false;
            node.neighbourUpdaterWorker.continueFlag = false;
            continueOperations = false;
         } else if ("fingertable".equalsIgnoreCase(exitStr)) {
            node.printFingerTable();
         } else if ("details".equalsIgnoreCase(exitStr)) {
            node.printDetails();
         } else if ("updateft".equalsIgnoreCase(exitStr)) {
            node.buildFingerTable();
            node.printDetails();
         }
      }

      System.out.println("Bye.");

   }

   private void askSuccessorTOUpdateFingerTable() {
      System.out.println("Update Finfer table");
      UpdateFingerTable updateFingerTable = new UpdateFingerTable();
      Command response = tcpSender.sendAndReceiveData(successor.ipAddress, successor.port, updateFingerTable.unpack());
      System.out.println("   Updated.");
      
      
   }

   private void printFingerTable() {
      for (int i = 0; i < fingerTable.size(); i++) {
         System.out.println("[" + i + "]" + fingerTable.get(i));
      }
   }

   private void printDetails() {
      System.out.println("");
      System.out.println("Successor: " + successor);
      System.out.println("Predecessor: " + predecessor);
      printFingerTable();
      System.out.println("");
   }

   public void verifyNeighbours() {
      //Ask SUC about it's PRED
      //If not you then update
//      System.out.println("Verifying Neighbours");
      
      if(nodeId == successor.id) {
         return;
      }
      
      PredecessorDetail predecessorDetail = new PredecessorDetail();
      NodeDetails predOfSuc = (NodeDetails) tcpSender.sendAndReceiveData(successor.ipAddress, successor.port, predecessorDetail.unpack());
      
      if(predOfSuc.id != nodeId) {
         System.out.println("   predOfSuc:" + predOfSuc);
         //change
         //set  PRED as your succ
         this.successor = predOfSuc;
         this.fingerTable.remove(0);
         this.fingerTable.add(0, predOfSuc.id);
         
         //set me as your pred
         SetMeAsPredecessor setMeAsPredecessor = new SetMeAsPredecessor(this.nodeIP, this.nodePort, this.nodeId);
         Command respo = tcpSender.sendAndReceiveData(predOfSuc.ipAddress, predOfSuc.port, setMeAsPredecessor.unpack());
         System.out.println("   " + respo);
         
         buildFingerTable();
         printDetails();
      }
      
   }

   // Find successor from this finger table. Greatest Id less than K
   private NodeDetails resolveTragetNode(int k) {
      
      System.out.println("Resolving TargetNode");
      int highestId = highestIdLessThanEqualTo(k);
      System.out.println("   Highest Id:" + highestId);
      NodeDetails response = null;
      if (highestId == nodeId) {
         response = new NodeDetails(nodeIP, nodePort, highestId, true, "Resolved Highest Id in finger table");
         response.mySuccessorId = this.successor.id;
      } else if (successor!=null && successor.port!=-1 && successor.id == highestId) {
         response =  new NodeDetails(successor.ipAddress, successor.port, highestId, true, "Resolved Highest Id in finger table");
      } else if (predecessor!=null && predecessor.port!=-1 && predecessor.id == highestId) {
         response =  new NodeDetails(predecessor.ipAddress, predecessor.port, highestId, true, "Resolved Highest Id in finger table");
      } else {
         response =  new NodeDetails("", -1, highestId, true, "Resolved Highest Id in finger table");
      }
      
      System.out.println("   Returning: " + response);
      return response;
   }

   public int highestIdLessThanEqualTo(int k) {
      int highestId = -1;
      
      if (fingerTable.isEmpty()) {
         return highestId;
      }

      List<Integer> sortedFingerTable = new ArrayList<>(fingerTable);
      Collections.sort(sortedFingerTable);

      if (k < sortedFingerTable.get(0)) {
         highestId = sortedFingerTable.get(0);
      } else if (k < fingerTable.get(0)) {
         highestId = fingerTable.get(0);
      } else {

         for (int i = 0; i < sortedFingerTable.size() - 1; i++) {
            int j = sortedFingerTable.get(i);
            int jPlusOne = sortedFingerTable.get(i + 1);

            if (j < k) {
               highestId = j;
               if (k < jPlusOne) {
                  break;
               }
            }
         }
      }
       
      System.out.println(highestId);
      return highestId;
   }

   private void tarnsferData() {
      // TODO Auto-generated method stub

   }

   private void buildFingerTable() {
      System.out.println("Building finger table");
      fingerTable.clear();
      if (nodeId == successor.id) { // First Node condition
         for (int i = 0; i < SYSTEM_SIZE_IN_BITS; i++) {
            fingerTable.add(nodeId);
         }
      } else {

         int maxK = (int) Math.pow(2, SYSTEM_SIZE_IN_BITS);

         int succID = successor.id;

         for (int i = 0; i < SYSTEM_SIZE_IN_BITS; i++) {
            int k = nodeId + (int) Math.pow(2, i);
            System.out.println("   k=" + k);

            if(k >= maxK) {
               k = k % maxK;
               System.out.println("   Adjusted K=" + k);
            }
            
            if (succID >= k) {
               fingerTable.add(successor.id);
               System.out.println("   FT[" + k + "]=" + successor.id);
            } else {
               // its beyond successor.
               
               NodeDetails succ = findSuccessor(successor, k);
               System.out.println("   " + succ);
               fingerTable.add(succ.id);
               System.out.println("   FT[" + k + "]=" + succ.id);
            }
         }
      }

   }

   private NodeDetails getNodeDetails(NodeDetails from, int k, int originId) {

      GetSuccessor successor = new GetSuccessor();
      NodeDetails scrDetails = (NodeDetails) tcpSender.sendAndReceiveData(from.ipAddress, from.port, successor.unpack());
      
      if(scrDetails.id == k) {
         return scrDetails;
      }
            
      if(scrDetails.id == originId) {
         return new NodeDetails("", -1, k, false, "Not Found");
      }

      return getNodeDetails(scrDetails, k, originId);
   }

   // Find node.id >= k
   public NodeDetails findSuccessor(NodeDetails fromNode, int k) {
      // 3. Ask for your successor
      System.out.println("   for k[" + k + "]Asking successor detail to: " + fromNode);
      ResolveSuccessorInFingerTableMessage asm =
            new ResolveSuccessorInFingerTableMessage("", -1, k);
      NodeDetails succNode = (NodeDetails) tcpSender.sendAndReceiveData(fromNode.ipAddress,
            fromNode.port, asm.unpack());

      System.out.println("   Suggested Successor :" + succNode);
      if (succNode.port == -1) {
         succNode = getNodeDetails(fromNode, succNode.id, fromNode.id);
         System.out.println("Resolved: " + succNode);
      }
      
      if (succNode.port == -1) {
         throw new RuntimeException("   Can not find your successor.");
      }


      boolean found = false;

      // CONDITION:1 Node(id) > = k
      found = succNode.id >= k;


      // CONDITION:2
      /**
       * Successor is the only node in the chord. successor id will be same as it's predecessor id.
       */
      if (!found)
         found = succNode.id == succNode.mySuccessorId;


      // CONDITION3:
      /**
       * k=29 from peer 28 (Page3 of Assignment).
       * 
       * Given: Node(28)'s succ is Node(1). Expected result is: Node(1). This condition can not be
       * solved using given mathematical function Node(id) > = k
       * 
       * When Node(28) will be asked to resolve 29, it will return 14 and this will go into infinite
       * loop. Find when Chord re-starts condition and that is the successor.
       * 
       * k > succNode(Id) AND succNode(Id) > SUCC(succNode(Id)) then return SUCC(succNode(Id)).
       */
      if (!found && nodeId > succNode.id && succNode.id > succNode.mySuccessorId) {
         found = true;
         // return node details with ID succNode.mySuccessorId
         GetSuccessor getSuccessor = new GetSuccessor();
         succNode = (NodeDetails) tcpSender.sendAndReceiveData(succNode.ipAddress, succNode.port,
               getSuccessor.unpack());
      }

      if (!found) {
         findSuccessor(succNode, k);
      }

      return succNode;

      // 2.x Has reached target??? receivedNode:ID >= myId:K
      // If no then repeat Point 2 with this receivedNode
      // If yes, go to step 3 (So we have found Successor, lets say Z

      // 3. Ask succcessor's predecessor Lets say X
   }

   private void insertIntoChord() throws Exception {

      System.out.println("Insert into Chord.");
      // 1. Ask discovery to return you a random node. (Send your id so that discovery returns you
      // other node).
      ReturnRandomNodeCommand randomNodeRequest =
            new ReturnRandomNodeCommand(nodeIP, nodePort, nodeId);
      NodeDetails randomNode = (NodeDetails) tcpSender.sendAndReceiveData(discoveryIP,
            discoveryPORT, randomNodeRequest.unpack());
      System.out.println("   RandomNode:" + randomNode);

      // 2. Check very first node condition. Successor will be same as random node. Or Random node's
      // id will be same as mine.
      if (randomNode.id == nodeId) {
         System.out.println("   I am the first node in system.");
         this.predecessor = new NodeDetails(nodeIP, nodePort, nodeId, true, "");
         this.successor = new NodeDetails(nodeIP, nodePort, nodeId, true, ""); // Redundant but kept
                                                                               // for ip/port

      } else {
         System.out.println("   I am NOT the first node in system. Findign Successor.");
         NodeDetails successor = findSuccessor(randomNode, nodeId);
         System.out.println("   Successor : " + successor);

         // My successor is you. Set successor as your successor
         this.successor = successor;
         // Create the Ring for the first time.
         if (successor.id == successor.mySuccessorId) {
            this.predecessor = successor;
         }
         
         // Your predecessor is me
         SetMeAsPredecessor setMeAsPredecessor =
               new SetMeAsPredecessor(this.nodeIP, this.nodePort, nodeId);
         tcpSender.sendAndReceiveData(successor.ipAddress, successor.port,
               setMeAsPredecessor.unpack());

         // Create the Ring for the first time.
         if (successor.id == successor.mySuccessorId) {

            SetMeAsSuccessor setMeAsSuccessor =
                  new SetMeAsSuccessor(this.nodeIP, this.nodePort, nodeId);
            tcpSender.sendAndReceiveData(successor.ipAddress, successor.port,
                  setMeAsSuccessor.unpack());
         }

         // My Predecessor ???
         // PRED(succ) is still pointing to succ

      }


      // 4. Insert your self (lets say Y) as PRED of succ.
      // Pred(Z) was X now Pred(Z) will be this node Y
      // QUESTION: Can I Set mySelf as Successor of X the Pred(Z)

      // So now X(id) < Y(id) < Z(id)

      // Also TRANSFER data which is <= y(id) from Z to Y(this node).
   }

   private void registerWithDiscoveryNode() throws Exception {
      System.out.println("Registering with discovery node.");
      RegistgerCommand command = new RegistgerCommand(nodeIP, nodePort, nodeId);
      Command resp = tcpSender.sendAndReceiveData(discoveryIP, discoveryPORT, command.unpack());
      System.out.println("   " + resp);

      Response response = (Response) resp;
      if (!response.isSuccess()) {
         throw new RuntimeException("Node Id already exist. Please use another id");
      }
   }
   
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
      
      neighbourUpdaterWorker = new NeighbourUpdaterWorker(this);
      neighbourUpdaterThread = new Thread(neighbourUpdaterWorker);
      neighbourUpdaterThread.start();

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
   public Command notify(Command command) throws Exception {
      if(!(command instanceof PredecessorDetail)) {
         System.out.println("Received command >> " + command);
      }
      Command response = new NodeDetails("", -1, -1, true, "Nothing");
      if (command instanceof ResolveSuccessorInFingerTableMessage) {
         ResolveSuccessorInFingerTableMessage asm = (ResolveSuccessorInFingerTableMessage) command;
         response = resolveTragetNode(asm.id);
      } else if (command instanceof SetMeAsSuccessor) {
         SetMeAsSuccessor msg = (SetMeAsSuccessor) command;
         response = successorChanged(msg);
      } else if (command instanceof SetMeAsPredecessor) {
         SetMeAsPredecessor msg = (SetMeAsPredecessor) command;
         response = predecessorChanged(msg);
      } else if (command instanceof GetSuccessor) {
         // GetSuccessor msg = (GetSuccessor) command;
         response = getSuccessor();
      } else if (command instanceof UpdateFingerTable) {
         // GetSuccessor msg = (GetSuccessor) command;
         response = updateFingerTable();
      } else if(command instanceof PredecessorDetail) {
         response = predecessor;
      }
//      System.out.println("Response: " + response);
      return response;
   }

   private Command updateFingerTable() throws Exception {
      buildFingerTable();
      return new Response(true, "Fingertable updated");
   }

   private NodeDetails getSuccessor() {
      return successor;
   }

   private Command predecessorChanged(SetMeAsPredecessor msg) {
      this.predecessor = new NodeDetails(msg.ipAddress, msg.port, msg.id, true, "");
      System.out.println("");
      printDetails();
      return new Response(true, "Predecessor set");
   }

   private Command successorChanged(SetMeAsSuccessor msg) {
      fingerTable.remove(0);
      fingerTable.add(0, msg.id);
      this.successor = new NodeDetails(msg.ipAddress, msg.port, msg.id, true, "");
      System.out.println("");
      printDetails();
      return new Response(false, "????");
   }

}
