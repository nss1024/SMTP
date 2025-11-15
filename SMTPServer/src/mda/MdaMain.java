package mda;

import commands.SMTPEmail;

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


    public MdaMain(){
        this.savePath= Paths.get("C:", "dev", "FileStore", "local");
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
    }

    public void saveEmail(SMTPEmail email){
            mdathreadPool.submit(new SaveEmail(email,savePath));
    }

}
