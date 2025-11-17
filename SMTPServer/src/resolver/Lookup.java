/*Runs a thread pool to perform   
 *
 */

package resolver;

import mda.DomainData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    public void lookupMXRecord(String domainName){
        if(lookupThreadPool!=null){
            lookupThreadPool.submit(new MXLookup(new DomainData(),domainName));
        }
    }

    public void lookupDNSRecord(String domainName){
        if(lookupThreadPool!=null){
            lookupThreadPool.submit(new DNSLookup(domainName));
        }
    }


}
