/* The interface all command classes implement. Vital for the command pattern.
 *
 */

package commands;

import java.io.IOException;

public interface SmtpCommand {

    void execute (SessionContext sc, String line) throws IOException;

}
