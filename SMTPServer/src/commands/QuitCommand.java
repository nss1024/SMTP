package commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SMTPUtils;
import utils.SmtpMessage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;

public class QuitCommand implements SmtpCommand{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void execute(SessionContext sc, String line) throws IOException {
        logger.trace("QUIT received");
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
                logger.warn("Failed to close BufferedWriter {}", e.getMessage());
            }
        }

    }
}
