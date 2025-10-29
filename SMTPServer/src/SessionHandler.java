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
    private boolean receiveData=false;
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

        SMTPUtils.sendMessage(socket,writer, SmtpMessage.GREET.getFullText());
        currentState = SessionState.INIT;
        validNextStates.add(SessionState.HELO);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                logger.log(Level.INFO, line);
                if (!receiveData) {
                    opCode = SMTPUtils.getOperation(line);

                    switch (opCode) {
                        case "HELO":
                            if (SMTPUtils.isValidNextState(SessionState.HELO, validNextStates)) {
                                handleHELO(socket,writer,clientDomain,currentState,line);
                            }else{
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                            }
                            break;
                        case "MAIL":
                            if (!SMTPUtils.isValidNextState(SessionState.MAIL, validNextStates)) {
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                                break;
                            }
                            String from = SMTPUtils.getEmailAddress(line);
                            smtpEmail.setFrom(from);
                            currentState = SessionState.MAIL_FROM_RECEIVED;
                            SMTPUtils.updateAcceptableStates(validNextStates, SessionState.RCPT);
                            SMTPUtils.sendMessage(socket, writer, SmtpMessage.OK.getFullText());

                            logger.log(Level.INFO, "MAIL received");
                            break;
                        case "RCPT":
                            if (!SMTPUtils.isValidNextState(SessionState.RCPT, validNextStates)) {
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                                break;
                            }
                            String to = SMTPUtils.getEmailAddress(line);
                            smtpEmail.addToRecipientList(to);
                            currentState = SessionState.RCPT_TO_RECEIVED;
                            SMTPUtils.updateAcceptableStates(validNextStates, SessionState.RCPT, SessionState.DATA);
                            SMTPUtils.sendMessage(socket, writer, SmtpMessage.OK.getFullText());
                            logger.log(Level.INFO, "RCPT received");
                            break;
                        case "DATA":
                            if (!SMTPUtils.isValidNextState(SessionState.MAIL, validNextStates)) {
                                SMTPUtils.sendMessage(socket, writer, SmtpMessage.BAD_SEQUENCE.getFullText());
                                break;
                            }
                            SMTPUtils.updateAcceptableStates(validNextStates, SessionState.DATA_COMPLETE);
                            SMTPUtils.sendMessage(socket, writer, SmtpMessage.START_MAIL_INPUT.getFullText());

                            logger.log(Level.INFO, "DATA received");
                            break;
                        case "QUIT":
                            logger.log(Level.INFO, "QUIT received");
                            break;
                        default:
                            SMTPUtils.sendMessage(socket, writer, SmtpMessage.SYNTAX_ERROR.getFullText());
                            break;
                    }
                }
                if(receiveData){
                    System.out.println("receive data");
                    if(!line.equals("\r\n.\r\n")){
                        emailBody.append(line);
                    }
                    smtpEmail.setEmailMessage(emailBody.toString());
                    currentState = SessionState.DATA_COMPLETE;
                    SMTPUtils.sendMessage(socket, writer, SmtpMessage.MSG_RECEIVED.getFullText());
                    break;
                }
            }

        } catch (IOException e) {
            logger.log(Level.WARNING, "Error reading socket input", e);
        }
    }

    private void handleHELO(Socket s, BufferedWriter w, String clientDomain, SessionState currentState, String data){
        logger.log(Level.INFO, "HELO received");
        currentState = SessionState.HELO_RECEIVED;
        SMTPUtils.updateAcceptableStates(validNextStates, SessionState.MAIL);
        clientDomain = SMTPUtils.getData(data);
        SMTPUtils.sendMessage(socket, writer, SmtpMessage.OK.getFullText());
    }



}
