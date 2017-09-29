package A2;

public class NeighbourUpdaterWorker implements Runnable {

   public boolean continueFlag = true;
   private ChordNode node;  
   
   public NeighbourUpdaterWorker(ChordNode node) {
      this.node = node;
   }

   @Override
   public void run() {
      
      while(continueFlag) {
         try {
            Thread.sleep(1000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         node.verifyNeighbours();
      }
      
   }

}
