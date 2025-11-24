/* This class is responsible for saving emails that are to be relayed to disk.
 * It launched the MX and domain lookups for each recipient domain that arrived in an RCPT message
 * It saves emails and metadata relating to those email in separate folders, but with the same name to make
 * retrieval easier.
 *
 */
package mda;

import commands.SMTPEmail;
import commands.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resolver.Lookup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;


public class RelayEmail implements Runnable{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SMTPEmail smtpEmail;
    private String fileName;
    private String metaName;
    private  String fileUudi;
    private final String fileExtension = ".eml";
    private final String metaExtension = ".meta";
    private Path relayDirectory;
    private Path metaDirectory;
    private  EmailMetaData emailMetaData;
    private Lookup lookup;
    private SessionContext sc;

    RelayEmail(SessionContext context,Lookup lookup, Path relayDirectory, Path metaDirectory){
        this.smtpEmail=context.getSmtpEmail();
        this.relayDirectory=relayDirectory;
        this.metaDirectory=metaDirectory;
        this.lookup=lookup;
    }

    @Override
    public void run() {
        fileUudi = UUID.randomUUID().toString();
        fileName = fileUudi +fileExtension;
        metaName = fileUudi + metaExtension;
        Path fullPath = relayDirectory.resolve(fileName);
        Path metaPath = metaDirectory.resolve(metaName);

        try{
            Files.write(fullPath,smtpEmail.toEmlFormat().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.warn("Failed to save email to disk! {}",e.getMessage());
        }

        try{
            emailMetaData=new EmailMetaData(fileUudi);
            getDomains();
            Files.write(metaPath,emailMetaData.toFileFormat().getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            logger.warn("Failed to save meta data to disk! {}",e.getMessage());
        }
    }

    private void getDomains(){
        List<String> domains = smtpEmail.getRecipientDomains();
        for (String dom : domains) {
            Future<DomainData> future = lookup.lookupMXRecord(dom);
            try {
                DomainData dd = future.get();
                emailMetaData.addDomain(dd);
            } catch (Exception e) {
                logger.warn("Failed to get domains {}",e.getMessage());
            }
        }
    }

}
