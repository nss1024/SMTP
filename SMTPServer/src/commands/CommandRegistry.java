/* Enum to represent a singleton of the command registry.
 * All available command classes must be registered here.
 *
 */

package commands;

import java.util.HashMap;
import java.util.Map;

public enum CommandRegistry {

    INSTANCE;
    private final Map<String, SmtpCommand> commands;

    CommandRegistry(){
        commands = new HashMap<>();
        commands.put("HELO",new HeloCommand());
        commands.put("MAIL",new MailCommand());
        commands.put("RCPT",new RcptCommand());
        commands.put("DATA",new DataCommand());
        commands.put("QUIT",new QuitCommand());
        commands.put("STARTTLS",new StartTlsCommand());
    }


    public SmtpCommand getCommand(String name) {
        return commands.get(name.toUpperCase());
    }

    public boolean hasCommand(String name) {
        return commands.containsKey(name.toUpperCase());
    }



}
