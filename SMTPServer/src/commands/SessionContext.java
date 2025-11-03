package commands;

import utils.SessionState;

import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class SessionContext {

    private Socket socket;
    private BufferedWriter writer;
    private List<SessionState> validNextStates;
    private SessionState currentState;
    private String myDomain;
    private String clientDomain;
    private SMTPEmail smtpEmail;
    private boolean isReceivingData;
    private boolean isQuitting;

    private SessionContext(){}

    public SessionContext(Socket socket, BufferedWriter writer, String myDomain) {
        this.socket = socket;
        this.writer = writer;
        this.myDomain = myDomain;
        this.isReceivingData=false;
        this.isQuitting=false;
        this.smtpEmail = new SMTPEmail();
        validNextStates=new ArrayList<>();
    }

   public List<SessionState> getValidNextStates() {
        return validNextStates;
    }

    public void setValidNextStates(List<SessionState> validNextStates) {
        this.validNextStates = validNextStates;
    }

    public SessionState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(SessionState currentState) {
        this.currentState = currentState;
    }

    public String getClientDomain() {
        return clientDomain;
    }

    public void setClientDomain(String clientDomain) {
        this.clientDomain = clientDomain;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public String getMyDomain() {
        return myDomain;
    }

    public SMTPEmail getSmtpEmail() {
        return smtpEmail;
    }

    public boolean isReceivingData() {
        return isReceivingData;
    }

    public void setReceivingData(boolean receivingData) {
        isReceivingData = receivingData;
    }

    public boolean isQuitting() {
        return isQuitting;
    }

    public void setQuitting(boolean quitting) {
        isQuitting = quitting;
    }
}
