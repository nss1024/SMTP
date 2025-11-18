/*This class holds all data relating to a single mx record
 *That is the domain name itself i.e. alt2.gmail-smtp-in.l.google.com (without the trailing dot)
 * as well as all addresses retrieved for that record i.e.  142.250.147.26
 * A number of these records is held in a DomainData
 */

package mda;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class MxRecordData {
    private final int smtpPort=25;
    private String hostname;
    private int priority;
    private List<InetSocketAddress> addresses=new ArrayList<>(); // each IP:25

    public MxRecordData(String hostname, int priority){
        this.hostname=hostname;
        this.priority=priority;


    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPriority() {
        return priority;
    }

    public List<InetSocketAddress> getAddresses() {
        return addresses;
    }

    public String getStringAddress(){
        StringBuilder sb = new StringBuilder();
        for(InetSocketAddress i:addresses){
            sb.append(i.getAddress().getHostAddress());
            sb.append("\t");

        }
        return sb.toString();
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void addInetSockeAddress(String address){
        addresses.add(new InetSocketAddress(address,smtpPort));
    }
}
