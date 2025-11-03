package commands;

import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataCommand implements SmtpCommand{

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public void execute(SessionContext sc, String line) throws IOException {
        logger.log(Level.INFO, "DATA received");
        sc.setReceivingData(true);
        SMTPUtils.updateAcceptableStates(sc.getValidNextStates(), SessionState.DATA_COMPLETE);
        SMTPUtils.sendMessage(sc.getSocket(), sc.getWriter(), SmtpMessage.START_MAIL_INPUT.getFullText());
    }
}
