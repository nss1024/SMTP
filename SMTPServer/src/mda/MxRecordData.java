package mda;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MxRecordData {
    private final int smtpPort=25;
    private String hostname;
    private int priority;
    private List<InetSocketAddress> addresses=new ArrayList<>(); // each IP:25

    public MxRecordData(String hostname, int priority, InetSocketAddress... addr){
        this.hostname=hostname;
        this.priority=priority;
        this.addresses.addAll(Arrays.asList(addr));

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
            sb.append(i.getAddress());
            sb.append("\t");

        }
        return sb.toString();
    }

}
