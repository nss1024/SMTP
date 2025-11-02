package commands;

import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeloCommand implements SmtpCommand {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());




    public HeloCommand(){
           }

    @Override
    public void execute(SessionContext sc, String line) throws IOException {

        logger.log(Level.INFO, "HELO received");
        sc.setCurrentState(SessionState.HELO_RECEIVED);
        SMTPUtils.updateAcceptableStates(sc.getValidNextStates(), SessionState.MAIL);
        sc.setClientDomain(SMTPUtils.getData(line));
        SMTPUtils.sendMessage(sc.getSocket(), sc.getWriter(), SmtpMessage.OK.getFullText());

    }
}
