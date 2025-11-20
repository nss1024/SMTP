package mda;

import commands.SMTPEmail;
import resolver.Lookup;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MdaMain {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private ThreadPoolExecutor mdathreadPool;
    private int noOfthreads = 2;
    private Path savePath;
    private Path relayPath;
    private Path metaPath;
    private Lookup lookup;


    public MdaMain(){
        this.savePath= Paths.get("C:", "dev", "FileStore", "local");
        this.relayPath= Paths.get("C:", "dev", "FileStore", "relay");
        this.metaPath= Paths.get("C:", "dev", "FileStore", "relay","meta");
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

    public void setLookup() {
        this.lookup = lookup;
    }

    public void relayEmail(SMTPEmail email, EmailMetaData emailMetaData){
        mdathreadPool.submit(new RelayEmail(email,emailMetaData,lookup,relayPath,metaPath));
    }

}
