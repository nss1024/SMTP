package commands;

import utils.SMTPUtils;
import utils.SmtpMessage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuitCommand implements SmtpCommand{

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());


    @Override
    public void execute(SessionContext sc, String line) throws IOException {
        logger.log(Level.INFO, "QUIT received");
        SMTPUtils.sendMessage(sc.getSocket(),sc.getWriter(), SmtpMessage.CLOSING_TRANSMISSION.getFullText());
        closeWriter(sc.getWriter());
        sc.setQuitting(true);
    }

    private void closeWriter(BufferedWriter writer){
        if (writer != null) {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to close BufferedWriter", e);
            }
        }

    }
}
