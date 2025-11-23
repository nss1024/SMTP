import commands.CommandRegistry;
import commands.SessionContext;
import commands.SmtpCommand;
import mda.MdaMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverConfigs.ServerConfigs;
import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SessionHandler implements Runnable{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Socket socket=null;
    private BufferedWriter writer;
    private BufferedReader reader;
    private SessionContext sc;
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
                logger.trace(line);
                if(sc.isReceivingData()){
                    if(!line.equals(".")){
                        emailBody.append(line).append("\r\n");
                    }else {
                        sc.getSmtpEmail().setEmailMessage(emailBody.toString());
                        sc.setCurrentState(SessionState.DATA_COMPLETE);

                        List<String> allRecipients = new ArrayList<>(sc.getSmtpEmail().getToList());
                        List<String> externalRecipients = new ArrayList<>();
                        for (String rcpt : allRecipients) {
                                if (localDomains.contains(rcpt.split("@")[1].trim())) {
                                    mdaMain.saveEmail(sc.getSmtpEmail()); // local delivery per address
                                } else {
                                    externalRecipients.add(rcpt);
                                }
                        }

                        if (!externalRecipients.isEmpty()) {
                            sc.getSmtpEmail().setToList(externalRecipients);// remove local addresses
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
            logger.error("Error reading socket input {}", e.getMessage());
        } finally{
            try {
                sc.getReader().close();
                socket.close();
            }catch(IOException e){
                logger.warn("Failed to close reader / socket! {}",e.getMessage());
            }
        }
    }


}
