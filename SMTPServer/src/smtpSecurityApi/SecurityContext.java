package smtpSecurityApi;

import commands.SMTPEmail;
import mda.EmailMetaData;

public class SecurityContext {

    private SMTPEmail smtpEmail;
    private EmailMetaData emailMetaData;

    SecurityContext (){}

    SecurityContext(SMTPEmail smtpEmail, EmailMetaData emailMetaData){

        this.smtpEmail=smtpEmail;
        this.emailMetaData=emailMetaData;

    }

    public SMTPEmail getSmtpEmail() {
        return smtpEmail;
    }

    public void setSmtpEmail(SMTPEmail smtpEmail) {
        this.smtpEmail = smtpEmail;
    }

    public EmailMetaData getEmailMetaData() {
        return emailMetaData;
    }

    public void setEmailMetaData(EmailMetaData emailMetaData) {
        this.emailMetaData = emailMetaData;
    }
}
