package smtpSecurityApi;

public interface SecurityPlugin {

    SecurityResult execute(SecurityContext sc);

}
