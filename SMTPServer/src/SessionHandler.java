import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionHandler implements Runnable{
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private Socket socket=null;

    SessionHandler(Socket s){
        this.socket=s;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
               
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error reading socket input", e);
        }



    }
}
