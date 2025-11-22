package mda;

import commands.SMTPEmail;
import commands.SessionContext;
import resolver.Lookup;
import serverConfigs.ServerConfigs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MdaMain {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private ThreadPoolExecutor mdathreadPool;
    private int noOfthreads = 0;
    private final Path savePath;
    private final Path relayPath;
    private final Path metaPath;
    private Lookup lookup;
    private ServerConfigs sc;
    SessionContext context;


    public MdaMain(ServerConfigs sc){
        this.sc=sc;
        this.savePath= sc.getMainSavePath();
        this.relayPath= sc.getRelayPath();
        this.metaPath= sc.getMetaPath();
        this.noOfthreads=sc.getMdaThreadCount();
    }


    private void startThreadPool(int threadCount){
        mdathreadPool= new ThreadPoolExecutor(threadCount,
                threadCount,
                30L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public void start(){
        startThreadPool(this.noOfthreads);
        lookup=new Lookup();
        lookup.start();
    }

    public void saveEmail(SMTPEmail email){
            mdathreadPool.submit(new SaveEmail(email,savePath));
    }

    public void setLookup(Lookup lookup) {
        this.lookup = lookup;
    }

    public void relayEmail(SessionContext context){
        mdathreadPool.submit(new RelayEmail(context,lookup,relayPath,metaPath));
    }

}
