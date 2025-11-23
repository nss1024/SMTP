package commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;

import java.io.IOException;
import java.util.logging.Level;

public class DataCommand implements SmtpCommand{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(SessionContext sc, String line) throws IOException {
        //check to see if there is at least one recipient before committing to data transfer.
        if (sc.getSmtpEmail().getToList().isEmpty()) {
            SMTPUtils.sendMessage(sc.getSocket(),sc.getWriter(), SmtpMessage.BAD_SEQUENCE.getFullText());
            return;
        }
        logger.trace("Data received");
        sc.setReceivingData(true);
        SMTPUtils.updateAcceptableStates(sc.getValidNextStates(), SessionState.DATA_COMPLETE);
        SMTPUtils.sendMessage(sc.getSocket(), sc.getWriter(), SmtpMessage.START_MAIL_INPUT.getFullText());
    }
}
