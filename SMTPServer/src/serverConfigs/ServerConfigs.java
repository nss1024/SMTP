package serverConfigs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ServerConfigs {

    private List<String> localDomains;
    private int port;
    private int serverThreadCount;
    private int mdaThreadCount;
    private int resolverThreadCount;
    private int failedConnectionsTolerance;
    private Path mainSavePath;
    private Path relayPath;
    private Path metaPath;

    public ServerConfigs(){}

    public List<String> getLocalDomains() {
        return localDomains;
    }

    public void setLocalDomains(List<String> localDomain) {
        this.localDomains = localDomain;
    }

    public void addLocalDomain(String domain){
        if(localDomains==null){
            localDomains=new ArrayList<>();
        }
        localDomains.add(domain);
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getServerThreadCount() {
        return serverThreadCount;
    }

    public void setServerThreadCount(int serverThreadCount) {
        this.serverThreadCount = serverThreadCount;
    }

    public int getMdaThreadCount() {
        return mdaThreadCount;
    }

    public void setMdaThreadCount(int mdaThreadCount) {
        this.mdaThreadCount = mdaThreadCount;
    }

    public int getResolverThreadCount() {
        return resolverThreadCount;
    }

    public void setResolverThreadCount(int resolverThreadCount) {
        this.resolverThreadCount = resolverThreadCount;
    }

    public int getFailedConnectionsTolerance() {
        return failedConnectionsTolerance;
    }

    public void setFailedConnectionsTolerance(int failedConnectionsTolerance) {
        this.failedConnectionsTolerance = failedConnectionsTolerance;
    }

    public Path getMainSavePath() {
        return mainSavePath;
    }

    public void setMainSavePath(String savePath) {
        this.mainSavePath= Paths.get(savePath);
    }

    public void setMainSavePath(String... savePath) {
        this.mainSavePath= Paths.get(savePath);
    }

    public Path getRelayPath() {
        return relayPath;
    }

    public void setRelayPath(Path relayPath) {
        this.relayPath = relayPath;
    }

    public Path getMetaPath() {
        return metaPath;
    }

    public void setMetaPath(Path metaPath) {
        this.metaPath = metaPath;
    }
}
