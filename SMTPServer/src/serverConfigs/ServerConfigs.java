package serverConfigs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ServerConfigs {

    private final List<String> localDomains;
    private final int port;
    private final int serverThreadCount;
    private final int mdaThreadCount;
    private final int resolverThreadCount;
    private final int failedConnectionsTolerance;
    private final Path mainSavePath;
    private final Path relayPath;
    private final Path metaPath;

    private ServerConfigs(Builder b) {
        this.localDomains = b.localDomains;
        this.port = b.port;
        this.serverThreadCount = b.serverThreadCount;
        this.mdaThreadCount = b.mdaThreadCount;
        this.resolverThreadCount = b.resolverThreadCount;
        this.failedConnectionsTolerance = b.failedConnectionsTolerance;
        this.mainSavePath = b.mainSavePath;
        this.relayPath = b.relayPath;
        this.metaPath = b.metaPath;
    }

    public List<String> getLocalDomains() { return localDomains; }
    public int getPort() { return port; }
    public int getServerThreadCount() { return serverThreadCount; }
    public int getMdaThreadCount() { return mdaThreadCount; }
    public int getResolverThreadCount() { return resolverThreadCount; }
    public int getFailedConnectionsTolerance() { return failedConnectionsTolerance; }
    public Path getMainSavePath() { return mainSavePath; }
    public Path getRelayPath() { return relayPath; }
    public Path getMetaPath() { return metaPath; }

    public static class Builder {
        private List<String> localDomains;
        private int port;
        private int serverThreadCount;
        private int mdaThreadCount;
        private int resolverThreadCount;
        private int failedConnectionsTolerance;
        private Path mainSavePath;
        private Path relayPath;
        private Path metaPath;

        public Builder localDomains(List<String> d) {
            this.localDomains = new ArrayList<>();
            for(String s:d){
                this.localDomains.add(s.trim());
            }
            return this;
        }
        public Builder port(int port) { this.port = port; return this; }
        public Builder serverThreadCount(int n) { this.serverThreadCount = n; return this; }
        public Builder mdaThreadCount(int n) { this.mdaThreadCount = n; return this; }
        public Builder resolverThreadCount(int n) { this.resolverThreadCount = n; return this; }
        public Builder failedConnectionsTolerance(int n) { this.failedConnectionsTolerance = n; return this; }
        public Builder mainSavePath(Path p) { this.mainSavePath = p; return this; }
        public Builder relayPath(Path p) { this.relayPath = p; return this; }
        public Builder metaPath(Path p) { this.metaPath = p; return this; }

        public ServerConfigs build() {
            return new ServerConfigs(this);
        }
    }
}
