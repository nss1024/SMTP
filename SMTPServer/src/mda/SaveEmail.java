/*This saves emails destined t local recipients.
 *It saves emails to disk
 */

package mda;

import commands.SMTPEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


public class SaveEmail implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
            logger.warn("Failed to save email to disk! {}",e.getMessage());
        }

    }
}
