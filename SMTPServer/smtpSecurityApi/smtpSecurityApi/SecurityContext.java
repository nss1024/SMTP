package smtpSecurityApi;


public class SecurityContext {

    private String senderIP;
    private String fromAddress;
    private String recipientDomain;
    private boolean isAuthenticated;
    private SpfContext spfContext;
    private DkimContext dkimContext;

    SecurityContext (){}

    public SecurityContext(String senderIP, String recipientDomain, boolean isAuthenticated) {
        this.senderIP = senderIP;
        this.recipientDomain = recipientDomain;
        this.isAuthenticated = isAuthenticated;
    }

    public String getSenderIP() {
        return senderIP;
    }

    public String getRecipientDomain() {
        return recipientDomain;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
