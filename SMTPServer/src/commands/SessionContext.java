package commands;

import utils.SessionState;

import java.io.BufferedWriter;
import java.net.Socket;
import java.util.List;

public class SessionContext {

    private Socket socket;
    private BufferedWriter writer;
    private List<SessionState> validNextStates;
    private SessionState currentState;
    private String myDomain;
    private String clientDomain;

    private SessionContext(){}

    public SessionContext(Socket socket, BufferedWriter writer, String myDomain) {
        this.socket = socket;
        this.writer = writer;
        this.myDomain = myDomain;
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
}
