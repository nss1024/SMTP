/*Email Metadata holds all data relating to an email, not received in a message
 *Domain relevant data (MX records, DNS data) are saved here
 *When all metadata are gathered this class produces the contents of the metadata file that is to be written to disk.
 *
 */
package mda;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailMetaData {
    private String emailId;
    private List<DomainData> destinations=new ArrayList<>();

    public EmailMetaData( String emailId){
        this.emailId=emailId;

    }

    public EmailMetaData( String emailId,DomainData... domainData){
        this.emailId=emailId;
        this.destinations.addAll(Arrays.asList(domainData));
    }

    public List<DomainData> getDestinations() {
        return destinations;
    }

    public void addDomain(DomainData domainData){
        destinations.add(domainData);
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
