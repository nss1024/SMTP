package commands;

import utils.SMTPUtils;
import utils.SessionState;
import utils.SmtpMessage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeloCommand implements SmtpCommand {
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private Socket socket;
    private BufferedWriter writer;
    private List<SessionState> validNextStates;
    private SessionState currentState;
    private String myDomain;
    private String clientDomain;




    public HeloCommand(Socket socket, BufferedWriter writer, List<SessionState> validNextStates, SessionState currentState, String myDomain){

        this.socket=socket;
        this.writer=writer;
        this.validNextStates=validNextStates;
        this.currentState=currentState;
        this.myDomain = myDomain;

    }

    @Override
    public void execute(String line) throws IOException {

        logger.log(Level.INFO, "HELO received");
        this.currentState = SessionState.HELO_RECEIVED;
        SMTPUtils.updateAcceptableStates(validNextStates, SessionState.MAIL);
        this.clientDomain = SMTPUtils.getData(line);
        SMTPUtils.sendMessage(socket, writer, SmtpMessage.OK.getFullText());

    }

    public String getClientDomain() {
        return clientDomain;
    }
}
