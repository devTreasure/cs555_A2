package A2;

import A2.message.Command;

public interface Node {
   Command notify(Command command) throws Exception;
}
