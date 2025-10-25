import utils.SMTPUtils;
import utils.SmtpMessage;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionHandler implements Runnable{
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private Socket socket=null;
    BufferedWriter writer = null;

    SessionHandler(Socket s){
        this.socket=s;
    }

    @Override
    public void run() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SMTPUtils.sendMessage(socket,writer, SmtpMessage.GREET.getFullText());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {

            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error reading socket input", e);
        }



    }
}
