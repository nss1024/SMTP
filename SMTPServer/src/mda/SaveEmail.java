package mda;

import commands.SMTPEmail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveEmail implements Runnable {

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private SMTPEmail smtpEmail;
    private String fileName;
    private final String fileExtension = ".eml";
    private Path saveDirectory;

    SaveEmail(SMTPEmail email, Path saveDirectory){
        this.smtpEmail=email;
        this.saveDirectory=saveDirectory;
    }

    @Override
    public void run() {
        fileName = UUID.randomUUID() +fileExtension;
        Path fullPath = saveDirectory.resolve(fileName);

        try{
            Files.write(fullPath,smtpEmail.toEmlFormat().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.log(Level.WARNING,"Failed to save email to disk! "+e.getMessage());
        }

    }
}
