package A2;

import A2.message.Command;

public interface Node {
   void notify(Command command) throws Exception;
}
