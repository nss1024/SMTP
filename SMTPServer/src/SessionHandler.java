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
    private String clientDomain;
    private String myDomain = "myserver.com";
    private boolean isReceivingData =false;
    private String opCode;
    private StringBuilder emailBody=new StringBuilder();
    private boolean quit = false;

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
                logger.log(Level.INFO, line);
                if (!isReceivingData) {
                    opCode = SMTPUtils.getOperation(line);

                    switch (opCode) {
                        case "HELO":
                            if (SMTPUtils.isValidNextState(SessionState.HELO, validNextStates)) {
                                handleHelo(socket,writer,clientDomain,currentState,line);
                            }else{
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                            }
                            break;
                        case "MAIL":
                            if (SMTPUtils.isValidNextState(SessionState.MAIL, validNextStates)) {
                                handleMail(socket,writer,smtpEmail,currentState,line);
                            }else{
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                            }
                            break;
                        case "RCPT":
                            if (SMTPUtils.isValidNextState(SessionState.RCPT, validNextStates)) {
                                handleRcpt(socket,writer,smtpEmail,currentState,line);
                            }else{
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                            }
                            break;
                        case "DATA":
                            if (SMTPUtils.isValidNextState(SessionState.DATA, validNextStates)) {
                                logger.log(Level.INFO, "DATA received");
                                SMTPUtils.updateAcceptableStates(validNextStates, SessionState.DATA_COMPLETE);
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.START_MAIL_INPUT.getFullText());
                                isReceivingData =true;
                            }else{
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                            }
                            break;
                        case "QUIT":
                            if (SMTPUtils.isValidNextState(SessionState.QUIT, validNextStates)) {
                                logger.log(Level.INFO, "QUIT received");
                                SMTPUtils.sendMessage(socket,writer,SmtpMessage.CLOSING_TRANSMISSION.getFullText());
                                closeWriter(writer);
                                quit=true;

                            }
                            else{
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                            }

                            break;
                        default:
                            SMTPUtils.sendMessage(socket, writer, SmtpMessage.SYNTAX_ERROR.getFullText());
                            break;
                    }
                }
                if(isReceivingData){
                    System.out.println("receive data");
                    if(!line.equals(".")){
                        emailBody.append(line).append("\r\n");
                    }
                    smtpEmail.setEmailMessage(emailBody.toString());
                    currentState = SessionState.DATA_COMPLETE;
                    SMTPUtils.sendMessage(socket, writer, SmtpMessage.MSG_RECEIVED.getFullText());
                    isReceivingData =false;
                }
                if(quit){
                    return;
                }
            }

        } catch (IOException e) {
            logger.log(Level.WARNING, "Error reading socket input", e);
        }
    }

    private void handleHelo(Socket s, BufferedWriter w, String clientDomain, SessionState currentState, String data){
        logger.log(Level.INFO, "HELO received");
        this.currentState = SessionState.HELO_RECEIVED;
        SMTPUtils.updateAcceptableStates(validNextStates, SessionState.MAIL);
        this.clientDomain = SMTPUtils.getData(data);
        SMTPUtils.sendMessage(socket, writer, SmtpMessage.OK.getFullText());
    }

    private void handleMail(Socket s, BufferedWriter w, SMTPEmail smtpEmail, SessionState currentState, String data){
        logger.log(Level.INFO, "MAIL received");
        String from = SMTPUtils.getEmailAddress(data);
        smtpEmail.setFrom(from);
        this.currentState = SessionState.MAIL_FROM_RECEIVED;
        SMTPUtils.updateAcceptableStates(validNextStates, SessionState.RCPT);
        SMTPUtils.sendMessage(socket, writer, SmtpMessage.OK.getFullText());
    }

    private void handleRcpt(Socket s, BufferedWriter w, SMTPEmail smtpEmail, SessionState currentState, String data){
        String to = SMTPUtils.getEmailAddress(data);
        smtpEmail.addToRecipientList(to);
        this.currentState = SessionState.RCPT_TO_RECEIVED;
        SMTPUtils.updateAcceptableStates(validNextStates, SessionState.RCPT, SessionState.DATA);
        SMTPUtils.sendMessage(socket, writer, SmtpMessage.OK.getFullText());
        logger.log(Level.INFO, "RCPT received");
    }

    private void closeWriter(BufferedWriter writer){
        if (writer != null) {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to close BufferedWriter", e);
            }
        }

    }

}
