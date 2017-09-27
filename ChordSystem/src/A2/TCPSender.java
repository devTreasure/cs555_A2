package A2;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPSender {

   public void sendData(String hostIp, int hostPort, byte[] data) throws Exception {
      Socket socket = null;
      try {
         socket = new Socket(hostIp, hostPort);
         sendData(socket, data);
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         socket.close();
      }
   }
   
   public void sendData(Socket socket, byte[] data) throws Exception {
      BufferedOutputStream dout = null;
      try {
         dout = new BufferedOutputStream(socket.getOutputStream());
         dout.write(data);
         dout.close();
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         dout.close();
      }
   }
   
}
