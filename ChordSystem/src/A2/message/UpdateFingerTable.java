package A2.message;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UpdateFingerTable implements Command {

   public static final String cmd = "CMD_UpdateFingerTable";

   @Override
   public byte[] unpack() {
      byte[] marshalledBytes = null;
      ByteArrayOutputStream baOutputStream = null;
      DataOutputStream dout = null;

      try {
         baOutputStream = new ByteArrayOutputStream();
         dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
         dout.writeInt(cmd.length());
         dout.write(cmd.getBytes());
         dout.flush();
         marshalledBytes = baOutputStream.toByteArray();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            baOutputStream.close();
            dout.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return marshalledBytes;
   }

   @Override
   public void pack(DataInputStream din) {
   }

   @Override
   public String toString() {
      return "UpdateFingerTable []";
   }

}
