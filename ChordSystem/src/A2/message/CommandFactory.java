package A2.message;

import java.io.DataInputStream;
import java.net.Socket;

public class CommandFactory {
   
     public static Command process(Socket socket) {
      
        Command cmd = null;
        DataInputStream din = null;
        try {
             din = new DataInputStream(socket.getInputStream());
             int request_Typelength = 0;
             request_Typelength = din.readInt();
             byte[] request_Type = new byte[request_Typelength];
             din.readFully(request_Type);
             String str_request_type = new String(request_Type);
             System.out.println(str_request_type);

             if (RegistgerCommand.cmd.equals(str_request_type)) {
                  RegistgerCommand registgerCommand = new RegistgerCommand();
                  registgerCommand.pack(din);
                  cmd = registgerCommand;
             } else if (RegisterReponse.cmd.equals(str_request_type)) {
                  RegisterReponse registerReponse = new RegisterReponse();
                  registerReponse.pack(din);
                  cmd = registerReponse;
             } else {
                System.out.println("ERROR: UNKNOWN COMMAND. " + str_request_type);
             }
             
        } catch (Exception e) {
             e.printStackTrace();
        }

        System.out.println("Created:" + cmd);
        return cmd;
        
     }
   
}
