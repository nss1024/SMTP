package mda;

import commands.SMTPEmail;
import resolver.Lookup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelayEmail implements Runnable{
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private SMTPEmail smtpEmail;
    private String fileName;
    private String metaName;
    private  String fileUudi;
    private final String fileExtension = ".eml";
    private final String metaExtension = ".meta";
    private Path relayDirectory;
    private Path metaDirectory;
    private  EmailMetaData emailMetaData;
    private Lookup lookup;

    RelayEmail(SMTPEmail email, EmailMetaData emailMetaData,Lookup lookup, Path relayDirectory, Path metaDirectiry){
        this.smtpEmail=email;
        this.relayDirectory=relayDirectory;
        this.emailMetaData = emailMetaData;
        this.metaDirectory=metaDirectiry;
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
            Files.write(metaPath,emailMetaData.toFileFormat().getBytes(StandardCharsets.UTF_8));


        } catch (IOException e) {
            logger.log(Level.WARNING,"Failed to save email to disk! "+e.getMessage());
        }

        try{

            Files.write(metaPath,emailMetaData.toFileFormat().getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            logger.log(Level.WARNING,"Failed to save meta data to disk! "+e.getMessage());
        }
    }

    private void getDomains(){
        List<String> domains = smtpEmail.getRecipientDomains();
        for(String dom:domains){
            emailMetaData.addDomain((DomainData) lookup.lookupMXRecord(dom));
        }
    }

}
