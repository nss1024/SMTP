/*Domain resolver uses dnsJava to perform the actual MX lookups and domain resolution for
 *domain names passed to it. It creates a domain data object that holds all information relating to a
 * specific domain recovered from a RCPT address (i.e. gmail.com)
 * The DomainData is populated here by MXRecordDdata, a data structure that holds data for every domain
 * returned by the MX lookup (i.e. gmail-smtp-in.l.google.com and IPs relating to that domain)
 *
 */
package resolver;

import mda.DomainData;
import mda.MxRecordData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.*;
import org.xbill.DNS.Lookup;

import java.util.concurrent.Callable;


public class DomainResolver implements Callable<DomainData> {
    String domanName;
    org.xbill.DNS.Lookup lookup = null;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DomainResolver(String domainName){
        this.domanName=domainName;

    }


    @Override
    public DomainData call() throws Exception {
        DomainData domainData = new DomainData();
        getMxRecords(domainData,domanName);
        for(MxRecordData mrd:domainData.getMxRecordDataList()){
            getDnsRecords(domainData,mrd.getHostname());
        }
        return domainData;
    }
    //get all MX records for a domain
    private void getMxRecords(DomainData dd, String domainName){

        try {
            lookup = new Lookup(domainName, Type.MX);
        } catch (TextParseException e) {
            logger.warn("Could not look up mx record!");
        }
        Record[] records = lookup.run();

        dd.setDomainName(domainName);

        if(records != null) {
            for (Record r : records) {
                MXRecord mx = (MXRecord) r;
                String host = mx.getTarget().toString(true);
                if (host.endsWith(".")) host = host.substring(0, host.length() - 1);
                dd.addMxRecord(new MxRecordData(host,mx.getPriority()));
            }
        }
    }

    //get all IP addresses for a given mx record, needs to be called for each mx record in DomainData
    private void getDnsRecords(DomainData dd,String mxDomainName) {
        if (!mxDomainName.isEmpty()) {
            try {

                lookup = new Lookup(mxDomainName, Type.A);

            } catch (TextParseException e) {
                logger.warn("Could not look up domain for : {} !",mxDomainName);
            }

            Record[] aRecords = lookup.run();
            if (aRecords != null) {
                for (Record rec : aRecords) {
                    ARecord a = (ARecord) rec;
                    dd.getMxRecordDataByName(mxDomainName).addInetSockeAddress(a.getAddress().getHostAddress());

                }
            }

        }
    }

}
