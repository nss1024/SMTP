import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private int listeningPort=0;
    private ServerSocket serverSocket = null;
    ThreadPoolExecutor tp = null;
    private int failedConnectionsCounter=0;
    private final int FAILED_CONNECTIONS_TOLERANCE = 10;
    private int numberOfThreads =2;

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

    private void startThreadPool(int threadCount){
        tp= new ThreadPoolExecutor(threadCount,
                threadCount,
                30L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public void start(){
        startServer();
        startThreadPool(numberOfThreads);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket soc = serverSocket.accept();
                    tp.submit(new SessionHandler(soc));

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
