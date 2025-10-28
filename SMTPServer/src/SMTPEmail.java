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

    @Override
    public String toString() {
        String recipients = "";
        if (toList != null) {
            recipients = String.join(" ", toList);
        }

        return "SMTPEmail{" +
                "from='" + from + '\'' +
                ", toList=" + recipients +
                ", emailMessage='" + emailMessage + '\'' +
                '}';
    }
}
