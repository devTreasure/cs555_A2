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
             } else if (Response.cmd.equals(str_request_type)) {
                  Response registerReponse = new Response();
                  registerReponse.pack(din);
                  cmd = registerReponse;
             } else if(ReturnRandomNodeCommand.cmd.equals(str_request_type)) {
                ReturnRandomNodeCommand command = new ReturnRandomNodeCommand();
                command.pack(din);
                cmd = command;
             } else if (NodeDetails.cmd.equals(str_request_type)) {
                NodeDetails command = new NodeDetails();
                command.pack(din);
                cmd = command;
             } else if(ResolveSuccessorInFingerTableMessage.cmd.equals(str_request_type)) {
                ResolveSuccessorInFingerTableMessage command = new ResolveSuccessorInFingerTableMessage();
                command.pack(din);
                cmd = command;
             } else if (SetMeAsPredecessor.cmd.equals(str_request_type)) {
                SetMeAsPredecessor command = new SetMeAsPredecessor();
                command.pack(din);
                cmd = command;
             } else if (SetMeAsSuccessor.cmd.equals(str_request_type)) {
                SetMeAsSuccessor command = new SetMeAsSuccessor();
                command.pack(din);
                cmd = command;
             } else if(GetSuccessor.cmd.equals(str_request_type)) {
                GetSuccessor command = new GetSuccessor();
                command.pack(din);
                cmd = command;
             } else {
                System.out.println("ERROR: UNKNOWN COMMAND. " + str_request_type);
             }
             
        } catch (Exception e) {
             e.printStackTrace();
        } finally {
//         try {
//            din.close();
//         } catch (IOException e) {
//            e.printStackTrace();
//         }
      }

        System.out.println("Created:" + cmd);
        return cmd;
        
     }
   
}
