package mda;

import commands.SMTPEmail;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MdaMain {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private ThreadPoolExecutor mdathreadPool;
    private int noOfthreads = 2;

    MdaMain(){

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

    }

}
