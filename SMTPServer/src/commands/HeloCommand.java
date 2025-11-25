/*Class to handle the initial HELO command
 *
 */
package commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;


import java.io.IOException;


public class HeloCommand implements SmtpCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());




    public HeloCommand(){
           }

    @Override
    public void execute(SessionContext sc, String line) throws IOException {
        logger.trace("HELO received");
        sc.setCurrentState(SessionState.HELO_RECEIVED);
        SMTPUtils.updateAcceptableStates(sc.getValidNextStates(), SessionState.MAIL);
        sc.setClientDomain(SMTPUtils.getData(line));
        SMTPUtils.sendMessage(sc.getSocket(), sc.getWriter(), SmtpMessage.OK.getFullText());

    }
}
