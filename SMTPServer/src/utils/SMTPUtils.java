package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMTPUtils {
    private static final Logger logger = Logger.getLogger(SMTPUtils.class.getSimpleName());
    private SMTPUtils(){}

    public static void sendMessage(Socket s, BufferedWriter w, String message){
      try{
        w.write(message);
        w.flush();
      }catch (IOException e) {
          logger.log(Level.WARNING,"Failed to write message to socket! "+message);
          throw new RuntimeException(e);
      }
    }

}
