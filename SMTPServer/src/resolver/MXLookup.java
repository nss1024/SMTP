package resolver;

import mda.DomainData;
import mda.MxRecordData;
import org.xbill.DNS.*;
import org.xbill.DNS.Lookup;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MXLookup implements Runnable{
    DomainData domainData;
    String domainName;
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    MXLookup(DomainData domainData,String domainName){
        this.domainData=domainData;
        this.domainName=domainName;
    }

    @Override
    public void run() {
        org.xbill.DNS.Lookup lookup = null;
        try {
            lookup = new Lookup(domainName, Type.MX);
        } catch (TextParseException e) {
            logger.log(Level.SEVERE,"Could not look up domain!");
        }
        Record[] records = lookup.run();

        domainData.setDomainName(domainName);

        if(records != null) {
            for (Record r : records) {
                MXRecord mx = (MXRecord) r;
                domainData.addMxRecord(new MxRecordData(mx.getTarget().toString(true),mx.getPriority()));
            }
        }
    }
}
