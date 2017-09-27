package A2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Sender {

   private int sendCounter = 0;
   private double sumOfDataSent;
   private ChordNode node;

   public Sender(ChordNode node) throws Exception {
      this.node = node;
   }

   public void sendMessages() throws IOException {

    //?????????????????
      for (int i = 0; i < 0; i++) {
         
       //?????????????????
         String randomNode = "";
         String[] SplitStrmessageSenderNode = randomNode.split(" ");

         // putting collator IP and PORT
         String receiverNodeIP = SplitStrmessageSenderNode[0];
         int receiverNodePORT = Integer.parseInt(SplitStrmessageSenderNode[1]);

         Socket socket = null;
         try {
            System.out.println("Round["+ i + "] Connecting to:" + receiverNodeIP + ":" + receiverNodePORT);
            socket = new Socket(receiverNodeIP, receiverNodePORT);
            LoopMessaging(socket);
            System.out.println("Sent messages:" + sendCounter);
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            if (socket != null)
               socket.close();
         }
      }
   }

   public void LoopMessaging(Socket socket) throws IOException {
      DataOutputStream dout = null;
      try {
         dout = new DataOutputStream(socket.getOutputStream());
       //?????????????????
         for (int j = 0; j < 0; j++) {
            
            //?????????????????
            int randomN = 1;
            this.sendCounter += 1;
            this.sumOfDataSent += randomN;
            System.out.println("Random number generated at [" + j + "] the client-payLoad is " + randomN);
            dout.writeInt(randomN);
         }

         dout.flush();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (dout != null)
            dout.close();
      }
   }

   public int getSendCounter() {
      return sendCounter;
   }

   public double getSumOfDataSent() {
      return sumOfDataSent;
   }

}
