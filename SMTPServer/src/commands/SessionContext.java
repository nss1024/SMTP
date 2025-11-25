/* The session context hold all session relevant information.
 *The program relies on the session context to move through states and
 * update necessary data.
 */

package commands;

import mda.EmailMetaData;
import utils.SessionState;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class SessionContext {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private List<SessionState> validNextStates;
    private SessionState currentState;
    private List<String> localDomains;
    private String clientDomain;
    private SMTPEmail smtpEmail;
    private boolean isReceivingData;
    private boolean isQuitting;
    private EmailMetaData emailMetaData;

    private SessionContext(){}

    public SessionContext(Socket socket,BufferedReader reader, BufferedWriter writer, List<String> domains) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.localDomains=domains;
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

    public BufferedReader getReader() {
        return reader;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public void upgradeConnection(Socket newSocket) throws IOException {
        this.socket = newSocket;
        this.reader = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(newSocket.getOutputStream()));
    }

    public EmailMetaData getEmailMetaData() {
        return emailMetaData;
    }

    public void setEmailMetaData(EmailMetaData emailMetaData) {
        this.emailMetaData = emailMetaData;
    }

    public List<String> getLocalDomains() {
        return localDomains;
    }

    public void setLocalDomains(List<String> localDomains) {
        this.localDomains = localDomains;
    }
}
