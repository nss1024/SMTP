package commands;

import java.io.IOException;

public interface SmtpCommand {

    void execute (String line) throws IOException;

}
