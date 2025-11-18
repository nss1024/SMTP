/*This class holds all data relating to a domain name, that is
 *all data relating to gmail.com for recipient john.doe1@gmail.com
 * including all mx records.
 */

package mda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainData {
    private String domainName;
    private List<MxRecordData> mxRecordDataList=new ArrayList<>();

    public DomainData(){}

    public DomainData(String name, MxRecordData... mxRecordData){
        this.domainName=name;
        this.mxRecordDataList.addAll(Arrays.asList(mxRecordData));
    }

    public String getDomainName() {
        return domainName;
    }

    public List<MxRecordData> getMxRecordDataList() {
        return mxRecordDataList;
    }

    public String getStringMxRecords(){
        StringBuilder sb = new StringBuilder();
        for(MxRecordData m:mxRecordDataList){
            sb.append("["+m.getHostname()+" "+m.getStringAddress()+"]"+"\n");

        }
        return sb.toString();
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void addMxRecord(MxRecordData mxRecord){
        mxRecordDataList.add(mxRecord);
    }

    public MxRecordData getMxRecordDataByName(String domainName){
        for(MxRecordData m : mxRecordDataList){
            if (m.getHostname().equalsIgnoreCase(domainName)){
                return m;
            }
        }
        return null;
    }

}


