import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionHandler implements Runnable{
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private Socket socket=null;
    BufferedWriter writer = null;
    private SessionState currentState;
    private List<SessionState> validNextStates=new ArrayList<>();
    private SMTPEmail smtpEmail = new SMTPEmail();

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

        SMTPUtils.sendMessage(socket,writer, SmtpMessage.GREET.getFullText());
        currentState = SessionState.INIT;
        validNextStates.add(SessionState.HELO);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                logger.log(Level.INFO,line);
                String opCode = SMTPUtils.getOperation(line);

                switch(opCode){
                    case "HELO":
                        if(!SMTPUtils.isValidNextState(SessionState.HELO,validNextStates)){
                            SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                        }
                        SMTPUtils.sendMessage(socket, writer, SmtpMessage.OK.getFullText());
                        currentState=SessionState.HELO_RECEIVED;
                        SMTPUtils.updateAcceptableStates(validNextStates,SessionState.MAIL);
                    break;
                    case "MAIL":
                        if(!SMTPUtils.isValidNextState(SessionState.MAIL,validNextStates)){
                            SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                        }
                        String from = SMTPUtils.getEmailAddress(line);
                        smtpEmail.setFrom(from);
                        currentState=SessionState.MAIL_FROM_RECEIVED;
                        SMTPUtils.updateAcceptableStates(validNextStates,SessionState.RCPT);
                        logger.log(Level.INFO,"MAIL received");
                    break;
                    case "RCPT":logger.log(Level.INFO,"RCPT received");
                    break;
                    case "DATA":logger.log(Level.INFO,"DATA received");
                    break;
                    case "QUIT":logger.log(Level.INFO,"QUIT received");
                    break;
                    default: SMTPUtils.sendMessage(socket, writer, SmtpMessage.SYNTAX_ERROR.getFullText());
                        break;
                }



            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error reading socket input", e);
        }
    }



}
