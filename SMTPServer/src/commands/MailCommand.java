package commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;

import java.io.IOException;
import java.util.logging.Level;

public class MailCommand implements SmtpCommand{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public MailCommand(){

    }

    @Override
    public void execute(SessionContext sc, String line) throws IOException {
        logger.trace("MAIL received");
        String from = SMTPUtils.getEmailAddress(line);
        if(!SMTPUtils.validateEmailAddress(from)){
            SMTPUtils.sendMessage(sc.getSocket(),sc.getWriter(),SmtpMessage.BAD_SENDER_ADDRESS.getFullText());
            return;
        }
        sc.getSmtpEmail().setFrom(from);
        sc.setCurrentState(SessionState.MAIL_FROM_RECEIVED);
        SMTPUtils.updateAcceptableStates(sc.getValidNextStates(), SessionState.RCPT);
        SMTPUtils.sendMessage(sc.getSocket(), sc.getWriter(), SmtpMessage.OK.getFullText());

    }
}
