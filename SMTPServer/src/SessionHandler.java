import commands.CommandRegistry;
import commands.SessionContext;
import commands.SmtpCommand;
import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;
import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionHandler implements Runnable{
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private Socket socket=null;
    private BufferedWriter writer;
    private SessionContext sc;
    private String myDomain = "myserver.com";
    private String opCode;
    private StringBuilder emailBody=new StringBuilder();


    SessionHandler(Socket s){
        this.socket=s;
    }

    @Override
    public void run() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sc = new SessionContext(socket,writer,myDomain);
        SMTPUtils.sendMessage(socket,writer, SmtpMessage.GREET.getFullText());
        sc.setCurrentState(SessionState.INIT);
        sc.getValidNextStates().add(SessionState.HELO);


        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                logger.log(Level.INFO, line);
                if(sc.isReceivingData()){
                    if(!line.equals(".")){
                        emailBody.append(line).append("\r\n");
                    }else {
                        sc.getSmtpEmail().setEmailMessage(emailBody.toString());
                        sc.setCurrentState(SessionState.DATA_COMPLETE);
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
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error reading socket input", e);
        }




    }
}
