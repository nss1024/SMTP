import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private int listeningPort=0;
    private ServerSocket serverSocket = null;
    private int failedConnectionsCounter=0;
    private final int FAILED_CONNECTIONS_TOLERANCE = 10;

    ServerMain(int port){
        this.listeningPort=port;
    }

    private void startServer(){
        try {
            serverSocket=new ServerSocket(this.listeningPort);
            logger.log(Level.INFO,"Server listening on port "+this.listeningPort);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Failed to start a ServerSocke instance!");
            throw new RuntimeException(e);
        }
    }

    public void stopServer(){
        if(serverSocket!=null && !serverSocket.isClosed()){
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.log(Level.WARNING,"Failed to close ServerSocket!");
            }
        }
    }

    public void start(){
        startServer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket soc = serverSocket.accept();

                } catch (IOException e) {
                    logger.log(Level.WARNING,"Failed to accept connection request!" );
                    failedConnectionsCounter++;
                    if(failedConnectionsCounter==FAILED_CONNECTIONS_TOLERANCE){
                        stopServer();
                    }
                }

            }
        }).start();
    }

}
