package A2.message;

import java.io.DataInputStream;

public interface Command {

   void pack(DataInputStream din);

   byte[] unpack();

}
