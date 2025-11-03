package commands;

import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RcptCommand implements SmtpCommand{
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    public void execute(SessionContext sc, String line) throws IOException {
        String to = SMTPUtils.getEmailAddress(line);
        sc.getSmtpEmail().addToRecipientList(to);
        sc.setCurrentState(SessionState.RCPT_TO_RECEIVED);
        SMTPUtils.updateAcceptableStates(sc.getValidNextStates(), SessionState.RCPT, SessionState.DATA);
        SMTPUtils.sendMessage(sc.getSocket(), sc.getWriter(), SmtpMessage.OK.getFullText());
        logger.log(Level.INFO, "RCPT received");
    }
}
