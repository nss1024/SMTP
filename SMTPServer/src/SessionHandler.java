import commands.CommandRegistry;
import commands.SessionContext;
import commands.SmtpCommand;
import mda.MdaMain;
import serverConfigs.ServerConfigs;
import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionHandler implements Runnable{
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private Socket socket=null;
    private BufferedWriter writer;
    private BufferedReader reader;
    private SessionContext sc;
    private String myDomain = "myserver.com";
    private List<String> localDomains;
    private String opCode;
    private StringBuilder emailBody=new StringBuilder();
    private MdaMain mdaMain;
    private ServerConfigs conf;


    SessionHandler(Socket s,MdaMain mdaMain, ServerConfigs conf){
        this.socket=s;
        this.mdaMain=mdaMain;
        this.conf=conf;
        this.localDomains=conf.getLocalDomains();
    }

    @Override
    public void run() {

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sc = new SessionContext(socket,reader,writer,localDomains);
        SMTPUtils.sendMessage(socket,writer, SmtpMessage.GREET.getFullText());
        sc.setCurrentState(SessionState.INIT);
        sc.getValidNextStates().add(SessionState.HELO);


        try {
            String line;
            while ((line = sc.getReader().readLine()) != null) {
                logger.log(Level.INFO, line);
                if(sc.isReceivingData()){
                    if(!line.equals(".")){
                        emailBody.append(line).append("\r\n");
                    }else {
                        sc.getSmtpEmail().setEmailMessage(emailBody.toString());
                        sc.setCurrentState(SessionState.DATA_COMPLETE);

                        List<String> allRecipients = new ArrayList<>(sc.getSmtpEmail().getToList());
                        List<String> externalRecipients = new ArrayList<>();
                        System.out.println(Arrays.toString(localDomains.toArray()));
                        for (String rcpt : allRecipients) {
                                if (localDomains.contains(rcpt.split("@")[1].trim())) {
                                    mdaMain.saveEmail(sc.getSmtpEmail()); // local delivery per address
                                } else {
                                    externalRecipients.add(rcpt);
                                }
                        }

                        if (!externalRecipients.isEmpty()) {
                            sc.getSmtpEmail().setToList(externalRecipients); // remove local addresses
                            System.out.println(sc.getEmailMetaData());
                            mdaMain.relayEmail(sc);
                        }

                        SMTPUtils.sendMessage(socket, writer, SmtpMessage.MSG_RECEIVED.getFullText());
                        sc.setReceivingData(false);
                    }
                    continue;
                }

                if (!sc.isReceivingData() && line.isEmpty()) {
                    continue;
                }

                    opCode = SMTPUtils.getOperation(line).toUpperCase(Locale.ROOT).trim();

                SmtpCommand command = CommandRegistry.INSTANCE.getCommand(opCode);
                if (command != null) {
                    command.execute(sc,line);
                } else {
                    SMTPUtils.sendMessage(socket, writer, SmtpMessage.SYNTAX_ERROR.getFullText());
                }
                if(sc.isQuitting()){
                    return;
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error reading socket input", e);
        } finally{
            try {
                sc.getReader().close();
                socket.close();
            }catch(IOException e){
                logger.log(Level.WARNING,"Failed to close reader / socket!");
            }
        }
    }


}
