/*The starting pont of the application logic,
 * This class is responsible for accepting inbound connections and starting the sate handler class.
 *
 *
 */

import mda.MdaMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverConfigs.ServerConfigs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
;

public class ServerMain {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private int listeningPort=0;
    private ServerSocket serverSocket = null;
    ThreadPoolExecutor tp = null;
    private int failedConnectionsCounter=0;
    private  int FAILED_CONNECTIONS_TOLERANCE = 0;
    private int numberOfThreads =0;
    MdaMain mdaMain;
    ServerConfigs sc;

    ServerMain(MdaMain mdaMain, ServerConfigs sc){

        this.mdaMain=mdaMain;
        this.sc=sc;
        loadConfigs(sc);
    }


    private void startServer(){
        try {
            serverSocket=new ServerSocket(this.listeningPort);
            logger.info("Server listening on port {}",this.listeningPort);
        } catch (IOException e) {
            logger.error("Failed to start a ServerSocke instance!");
            throw new RuntimeException(e);
        }
    }

    public void stopServer(){
        if(serverSocket!=null && !serverSocket.isClosed()){
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.warn("Failed to close ServerSocket!");
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
                    tp.submit(new SessionHandler(soc,mdaMain,sc));

                } catch (IOException e) {
                    logger.warn("Failed to accept connection request!");
                    failedConnectionsCounter++;
                    if(failedConnectionsCounter==FAILED_CONNECTIONS_TOLERANCE){
                        stopServer();
                    }
                }

            }
        }).start();
    }

    private void loadConfigs(ServerConfigs sc){
        listeningPort=sc.getPort();
        numberOfThreads=sc.getServerThreadCount();
        FAILED_CONNECTIONS_TOLERANCE=sc.getFailedConnectionsTolerance();
    }

}
