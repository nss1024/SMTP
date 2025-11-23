package commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;

import java.io.IOException;
import java.util.logging.Level;

public class RcptCommand implements SmtpCommand{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(SessionContext sc, String line) throws IOException {
        String to = SMTPUtils.getEmailAddress(line);
        if(SMTPUtils.validateEmailAddress(to)){
            sc.getSmtpEmail().addToRecipientList(to);
            sc.setCurrentState(SessionState.RCPT_TO_RECEIVED);
            SMTPUtils.updateAcceptableStates(sc.getValidNextStates(), SessionState.RCPT, SessionState.DATA);
            SMTPUtils.sendMessage(sc.getSocket(), sc.getWriter(), SmtpMessage.OK.getFullText());
            logger.trace("RCPT received");
        }else{
            sc.setCurrentState(SessionState.RCPT_TO_RECEIVED);
            SMTPUtils.updateAcceptableStates(sc.getValidNextStates(), SessionState.RCPT, SessionState.DATA);
            SMTPUtils.sendMessage(sc.getSocket(),sc.getWriter(), SmtpMessage.INVALID_RECIPIENT_SYNTAX.getFullText());
        }
    }
}
