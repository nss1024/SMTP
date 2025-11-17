package resolver;

import mda.DomainData;
import org.xbill.DNS.*;
import org.xbill.DNS.Lookup;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DNSLookup implements Runnable{
    private String domainName;
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private DomainData domainData;


    public DNSLookup(){}

    public DNSLookup(String domainName, DomainData domainData){
        this.domainName=domainName;
    }



    @Override
    public void run() {
        org.xbill.DNS.Lookup lookup = null;
        try {
            lookup = new Lookup(domainName, Type.A);
        } catch (TextParseException e) {
            logger.log(Level.SEVERE,"Could not look up domain!");
        }
        Record[] records = lookup.run();

        Record[] aRecords = lookup.run();
        if (aRecords != null) {
            for (Record rec : aRecords) {
                ARecord a = (ARecord) rec;
                domainData.getMxRecordDataByName(domainName).addInetSockeAddress(a.getAddress().getHostName());
                //addresses.add(a.getAddress());
            }
        }
    }
}
