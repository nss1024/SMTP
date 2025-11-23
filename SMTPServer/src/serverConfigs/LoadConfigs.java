package serverConfigs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

public final class LoadConfigs {

    private static final Logger logger = LoggerFactory.getLogger(LoadConfigs.class);

    private LoadConfigs() {} // prevent instantiation

    public static ServerConfigs load(Path file) {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(file.toFile())) {
            props.load(fis);
            logger.info("Loaded config from: {}", file);

        } catch (Exception e) {
            throw new RuntimeException("Could not load config file: " + file, e);
        }

        return new ServerConfigs.Builder()
                .localDomains(Arrays.asList(props.getProperty("domains").split(",")))
                .port(Integer.parseInt(props.getProperty("smtp.port")))
                .serverThreadCount(Integer.parseInt(props.getProperty("server.threads")))
                .mdaThreadCount(Integer.parseInt(props.getProperty("mda.threads")))
                .resolverThreadCount(Integer.parseInt(props.getProperty("resolver.threads")))
                .failedConnectionsTolerance(Integer.parseInt(props.getProperty("smtp.failedConnectionsTolerance")))
                .mainSavePath(Paths.get(props.getProperty("savePath")))
                .relayPath(Paths.get(props.getProperty("relayPath")))
                .metaPath(Paths.get(props.getProperty("metaPath")))
                .build();
    }
}
