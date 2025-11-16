package mda;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailMetaData {
    private String emailId;
    private List<DomainData> destinations=new ArrayList<>();


    public EmailMetaData( String emailId,DomainData... domainData){
        this.emailId=emailId;
        this.destinations.addAll(Arrays.asList(domainData));
    }

    public List<DomainData> getDestinations() {
        return destinations;
    }

    public String toFileFormat(){
        StringBuilder sb = new StringBuilder();
        sb.append("{"+emailId+" \n");
        for(DomainData d:destinations){
            sb.append("\t"+d.getDomainName()+"\n");
            sb.append(d.getStringMxRecords());

        }
        sb.append("}");
        return sb.toString();
    }

}
