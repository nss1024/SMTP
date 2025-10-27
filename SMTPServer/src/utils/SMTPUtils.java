package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
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

    public static String getOperation(String line) {
        if (line == null || line.trim().isEmpty()) return "";
        return line.split(" ")[0].toUpperCase();
    }

    public static String getData(String line) {
        if (line == null || line.trim().isEmpty()) return "";
        String[] parts = line.split(" ", 2);
        if (parts.length < 2) return "";
        String result = parts[1].trim();
        result = result.replaceAll("(?i)^TO:", "")
                .replaceAll("(?i)^FROM:", "")
                .trim();
        return result;
    }

    public static String getEmailAddress(String line) {
        int start = line.indexOf('<');
        int end = line.indexOf('>');
        if (start != -1 && end != -1 && end > start) {
            return line.substring(start + 1, end);
        }
        return "";
    }

    public static List<SessionState> updateAcceptableStates(List<SessionState> current, SessionState... newStates ){
        current.clear();
        Collections.addAll(current, newStates);
        return current;
    }

    public static boolean isValidNextState(SessionState s, List<SessionState> validNextStates){
        return validNextStates.contains(s);
    }

}
