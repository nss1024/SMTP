/*Runs a thread pool to perform
 *
 */

package resolver;

import mda.DomainData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class Lookup {
    private int numberOfthreads = 4;
    private ExecutorService lookupThreadPool;
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    public Lookup(){

    }

    public void start(){
        lookupThreadPool= Executors.newFixedThreadPool(numberOfthreads);
    }

    public Future<DomainData> lookupMXRecord(String domainName){
        if(lookupThreadPool!=null){
            return lookupThreadPool.submit(new DomainResolver(domainName));
        }
        return null;
    }

    public void shutdown() {
        if (lookupThreadPool != null) {
            lookupThreadPool.shutdown();
        }
    }
}
