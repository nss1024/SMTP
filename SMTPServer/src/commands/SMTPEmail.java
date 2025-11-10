package commands;

import java.util.ArrayList;
import java.util.List;

public class SMTPEmail {

    private String from;
    private List<String> toList;
    private String emailMessage;

    SMTPEmail(){}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getToList() {
        return toList;
    }

    public void addToRecipientList(String recipientAddress) {
        if(this.toList==null){
            toList=new ArrayList<>();
        }
        toList.add(recipientAddress);
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public String toEmlFormat() {
        String recipients = (toList != null && !toList.isEmpty())
                ? String.join(", ", toList)
                : "";

        StringBuilder sb = new StringBuilder();
        sb.append("From: ").append(from).append("\r\n");
        sb.append("To: ").append(recipients).append("\r\n");
        sb.append(emailMessage).append("\r\n");

        return sb.toString();
    }


    @Override
    public String toString() {
        String recipients = "";
        if (toList != null) {
            recipients = String.join(" ", toList);
        }

        return "commands.SMTPEmail{" +
                "from='" + from + '\'' +
                ", toList=" + recipients +
                ", emailMessage='" + emailMessage + '\'' +
                '}';
    }
}
