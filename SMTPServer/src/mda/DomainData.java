package mda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomainData {
    private String domainName;
    private List<MxRecordData> mxRecordDataList=new ArrayList<>();

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

}


