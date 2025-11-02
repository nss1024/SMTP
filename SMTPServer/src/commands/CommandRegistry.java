package commands;

import utils.SessionState;

import java.io.BufferedWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class CommandRegistry {

    private HashMap<String, SmtpCommand> commands = new HashMap<>();

    public CommandRegistry(Socket socket, BufferedWriter writer, List<SessionState> validNextStates, SessionState currentState, String myDomain){

        commands.put("HELO",new HeloCommand());


    }

    public HashMap<String, SmtpCommand> getCommands() {
        return commands;
    }
}
